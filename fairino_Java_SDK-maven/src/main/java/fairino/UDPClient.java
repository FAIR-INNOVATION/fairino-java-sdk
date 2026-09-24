package fairino;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UDPClient {
    /* 固定本地源端口：机器人端 DTLS socket 为连接态，重启后需同源端口才能被接受 */
    private static final int LOCAL_BIND_PORT = 20008;

    private DatagramSocket mSocket;
    private InetAddress mAddress;
    private int mPort;
    private boolean isRunning = false;
    private Thread mRecvThread;
    private UDPCallback mCallback;

    /** mTLS 链路（由 Robot 类在初始化时注入；null 或未启用 = 明文模式，与旧版本一致） */
    public MtlsLink mtls;

    /* DTLS 重握手控制 */
    private final Object dtlsHsLock = new Object();      /* 收发线程共用，防并发重握手 */
    private int udpIdleTimeout = 0;                      /* DTLS 连续无数据计数 */
    private long lastUdpHsOkAt = 0;                      /* 上次重握手成功时刻（双触发路径去重） */

    private static final ExecutorService hsExecutor = Executors.newCachedThreadPool();

    public interface UDPCallback {
        int Callback(int srcType, int count, int cmdID, int dataLen, String content);
    }

    public UDPClient(String ip, int port) {
        try {
            this.mAddress = InetAddress.getByName(ip);
            this.mPort = port;
            this.mSocket = createBoundSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 创建 socket 并固定本地源端口（端口占用则回退随机端口） */
    private static DatagramSocket createBoundSocket() throws SocketException {
        DatagramSocket socket = new DatagramSocket(null);
        try {
            socket.bind(new InetSocketAddress(LOCAL_BIND_PORT));
            System.out.println("[UDPClient] bound to local port " + LOCAL_BIND_PORT);
        } catch (SocketException e) {
            socket.close();
            socket = new DatagramSocket();
            System.out.println("[UDPClient] fixed local port busy, fallback to ephemeral (reconnect may fail)");
        }
        return socket;
    }

    /** 创建并绑定本地源端口 + connect 锁定对端的 UDP socket（握手超时重建用） */
    private DatagramSocket createUdpSocket() throws SocketException {
        DatagramSocket s = createBoundSocket();
        s.connect(mAddress, mPort);
        return s;
    }

    public boolean Connect() {
        try {
            if (mSocket == null || mSocket.isClosed()) {
                mSocket = createBoundSocket();
            }

            this.isRunning = true;

            /* mTLS 模式：socket 锁定对端地址后发起 DTLS 握手（带重试自愈——
             * 机器人端刚重启/旧会话未清理时，前几次握手可能失败） */
            if (mtls != null && mtls.enabled) {
                mSocket.connect(mAddress, mPort);
                int tryCount = 0;
                boolean handshakeOk = false;
                while (tryCount < 3 && !handshakeOk) {
                    tryCount++;
                    try {
                        /* 握手前清残留：上一轮失败握手（吊销拒绝等）留下的
                         * 旧 DTLS 包会污染新一轮握手 */
                        drainUdpSocket();
                        /* 握手总超时 5s：机器人端未开启加密时不会回 ServerHello，
                         * BC 内部会无限重传卡死——超时后抛异常走重试 */
                        startDtlsWithTimeout(5000);
                        System.out.println("[UDPClient-DTLS] ############DTLS handshake OK#############");
                        handshakeOk = true;
                    } catch (Exception ex) {
                        System.out.println("[UDPClient-DTLS] DTLS handshake attempt " + tryCount + "/3 failed: "
                                + ex.getClass().getSimpleName() + ": " + ex.getMessage());
                        /* 超时后旧 socket 仍被后台握手线程占用，重试前重建 */
                        try { mSocket.close(); } catch (Exception ignored) {}
                        if (tryCount < 3) {
                            try {
                                mSocket = createUdpSocket();
                            } catch (Exception ignored) {}
                            System.out.println("  retry in 2s ...");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ignored) {
                            }
                        }
                    }
                }
                if (!handshakeOk) {
                    /* 握手失败：保持加密模式不降级，直接报错。
                     * 后续发送失败/接收空闲会自动触发 rehandshakeDtls 重试自愈。
                     * 不阻塞 RPC 主流程（与 C# 一致：UDP 失败不影响 XML-RPC） */
                    System.out.println("[UDPClient-DTLS] ############DTLS handshake failed after retries##########");
                    System.out.println("[UDPClient-DTLS] 错误：DTLS 握手失败，保持加密模式不降级——"
                            + "请检查①机器人端加密开关是否开启 ②证书是否被吊销/过期 ③两端证书是否同一套");
                    /* 重建 socket（bind 20008 + connect 锁定对端），供后续重试握手使用 */
                    try { mSocket.close(); } catch (Exception ignored) {}
                    try {
                        mSocket = createUdpSocket();
                    } catch (Exception ignored) {}
                }
            }

            // 创建接收线程
            if (mRecvThread == null || !mRecvThread.isAlive()) {
                mRecvThread = new Thread(new RobotUDPCmdRecvThread());
                mRecvThread.start();
            }

            System.out.println("UDPClient 已连接到 " + mAddress.getHostAddress() + ":" + mPort + ", 本地端口: " + mSocket.getLocalPort());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** DTLS 握手（带总超时）：机器人端不回包时 BC 内部无限重传，必须外部限时 */
    private void startDtlsWithTimeout(int timeoutMs) throws IOException {
        final DatagramSocket sock = mSocket;
        Future<?> future = hsExecutor.submit(new Callable<Void>() {
            @Override
            public Void call() throws IOException {
                mtls.startDtls(sock);
                return null;
            }
        });
        try {
            future.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new IOException("DTLS handshake timeout (robot may not have mTLS enabled)");
        } catch (InterruptedException e) {
            future.cancel(true);
            throw new IOException("DTLS handshake interrupted", e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new IOException("DTLS handshake failed: " + cause, cause);
        }
    }

    /** 清空 socket 接收缓冲中的残留 DTLS 包。
     * 握手被拒（如吊销）时机器人发过的 ServerHello/证书/alert 可能残留在
     * 缓冲里，下一次握手的 epoch/记录序号从 0 开始与旧包重叠，BC 会把它
     * 们当新握手响应解析 → 状态错乱 → 机器人端 bad signature */
    private void drainUdpSocket() {
        try {
            byte[] tmp = new byte[2048];
            while (mSocket != null && !mSocket.isClosed() && mSocket.getReceiveBufferSize() > 0) {
                if (mSocket.getSoTimeout() != 1) {
                    mSocket.setSoTimeout(1);
                }
                try {
                    DatagramPacket p = new DatagramPacket(tmp, tmp.length);
                    mSocket.receive(p);
                } catch (java.net.SocketTimeoutException e) {
                    break;  /* 无更多残留 */
                }
            }
        } catch (Exception ignored) {
        }
    }

    /** DTLS 重新握手：socket 保持不动（connect 锁定对端不变），只重建会话。
     * 机器人端收到新 ClientHello 会经 0x16 探测关旧会话后重新握手。 */
    private boolean rehandshakeDtls() {
        synchronized (dtlsHsLock) {
            if (mtls == null || !mtls.enabled || mSocket == null || mSocket.isClosed()) {
                return false;
            }
            /* 3 秒内刚重握手成功过：另一触发路径（发送失败/30s无数据）的
             * 重复调用，直接跳过——避免重连期间连续两次重握手 */
            if (mtls.isDtlsActive() && (System.currentTimeMillis() - lastUdpHsOkAt) < 3000) {
                return true;
            }
            System.out.println("[UDPClient-DTLS] UDP re-handshaking (socket kept, new DTLS session) ...");
            try {
                mtls.dispose();           /* 关旧 transport（旧会话） */
                drainUdpSocket();         /* 清旧会话残留包，防污染新握手 */
                startDtlsWithTimeout(5000);
                lastUdpHsOkAt = System.currentTimeMillis();
                System.out.println("[UDPClient-DTLS] ############UDP re-handshake OK#############");
                return true;
            } catch (Exception ex) {
                System.out.println("[UDPClient-DTLS] UDP re-handshake failed: " + ex.getMessage() + ", retry later");
                return false;
            }
        }
    }

    public void SetUDPCmdRpyCallback(UDPCallback callback) {
        this.mCallback = callback;
    }

    public int sendFrame(String sendFrame) {
        try {
            byte[] data = sendFrame.getBytes(StandardCharsets.UTF_8);

            /* mTLS 模式：DTLS 通道发送（原帧直发，通道自动加密） */
            if (mtls != null && mtls.enabled) {
                try {
                    mtls.dtlsSend(data);
                    return data.length;
                } catch (Exception ex) {
                    /* 发送失败 = 会话已断：重握手后重发一次 */
                    if (rehandshakeDtls()) {
                        try {
                            mtls.dtlsSend(data);
                            return data.length;
                        } catch (Exception ex2) {
                            System.out.println("[UDPClient] UDP re-send failed: " + ex2.getMessage());
                        }
                    }
                    return -1;
                }
            }

            DatagramPacket packet = new DatagramPacket(data, data.length, mAddress, mPort);
            mSocket.send(packet);
            return data.length;
        } catch (IOException e) {
            System.err.println("sendFrame error: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    private class RobotUDPCmdRecvThread implements Runnable {
        @Override
        public void run() {
            byte[] buffer = new byte[4096];
            while (isRunning) {
                try {
                    int payloadLen;

                    if (mtls != null && mtls.enabled) {
                        /* DTLS 通道：Receive 出来的就是明文帧。
                         * 断线时 Receive 返回 -1（ICMP 不可达已被底层转换），空闲时超时返回<=0——
                         * 两者无法直接区分，累计 30 秒无数据才判定会话失效并重握手 */
                        payloadLen = mtls.dtlsReceive(buffer, 0, buffer.length, 2000);
                        if (payloadLen <= 0) {
                            udpIdleTimeout++;
                            if (udpIdleTimeout >= 15) {
                                udpIdleTimeout = 0;
                                rehandshakeDtls();
                            }
                            continue;
                        }
                        udpIdleTimeout = 0;
                    } else {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        mSocket.receive(packet);
                        payloadLen = packet.getLength();
                        if (payloadLen <= 0) {
                            continue;
                        }
                    }

                    String rawData = new String(buffer, 0, payloadLen, StandardCharsets.UTF_8);

                    // 解析指令帧数据，例如: /f/bIII20III303III6IIIMode(0)III/b/f
                    // 1. 去除包头包尾 /f/b 和 /b/f
                    if (rawData.startsWith("/f/b") && rawData.endsWith("/b/f")) {
                        String body = rawData.substring(4, rawData.length() - 4);
                        // 2. 根据 III 分隔符拆分字段
                        // 使用 split("III", -1) 确保保留空字符串，并处理开头可能存在的分隔符
                        String[] parts = body.split("III");

                        // 过滤掉拆分结果中的空字符串，因为 body 可能以 III 开头
                        List<String> filteredParts = new ArrayList<>();
                        for (String part : parts) {
                            if (!part.isEmpty()) {
                                filteredParts.add(part);
                            }
                        }

                        if (filteredParts.size() >= 4) {
                            try {
                                int count = Integer.parseInt(filteredParts.get(0));
                                int cmdID = Integer.parseInt(filteredParts.get(1));
                                int dataLen = Integer.parseInt(filteredParts.get(2));
                                String content = filteredParts.get(3);

                                // srcType 默认为 1
                                int srcType = 1;

                                if (mCallback != null) {
                                    try {
                                        mCallback.Callback(srcType, count, cmdID, dataLen, content);
                                    }catch (Exception e) {
                                        System.err.println("回调异常 ");
                                    }
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("UDP 数据帧字段格式错误: " + body);
                            }
                        }
                    }
                } catch (IOException e) {
                    if (isRunning) {
                        // 正常关闭或 DTLS 接收异常时不打印堆栈
                        // e.printStackTrace();
                    }
                }
            }
        }
    }

    public void Close() {
        isRunning = false;
        if (mtls != null) {
            mtls.dispose();
        }
        if (mSocket != null) {
            mSocket.close();
        }
        if (mRecvThread != null) {
            mRecvThread.interrupt();
        }
    }
}
