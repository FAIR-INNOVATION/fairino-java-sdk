package fairino;

//FR机器人TCP通信类

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

import javax.net.ssl.SSLSocket;

public class TCPClient
{
    private String ip;
    private int port;

    private Socket mSocket;
    private SocketAddress mSocketAddress;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private boolean isConnected = false;

    /** mTLS 加密通道（握手成功后有效，null = 明文模式） */
    private SSLSocket mSslSocket;

    /** TCP 8080 应答帧监听器（解密后的原协议帧，mTLS/明文模式均触发） */
    public interface TcpFrameListener {
        void onTcpFrameReceived(String frame);
    }
    private TcpFrameListener tcpFrameListener;
    public void SetTcpFrameListener(TcpFrameListener listener) { this.tcpFrameListener = listener; }

    private boolean comFlag = true;

    private boolean reconnEnable = true;  //重连使能
    private int reconnTimes = 100;        //重连次数
    private int curReconnTimes = 0;       //当前重连次数
    private int reconnPeriod = 200;       //重连时间间隔(ms)
    private boolean reconnState = false;  //当前重连状态

    /** connect 超时(ms)：拔线时 connect 对不可达 IP 会阻塞到超时，
     *  固定小值避免重连尝试被拉长到 20s 一次 */
    private static final int CONNECT_TIMEOUT_MS = 3000;

    /** mTLS 链路（WrapTls 时注入；加密模式下会话未就绪时绝不走明文收发） */
    private MtlsLink mtls;

    /** 断线重连回调（Robot 层注入 ReconnectTls；发送失败/会话未就绪时触发） */
    public interface ReconnectHandler {
        boolean onReconnect();
    }
    private ReconnectHandler reconnectHandler;
    public void SetReconnectHandler(ReconnectHandler handler) { this.reconnectHandler = handler; }

    private FRLog log;


    public TCPClient(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }

    /**
     * @brief 设置TCPClient重连使能
     * @param enable 是否使能，true:使能，false:不使能
     * @param times 重连次数
     * @param period 重连时间间隔
     * @return 错误码
     */
    public int SetReconnectParam(boolean enable, int times, int period)
    {
        try {
            reconnEnable = enable;
            reconnTimes = times;
            reconnPeriod = period;
        }
        catch (Throwable e)
        {
            return 0;
        }
        return 0;
    }

    /**
     * @brief 获取TCPClient重连状态
     * @return 重连状态，true：正在重连，false：未重连
     */
    public boolean GetReconnState()
    {
        return reconnState;
    }

    /**
     * @brief TCPClient重连
     * @return 重连状态，true：重连成功，false：重连失败
     */
    public boolean ReConnect()
    {
        curReconnTimes = 0;
        reconnState = true;
        if(!reconnEnable)
        {
            reconnState = false;
            return false;
        }

        while(curReconnTimes < reconnTimes)
        {
            closeSocketOnly();
            if(Connect())
            {
                if(log != null)
                {
                    log.LogInfo("SDK Disconnected from robot, try to reconnect robot success! ");
                }
                reconnState = false;
                return true;
            }
            else
            {
                curReconnTimes++;
                if(log != null)
                {
                    log.LogInfo("SDK Disconnected from robot, try to reconnect robot failed! " +  curReconnTimes + " / " + reconnTimes);
                }
                try { Thread.sleep(reconnPeriod); } catch (InterruptedException ignored) {}
                continue;
            }
        }
        reconnState = false;
        return false;
    }

    public boolean Connect()
    {
        try
        {
            this.mSocket = new Socket();
            this.mSocketAddress = new InetSocketAddress(ip, port);
            this.mSocket.connect( mSocketAddress, CONNECT_TIMEOUT_MS);

            /* 读超时：与 C# SendTimeout 3s 不同，Java SO_TIMEOUT 只影响读。
             * 拔线判死依赖 keepalive（下方参数） */
            this.mSocket.setSoTimeout(CONNECT_TIMEOUT_MS);

            /* TCP keepalive：空闲 1s 开始探测、每 1s 一次（与 C# 同节奏）。
             * 拔线半开连接的 FIN 会丢失，双方靠 keepalive 发现断线 */
            this.mSocket.setKeepAlive(true);
            try
            {
                /* JDK 8u261+ 支持扩展 keepalive 参数（jdk.net 模块，反射调用避免
                 * 编译模块依赖）；不支持时仅基础 keepalive（系统默认周期长） */
                Class<?> extOpts = Class.forName("jdk.net.ExtendedSocketOptions");
                java.lang.reflect.Method setOption = Socket.class.getMethod("setOption",
                        java.net.SocketOption.class, Object.class);
                setOption.invoke(this.mSocket,
                        extOpts.getField("TCP_KEEPIDLE").get(null), 1);
                setOption.invoke(this.mSocket,
                        extOpts.getField("TCP_KEEPINTERVAL").get(null), 1);
                setOption.invoke(this.mSocket,
                        extOpts.getField("TCP_KEEPCOUNT").get(null), 5);
            }
            catch (Throwable ignored)
            {
            }

            this.mOutputStream = mSocket.getOutputStream();
            this.mInputStream = mSocket.getInputStream();
            mSocket.setTcpNoDelay(true);

            this.isConnected = true;
            return this.isConnected;
        }
        catch (Throwable e)
        {
            this.isConnected = false;
            return this.isConnected;
        }
    }

    /**
     * @brief 对已连接的 socket 做 mTLS 握手（TCP 8080 加密通道）。
     *        握手成功后 Send/Recv 自动走加密通道；失败抛异常，由调用方回退明文。
     * @param mtls mTLS 链路模块
     * @return 错误码，0-成功
     */
    public int WrapTls(MtlsLink mtls)
    {
        this.mtls = mtls;
        try
        {
            mSslSocket = mtls.wrapTcp(mSocket, ip);
            mSslSocket.setSoTimeout(reconnPeriod * 100);
            this.mOutputStream = mSslSocket.getOutputStream();
            this.mInputStream = mSslSocket.getInputStream();
            return 0;
        }
        catch (Throwable e)
        {
            System.out.println("TCP mTLS handshake failed: " + e.getMessage());
            mSslSocket = null;
            /* 握手失败：streams 仍指向明文 socket——清空，防止加密模式下误发明文 */
            this.mOutputStream = null;
            this.mInputStream = null;
            return -1;
        }
    }

    /** @brief 加密模式（mtls 启用）但会话未就绪：绝不走明文收发 */
    public boolean IsTlsPending()
    {
        return mtls != null && mtls.enabled && mSslSocket == null;
    }

    /** @brief 是否处于 mTLS 加密模式 */
    public boolean IsTlsActive()
    {
        return mSslSocket != null;
    }

    public void Close()
    {
        // 禁用重连，防止主动关闭后触发重连机制
        reconnEnable = false;
        closeSocketOnly();
        this.isConnected = false;
    }

    /** 仅关闭 socket，不改动重连使能（供内部重连用） */
    private void closeSocketOnly()
    {
        if (this.mSslSocket != null) {
            try
            {
                this.mSslSocket.close();
                this.mSslSocket = null;
            }
            catch (Throwable e)
            {

            }
        }
        if (this.mSocket != null) {
            try
            {
                this.mSocket.close();
                this.mSocket = null;
            }
            catch (Throwable e)
            {

            }
        }
        this.isConnected = false;
    }

    public boolean isConnected() {
        return this.isConnected;
    }


    public void Send(byte[] bOutArray)
    {
        try
        {
            if (IsTlsPending() || this.mOutputStream == null)
            {
                /* 加密模式下会话未就绪：绝不走明文 Send——明文帧会被机器人判
                 * wrong version number 拒掉并污染正在握手的新连接 */
                return;
            }
            this.mOutputStream.write(bOutArray);
        }
        catch (Throwable e)
        {

        }
    }

    public int Send(String str)
    {
        try
        {
            if (IsTlsPending() || this.mOutputStream == null)
            {
                /* 加密模式下会话未就绪：绝不走明文 Send（同上）。
                 * 累计等待由 Robot 层 ReconnectTls 的 8s 去重控制，这里直接触发重连 */
                if (IsTlsPending() && reconnectHandler != null)
                {
                    reconnectHandler.onReconnect();
                }
                return -1;
            }
            byte[] bOutArray = str.getBytes();
            this.mOutputStream.write(bOutArray);
            return str.length();
        }
        catch (Throwable e)
        {
            System.out.println("send fail  " + e.getMessage());
            /* 发送异常（断线）：加密模式下触发重连 + 重新握手 */
            if (mtls != null && mtls.enabled && reconnectHandler != null)
            {
                reconnectHandler.onReconnect();
            }
            return -1;
        }
    }

    public int GetPkg(byte[] buf, int recvSize)
    {
        int totalRecvSize = 0;
        int tmpRecvSize = 0;
        byte[] tmpBuf = new byte[2048];
        try
        {
            if (TCPClient.this.mInputStream == null)
            {
                System.out.println("mInputStream is null ");
                return -1;
            }

            while (recvSize > totalRecvSize)
            {
                Arrays.fill(tmpBuf, (byte) 0);
                tmpRecvSize = TCPClient.this.mInputStream.read(tmpBuf, 0, recvSize - totalRecvSize);
                //System.out.println("single recv length " + tmpRecvSize);
                if (tmpRecvSize <= 0)
                {
                    return -1;
                }

                System.arraycopy(tmpBuf, 0, buf, totalRecvSize, tmpRecvSize);
                totalRecvSize += tmpRecvSize;

                if (recvSize == totalRecvSize) {
                    if (buf[0] == 0x5A && buf[1] == 0x5A) {
                        short len = 0;
                        len = (short) (len | (short) buf[4]);
                        len = (short) (len << 8);
                        short tmp = 0;
                        if( buf[3] < 0)
                        {
                            tmp = (short)((short)buf[3] + 256);
                        }
                        else
                        {
                            tmp = (short)buf[3];
                        }
                        len = (short) (len | tmp);
                        if (len + 7 > recvSize)
                        {
                            recvSize = len + 7;
                            continue;
                        } else if (len + 7 == recvSize) {
                            int j;
                            short checksum = 0;
                            short checkdata = 0;

                            short tmpH ,tmpL = 0;
                            if(buf[recvSize - 1] < 0)
                            {
                                tmpH = (short)((short)buf[recvSize - 1] + 256);
                            }
                            else
                            {
                                tmpH = (short)buf[recvSize - 1];
                            }

                            if(buf[recvSize - 2] < 0)
                            {
                                tmpL = (short)((short)buf[recvSize - 2] + 256);
                            }
                            else
                            {
                                tmpL = (short)buf[recvSize - 2];
                            }

                            checkdata = (short) (checkdata | tmpH);
                            checkdata = (short) (checkdata << 8);
                            checkdata = (short) (checkdata | tmpL);


                            for (j = 0; j < recvSize - 2; j++)
                            {
                                short tmp1 = 0;
                                if(buf[j] < 0)
                                {
                                    tmp1 = (short)((short)buf[j] + 256);
                                }
                                else
                                {
                                    tmp1 = (short)buf[j];
                                }
                                checksum += tmp1;
                            }


//                            System.out.println("error check sum" + checkdata + "    " + checksum  + "   " + Integer.toBinaryString(checkdata) + "  " + Integer.toBinaryString(checksum));
                            if (checksum == checkdata)
                            {
                                //System.out.println("error check sum" + checkdata + "    " + checksum  + "   " + Integer.toBinaryString(checkdata) + "  " + Integer.toBinaryString(checksum));
                                return 0;
                            }
                            else
                            {
                                System.out.println("error check sum" + checkdata + "    " + checksum  + "   " + Integer.toBinaryString(checkdata) + "  " + Integer.toBinaryString(checksum));
                                return -2;//和校验失败
                            }
                        } else {
                            System.out.println("error SDK version");
                            return -3;  //SDK 比机器人版本新，得更新机器人版本
                        }
                    }
                }
            }
        }
        catch (Throwable e)
        {
            System.out.println("get pkg exception " + e.getMessage());
            if(ReConnect())
            {

                return 0;
            }
            else
            {
                return -1;
            }
        }
        return -1;
    }

    public int Recv11(byte[] buffer) {
        try
        {
            if (TCPClient.this.mInputStream == null)
            {
                return -1;
            }
            int available = TCPClient.this.mInputStream.available();

            if (available > 0)
            {
                return this.mInputStream.read(buffer);
            }
            else
            {
                return -1;
            }
        }
        catch (Throwable e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
    }
    public int Recv(byte[] buffer) {
        try {
            if (IsTlsPending() || TCPClient.this.mInputStream == null) {
                /* 加密模式但会话未就绪（初始握手失败/重连中）：
                 * 不做明文 recv——等待发送线程或异常路径触发重连 */
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                return -1;
            }
            // 直接读取，依赖设置的读取超时
            int len = this.mInputStream.read(buffer);
            if (len == 0 && (mtls == null || !mtls.enabled))
            {
                /* 明文模式下连接被机器人主动关闭：机器人端开启加密时
                 * 会拒绝明文连接（握手失败即关），表现为 recv 返回 0 */
                System.out.println("[TCPClient] 连接被机器人关闭——机器人端可能已开启加密，"
                        + "SDK 当前为明文模式。请检查两端加密开关是否一致");
                return -1;
            }
            if (len > 0 && tcpFrameListener != null)
            {
                try { tcpFrameListener.onTcpFrameReceived(new String(buffer, 0, len)); } catch (Throwable ignored) {}
            }
            return len;
        }
        catch (Throwable e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public void SetLog(FRLog logger)
    {
        log = logger;
    }

}



