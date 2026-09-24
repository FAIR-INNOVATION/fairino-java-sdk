package fairino;
public class RobotError
{
    public final static int ERR_UPLOAD_FILE_NOT_FOUND = -7;   /* 上传文件不存在 */
    public final static int ERR_SAVE_FILE_PATH_NOT_FOUND = -6;/* 保存文件路径不存在 */
    public final static int ERR_NOT_FOUND_LUA_FILE = -5;      /* lua文件不存在 */
    public final static int ERR_RPC_ERROR = -4;
    public final static int ERR_SOCKET_COM_FAILED = -2;
    public final static int ERR_OTHER = -1;
    public final static int ERR_SUCCESS = 0;
    public final static int ERR_PARAM_NUM = 3;
    public final static int ERR_PARAM_VALUE = 4;
    public final static int ERR_TPD_FILE_OPEN_FAILED = 8;
    public final static int ERR_EXECUTION_FAILED = 14;
    public final static int ERR_PROGRAM_IS_RUNNING = 18;
    public final static int ERR_COMPUTE_FAILED = 25;
    public final static int ERR_INVERSE_KINEMATICS_COMPUTE_FAILED = 28;
    public final static int ERR_SERVOJ_JOINT_OVERRUN = 29;
    public final static int ERR_NON_RESSETTABLE_FAULT = 30;
    public final static int ERR_EXTAXIS_CONFIG_FAILURE = 33;//外部轴未处于零位，导程、分辨率设置失败
    public final static int ERR_WORKPIECE_NUM = 34;
    public final static int ERR_FILENAME_TOO_LONG = 36;
    public final static int ERR_TOOL_NUM = 37;
    public final static int ERR_STRANGE_POSE = 38;
    public final static int ERR_EXTAXIS_NOT_HOMING = 41;//外部轴未回零
    public final static int ERR_EXTAXIS_NOT_ACTIVING = 45;//外部轴未激活
    public final static int ERR_EXTAXIS_NOT_CALIB = 46;//同步功能需要标定外部轴
    public final static int ERR_EXTAXIS_SERVO_CONFIG_FAIL = 47;//外部驱动器信息配置失败
    public final static int ERR_EXTAXIS_SERVO_CONFIG_OVER = 48;//外部轴驱动器信息获取超时
    public final static int ERR_EXTAXIS_NOT_STEP_OPERATE = 52;//同步功能不能使用单步操作
    //60 里扭矩传感器参考坐标系未切换至工具
    public final static int ERR_NOT_ADD_CMD_QUEUE = 64;
    public final static int ERR_CIRCLE_SPIRAL_MIDDLE_POINT1 = 66;
    public final static int ERR_CIRCLE_SPIRAL_MIDDLE_POINT2 = 67;
    public final static int ERR_CIRCLE_SPIRAL_MIDDLE_POINT3 = 68;
    public final static int ERR_MOVEC_MIDDLE_POINT = 69;
    public final static int ERR_MOVEC_TARGET_POINT = 70;
    public final static int ERR_GRIPPER_MOTION = 73;
    public final static int ERR_LINE_POINT = 74;
    public final static int ERR_CHANNEL_FAULT = 75;
    public final static int ERR_WAIT_TIMEOUT = 76;
    public final static int ERR_TPD_CMD_POINT = 82;
    public final static int ERR_TPD_CMD_TOOL = 83;
    public final static int ERR_SPLINE_POINT = 94;
    public final static int ERR_SPIRAL_START_POINT = 108;
    public final static int ERR_TARGET_POSE_CANNOT_REACHED = 112;
    public final static int ERR_POINTTABLE_NOTFOUND = 130;
    public final static int ERR_TEACHINGPOINTNOTFOUND = 143;  //示教点位信息不存在
    public final static int ERR_LUAFILENITFOUND = 144;        //LUA文件不存在

    public final static int ERR_SOCKET_RECV_FAILED = -16;  /* socket接收失败 */
    public final static int ERR_SOCKET_SEND_FAILED = -15;    /* socket发送失败 */
    public final static int ERR_FILE_OPEN_FAILED = -14;    /* 文件打开失败 */
    public final static int ERR_FILE_TOO_LARGE = -13;    /* 文件大小超限 */
    public final static int ERR_UPLOAD_FILE_ERROR = -12;    /* 上传文件异常 */
    public final static int ERR_FILE_NAME = -11;    /* 文件名称异常 */
    public final static int ERR_DOWN_LOAD_FILE_WRITE_FAILED = -10;    /* 下载文件写入失败 */
    public final static int ERR_DOWN_LOAD_FILE_CHECK_FAILED = -9;     /* 文件下载校验失败 */
    public final static int ERR_DOWN_LOAD_FILE_FAILED = -8;     /* 文件下载失败 */
    public final static int ERR_CNDE_STATES_START_FAILED  = -21;     /* 机器人CNDE状态启动失败 */
    public final static int ERR_CMD_TLS_ENABLE_STATE  = -22;     /* 机器人SDK与服务端的指令协议加密开启状态不一致*/
    public final static int ERR_CMD_TLS_CERT_NOT_FOUND = -23; /* 已开启mTLS但证书文件缺失(client.crt/client.key/ca.crt) */
}
