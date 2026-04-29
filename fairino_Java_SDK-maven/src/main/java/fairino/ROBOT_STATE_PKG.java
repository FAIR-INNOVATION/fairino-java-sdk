package fairino;

/**
 * 机器人状态数据包 - 重构版本
 * 整合RobotStateData和原ROBOT_STATE_PKG，保持向后兼容
 * 用于CNDE协议(20005端口)实时状态反馈
 */
public class ROBOT_STATE_PKG {

    // ========== 基础字段 (与原ROBOT_STATE_PKG兼容) ==========

    // 帧头 (UINT16)
    public int frame_head;

    // 帧计数 (UINT8)
    public int frame_cnt;

    // 数据长度 (UINT16)
    public int data_len;

    // 程序状态 (UINT8) - 1-停止；2-运行；3-暂停
    public int program_state;

    // 机器人运动状态 (UINT8) - 1-停止；2-运行；3-暂停；4-拖动
    public int robot_state;

    // 主故障码 (INT32)
    public int main_code;

    // 子故障码 (INT32)
    public int sub_code;

    // 机器人模式 (UINT8) - 1-手动模式；0-自动模式
    public int robot_mode;

    // 6个轴当前关节位置 (DOUBLE_6)，单位deg
    public double[] jt_cur_pos = new double[6];

    // 工具当前位置 (DOUBLE_6) - [x,y,z,rx,ry,rz]
    public double[] tl_cur_pos = new double[6];

    // 末端法兰当前位置 (DOUBLE_6) - [x,y,z,rx,ry,rz]
    public double[] flange_cur_pos = new double[6];

    // 当前6个关节速度 (DOUBLE_6)，单位deg/s
    public double[] actual_qd = new double[6];

    // 当前6个关节加速度 (DOUBLE_6)，单位deg/s^2
    public double[] actual_qdd = new double[6];

    // TCP合成指令速度 (DOUBLE_2) - [位置mm/s, 姿态deg/s]
    public double[] target_TCP_CmpSpeed = new double[2];

    // TCP指令速度 (DOUBLE_6) - [vx,vy,vz,wx,wy,wz]
    public double[] target_TCP_Speed = new double[6];

    // TCP合成实际速度 (DOUBLE_2) - [位置mm/s, 姿态deg/s]
    public double[] actual_TCP_CmpSpeed = new double[2];

    // TCP实际速度 (DOUBLE_6) - [vx,vy,vz,wx,wy,wz]
    public double[] actual_TCP_Speed = new double[6];

    // 当前关节力矩 (DOUBLE_6)
    public double[] jt_cur_tor = new double[6];

    // 工具ID (INT32)
    public int tool;

    // 工件ID (INT32)
    public int user;

    // 控制柜数字输出高字节 (UINT8)
    public int cl_dgt_output_h;

    // 控制柜数字输出低字节 (UINT8)
    public int cl_dgt_output_l;

    // 工具数字输出低字节 (UINT8)
    public int tl_dgt_output_l;

    // 控制柜数字输入高字节 (UINT8)
    public int cl_dgt_input_h;

    // 控制柜数字输入低字节 (UINT8)
    public int cl_dgt_input_l;

    // 工具数字输入低字节 (UINT8)
    public int tl_dgt_input_l;

    // 控制柜模拟输入 (UINT16[2])
    public int[] cl_analog_input = new int[2];

    // 工具模拟输入 (UINT16)
    public int tl_anglog_input;

    // 力传感器原始数据 (DOUBLE_6)
    public double[] ft_sensor_raw_data = new double[6];

    // 力传感器数据 (DOUBLE_6)
    public double[] ft_sensor_data = new double[6];

    // 力传感器激活状态 (UINT8)
    public int ft_sensor_active;

    // 急停状态 (UINT8)
    public int EmergencyStop;

    // 运动完成 (INT32)
    public int motion_done;

    // 夹爪运动完成 (UINT8)
    public int gripper_motiondone;

    // 运动队列长度 (INT32)
    public int mc_queue_len;

    // 碰撞状态 (UINT8)
    public int collisionState;

    // 轨迹点序号 (INT32)
    public int trajectory_pnum;

    // 安全停止0状态 (UINT8)
    public int safety_stop0_state;

    // 安全停止1状态 (UINT8)
    public int safety_stop1_state;

    // 夹爪故障ID (UINT8)
    public int gripper_fault_id;

    // 夹爪故障 (UINT16)
    public int gripper_fault;

    // 夹爪激活 (UINT16)
    public int gripper_active;

    // 夹爪位置 (UINT8)
    public int gripper_position;

    // 夹爪速度 (原INT8，现int)
    public int gripper_speed;

    // 夹爪电流 (原INT8，现int)
    public int gripper_current;

    // 夹爪温度 (INT32)
    public int gripper_temp;

    // 夹爪电压 (INT32)
    public int gripper_voltage;

    // 内部辅助轴状态 (新数据结构)
    public AuxState aux_state = new AuxState();

    // 扩展轴状态数组
    public EXT_AXIS_STATUS[] extAxisStatus = new EXT_AXIS_STATUS[4];

    // 扩展IO
    public short[] extDIState = new short[8];
    public short[] extDOState = new short[8];
    public short[] extAIState = new short[4];
    public short[] extAOState = new short[4];

    // 机器人使能状态 (INT32)
    public int rbtEnableState;

    // 关节驱动器力矩 (DOUBLE_6)
    public double[] jointDriverTorque = new double[6];

    // 关节驱动器温度 (DOUBLE_6)
    public double[] jointDriverTemperature = new double[6];

    // 机器人时间对象
    public ROBOT_TIME robotTime = new ROBOT_TIME();

    // 软件升级状态 (INT32)
    public int softwareUpgradeState;

    // 末端Lua错误码 (UINT16)
    public int endLuaErrCode;

    // 控制柜模拟输出 (int[2])
    public int[] cl_analog_output = new int[2];

    // 工具模拟输出 (int)
    public int tl_analog_output;

    // 旋转夹爪圈数
    public float gripperRotNum;

    // 旋转夹爪速度 (UINT8)
    public int gripperRotSpeed;

    // 旋转夹爪力矩 (UINT8)
    public int gripperRotTorque;

    // 焊接中断状态
    public WELDING_BREAKOFF_STATE weldingBreakOffState = new WELDING_BREAKOFF_STATE();

    // 目标关节力矩 (DOUBLE_6)
    public double[] jt_tgt_tor = new double[6];

    // 智能工具状态 (INT32)
    public int smartToolState;

    // 宽电压控制箱温度
    public float wideVoltageCtrlBoxTemp;

    // 宽电压控制箱风扇电流 (UINT16)
    public int wideVoltageCtrlBoxFanCurrent;

    // 工具坐标系 (DOUBLE_6)
    public double[] toolCoord = new double[6];

    // 工件坐标系 (DOUBLE_6)
    public double[] wobjCoord = new double[6];

    // 外部工具坐标系 (DOUBLE_6)
    public double[] extoolCoord = new double[6];

    // 扩展轴坐标系 (DOUBLE_6)
    public double[] exAxisCoord = new double[6];

    // 负载 (DOUBLE)
    public double load;

    // 负载重心 (DOUBLE_3)
    public double[] loadCog = new double[3];

    // 上一次伺服J目标位置 (DOUBLE_6)
    public double[] lastServoTarget = new double[6];

    // 伺服J命令数量 (INT32)
    public int servoJCmdNum;

    // ========== 新增字段 (RobotStateData扩展) ==========

    // 目标关节位置 (DOUBLE_6)
    public double[] targetJointPos = new double[6];

    // 目标关节速度 (DOUBLE_6)
    public double[] targetJointVel = new double[6];

    // 目标关节加速度 (DOUBLE_6)
    public double[] targetJointAcc = new double[6];

    // 目标关节电流 (DOUBLE_6)
    public double[] targetJointCurrent = new double[6];

    // 实际关节电流 (DOUBLE_6)
    public double[] actualJointCurrent = new double[6];

    // 实际TCP力 (DOUBLE_6)
    public double[] actualTCPForce = new double[6];

    // 目标TCP位置 (DOUBLE_6)
    public double[] targetTCPPos = new double[6];

    // 碰撞等级 (UINT8_6)
    public int[] collisionLevel = new int[6];

    // 手动速度比例 (DOUBLE)
    public double speedScaleManual;

    // 自动速度比例 (DOUBLE)
    public double speedScaleAuto;

    // Lua行号 (INT32)
    public int luaLineNum;

    // 异常停止 (UINT8)
    public int abnomalStop;

    // 当前Lua文件名 (UINT8_256)
    public String currentLuaFileName;

    // 程序总行数 (UINT8)
    public int programTotalLine;

    // 安全箱信号 (UINT8_6)
    public int[] safetyBoxSingal = new int[6];

    // 焊接电压 (DOUBLE)
    public double weldVoltage;

    // 焊接电流 (DOUBLE)
    public double weldCurrent;

    // 焊接跟踪速度 (DOUBLE)
    public double weldTrackVel;

    // TPD异常 (UINT8)
    public int tpdException;

    // 报警重启机器人 (UINT8)
    public int alarmRebootRobot;

    // Modbus主站连接 (UINT8)
    public int modbusMasterConnect;

    // Modbus从站连接 (UINT8)
    public int modbusSlaveConnect;

    // 按钮盒停止信号 (UINT8)
    public int btnBoxStopSignal;

    // 拖动报警 (UINT8)
    public int dragAlarm;

    // 安全门报警 (UINT8)
    public int safetyDoorAlarm;

    // 安全平面报警 (UINT8)
    public int safetyPlaneAlarm;

    // 运动报警 (UINT8)
    public int motonAlarm;

    // 干涉报警 (UINT8)
    public int interfaceAlarm;

    // UDP命令状态 (INT32)
    public int udpCmdState;

    // 焊接准备状态 (UINT8)
    public int weldReadyState;

    // 报警检查急停按钮 (UINT8)
    public int alarmCheckEmergStopBtn;

    // 命令通信错误 (UINT8)
    public int tsTmCmdComError;

    // 状态通信错误 (UINT8)
    public int tsTmStateComError;

    // 控制箱错误 (INT32)
    public int ctrlBoxError;

    // 安全数据状态 (UINT8)
    public int safetyDataState;

    // 力传感器错误状态 (UINT8)
    public int forceSensorErrState;

    // 控制打开Lua错误码 (UINT8_4)
    public int[] ctrlOpenLuaErrCode = new int[4];

    // 奇异位置标志 (UINT8)
    public int strangePosFlag;

    // 报警 (UINT8)
    public int alarm;

    // 驱动器报警 (UINT8)
    public int driverAlarm;

    // 存活从站数量错误 (UINT8)
    public int aliveSlaveNumError;

    // 从站通信错误 (UINT8_8)
    public int[] slaveComError = new int[8];

    // 命令点错误 (UINT8)
    public int cmdPointError;

    // IO错误 (UINT8)
    public int IOError;

    // 夹爪错误 (UINT8)
    public int gripperError;

    // 文件错误 (UINT8)
    public int fileError;

    // 参数错误 (UINT8)
    public int paraError;

    // 扩展轴超出软限位错误 (UINT8)
    public int exaxisOutLimitError;

    // 驱动器通信错误 (UINT8_6)
    public int[] driverComError = new int[6];

    // 驱动器错误 (UINT8)
    public int driverError;

    // 超出软限位错误 (UINT8)
    public int outSoftLimitError;

    // 通用轴通信数据 (UINT8_130)
    public byte[] axleGenComData = new byte[130];

    // 校验和 (UINT16)
    public int check_sum;

    // Socket连接超时 (UINT8)
    public int socketConnTimeout;

    // Socket读取超时 (UINT8)
    public int socketReadTimeout;

    // TS Web状态通信错误 (UINT8)
    public int tsWebStateComErr;

    // ========== 辅助类定义 ==========

    /**
     * 辅助轴状态类 (新版)
     */
    public static class AuxState {
        public int servoId;           // UINT8
        public int servoErrCode;      // INT32
        public int servoState;        // INT32
        public double servoPos;       // DOUBLE
        public double servoVel;       // DOUBLE (原为float)
        public double servoTorque;    // DOUBLE (原为float)

        public void clear() {
            servoId = 0;
            servoErrCode = 0;
            servoState = 0;
            servoPos = 0.0;
            servoVel = 0.0f;
            servoTorque = 0.0f;
        }
    }

    /**
     * 焊接中断状态类 (新版)
     */
    public static class WeldingBreakOffState {
        public int breakOffState;   // UINT8
        public int weldArcState;    // UINT8

        public void clear() {
            breakOffState = 0;
            weldArcState = 0;
        }
    }

    /**
     * 清空所有数据
     */
    public void clear() {
        frame_head = 0;
        frame_cnt = 0;
        data_len = 0;
        program_state = 0;
        robot_state = 0;
        main_code = 0;
        sub_code = 0;
        robot_mode = 0;

        for (int i = 0; i < 6; i++) {
            jt_cur_pos[i] = 0.0;
            tl_cur_pos[i] = 0.0;
            flange_cur_pos[i] = 0.0;
            actual_qd[i] = 0.0;
            actual_qdd[i] = 0.0;
            target_TCP_Speed[i] = 0.0;
            actual_TCP_Speed[i] = 0.0;
            jt_cur_tor[i] = 0.0;
            ft_sensor_raw_data[i] = 0.0;
            ft_sensor_data[i] = 0.0;
            jointDriverTorque[i] = 0.0;
            jointDriverTemperature[i] = 0.0;
            jt_tgt_tor[i] = 0.0;
            toolCoord[i] = 0.0;
            wobjCoord[i] = 0.0;
            extoolCoord[i] = 0.0;
            exAxisCoord[i] = 0.0;
            lastServoTarget[i] = 0.0;
            targetJointPos[i] = 0.0;
            targetJointVel[i] = 0.0;
            targetJointAcc[i] = 0.0;
            targetJointCurrent[i] = 0.0;
            actualJointCurrent[i] = 0.0;
            actualTCPForce[i] = 0.0;
            targetTCPPos[i] = 0.0;
            collisionLevel[i] = 0;
            safetyBoxSingal[i] = 0;
        }

        target_TCP_CmpSpeed[0] = 0.0;
        target_TCP_CmpSpeed[1] = 0.0;
        actual_TCP_CmpSpeed[0] = 0.0;
        actual_TCP_CmpSpeed[1] = 0.0;

        tool = 0;
        user = 0;
        cl_dgt_output_h = 0;
        cl_dgt_output_l = 0;
        tl_dgt_output_l = 0;
        cl_dgt_input_h = 0;
        cl_dgt_input_l = 0;
        tl_dgt_input_l = 0;

        cl_analog_input[0] = 0;
        cl_analog_input[1] = 0;
        tl_anglog_input = 0;

        ft_sensor_active = 0;
        EmergencyStop = 0;
        motion_done = 0;
        gripper_motiondone = 0;
        mc_queue_len = 0;
        collisionState = 0;
        trajectory_pnum = 0;
        safety_stop0_state = 0;
        safety_stop1_state = 0;
        gripper_fault_id = 0;
        gripper_fault = 0;
        gripper_active = 0;
        gripper_position = 0;
        gripper_speed = 0;
        gripper_current = 0;
        gripper_temp = 0;
        gripper_voltage = 0;

        aux_state.clear();

        for (int i = 0; i < 4; i++) {
            if (extAxisStatus[i] == null) {
                extAxisStatus[i] = new EXT_AXIS_STATUS();
            } else {
                extAxisStatus[i].pos = 0.0;
                extAxisStatus[i].vel = 0.0;
                extAxisStatus[i].errorCode = 0;
                extAxisStatus[i].ready = 0;
                extAxisStatus[i].inPos = 0;
                extAxisStatus[i].alarm = 0;
                extAxisStatus[i].flerr = 0;
                extAxisStatus[i].nlimit = 0;
                extAxisStatus[i].pLimit = 0;
                extAxisStatus[i].mdbsOffLine = 0;
                extAxisStatus[i].mdbsTimeout = 0;
                extAxisStatus[i].homingStatus = 0;
            }
        }

        for (int i = 0; i < 8; i++) {
            extDIState[i] = 0;
            extDOState[i] = 0;
        }
        for (int i = 0; i < 4; i++) {
            extAIState[i] = 0;
            extAOState[i] = 0;
        }

        rbtEnableState = 0;

        robotTime.year = 0;
        robotTime.month = 0;
        robotTime.day = 0;
        robotTime.hour = 0;
        robotTime.minute = 0;
        robotTime.second = 0;
        robotTime.millisecond = 0;

        softwareUpgradeState = 0;
        endLuaErrCode = 0;

        cl_analog_output[0] = 0;
        cl_analog_output[1] = 0;
        tl_analog_output = 0;

        gripperRotNum = 0.0f;
        gripperRotSpeed = 0;
        gripperRotTorque = 0;

        weldingBreakOffState.breakOffState = 0;
        weldingBreakOffState.weldArcState = 0;

        smartToolState = 0;
        wideVoltageCtrlBoxTemp = 0.0f;
        wideVoltageCtrlBoxFanCurrent = 0;

        load = 0.0;
        loadCog[0] = 0.0;
        loadCog[1] = 0.0;
        loadCog[2] = 0.0;

        servoJCmdNum = 0;

        speedScaleManual = 0.0;
        speedScaleAuto = 0.0;
        luaLineNum = 0;
        abnomalStop = 0;

//        for (int i = 0; i < 256; i++) currentLuaFileName[i] = 0;

        programTotalLine = 0;

        weldVoltage = 0.0;
        weldCurrent = 0.0;
        weldTrackVel = 0.0;
        tpdException = 0;
        alarmRebootRobot = 0;
        modbusMasterConnect = 0;
        modbusSlaveConnect = 0;
        btnBoxStopSignal = 0;
        dragAlarm = 0;
        safetyDoorAlarm = 0;
        safetyPlaneAlarm = 0;
        motonAlarm = 0;
        interfaceAlarm = 0;
        udpCmdState = 0;
        weldReadyState = 0;
        alarmCheckEmergStopBtn = 0;
        tsTmCmdComError = 0;
        tsTmStateComError = 0;
        ctrlBoxError = 0;
        safetyDataState = 0;
        forceSensorErrState = 0;

        for (int i = 0; i < 4; i++) ctrlOpenLuaErrCode[i] = 0;

        strangePosFlag = 0;
        alarm = 0;
        driverAlarm = 0;
        aliveSlaveNumError = 0;

        for (int i = 0; i < 8; i++) slaveComError[i] = 0;

        cmdPointError = 0;
        IOError = 0;
        gripperError = 0;
        fileError = 0;
        paraError = 0;
        exaxisOutLimitError = 0;

        for (int i = 0; i < 6; i++) driverComError[i] = 0;

        driverError = 0;
        outSoftLimitError = 0;

        for (int i = 0; i < 130; i++) axleGenComData[i] = 0;

        check_sum = 0;
    }

    public ROBOT_STATE_PKG() {
        // 初始化扩展轴状态数组元素
        for (int i = 0; i < 4; i++) {
            if (extAxisStatus[i] == null) {
                extAxisStatus[i] = new EXT_AXIS_STATUS();
            }
        }
    }
}

