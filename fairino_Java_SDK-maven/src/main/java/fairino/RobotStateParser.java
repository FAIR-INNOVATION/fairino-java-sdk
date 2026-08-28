package fairino;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机器人状态解析器
 * 负责状态配置帧的组装和数据帧的解析
 * 使用反射自动映射枚举值到结构体字段
 */
public class RobotStateParser {

    // 状态映射信息类
    public static class StateMapping {
        public final String serverName;      // 发送给服务器的字符串
        public final String fieldName;       // 结构体字段名
        public final DataType serverType;    // 服务器发送的数据类型
        public final DataType structType;    // 结构体字段类型
        public final int size;               // 字节大小

        public StateMapping(String serverName, String fieldName, DataType structType, DataType serverType) {
            this.serverName = serverName;
            this.fieldName = fieldName;
            this.serverType = serverType;
            this.structType = structType;
            this.size = serverType.size;
        }
    }

    // 数据类型枚举
    public enum DataType {
        UINT8(1), UINT8_2(2), UINT8_4(4), UINT8_6(6), UINT8_8(8), UINT8_16(16), UINT8_29(29), UINT8_116(116), UINT8_130(130), UINT8_256(256),
        EXT_AXIS_STATUS_4(116),  // 4个扩展轴状态，每个29字节: 2×double(16)+int(4)+9×uint8(9)=29, 4×29=116
        AUX_STATE(25),  // UINT8+INT32+INT32+DOUBLE+DOUBLE+DOUBLE = 1+4+4+8+8+8 = 33
        ROBOT_TIME(28),  // UINT16+UINT8+UINT8+UINT8+UINT8+UINT8+UINT16 = 2+1+1+1+1+1+2 = 9
        DOUBLE_2_TO_UINT16_2(16),  // 服务器发DOUBLE×2(16字节)，转存为UINT16×2(4字节)
        INT8(1),
        UINT16(2), UINT16_2(4), UINT16_4(8), UINT16_8(16),
        INT16(2),
        INT32(4), INT32_4(16), INT32_7(28),
        UINT32(4),
        FLOAT(4),
        DOUBLE(8), DOUBLE_2(16), DOUBLE_3(24), DOUBLE_6(48);

        public final int size;

        DataType(int size) {
            this.size = size;
        }
    }

    // 状态映射表 (枚举 -> 映射信息)
    private static final Map<RobotState, StateMapping> stateMappingMap = new HashMap<>();

    // 反射字段缓存表 (枚举 -> 字段)
    private static final Map<RobotState, Field> fieldMap = new HashMap<>();

    // 默认状态配置列表
    private static final List<RobotState> stateConfigList = new ArrayList<>();

    static {
        // 初始化状态映射
        initStateMapping();

        // 初始化反射字段映射
        initFieldMap();

    }

    /**
     * 初始化状态映射表
     * 根据枚举字段与结构体对应关系文件配置
     */
    private static void initStateMapping() {
        // 程序和机器人状态
        addMapping(RobotState.ProgramState, "program_state", "program_state", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.RobotState, "robot_state", "robot_state", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.MainCode, "main_code", "main_code", DataType.INT32, DataType.INT32);
        addMapping(RobotState.SubCode, "sub_code", "sub_code", DataType.INT32, DataType.INT32);
        addMapping(RobotState.RobotMode, "robot_mode", "robot_mode", DataType.UINT8, DataType.UINT8);

        // 位置和速度 (DOUBLE_6)
        addMapping(RobotState.JointCurPos, "actual_joint_pos", "jt_cur_pos", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.ToolCurPos, "actual_TCP_pos", "tl_cur_pos", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.FlangeCurPos, "actual_flange_pos", "flange_cur_pos", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.ActualJointVel, "actual_joint_vel", "actual_qd", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.ActualJointAcc, "actual_joint_acc", "actual_qdd", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.TargetTCPCmpSpeed, "target_TCP_cmpvel", "target_TCP_CmpSpeed", DataType.DOUBLE_2, DataType.DOUBLE_2);
        addMapping(RobotState.TargetTCPSpeed, "target_TCP_vel", "target_TCP_Speed", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.ActualTCPCmpSpeed, "actual_TCP_cmpvel", "actual_TCP_CmpSpeed", DataType.DOUBLE_2, DataType.DOUBLE_2);
        addMapping(RobotState.ActualTCPSpeed, "actual_TCP_vel", "actual_TCP_Speed", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.ActualJointTorque, "actual_joint_torque", "jt_cur_tor", DataType.DOUBLE_6, DataType.DOUBLE_6);

        // 工具和工件ID
        addMapping(RobotState.Tool, "tool_id", "tool", DataType.INT32, DataType.INT32);
        addMapping(RobotState.User, "wobj_id", "user", DataType.INT32, DataType.INT32);

        // 数字IO (UINT8)
        addMapping(RobotState.ClDgtOutputH, "cfg_DO_box", "cl_dgt_output_h", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.ClDgtOutputL, "std_DO_box", "cl_dgt_output_l", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.TlDgtOutputL, "cfg_DO_tool", "tl_dgt_output_l", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.ClDgtInputH, "cfg_DI_box", "cl_dgt_input_h", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.ClDgtInputL, "std_DI_box", "cl_dgt_input_l", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.TlDgtInputL, "cfg_DI_tool", "tl_dgt_input_l", DataType.UINT8, DataType.UINT8);

        // 模拟IO (需要转换: 服务器发DOUBLE, 结构体存UINT16)
        addMapping(RobotState.ClAnalogInput, "std_AI0_box,std_AI1_box", "cl_analog_input", DataType.DOUBLE_2, DataType.DOUBLE_2);  // 服务器返回2个DOUBLE
        addMapping(RobotState.TlAnglogInput, "std_AI_tool", "tl_anglog_input", DataType.INT32, DataType.DOUBLE);

        // 力传感器
        addMapping(RobotState.FtSensorRawData, "ft_sensor_raw_data", "ft_sensor_raw_data", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.FtSensorData, "ft_sensor_data", "ft_sensor_data", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.FtSensorActive, "ft_sensor_active", "ft_sensor_active", DataType.UINT8, DataType.UINT8);

        // 安全和运动状态
        addMapping(RobotState.EmergencyStop, "emergency_stop", "EmergencyStop", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.MotionDone, "motion_done", "motion_done", DataType.INT32, DataType.INT32);
        addMapping(RobotState.GripperMotiondone, "gripper_motion_done", "gripper_motiondone", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.McQueueLen, "motion_queue_len", "mc_queue_len", DataType.INT32, DataType.INT32);
        addMapping(RobotState.CollisionState, "collision_state", "collisionState", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.TrajectoryPnum, "trajectory_pnum", "trajectory_pnum", DataType.INT32, DataType.INT32);
        addMapping(RobotState.SafetyStop0State, "safety_stop0_state", "safety_stop0_state", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.SafetyStop1State, "safety_stop1_state", "safety_stop1_state", DataType.UINT8, DataType.UINT8);

        // 夹爪状态
        addMapping(RobotState.GripperFaultId, "gripper_fault_id", "gripper_fault_id", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.GripperFault, "gripper_fault", "gripper_fault", DataType.UINT16, DataType.INT32);
        addMapping(RobotState.GripperActive, "gripper_active", "gripper_active", DataType.UINT16, DataType.INT32);
        addMapping(RobotState.GripperPosition, "gripper_position", "gripper_position", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.GripperSpeed, "gripper_speed", "gripper_speed", DataType.INT8, DataType.INT32);
        addMapping(RobotState.GripperCurrent, "gripper_current", "gripper_current", DataType.INT8, DataType.INT32);
        addMapping(RobotState.GripperTemp, "gripper_temp", "gripper_temp", DataType.INT32, DataType.INT32);
        addMapping(RobotState.GripperVoltage, "gripper_voltage", "gripper_voltage", DataType.INT32, DataType.INT32);

        // 辅助轴状态 (复合类型: UINT8+INT32+INT32+DOUBLE+DOUBLE+DOUBLE)
        addMapping(RobotState.AuxState, "aux_axis_state", "aux_state", DataType.AUX_STATE, DataType.AUX_STATE);
        addMapping(RobotState.ExtAxisStatus, "exaxis_status", "extAxisStatus", DataType.EXT_AXIS_STATUS_4, DataType.EXT_AXIS_STATUS_4);

        // 扩展IO
        addMapping(RobotState.ExtDIState, "ext_DI_state", "extDIState", DataType.UINT16_8, DataType.UINT8_16);
        addMapping(RobotState.ExtDOState, "ext_DO_state", "extDOState", DataType.UINT16_8, DataType.UINT8_16);
        addMapping(RobotState.ExtAIState, "ext_AI_state", "extAIState", DataType.UINT16_4, DataType.INT32_4);
        addMapping(RobotState.ExtAOState, "ext_AO_state", "extAOState", DataType.UINT16_4, DataType.INT32_4);
        addMapping(RobotState.RbtEnableState, "rbt_enable_state", "rbtEnableState", DataType.INT32, DataType.INT32);

        // 关节驱动器
        addMapping(RobotState.JointDriverTorque, "joint_driver_torque", "jointDriverTorque", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.JointDriverTemperature, "actual_joint_temp", "jointDriverTemperature", DataType.DOUBLE_6, DataType.DOUBLE_6);

        // 时间和软件状态
        addMapping(RobotState.RobotTime, "robot_time", "robotTime", DataType.INT32_7, DataType.INT32_7);
        addMapping(RobotState.SoftwareUpgradeState, "software_upgrade_state", "softwareUpgradeState", DataType.INT32, DataType.INT32);
        addMapping(RobotState.EndLuaErrCode, "end_lua_err_code", "endLuaErrCode", DataType.UINT16, DataType.INT32);

        // 模拟输出 (需要转换: 服务器发DOUBLE, 结构体存UINT16)
        addMapping(RobotState.ClAnalogOutput, "std_AO0_box,std_AO1_box", "cl_analog_output", DataType.DOUBLE_2, DataType.DOUBLE_2);  // 服务器返回2个DOUBLE
        addMapping(RobotState.TlAnalogOutput, "std_AO_tool", "tl_analog_output", DataType.UINT16, DataType.DOUBLE);

        // 旋转夹爪
        addMapping(RobotState.GripperRotNum, "rotating_gripper_num", "gripperRotNum", DataType.FLOAT, DataType.DOUBLE);
        addMapping(RobotState.GripperRotSpeed, "rotating_gripper_speed", "gripperRotSpeed", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.GripperRotTorque, "rotating_gripper_tor", "gripperRotTorque", DataType.UINT8, DataType.UINT8);

        // 焊接状态
        addMapping(RobotState.WeldingBreakOffState, "weld_break_off_state,weld_arc_state", "weldingBreakOffState", DataType.UINT8_2, DataType.UINT8_2);
        addMapping(RobotState.TargetJointTorque, "target_joint_torque", "jt_tgt_tor", DataType.DOUBLE_6, DataType.DOUBLE_6);

        // 工具状态
        addMapping(RobotState.SmartToolState, "smarttool_state", "smartToolState", DataType.INT32, DataType.UINT32);
        addMapping(RobotState.WideVoltageCtrlBoxTemp, "wide_voltage_ctrl_box_temp", "wideVoltageCtrlBoxTemp", DataType.FLOAT, DataType.DOUBLE);
        addMapping(RobotState.WideVoltageCtrlBoxFanCurrent, "wide_voltage_ctrl_box_fan_current", "wideVoltageCtrlBoxFanCurrent", DataType.INT32, DataType.INT32);

        // 坐标系
        addMapping(RobotState.ToolCoord, "tool_coord", "toolCoord", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.WobjCoord, "wobj_coord", "wobjCoord", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.ExtoolCoord, "exTool_coord", "extoolCoord", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.ExAxisCoord, "exAxis_coord", "exAxisCoord", DataType.DOUBLE_6, DataType.DOUBLE_6);

        // 负载
        addMapping(RobotState.Load, "payload", "load", DataType.DOUBLE, DataType.DOUBLE);
        addMapping(RobotState.LoadCog, "pay_cog", "loadCog", DataType.DOUBLE_3, DataType.DOUBLE_3);
        addMapping(RobotState.LastServoTarget, "last_servoJ_target", "lastServoTarget", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.ServoJCmdNum, "servoJ_cmd_num", "servoJCmdNum", DataType.INT32, DataType.INT32);

        // 目标位置
        addMapping(RobotState.TargetJointPos, "target_joint_pos", "targetJointPos", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.TargetJointVel, "target_joint_vel", "targetJointVel", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.TargetJointAcc, "target_joint_acc", "targetJointAcc", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.TargetJointCurrent, "target_joint_current", "targetJointCurrent", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.ActualJointCurrent, "actual_joint_current", "actualJointCurrent", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.ActualTCPForce, "actual_TCP_force", "actualTCPForce", DataType.DOUBLE_6, DataType.DOUBLE_6);
        addMapping(RobotState.TargetTCPPos, "target_TCP_pos", "targetTCPPos", DataType.DOUBLE_6, DataType.DOUBLE_6);

        // 碰撞等级和速度
        addMapping(RobotState.CollisionLevel, "collision_level", "collisionLevel", DataType.UINT8_6, DataType.UINT8_6);
        addMapping(RobotState.SpeedScaleManual, "speed_scaling_man", "speedScaleManual", DataType.DOUBLE, DataType.DOUBLE);
        addMapping(RobotState.SpeedScaleAuto, "speed_scaling_auto", "speedScaleAuto", DataType.DOUBLE, DataType.DOUBLE);

        // Lua程序
        addMapping(RobotState.LuaLineNum, "line_number", "luaLineNum", DataType.INT32, DataType.INT32);
        addMapping(RobotState.AbnomalStop, "abnormal_stop", "abnomalStop", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.CurrentLuaFileName, "cur_lua_file_name", "currentLuaFileName", DataType.UINT8_256, DataType.UINT8_256);
        addMapping(RobotState.ProgramTotalLine, "prog_total_line", "programTotalLine", DataType.UINT8, DataType.UINT8);

        // 安全信号
        addMapping(RobotState.SafetyBoxSingal, "safety_box_signal", "safetyBoxSingal", DataType.UINT8_6, DataType.UINT8_6);

        // 焊接数据
        addMapping(RobotState.WeldVoltage, "welding_voltage", "weldVoltage", DataType.DOUBLE, DataType.DOUBLE);
        addMapping(RobotState.WeldCurrent, "welding_current", "weldCurrent", DataType.DOUBLE, DataType.DOUBLE);
        addMapping(RobotState.WeldTrackVel, "welding_track_speed", "weldTrackVel", DataType.DOUBLE, DataType.DOUBLE);

        // 异常和报警
        addMapping(RobotState.TpdException, "tpd_exception", "tpdException", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.AlarmRebootRobot, "alarm_reboot_robot", "alarmRebootRobot", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.ModbusMasterConnect, "modbus_master_connect", "modbusMasterConnect", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.ModbusSlaveConnect, "modbus_slave_connect", "modbusSlaveConnect", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.BtnBoxStopSignal, "btn_box_stop_signal", "btnBoxStopSignal", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.DragAlarm, "drag_alarm", "dragAlarm", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.SafetyDoorAlarm, "safety_door_alarm", "safetyDoorAlarm", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.SafetyPlaneAlarm, "safety_plane_alarm", "safetyPlaneAlarm", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.MotonAlarm, "motion_alarm", "motonAlarm", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.InterfaceAlarm, "interfere_alarm", "interfaceAlarm", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.UdpCmdState, "udp_cmd_state", "udpCmdState", DataType.INT32, DataType.INT32);
        addMapping(RobotState.WeldReadyState, "weld_ready_state", "weldReadyState", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.AlarmCheckEmergStopBtn, "alarm_check_emerg_stop_btn", "alarmCheckEmergStopBtn", DataType.UINT8, DataType.UINT8);

        // 通信错误
        addMapping(RobotState.TsTmCmdComError, "ts_tm_cmd_com_err", "tsTmCmdComError", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.TsTmStateComError, "ts_tm_state_com_err", "tsTmStateComError", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.SocketConnTimeout, "socket_conn_timeout", "socketConnTimeout", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.SocketReadTimeout, "socket_read_timeout", "socketReadTimeout", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.TsWebStateComErr, "ts_web_state_com_err", "tsWebStateComErr", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.CtrlBoxError, "ctrl_box_err_code", "ctrlBoxError", DataType.INT32, DataType.INT32);
        addMapping(RobotState.SafetyDataState, "safety_data_state", "safetyDataState", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.ForceSensorErrState, "force_sensor_err_state", "forceSensorErrState", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.CtrlOpenLuaErrCode, "ctrl_open_lua_errcode", "ctrlOpenLuaErrCode", DataType.UINT8_4, DataType.UINT8_4);

        // 错误标志
        addMapping(RobotState.StrangePosFlag, "strange_pos_flag", "strangePosFlag", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.Alarm, "alarm", "alarm", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.DriverAlarm, "dr_alarm", "driverAlarm", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.AliveSlaveNumError, "alive_slave_num_error", "aliveSlaveNumError", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.SlaveComError, "slave_com_error", "slaveComError", DataType.UINT8_8, DataType.UINT8_8);
        addMapping(RobotState.CmdPointError, "cmd_point_error", "cmdPointError", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.IOError, "IO_error", "IOError", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.GripperError, "gripper_error", "gripperError", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.FileError, "file_error", "fileError", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.ParaError, "para_error", "paraError", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.ExaxisOutLimitError, "exaxis_out_slimit_error", "exaxisOutLimitError", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.DriverComError, "dr_com_err", "driverComError", DataType.UINT8_6, DataType.UINT8_6);
        addMapping(RobotState.DriverError, "dr_err", "driverError", DataType.UINT8, DataType.UINT8);
        addMapping(RobotState.OutSoftLimitError, "out_sflimit_err", "outSoftLimitError", DataType.UINT8, DataType.UINT8);

        // 通用数据
        addMapping(RobotState.AxleGenComData, "axle_gen_com_data", "axleGenComData", DataType.UINT8_130, DataType.UINT8_130);

        // 扩展轴
        addMapping(RobotState.ExaxisCoordID, "exaxis_coord_id", "exaxisCoordID", DataType.UINT8, DataType.UINT8);

        addMapping(RobotState.ProgramRunState, "program_run_state", "ProgramRunState", DataType.UINT8, DataType.UINT8);

    }

    private static void addMapping(RobotState state, String serverName, String fieldName, DataType structType, DataType serverType) {
        stateMappingMap.put(state, new StateMapping(serverName, fieldName, structType, serverType));
    }

    /**
     * 初始化反射字段映射
     */
    private static void initFieldMap() {
        for (RobotState state : RobotState.values()) {
            StateMapping mapping = stateMappingMap.get(state);
            if (mapping == null) continue;

            try {
                Field field = ROBOT_STATE_PKG.class.getDeclaredField(mapping.fieldName);
                field.setAccessible(true);
                fieldMap.put(state, field);
            } catch (NoSuchFieldException e) {
                // 字段不存在，跳过
            }
        }
    }

    /**
     * 获取状态映射
     */
    public static StateMapping getStateMapping(RobotState state) {
        return stateMappingMap.get(state);
    }

    /**
     * 获取状态大小（字节数）
     */
    public static int getStateSize(RobotState state) {
        StateMapping mapping = stateMappingMap.get(state);
        return mapping != null ? mapping.size : 0;
    }

    /**
     * 获取状态的数据类型
     * @param state 机器人状态枚举
     * @return 数据类型，不存在则返回null
     */
    public static DataType getStateType(RobotState state) {
        StateMapping mapping = stateMappingMap.get(state);
        return mapping != null ? mapping.serverType : null;
    }

    /**
     * 计算配置列表的总字节数
     */
    public static int calculateTotalSize(List<RobotState> configList) {
        int totalSize = 0;
        for (RobotState state : configList) {
            totalSize += getStateSize(state);
        }
        return totalSize;
    }

    /**
     * 生成配置帧数据内容
     * 格式：状态服务器名1,状态服务器名2,状态服务器名3 (逗号分隔的字符串)
     * @param configList 配置的状态列表
     * @return 字节数组
     */
    public static byte[] generateConfigData(List<RobotState> configList) {
        if (configList == null || configList.isEmpty()) {
            return new byte[0];
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < configList.size(); i++) {
            StateMapping mapping = stateMappingMap.get(configList.get(i));
            if (mapping != null) {
                sb.append(mapping.serverName);
                if (i < configList.size() - 1) {
                    sb.append(",");
                }
            }
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 解析数据帧到结构体
     * @param data 数据帧中的data部分（字节数组）
     * @param configList 配置的状态顺序列表
     * @param statePkg 解析结果存储的结构体 (ROBOT_STATE_PKG)
     * @return 0-成功，其他-错误码
     */
    public static int parseData(byte[] data, List<RobotState> configList, ROBOT_STATE_PKG statePkg) {
        if (data == null || configList == null || statePkg == null) {
            return -1;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int offset = 0;
        int totalExpectedSize = 0;
        for (RobotState state : configList) {
            StateMapping mapping = stateMappingMap.get(state);
            if (mapping != null) {
                totalExpectedSize += mapping.size;
            }
        }
        //System.out.println("[DEBUG] Configured states count: " + configList.size() + ", expected total size: " + totalExpectedSize + ", actual data length: " + data.length);

        for (RobotState state : configList) {
            StateMapping mapping = stateMappingMap.get(state);
            if (mapping == null) continue;

            int size = mapping.size;
            if (offset + size > data.length) {
                System.err.println("Parse error: insufficient data for state " + state.name() + " at offset " + offset + ", need " + size + " bytes, but only " + (data.length - offset) + " remaining");
                return -2;
            }

            buffer.position(offset);

            try {
                parseStateValue(statePkg, state, mapping, buffer);
            } catch (Exception e) {
                System.err.println("Parse error for state " + state.name() + ": " + e.getMessage());
                e.printStackTrace();
                return -3;
            }

            offset += size;
        }

        return 0;
    }

    /**
     * 解析单个状态的值并设置到结构体
     */
    private static void parseStateValue(ROBOT_STATE_PKG statePkg, RobotState state,
                                         StateMapping mapping, ByteBuffer buffer) throws Exception {
        Field field = fieldMap.get(state);
        if (field == null) {
            // 字段不存在，跳过
            buffer.position(buffer.position() + mapping.size);
            return;
        }

        // 获取字段所在的对象（处理嵌套类）
        Object targetObj = statePkg;

        // 根据服务器数据类型读取并转换
        switch (mapping.serverType) {
            case UINT8:
                int uint8Val = buffer.get() & 0xFF;
                setValue(targetObj, field, uint8Val, mapping.structType);
                break;

            case INT8:
                int int8Val = buffer.get();
                setValue(targetObj, field, int8Val, mapping.structType);
                break;

            case UINT16:
                int uint16Val = buffer.getShort() & 0xFFFF;
                setValue(targetObj, field, uint16Val, mapping.structType);
                break;

            case INT16:
                int int16Val = buffer.getShort();
                setValue(targetObj, field, int16Val, mapping.structType);
                break;

            case INT32:
                int int32Val = buffer.getInt();
                setValue(targetObj, field, int32Val, mapping.structType);
                break;

            case UINT32:
                long uint32Val = buffer.getInt() & 0xFFFFFFFFL;
                setValue(targetObj, field, (int)uint32Val, mapping.structType);
                break;

            case FLOAT:
                float floatVal = buffer.getFloat();
                setValue(targetObj, field, floatVal, mapping.structType);
                break;

            case DOUBLE:
                double doubleVal = buffer.getDouble();
                setValue(targetObj, field, doubleVal, mapping.structType);
                break;

            case UINT8_2:
                // 处理嵌套类 WeldingBreakOffState (2个UINT8字段)
                if (field.getType() == ROBOT_STATE_PKG.WeldingBreakOffState.class) {
                    ROBOT_STATE_PKG.WeldingBreakOffState weldState =
                        (ROBOT_STATE_PKG.WeldingBreakOffState) field.get(targetObj);
                    weldState.breakOffState = buffer.get() & 0xFF;
                    weldState.weldArcState = buffer.get() & 0xFF;
                } else if (field.getType() == int[].class) {
                    int[] arr = (int[]) field.get(targetObj);
                    for (int i = 0; i < 2 && i < arr.length; i++) {
                        arr[i] = buffer.get() & 0xFF;
                    }
                } else if (field.getType() == byte[].class) {
                    byte[] arr = (byte[]) field.get(targetObj);
                    buffer.get(arr, 0, Math.min(2, arr.length));
                } else {
                    buffer.position(buffer.position() + 2);
                }
                break;

            case UINT8_4:
            case UINT8_6:
            case UINT8_8:
            case UINT8_16:
            case UINT8_29:
            case UINT8_130:
            case UINT8_116:
            case UINT8_256:
            case EXT_AXIS_STATUS_4:
                int arraySize = mapping.serverType.size;
                if (field.getType() == int[].class) {
                    int[] arr = (int[]) field.get(targetObj);
                    for (int i = 0; i < arraySize && i < arr.length; i++) {
                        arr[i] = buffer.get() & 0xFF;
                    }
                } else if (field.getType() == byte[].class) {
                    byte[] arr = (byte[]) field.get(targetObj);
                    buffer.get(arr, 0, Math.min(arraySize, arr.length));
                } else if (field.getType() == String.class) {
                    // 处理字符串类型（如currentLuaFileName），读取256字节并转换为String
                    byte[] strBytes = new byte[arraySize];
                    buffer.get(strBytes, 0, arraySize);
                    // 找到第一个0字节作为字符串结束标志
                    int len = 0;
                    while (len < strBytes.length && strBytes[len] != 0) {
                        len++;
                    }
                    String str = new String(strBytes, 0, len, java.nio.charset.StandardCharsets.UTF_8);
                    field.set(targetObj, str);
                } else if (field.getType() == EXT_AXIS_STATUS[].class) {
                    // 处理EXT_AXIS_STATUS[4]数组 (29字节×4轴=116字节)
                    // C结构体顺序: pos(double)+vel(double)+errorCode(int)+9个uint8
                    EXT_AXIS_STATUS[] statuses = (EXT_AXIS_STATUS[]) field.get(targetObj);
                    for (int i = 0; i < 4 && i < statuses.length; i++) {
                        // 每个轴29字节，按C结构体顺序解析
                        // pos (8字节 double)
                        statuses[i].pos = buffer.getDouble();
                        // vel (8字节 double)
                        statuses[i].vel = buffer.getDouble();
                        // errorCode (4字节 int)
                        statuses[i].errorCode = buffer.getInt();
                        // 9个uint8字段
                        statuses[i].ready = buffer.get() & 0xFF;
                        statuses[i].inPos = buffer.get() & 0xFF;
                        statuses[i].alarm = buffer.get() & 0xFF;
                        statuses[i].flerr = buffer.get() & 0xFF;
                        statuses[i].nlimit = buffer.get() & 0xFF;
                        statuses[i].pLimit = buffer.get() & 0xFF;
                        statuses[i].mdbsOffLine = buffer.get() & 0xFF;
                        statuses[i].mdbsTimeout = buffer.get() & 0xFF;
                        statuses[i].homingStatus = buffer.get() & 0xFF;
                    }
                } else {
                    buffer.position(buffer.position() + arraySize);
                }
                break;

            case AUX_STATE:
                if (field.getType() == ROBOT_STATE_PKG.AuxState.class) {
                    ROBOT_STATE_PKG.AuxState aux = (ROBOT_STATE_PKG.AuxState) field.get(targetObj);
                    aux.servoId = buffer.get() & 0xFF;           // 1 byte
                    aux.servoErrCode = buffer.getInt();          // 4 bytes
                    aux.servoState = buffer.getInt();            // 4 bytes
                    aux.servoPos = buffer.getDouble();           // 8 bytes
                    aux.servoVel = buffer.getFloat();           // 4 bytes
                    aux.servoTorque = buffer.getFloat();        // 4 bytes
                } else {
                    buffer.position(buffer.position() + 25);  // 跳过33字节
                }
                break;

            case ROBOT_TIME:

                // 处理机器人时间对象 (UINT16+UINT8+UINT8+UINT8+UINT8+UINT8+UINT16)
                if (field.getType() == ROBOT_TIME.class) {
                    ROBOT_TIME rt = (ROBOT_TIME) field.get(targetObj);
                    rt.year = buffer.getShort() & 0xFFFF;   // year (UINT16)
                    rt.month = buffer.get() & 0xFF;          // month (UINT8) - 注意：ROBOT_TIME类中是mouth
                    rt.day = buffer.get() & 0xFF;          // day (UINT8)
                    rt.hour = buffer.get() & 0xFF;          // hour (UINT8)
                    rt.minute = buffer.get() & 0xFF;          // minute (UINT8)
                    rt.second = buffer.get() & 0xFF;          // second (UINT8)
                    rt.millisecond = buffer.getShort() & 0xFFFF;   // millisecond (UINT16)
                } else {
                    buffer.position(buffer.position() + 9);  // 跳过9字节
                }
                break;

            case UINT16_2:
            case UINT16_4:
            case UINT16_8:
                int uint16ArraySize = mapping.serverType.size / 2;
                if (field.getType() == int[].class) {
                    int[] arr = (int[]) field.get(targetObj);
                    for (int i = 0; i < uint16ArraySize && i < arr.length; i++) {
                        arr[i] = buffer.getShort() & 0xFFFF;
                    }
                } else {
                    buffer.position(buffer.position() + mapping.serverType.size);
                }
                break;

            case INT32_4:
            case INT32_7:
                if (field.getType() == ROBOT_TIME.class) {
                    // 特殊处理：服务器按INT32_7发送，但实际结构是时间字段
                    ROBOT_TIME rt = (ROBOT_TIME) field.get(targetObj);
                    rt.year = buffer.getInt();       // year (服务器按INT32发送)
                    rt.month = buffer.getInt();      // month
                    rt.day = buffer.getInt();        // day
                    rt.hour = buffer.getInt();       // hour
                    rt.minute = buffer.getInt();     // minute
                    rt.second = buffer.getInt();     // second
                    rt.millisecond = buffer.getInt(); // millisecond
                } else if (field.getType() == int[].class) {
                    int int32ArraySize = mapping.serverType.size / 4;
                    int[] arr = (int[]) field.get(targetObj);
                    for (int i = 0; i < int32ArraySize && i < arr.length; i++) {
                        arr[i] = buffer.getInt();
                    }
                } else {
                    buffer.position(buffer.position() + mapping.serverType.size);
                }
                break;

            case DOUBLE_2:
                if (field.getType() == double[].class) {
                    double[] arr = (double[]) field.get(targetObj);
                    for (int i = 0; i < 2 && i < arr.length; i++) {
                        arr[i] = buffer.getDouble();
                    }
                } else if (field.getType() == int[].class) {
                    // 服务器发DOUBLE，结构体存int（需要类型转换）
                    int[] arr = (int[]) field.get(targetObj);
                    for (int i = 0; i < 2 && i < arr.length; i++) {
                        arr[i] = (int) buffer.getDouble();
                    }
                } else {
                    buffer.position(buffer.position() + 16);
                }
                break;

            case DOUBLE_2_TO_UINT16_2:
                // 服务器发2个DOUBLE(16字节)，转换为2个UINT16存到int[2]
                if (field.getType() == int[].class) {
                    int[] arr = (int[]) field.get(targetObj);
                    double d0 = buffer.getDouble();  // 读取第1个double
                    double d1 = buffer.getDouble();  // 读取第2个double
                    // 转换逻辑：假设范围0.0~10.0对应0~65535，这里简单截断
                    arr[0] = (int) Math.max(0, Math.min(65535, d0));
                    arr[1] = (int) Math.max(0, Math.min(65535, d1));
                } else {
                    buffer.position(buffer.position() + 16);  // 跳过16字节
                }
                break;

            case DOUBLE_3:
                if (field.getType() == double[].class) {
                    double[] arr = (double[]) field.get(targetObj);
                    for (int i = 0; i < 3 && i < arr.length; i++) {
                        arr[i] = buffer.getDouble();
                    }
                } else {
                    buffer.position(buffer.position() + 24);
                }
                break;

            case DOUBLE_6:
                if (field.getType() == double[].class) {
                    double[] arr = (double[]) field.get(targetObj);
                    for (int i = 0; i < 6 && i < arr.length; i++) {
                        arr[i] = buffer.getDouble();
                    }
                } else {
                    buffer.position(buffer.position() + 48);
                }
                break;
        }

    }

    /**
     * 设置字段值，处理类型转换
     */
    private static void setValue(Object targetObj, Field field, Object value, DataType targetType) throws Exception {
        Class<?> fieldType = field.getType();

        if (fieldType == int.class || fieldType == Integer.class) {
            if (value instanceof Number) {
                field.setInt(targetObj, ((Number) value).intValue());
            }
        } else if (fieldType == double.class || fieldType == Double.class) {
            if (value instanceof Number) {
                field.setDouble(targetObj, ((Number) value).doubleValue());
            }
        } else if (fieldType == float.class || fieldType == Float.class) {
            if (value instanceof Number) {
                field.setFloat(targetObj, ((Number) value).floatValue());
            }
        } else if (fieldType == long.class || fieldType == Long.class) {
            if (value instanceof Number) {
                field.setLong(targetObj, ((Number) value).longValue());
            }
        } else if (fieldType == byte.class || fieldType == Byte.class) {
            if (value instanceof Number) {
                field.setByte(targetObj, ((Number) value).byteValue());
            }
        } else if (fieldType == short.class || fieldType == Short.class) {
            if (value instanceof Number) {
                field.setShort(targetObj, ((Number) value).shortValue());
            }
        }
    }

}
