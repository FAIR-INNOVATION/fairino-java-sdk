package fairino;

//FR机器人TCP通信类

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

public class TCPClient
{
    private String ip;
    private int port;

    private Socket mSocket;
    private SocketAddress mSocketAddress;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private boolean isConnected = false;

    private boolean comFlag = true;

    private boolean reconnEnable = true;  //重连使能
    private int reconnTimes = 100;        //重连次数
    private int curReconnTimes = 0;       //当前重连次数
    private int reconnPeriod = 200;       //重连时间间隔
    private boolean reconnState = false;  //当前重连状态

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
    private boolean ReConnect()
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
            Close();
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
            this.mSocket.setKeepAlive(true);
            this.mSocketAddress = new InetSocketAddress(ip, port);
            this.mSocket.connect( mSocketAddress, reconnPeriod * 100);

            this.mOutputStream = mSocket.getOutputStream();
            this.mInputStream = mSocket.getInputStream();
            mSocket.setTcpNoDelay(true);
            mSocket.setSoTimeout(reconnPeriod*100);

            this.isConnected = true;
            return this.isConnected;
        }
        catch (Throwable e)
        {
            this.isConnected = false;
            return this.isConnected;
        }
    }

    public void Close()
    {
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
            byte[] bOutArray = str.getBytes();
            this.mOutputStream.write(bOutArray);
            return str.length();
        }
        catch (Throwable e)
        {
            System.out.println("send fail  " + e.getMessage());
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
            if (TCPClient.this.mInputStream == null) {
                return -1;
            }
            // 直接读取，依赖设置的读取超时
            return this.mInputStream.read(buffer);
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



