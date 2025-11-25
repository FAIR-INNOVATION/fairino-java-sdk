package fairino;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.xml.bind.DatatypeConverter;

import javafx.scene.effect.BlendMode;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class Robot
{
    String SDK_VERSION = "JavaSDK V1.0.11  WebApp V3.9.0";
    private String robotIp = "192.168.58.2";//机器人ip

    int ROBOT_CMD_PORT = 8080;
    int MAX_UPLOAD_FILE_SIZE = 500 * 1024 * 1024;//最大上传文件为2Mb
    private int PauseMotionCnt = 0;
    private int ResumeMotionCnt = 0;
    private boolean isSendCmd = false;
    private String sendBuf = "";

    private boolean reconnEnable = true;  //重连使能
    private int reconnTimes = 100;          //重连次数
    private int reconnPeriod = 2000;       //重连时间间隔

    XmlRpcClientConfigImpl config;
    XmlRpcClient client;
    FRLog log;

    RobotStateRoutineThread robotStateRoutineThread;
    int sockErr = RobotError.ERR_SUCCESS;

    TCPClient clientCmd;

//
//            private void RobotTaskRoutineThread()
//            {
//                int s_isFirst = 0;
//                int s_check_cnt = 0;
//                UInt32 s_last_time = 0;
//
//                while (robot_task_exit == 0)
//                {
//                    if (sockErr == RobotError.ERR_SUCCESS & sock_cli_state != null)//如果当前没出错
//                    {
//                        //打时间戳
//                        UInt32 curtime = (UInt32)DateTime.UtcNow.TimeOfDay.TotalMilliseconds;
//
//                        if (s_isFirst == 0)
//                        {
//                            s_last_frame_cnt = robot_state_pkg.frame_cnt;
//                            s_last_time = curtime;
//                            s_isFirst = 1;
//                        }
//                        else
//                        {
//                            if (((robot_state_pkg.frame_cnt - s_last_frame_cnt) == 0) && ((curtime - s_last_time) < 10 * 30))//两次帧计数相同，正常帧计数是累加的
//                            {
//                                s_check_cnt++;
//                                if (s_check_cnt >= MAX_CHECK_CNT_COM)
//                                {
//                                    sockErr = (int)RobotError.ERR_SOCKET_COM_FAILED;
//                                    if (log != null)
//                                    {
//                                        log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "robot task loss pkg");
//                                    }
//                                    s_check_cnt = 0;
//                                }
//                            }
//                            else
//                            {
//                                s_check_cnt = 0;
//                            }
//
//                            s_last_frame_cnt = robot_state_pkg.frame_cnt;
//                            s_last_time = curtime;
//                        }
//                    }
//
//                    Thread.sleep(30);
//                }
//
//                s_isFirst = 0;
//
//                return;
//            }

    public int RPC(String ip)
    {
        try
        {
            robotIp = ip;
            config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://" + ip +":20003/RPC2"));
            config.setEnabledForExtensions(true);
            config.setConnectionTimeout(1000000);
            client = new XmlRpcClient();
            client.setConfig(config);

            sockErr = RobotError.ERR_SUCCESS;
            robotStateRoutineThread = new RobotStateRoutineThread(robotIp);//状态获取线程
            robotStateRoutineThread.start();

            Sleep(1000);

            if(robotStateRoutineThread != null)
            {
                //设置默认重连
                robotStateRoutineThread.SetReconnectParam(reconnEnable, reconnTimes, reconnPeriod);
                robotStateRoutineThread.clientRobotState.SetLog(log);
            }

            if (IsSockComError())
            {
                return sockErr;
            }

            clientCmd = new TCPClient(robotIp, ROBOT_CMD_PORT);//机械臂cmd指令端口
            boolean rtn = clientCmd.Connect();
            if(!rtn)
            {
                sockErr = RobotError.ERR_SOCKET_COM_FAILED;
                return sockErr;
            }

            return sockErr;
        }
        catch (Throwable e) {
            System.out.println("RPC exception   " + e.getMessage());
            return RobotError.ERR_SOCKET_COM_FAILED;
        }
    }

    /**
     * @brief  与机器人控制器关闭通讯
     * @return 错误码
     */
    public int CloseRPC()
    {
        robotStateRoutineThread.getRobotRealTimeFlag = false;
        robotStateRoutineThread.interrupt();
        sockErr = RobotError.ERR_SUCCESS;

        if(clientCmd != null)
        {
            clientCmd.Close();
        }

        if (log != null)
        {
            log.LogInfo("Close RPC");
        }

        if (log != null)
        {
            log.LogClose();
        }

        return 0;
    }


    /**
     * @brief  查询SDK版本号
     * @return  version SDK版本号
     */
    public String GetSDKVersion()
    {
        if (log != null)
        {
            log.LogInfo("GetSDKVersion(" + SDK_VERSION + ")");
        }
        return SDK_VERSION;
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

            if(robotStateRoutineThread != null)
            {
                robotStateRoutineThread.SetReconnectParam(enable, times, period);
            }

            if (log != null)
            {
                log.LogInfo("SetReconnectParam(" + enable + "," + times + "," + period + ") : " + 0);
            }
        }
        catch (Throwable e)
        {
            return 0;
        }
        return 0;
    }

    /**
     * @brief  获取控制器IP
     * @param  ip  控制器IP
     * @return  错误码
     */
    public int GetControllerIP(String[] ip)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetControllerIP", params) ;
            int rtn = (int)result[0];
            ip[0] = (String)result[1];

            if (log != null)
            {
                log.LogInfo("GetControllerIP(" + ip[0] + ") : " + rtn);
            }

            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 控制机器人手自动模式切换
     * @param mode 0-自动模式，1-手动模式
     * @return 错误码
     */
    public int Mode(int mode)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {mode};
            int rtn = (int) client.execute("Mode", params);
            if (log != null)
            {
                log.LogInfo("Mode(" + mode + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                Mode(mode);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception" + e.getMessage() + "  " + e.getClass().getName());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  控制机器人进入或退出拖动示教模式
     * @param  state 0-退出拖动示教模式，1-进入拖动示教模式
     * @return  错误码
     */
    public int DragTeachSwitch(int state)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {state};
            int rtn = (int)client.execute("DragTeachSwitch", params);
            if (log != null)
            {
                log.LogInfo("DragTeachSwitch(" + state + ") : " + rtn);
            }

            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                DragTeachSwitch(state);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  查询机器人是否处于拖动示教模式
     * @param  state 0-非拖动示教模式，1-拖动示教模式
     * @return  错误码
     */
//    public int IsInDragTeach(int state)
//    {
//        int errcode = 0;
//
//        if (sockErr == RobotError.ERR_SUCCESS)
//        {
//            if (robot_state_pkg.robot_state == 4)
//            {
//                state = 1;
//            }
//            else
//            {
//                state = 0;
//            }
//        }
//        else
//        {
//            errcode = sockErr;
//        }
//        if (log != null)
//        {
//            log.LogInfo("IsInDragTeach({state}) : {errcode}");
//        }
//        return errcode;
//    }

    /**
     * @brief  控制机器人上使能或下使能，机器人上电后默认自动上使能
     * @param  state  0-下使能，1-上使能
     * @return  错误码
     */
    public int RobotEnable(int state)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {state};
            int rtn = (int)client.execute("RobotEnable", params);
            if (log != null)
            {
                log.LogInfo("RobotEnable(" + state + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                RobotEnable(state);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief  jog点动
     * @param   refType 0-关节点动，2-基坐标系下点动，4-工具坐标系下点动，8-工件坐标系下点动
     * @param   nb 1-关节1(或x轴)，2-关节2(或y轴)，3-关节3(或z轴)，4-关节4(或绕x轴旋转)，5-关节5(或绕y轴旋转)，6-关节6(或绕z轴旋转)
     * @param   dir 0-负方向，1-正方向
     * @param   vel 速度百分比，[0~100]
     * @param   acc 加速度百分比， [0~100]
     * @param   max_dis 单次点动最大角度，单位[°]或距离，单位[mm]
     * @return  错误码
     */
    public int StartJOG(int refType, int nb, int dir, double vel, double acc, double max_dis)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {refType, nb, dir, vel, acc, max_dis};
            int rtn = (int)client.execute("StartJOG", params);
            if (log != null)
            {
                log.LogInfo("StartJOG(" + refType + ", " + nb + ", " + dir + ", " + vel + ", " + acc + ", " + max_dis + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  jog点动减速停止
     * @param   stopType  1-关节点动停止，3-基坐标系下点动停止，5-工具坐标系下点动停止，9-工件坐标系下点动停止
     * @return  错误码
     */
    public int StopJOG(int stopType)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {stopType};
            int rtn = (int)client.execute("StopJOG" , params);
            if (log != null)
            {
                log.LogInfo("StopJOG(" + stopType + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                StopJOG(stopType);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief jog点动立即停止
     * @return  错误码
     */
    public int ImmStopJOG()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ImmStopJOG" , params);
            if (log != null)
            {
                log.LogInfo("ImmStopJOG() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                ImmStopJOG();
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  关节空间运动
     * @param  joint_pos  目标关节位置,单位deg
     * @param  desc_pos  目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  epos  扩展轴位置，单位mm
     * @param  blendT [-1.0]-运动到位(阻塞)，[0~500.0]-平滑时间(非阻塞)，单位ms
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @return  错误码
     */
    public int MoveJ(JointPos joint_pos, DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, ExaxisPos epos, double blendT, int offset_flag, DescPose offset_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] joint = {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6};
            Object[] desc = { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };
            Object[] exteraxis = {epos.axis1, epos.axis2, epos.axis3, epos.axis4};
            Object[] offect = { offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry, offset_pos.rpy.rz };
            Object[] params = {joint, desc, tool, user, vel, acc, ovl, exteraxis, blendT, offset_flag, offect};

            int rtn = (int)client.execute("MoveJ" , params);
            if (log != null)
            {
                log.LogInfo("MoveJ(" + joint[0] + "," + joint[1] + "," + joint[2] + "," + joint[3] + "," + joint[4] + "," + joint[5] + "," + desc[0] + "," + desc[1] + "," + desc[2] + "," + desc[3] + "," + desc[4] + "," + desc[5] + "," + tool + "," + user + "," + vel + "," + acc + "," + ovl + "," +
                        epos.axis1 + "," + epos.axis2 + "," + epos.axis3 + "," + epos.axis4 + "," + blendT + "," + offset_flag + "," + offect[0] + "," + offect[1] + "," + offect[2] + "," + offect[3] + "," + offect[4] + "," + offect[5] + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                MoveJ(joint_pos, desc_pos, tool, user, vel, acc, ovl, epos, blendT, offset_flag, offset_pos);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage() + "  " + Thread.currentThread().getStackTrace()[1].getLineNumber());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  关节空间运动(重载函数 不需要输入笛卡尔位置)
     * @param  joint_pos  目标关节位置,单位deg
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  epos  扩展轴位置，单位mm
     * @param  blendT [-1.0]-运动到位(阻塞)，[0~500.0]-平滑时间(非阻塞)，单位ms
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @return  错误码
     */
    public int MoveJ(JointPos joint_pos, int tool, int user, double vel, double acc, double ovl, ExaxisPos epos, double blendT, int offset_flag, DescPose offset_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }

        DescPose desc_pos=new DescPose();
        int errcode = GetForwardKin(joint_pos,desc_pos);
        if (errcode != 0)
        {
            log.LogError("MoveJ GetForwardKin failed rtn is:"+errcode);
            return errcode;
        }

        errcode = MoveJ(joint_pos, desc_pos, tool, user, vel, acc, ovl, epos, blendT, offset_flag, offset_pos);
        return errcode;
    }

    /**
     * @brief  笛卡尔空间直线运动
     * @param  joint_pos  目标关节位置,单位deg
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  epos  扩展轴位置，单位mm
     * @param  search  0-不焊丝寻位，1-焊丝寻位
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  overSpeedStrategy  超速处理策略，1-标准；2-超速时报错停止；3-自适应降速，默认为0
     * @param  speedPercent  允许降速阈值百分比[0-100]，默认10%
     * @return  错误码
     */
    public int MoveL(JointPos joint_pos, DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, double blendR,ExaxisPos epos, int search, int offset_flag, DescPose offset_pos, int overSpeedStrategy, int speedPercent)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            int rtn = -1;
            if (overSpeedStrategy > 1)
            {
                Object[] paramProtectStart = new Object[] {overSpeedStrategy, speedPercent};
                rtn = (int)client.execute("JointOverSpeedProtectStart" , paramProtectStart);
                if (log != null)
                {
                    log.LogInfo("JointOverSpeedProtectStart(" + overSpeedStrategy + "," + speedPercent + ") : " + rtn);
                }
                if (rtn != 0)
                {
                    return rtn;
                }
            }

            Object[] joint = {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6};
            Object[] desc = { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };;
            Object[] exteraxis = {epos.axis1, epos.axis2, epos.axis3, epos.axis4};
            Object[] offect = { offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry, offset_pos.rpy.rz };
            Object[] params = new Object[] {joint, desc, tool, user, vel, acc, ovl, blendR, 0, exteraxis, search, offset_flag, offect};
            rtn = (int)client.execute("MoveL" , params);
            if (log != null)
            {
                log.LogInfo("MoveL(" + joint[0] + "," + joint[1] + "," + joint[2] + "," + joint[3] + "," + joint[4] + "," + joint[5] + "," + desc[0] + "," + desc[1] + "," + desc[2] + "," + desc[3] + "," + desc[4] + "," + desc[5] + "," + tool + "," + user + "," + vel + "," + acc + "," + ovl + "," + blendR +
                        epos.axis1 + "," + epos.axis2 + "," + epos.axis3 + "," + epos.axis4 + "," + search + "," + offset_flag + "," + offect[0] + "," + offect[1] + "," + offect[2] + "," + offect[3] + "," + offect[4] + "," + offect[5] + ") : " + rtn);
            }

            if (overSpeedStrategy > 1)
            {
                Object[] paramProtectStart = new Object[] {};
                rtn = (int)client.execute("JointOverSpeedProtectEnd" , params);
                if (log != null)
                {
                    log.LogInfo("JointOverSpeedProtectEnd() : " + rtn);
                }
                if (rtn != 0)
                {
                    return rtn;
                }
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(!IsSockComError())
            {
                return MoveL(joint_pos, desc_pos, tool, user, vel, acc, ovl, blendR, epos, search, offset_flag, offset_pos, overSpeedStrategy, speedPercent);
            }
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                MoveL(joint_pos, desc_pos, tool, user, vel, acc, ovl, blendR, epos, search, offset_flag, offset_pos, overSpeedStrategy, speedPercent);
            }
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return (int) RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  笛卡尔空间直线运动
     * @param  joint_pos  目标关节位置,单位deg
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  blendMode 过渡方式；0-内切过渡；1-角点过渡
     * @param  epos  扩展轴位置，单位mm
     * @param  search  0-不焊丝寻位，1-焊丝寻位
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  overSpeedStrategy  超速处理策略，1-标准；2-超速时报错停止；3-自适应降速，默认为0
     * @param  speedPercent  允许降速阈值百分比[0-100]，默认10%
     * @return  错误码
     */
    public int MoveL(JointPos joint_pos, DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, double blendR, int blendMode,ExaxisPos epos, int search, int offset_flag, DescPose offset_pos, int overSpeedStrategy, int speedPercent)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            int rtn = -1;
            if (overSpeedStrategy > 1)
            {
                Object[] paramProtectStart = new Object[] {overSpeedStrategy, speedPercent};
                rtn = (int)client.execute("JointOverSpeedProtectStart" , paramProtectStart);
                if (log != null)
                {
                    log.LogInfo("JointOverSpeedProtectStart(" + overSpeedStrategy + "," + speedPercent + ") : " + rtn);
                }
                if (rtn != 0)
                {
                    return rtn;
                }
            }

            Object[] joint = {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6};
            Object[] desc = { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };;
            Object[] exteraxis = {epos.axis1, epos.axis2, epos.axis3, epos.axis4};
            Object[] offect = { offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry, offset_pos.rpy.rz };
            Object[] params = new Object[] {joint, desc, tool, user, vel, acc, ovl, blendR,blendMode, exteraxis, search, offset_flag, offect};
            rtn = (int)client.execute("MoveL" , params);
            if (log != null)
            {
                log.LogInfo("MoveL(" + joint[0] + "," + joint[1] + "," + joint[2] + "," + joint[3] + "," + joint[4] + "," + joint[5] + "," + desc[0] + "," + desc[1] + "," + desc[2] + "," + desc[3] + "," + desc[4] + "," + desc[5] + "," + tool + "," + user + "," + vel + "," + acc + "," + ovl + "," + blendR +
                        epos.axis1 + "," + epos.axis2 + "," + epos.axis3 + "," + epos.axis4 + "," + search + "," + offset_flag + "," + offect[0] + "," + offect[1] + "," + offect[2] + "," + offect[3] + "," + offect[4] + "," + offect[5] + ") : " + rtn);
            }

            if (overSpeedStrategy > 1)
            {
                Object[] paramProtectStart = new Object[] {};
                rtn = (int)client.execute("JointOverSpeedProtectEnd" , params);
                if (log != null)
                {
                    log.LogInfo("JointOverSpeedProtectEnd() : " + rtn);
                }
                if (rtn != 0)
                {
                    return rtn;
                }
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(!IsSockComError())
            {
                return MoveL(joint_pos, desc_pos, tool, user, vel, acc, ovl, blendR,blendMode,epos, search, offset_flag, offset_pos, overSpeedStrategy, speedPercent);
            }
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                MoveL(joint_pos, desc_pos, tool, user, vel, acc, ovl, blendR, blendMode,epos, search, offset_flag, offset_pos, overSpeedStrategy, speedPercent);
            }
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return (int) RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  笛卡尔空间直线运动(重载函数2 不需要输入关节位置)
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  blendMode 过渡方式；0-内切过渡；1-角点过渡
     * @param  epos  扩展轴位置，单位mm
     * @param  search  0-不焊丝寻位，1-焊丝寻位
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  config 逆解关节空间配置，[-1]-参考当前关节位置解算，[0~7]-依据特定关节空间配置求解
     * @param  overSpeedStrategy  超速处理策略，1-标准；2-超速时报错停止；3-自适应降速，默认为0
     * @param  speedPercent  允许降速阈值百分比[0-100]，默认10%
     * @return  错误码
     */
    public int MoveL(DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, double blendR, int blendMode,ExaxisPos epos, int search, int offset_flag, DescPose offset_pos, int config, int overSpeedStrategy, int speedPercent) {
        if (IsSockComError()) {
            return sockErr;
        }

        if (GetSafetyCode() != 0) {
            return GetSafetyCode();
        }
        JointPos jPos=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int errcode = GetInverseKin(0, desc_pos, config, jPos);
        if (errcode != 0)
        {
            log.LogError("MoveL GetInverseKin failed rtn is:"+ errcode);
            return errcode;
        }

        errcode = MoveL(jPos, desc_pos, tool, user, vel, acc, ovl, blendR, blendMode, epos, search, offset_flag, offset_pos, overSpeedStrategy, speedPercent);
        return errcode;
    }

    /**
     * @brief  笛卡尔空间直线运动(重载函数1 增加blendMode)
     * @param  joint_pos  目标关节位置,单位deg
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[1~15]
     * @param  user  工件坐标号，范围[1~15]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  blendMode 过渡方式；0-内切过渡；1-角点过渡
     * @param  epos  扩展轴位置，单位mm
     * @param  search  0-不焊丝寻位，1-焊丝寻位
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  velAccParamMode 速度加速度参数模式；0-百分比；1-物理速度(mm/s)加速度(mm/s2)
     * @param  overSpeedStrategy  超速处理策略，1-标准；2-超速时报错停止；3-自适应降速，默认为0
     * @param  speedPercent  允许降速阈值百分比[0-100]，默认10%
     * @return  错误码
     */
    public int MoveL(JointPos joint_pos, DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, double blendR, int blendMode, ExaxisPos epos, int search, int offset_flag, DescPose offset_pos, int velAccParamMode, int overSpeedStrategy, int speedPercent)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try
        {
            int rtn = -1;
            if (overSpeedStrategy > 1)
            {
                Object[] paramProtectStart = new Object[] {overSpeedStrategy, speedPercent};
                rtn = (int)client.execute("JointOverSpeedProtectStart" , paramProtectStart);
                if (log != null)
                {
                    log.LogInfo("JointOverSpeedProtectStart(" + overSpeedStrategy + "," + speedPercent + ") : " + rtn);
                }
                if (rtn != 0)
                {
                    return rtn;
                }
            }

            Object[] params = new Object[] {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6,
                    desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz,
                    tool, user, vel, acc, ovl, blendR, blendMode,epos.axis1, epos.axis2, epos.axis3, epos.axis4,
                    search, offset_flag,offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry,
                    offset_pos.rpy.rz,100.0,velAccParamMode};

            Object[] params1 = new Object[]{params};

            rtn = (int)client.execute("MoveL" , params1);
            System.out.println(rtn);
            if (log != null)
            {
                log.LogInfo("MoveL(" + joint_pos.J1 + "," + joint_pos.J2 + "," + joint_pos.J3 + "," + joint_pos.J4 + "," + joint_pos.J5 + "," + joint_pos.J6 + "," + desc_pos.tran.x + "," + desc_pos.tran.y + "," + desc_pos.tran.z + "," + desc_pos.rpy.rx + "," + desc_pos.rpy.ry + "," + desc_pos.rpy.rz + "," + tool + "," + user + "," + vel + "," + acc + "," + ovl + "," + blendR +
                        epos.axis1 + "," + epos.axis2 + "," + epos.axis3 + "," + epos.axis4 + "," + search + "," + offset_flag + "," + offset_pos.tran.x + "," + offset_pos.tran.y + "," + offset_pos.tran.z + "," + offset_pos.rpy.rx + "," + offset_pos.rpy.ry + "," + offset_pos.rpy.rz + ") : " + rtn);
            }

            if (overSpeedStrategy > 1)
            {
                Object[] paramProtectStart = new Object[] {};
                rtn = (int)client.execute("JointOverSpeedProtectEnd" , paramProtectStart);
                if (log != null)
                {
                    log.LogInfo("JointOverSpeedProtectEnd() : " + rtn);
                }
                if (rtn != 0)
                {
                    return rtn;
                }
            }

            if((GetRobotRealTimeState().main_code != 0 || GetRobotRealTimeState().sub_code != 0) && rtn == 0){
                rtn=14;
            }

            return rtn;
        }
        catch (Throwable e)
        {
            if(!IsSockComError())
            {
                return MoveL(joint_pos, desc_pos, tool, user, vel, acc, ovl, blendR, blendMode, epos, search, offset_flag, offset_pos,velAccParamMode, overSpeedStrategy, speedPercent);
            }
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                MoveL(joint_pos, desc_pos, tool, user, vel, acc, ovl, blendR, blendMode, epos, search, offset_flag, offset_pos, velAccParamMode,overSpeedStrategy, speedPercent);
            }
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return (int) RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  笛卡尔空间直线运动(重载函数2 不需要输入关节位置)
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[1~15]
     * @param  user  工件坐标号，范围[1~15]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  blendMode 过渡方式；0-内切过渡；1-角点过渡
     * @param  epos  扩展轴位置，单位mm
     * @param  search  0-不焊丝寻位，1-焊丝寻位
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  config 逆解关节空间配置，[-1]-参考当前关节位置解算，[0~7]-依据特定关节空间配置求解
     * @param  velAccParamMode 速度加速度参数模式；0-百分比；1-物理速度(mm/s)加速度(mm/s2)
     * @param  overSpeedStrategy  超速处理策略，1-标准；2-超速时报错停止；3-自适应降速，默认为0
     * @param  speedPercent  允许降速阈值百分比[0-100]，默认10%
     * @return  错误码
     */
    public int MoveL(DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, double blendR, int blendMode, ExaxisPos epos, int search, int offset_flag, DescPose offset_pos, int config, int velAccParamMode, int overSpeedStrategy, int speedPercent)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        JointPos jPos=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int errcode = GetInverseKin(0, desc_pos, config, jPos);
        if (errcode != 0)
        {
            log.LogInfo("MoveL GetInverseKin failed rtn is "+ errcode);
            return errcode;
        }

        errcode = MoveL(jPos, desc_pos, tool, user, vel, acc, ovl, blendR, blendMode, epos, search, offset_flag, offset_pos, velAccParamMode, overSpeedStrategy, speedPercent);
        return errcode;
    }

    /**
     * @brief  笛卡尔空间直线运动
     * @param  joint_pos  目标关节位置,单位deg
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[1~15]
     * @param  user  工件坐标号，范围[1~15]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  epos  扩展轴位置，单位mm
     * @param  search  0-不焊丝寻位，1-焊丝寻位
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  velAccParamMode 速度加速度参数模式；0-百分比；1-物理速度(mm/s)加速度(mm/s2)
     * @param  overSpeedStrategy  超速处理策略，1-标准；2-超速时报错停止；3-自适应降速，默认为0
     * @param  speedPercent  允许降速阈值百分比[0-100]，默认10%
     * @return  错误码
     */
    public int MoveL(JointPos joint_pos, DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, double blendR, ExaxisPos epos, int search, int offset_flag, DescPose offset_pos, int velAccParamMode, int overSpeedStrategy, int speedPercent)
    {
        int errcode = MoveL(joint_pos, desc_pos, tool, user, vel, acc, ovl, blendR, 0/*blendMode*/, epos, search, offset_flag, offset_pos, 0/*velAccParamMode*/, overSpeedStrategy, speedPercent);

        return errcode;
    }

    /**
     * @brief  笛卡尔空间圆弧运动
     * @param  joint_pos_p  路径点关节位置,单位deg
     * @param  desc_pos_p   路径点笛卡尔位姿
     * @param  ptool  工具坐标号，范围[0~14]
     * @param  puser  工件坐标号，范围[0~14]
     * @param  pvel  速度百分比，范围[0~100]
     * @param  pacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_p  扩展轴位置，单位mm
     * @param  poffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_p  位姿偏移量
     * @param  joint_pos_t  目标点关节位置,单位deg
     * @param  desc_pos_t   目标点笛卡尔位姿
     * @param  ttool  工具坐标号，范围[0~14]
     * @param  tuser  工件坐标号，范围[0~14]
     * @param  tvel  速度百分比，范围[0~100]
     * @param  tacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_t  扩展轴位置，单位mm
     * @param  toffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_t  位姿偏移量
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @return  错误码
     */
    public int MoveC(JointPos joint_pos_p, DescPose desc_pos_p, int ptool, int puser, double pvel, double pacc, ExaxisPos epos_p, int poffset_flag, DescPose offset_pos_p, JointPos joint_pos_t, DescPose desc_pos_t, int ttool, int tuser, double tvel, double tacc, ExaxisPos epos_t, int toffset_flag, DescPose offset_pos_t, double ovl, double blendR)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] jointP = {joint_pos_p.J1, joint_pos_p.J2, joint_pos_p.J3, joint_pos_p.J4, joint_pos_p.J5, joint_pos_p.J6};
            Object[] descP = { desc_pos_p.tran.x, desc_pos_p.tran.y, desc_pos_p.tran.z, desc_pos_p.rpy.rx, desc_pos_p.rpy.ry, desc_pos_p.rpy.rz };
            Object[] exteraxisP = {epos_p.axis1, epos_p.axis2, epos_p.axis3, epos_p.axis4};
            Object[] offectP = { offset_pos_p.tran.x, offset_pos_p.tran.y, offset_pos_p.tran.z, offset_pos_p.rpy.rx, offset_pos_p.rpy.ry, offset_pos_p.rpy.rz };
            Object[] controlP = { (double)ptool, (double)puser, pvel, pacc };

            Object[] jointT = {joint_pos_t.J1, joint_pos_t.J2, joint_pos_t.J3, joint_pos_t.J4, joint_pos_t.J5, joint_pos_t.J6};
            Object[] descT = { desc_pos_t.tran.x, desc_pos_t.tran.y, desc_pos_t.tran.z, desc_pos_t.rpy.rx, desc_pos_t.rpy.ry, desc_pos_t.rpy.rz };
            Object[] exteraxisT = {epos_t.axis1, epos_t.axis2, epos_t.axis3, epos_t.axis4};
            Object[] offectT = { offset_pos_t.tran.x, offset_pos_t.tran.y, offset_pos_t.tran.z, offset_pos_t.rpy.rx, offset_pos_t.rpy.ry, offset_pos_t.rpy.rz };
            Object[] controlT = { (double)ttool, (double)tuser, tvel, tacc };
            Object[] params = new Object[] {jointP, descP, controlP, exteraxisP, poffset_flag, offectP, jointT, descT, controlT, exteraxisT, toffset_flag, offectT, ovl, blendR};
            int rtn = (int)client.execute("MoveC" , params);
            if (log != null)
            {
                log.LogInfo("MoveC(" + jointP[0] + "," + jointP[1] + "," + jointP[2] + "," + jointP[3] + "," + jointP[4] + "," + jointP[5] + "," + descP[0] + "," + descP[1] + "," + descP[2] + "," + descP[3] + "," + descP[4] + "," + descP[5] + "," + ptool + "," + puser + "," + pvel + "," + pacc + "," +
                        epos_p.axis1 + "," + epos_p.axis2 + "," + epos_p.axis3 + "," + epos_p.axis4 + "," + poffset_flag + "," + offectP[0] + "," + offectP[1] + "," + offectP[2] + "," + offectP[3] + "," + offectP[4] + "," + offectP[5] + ",) " +
                        jointT[0] + "," + jointT[1] + "," + jointT[2] + "," + jointT[3] + "," + jointT[4] + "," + jointT[5] + "," + descT[0] + "," + descT[1] + "," + descT[2] + "," + descT[3] + "," + descT[4] + "," + descT[5] + "," + ttool + "," + tuser + "," + tvel + "," + tacc + "," +
                        epos_t.axis1 + "," + epos_t.axis2 + "," + epos_t.axis3 + "," + epos_t.axis4 + "," + toffset_flag + "," + offectT[0] + "," + offectT[1] + "," + offectT[2] + "," + offectT[3] + "," + offectT[4] + "," + offectT[5] + "," + ovl + "," + blendR + " ): " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                MoveC(joint_pos_p, desc_pos_p, ptool, puser, pvel, pacc, epos_p, poffset_flag, offset_pos_p, joint_pos_t, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, toffset_flag, offset_pos_t, ovl, blendR);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  笛卡尔空间圆弧运动(重载函数1 不需要输入关节位置)
     * @param  desc_pos_p   路径点笛卡尔位姿
     * @param  ptool  工具坐标号，范围[0~14]
     * @param  puser  工件坐标号，范围[0~14]
     * @param  pvel  速度百分比，范围[0~100]
     * @param  pacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_p  扩展轴位置，单位mm
     * @param  poffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_p  位姿偏移量
     * @param  desc_pos_t   目标点笛卡尔位姿
     * @param  ttool  工具坐标号，范围[0~14]
     * @param  tuser  工件坐标号，范围[0~14]
     * @param  tvel  速度百分比，范围[0~100]
     * @param  tacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_t  扩展轴位置，单位mm
     * @param  toffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_t  位姿偏移量
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  config 逆解关节空间配置，[-1]-参考当前关节位置解算，[0~7]-依据特定关节空间配置求解
     * @return  错误码
     */
    public int MoveC(DescPose desc_pos_p, int ptool, int puser, double pvel, double pacc, ExaxisPos epos_p, int poffset_flag, DescPose offset_pos_p, DescPose desc_pos_t, int ttool, int tuser, double tvel, double tacc, ExaxisPos epos_t, int toffset_flag, DescPose offset_pos_t, double ovl, double blendR, int config) {
        if (IsSockComError()) {
            return sockErr;
        }
        if (GetSafetyCode() != 0) {
            return GetSafetyCode();
        }
        JointPos jPosP=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        JointPos jPosT=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int errcode = GetInverseKin(0, desc_pos_p, config, jPosP);
        if (errcode != 0)
        {
            log.LogError("MoveC  GetInverseKin P failed rtn is: "+ errcode);
            return errcode;
        }

        errcode = GetInverseKin(0, desc_pos_t, config, jPosT);
        if (errcode != 0)
        {
            log.LogError("MoveC  GetInverseKin T failed rtn is:"+ errcode);
            return errcode;
        }

        errcode = MoveC(jPosP, desc_pos_p, ptool, puser, pvel, pacc, epos_p, poffset_flag, offset_pos_p, jPosT, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, toffset_flag, offset_pos_t, ovl, blendR);

        return errcode;
    }

    /**
     * @brief  笛卡尔空间圆弧运动
     * @param  joint_pos_p  路径点关节位置,单位deg
     * @param  desc_pos_p   路径点笛卡尔位姿
     * @param  ptool  工具坐标号，范围[1~15]
     * @param  puser  工件坐标号，范围[1~15]
     * @param  pvel  速度百分比，范围[0~100]
     * @param  pacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_p  扩展轴位置，单位mm
     * @param  poffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_p  位姿偏移量
     * @param  joint_pos_t  目标点关节位置,单位deg
     * @param  desc_pos_t   目标点笛卡尔位姿
     * @param  ttool  工具坐标号，范围[1~15]
     * @param  tuser  工件坐标号，范围[1~15]
     * @param  tvel  速度百分比，范围[0~100]
     * @param  tacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_t  扩展轴位置，单位mm
     * @param  toffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_t  位姿偏移量
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  velAccParamMode 速度加速度参数模式；0-百分比；1-物理速度(mm/s)加速度(mm/s2)
     * @return  错误码
     */
    public int MoveC(JointPos joint_pos_p, DescPose desc_pos_p, int ptool, int puser, double pvel, double pacc, ExaxisPos epos_p, int poffset_flag, DescPose offset_pos_p, JointPos joint_pos_t, DescPose desc_pos_t, int ttool, int tuser, double tvel, double tacc, ExaxisPos epos_t, int toffset_flag, DescPose offset_pos_t, double ovl, double blendR, int velAccParamMode)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {joint_pos_p.J1, joint_pos_p.J2, joint_pos_p.J3, joint_pos_p.J4, joint_pos_p.J5, joint_pos_p.J6,
                    desc_pos_p.tran.x, desc_pos_p.tran.y, desc_pos_p.tran.z, desc_pos_p.rpy.rx, desc_pos_p.rpy.ry, desc_pos_p.rpy.rz,
                    ptool, puser, pvel, pacc,epos_p.axis1, epos_p.axis2, epos_p.axis3, epos_p.axis4,poffset_flag,
                    offset_pos_p.tran.x, offset_pos_p.tran.y, offset_pos_p.tran.z, offset_pos_p.rpy.rx, offset_pos_p.rpy.ry, offset_pos_p.rpy.rz,
                    joint_pos_t.J1, joint_pos_t.J2, joint_pos_t.J3, joint_pos_t.J4, joint_pos_t.J5, joint_pos_t.J6,
                    desc_pos_t.tran.x, desc_pos_t.tran.y, desc_pos_t.tran.z, desc_pos_t.rpy.rx, desc_pos_t.rpy.ry, desc_pos_t.rpy.rz,
                    ttool,tuser,tvel,tacc,epos_t.axis1, epos_t.axis2, epos_t.axis3, epos_t.axis4,toffset_flag,
                    offset_pos_t.tran.x, offset_pos_t.tran.y, offset_pos_t.tran.z, offset_pos_t.rpy.rx, offset_pos_t.rpy.ry, offset_pos_t.rpy.rz,
                    ovl, blendR,(double)100.0,velAccParamMode};
            Object[] params1 = new Object[]{params};
            int rtn = (int)client.execute("MoveC" , params1);
            if (log != null)
            {
                log.LogInfo("MoveC(" + joint_pos_p.J1 + "," + joint_pos_p.J2 + "," + joint_pos_p.J3 + "," + joint_pos_p.J4 + "," + joint_pos_p.J5 + "," + joint_pos_p.J6 + "," + desc_pos_p.tran.x + "," + desc_pos_p.tran.y + "," + desc_pos_p.tran.z + "," + desc_pos_p.rpy.rx + "," + desc_pos_p.rpy.ry + "," + desc_pos_p.rpy.rz + "," + ptool + "," + puser + "," + pvel + "," + pacc + "," +
                        epos_p.axis1 + "," + epos_p.axis2 + "," + epos_p.axis3 + "," + epos_p.axis4 + "," + poffset_flag + "," + offset_pos_p.tran.x + "," + offset_pos_p.tran.y + "," + offset_pos_p.tran.z + "," + offset_pos_p.rpy.rx + "," + offset_pos_p.rpy.ry + "," + offset_pos_p.rpy.rz + ",) " +
                        joint_pos_t.J1 + "," + joint_pos_t.J2 + "," + joint_pos_t.J3 + "," + joint_pos_t.J4 + "," + joint_pos_t.J5 + "," + joint_pos_t.J6 + "," + desc_pos_t.tran.x + "," + desc_pos_t.tran.y + "," + desc_pos_t.tran.z + "," + desc_pos_t.rpy.rx + "," + desc_pos_t.rpy.ry + "," + desc_pos_t.rpy.rz + "," + ttool + "," + tuser + "," + tvel + "," + tacc + "," +
                        epos_t.axis1 + "," + epos_t.axis2 + "," + epos_t.axis3 + "," + epos_t.axis4 + "," + toffset_flag + "," + offset_pos_t.tran.x + "," + offset_pos_t.tran.y + "," + offset_pos_t.tran.z + "," + offset_pos_t.rpy.rx + "," + offset_pos_t.rpy.ry + "," + offset_pos_t.rpy.rz + "," + ovl + "," + blendR + ", "+ velAccParamMode +" ): " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                MoveC(joint_pos_p, desc_pos_p, ptool, puser, pvel, pacc, epos_p, poffset_flag, offset_pos_p, joint_pos_t, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, toffset_flag, offset_pos_t, ovl, blendR,velAccParamMode);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  笛卡尔空间圆弧运动 (重载函数1 不需要输入关节位置)
     * @param  desc_pos_p   路径点笛卡尔位姿
     * @param  ptool  工具坐标号，范围[1~15]
     * @param  puser  工件坐标号，范围[1~15]
     * @param  pvel  速度百分比，范围[0~100]
     * @param  pacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_p  扩展轴位置，单位mm
     * @param  poffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_p  位姿偏移量
     * @param  desc_pos_t   目标点笛卡尔位姿
     * @param  ttool  工具坐标号，范围[1~15]
     * @param  tuser  工件坐标号，范围[1~15]
     * @param  tvel  速度百分比，范围[0~100]
     * @param  tacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_t  扩展轴位置，单位mm
     * @param  toffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_t  位姿偏移量
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  config 逆解关节空间配置，[-1]-参考当前关节位置解算，[0~7]-依据特定关节空间配置求解
     * @param  velAccParamMode 速度加速度参数模式；0-百分比；1-物理速度(mm/s)加速度(mm/s2)
     * @return  错误码
     */
    public int MoveC(DescPose desc_pos_p, int ptool, int puser, double pvel, double pacc, ExaxisPos epos_p, int poffset_flag, DescPose offset_pos_p, DescPose desc_pos_t, int ttool, int tuser, double tvel, double tacc, ExaxisPos epos_t, int toffset_flag, DescPose offset_pos_t, double ovl, double blendR, int config, int velAccParamMode)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }

        JointPos jPosP=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        JointPos jPosT=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int errcode = GetInverseKin(0, desc_pos_p, config, jPosP);
        if (errcode != 0)
        {
            log.LogInfo("MoveC  GetInverseKin P failed rtn is "+ errcode);
            return errcode;
        }

        errcode = GetInverseKin(0, desc_pos_t, config, jPosT);
        if (errcode != 0)
        {
            log.LogInfo("MoveC  GetInverseKin T failed rtn is "+ errcode);
            return errcode;
        }

        errcode = MoveC(jPosP, desc_pos_p, ptool, puser, pvel, pacc, epos_p, poffset_flag, offset_pos_p, jPosT, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, toffset_flag, offset_pos_t, ovl, blendR, velAccParamMode);

        return errcode;
    }

    /**
     * @brief  笛卡尔空间整圆运动
     * @param  joint_pos_p  路径点1关节位置,单位deg
     * @param  desc_pos_p   路径点1笛卡尔位姿
     * @param  ptool  工具坐标号，范围[0~14]
     * @param  puser  工件坐标号，范围[0~14]
     * @param  pvel  速度百分比，范围[0~100]
     * @param  pacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_p  扩展轴位置，单位mm
     * @param  joint_pos_t  路径点2关节位置,单位deg
     * @param  desc_pos_t   路径点2笛卡尔位姿
     * @param  ttool  工具坐标号，范围[0~14]
     * @param  tuser  工件坐标号，范围[0~14]
     * @param  tvel  速度百分比，范围[0~100]
     * @param  tacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_t  扩展轴位置，单位mm
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @return  错误码
     */
    public int Circle(JointPos joint_pos_p, DescPose desc_pos_p, int ptool, int puser, double pvel, double pacc, ExaxisPos epos_p, JointPos joint_pos_t, DescPose desc_pos_t, int ttool, int tuser, double tvel, double tacc, ExaxisPos epos_t, double ovl, int offset_flag, DescPose offset_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {

            Object[] jointP = {joint_pos_p.J1, joint_pos_p.J2, joint_pos_p.J3, joint_pos_p.J4, joint_pos_p.J5, joint_pos_p.J6};
            Object[] descP = { desc_pos_p.tran.x, desc_pos_p.tran.y, desc_pos_p.tran.z, desc_pos_p.rpy.rx, desc_pos_p.rpy.ry, desc_pos_p.rpy.rz };
            Object[] exteraxisP = {epos_p.axis1, epos_p.axis2, epos_p.axis3, epos_p.axis4};
            Object[] offect = { offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry, offset_pos.rpy.rz };
            Object[] controlP = { ptool * 1.0, puser * 1.0, pvel, pacc };
            Object[] jointT = {joint_pos_t.J1, joint_pos_t.J2, joint_pos_t.J3, joint_pos_t.J4, joint_pos_t.J5, joint_pos_t.J6};
            Object[] descT = { desc_pos_t.tran.x, desc_pos_t.tran.y, desc_pos_t.tran.z, desc_pos_t.rpy.rx, desc_pos_t.rpy.ry, desc_pos_t.rpy.rz };
            Object[] exteraxisT = {epos_t.axis1, epos_t.axis2, epos_t.axis3, epos_t.axis4};
            Object[] controlT = { ttool * 1.0, tuser * 1.0, tvel, tacc };
            Object[] params = new Object[] {jointP, descP, controlP, exteraxisP, jointT, descT, controlT, exteraxisT, ovl, offset_flag, offect};
            int rtn = (int)client.execute("Circle" , params);
            if (log != null)
            {
                log.LogInfo("Circle(" + jointP[0] + "," + jointP[1] + "," + jointP[2] + "," + jointP[3] + "," + jointP[4] + "," + jointP[5] + "," + descP[0] + "," + descP[1] + "," + descP[2] + "," + descP[3] + "," + descP[4] + "," + descP[5] + "," + ptool + "," + puser + "," + pvel + "," + pacc + "," +
                        epos_p.axis1 + "," + epos_p.axis2 + "," + epos_p.axis3 + "," + epos_p.axis4 + ",) " +
                        jointT[0] + "," + jointT[1] + "," + jointT[2] + "," + jointT[3] + "," + jointT[4] + "," + jointT[5] + "," + descT[0] + "," + descT[1] + "," + descT[2] + "," + descT[3] + "," + descT[4] + "," + descT[5] + "," + ttool + "," + tuser + "," + tvel + "," + tacc + "," +
                        epos_t.axis1 + "," + epos_t.axis2 + "," + epos_t.axis3 + "," + epos_t.axis4 + "," + ovl + "," + offset_flag + "," + offect[0] + "," + offect[1] + "," + offect[2] + "," + offect[3] + "," + offect[4] + "," + offect[5] + " : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                Circle(joint_pos_p, desc_pos_p, ptool, puser, pvel, pacc, epos_p, joint_pos_t, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, ovl, offset_flag, offset_pos);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  笛卡尔空间整圆运动
     * @param  joint_pos_p  路径点1关节位置,单位deg
     * @param  desc_pos_p   路径点1笛卡尔位姿
     * @param  ptool  工具坐标号，范围[0~14]
     * @param  puser  工件坐标号，范围[0~14]
     * @param  pvel  速度百分比，范围[0~100]
     * @param  pacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_p  扩展轴位置，单位mm
     * @param  joint_pos_t  路径点2关节位置,单位deg
     * @param  desc_pos_t   路径点2笛卡尔位姿
     * @param  ttool  工具坐标号，范围[0~14]
     * @param  tuser  工件坐标号，范围[0~14]
     * @param  tvel  速度百分比，范围[0~100]
     * @param  tacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_t  扩展轴位置，单位mm
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  oacc 加速度百分比
     * @param  blendR -1：阻塞；0~1000：平滑半径
     * @return  错误码
     */
    public int Circle(JointPos joint_pos_p, DescPose desc_pos_p, int ptool, int puser, double pvel, double pacc, ExaxisPos epos_p, JointPos joint_pos_t, DescPose desc_pos_t, int ttool, int tuser, double tvel, double tacc, ExaxisPos epos_t, double ovl, int offset_flag, DescPose offset_pos, double oacc, double blendR)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {

            Object[] jointP = {joint_pos_p.J1, joint_pos_p.J2, joint_pos_p.J3, joint_pos_p.J4, joint_pos_p.J5, joint_pos_p.J6};
            Object[] descP = { desc_pos_p.tran.x, desc_pos_p.tran.y, desc_pos_p.tran.z, desc_pos_p.rpy.rx, desc_pos_p.rpy.ry, desc_pos_p.rpy.rz };
            Object[] exteraxisP = {epos_p.axis1, epos_p.axis2, epos_p.axis3, epos_p.axis4};
            Object[] offect = { offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry, offset_pos.rpy.rz };
            Object[] controlP = { ptool * 1.0, puser * 1.0, pvel, pacc };
            Object[] jointT = {joint_pos_t.J1, joint_pos_t.J2, joint_pos_t.J3, joint_pos_t.J4, joint_pos_t.J5, joint_pos_t.J6};
            Object[] descT = { desc_pos_t.tran.x, desc_pos_t.tran.y, desc_pos_t.tran.z, desc_pos_t.rpy.rx, desc_pos_t.rpy.ry, desc_pos_t.rpy.rz };
            Object[] exteraxisT = {epos_t.axis1, epos_t.axis2, epos_t.axis3, epos_t.axis4};
            Object[] controlT = { ttool * 1.0, tuser * 1.0, tvel, tacc };
            Object[] ovl_offset={ovl,offset_flag*1.0};
            Object[] controlR = { oacc, blendR};
            Object[] params = new Object[] {jointP, descP, controlP, exteraxisP, jointT, descT, controlT, exteraxisT, ovl_offset, offect,controlR};
            int rtn = (int)client.execute("Circle" , params);
            if (log != null)
            {
                log.LogInfo("Circle(" + jointP[0] + "," + jointP[1] + "," + jointP[2] + "," + jointP[3] + "," + jointP[4] + "," + jointP[5] + "," + descP[0] + "," + descP[1] + "," + descP[2] + "," + descP[3] + "," + descP[4] + "," + descP[5] + "," + ptool + "," + puser + "," + pvel + "," + pacc + "," +
                        epos_p.axis1 + "," + epos_p.axis2 + "," + epos_p.axis3 + "," + epos_p.axis4 + ",) " +
                        jointT[0] + "," + jointT[1] + "," + jointT[2] + "," + jointT[3] + "," + jointT[4] + "," + jointT[5] + "," + descT[0] + "," + descT[1] + "," + descT[2] + "," + descT[3] + "," + descT[4] + "," + descT[5] + "," + ttool + "," + tuser + "," + tvel + "," + tacc + "," +
                        epos_t.axis1 + "," + epos_t.axis2 + "," + epos_t.axis3 + "," + epos_t.axis4 + "," + ovl + "," + offset_flag + "," + offect[0] + "," + offect[1] + "," + offect[2] + "," + offect[3] + "," + offect[4] + "," + offect[5] + ","+oacc +","+blendR +" : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                Circle(joint_pos_p, desc_pos_p, ptool, puser, pvel, pacc, epos_p, joint_pos_t, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, ovl, offset_flag, offset_pos,oacc,blendR);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  笛卡尔空间整圆运动 (重载函数1 不需要输入关节位置)
     * @param  desc_pos_p   路径点1笛卡尔位姿
     * @param  ptool  工具坐标号，范围[0~14]
     * @param  puser  工件坐标号，范围[0~14]
     * @param  pvel  速度百分比，范围[0~100]
     * @param  pacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_p  扩展轴位置，单位mm
     * @param  desc_pos_t   路径点2笛卡尔位姿
     * @param  ttool  工具坐标号，范围[0~14]
     * @param  tuser  工件坐标号，范围[0~14]
     * @param  tvel  速度百分比，范围[0~100]
     * @param  tacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_t  扩展轴位置，单位mm
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  oacc 加速度百分比
     * @param  blendR -1：阻塞；0~1000：平滑半径
     * @param  config 逆解关节空间配置，[-1]-参考当前关节位置解算，[0~7]-依据特定关节空间配置求解
     * @return  错误码
     */
    public int Circle(DescPose desc_pos_p, int ptool, int puser, double pvel, double pacc, ExaxisPos epos_p, DescPose desc_pos_t, int ttool, int tuser, double tvel, double tacc, ExaxisPos epos_t, double ovl, int offset_flag, DescPose offset_pos, double oacc, double blendR,int config)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        if (GetSafetyCode() != 0) {
            return GetSafetyCode();
        }

        JointPos jPosP=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        JointPos jPosT=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int errcode = GetInverseKin(0, desc_pos_p, config, jPosP);
        if (errcode != 0)
        {
            log.LogError("Circle GetInverseKin P failed rtn is: "+ errcode);
            return errcode;
        }

        errcode = GetInverseKin(0, desc_pos_t, config, jPosT);
        if (errcode != 0)
        {
            log.LogError("Circle GetInverseKin T failed rtn is:"+ errcode);
            return errcode;
        }

        errcode = Circle(jPosP, desc_pos_p, ptool, puser, pvel, pacc, epos_p, jPosT, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, ovl, offset_flag, offset_pos, oacc, blendR);

        return errcode;
    }

    /**
     *@brief  笛卡尔空间整圆运动
     *@param  joint_pos_p  路径点1关节位置,单位deg
     *@param  desc_pos_p   路径点1笛卡尔位姿
     *@param  ptool  工具坐标号，范围[1~15]
     *@param  puser  工件坐标号，范围[1~15]
     *@param  pvel  速度百分比，范围[0~100]
     *@param  pacc  加速度百分比，范围[0~100],暂不开放
     *@param  epos_p  扩展轴位置，单位mm
     *@param  joint_pos_t  路径点2关节位置,单位deg
     *@param  desc_pos_t   路径点2笛卡尔位姿
     *@param  ttool  工具坐标号，范围[1~15]
     *@param  tuser  工件坐标号，范围[1~15]
     *@param  tvel  速度百分比，范围[0~100]
     *@param  tacc  加速度百分比，范围[0~100],暂不开放
     *@param  epos_t  扩展轴位置，单位mm
     *@param  ovl  速度缩放因子，范围[0~100]
     *@param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     *@param  offset_pos  位姿偏移量
     *@param  oacc 加速度百分比
     *@param  blendR -1：阻塞；0~1000：平滑半径
     *@param  velAccParamMode 速度加速度参数模式；0-百分比；1-物理速度(mm/s)加速度(mm/s2)
     *@return  错误码
     */
    public int Circle(JointPos joint_pos_p, DescPose desc_pos_p, int ptool, int puser, double pvel, double pacc, ExaxisPos epos_p, JointPos joint_pos_t, DescPose desc_pos_t, int ttool, int tuser, double tvel, double tacc, ExaxisPos epos_t, double ovl, int offset_flag, DescPose offset_pos, double oacc, double blendR, int velAccParamMode)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        if (GetSafetyCode() != 0) {
            return GetSafetyCode();
        }

        try
        {

            Object[] params = new Object[] {joint_pos_p.J1, joint_pos_p.J2, joint_pos_p.J3, joint_pos_p.J4, joint_pos_p.J5, joint_pos_p.J6,
                    desc_pos_p.tran.x, desc_pos_p.tran.y, desc_pos_p.tran.z, desc_pos_p.rpy.rx, desc_pos_p.rpy.ry, desc_pos_p.rpy.rz,
                    ptool,puser,tvel,pacc,
                    epos_p.axis1, epos_p.axis2, epos_p.axis3, epos_p.axis4,
                    joint_pos_t.J1, joint_pos_t.J2, joint_pos_t.J3, joint_pos_t.J4, joint_pos_t.J5, joint_pos_t.J6,
                    desc_pos_t.tran.x, desc_pos_t.tran.y, desc_pos_t.tran.z, desc_pos_t.rpy.rx, desc_pos_t.rpy.ry, desc_pos_t.rpy.rz,
                    ttool,tuser,tvel,tacc,
                    epos_t.axis1, epos_t.axis2, epos_t.axis3, epos_t.axis4,
                    ovl,offset_flag,
                    offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry, offset_pos.rpy.rz,
                    oacc, blendR,velAccParamMode};

            Object[] params1 = new Object[]{params};

            int rtn = (int)client.execute("Circle" , params1);
            if (log != null)
            {
                log.LogInfo("Circle(" + joint_pos_p.J1 + "," + joint_pos_p.J2 + "," + joint_pos_p.J3 + "," + joint_pos_p.J4 + "," + joint_pos_p.J5 + "," + joint_pos_p.J6 + "," + desc_pos_p.tran.x + "," + desc_pos_p.tran.y + "," + desc_pos_p.tran.z + "," + desc_pos_p.rpy.rx + "," + desc_pos_p.rpy.ry + "," + desc_pos_p.rpy.rz + "," + ptool + "," + puser + "," + pvel + "," + pacc + "," +
                        epos_p.axis1 + "," + epos_p.axis2 + "," + epos_p.axis3 + "," + epos_p.axis4 + ",) " +
                        joint_pos_t.J1 + "," + joint_pos_t.J2 + "," + joint_pos_t.J3 + "," + joint_pos_t.J4 + "," + joint_pos_t.J5 + "," + joint_pos_t.J6 + "," + desc_pos_t.tran.x + "," + desc_pos_t.tran.y + "," + desc_pos_t.tran.z + "," + desc_pos_t.rpy.rx + "," + desc_pos_t.rpy.ry + "," + desc_pos_t.rpy.rz + "," + ttool + "," + tuser + "," + tvel + "," + tacc + "," +
                        epos_t.axis1 + "," + epos_t.axis2 + "," + epos_t.axis3 + "," + epos_t.axis4 + "," + ovl + "," + offset_flag + "," + offset_pos.tran.x + "," + offset_pos.tran.y + "," + offset_pos.tran.z + "," + offset_pos.rpy.rx + "," + offset_pos.rpy.ry + "," + offset_pos.rpy.rz + ","+oacc +","+blendR + ","+velAccParamMode+" : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                Circle(joint_pos_p, desc_pos_p, ptool, puser, pvel, pacc, epos_p, joint_pos_t, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, ovl, offset_flag, offset_pos,oacc,blendR,velAccParamMode);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  笛卡尔空间整圆运动 (重载函数1 不需要输入关节位置)
     * @param  desc_pos_p   路径点1笛卡尔位姿
     * @param  ptool  工具坐标号，范围[0~14]
     * @param  puser  工件坐标号，范围[0~14]
     * @param  pvel  速度百分比，范围[0~100]
     * @param  pacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_p  扩展轴位置，单位mm
     * @param  desc_pos_t   路径点2笛卡尔位姿
     * @param  ttool  工具坐标号，范围[0~14]
     * @param  tuser  工件坐标号，范围[0~14]
     * @param  tvel  速度百分比，范围[0~100]
     * @param  tacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_t  扩展轴位置，单位mm
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  oacc 加速度百分比
     * @param  blendR -1：阻塞；0~1000：平滑半径
     * @param  config 逆解关节空间配置，[-1]-参考当前关节位置解算，[0~7]-依据特定关节空间配置求解
     * @param  velAccParamMode 速度加速度参数模式；0-百分比；1-物理速度(mm/s)加速度(mm/s2)
     * @return  错误码
     */
    public int Circle(DescPose desc_pos_p, int ptool, int puser, double pvel, double pacc, ExaxisPos epos_p, DescPose desc_pos_t, int ttool, int tuser, double tvel, double tacc, ExaxisPos epos_t, double ovl, int offset_flag, DescPose offset_pos, double oacc, double blendR, int config, int velAccParamMode)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        if (GetSafetyCode() != 0) {
            return GetSafetyCode();
        }

        JointPos jPosP=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        JointPos jPosT=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int errcode = GetInverseKin(0, desc_pos_p, config, jPosP);
        if (errcode != 0)
        {
            log.LogInfo("Circle GetInverseKin P failed rtn is "+ errcode);
            return errcode;
        }
        System.out.println("p pos [0] "+ jPosT.J1);

        errcode = GetInverseKin(0, desc_pos_t, config, jPosT);
        if (errcode != 0)
        {
            log.LogInfo("Circle GetInverseKin T failed rtn is "+ errcode);
            return errcode;
        }
        System.out.println("t pos [0] "+ jPosT.J1);

        errcode = Circle(jPosP, desc_pos_p, ptool, puser, pvel, pacc, epos_p, jPosT, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, ovl, offset_flag, offset_pos, oacc, blendR, velAccParamMode);

        return errcode;
    }

    /**
     * @brief  笛卡尔空间螺旋线运动
     * @param  joint_pos  目标关节位置,单位deg
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  epos  扩展轴位置，单位mm
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  spiral_param  螺旋参数
     * @return  错误码
     */
//    public int NewSpiral(JointPos joint_pos, DescPose desc_pos, int tool, int user, double vel, double acc, ExaxisPos epos, double ovl, int offset_flag, DescPose offset_pos, SpiralParam spiral_param)
//    {
//        if (IsSockComError())
//        {
//            return sockErr;
//        }
//        if(GetSafetyCode()!=0){
//            return GetSafetyCode();
//        }
//        try
//        {
//            Object[] jointPos = {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6};
//            Object[] descPos = { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };
//            Object[] exteraxisPos = {epos.axis1, epos.axis2, epos.axis3, epos.axis4};
//            Object[] offectPos = { offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry, offset_pos.rpy.rz };
//            Object[] spiralParam = { spiral_param.circle_num * 1.0, spiral_param.circle_angle, spiral_param.rad_init, spiral_param.rad_add, spiral_param.rotaxis_add, spiral_param.rot_direction * 1.0 };
//            Object[] params = new Object[] {jointPos, descPos, tool, user, vel, acc, exteraxisPos, ovl, offset_flag , offectPos, spiralParam};
//            int rtn = (int)client.execute("NewSpiral" , params);
//            if (log != null)
//            {
//                log.LogInfo("NewSpiral(" + jointPos[0] + "," + jointPos[1] + "," + jointPos[2] + "," + jointPos[3] + "," + jointPos[4] + "," + jointPos[5] + "," + descPos[0] + "," + descPos[1] + "," + descPos[2] + "," + descPos[3] + "," + descPos[4] + "," + descPos[5] + "," + tool + "," + user + "," + vel + "," + acc + "," +
//                        epos.axis1 + "," + epos.axis2 + "," + epos.axis3 + "," + epos.axis4 + "," + ovl + "," + offset_flag + ",) " +
//                        offectPos[0] + "," + offectPos[1] + "," + offectPos[2] + "," + offectPos[3] + "," + offectPos[4] + "," + offectPos[5] + "," + spiralParam[0] + "," + spiralParam[1] + "," + spiralParam[2] + "," + spiralParam[3] + "," + spiralParam[4] + "," + spiralParam[5] + " : " + rtn);
//            }
//            return rtn;
//        }
//        catch (Throwable e)
//        {
//            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
//            {
//                NewSpiral(joint_pos, desc_pos, tool, user, vel, acc, epos, ovl, offset_flag, offset_pos, spiral_param);
//            }
//            else if (log != null)
//            {
//                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
//            }
//            return RobotError.ERR_RPC_ERROR;
//        }
//    }

    /**
     * @brief  笛卡尔空间螺旋线运动 (重载函数1 不需要输入关节位置)
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  epos  扩展轴位置，单位mm
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  spiral_param  螺旋参数
     * @param  config 逆解关节空间配置，[-1]-参考当前关节位置解算，[0~7]-依据特定关节空间配置求解
     * @return  错误码
     */
    public int NewSpiral(DescPose desc_pos, int tool, int user, double vel, double acc, ExaxisPos epos, double ovl, int offset_flag, DescPose offset_pos, SpiralParam spiral_param,int config)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        if (GetSafetyCode() != 0) {
            return GetSafetyCode();
        }

        JointPos jPos=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int errcode = GetInverseKin(0, desc_pos, config, jPos);
        if (errcode != 0)
        {
            log.LogError("NewSpiral GetInverseKin failed rtn is: "+ errcode);
            return errcode;
        }

        errcode = NewSpiral(jPos, desc_pos, tool, user, vel, acc, epos, ovl, offset_flag, offset_pos, spiral_param);
        return errcode;
    }

    /**
     *@brief  笛卡尔空间螺旋线运动
     *@param   joint_pos  目标关节位置,单位deg
     *@param   desc_pos   目标笛卡尔位姿
     *@param   tool  工具坐标号，范围[1~15]
     *@param   user  工件坐标号，范围[1~15]
     *@param   vel  速度百分比，范围[0~100]
     *@param   acc  加速度百分比，范围[0~100],暂不开放
     *@param   epos  扩展轴位置，单位mm
     *@param   ovl  速度缩放因子，范围[0~100]
     *@param   offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     *@param   offset_pos  位姿偏移量
     *@param   spiral_param  螺旋参数
     * @return  错误码
     */
    public int NewSpiral(JointPos joint_pos, DescPose desc_pos, int tool, int user, double vel, double acc, ExaxisPos epos, double ovl, int offset_flag, DescPose offset_pos, SpiralParam spiral_param)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] param = new Object[] {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6,
                    desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz,
                    tool,user,vel,acc,epos.axis1, epos.axis2, epos.axis3, epos.axis4,ovl,offset_flag,
                    offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry, offset_pos.rpy.rz ,
                    spiral_param.circle_num * 1.0, spiral_param.circle_angle, spiral_param.rad_init, spiral_param.rad_add, spiral_param.rotaxis_add, spiral_param.rot_direction,spiral_param.velAccMode};
            Object[] params = new Object[] {param};
            int rtn = (int)client.execute("NewSpiral" , params);
            if (log != null)
            {
                log.LogInfo("NewSpiral :" + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                NewSpiral(joint_pos, desc_pos, tool, user, vel, acc, epos, ovl, offset_flag, offset_pos, spiral_param);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 伺服运动开始，配合ServoJ、ServoCart指令使用
     * @return  错误码
     */
    public int ServoMoveStart()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ServoMoveStart" , params);
            if (log != null)
            {
                log.LogInfo("ServoMoveStart(" + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                ServoMoveStart();
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 伺服运动结束，配合ServoJ、ServoCart指令使用
     * @return  错误码
     */
    public int ServoMoveEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ServoMoveEnd" , params);
            if (log != null)
            {
                log.LogInfo("ServoMoveEnd(" + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                ServoMoveEnd();
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  关节空间伺服模式运动
     * @param  joint_pos  目标关节位置,单位deg
     * @param  axisPos  外部轴位置,单位mm
     * @param  acc  加速度百分比，范围[0~100],暂不开放，默认为0
     * @param  vel  速度百分比，范围[0~100]，暂不开放，默认为0
     * @param  cmdT  指令下发周期，单位s，建议范围[0.001~0.0016]
     * @param  filterT 滤波时间，单位s，暂不开放，默认为0
     * @param  gain  目标位置的比例放大器，暂不开放，默认为0
     * @return  错误码
     */
    public int ServoJ(JointPos joint_pos, ExaxisPos axisPos, double acc, double vel, double cmdT, double filterT, double gain)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] jointPos = {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6};
            Object[] axis = {axisPos.axis1, axisPos.axis2, axisPos.axis3, axisPos.axis4};
            Object[] params = new Object[] {jointPos, axis, acc, vel, cmdT, filterT, gain};
            int rtn = (int)client.execute("ServoJ" , params);
            if (log != null)
            {
                log.LogInfo("ServoJ(" + Arrays.toString(jointPos) + "," + Arrays.toString(axis) + "," + acc + "," + vel + "," + cmdT + "," + filterT + "," + gain + " ): " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                ServoJ(joint_pos, axisPos, acc, vel, cmdT, filterT, gain);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  关节空间伺服模式运动
     * @param  joint_pos  目标关节位置,单位deg
     * @param  axisPos  外部轴位置,单位mm
     * @param  acc  加速度百分比，范围[0~100],暂不开放，默认为0
     * @param  vel  速度百分比，范围[0~100]，暂不开放，默认为0
     * @param  cmdT  指令下发周期，单位s，建议范围[0.001~0.0016]
     * @param  filterT 滤波时间，单位s，暂不开放，默认为0
     * @param  gain  目标位置的比例放大器，暂不开放，默认为0
     * @param  id  servoJ指令ID,默认为0
     * @return  错误码
     */
    public int ServoJ(JointPos joint_pos, ExaxisPos axisPos, double acc, double vel, double cmdT, double filterT, double gain, int id)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] jointPos = {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6};
            Object[] axis = {axisPos.axis1, axisPos.axis2, axisPos.axis3, axisPos.axis4};
            Object[] params = new Object[] {jointPos, axis, acc, vel, cmdT, filterT, gain, id};
            int rtn = (int)client.execute("ServoJ" , params);
            if (log != null)
            {
                log.LogInfo("ServoJ(" + Arrays.toString(jointPos) + "," + Arrays.toString(axis) + "," + acc + "," + vel + "," + cmdT + "," + filterT + "," + gain + "," + id + " ): " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                ServoJ(joint_pos, axisPos, acc, vel, cmdT, filterT, gain);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  笛卡尔空间伺服模式运动
     * @param  mode  0-绝对运动(基坐标系)，1-增量运动(基坐标系)，2-增量运动(工具坐标系)
     * @param  desc_pose  目标笛卡尔位姿或位姿增量
     * @param  pos_gain  位姿增量比例系数，仅在增量运动下生效，范围[0~1]
     * @param  acc  加速度百分比，范围[0~100],暂不开放，默认为0
     * @param  vel  速度百分比，范围[0~100]，暂不开放，默认为0
     * @param  cmdT  指令下发周期，单位s，建议范围[0.001~0.0016]
     * @param  filterT 滤波时间，单位s，暂不开放，默认为0
     * @param  gain  目标位置的比例放大器，暂不开放，默认为0
     * @return  错误码
     */
    public int ServoCart(int mode, DescPose desc_pose, Object[] pos_gain, double acc, double vel, double cmdT, double filterT, double gain)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] descPos = { desc_pose.tran.x, desc_pose.tran.y, desc_pose.tran.z, desc_pose.rpy.rx, desc_pose.rpy.ry, desc_pose.rpy.rz };
            Object[] params = new Object[] {mode, descPos, pos_gain, acc, vel, cmdT, filterT, gain};
            int rtn = (int)client.execute("ServoCart" , params);
            if (log != null)
            {
                log.LogInfo("ServoCart(" + mode + "," + descPos[0] + "," + descPos[1] + "," + descPos[2] + "," + descPos[3] + "," + descPos[4] + "," + descPos[5] + "," + pos_gain[0] + "," + pos_gain[1] + "," + pos_gain[2] + "," + pos_gain[3] + "," + pos_gain[4] + "," + pos_gain[5] + "," + acc + "," + vel + "," + cmdT + "," + filterT + "," + gain + " : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                ServoCart(mode, desc_pose, pos_gain, acc, vel, cmdT, filterT, gain);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  笛卡尔空间点到点运动
     * @param  desc_pos  目标笛卡尔位姿或位姿增量
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendT [-1.0]-运动到位(阻塞)，[0~500.0]-平滑时间(非阻塞)，单位ms
     * @param  config  关节空间配置，[-1]-参考当前关节位置解算，[0~7]-参考特定关节空间配置解算，默认为-1
     * @return  错误码
     */
    public int MoveCart(DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, double blendT, int config)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {

            Object[] descPos = { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };
            Object[] params = new Object[] {descPos, tool, user, vel, acc, ovl, blendT, config};
            int rtn = (int)client.execute("MoveCart" , params);
            if (log != null)
            {
                log.LogInfo("MoveCart(" + descPos[0] + "," + descPos[1] + "," + descPos[2] + "," + descPos[3] + "," + descPos[4] + "," + descPos[5] + "," + tool + "," + user + "," + vel + "," + acc + "," + ovl + "," + blendT + "," + config + " ): " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                MoveCart(desc_pos, tool, user, vel, acc, ovl, blendT, config);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  样条运动开始
     * @return  错误码
     */
    public int SplineStart()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("SplineStart" , params);
            if (log != null)
            {
                log.LogInfo("SplineStart(" + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                SplineStart();
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  关节空间样条运动
     * @param  joint_pos  目标关节位置,单位deg
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @return  错误码
     */
    public int SplinePTP(JointPos joint_pos, DescPose desc_pos, int tool, int user, double vel, double acc, double ovl)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] jointPos = {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6};
            Object[] descPos =  { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };
            Object[] params = new Object[] {jointPos, descPos, tool, user, vel, acc, ovl};
            int rtn = (int)client.execute("SplinePTP" , params);
            if (log != null)
            {
                log.LogInfo("SplinePTP(" + jointPos[0] + "," + jointPos[1] + "," + jointPos[2] + "," + jointPos[3] + "," + jointPos[4] + "," + jointPos[5] + "," + descPos[0] + "," + descPos[1] + "," + descPos[2] + "," + descPos[3] + "," + descPos[4] + "," + descPos[5] + "," + tool + "," + user + "," + vel + "," + acc + "," + ovl + " : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                SplinePTP(joint_pos, desc_pos, tool, user, vel, acc, ovl);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  关节空间样条运动 (重载函数1 不需要输入笛卡尔位置)
     * @param  joint_pos  目标关节位置,单位deg
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @return  错误码
     */
    public int SplinePTP(JointPos joint_pos, int tool, int user, double vel, double acc, double ovl)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }

        DescPose desc_pos =new DescPose();
        int errcode = GetForwardKin(joint_pos, desc_pos);
        if (errcode != 0)
        {
            log.LogError("MoveJ GetForwardKin failed rtn is:"+ errcode);
            return errcode;
        }

        errcode = SplinePTP(joint_pos, desc_pos, tool, user, vel, acc, ovl);

        return errcode;
    }

    /**
     * @brief  样条运动结束
     * @return  错误码
     */
    public int SplineEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("SplineEnd" , params);
            if (log != null)
            {
                log.LogInfo("SplineEnd() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                SplineEnd();
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 新样条运动开始
     * @param  type   0-圆弧过渡，1-给定点位为路径点
     * @param  averageTime  全局平均衔接时间(ms)(10 ~  )，默认2000
     * @return  错误码
     */
    public int NewSplineStart(int type, int averageTime)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {type, averageTime};
            int rtn = (int)client.execute("NewSplineStart" , params);
            if (log != null)
            {
                log.LogInfo("NewSplineStart() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                NewSplineStart(type, averageTime);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 新样条指令点
     * @param  joint_pos  目标关节位置,单位deg
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  lastFlag 是否为最后一个点，0-否，1-是
     * @return  错误码
     */
    public int NewSplinePoint(JointPos joint_pos, DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, double blendR, int lastFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] jointPos = {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6};
            Object[] descPos = { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };
            Object[] params = new Object[] {jointPos, descPos, tool, user, vel, acc, ovl, blendR, lastFlag};
            int rtn = (int)client.execute("NewSplinePoint" , params);
            if (log != null)
            {
                log.LogInfo("NewSplinePoint(" + jointPos[0] + "," + jointPos[1] + "," + jointPos[2] + "," + jointPos[3] + "," + jointPos[4] + "," + jointPos[5] + "," + descPos[0] + "," + descPos[1] + "," + descPos[2] + "," + descPos[3] + "," + descPos[4] + "," + descPos[5] + "," +
                        tool + "," + user + "," + vel + "," + acc + "," + ovl + "," + blendR + "," + lastFlag + " : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                NewSplinePoint(joint_pos, desc_pos, tool, user, vel, acc, ovl, blendR, lastFlag);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 新样条指令点(重载函数1 不需要输入关节位置)
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  lastFlag 是否为最后一个点，0-否，1-是
     * @param  config 逆解关节空间配置，[-1]-参考当前关节位置解算，[0~7]-依据特定关节空间配置求解
     * @return  错误码
     */
    public int NewSplinePoint(DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, double blendR, int lastFlag,int config)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        JointPos jPos=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int errcode = GetInverseKin(0, desc_pos, config, jPos);
        if (errcode != 0)
        {
            log.LogError("MoveL GetInverseKin failed rtn is: "+ errcode);
            return errcode;
        }

        errcode = NewSplinePoint(jPos, desc_pos, tool, user, vel, acc, ovl, blendR, lastFlag);
        return errcode;
    }

    /**
     * @brief 新样条运动结束
     * @return  错误码
     */
    public int NewSplineEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("NewSplineEnd" , params);
            if (log != null)
            {
                log.LogInfo("NewSplineEnd() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                NewSplineEnd();
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 终止运动
     * @return  错误码
     */
    public int StopMotion()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("StopMotion" , params);
            if (log != null)
            {
                log.LogInfo("StopMotion() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                StopMotion();
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 暂停运动
     * @return  错误码
     */
    public int PauseMotion()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            sendBuf = "/f/bIII" + PauseMotionCnt + "III103III5IIIPAUSEIII/b/f";
            int rtn = clientCmd.Send(sendBuf);
            Sleep(20);
            byte[] recvBuf = new byte[1024];
            clientCmd.Recv(recvBuf);
            String recvStr = new String(recvBuf).split("/b/f")[0];
            if (log != null)
            {
                log.LogInfo("PauseMotion() : " + sockErr + "   " + sendBuf + "  recv  " + recvStr + "/b/f");
            }
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
        return 0;
    }

    /**
     * @brief 恢复运动
     * @return  错误码
     */
    public int ResumeMotion()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            sendBuf = "/f/bIII" + ResumeMotionCnt + "III104III6IIIRESUMEIII/b/f";
            clientCmd.Send(sendBuf);
            Sleep(20);
            byte[] recvBuf = new byte[1024];
            clientCmd.Recv(recvBuf);
            ResumeMotionCnt++;
            isSendCmd = true;
            String recvStr = new String(recvBuf).split("/b/f")[0];
            if (log != null)
            {
                log.LogInfo("ResumeMotion() : " + sockErr + "   " + sendBuf + "  recv  " + recvStr + "/b/f");
            }
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

        return 0;
    }

    /**
     * @brief  点位整体偏移开始
     * @param  flag  0-基坐标系下/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @return  错误码
     */
    public int PointsOffsetEnable(int flag, DescPose offset_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] offectPos = { offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry, offset_pos.rpy.rz };
            Object[] params = new Object[] {flag, offectPos};
            int rtn = (int)client.execute("PointsOffsetEnable" , params);
            if (log != null)
            {
                log.LogInfo("PointsOffsetEnable(" + flag + "," + offectPos[0] + "," + offectPos[1] + "," + offectPos[2] + "," + offectPos[3] + "," + offectPos[4] + "," + offectPos[5] + " : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                PointsOffsetEnable(flag, offset_pos);
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  点位整体偏移结束
     * @return  错误码
     */
    public int PointsOffsetDisable()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("PointsOffsetDisable" , params);
            if (log != null)
            {
                log.LogInfo("PointsOffsetDisable() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if(e.getMessage().contains("Connection timed out") || e.getMessage().contains("connect timed out"))
            {
                PointsOffsetDisable();
            }
            else if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置控制箱数字量输出
     * @param  id  io编号，范围[0~15]
     * @param  status 0-关，1-开
     * @param  smooth 0-不平滑， 1-平滑
     * @param  block  0-阻塞，1-非阻塞
     * @return  错误码
     */
    public int SetDO(int id, int status, int smooth, int block)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {id, status, smooth, block};
            int rtn = (int)client.execute("SetDO" , params);
            if (log != null)
            {
                log.LogInfo("SetDO(" + id + "," + status + "," + smooth + "," + block + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置工具数字量输出
     * @param  id  io编号，范围[0~1]
     * @param  status 0-关，1-开
     * @param  smooth 0-不平滑， 1-平滑
     * @param  block  0-阻塞，1-非阻塞
     * @return  错误码
     */
    public int SetToolDO(int id, int status, int smooth, int block)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {id, status, smooth, block};
            int rtn = (int)client.execute("SetToolDO" , params);
            if (log != null)
            {
                log.LogInfo("SetToolDO(" + id + "," + status + "," + smooth + "," + block + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置控制箱模拟量输出
     * @param  id  io编号，范围[0~1]
     * @param  value 电流或电压值百分比，范围[0~100]对应电流值[0~20mA]或电压[0~10V]
     * @param  block  0-阻塞，1-非阻塞
     * @return  错误码
     */
    public int SetAO(int id, double value, int block)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {id, value * 40.95, block};
            int rtn = (int)client.execute("SetAO" , params);
            if (log != null)
            {
                log.LogInfo("SetAO(" + id + "," + value + "," + block + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置工具模拟量输出
     * @param  id  io编号，范围[0]
     * @param  value 电流或电压值百分比，范围[0~100]对应电流值[0~20mA]或电压[0~10V]
     * @param  block  0-阻塞，1-非阻塞
     * @return  错误码
     */
    public int SetToolAO(int id, double value, int block)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {id, value * 40.95, block};
            int rtn = (int)client.execute("SetToolAO" , params);
            if (log != null)
            {
                log.LogInfo("SetToolAO(" + id + "," + value + "," + block + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  获取控制箱数字量输入
     * @param  id  io编号，范围[0~15]
     * @param  block  0-阻塞，1-非阻塞
     * @param  level  0-低电平，1-高电平
     * @return  错误码
     */
    public int GetDI(int id, int block, int[] level)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        int errcode = 0;
        try
        {
            ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();
            Object[] params = new Object[] {};
            if (sockErr == RobotError.ERR_SUCCESS) {
                if (id >= 0 && id < 8) {
                    level[0] = (int) ((robot_state_pkg.cl_dgt_input_l & (0x01 << id)) >> id);
                } else if (id >= 8 && id < 16) {
                    id -= 8;
                    level[0] = (int) ((robot_state_pkg.cl_dgt_input_h & (0x01 << id)) >> id);
                } else {
                    level[0] = 0;
                    errcode = -1;
                }
            }
            else {
                errcode = sockErr;
            }
            if (log != null)
            {
                log.LogInfo("GetDI(id + "+block + ",level: "+errcode+")");
            }
            return errcode;
        }
        catch (Throwable e)
        {
            return -1;
        }
    }

    /**
     * @brief  获取工具数字量输入
     * @param  id  io编号，范围[0~1]
     * @param  block  0-阻塞，1-非阻塞
     * @param  level  0-低电平，1-高电平
     * @return  错误码
     */
    public int GetToolDI(int id, int block, int[] level)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();
            Object[] params = new Object[] {};
            int errcode = 0;

            if (sockErr == RobotError.ERR_SUCCESS)
            {
                if (id >= 0 && id < 2)
                {
                    id += 1;
                    level[0] = (int)((robot_state_pkg.tl_dgt_input_l & (0x01 << id)) >> id);
                }
                else
                {
                    level[0] = 0;
                    errcode = -1;
                }
            }
            else
            {
                errcode = sockErr;
            }

            if (log != null)
            {
                log.LogInfo("GetToolDI(id : "+block + ",level:  "+errcode+")");
            }
            return errcode;
        }
        catch (Throwable e)
        {
            return -1;
        }
    }

    /**
     * @brief 等待控制箱数字量输入
     * @param   id  io编号，范围[0~15]
     * @param   status 0-关，1-开
     * @param   max_time  最大等待时间，单位ms
     * @param   opt  超时后策略，0-程序停止并提示超时，1-忽略超时提示程序继续执行，2-一直等待
     * @return  错误码
     */
    public int WaitDI(int id, int status, int max_time, int opt)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {id, status, max_time, opt};
            int rtn = (int)client.execute("WaitDI" , params);
            if (log != null)
            {
                log.LogInfo("WaitDI(" + id + "," + status + "," + max_time + "," + opt + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 等待控制箱多路数字量输入
     * @param   mode 0-多路与，1-多路或
     * @param   id  io编号，bit0~bit7对应DI0~DI7，bit8~bit15对应CI0~CI7
     * @param   status 0-关，1-开
     * @param   max_time  最大等待时间，单位ms
     * @param   opt  超时后策略，0-程序停止并提示超时，1-忽略超时提示程序继续执行，2-一直等待
     * @return  错误码
     */
    public int WaitMultiDI(int mode, int id, int status, int max_time, int opt)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {mode, id, status, max_time, opt};
            int rtn = (int)client.execute("WaitMultiDI" , params);
            if (log != null)
            {
                log.LogInfo("WaitMultiDI(" + id + "," + status + "," + max_time + "," + opt + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief   等待工具数字量输入
     * @param   id  io编号，范围[0~1]
     * @param   status 0-关，1-开
     * @param   max_time  最大等待时间，单位ms
     * @param   opt  超时后策略，0-程序停止并提示超时，1-忽略超时提示程序继续执行，2-一直等待
     * @return  错误码
     */
    public int WaitToolDI(int id, int status, int max_time, int opt)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {id + 1, status, max_time, opt};
            int rtn = (int)client.execute("WaitToolDI" , params);
            if (log != null)
            {
                log.LogInfo("WaitToolDI(" + id + "," + status + "," + max_time + "," + opt + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  获取控制箱模拟量输入
     * @param  id  io编号，范围[0~1]
     * @param  block  0-阻塞，1-非阻塞
     * @param  persent  输入电流或电压值百分比，范围[0~100]对应电流值[0~20mS]或电压[0~10V]
     * @return  错误码
     */
    public int GetAI(int id, int block, double[] persent)
    {
        try
        {
            Object[] params = new Object[] {};
            int errcode = 0;
            ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();

            if (sockErr == RobotError.ERR_SUCCESS)
            {
                if (id >= 0 && id < 2)
                {
                    persent[0] = (double)(robot_state_pkg.cl_analog_input[id] / 40.95);
                }
                else
                {
                    persent[0] = 0;
                    errcode = -1;
                }
            }
            else
            {
                errcode = sockErr;
            }
            if (log != null)
            {
                log.LogInfo("GetAI(id:"+block + ",persent:"+persent[0]+")");
            }
            return errcode;
        }
        catch (Throwable e)
        {
            return -1;
        }
    }

    /**
     * @brief  获取工具模拟量输入
     * @param  id  io编号，范围[0]
     * @param  block  0-阻塞，1-非阻塞
     * @param  persent  输入电流或电压值百分比，范围[0~100]对应电流值[0~20mS]或电压[0~10V]
     * @return  错误码
     */
    public int GetToolAI(int id, int block, double[] persent)
    {
        try
        {
            ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();

            Object[] params = new Object[] {};
            int errcode = 0;

            if (sockErr == RobotError.ERR_SUCCESS)
            {
                persent[0] = (double)(robot_state_pkg.tl_anglog_input / 40.95);
            }
            else
            {
                errcode = sockErr;
            }
            if (log != null)
            {
                log.LogInfo("GetToolAI(" + id + "," + block + "," + persent[0] + ") : " + errcode);
            }
            return errcode;
        }
        catch (Throwable e)
        {
            return -1;
        }

    }

    /**
     * @brief 获取机器人末端点记录按钮状态
     * @param state 按钮状态，0-按下，1-松开
     * @return 错误码
     */
    public int GetAxlePointRecordBtnState(int[] state)
    {
        int errcode = 0;
        ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            state[0] = (int)((robot_state_pkg.tl_dgt_input_l & 0x10) >> 4);
        }
        else
        {
            errcode = sockErr;
        }

        if (log != null)
        {
            log.LogInfo("GetAxlePointRecordBtnState(" + state[0] + ") : " + errcode);
        }

        return errcode;
    }

    /**
     * @brief 获取机器人末端DO输出状态
     * @param do_state DO输出状态，do0~do1对应bit1~bit2,从bit0开始
     * @return 错误码
     */
    public int GetToolDO(int[] do_state)
    {
        int errcode = 0;
        ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            do_state[0] = robot_state_pkg.tl_dgt_output_l;
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetToolDO(" + do_state[0] + ") : " + errcode);
        }

        return errcode;
    }

    /**
     * @brief 获取机器人控制器DO输出状态
     * @param do_state_h DO输出状态，co0~co7对应bit0~bit7
     * @param do_state_l DO输出状态，do0~do7对应bit0~bit7
     * @return 错误码
     */
    public int GetDO(int[] do_state_h, int[] do_state_l)
    {
        int errcode = 0;
        ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            do_state_h[0] = robot_state_pkg.cl_dgt_output_h;
            do_state_l[0] = robot_state_pkg.cl_dgt_output_l;
        }
        else
        {
            errcode = sockErr;
        }

        if (log != null)
        {
            log.LogInfo("GetDO(" + do_state_h[0] + ",  " + do_state_l[0] + ") : " + errcode);
        }

        return errcode;
    }

    /**
     * @brief 等待控制箱模拟量输入
     * @param   id  io编号，范围[0~1]
     * @param   sign 0-大于，1-小于
     * @param   value 输入电流或电压值百分比，范围[0~100]对应电流值[0~20mS]或电压[0~10V]
     * @param   max_time  最大等待时间，单位ms
     * @param   opt  超时后策略，0-程序停止并提示超时，1-忽略超时提示程序继续执行，2-一直等待
     * @return  错误码
     */
    public int WaitAI(int id, int sign, double value, int max_time, int opt)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {id, sign, value * 40.95, max_time, opt};
            int rtn = (int)client.execute("WaitAI" , params);
            if (log != null)
            {
                log.LogInfo("WaitAI(" + id + "," + sign + "," + id + "," + value + "," + max_time + "," + opt + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 等待工具模拟量输入
     * @param   id  io编号，范围[0]
     * @param   sign 0-大于，1-小于
     * @param   value 输入电流或电压值百分比，范围[0~100]对应电流值[0~20mS]或电压[0~10V]
     * @param   max_time  最大等待时间，单位ms
     * @param   opt  超时后策略，0-程序停止并提示超时，1-忽略超时提示程序继续执行，2-一直等待
     * @return  错误码
     */
    public int WaitToolAI(int id, int sign, double value, int max_time, int opt)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {id, sign, value * 40.95, max_time, opt};
            int rtn = (int)client.execute("WaitToolAI" , params);
            if (log != null)
            {
                log.LogInfo("WaitToolAI(" + id + "," + sign + "," + id + "," + value + "," + max_time + "," + opt + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置全局速度
     * @param   vel  速度百分比，范围[0~100]
     * @return  错误码
     */
    public int SetSpeed(int vel)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {vel};
            int rtn = (int)client.execute("SetSpeed" , params);
            if (log != null)
            {
                log.LogInfo("SetSpeed(" + vel  + " ) : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  设置系统变量值
     * @param   id  变量编号，范围[1~20]
     * @param   value 变量值
     * @return  错误码
     */
    public int SetSysVarValue(int id, double value)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {id, value};
            int rtn = (int)client.execute("SetSysVarValue" , params);
            if (log != null)
            {
                log.LogInfo("SetSysVarValue(" + id + "," + value + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置工具参考点-六点法
     * @param point_num 点编号,范围[1~6]
     * @return 错误码
     */
    public int SetToolPoint(int point_num)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {point_num};
            int rtn = (int)client.execute("SetToolPoint" , params);
            if (log != null)
            {
                log.LogInfo("SetToolPoint(" + point_num + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  计算工具坐标系
     * @param tcp_pose 工具坐标系
     * @return 错误码
     */
    public int ComputeTool(DescPose tcp_pose)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("ComputeTool" , params);
            if ((int)result[0] == 0)
            {
                tcp_pose.tran.x = (double)result[1];
                tcp_pose.tran.y = (double)result[2];
                tcp_pose.tran.z = (double)result[3];
                tcp_pose.rpy.rx = (double)result[4];
                tcp_pose.rpy.ry = (double)result[5];
                tcp_pose.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("ComputeTool(" + tcp_pose.tran.x + "," + tcp_pose.tran.y + "," + tcp_pose.tran.z + "," + tcp_pose.rpy.rx + "," + tcp_pose.rpy.ry + "," + tcp_pose.rpy.rz + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置工具参考点-四点法
     * @param point_num 点编号,范围[1~4]
     * @return 错误码
     */
    public int SetTcp4RefPoint(int point_num)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {point_num};
            int rtn = (int)client.execute("SetTcp4RefPoint" , params);
            if (log != null)
            {
                log.LogInfo("SetTcp4RefPoint(" + point_num + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  计算工具坐标系
     * @param tcp_pose 工具坐标系
     * @return 错误码
     */
    public int ComputeTcp4(DescPose tcp_pose)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("ComputeTcp4" , params);
            if ((int)result[0] == 0)
            {
                tcp_pose.tran.x = (double)result[1];
                tcp_pose.tran.y = (double)result[2];
                tcp_pose.tran.z = (double)result[3];
                tcp_pose.rpy.rx = (double)result[4];
                tcp_pose.rpy.ry = (double)result[5];
                tcp_pose.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("ComputeTcp4(" + tcp_pose.tran.x + "," + tcp_pose.tran.y + ", " + tcp_pose.tran.z + "," + tcp_pose.rpy.rx + "," + tcp_pose.rpy.ry + "," + tcp_pose.rpy.rz + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 根据点位信息计算工具坐标系
     * @param  method 计算方法；0-四点法；1-六点法
     * @param  pos 关节位置组，四点法时数组长度为4个，六点法时数组长度为6个
     * @param  tool_pose 输出的工具坐标系
     * @return 错误码
     */
    public int ComputeToolCoordWithPoints(int method, JointPos[] pos,DescPose tool_pose)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] jointPos1 = {pos[0].J1, pos[0].J2, pos[0].J3, pos[0].J4, pos[0].J5, pos[0].J6};
            Object[] jointPos2 = {pos[1].J1, pos[1].J2, pos[1].J3, pos[1].J4, pos[1].J5, pos[1].J6};
            Object[] jointPos3 = {pos[2].J1, pos[2].J2, pos[2].J3, pos[2].J4, pos[2].J5, pos[2].J6};
            Object[] jointPos4 = {pos[3].J1, pos[3].J2, pos[3].J3, pos[3].J4, pos[3].J5, pos[3].J6};
            Object[] jointPos5 = {0.0,0.0,0.0,0.0,0.0,0.0};
            Object[] jointPos6 = {0.0,0.0,0.0,0.0,0.0,0.0};

            if (method == 1)
            {
                jointPos5=new Object[]{pos[4].J1, pos[4].J2, pos[4].J3, pos[4].J4, pos[4].J5, pos[4].J6};
                jointPos6=new Object[]{pos[5].J1, pos[5].J2, pos[5].J3, pos[5].J4, pos[5].J5, pos[5].J6};
            }

            Object[] params = new Object[] {method,jointPos1,jointPos2,jointPos3,jointPos4,jointPos5,jointPos6};
            Object[] result = (Object[])client.execute("ComputeToolCoordWithPoints" , params);
            if ((int)result[0] == 0)
            {
                tool_pose.tran.x = (double)result[1];
                tool_pose.tran.y = (double)result[2];
                tool_pose.tran.z = (double)result[3];
                tool_pose.rpy.rx = (double)result[4];
                tool_pose.rpy.ry = (double)result[5];
                tool_pose.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("ComputeToolCoordWithPoints("+method+") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 根据点位信息计算工件坐标系
     * @param  method 计算方法；0：原点-x轴-z轴  1：原点-x轴-xy平面
     * @param  pos 三个TCP位置组
     * @param  refFrame 参考坐标系
     * @param  tcp_pose 输出工件坐标系
     * @return 错误码
     */
    public int ComputeWObjCoordWithPoints(int method, DescPose[] pos, int refFrame,DescPose tcp_pose)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] param1 = {pos[0].tran.x,pos[0].tran.y,pos[0].tran.z,pos[0].rpy.rx,pos[0].rpy.ry,pos[0].rpy.rz};
            Object[] param2 = {pos[1].tran.x,pos[1].tran.y,pos[1].tran.z,pos[1].rpy.rx,pos[1].rpy.ry,pos[1].rpy.rz};
            Object[] param3 = {pos[2].tran.x,pos[2].tran.y,pos[2].tran.z,pos[2].rpy.rx,pos[2].rpy.ry,pos[2].rpy.rz};

            Object[] params = new Object[] {method,param1,param2,param3,refFrame};
            Object[] result = (Object[])client.execute("ComputeWObjCoordWithPoints" , params);
            if ((int)result[0] == 0)
            {
                tcp_pose.tran.x = (double)result[1];
                tcp_pose.tran.y = (double)result[2];
                tcp_pose.tran.z = (double)result[3];
                tcp_pose.rpy.rx = (double)result[4];
                tcp_pose.rpy.ry = (double)result[5];
                tcp_pose.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("ComputeWObjCoordWithPoints("+method+") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置机器人焊接电弧意外中断检测参数
     * @param checkEnable 是否使能检测；0-不使能；1-使能
     * @param arcInterruptTimeLength 电弧中断确认时长(ms)
     * @return 错误码
     */
    public int WeldingSetCheckArcInterruptionParam(int checkEnable, int arcInterruptTimeLength)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {checkEnable,arcInterruptTimeLength};
            int rtn = (int)client.execute("WeldingSetCheckArcInterruptionParam" , params);
            if (log != null)
            {
                log.LogInfo("WeldingSetCheckArcInterruptionParam() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取机器人焊接电弧意外中断检测参数
     * @return List[0]:错误码; List[1]:double 是否使能检测；0-不使能；1-使能; List[2]:电弧中断确认时长(ms)
     */
    public List<Integer> WeldingGetCheckArcInterruptionParam()
    {
        int rtn = -1;
        int checkEnable = 0;
        int arcInterruptTimeLength = 0;
        List<Integer> rtnArray = new ArrayList<Integer>() {};
        rtnArray.add(rtn);
        rtnArray.add(checkEnable);
        rtnArray.add(arcInterruptTimeLength);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("WeldingGetCheckArcInterruptionParam" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0) {
                rtnArray.set(1, (int) result[1]);
                rtnArray.set(2, (int) result[2]);
            }
            if (log != null)
            {
                log.LogInfo("WeldingGetCheckArcInterruptionParam(" + result[1] + ",  " + result[2] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief 设置机器人焊接中断恢复参数
     * @param  enable 是否使能焊接中断恢复
     * @param  length 焊缝重叠距离(mm)
     * @param  velocity 机器人回到再起弧点速度百分比(0-100)
     * @param  moveType 机器人运动到再起弧点方式；0-LIN；1-PTP
     * @return 错误码
     */
    public int WeldingSetReWeldAfterBreakOffParam(int enable, double length, double velocity, int moveType)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {enable, length, velocity, moveType};
            int rtn = (int)client.execute("WeldingSetReWeldAfterBreakOffParam" , params);
            if (log != null)
            {
                log.LogInfo("WeldingSetReWeldAfterBreakOffParam(" + enable + "," + length+ ","+ velocity + "," + moveType + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief 获取机器人焊接中断恢复参数
     * @return List[0]:错误码; List[1]:int 是否使能焊接中断恢复; List[2]:double 焊缝重叠距离(mm); List[3]:double 机器人回到再起弧点速度百分比(0-100);List[4]:int 机器人运动到再起弧点方式；0-LIN；1-PTP
     */
    public List<Number> WeldingGetReWeldAfterBreakOffParam()
    {
        int rtn = -1;
        int enable = 0;
        double length = 0.0;
        double velocity = 0.0;
        int moveType = 0;

        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(rtn);
        rtnArray.add(enable);
        rtnArray.add(length);
        rtnArray.add(velocity);
        rtnArray.add(moveType);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("WeldingGetReWeldAfterBreakOffParam" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0) {
                rtnArray.set(1, (int) result[1]);
                rtnArray.set(2, (double) result[2]);
                rtnArray.set(3, (double) result[3]);
                rtnArray.set(4, (int) result[4]);
            }
            if (log != null)
            {
                log.LogInfo("WeldingGetReWeldAfterBreakOffParam(" + result[1] + ",  " + result[2] + ",  " + result[3]+",  " + result[4]+") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief 设置机器人焊接中断后恢复焊接
     * @return 错误码
     */
    public int WeldingStartReWeldAfterBreakOff()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("WeldingStartReWeldAfterBreakOff" , params);
            if (log != null)
            {
                log.LogInfo("WeldingStartReWeldAfterBreakOff: " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置机器人焊接中断后退出焊接
     * @return 错误码
     */
    public int WeldingAbortWeldAfterBreakOff()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("WeldingAbortWeldAfterBreakOff" , params);
            if (log != null)
            {
                log.LogInfo("WeldingAbortWeldAfterBreakOff: " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置工具坐标系
     * @param  id 坐标系编号，范围[0~14]
     * @param  coord  工具中心点相对于末端法兰中心位姿
     * @param  type  0-工具坐标系，1-传感器坐标系
     * @param  install 安装位置，0-机器人末端，1-机器人外部
     * @param  toolID  工具ID
     * @param  loadNum  负载编号
     * @return  错误码
     */
    public int SetToolCoord(int id, DescPose coord, int type, int install, int toolID, int loadNum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] descCoord = { coord.tran.x, coord.tran.y, coord.tran.z, coord.rpy.rx, coord.rpy.ry, coord.rpy.rz };
            Object[] params = new Object[] {id, descCoord, type, install,toolID,loadNum};
            int rtn = (int)client.execute("SetToolCoord" , params);
            if (log != null)
            {
                log.LogInfo("SetToolCoord(" + id + "," + descCoord[0] + "," + descCoord[1] + "," + descCoord[2] + "," + descCoord[3] + "," + descCoord[4] + "," + descCoord[5] + "," + type + "," + install + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  设置工具坐标系列表
     * @param  id 坐标系编号，范围[0~14]
     * @param  coord  工具中心点相对于末端法兰中心位姿
     * @param  type  0-工具坐标系，1-传感器坐标系
     * @param  install 安装位置，0-机器人末端，1-机器人外部
     * @param  loadNum 负载编号
     * @return  错误码
     */
    public int SetToolList(int id, DescPose coord, int type, int install, int loadNum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] descCoord = { coord.tran.x, coord.tran.y, coord.tran.z, coord.rpy.rx, coord.rpy.ry, coord.rpy.rz };
            Object[] params = new Object[] {id, descCoord, type, install,loadNum};
            int rtn = (int)client.execute("SetToolList" , params);
            if (log != null)
            {
                log.LogInfo("SetToolList(" + id + "," + descCoord[0] + "," + descCoord[1] + "," + descCoord[2] + "," + descCoord[3] + "," + descCoord[4] + "," + descCoord[5] + "," + type + "," + install + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }



    /**
     * @brief 设置外部工具参考点-三点法
     * @param point_num 点编号,范围[1~3]
     * @return 错误码
     */
    public int SetExTCPPoint(int point_num)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {point_num};
            int rtn = (int)client.execute("SetExTCPPoint" , params);
            if (log != null)
            {
                log.LogInfo("SetExTCPPoint(" + point_num + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  计算外部工具坐标系-三点法
     * @param tcp_pose 外部工具坐标系
     * @return 错误码
     */
    public int ComputeExTCF(DescPose tcp_pose)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("ComputeExTCF" , params);
            if ((int)result[0] == 0)
            {
                tcp_pose.tran.x = (double)result[1];
                tcp_pose.tran.y = (double)result[2];
                tcp_pose.tran.z = (double)result[3];
                tcp_pose.rpy.rx = (double)result[4];
                tcp_pose.rpy.ry = (double)result[5];
                tcp_pose.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("ComputeExTCF(" + tcp_pose.tran.x + "," + tcp_pose.tran.y + "," + tcp_pose.tran.z + "," + tcp_pose.rpy.rx + "," + tcp_pose.rpy.ry + "," + tcp_pose.rpy.rz + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置外部工具坐标系
     * @param  id 坐标系编号，范围[0~14]
     * @param  etcp  工具中心点相对末端法兰中心位姿
     * @param  etool  待定
     * @return  错误码
     */
    public int SetExToolCoord(int id, DescPose etcp, DescPose etool)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {

            Object[] descEtcp = { etcp.tran.x, etcp.tran.y, etcp.tran.z, etcp.rpy.rx, etcp.rpy.ry, etcp.rpy.rz };
            Object[] descEtool = { etool.tran.x, etool.tran.y, etool.tran.z, etool.rpy.rx, etool.rpy.ry, etool.rpy.rz };
            Object[] params = new Object[] {id, descEtcp, descEtool};
            int rtn = (int)client.execute("SetExToolCoord" , params);
            if (log != null)
            {
                log.LogInfo("SetExToolCoord(" + id + "," + descEtcp[0] + "," + descEtcp[1] + "," + descEtcp[2] + "," + descEtcp[3] + "," + descEtcp[4] + "," + descEtcp[5] + "," + descEtool[0] + "," + descEtool[1] + "," + descEtool[2] + "," + descEtool[3] + "," + descEtool[4] + "," + descEtool[5] + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置外部工具坐标系列表
     * @param  id 坐标系编号，范围[0~14]
     * @param  etcp  工具中心点相对末端法兰中心位姿
     * @param  etool  待定
     * @return  错误码
     */
    public int SetExToolList(int id, DescPose etcp, DescPose etool)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] descEtcp = { etcp.tran.x, etcp.tran.y, etcp.tran.z, etcp.rpy.rx, etcp.rpy.ry, etcp.rpy.rz };
            Object[] descEtool = { etool.tran.x, etool.tran.y, etool.tran.z, etool.rpy.rx, etool.rpy.ry, etool.rpy.rz };
            Object[] params = new Object[] {id, descEtcp, descEtool};
            int rtn = (int)client.execute("SetExToolList" , params);
            if (log != null)
            {
                log.LogInfo("SetExToolList(" + id + "," + descEtcp[0] + "," + descEtcp[1] + "," + descEtcp[2] + "," + descEtcp[3] + "," + descEtcp[4] + "," + descEtcp[5] + "," + descEtool[0] + "," + descEtool[1] + "," + descEtool[2] + "," + descEtool[3] + "," + descEtool[4] + "," + descEtool[5] + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置工件参考点-三点法
     * @param point_num 点编号,范围[1~3]
     * @return 错误码
     */
    public int SetWObjCoordPoint(int point_num)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {point_num};
            int rtn = (int)client.execute("SetWObjCoordPoint" , params);
            if (log != null)
            {
                log.LogInfo("SetWObjCoordPoint(" + point_num + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  计算工件坐标系
     * @param method 计算方式 0：原点-x轴-z轴  1：原点-x轴-xy平面
     * @param refFrame 参考坐标系
     * @param wobj_pose 工件坐标系
     * @return 错误码
     */
    public int ComputeWObjCoord(int method, int refFrame, DescPose wobj_pose)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {method,refFrame};
            Object[] result = (Object[])client.execute("ComputeWObjCoord" , params);
            if ((int)result[0] == 0)
            {
                wobj_pose.tran.x = (double)result[1];
                wobj_pose.tran.y = (double)result[2];
                wobj_pose.tran.z = (double)result[3];
                wobj_pose.rpy.rx = (double)result[4];
                wobj_pose.rpy.ry = (double)result[5];
                wobj_pose.rpy.rz = (double)result[6];
            }
            if (log != null) {
                log.LogInfo("ComputeWObjCoord(" + wobj_pose.tran.x + "," + wobj_pose.tran.y + "," + wobj_pose.tran.z + "," + wobj_pose.rpy.rx + "," + wobj_pose.rpy.ry + "," + wobj_pose.rpy.rz + ") : " + (int) result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    public int LaserSensorRecord(int status, int delayMode, int delayTime, int delayDisExAxisNum, double delayDis, double sensitivePara, double speed)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {status,delayMode,delayTime,delayDisExAxisNum,delayDis,sensitivePara,speed};
            int rtn = (int)client.execute("LaserSensorRecord" , params);
            if (log != null)
            {
                log.LogInfo("LaserSensorRecord() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    public int LaserTrackingLaserOn(int weldId)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {weldId};
            int rtn = (int)client.execute("LaserTrackingLaserOn" , params);
            if (log != null)
            {
                log.LogInfo("LaserTrackingLaserOn("+weldId+") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    public int LaserTrackingLaserOff()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("LaserTrackingLaserOff" , params);
            if (log != null)
            {
                log.LogInfo("LaserTrackingLaserOff() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    public int LaserTrackingTrackOn(int coordId)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {coordId};
            int rtn = (int)client.execute("LaserTrackingTrackOn" , params);
            if (log != null)
            {
                log.LogInfo("LaserTrackingTrackOn() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    public int LaserTrackingTrackOff()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("LaserTrackingTrackOff" , params);
            if (log != null)
            {
                log.LogInfo("LaserTrackingTrackOff() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    public int LaserTrackingSearchStart(int direction, DescTran directionPoint, int vel, int distance, int timeout, int posSensorNum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {direction,directionPoint.x,directionPoint.y,directionPoint.z,
                    vel,distance,timeout,posSensorNum};
            int rtn = (int)client.execute("LaserTrackingSearchStart" , params);
            if (log != null)
            {
                log.LogInfo("LaserTrackingSearchStart() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
    * @brief  激光寻位结束
    * @return 错误码
    */
    public int LaserTrackingSearchStop()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("LaserTrackingSearchStop" , params);
            if (log != null)
            {
                log.LogInfo("LaserTrackingSearchStop() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置工件坐标系
     * @param  id 坐标系编号，范围[1~15]
     * @param  coord  工件坐标系相对于末端法兰中心位姿
     * @param  refFrame 参考坐标系
     * @return  错误码
     */
    public int SetWObjCoord(int id, DescPose coord, int refFrame)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] descCoord = { coord.tran.x, coord.tran.y, coord.tran.z, coord.rpy.rx, coord.rpy.ry, coord.rpy.rz };
            Object[] params = new Object[] {id, descCoord,refFrame};
            int rtn = (int)client.execute("SetWObjCoord" , params);
            if (log != null)
            {
                log.LogInfo("SetWObjCoord(" + id + "," + descCoord[0] + "," + descCoord[1] + "," + descCoord[2] + "," + descCoord[3] + "," + descCoord[4] + "," + descCoord[5] + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置工件坐标系列表
     * @param  id 坐标系编号，范围[1~15]
     * @param  coord  工件坐标系相对于末端法兰中心位姿
     * @param  refFrame 参考坐标系
     * @return  错误码
     */
    public int SetWObjList(int id, DescPose coord, int refFrame)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] descCoord = { coord.tran.x, coord.tran.y, coord.tran.z, coord.rpy.rx, coord.rpy.ry, coord.rpy.rz };
            Object[] params = new Object[] {id, descCoord,refFrame};
            int rtn = (int)client.execute("SetWObjList" , params);
            if (log != null)
            {
                log.LogInfo("SetWObjList(" + id + "," + descCoord[0] + "," + descCoord[1] + "," + descCoord[2] + "," + descCoord[3] + "," + descCoord[4] + "," + descCoord[5] + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置末端负载重量
     * @param  loadNum 负载编号
     * @param  weight  负载重量，单位kg
     * @return  错误码
     */
    public int SetLoadWeight(int loadNum,double weight)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {loadNum,weight};
            int rtn = (int)client.execute("SetLoadWeight" , params);
            if (log != null)
            {
                log.LogInfo("SetLoadWeight(" + weight + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置末端负载质心坐标
     * @param  coord 质心坐标，单位mm
     * @return  错误码
     */
//    public int SetLoadCoord(DescTran coord)
//    {
//        if (IsSockComError())
//        {
//            return sockErr;
//        }
//
//        try
//        {
//            Object[] params = new Object[] {coord.x, coord.y, coord.z};
//            int rtn = (int)client.execute("SetLoadCoord" , params);
//            if (log != null)
//            {
//                log.LogInfo("SetLoadCoord(" + coord.x + "," + coord.y + "," + coord.z + ") : " + rtn);
//            }
//            return rtn;
//        }
//        catch (Throwable e)
//        {
//            if (log != null)
//            {
//                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
//            }
//            return RobotError.ERR_RPC_ERROR;
//        }
//    }

    /**
     * @brief  设置机器人安装方式
     * @param  install  安装方式，0-正装，1-侧装，2-倒装
     * @return  错误码
     */
    public int SetRobotInstallPos(int install)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {install};
            int rtn = (int)client.execute("SetRobotInstallPos" , params);
            if (log != null)
            {
                log.LogInfo("SetRobotInstallPos(" + install + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置机器人安装角度，自由安装
     * @param  yangle  倾斜角
     * @param  zangle  旋转角
     * @return  错误码
     */
    public int SetRobotInstallAngle(double yangle, double zangle)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {yangle, zangle};
            int rtn = (int)client.execute("SetRobotInstallAngle" , params);
            if (log != null)
            {
                log.LogInfo("SetRobotInstallAngle(" + yangle + "," + zangle + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  等待指定时间
     * @param   t_ms  单位ms
     * @return  错误码
     */
    public int WaitMs(int t_ms)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {t_ms};
            int rtn = (int)client.execute("WaitMs" , params);
            if (log != null)
            {
                log.LogInfo("WaitMs(" + t_ms + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief   设置碰撞等级
     * @param   mode  0-等级，1-百分比
     * @param   level 碰撞阈值，等级对应范围[1 - 10对应等级1-10， 100-关闭],百分比对应范围[0~10 对应 0% - 100%]
     * @param   config 0-不更新配置文件，1-更新配置文件
     * @return  错误码
     */
    public int SetAnticollision(int mode, Object[] level, int config)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {

            Object[] collisionLevel;
            if (mode == 1)
            {
                collisionLevel = level;//TODO Array.ConvertAll<double, double>(level, t => Convert.ToDouble(t) * 10);
            }
            else
            {
                collisionLevel = level;
            }
            Object[] params = new Object[] {mode, collisionLevel, config};
            int rtn = (int)client.execute("SetAnticollision" , params);
            if (log != null)
            {
                log.LogInfo("SetAnticollision(" + mode + "," + collisionLevel[0] + "," + collisionLevel[1] + "," + collisionLevel[2] + "," + collisionLevel[3] + "," + collisionLevel[4] + "," + collisionLevel[5] + "," + config + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置碰撞后策略
     * @param   strategy  0-报错停止，1-继续运行
     * @param   safeTime  安全停止时间[1000 - 2000]ms
     * @param   safeDistance  安全停止距离[1-150]mm
     * @param   safeVel     安全速度[50-250]mm/s
     * @param   safetyMargin  j1-j6安全系数[1-10]
     * @return  错误码
     */
    public int SetCollisionStrategy(int strategy, int safeTime, int safeDistance, int safeVel, int safetyMargin[])
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] safety = new Object[]{safetyMargin[0],safetyMargin[1],safetyMargin[2],safetyMargin[3],safetyMargin[4],safetyMargin[5]};
            Object[] params = new Object[] {strategy,safeTime,safeDistance,safeVel,safety};
            System.out.println(Arrays.toString(safetyMargin));
            int rtn = (int)client.execute("SetCollisionStrategy" , params);
            if (log != null)
            {
                log.LogInfo("SetCollisionStrategy(" + strategy + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  设置正限位
     * @param  limit 六个关节位置，单位deg
     * @return  错误码
     */
    public int SetLimitPositive(Object[] limit)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {limit};
            int rtn = (int)client.execute("SetLimitPositive" , params);
            if (log != null)
            {
                log.LogInfo("SetLimitPositive(" + Arrays.toString(limit) + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置负限位
     * @param  limit 六个关节位置，单位deg
     * @return  错误码
     */
    public int SetLimitNegative(Object[] limit)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {limit};
            int rtn = (int)client.execute("SetLimitNegative" , params);
            if (log != null)
            {
                log.LogInfo("SetLimitNegative(" + Arrays.toString(limit) + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  错误状态清除
     * @return  错误码
     */
    public int ResetAllError()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ResetAllError" , params);
            if (log != null)
            {
                log.LogInfo("ResetAllError() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  关节摩擦力补偿开关
     * @param   state  0-关，1-开
     * @return  错误码
     */
    public int FrictionCompensationOnOff(int state)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {state};
            int rtn = (int)client.execute("FrictionCompensationOnOff" , params);
            if (log != null)
            {
                log.LogInfo("FrictionCompensationOnOff(" + state + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置关节摩擦力补偿系数-正装
     * @param   coeff 六个关节补偿系数，范围[0~1]
     * @return  错误码
     */
    public int SetFrictionValue_level(Object[] coeff)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {coeff};
            int rtn = (int)client.execute("SetFrictionValue_level" , params);
            if (log != null)
            {
                log.LogInfo("SetFrictionValue_level(" + Arrays.toString(coeff) + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置关节摩擦力补偿系数-侧装
     * @param   coeff 六个关节补偿系数，范围[0~1]
     * @return  错误码
     */
    public int SetFrictionValue_wall(Object[] coeff)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {coeff};
            int rtn = (int)client.execute("SetFrictionValue_wall" , params);
            if (log != null)
            {
                log.LogInfo("SetFrictionValue_wall(" + Arrays.toString(coeff) + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置关节摩擦力补偿系数-倒装
     * @param   coeff 六个关节补偿系数，范围[0~1]
     * @return  错误码
     */
    public int SetFrictionValue_ceiling(Object[] coeff)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {coeff};
            int rtn = (int)client.execute("SetFrictionValue_ceiling" , params);
            if (log != null)
            {
                log.LogInfo("SetFrictionValue_ceiling(" + Arrays.toString(coeff) + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置关节摩擦力补偿系数-自由安装
     * @param   coeff 六个关节补偿系数，范围[0~1]
     * @return  错误码
     */
    public int SetFrictionValue_freedom(Object[] coeff)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {coeff};
            int rtn = (int)client.execute("SetFrictionValue_freedom" , params);
            if (log != null)
            {
                log.LogInfo("SetFrictionValue_freedom(" + Arrays.toString(coeff) + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取机器人安装角度
     * @return  List[0]:错误码; List[1]:double yangle 倾斜角; List[2]:double zangle 旋转角
     */
    public List<Number> GetRobotInstallAngle()
    {
        int rtn = -1;
        double yAngle = 0;
        double zAngle = 0;
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(rtn);
        rtnArray.add(yAngle);
        rtnArray.add(zAngle);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetRobotInstallAngle" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0) {
                rtnArray.set(1, (double) result[1]);
                rtnArray.set(2, (double) result[2]);
            }
            if (log != null)
            {
                log.LogInfo("GetRobotInstallAngle(" + result[1] + ",  " + result[2] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  获取系统变量值
     * @param  id 系统变量编号，范围[1~20]
     * @return  List[0]:错误码; List[1]:double value 系统变量值
     */
    public List<Number> GetSysVarValue(int id)
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(-1);
        rtnArray.add(0.0);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {id};
            Object[] result = (Object[])client.execute("GetSysVarValue" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (double)result[1]);
            }
            if (log != null)
            {
                log.LogInfo("GetSysVarValue(" + id + ",  " + result[1] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  获取当前关节位置(角度)
     * @param  jPos 获取的六个关节位置，单位deg
     * @return  错误码
     */
    public int GetActualJointPosDegree(JointPos jPos)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            jPos.J1 = robotStateRoutineThread.pkg.jt_cur_pos[0];
            jPos.J2 = robotStateRoutineThread.pkg.jt_cur_pos[1];
            jPos.J3 = robotStateRoutineThread.pkg.jt_cur_pos[2];
            jPos.J4 = robotStateRoutineThread.pkg.jt_cur_pos[3];
            jPos.J5 = robotStateRoutineThread.pkg.jt_cur_pos[4];
            jPos.J6 = robotStateRoutineThread.pkg.jt_cur_pos[5];
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetActualJointPosDegree(" + ",  " + jPos.J1 + "," + jPos.J2 + "," + jPos.J3 + "," + jPos.J4 + ") : " + errcode);
        }
        return errcode;
    }

//            /**
//             * @brief  获取当前关节位置(弧度)
//             * @param  flag 0-阻塞，1-非阻塞
//             * @param  jPos 六个关节位置，单位rad
//             * @return  错误码
//             */
//            public int GetActualJointPosRadian(int flag, JointPos jPos)
//            {
//                if (IsSockComError())
//                {
//                    return sockErr;
//                }
//
//                try
//                {
//                    Object[] params = new Object[] {flag};
//                    Object[] result = (Object[])client.execute("GetActualJointPosRadian" , params);
//                    if ((int)result[0] == 0)
//                    {
//                        jPos.J1 = (double)result[1];
//                        jPos.J2 = (double)result[2];
//                        jPos.J3 = (double)result[3];
//                        jPos.J4 = (double)result[4];
//                        jPos.J5 = (double)result[5];
//                        jPos.J6 = (double)result[6];
//                    }
//                    if (log != null)
//                    {
//                        log.LogInfo("GetActualJointPosRadian({flag}," + jPos.J1}," + jPos.J2}, " + jPos.J3}, " + jPos.J4}, " + jPos.J5}, " + jPos.J6 + ") : " + (int)result[0]);
//                    }
//                    return (int)result[0];
//                }
//                catch (Throwable e)
//                {
//                    if (log != null)
//                    {
//                        log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
//                    }
//                    return RobotError.ERR_RPC_ERROR;
//                }
//            }

    /**
     * @brief  获取关节反馈速度-deg/s
     * @param  speed 六个关节速度
     * @return  错误码
     */
    public int GetActualJointSpeedsDegree(Object[] speed)
    {
        int errcode = 0;
        int i;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            for (i = 0; i < 6; i++)
            {
                speed[i] = robotStateRoutineThread.pkg.actual_qd[i];
            }
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetActualJointSpeedsDegree(" +  "," + speed[0] + "," + speed[1] + ",  " + speed[2] + ",  " + speed[3] + ",  " + speed[4] + ",  " + speed[5] + ") : " + errcode);
        }
        return errcode;
    }


    /**
     * @brief  获取关节反馈加速度-deg/s^2
     * @param  flag 0-阻塞，1-非阻塞
     * @param  acc 六个关节加速度
     * @return  错误码
     */
    public int GetActualJointAccDegree(int flag, Object[] acc)
    {
        int errCode = 0;
        int i;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            for (i = 0; i < 6; i++)
            {
                acc[i] = robotStateRoutineThread.pkg.actual_qdd[i];
            }
        }
        else
        {
            errCode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetActualJointAccDegree(" + flag + "," + acc[0] + "," + acc[1] + ",  " + acc[2] + ",  " + acc[3] + ",  " + acc[4] + ",  " + acc[5] + ") : " + errCode);
        }
        return errCode;

    }

    /**
     * @brief  获取TCP指令速度
     * @param  flag 0-阻塞，1-非阻塞
     * @param  tcp_speed 线性速度
     * @param  ori_speed 姿态速度
     * @return  错误码
     */
    public int GetTargetTCPCompositeSpeed(int flag, double tcp_speed, double ori_speed)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            tcp_speed = (double)robotStateRoutineThread.pkg.target_TCP_CmpSpeed[0];
            ori_speed = (double)robotStateRoutineThread.pkg.target_TCP_CmpSpeed[1];
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetTargetTCPCompositeSpeed("+flag + "," + tcp_speed + "," + ori_speed + ") : " + errcode);
        }
        return errcode;
    }

    /**
     * @brief  获取TCP反馈速度
     * @param  flag 0-阻塞，1-非阻塞
     * @param  tcp_speed 线性速度
     * @param  ori_speed 姿态速度
     * @return  错误码
     */
    public int GetActualTCPCompositeSpeed(int flag, double tcp_speed, double ori_speed)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            tcp_speed = (double)robotStateRoutineThread.pkg.actual_TCP_CmpSpeed[0];
            ori_speed = (double)robotStateRoutineThread.pkg.actual_TCP_CmpSpeed[1];
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetActualTCPCompositeSpeed(" + flag + "," + tcp_speed + "," + ori_speed + ") : " + errcode);
        }
        return errcode;

    }

    /**
     * @brief  获取TCP指令速度
     * @param  flag 0-阻塞，1-非阻塞
     * @param  speed [x,y,z,rx,ry,rz]速度
     * @return  错误码
     */
    public int GetTargetTCPSpeed(int flag, Object[] speed)
    {
        int errcode = 0;
        int i;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            for (i = 0; i < 6; i++)
            {
                speed[i] = (double)robotStateRoutineThread.pkg.target_TCP_Speed[i];
            }
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetTargetTCPSpeed(" + flag + "," + speed[0] + "," + speed[1] + ",  " + speed[2] + ",  " + speed[3] + ",  " + speed[4] + ",  " + speed[5] + ") : " + errcode);
        }
        return errcode;
    }

    /**
     * @brief  获取TCP反馈速度
     * @param  flag 0-阻塞，1-非阻塞
     * @param  speed [x,y,z,rx,ry,rz]速度
     * @return  错误码
     */
    public int GetActualTCPSpeed(int flag, Object[] speed)
    {
        int errcode = 0;
        int i;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            for (i = 0; i < 6; i++)
            {
                speed[i] = (double)robotStateRoutineThread.pkg.actual_TCP_Speed[i];
            }
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetActualTCPSpeed(" + flag + "," + speed[0] + "," + speed[1] + ",  " + speed[2] + ",  " + speed[3] + ",  " + speed[4] + ",  " + speed[5] + ") : " + errcode);
        }
        return errcode;
    }

    /**
     * @brief  获取当前工具位姿
     * @param  desc_pos  工具位姿
     * @return  错误码
     */
    public int GetActualTCPPose(DescPose desc_pos)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            desc_pos.tran.x = robotStateRoutineThread.pkg.tl_cur_pos[0];
            desc_pos.tran.y = robotStateRoutineThread.pkg.tl_cur_pos[1];
            desc_pos.tran.z = robotStateRoutineThread.pkg.tl_cur_pos[2];
            desc_pos.rpy.rx = robotStateRoutineThread.pkg.tl_cur_pos[3];
            desc_pos.rpy.ry = robotStateRoutineThread.pkg.tl_cur_pos[4];
            desc_pos.rpy.rz = robotStateRoutineThread.pkg.tl_cur_pos[5];
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetActualTCPPose(" + ",  " + desc_pos.tran.x + "," + desc_pos.tran.y + "," + desc_pos.tran.z + "," + desc_pos.rpy.rx + "," + desc_pos.rpy.ry + "," + desc_pos.rpy.rz + ",) : " + errcode);
        }
        return errcode;
    }

    /**
     * @brief  获取当前工具坐标系编号
     * @param  flag  0-阻塞，1-非阻塞
     * @param  id  工具坐标系编号
     * @return  错误码
     */
    public int GetActualTCPNum(int flag, int[] id)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            id[0] = robotStateRoutineThread.pkg.tool;
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetActualTCPNum("+flag + "," +id+ ")"+errcode);
        }
        return errcode;
    }

    /**
     * @brief  获取当前工件坐标系编号
     * @param  flag  0-阻塞，1-非阻塞
     * @param  id  工件坐标系编号
     * @return  错误码
     */
    public int GetActualWObjNum(int flag, int id[])
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            id[0] = robotStateRoutineThread.pkg.user;
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetActualWObjNum(" + flag + ",  " + id + ") : " + errcode);
        }
        return errcode;
    }

    /**
     * @brief  获取当前末端法兰位姿
     * @param  flag  0-阻塞，1-非阻塞
     * @param  desc_pos  法兰位姿
     * @return  错误码
     */
    public int GetActualToolFlangePose(int flag, DescPose desc_pos)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            desc_pos.tran.x = robotStateRoutineThread.pkg.flange_cur_pos[0];
            desc_pos.tran.y = robotStateRoutineThread.pkg.flange_cur_pos[1];
            desc_pos.tran.z = robotStateRoutineThread.pkg.flange_cur_pos[2];
            desc_pos.rpy.rx = robotStateRoutineThread.pkg.flange_cur_pos[3];
            desc_pos.rpy.ry = robotStateRoutineThread.pkg.flange_cur_pos[4];
            desc_pos.rpy.rz = robotStateRoutineThread.pkg.flange_cur_pos[5];
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetActualToolFlangePose(" + flag + ",  " + desc_pos.tran.x + "," + desc_pos.tran.y + "," + desc_pos.tran.z + "," + desc_pos.rpy.rx + "," + desc_pos.rpy.ry + "," + desc_pos.rpy.rz + ",) : " + errcode);
        }
        return errcode;
    }

    /**
     * @brief  逆运动学求解
     * @param  type 0-绝对位姿(基坐标系)，1-增量位姿(基坐标系)，2-增量位姿(工具坐标系)
     * @param  desc_pos 笛卡尔位姿
     * @param  config 关节空间配置，[-1]-参考当前关节位置解算，[0~7]-依据特定关节空间配置求解
     * @param  joint_pos 关节位置
     * @return  错误码
     */
    public int GetInverseKin(int type, DescPose desc_pos, int config, JointPos joint_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] descPos = { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };
            Object[] params = new Object[] {type, descPos, config};
            Object[] result = (Object[])client.execute("GetInverseKin" , params);
            if ((int)result[0] == 0)
            {
                joint_pos.J1 = (double)result[1];
                joint_pos.J2 = (double)result[2];
                joint_pos.J3 = (double)result[3];
                joint_pos.J4 = (double)result[4];
                joint_pos.J5 = (double)result[5];
                joint_pos.J6 = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("GetInverseKin(" + type + "," + descPos[0] + "," + descPos[1] + "," + descPos[2] + "," + descPos[3] + "," + descPos[4] + "," + descPos[5] + "," + config + "," + joint_pos.J1 + "," + joint_pos.J2 + ",  " + joint_pos.J3 + ",  " + joint_pos.J4 + ",  " + joint_pos.J5 + ",  " + joint_pos.J6 + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  逆运动学求解，参考指定关节位置求解
     * @param  posMode 0绝对位姿， 1相对位姿-基坐标系   2相对位姿-工具坐标系
     * @param  desc_pos 笛卡尔位姿
     * @param  joint_pos_ref 参考关节位置
     * @param  joint_pos 关节位置
     * @return  错误码
     */
    public int GetInverseKinRef(int posMode, DescPose desc_pos, JointPos joint_pos_ref, JointPos joint_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {

            Object[] descPos = { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };
            Object[] jointPos = {joint_pos_ref.J1, joint_pos_ref.J2, joint_pos_ref.J3, joint_pos_ref.J4, joint_pos_ref.J5, joint_pos_ref.J6};
            Object[] params = new Object[] {posMode, descPos, jointPos};
            Object[] result = (Object[])client.execute("GetInverseKinRef" , params);
            if ((int)result[0] == 0)
            {
                joint_pos.J1 = (double)result[1];
                joint_pos.J2 = (double)result[2];
                joint_pos.J3 = (double)result[3];
                joint_pos.J4 = (double)result[4];
                joint_pos.J5 = (double)result[5];
                joint_pos.J6 = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("GetInverseKinRef(" + posMode + "," + descPos[0] + "," + descPos[1] + "," + descPos[2] + "," + descPos[3] + "," + descPos[4] + "," + descPos[5] + "," + jointPos[0] + "," + jointPos[1] + "," + jointPos[2] + "," + jointPos[3] + "," + jointPos[4] + "," + jointPos[5] + "," + joint_pos.J1 + "," + joint_pos.J2 + ", " + joint_pos.J3 + ", " + joint_pos.J4 + ", " + joint_pos.J5 + ", " + joint_pos.J6 + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  逆运动学求解，参考指定关节位置判断是否有解
     * @param  posMode 0绝对位姿， 1相对位姿-基坐标系   2相对位姿-工具坐标系
     * @param  desc_pos 笛卡尔位姿
     * @param  joint_pos_ref 参考关节位置
     * @return  List[0]:错误码; List[1]: int hasResult 0-无解，1-有解
     */
    public List<Integer> GetInverseKinHasSolution(int posMode, DescPose desc_pos, JointPos joint_pos_ref)
    {
        List<Integer> rtnArray = new ArrayList<Integer>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] descPos = { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };
            Object[] jointPos = {joint_pos_ref.J1, joint_pos_ref.J2, joint_pos_ref.J3, joint_pos_ref.J4, joint_pos_ref.J5, joint_pos_ref.J6};
            Object[] params = new Object[] {posMode, descPos, jointPos};
            Object[] result = (Object[])client.execute("GetInverseKinHasSolution" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (boolean)result[1]? 1:0);
            }
            if (log != null)
            {
                log.LogInfo("GetInverseKinHasSolution(" + posMode + ", " + desc_pos.tran.x + "," + desc_pos.tran.y + "," + desc_pos.tran.z + "," + desc_pos.rpy.rx + "," + desc_pos.rpy.ry + "," + desc_pos.rpy.rz + "," + jointPos[0] + "," + jointPos[1] + "," + jointPos[2] + "," + jointPos[3] + "," + jointPos[4] + "," + jointPos[5] + "," + (boolean)result[1] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {

            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  正运动学求解
     * @param  joint_pos 关节位置
     * @param  desc_pos 笛卡尔位姿
     * @return  错误码
     */
    public int GetForwardKin(JointPos joint_pos, DescPose desc_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] jointPos = {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6};
            Object[] params = new Object[] {jointPos};
            Object[] result = (Object[])client.execute("GetForwardKin" , params);
            if ((int)result[0] == 0)
            {
                desc_pos.tran.x = (double)result[1];
                desc_pos.tran.y = (double)result[2];
                desc_pos.tran.z = (double)result[3];
                desc_pos.rpy.rx = (double)result[4];
                desc_pos.rpy.ry = (double)result[5];
                desc_pos.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("GetForwardKin(" + jointPos[0] + "," + jointPos[1] + "," + jointPos[2] + "," + jointPos[3] + "," + jointPos[4] + "," + jointPos[5] + ",  " + desc_pos.tran.x + "," + desc_pos.tran.y + "," + desc_pos.tran.z + "," + desc_pos.rpy.rx + "," + desc_pos.rpy.ry + "," + desc_pos.rpy.rz + ",) : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取当前关节转矩
     * @param  flag 0-阻塞，1-非阻塞
     * @return  List<Number> 0-错误码,关节转矩
     */
    public List<Number> GetJointTorques(int flag)
    {
        List<Number> torques=new ArrayList<>();
        torques.add(0);
        torques.add(0.0);
        torques.add(0.0);
        torques.add(0.0);
        torques.add(0.0);
        torques.add(0.0);
        torques.add(0.0);
        int i;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            for (i = 0; i < 6; i++)
            {
                torques.set(i+1,(double)robotStateRoutineThread.pkg.jt_cur_tor[i]);
            }
        }
        else
        {
            torques.set(0,sockErr);
        }
        if (log != null)
        {
            log.LogInfo("GetJointTorques(" + flag + "," + torques + ")");
        }
        return torques;
    }

    /**
     * @brief  获取当前负载的重量
     * @param  flag 0-阻塞，1-非阻塞
     * @return  List[0]:int 错误码; List[1]: double weight  负载重量，单位kg
     */
    public List<Number> GetTargetPayload(int flag)
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {flag};
            Object[] result = (Object[])client.execute("GetTargetPayload" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (double)result[1]);
            }
            if (log != null)
            {
                log.LogInfo("GetTargetPayload(" + flag + ", " + (double)result[1] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  获取当前负载的质心
     * @param  flag 0-阻塞，1-非阻塞
     * @param  cog 负载质心，单位mm
     * @return  错误码
     */
    public int GetTargetPayloadCog(int flag, DescTran cog)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {flag};
            Object[] result = (Object[])client.execute("GetTargetPayloadCog" , params);
            if ((int)result[0] == 0)
            {
                cog.x = (double)result[1];
                cog.y = (double)result[2];
                cog.z = (double)result[3];
            }
            if (log != null) {
                log.LogInfo("GetTargetPayloadCog(" + flag + "," + cog.x + "," + cog.y + "," + cog.z + ") : " + (int) result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  获取当前工具坐标系
     * @param  flag 0-阻塞，1-非阻塞
     * @param  desc_pos 工具坐标系位姿
     * @return  错误码
     */
    public int GetTCPOffset(int flag, DescPose desc_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {flag};
            Object[] result = (Object[])client.execute("GetTCPOffset" , params);
            if ((int)result[0] == 0)
            {
                desc_pos.tran.x = (double)result[1];
                desc_pos.tran.y = (double)result[2];
                desc_pos.tran.z = (double)result[3];
                desc_pos.rpy.rx = (double)result[4];
                desc_pos.rpy.ry = (double)result[5];
                desc_pos.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("GetTCPOffset(" + flag + "," + desc_pos.tran.x + "," + desc_pos.tran.y + "," + desc_pos.tran.z + "," + desc_pos.rpy.rx + "," + desc_pos.rpy.ry + "," + desc_pos.rpy.rz + ",) : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取当前工件坐标系
     * @param  flag 0-阻塞，1-非阻塞
     * @param  desc_pos 工件坐标系位姿
     * @return  错误码
     */
    public int GetWObjOffset(int flag, DescPose desc_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {flag};
            Object[] result = (Object[])client.execute("GetWObjOffset" , params);
            if ((int)result[0] == 0)
            {
                desc_pos.tran.x = (double)result[1];
                desc_pos.tran.y = (double)result[2];
                desc_pos.tran.z = (double)result[3];
                desc_pos.rpy.rx = (double)result[4];
                desc_pos.rpy.ry = (double)result[5];
                desc_pos.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("GetWObjOffset(" + flag + "," + desc_pos.tran.x + "," + desc_pos.tran.y + "," + desc_pos.tran.z + "," + desc_pos.rpy.rx + "," + desc_pos.rpy.ry + "," + desc_pos.rpy.rz + ",) : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取关节软限位角度
     * @param  flag 0-阻塞，1-非阻塞
     * @param  negative  负限位角度，单位deg
     * @param  positive  正限位角度，单位deg
     * @return  错误码
     */
    public int GetJointSoftLimitDeg(int flag, Object[] negative, Object[] positive)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {flag};
            Object[] result = (Object[])client.execute("GetJointSoftLimitDeg" , params);
            if ((int)result[0] == 0)
            {
                negative[0] = (double)result[1];
                positive[0] = (double)result[2];
                negative[1] = (double)result[3];
                positive[1] = (double)result[4];
                negative[2] = (double)result[5];
                positive[2] = (double)result[6];
                negative[3] = (double)result[7];
                positive[3] = (double)result[8];
                negative[4] = (double)result[9];
                positive[4] = (double)result[10];
                negative[5] = (double)result[11];
                positive[5] = (double)result[12];
            }
            if (log != null)
            {
                log.LogInfo("GetJointSoftLimitDeg(" + flag + "," + negative[0] + "," + negative[1] + "," + negative[2] + "," + negative[3] + "," + negative[4] + "," + negative[5] + "," + positive[0] + "," + positive[1] + "," + positive[2] + "," + positive[3] + "," + positive[4] + "," + positive[5] + ")");
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取系统时间
     * @return  List[0]:int 错误码; List[1]:double t_ms 单位ms
     */
    public List<Number> GetSystemClock()
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetSystemClock" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (double)result[1]);
            }
            if (log != null)
            {
                log.LogInfo("GetSystemClock( " + result[1] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }

    }

    /**
     * @brief  获取机器人当前关节配置
     * @return  List[0]:int 错误码; List[1]:int config 关节空间配置，范围[0~7]
     */
    public List<Integer> GetRobotCurJointsConfig()
    {
        List<Integer> rtnArray = new ArrayList<Integer>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetRobotCurJointsConfig" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (int)result[1]);
            }
            if (log != null)
            {
                log.LogInfo("GetRobotCurJointsConfig(" + (int)result[1] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  获取机器人默认速度
     * @return  List[0]:int 错误码; List[1]: double vel 速度，单位mm/s
     */
    public List<Number> GetDefaultTransVel()
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetDefaultTransVel" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (double)result[1]);
            }
            if (log != null)
            {
                log.LogInfo("GetDefaultTransVel(" + (double)result[1] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  查询机器人运动是否完成
     * @param   state  0-未完成，1-完成
     * @return  错误码
     */
    public int GetRobotMotionDone(int state)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            state = (int)robotStateRoutineThread.pkg.motion_done;
        }
        else
        {
            errcode = sockErr;
        }

        if (log != null)
        {
            log.LogInfo("GetRobotMotionDone("+state+":" + errcode);
        }

        return errcode;
    }

    /**
     * @brief  查询机器人错误码
     * @param   maincode  主错误码
     * @param   subcode   子错误码
     * @return  错误码
     */
    public int GetRobotErrorCode(int[] maincode, int[] subcode)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            maincode[0] = robotStateRoutineThread.pkg.main_code;
            subcode[0] = robotStateRoutineThread.pkg.sub_code;
        }
        else
        {
            errcode = sockErr;
        }

        if (log != null)
        {
            log.LogInfo("GetRobotErrorCode(" + maincode[0]+ " ," + subcode[0]+")");
        }

        return errcode;
    }

//            private int GetRobotErrorCode() throws XmlRpcException
//            {
//                int err = 0;
//                int ree = 0;
//                Object[] params = new Object[] {};
//                Object[] result = (Object[])client.execute("GetRobotErrorCode" , params);
//                if ((int)result[0] == 0)
//                {
//                    err = (int)result[1];
//                    ree = (int)result[2];
//                }
//                if (log != null)
//                {
//                    log.LogInfo("GetError( {err}, {ree}");
//                }
//                return (int)result[0];
//            }

    /**
     * @brief  查询机器人示教管理点位数据
     * @param   name  点位名
     * @return  List[0]:错误码; List[1] - List[20] : 点位数据double[20]{x,y,z,rx,ry,rz,j1,j2,j3,j4,j5,j6,tool,wobj,speed,acc,e1,e2,e3,e4}
     */
    public List<Number> GetRobotTeachingPoint(String name)
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        for(int i = 0; i < 21; i++)
        {
            rtnArray.add(-1);
        }

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {name};
            Object[] result = (Object[])client.execute("GetRobotTeachingPoint" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0) {
                String paramStr = (String) result[1];
                String[] parS = paramStr.split(",");
                if (parS.length != 20) {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "get Teaching Point size fail");
                    rtnArray.set(0, -1);
                    return rtnArray;
                }
                for (int i = 0; i < 20; i++) {
                    rtnArray.set(i + 1, Double.parseDouble(parS[i]));
                }

            }
            if (log != null)
            {
                log.LogInfo("GetRobotTeachingPoint(" + name + "," + rtnArray.get(0) + "," + rtnArray.get(1) + "," + rtnArray.get(2) + "," + rtnArray.get(3) + "," + rtnArray.get(4) + "," + rtnArray.get(5) + "," + rtnArray.get(6) + "," + rtnArray.get(7) + "," + rtnArray.get(8) +
                        "," + rtnArray.get(9) + "," + rtnArray.get(10) + "," + rtnArray.get(11) + "," + rtnArray.get(12) + "," + rtnArray.get(13) + "," + rtnArray.get(14) + "," + rtnArray.get(15) + "," + rtnArray.get(16) + "," + rtnArray.get(17) + "," + rtnArray.get(18) + "," + rtnArray.get(19) + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }


    /**
     * @brief  查询机器人运动队列缓存长度
     * @param   len  缓存长度
     * @return  错误码
     */
    public int GetMotionQueueLength(int[] len)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            len[0]=robotStateRoutineThread.pkg.mc_queue_len;
        }
        else
        {
            errcode = sockErr;
        }

        if (log != null)
        {
            log.LogInfo("GetMotionQueueLength(" + len+"): " + errcode);
        }

        return errcode;
    }


    /**
     * @brief  设置轨迹记录参数
     * @param  type  记录数据类型，1-关节位置
     * @param  name  轨迹文件名
     * @param  period_ms  数据采样周期，固定值2ms或4ms或8ms
     * @param  di_choose  DI选择,bit0~bit7对应控制箱DI0~DI7，bit8~bit9对应末端DI0~DI1，0-不选择，1-选择
     * @param  do_choose  DO选择,bit0~bit7对应控制箱DO0~DO7，bit8~bit9对应末端DO0~DO1，0-不选择，1-选择
     * @return  错误码
     */
    public int SetTPDParam(int type, String name, int period_ms, int di_choose, int do_choose)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {type, name, period_ms, di_choose, do_choose};
            int rtn = (int)client.execute("SetTPDParam" , params);
            if (log != null)
            {
                log.LogInfo("SetTPDParam(" + type + "," + name + "," + period_ms + "," + di_choose + "," + do_choose + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  开始轨迹记录
     * @param  type  记录数据类型，1-关节位置
     * @param  name  轨迹文件名
     * @param  period_ms  数据采样周期，固定值2ms或4ms或8ms
     * @param  di_choose  DI选择,bit0~bit7对应控制箱DI0~DI7，bit8~bit9对应末端DI0~DI1，0-不选择，1-选择
     * @param  do_choose  DO选择,bit0~bit7对应控制箱DO0~DO7，bit8~bit9对应末端DO0~DO1，0-不选择，1-选择
     * @return  错误码
     */
    public int SetTPDStart(int type, String name, int period_ms, int di_choose, int do_choose)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {type, name, period_ms, di_choose, do_choose};
            int rtn = (int)client.execute("SetTPDStart" , params);
            if (log != null)
            {
                log.LogInfo("SetTPDStart(" + type + "," + name + "," + period_ms + "," + di_choose + "," + do_choose + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  停止轨迹记录
     * @return  错误码
     */
    public int SetWebTPDStop()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("SetWebTPDStop" , params);
            if (log != null)
            {
                log.LogInfo("SetWebTPDStop() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  删除轨迹记录
     * @param  name  轨迹文件名
     * @return  错误码
     */
    public int SetTPDDelete(String name)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {name};
            int rtn = (int)client.execute("SetTPDDelete" , params);
            if (log != null)
            {
                log.LogInfo("SetTPDDelete(" + name + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  轨迹预加载
     * @param  name  轨迹文件名
     * @return  错误码
     */
    public int LoadTPD(String name)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {name};
            int rtn = (int)client.execute("LoadTPD" , params);
            if (log != null)
            {
                log.LogInfo("LoadTPD(" + name + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取轨迹起始位姿
     * @param  name 轨迹文件名,不需要文件后缀
     * @param  desc_pose 获取的轨迹起始位姿
     * @return  错误码
     */
    public int GetTPDStartPose(String name, DescPose desc_pose)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {name};
            Object[] result = (Object[])client.execute("GetTPDStartPose" , params);
            if ((int)result[0] == 0)
            {
                desc_pose.tran.x = (double)result[1];
                desc_pose.tran.y = (double)result[2];
                desc_pose.tran.z = (double)result[3];
                desc_pose.rpy.rx = (double)result[4];
                desc_pose.rpy.ry = (double)result[5];
                desc_pose.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("GetTrajectoryStartPose(" + desc_pose.tran.x + "," + desc_pose.tran.y + "," + desc_pose.tran.z + "," + desc_pose.rpy.rx + "," + desc_pose.rpy.ry + "," + desc_pose.rpy.rz + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  轨迹复现
     * @param  name  轨迹文件名
     * @param  blend 0-不平滑，1-平滑
     * @param  ovl  速度缩放百分比，范围[0~100]
     * @return  错误码
     */
    public int MoveTPD(String name, int blend, double ovl)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {name, blend, ovl};
            int rtn = (int)client.execute("MoveTPD" , params);
            if (log != null)
            {
                log.LogInfo("MoveTPD(" + name + ", " + blend + "," + ovl + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  轨迹预处理
     * @param  name  轨迹文件名
     * @param  ovl 速度缩放百分比，范围[0~100]
     * @param  opt 1-控制点，默认为1
     * @return  错误码
     */
    public int LoadTrajectoryJ(String name, double ovl, int opt)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {name, ovl, opt};
            int rtn = (int)client.execute("LoadTrajectoryJ" , params);
            if (log != null)
            {
                log.LogInfo("LoadTrajectoryJ(" + name + ", " + ovl + "," + opt + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  轨迹复现
     * @return  错误码
     */
    public int MoveTrajectoryJ()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("MoveTrajectoryJ" , params);
            if (log != null)
            {
                log.LogInfo("MoveTrajectoryJ() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief  轨迹预处理(轨迹前瞻)
     * @param  name  轨迹文件名
     * @param  mode 采样模式，0-不进行采样；1-等数据间隔采样；2-等误差限制采样
     * @param  errorLim 误差限制，使用直线拟合生效
     * @param  type 平滑方式，0-贝塞尔平滑
     * @param  precision 平滑精度，使用贝塞尔平滑时生效
     * @param  vamx 设定的最大速度，mm/s
     * @param  amax 设定的最大加速度，mm/s2
     * @param  jmax 设定的最大加加速度，mm/s3
     * @return  错误码
     */
    public int LoadTrajectoryLA(String name, int mode, double errorLim, int type, double precision, double vamx, double amax, double jmax)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {name, mode, errorLim, type, precision, vamx, amax, jmax};
            int rtn = (int)client.execute("LoadTrajectoryLA" , params);
            if (log != null)
            {
                log.LogInfo("LoadTrajectoryLA(" + name + ", " + mode + "," + errorLim+ ","+type+ ","+precision+ "," +vamx+ amax+","+jmax+") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  轨迹预处理(轨迹前瞻)
     * @param  name  轨迹文件名
     * @param  mode 曲线拟合方式；0-直线连接；1-直线拟合；2-B样条曲线
     * @param  errorLim 误差限制，使用直线拟合生效
     * @param  type 平滑方式，0-贝塞尔平滑
     * @param  precision 平滑精度，使用贝塞尔平滑时生效
     * @param  vamx 设定的最大速度，mm/s
     * @param  amax 设定的最大加速度，mm/s2
     * @param  jmax 设定的最大加加速度，mm/s3
     * @param  flag 匀速前瞻开启开关 0-不开启；1-开启
     * @return  错误码
     */
    public int LoadTrajectoryLA(String name, int mode, double errorLim, int type, double precision, double vamx, double amax, double jmax, int flag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {name, mode, errorLim, type, precision, vamx, amax, jmax, flag};
            int rtn = (int)client.execute("LoadTrajectoryLA" , params);
            if (log != null)
            {
                log.LogInfo("LoadTrajectoryLA(" + name + ", " + mode + "," + errorLim+ ","+type+ ","+precision+ "," +vamx+ amax+","+jmax+","+flag+") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  轨迹复现(轨迹前瞻)
     * @return  错误码
     */
    public int MoveTrajectoryLA()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("MoveTrajectoryLA" , params);
            if (log != null)
            {
                log.LogInfo("MoveTrajectoryLA() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  获取轨迹起始位姿
     * @param  name 轨迹文件名
     * @param  desc_pose 获取的轨迹起始位姿
     * @return  错误码
     */
    public int GetTrajectoryStartPose(String name, DescPose desc_pose)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {name};
            Object[] result = (Object[])client.execute("GetTrajectoryStartPose" , params);
            if ((int)result[0] == 0)
            {
                desc_pose.tran.x = (double)result[1];
                desc_pose.tran.y = (double)result[2];
                desc_pose.tran.z = (double)result[3];
                desc_pose.rpy.rx = (double)result[4];
                desc_pose.rpy.ry = (double)result[5];
                desc_pose.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("GetTrajectoryStartPose(" + desc_pose.tran.x + "," + desc_pose.tran.y + "," + desc_pose.tran.z + "," + desc_pose.rpy.rx + "," + desc_pose.rpy.ry + "," + desc_pose.rpy.rz + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取轨迹点编号
     * @return  错误码
     */
    public int GetTrajectoryPointNum(int pnum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int errcode = 0;

            if (sockErr == RobotError.ERR_SUCCESS)
            {
                pnum = robotStateRoutineThread.pkg.trajectory_pnum;
            }
            else
            {
                errcode = sockErr;
            }

            if (log != null)
            {
                log.LogInfo("GetTrajectoryPointNum("+pnum + ") :"+errcode);
            }

            return errcode;
        }
        catch (Throwable e)
        {

            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置轨迹运行中的速度
     * @param  ovl 速度百分比
     * @return  错误码
     */
    public int SetTrajectoryJSpeed(double ovl)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {ovl};
            int rtn = (int)client.execute("SetTrajectoryJSpeed" , params);
            if (log != null)
            {
                log.LogInfo("SetTrajectoryJSpeed(" + ovl + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  设置轨迹运行中的力和扭矩
     * @param  ft 三个方向的力和扭矩，单位N和Nm
     * @return  错误码
     */
    public int SetTrajectoryJForceTorque(ForceTorque ft)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {

            Object[] ftData = { ft.fx, ft.fy, ft.fz, ft.tx, ft.ty, ft.tz };
            Object[] params = new Object[] {ftData};
            int rtn = (int)client.execute("SetTrajectoryJForceTorque" , params);
            if (log != null)
            {
                log.LogInfo("SetTrajectoryJForceTorque(" + ftData[0] + "," + ftData[1] + "," + ftData[2] + "," + ftData[3] + "," + ftData[4] + "," + ftData[5] + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置轨迹运行中的沿x方向的力
     * @param  fx 沿x方向的力，单位N
     * @return  错误码
     */
    public int SetTrajectoryJForceFx(double fx)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {fx};
            int rtn = (int)client.execute("SetTrajectoryJForceFx" , params);
            if (log != null)
            {
                log.LogInfo("SetTrajectoryJForceFx(" + fx + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  设置轨迹运行中的沿y方向的力
     * @param  fy 沿y方向的力，单位N
     * @return  错误码
     */
    public int SetTrajectoryJForceFy(double fy)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {fy};
            int rtn = (int)client.execute("SetTrajectoryJForceFy" , params);
            if (log != null)
            {
                log.LogInfo("SetTrajectoryJForceFy(" + fy + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  设置轨迹运行中的沿z方向的力
     * @param  fz 沿x方向的力，单位N
     * @return  错误码
     */
    public int SetTrajectoryJForceFz(double fz)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {fz};
            int rtn = (int)client.execute("SetTrajectoryJForceFz" , params);
            if (log != null)
            {
                log.LogInfo("SetTrajectoryJForceFz(" + fz + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  设置轨迹运行中的绕x轴的扭矩
     * @param  tx 绕x轴的扭矩，单位Nm
     * @return  错误码
     */
    public int SetTrajectoryJTorqueTx(double tx)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {tx};
            int rtn = (int)client.execute("SetTrajectoryJTorqueTx" , params);
            if (log != null)
            {
                log.LogInfo("SetTrajectoryJTorqueTx(" + tx + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置轨迹运行中的绕y轴的扭矩
     * @param  ty 绕y轴的扭矩，单位Nm
     * @return  错误码
     */
    public int SetTrajectoryJTorqueTy(double ty)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {ty};
            int rtn = (int)client.execute("SetTrajectoryJTorqueTy" , params);
            if (log != null)
            {
                log.LogInfo("SetTrajectoryJTorqueTy(" + ty + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  设置轨迹运行中的绕z轴的扭矩
     * @param  tz 绕z轴的扭矩，单位Nm
     * @return  错误码
     */
    public int SetTrajectoryJTorqueTz(double tz)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {tz};
            int rtn = (int)client.execute("SetTrajectoryJTorqueTz" , params);
            if (log != null)
            {
                log.LogInfo("SetTrajectoryJTorqueTz(" + tz + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  设置开机自动加载默认的作业程序
     * @param  flag  0-开机不自动加载默认程序，1-开机自动加载默认程序
     * @param  program_name 作业程序名及路径，如"/fruser/movej.lua"，其中"/fruser/"为固定路径
     * @return  错误码
     */
    public int LoadDefaultProgConfig(int flag, String program_name)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {flag, program_name};
            int rtn = (int)client.execute("LoadDefaultProgConfig" , params);
            if (log != null)
            {
                log.LogInfo("LoadDefaultProgConfig(" + flag + "," + program_name + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  加载指定的作业程序
     * @param  program_name 作业程序名及路径，如"/fruser/movej.lua"，其中"/fruser/"为固定路径
     * @return  错误码
     */
    public int ProgramLoad(String program_name)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {program_name};
            int rtn = (int)client.execute("ProgramLoad" , params);
            if (log != null)
            {
                log.LogInfo("ProgramLoad(" + program_name + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取已加载的作业程序名
     * @param  program_name program_name[0]:作业程序名及路径，如"/fruser/movej.lua"，其中"/fruser/"为固定路径
     * @return  错误码
     */
    public int GetLoadedProgram(String[] program_name)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetLoadedProgram" , params);
            if ((int)result[0] == 0)
            {
                program_name[0] = (String)result[1];
            }
            if (log != null)
            {
                log.LogInfo("GetLoadedProgram(" + program_name[0] + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取当前机器人作业程序执行的行号
     * @return  List[0]:错误码; List[1] : int line 行号
     */
    public List<Integer> GetCurrentLine()
    {
        List<Integer> rtnArray = new ArrayList<Integer>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetCurrentLine" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (int)result[1]);
            }
            if (log != null)
            {
                log.LogInfo("GetCurrentLine(" + (int)result[1] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  运行当前加载的作业程序
     * @return  错误码
     */
    public int ProgramRun()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ProgramRun" , params);
            if (log != null)
            {
                log.LogInfo("ProgramRun() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  暂停当前运行的作业程序
     * @return  错误码
     */
    public int ProgramPause()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ProgramPause" , params);
            if (log != null)
            {
                log.LogInfo("ProgramPause() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  恢复当前暂停的作业程序
     * @return  错误码
     */
    public int ProgramResume()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ProgramResume" , params);
            if (log != null)
            {
                log.LogInfo("ProgramResume() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  终止当前运行的作业程序
     * @return  错误码
     */
    public int ProgramStop()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ProgramStop" , params);
            if (log != null)
            {
                log.LogInfo("ProgramStop() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取机器人作业程序执行状态
     * @param   state 1-程序停止或无程序运行，2-程序运行中，3-程序暂停
     * @return  错误码
     */
    public int GetProgramState(int[] state)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            state[0] = robotStateRoutineThread.pkg.robot_state;
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetProgramState("+state[0] +") : "+errcode);
        }
        return errcode;
    }

    /**
     * @brief  配置夹爪
     * @param  config .company  夹爪厂商，1-Robotiq，2-慧灵，3-天机，4-大寰，5-知行
     * @param  config .device  设备号，Robotiq(0-2F-85系列)，慧灵(0-NK系列,1-Z-EFG-100)，天机(0-TEG-110)，大寰(0-PGI-140)，知行(0-CTPM2F20)
     * @param  config .softvesion  软件版本号，暂不使用，默认为0
     * @param  config .bus 设备挂在末端总线位置，暂不使用，默认为0
     * @return  错误码
     */
    public int SetGripperConfig(DeviceConfig config)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {config.company, config.device,config.softwareVersion, config.bus};
            int rtn = (int)client.execute("SetGripperConfig" , params);
            if (log != null)
            {
                log.LogInfo("SetGripperConfig(" + config.company + "," + config.device + "," + config.softwareVersion + "," + config.bus + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     *@brief  获取夹爪配置
     * @param  config .company  夹爪厂商，1-Robotiq，2-慧灵，3-天机，4-大寰，5-知行
     * @param  config .device  设备号，Robotiq(0-2F-85系列)，慧灵(0-NK系列,1-Z-EFG-100)，天机(0-TEG-110)，大寰(0-PGI-140)，知行(0-CTPM2F20)
     * @param  config .softvesion  软件版本号，暂不使用，默认为0
     * @param  config .bus 设备挂在末端总线位置，暂不使用，默认为0
     * @return  错误码
     */
    public int GetGripperConfig(DeviceConfig config)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetGripperConfig" , params);
            if ((int)result[0] == 0)
            {
                config.company         = (int)result[2] + 1;
                config.device          = (int)result[1];
                config.softwareVersion = (int)result[3];
                config.bus             = (int)result[4];
            }
            if (log != null)
            {
                log.LogInfo("GetGripperConfig(" + config.company + "," + config.device + "," + config.softwareVersion + "," + config.bus + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return (int)RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  激活夹爪
     * @param  index  夹爪编号
     * @param  act  0-复位，1-激活
     * @return  错误码
     */
    public int ActGripper(int index, int act)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {index, act};
            int rtn = (int)client.execute("ActGripper" , params);
            if (log != null)
            {
                log.LogInfo("ActGripper(" + index + "," + act + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  控制夹爪
     * @param   index  夹爪编号
     * @param   pos  位置百分比，范围[0~100]
     * @param   vel  速度百分比，范围[0~100]
     * @param   force  力矩百分比，范围[0~100]
     * @param   max_time  最大等待时间，范围[0~30000]，单位ms
     * @param   block  0-阻塞，1-非阻塞
     * @param   type 夹爪类型，0-平行夹爪；1-旋转夹爪
     * @param   rotNum 旋转圈数
     * @param   rotVel 旋转速度百分比[0-100]
     * @param   rotTorque 旋转力矩百分比[0-100]
     * @return  错误码
     */
    public int MoveGripper(int index, int pos, int vel, int force, int max_time, int block, int type, double rotNum, int rotVel, int rotTorque)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {index, pos, vel, force, max_time, block,type,rotNum,rotVel,rotTorque};
            int rtn = (int)client.execute("MoveGripper" , params);
            if (log != null)
            {
                log.LogInfo("MoveGripper(" + index + "," + pos + "," + vel + "," + force + "," + max_time + "," + block + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取夹爪运动状态
     * @return  List[0]:错误码; List[1] : fault  0-无错误，1-有错误; List[2]: staus  0-运动未完成，1-运动完成
     */
    public List<Integer> GetGripperMotionDone()
    {
        List<Integer> rtnArray = new ArrayList<Integer>() {};
        rtnArray.add(0);
        rtnArray.add(1);
        rtnArray.add(1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetGripperMotionDone" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (int)result[1]);
                rtnArray.set(2, (int)result[2]);
            }
            if (log != null)
            {
                log.LogInfo("GetGripperMotionDone( " + (int)result[1] + ", " + (int)result[2] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  获取夹爪激活状态
     * @return  List[0]:错误码; List[1] : fault  0-无错误，1-有错误; List[2]: status  bit0~bit15对应夹爪编号0~15，bit=0为未激活，bit=1为激活
     */
    public List<Number> GetGripperActivateStatus()
    {
        List<Number> rtnArray =new ArrayList<>();
        rtnArray.add(0);
        rtnArray.add(1);
        rtnArray.add(1);

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            rtnArray.set(1,robotStateRoutineThread.pkg.gripper_fault);
            rtnArray.set(2,robotStateRoutineThread.pkg.gripper_active);
        }
        else
        {
            rtnArray.set(0,sockErr);
        }
        if (log != null)
        {
            log.LogInfo("GetGripperActivateStatus(" + rtnArray.get(1)+"," + rtnArray.get(2)+"):" +rtnArray.get(0));
        }

        return rtnArray;
    }

    /**
     * @brief  获取夹爪位置
     * @return  List[0]:错误码; List[1] : fault  0-无错误，1-有错误; List[2]: position  位置百分比，范围0~100%
     */
    public List<Number> GetGripperCurPosition()
    {
        List<Number> rtnArray =new ArrayList<>();
        rtnArray.add(0);
        rtnArray.add(1);
        rtnArray.add(1);

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            rtnArray.set(1,robotStateRoutineThread.pkg.gripper_fault);
            rtnArray.set(2,robotStateRoutineThread.pkg.gripper_position);
        }
        else
        {
            rtnArray.set(0,sockErr);
        }

        if (log != null)
        {
            log.LogInfo("GetGripperCurPosition(" + rtnArray.get(1) + "," + rtnArray.get(2) + ") : " + rtnArray.get(0));
        }

        return rtnArray;
    }

    /**
     * @brief  获取夹爪速度
     * @return  List[0]:错误码; List[1] : fault  0-无错误，1-有错误; List[2]: speed  速度百分比，范围0~100%
     */
    public List<Number> GetGripperCurSpeed()
    {
        List<Number> rtnArray =new ArrayList<>();
        rtnArray.add(0);
        rtnArray.add(1);
        rtnArray.add(1);

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            rtnArray.set(1, robotStateRoutineThread.pkg.gripper_fault);
            rtnArray.set(2, robotStateRoutineThread.pkg.gripper_speed);
        }
        else
        {
            rtnArray.set(0,sockErr);
        }
        if (log != null)
        {
            log.LogInfo("GetGripperCurSpeed(" + rtnArray.get(1) + "," + rtnArray.get(2) + ") : " + rtnArray.get(0));
        }
        return rtnArray;
    }

    /**
     * @brief  获取夹爪电流
     * @return  List[0]:错误码; List[1] : fault  0-无错误，1-有错误; List[2]: current  电流百分比，范围0~100%
     */
    public List<Number> GetGripperCurCurrent()
    {
        List<Number> rtnArray =new ArrayList<>();
        rtnArray.add(0);
        rtnArray.add(1);
        rtnArray.add(1);

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            rtnArray.set(1,robotStateRoutineThread.pkg.gripper_fault);
            rtnArray.set(2,robotStateRoutineThread.pkg.gripper_current);
        }
        else
        {
            rtnArray.set(0,sockErr);

        }
        if (log != null)
        {
            log.LogInfo("GetGripperCurCurrent(" + rtnArray.get(1) + "," + rtnArray.get(2) + ") : " + rtnArray.get(0));
        }

        return rtnArray;
    }

    /**
     * @brief  获取夹爪电压
     * @return  List[0]:错误码; List[1] : fault  0-无错误，1-有错误; List[2]:voltage  电压,单位0.1V
     */
    public List<Number> GetGripperVoltage()
    {
        List<Number> rtnArray =new ArrayList<>();
        rtnArray.add(0);
        rtnArray.add(1);
        rtnArray.add(1);
        if (sockErr == RobotError.ERR_SUCCESS)
        {
            rtnArray.set(1,robotStateRoutineThread.pkg.gripper_fault);
            rtnArray.set(2,robotStateRoutineThread.pkg.gripper_voltage);
        }
        else
        {
            rtnArray.set(0,sockErr);
        }
        if (log != null)
        {
            log.LogInfo("GetGripperVoltage(" + rtnArray.get(1) + "," + rtnArray.get(2) + ") : " + rtnArray.get(0));
        }


        return rtnArray;
    }

    /**
     * @brief  获取夹爪温度
     * @return  List[0]:错误码; List[1] : fault  0-无错误，1-有错误; List[2]:temp  温度，单位℃
     */
    public List<Number> GetGripperTemp()
    {
        List<Number> rtnArray =new ArrayList<>();
        rtnArray.add(0);
        rtnArray.add(1);
        rtnArray.add(1);
        if (sockErr == RobotError.ERR_SUCCESS)
        {
            rtnArray.set(1,robotStateRoutineThread.pkg.gripper_fault);
            rtnArray.set(2, robotStateRoutineThread.pkg.gripper_tmp);
        }
        else
        {
            rtnArray.set(0,sockErr);
        }
        if (log != null)
        {
            log.LogInfo("GetGripperTemp(" + rtnArray.get(1) + "," + rtnArray.get(2) + ") : " + rtnArray.get(0));
        }
        return rtnArray;
    }

    /**
     * @brief  计算预抓取点-视觉
     * @param  desc_pos  抓取点笛卡尔位姿
     * @param  zlength   z轴偏移量
     * @param  zangle    绕z轴旋转偏移量
     * @param  pre_pos  获取点
     * @return  错误码
     */
    public int ComputePrePick(DescPose desc_pos, double zlength, double zangle, DescPose pre_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] descPos = { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };
            Object[] params = new Object[] {descPos, zlength, zangle};
            Object[] result = (Object[])client.execute("ComputePrePick" , params);
            if ((int)result[0] == 0)
            {
                pre_pos.tran.x = (double)result[1];
                pre_pos.tran.y = (double)result[2];
                pre_pos.tran.z = (double)result[3];
                pre_pos.rpy.rx = (double)result[4];
                pre_pos.rpy.ry = (double)result[5];
                pre_pos.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("ComputePrePick(" + descPos[0] + "," + descPos[1] + "," + descPos[2] + "," + descPos[3] + "," + descPos[4] + "," + descPos[5] + "," + zlength + "," + zangle + ",  " + pre_pos.tran.x + "," + pre_pos.tran.y + "," + pre_pos.tran.z + "," + pre_pos.rpy.rx + "," + pre_pos.rpy.ry + "," + pre_pos.rpy.rz + ",) : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  计算撤退点-视觉
     * @param  desc_pos  抓取点笛卡尔位姿
     * @param  zlength   z轴偏移量
     * @param  zangle    绕z轴旋转偏移量
     * @param  post_pos 撤退点
     * @return  错误码
     */
    public int ComputePostPick(DescPose desc_pos, double zlength, double zangle, DescPose post_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] descPos = { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };
            Object[] params = new Object[] {descPos, zlength, zangle};
            Object[] result = (Object[])client.execute("ComputePostPick" , params);
            if ((int)result[0] == 0)
            {
                post_pos.tran.x = (double)result[1];
                post_pos.tran.y = (double)result[2];
                post_pos.tran.z = (double)result[3];
                post_pos.rpy.rx = (double)result[4];
                post_pos.rpy.ry = (double)result[5];
                post_pos.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("ComputePostPick(" + descPos[0] + "," + descPos[1] + "," + descPos[2] + "," + descPos[3] + "," + descPos[4] + "," + descPos[5] + "," + zlength + "," + zangle + ",  " + post_pos.tran.x + "," + post_pos.tran.y + "," + post_pos.tran.z + "," + post_pos.rpy.rx + "," + post_pos.rpy.ry + "," + post_pos.rpy.rz + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  配置力传感器
     * @param  config company:力传感器厂商，17-坤维科技，19-航天十一院，20-ATI传感器，21-中科米点，22-伟航敏芯，23-NBIT，24-鑫精诚(XJC)，26-NSR
     * @param  config device: 设备号，坤维(0-KWR75B)，航天十一院(0-MCS6A-200-4)，ATI(0-AXIA80-M8)，中科米点(0-MST2010)，伟航敏芯(0-WHC6L-YB-10A)，NBIT(0-XLH93003ACS)，鑫精诚XJC(0-XJC-6F-D82)，NSR(0-NSR-FTSensorA)
     * @param  config softvesion:软件版本号，暂不使用，默认为0
     * @param  config bus:设备挂在末端总线位置，暂不使用，默认为0
     * @return  错误码
     */
    public int FT_SetConfig(DeviceConfig config)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {config.company, config.device, config.softwareVersion, config.bus};
            int rtn = (int)client.execute("FT_SetConfig" , params);
            if (log != null)
            {
                log.LogInfo("FT_SetConfig(" + config.company + "," + config.device + "," + config.softwareVersion + "," + config.bus + ") : " + rtn);
            }

            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取力传感器配置
     * @return  List[0]:错误码; List[1] : deviceID 力传感器编号; List[2]:company  力传感器厂商，待定;
     * List[3]:device  设备号，暂不使用，默认为0; List[4]: softvesion  软件版本号，暂不使用，默认为0
     */
    public int FT_GetConfig(DeviceConfig config)
    {

        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("FT_GetConfig" , params);
            if ((int)result[0] == 0)
            {
                config.device = (int)result[1];
                config.company = (int)result[2];
                config.softwareVersion = (int)result[3];
                config.bus = (int)result[4];
            }
            if (log != null)
            {
                log.LogInfo("FT_GetConfig(" + config.company + "," + config.device + "," + config.softwareVersion + "," + config.bus + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return (int)RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  力传感器激活
     * @param  act  0-复位，1-激活
     * @return  错误码
     */
    public int FT_Activate(int act)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {act};
            int rtn = (int)client.execute("FT_Activate" , params);
            if (log != null)
            {
                log.LogInfo("FT_Activate(" + act + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  力传感器校零
     * @param  act  0-去除零点，1-零点矫正
     * @return  错误码
     */
    public int FT_SetZero(int act)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {act};
            int rtn = (int)client.execute("FT_SetZero" , params);
            if (log != null)
            {
                log.LogInfo("FT_SetZero(" + act + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  设置力传感器参考坐标系
     * @param  type  0-工具坐标系，1-基坐标系, 2-自由坐标系
     * @param  coord  自由坐标系值
     * @return  错误码
     */
    public int FT_SetRCS(int type, DescPose coord)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] coords = {coord.tran.x, coord.tran.y, coord.tran.z, coord.rpy.rx, coord.rpy.ry, coord.rpy.rz};
            Object[] params = new Object[] {type, coords};
            int rtn = (int)client.execute("FT_SetRCS" , params);
            if (log != null)
            {
                log.LogInfo("FT_SetRCS(" + type + "," + coord.tran.x + "," + coord.tran.y + "," + coord.tran.z + "," + coord.rpy.rx + "," + coord.rpy.ry + "," + coord.rpy.rz + "," + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief  负载重量辨识记录
     * @param  id  传感器坐标系编号，范围[1~14]
     * @return  错误码
     */
    public int FT_PdIdenRecord(int id)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {id};
            int rtn = (int)client.execute("FT_PdIdenRecord" , params);
            if (log != null)
            {
                log.LogInfo("FT_PdIdenRecord(" + id + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  负载重量辨识计算
     * @return  List[0]:错误码; List[1] : double weight  负载重量，单位kg
     */
    public List<Number> FT_PdIdenCompute()
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("FT_PdIdenCompute" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (double)result[1]);
            }

            if (log != null)
            {
                log.LogInfo("FT_PdIdenCompute(" + (double)result[1] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }

    }

    /**
     * @brief  负载质心辨识记录
     * @param  id  传感器坐标系编号，范围[1~14]
     * @param  index 点编号，范围[1~3]
     * @return  错误码
     */
    public int FT_PdCogIdenRecord(int id, int index)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {id, index};
            int rtn = (int)client.execute("FT_PdCogIdenRecord" , params);
            if (log != null)
            {
                log.LogInfo("FT_PdCogIdenRecord(" + id + "," + index + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  负载质心辨识计算
     * @param  cog  负载质心，单位mm
     * @return  错误码
     */
    public int FT_PdCogIdenCompute(DescTran cog)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("FT_PdCogIdenCompute" , params);
            if ((int)result[0] == 0)
            {
                cog.x = (double)result[1];
                cog.y = (double)result[2];
                cog.z = (double)result[3];
            }
            if (log != null)
            {
                log.LogInfo("FT_PdCogIdenCompute(" + cog.x + "," + cog.y + "," + cog.z + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief  获取参考坐标系下力/扭矩数据
     * @param  flag 0-阻塞，1-非阻塞
     * @param  ft  力/扭矩，fx,fy,fz,tx,ty,tz
     * @return  错误码
     */
    public int FT_GetForceTorqueRCS(int flag, ForceTorque ft)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            ft.fx = robotStateRoutineThread.pkg.ft_sensor_data[0];
            ft.fy = robotStateRoutineThread.pkg.ft_sensor_data[1];
            ft.fz = robotStateRoutineThread.pkg.ft_sensor_data[2];
            ft.tx = robotStateRoutineThread.pkg.ft_sensor_data[3];
            ft.ty = robotStateRoutineThread.pkg.ft_sensor_data[4];
            ft.tz = robotStateRoutineThread.pkg.ft_sensor_data[5];
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("FT_GetForceTorqueRCS(" + flag + "," + ft.fx + "," + ft.fy + "," + ft.fz + "," + ft.tx + "," + ft.ty + "," + ft.tz + ") : " + errcode);
        }

        return errcode;
    }

    /**
     * @brief  获取力传感器原始力/扭矩数据
     * @param  flag 0-阻塞，1-非阻塞
     * @param  ft  力/扭矩，fx,fy,fz,tx,ty,tz
     * @return  错误码
     */
    public int FT_GetForceTorqueOrigin(int flag, ForceTorque ft)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            ft.fx = robotStateRoutineThread.pkg.ft_sensor_raw_data[0];
            ft.fy = robotStateRoutineThread.pkg.ft_sensor_raw_data[1];
            ft.fz = robotStateRoutineThread.pkg.ft_sensor_raw_data[2];
            ft.tx = robotStateRoutineThread.pkg.ft_sensor_raw_data[3];
            ft.ty = robotStateRoutineThread.pkg.ft_sensor_raw_data[4];
            ft.tz = robotStateRoutineThread.pkg.ft_sensor_raw_data[5];
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("FT_GetForceTorqueOrigin(" + flag + "," + ft.fx + "," + ft.fy + "," + ft.fz + "," + ft.tx + "," + ft.ty + "," + ft.tz + ") : " + errcode);
        }

        return errcode;
    }

    /**
     * @brief  碰撞守护
     * @param  flag 0-关闭碰撞守护，1-开启碰撞守护
     * @param  sensor_id 力传感器编号
     * @param  select  选择六个自由度是否检测碰撞，0-不检测，1-检测
     * @param  ft  碰撞力/扭矩，fx,fy,fz,tx,ty,tz
     * @param  max_threshold 最大阈值
     * @param  min_threshold 最小阈值
     * @note   力/扭矩检测范围：(ft-min_threshold, ft+max_threshold)
     * @return  错误码
     */
    public int FT_Guard(int flag, int sensor_id, Object[] select, ForceTorque ft, Object[] max_threshold, Object[] min_threshold)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {

            Object[] ftData = { ft.fx, ft.fy, ft.fz, ft.tx, ft.ty, ft.tz };
            Object[] params = new Object[] {flag, sensor_id, select, ftData, max_threshold, min_threshold};
            int rtn = (int)client.execute("FT_Guard" , params);
            if (log != null)
            {
                log.LogInfo("FT_Guard(" + flag + "," + sensor_id + "," + flag + "," + select[0] + "," + select[1] + "," + select[2] + "," + select[3] + "," + select[4] + "," + select[5] + "," + ft.fx + "," + ft.fy + "," + ft.fz + "," + ft.tx + "," + ft.ty + "," + ft.tz + "," +
                        max_threshold[0] + "," + max_threshold[1] + "," + max_threshold[2] + "," + max_threshold[3] + "," + max_threshold[4] + "," + max_threshold[5] + "," + min_threshold[0] + "," + min_threshold[1] + "," + min_threshold[2] + "," + min_threshold[3] + "," + min_threshold[4] + "," + min_threshold[5] + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  恒力控制
     * @param  flag 0-关闭恒力控制，1-开启恒力控制
     * @param  sensor_id 力传感器编号
     * @param  select  选择六个自由度是否检测碰撞，0-不检测，1-检测
     * @param  ft  碰撞力/扭矩，fx,fy,fz,tx,ty,tz
     * @param  ft_pid 力pid参数，力矩pid参数
     * @param  adj_sign 自适应启停控制，0-关闭，1-开启
     * @param  ILC_sign ILC启停控制， 0-停止，1-训练，2-实操
     * @param  max_dis 最大调整距离，单位mm
     * @param  max_ang 最大调整角度，单位deg
     * @param  filter_Sign 滤波开启标志 0-关；1-开，默认关闭
     * @param  posAdapt_sign 姿态顺应开启标志 0-关；1-开，默认关闭
     * @param  isNoBlock 阻塞标志，0-阻塞；1-非阻塞
     * @return  错误码
     */
//    public int FT_Control(int flag, int sensor_id, Object[] select, ForceTorque ft, Object[] ft_pid, int adj_sign, int ILC_sign, double max_dis, double max_ang, int filter_Sign, int posAdapt_sign, int isNoBlock)
//    {
//        if (IsSockComError())
//        {
//            return sockErr;
//        }
//
//        try
//        {
//
//            Object[] ftData = { ft.fx, ft.fy, ft.fz, ft.tx, ft.ty, ft.tz };
//            Object[] params = new Object[] {flag, sensor_id, select, ftData, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, filter_Sign, posAdapt_sign, isNoBlock};
//            int rtn = (int)client.execute("FT_Control" , params);
//            if (log != null)
//            {
//                log.LogInfo("FT_Control(" + flag + "," + sensor_id + "," + flag + "," + select[0] + "," + select[1] + "," + select[2] + "," + select[3] + "," + select[4] + "," + select[5] + "," + ft.fx + "," + ft.fy + "," + ft.fz + "," + ft.tx + "," + ft.ty + "," + ft.tz + "," +
//                        ft_pid[0] + "," + ft_pid[1] + "," + ft_pid[2] + "," + ft_pid[3] + "," + ft_pid[4] + "," + ft_pid[5] + "," + adj_sign + "," + ILC_sign + "," + max_dis + "," + max_ang + "," + filter_Sign + "," + posAdapt_sign + "," + isNoBlock + ") : " + rtn);
//            }
//            return rtn;
//        }
//        catch (Throwable e)
//        {
//            if (log != null)
//            {
//                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
//            }
//            return RobotError.ERR_RPC_ERROR;
//        }
//    }

    /**
     * @brief  恒力控制
     * @param  flag 0-关闭恒力控制，1-开启恒力控制
     * @param  sensor_id 力传感器编号
     * @param  select  选择六个自由度是否检测碰撞，0-不检测，1-检测
     * @param  ft  碰撞力/扭矩，fx,fy,fz,tx,ty,tz
     * @param  ft_pid 力pid参数，力矩pid参数
     * @param  adj_sign 自适应启停控制，0-关闭，1-开启
     * @param  ILC_sign ILC启停控制， 0-停止，1-训练，2-实操
     * @param  max_dis 最大调整距离，单位mm
     * @param  max_ang 最大调整角度，单位deg
     * @param  M 质量参数
     * @param  B 阻尼参数
     * @param  polishRadio 打磨半径，单位mm
     * @param  filter_Sign 滤波开启标志 0-关；1-开，默认关闭
     * @param  posAdapt_sign 姿态顺应开启标志 0-关；1-开，默认关闭
     * @param  isNoBlock 阻塞标志，0-阻塞；1-非阻塞
     * @return  错误码
     */
    public int FT_Control(int flag, int sensor_id, int[] select, ForceTorque ft, double[] ft_pid, int adj_sign, int ILC_sign, double max_dis, double max_ang,double[] M,double[] B, double polishRadio,int filter_Sign, int posAdapt_sign, int isNoBlock)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        if (GetSafetyCode() != 0) {
            return GetSafetyCode();
        }
        try {
            Object[] selectData={select[0],select[1],select[2],select[3],select[4],select[5]};
            Object[] ft_pidData={ft_pid[0],ft_pid[1],ft_pid[2],ft_pid[3],ft_pid[4],ft_pid[5]};
            Object[] ftData = {ft.fx, ft.fy, ft.fz, ft.tx, ft.ty, ft.tz};
            Object[] MB={M[0],M[1],B[0],B[1]};
            Object[] params = new Object[]{flag, sensor_id, selectData, ftData, ft_pidData, adj_sign, ILC_sign, max_dis, max_ang,polishRadio, filter_Sign, posAdapt_sign,MB, isNoBlock};
            int rtn = (int) client.execute("FT_Control", params);
            if (log != null) {
                log.LogInfo("FT_Control(" + flag + "," + sensor_id + "," + flag + "," + select[0] + "," + select[1] + "," + select[2] + "," + select[3] + "," + select[4] + "," + select[5] + "," + ft.fx + "," + ft.fy + "," + ft.fz + "," + ft.tx + "," + ft.ty + "," + ft.tz + "," +
                        ft_pid[0] + "," + ft_pid[1] + "," + ft_pid[2] + "," + ft_pid[3] + "," + ft_pid[4] + "," + ft_pid[5] + "," + adj_sign + "," + ILC_sign + "," + max_dis + "," + max_ang + "," + filter_Sign + "," + posAdapt_sign + "," + isNoBlock + ") : " + rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief  恒力控制
     * @param  flag 0-关闭恒力控制，1-开启恒力控制
     * @param  sensor_id 力传感器编号
     * @param  select  选择六个自由度是否检测碰撞，0-不检测，1-检测
     * @param  ft  碰撞力/扭矩，fx,fy,fz,tx,ty,tz
     * @param  ft_pid 力pid参数，力矩pid参数
     * @param  adj_sign 自适应启停控制，0-关闭，1-开启
     * @param  ILC_sign ILC启停控制， 0-停止，1-训练，2-实操
     * @param  max_dis 最大调整距离，单位mm
     * @param  max_ang 最大调整角度，单位deg
     * @param  filter_Sign 滤波开启标志 0-关；1-开，默认关闭
     * @param  posAdapt_sign 姿态顺应开启标志 0-关；1-开，默认关闭
     * @param  isNoBlock 阻塞标志，0-阻塞；1-非阻塞
     * @return  错误码
     */
    public int FT_Control(int flag, int sensor_id, int[] select, ForceTorque ft, double[] ft_pid, int adj_sign, int ILC_sign, double max_dis, double max_ang,int filter_Sign, int posAdapt_sign, int isNoBlock)
    {
        double[] M = { 0.0 };
        double[] B = { 0.0 };
        return FT_Control(flag, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, M, B, 0.0, filter_Sign, posAdapt_sign, isNoBlock);
    }

    /**
     * @brief  柔顺控制开启
     * @param  p 位置调节系数或柔顺系数
     * @param  force 柔顺开启力阈值，单位N
     * @return  错误码
     */
    public int FT_ComplianceStart(double p, double force)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {p, force};
            int rtn = (int)client.execute("FT_ComplianceStart" , params);
            if (log != null)
            {
                log.LogInfo("FT_ComplianceStart(" + p + "," + force + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  柔顺控制关闭
     * @return  错误码
     */
    public int FT_ComplianceStop()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("FT_ComplianceStop" , params);
            if (log != null)
            {
                log.LogInfo("FT_ComplianceStop() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 负载辨识初始化
     * @return 错误码
     */
    public int LoadIdentifyDynFilterInit()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("LoadIdentifyDynFilterInit" , params);
            if (log != null)
            {
                log.LogInfo("LoadIdentifyDynFilterInit() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 负载辨识变量初始化
     * @return 错误码
     */
    public int LoadIdentifyDynVarInit()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("LoadIdentifyDynVarInit" , params);
            if (log != null)
            {
                log.LogInfo("LoadIdentifyDynVarInit() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 负载辨识主程序
     * @param joint_torque 关节扭矩
     * @param joint_pos 关节位置
     * @param t 采样周期
     * @return 错误码
     */
    public int LoadIdentifyMain(Object[] joint_torque, Object[] joint_pos, double t)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {joint_torque, joint_pos, t};
            int rtn = (int)client.execute("LoadIdentifyMain" , params);
            if (log != null)
            {
                log.LogInfo("LoadIdentifyMain(" + joint_torque[0] + "," + joint_torque[1] + "," + joint_torque[2] + "," + joint_torque[3] + "," + joint_torque[4] + "," + joint_torque[5] + "," + joint_pos[0] + "," + joint_pos[1] + "," + joint_pos[2] + "," + joint_pos[3] + "," + joint_pos[4] + "," + joint_pos[5] + "," + t + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 获取负载辨识结果
     * @param gain
     * @return List[0]:错误码; List[1] : double weight 负载重量; List[2]: x 负载质心X mm
     */
    public List<Number> LoadIdentifyGetResult(Object[] gain)
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(-1);
        rtnArray.add(0.0);
        rtnArray.add(0.0);
        rtnArray.add(0.0);
        rtnArray.add(0.0);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {gain};
            Object[] result = (Object[])client.execute("LoadIdentifyGetResult" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (double)result[1]);
                rtnArray.set(2, (double)result[2]);
                rtnArray.set(3, (double)result[3]);
                rtnArray.set(4, (double)result[4]);
            }
            if (log != null)
            {
                log.LogInfo("LoadIdentifyGetResult(" + (double)result[1] + "," + (double)result[2] + "," + (double)result[3] + "," + (double)result[4] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }


    /**
     * @brief 传动带启动、停止
     * @param status 状态，1-启动，0-停止
     * @return 错误码
     */
    public int ConveyorStartEnd(int status)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {status};
            int rtn = (int)client.execute("ConveyorStartEnd" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorStartEnd(" + status + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief 记录IO检测点
     * @return 错误码
     */
    public int ConveyorPointIORecord()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ConveyorPointIORecord" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorPointIORecord() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }

            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief 记录A点
     * @return 错误码
     */
    public int ConveyorPointARecord()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ConveyorPointARecord" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorPointARecord() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 记录参考点
     * @return 错误码
     */
    public int ConveyorRefPointRecord()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ConveyorRefPointRecord" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorRefPointRecord() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 记录B点
     * @return 错误码
     */
    public int ConveyorPointBRecord()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ConveyorPointBRecord" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorPointBRecord() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 传送带工件IO检测
     * @param max_t 最大检测时间，单位ms
     * @return 错误码
     */
    public int ConveyorIODetect(int max_t)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {max_t};
            int rtn = (int)client.execute("ConveyorIODetect" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorIODetect(" + max_t + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取物体当前位置
     * @param mode 1-跟踪抓取，2-跟踪运动，3-TPD跟踪
     * @return 错误码
     */
    public int ConveyorGetTrackData(int mode)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {mode};
            int rtn = (int)client.execute("ConveyorGetTrackData" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorGetTrackData(" + mode + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 传动带跟踪开始
     * @param status 状态，1-启动，0-停止
     * @return 错误码
     */
    public int ConveyorTrackStart(int status)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {status};
            int rtn = (int)client.execute("ConveyorTrackStart" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorTrackStart(" + status + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 传动带跟踪停止
     * @return 错误码
     */
    public int ConveyorTrackEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ConveyorTrackEnd" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorTrackEnd() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 传动带参数配置
     * @param encChannel 编码器通道 1~2
     * @param resolution 编码器转一圈的脉冲数
     * @param lead 编码器转一圈传送带行走距离
     * @param wpAxis 工件坐标系编号 针对跟踪运动功能选择工件坐标系编号，跟踪抓取、TPD跟踪设为0
     * @param vision 是否配视觉  0 不配  1 配
     * @param speedRadio 速度比  针对传送带跟踪抓取选项（1-100）  其他选项默认为1
     * @param followType 跟踪运动类型，0-跟踪运动；1-追检运动
     * @param startDis 追检抓取需要设置， 跟踪起始距离， -1：自动计算(工件到达机器人下方后自动追检)，单位mm， 默认值0
     * @param endDis 追检抓取需要设置，跟踪终止距离， 单位mm， 默认值100
     * @return 错误码
     */
    public int ConveyorSetParam(int encChannel, int resolution, double lead, int wpAxis, int vision, double speedRadio, int followType, int startDis, int endDis)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] param = { encChannel, resolution, lead, wpAxis, vision, speedRadio, followType, startDis, endDis};
            Object[] params = new Object[] {param};
            int rtn = (int)client.execute("ConveyorSetParam" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorSetParam(" + encChannel + "," + resolution + ")," + lead + "," + wpAxis + "," + vision + "," + speedRadio + " : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置传动带抓取点补偿
     * @param cmp 补偿位置 double[3]{x, y, z}
     * @return 错误码
     */
    public int ConveyorPointComp(Object[] cmp)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {cmp};
            int rtn = (int)client.execute("ConveyorPointComp" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorPointComp(" + cmp + " : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置传动带抓取点补偿
     * @param cmp 补偿位置 double[3]{x, y, z}
     * @return 错误码
     */
    public int ConveyorCatchPointComp(Object[] cmp)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {cmp};
            int rtn = (int)client.execute("ConveyorCatchPointComp" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorCatchPointComp(" + Arrays.toString(cmp) + " ): " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 直线运动
     * @param name 运动点描述
     * @param tool 工具坐标号，范围[0~14]
     * @param wobj 工件坐标号，范围[0~14]
     * @param vel 速度百分比，范围[0~100]
     * @param acc 加速度百分比，范围[0~100],暂不开放
     * @param ovl 速度缩放因子，范围[0~100]
     * @param blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @return 错误码
     */
    public int ConveyorTrackMoveL(String name, int tool, int wobj, double vel, double acc, double ovl, double blendR)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {name, tool, wobj, vel, acc, ovl, blendR, 0, 0};
            int rtn = (int)client.execute("ConveyorTrackMoveL" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorTrackMoveL(" + name + "," + tool + "," + wobj + "," + vel + "," + acc + "," + ovl + "," + blendR + " : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief 获取SSH公钥
     * @param keygen 公钥
     * @return 错误码
     */
    public int GetSSHKeygen(String[] keygen)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetSSHKeygen" , params);
            if ((int)result[0] == 0)
            {
                keygen[0] = (String)result[1];
            }
            if (log != null)
            {
                log.LogInfo("GetSSHKeygen(" + Arrays.toString(keygen) + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 计算指定路径下文件的MD5值
     * @param file_path 文件路径包含文件名，默认Traj文件夹路径为:"/fruser/traj/",如"/fruser/traj/trajHelix_aima_1.txt"
     * @param md5 文件MD5值
     * @return 错误码
     */
    public int ComputeFileMD5(String file_path, String[] md5)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {file_path};
            Object[] result = (Object[])client.execute("ComputeFileMD5" , params);
            if ((int)result[0] == 0)
            {
                md5[0] = (String)result[1];
            }
            if (log != null)
            {
                log.LogInfo("ComputeFileMD5(" + file_path + ",  " + md5[0] + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief 获取机器人急停状态
     * @param state 急停状态，0-非急停，1-急停
     * @return 错误码
     */
    public int GetRobotEmergencyStopState(int[] state)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            state[0] = robotStateRoutineThread.pkg.EmergencyStop;
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetRobotEmergencyStopState(" + state + ") : " + errcode);
        }
        return errcode;
    }

    /**
     * @brief 获取SDK与机器人的通讯状态
     * @return state 通讯状态，0-通讯正常，1-通讯异常
     */
    public int GetSDKComState()
    {
        int state = -1;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            state = 0;
        }
        else if (sockErr == (int)RobotError.ERR_SOCKET_COM_FAILED)
        {
            state = 1;
        }
        if (log != null)
        {
            log.LogInfo("GetSDKComState(" + state + ") : " + state);
        }
        return state;
    }


    /**
     * @brief 获取安全停止信号
     * @param  si0_state 安全停止信号SI0，0-无效，1-有效
     * @param  si1_state 安全停止信号SI1，0-无效，1-有效
     */
    public int GetSafetyStopState(int[] si0_state, int[] si1_state)
    {
        int errcode = 0;

        if (sockErr == RobotError.ERR_SUCCESS)
        {
            si0_state[0] = robotStateRoutineThread.pkg.safety_stop0_state;
            si1_state[0] = robotStateRoutineThread.pkg.safety_stop1_state;
        }
        else
        {
            errcode = sockErr;
        }
        if (log != null)
        {
            log.LogInfo("GetSafetyStopState(" + si0_state[0] + ",  " + si1_state[0] + ") :"+errcode);
        }
        return errcode;
    }

    /**
     * @brief 获取机器人DH参数补偿值
     * @param dhCompensation 机器人DH参数补偿值(mm) [cmpstD1,cmpstA2,cmpstA3,cmpstD4,cmpstD5,cmpstD6]
     * @return 错误码
     */
    public int GetDHCompensation(Object[] dhCompensation)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetDHCompensation" , params);
            if ((int)result[0] == 0)
            {
                dhCompensation[0] = (double)result[1];
                dhCompensation[1] = (double)result[2];
                dhCompensation[2] = (double)result[3];
                dhCompensation[3] = (double)result[4];
                dhCompensation[4] = (double)result[5];
                dhCompensation[5] = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("GetDHCompensation(" + dhCompensation[0] + "," + dhCompensation[1] + "," + dhCompensation[2] + "," + dhCompensation[3] + "," + dhCompensation[4] + "," + dhCompensation[5] + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 下载点位表数据库
     * @param pointTableName 要下载的点位表名称    pointTable1.db
     * @param saveFilePath 下载点位表的存储路径   C://test/
     * @return 错误码
     */
    public int PointTableDownLoad(String pointTableName, String saveFilePath)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            File savePathF =new File(saveFilePath);
            //判断保存的文件路径是否存在
            if (!savePathF.exists())
            {
                if (log != null) {
                    log.LogInfo("point table down load save path not found");
                }
                return (int)RobotError.ERR_SAVE_FILE_PATH_NOT_FOUND;
            }
            Object[] params = new Object[] {pointTableName};
            int rtn = (int)client.execute("PointTableDownload" , params);
            if (rtn == -1)
            {
                return (int)RobotError.ERR_POINTTABLE_NOTFOUND;
            }
            else if (rtn != 0)
            {
                return rtn;
            }

            TCPClient clientPointTable = new TCPClient(robotIp, 20011);

            boolean connRtn = clientPointTable.Connect();
            if (!connRtn)
            {
                if (log != null) {
                    log.LogInfo("point table dowmload connect failed");
                }
                clientPointTable.Close();
                return (int)RobotError.ERR_OTHER;
            }
            byte[] totalbuffer = new byte[1024 * 1024 * 50];//50Mb
            int totalSize = 0;
            String recvMd5 = "";
            int recvSize = 0;
            boolean findHeadFlag = false;

            while (true)
            {
                byte[] buffer = new byte[1024 * 1024 * 50];
                int num = clientPointTable.Recv(buffer);
                if (num < 1)
                {
                    if (log != null) {
                        log.LogInfo("point table dowmload recv failed");
                    }
                    return (int)RobotError.ERR_OTHER;
                }

                System.arraycopy(buffer, 0, totalbuffer, totalSize, num);

                totalSize += num;

                String head = new String(subByte(totalbuffer, 0, 4));

                if (!findHeadFlag && totalSize > 4 && head.equals("/f/b"))
                {
                    findHeadFlag = true;
                }

                if (findHeadFlag && totalSize > 12 + 32)
                {
                    String sizeString = new String(subByte(totalbuffer, 4, 8));
                    recvSize = Integer.parseInt(sizeString);
                    recvMd5 = new String(subByte(totalbuffer, 12, 32));
                }

                if (findHeadFlag && totalSize == recvSize)
                {
                    break;
                }
            }

            byte[] fileBuffer = subByte(totalbuffer, 12 + 32, totalSize - 16 - 32);

            File file = new File(saveFilePath + pointTableName);
            if (!file.exists())
                file.createNewFile();

            FileOutputStream out = new FileOutputStream(saveFilePath + pointTableName);
            for (byte b : fileBuffer) {
                out.write(b);
            }
            out.close();

            String checkMd5 = computeMD5(saveFilePath + pointTableName).toLowerCase();
            if (checkMd5.equals(recvMd5))
            {
                clientPointTable.Send("SUCCESS");
                if (log != null) {
                    log.LogInfo("PointTableDownLoad(" + pointTableName + ", " + saveFilePath + " ) : " + "Success");
                }
                return 0;
            }
            else
            {
                clientPointTable.Send("FAIL");
                file.delete();
                if (log != null)
                {
                    log.LogInfo("PointTableDownLoad(" + pointTableName +  ", " + saveFilePath + " + ) : Fail");
                }
                return (int)RobotError.ERR_OTHER;
            }
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 上传点位表数据库
     * @param pointTableFilePath 上传点位表的全路径名   C://test/pointTable1.db
     * @return 错误码
     */
    public int PointTableUpLoad(String pointTableFilePath)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            //判断上传文件是否存在
            File fileInfo = new File(pointTableFilePath);
            if (!fileInfo.exists())
            {
                return (int)RobotError.ERR_UPLOAD_FILE_NOT_FOUND;
            }

            int tmpsize =  GetFileSize(pointTableFilePath);
            int totalSize = GetFileSize(pointTableFilePath) + 16 + 32;
            if (totalSize > MAX_UPLOAD_FILE_SIZE)
            {
                System.out.println("Files larger than 2 MB are not supported!");
                return -1;
            }

            String pointTableName = fileInfo.getName();

            Object[] nameParams = new Object[]{pointTableName};
            int rtn = (int)client.execute("PointTableUpload" , nameParams);
            if (rtn != 0)
            {
                return rtn;
            }

            TCPClient clientUpload = new TCPClient(robotIp, 20010);
            boolean bRtn = clientUpload.Connect();
            if(!bRtn)
            {
                log.LogError("PointTable Upload Connect fail");
                clientUpload.Close();
                return (int)RobotError.ERR_OTHER;
            }

            String sendMd5 = computeMD5(pointTableFilePath).toLowerCase();

            int num = clientUpload.Send("/f/b" + String.format("%8d", totalSize) + sendMd5);
            if (num < 1)
            {
                return (int)RobotError.ERR_OTHER;
            }

            byte[] bytes = Files.readAllBytes(Paths.get(pointTableFilePath));

            clientUpload.Send(bytes);
            num = clientUpload.Send("/b/f");
            if (num < 1)
            {
                return (int)RobotError.ERR_OTHER;
            }

            byte[] resultBuf = new byte[1024];
            Sleep(300);
            num = clientUpload.Recv(resultBuf);
            if (num < 1)
            {
                if (log != null)
                {
                    log.LogDebug("get result failed!");
                }
                return (int)RobotError.ERR_OTHER;
            }
            if (log != null)
            {
                log.LogDebug("recv success!");
            }
            String resultStr = new String(resultBuf, StandardCharsets.UTF_8).substring(0, 7);
            if (resultStr.equals("SUCCESS"))
            {
                if (log != null)
                {
                    log.LogInfo("point table upLoad(" + pointTableFilePath + ") : success");
                }
                return RobotError.ERR_SUCCESS;
            }
            else
            {
                if (log != null)
                {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "upload get result fail!  :  " + resultStr);
                }
                if (log != null)
                {
                    log.LogInfo("point table UpLoad(" + pointTableFilePath + ") : fail");
                }
                return (int)RobotError.ERR_OTHER;
            }
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 切换点位表并应用
     * @param pointTableName 要切换的点位表名称   "pointTable1.db"
     * @param errorStr 切换点位表错误信息
     * @return 错误码
     */
    public int PointTableSwitch(String pointTableName, String errorStr)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {pointTableName};
            int rtn = (int)client.execute("PointTableSwitch" , params);//切换点位表
            if (rtn != 0)
            {
                if (rtn == (int)RobotError.ERR_POINTTABLE_NOTFOUND)
                {
                    errorStr = "PointTable not Found!";
                }
                else
                {
                    errorStr = "not defined error";
                }
            }
            else
            {
                Thread.sleep(500);
                errorStr = "success";
            }
            if (log != null)
            {
                log.LogInfo("PointTableSwitch(" + pointTableName + ") : " + errorStr );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 点位表更新lua文件
     * @param pointTableName 要切换的点位表名称   "pointTable1.db",当点位表为空，即""时，表示将lua程序更新为未应用点位表的初始程序
     * @param luaFileName 要更新的lua文件名称   "testPointTable.lua"
     * @param errorStr 切换点位表错误信息
     * @return 错误码
     */
    public int PointTableUpdateLua(String pointTableName, String luaFileName, String errorStr)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {pointTableName};
            int rtn = (int)client.execute("PointTableSwitch" , params);//切换点位表
            if (rtn != 0)
            {
                if (rtn == (int)RobotError.ERR_POINTTABLE_NOTFOUND)
                {
                    errorStr = "PointTable not Found!";
                }
                else
                {
                    errorStr = "not defined error";
                }
                if (log != null)
                {
                    log.LogInfo("PointTableSwitch(" + pointTableName + ") : " + errorStr );
                }
                return rtn;
            }

            Thread.sleep(300);//切换点位表与更新lua程序之间在控制器里时异步的，为了保证切换后后端确实收到切换后的点位表名称，所以在更新前加点延时
            Object[] updateParams = new Object[] {luaFileName};
            Object[] result = (Object[])client.execute("PointTableUpdateLua" , updateParams);
            errorStr = (String)result[1];
            if (errorStr == "")
            {
                errorStr = "fail to update lua,please inspect pointtable";
            }
            if (log != null)
            {
                log.LogInfo("PointTableUpdateLua(" + luaFileName + ") : " + errorStr );
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    private int[] subints(int[] src, int begin, int count)
    {
        int[] bs = new int[count];
        for (int i = begin; i < begin + count; i++)
        {
            bs[i - begin] = src[i];
        }
        return bs;
    }

    private String computeMD5(String filePath)
    {
        DigestInputStream din = null;
        try
        {
            File file = new File(filePath);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //第一个参数是一个输入流
            din = new DigestInputStream(new BufferedInputStream(new FileInputStream(file)), md5);

            byte[] b = new byte[1024];
            while (din.read(b) != -1);

            byte[] digest = md5.digest();

            StringBuilder result = new StringBuilder(DatatypeConverter.printHexBinary(digest));
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (din != null) {
                    din.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    private int GetFileSize(String sFullName)
    {
        long lSize = 0;
        File file = new File(sFullName);
        if (file.exists())
            lSize = file.length();
        return (int)lSize;
    }

    /**
     * @brief 获取机器人软件版本
     * @param robotModel 机器人型号
     * @param webVersion web版本
     * @param controllerVersion 控制器版本
     * @return 错误码
     */
    public int GetSoftwareVersion(String[] robotModel, String[] webVersion, String[] controllerVersion)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetSoftwareVersion" , params);
            if ((int)result[0] == 0)
            {
                robotModel[0] = (String)result[1];
                webVersion[0] = (String)result[2];
                controllerVersion[0] = (String)result[3];
            }

            if (log != null)
            {
                log.LogInfo("GetSoftwareVersion(" + robotModel[0] +  "," + webVersion[0] + "," + controllerVersion[0] + ") : " + (int)result[0] );
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取机器人硬件版本
     * @param ctrlBoxBoardVersion 控制箱载板硬件版本
     * @param driver1Version 驱动器1硬件版本
     * @param driver1Version 驱动器2硬件版本
     * @param driver1Version 驱动器3硬件版本
     * @param driver1Version 驱动器4硬件版本
     * @param driver1Version 驱动器5硬件版本
     * @param driver1Version 驱动器6硬件版本
     * @param endBoardVersion 末端板硬件版本
     * @return 错误码
     */
    public int GetHardwareVersion(String[] ctrlBoxBoardVersion, String[] driver1Version, String[] driver2Version, String[] driver3Version,
                                  String[] driver4Version, String[] driver5Version, String[] driver6Version, String[] endBoardVersion)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetSlaveHardVersion" , params);
            if ((int)result[0] == 0)
            {
                ctrlBoxBoardVersion[0] = (String)result[1];
                driver1Version[0] = (String)result[2];
                driver2Version[0] = (String)result[3];
                driver3Version[0] = (String)result[4];
                driver4Version[0] = (String)result[5];
                driver5Version[0] = (String)result[6];
                driver6Version[0] = (String)result[7];
                endBoardVersion[0] = (String)result[8];
            }
            if (log != null)
            {
                log.LogInfo("GetSlaveHardVersion(" + ctrlBoxBoardVersion[0] +  "," + driver1Version[0] +  "," + driver2Version[0] + "," + driver3Version[0] + "," + driver4Version[0] + "," + driver5Version[0] + "," + driver6Version[0] + "," + endBoardVersion[0] + ") : " + (int)result[0] );
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取机器人固件版本
     * @param ctrlBoxBoardVersion 控制箱载板固件版本
     * @param driver1Version 驱动器1固件版本
     * @param driver1Version 驱动器2固件版本
     * @param driver1Version 驱动器3固件版本
     * @param driver1Version 驱动器4固件版本
     * @param driver1Version 驱动器5固件版本
     * @param driver1Version 驱动器6固件版本
     * @param endBoardVersion 末端板固件版本
     * @return 错误码
     */
    public int GetFirmwareVersion(String[] ctrlBoxBoardVersion, String[] driver1Version, String[] driver2Version, String[] driver3Version,
                                  String[] driver4Version, String[] driver5Version, String[] driver6Version, String[] endBoardVersion)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetSlaveFirmVersion" , params);
            if ((int)result[0] == 0)
            {
                ctrlBoxBoardVersion[0] = (String)result[1];
                driver1Version[0] = (String)result[2];
                driver2Version[0] = (String)result[3];
                driver3Version[0] = (String)result[4];
                driver4Version[0] = (String)result[5];
                driver5Version[0] = (String)result[6];
                driver6Version[0] = (String)result[7];
                endBoardVersion[0] = (String)result[8];
            }
            if (log != null)
            {
                log.LogInfo("GetSlaveFirmVersion(" + ctrlBoxBoardVersion[0] + "," + driver1Version[0] + "," + driver2Version[0] + "," + driver3Version[0] + "," + driver4Version[0] + "," + driver5Version[0] + "," + driver6Version[0] + "," + endBoardVersion[0] + ") : " + (int)result[0] );
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 焊接开始
     * @param ioType io类型 0-控制器IO； 1-扩展IO
     * @param arcNum 焊机配置文件编号
     * @param timeout 起弧超时时间
     * @return 错误码
     */
    public int ARCStart(int ioType, int arcNum, int timeout)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {ioType, arcNum, timeout};
            int rtn = (int)client.execute("ARCStart" , params);
            if (log != null)
            {
                log.LogInfo("ARCStart(" + ioType + "," + arcNum + "," + timeout + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 焊接结束
     * @param ioType io类型 0-控制器IO； 1-扩展IO
     * @param arcNum 焊机配置文件编号
     * @param timeout 熄弧超时时间
     * @return 错误码
     */
    public int ARCEnd(int ioType, int arcNum, int timeout)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {ioType, arcNum, timeout};
            int rtn = (int)client.execute("ARCEnd" , params);
            if (log != null)
            {
                log.LogInfo("ARCEnd(" + ioType + "," + arcNum + "," + timeout + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置焊接电流与输出模拟量对应关系
     * @param relation 关系值
     * @return 错误码
     */
    public int WeldingSetCurrentRelation(WeldCurrentAORelation relation)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {relation.currentMin, relation.currentMax, relation.outputVoltageMin, relation.outputVoltageMax, relation.AOIndex};
            int rtn = (int)client.execute("WeldingSetCurrentRelation" , params);
            if (log != null)
            {
                log.LogInfo("WeldingSetCurrentRelation(" + relation.currentMin + "," + relation.currentMax + "," + relation.outputVoltageMin + "," + relation.outputVoltageMax + "," + relation.AOIndex +") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置焊接电压与输出模拟量对应关系
     * @param relation 焊接电压-模拟量输出关系值
     * @return 错误码
     */
    public int WeldingSetVoltageRelation(WeldVoltageAORelation relation)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {relation.weldVoltageMin, relation.weldVoltageMax, relation.outputVoltageMin, relation.outputVoltageMax, relation.AOIndex};
            int rtn = (int)client.execute("WeldingSetVoltageRelation" , params);
            if (log != null)
            {
                log.LogInfo("WeldingSetVoltageRelation(" + relation.weldVoltageMin + "," + relation.weldVoltageMax + "," + relation.outputVoltageMin + "," + relation.outputVoltageMax + "," + relation.AOIndex + " ): " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取焊接电流与输出模拟量对应关系
     * @param relation 关系值
     * @return 错误码
     */
    public int WeldingGetCurrentRelation(WeldCurrentAORelation relation)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("WeldingGetCurrentRelation" , params);
            if ((int)result[0] == 0)
            {
                relation.currentMin = (double)result[1];
                relation.currentMax = (double)result[2];
                relation.outputVoltageMin = (double)result[3];
                relation.outputVoltageMax = (double)result[4];
                relation.AOIndex = (int)result[5];
            }
            if (log != null)
            {
                log.LogInfo("WeldingGetCurrentRelation(" + relation.currentMin + "," + relation.currentMax + "," + relation.outputVoltageMin + "," + relation.outputVoltageMax + "," + relation.AOIndex +  ") : " + (int)result[0] );
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取焊接电压与输出模拟量对应关系
     * @param relation 焊接电压-模拟量输出关系值
     * @return 错误码
     */
    public int WeldingGetVoltageRelation(WeldVoltageAORelation relation)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("WeldingGetVoltageRelation" , params);
            if ((int)result[0] == 0)
            {
                relation.weldVoltageMin = (double)result[1];
                relation.weldVoltageMax = (double)result[2];
                relation.outputVoltageMin = (double)result[3];
                relation.outputVoltageMax = (double)result[4];
                relation.AOIndex = (int)result[5];
            }
            if (log != null)
            {
                log.LogInfo("WeldingGetVoltageRelation(" + relation.weldVoltageMin + "," + relation.weldVoltageMax + "," + relation.outputVoltageMin + "," + relation.outputVoltageMax + "," + relation.AOIndex + ") : " + (int)result[0] );
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置焊接电流
     * @param ioType 控制IO类型 0-控制箱IO；1-扩展IO
     * @param current 焊接电流值(A)
     * @param AOIndex 焊接电流控制箱模拟量输出端口(0-1)
     * @param blend 是否平滑 0-不平滑；1-平滑
     * @return 错误码
     */
    public int WeldingSetCurrent(int ioType, double current, int AOIndex, int blend)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {ioType, current, AOIndex, blend};
            int rtn = (int)client.execute("WeldingSetCurrent" , params);
            if (log != null)
            {
                log.LogInfo("WeldingSetCurrent(" + ioType + "," + current + "," + AOIndex + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置焊接电压
     * @param ioType 控制IO类型 0-控制箱IO；1-扩展IO
     * @param voltage 焊接电压值(A)
     * @param AOIndex 焊接电压控制箱模拟量输出端口(0-1)
     * @param blend 是否平滑 0-不平滑；1-平滑
     * @return 错误码
     */
    public int WeldingSetVoltage(int ioType, double voltage, int AOIndex, int blend)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {ioType, voltage, AOIndex, blend};
            int rtn = (int)client.execute("WeldingSetVoltage" , params);
            if (log != null)
            {
                log.LogInfo("WeldingSetVoltage(" + ioType + "," + voltage + "," + AOIndex + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置摆动参数
     * @param weaveNum 摆焊参数配置编号
     * @param weaveType 摆动类型 0-平面三角波摆动；1-垂直L型三角波摆动；2-顺时针圆形摆动；3-逆时针圆形摆动；4-平面正弦波摆动；5-垂直L型正弦波摆动；6-垂直三角波摆动；7-垂直正弦波摆动
     * @param weaveFrequency 摆动频率(Hz)
     * @param weaveIncStayTime 等待模式 0-周期不包含等待时间；1-周期包含等待时间
     * @param weaveRange 摆动幅度(mm)
     * @param weaveLeftRange 垂直三角摆动左弦长度(mm)
     * @param weaveRightRange 垂直三角摆动右弦长度(mm)
     * @param additionalStayTime 垂直三角摆动垂三角点停留时间(mm)
     * @param weaveLeftStayTime 摆动左停留时间(ms)
     * @param weaveRightStayTime 摆动右停留时间(ms)
     * @param weaveCircleRadio 圆形摆动-回调比率(0-100%)
     * @param weaveStationary 摆动位置等待，0-等待时间内位置继续移动；1-等待时间内位置静止
     * @param weaveYawAngle 摆动方向方位角(绕摆动Z轴旋转)，单位°
     * @param weaveRotAngle 摆动方向方位角(绕摆动X轴旋转)，单位°
     * @return 错误码
     */
    public int WeaveSetPara(int weaveNum, int weaveType, double weaveFrequency, int weaveIncStayTime, double weaveRange, double weaveLeftRange, double weaveRightRange, int additionalStayTime, int weaveLeftStayTime, int weaveRightStayTime, int weaveCircleRadio, int weaveStationary, double weaveYawAngle,double weaveRotAngle)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {weaveNum, weaveType, weaveFrequency, weaveIncStayTime, weaveRange, weaveLeftRange, weaveRightRange, additionalStayTime, weaveLeftStayTime, weaveRightStayTime, weaveCircleRadio, weaveStationary, weaveYawAngle,weaveRotAngle};
            int rtn = (int)client.execute("WeaveSetPara" , params);
            if (log != null)
            {
                log.LogInfo("WeaveSetPara(" + weaveNum + "," + weaveType + "," + weaveFrequency + "," + weaveIncStayTime + "," + weaveRange + "," + weaveLeftRange + ", " + weaveRightRange + ", " + additionalStayTime + "," + weaveLeftStayTime + "," + weaveRightStayTime + "," + weaveCircleRadio + "," + weaveStationary + "," + weaveYawAngle + ","+weaveRotAngle+ ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 即时设置摆动参数
     * @param weaveNum 摆焊参数配置编号
     * @param weaveType 摆动类型 0-平面三角波摆动；1-垂直L型三角波摆动；2-顺时针圆形摆动；3-逆时针圆形摆动；4-平面正弦波摆动；5-垂直L型正弦波摆动；6-垂直三角波摆动；7-垂直正弦波摆动
     * @param weaveFrequency 摆动频率(Hz)
     * @param weaveIncStayTime 等待模式 0-周期不包含等待时间；1-周期包含等待时间
     * @param weaveRange 摆动幅度(mm)
     * @param weaveLeftStayTime 摆动左停留时间(ms)
     * @param weaveRightStayTime 摆动右停留时间(ms)
     * @param weaveCircleRadio 圆形摆动-回调比率(0-100%)
     * @param weaveStationary 摆动位置等待，0-等待时间内位置继续移动；1-等待时间内位置静止
     * @return 错误码
     */
    public int WeaveOnlineSetPara(int weaveNum, int weaveType, double weaveFrequency, int weaveIncStayTime, double weaveRange, int weaveLeftStayTime, int weaveRightStayTime, int weaveCircleRadio, int weaveStationary)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {weaveNum, weaveType, weaveFrequency, weaveIncStayTime, weaveRange, weaveLeftStayTime, weaveRightStayTime, weaveCircleRadio, weaveStationary};
            int rtn = (int)client.execute("WeaveOnlineSetPara" , params);
            if (log != null)
            {
                log.LogInfo("WeaveOnlineSetPara(" + weaveNum + "," + weaveType + "," + weaveFrequency + "," + weaveIncStayTime + "," + weaveRange + "," + weaveLeftStayTime + "," + weaveRightStayTime + "," + weaveCircleRadio + "," + weaveStationary + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 摆动开始
     * @param weaveNum 摆焊参数配置编号
     * @return 错误码
     */
    public int WeaveStart(int weaveNum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {weaveNum};
            int rtn = (int)client.execute("WeaveStart" , params);
            if (log != null)
            {
                log.LogInfo("WeaveStart(" + weaveNum + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage() + "  " + Thread.currentThread().getStackTrace()[1].getLineNumber());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 摆动结束
     * @param weaveNum 摆焊参数配置编号
     * @return 错误码
     */
    public int WeaveEnd(int weaveNum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {weaveNum};
            int rtn = (int)client.execute("WeaveEnd" , params);
            if (log != null)
            {
                log.LogInfo("WeaveEnd(" + weaveNum + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 正向送丝
     * @param ioType io类型  0-控制器IO；1-扩展IO
     * @param wireFeed 送丝控制  0-停止送丝；1-送丝
     * @return 错误码
     */
    public int SetForwardWireFeed(int ioType, int wireFeed)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {ioType, wireFeed};
            int rtn = (int)client.execute("SetForwardWireFeed" , params);
            if (log != null)
            {
                log.LogInfo("SetForwardWireFeed(" + ioType + "," + wireFeed + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 反向送丝
     * @param ioType io类型  0-控制器IO；1-扩展IO
     * @param wireFeed 送丝控制  0-停止送丝；1-送丝
     * @return 错误码
     */
    public int SetReverseWireFeed(int ioType, int wireFeed)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {ioType, wireFeed};
            int rtn = (int)client.execute("SetReverseWireFeed" , params);
            if (log != null)
            {
                log.LogInfo("SetReverseWireFeed(" + ioType + "," + wireFeed + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 送气
     * @param ioType io类型  0-控制器IO；1-扩展IO
     * @param airControl 送气控制  0-停止送气；1-送气
     * @return 错误码
     */
    public int SetAspirated(int ioType, int airControl)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {ioType, airControl};
            int rtn = (int)client.execute("SetAspirated" , params);
            if (log != null)
            {
                log.LogInfo("SetAspirated(" + ioType + "," + airControl + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 段焊开始
     * @param startDesePos 起始点笛卡尔位置
     * @param endDesePos 结束点笛卡尔位姿
     * @param startJPos 起始点关节位姿
     * @param endJPos 结束点关节位姿
     * @param weldLength 焊接段长度(mm)
     * @param noWeldLength 非焊接段长度(mm)
     * @param weldIOType 焊接IO类型(0-控制箱IO；1-扩展IO)
     * @param arcNum 焊机配置文件编号
     * @param weldTimeout 起/收弧超时时间
     * @param isWeave 是否摆动
     * @param weaveNum 摆焊参数配置编号
     * @param tool 工具号
     * @param user 工件号
     * @param vel  速度百分比，范围[0~100]
     * @param acc  加速度百分比，范围[0~100],暂不开放
     * @param ovl  速度缩放因子，范围[0~100]
     * @param blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param epos  扩展轴位置，单位mm
     * @param search  0-不焊丝寻位，1-焊丝寻位
     * @param offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param offset_pos  位姿偏移量
     * @return 错误码
     */
    public int SegmentWeldStart(DescPose startDesePos, DescPose endDesePos, JointPos startJPos, JointPos endJPos, double weldLength, double noWeldLength, int weldIOType,
                                int arcNum, int weldTimeout, boolean isWeave, int weaveNum, int tool, int user, double vel, double acc, double ovl, double blendR, ExaxisPos epos, int search, int offset_flag, DescPose offset_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }

        try
        {
            int rtn = 0;
            Object[] params = new Object[] {startDesePos.tran.x, startDesePos.tran.y, startDesePos.tran.z, endDesePos.tran.x, endDesePos.tran.y, endDesePos.tran.z};
            //获取起点到终点之间的距离和各方向角度余弦值
            Object[] result = (Object[])client.execute("GetSegWeldDisDir" , params);
            if ((int)result[0] != 0)
            {
                return (int)result[0];
            }

            double distance = (double)result[1];
            double directionX = (double)result[2];
            double directionY = (double)result[3];
            double directionZ = (double)result[4];

            rtn = MoveJ(startJPos, startDesePos, tool, user, vel, acc, ovl, epos, -1, offset_flag, offset_pos);
            if (rtn != 0)
            {
                return rtn;
            }
            int weldNum = 0;
            int noWeldNum = 0;
            int i = 0;
            while (i < (int)(distance / (weldLength + noWeldLength)) * 2 + 2)
            {
                if (i % 2 == 0)
                {
                    weldNum += 1;
                    if (weldNum * weldLength + noWeldNum * noWeldLength > distance)
                    {
                        rtn = ARCStart(weldIOType, arcNum, weldTimeout);
                        if (rtn != 0)
                        {
                            return rtn;
                        }
                        if (isWeave)
                        {
                            rtn = WeaveStart(weaveNum);
                            if (rtn != 0)
                            {
                                return rtn;
                            }
                        }
                        rtn = MoveL(endJPos, endDesePos, tool, user, vel, acc, ovl, blendR, epos, search, 0, offset_pos, 0, 100);
                        if (rtn != 0)
                        {
                            ARCEnd(weldIOType, arcNum, weldTimeout);
                            if (isWeave)
                            {
                                rtn = WeaveEnd(weaveNum);
                                if (rtn != 0)
                                {
                                    return rtn;
                                }
                            }
                            return rtn;
                        }
                        rtn = ARCEnd(weldIOType, arcNum, weldTimeout);
                        if (rtn != 0)
                        {
                            return rtn;
                        }
                        if (isWeave)
                        {
                            rtn = WeaveEnd(weaveNum);
                            if (rtn != 0)
                            {
                                return rtn;
                            }
                        }
                        break;
                    }
                    else
                    {
                        DescPose endOffPos = new DescPose(0, 0, 0, 0, 0, 0);
                        DescPose tmpWeldDesc = new DescPose(0, 0, 0, 0, 0, 0);
                        JointPos tmpJoint = new JointPos(0, 0, 0, 0, 0, 0);
                        Coord coord  = new Coord();
                        rtn = GetSegmentWeldPoint(startDesePos, endDesePos, weldNum * weldLength + noWeldNum * noWeldLength, tmpWeldDesc, tmpJoint, coord);
                        if (rtn != 0)
                        {
                            return rtn;
                        }
                        rtn = ARCStart(weldIOType, arcNum, weldTimeout);
                        if (rtn != 0)
                        {
                            return rtn;
                        }
                        if (isWeave)
                        {
                            rtn = WeaveStart(weaveNum);
                            if (rtn != 0)
                            {
                                return rtn;
                            }
                        }
                        rtn = MoveL(tmpJoint, tmpWeldDesc, coord.tool, coord.user, vel, acc, ovl, blendR,epos, search, 0, endOffPos, 0, 1000);
                        if (rtn != 0)
                        {
                            ARCEnd(weldIOType, arcNum, weldTimeout);
                            if (isWeave)
                            {
                                rtn = WeaveEnd(weaveNum);
                                if (rtn != 0)
                                {
                                    return rtn;
                                }
                            }
                            return rtn;
                        }
                        rtn = ARCEnd(weldIOType, arcNum, weldTimeout);
                        if (rtn != 0)
                        {
                            return rtn;
                        }
                        if (isWeave)
                        {
                            rtn = WeaveEnd(weaveNum);
                            if (rtn != 0)
                            {
                                return rtn;
                            }
                        }
                    }
                }
                else
                {
                    noWeldNum += 1;
                    if (weldNum * weldLength + noWeldNum * noWeldLength > distance)
                    {
                        rtn = MoveL(endJPos, endDesePos, tool, user, vel, acc, ovl, blendR,epos, search, 0, offset_pos, 0, 100);
                        if (rtn != 0)
                        {
                            return rtn;
                        }
                        break;
                    }
                    else
                    {
                        DescPose endOffPos = new DescPose(0, 0, 0, 0, 0, 0);
                        DescPose tmpWeldDesc = new DescPose(0, 0, 0, 0, 0, 0);
                        JointPos tmpJoint = new JointPos(0, 0, 0, 0, 0, 0);
                        Coord coord = new Coord();
                        rtn = GetSegmentWeldPoint(startDesePos, endDesePos, weldNum * weldLength + noWeldNum * noWeldLength, tmpWeldDesc, tmpJoint, coord);
                        if (rtn != 0)
                        {
                            return rtn;
                        }
                        rtn = MoveL(tmpJoint, tmpWeldDesc, coord.tool, coord.user, vel, acc, ovl, blendR, epos, search, 0, offset_pos,0,100);
                        if (rtn != 0)
                        {
                            return rtn;
                        }
                    }
                }
                i++;
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief CI功能配置
     * @param CIConfig CI配置 CIConfig[0] - CIConfig[7]表示CI0-CI7的功能配置 0-无配置；1-起弧成功；2-焊机准备；3-传送带检测；4-暂停；5-恢复；6-启动
     * 7-停止；8-暂停/恢复；9-启动/停止；10-脚踏拖动；11-移至作业原点；12-手自动切换；13-焊丝寻位成功；14-运动中断；15-启动主程序；16-启动倒带；17-启动确认；
     * 18-激光检测信号X；19-激光检测信号Y；20-外部急停输入信号1；21-外部急停输入信号2；22-一级缩减模式；23-二级缩减模式；24-三级缩减模式(停止)；25-恢复焊接；26-终止焊接
     * @return 错误码
     */
    public int SetDIConfig(int[] CIConfig)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            String configStr = "SetDIConfig(" + CIConfig[0] + "," + CIConfig[1] + "," + CIConfig[2] + "," + CIConfig[3] + "," + CIConfig[4] + "," + CIConfig[5] + "," + CIConfig[6] + "," + CIConfig[7] + " + )";
            int configLength = configStr.length();

            while (isSendCmd == true) //说明当前正在处理上一条指令
            {
                Thread.sleep(10);
            }

            sendBuf = "/f/bIII" + ResumeMotionCnt + " + III323III" + configLength + " + III" + configStr + " + III/b/f";
            if (log != null)
            {
                log.LogInfo("SetDIConfig(" + configStr + ") : " + sockErr);
            }
            ResumeMotionCnt++;
            isSendCmd = true;
        }
        catch (Throwable e)
        {
            return RobotError.ERR_RPC_ERROR;
        }
        return 0;
    }

    /**
     * @brief CI输入有效电平配置
     * @param CIConfig CI配置 CIConfig[0] - CIConfig[7]表示CI0-CI7的功能配置 0-高电平有效；1-低电平有效
     * @return 错误码
     */
    public int SetDIConfigLevel(int[] CIConfig)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try {
            String configStr = "SetDIConfigLevel(" + CIConfig[0] + "," + CIConfig[1] + "," + CIConfig[2] + "," + CIConfig[3] + "," + CIConfig[4] + "," + CIConfig[5] + "," + CIConfig[6] + "," + CIConfig[7] + " + )";
            int configLength = configStr.length();

            while (isSendCmd == true) //说明当前正在处理上一条指令
            {
                Thread.sleep(10);
            }

            sendBuf = "/f/bIII" + ResumeMotionCnt + " + III335III" + configLength + " + III" + configStr + " + III/b/f";
            if (log != null) {
                log.LogInfo("SetDIConfig(" + configStr + ") : " + sockErr );
            }
            ResumeMotionCnt++;
            isSendCmd = true;
        }
        catch (Throwable e)
        {
            return RobotError.ERR_RPC_ERROR;
        }
        return 0;
    }

    private int SegmentWeldEnd(int ioType, int arcNum, int timeout)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = ARCEnd(ioType, arcNum, timeout);
            if (rtn != 0)
            {
                return rtn;
            }
            rtn = StopMotion();
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 初始化日志参数
     * @param logType：输出模式，DIRECT-直接输出；BUFFER-缓冲输出；ASYNC-异步输出
     * @param logLevel：日志过滤等级，ERROR-错误；WARNING-警告;INFO-信息；DEBUG-调试
     * @param filePath: 文件保存路径，如“D://Log/”
     * @param saveFileNum：保存文件个数，同时超出保存文件个数和保存文件天数的文件将被删除
     * @param saveDays: 保存文件天数，同时超出保存文件个数和保存文件天数的文件将被删除
     * @return 错误码
     */
    public int LoggerInit(FrLogType logType, FrLogLevel logLevel, String filePath, int saveFileNum, int saveDays)
    {
        if (log != null)
        {
            log.LogInfo("log has already inited");
            return 0;
        }
        File logFile = new File(filePath);
        if (!logFile.exists())
        {
            return -6;
        }

        if (saveDays < 1)
        {
            return 4;
        }

        log = new FRLog(logType, logLevel, filePath, saveFileNum, saveDays);
        log.LogInfo("LoggerInit(" + logType + "," + logLevel + "," + filePath + "," + saveFileNum + "," + saveDays + ") : " + 0);
        return 0;
    }

    /**
     * @brief 设置日志过滤等级;
     * @param logLevel: 日志过滤等级，ERROR-错误；WARNING-警告;INFO-信息；DEBUG-调试
     * @return 错误码
     */
    public int SetLoggerLevel(FrLogLevel logLevel)
    {
        if (log != null)
        {
            log.SetLogLevel(logLevel);
        }
        if (log != null)
        {
            log.LogInfo("SetLogLevel(" + logLevel + ") : " + 0 );
        }
        return 0;
    }

    private boolean IsSockComError()
    {
        while(robotStateRoutineThread.clientRobotState.GetReconnState())
        {
            Sleep(10);
        }
        if (robotStateRoutineThread.GetSockErr() != RobotError.ERR_SUCCESS)
        {
            sockErr = robotStateRoutineThread.GetSockErr();
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "sdk socket error" + sockErr);
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isConnected(){
        return !IsSockComError();
    }

    /**
     * @brief 下载文件
     * @param fileType 文件类型    0-lua文件
     * @param fileName 文件名称    “test.lua”
     * @param saveFilePath 保存文件路径    “C：//test/”
     * @return 错误码
     */
    private int FileDownLoad(int fileType, String fileName, String saveFilePath)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {

            //判断保存的文件路径是否存在
            File savePathF = new File(saveFilePath);
            if (!savePathF.exists())
            {
                return (int)RobotError.ERR_SAVE_FILE_PATH_NOT_FOUND;
            }
            Object[] params = new Object[] {fileType, fileName};
            int rtn = (int)client.execute("FileDownload" , params);
            if (rtn != 0)
            {
                return rtn;
            }

            TCPClient clientDownLoad = new TCPClient(robotIp, 20011);
            Sleep(500);
            boolean bRtn = clientDownLoad.Connect();

            if (!bRtn)
            {
                if (log != null)
                {
                    log.LogInfo("download file connect fail");
                }
                clientDownLoad.Close();
                System.out.println("connect fail");
                return (int)RobotError.ERR_OTHER;
            }

            byte[] totalbuffer = new byte[1024 * 1024 * 50];//50Mb
            int totalSize = 0;
            String recvMd5 = "";
            int recvSize = 0;
            boolean findHeadFlag = false;

            int cur=1;
            while (true)
            {
                byte[] buffer = new byte[1024 * 1024 * 50];//50M
                int num = clientDownLoad.Recv(buffer);
                if (num < 1)
                {
                    if (log != null)
                    {
                        log.LogInfo("recv file failed  " + num);
                    }
                    return (int)RobotError.ERR_OTHER;
                }

                System.arraycopy(buffer, 0, totalbuffer, totalSize, num);
                totalSize += num;

                String head = new String(subByte(totalbuffer, 0, 4));

                if (!findHeadFlag && totalSize > 4 && head.equals("/f/b"))
                {
                    findHeadFlag = true;
                }
                if (findHeadFlag && totalSize >= 14 + 32)
                {
                    String fileSizeStr = new String(subByte(totalbuffer, 4, 10));
                    fileSizeStr = fileSizeStr.trim();
                    recvSize = Integer.parseInt(fileSizeStr);
                    recvMd5 = new String(subByte(totalbuffer, 14, 32));
                }

                if (findHeadFlag && totalSize == recvSize)
                {
                    break;
                }

            }

            byte[] fileBuffer = subByte(totalbuffer, 14 + 32, totalSize - 18 - 32);

            File saveFile = new File(saveFilePath + fileName);
            if(!saveFile.exists())
            {
                saveFile.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(saveFilePath + fileName);  //注意字节流与字符流可能导致写入文件长度不同
            for (byte b : fileBuffer) {
                out.write(b);
            }
            out.close();

            String checkMd5 = computeMD5(saveFilePath + fileName).toLowerCase();
            if (checkMd5.equals(recvMd5))
            {
                clientDownLoad.Send("SUCCESS");
                if (log != null)
                {
                    log.LogInfo("FileDownLoad(" + fileName + ", " + saveFilePath + ") : success");
                }
                return 0;
            }
            else
            {
                clientDownLoad.Send("FAIL");
                saveFile = new File(saveFilePath + fileName);
                saveFile.delete();
                if (log != null)
                {
                    log.LogInfo("PointTableDownLoad(" + fileName + ", " + saveFilePath + " + ) : FAIL");
                }
                return (int)RobotError.ERR_OTHER;
            }
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    private int FileDownLoad002(int fileType, String fileName, String saveFilePath) {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            // 检查文件路径有效性
            File savePathF = new File(saveFilePath);
            if (!savePathF.exists()) {
                return (int)RobotError.ERR_SAVE_FILE_PATH_NOT_FOUND;
            }

            // 发起RPC请求
            Object[] params = new Object[]{fileType, fileName};
            int rtn = (int)client.execute("FileDownload", params);
            if (rtn != 0) {
                return rtn;
            }

            // 创建TCP客户端并连接
            TCPClient clientDownLoad = new TCPClient(robotIp, 20011);
            Sleep(500);
            // 设置合理的超时时间（15秒）
//            clientDownLoad.setTimeouts(5000, 15000, 5000);

            if (!clientDownLoad.Connect()) {
                if (log != null) {
                    log.LogInfo("download file connect fail");
                }
                clientDownLoad.Close();
                return (int)RobotError.ERR_SOCKET_COM_FAILED;
            }

            // 初始化接收缓冲区
            ByteArrayOutputStream totalBuffer = new ByteArrayOutputStream(50 * 1024 * 1024); // 50MB
            String recvMd5 = "";
            int recvSize = 0;
            boolean findHeadFlag = false;
            long startTime = System.currentTimeMillis();
            final long DOWNLOAD_TIMEOUT_MS = 15000; // 15秒超时

            // 接收数据循环
            byte[] buffer = new byte[8 * 1024]; // 8KB缓冲区
            while (true) {
                // 检查超时
                if (System.currentTimeMillis() - startTime > DOWNLOAD_TIMEOUT_MS) {
                    if (log != null) {
                        log.LogError("FileDownLoad", 0, "Download timeout");
                    }
                    return (int)RobotError.ERR_DOWN_LOAD_FILE_FAILED;
                }

                int num = clientDownLoad.Recv(buffer);
                if (num < 0) {
                    if (log != null) {
                        log.LogError("FileDownLoad", 0, "Receive error, code: " + num);
                    }
                    return (int)RobotError.ERR_DOWN_LOAD_FILE_FAILED;
                }

                totalBuffer.write(buffer, 0, num);

                // 检查数据头
                if (!findHeadFlag && totalBuffer.size() > 4) {
                    String head = new String(totalBuffer.toByteArray(), 0, 4);
                    if (head.equals("/f/b")) {
                        findHeadFlag = true;
                    } else {
                        if (log != null) {
                            log.LogError("FileDownLoad", 0, "Invalid file header");
                        }
                        return (int)RobotError.ERR_DOWN_LOAD_FILE_FAILED;
                    }
                }

                // 解析长度和MD5
                if (findHeadFlag && totalBuffer.size() >= 14 + 32) {
                    String fileSizeStr = new String(totalBuffer.toByteArray(), 4, 10).trim();
                    try {
                        recvSize = Integer.parseInt(fileSizeStr);
                        recvMd5 = new String(totalBuffer.toByteArray(), 14, 32);
                    } catch (NumberFormatException e) {
                        if (log != null) {
                            log.LogError("FileDownLoad", 0, "Invalid file size format");
                        }
                        return (int)RobotError.ERR_DOWN_LOAD_FILE_FAILED;
                    }
                }

                // 检查是否接收完成
                if (findHeadFlag && totalBuffer.size() == recvSize) {
                    break;
                }
            }

            // 提取文件内容（跳过头部14字节和MD5 32字节，以及尾部4字节）
            byte[] totalBytes = totalBuffer.toByteArray();
            byte[] fileContent = Arrays.copyOfRange(totalBytes, 14 + 32, totalBytes.length - 4);

            // 写入文件
            File saveFile = new File(saveFilePath + fileName);
            try (FileOutputStream out = new FileOutputStream(saveFile)) {
                out.write(fileContent);
            }

            // 校验MD5
            String checkMd5 = computeMD5(saveFilePath + fileName).toLowerCase();
            if (!checkMd5.equals(recvMd5)) {
                saveFile.delete();
                if (log != null) {
                    log.LogError("FileDownLoad", 0, "MD5 check failed");
                }
                return (int)RobotError.ERR_DOWN_LOAD_FILE_CHECK_FAILED;
            }

            // 发送成功响应
            clientDownLoad.Send("SUCCESS");
            if (log != null) {
                log.LogInfo("FileDownLoad(" + fileName + ", " + saveFilePath + ") : success");
            }
            return 0;

        } catch (Throwable e) {
            if (log != null) {
                log.LogError("FileDownLoad", 0, "Exception: " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 上传文件
     * @param fileType 文件类型    0-lua文件
     * @param filePath 文件路径    “D://test.lua”
     * @return 错误码
     */
    private int FileUpLoad(int fileType, String filePath)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {

            //判断上传文件是否存在
            File fileInfo = new File(filePath);
            if (!fileInfo.exists())
            {
                if (log != null)
                {
                    log.LogInfo("file not existed!");
                }
                return (int)RobotError.ERR_UPLOAD_FILE_NOT_FOUND;
            }

            int totalSize = GetFileSize(filePath) + 4 + 46;
            if (totalSize > MAX_UPLOAD_FILE_SIZE)
            {
                if (log != null)
                {
                    log.LogInfo("Files larger than 2 MB are not supported!");
                }
                return -1;
            }
            if (log != null)
            {
                log.LogInfo("all upload file size is " + totalSize);
            }
            String fileName = fileInfo.getName();
            Object[] params = new Object[] {fileType, fileName};
            int rtn = (int)client.execute("FileUpload", params);
            if (rtn != 0)
            {
                if (log != null)
                {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "execute FileUpload fail : " + rtn);
                }
                return rtn;
            }

            TCPClient clientUpLoad = new TCPClient(robotIp, 20010);
            boolean bRtn = clientUpLoad.Connect();
            if (!bRtn)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "FileUpload TCP Connect fail : " + rtn);
                clientUpLoad.Close();
                return (int)RobotError.ERR_OTHER;
            }
            if (log != null)
            {
                log.LogInfo("Upload file connected!");
            }

            String sendMd5 = computeMD5(filePath).toLowerCase();
            if (log != null)
            {
                log.LogInfo("send Md5 is " + sendMd5);
            }

            int num = clientUpLoad.Send("/f/b" + String.format("%10d", totalSize) + sendMd5);
            if (num < 1)
            {
                if (log != null)
                {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "send head failed!");
                }
                clientUpLoad.Close();
                return (int)RobotError.ERR_OTHER;
            }

            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            clientUpLoad.Send(bytes);

            num = clientUpLoad.Send("/b/f");
            if (num < 1)
            {
                if (log != null)
                {
                    log.LogError("send end failed!");
                }
                return (int)RobotError.ERR_OTHER;
            }

            if (log != null)
            {
                log.LogError("send file end success!");
            }

            byte[] resultBuf = new byte[1024];
            num = 0;
            for(int i = 0; i < 20; i++)
            {
                num = clientUpLoad.Recv(resultBuf);
                if(num > 0)
                {
                    break;
                }
                else
                {
                    Sleep(500);
                }
            }
            if (num < 1)
            {
                if (log != null)
                {
                    log.LogError("get result failed! rtn is " + num);
                }
                return (int)RobotError.ERR_OTHER;
            }
            if (log != null)
            {
                log.LogError("recv success!");
            }
            String resultStr = new String(resultBuf, StandardCharsets.UTF_8).substring(0, 7);
            if (resultStr.equals("SUCCESS"))
            {
                if (log != null)
                {
                    log.LogInfo("fileUpLoad(" + filePath + ") : success");
                }
                return RobotError.ERR_SUCCESS;
            }
            else
            {
                if (log != null)
                {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "upload get result fail!  :  " + resultStr);
                }
                if (log != null)
                {
                    log.LogError("fileUpLoad(" + filePath + ") : fail");
                }
                return (int)RobotError.ERR_OTHER;
            }
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 删除文件
     * @param fileType 文件类型    0-lua文件
     * @param fileName 文件名称    “test.lua”
     * @return 错误码
     */
    private int FileDelete(int fileType, String fileName)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {fileType, fileName};
            int rtn = (int)client.execute("FileDelete" , params);
            if (log != null)
            {
                log.LogInfo("FileDelete(" + fileType + "," + fileName + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 上传Lua文件
     * @param filePath 本地lua文件路径名 ".../test.lua"或".../test.tar.gz"
     * @param errStr 错误信息
     * @return 错误码
     */
    public int LuaUpload(String filePath, String errStr)
    {
        try
        {
            File fileInfo = new File(filePath);
            if (!fileInfo.exists())
            {
                return (int)RobotError.ERR_UPLOAD_FILE_NOT_FOUND;
            }

            int rtn = FileUpLoad(0, filePath);
            if (rtn == 0)
            {
                Object[] params = new Object[] {fileInfo.getName()};
                Object[] result = (Object[])client.execute("LuaUpLoadUpdate" , params);

                errStr = (String)result[1];
                if ((int)result[0] != 0)
                {
                    if (log != null)
                    {
                        log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "LuaUpLoadUpdate(" + errStr + ")");
                    }
                }

                if (log != null)
                {
                    log.LogInfo("LuaUpLoadUpdate(" + filePath + ", " + errStr + ") : " + (int)result[0] );
                }
                return (int)result[0];
            }
            else
            {
                errStr = "Lua Upload Fail";
                if (log != null)
                {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "LuaUpLoadFail(" + filePath + ",  " + errStr + ") : " + rtn );
                }
                return rtn;
            }
        }
        catch (Throwable e)
        {
            return RobotError.ERR_OTHER;
        }

    }

    /**
     * @brief 下载Lua文件
     * @param fileName 要下载的lua文件名"test.lua"或"test.tar.gz"
     * @param savePath 保存文件本地路径“D://Down/”
     * @return 错误码
     */
    public int LuaDownLoad(String fileName, String savePath)
    {
        try
        {
            int rtn = 0;
            String[] nameStrs = fileName.split("\\.");
            if (nameStrs.length > 2 && Objects.equals(nameStrs[1], "tar") && Objects.equals(nameStrs[2], "gz"))
            {
                Object[] params = new Object[]{fileName};
                rtn = (int)client.execute("LuaDownLoadPrepare" , params);
                if (log != null)
                {
                    log.LogDebug("LuaDownLoad(" + fileName + " +  rtn " + rtn + " + )");
                }
                if (rtn != 0)
                {
                    return rtn;
                }
            }
            rtn = FileDownLoad(0, fileName, savePath);
            if (rtn == -1)
            {
                return (int)RobotError.ERR_LUAFILENITFOUND;
            }
            else
            {
                return rtn;
            }
        }
        catch (Throwable e)
        {
            return RobotError.ERR_OTHER;
        }
    }

    /**
     * @brief 删除Lua文件
     * @param fileName 要删除的lua文件名"test.lua"
     * @return 错误码
     */
    public int LuaDelete(String fileName)
    {
        int rtn = FileDelete(0, fileName);
        return rtn;
    }

    /**
     * @brief 获取当前所有lua文件名称
     * @param luaNames lua文件名列表
     * @return 错误码
     */
    public int GetLuaList(List<String> luaNames)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int luaNum = 0;
            String luaNameStr = "";
            Object[] result = (Object[])client.execute("GetLuaList" , params);
            if ((int)result[0] == 0)
            {
                luaNum = (int)result[1];
                luaNameStr = (String)result[2];
                String[] names = luaNameStr.split(";");
                for (int i = 0; i < luaNum; i++)
                {
                    luaNames.add(names[i]);
                }

            }
            if (log != null)
            {
                log.LogInfo("GetLuaList(" + luaNameStr + ") : " + (int)result[0] );
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 设置485扩展轴参数
     * @param servoId 伺服驱动器ID，范围[1-16],对应从站ID
     * @param param 485扩展轴参数
     * @return 错误码
     */
    public int AuxServoSetParam(int servoId, Axis485Param param)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {servoId, param.servoCompany, param.servoModel, param.servoSoftVersion, param.servoResolution, param.axisMechTransRatio};
            int rtn = (int)client.execute("AuxServoSetParam" , params);
            if (log != null) {
                log.LogInfo("AuxServoSetParam(" + servoId + "," + param.servoCompany + "," + param.servoModel + "," + param.servoSoftVersion + "," + param.servoResolution + "," + param.axisMechTransRatio + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取485扩展轴配置参数
     * @param servoId 伺服驱动器ID，范围[1-16],对应从站ID
     * @param param 485扩展轴参数
     * @return 错误码
     */
    public int AuxServoGetParam(int servoId, Axis485Param param)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {servoId};
            Object[] result = (Object[])client.execute("AuxServoGetParam" , params);
            if ((int)result[0] == 0)
            {
                param.servoCompany = (int)result[1];
                param.servoModel = (int)result[2];
                param.servoSoftVersion = (int)result[3];
                param.servoResolution = (int)result[4];
                param.axisMechTransRatio = (double)result[5];
            }
            if (log != null)
            {
                log.LogInfo("AuxServoGetParam(" + servoId + "," + param.servoCompany + "," + param.servoModel + "," + param.servoSoftVersion + "," + param.servoResolution + "," + param.axisMechTransRatio + ") : " + (int)result[0] );
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置485扩展轴使能/去使能
     * @param servoId 伺服驱动器ID，范围[1-16],对应从站ID
     * @param status 使能状态，0-去使能， 1-使能
     * @return 错误码
     */
    public int AuxServoEnable(int servoId, int status)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {servoId, status};
            int rtn = (int)client.execute("AuxServoEnable" , params);
            if (log != null)
            {
                log.LogInfo("AuxServoEnable(" + servoId + "," + status + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置485扩展轴控制模式
     * @param servoId 伺服驱动器ID，范围[1-16],对应从站ID
     * @param mode 控制模式，0-位置模式，1-速度模式
     * @return 错误码
     */
    public int AuxServoSetControlMode(int servoId, int mode)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {servoId, mode};
            int rtn = (int)client.execute("AuxServoSetControlMode" , params);
            if (log != null)
            {
                log.LogInfo("AuxServoSetControlMode(" + servoId + "," + mode + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置485扩展轴目标位置(位置模式)
     * @param servoId 伺服驱动器ID，范围[1-16],对应从站ID
     * @param pos 目标位置，mm或°
     * @param speed 目标速度，mm/s或°/s
     * @param acc 加速度百分比[0-100]
     * @return 错误码
     */
    public int AuxServoSetTargetPos(int servoId, double pos, double speed, double acc)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {servoId, pos, speed, acc};
            int rtn = (int)client.execute("AuxServoSetTargetPos" , params);
            if (log != null)
            {
                log.LogInfo("AuxServoSetTargetPos(" + servoId + "," + pos + "," + speed + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置485扩展轴目标速度(速度模式)
     * @param servoId 伺服驱动器ID，范围[1-16],对应从站ID
     * @param speed 目标速度，mm/s或°/s
     * @param acc 加速度百分比[0-100]
     * @return 错误码
     */
    public int AuxServoSetTargetSpeed(int servoId, double speed, double acc)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {servoId, speed, acc};
            int rtn = (int)client.execute("AuxServoSetTargetSpeed" , params);
            if (log != null)
            {
                log.LogInfo("AuxServoSetTargetSpeed(" + servoId + "," + speed + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置485扩展轴目标转矩(力矩模式)
     * @param servoId 伺服驱动器ID，范围[1-16],对应从站ID
     * @param torque 目标力矩，Nm
     * @return 错误码
     */
    public int AuxServoSetTargetTorque(int servoId, double torque)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {servoId, torque};
            int rtn = (int)client.execute("AuxServoSetTargetTorque" , params);
            if (log != null)
            {
                log.LogInfo("AuxServoSetTargetTorque(" + servoId + "," + torque + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置485扩展轴回零
     * @param servoId 伺服驱动器ID，范围[1-16],对应从站ID
     * @param mode 回零模式，1-当前位置回零；2-负限位回零；3-正限位回零
     * @param searchVel 回零速度，mm/s或°/s
     * @param latchVel 箍位速度，mm/s或°/s
     * @param acc 加速度百分比[0-100]
     * @return 错误码
     */
    public int AuxServoHoming(int servoId, int mode, double searchVel, double latchVel, double acc)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {servoId, mode, searchVel, latchVel, acc};
            int rtn = (int)client.execute("AuxServoHoming" , params);
            if (log != null)
            {
                log.LogInfo("AuxServoHoming(" + servoId + "," + mode + "," + searchVel + "," + latchVel + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 清除485扩展轴错误信息
     * @param servoId 伺服驱动器ID，范围[1-16],对应从站ID
     * @return 错误码
     */
    public int AuxServoClearError(int servoId)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {servoId};
            int rtn = (int)client.execute("AuxServoClearError" , params);
            if (log != null)
            {
                log.LogInfo("AuxServoClearError(" + servoId + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取485扩展轴伺服状态
     * @param servoId 伺服驱动器ID，范围[1-16],对应从站ID
     * @param servoErrCode 伺服驱动器故障码
     * @param servoState 伺服驱动器状态 bit0:0-未使能；1-使能;  bit1:0-未运动；1-正在运动;  bit4 0-未定位完成；1-定位完成；  bit5：0-未回零；1-回零完成
     * @param servoPos 伺服当前位置 mm或°
     * @param servoSpeed 伺服当前速度 mm/s或°/s
     * @param servoTorque 伺服当前转矩Nm
     * @return 错误码
     */
    public int AuxServoGetStatus(int servoId, int[] servoErrCode, int[] servoState, double[] servoPos, double[] servoSpeed, double[] servoTorque)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {servoId};
            Object[] result = (Object[])client.execute("AuxServoGetStatus" , params);
            if ((int)result[0] == 0)
            {
                servoErrCode[0] = (int)result[1];
                servoState[0] = (int)result[2];
                servoPos[0] = (double)result[3];
                servoSpeed[0] = (double)result[4];
                servoTorque[0] = (double)result[5];
            }
            if (log != null)
            {
                log.LogInfo("AuxServoGetStatus(" + servoId + "," + servoErrCode[0] + "," + servoState[0] + "," + servoPos[0] + "," + servoSpeed[0] + "," + servoTorque[0] + ") : " + (int)result[0] );
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置状态反馈中485扩展轴数据轴号
     * @param servoId 伺服驱动器ID，范围[1-16],对应从站ID
     * @return 错误码
     */
    public int AuxServosetStatusID(int servoId)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {servoId};
            int rtn = (int)client.execute("AuxServosetStatusID" , params);
            if (log != null)
            {
                log.LogInfo("AuxServosetStatusID(" + servoId + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取机器人实时状态结构体
     * @return 错误码
     */
    public ROBOT_STATE_PKG GetRobotRealTimeState()
    {
        if (IsSockComError())
        {
            return new ROBOT_STATE_PKG();
        }
        else
        {
            return robotStateRoutineThread.GetRobotRealTimeState();
        }

    }

    /**
     * @brief 获取机器人外设协议
     * @return List[0]:错误码; List[1] : int protocol 机器人外设协议号 4096-扩展轴控制卡；4097-ModbusSlave；4098-ModbusMaster
     */
    public List<Integer> GetExDevProtocol()
    {
        List<Integer> rtnArray = new ArrayList<Integer>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetExDevProtocol" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (int)result[1]);
            }
            if (log != null)
            {
                log.LogInfo("GetExDevProtocol(" + (int)result[1] + ") : " + (int)result[0] );
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief 设置机器人外设协议
     * @param protocol 机器人外设协议号 4096-扩展轴控制卡；4097-ModbusSlave；4098-ModbusMaster
     * @return 错误码
     */
    public int SetExDevProtocol(int protocol)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {protocol};
            int rtn = (int)client.execute("SetExDevProtocol" , params);
            if (log != null)
            {
                log.LogInfo("SetExDevProtocol(" + protocol + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置机器人加速度
     * @param acc 机器人加速度百分比
     * @return 错误码
     */
    public int SetOaccScale(double acc)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {acc};
            int rtn = (int)client.execute("SetOaccScale" , params);
            if (log != null)
            {
                log.LogInfo("SetOaccScale(" + acc + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 控制箱AO飞拍开始
     * @param AONum 控制箱AO编号
     * @param maxTCPSpeed 最大TCP速度值[1-5000mm/s]，默认1000
     * @param maxAOPercent 最大TCP速度值对应的AO百分比，默认100%
     * @param zeroZoneCmp 死区补偿值AO百分比，整形，默认为20%，范围[0-100]
     * @return 错误码
     */

    public int MoveAOStart(int AONum, int maxTCPSpeed, int maxAOPercent, int zeroZoneCmp)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {AONum, maxTCPSpeed, maxAOPercent, zeroZoneCmp};
            int rtn = (int)client.execute("MoveAOStart" , params);
            if (log != null)
            {
                log.LogInfo("MoveAOStart(" + AONum + ", " + maxTCPSpeed + ", " + maxAOPercent + ", " + zeroZoneCmp + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 控制箱AO飞拍停止
     * @return 错误码
     */
    public int MoveAOStop()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("MoveAOStop" , params);
            if (log != null)
            {
                log.LogInfo("MoveAOStop() : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 末端AO飞拍开始
     * @param AONum 末端AO编号
     * @param maxTCPSpeed 最大TCP速度值[1-5000mm/s]，默认1000
     * @param maxAOPercent 最大TCP速度值对应的AO百分比，默认100%
     * @param zeroZoneCmp 死区补偿值AO百分比，整形，默认为20%，范围[0-100]
     * @return 错误码
     */
    public int MoveToolAOStart(int AONum, int maxTCPSpeed, int maxAOPercent, int zeroZoneCmp)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {AONum, maxTCPSpeed, maxAOPercent, zeroZoneCmp};
            int rtn = (int)client.execute("MoveToolAOStart" , params);
            if (log != null)
            {
                log.LogInfo("MoveToolAOStart(" + AONum + ", " + maxTCPSpeed + ", " + maxAOPercent + ", " + zeroZoneCmp + ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 末端AO飞拍停止
     * @return 错误码
     */
    public int MoveToolAOStop()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("MoveToolAOStop" , params);
            if (log != null)
            {
                log.LogInfo("MoveToolAOStop() : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief UDP扩展轴通讯参数配置
     * @param param 通讯参数
     * @return 错误码
     */
    public int ExtDevSetUDPComParam(UDPComParam param)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {param.ip, param.port, param.period, param.lossPkgTime, param.lossPkgNum,
                    param.disconnectTime, param.reconnectEnable, param.reconnectPeriod, param.reconnectNum,param.selfConnect};
            int rtn = (int)client.execute("ExtDevSetUDPComParam" , params);
            if (log != null)
            {
                log.LogInfo("ExtDevSetUDPComParam(" + param.ip + ", " + param.port + ", " + param.period + ", " + param.lossPkgTime + ", " + param.lossPkgNum + ", " + param.disconnectTime + ", " + param.reconnectEnable + ", " + param.reconnectPeriod + ", " + param.reconnectNum +","+param.selfConnect+ ") : " + rtn );
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取UDP扩展轴通讯参数
     * @param param 通讯参数
     * @return 错误码
     */
    public int ExtDevGetUDPComParam(UDPComParam param)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("ExtDevGetUDPComParam" , params);
            if ((int)result[0] == 0)
            {
                param.ip = (String)result[1];
                param.port = (int)result[2];
                param.period = (int)result[3];
                param.lossPkgTime = (int)result[4];
                param.lossPkgNum = (int)result[5];
                param.disconnectTime = (int)result[6];
                param.reconnectEnable = (int)result[7];
                param.reconnectPeriod = (int)result[8];
                param.reconnectNum = (int)result[9];

            }
            if (log != null)
            {
                log.LogInfo("ExtDevGetUDPComParam(" + param.ip + ",  " + param.port + " , " + param.period + " , " + param.lossPkgTime + " , " + param.lossPkgNum + " , " + param.disconnectTime + " , " + param.reconnectEnable + " , " + param.reconnectPeriod + " , " + param.reconnectNum + " ) : " + (int)result[0] + " ");
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 加载UDP通信
     * @return 错误码
     */
    public int ExtDevLoadUDPDriver()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ExtDevLoadUDPDriver" , params);
            if (log != null)
            {
                log.LogInfo("ExtDevLoadUDPDriver() : " + rtn + " ");
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 卸载UDP通信
     * @return 错误码
     */
    public int ExtDevUnloadUDPDriver()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ExtDevUnloadUDPDriver" , params);
            if (log != null)
            {
                log.LogInfo("ExtDevUnloadUDPDriver() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief UDP扩展轴回零
     * @param axisID 轴号[1-4]
     * @param mode 回零方式 0-当前位置回零，1-负限位回零，2-正限位回零
     * @param searchVel 寻零速度(mm/s)
     * @param latchVel 寻零箍位速度(mm/s)
     * @return 错误码
     */
    public int ExtAxisSetHoming(int axisID, int mode, double searchVel, double latchVel)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {axisID, mode, searchVel, latchVel};
            int rtn = (int)client.execute("ExtAxisSetHoming" , params);
            if (log != null)
            {
                log.LogInfo("ExtAxisSetHoming(" + axisID + ", " + mode + ", " + searchVel + ", " + latchVel + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief UDP扩展轴点动开始
     * @param axisID 轴号[1-4]
     * @param direction 转动方向 0-反向；1-正向
     * @param vel 速度(mm/s)
     * @param acc (加速度 mm/s2)
     * @param maxDistance 最大点动距离
     * @return 错误码
     */
    public int ExtAxisStartJog(int axisID, int direction, double vel, double acc, double maxDistance)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {6, axisID, direction, vel, acc, maxDistance};
            int rtn = (int)client.execute("ExtAxisStartJog" , params);
            if (log != null)
            {
                log.LogInfo("ExtAxisStartJog(" + axisID + ", " + direction + ", " + vel + ", " + acc + ", " + maxDistance + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief UDP扩展轴点动停止
     * @param axisID 轴号[1-4]
     * @return 错误码
     */
    public int ExtAxisStopJog(int axisID)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            while (isSendCmd == true) //说明当前正在处理上一条指令
            {
                Thread.sleep(10);
            }

            sendBuf = "/f/bIII19III240III14IIIStopExtAxisJogIII/b/f";
            clientCmd.Send(sendBuf);
            byte[] recvBuf = new byte[1024];
            clientCmd.Recv(recvBuf);
            isSendCmd = true;
            if (log != null)
            {
                log.LogInfo("StopExtAxisJog() : " + sockErr);
            }
        }
        catch (Throwable e)
        {
            return RobotError.ERR_RPC_ERROR;
        }
        return 0;
    }

    /**
     * @brief UDP扩展轴使能
     * @param axisID 轴号[1-4]
     * @param status 0-去使能；1-使能
     * @return 错误码
     */
    public int ExtAxisServoOn(int axisID, int status)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {axisID, status};
            int rtn = (int)client.execute("ExtAxisServoOn" , params);
            if (log != null)
            {
                log.LogInfo("ExtAxisServoOn(" + axisID + ", " + status + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief UDP扩展轴运动
     * @param pos 目标位置
     * @param ovl 速度百分比
     * @return 错误码
     */
    public int ExtAxisMove(ExaxisPos pos, double ovl)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {0, pos.axis1, pos.axis2, pos.axis3, pos.axis4, ovl};
            //单独调用时，默认异步运动
            int rtn = (int)client.execute("ExtAxisMove" , params);
            if (log != null)
            {
                log.LogInfo("ExtAxisMove(" + pos.axis1 + ", " + pos.axis2 + ", " + pos.axis3 + ", " + pos.axis4 + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief UDP扩展轴运动
     * @param pos 目标位置
     * @param ovl 速度百分比
     * @param blend 平滑参数(mm或ms)
     * @return 错误码
     */
    public int ExtAxisMove(ExaxisPos pos, double ovl, double blend)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            Object[] params = new Object[] {0, pos.axis1, pos.axis2, pos.axis3, pos.axis4, ovl, blend};
            //单独调用时，默认异步运动
            int rtn = (int)client.execute("ExtAxisMoveJ" , params);
            if (log != null)
            {
                log.LogInfo("ExtAxisMoveJ(" + pos.axis1 + ", " + pos.axis2 + ", " + pos.axis3 + ", " + pos.axis4 + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置扩展DO
     * @param DONum DO编号
     * @param bOpen 开关 true-开；false-关
     * @param smooth 是否平滑
     * @param block 是否阻塞
     * @return 错误码
     */
    public int SetAuxDO(int DONum, boolean bOpen, boolean smooth, boolean block)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {

            int openFlag = bOpen ? 1 : 0;
            int smoothFlag = smooth ? 1 : 0;
            int noBlockFlag = block ? 0 : 1;
            Object[] params = new Object[] {DONum, openFlag, smoothFlag, 0};  //TODO 最后一个参数设置为1时不生效
            int rtn = (int)client.execute("SetAuxDO" , params);
            if (log != null)
            {
                log.LogInfo("SetAuxDO(" + DONum + ", " + openFlag + ", " + smoothFlag + ", " + noBlockFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置扩展AO
     * @param AONum AO编号
     * @param value 模拟量值[0-4095]
     * @param block 是否阻塞
     * @return 错误码
     */
    public int SetAuxAO(int AONum, double value, boolean block)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            int noBlockFlag = block ? 0 : 1;
            Object[] params = new Object[] {AONum, value, 0};//TODO 最后一个参数设置为1时不生效
            int rtn = (int)client.execute("SetAuxAO" , params);
            if (log != null)
            {
                log.LogInfo("SetAuxAO(" + AONum + ", " + value + ", " + block + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置扩展DI输入滤波时间
     * @param filterTime 滤波时间(ms)
     * @return 错误码
     */
    public int SetAuxDIFilterTime(int filterTime)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {filterTime};
            int rtn = (int)client.execute("SetAuxDIFilterTime" , params);
            if (log != null)
            {
                log.LogInfo("SetAuxDIFilterTime(" + filterTime + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置扩展AI输入滤波时间
     * @param AONum AO编号
     * @param filterTime 滤波时间(ms)
     * @return 错误码
     */
    public int SetAuxAIFilterTime(int AONum, int filterTime)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {AONum, filterTime};
            int rtn = (int)client.execute("SetAuxAIFilterTime" , params);
            if (log != null)
            {
                log.LogInfo("SetAuxAIFilterTime(" + AONum + "," + filterTime + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 等待扩展DI输入
     * @param DINum DI编号
     * @param bOpen 开关 0-关；1-开
     * @param time 最大等待时间(ms)
     * @param errorAlarm 是否继续运动
     * @return 错误码
     */
    public int WaitAuxDI(int DINum, boolean bOpen, int time, boolean errorAlarm)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            int openFlag = bOpen ? 1 : 0;
            int errorAlarmFlag = errorAlarm ? 1 : 0;
            Object[] params = new Object[] {DINum, openFlag, time, errorAlarmFlag};
            int rtn = (int)client.execute("WaitAuxDI" , params);
            if (log != null)
            {
                log.LogInfo("WaitAuxDI(" + DINum + ", " + bOpen + ", " + time + ", " + errorAlarm + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 等待扩展AI输入
     * @param AINum AI编号
     * @param sign 0-大于；1-小于
     * @param value AI值
     * @param time 最大等待时间(ms)
     * @param errorAlarm 是否继续运动
     * @return 错误码
     */
    public int WaitAuxAI(int AINum, int sign, int value, int time, boolean errorAlarm)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            int errorAlarmFlag = errorAlarm ? 1 : 0;
            Object[] params = new Object[] {AINum, sign, value, time, errorAlarmFlag};
            int rtn = (int)client.execute("WaitAuxAI" , params);
            if (log != null)
            {
                log.LogInfo("WaitAuxAI(" + AINum + ", " + sign + ", " + value + ", " + time + ", " + errorAlarm + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

//            /**
//             * @brief 获取扩展DI值
//             * @param DINum DI编号
//             * @param isNoBlock 是否阻塞
//             * @return List[0]:错误码; List[1] : isOpen 0-关；1-开
//             */
//            public List<Integer> GetAuxDI(int DINum, boolean isNoBlock, boolean isOpen)
//            {
//                List<Integer> rtnArray = new ArrayList<Integer>() {};
//                rtnArray.add(-1);
//                rtnArray.add(-1);
//
//                if (IsSockComError())
//                {
//                    rtnArray.set(0, sockErr);
//                    return rtnArray;
//                }
//
//                try
//                {
//                    int blockFlag = isNoBlock ? 0 : 1;
//                    Object[] params = new Object[] {DINum, blockFlag};
//                    Object[] result = (Object[])client.execute("GetAuxDI" , params);
//                    rtnArray.set(0, (int)result[0]);
//                    if ((int)result[0] == 0)
//                    {
//                        rtnArray.set(1, (int)result[1]);
//                    }
//                    if (log != null)
//                    {
//                        log.LogInfo("GetAuxDI(" + DINum}, " + isNoBlock + ",  " + isOpen + ") : " + (int)result[0]);
//                    }
//                    return rtnArray;
//                }
//                catch (Throwable e)
//                {
//                    if (log != null)
//                    {
//                        log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
//                    }
//                    rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
//                    return rtnArray;
//                }
//            }

    /**
     * @brief 获取扩展AI值
     * @param AINum AI编号
     * @param isNoBlock 是否阻塞
     * @return List[0]:错误码; List[1] : value 输入值
     */
    public List<Integer> GetAuxAI(int AINum, boolean isNoBlock)
    {
        List<Integer> rtnArray = new ArrayList<Integer>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            int blockFlag = isNoBlock ? 0 : 1;
            Object[] params = new Object[] {AINum, blockFlag};
            Object[] result = (Object[])client.execute("GetAuxAI" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (int)result[1]);
            }
            if (log != null)
            {
                log.LogInfo("GetAuxAI(" + AINum + ", " + isNoBlock + ",  " + (int)result[1] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief UDP扩展轴通信异常断开后恢复连接
     * @return 错误码
     */
    public int ExtDevUDPClientComReset()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ExtDevUDPClientComReset" , params);
            if (log != null)
            {
                log.LogInfo("ExtDevUDPClientComReset() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief UDP扩展轴通信异常断开后关闭通讯
     * @return 错误码
     */
    public int ExtDevUDPClientComClose()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ExtDevUDPClientComClose" , params);
            if (log != null)
            {
                log.LogInfo("ExtDevUDPClientComClose() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief UDP扩展轴参数配置
     * @param axisID 轴号
     * @param axisType 扩展轴类型 0-平移；1-旋转
     * @param axisDirection 扩展轴方向 0-正向；1-方向
     * @param axisMax 扩展轴最大位置 mm
     * @param axisMin 扩展轴最小位置 mm
     * @param axisVel 速度mm/s
     * @param axisAcc 加速度mm/s2
     * @param axisLead 导程mm
     * @param encResolution 编码器分辨率
     * @param axisOffect 焊缝起始点扩展轴偏移量
     * @param axisCompany 驱动器厂家 1-禾川；2-汇川；3-松下
     * @param axisModel 驱动器型号 1-禾川-SV-XD3EA040L-E，2-禾川-SV-X2EA150A-A，1-汇川-SV620PT5R4I，1-松下-MADLN15SG，2-松下-MSDLN25SG，3-松下-MCDLN35SG
     * @param axisEncType 编码器类型  0-增量；1-绝对值
     * @return 错误码
     */
    public int ExtAxisParamConfig(int axisID, int axisType, int axisDirection, double axisMax, double axisMin, double axisVel, double axisAcc, double axisLead, int encResolution, double axisOffect, int axisCompany, int axisModel, int axisEncType)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {axisID, axisType, axisDirection, axisMax, axisMin, axisVel, axisAcc, axisLead, encResolution, axisOffect, axisCompany, axisModel, axisEncType};
            int rtn = (int)client.execute("ExtAxisParamConfig" , params);
            if (log != null)
            {
                log.LogInfo("ExtAxisParamConfig(" + axisType + ", " + axisDirection + ", " + axisMax + ", " + axisMin + ", " + axisVel + ", " + axisAcc + ", " + axisLead + ", " + encResolution + ", " + axisOffect + ", " + axisCompany + ", " + axisModel + ", " + axisEncType + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取扩展轴驱动器配置信息
     * @param axisId 轴号[1-4]
     * @return List[0]: 错误码 List[1]: axisCompany 驱动器厂家 1-禾川；2-汇川；3-松下;
     * List[2]: axisModel 驱动器型号 1-禾川-SV-XD3EA040L-E，2-禾川-SV-X2EA150A-A，1-汇川-SV620PT5R4I，1-松下-MADLN15SG，2-松下-MSDLN25SG，3-松下-MCDLN35SG
     * List[3]: axisEncType 编码器类型  0-增量；1-绝对值
     */
    private List<Integer> GetExAxisDriverConfig(int axisId)
    {
        List<Integer> rtnArray = new ArrayList<Integer>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {axisId};
            Object[] result = (Object[])client.execute("GetExAxisDriverConfig" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (int)result[1]);
                rtnArray.set(2, (int)result[2]);
                rtnArray.set(3, (int)result[3]);
            }
            if (log != null)
            {
                log.LogInfo("GetExAxisDriverConfig(" + axisId + ",  " + (int)result[1] + ",  " + (int)result[2] + ",  " + (int)result[3] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief 设置扩展轴安装位置
     * @param installType 0-机器人安装在外部轴上，1-机器人安装在外部轴外
     * @return 错误码
     */
    public int SetRobotPosToAxis(int installType)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {installType};
            int rtn = (int)client.execute("SetRobotPosToAxis" , params);
            if (log != null)
            {
                log.LogInfo("SetRobotPosToAxis(" + installType + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置扩展轴系统DH参数配置
     * @param  axisConfig 外部轴构型，0-单自由度直线滑轨，1-两自由度L型变位机，2-三自由度，3-四自由度，4-单自由度变位机
     * @param  axisDHd1 外部轴DH参数d1 mm
     * @param  axisDHd2 外部轴DH参数d2 mm
     * @param  axisDHd3 外部轴DH参数d3 mm
     * @param  axisDHd4 外部轴DH参数d4 mm
     * @param  axisDHa1 外部轴DH参数11 mm
     * @param  axisDHa2 外部轴DH参数a2 mm
     * @param  axisDHa3 外部轴DH参数a3 mm
     * @param  axisDHa4 外部轴DH参数a4 mm
     * @return 错误码
     */
    public int SetAxisDHParaConfig(int axisConfig, double axisDHd1, double axisDHd2, double axisDHd3, double axisDHd4, double axisDHa1, double axisDHa2, double axisDHa3, double axisDHa4)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {axisConfig, axisDHd1, axisDHd2, axisDHd3, axisDHd4, axisDHa1, axisDHa2, axisDHa3, axisDHa4};
            int rtn = (int)client.execute("SetAxisDHParaConfig" , params);
            if (log != null)
            {
                log.LogInfo("SetAxisDHParaConfig(" + axisConfig + ", " + axisDHd1 + ", " + axisDHd2 + ", " + axisDHd3 + ", " + axisDHd4 + ", " + axisDHa1 + ", " + axisDHa2 + ", " + axisDHa3 + ", " + axisDHa4 + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置扩展轴坐标系参考点-四点法
     * @param  pointNum 点编号[1-4]
     * @return 错误码
     */
    public int ExtAxisSetRefPoint(int pointNum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {pointNum};
            int rtn = (int)client.execute("ExtAxisSetRefPoint" , params);
            if (log != null)
            {
                log.LogInfo("ExtAxisSetRefPoint(" + pointNum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 计算扩展轴坐标系-四点法
     * @param  coord 坐标系值
     * @return 错误码
     */
    public int ExtAxisComputeECoordSys(DescPose coord)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("ExtAxisComputeECoordSys" , params);
            if ((int)result[0] == 0)
            {
                coord.tran.x = (double)result[1];
                coord.tran.y = (double)result[2];
                coord.tran.z = (double)result[3];
                coord.rpy.rx = (double)result[4];
                coord.rpy.ry = (double)result[5];
                coord.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("ExtAxisComputeECoordSys(" + coord.tran.x + ",  " + coord.tran.y + ",  " + coord.tran.z + ",  " + coord.rpy.rx + ",  " + coord.rpy.ry + ",  " + coord.rpy.rz + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 应用扩展轴坐标系
     * @param  applyAxisId 扩展轴编号 bit0-bit3对应扩展轴编号1-4，如应用扩展轴1和3，则是 0b 0000 0101；也就是5
     * @param  axisCoordNum 扩展轴坐标系编号
     * @param  coord 坐标系值
     * @param  calibFlag 标定标志 0-否，1-是
     * @return 错误码
     */
    public int ExtAxisActiveECoordSys(int applyAxisId, int axisCoordNum, DescPose coord, int calibFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {applyAxisId, axisCoordNum, coord.tran.x, coord.tran.y, coord.tran.z, coord.rpy.rx, coord.rpy.ry, coord.rpy.rz, calibFlag};
            int rtn = (int)client.execute("ExtAxisActiveECoordSys" , params);
            if (log != null)
            {
                log.LogInfo("ExtAxisActiveECoordSys(" + applyAxisId + ", " + axisCoordNum + ", " + coord.tran.x + ", " + coord.tran.y + ", " + coord.tran.z + ", " + coord.rpy.rx + ", " + coord.rpy.ry + ", " + coord.rpy.rz + ", " + calibFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置标定参考点在变位机末端坐标系下位姿
     * @param pos 位姿值
     * @return 错误码
     */
    public int SetRefPointInExAxisEnd(DescPose pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {pos.tran.x, pos.tran.y, pos.tran.z, pos.rpy.rx, pos.rpy.ry, pos.rpy.rz};
            int rtn = (int)client.execute("SetRefPointInExAxisEnd" , params);
            if (log != null)
            {
                log.LogInfo("SetRefPointInExAxisEnd(" + pos.tran.x + ", " + pos.tran.y + ", " + pos.tran.z + ", " + pos.rpy.rx + ", " + pos.rpy.ry + ", " + pos.rpy.rz + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 变位机坐标系参考点设置
     * @param  pointNum 点编号[1-4]
     * @return 错误码
     */
    public int PositionorSetRefPoint(int pointNum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {pointNum};
            int rtn = (int)client.execute("PositionorSetRefPoint" , params);
            if (log != null)
            {
                log.LogInfo("PositionorSetRefPoint(" + pointNum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 变位机坐标系计算-四点法
     * @param  coord 坐标系值
     * @return 错误码
     */
    public int PositionorComputeECoordSys(DescPose coord)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("PositionorComputeECoordSys" , params);
            if ((int)result[0] == 0)
            {
                coord.tran.x = (double)result[1];
                coord.tran.y = (double)result[2];
                coord.tran.z = (double)result[3];
                coord.rpy.rx = (double)result[4];
                coord.rpy.ry = (double)result[5];
                coord.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("PositionorComputeECoordSys(" + coord.tran.x + ",  " + coord.tran.y + ",  " + coord.tran.z + ",  " + coord.rpy.rx + ",  " + coord.rpy.ry + ",  " + coord.rpy.rz + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  UDP扩展轴与机器人关节运动同步运动
     * @param  joint_pos  目标关节位置,单位deg
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  epos  扩展轴位置，单位mm
     * @param  blendT [-1.0]-运动到位(阻塞)，[0~500.0]-平滑时间(非阻塞)，单位ms
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @return  错误码
     */
    public int ExtAxisSyncMoveJ(JointPos joint_pos, DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, ExaxisPos epos, double blendT, int offset_flag, DescPose offset_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {

            Object[] joint = {joint_pos.J1, joint_pos.J2, joint_pos.J3, joint_pos.J4, joint_pos.J5, joint_pos.J6};
            Object[] desc =  { desc_pos.tran.x, desc_pos.tran.y, desc_pos.tran.z, desc_pos.rpy.rx, desc_pos.rpy.ry, desc_pos.rpy.rz };
            Object[] offect = { offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry, offset_pos.rpy.rz };
            Object[] params = new Object[] {1, epos.axis1, epos.axis2, epos.axis3, epos.axis4, ovl, blendT};
            int rtn = (int)client.execute("ExtAxisMoveJ" , params);
            if (rtn != 0)
            {
                if (log != null)
                {
                    log.LogInfo("ExtAxisSyncMoveJ(" + joint[0] + "," + joint[1] + "," + joint[2] + "," + joint[3] + "," + joint[4] + "," + joint[5] + "," + desc[0] + "," + desc[1] + "," + desc[2] + "," + desc[3] + "," + desc[4] + "," + desc[5] + "," + tool + "," + user + "," + vel + "," + acc + "," + ovl + "," +
                            epos.axis1 + "," + epos.axis2 + "," + epos.axis3 + "," + epos.axis4 + "," + blendT + "," + offset_flag + "," + offect[0] + "," + offect[1] + "," + offect[2] + "," + offect[3] + "," + offect[4] + "," + offect[5] + ") : " + rtn);
                }
                return rtn;
            }
            rtn = MoveJ(joint_pos, desc_pos, tool, user, vel, acc, ovl, epos, blendT, offset_flag, offset_pos);
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief  UDP扩展轴与机器人关节运动同步运动 (重载函数 不需要输入笛卡尔位置)
     * @param  joint_pos  目标关节位置,单位deg
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  epos  扩展轴位置，单位mm
     * @param  blendT [-1.0]-运动到位(阻塞)，[0~500.0]-平滑时间(非阻塞)，单位ms
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @return  错误码
     */
    public int ExtAxisSyncMoveJ(JointPos joint_pos, int tool, int user, double vel, double acc, double ovl, ExaxisPos epos, double blendT, int offset_flag, DescPose offset_pos)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        if (GetSafetyCode() != 0) {
            return GetSafetyCode();
        }
        DescPose desc_pos =new DescPose();
        int errcode = GetForwardKin(joint_pos, desc_pos);
        if (errcode != 0)
        {
            log.LogError("ExtAxisSyncMoveJ GetForwardKin failed rtn is: "+ errcode);
            return errcode;
        }

        errcode = ExtAxisSyncMoveJ(joint_pos, desc_pos, tool, user, vel, acc, ovl, epos, blendT, offset_flag, offset_pos);

        return errcode;
    }

    /**
     * @brief  UDP扩展轴与机器人直线运动同步运动
     * @param  joint_pos  目标关节位置,单位deg
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  epos  扩展轴位置，单位mm
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @return  错误码
     */
    public int ExtAxisSyncMoveL(JointPos joint_pos, DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, double blendR, ExaxisPos epos, int offset_flag, DescPose offset_pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {
            int rtn = -1;
            Object[] offect = { offset_pos.tran.x, offset_pos.tran.y, offset_pos.tran.z, offset_pos.rpy.rx, offset_pos.rpy.ry, offset_pos.rpy.rz };
            Object[] params = new Object[] {1, epos.axis1, epos.axis2, epos.axis3, epos.axis4, ovl, blendR};
            rtn = (int)client.execute("ExtAxisMoveJ" , params);
            if (rtn != 0)
            {
                if (log != null)
                {
                    log.LogInfo("ExtAxisMoveJ( " + epos.axis1 + "," + epos.axis2 + "," + epos.axis3 + "," + epos.axis4 + "," + offset_flag + "," + offect[0] + "," + offect[1] + "," + offect[2] + "," + offect[3] + "," + offect[4] + "," + offect[5] + ") : " + rtn);
                }
                return rtn;
            }
            Object[] axis = {epos.axis1, epos.axis2, epos.axis3, epos.axis4};
            rtn = MoveL(joint_pos, desc_pos, tool, user, vel, acc, ovl, blendR, 0,epos,0,  offset_flag, offset_pos, 0,0, 100);
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  UDP扩展轴与机器人直线运动同步运动 (重载函数 不需要输入关节位置)
     * @param  desc_pos   目标笛卡尔位姿
     * @param  tool  工具坐标号，范围[0~14]
     * @param  user  工件坐标号，范围[0~14]
     * @param  vel  速度百分比，范围[0~100]
     * @param  acc  加速度百分比，范围[0~100],暂不开放
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  epos  扩展轴位置，单位mm
     * @param  offset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos  位姿偏移量
     * @param  config 逆解关节空间配置，[-1]-参考当前关节位置解算，[0~7]-依据特定关节空间配置求解
     * @return  错误码
     */
    public int ExtAxisSyncMoveL(DescPose desc_pos, int tool, int user, double vel, double acc, double ovl, double blendR, ExaxisPos epos, int offset_flag, DescPose offset_pos,int config)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        if (GetSafetyCode() != 0) {
            return GetSafetyCode();
        }
        JointPos jPos=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int errcode = GetInverseKin(0, desc_pos, config, jPos);
        if (errcode != 0)
        {
            log.LogError("ExtAxisSyncMoveL GetInverseKin failed rtn is: "+ errcode);
            return errcode;
        }

        errcode = ExtAxisSyncMoveL(jPos, desc_pos, tool, user, vel, acc, ovl, blendR, epos, offset_flag, offset_pos);
        return errcode;
    }

    /**
     * @brief  UDP扩展轴与机器人圆弧运动同步运动
     * @param  joint_pos_p  路径点关节位置,单位deg
     * @param  desc_pos_p   路径点笛卡尔位姿
     * @param  ptool  工具坐标号，范围[0~14]
     * @param  puser  工件坐标号，范围[0~14]
     * @param  pvel  速度百分比，范围[0~100]
     * @param  pacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_p  中间点扩展轴位置，单位mm
     * @param  poffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_p  位姿偏移量
     * @param  joint_pos_t  目标点关节位置,单位deg
     * @param  desc_pos_t   目标点笛卡尔位姿
     * @param  ttool  工具坐标号，范围[0~14]
     * @param  tuser  工件坐标号，范围[0~14]
     * @param  tvel  速度百分比，范围[0~100]
     * @param  tacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_t  扩展轴位置，单位mm
     * @param  toffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_t  位姿偏移量
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @return  错误码
     */
    public int ExtAxisSyncMoveC(JointPos joint_pos_p, DescPose desc_pos_p, int ptool, int puser, double pvel, double pacc, ExaxisPos epos_p, int poffset_flag, DescPose offset_pos_p, JointPos joint_pos_t, DescPose desc_pos_t, int ttool, int tuser, double tvel, double tacc, ExaxisPos epos_t, int toffset_flag, DescPose offset_pos_t, double ovl, double blendR)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if(GetSafetyCode()!=0){
            return GetSafetyCode();
        }
        try
        {

            int rtn = 0;

            Object[] params = new Object[] {1, epos_t.axis1, epos_t.axis2, epos_t.axis3, epos_t.axis4, ovl, blendR};
            rtn = (int)client.execute("ExtAxisMoveJ" , params);
            if (rtn != 0)
            {
                if (log != null)
                {
                    log.LogInfo("ExtAxisMoveJ(" + epos_t.axis1 + "," + epos_t.axis2 + "," + epos_t.axis3 + "," + epos_t.axis4 + "," + blendR + " : " + rtn);
                }
                return rtn;
            }
            //joint_pos_p, desc_pos_p, ptool, puser, pvel, pacc, epos_p, poffset_flag, offset_pos_p, joint_pos_t, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, toffset_flag, offset_pos_t, ovl, blendR
            rtn = MoveC(joint_pos_p, desc_pos_p, ptool, puser, pvel, pacc, epos_p, poffset_flag, offset_pos_p, joint_pos_t, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, toffset_flag, offset_pos_t, ovl, blendR);
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  UDP扩展轴与机器人圆弧运动同步运动 (重载函数 不需要输入关节位置)
     * @param  desc_pos_p   路径点笛卡尔位姿
     * @param  ptool  工具坐标号，范围[0~14]
     * @param  puser  工件坐标号，范围[0~14]
     * @param  pvel  速度百分比，范围[0~100]
     * @param  pacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_p  中间点扩展轴位置，单位mm
     * @param  poffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_p  位姿偏移量
     * @param  desc_pos_t   目标点笛卡尔位姿
     * @param  ttool  工具坐标号，范围[0~14]
     * @param  tuser  工件坐标号，范围[0~14]
     * @param  tvel  速度百分比，范围[0~100]
     * @param  tacc  加速度百分比，范围[0~100],暂不开放
     * @param  epos_t  扩展轴位置，单位mm
     * @param  toffset_flag  0-不偏移，1-基坐标系/工件坐标系下偏移，2-工具坐标系下偏移
     * @param  offset_pos_t  位姿偏移量
     * @param  ovl  速度缩放因子，范围[0~100]
     * @param  blendR [-1.0]-运动到位(阻塞)，[0~1000.0]-平滑半径(非阻塞)，单位mm
     * @param  config 逆解关节空间配置，[-1]-参考当前关节位置解算，[0~7]-依据特定关节空间配置求解
     * @return  错误码
     */
    public int ExtAxisSyncMoveC(DescPose desc_pos_p, int ptool, int puser, double pvel, double pacc, ExaxisPos epos_p, int poffset_flag, DescPose offset_pos_p, DescPose desc_pos_t, int ttool, int tuser, double tvel, double tacc, ExaxisPos epos_t, int toffset_flag, DescPose offset_pos_t, double ovl, double blendR,int config)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        if (GetSafetyCode() != 0) {
            return GetSafetyCode();
        }

        JointPos jPosP=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        JointPos jPosT=new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int errcode = GetInverseKin(0, desc_pos_p, config, jPosP);
        if (errcode != 0)
        {
            log.LogError("ExtAxisSyncMoveC  GetInverseKin P failed rtn is:"+ errcode);
            return errcode;
        }

        errcode = GetInverseKin(0, desc_pos_t, config, jPosT);
        if (errcode != 0)
        {
            log.LogError("ExtAxisSyncMoveC  GetInverseKin T failed rtn is:"+ errcode);
            return errcode;
        }

        errcode = ExtAxisSyncMoveC(jPosP, desc_pos_p, ptool, puser, pvel, pacc, epos_p, poffset_flag, offset_pos_p, jPosT, desc_pos_t, ttool, tuser, tvel, tacc, epos_t, toffset_flag, offset_pos_t, ovl, blendR);

        return errcode;
    }

    /**
     * @brief  焊丝寻位开始
     * @param  refPos  1-基准点 2-接触点
     * @param  searchVel   寻位速度 %
     * @param  searchDis  寻位距离 mm
     * @param  autoBackFlag 自动返回标志，0-不自动；-自动
     * @param  autoBackVel  自动返回速度 %
     * @param  autoBackDis  自动返回距离 mm
     * @param  offectFlag  1-带偏移量寻位；2-示教点寻位
     * @return  错误码
     */
    public int WireSearchStart(int refPos, double searchVel, int searchDis, int autoBackFlag, double autoBackVel, int autoBackDis, int offectFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {refPos, searchVel, searchDis, autoBackFlag, autoBackVel, autoBackDis, offectFlag};
            int rtn = (int)client.execute("WireSearchStart" , params);
            if (log != null) {
                log.LogInfo("WireSearchStart(" + refPos + ", " + searchVel + ", " + searchDis + ", " + autoBackFlag + ", " + autoBackVel + ", " + autoBackDis + ", " + offectFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  焊丝寻位结束
     * @param  refPos  1-基准点 2-接触点
     * @param  searchVel   寻位速度 %
     * @param  searchDis  寻位距离 mm
     * @param  autoBackFlag 自动返回标志，0-不自动；-自动
     * @param  autoBackVel  自动返回速度 %
     * @param  autoBackDis  自动返回距离 mm
     * @param  offectFlag  1-带偏移量寻位；2-示教点寻位
     * @return  错误码
     */
    public int WireSearchEnd(int refPos, double searchVel, int searchDis, int autoBackFlag, double autoBackVel, int autoBackDis, int offectFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {refPos, searchVel, searchDis, autoBackFlag, autoBackVel, autoBackDis, offectFlag};
            int rtn = (int)client.execute("WireSearchEnd" , params);
            if (log != null)
            {
                log.LogInfo("WireSearchEnd(" + refPos + ", " + searchVel + ", " + searchDis + ", " + autoBackFlag + ", " + autoBackVel + ", " + autoBackDis + ", " + offectFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  计算焊丝寻位偏移量
     * @param  seamType  焊缝类型
     * @param  method   计算方法
     * @param  varNameRef 基准点1-6，“#”表示无点变量
     * @param  varNameRes 接触点1-6，“#”表示无点变量
     * @param  offset 偏移位姿[x, y, z, a, b, c]及偏移方式
     * @return  错误码
     */
    public int GetWireSearchOffset(int seamType, int method, String[] varNameRef, String[] varNameRes, DescOffset offset)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {seamType, method, varNameRef[0], varNameRef[1], varNameRef[2], varNameRef[3], varNameRef[4], varNameRef[5], varNameRes[0], varNameRes[1], varNameRes[2], varNameRes[3], varNameRes[4], varNameRes[5]};
            Object[] result = (Object[])client.execute("GetWireSearchOffset" , params);
            if ((int)result[0] == 0)
            {
                offset.offsetFlag = (int)result[1];
                offset.offset.tran.x = (double)result[2];
                offset.offset.tran.y = (double)result[3];
                offset.offset.tran.z = (double)result[4];
                offset.offset.rpy.rx = (double)result[5];
                offset.offset.rpy.ry = (double)result[6];
                offset.offset.rpy.rz = (double)result[7];
            }
            if (log != null)
            {
                log.LogInfo("GetWireSearchOffect(" + seamType + ", " + method + ", " + varNameRef[0] + ", " + varNameRef[1] + ", " + varNameRef[2] + ", " + varNameRef[3] + ", " + varNameRef[4] + ", " + varNameRef[5] + ", " + varNameRes[0] + ", " + varNameRes[1] + ", " + varNameRes[2] + ", " + varNameRes[3] + ", " + varNameRes[4] + ", " + varNameRes[5] + ", " + offset.offsetFlag + ", " + offset.offset.tran.x + ",  " + offset.offset.tran.y + ",  " + offset.offset.tran.z + ",  " + offset.offset.rpy.rx + ",  " + offset.offset.rpy.ry + ",  " + offset.offset.rpy.rz + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  等待焊丝寻位完成
     * @return  错误码
     */
    public int WireSearchWait(String name)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {name};
            int rtn = (int)client.execute("WireSearchWait" , params);
            if (log != null)
            {
                log.LogInfo( "WireSearchWait(" + name + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  焊丝寻位接触点写入数据库
     * @param  varName  接触点名称 “RES0” ~ “RES99”
     * @param  pos  接触点数据[x, y, x, a, b, c]
     * @return  错误码
     */
    public int SetPointToDatabase(String varName, DescPose pos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {

            Object[] tmpPos = { pos.tran.x, pos.tran.y, pos.tran.z, pos.rpy.rx, pos.rpy.ry, pos.rpy.rz };
            Object[] params = new Object[] {varName, tmpPos};
            int rtn = (int)client.execute("SetPointToDatabase" , params);
            if (log != null)
            {
                log.LogInfo("SetPointToDatabase(" + varName + ", " + pos.tran.x + ", " + pos.tran.y + ", " + pos.tran.z + ", " + pos.rpy.rx + ", " + pos.rpy.ry + ", " + pos.rpy.rz + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  电弧跟踪控制
     * @param  flag 开关，0-关；1-开
     * @param  delaytime 滞后时间，单位ms
     * @param  isLeftRight 左右偏差补偿
     * @param  klr 左右调节系数(灵敏度)
     * @param  tStartLr 左右开始补偿时间cyc
     * @param  stepMaxLr 左右每次最大补偿量 mm
     * @param  sumMaxLr 左右总计最大补偿量 mm
     * @param  isUpLow 上下偏差补偿
     * @param  kud 上下调节系数(灵敏度)
     * @param  tStartUd 上下开始补偿时间cyc
     * @param  stepMaxUd 上下每次最大补偿量 mm
     * @param  sumMaxUd 上下总计最大补偿量
     * @param  axisSelect 上下坐标系选择，0-摆动；1-工具；2-基座
     * @param  referenceType 上下基准电流设定方式，0-反馈；1-常数
     * @param  referSampleStartUd 上下基准电流采样开始计数(反馈)，cyc
     * @param  referSampleCountUd 上下基准电流采样循环计数(反馈)，cyc
     * @param  referenceCurrent 上下基准电流mA
     * @param  offsetType 偏置跟踪类型，0-不偏置；1-采样；2-百分比
     * @param  offsetParameter 偏置参数；采样(偏置采样开始时间，默认采一周期)；百分比(偏置百分比(-100 ~ 100))
     * @return  错误码
     */
    public int ArcWeldTraceControl(int flag, double delaytime, int isLeftRight, double klr, double tStartLr, double stepMaxLr, double sumMaxLr, int isUpLow, double kud, double tStartUd, double stepMaxUd, double sumMaxUd, int axisSelect, int referenceType, double referSampleStartUd, double referSampleCountUd, double referenceCurrent, int offsetType, int offsetParameter)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {

            Object[] paramLR = { klr, tStartLr, stepMaxLr, sumMaxLr };
            Object[] paramUD = { kud, tStartUd, stepMaxUd, sumMaxUd };
            Object[] params = new Object[] {flag, delaytime, isLeftRight, paramLR, isUpLow, paramUD, axisSelect, referenceType, referSampleStartUd, referSampleCountUd, referenceCurrent,offsetType,offsetParameter};
            int rtn = (int)client.execute("ArcWeldTraceControl" , params);
            if (log != null)
            {
                log.LogInfo("ArcWeldTraceControl(" + flag + ", " + delaytime + ", " + isLeftRight + ", " + klr + ", " + tStartLr + ", " + stepMaxLr + ", " + sumMaxLr + ", " + isUpLow + ", " + kud + ", " + tStartUd + ", " + stepMaxUd + ", " + sumMaxUd + ", " + axisSelect + ", " + referenceType + ", " + referSampleStartUd + ", " + referSampleCountUd + ", " + referenceCurrent + ","+offsetType+", " +offsetParameter+") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  电弧跟踪AI通带选择
     * @param  channel 电弧跟踪AI通带选择,[0-3]
     * @return  错误码
     */
    public int ArcWeldTraceExtAIChannelConfig(int channel)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {channel};
            int rtn = (int)client.execute("ArcWeldTraceExtAIChannelConfig" , params);
            if (log != null)
            {
                log.LogInfo("ArcWeldTraceExtAIChannelConfig(" + channel + " ) : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  力传感器辅助拖动
     * @param  status 控制状态，0-关闭；1-开启
     * @param  asaptiveFlag 自适应开启标志，0-关闭；1-开启
     * @param  interfereDragFlag 干涉区拖动标志，0-关闭；1-开启
     * @param  ingularityConstraintsFlag 奇异点策略，0-规避；1-穿越
     * @param  M 惯性系数
     * @param  B 阻尼系数
     * @param  K 刚度系数
     * @param  F 拖动六维力阈值
     * @param  Fmax 最大拖动力限制 Nm
     * @param  Vmax 最大关节速度限制 °/s
     * @return  错误码
     */
    public int EndForceDragControl(int status, int asaptiveFlag, int interfereDragFlag,int ingularityConstraintsFlag, Object[] M, Object[] B, Object[] K, Object[] F, double Fmax, double Vmax)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {status, asaptiveFlag, interfereDragFlag,ingularityConstraintsFlag, M, B, K, F, Fmax, Vmax};
            int rtn = (int)client.execute("EndForceDragControl" , params);
            if (log != null)
            {
                log.LogInfo("EndForceDragControl(" + status + ", " + asaptiveFlag + ", " + interfereDragFlag + ", " + Arrays.toString(M) + ", " + Arrays.toString(B) + ", " + Arrays.toString(K) + ", " + Arrays.toString(F) + ", " + Fmax + ", " + Vmax + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  力传感器辅助拖动
     * @param  status 控制状态，0-关闭；1-开启
     * @param  asaptiveFlag 自适应开启标志，0-关闭；1-开启
     * @param  interfereDragFlag 干涉区拖动标志，0-关闭；1-开启
     * @param  ingularityConstraintsFlag 奇异点策略，0-规避；1-穿越
     * @param  forceCollisionFlag 辅助拖动时机器人碰撞检测标志；0-关闭；1-开启
     * @param  M 惯性系数
     * @param  B 阻尼系数
     * @param  K 刚度系数
     * @param  F 拖动六维力阈值
     * @param  Fmax 最大拖动力限制
     * @param  Vmax 最大关节速度限制
     * @return  错误码
     */
    public int EndForceDragControl(int status, int asaptiveFlag, int interfereDragFlag,int ingularityConstraintsFlag, int forceCollisionFlag, Object[] M, Object[] B, Object[] K, Object[] F, double Fmax, double Vmax)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {status, asaptiveFlag, interfereDragFlag,ingularityConstraintsFlag, forceCollisionFlag, M, B, K, F, Fmax, Vmax};
            int rtn = (int)client.execute("EndForceDragControl" , params);
            if (log != null)
            {
                log.LogInfo("EndForceDragControl(" + status + ", " + asaptiveFlag + ", " + interfereDragFlag +","+ingularityConstraintsFlag+", "+ forceCollisionFlag+ ","+ Arrays.toString(M) + ", " + Arrays.toString(B) + ", " + Arrays.toString(K) + ", " + Arrays.toString(F) + ", " + Fmax + ", " + Vmax + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  报错清除后力传感器自动开启
     * @param  status 控制状态，0-关闭；1-开启
     * @return  错误码
     */
    public int SetForceSensorDragAutoFlag(int status)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {status};
            int rtn = (int)client.execute("SetForceSensorDragAutoFlag" , params);
            if (log != null)
            {
                log.LogInfo("SetForceSensorDragAutoFlag(" + status + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置六维力和关节阻抗混合拖动开关及参数
     * @param  status 控制状态，0-关闭；1-开启
     * @param  impedanceFlag 阻抗开启标志，0-关闭；1-开启
     * @param  lamdeGain 拖动增益
     * @param  KGain 刚度增益
     * @param  BGain 阻尼增益
     * @param  dragMaxTcpVel 拖动末端最大线速度限制
     * @param  dragMaxTcpOriVel 拖动末端最大角速度限制
     * @return  错误码
     */
    public int ForceAndJointImpedanceStartStop(int status, int impedanceFlag, Object[] lamdeGain, Object[] KGain, Object[] BGain, double dragMaxTcpVel, double dragMaxTcpOriVel)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {status, impedanceFlag, lamdeGain, KGain, BGain, dragMaxTcpVel, dragMaxTcpOriVel};
            int rtn = (int)client.execute("ForceAndJointImpedanceStartStop" , params);
            if (log != null)
            {
                log.LogInfo("ForceAndJointImpedanceStartStop(" + status + ", " + impedanceFlag + ", " + Arrays.toString(lamdeGain) + ", " + Arrays.toString(KGain) + ", " + Arrays.toString(BGain) + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  获取力传感器拖动开关状态
     * @return  List[0]:错误码; List[1] : dragState 力传感器辅助拖动控制状态，0-关闭；1-开启; List[1] : sixDimensionalDragState 六维力辅助拖动控制状态，0-关闭；1-开启
     */
    public List<Integer> GetForceAndTorqueDragState()
    {
        List<Integer> rtnArray = new ArrayList<Integer>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetForceAndTorqueDragState" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (int)result[1]);
                rtnArray.set(2, (int)result[2]);
            }
            if (log != null)
            {
                log.LogInfo("GetForceAndTorqueDragState(" + (int)result[1] + ",  " + (int)result[2] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  设置力传感器下负载重量
     * @param  weight 负载重量 kg
     * @return  错误码
     */
    public int SetForceSensorPayload(double weight)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {weight};
            int rtn = (int)client.execute("SetForceSensorPayload" , params);
            if (log != null)
            {
                log.LogInfo("SetForceSensorPayLoad(" + weight + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置力传感器下负载质心
     * @param  cog 负载质心 mm
     * @return  错误码
     */
    public int SetForceSensorPayloadCog(DescTran cog)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {cog.x, cog.y, cog.z};
            int rtn = (int)client.execute("SetForceSensorPayloadCog" , params);
            if (log != null)
            {
                log.LogInfo("SetForceSensorPayLoadCog(" + cog.x + ", " + cog.y + ", " + cog.z + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  获取力传感器下负载重量
     * @return  List[0]:错误码; List[1] : weight 负载重量 kg
     */
    public List<Number> GetForceSensorPayload()
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);

        if (IsSockComError())
        {
            rtnArray.set(0, sockErr);
            return rtnArray;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetForceSensorPayload" , params);
            rtnArray.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArray.set(1, (double)result[1]);
            }
            if (log != null)
            {
                log.LogInfo("GetForceSensorPayLoad(" + (double)result[1] + ") : " + (int)result[0]);
            }
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  获取力传感器下负载质心
     * @param  cog 负载质心 mm
     * @return  错误码
     */
    public int GetForceSensorPayloadCog(DescTran cog)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetForceSensorPayloadCog" , params);
            if ((int)result[0] == 0)
            {
                cog.x = (double)result[1];
                cog.y = (double)result[2];
                cog.z = (double)result[3];
            }
            if (log != null)
            {
                log.LogInfo("GetForceSensorPayLoadCog(" + cog.x + ",  " + cog.y + ",  " + cog.z + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  力传感器自动校零
     * @param  massCenter 传感器质量(kg) 及 质心(mm)
     * @return  错误码
     */
    public int ForceSensorAutoComputeLoad(MassCenter massCenter)
    {
        JointPos startJ = new JointPos(0, 0, 0, 0, 0, 0);
        DescPose startDesc = new DescPose(0, 0, 0, 0, 0, 0);
        GetActualJointPosDegree(startJ);
        GetActualTCPPose( startDesc);

        JointPos tmpJPos = new JointPos(0, 0, 0, 0, 0, 0);
        DescPose tmpDescPos = new DescPose(0, 0, 0, 0, 0, 0);
        DescPose offectPos = new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos tmpExaxisPos = new ExaxisPos(0, 0, 0, 0);

        ForceSensorSetSaveDataFlag(1);

        GetActualJointPosDegree( tmpJPos);
        if (tmpJPos.J3 < 0)
        {
            tmpJPos.J4 += 90;
            GetForwardKin(tmpJPos, tmpDescPos);
        }
        else
        {
            tmpJPos.J4 -= 90;
            GetForwardKin(tmpJPos, tmpDescPos);
        }
        MoveJ(tmpJPos, tmpDescPos, 0, 0, 100, 100, 100, tmpExaxisPos, -1, 0, offectPos);

        ForceSensorSetSaveDataFlag(2);

        GetActualJointPosDegree( tmpJPos);
        if (tmpJPos.J6 < 0)
        {
            tmpJPos.J6 += 90;
            GetForwardKin(tmpJPos, tmpDescPos);
        }
        else
        {
            tmpJPos.J6 -= 90;
            GetForwardKin(tmpJPos, tmpDescPos);
        }
        MoveJ(tmpJPos, tmpDescPos, 0, 0, 100, 100, 100, tmpExaxisPos, -1, 0, offectPos);

        ForceSensorSetSaveDataFlag(3);

        ForceSensorComputeLoad(massCenter);
        WaitMs(100);
        MoveJ(startJ, startDesc, 0, 0, 100, 100, 100, tmpExaxisPos, -1, 0, offectPos);
        return 0;
    }

    /**
     * @brief  传感器自动校零数据记录
     * @param  recordCount 记录数据个数 1-3
     * @return  错误码
     */
    public int ForceSensorSetSaveDataFlag(int recordCount)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {recordCount};
            int rtn = (int)client.execute("ForceSensorSetSaveDataFlag" , params);
            if (log != null)
            {
                log.LogInfo("ForceSensorSetSaveDataFlag(" + recordCount + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  传感器自动校零计算
     * @param  massCenter 传感器质量kg 及质心mm
     * @return  错误码
     */
    public int ForceSensorComputeLoad(MassCenter massCenter)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("ForceSensorComputeLoad" , params);
            if ((int)result[0] == 0)
            {
                massCenter.weight = (double)result[1];
                massCenter.cog.x = (double)result[2];
                massCenter.cog.y = (double)result[3];
                massCenter.cog.z = (double)result[4];
            }
            if (log != null)
            {
                log.LogInfo("ForceSensorComputeLoad(" + massCenter.weight + ",  " + massCenter.cog.x + ",  " + massCenter.cog.y + ",  " + massCenter.cog.z + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  段焊获取位置和姿态
     * @param  startPos 起始点坐标
     * @param  endPos 终止点坐标
     * @param  startDistance 焊接点至起点的长度
     * @param  weldPointDesc 焊接点的笛卡尔坐标信息
     * @param  weldPointJoint 焊接点的关节坐标信息
     * @param  coord 工具号和工件号
     * @return  错误码
     */
    public int GetSegmentWeldPoint(DescPose startPos, DescPose endPos, double startDistance, DescPose weldPointDesc, JointPos weldPointJoint, Coord coord)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {

            //log.LogInfo("segment input param {startPos.tran.x} {startPos.tran.y} {startPos.tran.z} {startPos.rpy.rx}");
            Object[] tmpStartDesc = { startPos.tran.x, startPos.tran.y, startPos.tran.z, startPos.rpy.rx, startPos.rpy.ry, startPos.rpy.rz };
            Object[] tmpEndDesc = { endPos.tran.x, endPos.tran.y, endPos.tran.z, endPos.rpy.rx, endPos.rpy.ry, endPos.rpy.rz };
            Object[] params = new Object[] {tmpStartDesc, tmpEndDesc, startDistance};
            Object[] result = (Object[])client.execute("GetSegmentWeldPoint" , params);
            if ((int)result[0] == 0)
            {
                String paramStr = (String)result[1];
                //System.out.println(paramStr);
                String[] parS = paramStr.split(",");
                if (parS.length != 14)
                {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "get segment weld point fail");
                    return -1;
                }
                weldPointJoint.J1 = Double.parseDouble(parS[0]);
                weldPointJoint.J2 = Double.parseDouble(parS[1]);
                weldPointJoint.J3 = Double.parseDouble(parS[2]);
                weldPointJoint.J4 = Double.parseDouble(parS[3]);
                weldPointJoint.J5 = Double.parseDouble(parS[4]);
                weldPointJoint.J6 = Double.parseDouble(parS[5]);

                weldPointDesc.tran.x = Double.parseDouble(parS[6]);
                weldPointDesc.tran.y = Double.parseDouble(parS[7]);
                weldPointDesc.tran.z = Double.parseDouble(parS[8]);
                weldPointDesc.rpy.rx = Double.parseDouble(parS[9]);
                weldPointDesc.rpy.ry = Double.parseDouble(parS[10]);
                weldPointDesc.rpy.rz = Double.parseDouble(parS[11]);

                coord.tool = (int)Double.parseDouble(parS[12]);
                coord.user = (int)Double.parseDouble(parS[13]);

                if (log != null)
                {
                    log.LogInfo("GetSegmentWeldPoint(" + startPos + ",  " + endPos + ", " + startDistance + ",  " + weldPointDesc + ") : " + (int)result[0]);
                }

                return (int)result[0];
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return (int) RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置焊接工艺曲线参数
     * @param  id 焊接工艺编号(1-99)
     * @param  param 焊接工艺参数
     * @return  错误码
     */
    public int WeldingSetProcessParam(int id, WeldingProcessParam param)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {id, param.startCurrent, param.startVoltage, param.startTime * 1.0, param.weldCurrent, param.weldVoltage, param.endCurrent, param.endVoltage, param.endTime * 1.0};
            int rtn = (int)client.execute("WeldingSetProcessParam" , params);
            if (log != null)
            {
                log.LogInfo("WeldingSetProcessParam(" + id + ", " + param.startCurrent + ", " + param.startVoltage + ", " + param.startTime + ", " + param.weldCurrent + ", " + param.weldVoltage + ", " + param.endCurrent + ", " + param.endVoltage + ", " + param.endTime + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  获取焊接工艺曲线参数
     * @param  id 焊接工艺编号(1-99)
     * @param  param 焊接工艺参数
     * @return  错误码
     */
    public int WeldingGetProcessParam(int id, WeldingProcessParam param)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {id};
            Object[] result = (Object[])client.execute("WeldingGetProcessParam" , params);
            if ((int)result[0] == 0)
            {
                param.startCurrent = (double)result[1];
                param.startVoltage = (double)result[2];
                param.startTime = (int)(double)result[3];
                param.weldCurrent = (double)result[4];
                param.weldVoltage = (double)result[5];
                param.endCurrent = (double)result[6];
                param.endVoltage = (double)result[7];
                param.endTime = (int)(double)result[8];
            }
            if (log != null)
            {
                log.LogInfo("WeldingGetProcessParam(" + id + ",  " + param.startCurrent + ",  " + param.startVoltage + ",  " + param.startTime + ",  " + param.weldCurrent + ",  " + param.weldVoltage + ",  " + param.endCurrent + ",  " + param.endVoltage + ",  " + param.endTime + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  末端传感器配置
     * @param  config idCompany 厂商，18-JUNKONG；25-HUIDE
     * @param  config idDevice 类型，0-JUNKONG/RYR6T.V1.0
     * @param  config idSoftware 软件版本，0-J1.0/HuiDe1.0(暂未开放)
     * @param  config idBus 挂载位置，1-末端1号口；2-末端2号口...8-末端8号口(暂未开放)
     * @return  错误码
     */
    public int AxleSensorConfig(DeviceConfig config)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {config.company, config.device, config.softwareVersion, config.bus};
            int rtn = (int)client.execute("AxleSensorConfig" , params);
            if (log != null)
            {
                log.LogInfo("AxleSensorConfig(" + config.company + ", " + config.device + ", " + config.softwareVersion + ", " + config.bus + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  获取末端传感器配置
     * @param  config idCompany 厂商，18-JUNKONG；25-HUIDE
     * @param  config idDevice 类型，0-JUNKONG/RYR6T.V1.0
     * @return  错误码
     */
    public int AxleSensorConfigGet(DeviceConfig config)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("AxleSensorConfigGet" , params);
            if ((int)result[0] == 0)
            {
                config.company = (int)result[1] + 1;
                config.device = (int)result[2];
            }
            if (log != null)
            {
                log.LogInfo("AxleSensorConfigGet(" + config.company + ",  " + config.device + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  末端传感器激活
     * @param  actFlag 0-复位；1-激活
     * @return  错误码
     */
    public int AxleSensorActivate(int actFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {actFlag};
            int rtn = (int)client.execute("AxleSensorActivate" , params);
            if (log != null)
            {
                log.LogInfo("AxleSensorActivate(" + actFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  末端传感器寄存器写入
     * @param  devAddr  设备地址编号 0-255
     * @param  regHAddr 寄存器地址高8位
     * @param  regLAddr 寄存器地址低8位
     * @param  regNum  寄存器个数 0-255
     * @param  data1 写入寄存器数值1
     * @param  data2 写入寄存器数值2
     * @param  isNoBlock 0-阻塞；1-非阻塞
     * @return  错误码
     */
    public int AxleSensorRegWrite(int devAddr, int regHAddr, int regLAddr, int regNum, int data1, int data2, int isNoBlock)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {devAddr, regHAddr, regLAddr, regNum, data1, data2, isNoBlock};
            int rtn = (int)client.execute("AxleSensorRegWrite" , params);
            if (log != null)
            {
                log.LogInfo("AxleSensorRegWrite(" + devAddr + ", " + regHAddr + ", " + regLAddr + ", " + regNum + ", " + data1 + ", " + data2 + ", " + isNoBlock + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置控制箱DO停止/暂停后输出是否复位
     * @param  resetFlag  0-不复位；1-复位
     * @return  错误码
     */
    public int SetOutputResetCtlBoxDO(int resetFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {resetFlag};
            int rtn = (int)client.execute("SetOutputResetCtlBoxDO" , params);
            if (log != null)
            {
                log.LogInfo("SetOutputResetCtlBoxDO(" + resetFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置控制箱AO停止/暂停后输出是否复位
     * @param  resetFlag  0-不复位；1-复位
     * @return  错误码
     */
    public int SetOutputResetCtlBoxAO(int resetFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {resetFlag};
            int rtn = (int)client.execute("SetOutputResetCtlBoxAO" , params);
            if (log != null)
            {
                log.LogInfo("SetOutputResetCtlBoxAO(" + resetFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置末端工具DO停止/暂停后输出是否复位
     * @param  resetFlag  0-不复位；1-复位
     * @return  错误码
     */
    public int SetOutputResetAxleDO(int resetFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {resetFlag};
            int rtn = (int)client.execute("SetOutputResetAxleDO" , params);
            if (log != null)
            {
                log.LogInfo("SetOutputResetAxleDO(" + resetFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置末端工具AO停止/暂停后输出是否复位
     * @param  resetFlag  0-不复位；1-复位
     * @return  错误码
     */
    public int SetOutputResetAxleAO(int resetFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {resetFlag};
            int rtn = (int)client.execute("SetOutputResetAxleAO" , params);
            if (log != null)
            {
                log.LogInfo("SetOutputResetAxleAO(" + resetFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置扩展DO停止/暂停后输出是否复位
     * @param  resetFlag  0-不复位；1-复位
     * @return  错误码
     */
    public int SetOutputResetExtDO(int resetFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {resetFlag};
            int rtn = (int)client.execute("SetOutputResetExtDO" , params);
            if (log != null)
            {
                log.LogInfo("SetOutputResetExtDO(" + resetFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief  设置扩展AO停止/暂停后输出是否复位
     * @param  resetFlag  0-不复位；1-复位
     * @return  错误码
     */
    public int SetOutputResetExtAO(int resetFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {resetFlag};
            int rtn = (int)client.execute("SetOutputResetExtAO" , params);
            if (log != null)
            {
                log.LogInfo("SetOutputResetExtAO(" + resetFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置SmartTool停止/暂停后输出是否复位
     * @param  resetFlag  0-不复位；1-复位
     * @return  错误码
     */
    public int SetOutputResetSmartToolDO(int resetFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {resetFlag};
            int rtn = (int)client.execute("SetOutputResetSmartToolDO" , params);
            if (log != null)
            {
                log.LogInfo("SetOutputResetSmartToolDO(" + resetFlag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  仿真摆动开始
     * @param  weaveNum  摆动参数编号
     * @return  错误码
     */
    public int WeaveStartSim(int weaveNum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {weaveNum};
            int rtn = (int)client.execute("WeaveStartSim" , params);
            if (log != null)
            {
                log.LogInfo("WeaveStartSim(" + weaveNum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  仿真摆动结束
     * @param  weaveNum  摆动参数编号
     * @return  错误码
     */
    public int WeaveEndSim(int weaveNum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {weaveNum};
            int rtn = (int)client.execute("WeaveEndSim" , params);
            if (log != null)
            {
                log.LogInfo("WeaveEndSim(" + weaveNum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  开始轨迹检测预警(不运动)
     * @param  weaveNum   摆动参数编号
     * @return  错误码
     */
    public int WeaveInspectStart(int weaveNum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {weaveNum};
            int rtn = (int)client.execute("WeaveInspectStart" , params);
            if (log != null)
            {
                log.LogInfo("WeaveInspectStart(" + weaveNum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 结束轨迹检测预警(不运动)
     * @param  weaveNum   摆动参数编号
     * @return  错误码
     */
    public int WeaveInspectEnd(int weaveNum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {weaveNum};
            int rtn = (int)client.execute("WeaveInspectEnd" , params);
            if (log != null)
            {
                log.LogInfo("WeaveInspectEnd(" + weaveNum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  摆动渐变开始
     * @param  weaveChangeFlag 1-变摆动参数；2-变摆动参数+焊接速度
     * @param  weaveNum 摆动编号
     * @param  velStart 焊接开始速度，(cm/min)
     * @param  velEnd 焊接结束速度，(cm/min)
     * @return  错误码
     */
    public int WeaveChangeStart(int weaveChangeFlag, int weaveNum, double velStart, double velEnd)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {weaveChangeFlag,weaveNum,velStart,velEnd};
            int rtn = (int)client.execute("WeaveChangeStart" , params);
            if (log != null)
            {
                log.LogInfo("WeaveChangeStart() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  摆动渐变结束
     * @return  错误码
     */
    public int WeaveChangeEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("WeaveChangeEnd" , params);
            if (log != null)
            {
                log.LogInfo("WeaveChangeEnd() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 扩展IO-配置焊机气体检测信号
     * @param  DONum  气体检测信号扩展DO编号
     * @return  错误码
     */
    public int SetAirControlExtDoNum(int DONum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {DONum};
            int rtn = (int)client.execute("SetAirControlExtDoNum" , params);
            if (log != null)
            {
                log.LogInfo("SetAirControlExtDoNum(" + DONum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 扩展IO-配置焊机起弧信号
     * @param  DONum  焊机起弧信号扩展DO编号
     * @return  错误码
     */
    public int SetArcStartExtDoNum(int DONum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {DONum};
            int rtn = (int)client.execute("SetArcStartExtDoNum" , params);
            if (log != null)
            {
                log.LogInfo("SetArcStartExtDoNum(" + DONum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 扩展IO-配置焊机反向送丝信号
     * @param  DONum  反向送丝信号扩展DO编号
     * @return  错误码
     */
    public int SetWireReverseFeedExtDoNum(int DONum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {DONum};
            int rtn = (int)client.execute("SetWireReverseFeedExtDoNum" , params);
            if (log != null)
            {
                log.LogInfo("SetWireReverseFeedExtDoNum(" + DONum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 扩展IO-配置焊机正向送丝信号
     * @param  DONum  正向送丝信号扩展DO编号
     * @return  错误码
     */
    public int SetWireForwardFeedExtDoNum(int DONum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {DONum};
            int rtn = (int)client.execute("SetWireForwardFeedExtDoNum" , params);
            if (log != null)
            {
                log.LogInfo("SetWireForwardFeedExtDoNum(" + DONum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 扩展IO-配置焊机起弧成功信号
     * @param  DINum  起弧成功信号扩展DI编号
     * @return  错误码
     */
    public int SetArcDoneExtDiNum(int DINum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {DINum};
            int rtn = (int)client.execute("SetArcDoneExtDiNum" , params);
            if (log != null)
            {
                log.LogInfo("SetArcDoneExtDINum(" + DINum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 扩展IO-配置焊机准备信号
     * @param  DINum  焊机准备信号扩展DI编号
     * @return  错误码
     */
    public int SetWeldReadyExtDiNum(int DINum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {DINum};
            int rtn = (int)client.execute("SetWeldReadyExtDiNum" , params);
            if (log != null)
            {
                log.LogInfo("SetWeldReadyExtDiNum(" + DINum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 扩展IO-配置焊接中断恢复信号
     * @param  reWeldDINum  焊接中断后恢复焊接信号扩展DI编号
     * @param  abortWeldDINum  焊接中断后退出焊接信号扩展DI编号
     * @return  错误码
     */
    public int SetExtDIWeldBreakOffRecover(int reWeldDINum, int abortWeldDINum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {reWeldDINum, abortWeldDINum};
            int rtn = (int)client.execute("SetExtDIWeldBreakOffRecover" , params);
            if (log != null)
            {
                log.LogInfo("SetExtDIWeldBreakOffRecover(" + reWeldDINum + "," + abortWeldDINum + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置机器人碰撞检测方法
     * @param  method 碰撞检测方法：0-电流模式；1-双编码器；2-电流和双编码器同时开启
     * @return  错误码
     */
    public int SetCollisionDetectionMethod(int method)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {method};
            int rtn = (int)client.execute("SetCollisionDetectionMethod" , params);
            if (log != null)
            {
                log.LogInfo("SetCollisionDetectionMethod(" + method + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置机器人碰撞检测方法
     * @param method 碰撞检测方法：0-电流模式；1-双编码器；2-电流和双编码器同时开启
     * @param thresholdMode 碰撞等级阈值方式；0-碰撞等级固定阈值方式；1-自定义碰撞检测阈值
     * @return 错误码
     */
    public int SetCollisionDetectionMethod(int method,int thresholdMode)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {method,thresholdMode};
            int rtn = (int)client.execute("SetCollisionDetectionMethod" , params);
            if (log != null)
            {
                log.LogInfo("SetCollisionDetectionMethod(" + method + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置静态下碰撞检测开始关闭
     * @param  status 0-关闭；1-开启
     * @return  错误码
     */
    public int SetStaticCollisionOnOff(int status)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {status};
            int rtn = 0;
            client.execute("SetStaticCollisionOnOff" , params);
            if (log != null)
            {
                log.LogInfo("SetStaticCollisionOnOff(" + status + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  自定义碰撞检测阈值功能开始，设置关节端和TCP端的碰撞检测阈值
     * @param   flag 1-仅关节检测开启；2-仅TCP检测开启；3-关节和TCP检测同时开启
     * @param   jointDetectionThreshould 关节碰撞检测阈值 j1-j6
     * @param   tcpDetectionThreshould  TCP碰撞检测阈值，xyzabc
     * @param   block 0-非阻塞；1-阻塞
     * @return  错误码
     */
    public int CustomCollisionDetectionStart(int flag, double[] jointDetectionThreshould, double[] tcpDetectionThreshould, int block)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try
        {
            Object[] params1 = new Object[]{jointDetectionThreshould[0],jointDetectionThreshould[1],jointDetectionThreshould[2],
                    jointDetectionThreshould[3],jointDetectionThreshould[4],jointDetectionThreshould[5]};
            Object[] params2 = new Object[]{tcpDetectionThreshould[0],tcpDetectionThreshould[1],tcpDetectionThreshould[2],
                    tcpDetectionThreshould[3],tcpDetectionThreshould[4],tcpDetectionThreshould[5]};
            Object[] params = new Object[] {flag,params1,params2,block};
            int rtn =(int)client.execute("CustomCollisionDetectionStart" , params);
            if (log != null)
            {
                log.LogInfo("CustomCollisionDetectionStart(" + flag + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  自定义碰撞检测阈值功能关闭
     * @return  错误码
     */
    public int CustomCollisionDetectionEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn =(int)client.execute("CustomCollisionDetectionEnd" , params);
            if (log != null)
            {
                log.LogInfo("CustomCollisionDetectionEnd() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 关节扭矩功率检测
     * @param  status 0-关闭；1-开启
     * @param  power 设定最大功率(W)
     * @return  错误码
     */
    public int SetPowerLimit(int status, double power)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {status, power};
            int rtn = (int)client.execute("SetPowerLimit" , params);
            if (log != null)
            {
                log.LogInfo("SetPowerLimit(" + status + "," + power + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 关节扭矩控制开始
     * @return  错误码
     */
    public int ServoJTStart()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ServoJTStart" , params);
            if (log != null)
            {
                log.LogInfo("ServoJTStart() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 关节扭矩控制
     * @param  torque j1~j6关节扭矩，单位Nm
     * @param  interval 指令周期，单位s，范围[0.001~0.008]
     * @return  错误码
     */
    public int ServoJT(Object[] torque, double interval)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {torque, interval};
            int rtn = (int)client.execute("ServoJT" , params);
            if (log != null)
            {
                log.LogInfo("ServoJT() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 关节扭矩控制
     * @param  torque j1~j6关节扭矩，单位Nm
     * @param  interval 指令周期，单位s，范围[0.001~0.008]
     * @return  错误码
     */
     public int ServoJT(double[] torque, double interval)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }
        int checkFlag = 0;
        double[] jPowerLimit =new double[] { 0.0,0.0,0.0,0.0,0.0,0.0 };
        double[] jVelLimit = new double[] { 0.0,0.0,0.0,0.0,0.0,0.0 };
        int errcode = ServoJT(torque, interval, checkFlag, jPowerLimit, jVelLimit);
        return errcode;
    }

    /**
     * @brief 关节扭矩控制
     * @param  torque j1~j6关节扭矩，单位Nm
     * @param  interval 指令周期，单位s，范围[0.001~0.008]
     * @param  checkFlag 检测策略 0-不限制；1-限制功率；2-限制速度；3-功率和速度同时限制
     * @param  jPowerLimit 关节最大功率限制(W)
     * @param  jVelLimit 关节最大速度(°/s)
     * @return  错误码
     */
    public int ServoJT(double[] torque, double interval, int checkFlag, double[] jPowerLimit, double[] jVelLimit)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try
        {
            Object[] tor=new Object[]{torque[0],torque[1],torque[2],torque[3],torque[4],torque[5]};
            Object[] jpow=new Object[]{jPowerLimit[0],jPowerLimit[1],jPowerLimit[2],jPowerLimit[3],jPowerLimit[4],jPowerLimit[5]};
            Object[] jvel=new Object[]{jVelLimit[0],jVelLimit[1],jVelLimit[2],jVelLimit[3],jVelLimit[4],jVelLimit[5]};

            Object[] params = new Object[] {tor, interval,checkFlag,jpow,jvel};
            int rtn = (int)client.execute("ServoJT" , params);
            if (log != null)
            {
                log.LogInfo("ServoJT() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief 关节扭矩控制结束
     * @return  错误码
     */
    public int ServoJTEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ServoJTEnd" , params);
            if (log != null)
            {
                log.LogInfo("ServoJTEnd() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置机器人 20004 端口反馈周期
     * @param period 机器人 20004 端口反馈周期(ms)
     * @return  错误码
     */
    public int SetRobotRealtimeStateSamplePeriod(int period)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {period};
            int rtn = (int)client.execute("SetRobotRealtimeStateSamplePeriod" , params);
            if (log != null)
            {
                log.LogInfo("SetRobotRealtimeStateSamplePeriod(" + period + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  获取机器人 20004 端口反馈周期
     * @return  List[0]:错误码; List[1]:机器人 20004 端口反馈周期(ms)
     */
    public List<Integer> GetRobotRealtimeStateSamplePeriod()
    {
        List<Integer> rtnArr = new ArrayList<Integer>(){};
        rtnArr.add(-1);
        rtnArr.add(-1);

        if (IsSockComError())
        {
            rtnArr.set(0, sockErr);
            return rtnArr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetRobotRealtimeStateSamplePeriod" , params);
            rtnArr.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArr.set(1, (int)result[1]);
            }
            if (log != null)
            {
                log.LogInfo("GetRobotRealtimeStateSamplePeriod(" + (int)result[1] + ") : " + (int)result[0]);
            }
            return rtnArr;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArr.set(0, RobotError.ERR_RPC_ERROR);
            return rtnArr;
        }
    }

    /**
     * @brief 获取机器人关节驱动器温度(℃)
     * @return 错误码
     */
    public int GetJointDriverTemperature(double[] temperature)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            ROBOT_STATE_PKG pkg = GetRobotRealTimeState();
            for(int i = 0; i < 6; i++)
            {
                temperature[i] = pkg.jointDriverTemperature[i];
            }
            return sockErr;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取机器人关节驱动器扭矩(Nm)
     * @return 错误码
     */
    public int GetJointDriverTorque(double[] torque)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            ROBOT_STATE_PKG pkg = GetRobotRealTimeState();
            for(int i = 0; i < 6; i++)
            {
                torque[i] = pkg.jointDriverTorque[i];
            }
            return sockErr;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 电弧追踪 + 多层多道补偿开启
     * @return 错误码
     */
    public int ArcWeldTraceReplayStart()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ArcWeldTraceReplayStart" , params);
            if (log != null)
            {
                log.LogInfo("ArcWeldTraceReplayStart() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 电弧追踪 + 多层多道补偿关闭
     * @return 错误码
     */
    public int ArcWeldTraceReplayEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("ArcWeldTraceReplayEnd" , params);
            if (log != null)
            {
                log.LogInfo("ArcWeldTraceReplayEnd() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 偏移量坐标变化-多层多道焊
     * @param pointO 基准点笛卡尔位姿
     * @param pointX 基准点X向偏移方向点笛卡尔位姿
     * @param pointZ 基准点Z向偏移方向点笛卡尔位姿
     * @param dx x方向偏移量(mm)
     * @param dz z方向偏移量(mm)
     * @param dry 绕y轴偏移量(°)
     * @param offset 计算结果偏移量
     * @return 错误码
     */
    public int MultilayerOffsetTrsfToBase(DescTran pointO, DescTran pointX, DescTran pointZ, double dx, double dz, double dry, DescPose offset)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {pointO.x, pointO.y, pointO.z, pointX.x, pointX.y, pointX.z, pointZ.x, pointZ.y, pointZ.z, dx, dz, dry};
            Object[] result = (Object[]) client.execute("MultilayerOffsetTrsfToBase" , params);
            if ((int)result[0] == 0)
            {
                offset.tran.x = (double)result[1];
                offset.tran.y = (double)result[2];
                offset.tran.z = (double)result[3];
                offset.rpy.rx = (double)result[4];
                offset.rpy.ry = (double)result[5];
                offset.rpy.rz = (double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("MultilayerOffsetTrsfToBase(" + pointO.x + "," + pointO.y + "," + pointO.z + "," + pointX.x + "," + pointX.y + "," + pointX.z + "," + pointZ.x + "," + pointZ.y + "," + pointZ.z + "," + dx + "," + dz + "," + dry + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 指定姿态速度开启
     * @param ratio 姿态速度百分比[0-300]
     * @return  错误码
     */
    public int AngularSpeedStart(int ratio)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {ratio};
            int rtn = (int)client.execute("AngularSpeedStart" , params);
            if (log != null)
            {
                log.LogInfo("AngularSpeedStart(" + ratio + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 指定姿态速度关闭
     * @return  错误码
     */
    public int AngularSpeedEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("AngularSpeedEnd" , params);
            if (log != null)
            {
                log.LogInfo("AngularSpeedEnd() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 机器人软件升级
     * @param filePath 软件升级包全路径
     * @param block 是否阻塞至升级完成 true:阻塞；false:非阻塞
     * @return  错误码
     */
    public int SoftwareUpgrade(String filePath, boolean block)
    {
        try
        {
            File fileInfo = new File(filePath);
            if (!fileInfo.exists())
            {
                return (int)RobotError.ERR_UPLOAD_FILE_NOT_FOUND;
            }

            int rtn = FileUpLoad(1, filePath);
            if (rtn == 0)
            {
                Object[] params = new Object[] {};
                rtn = (int)client.execute("SoftwareUpgrade" , params);

                if (rtn != 0)
                {
                    if (log != null)
                    {
                        log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "Software UpLoad fail");
                    }
                }

                if(block)
                {
                    Sleep(3000);
                    int state = GetSoftwareUpgradeState().get(1);
                    if(state == 0)
                    {
                        if (log != null)
                        {
                            log.LogError("software upgrade not start");
                            return -1;
                        }
                    }
                    while(state > 0 && state < 100)
                    {
                        Sleep(500);
                        state = GetSoftwareUpgradeState().get(1);
                    }

                    if(state == 100)
                    {
                        rtn = 0;
                    }
                    else
                    {
                        rtn = state;
                    }
                }

                if (log != null)
                {
                    log.LogInfo("SoftwareUpgrade(" + filePath + ") : " + rtn);
                }
                return rtn;
            }
            else
            {
                if (log != null)
                {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "Software UpLoad Fail(" + filePath +  ") : " + rtn );
                }
                return rtn;
            }
        }
        catch (Throwable e)
        {
            return RobotError.ERR_OTHER;
        }
    }

    /**
     * @brief  获取机器人软件升级状态
     * @return  List[0]:错误码; List[1]:机器人软件升级状态 0-空闲中或上传升级包中；1~100：升级完成百分比；-1:升级软件失败；-2：校验失败；-3：版本校验失败；-4：解压失败；-5：用户配置升级失败；-6：外设配置升级失败；-7：扩展轴配置升级失败；-8：机器人配置升级失败；-9：DH参数配置升级失败
     */
    public List<Integer> GetSoftwareUpgradeState()
    {
        List<Integer> rtnArr = new ArrayList<Integer>(){};
        rtnArr.add(-1);
        rtnArr.add(-1);

        if (IsSockComError())
        {
            rtnArr.set(0, sockErr);
            return rtnArr;
        }

        try
        {
            ROBOT_STATE_PKG pkg = GetRobotRealTimeState();
            rtnArr.set(1, pkg.softwareUpgradeState);
            return rtnArr;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }

            rtnArr.set(0, RobotError.ERR_RPC_ERROR);
            return rtnArr;
        }
    }

    /**
     * @brief 设置485扩展轴运动加减速度
     * @param acc 485扩展轴运动加速度
     * @param dec 485扩展轴运动减速度
     * @return  错误码
     */
    public int AuxServoSetAcc(double acc, double dec)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {acc, dec};
            int rtn = (int)client.execute("AuxServoSetAcc" , params);
            if (log != null)
            {
                log.LogInfo("AuxServoSetAcc(" + acc + "," + dec + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置485扩展轴急停加减速度
     * @param acc 485扩展轴急停加速度
     * @param dec 485扩展轴急停减速度
     * @return  错误码
     */
    public int AuxServoSetEmergencyStopAcc(double acc, double dec)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {acc, dec};
            int rtn = (int)client.execute("AuxServoSetEmergencyStopAcc" , params);
            if (log != null)
            {
                log.LogInfo("AuxServoSetEmergencyStopAcc(" + acc + "," + dec + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取485扩展轴运动加减速度
     * @return  List[0]:错误码; List[1]:485扩展轴运动加速度; List[2]:485扩展轴运动减速度
     */
    public List<Number> AuxServoGetAcc()
    {
        List<Number> rtnArr = new ArrayList<Number>(){};
        rtnArr.add(-1);
        rtnArr.add(-1);
        rtnArr.add(-1);

        if (IsSockComError())
        {
            rtnArr.set(0, sockErr);
            return rtnArr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("AuxServoGetAcc" , params);
            rtnArr.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArr.set(1, (double)result[1]);
                rtnArr.set(2, (double)result[2]);
            }
            if (log != null)
            {
                log.LogInfo("AuxServoGetAcc(" + (double)result[1] + "," + (double)result[2] + ") : " + (int)result[0]);
            }
            return rtnArr;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArr.set(0, RobotError.ERR_RPC_ERROR);
            return rtnArr;
        }
    }

    /**
     * @brief 获取485扩展轴运动加减速度
     * @return  List[0]:错误码; List[1]:485扩展轴急停加速度; List[2]:485扩展轴急停减速度
     */
    public List<Number> AuxServoGetEmergencyStopAcc()
    {
        List<Number> rtnArr = new ArrayList<Number>(){};
        rtnArr.add(-1);
        rtnArr.add(-1);
        rtnArr.add(-1);

        if (IsSockComError())
        {
            rtnArr.set(0, sockErr);
            return rtnArr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("AuxServoGetEmergencyStopAcc" , params);
            rtnArr.set(0, (int)result[0]);
            if ((int)result[0] == 0)
            {
                rtnArr.set(1, (double)result[1]);
                rtnArr.set(2, (double)result[2]);
            }
            if (log != null)
            {
                log.LogInfo("AuxServoGetEmergencyStopAcc(" + (double)result[1] + "," + (double)result[2] + ") : " + (int)result[0]);
            }
            return rtnArr;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArr.set(0, RobotError.ERR_RPC_ERROR);
            return rtnArr;
        }
    }

    /**
     * @brief 获取末端通讯参数
     * @param param 末端通讯参数
     * @return  错误码
     */
    public int GetAxleCommunicationParam(AxleComParam param)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetAxleCommunicationParam" , params);
            if ((int)result[0] == 0)
            {
                param.baudRate = (int)result[1];
                param.dataBit = (int)result[2];
                param.stopBit = (int)result[3];
                param.verify = (int)result[4];
                param.timeout = (int)result[5];
                param.timeoutTimes = (int)result[6];
                param.period = (int)result[7];
            }
            if (log != null)
            {
                log.LogInfo("GetAxleCommunicationParam(" + param.baudRate + ",  " + param.dataBit + ",  " + param.stopBit + ",  " + param.verify + ",  " + param.timeout + ",  " + param.timeoutTimes + ",  " + param.period + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置末端通讯参数
     * @param param  末端通讯参数
     * @return  错误码
     */
    public int SetAxleCommunicationParam(AxleComParam param)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {param.baudRate, param.dataBit, param.stopBit, param.verify, param.timeout, param.timeoutTimes, param.period};
            int rtn = (int)client.execute("SetAxleCommunicationParam" , params);
            if (log != null)
            {
                log.LogInfo("SetAxleCommunicationParam(" + param.baudRate + "," + param.dataBit + "," + param.stopBit + "," + param.verify + "," + param.timeout + "," + param.timeoutTimes + "," + param.period + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置末端文件传输类型
     * @param type 1-MCU升级文件；2-LUA文件
     * @return  错误码
     */
    public int SetAxleFileType(int type)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {type};
            int rtn = (int)client.execute("SetAxleFileType" , params);
            if (log != null)
            {
                log.LogInfo("SetAxleFileType(" + type + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置启用末端LUA执行
     * @param enable 0-不启用；1-启用
     * @return  错误码
     */
    public int SetAxleLuaEnable(int enable)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {enable};
            int rtn = (int)client.execute("SetAxleLuaEnable" , params);
            if (log != null)
            {
                log.LogInfo("SetAxleLuaEnable(" + enable + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 末端LUA文件异常错误恢复
     * @param status 0-不恢复；1-恢复
     * @return  错误码
     */
    public int SetRecoverAxleLuaErr(int status)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {status};
            int rtn = (int)client.execute("SetRecoverAxleLuaErr" , params);
            if (log != null)
            {
                log.LogInfo("SetRecoverAxleLuaErr(" + status + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取末端LUA执行使能状态
     * @param status status[0]: 0-未使能；1-已使能
     * @return  错误码
     */
    public int GetAxleLuaEnableStatus(int[] status)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetAxleLuaEnableStatus" , params);
            if ((int)result[0] == 0)
            {
                status[0] = (int)result[1];
            }
            if (log != null)
            {
                log.LogInfo("GetAxleLuaEnableStatus(" + status[0] + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置末端LUA末端设备启用类型
     * @param forceSensorEnable 力传感器启用状态，0-不启用；1-启用
     * @param gripperEnable 夹爪启用状态，0-不启用；1-启用
     * @param IOEnable IO设备启用状态，0-不启用；1-启用
     * @return  错误码
     */
    public int SetAxleLuaEnableDeviceType(int forceSensorEnable, int gripperEnable, int IOEnable)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {forceSensorEnable, gripperEnable, IOEnable};
            int rtn = (int)client.execute("SetAxleLuaEnableDeviceType" , params);
            if (log != null)
            {
                log.LogInfo("SetAxleLuaEnableDeviceType(" + forceSensorEnable + "," + gripperEnable + "," + IOEnable + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取末端LUA末端设备启用类型
     * @param enable enable[0]:forceSensorEnable 力传感器启用状态，0-不启用；1-启用
     * @param enable enable[1]:gripperEnable 夹爪启用状态，0-不启用；1-启用
     * @param enable enable[2]:IOEnable IO设备启用状态，0-不启用；1-启用
     * @return  错误码
     */
    public int GetAxleLuaEnableDeviceType(int[] enable)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetAxleLuaEnableDeviceType" , params);
            if ((int)result[0] == 0)
            {
                enable[0] = (int)result[1];
                enable[1] = (int)result[2];
                enable[2] = (int)result[3];
            }
            if (log != null)
            {
                log.LogInfo("GetAxleLuaEnableDeviceType(" + enable[0] + "," + enable[1] + "," + enable[2] + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取当前配置的末端设备
     * @param forceSensorEnable 力传感器启用设备编号 0-未启用；1-启用
     * @param gripperEnable 夹爪启用设备编号，0-不启用；1-启用
     * @param IODeviceEnable IO设备启用设备编号，0-不启用；1-启用
     * @return  错误码
     */
    public int GetAxleLuaEnableDevice(int[] forceSensorEnable, int[] gripperEnable, int[] IODeviceEnable)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        String paramStr = "";
        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetAxleLuaEnableDevice" , params);
            if ((int)result[0] == 0)
            {
                paramStr = (String)result[1];
                String[] parS = paramStr.split(",");
                if (parS.length != 24)
                {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "get segment weld point fail");
                    return -1;
                }
                forceSensorEnable[0] = Integer.parseInt(parS[0]);
                forceSensorEnable[1] = Integer.parseInt(parS[1]);
                forceSensorEnable[2] = Integer.parseInt(parS[2]);
                forceSensorEnable[3] = Integer.parseInt(parS[3]);
                forceSensorEnable[4] = Integer.parseInt(parS[4]);
                forceSensorEnable[5] = Integer.parseInt(parS[5]);
                forceSensorEnable[6] = Integer.parseInt(parS[6]);
                forceSensorEnable[7] = Integer.parseInt(parS[7]);

                gripperEnable[0] = Integer.parseInt(parS[8]);
                gripperEnable[1] = Integer.parseInt(parS[9]);
                gripperEnable[2] = Integer.parseInt(parS[10]);
                gripperEnable[3] = Integer.parseInt(parS[11]);
                gripperEnable[4] = Integer.parseInt(parS[12]);
                gripperEnable[5] = Integer.parseInt(parS[13]);
                gripperEnable[6] = Integer.parseInt(parS[14]);
                gripperEnable[7] = Integer.parseInt(parS[15]);

                IODeviceEnable[0] = Integer.parseInt(parS[16]);
                IODeviceEnable[1] = Integer.parseInt(parS[17]);
                IODeviceEnable[2] = Integer.parseInt(parS[18]);
                IODeviceEnable[3] = Integer.parseInt(parS[19]);
                IODeviceEnable[4] = Integer.parseInt(parS[20]);
                IODeviceEnable[5] = Integer.parseInt(parS[21]);
                IODeviceEnable[6] = Integer.parseInt(parS[22]);
                IODeviceEnable[7] = Integer.parseInt(parS[23]);
            }
            if (log != null)
            {
                log.LogInfo("GetAxleLuaEnableDevice(" + paramStr + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置启用夹爪动作控制功能
     * @param id 夹爪设备编号
     * @param func func[0]-夹爪使能；func[1]-夹爪初始化；2-位置设置；3-速度设置；4-力矩设置；6-读夹爪状态；7-读初始化状态；8-读故障码；9-读位置；10-读速度；11-读力矩
     * @return  错误码
     */
    public int SetAxleLuaGripperFunc(int id, int[] func)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] funcs = new Object[]{func[0], func[1], func[2], func[3], func[4], func[5], func[6], func[7], func[8], func[9], func[10], func[11], func[12], func[13], func[14], func[15]};
            Object[] params = new Object[] {id, funcs};
            int rtn = (int)client.execute("SetAxleLuaGripperFunc" , params);
            if (log != null)
            {
                log.LogInfo("SetAxleLuaGripperFunc(" + id + "," + Arrays.toString(func) + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取启用夹爪动作控制功能
     * @param id 夹爪设备编号
     * @param func func[0]-夹爪使能；func[1]-夹爪初始化；2-位置设置；3-速度设置；4-力矩设置；6-读夹爪状态；7-读初始化状态；8-读故障码；9-读位置；10-读速度；11-读力矩
     * @return  错误码
     */
    public int GetAxleLuaGripperFunc(int id, int[] func)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        String paramStr = "";
        try
        {
            Object[] params = new Object[] {id};
            Object[] result = (Object[])client.execute("GetAxleLuaGripperFunc" , params);
            if ((int)result[0] == 0)
            {
                paramStr = (String)result[1];
                String[] parS = paramStr.split(",");
                if (parS.length != 16)
                {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "get segment weld point fail");
                    return -1;
                }
                func[0] = Integer.parseInt(parS[0]);
                func[1] = Integer.parseInt(parS[1]);
                func[2] = Integer.parseInt(parS[2]);
                func[3] = Integer.parseInt(parS[3]);
                func[4] = Integer.parseInt(parS[4]);
                func[5] = Integer.parseInt(parS[5]);
                func[6] = Integer.parseInt(parS[6]);
                func[7] = Integer.parseInt(parS[7]);
                func[8] = Integer.parseInt(parS[8]);
                func[9] = Integer.parseInt(parS[9]);
                func[10] = Integer.parseInt(parS[10]);
                func[11] = Integer.parseInt(parS[11]);
                func[12] = Integer.parseInt(parS[12]);
                func[13] = Integer.parseInt(parS[13]);
                func[14] = Integer.parseInt(parS[14]);
                func[15] = Integer.parseInt(parS[15]);
            }
            if (log != null)
            {
                log.LogInfo("GetAxleLuaGripperFunc(" + paramStr + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  获取旋转夹爪的旋转圈数
     * @return  List[0]:错误码 List[1]: 0-无错误，1-有错误 List[2]:旋转圈数
     */
    public List<Number> GetGripperRotNum()
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);
        rtnArray.add(-1);

        rtnArray.set(0, sockErr);
        if (IsSockComError())
        {
            return rtnArray;
        }

        try
        {
            ROBOT_STATE_PKG pkg = GetRobotRealTimeState();
            rtnArray.set(1, (int)pkg.gripper_fault);
            rtnArray.set(2, (double)pkg.gripperRotNum);
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  获取旋转夹爪的旋转速度百分比
     * @return  List[0]:错误码 List[1]: 0-无错误，1-有错误 List[2]:旋转速度百分比
     */
    public List<Number> GetGripperRotSpeed()
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);
        rtnArray.add(-1);

        rtnArray.set(0, sockErr);
        if (IsSockComError())
        {
            return rtnArray;
        }

        try
        {
            ROBOT_STATE_PKG pkg = GetRobotRealTimeState();
            rtnArray.set(1, (int)pkg.gripper_fault);
            rtnArray.set(2, (int)pkg.gripperRotSpeed);
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief  获取旋转夹爪的旋转力矩百分比
     * @return  List[0]:错误码 List[1]: 0-无错误，1-有错误 List[2]:旋转力矩百分比
     */
    public List<Number> GetGripperRotTorque()
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);
        rtnArray.add(-1);

        rtnArray.set(0, sockErr);
        if (IsSockComError())
        {
            return rtnArray;
        }

        try
        {
            ROBOT_STATE_PKG pkg = GetRobotRealTimeState();
            rtnArray.set(1, (int)pkg.gripper_fault);
            rtnArray.set(2, (int)pkg.gripperRotTorque);
            return rtnArray;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            rtnArray.set(0, (int)RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief 开始Ptp运动FIR滤波
     * @param  maxAcc 最大加速度极值(deg/s2)
     * @return 错误码
     */
    public int PtpFIRPlanningStart(double maxAcc)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {maxAcc};
            int rtn = (int)client.execute("PtpFIRPlanningStart" , params);
            if (log != null)
            {
                log.LogInfo("PtpFIRPlanningStart(" + maxAcc +") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 开始Ptp运动FIR滤波
     * @param maxAcc 最大加速度极值(deg/s2)
     * @param maxJek 统一关节急动度极值(deg/s3)
     * @return 错误码
     */
    public int PtpFIRPlanningStart(double maxAcc,double maxJek)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {maxAcc,maxJek};
            int rtn = (int)client.execute("PtpFIRPlanningStart" , params);
            if (log != null)
            {
                log.LogInfo("PtpFIRPlanningStart(" + maxAcc +") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 关闭Ptp运动FIR滤波
     * @return 错误码
     */
    public int PtpFIRPlanningEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("PtpFIRPlanningEnd" , params);
            if (log != null)
            {
                log.LogInfo("PtpFIRPlanningEnd() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 上传轨迹J文件
     * @param filePath 上传轨迹文件的全路径名   C://test/testJ.txt
     * @return 错误码
     */
    public int TrajectoryJUpLoad(String filePath)
    {
        return FileUpLoad(20, filePath);
    }

    /**
     * @brief 删除轨迹J文件
     * @param fileName 文件名称 testJ.txt
     * @return 错误码
     */
    public int TrajectoryJDelete(String fileName)
    {
        return FileDelete(20, fileName);
    }

    /**
     * @brief 开始LIN、ARC运动FIR滤波
     * @param  maxAccLin 线加速度极值(mm/s2)
     * @param  maxAccDeg 角加速度极值(deg/s2)
     * @param  maxJerkLin 线加加速度极值(mm/s3)
     * @param  maxJerkDeg 角加加速度极值(deg/s3)
     * @return 错误码
     */
    public int LinArcFIRPlanningStart(double maxAccLin, double maxAccDeg, double maxJerkLin, double maxJerkDeg)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {maxAccLin,maxAccDeg,maxJerkLin,maxJerkDeg};
            int rtn = (int)client.execute("LinArcFIRPlanningStart" , params);
            if (log != null)
            {
                log.LogInfo("LinArcFIRPlanningStart(): "+rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 关闭LIN、ARC运动FIR滤波
     * @return 错误码
     */
    public int LinArcFIRPlanningEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("LinArcFIRPlanningEnd" , params);
            if (log != null)
            {
                log.LogInfo("LinArcFIRPlanningEnd(): "+rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置控制器外设协议LUA文件名
     * @param id 协议编号
     * @param name lua文件名称 “CTRL_LUA_test.lua”
     * @return  错误码
     */
    public int SetCtrlOpenLUAName(int id, String name)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {id, name};
            int rtn = (int)client.execute("SetCtrlOpenLUAName" , params);
            if (log != null)
            {
                log.LogInfo("SetCtrlOpenLUAName(" + id + "," + name + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取当前配置的控制器外设协议LUA文件名
     * @param name 4个lua文件名称 “CTRL_LUA_test.lua”
     * @return  错误码
     */
    public int GetCtrlOpenLUAName(String[] name)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        String paramStr = "";
        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetCtrlOpenLUAName" , params);
            if ((int)result[0] == 0)
            {
                paramStr = (String)result[1];
                String[] parS = paramStr.split(",");
                if (parS.length != 16)
                {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "get segment weld point fail");
                    return -1;
                }
                name[0] = parS[0];
                name[1] = parS[1];
                name[2] = parS[2];
                name[3] = parS[3];
            }
            if (log != null)
            {
                log.LogInfo("GetCtrlOpenLUAName(" + paramStr + ") : " + (int)result[0]);
            }
            return (int)result[0];
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 加载控制器LUA协议
     * @param id 控制器LUA协议编号
     * @return  错误码
     */
    public int LoadCtrlOpenLUA(int id)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {id};
            int rtn = (int)client.execute("LoadCtrlOpenLUA" , params);
            if (log != null)
            {
                log.LogInfo("LoadCtrlOpenLUA(" + id + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 卸载控制器LUA协议
     * @param id 控制器LUA协议编号
     * @return  错误码
     */
    public int UnloadCtrlOpenLUA(int id)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {id};
            int rtn = (int)client.execute("UnloadCtrlOpenLUA" , params);
            if (log != null)
            {
                log.LogInfo("UnloadCtrlOpenLUA(" + id + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置控制器LUA协议错误码
     * @param id 控制器LUA协议编号
     * @return  错误码
     */
    private int SetCtrlOpenLuaErrCode(int id, int code)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {id, code};
            int rtn = (int)client.execute("SetCtrlOpenLuaErrCode" , params);
            if (log != null)
            {
                log.LogInfo("SetCtrlOpenLuaErrCode(" + id + "," + code + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 机器人Ethercat从站文件写入
     * @param type 从站文件类型，1-升级从站文件；2-升级从站配置文件
     * @param slaveID 从站号
     * @param fileName 上传文件名
     * @return  错误码
     */
    public int SlaveFileWrite(int type, int slaveID, String fileName)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {type, slaveID, fileName};
            int rtn = (int)client.execute("SlaveFileWrite" , params);
            if (log != null)
            {
                log.LogInfo("SlaveFileWrite(" + type + "," + slaveID + "," + fileName + ") : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 机器人Ethercat从站进入boot模式
     * @return  错误码
     */
    public int SetSysServoBootMode()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try
        {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("SetSysServoBootMode" , params);
            if (log != null)
            {
                log.LogInfo("SetSysServoBootMode() : " + rtn);
            }
            return rtn;
        }
        catch (Throwable e)
        {
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 上传末端Lua开放协议文件
     * @param filePath 本地lua文件路径名 ".../AXLE_LUA_End_DaHuan.lua"
     * @return 错误码
     */
    public int AxleLuaUpload(String filePath)
    {
        try
        {
            File fileInfo = new File(filePath);
            if (!fileInfo.exists())
            {
                return (int)RobotError.ERR_UPLOAD_FILE_NOT_FOUND;
            }

            int rtn = FileUpLoad(10, filePath);
            if (rtn == 0)
            {
                String fileName = "/tmp/" + fileInfo.getName();
                rtn = SetAxleFileType(2);
                if(rtn != 0)
                {
                    return -1;
                }
                rtn = SetSysServoBootMode();
                if(rtn != 0)
                {
                    return -1;
                }
                rtn = SlaveFileWrite(1, 7, fileName);
                if(rtn != 0)
                {
                    return -1;
                }
                return 0;
            }
            else
            {

                if (log != null)
                {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "LuaUpLoadFail(" + filePath + ") : " + rtn );
                }
                return rtn;
            }
        }
        catch (Throwable e)
        {
            return RobotError.ERR_OTHER;
        }

    }

    /**
     * @brief 可移动装置使能
     * @param enable false-去使能；true-使能
     * @return 错误码
     */
    public int TractorEnable(Boolean enable)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        int int_enable = enable ? 1 : 0;
        try{
            Object[] params = new Object[] {int_enable};
            int rtn = (int)client.execute("TractorEnable" , params);
            if (log != null)
            {
                log.LogInfo("TractorEnable(): " + rtn);
            }
            return rtn;
        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 可移动装置回零
     * @return 错误码
     */
    public int TractorHoming()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try{
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("TractorHoming" , params);
            if (log != null)
            {
                log.LogInfo("TractorHoming(): " + rtn);
            }
            return rtn;
        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 可移动装置直线运动
     * @param distance 直线运动距离（mm）
     * @param vel 直线运动速度百分比（0-100）
     * @return 错误码
     */
    public int TractorMoveL(double distance, double vel)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try{
            Object[] params = new Object[] {distance,vel};
            int rtn = (int)client.execute("TractorMoveL" , params);
            if (log != null)
            {
                log.LogInfo("TractorMoveL(): " + rtn);
            }
            return rtn;
        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 可移动装置圆弧运动
     * @param radio 圆弧运动半径（mm）
     * @param angle 圆弧运动角度（°）
     * @param vel 直线运动速度百分比（0-100）
     * @return 错误码
     */
    public int TractorMoveC(double radio, double angle, double vel)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try{
            Object[] params = new Object[] {radio,angle,vel};
            int rtn = (int)client.execute("TractorMoveC" , params);
            if (log != null)
            {
                log.LogInfo("TractorMoveC(): " + rtn);
            }
            return rtn;
        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 可移动装置停止运动
     * @return 错误码
     */
    public int TractorStop()
    {
        int rtn =ProgramStop();
        return rtn;
    }

    /**
     * @brief 设置焊丝寻位扩展IO端口
     * @param searchDoneDINum 焊丝寻位成功DO端口(0-127)
     * @param searchStartDONum 焊丝寻位启停控制DO端口(0-127)
     * @return 错误码
     */
    public int SetWireSearchExtDIONum(int searchDoneDINum, int searchStartDONum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try{
            Object[] params = new Object[] {searchDoneDINum,searchStartDONum};
            int rtn = (int)client.execute("SetWireSearchExtDIONum" , params);
            if (log != null)
            {
                log.LogInfo("SetWireSearchExtDIONum(): " + rtn);
            }
            return rtn;
        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置焊机控制模式扩展DO端口
     * @param DONum 焊机控制模式DO端口(0-127)
     * @return 错误码
     */
    public int SetWeldMachineCtrlModeExtDoNum(int DONum)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try{
            Object[] params = new Object[] {DONum};
            int rtn = (int)client.execute("SetWeldMachineCtrlModeExtDoNum" , params);
            if (log != null)
            {
                log.LogInfo("SetWeldMachineCtrlModeExtDoNum(): " + rtn);
            }
            return rtn;
        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置焊机控制模式
     * @param mode 焊机控制模式;0-一元化
     * @return 错误码
     */
    public int SetWeldMachineCtrlMode(int mode)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try{
            Object[] params = new Object[] {mode};
            int rtn = (int)client.execute("SetWeldMachineCtrlMode" , params);
            if (log != null)
            {
                log.LogInfo("SetWeldMachineCtrlMode(): " + rtn);
            }
            return rtn;
        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 开始奇异位姿保护
     * @param protectMode 奇异保护模式，0：关节模式；1-笛卡尔模式
     * @param minShoulderPos 肩奇异调整范围(mm), 默认100
     * @param minElbowPos 肘奇异调整范围(mm), 默认50
     * @param minWristPos 腕奇异调整范围(°), 默认10
     * @return 错误码
     */
    public int SingularAvoidStart(int protectMode, double minShoulderPos, double minElbowPos, double minWristPos)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try {
            Object[] params = new Object[] {protectMode,minShoulderPos,minElbowPos,minWristPos};
            int rtn = (int)client.execute("SingularAvoidStart" , params);
            if (log != null)
            {
                log.LogInfo("SingularAvoidStart(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief 停止奇异位姿保护
     * @return 错误码
     */
    public int SingularAvoidEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        try {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("SingularAvoidEnd" , params);
            if (log != null)
            {
                log.LogInfo("SingularAvoidEnd(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  控制器日志下载
     * @param  savePath 保存文件路径"D://zDown/"
     * @return  错误码
     */
    public int RbLogDownload(String savePath)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        int errcode=0;
        try {
            Object[] params = new Object[] {};
            errcode= (int)client.execute("RbLogDownloadPrepare" , params);
            if (log != null)
            {
                log.LogInfo("RbLogDownloadPrepare(): " + errcode);
            }
            if(errcode!=0){
                log.LogInfo("RbLogDownloadPrepare fail.");
                return errcode;
            }
            log.LogInfo("RbLogDownloadPrepare success.");

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
        String fileName = "rblog.tar.gz";
        errcode = FileDownLoad(1, fileName, savePath);
        return errcode;
    }

    /**
     * @brief 所有数据源下载
     * @param savePath 保存文件路径"D://zDown/"
     * @return  错误码
     */
    public int AllDataSourceDownload(String savePath)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        int errcode=0;
        try {
            Object[] params = new Object[] {};
            errcode= (int)client.execute("AllDataSourceDownloadPrepare" , params);
            if (log != null)
            {
                log.LogInfo("AllDataSourceDownloadPrepare(): " + errcode);
            }
            if(errcode!=0){
                log.LogInfo("AllDataSourceDownloadPrepare fail.");
                return errcode;
            }
            log.LogInfo("AllDataSourceDownloadPrepare success.");

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
        String fileName = "alldatasource.tar.gz";
        errcode = FileDownLoad(2, fileName, savePath);
        return errcode;
    }

    /**
     * @brief 数据备份包下载
     * @param savePath 保存文件路径"D://zDown/"
     * @return  错误码
     */
    public int DataPackageDownload(String savePath)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        int errcode=0;
        try {
            Object[] params = new Object[] {};
            errcode= (int)client.execute("DataPackageDownloadPrepare" , params);
            if (log != null)
            {
                log.LogInfo("DataPackageDownloadPrepare(): " + errcode);
            }
            if(errcode!=0){
                log.LogInfo("DataPackageDownloadPrepare fail.");
                return errcode;
            }
            log.LogInfo("DataPackageDownloadPrepare success.");

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
        String fileName = "fr_user_data.tar.gz";
        errcode = FileDownLoad(3, fileName, savePath);
        return errcode;
    }

    /**
     * @brief 获取控制箱SN码
     * @param SNCode 控制箱SN码
     * @return 错误码
     */
    public int GetRobotSN(String[] SNCode)
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        int errcode=0;
        try {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetRobotSN" , params);
            errcode=(int)result[0];
            if (log != null)
            {
                log.LogInfo("GetRobotSN(): " + errcode);
            }
            if (0 == errcode)
            {
                SNCode[0] =(String)result[1];
            }
            return errcode;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 关闭机器人操作系统
     * @return 错误码
     */
    public int ShutDownRobotOS()
    {
        if (IsSockComError())
        {
            return sockErr;
        }
        int errcode=0;
        try {
            Object[] params = new Object[] {};
            errcode= (int)client.execute("ShutDownRobotOS" , params);
            if (log != null)
            {
                log.LogInfo("ShutDownRobotOS(): " + errcode);
            }
            return errcode;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    public int GetSafetyCode()
    {
        ROBOT_STATE_PKG pkg = GetRobotRealTimeState();
        if (pkg.safety_stop0_state == 1 || pkg.safety_stop1_state == 1)
        {
            return 99;
        }
        return 0;
    }

    /**
     * @brief 加速度平滑开启
     * @param  saveFlag 是否断电保存
     * @return  错误码
     */
    public int AccSmoothStart(boolean saveFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }
        int errcode = 0;

        try {
            int flag=saveFlag? 1 : 0;
            Object[] params = new Object[] {flag};

            errcode = (int)client.execute("AccSmoothStart" , params);
            if (log != null)
            {
                log.LogInfo("AccSmoothStart(): " + errcode);
            }

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

        ROBOT_STATE_PKG pkg = GetRobotRealTimeState();
        if ((pkg.main_code != 0 || pkg.sub_code != 0) && errcode == 0)
        {
            errcode = 14;
        }

        return errcode;
    }

    /**
     * @brief 加速度平滑关闭
     * @param saveFlag 是否断电保存
     * @return  错误码
     */
    public int AccSmoothEnd(boolean saveFlag)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try {
            int flag=saveFlag? 1 : 0;
            Object[] params = new Object[] {flag};

            int rtn = (int)client.execute("AccSmoothEnd" , params);
            if (log != null)
            {
                log.LogInfo("AccSmoothEnd(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 传送带通讯输入检测
     * @param timeout 等待超时时间ms
     * @return 错误码
     */
    public int ConveyorComDetect(int timeout)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try {
            Object[] params = new Object[] {timeout};
            int rtn = (int)client.execute("ConveyorComDetect" , params);
            if (log != null)
            {
                log.LogInfo("ConveyorComDetect(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    // 类成员变量
    private static int cnt = 0;
    /**
     * @brief 传送带通讯输入检测触发
     * @return 错误码
     */
    public int ConveyorComDetectTrigger()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try {
            int errcode = 0;

            while (isSendCmd == true) //说明当前正在处理上一条指令
            {
                Thread.sleep(10);
            }

            sendBuf = "/f/bIII"+cnt+"III1149III25IIIConveyorComDetectTrigger()III/b/f";
            System.out.println(sendBuf);
            clientCmd.Send(sendBuf);
            byte[] recvBuf = new byte[1024];
            clientCmd.Recv(recvBuf);
            cnt++;
            isSendCmd = true;  // 设置发送标志

            System.out.println("ConveryComDetectTrigger() executed. Count: " + cnt);

            if (log != null)
            {
                log.LogInfo("ConveryComDetectTrigger(): " + errcode);
            }
            return errcode;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 电弧跟踪焊机电流反馈AI通道选择
     * @param  channel 通道；0-扩展AI0；1-扩展AI1；2-扩展AI2；3-扩展AI3；4-控制箱AI0；5-控制箱AI1
     * @return 错误码
     */
    public int ArcWeldTraceAIChannelCurrent(int channel)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try {
            Object[] params = new Object[] {channel};
            int rtn = (int)client.execute("ArcWeldTraceAIChannelCurrent" , params);
            if (log != null)
            {
                log.LogInfo("ArcWeldTraceAIChannelCurrent(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 电弧跟踪焊机电压反馈AI通道选择
     * @param  channel 通道；0-扩展AI0；1-扩展AI1；2-扩展AI2；3-扩展AI3；4-控制箱AI0；5-控制箱AI1
     * @return 错误码
     */
    public int ArcWeldTraceAIChannelVoltage(int channel)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try {
            Object[] params = new Object[] {channel};
            int rtn = (int)client.execute("ArcWeldTraceAIChannelVoltage" , params);
            if (log != null)
            {
                log.LogInfo("ArcWeldTraceAIChannelVoltage(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 电弧跟踪焊机电流反馈转换参数
     * @param AILow AI通道下限，默认值0V，范围[0-10V]
     * @param AIHigh AI通道上限，默认值10V，范围[0-10V]
     * @param currentLow AI通道下限对应焊机电流值，默认值0V，范围[0-200V]
     * @param currentHigh AI通道上限对应焊机电流值，默认值100V，范围[0-200V]
     * @return 错误码
     */
    public int ArcWeldTraceCurrentPara(double AILow, double AIHigh, double currentLow, double currentHigh)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try {
            Object[] params = new Object[] {AILow,AIHigh,currentLow,currentHigh};
            int rtn = (int)client.execute("ArcWeldTraceCurrentPara" , params);
            if (log != null)
            {
                log.LogInfo("ArcWeldTraceCurrentPara(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 电弧跟踪焊机电压反馈转换参数
     * @param AILow AI通道下限，默认值0V，范围[0-10V]
     * @param AIHigh AI通道上限，默认值10V，范围[0-10V]
     * @param voltageLow AI通道下限对应焊机电压值，默认值0V，范围[0-200V]
     * @param voltageHigh AI通道上限对应焊机电压值，默认值100V，范围[0-200V]
     * @return 错误码
     */
    public int ArcWeldTraceVoltagePara(double AILow, double AIHigh, double voltageLow, double voltageHigh)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try {
            Object[] params = new Object[] {AILow,AIHigh,voltageLow,voltageHigh};
            int rtn = (int)client.execute("ArcWeldTraceVoltagePara" , params);
            if (log != null)
            {
                log.LogInfo("ArcWeldTraceVoltagePara(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置焊接电压渐变开始
     * @param IOType 控制类型；0-控制箱IO；1-数字通信协议(UDP);2-数字通信协议(ModbusTCP)
     * @param voltageStart 起始焊接电压(V)
     * @param voltageEnd 终止焊接电压(V)
     * @param AOIndex 控制箱AO端口号(0-1)
     * @param blend 是否平滑 0-不平滑；1-平滑
     * @return 错误码
     */
    public int WeldingSetVoltageGradualChangeStart(int IOType, double voltageStart, double voltageEnd, int AOIndex, int blend)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try {
            Object[] params = new Object[] {IOType,voltageStart,voltageEnd,AOIndex,blend};
            int rtn = (int)client.execute("WeldingSetVoltageGradualChangeStart" , params);
            if (log != null)
            {
                log.LogInfo("WeldingSetVoltageGradualChangeStart(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置焊接电压渐变结束
     * @return 错误码
     */
    public int WeldingSetVoltageGradualChangeEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("WeldingSetVoltageGradualChangeEnd" , params);
            if (log != null)
            {
                log.LogInfo("WeldingSetVoltageGradualChangeEnd(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置焊接电流渐变开始
     * @param IOType 控制类型；0-控制箱IO；1-数字通信协议(UDP);2-数字通信协议(ModbusTCP)
     * @param currentStart 起始焊接电流(A)
     * @param currentEnd 终止焊接电流(A)
     * @param AOIndex 控制箱AO端口号(0-1)
     * @param blend 是否平滑 0-不平滑；1-平滑
     * @return 错误码
     */
    public int WeldingSetCurrentGradualChangeStart(int IOType, double currentStart, double currentEnd, int AOIndex, int blend)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try {
            Object[] params = new Object[] {IOType,currentStart,currentEnd,AOIndex,blend};
            int rtn = (int)client.execute("WeldingSetCurrentGradualChangeStart" , params);
            if (log != null)
            {
                log.LogInfo("WeldingSetCurrentGradualChangeStart(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置焊接电流渐变结束
     * @return 错误码
     */
    public int WeldingSetCurrentGradualChangeEnd()
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        try {
            Object[] params = new Object[] {};
            int rtn = (int)client.execute("WeldingSetCurrentGradualChangeEnd" , params);
            if (log != null)
            {
                log.LogInfo("WeldingSetCurrentGradualChangeEnd(): " + rtn);
            }
            return rtn;

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 获取SmartTool按钮状态
     * @param state SmartTool手柄按钮状态;(bit0:0-通信正常；1-通信掉线；bit1-撤销操作；bit2-清空程序；
    bit3-A键；bit4-B键；bit5-C键；bit6-D键；bit7-E键；bit8-IO键；bit9-手自动；bit10开始)
     * @return 错误码
     */
    public int GetSmarttoolBtnState(int[] state)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();
        state[0]=(int)robot_state_pkg.smartToolState;
        return 0;
    }

    /**
     * @brief 获取扩展轴坐标系
     * @param  coord 扩展轴坐标系
     * @return 错误码
     */
    public int ExtAxisGetCoord(DescPose coord)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("ExtAxisGetCoord" , params);
            if ((int)result[0] == 0)
            {
                coord.tran.x = (double)result[1];
                coord.tran.y = (double)result[2];
                coord.tran.z = (double)result[3];
                coord.rpy.rx = (double)result[4];
                coord.rpy.ry = (double)result[5];
                coord.rpy.rz = (double)result[6];
            }
            return (int)result[0];

        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 下发SCP指令
     * @param  mode 0-上传（上位机->控制器），1-下载（控制器->上位机）
     * @param  sshname 上位机用户名
     * @param  sship 上位机ip地址
     * @param  usr_file_url 上位机文件路径
     * @param  robot_file_url 机器人控制器文件路径
     * @return 错误码
     */
    public int SetSSHScpCmd(int mode, String sshname, String sship, String usr_file_url, String robot_file_url)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try {
            Object[] params = new Object[] {mode,sshname,sship,usr_file_url,robot_file_url};
            int rtn = (int)client.execute("SetSSHScpCmd" , params);
            return rtn;
        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置宽电压控制箱温度及风扇转速监控参数
     * @param enable 0-不使能监测；1-使能监测
     * @param period 监测周期(s),范围1-100
     * @return 错误码
     */
    public int SetWideBoxTempFanMonitorParam(int enable, int period)
    {
        if (IsSockComError())
        {
            return sockErr;
        }

        try {
            Object[] params = new Object[] {enable, period};
            int rtn = (int)client.execute("SetWideBoxTempFanMonitorParam" , params);
            return rtn;
        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 获取宽电压控制箱温度及风扇转速监控参数
     * @return List[0]-错误码,List[1]-enable 0-不使能监测；1-使能监测,List[2]-period 监测周期(s),范围1-100
     */
    public List<Number> GetWideBoxTempFanMonitorParam()
    {
        List<Number> rtnArray = new ArrayList<Number>() {};
        rtnArray.add(-1);
        rtnArray.add(-1);
        rtnArray.add(-1);

        rtnArray.set(0, sockErr);
        if (IsSockComError())
        {
            return rtnArray;
        }

        try {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetWideBoxTempFanMonitorParam" , params);
            rtnArray.add(0,(int)result[0]);
            rtnArray.add(1,(double)result[1]);
            rtnArray.add(2,(double)result[2]);
            return rtnArray;
        }catch (Throwable e){
            if (log != null)
            {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            rtnArray.set(0,RobotError.ERR_RPC_ERROR);
            return rtnArray;
        }
    }

    /**
     * @brief 设置焦点标定点
     * @param pointNum 焦点标定点编号 1-8
     * @param point    标定点坐标
     * @return 错误码
     */
    public int SetFocusCalibPoint(int pointNum, DescPose point)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{pointNum, point.tran.x, point.tran.y, point.tran.z, point.rpy.rx, point.rpy.ry, point.rpy.rz};
            int rtn = (int) client.execute("SetFocusCalibPoint", params);
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 计算焦点标定结果
     * @param pointNum  标定点个数
     * @param resultPos 标定结果XYZ
     * @param accuracy  标定精度误差
     * @return 错误码
     */
    public int ComputeFocusCalib(int pointNum, DescTran resultPos, double[] accuracy) {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{pointNum};
            Object[] result = (Object[]) client.execute("ComputeFocusCalib", params);
            if ((int) result[0] == 0) {
                resultPos.x = (double) result[1];
                resultPos.y = (double) result[2];
                resultPos.z = (double) result[3];
                accuracy[0] = (double) result[4];
            }
            return (int) result[0];
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 开启焦点跟随
     * @param kp       比例参数，默认50.0
     * @param kpredict 前馈参数，默认19.0
     * @param aMax     最大角加速度限制，默认1440°/s^2
     * @param vMax     最大角速度限制，默认180°/s
     * @param type     锁定X轴指向(0-参考输入矢量；1-水平；2-垂直)
     * @return 错误码
     */
    public int FocusStart(double kp, double kpredict, double aMax, double vMax, int type) {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{kp, kpredict, aMax, vMax, type};
            int rtn = (int) client.execute("FocusStart", params);

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 停止焦点跟随
     * @return 错误码
     */
    public int FocusEnd() {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{};
            int rtn = (int) client.execute("FocusEnd", params);

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 设置焦点坐标
     * @param pos 焦点坐标XYZ
     * @return 错误码
     */
    public int SetFocusPosition(DescTran pos) {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{pos.x, pos.y, pos.z};
            int rtn = (int) client.execute("SetFocusPosition", params);

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置编码器升级
     * @param path 本地升级包全路径(D://zUP/XXXXX.bin)
     * @return 错误码
     */
    public int SetEncoderUpgrade(String path) {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{};
            int rtn = (int) client.execute("SetEncoderUpgrade", params);

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 设置关节固件升级
     * @param type 升级文件类型；1-升级固件；2-升级从站配置文件
     * @param path 本地升级包全路径(D://zUP/XXXXX.bin)
     * @return 错误码
     */
    public int SetJointFirmwareUpgrade(int type, String path) {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            int errcode = FileUpLoad(2, path);
            if (0 == errcode) {
                File f = new File(path);
                String pathInRobot = "/tmp/" + f.getName();
                for (int i = 1; i < 7; i++) {
                    errcode = SlaveFileWrite(1, i, pathInRobot);
                    if (errcode != 0) {
                        return errcode;
                    }
                }
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 设置控制箱固件升级
     * @param type 升级文件类型；1-升级固件；2-升级从站配置文件
     * @param path 本地升级包全路径(D://zUP/XXXXX.bin)
     * @return 错误码
     */
    public int SetCtrlFirmwareUpgrade(int type, String path) {
        if (IsSockComError()) {
            return sockErr;
        }
        try {
            int errcode = FileUpLoad(2, path);
            if (0 == errcode) {
                File f = new File(path);
                String pathInRobot = "/tmp/" + f.getName();
                errcode = SlaveFileWrite(type, 0, pathInRobot);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 设置末端固件升级
     * @param type 升级文件类型；1-升级固件；2-升级从站配置文件
     * @param path 本地升级包全路径(D://zUP/XXXXX.bin)
     * @return 错误码
     */
    public int SetEndFirmwareUpgrade(int type, String path) {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            int errcode = FileUpLoad(2, path);
            if (0 == errcode) {
                File f = new File(path);
                String pathInRobot = "/tmp/" + f.getName();
                errcode = SlaveFileWrite(type, 7, pathInRobot);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 关节全参数配置文件升级
     * @param path 本地升级包全路径(D://zUP/XXXXX.bin)
     * @return 错误码
     */
    public int JointAllParamUpgrade(String path)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            int errcode = FileUpLoad(5, path);
            if (0 == errcode) {
                Object[] params = new Object[]{};
                errcode = (int) client.execute("JointAllParamUpgrade", params);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 激光传感器记录点
     * @param  coordID 激光传感器坐标系
     * @param  desc 激光传感器识别点笛卡尔位置
     * @param  joint 激光传感器识别点关节位置
     * @param  exaxis 激光传感器识别点扩展轴位置
     * @return 错误码
     */
    public int LaserRecordPoint(int coordID, DescPose desc, JointPos joint, ExaxisPos exaxis)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{coordID, 0 ,100};

            Object[] result = (Object[]) client.execute("LaserRecordPoint", params);
            int rtn = (int) result[0];
            if (rtn == 0) {
                String paramStr = (String) result[1];
                String[] parS = paramStr.split(",");

                joint.J1  = Double.parseDouble(parS[0]);
                joint.J2  = Double.parseDouble(parS[1]);
                joint.J3  = Double.parseDouble(parS[2]);
                joint.J4  = Double.parseDouble(parS[3]);
                joint.J5  = Double.parseDouble(parS[4]);
                joint.J6  = Double.parseDouble(parS[5]);


                desc.tran.x= Double.parseDouble(parS[6]);
                desc.tran.y= Double.parseDouble(parS[7]);
                desc.tran.z= Double.parseDouble(parS[8]);
                desc.rpy.rx= Double.parseDouble(parS[9]);
                desc.rpy.ry= Double.parseDouble(parS[10]);
                desc.rpy.rz= Double.parseDouble(parS[11]);


                exaxis.axis1 = Double.parseDouble(parS[12]);
                exaxis.axis2 = Double.parseDouble(parS[13]);
                exaxis.axis3 = Double.parseDouble(parS[14]);
                exaxis.axis4 = Double.parseDouble(parS[15]);

            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置扩展轴与机器人同步运动策略
     * @param strategy 策略；0-以机器人为主；1-扩展轴与机器人同步
     * @return 错误码
     */
    public int SetExAxisRobotPlan(int strategy)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{strategy};
            int rtn = (int) client.execute("SetExAxisRobotPlan", params);
            if (log != null)
            {
                log.LogInfo("SetExAxisRobotPlan:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }
    
    /**
     * @brief 上传开放协议的Lua文件
     * @param  filePath 本地开放协议lua文件路径名
     * @return 错误码
     */
    public int OpenLuaUpload(String filePath)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {

            /* 上传 */
            int errcode = FileUpLoad(11, filePath);
            if (0 == errcode)
            {
                /* 提取文件名称 */
                int pos = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
                if (pos == -1) {
                    log.LogInfo("format of path is wrong, should be like /home/fd/xxx.lua");
                    return RobotError.ERR_FILE_NAME;
                }
                String filename = filePath.substring(pos + 1);

                Object[] params = new Object[]{filename};
                Object[] result = (Object[])client.execute("CtrlOpenLuaUpLoadCheck", params);
                errcode=(int)result[0];

                if (errcode==0)
                {
                    String res_str = (String)result[1];
                    log.LogInfo("lua format, error code is:"+errcode+" ,"+ res_str);
                }
            }

            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  获取从站板卡参数
     * @param  type  0-Ethercat，1-CClink, 3-Ethercat, 4-EIP
     * @param  version  协议版本
     * @param  connState  0-未连接 1-已连接
     * @return  错误码
     */
    public int GetFieldBusConfig(int[] type, int[] version, int[] connState)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        try {
            Object[] params = new Object[]{};
            Object[] result = (Object[]) client.execute("GetFieldBusConfig", params);
            int rtn = (int) result[0];
            if (rtn == 0) {
                type[0] = (int)result[2];
                version[0] = (int)result[3];
                connState[0] = (int)result[4];
            }
            if (log != null)
            {
                log.LogInfo("GetFieldBusConfig:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  写入从站DO
     * @param   DOIndex  DO编号
     * @param   wirteNum  写入的数量
     * @param   status 写入的数值，最多写8个
     * @return  错误码
     */
    public int FieldBusSlaveWriteDO(int DOIndex, int wirteNum, int[] status)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] cur_status={status[0],status[1],status[2],status[3],status[4],status[5],status[6],status[7]};
            Object[] params = new Object[]{DOIndex,wirteNum,cur_status};

            int rtn = (int) client.execute("FieldBusSlaveWriteDO", params);

            if (log != null)
            {
                log.LogInfo("FieldBusSlaveWriteDO:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  写入从站AO
     * @param  AOIndex  AO编号
     * @param  wirteNum  写入的数量
     * @param  status 写入的数值，最多写8个
     * @return  错误码
     */
    public int FieldBusSlaveWriteAO(int AOIndex, int wirteNum, int[] status)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] cur_status={status[0],status[1],status[2],status[3],status[4],status[5],status[6],status[7]};
            Object[] params = new Object[]{AOIndex,wirteNum,cur_status};

            int rtn = (int) client.execute("FieldBusSlaveWriteAO", params);

            if (log != null)
            {
                log.LogInfo("FieldBusSlaveWriteAO:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  读取从站DI
     * @param  DOIndex  DI编号
     * @param  readNum  读取的数量
     * @param  status 读取到的数值，最多读8个
     * @return  错误码
     */
    public int FieldBusSlaveReadDI(int DOIndex, int readNum, int[] status)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{DOIndex,readNum};

            Object[] result = (Object[]) client.execute("FieldBusSlaveReadDI", params);
            int rtn=(int)result[0];
            if(rtn==0){
                for (int i = 0; i < readNum; i++)
                {
                    status[i] = (int)result[i+1];
                }
            }
            if (log != null)
            {
                log.LogInfo("FieldBusSlaveReadDI:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  读取从站AI
     * @param  AIIndex  AI编号
     * @param  readNum  读取的数量
     * @param  status 读取到的数值，最多读8个
     * @return  错误码
     */
    public int FieldBusSlaveReadAI(int AIIndex, int readNum, double[] status)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{AIIndex,readNum};

            Object[] result = (Object[]) client.execute("FieldBusSlaveReadAI", params);
            int rtn=(int)result[0];
            if(rtn==0){
                for (int i = 0; i < readNum; i++)
                {
                    status[i] = (double)result[i + 1];
                }
            }
            if (log != null)
            {
                log.LogInfo("FieldBusSlaveReadAI:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 等待扩展DI输入
     * @param  DIIndex DI编号
     * @param  status 0-低电平；1-高电平
     * @param  waitMs 最大等待时间(ms)
     * @return 错误码
     */
    public int FieldBusSlaveWaitDI(int DIIndex, int status, int waitMs)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{DIIndex,status,waitMs};

            int rtn = (int) client.execute("FieldBusSlaveWaitDI", params);

            if (log != null)
            {
                log.LogInfo("FieldBusSlaveWaitDI:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 等待扩展AI输入
     * @param  AIIndex AI编号
     * @param  waitType 0-大于；1-小于
     * @param  value AI值
     * @param  waitMs 最大等待时间(ms)
     * @return 错误码
     */
    public int FieldBusSlaveWaitAI(int AIIndex, int waitType, double value, int waitMs)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{AIIndex,waitType,value,waitMs};

            int rtn = (int) client.execute("FieldBusSlaveWaitAI", params);

            if (log != null)
            {
                log.LogInfo("FieldBusSlaveWaitAI:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 控制阵列式吸盘
     * @param  slaveID 从站号
     * @param  len 长度
     * @param  ctrlValue 控制值 1-按最大真空度吸取 2-按设定真空度吸取 3-停止吸取
     * @return 错误码
     */
    public int SetSuckerCtrl(int slaveID, int len, int[] ctrlValue)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] cur =new Object[len];
            for (int i = 0; i < len; i++)
            {
                cur[i] = ctrlValue[i];
            }
            Object[] params = new Object[]{slaveID,len,cur};

            int rtn = (int) client.execute("SetSuckerCtrl", params);

            if (log != null)
            {
                log.LogInfo("SetSuckerCtrl:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取阵列式吸盘状态
     * @param  slaveID 从站号
     * @param  state 吸附状态 0-释放物体 1-检测到工件吸附成功 2-没有吸附到物体 3-物体脱离
     * @param  pressValue 当前真空度 单位kpa
     * @param  error 吸盘当前的错误码
     * @return 错误码
     */
    public int GetSuckerState(int slaveID, int[] state, int[] pressValue, int[] error)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{slaveID};

            Object[] result = (Object[])client.execute("GetSuckerState", params);
            int rtn=(int)result[0];
            if(rtn==0){
                state[0] = (int)result[1];
                pressValue[0] = (int)result[2];
                error[0] = (int)result[3];
            }
            if (log != null)
            {
                log.LogInfo("GetSuckerState:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 等待吸盘状态
     * @param  slaveID 从站号
     * @param  state 吸附状态 0-释放物体 1-检测到工件吸附成功 2-没有吸附到物体 3-物体脱离
     * @param  ms 等待最大时间
     * @return 错误码
     */
    public int WaitSuckerState(int slaveID, int state, int ms)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{slaveID,state,ms};

            int rtn = (int)client.execute("WaitSuckerState", params);

            if (log != null)
            {
                log.LogInfo("WaitSuckerState:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 设置拖动开启前负载力检测
     * @param flag 0-关闭；1-开启
     * @return 错误码
     */
    public int SetTorqueDetectionSwitch(int flag)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{flag};

            int rtn = (int)client.execute("SetTorqueDetectionSwitch", params);

            if (log != null)
            {
                log.LogInfo("SetTorqueDetectionSwitch:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 激光外设打开关闭函数
     * @param  OnOff 0-关闭 1-打开
     * @param  weldId 焊缝ID 默认为0
     * @return 错误码
     */
    public int LaserTrackingLaserOnOff(int OnOff, int weldId)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{OnOff,weldId};

            int rtn = (int)client.execute("LaserTrackingLaserOnOff", params);

            if (log != null)
            {
                log.LogInfo("LaserTrackingLaserOnOff:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 激光跟踪开始结束函数
     * @param  OnOff 0-结束 1-开始
     * @param  coordId 激光外设工具坐标系编号
     * @return 错误码
     */
    public int LaserTrackingTrackOnOff(int OnOff, int coordId)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{OnOff,coordId};

            int rtn = (int)client.execute("LaserTrackingTrackOnOff", params);

            if (log != null)
            {
                log.LogInfo("LaserTrackingTrackOnOff:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

     /**
     * @brief 激光寻位-固定反向
     * @param  direction 0-x+ 1-x- 2-y+ 3-y- 4-z+ 5-z-
     * @param  vel 速度 单位%
     * @param  distance 最大寻位距离 单位mm
     * @param  timeout 寻位超时时间 单位ms
     * @param  posSensorNum 激光标定的工具坐标编号
     * @return 错误码
     */
    public int LaserTrackingSearchStart_xyz(int direction, int vel, int distance, int timeout, int posSensorNum)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{direction,vel,distance,timeout,posSensorNum};

            int rtn = (int)client.execute("LaserTrackingSearchStart_xyz", params);

            if (log != null)
            {
                log.LogInfo("LaserTrackingSearchStart_xyz:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 激光寻位-任意方向
     * @param  directionPoint 寻位输入的点的xyz左边
     * @param  vel 速度 单位%
     * @param  distance 最大寻位距离 单位mm
     * @param  timeout 寻位超时时间 单位ms
     * @param  posSensorNum 激光标定的工具坐标编号
     * @return 错误码
     */
    public int LaserTrackingSearchStart_point(DescTran directionPoint, int vel, int distance, int timeout, int posSensorNum)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{6,vel,distance,timeout,posSensorNum,directionPoint.x,directionPoint.y,directionPoint.z};

            int rtn = (int)client.execute("LaserTrackingSearchStart_point", params);

            if (log != null)
            {
                log.LogInfo("LaserTrackingSearchStart_point:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 激光IP配置
     * @param  ip 激光外设的ip地址
     * @param  port 激光外设的端口号
     * @return 错误码
     */
    public int LaserTrackingSensorConfig(String ip, int port)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{ip,port};

            int rtn = (int)client.execute("LaserTrackingSensorConfig", params);

            if (log != null)
            {
                log.LogInfo("LaserTrackingSensorConfig:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 激光外设采样周期配置
     * @param period 激光外设采样周期 单位ms
     * @return 错误码
     */
    public int LaserTrackingSensorSamplePeriod(int period)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{period};

            int rtn = (int)client.execute("LaserTrackingSensorSamplePeriod", params);

            if (log != null)
            {
                log.LogInfo("LaserTrackingSensorSamplePeriod:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 激光外设驱动加载
     * @param  type 激光外设驱动的协议类型 101-睿牛 102-创想 103-全视 104-同舟 105-奥太
     * @return 错误码
     */
    public int LoadPosSensorDriver(int type)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{type};

            int rtn = (int)client.execute("LoadPosSensorDriver", params);

            if (log != null)
            {
                log.LogInfo("LoadPosSensorDriver:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 激光外设驱动卸载
     * @return 错误码
     */
    public int UnLoadPosSensorDriver()
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{};

            int rtn = (int)client.execute("UnLoadPosSensorDriver", params);

            if (log != null)
            {
                log.LogInfo("UnLoadPosSensorDriver:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 激光焊缝轨迹记录
     * @param  status 0-停止记录 1-实时跟踪  2-开始记录
     * @param  delayTime 延时时间 单位ms
     * @return 错误码
     */
    public int LaserSensorRecord1(int status, int delayTime)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{status,delayTime};

            int rtn = (int)client.execute("LaserSensorRecord1", params);

            if (log != null)
            {
                log.LogInfo("LaserSensorRecord1:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 激光焊缝轨迹复现
     * @param  delayTime 延时时间 单位ms
     * @param  speed 速度 单位%
     * @return 错误码
     */
    public int LaserSensorReplay(int delayTime, double speed)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{3,delayTime,speed};

            int rtn = (int)client.execute("LaserSensorReplay", params);

            if (log != null)
            {
                log.LogInfo("LaserSensorReplay:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 激光跟踪复现
     * @return 错误码
     */
    public int MoveLTR()
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{0};

            int rtn = (int)client.execute("MoveLTR", params);

            if (log != null)
            {
                log.LogInfo("MoveLTR:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 激光焊缝轨迹复现
     * @param  delayMode 模式 0-延时时间 1-延时距离
     * @param  delayTime 延时时间 单位ms
     * @param  delayDisExAxisNum 扩展轴编号
     * @param  delayDis 延时距离 单位mm
     * @param  sensitivePara 补偿灵敏系数
     * @param  speed 速度 单位%
     * @return 错误码
     */
    public int LaserSensorRecordandReplay(int delayMode, int delayTime, int delayDisExAxisNum, double delayDis, double sensitivePara, double speed)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{4,delayMode,delayTime,delayDisExAxisNum,delayDis,sensitivePara,speed};

            int rtn = (int)client.execute("LaserSensorRecordandReplay", params);

            if (log != null)
            {
                log.LogInfo("LaserSensorRecordandReplay:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 运动到焊缝记录的起点
     * @param  moveType 0-PTP 1-LIN
     * @param  ovl 速度 单位%
     * @return 错误码
     */
    public int MoveToLaserRecordStart(int moveType, double ovl)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{moveType,ovl};

            int rtn = (int)client.execute("MoveToLaserRecordStart", params);

            if (log != null)
            {
                log.LogInfo("MoveToLaserRecordStart:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 运动到焊缝记录的终点
     * @param  moveType 0-PTP 1-LIN
     * @param  ovl 速度 单位%
     * @return 错误码
     */
    public int MoveToLaserRecordEnd(int moveType, double ovl)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{moveType,ovl};

            int rtn = (int)client.execute("MoveToLaserRecordEnd", params);

            if (log != null)
            {
                log.LogInfo("MoveToLaserRecordEnd:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 运动到激光传感器寻位点
     * @param  moveFlag 运动类型：0-PTP；1-LIN
     * @param  ovl 速度缩放因子，0-100
     * @param  dataFlag 焊缝缓存数据选择：0-执行规划数据；1-执行记录数据
     * @param  plateType 板材类型：0-波纹板；1-瓦楞板；2-围栏板；3-油桶；4-波纹甲壳钢
     * @param  trackOffectType 激光传感器偏移类型：0-不偏移；1-基坐标系偏移；2-工具坐标系偏移；3-激光传感器原始数据偏移
     * @param  offset 偏移量
     * @return 错误码
     */
    public int MoveToLaserSeamPos(int moveFlag, double ovl, int dataFlag, int plateType, int trackOffectType, DescPose offset)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{moveFlag,ovl,dataFlag,plateType,trackOffectType,offset.tran.x,offset.tran.y,offset.tran.z,offset.rpy.rx,offset.rpy.ry,offset.rpy.rz};
            Object[] param = new Object[]{params};
            int rtn = (int)client.execute("MoveToLaserSeamPos", param);

            if (log != null)
            {
                log.LogInfo("MoveToLaserSeamPos:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 获取激光传感器寻位点坐标信息
     * @param  trackOffectType 激光传感器偏移类型：0-不偏移；1-基坐标系偏移；2-工具坐标系偏移；3-激光传感器原始数据偏移
     * @param  offset 偏移量
     * @param  jPos 关节位置[°]
     * @param  descPos 笛卡尔位置[mm]
     * @param  tool 工具坐标系
     * @param  user 工件坐标系
     * @param  exaxis 扩展轴位置[mm]
     * @return 错误码
     */
    public int GetLaserSeamPos(int trackOffectType, DescPose offset, JointPos jPos, DescPose descPos, int[] tool, int[] user, ExaxisPos exaxis)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{trackOffectType,offset.tran.x,offset.tran.y,offset.tran.z,offset.rpy.rx,offset.rpy.ry,offset.rpy.rz};
            Object[] param = new Object[]{params};

            Object[] result = (Object[])client.execute("GetLaserSeamPos", param) ;
            int rtn = (int)result[0];
            if(rtn==0){
                String paramStr = (String)result[1];

                // 假设 result 是一个数组或列表，result[1] 是字符串类型
                System.out.println(paramStr + "\n");

                // 使用split方法分割字符串
                String[] parS = paramStr.split(",");
                if (parS.length != 20) {
                    log.LogInfo("get SeamPos Point size fail");
                    return -1;
                }

                // 假设 jPos 是一个包含 jPos 数组的对象
                jPos.J1 = Double.parseDouble(parS[0]);
                jPos.J2 = Double.parseDouble(parS[1]);
                jPos.J3 = Double.parseDouble(parS[2]);
                jPos.J4 = Double.parseDouble(parS[3]);
                jPos.J5 = Double.parseDouble(parS[4]);
                jPos.J6 = Double.parseDouble(parS[5]);

                // 假设 descPos 是一个包含 tran 和 rpy 的对象
                descPos.tran.x = Double.parseDouble(parS[6]);
                descPos.tran.y = Double.parseDouble(parS[7]);
                descPos.tran.z = Double.parseDouble(parS[8]);
                descPos.rpy.rx = Double.parseDouble(parS[9]);
                descPos.rpy.ry = Double.parseDouble(parS[10]);
                descPos.rpy.rz = Double.parseDouble(parS[11]);

                // 解析整数
                tool[0] = Integer.parseInt(parS[12]);
                user[0] = Integer.parseInt(parS[13]);

                // 假设 exaxis 是一个包含 ePos 数组的对象
                exaxis.axis1 = Double.parseDouble(parS[16]);
                exaxis.axis2 = Double.parseDouble(parS[17]);
                exaxis.axis3 = Double.parseDouble(parS[18]);
                exaxis.axis4 = Double.parseDouble(parS[19]);
            }

            if (log != null)
            {
                log.LogInfo("GetLaserSeamPos:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 阻抗启停控制
     * @param  status 0：关闭；1-开启
     * @param  workSpace 0-关节空间；1-迪卡尔空间
     * @param  forceThreshold 触发力阈值(N)
     * @param  m 质量参数
     * @param  b 阻尼参数
     * @param  k 刚度参数
     * @param  maxV 最大线速度(mm/s)
     * @param  maxVA 最大线加速度(mm/s2)
     * @param  maxW 最大角速度(°/s)
     * @param  maxWA 最大角加速度(°/s2)
     * @return 错误码
     */
    public int ImpedanceControlStartStop(int status, int workSpace, double[] forceThreshold, double[] m, double[] b, double[] k, double maxV, double maxVA, double maxW, double maxWA)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{status,workSpace,forceThreshold[0],forceThreshold[1],forceThreshold[2],
                    forceThreshold[3],forceThreshold[4],forceThreshold[5],m[0],m[1],m[2],m[3],m[4],m[5],
                    b[0],b[1],b[2],b[3],b[4],b[5],k[0],k[1],k[2],k[3],k[0],k[5],maxV,maxVA,maxW,maxWA};
            Object[] param = new Object[]{params};

            int rtn = (int)client.execute("ImpedanceControlStartStop", param) ;

            if (log != null)
            {
                log.LogInfo("ImpedanceControlStartStop:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief 根据编号获取工具坐标系
     * @param  id 工具坐标系编号
     * @param  coord 坐标系数值
     * @return 错误码
     */
    public int GetToolCoordWithID(int id, DescPose coord)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;

        if (id < 0 || id > 14)
        {
            errcode = 4;
            return errcode;
        }

        try {
            Object[] params = new Object[]{id};

            Object[] result = (Object[]) client.execute("GetToolCoordWithID", params) ;
            int rtn=(int)result[0];

            if(rtn==0){
                coord.tran.x = (double)result[1];
                coord.tran.y = (double)result[2];
                coord.tran.z = (double)result[3];
                coord.rpy.rx = (double)result[4];
                coord.rpy.ry = (double)result[5];
                coord.rpy.rz = (double)result[6];
            }

            if (log != null)
            {
                log.LogInfo("GetToolCoordWithID:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 根据编号获取工件坐标系
     * @param  id 工件坐标系编号
     * @param  coord 坐标系数值
     * @return 错误码
     */
    public int GetWObjCoordWithID(int id, DescPose coord)
    {
        int errcode = 0;

        if (id < 0 || id > 14)
        {
            errcode = 4;
            return errcode;
        }

        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{id};

            Object[] result = (Object[]) client.execute("GetWObjCoordWithID", params) ;
            int rtn=(int)result[0];

            if(rtn==0){
                coord.tran.x = (double)result[1];
                coord.tran.y = (double)result[2];
                coord.tran.z = (double)result[3];
                coord.rpy.rx = (double)result[4];
                coord.rpy.ry = (double)result[5];
                coord.rpy.rz = (double)result[6];
            }

            if (log != null)
            {
                log.LogInfo("GetWObjCoordWithID:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 根据编号获取外部工具坐标系
     * @param  id 外部工具坐标系编号
     * @param  coord 坐标系数值
     * @return 错误码
     */
    public int GetExToolCoordWithID(int id, DescPose coord)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;

        if (id < 0 || id > 14)
        {
            errcode = 4;
            return errcode;
        }

        try {
            Object[] params = new Object[]{id};

            Object[] result = (Object[]) client.execute("GetExToolCoordWithID", params) ;
            int rtn=(int)result[0];

            if(rtn==0){
                coord.tran.x = (double)result[1];
                coord.tran.y = (double)result[2];
                coord.tran.z = (double)result[3];
                coord.rpy.rx = (double)result[4];
                coord.rpy.ry = (double)result[5];
                coord.rpy.rz = (double)result[6];
            }

            if (log != null)
            {
                log.LogInfo("GetExToolCoordWithID:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 根据编号获取扩展轴坐标系
     * @param  id 外部工具坐标系编号
     * @param  coord 坐标系数值
     * @return 错误码
     */
    public int GetExAxisCoordWithID(int id, DescPose coord)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;

        if (id < 0 || id > 4)
        {
            errcode = 4;
            return errcode;
        }

        try {
            Object[] params = new Object[]{id};

            Object[] result = (Object[]) client.execute("GetExAxisCoordWithID", params) ;
            int rtn=(int)result[0];

            if(rtn==0){
                coord.tran.x = (double)result[1];
                coord.tran.y = (double)result[2];
                coord.tran.z = (double)result[3];
                coord.rpy.rx = (double)result[4];
                coord.rpy.ry = (double)result[5];
                coord.rpy.rz = (double)result[6];
            }

            if (log != null)
            {
                log.LogInfo("GetExAxisCoordWithID:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 根据编号获取负载质量及质心
     * @param  id 负载编号
     * @param  weight 负载质量
     * @param  cog 负载质心
     * @return 错误码
     */
    public int GetTargetPayloadWithID(int id, double[] weight, DescTran cog)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;

        if (id < 0 || id > 19)
        {
            errcode = 4;
            return errcode;
        }

        try {
            Object[] params = new Object[]{id};

            Object[] result = (Object[]) client.execute("GetTargetPayloadWithID", params) ;
            int rtn=(int)result[0];

            if(rtn==0){
                weight[0] = (double)result[1];
                cog.x = (double)result[2];
                cog.y = (double)result[3];
                cog.z = (double)result[4];
            }

            if (log != null)
            {
                log.LogInfo("GetTargetPayloadWithID:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取当前工具坐标系
     * @param  coord 坐标系数值
     * @return 错误码
     */
    public int GetCurToolCoord(DescPose coord)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;


        try {
            ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();
            if (sockErr == RobotError.ERR_SUCCESS)
            {
                coord.tran.x = robot_state_pkg.toolCoord[0];
                coord.tran.y = robot_state_pkg.toolCoord[1];
                coord.tran.z = robot_state_pkg.toolCoord[2];
                coord.rpy.rx = robot_state_pkg.toolCoord[3];
                coord.rpy.ry = robot_state_pkg.toolCoord[4];
                coord.rpy.rz = robot_state_pkg.toolCoord[5];
            }
            else
            {
                errcode = sockErr;
            }

            if (log != null)
            {
                log.LogInfo(robot_state_pkg.toolCoord[0]+","+
                        robot_state_pkg.toolCoord[1]+","+
                        robot_state_pkg.toolCoord[2]+","+
                        robot_state_pkg.toolCoord[3]+","+
                        robot_state_pkg.toolCoord[4]+","+
                        robot_state_pkg.toolCoord[5]+": "+sockErr);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取当前工件坐标系
     * @param  coord 坐标系数值
     * @return 错误码
     */
    public int GetCurWObjCoord(DescPose coord)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;


        try {
            ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();
            if (sockErr == RobotError.ERR_SUCCESS)
            {
                coord.tran.x = robot_state_pkg.wobjCoord[0];
                coord.tran.y = robot_state_pkg.wobjCoord[1];
                coord.tran.z = robot_state_pkg.wobjCoord[2];
                coord.rpy.rx = robot_state_pkg.wobjCoord[3];
                coord.rpy.ry = robot_state_pkg.wobjCoord[4];
                coord.rpy.rz = robot_state_pkg.wobjCoord[5];
            }
            else
            {
                errcode = sockErr;
            }

            if (log != null)
            {
                log.LogInfo(robot_state_pkg.wobjCoord[0]+","+
                                   robot_state_pkg.wobjCoord[1]+","+
                                   robot_state_pkg.wobjCoord[2]+","+
                                   robot_state_pkg.wobjCoord[3]+","+
                                   robot_state_pkg.wobjCoord[4]+","+
                                   robot_state_pkg.wobjCoord[5]+": "+sockErr);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取当前外部工具坐标系
     * @param  coord 坐标系数值
     * @return 错误码
     */
    public int GetCurExToolCoord(DescPose coord)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;


        try {
            ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();
            if (sockErr == RobotError.ERR_SUCCESS)
            {
                coord.tran.x = robot_state_pkg.extoolCoord[0];
                coord.tran.y = robot_state_pkg.extoolCoord[1];
                coord.tran.z = robot_state_pkg.extoolCoord[2];
                coord.rpy.rx = robot_state_pkg.extoolCoord[3];
                coord.rpy.ry = robot_state_pkg.extoolCoord[4];
                coord.rpy.rz = robot_state_pkg.extoolCoord[5];
            }
            else
            {
                errcode = sockErr;
            }

            if (log != null)
            {
                log.LogInfo(robot_state_pkg.extoolCoord[0]+","+
                        robot_state_pkg.extoolCoord[1]+","+
                        robot_state_pkg.extoolCoord[2]+","+
                        robot_state_pkg.extoolCoord[3]+","+
                        robot_state_pkg.extoolCoord[4]+","+
                        robot_state_pkg.extoolCoord[5]+": "+sockErr);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取当前扩展轴坐标系
     * @param coord 坐标系数值
     * @return 错误码
     */
    public int GetCurExAxisCoord(DescPose coord)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;


        try {
            ROBOT_STATE_PKG robot_state_pkg = GetRobotRealTimeState();
            if (sockErr == RobotError.ERR_SUCCESS)
            {
                coord.tran.x = robot_state_pkg.exAxisCoord[0];
                coord.tran.y = robot_state_pkg.exAxisCoord[1];
                coord.tran.z = robot_state_pkg.exAxisCoord[2];
                coord.rpy.rx = robot_state_pkg.exAxisCoord[3];
                coord.rpy.ry = robot_state_pkg.exAxisCoord[4];
                coord.rpy.rz = robot_state_pkg.exAxisCoord[5];
            }
            else
            {
                errcode = sockErr;
            }

            if (log != null)
            {
                log.LogInfo(robot_state_pkg.exAxisCoord[0]+","+
                        robot_state_pkg.exAxisCoord[1]+","+
                        robot_state_pkg.exAxisCoord[2]+","+
                        robot_state_pkg.exAxisCoord[3]+","+
                        robot_state_pkg.exAxisCoord[4]+","+
                        robot_state_pkg.exAxisCoord[5]+": "+sockErr);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief  设置末端负载质心坐标
     * @param   coord 质心坐标，单位mm
     * @return  错误码
     */
    public int SetLoadCoord(DescTran coord)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;


        try {
            Object[] params = new Object[]{coord.x,coord.y,coord.z,0};

            int rtn= (int) client.execute("SetLoadCoord", params) ;

            if (log != null)
            {
                log.LogInfo("SetLoadCoord:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief  设置末端负载质心坐标
     * @param   loadNum 负载编号
     * @param   coord 质心坐标，单位mm
     * @return  错误码
     */
    public int SetLoadCoord(int loadNum, DescTran coord)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;


        try {
            Object[] params = new Object[]{coord.x,coord.y,coord.z,loadNum};

            int rtn = (int)client.execute("SetLoadCoord", params) ;

            if (log != null)
            {
                log.LogInfo("SetLoadCoord:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置自定义摆动参数
     * @param  id 自定义摆动编号：0-2
     * @param  pointNum 摆动点位个数 0-10
     * @param  point 移动端点数据x,y,z
     * @param  stayTime 摆动停留时间ms
     * @param  frequency 摆动频率 Hz
     * @param  incStayType 等待模式：0-周期不包含等待时间；1-周期包含等待时间
     * @param  stationary 摆动位置等待：0-等待时间内继续运动；1-等待时间内位置静止
     * @return  错误码
     */
    public int CustomWeaveSetPara(int id, int pointNum, DescTran[] point, double[] stayTime, double frequency, int incStayType, int stationary)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;


        try {
            Object[] param = new Object[45];

            param[0] = id;
            param[1] = pointNum;

            for (int i = 0; i < 10; i++)
            {
                param[i * 3 + 2] = point[i].x;
                param[i * 3 + 3] = point[i].y;
                param[i * 3 + 4] = point[i].z;

                param[i + 32] = stayTime[i];
            }

            param[42] = frequency;
            param[43] = incStayType;
            param[44] = stationary;

            Object[] params = new Object[]{param};
            int rtn = (int)client.execute("CustomWeaveSetPara", params) ;

            if (log != null)
            {
                log.LogInfo("CustomWeaveSetPara:"+rtn);
            }
            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 获取自定义摆动参数
     * @param  id 自定义摆动编号：0-2
     * @param  pointNum 摆动点位个数 0-10
     * @param  point 移动端点数据x,y,z
     * @param  stayTime 摆动停留时间ms
     * @param  frequency 摆动频率 Hz
     * @param  incStayType 等待模式：0-周期不包含等待时间；1-周期包含等待时间
     * @param  stationary 摆动位置等待：0-等待时间内继续运动；1-等待时间内位置静止
     * @return  错误码
     */
    public int CustomWeaveGetPara(int id, int[] pointNum, DescTran[] point, double[] stayTime, double[] frequency, int[] incStayType, int[] stationary)
    {
        if (id < 0 || id > 2)
        {
            return 4;
        }

        if (IsSockComError()) {
            return sockErr;
        }
        int errcode = 0;
        try {
            Object[] params = new Object[]{id};
            Object[] result = (Object[])client.execute("CustomWeaveGetPara", params) ;
            int rtn=(int)result[0];
            if(rtn==0) {
                String rtnStr = (String) result[1];
                String[] parS = rtnStr.split(",");
                if (parS.length != 44) {
                    log.LogInfo("CustomWeaveGetPara size fail");
                    return -1;
                }
                pointNum[0] = Integer.parseInt(parS[0]);
                for (int i = 0; i < 10; i++) {
                    point[i].x = Double.parseDouble(parS[i * 3 + 1]);
                    point[i].y = Double.parseDouble(parS[i * 3 + 2]);
                    point[i].z = Double.parseDouble(parS[i * 3 + 3]);
                    stayTime[i] = Double.parseDouble(parS[i + 31]);
                }
                frequency[0] = Double.parseDouble(parS[41]);
                incStayType[0] = Integer.parseInt(parS[42]);
                stationary[0] = Integer.parseInt(parS[43]);
            }
                if (log != null)
                {
                    log.LogInfo("CustomWeaveGetPara:"+rtn);
                }
                return rtn;
        } catch (Throwable e) {
                if (log != null) {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                            "RPC exception " + e.getMessage());
                }
                return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 机器人操作系统升级(LA控制箱)
     * @param  filePath 操作系统升级包全路径
     * @return  错误码
     */
    public int KernelUpgrade(String filePath)
    {
        if (IsSockComError()) {
            return sockErr;
        }
        try {
            int errcode = FileUpLoad(6, filePath);

            if(errcode==0)
            {
                Object[] params=new Object[]{};
                errcode = (int)client.execute("KernelUpgrade", params) ;

                if (log != null)
                {
                     log.LogInfo("KernelUpgrade:"+errcode);
                }
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 获取机器人操作系统升级结果(LA控制箱)
     * @param  result 升级结果：0:成功；-1:失败
     * @return  错误码
     */
    public int GetKernelUpgradeResult(int[] result)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params=new Object[]{};
            Object[] results = (Object[])client.execute("GetKernelUpgradeResult", params) ;
            int rtn=(int)results[0];

            if(rtn==0){
                result[0]=(int)results[1];
            }
            if (log != null)
            {
                log.LogInfo("GetKernelUpgradeResult:"+rtn);
            }

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }


    /**
     * @brief 关节扭矩传感器灵敏度标定功能开启
     * @param status 0-关闭；1-开启
     * @return 错误码
     */
    public int JointSensitivityEnable(int status)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{status};
            Object[] params1 = new Object[]{params};
            int results = (int)client.execute("JointSensitivityEnable", params1);
            int rtn = results;

            if (rtn != 0) {
                if (log != null) {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                            Thread.currentThread().getStackTrace()[1].getLineNumber(),
                            "execute JointSensitivityEnable fail: " + rtn);
                }
            }

            if (log != null) {
                log.LogInfo("JointSensitivityEnable:" + rtn);
            }

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取关节扭矩传感器灵敏度标定结果
     * @param calibResult j1~j6关节灵敏度[0-1]
     * @return 错误码
     */
    public int JointSensitivityCalibration(double[] calibResult)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{};
            Object[] results = (Object[])client.execute("JointSensitivityCalibration", params);
            int rtn = (int)results[0];

            if (rtn == 0) {
                calibResult[0] = (double)results[1];
                calibResult[1] = (double)results[2];
                calibResult[2] = (double)results[3];
                calibResult[3] = (double)results[4];
                calibResult[4] = (double)results[5];
                calibResult[5] = (double)results[6];
            } else {
                if (log != null) {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                            Thread.currentThread().getStackTrace()[1].getLineNumber(),
                            "execute JointSensitivityCalibration fail: " + rtn);
                }
            }

            if (log != null) {
                log.LogInfo("JointSensitivityCalibration:" + rtn);
            }

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取关节扭矩传感器灵敏度标定结果
     * @param calibResult j1~j6关节灵敏度[0-1]
     * @param linearity j1~j6关节线性度[0-1]
     * @return 错误码
     */
    public int JointSensitivityCalibration(double[] calibResult,double[] linearity)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{};
            Object[] results = (Object[])client.execute("JointSensitivityCalibration", params);
            int rtn = (int)results[0];

            if (rtn == 0) {
                calibResult[0] = (double)results[1];
                calibResult[1] = (double)results[2];
                calibResult[2] = (double)results[3];
                calibResult[3] = (double)results[4];
                calibResult[4] = (double)results[5];
                calibResult[5] = (double)results[6];
                linearity[0] = (double)results[7];
                linearity[1] = (double)results[8];
                linearity[2] = (double)results[9];
                linearity[3] = (double)results[10];
                linearity[4] = (double)results[11];
                linearity[5] = (double)results[12];
            } else {
                if (log != null) {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                            Thread.currentThread().getStackTrace()[1].getLineNumber(),
                            "execute JointSensitivityCalibration fail: " + rtn);
                }
            }

            if (log != null) {
                log.LogInfo("JointSensitivityCalibration:" + rtn);
            }

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 关节扭矩传感器灵敏度数据采集
     * @return 错误码
     */
    public int JointSensitivityCollect()
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{};
            int results = (int)client.execute("JointSensitivityCollect", params);
            int rtn = results;

            if (rtn != 0) {
                if (log != null) {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                            Thread.currentThread().getStackTrace()[1].getLineNumber(),
                            "execute JointSensitivityCollect fail: " + rtn);
                }
            }

            if (log != null) {
                log.LogInfo("JointSensitivityCollect:" + rtn);
            }

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 清空运动指令队列
     * @return 错误码
     */
    public int MotionQueueClear()
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{};
            int rtn = (int)client.execute("MotionQueueClear", params);

            if (log != null) {
                log.LogInfo("MotionQueueClear:" + rtn);
            }

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取机器人8个从站端口错误帧数
     * @param  inRecvErr 输入接收错误帧数
     * @param  inCRCErr 输入CRC错误帧数
     * @param  inTransmitErr 输入转发错误帧数
     * @param  inLinkErr 输入链接错误帧数
     * @param  outRecvErr 输出接收错误帧数
     * @param  outCRCErr 输出CRC错误帧数
     * @param  outTransmitErr 输出转发错误帧数
     * @param  outLinkErr 输出链接错误帧数
     * @return 错误码
     */
    public int GetSlavePortErrCounter(int[] inRecvErr, int[] inCRCErr, int[] inTransmitErr, int[] inLinkErr, int[] outRecvErr, int[] outCRCErr, int[] outTransmitErr, int[] outLinkErr)
    {

        if (IsSockComError())
        {
            return sockErr;
        }

        try
        {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetSlavePortErrCounter" , params);
            int errcode=(int)result[0];

            if (errcode == 0) {
                String paramStr = (String) result[1];
                String[] parS = paramStr.split(",");
                if (parS.length != 64) {
                    log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), "GetSlavePortErrCounter size fail");
                    return -1;
                }
                for (int i = 0; i < 8; i++) {
                    inRecvErr[i] = Integer.parseInt(parS[i * 4]);
                    inCRCErr[i] = Integer.parseInt(parS[i * 4 + 1]);
                    inTransmitErr[i] = Integer.parseInt(parS[i * 4 + 2]);
                    inLinkErr[i] = Integer.parseInt(parS[i * 4 + 3]);

                    outRecvErr[i] = Integer.parseInt(parS[i * 4 + 32]);
                    outCRCErr[i] = Integer.parseInt(parS[i * 4 + 32 + 1]);
                    outTransmitErr[i] = Integer.parseInt(parS[i * 4 + 32 + 2]);
                    outLinkErr[i] = Integer.parseInt(parS[i * 4 + 32 + 3]);
                }

            }
            if (log != null)
            {
                log.LogInfo("GetSlavePortErrCounter( : " +errcode);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 从站端口错误帧清零
     * @param slaveID 从站编号0~7
     * @return 错误码
     */
    public int SlavePortErrCounterClear(int slaveID)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[]{slaveID};
            int rtn = (int)client.execute("SlavePortErrCounterClear", params);

            if (log != null) {
                log.LogInfo("SlavePortErrCounterClear:" + rtn);
            }

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 设置各轴速度前馈系数
     * @param  radio 各轴速度前馈系数
     * @return 错误码
     */
    public int SetVelFeedForwardRatio(double[] radio)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] param = new Object[]{radio[0],radio[1],radio[2],radio[3],radio[4],radio[5]};
            Object[] params = new Object[]{param};
            int rtn = (int)client.execute("SetVelFeedForwardRatio", params);

            if (log != null) {
                log.LogInfo("SetVelFeedForwardRatio:" + rtn);
            }

            return rtn;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取各轴速度前馈系数
     * @param  radio 各轴速度前馈系数
     * @return 错误码
     */
    public int GetVelFeedForwardRatio(double[] radio)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[] {};
            Object[] result = (Object[])client.execute("GetVelFeedForwardRatio" , params);
            int errcode=(int)result[0];

            if (errcode == 0) {
                radio[0]=(double)result[1];
                radio[1]=(double)result[2];
                radio[2]=(double)result[3];
                radio[3]=(double)result[4];
                radio[4]=(double)result[5];
                radio[5]=(double)result[6];
            }
            if (log != null)
            {
                log.LogInfo("GetVelFeedForwardRatio( : " + errcode);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 机器人MCU日志生成
     * @return 错误码
     */
    public int RobotMCULogCollect()
    {
        if (IsSockComError()) {
            return sockErr;
        }

        try {
            Object[] params = new Object[] {};
            int errcode = (int)client.execute("RobotMCULogCollect" , params);
            if (log != null)
            {
                log.LogInfo("RobotMCULogCollect( : " + errcode);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 移动到相贯线起始点
     * @param  mainPoint 主管6个示教点的笛卡尔位姿
     * @param  piecePoint 辅管6个示教点的笛卡尔位姿
     * @param  tool 工具坐标系编号
     * @param  wobj 工件坐标系编号
     * @param  vel 速度百分比
     * @param  acc 加速度百分比
     * @param  ovl 速度缩放因子
     * @param  oacc 加速度缩放因子
     * @param  moveType 运动类型; 0-PTP；1-LIN
     * @return 错误码
     */
//    public int MoveToIntersectLineStart(DescPose[] mainPoint, DescPose[] piecePoint, int tool, int wobj, double vel, double acc, double ovl, double oacc, int moveType)
//    {
//        if (IsSockComError()) {
//            return sockErr;
//        }
//
//        if (GetSafetyCode() != 0)
//        {
//            return GetSafetyCode();
//        }
//
//        int errcode=-1;
//
//        try {
//            Object[] param = new Object[79];
//            for (int i = 0; i < 6; i++)
//            {
//                param[i * 6 + 0] = mainPoint[i].tran.x;
//                param[i * 6 + 1] = mainPoint[i].tran.y;
//                param[i * 6 + 2] = mainPoint[i].tran.z;
//                param[i * 6 + 3] = mainPoint[i].rpy.rx;
//                param[i * 6 + 4] = mainPoint[i].rpy.ry;
//                param[i * 6 + 5] = mainPoint[i].rpy.rz;
//            }
//
//            for (int i = 0; i < 6; i++)
//            {
//                param[i * 6 + 0 + 36] = piecePoint[i].tran.x;
//                param[i * 6 + 1 + 36] = piecePoint[i].tran.y;
//                param[i * 6 + 2 + 36] = piecePoint[i].tran.z;
//                param[i * 6 + 3 + 36] = piecePoint[i].rpy.rx;
//                param[i * 6 + 4 + 36] = piecePoint[i].rpy.ry;
//                param[i * 6 + 5 + 36] = piecePoint[i].rpy.rz;
//            }
//
//            param[72] = tool;
//            param[73] = wobj;
//            param[74] = vel;
//            param[75] = acc;
//            param[76] = ovl;
//            param[77] = oacc;
//            param[78] = moveType;
//
//            Object[] params = new Object[]{param};
//
//            errcode = (int)client.execute("MoveToIntersectLineStart" , params);
//            if (log != null)
//            {
//                log.LogInfo("MoveToIntersectLineStart: " + errcode);
//            }
//        } catch (Throwable e) {
//            if (log != null) {
//                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
//                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
//                        "RPC exception " + e.getMessage());
//            }
//            return RobotError.ERR_RPC_ERROR;
//        }
//
//        ROBOT_STATE_PKG robot_state_pkg=GetRobotRealTimeState();
//        if ((robot_state_pkg.main_code != 0 || robot_state_pkg.sub_code != 0) && errcode == 0)
//        {
//            errcode = 14;
//        }
//
//        return errcode;
//    }

    /**
     * @brief 移动到相贯线起始点
     * @param  mainPoint 主管6个示教点的笛卡尔位姿
     * @param  piecePoint 辅管6个示教点的笛卡尔位姿
     * @param  tool 工具坐标系编号
     * @param  wobj 工件坐标系编号
     * @param  vel 速度百分比
     * @param  acc 加速度百分比
     * @param  ovl 速度缩放因子
     * @param  oacc 加速度缩放因子
     * @param  moveType 运动类型; 0-PTP；1-LIN
     * @return 错误码
     */
    public int MoveToIntersectLineStart(DescPose[] mainPoint, DescPose[] piecePoint, int tool, int wobj, double vel, double acc, double ovl, double oacc, int moveType)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        ExaxisPos[] mainExaxisPos = {new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos()};
        ExaxisPos[] pieceExaxisPos = {new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos()};
        int extAxisFlag = 0;
        ExaxisPos exaxisPos = new ExaxisPos();
        int moveDirection = 0;
        DescPose offset = new DescPose();
        int errcode = MoveToIntersectLineStart(mainPoint, mainExaxisPos, piecePoint, pieceExaxisPos, extAxisFlag, exaxisPos, tool, wobj, vel, acc, ovl, oacc, moveType, moveDirection, offset);

        return errcode;
    }

    /**
     * @brief 移动到相贯线起始点
     * @param mainPoint 主管6个示教点的笛卡尔位姿
     * @param mainExaxisPos 主管6个示教点扩展轴位置
     * @param piecePoint 辅管6个示教点的笛卡尔位姿
     * @param pieceExaxisPos 拼接管6个示教点扩展轴位置
     * @param extAxisFlag 是否启用扩展轴；0-不启用；1-启用
     * @param exaxisPos 起点扩展轴位置
     * @param tool 工具坐标系编号
     * @param wobj 工件坐标系编号
     * @param vel 速度百分比
     * @param acc 加速度百分比
     * @param ovl 速度缩放因子
     * @param oacc 加速度缩放因子
     * @param moveType 运动类型; 0-PTP；1-LIN
     * @param moveDirection 运动方向；0-顺时针；1-逆时针
     * @param offset 偏移量
     * @return 错误码
     */
    public int MoveToIntersectLineStart(DescPose[] mainPoint, ExaxisPos[] mainExaxisPos, DescPose[] piecePoint, ExaxisPos[] pieceExaxisPos, int extAxisFlag, ExaxisPos exaxisPos, int tool, int wobj, double vel, double acc, double ovl, double oacc, int moveType, int moveDirection, DescPose offset)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        int errcode=-1;

        try {
            Object[] param = new Object[139];
            for (int i = 0; i < 6; i++)
            {
                param[i * 6 + 0] = mainPoint[i].tran.x;
                param[i * 6 + 1] = mainPoint[i].tran.y;
                param[i * 6 + 2] = mainPoint[i].tran.z;
                param[i * 6 + 3] = mainPoint[i].rpy.rx;
                param[i * 6 + 4] = mainPoint[i].rpy.ry;
                param[i * 6 + 5] = mainPoint[i].rpy.rz;
            }

            for (int i = 0; i < 6; i++)
            {
                param[i * 4 + 0 + 36] = mainExaxisPos[i].axis1;
                param[i * 4 + 1 + 36] = mainExaxisPos[i].axis2;
                param[i * 4 + 2 + 36] = mainExaxisPos[i].axis3;
                param[i * 4 + 3 + 36] = mainExaxisPos[i].axis4;
            }

            for (int i = 0; i < 6; i++)
            {
                param[i * 6 + 0 + 60] = piecePoint[i].tran.x;
                param[i * 6 + 1 + 60] = piecePoint[i].tran.y;
                param[i * 6 + 2 + 60] = piecePoint[i].tran.z;
                param[i * 6 + 3 + 60] = piecePoint[i].rpy.rx;
                param[i * 6 + 4 + 60] = piecePoint[i].rpy.ry;
                param[i * 6 + 5 + 60] = piecePoint[i].rpy.rz;
            }

            for (int i = 0; i < 6; i++)
            {
                param[i * 4 + 0 + 96] = pieceExaxisPos[i].axis1;
                param[i * 4 + 1 + 96] = pieceExaxisPos[i].axis2;
                param[i * 4 + 2 + 96] = pieceExaxisPos[i].axis3;
                param[i * 4 + 3 + 96] = pieceExaxisPos[i].axis4;
            }

            param[120] = extAxisFlag;
            param[121] = exaxisPos.axis1;
            param[122] = exaxisPos.axis2;
            param[123] = exaxisPos.axis3;
            param[124] = exaxisPos.axis4;

            param[125] = tool;
            param[126] = wobj;
            param[127] = vel;
            param[128] = acc;
            param[129] = ovl;
            param[130] = oacc;
            param[131] = moveType;
            param[132] = moveDirection;

            param[133] = offset.tran.x;
            param[134] = offset.tran.y;
            param[135] = offset.tran.z;
            param[136] = offset.rpy.rx;
            param[137] = offset.rpy.ry;
            param[138] = offset.rpy.rz;

            Object[] params = new Object[]{param};

            errcode = (int)client.execute("MoveToIntersectLineStart" , params);
            if (log != null)
            {
                log.LogInfo("MoveToIntersectLineStart: " + errcode);
            }
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

        ROBOT_STATE_PKG robot_state_pkg=GetRobotRealTimeState();
        if ((robot_state_pkg.main_code != 0 || robot_state_pkg.sub_code != 0) && errcode == 0)
        {
            errcode = 14;
        }

        return errcode;
    }

    /**
     * @brief 相贯线运动
     * @param  mainPoint 主管6个示教点的笛卡尔位姿
     * @param  piecePoint 辅管6个示教点的笛卡尔位姿
     * @param  tool 工具坐标系编号
     * @param  wobj 工件坐标系编号
     * @param  vel 速度百分比
     * @param  acc 加速度百分比
     * @param  ovl 速度缩放因子
     * @param  oacc 加速度缩放因子
     * @param  moveDirection 运动方向; 0-顺时针；1-逆时针
     * @return 错误码
     */
//    public int MoveIntersectLine(DescPose[] mainPoint, DescPose[] piecePoint, int tool, int wobj, double vel, double acc, double ovl, double oacc, int moveDirection)
//    {
//        if (IsSockComError()) {
//            return sockErr;
//        }
//
//        if (GetSafetyCode() != 0)
//        {
//            return GetSafetyCode();
//        }
//
//        int errcode=-1;
//
//        try {
//            Object[] param = new Object[79];
//            for (int i = 0; i < 6; i++)
//            {
//                param[i * 6 + 0] = mainPoint[i].tran.x;
//                param[i * 6 + 1] = mainPoint[i].tran.y;
//                param[i * 6 + 2] = mainPoint[i].tran.z;
//                param[i * 6 + 3] = mainPoint[i].rpy.rx;
//                param[i * 6 + 4] = mainPoint[i].rpy.ry;
//                param[i * 6 + 5] = mainPoint[i].rpy.rz;
//            }
//
//            for (int i = 0; i < 6; i++)
//            {
//                param[i * 6 + 0 + 36] = piecePoint[i].tran.x;
//                param[i * 6 + 1 + 36] = piecePoint[i].tran.y;
//                param[i * 6 + 2 + 36] = piecePoint[i].tran.z;
//                param[i * 6 + 3 + 36] = piecePoint[i].rpy.rx;
//                param[i * 6 + 4 + 36] = piecePoint[i].rpy.ry;
//                param[i * 6 + 5 + 36] = piecePoint[i].rpy.rz;
//            }
//
//            param[72] = tool;
//            param[73] = wobj;
//            param[74] = vel;
//            param[75] = acc;
//            param[76] = ovl;
//            param[77] = oacc;
//            param[78] = moveDirection;
//
//            Object[] params = new Object[]{param};
//
//            errcode = (int)client.execute("MoveIntersectLine" , params);
//            if (log != null)
//            {
//                log.LogInfo("MoveIntersectLine: " + errcode);
//            }
//        } catch (Throwable e) {
//            if (log != null) {
//                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
//                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
//                        "RPC exception " + e.getMessage());
//            }
//            return RobotError.ERR_RPC_ERROR;
//        }
//
//        ROBOT_STATE_PKG robot_state_pkg=GetRobotRealTimeState();
//        if ((robot_state_pkg.main_code != 0 || robot_state_pkg.sub_code != 0) && errcode == 0)
//        {
//            errcode = 14;
//        }
//
//        return errcode;
//    }


    /**
     * @brief 相贯线运动
     * @param mainPoint 主管6个示教点的笛卡尔位姿
     * @param piecePoint 辅管6个示教点的笛卡尔位姿
     * @param tool 工具坐标系编号
     * @param wobj 工件坐标系编号
     * @param vel 速度百分比
     * @param acc 加速度百分比
     * @param ovl 速度缩放因子
     * @param oacc 加速度缩放因子
     * @param moveDirection 运动方向; 0-顺时针；1-逆时针
     * @return 错误码
     */
    public int MoveIntersectLine(DescPose[] mainPoint, DescPose[] piecePoint, int tool, int wobj, double vel, double acc, double ovl, double oacc, int moveDirection)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        int errcode = 0;
        ExaxisPos[] mainExaxisPos = {new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos()};
        ExaxisPos[] pieceExaxisPos = {new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos()};
        int extAxisFlag = 0;
        ExaxisPos[] exaxisPos = {new ExaxisPos(),new ExaxisPos(),new ExaxisPos(),new ExaxisPos()};
        DescPose offset =new DescPose();
        errcode = MoveIntersectLine(mainPoint, mainExaxisPos, piecePoint, pieceExaxisPos, extAxisFlag, exaxisPos, tool, wobj, vel, acc, ovl, oacc, moveDirection, offset);

        return errcode;
    }

    /**
     * @brief 相贯线运动
     * @param mainPoint 主管6个示教点的笛卡尔位姿
     * @param mainExaxisPos 主管6个示教点扩展轴位置
     * @param piecePoint 辅管6个示教点的笛卡尔位姿
     * @param pieceExaxisPos 拼接管6个示教点扩展轴位置
     * @param extAxisFlag 是否启用扩展轴；0-不启用；1-启用
     * @param exaxisPos 起点扩展轴位置
     * @param tool 工具坐标系编号
     * @param wobj 工件坐标系编号
     * @param vel 速度百分比
     * @param acc 加速度百分比
     * @param ovl 速度缩放因子
     * @param oacc 加速度缩放因子
     * @param moveDirection 运动方向; 0-顺时针；1-逆时针
     * @param offset 偏移量
     * @return 错误码
     */
    public int MoveIntersectLine(DescPose[] mainPoint, ExaxisPos[] mainExaxisPos, DescPose[] piecePoint, ExaxisPos[] pieceExaxisPos, int extAxisFlag, ExaxisPos[] exaxisPos, int tool, int wobj, double vel, double acc, double ovl, double oacc, int moveDirection, DescPose offset)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        if (GetSafetyCode() != 0)
        {
            return GetSafetyCode();
        }

        int errcode=-1;

        try {
            Object[] param = new Object[150];
            for (int i = 0; i < 6; i++)
            {
                param[i * 6 + 0] = mainPoint[i].tran.x;
                param[i * 6 + 1] = mainPoint[i].tran.y;
                param[i * 6 + 2] = mainPoint[i].tran.z;
                param[i * 6 + 3] = mainPoint[i].rpy.rx;
                param[i * 6 + 4] = mainPoint[i].rpy.ry;
                param[i * 6 + 5] = mainPoint[i].rpy.rz;
            }

            for (int i = 0; i < 6; i++)
            {
                param[i * 4 + 0 + 36] = mainExaxisPos[i].axis1;
                param[i * 4 + 1 + 36] = mainExaxisPos[i].axis2;
                param[i * 4 + 2 + 36] = mainExaxisPos[i].axis3;
                param[i * 4 + 3 + 36] = mainExaxisPos[i].axis4;
            }

            for (int i = 0; i < 6; i++)
            {
                param[i * 6 + 0 + 60] = piecePoint[i].tran.x;
                param[i * 6 + 1 + 60] = piecePoint[i].tran.y;
                param[i * 6 + 2 + 60] = piecePoint[i].tran.z;
                param[i * 6 + 3 + 60] = piecePoint[i].rpy.rx;
                param[i * 6 + 4 + 60] = piecePoint[i].rpy.ry;
                param[i * 6 + 5 + 60] = piecePoint[i].rpy.rz;
            }

            for (int i = 0; i < 6; i++)
            {
                param[i * 4 + 0 + 96] = pieceExaxisPos[i].axis1;
                param[i * 4 + 1 + 96] = pieceExaxisPos[i].axis2;
                param[i * 4 + 2 + 96] = pieceExaxisPos[i].axis3;
                param[i * 4 + 3 + 96] = pieceExaxisPos[i].axis4;
            }

            param[120] = extAxisFlag;

            for (int i = 0; i < 4; i++)
            {
                param[121 + i * 4] = exaxisPos[i].axis1;
                param[122 + i * 4] = exaxisPos[i].axis2;
                param[123 + i * 4] = exaxisPos[i].axis3;
                param[124 + i * 4] = exaxisPos[i].axis4;
            }

            param[137] = tool;
            param[138] = wobj;
            param[139] = vel;
            param[140] = acc;
            param[141] = ovl;
            param[142] = oacc;
            param[143] = moveDirection;

            param[144] = offset.tran.x;
            param[145] = offset.tran.y;
            param[146] = offset.tran.z;
            param[147] = offset.rpy.rx;
            param[148] = offset.rpy.ry;
            param[149] = offset.rpy.rz;

            Object[] params = new Object[]{param};

            errcode = (int)client.execute("MoveIntersectLine" , params);
            if (log != null)
            {
                log.LogInfo("MoveIntersectLine: " + errcode);
            }
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

        ROBOT_STATE_PKG robot_state_pkg=GetRobotRealTimeState();
        if ((robot_state_pkg.main_code != 0 || robot_state_pkg.sub_code != 0) && errcode == 0)
        {
            errcode = 14;
        }

        return errcode;
    }


    /**
     * @brief 获取关节扭矩传感器迟滞误差
     * @param  hysteresisError j1~j6关节迟滞误差
     * @return 错误码
     */
    public int JointHysteresisError(double[] hysteresisError)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        int errcode = 0;

        try {
            Object[] params = new Object[]{};
            Object[] result = (Object[]) client.execute("JointHysteresisError", params);
            errcode = (int) result[0];
            if (errcode == 0) {
                hysteresisError[0] = (double) result[1];
                hysteresisError[1] = (double) result[2];
                hysteresisError[2] = (double) result[3];
                hysteresisError[3] = (double) result[4];
                hysteresisError[4] = (double) result[5];
                hysteresisError[5] = (double) result[6];
            }
            if (log != null) {
                log.LogInfo("JointHysteresisError: " + errcode);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * @brief 获取关节扭矩传感器重复精度
     * @param repeatability j1~j6关节重复精度
     * @return 错误码
     */
    public int JointRepeatability(double[] repeatability)
    {
        if (IsSockComError()) {
            return sockErr;
        }

        int errcode = 0;

        try {
            Object[] params = new Object[]{};
            Object[] result = (Object[]) client.execute("JointRepeatability", params);
            errcode = (int) result[0];
            if (errcode == 0) {
                repeatability[0] = (double) result[1];
                repeatability[1] = (double) result[2];
                repeatability[2] = (double) result[3];
                repeatability[3] = (double) result[4];
                repeatability[4] = (double) result[5];
                repeatability[5] = (double) result[6];
            }
            if (log != null) {
                log.LogInfo("JointRepeatability: " + errcode);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }

    }

    /**
     * @brief 设置关节力传感器参数
     * @param M J1-J6质量系数[]
     * @param B J1-J6阻尼系数[]
     * @param K J1-J6刚度系数[]
     * @param threshold 力控制阈值，Nm
     * @param sensitivity 灵敏度,Nm/V,[]
     * @param setZeroFlag 功能开启标志位；0-关闭；1-开启；2-位置1记录零点；3-位置2记录零点
     * @return 错误码
     */
    public int SetAdmittanceParams(double[] M, double[] B, double[] K, double[] threshold, double[] sensitivity, int setZeroFlag) {
        if (IsSockComError()) {
            return sockErr;
        }

        if (GetSafetyCode() != 0) {
            return GetSafetyCode();
        }

        int errcode = 0;
        try {
            Object[] param = new Object[31];
            param[0] = M[0];
            param[1] = M[1];
            param[2] = M[2];
            param[3] = M[3];
            param[4] = M[4];
            param[5] = M[5];
            param[6] = B[0];
            param[7] = B[1];
            param[8] = B[2];
            param[9] = B[3];
            param[10] = B[4];
            param[11] = B[5];
            param[12] = K[0];
            param[13] = K[1];
            param[14] = K[2];
            param[15] = K[3];
            param[16] = K[4];
            param[17] = K[5];
            param[18] = threshold[0];
            param[19] = threshold[1];
            param[20] = threshold[2];
            param[21] = threshold[3];
            param[22] = threshold[4];
            param[23] = threshold[5];
            param[24] = sensitivity[0];
            param[25] = sensitivity[1];
            param[26] = sensitivity[2];
            param[27] = sensitivity[3];
            param[28] = sensitivity[4];
            param[29] = sensitivity[5];
            param[30] = setZeroFlag;

            Object[] params = new Object[]{param};

            errcode = (int) client.execute("SetAdmittanceParams", params);

            if (log != null) {
                log.LogInfo("SetAdmittanceParams: " + errcode);
            }
            return errcode;
        } catch (Throwable e) {
            if (log != null) {
                log.LogError(Thread.currentThread().getStackTrace()[1].getMethodName(),
                        Thread.currentThread().getStackTrace()[1].getLineNumber(),
                        "RPC exception " + e.getMessage());
            }
            return RobotError.ERR_RPC_ERROR;
        }
    }

    /**
     * 截取byte数组   不改变原数组
     * @param b 原数组
     * @param off 偏差值（索引）
     * @param length 长度
     * @return 截取后的数组
     */
    public byte[] subByte(byte[] b,int off,int length)
    {
        byte[] b1 = new byte[length];
        System.arraycopy(b, off, b1, 0, length);
        return b1;
    }

    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes).flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    public void Sleep(int ms)
    {
        try {

            Thread.sleep(ms);   // 休眠3秒

        } catch (Exception e) {
            System.out.println("Got an exception in Sleep!  :  " +  e.getMessage());
        }
    }

}