package fairino;

import java.util.ArrayList;
import java.util.List;

/**
 * FR CNDE 客户端类
 * 负责 CNDE 协议的连接、数据接收和状态管理
 */
public class FRCNDEClient {
    // CNDE 最大包大小
    private static final int CNDE_MAX_PKG_SIZE = 4096;

    // TCP 客户端
    private TCPClient rtClient;

    // 机器人状态包引用
    private ROBOT_STATE_PKG robotStatePkg;

    // 运行标志
    private boolean robotStateRunFlag = false;

    // 状态周期 (ms)
    private int robotStatePeriod = 8;

    // 发送计数
    private int sendCount = 0;

    // 互斥锁
    private final Object recvCNDEPkgMutex = new Object();

    // 接收线程
    private Thread recvThread;

    // 机器人 IP 和端口
    private String robotIp = "192.168.58.2";
    private int robotPort = 20005;

    // 状态配置列表
    private List<RobotState> stateConfigList = new ArrayList<>();

    // 状态数据存储 (使用ROBOT_STATE_PKG)
    private ROBOT_STATE_PKG statePkg;

    // 连接状态标志
    private volatile boolean isConnected = false;

    /**
     * 构造函数
     * @param pkg 机器人状态包引用
     */
    public FRCNDEClient(ROBOT_STATE_PKG pkg) {
        this.robotStatePkg = pkg;
        this.statePkg = pkg;
        this.rtClient = new TCPClient(robotIp, robotPort);
    }

    /**
     * 构造函数
     * @param ip 机器人 IP
     * @param port 机器人端口
     * @param pkg 机器人状态包引用
     */
    public FRCNDEClient(String ip, int port, ROBOT_STATE_PKG pkg) {
        this.robotIp = ip;
        this.robotPort = port;
        this.robotStatePkg = pkg;
        this.statePkg = pkg;
        this.rtClient = new TCPClient(ip, port);
    }

    /**
     * 构造函数（创建内部 ROBOT_STATE_PKG）
     * @param ip 机器人 IP
     * @param port 机器人端口
     */
    public FRCNDEClient(String ip, int port) {
        this.robotIp = ip;
        this.robotPort = port;
        this.statePkg = new ROBOT_STATE_PKG();
        this.robotStatePkg = this.statePkg;
        this.rtClient = new TCPClient(ip, port);
    }

    /**
     * 连接到机器人 CNDE 服务
     * @param ip 机器人 IP
     * @param port 机器人端口
     * @return 0-成功，其他-错误码
     */
    public int Connect(String ip, int port) {
        this.robotIp = ip;
        this.robotPort = port;
        this.rtClient = new TCPClient(ip, port);
        boolean rtn = rtClient.Connect();
        if (!rtn) {
            System.out.println("rtn:"+rtn);
            return -1;
        } else {
            // 如果已配置状态列表，连接成功后自动发送配置给服务器
            if (!stateConfigList.isEmpty()) {
                int ret = SendStateConfig();
                if (ret != 0) {
                    System.out.println("SendStateConfig rtn : " + ret);
                    return ret;
                }
            }

            // 发送 START 帧 (5A 5A 01 02 00 00 A5 A5)
            CNDEPkg startPkg = new CNDEPkg();
            startPkg.count = 1;
            startPkg.type = CNDEPkg.CNDE_FRAME_TYPE_START;  // 帧类型 02
            startPkg.len = 0;  // 数据长度为0
            List<Byte> startFrame = CNDEFrameHandle.CNDEPkgToFrame(startPkg);
            byte[] startData = CNDEFrameHandle.toByteArray(startFrame);
            rtClient.Send(startData);




            isConnected = true;
            robotStateRunFlag = true;
            recvThread = new Thread(new RecvRobotStateThread());
            recvThread.start();


        }
        return 0;
    }

    /**
     * 设置状态配置
     * @param configList 状态配置列表
     */
    public void SetStateConfig(List<RobotState> configList) {
        if (configList != null) {
            stateConfigList.clear();
            stateConfigList.addAll(configList);
        }
    }

    /**
     * 获取状态配置
     * @return 状态配置列表
     */
    public List<RobotState> GetStateConfig() {
        return new ArrayList<>(stateConfigList);
    }

    /**
     * 发送状态配置帧到服务器
     * @return 0-成功，-1-发送失败
     */
    public int SendStateConfig()
    {
        try {
            if (stateConfigList.isEmpty()) {
                System.err.println("State config is empty, please set config first");

                return -1;
            }

            synchronized (recvCNDEPkgMutex) {
                // 生成配置数据内容（逗号分隔的状态名）
                byte[] configData = RobotStateParser.generateConfigData(stateConfigList);
                System.out.println("[DEBUG] Config data string: " + new String(configData));
                // 组装最终数据：前2字节是period（小端序），后面是配置数据
                byte[] finalData = new byte[2 + configData.length];
                finalData[0] = (byte) (robotStatePeriod & 0xFF);         // period低字节
                finalData[1] = (byte) ((robotStatePeriod >> 8) & 0xFF);  // period高字节
                System.arraycopy(configData, 0, finalData, 2, configData.length);

                CNDEPkg configPkg = new CNDEPkg();
                configPkg.count = (sendCount++) & 0xFF;
                // configPkg.type = CNDEPkg.CNDE_FRAME_TYPE_OUTPUT_STATE;  // 使用 OUTPUT_STATE 类型发送配置
                configPkg.type = 1;
                configPkg.len = finalData.length;  // 长度字段也改为2字节（在CNDEPkgToFrame中处理）

                // 将数据添加到 pkg.data
                for (byte b : finalData) {
                    configPkg.data.add(b);
                }

                List<Byte> configFrame = CNDEFrameHandle.CNDEPkgToFrame(configPkg);
                byte[] sendData = CNDEFrameHandle.toByteArray(configFrame);

                rtClient.Send(sendData);

                // 接收服务器响应
                byte[] pkgBuf = new byte[CNDE_MAX_PKG_SIZE];
                CNDEPkg recvPkg = new CNDEPkg();

                int recvLen = RecvCNDEPkg(pkgBuf);
                if (recvLen < 0) {
                    System.err.println("[CNDE] Failed to receive config response");
                    return -1;
                } else if (recvLen > 0) {
                    List<Byte> frame = CNDEFrameHandle.toByteList(pkgBuf);
                    frame = frame.subList(0, recvLen);
                    int rtn = CNDEFrameHandle.FrameToCNDEPkg(frame, recvPkg);

                    if (rtn == 0) {
                        // 检查消息类型是否为消息帧 (CNDE_FRAME_TYPE_MESSAGE = 6)
                        if (recvPkg.type == CNDEPkg.CNDE_FRAME_TYPE_MESSAGE) {
                            // 将数据转换为字符串并检查是否包含 NOT_FOUND
                            if (!recvPkg.data.isEmpty()) {
                                byte[] dataBytes = CNDEFrameHandle.toByteArray(recvPkg.data);
                                String responseStr = new String(dataBytes, java.nio.charset.StandardCharsets.UTF_8);
                                System.out.println("[DEBUG] Server response: " + responseStr);

                                if (responseStr.contains("NOT_FOUND")) {
                                    System.err.println("[CNDE] Config error: State not found - " + responseStr);
                                    return -18;
                                }
                            }
                        }
                    }
                }

                return 0;
            }
        }
        catch (Throwable e)
        {
            System.err.println("[CNDE] Failed to receive config response");
        }
        return 0;
    }

    /**
     * 获取当前状态数据包
     * @return 状态数据结构体 (ROBOT_STATE_PKG)，如果连接断开则返回null
     */
    public ROBOT_STATE_PKG GetStatePkg() {
        if (!isConnected) {
            return null;
        }
        return statePkg;
    }

    /**
     * 获取连接状态
     * @return true-已连接，false-未连接
     */
    public boolean IsConnected() {
        return isConnected;
    }

    /**
     * 关闭连接
     * @return 0-成功
     */
    public int Close() {
        robotStateRunFlag = false;
        if (recvThread != null) {
            recvThread.interrupt();
        }
        rtClient.Close();
        return 0;
    }

    /**
     * 设置 CNDE 机器人状态周期
     * @param period 周期 (ms)，范围 8-1000
     * @return 0-成功，4-参数异常
     */
    public int SetCNDERobotStatePeriod(int period) {
        if (period < 8 || period > 1000) {
            return 4;
        }
        robotStatePeriod = period;
        return 0;
    }

    /**
     * 设置TCPClient重连使能
     * @param enable 是否使能，true:使能，false:不使能
     * @param times 重连次数
     * @param period 重连时间间隔
     * @return 错误码
     */
    public int SetReconnectParam(boolean enable, int times, int period) {
        if (rtClient != null) {
            return rtClient.SetReconnectParam(enable, times, period);
        }
        return 0;
    }

    /**
     * 启动 CNDE 状态上报
     * @return 0-成功，-1-发送失败，-2-错误响应
     */
    public int SetCNDEStart() {
        synchronized (recvCNDEPkgMutex) {
            CNDEPkg startPkg = new CNDEPkg();
            startPkg.count = (sendCount++) & 0xFF;
            startPkg.type = CNDEPkg.CNDE_FRAME_TYPE_START;
            startPkg.len = 0;

            List<Byte> startFrame = CNDEFrameHandle.CNDEPkgToFrame(startPkg);
            byte[] sendData = CNDEFrameHandle.toByteArray(startFrame);
            rtClient.Send(sendData);

            byte[] pkgBuf = new byte[CNDE_MAX_PKG_SIZE];
            CNDEPkg pkg = new CNDEPkg();

            while (robotStateRunFlag) {
                int recvLen = RecvCNDEPkg(pkgBuf);
                if (recvLen < 0) {
                    return -1;
                } else if (recvLen == 0) {
                    // 发生重连，需要再次发送开始指令帧
                    rtClient.Send(sendData);
                } else {
                    pkg.clear();
                    List<Byte> frame = CNDEFrameHandle.toByteList(pkgBuf);
                    frame = frame.subList(0, recvLen);
                    int rtn = CNDEFrameHandle.FrameToCNDEPkg(frame, pkg);

                    if (rtn == 0) {
                        switch (pkg.type) {
                            case CNDEPkg.CNDE_FRAME_TYPE_MESSAGE:
                                if (pkg.data.size() > 0 && pkg.data.get(0) == 0x00) {
                                    return 0;
                                } else {
                                    return -2;
                                }
                            default:
                                break;
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 停止 CNDE 状态上报
     * @return 0-成功，-1-发送失败，-2-错误响应
     */
    public int SetCNDEStop() {
        synchronized (recvCNDEPkgMutex) {
            CNDEPkg stopPkg = new CNDEPkg();
            stopPkg.count = (sendCount++) & 0xFF;
            stopPkg.type = CNDEPkg.CNDE_FRAME_TYPE_STOP;
            stopPkg.len = 0;

            List<Byte> stopFrame = CNDEFrameHandle.CNDEPkgToFrame(stopPkg);
            byte[] sendData = CNDEFrameHandle.toByteArray(stopFrame);
            rtClient.Send(sendData);

            byte[] pkgBuf = new byte[CNDE_MAX_PKG_SIZE];
            CNDEPkg pkg = new CNDEPkg();

            while (robotStateRunFlag) {
                int recvLen = RecvCNDEPkg(pkgBuf);
                if (recvLen < 0) {
                    return -1;
                } else if (recvLen == 0) {
                    // 发生重连，需要再次发送停止指令帧
                    rtClient.Send(sendData);
                } else {
                    pkg.clear();
                    List<Byte> frame = CNDEFrameHandle.toByteList(pkgBuf);
                    frame = frame.subList(0, recvLen);
                    int rtn = CNDEFrameHandle.FrameToCNDEPkg(frame, pkg);

                    if (rtn == 0) {
                        switch (pkg.type) {
                            case CNDEPkg.CNDE_FRAME_TYPE_MESSAGE:
                                if (pkg.data.size() > 0 && pkg.data.get(0) == 0x00) {
                                    return 0;
                                } else {
                                    return -2;
                                }
                            default:
                                break;
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 接收 CNDE 数据包
     * @param pkgBuf 接收缓冲区
     * @return 接收到的数据长度，<0-错误，0-重连
     */
    private int RecvCNDEPkg(byte[] pkgBuf) {
        // 先读取头部 6 字节 (head[2] + count[1] + type[1] + len[2])
        byte[] header = new byte[6];
        int headerRecvLen = 0;

        while (headerRecvLen < 6) {
            int rtn = rtClient.Recv(header);
            if (rtn < 0) {
                return -1;
            } else if (rtn == 0) {
                // 可能是重连或超时，返回 0 表示需要重发
                return 0;
            }
            headerRecvLen += rtn;
        }

        // 解析长度
        int dataLen = CNDEFrameHandle.ByteToInt16(new byte[]{header[4], header[5]});

        // 总长度 = 6(头部) + dataLen + 2(尾部)
        int totalLen = 6 + dataLen + 2;

        // 复制头部到缓冲区
        System.arraycopy(header, 0, pkgBuf, 0, 6);

        // 接收剩余数据
        int remainingLen = dataLen + 2;
        int totalRecvLen = 6;

        while (remainingLen > 0) {
            byte[] tmpBuf = new byte[remainingLen];
            int rtn = rtClient.Recv(tmpBuf);
            if (rtn < 0) {
                return -1;
            } else if (rtn == 0) {
                return 0;
            }
            System.arraycopy(tmpBuf, 0, pkgBuf, totalRecvLen, rtn);
            totalRecvLen += rtn;
            remainingLen -= rtn;
        }

        return totalLen;
    }

    /**
     * 接收机器人状态线程
     */
    private class RecvRobotStateThread implements Runnable {
        @Override
        public void run() {
            byte[] pkgBuf = new byte[CNDE_MAX_PKG_SIZE];
            CNDEPkg pkg = new CNDEPkg();

            while (robotStateRunFlag) {
                synchronized (recvCNDEPkgMutex)
                {
                    try {
                        int recvLen = RecvCNDEPkg(pkgBuf);
                        if (recvLen < 0) {
                            // 接收数据失败，检查是否仍在运行
                            if (!robotStateRunFlag) {
                                System.out.println("[CNDE] Reception stopped, not attempting reconnect");
                                return;  // 已停止运行，直接退出线程
                            }
                            // 标记断开并触发重连
                            isConnected = false;
                            System.err.println("[CNDE] Connection lost, attempting to reconnect...");
                            if (DoReconnect()) {
                                System.out.println("[CNDE] Reconnect successful, resuming state reception");
                                continue;  // 重连成功，继续接收循环
                            } else {
                                System.err.println("[CNDE] Reconnect failed, stopping reception thread");
                                return;  // 重连失败，结束线程
                            }
                        } else if (recvLen == 0) {
                            // 未收到数据，继续循环
                            continue;
                        } else {
                            pkg.clear();
                            List<Byte> frame = CNDEFrameHandle.toByteList(pkgBuf);
                            frame = frame.subList(0, recvLen);
                            int rtn = CNDEFrameHandle.FrameToCNDEPkg(frame, pkg);

                            if (rtn == 0) {
                                switch (pkg.type) {
                                    case CNDEPkg.CNDE_FRAME_TYPE_OUTPUT_STATE:
                                        // 解析状态输出数据
                                        if (!stateConfigList.isEmpty()) {
                                            byte[] dataBytes = CNDEFrameHandle.toByteArray(pkg.data);
                                            int parseRtn = RobotStateParser.parseData(dataBytes, stateConfigList, statePkg);
                                            if (parseRtn != 0) {
                                                System.err.println("Parse state data failed: " + parseRtn);
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    catch(Throwable e)
                    {
                        //System.err.println("recv cnde exception " + e.getMessage());
                        continue;
                    }
                }

//                try {
//                    //Thread.sleep(robotStatePeriod);
//                } catch (InterruptedException e) {
//                    break;
//                }
            }

            rtClient.Close();
        }
    }

    /**
     * 获取运行状态
     * @return true-运行中，false-已停止
     */
    public boolean isRunning() {
        return robotStateRunFlag;
    }

    /**
     * 获取发送计数
     * @return 发送计数
     */
    public int getSendCount() {
        return sendCount;
    }

    /**
     * 获取状态周期
     * @return 状态周期 (ms)
     */
    public int getRobotStatePeriod() {
        return robotStatePeriod;
    }

    /**
     * 执行重连操作
     * @return true-重连成功，false-重连失败
     */
    private boolean DoReconnect() {
        // 检查是否已停止运行，防止主动关闭后触发重连
        if (!robotStateRunFlag) {
            System.out.println("[CNDE] Reconnect aborted: client is stopped");
            return false;
        }

        // 使用TCPClient的重连参数进行重连
        int maxRetries = 100;  // 默认重试次数
        int retryInterval = 200;  // 默认重试间隔(ms)

        for (int i = 0; i < maxRetries; i++) {
            // 每次重试前检查运行标志
            if (!robotStateRunFlag) {
                System.out.println("[CNDE] Reconnect aborted during retry: client is stopped");
                return false;
            }

            rtClient.Close();
            boolean connected = rtClient.Connect();
            if (connected) {
                isConnected = true;
                System.out.println("[CNDE] Reconnect attempt " + (i + 1) + "/" + maxRetries + " successful");
                // 重连成功后，重新发送配置和开始帧
                if (!stateConfigList.isEmpty()) {
                    SendStateConfig();
                    // 发送 START 帧 (5A 5A 01 02 00 00 A5 A5)
                    CNDEPkg startPkg = new CNDEPkg();
                    startPkg.count = 1;
                    startPkg.type = CNDEPkg.CNDE_FRAME_TYPE_START;  // 帧类型 02
                    startPkg.len = 0;  // 数据长度为0
                    List<Byte> startFrame = CNDEFrameHandle.CNDEPkgToFrame(startPkg);
                    byte[] startData = CNDEFrameHandle.toByteArray(startFrame);
                    rtClient.Send(startData);
                }
                return true;
            } else {
                System.out.println("[CNDE] Reconnect attempt " + (i + 1) + "/" + maxRetries + " failed");
                try {
                    Thread.sleep(retryInterval);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    isConnected = false;
                    return false;
                }
            }
        }
        isConnected = false;
        return false;
    }
}
