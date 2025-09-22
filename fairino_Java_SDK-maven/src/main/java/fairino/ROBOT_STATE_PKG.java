package fairino;

public class ROBOT_STATE_PKG
{
    public short frame_head = 0;            //帧头 0x5A5A
    public byte frame_cnt = 0;              //帧计数
    public short data_len = 0;              //数据长度  5
    public int program_state = 0;          //程序运行状态，1-停止；2-运行；3-暂停
    public int robot_state = 0;            //机器人运动状态，1-停止；2-运行；3-暂停；4-拖动  7
    public int main_code = 0;               //主故障码
    public int sub_code = 0;                //子故障码
    public int robot_mode = 0;             //机器人模式，0-自动模式；1-手动模式 16
    public double[] jt_cur_pos  =new double[6];                  //关节当前位置
    public double[] tl_cur_pos = new double[6];                  //工具当前位姿
    public double[] flange_cur_pos = new double[6];              //末端法兰当前位姿
    public double[] actual_qd = new double[6];                   //机器人当前关节速度
    public double[] actual_qdd = new double[6];                  //机器人当前关节加速度
    public double[] target_TCP_CmpSpeed = new double[2];         //机器人TCP合成指令速度
    public double[] target_TCP_Speed = new double[6];            //机器人TCP指令速度
    public double[] actual_TCP_CmpSpeed = new double[2];         //机器人TCP合成实际速度
    public double[] actual_TCP_Speed = new double[6];            //机器人TCP实际速度
    public double[] jt_cur_tor = new double[6];                             //当前扭矩
    public int tool = 0;                        //工具号
    public int user = 0;                        //工件号
    public int cl_dgt_output_h = 0;            //数字输出15-8
    public int cl_dgt_output_l = 0;            //数字输出7-0
    public int tl_dgt_output_l = 0;            //工具数字输出7-0(仅bit0-bit1有效)
    public int cl_dgt_input_h = 0;             //数字输入15-8
    public int cl_dgt_input_l = 0;             //数字输入7-0
    public int tl_dgt_input_l = 0;             //工具数字输入7-0(仅bit0-bit1有效)
    public short[] cl_analog_input = new short[2];          //控制箱模拟量输入
    public short tl_anglog_input = 0;                       //工具模拟量输入
    public double[] ft_sensor_raw_data = new double[6];     //力/扭矩传感器原始数据
    public double[] ft_sensor_data = new double[6];         //参考坐标系下力/扭矩传感器数据
    public int ft_sensor_active = 0;           //力/扭矩传感器激活状态， 0-复位，1-激活
    public int EmergencyStop = 0;              //急停标志
    public int motion_done = 0;                 //到位信号
    public int gripper_motiondone = 0;         //夹爪运动完成信号
    public int mc_queue_len = 0;                //运动队列长度
    public int collisionState = 0;             //碰撞检测，1-碰撞；0-无碰撞
    public int trajectory_pnum = 0;             //轨迹点编号
    public int safety_stop0_state = 0;  /* 安全停止信号SI0 */
    public int safety_stop1_state = 0;  /* 安全停止信号SI1 */
    public int gripper_fault_id = 0;    /* 错误夹爪号 */               // + 19 = 567
    public short gripper_fault = 0;      /* 夹爪故障 */
    public short gripper_active = 0;     /* 夹爪激活状态 */
    public int gripper_position = 0;    /* 夹爪位置 */
    public int gripper_speed = 0;       /* 夹爪速度 */
    public int gripper_current = 0;     /* 夹爪电流 */
    public int gripper_tmp = 0;          /* 夹爪温度 */
    public int gripper_voltage = 0;      /* 夹爪电压 */
    public ROBOT_AUX_STATE auxState = new ROBOT_AUX_STATE(); /* 485扩展轴状态 */
    public EXT_AXIS_STATUS extAxisStatus0 = new EXT_AXIS_STATUS();//扩展轴位置
    public EXT_AXIS_STATUS extAxisStatus1 = new EXT_AXIS_STATUS();
    public EXT_AXIS_STATUS extAxisStatus2 = new EXT_AXIS_STATUS();
    public EXT_AXIS_STATUS extAxisStatus3 = new EXT_AXIS_STATUS();
    public short[] extDIState = new short[8];        //扩展DI输入
    public short[] extDOState = new short[8];        //扩展DO输出
    public short[] extAIState = new short[4];        //扩展AI输入
    public short[] extAOState = new short[4];        //扩展AO输出
    public int rbtEnableState = 0;       //机器人使能状态--robot enable s
    public double[] jointDriverTorque  =new double[6];       //关节驱动器当前扭矩
    public double[] jointDriverTemperature = new double[6];  //关节驱动器当前温度
    public ROBOT_TIME robotTime = new ROBOT_TIME();
    public int softwareUpgradeState = 0;   //机器人软件升级状态 0-空闲中或上传升级包中；1~100：升级完成百分比；-1:升级软件失败；-2：校验失败；-3：版本校验失败；-4：解压失败；-5：用户配置升级失败；-6：外设配置升级失败；-7：扩展轴配置升级失败；-8：机器人配置升级失败；-9：DH参数配置升级失败
    public int endLuaErrCode;              //末端LUA运行状态

    public int[] cl_analog_output=new int[2];  //控制箱模拟量输出
    public int tl_analog_output;     //工具模拟量输出
    public float gripperRotNum;           //旋转夹爪当前旋转圈数
    public int gripperRotSpeed;       //旋转夹爪当前旋转速度百分比
    public int gripperRotTorque;	   //旋转夹爪当前旋转力矩百分比

    public  WELDING_BREAKOFF_STATE weldingBreakOffstate=new WELDING_BREAKOFF_STATE();//焊接中断状态

    public double[]  jt_tgt_tor=new double[6];    //关节指令力矩
    public int smartToolState;         //SmartTool手柄按钮状态

    public float wideVoltageCtrlBoxTemp;        //宽电压控制箱温度
    public int wideVoltageCtrlBoxFanCurrent;   //宽电压控制箱风扇转速(mA)

    public double[] toolCoord=new double[6];           //工具坐标系
    public double[] wobjCoord=new double[6];		   //工件坐标系
    public double[] extoolCoord=new double[6];		   //外部工具坐标系
    public double[] exAxisCoord=new double[6];		   //扩展轴坐标系
    public double load;                   //负载质量
    public double[] loadCog=new double[3];             //负载质心

    public short check_sum = 0;          /* 和校验 */

    public ROBOT_STATE_PKG()
    {

    }
}
