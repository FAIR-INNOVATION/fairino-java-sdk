package fairino;


public class RobotStateRoutineThread extends Thread
{
    final int STRUCTSIZE = 1184;
    int ROBOT_REALTIME_PORT = 20004;
    String robotIp = "";
    ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();

    boolean getRobotRealTimeFlag = false;

    int sockErr = RobotError.ERR_SUCCESS;

    TCPClient clientRobotState;

    public RobotStateRoutineThread(String ip)
    {
        try
        {
            robotIp = ip;
        }
        catch (Throwable e)
        {
            System.out.println(e.getMessage());
        }

    }



    public void run()
    {
        clientRobotState = new TCPClient(robotIp, ROBOT_REALTIME_PORT);
        boolean bRtn = clientRobotState.Connect();
        if(!bRtn)
        {
            sockErr = RobotError.ERR_SOCKET_COM_FAILED;
        }
        int rtn = -1;
        getRobotRealTimeFlag = true;

        byte[] pkgBuf = new byte[1024 * 2];
        while (getRobotRealTimeFlag)
        {
            try
            {
                byte[] tmp = new byte[32];
                rtn = clientRobotState.GetPkg(pkgBuf, STRUCTSIZE);
                System.out.println("GetPkg():"+rtn);
                if(rtn != 0)
                {
                    sockErr = RobotError.ERR_SOCKET_COM_FAILED;
                    return;
                }

                pkg.program_state = (int)pkgBuf[5];
                pkg.robot_state = (int)pkgBuf[6];            //机器人运动状态，1-停止；2-运行；3-暂停；4-拖动  7

                System.arraycopy(pkgBuf, 7, tmp, 0, 4);
                pkg.main_code = bytesToInt(tmp);               //主故障码

                System.arraycopy(pkgBuf, 11, tmp, 0, 4);
                pkg.sub_code = bytesToInt(tmp);                //子故障码

                pkg.robot_mode = (int)pkgBuf[15];             //机器人模式，0-自动模式；1-手动模式 16

                System.arraycopy(pkgBuf, 16, tmp, 0, 8);
                pkg.jt_cur_pos[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 24, tmp, 0, 8);
                pkg.jt_cur_pos[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 32, tmp, 0, 8);
                pkg.jt_cur_pos[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 40, tmp, 0, 8);
                pkg.jt_cur_pos[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 48, tmp, 0, 8);
                pkg.jt_cur_pos[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 56, tmp, 0, 8);
                pkg.jt_cur_pos[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 64, tmp, 0, 8);
                pkg.tl_cur_pos[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 72, tmp, 0, 8);
                pkg.tl_cur_pos[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 80, tmp, 0, 8);
                pkg.tl_cur_pos[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 88, tmp, 0, 8);
                pkg.tl_cur_pos[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 96, tmp, 0, 8);
                pkg.tl_cur_pos[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 104, tmp, 0, 8);
                pkg.tl_cur_pos[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 112, tmp, 0, 8);
                pkg.flange_cur_pos[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 120, tmp, 0, 8);
                pkg.flange_cur_pos[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 128, tmp, 0, 8);
                pkg.flange_cur_pos[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 136, tmp, 0, 8);
                pkg.flange_cur_pos[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 144, tmp, 0, 8);
                pkg.flange_cur_pos[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 152, tmp, 0, 8);
                pkg.flange_cur_pos[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 160, tmp, 0, 8);
                pkg.actual_qd[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 168, tmp, 0, 8);
                pkg.actual_qd[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 176, tmp, 0, 8);
                pkg.actual_qd[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 184, tmp, 0, 8);
                pkg.actual_qd[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 192, tmp, 0, 8);
                pkg.actual_qd[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 200, tmp, 0, 8);
                pkg.actual_qd[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 208, tmp, 0, 8);
                pkg.actual_qdd[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 216, tmp, 0, 8);
                pkg.actual_qdd[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 224, tmp, 0, 8);
                pkg.actual_qdd[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 232, tmp, 0, 8);
                pkg.actual_qdd[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 240, tmp, 0, 8);
                pkg.actual_qdd[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 248, tmp, 0, 8);
                pkg.actual_qdd[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 256, tmp, 0, 8);
                pkg.target_TCP_CmpSpeed[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 264, tmp, 0, 8);
                pkg.target_TCP_CmpSpeed[1] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 272, tmp, 0, 8);
                pkg.target_TCP_Speed[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 280, tmp, 0, 8);
                pkg.target_TCP_Speed[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 288, tmp, 0, 8);
                pkg.target_TCP_Speed[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 296, tmp, 0, 8);
                pkg.target_TCP_Speed[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 304, tmp, 0, 8);
                pkg.target_TCP_Speed[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 312, tmp, 0, 8);
                pkg.target_TCP_Speed[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 320, tmp, 0, 8);
                pkg.actual_TCP_CmpSpeed[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 328, tmp, 0, 8);
                pkg.actual_TCP_CmpSpeed[1] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 336, tmp, 0, 8);
                pkg.actual_TCP_Speed[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 344, tmp, 0, 8);
                pkg.actual_TCP_Speed[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 352, tmp, 0, 8);
                pkg.actual_TCP_Speed[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 360, tmp, 0, 8);
                pkg.actual_TCP_Speed[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 368, tmp, 0, 8);
                pkg.actual_TCP_Speed[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 376, tmp, 0, 8);
                pkg.actual_TCP_Speed[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 384, tmp, 0, 8);
                pkg.jt_cur_tor[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 392, tmp, 0, 8);
                pkg.jt_cur_tor[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 400, tmp, 0, 8);
                pkg.jt_cur_tor[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 408, tmp, 0, 8);
                pkg.jt_cur_tor[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 416, tmp, 0, 8);
                pkg.jt_cur_tor[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 424, tmp, 0, 8);
                pkg.jt_cur_tor[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 432, tmp, 0, 4);
                pkg.tool = bytesToInt(tmp);

                System.arraycopy(pkgBuf, 436, tmp, 0, 4);
                pkg.user = bytesToInt(tmp);

                pkg.cl_dgt_output_h = (int)pkgBuf[440];
                pkg.cl_dgt_output_l = (int)pkgBuf[441];
                pkg.tl_dgt_output_l = (int)pkgBuf[442];
                pkg.cl_dgt_input_h = (int)pkgBuf[443];
                pkg.cl_dgt_input_l = (int)pkgBuf[444];
                pkg.tl_dgt_input_l = (int)pkgBuf[445];

                System.arraycopy(pkgBuf, 446, tmp, 0, 4);
                pkg.cl_analog_input = byteToShort(tmp);

                System.arraycopy(pkgBuf, 450, tmp, 0, 2);
                pkg.tl_anglog_input = byteToShort(tmp)[0];

                System.arraycopy(pkgBuf, 452, tmp, 0, 8);
                pkg.ft_sensor_raw_data[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 460, tmp, 0, 8);
                pkg.ft_sensor_raw_data[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 468, tmp, 0, 8);
                pkg.ft_sensor_raw_data[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 476, tmp, 0, 8);
                pkg.ft_sensor_raw_data[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 484, tmp, 0, 8);
                pkg.ft_sensor_raw_data[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 492, tmp, 0, 8);
                pkg.ft_sensor_raw_data[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 500, tmp, 0, 8);
                pkg.ft_sensor_data[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 508, tmp, 0, 8);
                pkg.ft_sensor_data[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 516, tmp, 0, 8);
                pkg.ft_sensor_data[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 524, tmp, 0, 8);
                pkg.ft_sensor_data[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 532, tmp, 0, 8);
                pkg.ft_sensor_data[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 540, tmp, 0, 8);
                pkg.ft_sensor_data[5] = bytes2Double(tmp);

                pkg.ft_sensor_active = (int)pkgBuf[548];
                pkg.EmergencyStop = (int)pkgBuf[549];

                System.arraycopy(pkgBuf, 550, tmp, 0, 4);
                pkg.motion_done = bytesToInt(tmp);

                pkg.gripper_motiondone = (int)pkgBuf[554];

                System.arraycopy(pkgBuf, 555, tmp, 0, 4);
                pkg.mc_queue_len = bytesToInt(tmp);

                pkg.collisionState = (int)pkgBuf[559];

                System.arraycopy(pkgBuf, 560, tmp, 0, 4);
                pkg.trajectory_pnum = bytesToInt(tmp);

                pkg.safety_stop0_state = (int)pkgBuf[564];
                pkg.safety_stop1_state = (int)pkgBuf[565];
                pkg.gripper_fault_id = (int)pkgBuf[566];

                System.arraycopy(pkgBuf, 567, tmp, 0, 2);
                pkg.gripper_fault = byteToShort(tmp)[0];

                System.arraycopy(pkgBuf, 569, tmp, 0, 2);
                pkg.gripper_active = byteToShort(tmp)[0];

                pkg.gripper_position = (int)pkgBuf[571];
                pkg.gripper_speed = (int)pkgBuf[572];
                pkg.gripper_current = (int)pkgBuf[573];

                System.arraycopy(pkgBuf, 574, tmp, 0, 4);
                pkg.gripper_tmp = bytesToInt(tmp);

                System.arraycopy(pkgBuf, 578, tmp, 0, 4);
                pkg.gripper_voltage = bytesToInt(tmp);

                pkg.auxState.servoId = (int)pkgBuf[582];           //伺服驱动器ID号

                System.arraycopy(pkgBuf, 583, tmp, 0, 4);
                pkg.auxState.servoErrCode = bytesToInt(tmp);

                System.arraycopy(pkgBuf, 587, tmp, 0, 4);
                pkg.auxState.servoState = bytesToInt(tmp);

                System.arraycopy(pkgBuf, 591, tmp, 0, 8);
                pkg.auxState.servoPos = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 599, tmp, 0, 4);
                pkg.auxState.servoVel = byte2float(tmp);

                System.arraycopy(pkgBuf, 603, tmp, 0, 4);
                pkg.auxState.servoTorque = byte2float(tmp);

                System.arraycopy(pkgBuf, 607, tmp, 0, 8);
                pkg.extAxisStatus0.pos = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 615, tmp, 0, 8);
                pkg.extAxisStatus0.vel = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 623, tmp, 0, 4);
                pkg.extAxisStatus0.errorCode = bytesToInt(tmp);

                pkg.extAxisStatus0.ready = (int)pkgBuf[627];
                pkg.extAxisStatus0.inPos = (int)pkgBuf[628];
                pkg.extAxisStatus0.alarm = (int)pkgBuf[629];
                pkg.extAxisStatus0.flerr = (int)pkgBuf[630];
                pkg.extAxisStatus0.nlimit = (int)pkgBuf[631];
                pkg.extAxisStatus0.pLimit = (int)pkgBuf[632];
                pkg.extAxisStatus0.mdbsOffLine = (int)pkgBuf[633];
                pkg.extAxisStatus0.mdbsTimeout = (int)pkgBuf[634];
                pkg.extAxisStatus0.homingStatus = (int)pkgBuf[635];

                System.arraycopy(pkgBuf, 636, tmp, 0, 8);
                pkg.extAxisStatus1.pos = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 644, tmp, 0, 8);
                pkg.extAxisStatus1.vel = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 652, tmp, 0, 4);
                pkg.extAxisStatus1.errorCode = bytesToInt(tmp);
                pkg.extAxisStatus1.ready = (int)pkgBuf[656];
                pkg.extAxisStatus1.inPos = (int)pkgBuf[657];
                pkg.extAxisStatus1.alarm = (int)pkgBuf[658];
                pkg.extAxisStatus1.flerr = (int)pkgBuf[659];
                pkg.extAxisStatus1.nlimit = (int)pkgBuf[660];
                pkg.extAxisStatus1.pLimit = (int)pkgBuf[661];
                pkg.extAxisStatus1.mdbsOffLine = (int)pkgBuf[662];
                pkg.extAxisStatus1.mdbsTimeout = (int)pkgBuf[663];
                pkg.extAxisStatus1.homingStatus = (int)pkgBuf[664];

                System.arraycopy(pkgBuf, 665, tmp, 0, 8);
                pkg.extAxisStatus2.pos = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 673, tmp, 0, 8);
                pkg.extAxisStatus2.vel = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 681, tmp, 0, 4);
                pkg.extAxisStatus2.errorCode = bytesToInt(tmp);
                pkg.extAxisStatus2.ready = (int)pkgBuf[685];
                pkg.extAxisStatus2.inPos = (int)pkgBuf[686];
                pkg.extAxisStatus2.alarm = (int)pkgBuf[687];
                pkg.extAxisStatus2.flerr = (int)pkgBuf[688];
                pkg.extAxisStatus2.nlimit = (int)pkgBuf[689];
                pkg.extAxisStatus2.pLimit = (int)pkgBuf[690];
                pkg.extAxisStatus2.mdbsOffLine = (int)pkgBuf[691];
                pkg.extAxisStatus2.mdbsTimeout = (int)pkgBuf[692];
                pkg.extAxisStatus2.homingStatus = (int)pkgBuf[693];

                System.arraycopy(pkgBuf, 694, tmp, 0, 8);
                pkg.extAxisStatus3.pos = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 702, tmp, 0, 8);
                pkg.extAxisStatus3.vel = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 710, tmp, 0, 4);
                pkg.extAxisStatus3.errorCode = bytesToInt(tmp);
                pkg.extAxisStatus3.ready = (int)pkgBuf[714];
                pkg.extAxisStatus3.inPos = (int)pkgBuf[715];
                pkg.extAxisStatus3.alarm = (int)pkgBuf[716];
                pkg.extAxisStatus3.flerr = (int)pkgBuf[717];
                pkg.extAxisStatus3.nlimit = (int)pkgBuf[718];
                pkg.extAxisStatus3.pLimit = (int)pkgBuf[719];
                pkg.extAxisStatus3.mdbsOffLine = (int)pkgBuf[720];
                pkg.extAxisStatus3.mdbsTimeout = (int)pkgBuf[721];
                pkg.extAxisStatus3.homingStatus = (int)pkgBuf[722];

                System.arraycopy(pkgBuf, 723, tmp, 0, 16);
                pkg.extDIState = byteToShort(tmp);

                System.arraycopy(pkgBuf, 739, tmp, 0, 16);
                pkg.extDOState = byteToShort(tmp);

                System.arraycopy(pkgBuf, 755, tmp, 0, 8);
                pkg.extAIState = byteToShort(tmp);

                System.arraycopy(pkgBuf, 763, tmp, 0, 8);
                pkg.extAOState = byteToShort(tmp);

                System.arraycopy(pkgBuf, 771, tmp, 0, 4);
                pkg.rbtEnableState = bytesToInt(tmp);

                System.arraycopy(pkgBuf, 775, tmp, 0, 8);
                pkg.jointDriverTorque[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 783, tmp, 0, 8);
                pkg.jointDriverTorque[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 791, tmp, 0, 8);
                pkg.jointDriverTorque[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 799, tmp, 0, 8);
                pkg.jointDriverTorque[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 807, tmp, 0, 8);
                pkg.jointDriverTorque[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 815, tmp, 0, 8);
                pkg.jointDriverTorque[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 823, tmp, 0, 8);
                pkg.jointDriverTemperature[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 831, tmp, 0, 8);
                pkg.jointDriverTemperature[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 839, tmp, 0, 8);
                pkg.jointDriverTemperature[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 847, tmp, 0, 8);
                pkg.jointDriverTemperature[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 855, tmp, 0, 8);
                pkg.jointDriverTemperature[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 863, tmp, 0, 8);
                pkg.jointDriverTemperature[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 871, tmp, 0, 2);
                pkg.robotTime.year = byteToShort(tmp)[0];
                pkg.robotTime.mouth = pkgBuf[873];
                pkg.robotTime.day = pkgBuf[874];
                pkg.robotTime.hour = pkgBuf[875];
                pkg.robotTime.minute = pkgBuf[876];
                pkg.robotTime.second = pkgBuf[877];
                System.arraycopy(pkgBuf, 878, tmp, 0, 2);
                pkg.robotTime.millisecond = byteToShort(tmp)[0];

                System.arraycopy(pkgBuf, 880, tmp, 0, 4);
                pkg.softwareUpgradeState = bytesToInt(tmp);

                System.arraycopy(pkgBuf, 884, tmp, 0, 2);
                pkg.endLuaErrCode = byteToShort(tmp)[0];

                System.arraycopy(pkgBuf, 886, tmp, 0, 2);
                pkg.cl_analog_output[0] = byteToShort(tmp)[0];
                System.arraycopy(pkgBuf, 888, tmp, 0, 2);
                pkg.cl_analog_output[1] = byteToShort(tmp)[0];

                System.arraycopy(pkgBuf, 890, tmp, 0, 2);
                pkg.tl_analog_output = byteToShort(tmp)[0];
                System.arraycopy(pkgBuf, 892, tmp, 0, 4);
                pkg.gripperRotNum = byte2float(tmp);
//                System.arraycopy(pkgBuf, 896, tmp, 0, 1);
                pkg.gripperRotSpeed = (int)pkgBuf[896];
//                System.arraycopy(pkgBuf, 897, tmp, 0, 1);
                pkg.gripperRotTorque =  (int)pkgBuf[897];

                pkg.weldingBreakOffstate.breakOffState = (int)pkgBuf[898];
                pkg.weldingBreakOffstate.weldArcState = (int)pkgBuf[899];

                System.arraycopy(pkgBuf, 900, tmp, 0, 8);
                pkg.jt_tgt_tor[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 908, tmp, 0, 8);
                pkg.jt_tgt_tor[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 916, tmp, 0, 8);
                pkg.jt_tgt_tor[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 924, tmp, 0, 8);
                pkg.jt_tgt_tor[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 932, tmp, 0, 8);
                pkg.jt_tgt_tor[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 940, tmp, 0, 8);
                pkg.jt_tgt_tor[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 948, tmp, 0, 4);
//                pkg.smartToolState = byteToInt(tmp)[0];
                pkg.smartToolState = bytesToInt(tmp);

                System.arraycopy(pkgBuf, 952, tmp, 0, 4);
                pkg.wideVoltageCtrlBoxTemp = byte2float(tmp);
                System.arraycopy(pkgBuf, 956, tmp, 0, 2);
                pkg.wideVoltageCtrlBoxFanCurrent = byteToShort(tmp)[0];

                System.arraycopy(pkgBuf, 958, tmp, 0, 8);
                pkg.toolCoord[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 966, tmp, 0, 8);
                pkg.toolCoord[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 974, tmp, 0, 8);
                pkg.toolCoord[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 982, tmp, 0, 8);
                pkg.toolCoord[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 990, tmp, 0, 8);
                pkg.toolCoord[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 998, tmp, 0, 8);
                pkg.toolCoord[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 1006, tmp, 0, 8);
                pkg.wobjCoord[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1014, tmp, 0, 8);
                pkg.wobjCoord[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1022, tmp, 0, 8);
                pkg.wobjCoord[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1030, tmp, 0, 8);
                pkg.wobjCoord[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1038, tmp, 0, 8);
                pkg.wobjCoord[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1046, tmp, 0, 8);
                pkg.wobjCoord[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 1054, tmp, 0, 8);
                pkg.extoolCoord[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1062, tmp, 0, 8);
                pkg.extoolCoord[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1070, tmp, 0, 8);
                pkg.extoolCoord[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1078, tmp, 0, 8);
                pkg.extoolCoord[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1086, tmp, 0, 8);
                pkg.extoolCoord[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1094, tmp, 0, 8);
                pkg.extoolCoord[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 1102, tmp, 0, 8);
                pkg.exAxisCoord[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1110, tmp, 0, 8);
                pkg.exAxisCoord[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1118, tmp, 0, 8);
                pkg.exAxisCoord[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1126, tmp, 0, 8);
                pkg.exAxisCoord[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1134, tmp, 0, 8);
                pkg.exAxisCoord[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1142, tmp, 0, 8);
                pkg.exAxisCoord[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 1150, tmp, 0, 8);
                pkg.load = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 1158, tmp, 0, 8);
                pkg.loadCog[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1166, tmp, 0, 8);
                pkg.loadCog[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1174, tmp, 0, 8);
                pkg.loadCog[2] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 1182, tmp, 0, 8);
                pkg.lastServoTarget[0] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1190, tmp, 0, 8);
                pkg.lastServoTarget[1] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1198, tmp, 0, 8);
                pkg.lastServoTarget[2] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1206, tmp, 0, 8);
                pkg.lastServoTarget[3] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1214, tmp, 0, 8);
                pkg.lastServoTarget[4] = bytes2Double(tmp);
                System.arraycopy(pkgBuf, 1222, tmp, 0, 8);
                pkg.lastServoTarget[5] = bytes2Double(tmp);

                System.arraycopy(pkgBuf, 1230, tmp, 0, 2);
                pkg.servoJCmdNum = byteToShort(tmp)[0];

                System.arraycopy(pkgBuf, 1232, tmp, 0, 2);
                pkg.check_sum = byteToShort(tmp)[0];

            }
            catch (Throwable e)
            {
                System.out.println("the thread error " + e.getMessage());
            }
        }
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
            if(clientRobotState != null)
            {
                clientRobotState.SetReconnectParam(enable, times, period);
            }
        }
        catch (Throwable e)
        {
            return 0;
        }
        return 0;
    }

    public ROBOT_STATE_PKG GetRobotRealTimeState()
    {
        return pkg;
    }

    public int GetSockErr()
    {
        return sockErr;
    }

    public static double bytes2Double(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    public static int bytesToInt(byte[] a){
//        int ans=0;
//        for(int i=0;i<4;i++){
//            ans<<=8;//左移 8 位
//            ans|=a[3-i];//保存 byte 值到 ans 的最低 8 位上
//        }
//        return ans;
        int ans = 0;
        for (int i = 0; i < 4; i++) {
            ans <<= 8;              // 左移 8 位
            ans |= (a[3 - i] & 0xFF); // 保存无符号字节值到 ans 的最低 8 位
        }
        return ans;
    }

    public static short[] byteToShort(byte[] data) {
        short[] shortValue = new short[data.length / 2];
        for (int i = 0; i < shortValue.length; i++) {
            shortValue[i] = (short) ((data[i * 2] & 0xff) | ((data[i * 2 + 1] & 0xff) << 8));
        }
        return shortValue;
    }

    public static float byte2float(byte[] b)
    {
        int l;
        int index = 0;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

}
