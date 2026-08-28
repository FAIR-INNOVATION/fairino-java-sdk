package fairino;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UDPClient {
    private DatagramSocket mSocket;
    private InetAddress mAddress;
    private int mPort;
    private boolean isRunning = false;
    private Thread mRecvThread;
    private UDPCallback mCallback;

    public interface UDPCallback {
        int Callback(int srcType, int count, int cmdID, int dataLen, String content);
    }

    public UDPClient(String ip, int port) {
        try {
            this.mAddress = InetAddress.getByName(ip);
            this.mPort = port;
            this.mSocket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean Connect() {
        try {
            if (mSocket == null || mSocket.isClosed()) {
                mSocket = new DatagramSocket();
            }
            
            this.isRunning = true;
            
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

    public void SetUDPCmdRpyCallback(UDPCallback callback) {
        this.mCallback = callback;
    }

    public int sendFrame(String sendFrame) {
        try {
            byte[] data = sendFrame.getBytes(StandardCharsets.UTF_8);
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
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    mSocket.receive(packet);
                    
                    String rawData = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                    
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
                        // 正常关闭时不打印堆栈
                        // e.printStackTrace();
                    }
                }
            }
        }
    }

    public void Close() {
        isRunning = false;
        if (mSocket != null) {
            mSocket.close();
        }
        if (mRecvThread != null) {
            mRecvThread.interrupt();
        }
    }
}
