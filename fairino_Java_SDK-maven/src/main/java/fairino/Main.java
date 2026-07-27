package fairino;

import com.sun.scenario.effect.Offset;
import sun.security.krb5.internal.crypto.Des;

import java.util.Arrays;
import java.util.List;
import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Robot robot = new Robot();
        robot.SetReconnectParam(true, 1000, 50);//设置重连次数、间隔
        robot.LoggerInit(FrLogType.DIRECT, FrLogLevel.INFO, "D://log", 10, 10);
//
//        int rtn = robot.RPC("192.168.58.2");
//        if (rtn == 0) {
//            System.out.println("rpc连接 success");
//        } else {
//            System.out.println("rpc连接 fail");
//            return;
//        }
//        robot.Sleep(1000);


//        TestSetWeldParam(robot);//焊接参数配置
//        TestExtDIConfig(robot);//设置扩展IO焊接信号
//        TestCoord(robot);

//        TestStationaryTrack(robot);
// TestWorkPieceTrsf(robot);
//TestSetJointVelReducePara(robot);

//        TestFiveDexterousHands(robot);

//        TestWeaveSpeedAndOffset(robot);
//        TestWelding(robot);//机器人焊接
//        TestSegWeld(robot);//机器人段焊
//        TestWeave(robot);// 机器人摆动渐变焊接
//        TestArcWeldTrace(robot);//多层多道焊电弧跟踪
//        WeldTraceControlWithCtrlBoxAI(robot);//电弧跟踪
//        TestWireSearch(robot);//机器人焊丝寻位
//        WeldparamChange(robot);//机器人焊接电流电压渐变
//        TestCustomWeaveSetPara(robot);//自定义摆动参数
//        testLsaerWeld(robot);//激光焊接



//        TestGripper(robot);//夹爪
//        TestRotGripperState(robot);//获取旋转夹爪状态
//        TestAxleSensor(robot);//末端传感器
//        TestExDevProtocol(robot);//设置机器人外设协议

//        testLaserConfig(robot);


//eee-----
//        TestWeaveSpeedAndOffset(robot);
//        TestSplineWeave(robot);
        //  testSDKCompatibility(robot);
//        TestRobotCtrl(robot);//机器人基础控制
//        TestGetVersions(robot);//获取机器人软固件版本
//        TestJOG(robot);//机器人点动控制
//        TestMove(robot);//机器人基本运动指令
//        TestSpiral(robot);//螺旋线运动
//        TestServoJ(robot);//基于UDP通信的ServoJ、ServoMoveStart、ServoMoveEnd
//         ServoJTWithSafety(robot);//基于UDP通信的ServoJT、ServoJTStart、ServoJTEnd
//        TestServoCart(robot);//笛卡尔空间伺服模式运动
//        TestSpline(robot);//关节空间样条运动
//        TestNewSpline(robot);//新样条指令点
//        TestPause(robot);//运动暂停、恢复、停止
//        TestOffset(robot);//点位偏移
//        TestMoveAO(robot);//AO飞拍
//        TestFIR(robot);//FIR滤波
//        TestAccSmooth(robot);//加速度平滑
//        TestAngularSpeed(robot);//机器人指定姿态速度
//        TestAngularSpeed(robot);//机器人奇异位姿保护
//        TestIntersectLineMove(robot);//机器人相贯线运动-XXXX
//        test_RecordandReplay(robot);//原地空运动-XXXXX
//        TestOriginPointWeave(robot);//定点摆动
//        ServoJVtest(robot);//关节空间速度伺服模式运动
//        ServoMITtest(robot);//机器人关节MIT控制
//        TestAODO(robot);//设置数字量、模拟量输出
//        TestGetDIAI(robot);//获取机器人DI、DO状态
//        TestWaitDIAI(robot);//等待控制箱数字、模拟输入信号
//        TestDOReset(robot);//设置LUA程序停止/暂停后输出复位
//        TestIOConfig(robot);//机器人IO配置
//        TestTCPCompute(robot);//机器人工具坐标系操作
//        TestExtCoord(robot);//机器人外部工具坐标系操作
//        TestWobjCoord(robot);//机器人工件坐标系操作
//        TestLoadInstall(robot);//机器人常用设置
//        TestFriction(robot);//机器人设置关节摩擦力补偿
//        TestGetError(robot);//机器人故障状态获取及清除错误
//        TestWideVoltageCtrlBoxtemp(robot);//LINUX-宽电压控制箱温度和风扇电流状态获取
//        TestFocus(robot);//焦点跟随
//        TestSensitivityCalib(robot);//关节扭矩传感器灵敏度自动标定
//        TestSlavePortErr(robot);//获取从站端口错误帧
//        TestVelFeedForwardRatio(robot);//机器人速度前馈系
//        TestSetTrajectoryJSpeed(robot);//测试示例1







// ----==end




//        TestSingularAvoid(robot);



//        testCndeServoJ(robot);

//        GripperDropAlarmTest(robot);
//        ProgramLoadTest(robot);
        // LoadDefaultProgConfigTest(robot);
        // TestLuaOp(robot);
        // TestSetTrajectoryJSpeed(robot);
//         TestTraj(robot);
        // TestLoadTrajLA(robot);
        // TestDOReset(robot);
    // TestDexterousHands(robot);
//        TestUDPAxis_CPP(robot);
        // testLsaerWeld(robot);
    //    TestSetTrajectoryJSpeed(robot);

        // 测试新增CNDE状态配置
       TestCNDEStarte(robot);

        // 测试读取默认的状态值
//        TestReadBasicStates(robot);

        // 测试参数异常
//        TestCNDEParamError(robot);
        // 测试默认值打印
//          TestDefault(robot);
        // 验证SDK配置和获取机器人关节位置、笛卡尔位置、关节力矩等机器人本体相关状态数据正常生效
        // TestExtendedStates(robot);
        // TestExtendedStates2(robot);
        
//        TestAlarmStates(robot);
//        TestSensorGripperStates(robot);
//        TestExtAxisStates(robot);
        // 稳定性测试
        // MoveRectangleMove(robot);

        // TestAxleGenComData(robot);

        
        // 测试实时状态配置接口
//         TestRealtimeStateConfig(robot);

//        ServoJVtest(robot);

        // TestCtrlOpenLuaOperate(robot);
        // TestUDPAxis(robot);
        // TestIOConfig(robot);
//         TestOriginPointWeave(robot);
//        TestRobotUDP(robot);
//         TestServoJ(robot);
        // ServoJTWithSafety(robot);
        // testAxleGenCom(robot);
//        TestRobotStopOnComDisc(robot);
//        while(true){
//            System.out.println(robot.GetRobotRealTimeState().robot_state==4);
//            System.out.println("当前状态:"+robot.GetRobotRealTimeState().robot_state);
//        }
//*********************************日志功能 begin *******************************
//        String[] ip={""};
//        robot.GetControllerIP(ip);
//        System.out.println("ip: "+ip[0]);
//
//
//        for (int i=0;i<50;i++){
//            robot.GetControllerIP(ip);
//            robot.Sleep(500);
//        }
//        robot.Sleep(500);
//        String version = "";
//        version = robot.GetSDKVersion();
//*********************************日志功能 end *******************************

//        int rec=robot.StartJOG(0, 1, 0, 20, 100, 90);//关节点动
//        System.out.println("StartJOG: "+rec);
        //DescPose coord = new DescPose(0, 0 ,0, 1, 0, 0);
        //robot.FT_SetRCS(0, coord);//16、UDP扩展轴圆弧运动
        //DOReset(robot);//3、D0、A0测试
        //Standard(robot);//1、上下使能、手动自动状态切换  获取版本号
//        Moves(robot);//2、机器人运动测试，逆向运动学计算后做直线运动
        //IOTest(robot);//3、设置控制箱DO、AO停止/暂停后输出是否复位
//        CommonSets(robot);//4、机器人常用设置、标定
        //RobotSafety(robot);//5、机器人安全设置
//        RobotState(robot);//6、机器人状态查询
//        TPD(robot);//7、轨迹复现
//        TrajectoryJ(robot);//7、轨迹文件
//        Program(robot);//8、下载Lua文件
        //GripperTest(robot);//9、夹爪。11、传送带
        //ForceSensor(robot);//10、恒力控制
        //PointTableTest(robot);//12、点位表
//        Welding(robot);//14、焊接
        //SegmentWeld(robot);//14、段焊
        //TestAuxServo(robot);//15、485扩展轴
        //UDPAxis(robot);//16、UDP扩展轴
        //UDPAxisSyncMove(robot);//16、UDP扩展轴圆弧、直线运动
        //EndLuaUpload(robot);//17、Lua开发协议功能
//        TestEndLuaGripper(robot);//17、第二步 Lua夹爪
        //TestEndLuaForce(robot);//17
        //RobotStateTest(robot);//18、状态反馈

        //TestUDPWireSearch(robot);//焊丝寻位
        //TestTractorMove(robot);//测移动物体-小车的启停
        //TestWeldmechineMode(robot);//焊机控制模式切换
//        TestWeave1(robot);
        //Stable(robot);
//        TestSingularAvoidSLin(robot);//肩部Line，测试奇异点保护
//        TestSingularAvoidSArc(robot);//肩部ARC，测试奇异点保护
        //TestSingularAvoidWLin(robot);//腕部Line，测试奇异点保护
        //TestSingularAvoidWArc(robot);//腕部ARC，测试奇异点保护
        //TestSingularAvoidEArc(robot);//肘部ARC，测试奇异点保护

        //上传轨迹J文件等功能测试-2024.12.16
////        SetAO(robot, 50);
//        MoveRotGripper(robot, 30, 2.2);
//
//        while (true){
//            ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
////            System.out.println("the robot AO0 "+pkg.cl_analog_output[0]/40.96+", AO1 "+pkg.cl_analog_output[1]/40.96+", tool AO0:  "+pkg.tl_analog_output/40.96);
//            System.out.println("gripper pos "+pkg.gripper_position+"- vel "+pkg.gripper_speed+" - torque "+pkg.gripper_current+" - rotPos "+pkg.gripperRotNum+" - rotvel "+pkg.gripperRotSpeed+" - rotTor "+ pkg.gripperRotTorque);
//            robot.Sleep(200);
//        }

        //奇异点测试
//        TestSingularAvoidSArc(robot);
//        TestSingularAvoidEArc(robot);
//        TestSingularAvoidSLin(robot);
//        TestSingularAvoidWArc(robot);
//        TestSingularAvoidWLin(robot);
        //end 奇异点测试

//        UploadTrajectoryJ(robot);//轨迹J文件删除、上传、运行
//        UploadTrajectoryB(robot);

        //稳定性测试
//        while (true)
//        {
//            MoveRotGripper(robot, 30, 0);
//            MoveRotGripper(robot, 90, 0);
//            UploadTrajectoryJ(robot);
//            MoveRotGripper(robot, 90, 2);
//            robot.Sleep(5000);
//            MoveRotGripper(robot, 30, 0);
//            robot.Sleep(1000);
//            MoveRotGripper(robot, 90, 0);
//            UploadTrajectoryB(robot);//轨迹J文件上传与删除
//            //MoveRotGripper(robot, 90, 0);
//            robot.Sleep(5000);
//            MoveRotGripper(robot, 30, 0);
//            ROBOT_STATE_PKG pkg=robot.GetRobotRealTimeState();
//            System.out.println("the robot AO0:"+ pkg.cl_analog_output[0]+", AO1: "+pkg.cl_analog_output[1]+"tool AO0:"+pkg.tl_analog_output);
//            System.out.println("gripper pos"+pkg.gripper_position+ "- vel "+pkg.gripper_speed+"- torque "+pkg.gripper_current+" - rotPos "+pkg.gripperRotNum+" - rotvel "+pkg.gripperRotSpeed+" - rotTor"+pkg.gripperRotTorque);
//        }
        //end 稳定性测试
        //end-上传轨迹J文件等功能测试-2024.12.16


        //测试FIR滤波
//         FIRPTP(robot, false);//PTP
//         FIRPTP(robot, true);
//
//        FIRLinL(robot, false);//Lin匀速
//        FIRLinL(robot, true);

//        FIRLin(robot,false);//Lin无匀速
//        FIRLin(robot,true);


//        FIRArc(robot, false);//arc匀速
//        FIRArc(robot, true);
        //end 测试FIR滤波


//        TestReWeld(robot);
//        TestTCP(robot);
//        TestTCP6(robot);
//        TestWObj(robot);
//        ExtAxisLaserTracking(robot);

//        TestArcWeldTraceChange(robot);//电弧跟踪控制
//        TestWeaveChange(robot);//摆动渐变测试

//        TestTrajectoryLA(robot);//测试轨迹预处理(轨迹前瞻)
//        CustomCollisionTest(robot);//自定义碰撞检测阈值功能开始

//        reconnect_test(robot);//测试重连
//        testSmooth(robot);

//        TestInverseKen(robot);
//        ConveyorTest(robot);//测试传送带
//        testdown(robot);//下载文件
//        WeaveAngle(robot);//测试摆动
//        WeldTraceControlWithCtrlBoxAI(robot);//焊接

//        int[] state = {0};
//        int tmit = 0;
//        while (tmit < 2000)
//        {
//            tmit ++;
//            robot.GetSmarttoolBtnState(state);
//
//            String binaryString = String.format("%32s", Integer.toBinaryString(state[0])).replace(' ', '0');
//            System.out.println("GetSmarttoolBtnState:"+binaryString);
//            robot.Sleep(100);
//        }

//        TestWeaveChange1(robot);//摆动渐变
//        WeldparamChange(robot);

        //测试
//        ROBOT_STATE_PKG pkg=new ROBOT_STATE_PKG();//状态结构体
//        String err1=new String();
//        while (true)
//        {
//            auto start = duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();
//            rtn = robot.LuaUpload("D://zUP/27.lua");
//            auto end = duration_cast<milliseconds>(system_clock::now().time_since_epoch()).count();
//            cout << "file upload time is : " << (end - start) <<  "    rtn is " << rtn <<endl;

//            long start = System.currentTimeMillis();
//
//
//            rtn = robot.LuaUpload("D://zUP/27.lua",err1);
//            // 获取当前时间（毫秒）
//            long end = System.currentTimeMillis();
//
//            // 输出上传时间和返回值
//            System.out.println("file upload time is : " + (end - start) + "    rtn is " + rtn);
//
//            robot.Sleep(100);
//        }


//        while (true)
//        {
//            DescPose p1Desc=new DescPose(327.604, -104.449, 469.385, -170.284, 2.548, -75.248);
//            JointPos p1Joint=new JointPos(-1.953, -64.856, -112.831, -83.705, 95.191, -16.097);
//
//            DescPose p2Desc=new DescPose(305.959, 260.947, 408.152, 177.966, 0.257, -18.624);
//            JointPos p2Joint=new JointPos(55.276, -76.083, -111.563, -84.379, 89.683, -16.099);
//
//            ExaxisPos exaxisPos=new ExaxisPos(0.0, 0.0, 0.0, 0.0);
//            DescPose offdese=new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
//            rtn = robot.MoveL(p1Joint, p1Desc, 0, 0, 50, 100, 100, -1, exaxisPos, 0, 0, offdese,0,10);
//            if (rtn != 0)
//            {
//                System.out.println("moveL rtn is: "+ rtn);
//                break;
//            }
//            rtn = robot.MoveL(p2Joint, p2Desc, 0, 0, 50, 100, 100, -1, exaxisPos, 0, 0, offdese,0,10);
//            if (rtn != 0)
//            {
//                System.out.println("moveL rtn is: "+ rtn);
//                break;
//            }
//        }
//
//        int flag=0;
//        while (true)
//        {
//            if(flag<50){
//                flag++;
//            }
//            if(flag==50){
//                robot.ResetAllError();//清除错误
//                robot.Sleep(500);
//                flag=100;
//            }
//
//            pkg=robot.GetRobotRealTimeState();
//            System.out.println("main code is "+pkg.main_code+",  sub code is:  " +pkg.sub_code );
//
//            robot.Sleep(100);
//        }
//        TestBlend(robot);
//        TestCircle(robot);//测试圆新增参数
//        TestGetVersions(robot);
        //TestJOG(robot);
//        TestMove(robot);
//        TestSpiral(robot);//笛卡尔空间螺旋线运动
//        TestServoJT(robot);
//        TestServoCart(robot);
//        TestSpline(robot);//关节空间样条运动
//        TestNewSpline(robot);//新样条指令点
//        TestPause(robot);
//        TestOffset(robot);
//        TestMoveAO(robot);
//        TestAODO(robot);
//        TestGetDIAI(robot);
//        TestWaitDIAI(robot);
//        TestDOReset(robot);
//        TestTCPCompute(robot);
//        TestWobjCoord(robot);
//        TestExtCoord(robot);
//        TestLoadInstall(robot);
//        TestFriction(robot);
//        TestGetError(robot);
//        TestCollision(robot);
//        TestLimit(robot);
//        TestCollisionMethod(robot);
//        TestPowerLimit(robot);
//        TestSetVelReducePara(robot);
//        TestGetStatus(robot);
//        TestInverseKin(robot);
//        TestGetTeachPoint(robot);
//        TestTPD(robot);
 //TestSetTrajectoryJSpeed(robot);
//        TestTraj(robot);
//        TestLoadTrajLA(robot);
//        TestLuaOp(robot);
//        TestLUAUpDownLoad(robot);
//        TestGripper(robot);//夹爪
//        TestRotGripperState(robot);
//        Test485Auxservo(robot);

//        TestFTInit(robot);//力控
//        TestFTLoadCompute(robot);
//        TestFTGuard(robot);
//        TestFTControlWithAdjustCoeff(robot);
//        TestFTControl(robot);
//        TestCompliance(robot);//柔顺控制
//        TestFTSearch(robot);
//        TestSurface(robot);
//        TestIdentify(robot);
//        TestEndForceDragCtrl(robot);//力传感器辅助拖动
//        TestForceAndJointImpedance(robot);//六维力
//        TestImpedanceControl_old(robot);
//        TestUDPAxis(robot);//udp

//        TestFIR(robot);
//        TestAccSmooth(robot);
//        TestAngularSpeed(robot);
//        TestSingularAvoid(robot);

//        TestUDPAxis(robot);//UDP
//        TestUDPAxisCalib(robot);
//        TestUDPAxisMove(robot);
//        TestUDPAxisSync(robot);
//        TestAuxDOAO(robot);
//        TestT
//        ractor(robot);//焊接小车操作
//        TestIdentify(robot);

//        TestSetWeldParam(robot);//焊接参数配置
//        TestWelding(robot);//机器人焊接
//        TestSegWeld(robot);
//        TestWeave(robot);

//        TestExtDIConfig(robot);
//        TestArcWeldTrace(robot);
//        TestWireSearch(robot);
//        TestSSHMd5(robot);
//                TestRealtimePeriod(robot);

//        TestPointTable(robot);
//        TestDownLoadRobotData(robot);
//        TestRobotUDP(robot);
//        TestFIR(robot);
//        TestAccSmooth(robot);
//        TestAngularSpeed(robot);

//        TestSingularAvoid(robot);
//        TestAxleSensor(robot);
//        TestExDevProtocol(robot);
//        TestAxleLua(robot);
//        TestUpgrade(robot);
//                TestConveyor(robot);//传送带
//        TestBlend1(robot);
//                TestFRT(robot);
//        TestFocus(robot);
//        TestFirmWareUpgrade2(robot);
//        TestFirmWareUpgrade3(robot);
//        TestFirmWareUpgrade1(robot);
//        TestExtAxisMoveBlend(robot);
//        TestInver(robot);
//        TestMove100(robot);
//        test_LaserRecordPoint(robot);
//                TestExtAxisSyncMove(robot);
//        TestMoveJ(robot);
//        TestMoveC(robot);

//        TestCircle1(robot);

//        testSetSuckerCtrl(robot);//控制吸盘
//        testGetSuckerState(robot);//监控吸盘
//        testWaitSuckerState(robot);//吸盘超时
//        testSucker(robot);

//        testGetFieldBusConfig(robot);
//        testFieldBusSlaveWriteDOAO(robot);
//        testFieldBusSlaveReadDIAI_waitDIAI(robot);
//        testFieldBusBoard(robot);
//        testSuckerMove(robot);
//        TestMovePhy(robot);

//        TestDragSwitchDetect(robot);

//                testLaserConfig(robot);
//        testGetLaserPoint(robot);
//        testMoveToLaserRecordStart(robot);
//        testMoveToLaserRecordEnd(robot);
//                testLasertrack_xyz(robot);
//        testLasertrack_point(robot);
//        testLaserRecordAndReplay(robot);
//        testLasertrack(robot);
//        testLasertrackandExitAxis(robot);
//        TestImpedanceControl(robot);
//        TestCustomWeaveSetPara(robot);

//        testLaserRecordAndReplayMoveC(robot);
//        TestFTControlWithAdjustCoeff(robot);
//        testLasertrackMoveC(robot);
//        TestSensitivityCalib(robot);
//        TestServoJ_1015(robot);
//                TestSlavePortErr(robot);
//        TestSpiral(robot);
//        TestFTControlWithDamping(robot);

//        TestIntersectLineMove(robot);
//        test_RecordandReplay(robot);
//                TestRotInsert(robot);
//        TestInverseKinExaxis(robot);
//        TestServoCart1(robot);
//        TestDOReset(robot);
//        TestTPD2(robot);
//        TestUserLed(robot);
        robot.CloseRPC();//关闭连接
//
////        while (true)
////        {
////            ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
//            System.out.println(pkg.jt_cur_pos[0]);
//            robot.Sleep(500);
//            System.out.println("robot Enable stare is " + pkg.rbtEnableState);
//        }
//        JointPos pos = new JointPos();
//        robot.GetActualJointPosDegree(1, pos);
        //System.out.println("J1: " + Double.toString(pos.J1) + "   J2: " + Double.toString(pos.J2) +"    J3: " + Double.toString(pos.J3) +"J4: "  + Double.toString(pos.J4) + "J5: " + Double.toString(pos.J5) +"J6: " + Double.toString(pos.J6));
    }

    public static void TestSplineWeave(Robot robot)
    {
        JointPos j1 = new JointPos(9.000, -66.067, 67.706, -103.217, -90.151, 100.669);
        JointPos j2 = new JointPos(-4.660, -107.973, 103.734, -76.214, -89.999, 90.886);
        JointPos j3 = new JointPos(-36.762, -77.380, 91.364, -127.159, -90.024, 54.833);
        JointPos j4 = new JointPos(-62.875, -89.460, 86.437, -77.030, -90.012, 31.539);
        DescPose desc_pos1 = new DescPose(-654.129, -235.344, 246.543, 6.010, -11.535, -176.787);
        DescPose desc_pos2 = new DescPose(-273.710, -100.871, 280.935, 5.692, 9.522, 179.512);
        DescPose desc_pos3 = new DescPose(-566.093, 311.278, 215.008, -10.453, -17.486, -174.209);
        DescPose desc_pos4 = new DescPose(-246.558, 328.240, 292.173, 13.912, 4.437, -179.067);
        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);
        int tool = 2;
        int user = 0;
        float vel = 100.0f;
        float acc = 100.0f;
        float ovl = 20.0f;
        float oacc = 100.0f;
        float blendT = 0.0f;
        float blendR = 0.0f;
        int flag = 0;
        int search = 0;
        int blendMode = 0;
        int velAccMode = 0;

        robot.WeaveEnd(0);
        robot.SetSpeed(1);

        robot.SetWeaveBackCenterConfig(1);
        int[] weaveBackConfig = new int[1];
        robot.GetWeaveBackCenterConfig(weaveBackConfig);
        System.out.printf("GetWeaveBackCenterConfig:  %d \n", weaveBackConfig[0]);

        robot.MoveJ(j1, desc_pos1, tool, user, vel, acc, 100.0f, epos, blendT, flag, offset_pos);

        robot.WeaveStart(0);
        robot.NewSplineStart(0, 6000);
        robot.NewSplinePoint(j1, desc_pos1, tool, user, vel, acc, ovl, -1, 0);
        robot.NewSplinePoint(j2, desc_pos2, tool, user, vel, acc, ovl, -1, 0);
        robot.NewSplinePoint(j3, desc_pos3, tool, user, vel, acc, ovl, -1, 0);
        robot.NewSplinePoint(j4, desc_pos4, tool, user, vel, acc, ovl, -1, 1);
        robot.NewSplineEnd();
        robot.WeaveEnd(0);
    }


    public static void testSDKCompatibility(Robot robot)
    {
//        robot.Mode(0);
        JointPos startjointPos=new JointPos(-3.656,-85.823,122.548,-28.773,31.438,-0.445);
        DescPose startdescPose=new DescPose(-396.260,-162.385,231.439,94.163,-6.335,-35.308);
        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
        robot.MoveL(startjointPos, startdescPose, 0, 0, 100, 100, 100, -1, 0,exaxisPos, 0, 0, offdese, 100,0,1, 1);

//        int rtn = robot.SetTrajectoryJSpeed(50.0,1);
//        System.out.println("SetTrajectoryJSpeed is:"+ rtn);

   }



    public static void TestDOReset(Robot robot)
    {
        for (int i = 0; i < 16; i++)
        {
            robot.SetDO(i, 1, 0, 0);
            robot.Sleep(200);
        }

        int resetFlag = 1;
        int resumeReloadFlag = 1;
        int rtn = robot.SetOutputResetCtlBoxDO(resetFlag, resumeReloadFlag);
        robot.SetOutputResetCtlBoxAO(resetFlag, resumeReloadFlag);
        robot.SetOutputResetAxleDO(resetFlag, resumeReloadFlag);
        robot.SetOutputResetAxleAO(resetFlag, resumeReloadFlag);
        robot.SetOutputResetExtDO(resetFlag, resumeReloadFlag);
        robot.SetOutputResetExtAO(resetFlag, resumeReloadFlag);
        robot.SetOutputResetSmartToolDO(resetFlag, resumeReloadFlag);

        robot.ProgramLoad("test.lua");
        robot.ProgramRun();

        robot.Sleep(2000);
        robot.PauseMotion();
        robot.Sleep(2000);
        robot.ResumeMotion();
        robot.Sleep(2000);

        robot.CloseRPC();
    }

    public static void  TestInverseKinExaxis(Robot robot)
    {
        DescPose desc=new DescPose(99.957, -0.002, 29.994, -176.569, -6.757, -167.462);
        ExaxisPos exaxis=new ExaxisPos(100.0, 0.0, 0.0, 0.0);
        JointPos jointPos =new JointPos();
        DescPose offsetPos =new DescPose();

        ROBOT_STATE_PKG pkg=robot.GetRobotRealTimeState();
        int toolnum = pkg.tool;
        int workPcsNum = pkg.user;
        robot.GetInverseKinExaxis(0, desc, exaxis, toolnum, workPcsNum, jointPos);
        System.out.printf("GetInverseKinExaxis joint is %f, %f, %f, %f, %f, %f\n", jointPos.J1, jointPos.J2, jointPos.J3, jointPos.J4, jointPos.J5, jointPos.J6);

        robot.ExtAxisMove(exaxis, 100, -1);
        robot.MoveJ(jointPos, desc, toolnum, workPcsNum, 100.0, 100.0, 100.0, exaxis, -1, 0, offsetPos);

        robot.CloseRPC();

        robot.Sleep(9999999);
    }

    public static void TestServoCart(Robot robot)
    {
        DescPose desc_pos_dt = new DescPose(83.00800, 50.525000 , 29.246 , 179.629 , -7.138 , -166.975 );
        ExaxisPos exaxis = new ExaxisPos( 100.0, 0.0, 0.0, 0.0 );
        double[] pos_gain = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        int mode = 0;
        double vel = 0.0;
        double acc = 0.0;
        double cmdT = 0.001;
        double filterT = 0.0;
        double gain = 0.0;
        int flag = 0;
        int count = 5000;

        robot.GetActualTCPPose(desc_pos_dt);

        robot.SetSpeed(10);

        while (count>0)
        {
            int rtn = robot.ServoCart(mode, desc_pos_dt, exaxis, pos_gain, acc, vel, cmdT, filterT, gain);
            System.out.printf("ServoCart rtn is %d\n", rtn);
            count -= 1;
            desc_pos_dt.tran.x += 0.01;
            exaxis.axis1 += 0.01;
        }

        robot.CloseRPC();
    }

    public static void TestPhotoelectricSensorTCPCalib(Robot robot)
    {
        DescTran offset = new DescTran(10.0, 10.0, 3.0 );
        DescPose TCP = new DescPose();
        int rtn = robot.PhotoelectricSensorTCPCalibration("FR_CalibrateTheToolTcp-061101.lua", offset, TCP);
        System.out.printf("PhotoelectricSensorTCPCalibration rtn is %d %f %f %f %f %f %f \n", rtn, TCP.tran.x, TCP.tran.y, TCP.tran.z, TCP.rpy.rx, TCP.rpy.ry, TCP.rpy.rz);
        robot.CloseRPC();
        robot.Sleep(9999999);
    }

    public static void TestRotInsert(Robot robot)
    {
        double forceInsertion = 2.0; //力或力矩阈值（0~100），单位N或Nm
        int angleMax = 12; //最大旋转角度，单位°
        int orn = 2; //力的方向，1-fz,2-mz
        double angAccmax = 5; //最大旋转角加速度，单位°/s^2,暂不使用
        int status = 1;  //恒力控制开启标志，0-关，1-开
        int sensor_num = 2; //力传感器编号
        double[] gain = { 0.0001,0.0,0.0,0.0,0.0,0.0 };  //最大阈值
        int adj_sign = 0;  //自适应启停状态，0-关闭，1-开启
        int ILC_sign = 0;  //ILC控制启停状态，0-停止，1-训练，2-实操
        double max_dis = 1000.0;  //最大调整距离
        double max_ang = 5.0;  //最大调整角度
        ForceTorque ft=new ForceTorque(0,0,0,0,0,0);
        int rcs = 0;  //参考坐标系，0-工具坐标系，1-基坐标系
        double angVelRot = 2.0;  //旋转角速度，单位°/s
        int rotorn = 1; //旋转方向，1-顺时针，2-逆时针

        JointPos j1=new JointPos(58.417, -85.578, -100.516, -83.915, 90.000, -31.662);
        JointPos j2=new JointPos(58.417, -87.111, -107.956, -74.942, 90.000, -31.662);
        DescPose desc_p1=new DescPose(328.796, 339.109, 433.617, 179.993, 0.005, 0.079);
        DescPose desc_p2=new DescPose(328.795, 339.109, 373.605, 179.993, 0.005, 0.079);
        ExaxisPos epos =new ExaxisPos( 0.0, 0.0, 0.0, 0.0 );
        DescPose offset_pos=new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        robot.MoveL(j1, desc_p1, 0, 0, 100.0, 180.0, 100.0, -1.0, epos, 0, 1, offset_pos,0,10);
        int[] select3 = { 0,0,1,0,0,0 };  //六个自由度选择[fx,fy,fz,mx,my,mz]，0-不生效，1-生效
        ft.fz = -10.0;
        gain[0] = 0.0001;
        status = 1;
        robot.FT_Control(status, sensor_num, select3, ft, gain, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);
        int rtn = robot.FT_RotInsertion(rcs, angVelRot, forceInsertion, angleMax, orn, angAccmax, rotorn, 1);
        System.out.printf("FT_RotInsertion rtn is %d\n", rtn);
        status = 0;
        robot.FT_Control(status, sensor_num, select3, ft, gain, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);
        robot.MoveL(j2, desc_p2, 0, 0, 100.0, 180.0, 100.0, -1.0, epos, 0, 0, offset_pos,0,10);
        robot.Sleep(1000);
        ROBOT_STATE_PKG pkg=robot.GetRobotRealTimeState();
        System.out.printf("robot errcode %d  %d\n", pkg.main_code, pkg.sub_code);
        robot.CloseRPC();
    }


    public static void test_RecordandReplay(Robot robot)
    {

        int rtn = robot.LaserSensorRecordandReplay(0, 10, 1, 0, 0.1, 1, 0, 10, 100);
        System.out.printf("LaserSensorRecordandReplay rtn is %d\n", rtn);
        rtn = robot.MoveStationary();
        System.out.printf("MoveStationary rtn is %d\n", rtn);
        rtn = robot.LaserSensorRecord1(0, 10);
        System.out.printf("LaserSensorRecordandReplay rtn is %d\n", rtn);

        robot.CloseRPC();

        robot.Sleep(9999999);
    }


    //带有力矩调节系数的恒力控制代码实例
    public static int TestFTControlWithAdjustCoeff(Robot robot)
    {
        int sensor_id = 1;
        int[] select = { 0,0,1,0,0,0 };
        double[] ft_pid = { 0.0008, 0.0, 0.0, 0.0, 0.0, 0.0 };
        int adj_sign = 0;
        int ILC_sign = 0;
        double max_dis = 1000.0;
        double max_ang = 20;

        ForceTorque ft = new ForceTorque(0.0,0,0,0,0,0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);


        JointPos j1=new JointPos(48.515,-64.398,90.405,-115.989,-90.009,62.592);
        JointPos j2=new JointPos(80.610,-80.716,110.032,-116.662,-90.335,65.334);
        DescPose desc_p1=new DescPose(-347.968,-547.463,262.046,180.000,-0.019,75.923);
        DescPose desc_p2=new DescPose(16.807,-519.946,273.415,179.196,-2.552,105.301);
        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);

        double[] M = { 2.0, 2.0 };
        double[] B = { 15.0, 15.0 };
        double[] threshold = {1.0, 1.0};
        double[] adjustCoeff = {1.0, 0.8};
        double polishRadio = 0.0;
        int filter_Sign = 0;
        int posAdapt_sign = 1;
        int isNoBlock;

        ft.fz = -10.0;
        while(true)
        {
            int rtn = robot.FT_Control(1, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, M, B, threshold, adjustCoeff, 0, 0, 1, 0);
            System.out.printf("FT_Control start rtn is %d\n", rtn);

            robot.MoveL(j1, desc_p1, 0, 0, 100.0, 100.0, 100.0, -1.0, 0, epos, 0, 0, offset_pos, 0,0, 0,10);
            robot.MoveL(j2, desc_p2, 0, 0, 100.0, 100.0, 100.0, -1.0, 0, epos, 0, 0, offset_pos, 0,0, 0,10);

            rtn = robot.FT_Control(0, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, M, B, threshold, adjustCoeff, 0, 0, 1, 0);
            System.out.printf("FT_Control end rtn is %d\n", rtn);
        }
    }

    public static void TestIntersectLineMove(Robot robot)
    {
        DescPose[] mainPoint = new DescPose[6];
        DescPose[] piecePoint = new DescPose[6];

        ExaxisPos[] mainExaxisPos = new ExaxisPos[6];
        ExaxisPos[] pieceExaxisPos = new ExaxisPos[6];
        int extAxisFlag = 0;
        ExaxisPos[] exaxisPos = new ExaxisPos[4];
        DescPose offset =new DescPose(0.0, 2.0 ,30.0, -2.0, 0.0, 0.0 );

        mainPoint[0] = new DescPose(-411.572, -516.869, 197.724, -111.821, 31.353, -145.537);
        mainPoint[1] = new DescPose(-430.242, -575.160, 205.215, -107.763, 47.877, -141.814);
        mainPoint[2] = new DescPose(-443.560, -608.068, 180.211, -139.983, 78.547, -170.874);
        mainPoint[3] = new DescPose(-443.718, -608.250, 130.382, -155.397, 80.964, 173.955 );
        mainPoint[4] = new DescPose(-436.198, -582.428, 100.045, 174.899, 72.468, 157.366  );
        mainPoint[5] = new DescPose(-420.815, -527.510, 106.649, 123.128, 67.885, 110.539  );

        mainExaxisPos[0] = new ExaxisPos(0.000, 0.000, 0.000, 0.000 );
        mainExaxisPos[1] = new ExaxisPos(0.000, 0.000, 0.000, 0.000 );
        mainExaxisPos[2] = new ExaxisPos(0.000, 0.000, 0.000, 0.000 );
        mainExaxisPos[3] = new ExaxisPos(0.000, 0.000, 0.000, 0.000 );
        mainExaxisPos[4] = new ExaxisPos(0.000, 0.000, 0.000, 0.000 );
        mainExaxisPos[5] = new ExaxisPos(0.000, 0.000, 0.000, 0.000 );

        piecePoint[0] =new DescPose(-341.600, -568.334, 327.186, 5.404, -3.657, -145.629 );
        piecePoint[1] =new DescPose(-319.224, -619.882, 330.833, 2.439, -3.294, -141.933 );
        piecePoint[2] =new DescPose(-278.636, -609.413, 329.042, 4.194, -7.682, -138.522 );
        piecePoint[3] =new DescPose(-270.948, -567.929, 326.010, 1.932, -4.908, -138.190 );
        piecePoint[4] =new DescPose(-291.152, -544.315, 324.130, -1.220, -5.373, -139.433);
        piecePoint[5] =new DescPose(-316.419, -543.041, 324.621, 0.387, -5.188, -142.384 );

        pieceExaxisPos[0] = new ExaxisPos( 0.000, 0.000, 0.000, 0.000);
        pieceExaxisPos[1] = new ExaxisPos( 0.000, 0.000, 0.000, 0.000);
        pieceExaxisPos[2] = new ExaxisPos( 0.000, 0.000, 0.000, 0.000);
        pieceExaxisPos[3] = new ExaxisPos( 0.000, 0.000, 0.000, 0.000);
        pieceExaxisPos[4] = new ExaxisPos( 0.000, 0.000, 0.000, 0.000);
        pieceExaxisPos[5] = new ExaxisPos( 0.000, 0.000, 0.000, 0.000);

        exaxisPos[0] = new ExaxisPos( 0.000, 0.000, 0.000, 0.0000);
        exaxisPos[1] = new ExaxisPos( 0.000, 0.000, 0.000, 0.0000);
        exaxisPos[2] = new ExaxisPos( 0.000, 0.000, 0.000, 0.000);
        exaxisPos[3] = new ExaxisPos( 0.000, 0.000, 0.000, 0.00000);

        int tool = 2;
        int wobj = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 12.0;
        double oacc = 12.0;
        int moveType = 0;
        int moveDirection = 0;
        int  rtn = robot.MoveToIntersectLineStart(mainPoint, mainExaxisPos, piecePoint, pieceExaxisPos, extAxisFlag, exaxisPos[0], tool, wobj, vel, acc, ovl, oacc, moveType, moveDirection, offset);
        System.out.printf("MoveToIntersectLineStart rtn is %d\n", rtn);
        rtn = robot.MoveIntersectLine(mainPoint, mainExaxisPos, piecePoint, pieceExaxisPos, extAxisFlag, exaxisPos, tool, wobj, vel, acc, 5.0, 5.0, moveDirection, offset);
        System.out.printf("MoveIntersectLine rtn is %d\n", rtn);

        robot.CloseRPC();
        return ;
    }

    public static void TestSensitivityCalib(Robot robot)
    {
        int rtn = robot.JointSensitivityEnable(0);
        rtn = robot.JointSensitivityEnable(1);
        System.out.printf("JointSensitivityEnable rtn is %d\n", rtn);
        JointPos curJPos = new JointPos();
        robot.GetActualJointPosDegree(curJPos);
        ExaxisPos epos = new ExaxisPos(0,0,0,0);
        DescPose offset_pos =new DescPose(0,0,0,0,0,0 );

        JointPos jointPos1 = new JointPos(curJPos.J1, 0, 0, -90, 0.02, curJPos.J6);
        DescPose descPos1 = new DescPose();
        robot.GetForwardKin(jointPos1, descPos1);
        robot.MoveJ(jointPos1, descPos1, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);

        robot.Sleep(200);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 1 rtn is %d\n", rtn);
        robot.Sleep(100);
        JointPos jointPos2 =new JointPos( curJPos.J1, -30, 0, -90, 0.02, curJPos.J6 );
        DescPose descPos2 =new DescPose();
        robot.GetForwardKin(jointPos2, descPos2);

        robot.MoveJ(jointPos2, descPos2, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);

        robot.Sleep(100);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 2 rtn is %d\n", rtn);
        robot.Sleep(100);

        JointPos jointPos3 = new JointPos( curJPos.J1, -60, 0, -90, 0.02, curJPos.J6 );
        DescPose descPos3 =new DescPose();
        robot.GetForwardKin(jointPos3, descPos3);
        robot.MoveJ(jointPos3, descPos3, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);

        robot.Sleep(100);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 3 rtn is %d\n", rtn);
        robot.Sleep(100);

        JointPos jointPos4 = new JointPos(curJPos.J1, -90, 0, -90, 0.02, curJPos.J6);
        DescPose descPos4 = new DescPose();
        robot.GetForwardKin(jointPos4, descPos4);
        robot.MoveJ(jointPos4, descPos4, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);

        robot.Sleep(100);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 4 rtn is %d\n", rtn);
        robot.Sleep(100);

        JointPos jointPos5 = new JointPos(curJPos.J1, -120, 0, -90, 0.02, curJPos.J6);
        DescPose descPos5 = new DescPose();
        robot.GetForwardKin(jointPos5, descPos5);
        robot.MoveJ(jointPos5, descPos5, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);

        robot.Sleep(100);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 5 rtn is %d\n", rtn);
        robot.Sleep(100);

        JointPos jointPos6 = new JointPos(curJPos.J1, -150, 0, -90, 0.02, curJPos.J6);
        DescPose descPos6 = new DescPose();
        robot.GetForwardKin(jointPos6, descPos6);
        robot.MoveJ(jointPos6, descPos6, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);

        robot.Sleep(100);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 6 rtn is %d\n", rtn);
        robot.Sleep(100);

        JointPos jointPos7 = new JointPos(curJPos.J1, -180, 0, -90, 0.02, curJPos.J6);
        DescPose descPos7 = new DescPose();
        robot.GetForwardKin(jointPos7, descPos7);
        robot.MoveJ(jointPos7, descPos7, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);

        robot.Sleep(100);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 7 rtn is %d\n", rtn);
        robot.Sleep(100);

        //反向行程
        robot.MoveJ(jointPos6, descPos6, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.Sleep(100);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 8 rtn is %d\n", rtn);
        robot.Sleep(100);

        robot.MoveJ(jointPos5, descPos5, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.Sleep(100);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 9 rtn is %d\n", rtn);
        robot.Sleep(100);

        robot.MoveJ(jointPos4, descPos4, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.Sleep(100);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 10 rtn is %d\n", rtn);
        robot.Sleep(100);

        robot.MoveJ(jointPos3, descPos3, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.Sleep(100);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 11 rtn is %d\n", rtn);
        robot.Sleep(100);

        robot.MoveJ(jointPos2, descPos2, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.Sleep(100);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 12 rtn is %d\n", rtn);
        robot.Sleep(100);

        robot.MoveJ(jointPos1, descPos1, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.Sleep(200);
        rtn = robot.JointSensitivityCollect();
        System.out.printf("JointSensitivityCollect 13 rtn is %d\n", rtn);
        robot.Sleep(100);


        double[] calibResult =new double[6];
        double[] linearity = new double[6];
        rtn = robot.JointSensitivityCalibration(calibResult, linearity);
        System.out.printf("JointSensitivityCalibration rtn is %d\n", rtn);

        rtn = robot.JointSensitivityEnable(0);
        System.out.printf("JointSensitivityEnable rtn is %d\n", rtn);

        System.out.printf("jointSensor Calib result is %f %f %f %f %f %f\njointSensor linearity is %f %f %f %f %f %f\n",
                calibResult[0], calibResult[1], calibResult[2],
                calibResult[3], calibResult[4], calibResult[5],
                linearity[0], linearity[1], linearity[2],
                linearity[3], linearity[4], linearity[5]);

        double[] hysteresisError = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        rtn = robot.JointHysteresisError(hysteresisError);
        System.out.printf("JointHysteresisError result is %f %f %f %f %f %f\n",
                hysteresisError[0], hysteresisError[1], hysteresisError[2],
                hysteresisError[3], hysteresisError[4], hysteresisError[5]);

        double[] repeatability = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        rtn = robot.JointRepeatability(repeatability);
        System.out.printf("JointRepeatability result is %f %f %f %f %f %f\n",
                repeatability[0], repeatability[1], repeatability[2],
                repeatability[3], repeatability[4], repeatability[5]);

        double[] M = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        double[] B = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        double[] K = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        double[] threshold = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        int setZeroFlag = 1;
        rtn = robot.SetAdmittanceParams(M, B, K, threshold, calibResult, setZeroFlag);
        System.out.printf("SetAdmittanceParams rtn is %d\n", rtn);
    }


//    public static void TestIntersectLineMove(Robot robot)
//    {
//        DescPose[] mainPoint = new DescPose[6];
//        DescPose[] piecePoint = new DescPose[6];
//
//        mainPoint[0] = new DescPose(144.084, 512.064, 8.899, -58.958, 40.838, 23.295);
//        mainPoint[1] = new DescPose(132.150, 512.638, 28.157, -58.626, 40.788, 24.966);
//        mainPoint[2] = new DescPose(150.155, 514.479, 74.107, -47.740, 40.410, 27.552);
//        mainPoint[3] = new DescPose(188.346, 518.501, 73.946, -17.227, 60.139, 15.265);
//        mainPoint[4] = new DescPose(206.811, 520.214, 52.966, -18.198, 60.381, 12.624);
//        mainPoint[5] = new DescPose(203.002, 518.627, 19.028, -23.830, 61.410, 8.343);
//        piecePoint[0] = new DescPose( 190.428,480.862,102.236,8.966,46.472,35.582 );
//        piecePoint[1] = new DescPose(201.770,510.904,101.912,12.079,66.897,26.452);
//        piecePoint[2] = new DescPose(186.344,533.294,102.866,1.980,62.882,25.094);
//        piecePoint[3] = new DescPose(162.969,537.537,103.015,-22.013,46.227,28.606);
//        piecePoint[4] = new DescPose(139.465,510.090,103.505,-23.996,33.774,41.829);
//        piecePoint[5] = new DescPose(168.329,475.251,102.325,4.241,42.293,38.624);
//
//        int tool = 4;
//        int wobj = 0;
//        double vel = 100.0;
//        double acc = 100.0;
//        double ovl = 10.0;
//        double oacc = 10.0;
//        int moveType = 1;
//        int moveDirection = 1;
//        int rtn = robot.MoveToIntersectLineStart(mainPoint, piecePoint, tool, wobj, vel, acc, ovl, oacc, moveType);
//        System.out.printf("MoveToIntersectLineStart rtn is %d\n", rtn);
//        rtn = robot.MoveIntersectLine(mainPoint, piecePoint, tool, wobj, vel, acc, ovl, oacc, moveDirection);
//        System.out.printf("MoveIntersectLine rtn is %d\n", rtn);
//    }


    public static void ServoJTWithSafety(Robot robot)
    {
        robot.udpCmdClient.SetUDPCmdRpyCallback((srcType, count, cmdID, dataLen, content) -> {
            System.out.println("\n[Received UDP reply from robot]");
            System.out.println("srcType: " + srcType);
            System.out.println("count: " + count);
            System.out.println("cmdID: " + cmdID);
            System.out.println("dataLen: " + dataLen);
            System.out.println("content: " + content);
            return 0;
        });
        while (true) {
           robot.ResetAllError();
            robot.Sleep(500);
            List<Number> torques;
            torques=robot.GetJointTorques(1);
            robot.ServoJTStart(1); //   #servoJT开始
            ROBOT_STATE_PKG pkg=new ROBOT_STATE_PKG();
            robot.DragTeachSwitch(1);
            int checkFlag = 3;//-1,3
            double[] jPowerLimit = { 10.0, 10.0, 10.0, 10.0, 10.0, 10.0 };
            double[] jVelLimit = { 50, 50, 50, 50, 50, 50};//180.1,-1
            int count = 800000;
            int error = 0;
            int comType = 1;

            double[] tor=new double[]{(double)torques.get(1),(double)torques.get(2),(double)torques.get(3),(double)torques.get(4),(double)torques.get(5),(double)torques.get(6)};

            while (true) {
                tor[0] = 0.08;//  #每次1轴增加0.01NM，运动100次
                error = robot.ServoJT(tor, 0.01, checkFlag, jPowerLimit, jVelLimit, comType);  //# 关节空间伺服模式运动
                System.out.printf("ServoJT rtn is %d\n", error);
                count = count - 1;
                robot.Sleep(1);
                pkg = robot.GetRobotRealTimeState();
                System.out.printf("maincode %d, subcode %d\n", pkg.main_code, pkg.sub_code);
                if (pkg.jt_cur_pos[0] > 30)
                    break;
            }

            tor = new double[]{(double) torques.get(1), (double) torques.get(2), (double) torques.get(3), (double) torques.get(4), (double) torques.get(5), (double) torques.get(6)};
            while (true) {
                tor[0] = -0.08;//  #每次1轴增加0.01NM，运动100次
                error = robot.ServoJT(tor, 0.01, checkFlag, jPowerLimit, jVelLimit, 1);  //# 关节空间伺服模式运动
                System.out.printf("ServoJT rtn is %d\n", error);
                count = count - 1;
                robot.Sleep(1);
                pkg = robot.GetRobotRealTimeState();
                System.out.printf("maincode %d, subcode %d\n", pkg.main_code, pkg.sub_code);
                if (pkg.jt_cur_pos[0] < 0)
                    break;
            }


            robot.DragTeachSwitch(0);

            error = robot.ServoJTEnd(1);  //#伺服运动结束
        }
    }


    public static void TestFTControlWithDamping(Robot robot)
    {
        int sensor_id = 10;
        int[] select = { 0,0,1,0,0,0 };
        double[] ft_pid = { 0.0008, 0.0, 0.0, 0.0, 0.0, 0.0 };
        int adj_sign = 0;
        int ILC_sign = 0;
        double max_dis = 100.0;
        double max_ang = 20;

        ForceTorque ft =new ForceTorque(0,0,0,0,0,0);
        ft.fz = -10.0;
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);
        JointPos j1=new JointPos(-118.985, -86.882, -118.139, -65.019, 90.002, 54.951);
        JointPos j2=new JointPos(-77.055, -77.218, -126.219, -66.591, 90.028, 96.881);
        DescPose desc_p1=new DescPose(-300.856, -332.618, 309.240, 179.976, -0.031, 96.065);
        DescPose desc_p2=new DescPose(-16.399, -383.760, 309.312, 179.975, -0.031, 96.064);
        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);

        double[] M = {2.0, 2.0};
        double[] B = {8.0, 8.0};
        double polishRadio;
        int filter_Sign;
        int posAdapt_sign;
        int isNoBlock;

        DescPose ftCoord =new DescPose();
        robot.FT_SetRCS(2, ftCoord);
        int rtn = robot.FT_Control(1, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, M, B, 0, 0, 1, 0);
        System.out.printf("FT_Control start rtn is %d\n", rtn);

        rtn = robot.MoveL(j1, desc_p1, 0, 0, 100.0, 100.0, 20.0, -1.0,0, epos, 0, 0, offset_pos,100,0,0,10);
        rtn = robot.MoveL(j2, desc_p2, 0, 0, 100.0, 100.0, 20.0, -1.0, 0,epos, 0, 0, offset_pos,100,0,0,10);
        rtn = robot.FT_Control(1, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, M, B, 0, 0, 1, 0);
        System.out.printf("FT_Control end rtn is %d\n", rtn);

        robot.CloseRPC();
    }

    public static void TestSpiral(Robot robot)
    {
        JointPos j=new JointPos(67.957, -81.482, 87.595, -95.691, -94.899, -9.727);
        DescPose desc_pos=new DescPose(-123.142, -551.735, 430.549, 178.753, -4.757, 167.754);
        DescPose offset_pos1=new DescPose(50, 0, 0, -30, 0, 0);
        DescPose offset_pos2=new DescPose(50, 0, 0, -30, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);
        SpiralParam sp=new SpiralParam();
        sp.circle_num = 2;
        sp.circle_angle = 30.0;
        sp.rad_init = 50.0;
        sp.rad_add = 10.0;
        sp.rotaxis_add = 10.0;
        sp.rot_direction = 0;
        sp.velAccMode = 1;

        int tool = 0;
        int user = 0;
        double vel = 30;
        double acc = 60;
        double ovl = 100.0;
        double blendT = -1;
        int flag = 2;

        robot.SetSpeed(20);

        int rtn = robot.MoveJ(j, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos1);
        System.out.printf("movej errcode:%d\n", rtn);

        rtn = robot.NewSpiral(desc_pos, tool, user, vel, acc, epos, ovl, flag, offset_pos2, sp,-1);
        System.out.printf("newspiral errcode:%d\n", rtn);

        robot.CloseRPC();
    }

    public static void TestVelFeedForwardRatio(Robot robot)
    {
        double[] setRadio =new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
        robot.SetVelFeedForwardRatio(setRadio);
        double[] getRadio = new double[]{ 0.0 };
        robot.GetVelFeedForwardRatio(getRadio);
        System.out.printf(" %f %f %f %f %f %f\n", getRadio[0], getRadio[1], getRadio[2], getRadio[3], getRadio[4], getRadio[5]);
        robot.CloseRPC();
    }

    public static void TestServoJ_1015(Robot robot)
    {
        ROBOT_STATE_PKG pkg=new ROBOT_STATE_PKG();

        JointPos j=new JointPos(0, 0, 0, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);

        double vel = 0.0;
        double acc = 0.0;
        double cmdT = 0.008;
        double filterT = 0.0;
        double gain = 0.0;
        int flag = 0;
        int count = 500;
        double dt = 0.1;
        int cmdID = 0;
        int ret = robot.GetActualJointPosDegree(j);
        if (ret == 0)
        {
            cmdID += 1;
            robot.ServoMoveStart(1);
            while (count>0)
            {
                robot.ServoJ(j, epos, acc, vel, cmdT, filterT, gain, cmdID,1);
                j.J5 += dt;
                count -= 1;
                robot.WaitMs((int)(1000*cmdT));
                pkg=robot.GetRobotRealTimeState();
                System.out.println("Servoj Count :"+pkg.servoJCmdNum+" ;  last pos is :"+pkg.lastServoTarget[0]+" "+ pkg.lastServoTarget[1]+" "+ pkg.lastServoTarget[2]+" "+
                        pkg.lastServoTarget[3]+" "+ pkg.lastServoTarget[4]+" "+ pkg.lastServoTarget[5]);

                if (count < 50)
                {
                    robot.MotionQueueClear();
                    System.out.println("After queue clear, Servoj Count :"+pkg.servoJCmdNum+";  last pos is :"+
                            pkg.lastServoTarget[0]+" "+ pkg.lastServoTarget[1]+" "+ pkg.lastServoTarget[2]+" "+
                            pkg.lastServoTarget[3]+" "+ pkg.lastServoTarget[4]+" "+ pkg.lastServoTarget[5]);
                    break;
                }
            }
            robot.ServoMoveEnd(1);
        }
        else
        {
            System.out.println("GetActualJointPosDegree errcode:"+ ret);
        }

        robot.CloseRPC();
    }

    public static void TestSlavePortErr(Robot robot)
    {
        ROBOT_STATE_PKG pkg =new ROBOT_STATE_PKG();
        int[] inRecvErr =new int[8];
        int[] inCRCErr =new int[8];
        int[] inTransmitErr =new int[8];
        int[] inLinkErr =new int[8];
        int[] outRecvErr =new int[8];
        int[] outCRCErr =new int[8];
        int[] outTransmitErr =new int[8];
        int[] outLinkErr =new int[8];
        robot.GetSlavePortErrCounter(inRecvErr,  inCRCErr, inTransmitErr, inLinkErr,
                outRecvErr, outCRCErr, outTransmitErr, outLinkErr);
        for (int i = 0; i < 8; i++)
        {
            if (inRecvErr[i] != 0)
            {
                System.out.printf("inRecvErr %d is %d\n", i, inRecvErr[i]);
            }

            if (inCRCErr[i] != 0)
            {
                System.out.printf("inRecvErr %d is %d\n", i, inCRCErr[i]);
            }

            if (inTransmitErr[i] != 0)
            {
                System.out.printf("inRecvErr %d is %d\n", i, inTransmitErr[i]);
            }

            if (inLinkErr[i] != 0)
            {
                System.out.printf("inRecvErr %d is %d\n", i, inLinkErr[i]);
            }

            if (outRecvErr[i] != 0)
            {
                System.out.printf("outRecvErr %d is %d\n", i, outRecvErr[i]);
            }

            if (outCRCErr[i] != 0)
            {
                System.out.printf("outCRCErr %d is %d\n", i, outCRCErr[i]);
            }

            if (outTransmitErr[i] != 0)
            {
                System.out.printf("outTransmitErr %d is %d\n", i, outTransmitErr[i]);
            }

            if (outLinkErr[i] != 0)
            {
                System.out.printf("outLinkErr %d is %d\n", i, outLinkErr[i]);
            }
        }
        System.out.printf("others has no err!\n");

        for (int i = 0; i < 8; i++)
        {
            robot.SlavePortErrCounterClear(i);
        }

        robot.CloseRPC();
    }

    public static void testLasertrackMoveC(Robot robot)
    {

        //上传并加载开放协议文件
        //robot.OpenLuaUpload("E://openlua/CtrlDev_laser_ruiniu-0117.lua");
        //robot.Sleep(2000);
        //robot.SetCtrlOpenLUAName(0, "CtrlDev_laser_ruiniu-0117.lua");
        //robot.UnloadCtrlOpenLUA(0);
        //robot.LoadCtrlOpenLUA(0);
        //robot.Sleep(8000);
        robot.ResetAllError();
        int cnt = 1;
        while (cnt < 2)
        {
            //运动到需要寻位的起始点
            JointPos startjointPos=new JointPos(40.947, -133.649, 128.497, -108.428, -87.159, -21.741);
            DescPose startdescPose=new DescPose(-167.396, -301.742, 224.468, -157.008, -6.084, 152.043);
            ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
            DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
            DescTran directionPoint=new DescTran();
            robot.MoveL(startjointPos, startdescPose, 1, 0, 50, 100, 100, -1, 0,exaxisPos, 0, 0, offdese,100,0, 1, 1);
            robot.Sleep(2000);
            //沿着-y方向开始寻位
            int ret = robot.LaserTrackingSearchStart_xyz(0, 100, 300, 1000, 2);
            robot.LaserTrackingSearchStop();
            //如果寻位成功
            if (ret == 0)
            {
                //运动到寻位点
                robot.MoveToLaserSeamPos(1, 30, 0, 0, 0, offdese);
                //开始沿着寻位点进行激光跟踪
                robot.LaserTrackingTrackOnOff(1, 2);
                JointPos midjointPos=new JointPos(23.925, -68.391, 72.649, -89.970, -102.641, 102.979);
                DescPose middescPose=new DescPose(-561.957, -282.616, 179.994, -166.732, -1.366, 11.262);
                JointPos endjointPos=new JointPos(-11.146, -110.681, 112.893, -71.999, -83.325, 208.723);
                DescPose enddescPose=new DescPose(-250.875, -93.272, 182.343, -159.126, 4.036, -130.316);
                robot.MoveC(midjointPos, middescPose, 1, 0, 30, 100, exaxisPos, 0, offdese, endjointPos, enddescPose, 1, 0, 30, 100, exaxisPos, 0, offdese,100, 100, -1, 0);
//                robot.Circle(midjointPos, middescPose, 1, 0, 30, 100, exaxisPos, endjointPos, enddescPose, 1, 0, 30, 100, exaxisPos, 100, 0, offdese, 100, -1, 0);

                //停止跟踪
                robot.LaserTrackingTrackOnOff(0, 2);

            }
            cnt++;
        }
    }


    public static void testLaserRecordAndReplayMoveC(Robot robot)
    {

        int cnt = 1;
        while (cnt < 2)
        {
            //运动到扫描的起点
            JointPos startjointPos1=new JointPos(-15.647, -119.042, 109.960, -71.222, -73.948, -122.687);
            DescPose startdescPose1=new DescPose(-274.669, -122.896, 246.972, -161.315, -0.312, -164.381);
            ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
            DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);

            robot.MoveJ(startjointPos1, startdescPose1, 1, 0, 100, 100, 50, exaxisPos, -1, 0, offdese);

            //运动到扫描的起点
            JointPos startjointPos=new JointPos(-23.965, -150.841, 137.707, -89.747, -56.114, -122.685);
            DescPose startdescPose=new DescPose(-274.002, -189.344, 194.938, -157.388, -28.759, -173.209);
            robot.MoveL(startjointPos, startdescPose, 1, 0, 10, 100, 100, -1,0, exaxisPos, 0, 0, offdese, 100,0,1, 1);
            //开始轨迹记录
            robot.LaserSensorRecord1(2, 10);
            //运动到需要记录的终点
            JointPos midjointPos=new JointPos(36.350, -59.819, 63.114, -51.373, -105.011, 98.495);
            DescPose middescPose=new DescPose(-370.608, -294.229, 181.531, -158.073, -39.221, 25.737);
            System.out.println("111111");
            JointPos endjointPos=new JointPos(-26.944, -101.993, 115.794, -72.164, -53.080, 164.700);
            DescPose enddescPose=new DescPose(-353.625, -155.023, 185.415, -151.407, -39.177, -122.813);
            robot.MoveC(midjointPos, middescPose, 1, 0, 10, 100, exaxisPos, 0, offdese, endjointPos, enddescPose, 1, 0, 10, 100, exaxisPos, 0, offdese, 100, -1,100, 0);
//            robot.Circle(midjointPos, middescPose, 1, 0, 10, 100, exaxisPos, endjointPos, enddescPose, 1, 0, 10, 100, exaxisPos, 100, 0, offdese, 100, -1, 0);
            System.out.println("222222\n");
            //停止记录
            robot.LaserSensorRecord1(0, 10);
            System.out.println("333333");

            robot.Sleep(2000);
            //robot.StopMotion();

            JointPos startjointPos2=new JointPos(-6.592, -140.898, 122.764, -88.529, -81.143, -82.069);
            DescPose startdescPose2=new DescPose(-251.875, -124.247, 250.719, -168.899, -15.289, 165.278);
            robot.MoveJ(startjointPos2, startdescPose2, 1, 0, 100, 100, 50, exaxisPos, -1, 0, offdese);
            System.out.println("4444444");
            ////运动到记录的焊缝起点
            robot.MoveToLaserRecordStart(1, 30);
            //开始轨迹复现
            robot.LaserSensorReplay(10, 100);

            robot.MoveLTR();
            //停止轨迹复现
            robot.LaserSensorRecord1(0, 10);
            cnt++;
        }

        robot.CloseRPC();
    }

    public static void TestKernelOTA(Robot robot)
    {
        robot.KernelUpgrade("D://zUP/file.txt");

        int[] result =new int[1];
        robot.GetKernelUpgradeResult(result);
        System.out.println("OTA result :"+ result);

        robot.CloseRPC();
    }

    public static void TestCustomWeaveSetPara(Robot robot)
    {
        DescTran[] point = new DescTran[10];
        point[0]=new DescTran();
        point[0].x = -3;
        point[0].y = -3;
        point[0].z = 0;

        point[1]=new DescTran();
        point[1].x = -6;
        point[1].y = 0;
        point[1].z = 0;

        point[2]=new DescTran();
        point[2].x = -3;
        point[2].y = 3;
        point[2].z = 0;

        point[3]=new DescTran();
        point[3].x = 0;
        point[3].y = 0;
        point[3].z = 0;
        point[4]=new DescTran(0,0,0);
        point[5]=new DescTran(0,0,0);
        point[6]=new DescTran(0,0,0);
        point[7]=new DescTran(0,0,0);
        point[8]=new DescTran(0,0,0);
        point[9]=new DescTran(0,0,0);

        double[] stayTime =new double[] { 0,0,0,0,0,0,0,0,0,0 };
        int rtn = robot.CustomWeaveSetPara(2, 4, point, stayTime, 1.000, 0, 0);
        System.out.println("CustomWeaveSetPara rtn is :"+ rtn);
        robot.Sleep(1000);

        int[] pointNum = new int[1];
        double[] frequency=new double[1];
        int[] incStayType=new int[1];
        int[] stationary=new int[1];
        robot.CustomWeaveGetPara(2, pointNum, point, stayTime, frequency, incStayType, stationary);
        System.out.println("pointNum is :"+ pointNum[0]);
        for (int i = 0; i < pointNum[0]; i++)
        {
            System.out.println("point:"+i+", "+ point[i].x+", "+ point[i].y+", "+ point[i].z);
        }
        System.out.println("fre is :"+ frequency[0]+", stay is:"+ incStayType[0]+", "+ stationary[0]);

        robot.WeaveSetPara(0, 9, 1.000000, 1, 5.000000,
                6.000000, 5.000000, 50, 100, 100,
                0, 1, 0.000000, 0.000000);

        DescPose desc_p1 =new DescPose(-668.674,50.127,211.698,-176.978,5.333,10.535 );
        DescPose desc_p2 = new DescPose( -529.591,-77.247,204.008,-178.889,1.356,-14.668);
        DescPose desc_p3 = new DescPose( -529.593,282.831,204.007,-178.890,1.357,-14.667 );
        JointPos j1 = new JointPos( -12.938,-58.855,91.909,-129.146,-90.652,66.633 );
        JointPos j2 = new JointPos( -2.427,-73.309,113.912,-131.693,-91.373,102.241 );
        JointPos j3 = new JointPos( -37.804,-66.024,103.325,-128.984,-90.488,66.870 );
        robot.GetForwardKin(j1, desc_p1);
        robot.GetForwardKin(j2, desc_p2);
        robot.GetForwardKin(j3, desc_p3);


        ExaxisPos epos = new ExaxisPos();
        DescPose offset_pos = new DescPose();

        robot.MoveJ(j1, desc_p1, 3, 0, 100, 100,100, epos, -1, 0, offset_pos);
        robot.WeaveStart(0);
        robot.Circle(j3, desc_p3, 3, 0, 100, 100, epos, j2, desc_p2, 3, 0, 100, 100, epos, 10, -1, offset_pos,0,-1,0);
        robot.WeaveEnd(0);
        robot.MoveJ(j1, desc_p1, 3, 0, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.WeaveStart(0);
        robot.MoveC(j3, desc_p3, 3, 0, 100, 100, epos, 0, offset_pos, j2, desc_p2, 3, 0, 100, 100, epos, 0, offset_pos, 10, -1,100,0);
        robot.WeaveEnd(0);
        robot.MoveJ(j1, desc_p1, 3, 0, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.WeaveStart(0);
        robot.MoveL(j2, desc_p2, 3, 0, 100, 100, 10, -1,epos, 0, 0, offset_pos, 0, 100);
        robot.WeaveEnd(0);

        robot.CloseRPC();
    }

    public static int TestImpedanceControl(Robot robot)
    {
        JointPos j1=new JointPos(102.622, -135.990, 120.769, -73.950, -90.848, 35.507);
        JointPos j2=new JointPos(93.674, -80.062, 82.947, -92.199, -90.967, 26.559);

        DescPose desc_pos1=new DescPose(136.552, -149.799, 449.532, 179.817, -1.172, 157.123);
        DescPose desc_pos2=new DescPose(136.540, -561.048, 449.542, 179.819, -1.172, 157.122);

        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 200.0;
        double ovl = 100.0;
        double blendT = -1.0;
        double blendR = -1.0;
        int flag = 0;
        int search = 0;

        robot.SetSpeed(20);

        double[] forceThreshold = { 10,10,10,1,1,1 };
        double[] m = { 0.04,0.04,0.04,0.01,0.01,0.01 };
        double[] b = { 0.1,0.1,0.1,0.08,0.08,0.08 };
        double[] k = { 0,0,0,0,0,0 };

        int rtn = robot.ImpedanceControlStartStop(1, 0, forceThreshold, m, b, k, 50, 50, 100, 100);
        System.out.println("ImpedanceControlStartStop errcode:%d" + rtn);
        rtn = robot.MoveJ(j1, tool, user, vel, acc, ovl, epos, -1, 0, offset_pos);
        rtn = robot.MoveJ(j2, tool, user, vel, acc, ovl, epos, -1, 0, offset_pos);
        rtn = robot.MoveJ(j1, tool, user, vel, acc, ovl, epos, -1, 0, offset_pos);
        rtn = robot.MoveJ(j2, tool, user, vel, acc, ovl, epos, -1, 0, offset_pos);

        System.out.println("movel errcode:"+ rtn);

        robot.ImpedanceControlStartStop(0, 1, forceThreshold, m, b, k, 1000, 500, 100, 100);


        robot.CloseRPC();
        return 0;
    }

    public static int TestImpedanceControl_old(Robot robot)
    {
        JointPos j1=new JointPos(102.622, -135.990, 120.769, -73.950, -90.848, 35.507);
        JointPos j2=new JointPos(93.674, -80.062, 82.947, -92.199, -90.967, 26.559);

        DescPose desc_pos1=new DescPose(136.552, -149.799, 449.532, 179.817, -1.172, 157.123);
        DescPose desc_pos2=new DescPose(136.540, -561.048, 449.542, 179.819, -1.172, 157.122);

        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 200.0;
        double ovl = 100.0;
        double blendT = -1.0;
        double blendR = -1.0;
        int flag = 0;
        int search = 0;

        robot.SetSpeed(20);

        int company = 22;
        int device = 0;
        int softversion = 0;
        int bus = 1;

        DeviceConfig con=new DeviceConfig(company, device, softversion, bus);
        robot.FT_SetConfig(con);
        robot.Sleep(1000);
        robot.FT_GetConfig(con);
        System.out.println("FT config:"+con.company+","+con.device+","+con.softwareVersion+"con"+ con.bus);
        robot.Sleep(1000);

        robot.FT_Activate(0);
        robot.Sleep(1000);
        robot.FT_Activate(1);
        robot.Sleep(1000);

        robot.Sleep(1000);
        robot.FT_SetZero(0);
        robot.Sleep(1000);
        robot.FT_SetZero(1);
        robot.Sleep(1000);

        double[] forceThreshold = { 30,30,30,5,5,5 };
        double[] m= { 0.1,0.1,0.1,0.02,0.02,0.02 };
        double[] b = { 1,1,1,0.08,0.08,0.08 };
        double[] k = { 0,0,0,0,0,0 };

        int rtn = robot.ImpedanceControlStartStop(1, 1, forceThreshold, m, b, k, 1000, 500, 100, 100);
        System.out.println("ImpedanceControlStartStop errcode:"+ rtn);
        rtn = robot.MoveL(desc_pos1, tool, user, vel, acc, ovl, blendR, 0, epos, search, flag, offset_pos, -1,0,-1, 1);
        rtn = robot.MoveL(desc_pos2, tool, user, vel, acc, ovl, blendR, 0, epos, search, flag, offset_pos, -1,0,-1, 1);
        rtn = robot.MoveL(desc_pos1, tool, user, vel, acc, ovl, blendR, 0, epos, search, flag, offset_pos, -1,0,-1, 1);
        rtn = robot.MoveL(desc_pos2, tool, user, vel, acc, ovl, blendR, 0, epos, search, flag, offset_pos, -1,0,-1, 1);
        rtn = robot.MoveL(desc_pos1, tool, user, vel, acc, ovl, blendR, 0, epos, search, flag, offset_pos, -1,0,-1, 1);
        rtn = robot.MoveL(desc_pos2, tool, user, vel, acc, ovl, blendR, 0, epos, search, flag, offset_pos, -1,0,-1, 1);

        System.out.println("movel errcode:"+ rtn);

        robot.ImpedanceControlStartStop(0, 1, forceThreshold, m, b, k, 1000, 500, 100, 100);


        robot.CloseRPC();
        return 0;
    }

    public static void testLasertrackandExitAxis(Robot robot)
    {
        ExaxisPos startexaxisPos =new ExaxisPos( 0,0,0,0 );
        ExaxisPos seamexaxisPos = new ExaxisPos(-10,0,0,0 );
        ExaxisPos endexaxisPos = new ExaxisPos(-30, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0 );
        JointPos seamjointPos=new JointPos(0, 0, 0, 0, 0, 0);
        DescPose seamdescPose=new DescPose(0, 0, 0, 0, 0, 0);

        for(int i =0;i<10;++i) {
            //运动到需要寻位的起始点
            JointPos startjointPos = new JointPos(58.337, -119.628, 146.037, -116.358, -92.224, -117.654);
            DescPose startdescPose = new DescPose(-53.375, -255.363, 0.919, 178.054, 1.077, -94.026);
            robot.ExtAxisSyncMoveJ(startjointPos, startdescPose, 1, 0, 100, 100, 100, startexaxisPos, -1, 0, offdese);

            System.out.println("11111");
            //沿着-y方向开始寻位
            int ret = robot.LaserTrackingSearchStart_xyz(3, 100, 300, 1000, 2);
            robot.LaserTrackingSearchStop();
            System.out.println("2222");
            int[] tool = new int[1];
            int[] user = new int[1];
            robot.GetLaserSeamPos(0, offdese, seamjointPos, seamdescPose, tool, user, startexaxisPos);
            System.out.println(seamjointPos.J1 + ", " + seamjointPos.J2 + ", " +
                    seamjointPos.J3 + ", " + seamjointPos.J4 + ", " +
                    seamjointPos.J5 + ", " + seamjointPos.J6 + ", " +
                    seamdescPose.tran.x + ", " + seamdescPose.tran.y + ", " +
                    seamdescPose.tran.z + ", " + seamdescPose.rpy.rx + ", " +
                    seamdescPose.rpy.ry + ", " + seamdescPose.rpy.rz);
            //如果寻位成功
            if (ret == 0) {
                //机器人和扩展轴同步运动到寻位点
                robot.ExtAxisSyncMoveJ(seamjointPos, seamdescPose, 1, 0, 100, 100, 100, seamexaxisPos, -1, 0, offdese);

                //开始沿着寻位点进行激光跟踪并与扩展轴同步运动
                System.out.println("3333");
                robot.LaserTrackingTrackOnOff(1, 2);
                JointPos endjointPos = new JointPos(70.580, -90.918, 126.593, -125.154, -92.162, -105.403);
                DescPose enddescPose = new DescPose(-53.375, -419.020, 0.920, 178.054, 1.076, -94.026);
                robot.ExtAxisSyncMoveL(endjointPos, enddescPose, 1, 0, 20, 100, 100, -1, endexaxisPos, 0, offdese);
                ;
                //停止跟踪
                robot.LaserTrackingTrackOnOff(0, 2);
                System.out.println("44444");
            }
            System.out.println("当前运行次数为:"+i);
        }
        robot.CloseRPC();
    }

    public static void testLaserConfig(Robot robot)
    {
        robot.LaserTrackingSensorConfig("192.168.58.20", 5020);

        robot.LaserTrackingSensorSamplePeriod(20);

        robot.LoadPosSensorDriver(101);
        robot.LaserTrackingLaserOnOff(0,0);

        robot.Sleep(3000);

        robot.LaserTrackingLaserOnOff(1, 0);

        robot.CloseRPC();
    }

    public static void  testGetLaserPoint(Robot robot)
    {
        String name = "laserPoint";
        List<Number> data;

        data=robot.GetRobotTeachingPoint(name);
        System.out.println(data.get(1).doubleValue()+","+data.get(2).doubleValue()+","+data.get(3).doubleValue()+","+ data.get(4).doubleValue()+","+ data.get(5).doubleValue()+","+data.get(6).doubleValue()+","+data.get(7).doubleValue()+","+data.get(8).doubleValue()+","+data.get(9).doubleValue()+","+data.get(10).doubleValue()+","+data.get(11).doubleValue()+","+data.get(12).doubleValue());
        JointPos startjointPos=new JointPos(data.get(7).doubleValue(), data.get(8).doubleValue(),data.get(9).doubleValue(),data.get(10).doubleValue(), data.get(11).doubleValue(),data.get(12).doubleValue());
        DescPose startdescPose=new DescPose(data.get(1).doubleValue(), data.get(2).doubleValue(),data.get(3).doubleValue(),data.get(4).doubleValue(),data.get(5).doubleValue(),data.get(6).doubleValue());
//        JointPos startjointPos=new JointPos(87.516,-130.505,116.883,-72.711,-87.278,4.4);
//        DescPose startdescPose=new DescPose(107.882,-195.329,262.601,176.131,2.427,172.947);
        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
        robot.MoveL(startjointPos, startdescPose, 1, 0, 10, 100, 100, -1,0, exaxisPos, 0, 0,offdese, 100,0,1, 1);

        //robot.LaserSensorRecord1(2, 10);

        //JointPos endjointPos(68.809, -87.100, 121.120, -127.233, -95.038, -109.555);
        //DescPose enddescPose(-103.555, -464.234, 13.076, 174.179, -1.344, -91.709);
        //robot.MoveL(&endjointPos, &enddescPose, 1, 0, 100, 100, 100, -1, &exaxisPos, 0, 0, &offdese, 1, 1);

        //robot.LaserSensorRecord1(0, 10);

        //robot.MoveToLaserRecordStart(1, 30);

//        robot.CloseRPC();
    }

    public static void testMoveToLaserRecordStart(Robot robot)
    {

        JointPos startjointPos=new JointPos(56.205, -117.951, 141.872, -118.149, -94.217, -122.176);
        DescPose startdescPose=new DescPose(-97.552, -282.855, 26.675, 174.182, -1.338, -91.707);
        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
        robot.MoveL(startjointPos, startdescPose, 1, 0, 100, 100, 100, -1, 0,exaxisPos, 0, 0, offdese, 100,0,1, 1);

        robot.LaserSensorRecord1(2, 10);

        JointPos endjointPos=new JointPos(68.809, -87.100, 121.120, -127.233, -95.038, -109.555);
        DescPose enddescPose=new DescPose(-103.555, -464.234, 13.076, 174.179, -1.344, -91.709);
        robot.MoveL(endjointPos, enddescPose, 1, 0, 50, 100, 100, -1, 0,exaxisPos, 0, 0, offdese, 100,0,1, 1);

        robot.LaserSensorRecord1(0, 10);

        robot.MoveToLaserRecordStart(1, 30);

        robot.CloseRPC();
    }

    public static void testMoveToLaserRecordEnd(Robot robot)
    {

        JointPos startjointPos=new JointPos(56.205, -117.951, 141.872, -118.149, -94.217, -122.176);
        DescPose startdescPose=new DescPose(-97.552, -282.855, 26.675, 174.182, -1.338, -91.707);
        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
        robot.MoveL(startjointPos, startdescPose, 1, 0, 100, 100, 100, -1,0, exaxisPos, 0, 0, offdese, 100,0,1, 1);

        robot.LaserSensorRecord1(2, 10);

        JointPos endjointPos=new JointPos(68.809, -87.100, 121.120, -127.233, -95.038, -109.555);
        DescPose enddescPose=new DescPose(-103.555, -464.234, 13.076, 174.179, -1.344, -91.709);
        robot.MoveL(endjointPos, enddescPose, 1, 0, 50, 100, 100, -1, 0,exaxisPos, 0, 0, offdese, 100,0,1, 1);

        robot.LaserSensorRecord1(0, 10);

        robot.MoveToLaserRecordEnd(1, 30);

        robot.CloseRPC();
    }

    public static void testLasertrack_point(Robot robot)
    {
        String name = "laserEnd";
        List<Number> data;
        JointPos startjointPos=new JointPos(56.205, -117.951, 141.872, -118.149, -94.217, -122.176);
        DescPose startdescPose=new DescPose(-97.552, -282.855, 26.675, 174.182, -1.338, -91.707);
        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
        DescTran directionPoint=new DescTran();

        robot.MoveL(startjointPos,startdescPose, 1, 0, 100, 100, 100, -1,0, exaxisPos, 0, 0, offdese, 100,0,1, 1);

        data=robot.GetRobotTeachingPoint(name);
        System.out.println(data.get(1).doubleValue()+","+data.get(2).doubleValue()+","+data.get(3).doubleValue()+","+data.get(4).doubleValue()+","+data.get(5).doubleValue()+","+data.get(6).doubleValue()+","+data.get(7).doubleValue()+","+data.get(8).doubleValue()+","+data.get(9).doubleValue()+","+data.get(10).doubleValue()+","+data.get(11).doubleValue()+","+data.get(12).doubleValue());
        directionPoint.x = data.get(1).doubleValue();
        directionPoint.y = data.get(2).doubleValue();
        directionPoint.z = data.get(3).doubleValue();
        System.out.println( directionPoint.x+","+ directionPoint.y+","+ directionPoint.z);

        robot.LaserTrackingSearchStart_point(directionPoint, 100, 500, 1000, 3);

        robot.LaserTrackingSearchStop();

        robot.MoveToLaserSeamPos(1, 30, 0, 0, 0, offdese);


        robot.CloseRPC();
    }

    public static void testLaserRecordAndReplay(Robot robot)
    {
//        //上传并加载开放协议文件
//        robot.OpenLuaUpload("D://zUP/CtrlDev_laser_ruiniu-0117.lua");
//        robot.Sleep(2000);
//        robot.SetCtrlOpenLUAName(0, "CtrlDev_laser_ruiniu-0117.lua");
//        robot.UnloadCtrlOpenLUA(0);
//        robot.LoadCtrlOpenLUA(0);
//        robot.Sleep(8000);

        for (int i=0;i<3;++i){
            JointPos startjointPos=new JointPos(58.830, -92.757, 86.939, -81.135, -90.548, 26.358);
            DescPose startdescPose=new DescPose(-74.319, -312.541, 39.168, 177.512, -1.843, 122.527);
            ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
            DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
            robot.MoveL(startjointPos, startdescPose, 1, 0, 50, 100, 100, -1, 0,exaxisPos, 0, 0, offdese, 100,0,1, 1);

            robot.LaserSensorRecord1(2, 10);

            JointPos endjointPos=new JointPos(76.229, -78.219, 71.540, -82.615, -88.277, 42.332);
            DescPose enddescPose=new DescPose(17.298, -408.461, 40.967, 178.317, 0.798, 123.875);
            robot.MoveL(endjointPos, enddescPose, 1, 0, 10, 100, 100, -1,0, exaxisPos, 0, 0, offdese,100, 0,1, 1);

            robot.LaserSensorRecord1(0, 10);

            robot.MoveToLaserRecordStart(1, 30);

            robot.LaserSensorReplay(10, 100);

            robot.MoveLTR();

            robot.LaserSensorRecord1(0, 10);
        }

        robot.CloseRPC();
    }


    public static void testLasertrack(Robot robot)
    {
        //上传并加载开放协议文件
//        robot.OpenLuaUpload("D://zUP/CtrlDev_laser_ruiniu-0117.lua");
//        robot.Sleep(2000);
//        robot.SetCtrlOpenLUAName(0, "CtrlDev_laser_ruiniu-0117.lua");
//        robot.UnloadCtrlOpenLUA(0);
//        robot.LoadCtrlOpenLUA(0);
//        robot.Sleep(8000);
        for(int i=0;i<10;++i){
            JointPos startjointPos=new JointPos(58.830, -92.757, 86.939, -81.135, -90.548, 26.358);
            DescPose startdescPose=new DescPose(-74.319, -312.541, 39.168, 177.512, -1.843, 122.527);
            ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
            DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
            DescTran directionPoint=new DescTran();
            robot.MoveL(startjointPos, startdescPose, 1, 0, 100, 100, 100, -1, 0,exaxisPos, 0, 0, offdese, 100,0,1, 1);

            robot.LaserTrackingSearchStart_xyz(0, 100, 300, 1000, 2);
            robot.LaserTrackingSearchStop();

            //robot.GetRobotTeachingPoint(name, data);
            robot.MoveToLaserSeamPos(1, 30, 0, 0, 0, offdese);
            //printf("%f, %f, %f,%f, %f, %f,%f, %f, %f,%f, %f, %f\n", data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11]);

            robot.LaserTrackingTrackOnOff(1, 2);
            //robot.LaserTrackingTrackOn(3);
            JointPos endjointPos=new JointPos(76.229, -78.219, 71.540, -82.615, -88.277, 42.332);
            DescPose enddescPose=new DescPose(17.298, -408.461, 40.967, 178.317, 0.798, 123.875);
            robot.MoveL(endjointPos, enddescPose, 1, 0, 10, 100, 100, -1, 0,exaxisPos, 0, 0, offdese, 100,0,1, 1);

            robot.LaserTrackingTrackOnOff(0, 2);
            System.out.println("当前是第"+(i+1)+"次");
        }
        robot.CloseRPC();
    }

    public static void testLasertrack_xyz(Robot robot)
    {
        JointPos startjointPos=new JointPos(56.205, -117.951, 141.872, -118.149, -94.217, -122.176);
        DescPose startdescPose=new DescPose(-97.552, -282.855, 26.675, 174.182, -1.338, -91.707);
        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
        DescTran directionPoint;
        robot.MoveL(startjointPos, startdescPose, 1, 0, 100, 100, 100, -1, 0,exaxisPos, 0, 0, offdese,100, 0,1, 1);

        robot.LaserTrackingSearchStart_xyz(3, 100, 300, 1000, 3);

        robot.LaserTrackingSearchStop();

        robot.MoveToLaserSeamPos(1, 30, 0, 0, 0, offdese);


        robot.CloseRPC();
    }

    public static void TestDragSwitchDetect(Robot robot)
    {
        int rtn = robot.SetTorqueDetectionSwitch(1);
        System.out.println("SetTorqueDetectionSwitch rtn : "+ rtn);
        rtn = robot.DragTeachSwitch(1);
        System.out.println("DragTeachSwitch in rtn : "+ rtn);

        robot.Sleep(1000);

        rtn = robot.DragTeachSwitch(0);
        System.out.println("DragTeachSwitch out rtn : "+ rtn);

        int[] maincode=new int[1];
        int[] subcode=new int[1];

        while (true)
        {
            robot.GetRobotErrorCode(maincode, subcode);
            System.out.println("robot maincode is "+maincode[0]+";  subcode is "+ subcode[0]);
            robot.Sleep(1000);
        }

//        robot.CloseRPC();
    }

    public static void TestExtAxisStrategy(Robot robot)
    {
        JointPos joint_pos1 =new JointPos( -22.016, -49.217, 124.714, -161.100, -85.108, -0.333);
        JointPos joint_pos2 = new JointPos( -21.083, -46.613, 110.079, -147.796, -80.757, -0.330 );
        JointPos joint_pos3 = new JointPos(-25.572, -60.090, 135.397, -163.889, -82.489, -0.345);
        DescPose desc_pos1 = new DescPose( 2.637, -0.001, 30.673, 178.786, -4.134, 68.326);
        DescPose desc_pos2 =new DescPose( 213.812, -1.440, 47.311, 177.410, 0.166, 68.946);
        DescPose desc_pos3 = new DescPose( 444.342, -12.723, 82.470, -177.701, -1.325, 65.151);

        ExaxisPos epos1 = new ExaxisPos( 0.001, 0.000, 0.000, 0.000 );
        ExaxisPos epos2 = new ExaxisPos( 299.977, 0.000, 0.000, 0.000 );
        ExaxisPos epos3 = new ExaxisPos(399.969, 0.000, 0.000, 0.000 );
        DescPose offset_pos = new DescPose( 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 );

        int rtn = robot.SetExAxisRobotPlan(0);
        System.out.println("SetExAxisRobotPlan rtn is "+ rtn);
        robot.Sleep(1000);
        rtn = robot.ExtAxisSyncMoveL(joint_pos1, desc_pos1, 1, 0, 100, 100, 100, -1, epos1, 0, offset_pos);
        System.out.println("ExtAxisSyncMoveL 1 rtn is "+ rtn);
        rtn = robot.ExtAxisSyncMoveL(joint_pos2, desc_pos2, 1, 0, 100, 100, 100, -1, epos2, 0, offset_pos);
        System.out.println("ExtAxisSyncMoveL 2 rtn is "+ rtn);
        rtn = robot.ExtAxisSyncMoveL(joint_pos3, desc_pos3, 1, 0, 100, 100, 100, -1, epos3, 0, offset_pos);
        System.out.println("ExtAxisSyncMoveL 3 rtn is "+ rtn);
        robot.Sleep(8000);
        robot.CloseRPC();
    }

    public static void TestMovePhy(Robot robot)
    {
        JointPos j1=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos j2=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);
        JointPos j3=new JointPos(-29.777, -84.536, 109.275, -114.075, -86.655, 74.257);
        JointPos j4=new JointPos(-31.154, -95.317, 94.276, -88.079, -89.740, 74.256);
        DescPose desc_pos1=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose desc_pos2=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);
        DescPose desc_pos3=new DescPose(-487.434, 154.362, 308.576, 176.600, 0.268, -14.061);
        DescPose desc_pos4=new DescPose(-443.165, 147.881, 480.951, 179.511, -0.775, -15.409);
        DescPose desc_pos5=new DescPose(-385.268, -386.759, 238.349, 179.619, -2.046, 162.332);
        DescPose desc_pos6=new DescPose(-257.470, -566.986, 241.908, -177.038, -2.886, -176.577);
        DescPose desc_pos7=new DescPose(-190.925, -390.644, 240.374, 179.089, 0.019, 177.836);
        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 200.0;
        double ovl = 100.0;
        double blendT = -1.0;
        double blendR = -1.0;
        int flag = 0;
        int search = 0;

        robot.SetSpeed(20);

        int rtn = robot.MoveL(desc_pos1, tool, user, vel, acc, ovl, blendR, 0, epos, search, flag, offset_pos, -1, 1,0,10);
        System.out.println("movel errcode: "+ rtn);

        rtn = robot.MoveC(desc_pos3, tool, user, vel, acc, epos, flag, offset_pos, desc_pos4, tool, user, vel, acc, epos, flag, offset_pos, ovl, blendR, -1, 1);
        System.out.println("movec errcode:"+ rtn);

        rtn = robot.MoveL(desc_pos5, tool, user, vel, acc, ovl, blendR, 0, epos, search, flag, offset_pos, -1, 1, 0 ,10);
        System.out.println("movel errcode:"+ rtn);

        rtn = robot.Circle(desc_pos6, tool, user, vel, acc, epos, desc_pos7, tool, user, vel, acc, epos, ovl, flag, offset_pos, 100, -1, -1, 1);
        System.out.println("circle errcode:"+ rtn);

        robot.CloseRPC();
    }

    public static void testSuckerMove(Robot robot)
    {
        //上传并加载开放协议文件
        //robot.OpenLuaUpload("C：//项目/外设SDK/CtrlDev_sucker.lua");
        robot.Sleep(2000);
        robot.UnloadCtrlOpenLUA(1);
        robot.LoadCtrlOpenLUA(1);
        robot.Sleep(1000);

        JointPos j1=new JointPos(76.558, -81.447, 132.913, -145.499, -92.762, -0.485);
        DescPose desc_pos1=new DescPose(-2.659, -429.194, 170.829, -175.985, -2.789, 166.848);

        JointPos j2=new JointPos(76.559, -90.243, 128.285, -132.076, -92.762, -0.485);
        DescPose desc_pos2=new DescPose(-2.658, -429.198, 241.123, -175.985, -2.789, 166.848);
        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 100.0;
        double blendT = -1.0;
        double blendR = 0.0;
        int flag = 0;
        int search = 0;

        robot.SetSpeed(20);
        int[] ctrl =new int[8];
        int[] state =new int[1];
        int[] pressVlaue =new int[1];
        int[] error =new int[1];
        while (true)
        {
            int rtn = robot.MoveJ(j1, desc_pos1, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);

            //控制吸盘广播模式下，按照最大能力吸附
            ctrl[0] = 2;
            robot.SetSuckerCtrl(0, 1, ctrl);

            //循环监控1号吸盘和12号吸盘的状态
            for (int i = 0; i < 20; i++)
            {
                robot.GetSuckerState(1, state, pressVlaue, error);
                System.out.println("sucker1 state is "+state[0]+", pressVlaue is "+pressVlaue[0]+", error num is "+error[0]);
                robot.GetSuckerState(12, state, pressVlaue, error);
                System.out.println("sucker12 state is "+state[0]+", pressVlaue is "+pressVlaue[0]+", error num is "+error[0]);
                robot.Sleep(100);
            }
            //等待1号吸盘是否为吸附到物体的状态，等待时间100ms
            int ret = robot.WaitSuckerState(1, 1, 100);
            System.out.println("WaitSuckerState result is  "+ ret);
            rtn = robot.MoveJ(j2, desc_pos2, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
            if (ret == 0)
            {
                System.out.println("sucker1 吸附到物体");
            }
            else
            {
                System.out.println("sucker1 未吸附到物体");
                continue;
            }

            //单播模式关闭1号和12号吸盘
            ctrl[0] = 3;
            robot.SetSuckerCtrl(1, 1, ctrl);
            robot.SetSuckerCtrl(12, 1, ctrl);

            robot.Sleep(1000);
        }
    }

    public static void testSucker(Robot robot)
    {
        //上传并加载开放协议文件
        robot.OpenLuaUpload("C：//项目/外设SDK/CtrlDev_sucker.lua");
        robot.Sleep(2000);
        robot.UnloadCtrlOpenLUA(1);
        robot.LoadCtrlOpenLUA(1);
        robot.Sleep(1000);

        //控制吸盘广播模式下，按照最大能力吸附
        int[] ctrl = {1};
        robot.SetSuckerCtrl(0, 1, ctrl);

        int[] state=new int[1];
        int[] pressVlaue=new int[1];
        int[] error=new int[1];

        //循环监控1号吸盘和12号吸盘的状态
        for (int i = 0; i < 100; i++)
        {
            robot.GetSuckerState(1, state,pressVlaue, error);
            System.out.println("sucker1 state is:"+state[0]+",pressVlaue is:"+pressVlaue[0]+",error num is"+error[0]);
            robot.GetSuckerState(12, state, pressVlaue, error);
            System.out.println("sucker12 state is :"+state[0]+", pressVlaue is:"+pressVlaue[0]+",error num is:"+error[0]);
            robot.Sleep(100);
        }

        //等待1号吸盘是否为吸附到物体的状态，等待时间100ms
        int ret = robot.WaitSuckerState(1, 1, 100);
        System.out.println("WaitSuckerState result is:"+ ret);

        //单播模式关闭1号和12号吸盘
        ctrl[0] = 3;
        robot.SetSuckerCtrl(1, 1, ctrl);
        robot.SetSuckerCtrl(12, 1, ctrl);

        robot.CloseRPC();
    }

    public static void testFieldBusBoard(Robot robot)
    {
        //上传并加载开放协议文件
//        robot.OpenLuaUpload("D://zUP/1111/CtrlDev_field.lua");
//        robot.Sleep(2000);
//        robot.SetCtrlOpenLUAName(3, "CtrlDev_field.lua");
//        robot.UnloadCtrlOpenLUA(3);
//        robot.LoadCtrlOpenLUA(3);
//        robot.Sleep(8000);

        int[] type=new int[1];
        int[] version=new int[1];
        int[] connState=new int[1];
        //获取从站板卡的协议类型、软件版本、与PLC的连接状态
        robot.GetFieldBusConfig(type, version, connState);
        System.out.println("type is: "+type[0]+", version is : "+version[0]+", connState is : "+connState[0]);

        //写入DO0 = 1、DO1 = 0、DO2 = 1
        int[] ctrl =new int[8];
        ctrl[0] = 1;
        ctrl[1] = 0;
        ctrl[2] = 1;
        robot.FieldBusSlaveWriteDO(0, 3, ctrl);

        //写入AO2 = 0x1000
        int[] ctrlAO =new int[8];
        ctrlAO[0] = 0x1000;
        robot.FieldBusSlaveWriteAO(2, 1, ctrlAO);

        int[] DI=new int[4];
        double[] AI=new double[3];
        //循环监控DI0~DI3 AI0~AI2
        for (int i = 0; i < 100; i++)
        {
            robot.FieldBusSlaveReadDI(0, 4, DI);
            System.out.println("DI0 is: "+DI[0]+", DI1 is: "+DI[1]+",DI2 is: "+DI[2]+",DI3 is: "+DI[3]);
            robot.FieldBusSlaveReadAI(0, 3, AI);
            System.out.println("AI0 is: "+AI[0]+ ",AI1 is: "+AI[1]+",AI2 is: "+AI[2]);
            robot.Sleep(10);
        }

        //等待DI0是否为1，等待时间100ms，并打印结果
        int ret = robot.FieldBusSlaveWaitDI(0, 1, 100);
        System.out.println("FieldBusSlaveWaitDI result is: "+ ret);

        //等待AI0是否大于400，等待时间100ms，并打印结果
        ret = robot.FieldBusSlaveWaitAI(0,0,400.00,100);
        System.out.println("FieldBusSlaveWaitAI result is: "+ ret);

        robot.CloseRPC();
    }

    public static void testSetSuckerCtrl(Robot robot)
    {
        int[] ctrl=new int[1];
        for (int i = 0; i < 5; i++)
        {
            System.out.println("------------------第  "+(i+1)+"  次测试------------------");
            //控制吸盘广播模式下，按照最大能力吸附
            ctrl[0] = 1;
            robot.SetSuckerCtrl(0, 1, ctrl);
            System.out.println("sucker boardcast start");
            robot.Sleep(3000);
            ctrl[0] = 3;
            robot.SetSuckerCtrl(0, 1, ctrl);
            System.out.println("sucker boardcast stop");
            robot.Sleep(3000);
            //单播模式测试控制吸盘，按设定值吸附
            ctrl[0] = 2;
            robot.SetSuckerCtrl(1, 1, ctrl);
            robot.SetSuckerCtrl(12, 1, ctrl);
            System.out.println("sucker unicast start");
            robot.Sleep(2000);
            ctrl[0] = 3;
            robot.SetSuckerCtrl(1, 1, ctrl);
            robot.SetSuckerCtrl(12, 1, ctrl);
            System.out.println("sucker unicast stop");
            robot.Sleep(2000);
        }
        robot.CloseRPC();
    }

    public static void testGetSuckerState(Robot robot)
    {
        //控制吸盘广播模式下，按照最大能力吸附
        int[] ctrl = new int[1];
        ctrl[0] = 1;
        robot.SetSuckerCtrl(0, 1, ctrl);

        int[] state=new int[1];
        int[] pressVlaue=new int[1];
        int[] error=new int[1];

        //循环监控1号吸盘和12号吸盘的状态
        for (int i = 0; i < 100; i++)
        {
            robot.GetSuckerState(1, state, pressVlaue, error);
            System.out.println("sucker1 state is: "+state[0]+", pressVlaue is: "+pressVlaue[0]+", error num is: "+error[0]);
            robot.GetSuckerState(12, state, pressVlaue, error);
            System.out.println("sucker12 state is: "+state[0]+", pressVlaue is: "+pressVlaue[0]+", error num is: "+error[0]);
            robot.Sleep(100);
        }

        //单播模式关闭1号和12号吸盘
        ctrl[0] = 3;
        robot.SetSuckerCtrl(0, 1, ctrl);
        robot.CloseRPC();
    }

    public static void testWaitSuckerState(Robot robot)
    {
        int[] ctrl=new int[20];
        int[] state=new int[1];
        int[] pressVlaue=new int[1];
        int[] error=new int[1];

        //控制吸盘广播模式下，按照最大能力吸附
        ctrl[0] = 1;
        robot.SetSuckerCtrl(0, 1, ctrl);

        //循环监控1号吸盘和12号吸盘的状态
        for (int i = 0; i < 100; i++)
        {
            robot.GetSuckerState(1, state, pressVlaue, error);
            System.out.println("sucker1 state is "+ state[0]);
            robot.GetSuckerState(12, state, pressVlaue, error);
            System.out.println("sucker12 state is "+ state[0]);
            robot.Sleep(100);
        }

        //等待1号吸盘是否为吸附到物体的状态，等待时间100ms
        int ret = robot.WaitSuckerState(1, 1, 100);
        System.out.println("WaitSuckerState1 result is  "+ ret);
        //等待12号吸盘是否为吸附到物体的状态，等待时间100ms
        ret = robot.WaitSuckerState(12, 1, 100);
        System.out.println("WaitSuckerState12 result is  "+ ret);
        ctrl[0] = 3;
        robot.SetSuckerCtrl(0, 1, ctrl);

        robot.CloseRPC();
    }

    public static void  testGetFieldBusConfig(Robot robot)
    {
        int[] type=new int[1];
        int[] version=new int[1];
        int[] connState=new int[1];

        //获取从站板卡的协议类型、软件版本、与PLC的连接状态
        robot.GetFieldBusConfig(type, version, connState);
        System.out.println("type is "+type[0]+", version is "+version[0]+",connState is "+ connState[0]);

        robot.CloseRPC();
    }

    public static void testFieldBusSlaveWriteDOAO(Robot robot)
    {
        int[] type=new int[1];
        int[] version=new int[1];
        int[] connState=new int[1];
        int[] ctrl=new int[8];
        int[] ctrlAO=new int[8];

        //写入DO0 = 1、DO1 = 1、DO2 = 0
        ctrl[0] = 1;
        ctrl[1] = 1;
        ctrl[2] = 0;
        ctrl[3] = 1;
        robot.FieldBusSlaveWriteDO(0, 4, ctrl);

        //写入AO2 = 0x1001
        ctrlAO[0] = 0x1002;
        robot.FieldBusSlaveWriteAO(2, 1, ctrlAO);

        robot.CloseRPC();
    }

    public static void testFieldBusSlaveReadDIAI_waitDIAI(Robot robot)
    {
        int[] type=new int[1];
        int[] version=new int[1];
        int[] connState=new int[1];
        int[] ctrl=new int[8];
        int[] ctrlAO=new int[8];
        int[] DI=new int[8];
        double[] AI=new double[8];


        //循环监控DI0~DI3 AI0~AI2
        for (int i = 0; i < 100; i++)
        {
            robot.FieldBusSlaveReadDI(0, 4, DI);
            System.out.println("DI0 is "+DI[0]+", DI1 is "+DI[1]+",DI2 is "+DI[2]+",DI3 is "+DI[3]);
            robot.FieldBusSlaveReadAI(0, 3, AI);
            System.out.println("AI0 is "+AI[0]+", AI1 is "+AI[1]+",AI2 is "+ AI[2]);
            robot.Sleep(100);
        }

        //等待DI0是否为1，等待时间100ms，并打印结果
        int ret = robot.FieldBusSlaveWaitDI(0, 1, 100);
        System.out.println("FieldBusSlaveWaitDI result is  "+ ret);

        //等待AI0是否大于400，等待时间100ms，并打印结果
        ret = robot.FieldBusSlaveWaitAI(0, 0, 400.00, 100);
        System.out.println("FieldBusSlaveWaitAI result is "+ ret);

        robot.CloseRPC();
    }

    public static void TestExtAxisSyncMove(Robot robot)
    {
        JointPos joint_safe = new JointPos(116.142, -83.505, 53.915, -64.393, -87.817, -49.868);
        JointPos joint_pos1 = new JointPos(107.108, -93.262, 90.994, -86.002, -89.095, -50.897 );
        JointPos joint_pos2 = new JointPos(101.428, -60.890, 57.914, -63.062, -85.489, -50.897 );
        JointPos joint_pos3 = new JointPos(124.486, -97.485, 95.883, -90.374, -91.062, -49.860 );
        JointPos joint_pos4 = new JointPos(113.734, -76.571, 78.254, -93.291, -91.100, -49.867 );
        JointPos joint_pos5 = new JointPos(116.544, -63.563, 63.923, -88.894, -83.999, -49.869 );

        DescPose desc_safe =new DescPose(334.834, -416.117, 377.662, -175.764, -1.640, -103.974 );
        DescPose desc_pos1 =new DescPose( 238.956, -408.157, 189.101, 179.611, 1.912, -112.015 );
        DescPose desc_pos2 =new DescPose( 236.316, -495.487, 138.006, 167.595, 21.130, -120.956 );
        DescPose desc_pos3 =new DescPose( 335.706, -321.654, 188.246, -179.537, -2.195, -95.681 );
        DescPose desc_pos4 =new DescPose( 330.540, -517.439, 156.875, -179.804, -1.939, -106.418);
        DescPose desc_pos5 =new DescPose( 430.747, -540.471, 129.780, -176.351, 4.987, -103.505 );

        ExaxisPos eposSafe = new ExaxisPos(-0.000, 0.000, 0.000, 0.000);
        ExaxisPos epos1 =new ExaxisPos( -0.000, 0.000, 0.000, 0.000 );
        ExaxisPos epos2 = new ExaxisPos( -24.972, 0.000, 0.000, 0.000 );
        ExaxisPos epos3 = new ExaxisPos( 14.982, 0.000, 0.000, 0.000 );
        ExaxisPos epos4 = new ExaxisPos( -10.026,0.000,0.000,0.000 );
        ExaxisPos epos5 = new ExaxisPos( -20.015, 0.000, 0.000, 0.000 );

        DescPose offset_pos =new DescPose( 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int tool = 1, user = 0, vel = 100, acc = 100, ovl = 100, blendT = -1;
        robot.MoveJ(joint_safe, tool, user, vel, acc, ovl, eposSafe, blendT, 0, offset_pos);
        robot.ExtAxisMove(eposSafe, 100, -1);

        int rtn = robot.ExtAxisSyncMoveJ(joint_pos1, tool, user, vel, acc, ovl, epos1, blendT, 0, offset_pos);
        rtn = robot.ExtAxisSyncMoveJ(joint_pos2, tool, user, vel, acc, ovl, epos2, blendT, 0, offset_pos);
        System.out.println("ExtAxisSyncMoveJ  rtn is:"+ rtn);

        robot.MoveJ(joint_safe, tool, user, vel, acc, ovl, eposSafe, blendT, 0, offset_pos);
        robot.ExtAxisMove(eposSafe, 100, -1);
        rtn = robot.ExtAxisSyncMoveJ(joint_pos1, tool, user, vel, acc, ovl, epos1, blendT, 0, offset_pos);
        rtn = robot.ExtAxisSyncMoveL(desc_pos3, tool, user, vel, acc, ovl, -1, epos3, 0, offset_pos,-1);
        System.out.println("ExtAxisSyncMoveL  rtn is:"+ rtn);

        robot.MoveJ(joint_safe, tool, user, vel, acc, ovl, eposSafe, blendT, 0, offset_pos);
        robot.ExtAxisMove(eposSafe, 100, -1);
        rtn = robot.ExtAxisSyncMoveJ(joint_pos1, tool, user, vel, acc, ovl, epos1, blendT, 0, offset_pos);
        rtn = robot.ExtAxisSyncMoveC(desc_pos4, tool, user, vel, acc, epos4, 0, offset_pos, desc_pos5, tool, user, vel, acc, epos5, 0, offset_pos, ovl, -1,-1);
        System.out.println("ExtAxisSyncMoveC  rtn is:"+ rtn);

        robot.CloseRPC();
    }

    public static void TestMove100(Robot robot)
    {
        int num=0;
        while(num<100){
            TestMoveJ(robot);
            TestMoveL(robot);
            TestMoveC(robot);
            TestCircle1(robot);
            TestSpiral(robot);//笛卡尔空间螺旋线运动
            TestSpline(robot);//关节空间样条运动
            TestNewSpline(robot);//新样条指令点
            ++num;
            System.out.println("当前运行次数:"+num);
        }
    }

    public static void TestNewMove(Robot robot)
    {
        JointPos j1 = new JointPos(-44.185,-95.599,102.888,-100.999,-90.04,-54.095);
        JointPos j2 = new JointPos(39.128,-95.532,102.739,-101.114,-90.038,-54.095);

        DescPose desc_pos1 = new DescPose(-399.733,246.791,430.238,-177.855,-3.027,99.853);
        DescPose descPos = new DescPose(-443.453,453.469,197.678,-179.228,-0.561,89.187);
        DescPose desc_pos2 = new DescPose(-292.353, -368.834, 431.221, -177.737, -3.186, -176.836);

        DescPose desc_pos3 = new DescPose(-599.313,-314.327,365.756,179.445,0.718,163.133);
        DescPose desc_pos4 = new DescPose(-430.217,-83.687,370.316,177.476,3.436,141.619);

        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 100.0;
        double blendT = 0.0;
        double blendR = 0.0;

        int blendMode = 0;
        int flag = 0;
        double oacc = 0.0;
        int search = 0;
        int config = -1;
        int rtn;
        rtn = robot.MoveJ(j2, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        System.out.println("MoveJ errcode:"+rtn);

        rtn = robot.MoveL(desc_pos1, tool, user, vel, acc, ovl, blendR, blendMode, epos, search, flag, offset_pos, config,0,10);
        System.out.println("MoveL errcode:"+rtn);

        rtn = robot.MoveL(descPos, tool, user, vel, acc, ovl, blendR, blendMode, epos, search, flag, offset_pos, config,0,10);
        System.out.println("MoveL errcode:"+rtn);

        rtn = robot.MoveC(desc_pos1, tool, user, vel, acc, epos, flag, offset_pos, desc_pos2, tool, user, vel, acc, epos, flag, offset_pos, ovl, blendR, config);
        System.out.println("MoveC errcode:"+rtn);

        rtn = robot.Circle(desc_pos3, tool, user, vel, acc, epos, desc_pos4, tool, user, vel, acc, epos, ovl, flag, offset_pos, oacc, blendR, config);
        System.out.println("Circle errcode:"+rtn);
    }

    public static void TestMoveJ(Robot robot)
    {
        JointPos j1 = new JointPos(-44.185,-95.599,102.888,-100.999,-90.04,-54.095);
        JointPos j2 = new JointPos(39.128,-95.532,102.739,-101.114,-90.038,-54.095);
        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 100.0;
        double blendT = 0.0;
        double blendR = 0.0;

        int blendMode = 0;
        int flag = 0;
        double oacc = 0.0;
        int search = 0;
        int config = -1;
        int rtn;
        rtn = robot.MoveJ(j2, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        System.out.println("MoveJ errcode:"+rtn);
    }

    public static void TestMoveL(Robot robot)
    {
        DescPose desc_pos1 = new DescPose(-399.733,246.791,430.238,-177.855,-3.027,99.853);
        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 100.0;
        double blendT = 0.0;
        double blendR = 0.0;

        int blendMode = 0;
        int flag = 0;
        int search = 0;
        int config = -1;
        int rtn;

        rtn = robot.MoveL(desc_pos1, tool, user, vel, acc, ovl, blendR, blendMode, epos, search, flag, offset_pos, config,0,10);
        System.out.println("MoveL errcode:"+rtn);
    }

    public static void TestMoveC(Robot robot)
    {
        DescPose descPos = new DescPose(-443.453,453.469,197.678,-179.228,-0.561,89.187);
        DescPose desc_pos1 = new DescPose(-399.733, 246.791, 430.238, -177.855, -3.027, 99.853);
        DescPose desc_pos2 = new DescPose(-292.353, -368.834, 431.221, -177.737, -3.186, -176.836);

        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 100.0;
        double blendT = 0.0;
        double blendR = 0.0;

        int blendMode = 0;
        int flag = 0;
        int search = 0;
        int config = -1;
        int rtn;

        rtn = robot.MoveL(descPos, tool, user, vel, acc, ovl, blendR, blendMode, epos, search, flag, offset_pos, config,0,10);
        System.out.println("MoveL errcode:"+rtn);

        rtn = robot.MoveC(desc_pos1, tool, user, vel, acc, epos, flag, offset_pos, desc_pos2, tool, user, vel, acc, epos, flag, offset_pos, ovl, blendR, config);
        System.out.println("MoveC errcode:"+rtn);

    }
    public static void TestCircle1(Robot robot)
    {
        DescPose desc_pos1 = new DescPose(-599.313,-314.327,365.756,179.445,0.718,163.133);
        DescPose desc_pos2 = new DescPose(-430.217,-83.687,370.316,177.476,3.436,141.619);

        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 100.0;
        double blendT = 0.0;
        double blendR = 0.0;
        double oacc = 0.0;

        int blendMode = 0;
        int flag = 0;
        int search = 0;
        int config = -1;
        int rtn;

        rtn = robot.Circle(desc_pos1, tool, user, vel, acc, epos, desc_pos2, tool, user, vel, acc, epos, ovl, flag, offset_pos, oacc, blendR, config);
        System.out.println("Circle errcode:"+rtn);
    }

    public static void  test_LaserRecordPoint(Robot robot)
    {
        DescTran directionPoint = new DescTran( 0,0,0 );
        int rtn = robot.LaserTrackingSearchStart(2, directionPoint, 30, 100, 10000, 4);
        System.out.println("LaserTrackingSearchStart rtn is:"+ rtn);
        robot.LaserTrackingSearchStop();
        int coordID = 4;
        DescPose desc =new DescPose(0,0,0,0,0,0);
        JointPos joint = new JointPos(0,0,0,0,0,0);
        ExaxisPos exaxis =new ExaxisPos(0,0,0,0);
        rtn = robot.LaserRecordPoint(coordID, desc, joint, exaxis);
        System.out.println("rtn is:"+ rtn);
        System.out.println("desc_pos:"+ desc.tran.x+","+ desc.tran.y+","+ desc.tran.z+","+ desc.rpy.rx+","+ desc.rpy.ry+","+ desc.rpy.rz);
        System.out.println("joint_pos:"+ joint.J1+","+ joint.J2+","+ joint.J3+","+ joint.J4+","+ joint.J5+","+ joint.J6);
        System.out.println("exaxis pos is:"+ exaxis.axis1+","+ exaxis.axis2+","+ exaxis.axis3+","+ exaxis.axis4);
        DescPose off = new DescPose(0,0,0,0,0,0);
        robot.MoveJ(joint,desc,3, 0, 100, 100, 50, exaxis, -1, 0, off);
        robot.CloseRPC();
    }

    public static void TestFirmWareUpgrade2(Robot robot)
    {
        robot.RobotEnable(0);
        robot.Sleep(200);
        int rtn = robot.JointAllParamUpgrade("D://zUP/standardQX/jointallparametersFR56.0.db");
        System.out.println("robot JointAllParamUpgrade rtn is:"+ rtn);

        rtn = robot.SetCtrlFirmwareUpgrade(2, "D://zUP/upgrade/FAIR_Cobot_Cbd_Asix_V2.0.bin");
        System.out.println("robot SetCtrlFirmwareUpgrade config param rtn is:"+ rtn);

        rtn = robot.SetEndFirmwareUpgrade(2, "D://zUP/upgrade/FAIR_Cobot_Axle_Asix_V2.4.bin");
        System.out.println("robot SetEndFirmwareUpgrade config param rtn is:"+ rtn);

        robot.SetSysServoBootMode();
        rtn = robot.SetCtrlFirmwareUpgrade(1, "D://zUP/standardQX/FR_CTRL_PRIMCU_FV201010_MAIN_U4_T01_20240529.bin");
        System.out.println("robot SetCtrlFirmwareUpgrade rtn is:"+ rtn);

        rtn = robot.SetEndFirmwareUpgrade(1, "D://zUP/standardQX/FR_END_FV201010_MAIN_U01_T01_20250522.bin");
        System.out.println("robot SetEndFirmwareUpgrade rtn is:"+ rtn);

        rtn = robot.SetJointFirmwareUpgrade(1, "D://zUP/standardQX/FR_SERVO_FV502211_MAIN_U7_T07_20250217.bin");
        System.out.println("robot SetJointFirmwareUpgrade rtn is:"+ rtn);

        robot.CloseRPC();
    }

    public static void TestFirmWareUpgrade3(Robot robot)
    {
        robot.RobotEnable(0);
        robot.Sleep(200);
        int rtn = robot.JointAllParamUpgrade("D://zUP/standardQX/jointallparametersFR56.0.db");
        System.out.println("robot JointAllParamUpgrade rtn is:"+ rtn);

        rtn = robot.SetCtrlFirmwareUpgrade(2, "D://zUP/upgrade/FAIR_Cobot_Cbd_Asix_V2.0.bin");
        System.out.println("robot SetCtrlFirmwareUpgrade config param rtn is:"+ rtn);

        rtn = robot.SetEndFirmwareUpgrade(2, "D://zUP/upgrade/FAIR_Cobot_Axle_Asix_V2.4.bin");
        System.out.println("robot SetEndFirmwareUpgrade config param rtn is:"+ rtn);

        robot.SetSysServoBootMode();
        rtn = robot.SetCtrlFirmwareUpgrade(1, "D://zUP/standardQX/FR_CTRL_PRIMCU_FV201011_MAIN_U4_T01_20250208.bin");
        System.out.println("robot SetCtrlFirmwareUpgrade rtn is:"+ rtn);

        rtn = robot.SetEndFirmwareUpgrade(1, "D://zUP/standardQX/FR_END_FV201008_MAIN_U01_T01_20250416.bin");
        System.out.println("robot SetEndFirmwareUpgrade rtn is:"+ rtn);

        rtn = robot.SetJointFirmwareUpgrade(1, "D://zUP/standardQX/FR_SERVO_FV502211_MAIN_U7_T07_20250217.bin");
        System.out.println("robot SetJointFirmwareUpgrade rtn is:"+ rtn);

        robot.CloseRPC();
    }

    public static void TestExtAxisMoveBlend(Robot robot)
    {
        JointPos joint_pos1 =new JointPos(-68.732, -99.773, -77.729, -77.167, 100.772, -13.317);
        JointPos joint_pos2 =new JointPos( -101.678, -102.823, -77.512, -77.185, 88.388, -13.317 );
        JointPos joint_pos3 =new JointPos( -129.905, -99.715, -71.965, -77.209, 81.678, -13.317 );
        DescPose desc_pos1 =new DescPose( 103.887, -434.739, 244.938, -162.495, 6.575, -142.948 );
        DescPose desc_pos2 = new DescPose(-196.883, -418.054, 218.942, -168.196, -4.388, -178.991);
        DescPose desc_pos3 = new DescPose(-396.665, -265.695, 284.380, -160.913, -12.378, 149.770);

        ExaxisPos epos1 = new ExaxisPos(0.000, 6.996, 0.000, 0.000);
        ExaxisPos epos2 = new ExaxisPos(0.000, 20.987, 0.000, 0.000);
        ExaxisPos epos3 =new ExaxisPos(-0.000, 30.982, 0.000, 0.000);

        DescPose offset_pos = new DescPose( 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 );
        int rtn;
         rtn= robot.AccSmoothStart(true);
        System.out.println("AccSmoothStart rtn is:"+ rtn);
        robot.Sleep(1000);
        rtn = robot.ExtAxisSyncMoveL(joint_pos1, desc_pos1, 1, 0, 100, 100, 100, 100, epos1, 0, offset_pos);
        System.out.println("ExtAxisSyncMoveL 1 rtn is: "+ rtn);
        rtn = robot.ExtAxisSyncMoveL(joint_pos2, desc_pos2, 1, 0, 100, 100, 100, 200, epos2, 0, offset_pos);
        System.out.println("ExtAxisSyncMoveL 2 rtn is: "+ rtn);
        rtn = robot.ExtAxisSyncMoveL(joint_pos3, desc_pos3, 1, 0, 100, 100, 100, 300, epos3, 0, offset_pos);
        System.out.println("ExtAxisSyncMoveL 3 rtn is: "+ rtn);
        robot.Sleep(8000);
        rtn = robot.AccSmoothEnd(true);
        System.out.println("AccSmoothEnd rtn is: "+ rtn);

        robot.CloseRPC();
    }

    public static int TestFirmWareUpgrade1(Robot robot)
    {
        robot.RobotEnable(0);
        robot.Sleep(200);
        int rtn=-1;
//        rtn = robot.JointAllParamUpgrade("D://zUP/upgrade/jointallparameters.db");
//        System.out.println("robot JointAllParamUpgrade rtn is: "+ rtn);
//
//        rtn = robot.SetCtrlFirmwareUpgrade(2, "D://zUP/upgrade/FAIR_Cobot_Cbd_Asix_V2.0.bin");
//        System.out.println("robot SetCtrlFirmwareUpgrade config param rtn is: "+ rtn);
//
//        rtn = robot.SetEndFirmwareUpgrade(2, "D://zUP/upgrade/FAIR_Cobot_Axle_Asix_V2.4.bin");
//        System.out.println("robot SetEndFirmwareUpgrade config param rtn is: "+ rtn);

        robot.SetSysServoBootMode();
        rtn = robot.SetCtrlFirmwareUpgrade(1, "D://zUP/upgrade/FR_CTRL_PRIMCU_FV201013_MAIN_U4_T01_20260424.bin");
        System.out.println("robot SetCtrlFirmwareUpgrade rtn is: "+ rtn);

        rtn = robot.SetEndFirmwareUpgrade(1, "D://zUP/upgrade/FR_END_FV201013_MAIN_U1_T01_20260407.bin");
        System.out.println("robot SetEndFirmwareUpgrade rtn is: "+ rtn);

        rtn = robot.SetJointFirmwareUpgrade(1, "D://zUP/upgrade/FR_SERVO_FV504316_MAIN_U7_T07_20250715.bin");
        System.out.println("robot SetJointFirmwareUpgrade rtn is: "+ rtn);

        return 0;
    }

    public static int TestFirmWareUpgrade(Robot robot)
    {
        robot.SetSysServoBootMode();
//        robot.RobotEnable(0);
        robot.Sleep(200);
        int rtn=-1;
        rtn = robot.SetCtrlFirmwareUpgrade(2, "D://zUP/2025_07_09_FAIR_Cobot_Axle_Asix_V2.4/FAIR_Cobot_Cbd_Asix_V2.0.bin");
//        rtn = robot.SetCtrlFirmwareUpgrade(1, "D://zUP/FR_CTRL_PRIMCU_FV201011_MAIN_U4_T01_20250208.bin");

        System.out.println("robot SetCtrlFirmwareUpgrade rtn is:"+ rtn);

        rtn = robot.SetEndFirmwareUpgrade(2, "D://zUP/2025_07_09_FAIR_Cobot_Axle_Asix_V2.4/FAIR_Cobot_Axle_Asix_V2.4.bin");
        System.out.println("robot SetEndFirmwareUpgrade rtn is :"+ rtn);

//        rtn = robot.SetJointFirmwareUpgrade(1, "D://zUP/FR_SERVO_FV502211_MAIN_U7_T07_20250217.bin");
//        System.out.println("robot SetJointFirmwareUpgrade rtn is:"+ rtn);

//        rtn = robot.JointAllParamUpgrade("D://zUP/全参数配置文件-V6.0-FR3MT-10010-20250428/jointallparameters.db");
//        rtn = robot.JointAllParamUpgrade("D://zUP/20250603/jointallparameters.db");

//        System.out.println("robot JointAllParamUpgrade rtn is: "+ rtn);

        robot.CloseRPC();
        return 0;
    }

    public static void TestFocus(Robot robot){
        DescPose p1Desc=new DescPose(186.331, 487.913, 209.850, 149.030, 0.688, -114.347);
        JointPos p1Joint = new JointPos(-127.876, -75.341, 115.417, -122.741, -59.820, 74.300);

        DescPose p2Desc = new DescPose(69.721, 535.073, 202.882, -144.406, -14.775, -89.012);
        JointPos p2Joint = new JointPos(-101.780, -69.828, 110.917, -125.740, -127.841, 74.300);

        DescPose p3Desc = new DescPose(146.861, 578.426, 205.598, 175.997, -36.178, -93.437);
        JointPos p3Joint = new JointPos(-112.851, -60.191, 86.566, -80.676, -97.463, 74.300);

        DescPose p4Desc = new DescPose(136.284, 509.876, 225.613, 178.987, 1.372, -100.696);
        JointPos p4Joint = new JointPos(-116.397, -76.281, 113.845, -128.611, -88.654, 74.299);

        DescPose p5Desc = new DescPose(138.395, 505.972, 298.016, 179.134, 2.147, -101.110);
        JointPos p5Joint = new JointPos(-116.814, -82.333, 109.162, -118.662, -88.585, 74.302);

        DescPose p6Desc = new DescPose(105.553, 454.325, 232.017, -179.426, 0.444, -99.952);
        JointPos p6Joint = new JointPos(-115.649, -84.367, 122.447, -128.663, -90.432, 74.303);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 100, 0, 0, 0);

        robot.GetForwardKin(p1Joint, p1Desc);
        robot.GetForwardKin(p2Joint,  p2Desc);
        robot.GetForwardKin(p3Joint,  p3Desc);
        robot.GetForwardKin(p4Joint,  p4Desc);
        robot.GetForwardKin(p5Joint,  p5Desc);
        robot.GetForwardKin(p6Joint,  p6Desc);

        robot.MoveJ(p1Joint, p1Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(1);
        robot.MoveJ(p2Joint, p2Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(2);
        robot.MoveJ(p3Joint, p3Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(3);
        robot.MoveJ(p4Joint, p4Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(4);

        DescPose coordRtn = new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int rtn = robot.ComputeTcp4( coordRtn);

        robot.SetToolCoord(1, coordRtn, 0, 0, 1, 0);

        robot.GetForwardKin(p1Joint, p1Desc);
        robot.GetForwardKin(p2Joint, p2Desc);
        robot.GetForwardKin(p3Joint, p3Desc);

        robot.SetFocusCalibPoint(1, p1Desc);
        robot.SetFocusCalibPoint(2, p2Desc);
        robot.SetFocusCalibPoint(3, p3Desc);

        DescTran resultPos = new DescTran(0.0, 0.0, 0.0);
        double[] accuracy = {0.0};
        rtn = robot.ComputeFocusCalib(3,  resultPos,  accuracy);
        rtn = robot.SetFocusPosition(resultPos);

        robot.GetForwardKin(p5Joint,  p5Desc);
        robot.GetForwardKin(p6Joint,  p6Desc);

        robot.MoveL(p5Joint, p5Desc, 1, 0, 10, 100, 100, -1, 0, exaxisPos, 0, 1, offdese,100,0,1, 1);
        robot.MoveL(p6Joint, p6Desc, 1, 0, 10, 100, 100, -1, 0, exaxisPos, 0, 1, offdese,100,0,1, 1);

        robot.FocusStart(50, 19, 710, 90, 0);
        robot.MoveL(p5Joint, p5Desc, 1, 0, 10, 100, 100, -1, 0, exaxisPos, 0, 1, offdese,100,0,1, 1);
        robot.MoveL(p6Joint, p6Desc, 1, 0, 10, 100, 100, -1, 0, exaxisPos, 0, 1, offdese,100,0,1, 1);
        robot.FocusEnd();
    }
    public static void TestFRT(Robot robot){
        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);
        JointPos JP1 = new JointPos(43.849, -71.535, 109.564, -135.187, -89.016, 18.225);
        DescPose DP1 = new DescPose(-331.581, -462.334, 225.274, -173.501, 3.161, 115.864);
        JointPos JP2 = new JointPos(67.950, -31.106, 58.618, -135.151, -89.017, 18.226);
        DescPose DP2 = new DescPose(-219.871, -819.093, 124.722, -163.475, 6.333, 140.797);
        JointPos JP3 = new JointPos(97.159, -26.141, 53.021, -120.936, -103.329, 18.230);
        DescPose DP3 = new DescPose(183.319, -826.070, 70.807, -171.844, -11.320, 167.645);

        JointPos JP4 = new JointPos(43.849, -71.535, 109.564, -135.187, -89.016, 18.225);
        DescPose DP4 = new DescPose(-331.581, -462.334, 225.274, -173.501, 3.161, 115.864);

        JointPos JP5 = new JointPos(67.950, -31.106, 58.618, -135.151, -89.017, 18.226);
        DescPose DP5 = new DescPose(-219.871, -819.093, 124.722, -163.475, 6.333, 140.797);

        JointPos JP6 = new JointPos(105.694, -125.732, 124.263, -105.860, -90.554, 18.230);
        DescPose DP6 = new DescPose(171.338, -236.287, 442.053, -163.332, 4.843, 178.090);
        robot.LinArcFIRPlanningStart(2000, 10000, 720, 1440);

        robot.MoveL(JP1, DP1, 0, 0, 100, 100, 100, 20, -1, exaxisPos, 0, 0, offdese,0,10);

        robot.MoveL(JP2, DP2, 0, 0, 100, 100, 100, 20, 100, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveL(JP3, DP3, 0, 0, 100, 100, 100, 20, 100, exaxisPos, 0, 0, offdese,0,10);



        robot.LinArcFIRPlanningEnd();

        robot.PtpFIRPlanningStart(240, 1200);

        robot.MoveJ(JP4, DP4, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveJ(JP5, DP5, 0, 0, 100, 100, 100, exaxisPos, 200, 0, offdese);
        robot.MoveJ(JP6, DP6, 0, 0, 100, 100, 100, exaxisPos, 200, 0, offdese);

        robot.PtpFIRPlanningEnd();

        JointPos JP7 = new JointPos(138.430, -103.926, 135.390, -120.507, -116.912, 18.198);
        DescPose DP7 = new DescPose(288.379, -179.924, 267.471, -171.989, -25.794, -151.376);

        JointPos JP8 = new JointPos(122.158, -69.748, 92.480, -120.510, -116.988, 18.175);
        DescPose DP8 = new DescPose(380.357, -498.600, 323.600, -163.066, -22.643, -171.300);
        JointPos JP88 = new JointPos(70.960, -53.189, 85.689, -123.253, -116.780, 18.175);
        DescPose DP88 = new DescPose(-171.581, -671.727, 192.097, -170.274, -25.085, 140.438);

        robot.LinArcFIRPlanningStart(2000, 10000, 720, 1440);
        robot.MoveL(JP7, DP7, 0, 0, 100, 100, 100, 20, 50, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveC(JP8, DP8, 0, 0, 100, 100, exaxisPos, 0, offdese, JP88, DP88, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);
        robot.LinArcFIRPlanningEnd();
        JointPos JP9 = new JointPos(138.430, -103.926, 135.390, -120.507, -116.912, 18.198);
        DescPose DP9 = new DescPose(288.379, -179.924, 267.471, -171.989, -25.794, -151.376);
        JointPos JP10 = new JointPos(122.158, -69.748, 92.480, -120.510, -116.988, 18.175);
        DescPose DP10 = new DescPose(380.357, -498.600, 323.600, -163.066, -22.643, -171.300);

        JointPos JP10_ = new JointPos(70.960, -53.189, 85.689, -123.253, -116.780, 18.175);
        DescPose DP10_ = new DescPose(-171.581, -671.727, 192.097, -170.274, -25.085, 140.438);

        JointPos JP11 = new JointPos(38.619, -93.376, 100.695, -79.572, -116.773, 18.172);
        DescPose DP11 = new DescPose(-305.647, -317.052, 409.820, 169.616, -30.178, 117.509);
        JointPos JP11_ = new JointPos(110.873, -113.738, 126.180, -79.561, -116.964, 18.173);
        DescPose DP11_ = new DescPose(150.549, -235.789, 334.164, 163.763, -31.210, -167.182);
        robot.LinArcFIRPlanningStart(2000, 10000, 720, 1440);
        robot.MoveL(JP9, DP9, 0, 0, 100, 100, 100, 20, -1, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveC(JP10, DP10, 0, 0, 100, 100, exaxisPos, 0, offdese, JP10_, DP10_, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, 120);
        robot.MoveC(JP11, DP11, 0, 0, 100, 100, exaxisPos, 0, offdese, JP11_, DP11_, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);

        robot.LinArcFIRPlanningEnd(); ;
        JointPos JP12 = new JointPos(138.430, -103.926, 135.390, -120.507, -116.912, 18.198);
        DescPose DP12 = new DescPose(288.379, -179.924, 267.471, -171.989, -25.794, -151.376);
        JointPos JP13 = new JointPos(122.158, -69.748, 92.480, -120.510, -116.988, 18.175);
        DescPose DP13 = new DescPose(380.357, -498.600, 323.600, -163.066, -22.643, -171.300);

        JointPos JP13_ = new JointPos(70.960, -53.189, 85.689, -123.253, -116.780, 18.175);
        DescPose DP13_ = new DescPose(-171.581, -671.727, 192.097, -170.274, -25.085, 140.438);
        JointPos JP14 = new JointPos(38.619, -93.376, 100.695, -79.572, -116.773, 18.172);
        DescPose DP14 = new DescPose(-305.647, -317.052, 409.820, 169.616, -30.178, 117.509);
        robot.LinArcFIRPlanningStart(2000, 10000, 720, 1440);
        robot.MoveL(JP12, DP12, 0, 0, 100, 100, 100, 20, -1, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveC(JP13, DP13, 0, 0, 100, 100, exaxisPos, 0, offdese, JP13_, DP13_, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);


        robot.MoveL(JP14, DP14, 0, 0, 100, 100, 100, 20, -1, exaxisPos, 0, 0, offdese,0,10);
        robot.LinArcFIRPlanningEnd();
    }
    public static void TestWideVoltageCtrlBoxtemp(Robot robot)
    {
        robot.SetWideBoxTempFanMonitorParam(1, 2);
        List<Number> list=robot.GetWideBoxTempFanMonitorParam();
        System.out.println("GetWideBoxTempFanMonitorParam enable is:"+list.get(1)+", period is:"+list.get(2));
        ROBOT_STATE_PKG pkg=new ROBOT_STATE_PKG();
        for (int i = 0; i < 100; i++)
        {
            pkg=robot.GetRobotRealTimeState();
            System.out.println("robot ctrl box temp is:"+pkg.wideVoltageCtrlBoxTemp+",fan current is:"+pkg.wideVoltageCtrlBoxFanCurrent);
            robot.Sleep(100);
        }

        int rtn = robot.SetWideBoxTempFanMonitorParam(0, 2);

        list=robot.GetWideBoxTempFanMonitorParam();
        for (int i = 0; i < 100; i++)
        {
            pkg=robot.GetRobotRealTimeState();
            System.out.println("robot ctrl box temp is:"+pkg.wideVoltageCtrlBoxTemp+" ,fan current is:"+pkg.wideVoltageCtrlBoxFanCurrent);
            robot.Sleep(100);
        }

        robot.CloseRPC();
        robot.Sleep(2000);
    }

    public static void DragControl(Robot robot)
    {
        Object[] M = { 15.0, 15.0, 15.0, 0.5, 0.5, 0.1 };
        Object[] B = { 150.0, 150.0, 150.0, 5.0, 5.0, 1.0 };
        Object[] K = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        Object[] F = { 10.0, 10.0, 10.0, 1.0, 1.0, 1.0 };
        int rtn = robot.EndForceDragControl(1, 0, 0, 0, 1, M, B, K, F, 50, 100);
        System.out.println("force drag control start rtn is:"+ rtn);
        robot.Sleep(5000);

        rtn = robot.EndForceDragControl(0, 0, 0, 0, 1, M, B, K, F, 50, 100);
        System.out.println("force drag control end rtn is:"+ rtn);

        rtn = robot.ResetAllError();
        System.out.println("ResetAllError rtn is:"+ rtn);

        robot.EndForceDragControl(1, 0, 0, 0, 1, M, B, K, F, 50, 100);
        System.out.println("force drag control start again rtn is:"+ rtn);
        robot.Sleep(5000);

        rtn = robot.EndForceDragControl(0, 0, 0, 0, 1, M, B, K, F, 50, 100);
        System.out.println("force drag control end again rtn is:"+ rtn);

    }

    public static void TestBlend1(Robot robot)
    {
        ExaxisPos exaxisPos =new ExaxisPos( 0, 0, 0, 0);
        DescPose offdese =new DescPose(0, 0, 0, 0, 0, 0 );

        JointPos JP1 = new JointPos(55.203, -69.138, 75.617, -103.969, -83.549, -0.001);
        DescPose DP1 = new DescPose(0, 0, 0, 0, 0, 0);
        JointPos JP2 = new JointPos(57.646, -61.846, 59.286, -69.645, -99.735, 3.824);
        DescPose DP2 =new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP3 = new JointPos(57.304, -61.380, 58.260, -67.641, -97.447, 2.685);
        DescPose DP3 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP4 =new JointPos(57.297, -61.373, 58.250, -67.637, -97.448, 2.677);
        DescPose DP4 =new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP5 = new JointPos(23.845, -108.202, 111.300, -80.971, -106.753, -30.246);
        DescPose DP5 =new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP6 =new JointPos(23.845, -108.202, 111.300, -80.971, -106.753, -30.246);
        DescPose DP6 = new DescPose(0, 0, 0, 0, 0, 0 );
        robot.GetForwardKin(JP1, DP1);
        robot.GetForwardKin(JP2, DP2);
        robot.GetForwardKin(JP3, DP3);
        robot.GetForwardKin(JP4, DP4);
        robot.GetForwardKin(JP5, DP5);
        robot.GetForwardKin(JP6, DP6);
        robot.MoveJ(JP1, DP1, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveJ(JP2, DP2, 0, 0, 100, 100, 100, exaxisPos, 200, 0,offdese);
        robot.MoveJ(JP3, DP3, 0, 0, 100, 100, 100, exaxisPos, 200, 0,offdese);
        robot.MoveJ(JP4, DP4, 0, 0, 100, 100, 100, exaxisPos, 200, 0,offdese);
        robot.MoveJ(JP5, DP5, 0, 0, 100, 100, 100, exaxisPos, 200, 0,offdese);
        robot.MoveJ(JP6, DP6, 0, 0, 100, 100, 100, exaxisPos, 200, 0,offdese);


        JointPos JP7 =new JointPos( -10.503, -93.654, 111.333, -84.702, -103.479, -30.179);
        DescPose DP7 =new DescPose( 0, 0, 0, 0, 0, 0 );

        JointPos JP8 = new JointPos(-10.503, -93.654, 111.333, -84.702, -103.479, -30.179 );
        DescPose DP8 = new DescPose( 0, 0, 0, 0, 0, 0);

        JointPos JP9 = new JointPos(-10.503, -93.654, 111.333, -84.702, -103.479, -30.179);
        DescPose DP9 = new DescPose( 0, 0, 0, 0, 0, 0 );

        JointPos JP10 = new JointPos(-30.623, -74.158, 89.844, -91.942, -97.060, -30.180 );
        DescPose DP10 = new DescPose( 0, 0, 0, 0, 0, 0 );

        JointPos JP11 = new JointPos( -34.797, -72.641, 93.917, -104.961, -84.449, -30.287 );
        DescPose DP11 = new DescPose( 0, 0, 0, 0, 0, 0 );

        JointPos JP12 = new JointPos( -17.454, -58.309, 82.054, -111.034, -109.900, -30.241 );
        DescPose DP12 = new DescPose( 0, 0, 0, 0, 0, 0 );

        JointPos JP13 = new JointPos( -4.930, -72.469, 100.631, -109.906, -76.760, -10.947 );
        DescPose DP13 = new DescPose(0, 0, 0, 0, 0, 0 );
        robot.GetForwardKin(JP7, DP7);
        robot.GetForwardKin(JP8, DP8);
        robot.GetForwardKin(JP9, DP9);
        robot.GetForwardKin(JP10,DP10);
        robot.GetForwardKin(JP11,DP11);
        robot.GetForwardKin(JP12,DP12);
        robot.GetForwardKin(JP13,DP13);
        robot.MoveJ(JP7, DP7, 0, 0, 100, 100, 100, exaxisPos, 200, 0, offdese);
        robot.MoveL(JP8, DP8, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveJ(JP9, DP9, 0, 0, 100, 100, 100, exaxisPos, 200, 0, offdese);
        robot.MoveL(JP10, DP10, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveJ(JP11, DP11, 0, 0, 100, 100, 100, exaxisPos, 200, 0, offdese);
        robot.MoveC(JP12, DP12, 0, 0, 100, 100, exaxisPos, 0, offdese, JP13, DP13, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, 20);

        JointPos JP14 = new JointPos( 9.586, -66.925, 85.589, -99.109, -103.403, -30.280);
        DescPose DP14 =new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP15 = new JointPos( 23.056, -59.187, 76.487, -102.155, -77.560, -30.250 );
        DescPose DP15 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP16 = new JointPos( 28.028, -71.754, 91.463, -102.182, -102.361, -30.253 );
        DescPose DP16 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP17 = new JointPos( 38.974, -62.622, 79.068, -102.543, -101.630, -30.253 );
        DescPose DP17 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP18 = new JointPos( -34.797, -72.641, 93.917, -104.961, -84.449, -30.287 );
        DescPose DP18 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP19 = new JointPos( -17.454, -58.309, 82.054, -111.034, -109.900, -30.241 );
        DescPose DP19 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP20 = new JointPos( -4.930, -72.469, 100.631, -109.906, -76.760, -10.947 );
        DescPose DP20 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP21 = new JointPos( 3.021, -76.365, 81.332, -98.130, -68.530, -30.284 );
        DescPose DP21 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP22 = new JointPos( 12.532, -94.241, 106.254, -87.131, -102.719, -30.227 );
        DescPose DP22 = new DescPose( 0, 0, 0, 0, 0, 0 );

        robot.GetForwardKin(JP14, DP14);
        robot.GetForwardKin(JP15, DP15);
        robot.GetForwardKin(JP16, DP16);
        robot.GetForwardKin(JP17, DP17);
        robot.GetForwardKin(JP18, DP18);
        robot.GetForwardKin(JP19, DP19);
        robot.GetForwardKin(JP20, DP20);
        robot.GetForwardKin(JP21, DP21);
        robot.GetForwardKin(JP22, DP22);

        robot.MoveJ(JP14, DP14, 0, 0, 100, 100, 100, exaxisPos, 200, 0, offdese);
        robot.Circle(JP15, DP15, 0, 0, 100, 100, exaxisPos, JP16, DP16, 0, 0, 100, 100, exaxisPos, 100, 0, offdese, 100, 20);
        robot.MoveJ(JP17, DP17, 0, 0, 100, 100, 100, exaxisPos, 200, 0, offdese);
        robot.MoveL(JP18, DP18, 0, 0, 100, 100, 100, 100, 0, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveC(JP19, DP19, 0, 0, 100, 100, exaxisPos, 0, offdese, JP20, DP20, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, 20);
        robot.MoveC(JP21, DP21, 0, 0, 100, 100, exaxisPos, 0, offdese, JP22, DP22, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, 20);

        JointPos JP23 = new JointPos( 9.586, -66.925, 85.589, -99.109, -103.403, -30.280 );
        DescPose DP23 =  new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP24 = new JointPos( 23.056, -59.187, 76.487, -102.155, -77.560, -30.250 );
        DescPose DP24 =  new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP25 = new JointPos( 28.028, -71.754, 91.463, -102.182, -102.361, -30.253 );
        DescPose DP25 =  new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP26 = new JointPos( -11.207, -81.555, 110.050, -108.983, -74.292, -30.249);
        DescPose DP26 =  new DescPose( 0, 0, 0, 0, 0, 0);
        JointPos JP27 = new JointPos( 18.930, -70.987, 100.659, -115.974, -115.465, -30.231 );
        DescPose DP27 =  new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP28 = new JointPos( 32.493, -65.561, 86.053, -109.669, -103.427, -30.267 );
        DescPose DP28 =  new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP29 = new JointPos( 21.954, -87.113, 123.299, -109.730, -72.157, -9.013 );
        DescPose DP29 =  new DescPose( 0, 0, 0, 0, 0, 0);
        JointPos JP30= new JointPos( 19.084, -69.127, 104.304, -109.629, -106.997, -9.011 );
        DescPose DP30=  new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP31 = new JointPos( 38.654, -60.146, 93.485, -109.637, -87.023, -8.989 );
        DescPose DP31 =  new DescPose( 0, 0, 0, 0, 0, 0 );

        robot.GetForwardKin(JP23, DP23);
        robot.GetForwardKin(JP24, DP24);
        robot.GetForwardKin(JP25, DP25);
        robot.GetForwardKin(JP26, DP26);
        robot.GetForwardKin(JP27, DP27);
        robot.GetForwardKin(JP28, DP28);
        robot.GetForwardKin(JP29, DP29);
        robot.GetForwardKin(JP30, DP30);
        robot.GetForwardKin(JP31, DP31);


        robot.MoveL(JP23, DP23, 0, 0, 100, 100, 100, 20, 1, exaxisPos, 0, 0, offdese,0,10);
        robot.Circle(JP24, DP24, 0, 0, 100, 100, exaxisPos, JP25, DP25, 0, 0, 100, 100, exaxisPos, 100, 0, offdese, 100, 20);
        robot.Circle(JP26, DP26, 0, 0, 100, 100, exaxisPos, JP27, DP27, 0, 0, 100, 100, exaxisPos, 100, 0, offdese, 100, 20);
        robot.MoveC(JP28, DP28, 0, 0, 100, 100, exaxisPos, 0, offdese, JP29, DP29, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, 20);
        robot.Circle(JP30, DP30, 0, 0, 100, 100, exaxisPos, JP31, DP31, 0, 0, 100, 100, exaxisPos, 100, 0, offdese, 100, 20);

        JointPos JP32 = new JointPos( 38.654, -60.146, 93.485, -109.637, -87.023, -8.989 );
        DescPose DP32 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP33 = new JointPos( 55.203, -69.138, 75.617, -103.969, -83.549, -0.001 );
        DescPose DP33 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP34 = new JointPos( 57.646, -61.846, 59.286, -69.645, -99.735, 3.824);
        DescPose DP34 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP35 = new JointPos( 57.304, -61.380, 58.260, -67.641, -97.447, 2.685 );
        DescPose DP35 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP36 = new JointPos( 57.297, -61.373, 58.250, -67.637, -97.448, 2.677 );
        DescPose DP36 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP37 = new JointPos( 23.845, -108.202, 111.300, -80.971, -106.753, -30.246 );
        DescPose DP37 = new DescPose(0, 0, 0, 0, 0, 0 );
        JointPos JP38 = new JointPos( 23.845, -108.202, 111.300, -80.971, -106.753, -30.246 );
        DescPose DP38 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP39 = new JointPos( -10.503, -93.654, 111.333, -84.702, -103.479, -30.179 );
        DescPose DP39 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP40 = new JointPos( -30.623, -74.158, 89.844, -91.942, -97.060, -30.180 );
        DescPose DP40 = new DescPose( 0, 0, 0, 0, 0, 0 );

        robot.GetForwardKin(JP32, DP32);
        robot.GetForwardKin(JP33, DP33);
        robot.GetForwardKin(JP34, DP34);
        robot.GetForwardKin(JP35, DP35);
        robot.GetForwardKin(JP36, DP36);
        robot.GetForwardKin(JP37, DP37);
        robot.GetForwardKin(JP38, DP38);
        robot.GetForwardKin(JP39, DP39);
        robot.GetForwardKin(JP40, DP40);

        robot.MoveL(JP32, DP32, 0, 0, 100, 100, 100, 20, 1, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveJ(JP33, DP33, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveL(JP34, DP34, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveL(JP35, DP35, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveL(JP36, DP36, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveL(JP37, DP37, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveL(JP38, DP38, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveL(JP39, DP39, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveJ(JP40, DP40, 0, 0, 100, 100, 100, exaxisPos, 20, 0, offdese);

        JointPos JP50 = new JointPos( -34.797, -72.641, 93.917, -104.961, -84.449, -30.287 );
        DescPose DP50 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP41 = new JointPos( -17.454, -58.309, 82.054, -111.034, -109.900, -30.241 );
        DescPose DP41 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP42 = new JointPos( -4.930, -72.469, 100.631, -109.906, -76.760, -10.947 );
        DescPose DP42 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP43 = new JointPos( 9.586, -66.925, 85.589, -99.109, -103.403, -30.280 );
        DescPose DP43 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP44 = new JointPos( 23.056, -59.187, 76.487, -102.155, -77.560, -30.250 );
        DescPose DP44 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP45 = new JointPos( 28.028, -71.754, 91.463, -102.182, -102.361, -30.253 );
        DescPose DP45 = new DescPose( 0, 0, 0, 0, 0, 0 );
        JointPos JP46 = new JointPos( 38.974, -62.622, 79.068, -102.543, -101.630, -30.253 );
        DescPose DP46 = new DescPose( 0, 0, 0, 0, 0, 0 );

        robot.GetForwardKin(JP50, DP50);
        robot.GetForwardKin(JP41, DP41);
        robot.GetForwardKin(JP42, DP42);
        robot.GetForwardKin(JP43, DP43);
        robot.GetForwardKin(JP44, DP44);
        robot.GetForwardKin(JP45, DP45);
        robot.GetForwardKin(JP46, DP46);

        robot.MoveL(JP50, DP50, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese,0,10);
        robot.MoveC(JP41, DP41, 0, 0, 100, 100, exaxisPos, 0, offdese, JP42, DP42, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, 20);
        robot.MoveL(JP43, DP43, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese,0,10);
        robot.Circle(JP44, DP44, 0, 0, 100, 100, exaxisPos, JP45, DP45, 0, 0, 100, 100, exaxisPos, 100, 0, offdese, 100, 20);
        robot.MoveL(JP46, DP46, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese,0,10);
    }

    public static int TestRobotCtrl(Robot robot)
    {
        String version = "";
        version = robot.GetSDKVersion();
        System.out.println("SDK version:"+version);
        String[] ip={""};
        robot.GetControllerIP(ip);
        System.out.println("ip: "+ip[0]);

        robot.Mode(1);
        robot.Sleep(1000);
        robot.DragTeachSwitch(1);
        robot.Sleep(1000);
        ROBOT_STATE_PKG pkg=robot.GetRobotRealTimeState();
        System.out.println("drag state :" +pkg.robot_state);

        robot.Sleep(3000);
        robot.DragTeachSwitch(0);
        robot.Sleep(1000);
        pkg=robot.GetRobotRealTimeState();
        System.out.println("drag state :" +pkg.robot_state);
        robot.Sleep(3000);

        robot.RobotEnable(0);
        robot.Sleep(3000);
        robot.RobotEnable(1);

        robot.Mode(0);
        robot.Sleep(1000);
        robot.Mode(1);

        System.out.println("Press any key to exit！");
        return 0;
    }

    public static int TestGetVersions(Robot robot)
    {
        int rtn=-1;
        String[] robotModel={""};
        String[] webversion={""};
        String[] controllerVersion={""};


        rtn = robot.GetSoftwareVersion(robotModel, webversion, controllerVersion);
        System.out.println("Getsoftwareversion rtn is: "+ rtn);
        System.out.println("robotmodel is:"+robotModel[0]+", webversion is:"+webversion[0]+", controllerVersion is: "+controllerVersion[0]);

        String[] ctrlBoxBoardVersion = {""};
        String[] driver1Version = {""};
        String[] driver2Version = {""};
        String[] driver3Version = {""};
        String[] driver4Version = {""};
        String[] driver5Version = {""};
        String[] driver6Version = {""};
        String[] endBoardVersion ={""};
        robot.GetHardwareVersion(ctrlBoxBoardVersion ,driver1Version,  driver2Version,  driver3Version,
                 driver4Version,  driver5Version,  driver6Version,  endBoardVersion);

        robot.GetFirmwareVersion(ctrlBoxBoardVersion, driver1Version, driver2Version, driver3Version,
                driver4Version, driver5Version, driver6Version, endBoardVersion);

        System.out.println("Press any key to exit！");
        return 0;
    }

    public static  int TestJOG(Robot robot)
    {
        for (int i = 0; i < 6; i++)
        {
            robot.StartJOG(0, i + 1, 0, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.ImmStopJOG();
            robot.Sleep(1000);
        }

        for (int i = 0; i < 6; i++)
        {
            robot.StartJOG(2, i + 1, 0, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.ImmStopJOG();
            robot.Sleep(1000);
        }

        for (int i = 0; i < 6; i++)
        {
            robot.StartJOG(4, i + 1, 0, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.StopJOG(5);
            robot.Sleep(1000);
        }

        for (int i = 0; i < 6; i++)
        {
            robot.StartJOG(8, i + 1, 0, 20.0, 20.0, 30.0);
            robot.Sleep(1000);
            robot.StopJOG(9);
            robot.Sleep(1000);
        }

        return 0;
    }

    public static int TestMove(Robot robot)
    {
        int rtn=-1;
        JointPos j1=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos j2=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);
        JointPos j3=new JointPos(-29.777, -84.536, 109.275, -114.075, -86.655, 74.257);
        JointPos j4=new JointPos(-31.154, -95.317, 94.276, -88.079, -89.740, 74.256);
        DescPose desc_pos1=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose desc_pos2=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);
        DescPose desc_pos3=new DescPose(-487.434, 154.362, 308.576, 176.600, 0.268, -14.061);
        DescPose desc_pos4=new DescPose(-443.165, 147.881, 480.951, 179.511, -0.775, -15.409);
        robot.GetForwardKin(j1,desc_pos1);
        robot.GetForwardKin(j2,desc_pos2);
        robot.GetForwardKin(j3,desc_pos3);
        robot.GetForwardKin(j4,desc_pos4);

        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);
        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 100.0;
        double oacc = 100.0;
        double blendT = 0.0;
        double blendR = 0.0;
        int flag = 0;
        int search = 0;
        int blendMode = 0;
        int velAccMode = 0;
        robot.SetSpeed(20);
        rtn = robot.MoveJ(j1, desc_pos1, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        System.out.printf("movej errcode:%d\n", rtn);
        rtn = robot.MoveL(j2, desc_pos2, tool, user, vel, acc, ovl, blendR, blendMode, epos, search, flag, offset_pos, oacc, velAccMode,0,10);
        System.out.printf("movel errcode:%d\n", rtn);
        rtn = robot.MoveC(j3, desc_pos3, tool, user, vel, acc, epos, flag, offset_pos, j4, desc_pos4, tool, user, vel, acc, epos, flag, offset_pos, ovl, blendR, oacc, velAccMode);
        System.out.printf("movec errcode:%d\n", rtn);
        rtn = robot.MoveJ(j2, desc_pos2, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        System.out.printf("movej errcode:%d\n", rtn);
        rtn = robot.Circle(j3, desc_pos3, tool, user, vel, acc, epos, j1, desc_pos1, tool, user, vel, acc, epos, ovl, flag, offset_pos, oacc, -1, velAccMode);
        System.out.printf("circle errcode:%d\n", rtn);
        rtn = robot.MoveCart(desc_pos4, tool, user, vel, acc, ovl, blendT, -1);
        System.out.printf("MoveCart errcode:%d\n", rtn);
        rtn = robot.MoveJ(j1, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        System.out.printf("movej errcode:%d\n", rtn);
        rtn = robot.MoveL(desc_pos2, tool, user, vel, acc, ovl, blendR, blendMode, epos, search, flag, offset_pos, -1, velAccMode,0,10);
        System.out.printf("movel errcode:%d\n", rtn);
        rtn = robot.MoveC(desc_pos3, tool, user, vel, acc, epos, flag, offset_pos, desc_pos4, tool, user, vel, acc, epos, flag, offset_pos, ovl, blendR, -1, velAccMode);
        System.out.printf("movec errcode:%d\n", rtn);
        rtn = robot.MoveJ(j2, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        System.out.printf("movej errcode:%d\n", rtn);
        rtn = robot.Circle(desc_pos3, tool, user, vel, acc, epos, desc_pos1, tool, user, vel, acc, epos, ovl, flag, offset_pos, oacc, blendR, -1, velAccMode);
        System.out.printf("circle errcode:%d\n", rtn);
        return 0;
    }

//    public static int TestSpiral(Robot robot)
//    {
//        int rtn=-1;
//        JointPos j=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
//        DescPose desc_pos=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
//        DescPose offset_pos1=new DescPose(50, 0, 0, -30, 0, 0);
//        DescPose offset_pos2=new DescPose(50, 0, 0, -5, 0, 0);
//        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);
//        SpiralParam sp=new SpiralParam(1,5.0,50.0,10.0,10.0,0,0);
//
//        int tool = 0;
//        int user = 0;
//        double vel = 100.0;
//        double acc = 100.0;
//        double ovl = 100.0;
//        double blendT = 0.0;
//        int flag = 2;
//
//        rtn = robot.MoveJ(j, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos1);
//        System.out.println("movej errcode:"+ rtn);
//
//        rtn = robot.NewSpiral(desc_pos, tool, user, vel, acc, epos, ovl, flag, offset_pos2, sp,-1);
//        System.out.println("newspiral errcode:"+ rtn);
//
//        return 0;
//    }

    public static int TestServoJ(Robot robot)
    {
        robot.udpCmdClient.SetUDPCmdRpyCallback((srcType, count, cmdID, dataLen, content) -> {
            System.out.println("\n[Received UDP reply from robot]");
            System.out.println("srcType: " + srcType);
            System.out.println("count: " + count);
            System.out.println("cmdID: " + cmdID);
            System.out.println("dataLen: " + dataLen);
            System.out.println("内容 (content): " + content);
            return 0;
        });
        int rtn=-1;

        JointPos j=new JointPos(0, 0, 0, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);

        double vel = 0.0;
        double acc = 0.0;
        double cmdT = 0.016;
        double filterT = 0.0;
        double gain = 0.0;
        int flag = 0;
        int count = 300;
        double dt = 0.1;
        int cmdID = 0;
        int comType = 1;
        int ret = robot.GetActualJointPosDegree(j);
        if (ret == 0)
        {
            robot.ServoMoveStart(comType);
            count = 300;
            while (count>0)
            {
                robot.ServoJ(j, epos, acc, vel, cmdT, filterT, gain, cmdID, comType);
                j.J1 += dt;
                j.J2 += dt;
                j.J4 += dt;
                j.J5 += dt;
                j.J6 += dt;
                epos.axis1 += dt;
                count -= 1;
                robot.Sleep(10);
            }
            robot.ServoMoveEnd(comType);

            robot.Sleep(1000);
            robot.ServoMoveStart(comType);
            count = 300;
            while (count>0)
            {
                robot.ServoJ(j, epos, acc, vel, cmdT, filterT, gain, cmdID, comType);
                j.J1 -= dt;
                j.J2 -= dt;
                j.J4 -= dt;
                j.J5 -= dt;
                j.J6 -= dt;
                epos.axis1 -= dt;
                count -= 1;
                robot.Sleep(10);
            }
            robot.ServoMoveEnd(comType);
        }
        else
        {
            System.out.println("GetActualJointPosDegree errcode:"+ ret);
        }
        return ret;
    }

    public static int TestSpline(Robot robot)
    {
        JointPos j1=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos j2=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);
        JointPos j3=new JointPos(-61.954, -84.409, 108.153, -116.316, -91.283, 74.260);
        JointPos j4=new JointPos(-89.575, -80.276, 102.713, -116.302, -91.284, 74.267);
        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 100.0;
        double blendT = -1.0;
        int flag = 0;

        int err1 = robot.MoveJ(j1, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        System.out.println("movej errcode:"+ err1);
        robot.SplineStart();
        robot.SplinePTP(j1, tool, user, vel, acc, ovl);
        robot.SplinePTP(j2, tool, user, vel, acc, ovl);
        robot.SplinePTP(j3, tool, user, vel, acc, ovl);
        robot.SplinePTP(j4, tool, user, vel, acc, ovl);
        robot.SplineEnd();
        return 0;
    }

    public static int TestNewSpline(Robot robot)
    {
        JointPos j1=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        DescPose desc_pos1=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose desc_pos2=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);
        DescPose desc_pos3=new DescPose(-327.622, 402.230, 320.402, -178.067, 2.127, -46.207);
        DescPose desc_pos4=new DescPose(-104.066, 544.321, 327.023, -177.715, 3.371, -73.818);
        DescPose desc_pos5=new DescPose(-33.421, 732.572, 275.103, -177.907, 2.709, -79.482);
        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 100.0;
        double blendT = -1.0;
        int flag = 0;

        int err1 = robot.MoveJ(j1, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        System.out.println("movej errcode:"+ err1);
        robot.NewSplineStart(1, 2000);
        robot.NewSplinePoint(desc_pos1, tool, user, vel, acc, ovl, -1, 0,-1);
        robot.NewSplinePoint(desc_pos2, tool, user, vel, acc, ovl, -1, 0,-1);
        robot.NewSplinePoint(desc_pos3, tool, user, vel, acc, ovl, -1, 0,-1);
        robot.NewSplinePoint(desc_pos4, tool, user, vel, acc, ovl, -1, 0,-1);
        robot.NewSplinePoint(desc_pos5, tool, user, vel, acc, ovl, -1, 0,-1);
        robot.NewSplineEnd();
        return 0;
    }

    public static int TestPause(Robot robot)
    {
        JointPos j1=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos j5=new JointPos(-95.228, -54.621, 73.691, -112.245, -91.280, 74.268);
        DescPose desc_pos1=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose desc_pos5=new DescPose(-33.421, 732.572, 275.103, -177.907, 2.709, -79.482);
        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 100.0;
        double blendT = -1.0;
        int flag = 0;

        robot.SetSpeed(20);
        int rtn=-1;
        rtn = robot.MoveJ(j1, desc_pos1, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        rtn = robot.MoveJ(j5, desc_pos5, tool, user, vel, acc, ovl, epos, 1, flag, offset_pos);
        robot.Sleep(1000);
        robot.PauseMotion();

        robot.Sleep(1000);
        robot.ResumeMotion();

        robot.Sleep(1000);
        robot.StopMotion();

        robot.Sleep(1000);

        return 0;
    }

    public static int TestOffset(Robot robot)
    {
        JointPos j1=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos j2=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);

        DescPose desc_pos1=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose desc_pos2=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);

        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);
        DescPose offset_pos1=new DescPose(0, 0, 50, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 100.0;
        double acc = 100.0;
        double ovl = 100.0;
        double blendT = -1.0;
        int flag = 0;

        robot.SetSpeed(20);

        robot.MoveJ(j1, desc_pos1, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        robot.MoveJ(j2, desc_pos2, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        robot.Sleep(1000);
        robot.PointsOffsetEnable(0, offset_pos1);
        robot.MoveJ(j1, desc_pos1, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        robot.MoveJ(j2, desc_pos2, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        robot.PointsOffsetDisable();

        return 0;
    }

    public static int TestMoveAO(Robot robot)
    {
        JointPos j1=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos j2=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);

        DescPose desc_pos1=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose desc_pos2=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);

        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);
        DescPose offset_pos1=new DescPose(0, 0, 50, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);

        int tool = 0;
        int user = 0;
        double vel = 20.0;
        double acc = 20.0;
        double ovl = 100.0;
        double blendT = -1.0;
        int flag = 0;

        robot.SetSpeed(20);

        robot.MoveAOStart(0, 100, 100, 20);
        robot.MoveJ(j1, desc_pos1, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        robot.MoveJ(j2, desc_pos2, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        robot.MoveAOStop();

        robot.Sleep(1000);

        robot.MoveToolAOStart(0, 100, 100, 20);
        robot.MoveJ(j1, desc_pos1, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        robot.MoveJ(j2, desc_pos2, tool, user, vel, acc, ovl, epos, blendT, flag, offset_pos);
        robot.MoveToolAOStop();

        return 0;
    }

    public static int TestAODO(Robot robot)
    {

        int status = 1;
        int smooth = 0;
        int block = 0;

        for (int i = 0; i < 16; i++)
        {
            robot.SetDO(i, status, smooth, block);
            robot.Sleep(300);
        }

        status = 0;

        for (int i = 0; i < 16; i++)
        {
            robot.SetDO(i, status, smooth, block);
            robot.Sleep(300);
        }

        status = 1;

        for (int i = 0; i < 2; i++)
        {
            robot.SetToolDO(i, status, smooth, block);
            robot.Sleep(1000);
        }

        status = 0;

        for (int i = 0; i < 2; i++)
        {
            robot.SetToolDO(i, status, smooth, block);
            robot.Sleep(1000);
        }

        for (int i = 0; i < 100; i++)
        {
            robot.SetAO(0, i, block);
            robot.Sleep(30);
        }

        for (int i = 0; i < 100; i++)
        {
            robot.SetToolAO(0, i, block);
            robot.Sleep(30);
        }

        robot.CloseRPC();
        return 0;
    }

    public static int TestGetDIAI(Robot robot)
    {
        int status = 1;
        int smooth = 0;
        int block = 0;
        int[] di =new int[]{0}, tool_di =new int[] {0};
        double[] ai =new double[] {0}, tool_ai = new double[]{0};
        double value = 0.0;


        robot.GetDI(0, block, di);
        System.out.println("di0:"+di[0]);

        robot.GetToolDI(1, block, tool_di);
        System.out.println("tool_di1:"+ tool_di[0]);

        robot.GetAI(0, block, ai);
        System.out.println("ai0:"+ ai[0]);

        robot.GetToolAI(0, block, tool_ai);
        System.out.println("tool_ai0:"+ tool_ai[0]);

        int[] _button_state=new int[]{0};
        robot.GetAxlePointRecordBtnState(_button_state);
        System.out.println("_button_state is: "+ _button_state[0]);

        int[] tool_do_state=new int[]{0};
        robot.GetToolDO(tool_do_state);
        System.out.println("tool DO state is: "+ tool_do_state[0]);

        int[] do_state_h=new int[]{0};
        int[] do_state_l=new int[]{0};
        robot.GetDO(do_state_h, do_state_l);
        System.out.println("DO state high is: "+do_state_h[0]+", DO state low is: "+ do_state_l[0]);

        return 0;
    }

    public static int TestWaitDIAI(Robot robot)
    {
        int rtn=-1;

        int status = 1;
        int smooth = 0;
        int block = 0;
        int di = 0, tool_di = 0;
        double ai = 0.0, tool_ai = 0.0;
        double value = 0.0;

        rtn = robot.WaitDI(0, 1, 1000, 1);
        System.out.println("WaitDI over; rtn is: "+ rtn);

        robot.WaitMultiDI(1, 3, 3, 1000, 1);
        System.out.println("WaitDI over; rtn is: "+ rtn);

        robot.WaitToolDI(1, 1, 1000, 1);
        System.out.println("WaitDI over; rtn is: " + rtn);

        robot.WaitAI(0, 0, 50, 1000, 1);
        System.out.println("WaitDI over; rtn is: " + rtn);

        robot.WaitToolAI(0, 0, 50, 1000, 1);
        System.out.println("WaitDI over; rtn is: " + rtn);
        return 0;
    }


//    public static int TestDOReset(Robot robot)
//    {
//
//        int rtn=-1;
//        for (int i = 0; i < 16; i++)
//        {
//            robot.SetDO(i, 1, 0, 0);
//            robot.Sleep(300);
//        }
//
//        int resetFlag = 1;
//        rtn = robot.SetOutputResetCtlBoxDO(resetFlag);
//        robot.SetOutputResetCtlBoxAO(resetFlag);
//        robot.SetOutputResetAxleDO(resetFlag);
//        robot.SetOutputResetAxleAO(resetFlag);
//        robot.SetOutputResetExtDO(resetFlag);
//        robot.SetOutputResetExtAO(resetFlag);
//        robot.SetOutputResetSmartToolDO(resetFlag);
//
//        robot.ProgramLoad("/fruser/test.lua");
//        robot.ProgramRun();
//        return 0;
//    }

    public static int TestTCPCompute(Robot robot)
    {
        DescPose p1Desc=new DescPose(186.331, 487.913, 209.850, 149.030, 0.688, -114.347);
        JointPos p1Joint=new JointPos(-127.876, -75.341, 115.417, -122.741, -59.820, 74.300);

        DescPose p2Desc=new DescPose(69.721, 535.073, 202.882, -144.406, -14.775, -89.012);
        JointPos p2Joint=new JointPos(-101.780, -69.828, 110.917, -125.740, -127.841, 74.300);

        DescPose p3Desc=new DescPose(146.861, 578.426, 205.598, 175.997, -36.178, -93.437);
        JointPos p3Joint=new JointPos(-112.851, -60.191, 86.566, -80.676, -97.463, 74.300);

        DescPose p4Desc=new DescPose(136.284, 509.876, 225.613, 178.987, 1.372, -100.696);
        JointPos p4Joint=new JointPos(-116.397, -76.281, 113.845, -128.611, -88.654, 74.299);

        DescPose p5Desc=new DescPose(138.395, 505.972, 298.016, 179.134, 2.147, -101.110);
        JointPos p5Joint=new JointPos(-116.814, -82.333, 109.162, -118.662, -88.585, 74.302);

        DescPose p6Desc=new DescPose(105.553, 454.325, 232.017, -179.426, 0.444, -99.952);
        JointPos p6Joint=new JointPos(-115.649, -84.367, 122.447, -128.663, -90.432, 74.303);

        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);

        JointPos[] posJ = { p1Joint , p2Joint , p3Joint , p4Joint , p5Joint , p6Joint };
        DescPose coordRtn =new DescPose() {};
        int rtn = robot.ComputeToolCoordWithPoints(1, posJ, coordRtn);

        robot.MoveJ(p1Joint, p1Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(1);
        robot.MoveJ(p2Joint, p2Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(2);
        robot.MoveJ(p3Joint, p3Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(3);
        robot.MoveJ(p4Joint, p4Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(4);
        robot.MoveJ(p5Joint, p5Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(5);
        robot.MoveJ(p6Joint, p6Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(6);
        rtn = robot.ComputeTool(coordRtn);
        robot.SetToolList(3, coordRtn, 0, 0, 0);

        robot.MoveJ(p1Joint, p1Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(1);
        robot.MoveJ(p2Joint, p2Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(2);
        robot.MoveJ(p3Joint, p3Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(3);
        robot.MoveJ(p4Joint, p4Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(4);
        rtn = robot.ComputeTcp4(coordRtn);

        robot.SetToolCoord(4, coordRtn, 0, 0, 1, 0);

        DescPose getCoord = new DescPose(){};
        rtn = robot.GetTCPOffset(0, getCoord);
        return 0;
    }

    public static int TestWobjCoord(Robot robot)
    {
        DescPose p1Desc=new DescPose(-89.606, 779.517, 193.516, 178.000, 0.476, -92.484);
        JointPos p1Joint=new JointPos(-108.145, -50.137, 85.818, -125.599, -87.946, 74.329);

        DescPose p2Desc=new DescPose(-24.656, 850.384, 191.361, 177.079, -2.058, -95.355);
        JointPos p2Joint=new JointPos(-111.024, -41.538, 69.222, -114.913, -87.743, 74.329);

        DescPose p3Desc=new DescPose(-99.813, 766.661, 241.878, -176.817, 1.917, -91.604);
        JointPos p3Joint=new JointPos(-107.266, -56.116, 85.971, -122.560, -92.548, 74.331);

        robot.GetForwardKin(p1Joint,p1Desc);
        robot.GetForwardKin(p2Joint,p2Desc);
        robot.GetForwardKin(p3Joint,p3Desc);

        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);

        DescPose[] posTCP =new DescPose[]{p1Desc , p2Desc , p3Desc };
        DescPose coordRtn =new DescPose();
        int rtn = robot.ComputeWObjCoordWithPoints(1, posTCP, 0, coordRtn);

        robot.MoveJ(p1Joint, p1Desc, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetWObjCoordPoint(1);
        robot.MoveJ(p2Joint, p2Desc, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetWObjCoordPoint(2);
        robot.MoveJ(p3Joint, p3Desc, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetWObjCoordPoint(3);
        rtn = robot.ComputeWObjCoord(1, 0, coordRtn);

        robot.SetWObjCoord(1, coordRtn, 0);
        robot.SetWObjList(1, coordRtn, 0);

        DescPose getWobjDesc = new DescPose();
        rtn = robot.GetWObjOffset(0, getWobjDesc);
        return 0;
    }

    public static int TestExtCoord(Robot robot)
    {
        DescPose p1Desc=new DescPose(-89.606, 779.517, 193.516, 178.000, 0.476, -92.484);
        JointPos p1Joint=new JointPos(-108.145, -50.137, 85.818, -125.599, -87.946, 74.329);

        DescPose p2Desc=new DescPose(-24.656, 850.384, 191.361, 177.079, -2.058, -95.355);
        JointPos p2Joint=new JointPos(-111.024, -41.538, 69.222, -114.913, -87.743, 74.329);

        DescPose p3Desc=new DescPose(-99.813, 766.661, 241.878, -176.817, 1.917, -91.604);
        JointPos p3Joint=new JointPos(-107.266, -56.116, 85.971, -122.560, -92.548, 74.331);

        robot.GetForwardKin(p1Joint,p1Desc);
        robot.GetForwardKin(p2Joint,p2Desc);
        robot.GetForwardKin(p3Joint,p3Desc);

        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);

        DescPose[] posTCP = { p1Desc , p2Desc , p3Desc };
        DescPose coordRtn = new DescPose();

        robot.MoveJ(p1Joint, p1Desc, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetExTCPPoint(1);
        robot.MoveJ(p2Joint, p2Desc, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetExTCPPoint(2);
        robot.MoveJ(p3Joint, p3Desc, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetExTCPPoint(3);
        int rtn = robot.ComputeExTCF(coordRtn);

        robot.SetExToolCoord(1, coordRtn, offdese);
        robot.SetExToolList(1, coordRtn, offdese);
        return 0;
    }

    public static int TestLoadInstall(Robot robot)
    {
        for (int i = 1; i < 100; i++)
        {
            robot.SetSpeed(i);
            robot.SetOaccScale(i);
            robot.Sleep(30);
        }

        List<Number> defaultVel=new ArrayList<>();

        defaultVel=robot.GetDefaultTransVel();
        System.out.println("GetDefaultTransVel is:"+ defaultVel.get(1));

        for (int i = 1; i < 21; i++)
        {
            robot.SetSysVarValue(i, i + 0.5);
            robot.Sleep(100);
        }

        for (int i = 1; i < 21; i++)
        {
            float value = 0;
            defaultVel=robot.GetSysVarValue(i);
            System.out.println("sys value :"+i+", is :"+defaultVel.get(1));
            robot.Sleep(100);
        }

        robot.SetLoadWeight(0, 2.5);

        DescTran loadCoord = new DescTran();
        loadCoord.x = 3.0;
        loadCoord.y = 4.0;
        loadCoord.z = 5.0;
        robot.SetLoadCoord(loadCoord);

        robot.Sleep(1000);

        List<Number> getLoad = new ArrayList<>();

        getLoad=robot.GetTargetPayload(0);

        DescTran getLoadTran =new DescTran();
        robot.GetTargetPayloadCog(0, getLoadTran);
        System.out.println("get load is:"+getLoad.get(1)+", get load cog is: "+getLoadTran.x+","+getLoadTran.y+","+getLoadTran.z);

        robot.SetRobotInstallPos(0);
        robot.SetRobotInstallAngle(15.0, 25.0);

        List<Number> angle=new ArrayList<>();
        angle=robot.GetRobotInstallAngle();
        System.out.println("GetRobotInstallAngle x:"+angle.get(1)+";  y:"+angle.get(2));

        robot.CloseRPC();
        return 0;
    }


    public static int TestFriction(Robot robot)
    {

        Object[] lcoeff = { 0.9,0.9,0.9,0.9,0.9,0.9 };
        Object[] wcoeff = { 0.4,0.4,0.4,0.4,0.4,0.4 };
        Object[] ccoeff = { 0.6,0.6,0.6,0.6,0.6,0.6 };
        Object[] fcoeff = { 0.5,0.5,0.5,0.5,0.5,0.5 };

        int rtn = robot.FrictionCompensationOnOff(1);
        System.out.println("FrictionCompensationOnOff rtn is:"+ rtn);

        rtn = robot.SetFrictionValue_level(lcoeff);
        System.out.println("SetFrictionValue_level rtn is:"+ rtn);

        rtn = robot.SetFrictionValue_wall(wcoeff);
        System.out.println("SetFrictionValue_wall rtn is:"+ rtn);

        rtn = robot.SetFrictionValue_ceiling(ccoeff);
        System.out.println("SetFrictionValue_ceiling rtn is:"+ rtn);

        rtn = robot.SetFrictionValue_freedom(fcoeff);
        System.out.println("SetFrictionValue_freedom rtn is:"+ rtn);

        robot.CloseRPC();
        return 0;
    }

    public static int TestGetError(Robot robot)
    {
        int[] maincode={0}, subcode={0};
        robot.GetRobotErrorCode(maincode, subcode);

        robot.ResetAllError();

        robot.Sleep(1000);

        robot.GetRobotErrorCode(maincode, subcode);
        return 0;
    }

    public static int TestCollision(Robot robot)
    {
        int mode = 0;
        int config = 1;
        Object[] level1 = new Object[]{ 1.0,2.0,3.0,4.0,5.0,6.0 };
        Object[] level2 = new Object[]{ 50.0,20.0,30.0,40.0,50.0,60.0 };

        int rtn = robot.SetAnticollision(mode, level1, config);
        System.out.println("SetAnticollision mode 0 rtn is: "+ rtn);
        mode = 1;
        rtn = robot.SetAnticollision(mode, level2, config);
        System.out.println("SetAnticollision mode 1 rtn is :"+ rtn);

        JointPos p1Joint=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos p2Joint=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);

        DescPose p1Desc=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose p2Desc=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);
//        robot.GetForwardKin(p1Joint,p1Desc);
//        robot.GetForwardKin(p2Joint,p2Desc);

        ExaxisPos exaxisPos=new ExaxisPos(0.0, 0.0, 0.0, 0.0);
        DescPose offdese=new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        robot.MoveL(p2Joint, p2Desc,  0, 0, 100, 100, 100, -1, 0,exaxisPos, 0, 0, offdese, 100,0,1, 1);
        robot.ResetAllError();
        int[] safety = new int[]{ 5,5,5,5,5,5 };
        rtn = robot.SetCollisionStrategy(3, 1000, 150, 250, safety);
        System.out.println("SetCollisionStrategy rtn is:"+ rtn);

        double[] jointDetectionThreshould = new double[]{ 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
        double[] tcpDetectionThreshould =new double[] { 60,60,60,60,60,60 };
        rtn = robot.CustomCollisionDetectionStart(3, jointDetectionThreshould, tcpDetectionThreshould, 0);
        System.out.println("CustomCollisionDetectionStart rtn is :"+ rtn);
        robot.MoveL(p1Joint, p1Desc,  0, 0, 100, 100, 100, -1, 0,exaxisPos, 0, 0, offdese, 100,0,1, 1);
        robot.MoveL(p2Joint, p2Desc, 0, 0, 100, 100, 100, -1, 0,exaxisPos, 0, 0, offdese, 100,0,1, 1);
        rtn = robot.CustomCollisionDetectionEnd();
        System.out.println("CustomCollisionDetectionEnd rtn is: "+ rtn);
        return 0;
    }

    public static int TestLimit(Robot robot)
    {
        Object[] plimit =new Object[] { 170.0,80.0,150.0,80.0,170.0,160.0 };
        robot.SetLimitPositive(plimit);
        Object[] nlimit =new Object[] { -170.0,-260.0,-150.0,-260.0,-170.0,-160.0 };
        robot.SetLimitNegative(nlimit);

        Object[] neg_deg =new Object[] {0, 0 , 0, 0, 0, 0}, pos_deg = new Object[]{0, 0 , 0, 0, 0, 0};
        robot.GetJointSoftLimitDeg(1,  neg_deg,  pos_deg);
        System.out.println("neg limit deg:"+ neg_deg[0]+","+ neg_deg[1]+","+ neg_deg[2]+","+ neg_deg[3]+","+ neg_deg[4]+","+ neg_deg[5]);
        System.out.println("pos limit deg:"+pos_deg[0]+","+ pos_deg[1]+","+ pos_deg[2]+","+ pos_deg[3]+","+ pos_deg[4]+","+pos_deg[5]);
        return 0;
    }

    public static int TestCollisionMethod(Robot robot)
    {
        int rtn = robot.SetCollisionDetectionMethod(0);

        rtn = robot.SetStaticCollisionOnOff(1);
        System.out.println("SetStaticCollisionOnOff On rtn is:"+ rtn);
        robot.Sleep(5000);
        rtn = robot.SetStaticCollisionOnOff(0);
        System.out.println("SetStaticCollisionOnOff Off rtn is:"+ rtn);

        robot.CloseRPC();
        return 0;
    }

    public static int TestPowerLimit(Robot robot)
    {
        robot.DragTeachSwitch(1);
        robot.SetPowerLimit(1, 200);
        List<Number> joint_toq=new ArrayList<>();
        joint_toq=robot.GetJointTorques(1);

        int count = 100;
        robot.ServoJTStart(1); //   #servoJT开始
        int error = 0;
        while (count > 0)
        {
//            error = robot.ServoJT(torques, 0.001);
            count = count - 1;
            robot.Sleep(1);
        }
        error = robot.ServoJTEnd(1);
        robot.DragTeachSwitch(0);

        robot.CloseRPC();
        return 0;
    }

    public static int TestServoJT(Robot robot)
    {

        robot.DragTeachSwitch(1);
        List<Number> joint_toq=new ArrayList<>();
        joint_toq=robot.GetJointTorques(1);

        int count = 100;
        robot.ServoJTStart(1); //   #servoJT开始
        int error = 0;
        while (count > 0)
        {
//            error = robot.ServoJT(torques, 0.001);
            count = count - 1;
            robot.Sleep(1);
        }
        error = robot.ServoJTEnd(1);
        robot.DragTeachSwitch(0);

        robot.CloseRPC();
        return 0;
    }

    public static int TestGetStatus(Robot robot)
    {

        List<Number> angle=new ArrayList<>();
        angle=robot.GetRobotInstallAngle();
        System.out.println("yangle:"+angle.get(1)+",zangle:"+angle.get(2));

        JointPos j_deg =new JointPos(){};
        robot.GetActualJointPosDegree( j_deg);

        Object[] jointSpeed =new Object[] { 0,0,0,0,0,0 };
        robot.GetActualJointSpeedsDegree(jointSpeed);

        Object[] jointAcc = new Object[]{0,0,0,0,0,0 };
        robot.GetActualJointAccDegree(0, jointAcc);

        double tcp_speed = 0.0;
        double ori_speed = 0.0;
        robot.GetTargetTCPCompositeSpeed(0, tcp_speed, ori_speed);

        robot.GetActualTCPCompositeSpeed(0, tcp_speed, ori_speed);

        Object[] targetSpeed =new Object[] { 0,0,0,0,0,0 };
        robot.GetTargetTCPSpeed(0, targetSpeed);

        Object[] actualSpeed =new Object[] {0,0,0,0,0,0 };
        robot.GetActualTCPSpeed(0, actualSpeed);

        DescPose tcp = new DescPose(){};
        robot.GetActualTCPPose(tcp);

        DescPose flange = new DescPose(){};
        robot.GetActualToolFlangePose(0, flange);

        int[] id = {0};
        robot.GetActualTCPNum(0, id);

        robot.GetActualWObjNum(0, id);

        List<Number> jtorque=new ArrayList<>();
        jtorque=robot.GetJointTorques(0);

        List<Number> t_ms = new ArrayList<>();
        t_ms=robot.GetSystemClock();

        List<Integer> config = new ArrayList<>();
        config=robot.GetRobotCurJointsConfig();

        int motionDone = 0;
        robot.GetRobotMotionDone(motionDone);

        int[] len ={0 };
        robot.GetMotionQueueLength(len);

        int[] emergState = {0};
        robot.GetRobotEmergencyStopState(emergState);

        int comstate = 0;
        comstate=robot.GetSDKComState();

        int[] si0_state=new int[]{0}, si1_state=new int[]{0};
        robot.GetSafetyStopState(si0_state, si1_state);

        double[] temp =new double[] { 0,0,0,0,0,0 };
        robot.GetJointDriverTemperature(temp);

        double[] torque = new double[]{ 0,0,0,0,0,0 };
        robot.GetJointDriverTorque(torque);

        ROBOT_STATE_PKG pkg=new ROBOT_STATE_PKG();
        pkg=robot.GetRobotRealTimeState();

        return 0;
    }


    public static int TestInverseKin(Robot robot)
    {
        JointPos j1=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        DescPose desc_pos1=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);

        JointPos inverseRtn = new JointPos(){};

        robot.GetInverseKin(0, desc_pos1, -1, inverseRtn);
        robot.GetInverseKinRef(0, desc_pos1, j1, inverseRtn);

        int hasResut = 0;
        robot.GetInverseKinHasSolution(0, desc_pos1, j1);

        DescPose forwordResult = new DescPose(){};
        robot.GetForwardKin(j1, forwordResult);

        return 0;
    }

    public static int TestGetTeachPoint(Robot robot)
    {
        String name = "p4";
        List<Number> data=new ArrayList<>();
        data = robot.GetRobotTeachingPoint(name);
        System.out.println(name+" name is: "+data.get(0));
        for (int i = 0; i < 20; i++)
        {
            System.out.println("data is: "+ data.get(i+1));
        }

        int[] que_len = {0};
        int rtn = robot.GetMotionQueueLength(que_len);
        System.out.println("GetMotionQueueLength rtn is:"+rtn+", queue length is:"+ que_len[0]);

        Object[] dh = new Object[]{ 0,0,0,0,0,0 };
        int retval = 0;
        retval = robot.GetDHCompensation(dh);
        System.out.println("retval is: "+retval);

        String[] SN = new String[]{""};
        robot.GetRobotSN(SN);
        System.out.println("robot SN is "+SN[0]);
        return 0;
    }

    public static int TestTPD(Robot robot)
    {
        int type = 1;
        String name = "tpd2025";
        int period_ms = 4;
        int di_choose = 0;
        int do_choose = 0;

        robot.SetTPDParam(type, name, period_ms, di_choose, do_choose);

        robot.Mode(1);
        robot.Sleep(1000);
        robot.DragTeachSwitch(1);
        robot.SetTPDStart(type, name, period_ms, di_choose, do_choose);
        robot.Sleep(10000);
        robot.SetWebTPDStop();
        robot.DragTeachSwitch(0);

        double ovl = 100.0;
        int blend = 0;

        DescPose start_pose =new DescPose() {};

        int rtn = robot.LoadTPD(name);
        System.out.println("LoadTPD rtn is:"+ rtn);

        robot.GetTPDStartPose(name, start_pose);
        robot.MoveCart(start_pose, 0, 0, 100, 100, ovl, -1, -1);
        robot.Sleep(1000);

        rtn = robot.MoveTPD(name, blend, ovl);
        System.out.println("MoveTPD rtn is: "+ rtn);
        robot.Sleep(5000);

        robot.SetTPDDelete(name);
        return 0;
    }

    public static int TestTraj(Robot robot)
    {
        int rtn = robot.TrajectoryJUpLoad("D://zUP/horse.txt");
        System.out.println("Upload TrajectoryJ A :"+ rtn);

        String traj_file_name = "horse.txt";
        rtn = robot.LoadTrajectoryJ(traj_file_name, 100, 1);
        System.out.println("LoadTrajectoryJ:"+traj_file_name+", rtn is:"+ rtn);

        DescPose traj_start_pose=new DescPose(0,0,0,0,0,0);
        rtn = robot.GetTrajectoryStartPose(traj_file_name, traj_start_pose);

        robot.Sleep(1000);

    //        JointPos j1=new JointPos(0,0,0,0,0,0);
    //        robot.GetInverseKin(0,traj_start_pose,-1,j1);

        ExaxisPos epos=new ExaxisPos(0,0,0,0);
        DescPose po=new DescPose(0,0,0,0,0,0);
        robot.SetSpeed(50);
        robot.MoveCart(traj_start_pose, 0, 0, 100, 100, 100, -1, -1);
    //        robot.MoveJ(j1,traj_start_pose, 0, 0, 50, 100, 100, epos,-1.0,0, po);

        int traj_num = 0;
        rtn = robot.GetTrajectoryPointNum(traj_num);

        rtn = robot.SetTrajectoryJSpeed(50.0);
        System.out.println("SetTrajectoryJSpeed is:"+ rtn);

        ForceTorque traj_force=new ForceTorque(0,0,0,0,0,0);
        traj_force.fx = 10;
        rtn = robot.SetTrajectoryJForceTorque(traj_force);
        System.out.println("SetTrajectoryJForceTorque rtn is: "+ rtn);

        rtn = robot.SetTrajectoryJForceFx(10.0);
        System.out.println("SetTrajectoryJForceFx rtn is:"+ rtn);

        rtn = robot.SetTrajectoryJForceFy(0.0);
        System.out.println("SetTrajectoryJForceFy rtn is:"+ rtn);

        rtn = robot.SetTrajectoryJForceFz(0.0);
        System.out.println("SetTrajectoryJForceFz rtn is: "+ rtn);

        rtn = robot.SetTrajectoryJTorqueTx(10.0);
        System.out.println("SetTrajectoryJTorqueTx rtn is: "+ rtn);

        rtn = robot.SetTrajectoryJTorqueTy(10.0);
        System.out.println("SetTrajectoryJTorqueTy rtn is:"+ rtn);

        rtn = robot.SetTrajectoryJTorqueTz(10.0);
        System.out.println("SetTrajectoryJTorqueTz rtn is:"+ rtn);

        rtn = robot.MoveTrajectoryJ();
        System.out.println("MoveTrajectoryJ rtn is: "+ rtn);

        return 0;
    }

    public static int TestLuaOp(Robot robot)
    {
        String program_name = "test.lua";
        String[] loaded_name = new String[]{""};
        int[] state=new int[]{0};
        List<Integer> line=new ArrayList<>();

        robot.Mode(0);
        robot.LoadDefaultProgConfig(0, program_name);
        robot.ProgramLoad(program_name);
        robot.ProgramRun();
        robot.Sleep(1000);
        robot.ProgramPause();
        robot.GetProgramState(state);
        System.out.println("program state:"+ state[0]);
        line=robot.GetCurrentLine();
        System.out.println("current line:"+ line);
        robot.GetLoadedProgram(loaded_name);
        System.out.println("program name:"+ loaded_name[0]);
        robot.Sleep(1000);
        robot.ProgramResume();
        robot.Sleep(1000);
        robot.ProgramStop();
        robot.Sleep(1000);

        robot.CloseRPC();
        return 0;
    }

    public static int TestLUAUpDownLoad(Robot robot)
    {
        /* 获取lua名称 */
        List<String> luaNames=new ArrayList<>();
        int rtn = robot.GetLuaList(luaNames);
        System.out.println("res is: "+rtn);
        System.out.println("size is: "+luaNames.size());
        for (int it =1; it < luaNames.size(); it++)
        {
            System.out.println(luaNames.get(it));
        }

        /* 下载lua */
        rtn = robot.LuaDownLoad("test.lua", "D://zDOWN/");
        System.out.println("LuaDownLoad rtn is:"+rtn);

        /* 上传lua */
        rtn = robot.LuaUpload("D://zUP/test.lua","");
        System.out.println("LuaUpload rtn is:"+ rtn);

        /* 删除lua */
        rtn = robot.LuaDelete("test.lua");
        System.out.println("LuaDelete rtn is:"+ rtn);

        return 0;
    }

    public static int Test485Auxservo(Robot robot)
    {
        Axis485Param ax=new Axis485Param(1, 1, 1, 131072, 15.45);
        int retval = robot.AuxServoSetParam(1, ax);

        Axis485Param ax2=new Axis485Param();
        retval = robot.AuxServoGetParam(1, ax2);

        ax.servoCompany=10;
        ax.servoModel=11;
        ax.servoSoftVersion=12;
        ax.servoResolution=13;
        ax.axisMechTransRatio=14;

        retval = robot.AuxServoSetParam(1, ax);

        retval = robot.AuxServoGetParam(1,ax2);

        ax.servoCompany=1;
        ax.servoModel=1;
        ax.servoSoftVersion=1;
        ax.servoResolution=131072;
        ax.axisMechTransRatio=36;

        retval = robot.AuxServoSetParam(1, ax);
        robot.Sleep(3000);

        robot.AuxServoSetAcc(3000, 3000);
        robot.AuxServoSetEmergencyStopAcc(5000, 5000);
        robot.Sleep(1000);
        double emagacc = 0, acc = 0;
        double emagdec = 0, dec = 0;

        List<Number> aux=new ArrayList<>();

        aux=robot.AuxServoGetEmergencyStopAcc();
        aux=robot.AuxServoGetAcc();

        robot.AuxServoSetControlMode(1, 0);
        robot.Sleep(2000);

        retval = robot.AuxServoEnable(1, 0);
        robot.Sleep(1000);
        int[] servoerrcode =new int[]{0};
        int[] servoErrCode=new int[]{0};
        int[] servoState=new int[]{0};
        double[] servoPos=new double[]{0};
        double[] servoSpeed=new double[]{0};
        double[] servoTorque=new double[]{0};
        retval = robot.AuxServoGetStatus(1, servoErrCode, servoState, servoPos, servoSpeed, servoTorque);
        robot.Sleep(1000);;

        retval = robot.AuxServoEnable(1, 1);
        robot.Sleep(1000);
        retval = robot.AuxServoGetStatus(1, servoErrCode, servoState, servoPos, servoSpeed, servoTorque);
        robot.Sleep(1000);

        retval = robot.AuxServoHoming(1, 1, 5, 1,100);
        robot.Sleep(3000);

        retval = robot.AuxServoSetTargetPos(1, 200, 30,100);
        robot.Sleep(1000);
        retval = robot.AuxServoGetStatus(1, servoErrCode, servoState, servoPos, servoSpeed, servoTorque);
        robot.Sleep(8000);


        robot.AuxServoSetControlMode(1, 1);
        robot.Sleep(2000);

        robot.AuxServoEnable(1, 0);
        robot.Sleep(1000);
        robot.AuxServoEnable(1, 1);
        robot.Sleep(1000);
        robot.AuxServoSetTargetSpeed(1, 100, 80);

        robot.Sleep(5000);
        robot.AuxServoSetTargetSpeed(1, 0, 80);

        robot.CloseRPC();
        return 0;
    }

    public static int TestGripper(Robot robot)
    {
        int company = 4;
        int device = 0;
        int softversion = 0;
        int bus = 2;
        int index = 2;
        int act = 0;
        int max_time = 30000;
        int block = 0;

        int current_pos = 0;
        int current = 0;
        int voltage = 0;
        int temp = 0;
        int speed = 0;

        DeviceConfig cnn=new DeviceConfig(company,device,softversion,bus);
        robot.SetGripperConfig(cnn);
        robot.GetGripperConfig(cnn);

        robot.ActGripper(index, act);
        robot.Sleep(1000);
        act = 1;
        robot.ActGripper(index, act);
        robot.Sleep(1000);

        robot.MoveGripper(index, 100, 50, 50, max_time, block, 0, 0, 0, 0);
        robot.Sleep(1000);
        robot.MoveGripper(index, 0, 50, 0, max_time, block, 0, 0, 0, 0);

        List<Integer> stat=new ArrayList<>();
        stat=robot.GetGripperMotionDone();

        List<Number> list=new ArrayList<>();
        list=robot.GetGripperActivateStatus();

        list=robot.GetGripperCurPosition();

        list=robot.GetGripperCurCurrent();

        list=robot.GetGripperVoltage();

        list=robot.GetGripperTemp();

        list=robot.GetGripperCurSpeed();

        int retval = 0;
        DescPose prepick_pose = new DescPose(){};
        DescPose postpick_pose = new DescPose(){};

        DescPose p1Desc=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose p2Desc=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);

        retval = robot.ComputePrePick(p1Desc, 10, 0, prepick_pose);

        retval = robot.ComputePostPick(p2Desc, -10, 0, postpick_pose);
        return 0;
    }

    public static int TestRotGripperState(Robot robot)
    {
        int fault = 0;
        List<Number> rotNum=new ArrayList<>();
        List<Number> rotSpeed=new ArrayList<>();
        List<Number> rotTorque=new ArrayList<>();

        rotNum=robot.GetGripperRotNum();
        rotSpeed=robot.GetGripperRotSpeed();
        rotTorque=robot.GetGripperRotTorque();
        System.out.println("gripper rot num :"+rotNum.get(2)+ ", gripper rotSpeed :"+rotSpeed.get(2)+",gripper rotTorque : "+rotTorque.get(2));

        return 0;
    }

    public static int TestConveyor(Robot robot)
    {
        DescPose pos1 = new DescPose(-354.549, 63.914, 270.176, -179.679, -0.134, 2.468);
        DescPose pos2 = new DescPose(-351.203, -213.393, 351.054, -179.932, -0.508, 2.472);

        Object[] cmp = {0.0, 0.0, 0.0};
        int rtn = robot.ConveyorCatchPointComp(cmp); // 设置传动带抓取点补偿
        System.out.println("ConveyorCatchPointComp: rtn  " + rtn);

        rtn = robot.MoveCart(pos1, 1, 0,  30.0,  180.0, 100.0,  -1.0, -1);
        System.out.println("MoveCart: rtn  " + rtn);

        rtn = robot.ConveyorIODetect(10000); // 传送带工件IO检测
        System.out.println("ConveyorIODetect: rtn   " + rtn);

        robot.ConveyorGetTrackData(1); // 配置传送带跟踪抓取
        rtn = robot.ConveyorTrackStart(1); // 跟踪开始
        System.out.println("ConveyorTrackStart: rtn  " + rtn);

        rtn = robot.ConveyorTrackMoveL("cvrCatchPoint", 1, 0,  100.0,  0.0,  100.0,  -1.0);
        System.out.println("ConveyorTrackMoveL: rtn  " + rtn);

        rtn = robot.MoveGripper(2, 30, 60, 30, 30000, 0, 0, 0, 50, 50);
        System.out.println("ConveyorTrackMoveL: rtn  " + rtn);

        rtn = robot.ConveyorTrackMoveL("cvrRaisePoint", 1, 0,  100.0,  0.0,  100.0, -1.0);
        System.out.println("ConveyorTrackMoveL: rtn   " + rtn);

        rtn = robot.ConveyorTrackEnd(); // 传送带跟踪停止
        System.out.println("ConveyorTrackEnd: rtn  " + rtn);

        rtn = robot.MoveCart(pos2, 1, 0,30.0,180.0, 100.0,  -1.0, -1);
        System.out.println("MoveCart: rtn  " + rtn);

        rtn = robot.MoveGripper(2, 100, 60, 30, 30000, 0, 0, 0, 50, 50);
        System.out.println("MoveGripper: rtn  " + rtn);
        return 0;
    }

    public static int TestAxleSensor(Robot robot)//末端传感器
    {
        DeviceConfig con=new DeviceConfig(18,0,0,1);
        robot.AxleSensorConfig(con);
        int company = -1;
        int type = -1;
        robot.AxleSensorConfigGet(con);

        int rtn = robot.AxleSensorActivate(1);

        robot.Sleep(1000);

        rtn = robot.AxleSensorRegWrite(1, 4, 6, 1, 0, 0, 0);
        return 0;
    }

    public static int TestExDevProtocol(Robot robot)//设置外设协议
    {
        int protocol = 4096;
        int rtn = robot.SetExDevProtocol(protocol);
        List<Integer> integer=new ArrayList<>();
        integer = robot.GetExDevProtocol();

        return 0;
    }

    public static int TestAxleLua(Robot robot)//末端外设开发协议
    {
//        robot.AxleLuaUpload("D://zUP/AXLE_LUA_End_DaHuan.lua");

        AxleComParam param=new AxleComParam(7, 8, 1, 0, 5, 3, 1);
        robot.SetAxleCommunicationParam(param);

        robot.GetAxleCommunicationParam(param);

        robot.SetAxleLuaEnable(1);
        int[] luaEnableStatus = new int[]{0};
        robot.GetAxleLuaEnableStatus(luaEnableStatus);
        robot.SetAxleLuaEnableDeviceType(0, 1, 0, 0);

        int forceEnable = 0;
        int gripperEnable = 0;
        int ioEnable = 0;
        int [] enable=new int[]{0,0,0,0};
        robot.GetAxleLuaEnableDeviceType(enable);

        int[] func = { 0,1,1,1,1,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 };
        robot.SetAxleLuaGripperFunc(1, func);
        int[] getFunc = { 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        robot.GetAxleLuaGripperFunc(1, getFunc);
        int[] getforceEnable = { 0,0,0,0,0,0,0,0};
        int[] getgripperEnable = { 0,0,0,0,0,0,0,0};
        int[] getioEnable = { 0,0,0,0,0,0,0,0};
        int[] dexhandEnable1 = { 0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
        robot.GetAxleLuaEnableDevice(getforceEnable, getgripperEnable, getioEnable, dexhandEnable1);
        for (int i = 0; i < 8; i++)
        {
            System.out.println(getforceEnable[i]);
        }
        System.out.println("getgripperEnable status : ");
        for (int i = 0; i < 8; i++)
        {
            System.out.println(getgripperEnable[i]);
        }
        System.out.println("getioEnable status : ");
        for (int i = 0; i < 8; i++)
        {
            System.out.println(getioEnable[i]);
        }
        robot.ActGripper(1, 0);
        robot.Sleep(2000);
        robot.ActGripper(1, 1);
        robot.Sleep(2000);
        robot.MoveGripper(1, 90, 10, 100, 50000, 0, 0, 0, 0, 0);
        int pos = 0;
        while (true)
        {
            ROBOT_STATE_PKG pkg=new ROBOT_STATE_PKG();
            pkg=robot.GetRobotRealTimeState();
            System.out.println("gripper pos is:"+pkg.gripper_position);
            robot.Sleep(100);
        }

    }

    public static int TestSetWeldParam(Robot robot) {
        // 1. 设置焊接工艺参数
        WeldingProcessParam para1 = new WeldingProcessParam(177, 27, 1000, 178, 28, 176, 26, 1000);
        WeldingProcessParam para2 = new WeldingProcessParam(188, 28, 555, 199, 29, 133, 23, 333);
        robot.WeldingSetProcessParam(1, para1);
        robot.WeldingSetProcessParam(2, para2);

        // 2. 获取并打印第1组参数
        WeldingProcessParam param = new WeldingProcessParam(0, 0, 0, 0, 0, 0, 0, 0);
        robot.WeldingGetProcessParam(1, param);
        System.out.println("the Num 1 process param is "
                + param.startCurrent + " " + param.startVoltage + " "
                + param.startTime + " " + param.weldCurrent + " "
                + param.weldVoltage + " " + param.endCurrent + " "
                + param.endVoltage + " " + param.endTime);

        // 3. 获取并打印第2组参数
        robot.WeldingGetProcessParam(2, param);
        System.out.println("the Num 2 process param is "
                + param.startCurrent + " " + param.startVoltage + " "
                + param.startTime + " " + param.weldCurrent + " "
                + param.weldVoltage + " " + param.endCurrent + " "
                + param.endVoltage + " " + param.endTime);

        // 4. 设置电流/电压关系并打印返回值
        WeldCurrentAORelation rela1 = new WeldCurrentAORelation(0, 400, 0, 10, 0);
        int rtn = robot.WeldingSetCurrentRelation(rela1);
        System.out.println("WeldingSetCurrentRelation rtn is: " + rtn);

        WeldVoltageAORelation rela2 = new WeldVoltageAORelation(0, 40, 0, 10, 1);
        rtn = robot.WeldingSetVoltageRelation(rela2);
        System.out.println("WeldingSetVoltageRelation rtn is: " + rtn);

        // 5. 获取并打印电流关系
        WeldCurrentAORelation rela3 = new WeldCurrentAORelation(0, 0, 0, 0, 0);
        rtn = robot.WeldingGetCurrentRelation(rela3);
        System.out.println("WeldingGetCurrentRelation rtn is: " + rtn);
        System.out.println("current min " + rela3.currentMin
                + " current max " + rela3.currentMax
                + " output vol min " + rela3.outputVoltageMin
                + " output vol max " + rela3.outputVoltageMax);

        // 6. 获取并打印电压关系
        WeldVoltageAORelation rela4 = new WeldVoltageAORelation(0, 0, 0, 0, 0);
        rtn = robot.WeldingGetVoltageRelation(rela4);
        System.out.println("WeldingGetVoltageRelation rtn is: " + rtn);
        System.out.println("vol min " + rela4.weldVoltageMin
                + " vol max " + rela4.weldVoltageMax
                + " output vol min " + rela4.outputVoltageMin
                + " output vol max " + rela4.outputVoltageMax);

        // 7. 设置电流/电压并打印返回值
        rtn = robot.WeldingSetCurrent(0, 100, 0, 0);
        System.out.println("WeldingSetCurrent rtn is: " + rtn);

        robot.Sleep(3000);  // 对应 this_thread::sleep_for(chrono::seconds(3))

        rtn = robot.WeldingSetVoltage(0, 10, 0, 0);
        System.out.println("WeldingSetVoltage rtn is: " + rtn);

        // 8. 设置摆动参数
        rtn = robot.WeaveSetPara(0, 0, 2.000000, 0, 10.000000, 0.000000, 0.000000, 0, 0, 0, 0, 0, 60.000000,0);
        System.out.println("rtn is: " + rtn);

        robot.WeaveOnlineSetPara(0, 0, 1, 0, 20, 0, 0, 0, 0);

        // 9. 设置断弧检测和重焊参数
        rtn = robot.WeldingSetCheckArcInterruptionParam(1, 200);
        System.out.println("WeldingSetCheckArcInterruptionParam  " + rtn);

        rtn = robot.WeldingSetReWeldAfterBreakOffParam(1, 5.7, 98.2, 0);
        System.out.println("WeldingSetReWeldAfterBreakOffParam  " + rtn);

        // 10. 获取并打印断弧检测参数
        List<Integer> inter = robot.WeldingGetCheckArcInterruptionParam();
        int checkEnable = inter.get(0);
        int arcInterruptTimeLength = inter.get(1);
        System.out.println("WeldingGetCheckArcInterruptionParam checkEnable " + checkEnable
                + "  arcInterruptTimeLength " + arcInterruptTimeLength);

        // 11. 获取并打印重焊参数（返回 List<Number>）
        List<Number> num = robot.WeldingGetReWeldAfterBreakOffParam();
        int enable = num.get(0).intValue();
        double length = num.get(1).doubleValue();
        double velocity = num.get(2).doubleValue();
        int moveType = num.get(3).intValue();
        System.out.printf("WeldingGetReWeldAfterBreakOffParam enable = %d, length = %f, velocity = %f, moveType = %d%n",
                enable, length, velocity, moveType);

        // 12. 设置扩展 DO 并循环控制
        robot.SetWeldMachineCtrlModeExtDoNum(17);
        for (int i = 0; i < 5; i++) {
            int[] mode = new int[1];   // 用于接收输出值

            robot.SetWeldMachineCtrlMode(0);
            rtn = robot.GetWeldMachineCtrlMode(mode);
            if (rtn == 0) {
                System.out.println("GetWeldMachineCtrlMode " + mode[0]);
            } else {
                System.out.println("GetWeldMachineCtrlMode failed, err: " + rtn);
            }
            robot.Sleep(1000);

            robot.SetWeldMachineCtrlMode(1);
            rtn = robot.GetWeldMachineCtrlMode(mode);
            if (rtn == 0) {
                System.out.println("GetWeldMachineCtrlMode " + mode[0]);
            } else {
                System.out.println("GetWeldMachineCtrlMode failed, err: " + rtn);
            }
            robot.Sleep(1000);
        }

        return 0;
    }


    public static int TestWelding(Robot robot)
    {
        robot.WeldingSetCurrent(0, 230, 0, 0);
        robot.WeldingSetVoltage(0, 24, 0, 1);

        DescPose p1Desc=new DescPose(-529.591,-77.247,204.008,-178.889,1.356,-14.668);
        JointPos p1Joint=new JointPos(-2.427,-73.309,113.912,-131.693,-91.373,102.241);

        DescPose p2Desc=new DescPose(-529.593,282.831,204.007,-178.890,1.357,-14.667);
        JointPos p2Joint=new JointPos(-37.804,-66.024,103.325,-128.984,-90.488,66.870);

        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);

        robot.MoveJ(p1Joint, p1Desc, 13, 0, 20, 100, 100, exaxisPos, -1, 0, offdese);
        robot.ARCStart(1, 0, 10000);
        robot.WeaveStart(0);
        robot.MoveL(p2Joint, p2Desc, 13, 0, 20, 100, 100, -1, 0, exaxisPos, 0, 0, offdese,100,0,1, 1);
        robot.ARCEnd(1, 0, 10000);
        robot.WeaveEnd(0);
        return 0;
    }

    public static int TestSegWeld(Robot robot)
    {
        robot.WeldingSetCurrent(0, 230, 0, 0);
        robot.WeldingSetVoltage(0, 24, 0, 1);

        DescPose p1Desc=new DescPose(-529.591,-77.247,204.008,-178.889,1.356,-14.668);
        JointPos p1Joint=new JointPos(-2.427,-73.309,113.912,-131.693,-91.373,102.241);

        DescPose p2Desc=new DescPose(-529.593,282.831,204.007,-178.890,1.357,-14.667);
        JointPos p2Joint=new JointPos(-37.804,-66.024,103.325,-128.984,-90.488,66.870);

        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);

        robot.GetForwardKin(p1Joint,p1Desc);
        robot.GetForwardKin(p2Joint,p2Desc);

        int rtn = robot.SegmentWeldStart(p1Desc, p2Desc, p1Joint, p2Joint, 20, 20, 0, 0, 5000, true,0, 0, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese);
        return 0;
    }

    public static int TestWeave(Robot robot)
    {
        DescPose p1Desc=new DescPose(-529.591,-77.247,204.008,-178.889,1.356,-14.668);
        JointPos p1Joint=new JointPos(-2.427,-73.309,113.912,-131.693,-91.373,102.241);

        DescPose p2Desc=new DescPose(-529.593,282.831,204.007,-178.890,1.357,-14.667);
        JointPos p2Joint=new JointPos(-37.804,-66.024,103.325,-128.984,-90.488,66.870);
        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);

        robot.MoveJ(p1Joint, p1Desc, 13, 0, 20, 100, 100, exaxisPos, -1, 0, offdese);
        robot.WeaveStartSim(0);
        robot.MoveL(p2Joint, p2Desc, 13, 0, 20, 100, 100, -1, 0, exaxisPos, 0, 0, offdese,100,0,1, 1);
        robot.WeaveEndSim(0);
        robot.MoveJ(p1Joint, p1Desc, 13, 0, 20, 100, 100, exaxisPos, -1, 0, offdese);
        robot.WeaveInspectStart(0);
        robot.MoveL(p2Joint, p2Desc, 13, 0, 20, 100, 100, -1, 0, exaxisPos, 0, 0, offdese, 100,0,1, 1);
        robot.WeaveInspectEnd(0);

        robot.WeldingSetVoltage(1, 19, 0, 0);
        robot.WeldingSetCurrent(1, 190, 0, 0);
        robot.MoveL(p1Joint, p1Desc, 13, 0, 100, 100, 50, -1,0, exaxisPos, 0, 0, offdese,100,0,1, 1);
        robot.ARCStart(1, 0, 10000);
        robot.ArcWeldTraceControl(1, 0, 1, 0.06, 5, 5, 60, 1, 0.06, 5, 5, 80, 0, 0, 4, 1, 10, 0, 0);
        robot.WeaveStart(0);
        robot.WeaveChangeStart(1, 0, 50, 30);
        robot.MoveL(p2Joint, p2Desc, 13, 0, 100, 100, 1, -1, 0,exaxisPos, 0, 0, offdese,100,0,1, 1);
        robot.WeaveChangeEnd();
        robot.WeaveEnd(0);
        robot.ArcWeldTraceControl(0, 0, 1, 0.06, 5, 5, 60, 1, 0.06, 5, 5, 80, 0, 0, 4, 1, 10, 0, 0);
        robot.ARCEnd(1, 0, 10000);
        return 0;
    }

    public static int TestSSHMd5(Robot robot)
    {
        String file_path= "/usr/local/etc/controller/lua/airlab.lua";
        String[] md5 =new String[]{""};

        String[] ssh_keygen=new String[]{""};
        int retval = robot.GetSSHKeygen(ssh_keygen);
        System.out.println(ssh_keygen[0]);

        String ssh_name = "fr";
        String ssh_ip = "192.168.58.45";
        String ssh_route = "/home/fr";
        String ssh_robot_url = "/root/robot/dhpara.config";
        retval = robot.SetSSHScpCmd(1, ssh_name, ssh_ip, ssh_route, ssh_robot_url);
        System.out.println("SetSSHScpCmd retval is:"+ retval);
        System.out.println("robot url is:"+ ssh_robot_url);

        robot.ComputeFileMD5(file_path, md5);
        System.out.println("md5 is:+"+ md5[0]);
        return 0;
    }

    public static int TestRealtimePeriod(Robot robot)
    {
        robot.SetRobotRealtimeStateSamplePeriod(100);
        List<Integer> getPeriod = new ArrayList<>();
        getPeriod=robot.GetRobotRealtimeStateSamplePeriod();
        System.out.println("get period is: "+ getPeriod);
        robot.Sleep(1000);

        return 0;
    }

    public static int TestUpgrade(Robot robot)
    {
        robot.SoftwareUpgrade("D://zUP/linux397/software.tar.gz", false);
        while (true)
        {
            List<Integer> inter=new ArrayList<>();
            inter=robot.GetSoftwareUpgradeState();
            System.out.println("upgrade state is:"+ inter.get(1));
            robot.Sleep(300);
        }
    }

    public static int TestPointTable(Robot robot)
    {
        String save_path = "D://zDOWN/";
        String point_table_name = "point_table_FR5.db";
        int rtn = robot.PointTableDownLoad(point_table_name, save_path);

        String upload_path = "D://zUP/point_table_FR5.db";
        rtn = robot.PointTableUpLoad(upload_path);

        String point_tablename = "point_table_FR5.db";
        String lua_name = "airlab.lua";
        String err="";
        rtn = robot.PointTableUpdateLua(point_tablename, lua_name,err);

        robot.CloseRPC();
        return 0;
    }

    public static int TestDownLoadRobotData(Robot robot)
    {
        int rtn = robot.RbLogDownload("D://zDOWN/");

        rtn = robot.AllDataSourceDownload("D://zDOWN/");

        rtn = robot.DataPackageDownload("D://zDOWN/");
        return 0;
    }

    public static int TestExtDIConfig(Robot robot)
    {
        robot.SetArcStartExtDoNum(10);
        robot.SetAirControlExtDoNum(20);
        robot.SetWireForwardFeedExtDoNum(30);
        robot.SetWireReverseFeedExtDoNum(40);

        robot.SetWeldReadyExtDiNum(50);
        robot.SetArcDoneExtDiNum(60);
        robot.SetExtDIWeldBreakOffRecover(70, 80);
        robot.SetWireSearchExtDIONum(0, 1);

        int[] DIConfig = new int[16];
        int[] DOConfig = new int[16];
        int rtn = robot.GetExtDIConfig(DIConfig);
        System.out.printf("GetExtDIConfig rtn is %d\n welder ready %d\narc done %d\nreweld start %d\nabort reweld %d\nwiresearch done %d\nLaser welding State %d\nlaser welding error state %d\n",
            rtn, DIConfig[0], DIConfig[1], DIConfig[2], DIConfig[3], DIConfig[4], DIConfig[5], DIConfig[6]);

        rtn = robot.GetExtDOConfig(DOConfig);
        System.out.printf("GetExtDOConfig rtn is %d\n Arc Start %d\nAir Test %d\nWire forward %d\nWire Inverse %d\nwiresearch %d\nWeld Mode %d\nlaser Enable %d\nLaser On %d\nLaser Reset Error %d\n",
            rtn, DOConfig[0], DOConfig[1], DOConfig[2], DOConfig[3], DOConfig[4], DOConfig[5], DOConfig[6], DOConfig[7], DOConfig[8]);



        return 0;
    }

    public static int TestArcWeldTrace(Robot robot)
    {
        JointPos mulitilineorigin1_joint=new JointPos(-2.427,-73.309,113.912,-131.693,-91.373,102.241);
        DescPose mulitilineorigin1_desc=new DescPose(-529.591,-77.247,204.008,-178.889,1.356,-14.668);

        DescTran mulitilineX1_desc=new DescTran(0,0,0);
        mulitilineX1_desc.x = -677.556;
        mulitilineX1_desc.y = 211.949;
        mulitilineX1_desc.z = -1.206;

        DescTran mulitilineZ1_desc=new DescTran(0,0,0);
        mulitilineZ1_desc.x = -677.564;
        mulitilineZ1_desc.y = 190.956;
        mulitilineZ1_desc.z = 19.817;

        JointPos mulitilinesafe_joint=new JointPos(-12.938,-58.855,91.909,-129.146,-90.652,66.633);
        DescPose mulitilinesafe_desc=new DescPose(-668.674,50.127,211.698,-176.978,5.333,10.535);
        JointPos mulitilineorigin2_joint=new JointPos(-37.804,-66.024,103.325,-128.984,-90.488,66.870);
        DescPose mulitilineorigin2_desc=new DescPose(-529.593,282.831,204.007,-178.890,1.357,-14.667);

        DescTran mulitilineX2_desc=new DescTran(0,0,0);
        mulitilineX2_desc.x = -563.965;
        mulitilineX2_desc.y = 220.355;
        mulitilineX2_desc.z = -0.680;

        DescTran mulitilineZ2_desc=new DescTran(0,0,0);
        mulitilineZ2_desc.x = -563.968;
        mulitilineZ2_desc.y = 215.362;
        mulitilineZ2_desc.z = 4.331;

        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);
        DescPose offset=new DescPose(0, 0, 0, 0, 0, 0);

        robot.Sleep(10);
        int error = robot.MoveJ(mulitilinesafe_joint, mulitilinesafe_desc, 13, 0, 10, 100, 100, epos, -1, 0, offset);

        error = robot.MoveL(mulitilineorigin1_joint, mulitilineorigin1_desc, 13, 0, 10, 100, 100, -1, 0,epos, 0, 0, offset, 100,0,1, 1);

        error = robot.MoveJ(mulitilinesafe_joint, mulitilinesafe_desc, 13, 0, 10, 100, 100, epos, -1, 0, offset);

        error = robot.MoveL(mulitilineorigin2_joint, mulitilineorigin2_desc, 13, 0, 10, 100, 100, -1, 0,epos, 0, 0, offset, 100,0,1, 1);

        error = robot.MoveJ(mulitilinesafe_joint, mulitilinesafe_desc, 13, 0, 10, 100, 100, epos, -1, 0, offset);

        error = robot.MoveL(mulitilineorigin1_joint, mulitilineorigin1_desc, 13, 0, 10, 100, 100, -1,0, epos, 0, 0, offset, 100,0,1, 1);

        error = robot.ARCStart(1, 0, 3000);

        error = robot.WeaveStart(0);

        error = robot.ArcWeldTraceControl(1, 0, 1, 0.06, 5, 5, 50, 1, 0.06, 5, 5, 55, 0, 0, 4, 1, 10,0,0);

        error = robot.MoveL(mulitilineorigin2_joint, mulitilineorigin2_desc, 13, 0, 1, 100, 100, -1, 0,epos, 0, 0,offset, 100,0,1, 1);

        error = robot.ArcWeldTraceControl(0, 0, 1, 0.06, 5, 5, 50, 1, 0.06, 5, 5, 55, 0, 0, 4, 1, 10,0,0);

        error = robot.WeaveEnd(0);

        error = robot.ARCEnd(1, 0, 10000);

        error = robot.MoveJ(mulitilinesafe_joint, mulitilinesafe_desc, 13, 0, 10, 100, 100, epos, -1, 0, offset);

        error = robot.MultilayerOffsetTrsfToBase(mulitilineorigin1_desc.tran, mulitilineX1_desc, mulitilineZ1_desc, 10.0, 0.0, 0.0, offset);

        error = robot.MoveL(mulitilineorigin1_joint, mulitilineorigin1_desc, 13, 0, 10, 100, 100, -1, 0,epos, 0, 1, offset, 100,0,1, 1);

        error = robot.ARCStart(1, 0, 3000);

        error = robot.MultilayerOffsetTrsfToBase(mulitilineorigin2_desc.tran, mulitilineX2_desc, mulitilineZ2_desc, 10, 0, 0, offset);

        error = robot.ArcWeldTraceReplayStart();

        error = robot.MoveL(mulitilineorigin2_joint, mulitilineorigin2_desc, 13, 0, 2, 100, 100, -1, 0,epos, 0, 1, offset, 100,0,1, 1);

        error = robot.ArcWeldTraceReplayEnd();

        error = robot.ARCEnd(1, 0, 10000);

        error = robot.MoveJ(mulitilinesafe_joint, mulitilinesafe_desc, 13, 0, 10, 100, 100, epos, -1, 0, offset);

        error = robot.MultilayerOffsetTrsfToBase(mulitilineorigin1_desc.tran, mulitilineX1_desc, mulitilineZ1_desc, 0, 10, 0, offset);

        error = robot.MoveL(mulitilineorigin1_joint, mulitilineorigin1_desc, 13, 0, 10, 100, 100, -1,0, epos, 0, 1, offset, 100,0,1, 1);

        error = robot.ARCStart(1, 0, 3000);

        error = robot.MultilayerOffsetTrsfToBase(mulitilineorigin2_desc.tran, mulitilineX2_desc, mulitilineZ2_desc, 0, 10, 0, offset);

        error = robot.ArcWeldTraceReplayStart();

        error = robot.MoveL(mulitilineorigin2_joint, mulitilineorigin2_desc, 13, 0, 2, 100, 100, -1,0, epos, 0, 1, offset, 100,0,1, 1);

        error = robot.ArcWeldTraceReplayEnd();

        error = robot.ARCEnd(1, 0, 3000);

        error = robot.MoveJ(mulitilinesafe_joint, mulitilinesafe_desc, 13, 0, 10, 100, 100, epos, -1, 0, offset);

        robot.CloseRPC();
        return 0;
    }

    public static int TestWireSearch(Robot robot)
    {
//        DescPose toolCoord=new DescPose(0, 0, 0, 0, 0, 0);
//        robot.SetToolCoord(0, toolCoord, 0, 0, 0, 0);
//        DescPose wobjCoord=new DescPose(0, 0, 0, 0, 0, 0);
//        robot.SetWObjCoord(0, wobjCoord, 0);

        int rtn0, rtn1, rtn2 = 0;
        ExaxisPos exaxisPos = new ExaxisPos( 0, 0, 0, 0 );
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);


        DescPose descStart = new DescPose(-529.591,-77.247,204.008,-178.889,1.356,-14.668);
        JointPos jointStart = new JointPos(-2.427,-73.309,113.912,-131.693,-91.373,102.241);

        DescPose descEnd =new DescPose(-529.593,282.831,204.007,-178.890,1.357,-14.667);
        JointPos jointEnd =new JointPos(-37.804,-66.024,103.325,-128.984,-90.488,66.870);




        robot.MoveL(jointStart, descStart, 0, 0, 100, 100, 100, -1,0, exaxisPos, 0, 0, offdese,100,0,1, 1);
        robot.MoveL(jointEnd, descEnd, 0, 0, 100, 100, 100, -1, 0,exaxisPos, 0, 0, offdese,100,0,1, 1);

//        DescPose descREF0A = new DescPose(-649.607,-78.776,204.009,-178.889,1.356,-14.668);
//        JointPos jointREF0A =new JointPos(-126.794, -100.834, 128.922, -119.864, -89.218, 74.302);
//
//        DescPose descREF0B = new DescPose(-649.607,-48.776,204.009,-178.889,1.356,-14.668);
//        JointPos jointREF0B = new JointPos(-130.413, -81.093, 112.044, -123.163, -89.217, 74.303);
//
//        DescPose descREF1A =new DescPose(-649.625,93.316,203.999,-178.890,1.355,-14.667);
//        JointPos jointREF1A =new JointPos(-113.231, -83.815, 119.877, -129.092, -89.217, 74.303);
//
//        DescPose descREF1B =new DescPose(-649.625,123.316,203.999,-178.890,1.355,-14.667);
//        JointPos jointREF1B = new JointPos(-119.088, -69.676, 98.692, -121.761, -89.219, 74.303);

        DescPose descREF0A = new DescPose(-529.591,-77.247,204.008,-178.889,1.356,-14.668);
        JointPos jointREF0A =new JointPos(-2.427,-73.309,113.912,-131.693,-91.373,102.241);

        DescPose descREF0B =new DescPose(-529.593,282.831,204.007,-178.890,1.357,-14.667);
        JointPos jointREF0B =new JointPos(-37.804,-66.024,103.325,-128.984,-90.488,66.870);

        DescPose descREF1A = new DescPose(-529.591,-77.247,204.008,-178.889,1.356,-14.668);
        JointPos jointREF1A =new JointPos(-2.427,-73.309,113.912,-131.693,-91.373,102.241);

        DescPose descREF1B =new DescPose(-529.593,282.831,204.007,-178.890,1.357,-14.667);
        JointPos jointREF1B =new JointPos(-37.804,-66.024,103.325,-128.984,-90.488,66.870);

        rtn0 = robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
        robot.MoveL(jointREF0A, descREF0A, 0, 0, 100, 100, 100, -1,0, exaxisPos, 0, 0, offdese,100,0,1, 1);  //起点
        robot.MoveL(jointREF0B, descREF0B, 0, 0, 100, 100, 100, -1,0, exaxisPos, 1, 0, offdese,100,0,1, 1);  //方向点
        rtn1 = robot.WireSearchWait("REF0");
        rtn2 = robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);

        rtn0 = robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
        robot.MoveL(jointREF1A, descREF1A, 0, 0, 100, 100, 100, -1, 0,exaxisPos, 0, 0, offdese,100,0,1, 1);  //起点
        robot.MoveL(jointREF1B, descREF1B, 0, 0, 100, 100, 100, -1,0, exaxisPos, 1, 0, offdese,100,0,1, 1);  //方向点
        rtn1 = robot.WireSearchWait("REF1");
        rtn2 = robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);

        rtn0 = robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
        robot.MoveL(jointREF0A, descREF0A, 0, 0, 100, 100, 100, -1,0, exaxisPos, 0, 0, offdese,100,0,1, 10);  //起点
        robot.MoveL(jointREF0B, descREF0B, 0, 0, 100, 100, 100, -1,0, exaxisPos, 1, 0, offdese,100,0,1, 1);  //方向点
        rtn1 = robot.WireSearchWait("RES0");
        rtn2 = robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);

        rtn0 = robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
        robot.MoveL(jointREF1A, descREF1A, 0, 0, 100, 100, 100, -1, 0,exaxisPos, 0, 0, offdese,100,0,1, 1);  //起点
        robot.MoveL(jointREF1B, descREF1B, 0, 0, 100, 100, 100, -1, 0,exaxisPos, 1, 0, offdese,100,0,1, 1);  //方向点
        rtn1 = robot.WireSearchWait("RES1");
        rtn2 = robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);

        String[] varNameRef =new String[]{"REF0", "REF1", "#", "#", "#", "#"};
        String[] varNameRes = new String[]{ "RES0", "RES1", "#", "#", "#", "#" };
        int offectFlag = 0;

        DescPose pos = new DescPose(0,0,0,0,0,0);
        DescOffset offectPos=new DescOffset();
        offectPos.offset=pos;
        offectPos.offsetFlag=0;

        rtn0 = robot.GetWireSearchOffset(0, 0, varNameRef, varNameRes, offectPos);
        robot.PointsOffsetEnable(0, pos);
        robot.MoveL(jointStart, descStart, 0, 0, 100, 100, 100, -1,0, exaxisPos, 0, 0, offdese,100,0,1, 1);
        robot.MoveL(jointEnd, descEnd, 0, 0, 100, 100, 100, -1, 0,exaxisPos, 1, 0, offdese,100,0,1, 1);
        robot.PointsOffsetDisable();

        robot.CloseRPC();
        return 0;
    }

    public static int TestFTInit(Robot robot)//力控
    {
        DescTran tr1=new DescTran(0,0,0);
        robot.SetForceSensorPayload(0);
        robot.SetForceSensorPayloadCog(tr1);

        int company = 22;
        int device = 0;
        int softversion = 0;
        int bus = 1;
        int index = 1;

        DeviceConfig con=new DeviceConfig(company,device,softversion,bus);
        robot.FT_SetConfig(con);
        robot.Sleep(1000);
        robot.FT_GetConfig(con);
        robot.Sleep(1000);

        robot.FT_Activate(0);
        robot.Sleep(1000);
        robot.FT_Activate(1);
        robot.Sleep(1000);

        robot.Sleep(1000);
        robot.FT_SetZero(0);
        robot.Sleep(1000);

        ForceTorque ft=new ForceTorque(0,0,0,0,0,0);
        robot.FT_GetForceTorqueOrigin(0, ft);
        robot.FT_SetZero(1);
        robot.Sleep(1000);

        DescPose ftCoord = new DescPose();
        robot.FT_SetRCS(0, ftCoord);

        robot.SetForceSensorPayload(0.824);

        DescTran tr=new DescTran(0.778, 2.554, 48.765);
        robot.SetForceSensorPayloadCog(tr);
        List<Number> weight = new ArrayList<>();
        double x = 0, y = 0, z = 0;
        weight=robot.GetForceSensorPayload();
        robot.GetForceSensorPayloadCog(tr);
        tr.x=0;
        tr.y=0;
        tr.z=0;
        robot.SetForceSensorPayload(0);
        robot.SetForceSensorPayloadCog(tr);

        double computeWeight = 0;
        DescTran tran = new DescTran();
        MassCenter mass=new MassCenter();
        mass.weight=weight.get(1).doubleValue();
        mass.cog=tran;
        robot.ForceSensorAutoComputeLoad(mass);

        return 0;
    }

    public static int TestFTLoadCompute(Robot robot)//力传感器负载辨识
    {
        DescTran tr1=new DescTran(0,0,0);
        robot.SetForceSensorPayload(0);
        robot.SetForceSensorPayloadCog(tr1);

        int company = 22;
        int device = 0;
        int softversion = 0;
        int bus = 1;
        int index = 1;

        DeviceConfig con=new DeviceConfig(company, device, softversion, bus);
        robot.FT_SetConfig(con);
        robot.Sleep(1000);
        robot.FT_GetConfig(con);
        robot.Sleep(1000);

        robot.FT_Activate(0);
        robot.Sleep(1000);
        robot.FT_Activate(1);
        robot.Sleep(1000);

        robot.Sleep(1000);
        robot.FT_SetZero(0);
        robot.Sleep(1000);

        ForceTorque ft=new ForceTorque(0,0,0,0,0,0);
        robot.FT_GetForceTorqueOrigin(0, ft);
        robot.FT_SetZero(1);
        robot.Sleep(1000);

        DescPose tcoord = new DescPose();
        tcoord.tran.z = 35.0;
        robot.SetToolCoord(10, tcoord, 1, 0, 0, 0);

        robot.FT_PdIdenRecord(10);
        robot.Sleep(1000);

        List<Number> weight =new ArrayList<>();
        weight=robot.FT_PdIdenCompute();

        DescPose desc_p1=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose desc_p2=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);
        DescPose desc_p3=new DescPose(-327.622, 402.230, 320.402, -178.067, 2.127, -46.207);

        robot.MoveCart(desc_p1, 0, 0, 100.0, 100.0, 100.0, -1.0, -1);
        robot.Sleep(1000);
        robot.FT_PdCogIdenRecord(10, 1);
        robot.MoveCart(desc_p2, 0, 0, 100.0, 100.0, 100.0, -1.0, -1);
        robot.Sleep(1000);
        robot.FT_PdCogIdenRecord(10, 2);
        robot.MoveCart(desc_p3, 0, 0, 100.0, 100.0, 100.0, -1.0, -1);
        robot.Sleep(1000);
        robot.FT_PdCogIdenRecord(10, 3);
        robot.Sleep(1000);
        DescTran cog=new DescTran(0,0,0);
        robot.FT_PdCogIdenCompute(cog);

        robot.CloseRPC();
        return 0;
    }

    public static int TestFTGuard(Robot robot)
    {
        DescTran tr1=new DescTran(0,0,0);
        robot.SetForceSensorPayload(0);
        robot.SetForceSensorPayloadCog(tr1);

        int company = 22;
        int device = 0;
        int softversion = 0;
        int bus = 1;
        int index = 1;

        DeviceConfig con=new DeviceConfig(company, device, softversion, bus);
        robot.FT_SetConfig(con);
        robot.Sleep(1000);
        robot.FT_GetConfig(con);
        robot.Sleep(1000);

        robot.FT_Activate(0);
        robot.Sleep(1000);
        robot.FT_Activate(1);
        robot.Sleep(1000);

        robot.Sleep(1000);
        robot.FT_SetZero(0);
        robot.Sleep(1000);

        int sensor_id = 1;
        Object[] select =new Object[] { 1,1,1,1,1,1 };
        Object[] max_threshold = new Object[]{ 10.0,10.0,10.0,10.0,10.0,10.0 };
        Object[] min_threshold = new Object[]{ 5.0,5.0,5.0,5.0,5.0,5.0 };

        ForceTorque ft=new ForceTorque(0,0,0,0,0,0);
        DescPose desc_p1=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose desc_p2=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);
        DescPose desc_p3=new DescPose(-327.622, 402.230, 320.402, -178.067, 2.127, -46.207);

        robot.FT_Guard(1, sensor_id, select, ft,max_threshold,min_threshold);
        robot.MoveCart(desc_p1, 0, 0, 100.0, 100.0, 100.0, -1.0, -1);
        robot.MoveCart(desc_p2, 0, 0, 100.0, 100.0, 100.0, -1.0, -1);
        robot.MoveCart(desc_p3, 0, 0, 100.0, 100.0, 100.0, -1.0, -1);

        robot.FT_Guard(0, sensor_id, select, ft, max_threshold, min_threshold);
        return 0;
    }

    public static int TestFTControl(Robot robot)
    {
        DescTran tr1=new DescTran(0,0,0);
        robot.SetForceSensorPayload(0);
        robot.SetForceSensorPayloadCog(tr1);

        int company = 22;
        int device = 0;
        int softversion = 0;
        int bus = 1;
        int index = 1;
        DeviceConfig con=new DeviceConfig(company, device, softversion, bus);

        robot.FT_SetConfig(con);
        robot.Sleep(1000);
        robot.FT_GetConfig(con);
        robot.Sleep(1000);

        robot.FT_Activate(0);
        robot.Sleep(1000);
        robot.FT_Activate(1);
        robot.Sleep(1000);

        robot.Sleep(1000);
        robot.FT_SetZero(0);
        robot.Sleep(1000);

        int sensor_id = 1;
        int[] select =new int[] { 0,0,1,0,0,0 };
        double[] ft_pid =new double[]{ 0.0005,0.0,0.0,0.0,0.0,0.0 };
        int adj_sign = 0;
        int ILC_sign = 0;
        double max_dis = 100.0;
        double max_ang = 0.0;

        ForceTorque ft=new ForceTorque(0,0,0,0,0,0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);
        JointPos j1=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos j2=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);
        DescPose desc_p1=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose desc_p2=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);
        DescPose offset_pos=new DescPose(0, 0, 0, 0, 0, 0);

        ft.fz = -10.0;

        int rtn = robot.MoveJ(j1, desc_p1, 0, 0, 100.0, 180.0, 100.0, epos, -1.0, 0, offset_pos);
        robot.FT_Control(1, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang,0,0,0);
        rtn = robot.MoveJ(j2, desc_p2, 0, 0, 100.0, 180.0, 100.0, epos, -1.0, 0, offset_pos);

//        rtn = robot.MoveJ(j2, desc_p2, 0, 0, 100.0, 180.0, 20.0, -1.0,0, epos, 0, 0, offset_pos);
        robot.FT_Control(0, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang,0,0,0);
        return 0;
    }

    public static int TestFTSearch(Robot robot)
    {
        int company = 22;
        int device = 0;
        int softversion = 0;
        int bus = 1;
        int index = 1;

        DeviceConfig conn=new DeviceConfig(company, device, softversion, bus);
        robot.FT_SetConfig(conn);
        robot.Sleep(1000);
        robot.FT_GetConfig(conn);
        robot.Sleep(1000);

        robot.FT_Activate(0);
        robot.Sleep(1000);
        robot.FT_Activate(1);
        robot.Sleep(1000);

        robot.Sleep(1000);
        robot.FT_SetZero(0);
        robot.Sleep(1000);

        //恒力参数
        int status = 1;  //恒力控制开启标志，0-关，1-开
        int sensor_num = 1; //力传感器编号
        double[] gain = new double[]{ 0.0001,0.0,0.0,0.0,0.0,0.0 };  //最大阈值
        int adj_sign = 0;  //自适应启停状态，0-关闭，1-开启
        int ILC_sign = 0;  //ILC控制启停状态，0-停止，1-训练，2-实操
        double max_dis = 100.0;  //最大调整距离
        double max_ang = 5.0;  //最大调整角度

        ForceTorque ft=new ForceTorque(0,0,0,0,0,0);

        //螺旋线探索参数
        int rcs = 0;  //参考坐标系，0-工具坐标系，1-基坐标系
        double dr = 0.7;  //每圈半径进给量，单位mm
        double fFinish = 1.0; //力或力矩阈值（0~100），单位N或Nm
        double t = 60000.0; //最大探索时间，单位ms
        double vmax = 3.0; //线速度最大值，单位mm/s

        //直线插入参数
        double force_goal = 20.0;  //力或力矩阈值（0~100），单位N或Nm
        double lin_v = 0.0; //直线速度，单位mm/s
        double lin_a = 0.0; //直线加速度，单位mm/s^2,暂不使用
        double disMax = 100.0; //最大插入距离，单位mm
        int linorn = 1; //插入方向，1-正方向，2-负方向

        //旋转插入参数
        double angVelRot = 2.0;  //旋转角速度，单位°/s
        double forceInsertion = 1.0; //力或力矩阈值（0~100），单位N或Nm
        int angleMax = 45; //最大旋转角度，单位°
        int orn = 1; //力的方向，1-fz,2-mz
        double angAccmax = 0.0; //最大旋转角加速度，单位°/s^2,暂不使用
        int rotorn = 1; //旋转方向，1-顺时针，2-逆时针

        int[] select1 =new int[] { 0,0,1,1,1,0 }; //六个自由度选择[fx,fy,fz,mx,my,mz]，0-不生效，1-生效
        ft.fz = -10.0;
        robot.FT_Control(status, sensor_num, select1, ft, gain, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);
//        rtn = robot.FT_SpiralSearch(rcs, dr, fFinish, t, vmax);
        status = 0;
        robot.FT_Control(status, sensor_num, select1, ft, gain, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);

        int[] select2 =new int[] { 1,1,1,0,0,0 };  //六个自由度选择[fx,fy,fz,mx,my,mz]，0-不生效，1-生效
        gain[0] = 0.00005;
        ft.fz = -30.0;
        status = 1;
        robot.FT_Control(status, sensor_num, select2, ft, gain, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);
//        rtn = robot.FT_LinInsertion(rcs, force_goal, lin_v, lin_a, disMax, linorn);
        status = 0;
        robot.FT_Control(status, sensor_num, select2, ft, gain, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);

        int[] select3 =new int[] { 0,0,1,1,1,0 };  //六个自由度选择[fx,fy,fz,mx,my,mz]，0-不生效，1-生效
        ft.fz = -10.0;
        gain[0] = 0.0001;
        status = 1;
        robot.FT_Control(status, sensor_num, select3, ft, gain, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);
//        rtn = robot.FT_RotInsertion(rcs, angVelRot, forceInsertion, angleMax, orn, angAccmax, rotorn);
        status = 0;
        robot.FT_Control(status, sensor_num, select3, ft, gain, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);

        int[] select4 = new int[]{ 1,1,1,0,0,0 };  //六个自由度选择[fx,fy,fz,mx,my,mz]，0-不生效，1-生效
        ft.fz = -30.0;
        status = 1;
        robot.FT_Control(status, sensor_num, select4, ft, gain, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);
//        rtn = robot.FT_LinInsertion(rcs, force_goal, lin_v, lin_a, disMax, linorn);
        status = 0;
        robot.FT_Control(status, sensor_num, select4, ft, gain, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);

        robot.CloseRPC();
        return 0;
    }

    public static int TestSurface(Robot robot)
    {
        int company = 22;
        int device = 0;
        int softversion = 0;
        int bus = 1;
        int index = 1;

        DeviceConfig con=new DeviceConfig(company, device, softversion, bus);
        robot.FT_SetConfig(con);
        robot.Sleep(1000);
        robot.FT_GetConfig(con);
        robot.Sleep(1000);

        robot.FT_Activate(0);
        robot.Sleep(1000);
        robot.FT_Activate(1);
        robot.Sleep(1000);

        robot.Sleep(1000);
        robot.FT_SetZero(0);
        robot.Sleep(1000);

        int rcs = 0;
        int dir = 1;
        int axis = 1;
        double lin_v = 3.0;
        double lin_a = 0.0;
        double maxdis = 50.0;
        double ft_goal = 2.0;
        DescPose desc_pos=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose xcenter=new DescPose(0, 0, 0, 0, 0, 0);
        DescPose ycenter=new DescPose(0, 0, 0, 0, 0, 0);

        ForceTorque ft=new ForceTorque(0,0,0,0,0,0);

        ft.fx = -2.0;

        robot.MoveCart(desc_pos, 1, 0, 100.0, 100.0, 100.0, -1.0, -1);

        robot.FT_CalCenterStart();
        robot.FT_FindSurface(rcs, dir, axis, lin_v, lin_a, maxdis, ft_goal);
        robot.MoveCart(desc_pos, 1, 0, 100.0, 100.0, 100.0, -1.0, -1);
        robot.WaitMs(1000);

        dir = 2;
        robot.FT_FindSurface(rcs, dir, axis, lin_v, lin_a, maxdis, ft_goal);
        robot.FT_CalCenterEnd(xcenter);
//        printf("xcenter:%f,%f,%f,%f,%f,%f\n", xcenter.tran.x, xcenter.tran.y, xcenter.tran.z, xcenter.rpy.rx, xcenter.rpy.ry, xcenter.rpy.rz);
        robot.MoveCart(xcenter, 1, 0, 60.0, 50.0, 50.0, -1.0, -1);

        robot.FT_CalCenterStart();
        dir = 1;
        axis = 2;
        lin_v = 6.0;
        maxdis = 150.0;
        robot.FT_FindSurface(rcs, dir, axis, lin_v, lin_a, maxdis, ft_goal);
        robot.MoveCart(desc_pos, 1, 0, 100.0, 100.0, 100.0, -1.0, -1);
        robot.WaitMs(1000);

        dir = 2;
        robot.FT_FindSurface(rcs, dir, axis, lin_v, lin_a, maxdis, ft_goal);
        robot.FT_CalCenterEnd(ycenter);
//        printf("ycenter:%f,%f,%f,%f,%f,%f\n", ycenter.tran.x, ycenter.tran.y, ycenter.tran.z, ycenter.rpy.rx, ycenter.rpy.ry, ycenter.rpy.rz);
        robot.MoveCart(ycenter, 1, 0, 60.0, 50.0, 50.0, 0.0, -1);
        return 0;
    }

    public static int TestCompliance(Robot robot)
    {
        DescTran tr1=new DescTran(0,0,0);
        robot.SetForceSensorPayload(0);
        robot.SetForceSensorPayloadCog(tr1);

        int company = 22;
        int device = 0;
        int softversion = 0;
        int bus = 1;
        int index = 1;

        DeviceConfig con=new DeviceConfig(company, device, softversion, bus);
        robot.FT_SetConfig(con);
        robot.Sleep(1000);
        robot.FT_GetConfig(con);

        robot.Sleep(1000);

        robot.FT_Activate(0);
        robot.Sleep(1000);
        robot.FT_Activate(1);
        robot.Sleep(1000);

        robot.Sleep(1000);
        robot.FT_SetZero(0);
        robot.Sleep(1000);

        int flag = 1;
        int sensor_id = 1;
        int[] select =new int[] { 1,1,1,0,0,0 };
        double[] ft_pid =new double[] { 0.0005,0.0,0.0,0.0,0.0,0.0 };
        int adj_sign = 0;
        int ILC_sign = 0;
        double max_dis = 100.0;
        double max_ang = 0.0;
        double[] M =new double[] {2.0, 2.0};
        double[] B =new double[] {15.0, 15.0};
        double[] threshold =new double[] {1.0, 1.0};
        double[] adjustCoeff =new double[] {1.0, 0.8};
        ForceTorque ft=new ForceTorque(0,0,0,0,0,0);
        DescPose  offset_pos=new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos=new ExaxisPos(0, 0, 0, 0);


        JointPos j1=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos j2=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);
        DescPose desc_p1=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose desc_p2=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);

        ft.fx = -10.0;
        ft.fy = -10.0;
        ft.fz = -10.0;
//        robot.FT_Control(flag, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);
        robot.FT_Control(1, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, M, B, threshold,
                adjustCoeff, 0, 0, 1, 0);
        double p = 0.00005;
        double force = 30.0;
        int rtn = robot.FT_ComplianceStart(p, force);

        int count = 15;
        while (count>0)
        {
            robot.MoveL(j1, desc_p1, 0, 0, 100.0, 100.0, 100.0, -1.0,0, epos, 0, 0, offset_pos,100,0,0,10);
            robot.MoveL(j2, desc_p2, 0, 0, 100.0, 100.0, 100.0, -1.0,0, epos, 0, 0, offset_pos,100,0,0,10);

            count -= 1;
        }
        robot.FT_ComplianceStop();
        flag = 0;
//        robot.FT_Control(flag, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);
        robot.FT_Control(0, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, M, B, threshold,
                adjustCoeff, 0, 0, 1, 0);
        robot.CloseRPC();
        return 0;
    }

    public static int TestEndForceDragCtrl(Robot robot)
    {
        DescTran tr1=new DescTran(0,0,0);
        robot.SetForceSensorPayload(0);
        robot.SetForceSensorPayloadCog(tr1);

        robot.SetForceSensorDragAutoFlag(1);

        Object[] M =new Object[] { 15.0, 15.0, 15.0, 0.5, 0.5, 0.1 };
        Object[] B =new Object[] { 150.0, 150.0, 150.0, 5.0, 5.0, 1.0 };
        Object[] K =new Object[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        Object[] F =new Object[] { 10.0, 10.0, 10.0, 1.0, 1.0, 1.0 };
        robot.EndForceDragControl(1, 0, 0, 0, M, B, K, F, 50, 100);

        robot.Sleep(10000);

        int dragState = 0;
        int sixDimensionalDragState = 0;
        List<Integer> state=new ArrayList<>();
        state=robot.GetForceAndTorqueDragState();

        robot.EndForceDragControl(0, 0, 0, 0, M, B, K, F, 50, 100);
        return 0;
    }

    public static int TestForceAndJointImpedance(Robot robot)//六维力
    {
        robot.DragTeachSwitch(1);
        Object[] lamdeDain =new Object[] { 3.0, 2.0, 2.0, 2.0, 2.0, 3.0 };
        Object[] KGain = new Object[]{ 0, 0, 0, 0, 0, 0 };
        Object[] BGain =new Object[] { 150, 150, 150, 5.0, 5.0, 1.0 };
        int rtn = robot.ForceAndJointImpedanceStartStop(1, 0, lamdeDain, KGain, BGain, 1000.0, 180.0);

        robot.Sleep(10000);

        robot.DragTeachSwitch(0);
        rtn = robot.ForceAndJointImpedanceStartStop(0, 0, lamdeDain, KGain, BGain, 1000.0, 180.0);

        robot.CloseRPC();
        return 0;
    }

    public static int TestUDPAxisCalib(Robot robot)
    {
//        UDPComParam para1=new UDPComParam("192.168.58.88", 2021, 2, 100, 3, 200, 1, 100, 5, 1);
//
//        int rtn = robot.ExtDevSetUDPComParam(para1);
//        String ip = ""; int port = 0; int period = 0; int lossPkgTime = 0; int lossPkgNum = 0; int disconnectTime = 0; int reconnectEnable = 0; int reconnectPeriod = 0; int reconnectNum = 0;
//        UDPComParam para2=new UDPComParam(ip, port, period, lossPkgTime, lossPkgNum, disconnectTime, reconnectEnable, reconnectPeriod, reconnectNum,0);
//
//        rtn = robot.ExtDevGetUDPComParam(para2);
//
//        robot.ExtDevLoadUDPDriver();
//
//        rtn = robot.ExtAxisServoOn(1, 1);
//        rtn = robot.ExtAxisServoOn(2, 1);
//        robot.Sleep(2000);

//        robot.ExtAxisSetHoming(1, 0, 10, 2);
//        robot.Sleep(2000);
//        rtn = robot.ExtAxisSetHoming(2, 0, 10, 2);

        robot.Sleep(4000);
        int rtn =0;
        rtn = robot.SetRobotPosToAxis(1);
        rtn = robot.SetAxisDHParaConfig(1, 128.5, 206.4,  0, 0, 0, 0, 0, 0);
        rtn = robot.ExtAxisParamConfig(1, 1, 1, 1000, -1000, 1000, 1000, 1.905, 262144, 200, 1, 0, 0);
        rtn = robot.ExtAxisParamConfig(2, 1, 1, 1000, -1000, 1000, 1000, 4.444, 262144, 200, 1, 0, 0);

        DescPose toolCoord=new DescPose(0, 0, 300, 0, 0, 0);
        robot.SetToolCoord(1, toolCoord, 0, 0, 1, 0);

        JointPos jSafe=new JointPos(47.434, -74.061, -46.445, -140.394, 52.175, 108.040);
        JointPos j1=new JointPos(46.778, -75.370, -45.376, -140.058, 51.582, 108.038);
        JointPos j2=new JointPos(26.821, -79.971, -41.801, -124.459, 65.051, 108.036);
        JointPos j3=new JointPos(26.709, -82.025, -39.224, -124.958, 64.560, 108.035);
        JointPos j4=new JointPos(27.177, -82.909, -38.352, -124.937, 63.591, 108.035);

        DescPose descSafe =new DescPose(0,0,0,0,0,0);
        DescPose desc1 = new DescPose(0,0,0,0,0,0);
        DescPose desc2 = new DescPose(0,0,0,0,0,0);
        DescPose desc3 = new DescPose(0,0,0,0,0,0);
        DescPose desc4 = new DescPose(0,0,0,0,0,0);
        ExaxisPos exaxisPos =new ExaxisPos(0,0,0,0);
        DescPose offdese =new DescPose(0, 0, 0, 0, 0, 0);

        robot.GetForwardKin(jSafe, descSafe);
        robot.MoveJ(jSafe, descSafe, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.Sleep(2000);

        robot.GetForwardKin(j1, desc1);
        robot.MoveJ(j1, desc1, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.Sleep(2000);

        DescPose actualTCPPos =new DescPose(0,0,0,0,0,0);

        robot.GetActualTCPPose(actualTCPPos);
        robot.SetRefPointInExAxisEnd(actualTCPPos);
        rtn = robot.PositionorSetRefPoint(1);
        robot.Sleep(2000);

        robot.MoveJ(jSafe, descSafe, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.ExtAxisStartJog(1, 0, 50, 50, 10);
        robot.Sleep(1000);
        robot.ExtAxisStartJog(2, 1, 50, 50, 5);
        robot.Sleep(1000);
        robot.GetForwardKin(j2, desc2);
        rtn = robot.MoveJ(j2, desc2, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        rtn = robot.PositionorSetRefPoint(2);
        robot.Sleep(2000);

        robot.MoveJ(jSafe, descSafe, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.ExtAxisStartJog(1, 0, 50, 50, 10);
        robot.Sleep(1000);
        robot.ExtAxisStartJog(2, 1, 50, 50, 5);
        robot.Sleep(1000);
        robot.GetForwardKin(j3, desc3);
        robot.MoveJ(j3, desc3, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        rtn = robot.PositionorSetRefPoint(3);
        robot.Sleep(2000);

        robot.MoveJ(jSafe, descSafe, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.ExtAxisStartJog(1, 0, 50, 50, 10);
        robot.Sleep(1000);
        robot.ExtAxisStartJog(2, 1, 50, 50, 5);
        robot.Sleep(1000);
        robot.GetForwardKin(j4, desc4);
        robot.MoveJ(j4, desc4, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        rtn = robot.PositionorSetRefPoint(4);
        robot.Sleep(2000);

        DescPose axisCoord = new DescPose();
        robot.PositionorComputeECoordSys(axisCoord);
        robot.MoveJ(jSafe, descSafe, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        rtn = robot.ExtAxisActiveECoordSys(3, 3, axisCoord, 1);

        robot.CloseRPC();
        return 0;
    }

    public static int TestUDPAxisMove(Robot robot)
    {
        ExaxisPos exaxisPos = new ExaxisPos( 20, 0, 0, 0 );
        robot.ExtAxisMove(exaxisPos,40);
        robot.CloseRPC();
        return 0;
    }

    public static void TestUDPAxisSync(Robot robot)
    {
        JointPos joint_safe = new JointPos(33.513, -89.540, -19.754, -135.044, 64.077, 107.990);
        JointPos joint_pos1 = new JointPos(60.164, -104.046, -20.299, -157.828, 53.871, 108.125);
        JointPos joint_pos2 = new JointPos(58.054, -107.816, -15.798, -153.559, 49.501, 108.121);
        JointPos joint_pos3 = new JointPos(55.266, -89.767, -46.349, -128.985, 45.001, 108.13);
        //JointPos joint_pos4 = new JointPos(113.734, -76.571, 78.254, -93.291, -91.100, -49.867);
        //JointPos joint_pos5 = new JointPos(116.544, -63.563, 63.923, -88.894, -83.999, -49.869);

        DescPose desc_safe = new DescPose(423.659, -51.518, 366.413, -163.442, 32.248, -165.661);
        DescPose desc_pos1 = new DescPose(409.950, 35.714, 272.466, -142.158, -1.209, -134.392);
        DescPose desc_pos2 = new DescPose(456.062, 47.663, 291.916, -139.201, 4.688, -135.673);
        DescPose desc_pos3 = new DescPose(485.838, 25.316, 313.259, -137.616, 17.480, -138.072);
        //DescPose desc_pos4 = new DescPose(330.540, -517.439, 156.875, -179.804, -1.939, -106.418);
        //DescPose desc_pos5 = new DescPose(430.747, -540.471, 129.780, -176.351, 4.987, -103.505);

        ExaxisPos eposSafe = new ExaxisPos(35.00, 25.00, 0.000, 0.000);
        ExaxisPos epos1 = new ExaxisPos(35.00, 25.00, 0.000, 0.000);
        ExaxisPos epos2 = new ExaxisPos(35.00, -25.000, 0.000, 0.000);
        ExaxisPos epos3 = new ExaxisPos(35.00, -60.000, 0.000, 0.000);
        ExaxisPos epos4 = new ExaxisPos(35.00, 0.000, 0.000, 0.000);
        ExaxisPos epos5 = new ExaxisPos(35.00, 0.000, 0.000, 0.000);

        DescPose offset_pos = new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        int tool = 1, user = 0, vel = 100, acc = 100, ovl = 100, blendT = -1;
        int rtn = 0;

        // moveJ
        robot.MoveJ(joint_safe, tool, user, vel, acc, ovl, eposSafe, blendT, 0, offset_pos);
        robot.ExtAxisMove(eposSafe, 100, -1);
        rtn = robot.ExtAxisSyncMoveJ(joint_pos1, tool, user, vel, acc, ovl, epos1, blendT, 0, offset_pos);
        rtn = robot.ExtAxisSyncMoveJ(joint_pos3, tool, user, vel, acc, ovl, epos2, blendT, 0, offset_pos);
        System.out.println("ExtAxisSyncMoveJ rtn is: " + rtn);

        // moveL
//        robot.MoveJ(joint_safe, tool, user, vel, acc, ovl, eposSafe, blendT, 0, offset_pos);
//        robot.ExtAxisMove(eposSafe, 100, -1);
//        rtn = robot.ExtAxisSyncMoveJ(joint_pos1, tool, user, vel, acc, ovl, epos1, blendT, 0, offset_pos);
//        rtn = robot.ExtAxisSyncMoveL(desc_pos3, tool, user, vel, acc, ovl, -1, epos3, 0, offset_pos, -1);
//        System.out.println("ExtAxisSyncMoveL rtn is: " + rtn);

        // moveC
//        robot.MoveJ(joint_safe, tool, user, vel, acc, ovl, eposSafe, blendT, 0, offset_pos);
//        robot.ExtAxisMove(eposSafe, 100, -1);
//        rtn = robot.ExtAxisSyncMoveJ(joint_pos1, tool, user, vel, acc, ovl, epos1, blendT, 0, offset_pos);
//        rtn = robot.ExtAxisSyncMoveC(desc_pos2, tool, user, vel, acc, epos2, 0, offset_pos,
//                desc_pos3, tool, user, vel, acc, epos3, 0, offset_pos, ovl, -1, -1);
//        System.out.println("ExtAxisSyncMoveC rtn is: " + rtn);
    }

    public static int TestAuxDOAO(Robot robot)
    {
        for (int i = 0; i < 128; i++)
        {
            robot.SetAuxDO(i, true, false, true);
            robot.Sleep(100);
        }
        for (int i = 0; i < 128; i++)
        {
            robot.SetAuxDO(i, false, false, true);
            robot.Sleep(100);
        }

        for (int i = 0; i < 409; i++)
        {
            robot.SetAuxAO(0, i * 10, true);
            robot.SetAuxAO(1, 4095 - i * 10, true);
            robot.SetAuxAO(2, i * 10, true);
            robot.SetAuxAO(3, 4095 - i * 10, true);
            robot.Sleep(10);
        }

        robot.SetAuxDIFilterTime(10);
        robot.SetAuxAIFilterTime(0, 10);


        int curValue = -1;
        List<Integer> liter=new ArrayList<>();
        for (int i = 0; i < 4; i++)
        {
            liter = robot.GetAuxAI(i, true);
        }

        robot.WaitAuxDI(1, false, 1000, false);
        robot.WaitAuxAI(1, 1, 132, 1000, false);

        robot.CloseRPC();
        return 0;
    }

    public static int TestTractor(Robot robot)
    {
        UDPComParam param=new UDPComParam("192.168.58.2", 2021, 2, 50, 5, 50, 1, 50, 10, 1);
        robot.ExtDevSetUDPComParam(param);
        robot.ExtDevLoadUDPDriver();

        int rtn = robot.ExtAxisServoOn(1, 1);
        rtn = robot.ExtAxisServoOn(2, 1);
        robot.Sleep(2000);

        robot.ExtAxisSetHoming(1, 0, 10, 2);
        robot.Sleep(2000);
        rtn = robot.ExtAxisSetHoming(2, 0, 10, 2);

        robot.Sleep(4000);

        robot.ExtAxisParamConfig(1, 0, 0, 1000, -1000, 1000, 1000, 6.280, 16384, 200, 0, 0, 0);
        robot.ExtAxisParamConfig(2, 0, 0, 1000, -1000, 1000, 1000, 6.280, 16384, 200, 0, 0, 0);
        robot.SetAxisDHParaConfig(5, 0, 0, 0, 0, 0, 0, 0, 0);

        robot.TractorEnable(false);
        robot.Sleep(2000);
        robot.TractorEnable(true);
        robot.Sleep(2000);
        robot.TractorHoming();
        robot.Sleep(2000);
        robot.TractorMoveL(100, 2);
        robot.Sleep(5000);
        robot.TractorStop();
        robot.TractorMoveL(-100, 20);
        robot.Sleep(5000);
        robot.TractorMoveC(300, 90, 20);
        robot.Sleep(10000);
        robot.TractorMoveC(300, -90, 20);
        robot.Sleep(1);

        robot.CloseRPC();
        return 0;
    }

    public static int TestFIR(Robot robot)
    {
        JointPos startjointPos=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos midjointPos=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);
        JointPos endjointPos=new JointPos(-29.777, -84.536, 109.275, -114.075, -86.655, 74.257);

        DescPose startdescPose=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose middescPose=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);
        DescPose enddescPose=new DescPose(-487.434, 154.362, 308.576, 176.600, 0.268, -14.061);

        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);

        int rtn = robot.PtpFIRPlanningStart(1000, 1000);
        robot.MoveJ(startjointPos, startdescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveJ(endjointPos, enddescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.PtpFIRPlanningEnd();

        robot.LinArcFIRPlanningStart(1000, 1000, 1000, 1000);
        robot.MoveL(startjointPos, startdescPose, 0, 0, 100, 100, 100, -1, 0,exaxisPos, 0, 0, offdese, 1, 1);
        robot.MoveC(midjointPos, middescPose, 0, 0, 100, 100, exaxisPos, 0, offdese, endjointPos, enddescPose, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);
        robot.LinArcFIRPlanningEnd();
        return 0;
    }

    public static int TestAccSmooth(Robot robot)
    {
        JointPos startjointPos=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos endjointPos=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);

        DescPose startdescPose=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose enddescPose=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);

        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0,0,0,0,0,0);
        int rtn = robot.AccSmoothStart(false);
        robot.MoveJ(startjointPos, startdescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveJ(endjointPos, enddescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        rtn = robot.AccSmoothEnd(false);

        robot.CloseRPC();
        return 0;
    }

    public static int TestAngularSpeed(Robot robot)
    {
        JointPos startjointPos=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos endjointPos=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);

        DescPose startdescPose=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose enddescPose=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);

        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
        int rtn = robot.AngularSpeedStart(50);
        robot.MoveJ(startjointPos, startdescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveJ(endjointPos, enddescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        rtn = robot.AngularSpeedEnd();

        return 0;
    }
    public static int TestSingularAvoid(Robot robot)
    {

        JointPos startjointPos=new JointPos(-11.904, -99.669, 117.473, -108.616, -91.726, 74.256);
        JointPos endjointPos=new JointPos(-45.615, -106.172, 124.296, -107.151, -91.282, 74.255);

        DescPose startdescPose=new DescPose(-419.524, -13.000, 351.569, -178.118, 0.314, 3.833);
        DescPose enddescPose=new DescPose(-321.222, 185.189, 335.520, -179.030, -1.284, -29.869);

        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);

        int rtn = robot.SingularAvoidStart(2, 10, 5, 5);
        robot.MoveJ(startjointPos, startdescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveJ(endjointPos, enddescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        rtn = robot.SingularAvoidEnd();

        robot.CloseRPC();
        return 0;
    }

    public static int TestLoadTrajLA(Robot robot)
    {
        int rtn = robot.TrajectoryJUpLoad("D://zUP/horse.txt");
        System.out.println("Upload TrajectoryJ A:"+ rtn);

        String  traj_file_name = "horse.txt";
        rtn = robot.LoadTrajectoryLA(traj_file_name, 2, 0, 0, 1, 40, 100, 100, 1);
        System.out.println("LoadTrajectoryLA:"+traj_file_name+", rtn is:"+ rtn);

        DescPose traj_start_pose=new DescPose();
        rtn = robot.GetTrajectoryStartPose(traj_file_name, traj_start_pose);
        System.out.println("GetTrajectoryStartPose is: "+ rtn);

        robot.Sleep(1000);

        robot.SetSpeed(50);
        robot.MoveCart(traj_start_pose, 0, 0, 100, 100, 100, -1, -1);

        rtn = robot.MoveTrajectoryLA();
        System.out.println("MoveTrajectoryLA rtn is: "+ rtn);

        return 0;
    }

    public static int TestIdentify(Robot robot)
    {
        int retval = 0;

        retval = robot.LoadIdentifyDynFilterInit();

        retval = robot.LoadIdentifyDynVarInit();

        JointPos posJ = new JointPos(0,0,0,0,0,0);
        DescPose posDec = new DescPose(0,0,0,0,0,0);
        List<Number> joint_toq=new ArrayList<>();
        robot.GetActualJointPosDegree( posJ);
        posJ.J2 = posJ.J2 + 10;
        joint_toq=robot.GetJointTorques(0);

        Object[] gain =new Object[] { 0,0.05,0,0,0,0,0,0.02,0,0,0,0 };
        double weight = 0;
        DescTran load_pos=new DescTran(0,0,0);
        List<Number> num=new ArrayList<>();
        num = robot.LoadIdentifyGetResult(gain);

        robot.CloseRPC();
        return 0;

    }







    //测试Circle新增参数
    public static void TestCircle(Robot robot){
        DescPose middescPoseCir1=new DescPose(-435.414, -342.926, 309.205, -171.382, -4.513, 171.520);

        JointPos midjointPosCir1=new JointPos(26.804, -79.866, 106.642, -125.433, -85.562, -54.721);

        DescPose enddescPoseCir1=new DescPose(-524.862, -217.402, 308.459, -171.425, -4.810, 156.088);

        JointPos endjointPosCir1=new JointPos(11.399,-78.055,104.603,-125.421,-85.770,-54.721);

        DescPose middescPoseCir2=new DescPose(-482.691, -587.899, 318.594, -171.001, -4.999, -172.996);

        JointPos midjointPosCir2=new JointPos(42.314, -53.600, 67.296, -112.969, -85.533, -54.721);

        DescPose enddescPoseCir2=new DescPose(-403.942, -489.061, 317.038, -163.189, -10.425, -175.627);

        JointPos endjointPosCir2=new JointPos(39.959, -70.616, 96.679, -134.243, -82.276, -54.721);

        DescPose middescPoseMoveC=new DescPose(-435.414, -342.926, 309.205, -171.382, -4.513, 171.520);

        JointPos midjointPosMoveC=new JointPos(26.804, -79.866, 106.642, -125.433, -85.562, -54.721);

        DescPose enddescPoseMoveC=new DescPose(-524.862, -217.402, 308.459, -171.425, -4.810, 156.088);

        JointPos endjointPosmoveC=new JointPos(11.399, -78.055, 104.603, -125.421, -85.770, -54.721);

        DescPose middescPoseCir3=new DescPose(-435.414, -342.926, 309.205, -171.382, -4.513, 171.520);

        JointPos midjointPosCir3=new JointPos(26.804, -79.866, 106.642, -125.433, -85.562, -54.721);

        DescPose enddescPoseCir3=new DescPose(-569.505, -405.378, 357.596, -172.862, -10.939, 171.108);

        JointPos endjointPosCir3=new JointPos(27.138, -63.750, 78.586, -117.861, -90.588, -54.721);

        DescPose middescPoseCir4=new DescPose(-482.691, -587.899, 318.594, -171.001, -4.999, -172.996);

        JointPos midjointPosCir4=new JointPos(42.314, -53.600, 67.296, -112.969, -85.533, -54.721);

        DescPose enddescPoseCir4=new DescPose(-569.505, -405.378, 357.596, -172.862, -10.939, 171.108);

        JointPos endjointPosCir4=new JointPos(27.138, -63.750, 78.586, -117.861, -90.588, -54.721);

        DescPose startdescPose =new DescPose(-569.505,  -405.378,  357.596,  -172.862,  -10.939,  171.108);
        JointPos startjointPos = new JointPos(27.138, -63.750, 78.586, -117.861, -90.588, -54.721);

        DescPose linedescPose =new DescPose(-403.942, -489.061, 317.038, -163.189, -10.425, -175.627);
        JointPos linejointPos = new JointPos(39.959, -70.616, 96.679, -134.243, -82.276, -54.721);


        ExaxisPos exaxisPos =new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);


         robot.MoveJ(startjointPos,startdescPose,3,0,100,100,100,exaxisPos,-1,0,offdese);
         robot.Circle(midjointPosCir1,middescPoseCir1,3,0,100,100,exaxisPos,endjointPosCir1,enddescPoseCir1,3,0,100,100,exaxisPos,100,-1,offdese,100,20);
         robot.Circle(midjointPosCir2,middescPoseCir2,3,0,100,100,exaxisPos,endjointPosCir2,enddescPoseCir2,3,0,100,100,exaxisPos,100,-1,offdese,100,20);
         robot.MoveC(midjointPosMoveC,middescPoseMoveC,3,0,100,100,exaxisPos,0,offdese,endjointPosmoveC,enddescPoseMoveC,3,0,100,100,exaxisPos,0,offdese,100,20);
         robot.Circle(midjointPosCir3,middescPoseCir3,3,0,100,100,exaxisPos,endjointPosCir3,enddescPoseCir3,3,0,100,100,exaxisPos,100,-1,offdese,100,20);
         robot.MoveL(linejointPos,linedescPose,3,0,100,100,100,-1,0,exaxisPos,0,0,offdese,0,10);
         robot.Circle(midjointPosCir4,middescPoseCir4,3,0,100,100,exaxisPos,endjointPosCir4,enddescPoseCir4,3,0,100,100,exaxisPos,100,-1,offdese,100,20);
    }

    //测试Movel新增参数
    public static int TestBlend(Robot robot) {
        DescPose DP1 = new DescPose(-324.688, -512.411, 319.936, 177.834, -13.926, -123.378);
        JointPos JP1 = new JointPos(47.944, -74.115, 99.306, -129.280, -90.062, -98.421);

        DescPose DP2 = new DescPose(-388.074, -328.779, 340.076, -159.121, 16.169, -174.291);
        JointPos JP2 = new JointPos(23.798, -86.390, 105.682, -100.633, -65.192, -70.820);

        DescPose DP3 = new DescPose(-492.692, -49.563, 375.256, 161.781, -14.476, 159.830);
        JointPos JP3 = new JointPos(-1.812, -89.883, 108.067, -116.040, -111.809, -70.825);

        DescPose DP4 = new DescPose(-432.689, -287.194, 305.739, -177.999, 1.920, -177.450);
        JointPos JP4 = new JointPos(21.721, -83.395, 108.235, -113.684, -87.480, -70.821);

        DescPose DP5 = new DescPose(-232.690, -287.193, 305.746, -177.999, 1.919, -177.450);
        JointPos JP5 = new JointPos(34.158, -105.217, 128.305, -112.503, -87.290, -58.372);

        DescPose DP6 = new DescPose(-232.695, -487.192, 305.744, -177.999, 1.919, -177.452);
        JointPos JP6 = new JointPos(53.031, -80.893, 105.748, -115.179, -87.247, -39.476);

        JointPos JP7 = new JointPos(38.933, -66.532, 86.532, -109.644, -87.251, -53.590);
        DescPose DP7 = new DescPose(-432.695, -487.196, 305.749, -177.999, 1.918, -177.452);

        JointPos JP8 = new JointPos(42.245, -82.011, 99.838, -116.087, -69.438, -70.824);
        DescPose DP8 = new DescPose(-315.138, -471.802, 373.506, -157.941, -1.233, -155.671);

        DescPose DP9 = new DescPose(-513.450, -302.627, 402.163, 171.249, -16.204, -176.411);
        JointPos JP9 = new JointPos(22.919, -78.425, 92.035, -116.080, -103.583, -70.913);

        DescPose DP10 = new DescPose(-428.141, -188.113, 351.314, 176.576, -19.670, 142.831);
        JointPos JP10 = new JointPos(14.849, -92.942, 114.901, -121.601, -107.553, -38.881);

        DescPose DP11 = new DescPose(-587.412, -70.091, 370.337, 177.676, -23.575, 127.293);
        JointPos JP11 = new JointPos(0.209, -77.444, 96.217, -121.606, -110.075, -38.879);

        JointPos JP12 = new JointPos(-21.947, -88.425, 108.395, -111.062, -77.881, -38.879);
        DescPose DP12 = new DescPose(-498.493, 67.966, 345.644, -171.472, 8.710, 107.699);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        robot.GetForwardKin(JP1,DP1);
        robot.GetForwardKin(JP2,DP2);
        robot.GetForwardKin(JP3,DP3);
        robot.GetForwardKin(JP4,DP4);
        robot.GetForwardKin(JP5,DP5);
        robot.GetForwardKin(JP6,DP6);
        robot.GetForwardKin(JP7,DP7);
        robot.GetForwardKin(JP8,DP8);
        robot.GetForwardKin(JP9,DP9);
        robot.GetForwardKin(JP10,DP10);
        robot.GetForwardKin(JP11,DP11);
        robot.GetForwardKin(JP12,DP12);


        robot.MoveJ(JP1, DP1, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveJ(JP2, DP2, 0, 0, 100, 100, 100, exaxisPos, 200, 0, offdese);
        robot.MoveJ(JP3, DP3, 0, 0, 100, 100, 100, exaxisPos, 200, 0, offdese);
        robot.MoveJ(JP4, DP4, 0, 0, 100, 100, 100, exaxisPos, 200, 0, offdese);

        robot.MoveL(JP5, DP5, 0, 0, 100, 100, 100, 20,0, exaxisPos, 0, 0, offdese, 100,0,0, 10);
        robot.MoveL(JP6, DP6, 0, 0, 100, 100, 100, 20, 1, exaxisPos, 0, 0, offdese, 0, 10);
        robot.MoveL(JP7, DP7, 0, 0, 100, 100, 100, 20, 0, exaxisPos, 0, 0, offdese, 0, 10);

        robot.MoveJ(JP8, DP8, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveC(JP9, DP9, 0, 0, 100, 100, exaxisPos, 0, offdese, JP10, DP10, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, 30);
        robot.MoveC(JP11, DP11, 0, 0, 100, 100, exaxisPos, 0, offdese, JP12, DP12, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);
        return 0;
    }

    //摆动渐变-测试用例
    public static void TestWeaveChange1(Robot robot) {
        DescPose p1Desc = new DescPose(132.497, -558.258, 257.293, -179.619, -0.544, -163.382);
        JointPos p1Joint = new JointPos(93.189, -73.209, 104.049, -121.335, -90.439, -13.429);

        DescPose p2Desc = new DescPose(-204.705, -495.507, 217.007, 178.949, -0.139, 160.188);
        JointPos p2Joint = new JointPos(56.658, -74.379, 112.264, -126.895, -90.38, -13.52);

        DescPose p3Desc = new DescPose(-243.249, -655.089, 182.985, 178.35, 0.019, 164.815);
        JointPos p3Joint = new JointPos(61.289, -53.089, 84.529, -119.832, -90.366, -13.518);

        ExaxisPos exaxisPos = new ExaxisPos(0.0, 0.0, 0.0, 0.0);
        DescPose offdese = new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        robot.MoveJ(p1Joint, p1Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveL(p2Joint, p2Desc, 0, 0, 100, 100, 50, -1, 0, exaxisPos, 0, 0, offdese, 0, 10);

        robot.WeaveChangeStart(1, 1, 24, 36);
        robot.MoveL(p3Joint, p3Desc, 0, 0, 100, 100, 1, -1, 0, exaxisPos, 0, 0, offdese, 0, 10);
        robot.WeaveChangeEnd();
    }

    public static void WeldparamChange(Robot robot) {
        DescPose startdescPose = new DescPose(-484.707, 276.996, -14.013, -37.657, -40.508, -1.548);
        JointPos startjointPos = new JointPos(-45.421, -75.673, 93.627, -104.302, -87.938, 6.005);

        DescPose enddescPose = new DescPose(-508.767, 137.109, -13.966, -37.639, -40.508, -1.559);
        JointPos endjointPos = new JointPos(-32.768, -80.947, 100.254, -106.201, -87.201, 18.648);

        DescPose safedescPose = new DescPose(-484.709, 294.436, 13.621, -37.660, -40.508, -1.545);
        JointPos safejointPos = new JointPos(-46.604, -75.410, 89.109, -100.003, -88.012, 4.823);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        WeldCurrentAORelation cur = new WeldCurrentAORelation(0, 495, 1, 10, 0);
        WeldVoltageAORelation vol = new WeldVoltageAORelation(10, 45, 1, 10, 1);
        robot.WeldingSetCurrentRelation(cur);
        robot.WeldingSetVoltageRelation(vol);

        robot.WeldingSetVoltage(0, 25, 1, 0);// ----设置电压
        robot.WeldingSetCurrent(0, 260, 0, 0);// ----设置电流

        robot.MoveJ(safejointPos, safedescPose, 1, 0, 5, 100, 100, exaxisPos, -1, 0, offdese);

        robot.WeldingSetCurrentGradualChangeStart(0, 260, 220, 0, 0);
        robot.WeldingSetVoltageGradualChangeStart(0, 25, 22, 1, 0);
        int rtn = robot.ArcWeldTraceControl(1, 0, 1, 0.08, 5, 5, 300, 1, 0.06, 4, 4, 300, 1, 0, 4, 1, 10, 0, 0);

        robot.MoveJ(startjointPos, startdescPose, 1, 0, 5, 100, 100, exaxisPos, -1, 0, offdese);
        System.out.println("ArcWeldTraceControl rtn is " + rtn);

        robot.ARCStart(0, 0, 10000);
        robot.WeaveStart(0);
        robot.WeaveChangeStart(2, 1, 24, 36);
        robot.MoveL(endjointPos, enddescPose, 1, 0, 100, 100, 2, -1, 0, exaxisPos, 0, 0, offdese, 0, 10);
        robot.ARCEnd(0, 0, 10000);
        robot.WeaveChangeEnd();
        robot.WeaveEnd(0);
        robot.ArcWeldTraceControl(0, 0, 1, 0.08, 5, 5, 300, 1, 0.06, 4, 4, 300, 1, 0, 4, 1, 10, 0, 0);
        robot.WeldingSetCurrentGradualChangeEnd();
        robot.WeldingSetVoltageGradualChangeEnd();
    }

    public static void WeldTraceControlWithCtrlBoxAI(Robot robot)
    {
        DescPose startdescPose = new DescPose(-473.86, 257.879, -20.849, -37.317, -42.021, 2.543);
        JointPos startjointPos = new JointPos(-43.487, -76.526, 95.568, -104.445, -89.356, 3.72);

        DescPose safedescPose = new DescPose(-504.043, 275.181, 40.908, -28.002, -42.025, -14.044);
        JointPos safejointPos = new JointPos(-39.078, -76.732, 87.227, -99.47, -94.301, 18.714);

        DescPose enddescPose = new DescPose(-499.844, 141.225, 7.72, -34.856, -40.17, 13.13);
        JointPos endjointPos = new JointPos(-31.305, -82.998, 99.401, -104.426, -89.35, 3.696);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);
        //起始运动到安全点
        robot.MoveJ(safejointPos, safedescPose, 1, 0, 5, 20, 100, exaxisPos, -1, 0, offdese);

        WeldCurrentAORelation current = new WeldCurrentAORelation(0, 495, 1, 10, 0);
        WeldVoltageAORelation voltage = new WeldVoltageAORelation(10, 45, 1, 10, 1);
        robot.WeldingSetCurrentRelation(current);//电流与输出模拟量的关系
        robot.WeldingSetVoltageRelation(voltage);//电压与输出模拟量的关系
        robot.WeldingSetVoltage(0, 25, 1, 0);//设置电压
        robot.WeldingSetCurrent(0, 260, 0, 0);//设置电流

        int rtn = robot.ArcWeldTraceAIChannelCurrent(4);
        System.out.println("ArcWeldTraceAIChannelCurrent rtn is " + rtn);

        rtn = robot.ArcWeldTraceAIChannelVoltage(5);
        System.out.println("ArcWeldTraceAIChannelVoltage rtn is " + rtn);

        rtn = robot.ArcWeldTraceCurrentPara(0.0, 5, 0, 500);
        System.out.println("ArcWeldTraceCurrentPara rtn is " + rtn);

        rtn = robot.ArcWeldTraceVoltagePara(1.018, 10, 0, 50);
        System.out.println("ArcWeldTraceVoltagePara rtn is " + rtn);

        robot.MoveJ(startjointPos, startdescPose, 1, 0, 20, 20, 100, exaxisPos, -1, 0, offdese);
        robot.ArcWeldTraceControl(1, 0, 1, 0.08, 5, 5, 300, 1, 0.06, 4, 4, 300, 1, 0, 4, 1, 10, 0, 0);
        robot.ARCStart(0, 0, 10000);
        robot.WeaveStart(0);
        robot.MoveL(endjointPos, enddescPose, 1, 0, 100, 100, 2, -1, 0, exaxisPos, 0, 0, offdese, 0, 10);
        robot.ARCEnd(0, 0, 10000);
        robot.WeaveEnd(0);
        robot.ArcWeldTraceControl(0, 0, 1, 0.08, 5, 5, 300, 1, 0.06, 4, 4, 300, 1, 0, 4, 1, 10, 0, 0);

        //结束运动到安全点
        robot.MoveJ(safejointPos, safedescPose, 1, 0, 20, 20, 100, exaxisPos, -1, 0, offdese);
    }

    public static void WeaveAngle(Robot robot) {
        DescPose startdescPose = new DescPose(146.273, -208.110, 270.102, 177.523, -3.782, -158.101);
        JointPos startjointPos = new JointPos(98.551, -128.309, 127.341, -87.490, -94.249, -13.208);
        DescPose enddescPose = new DescPose(146.272, -476.204, 270.102, 177.523, -3.781, -158.101);
        JointPos endjointPos = new JointPos(93.931, -89.722, 102.216, -101.300, -94.359, -17.840);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);
        robot.WeaveSetPara(0, 0, 2.000000, 0, 10.000000, 0.000000, 0.000000, 0, 0, 0, 0, 0, 0, 0);
        robot.MoveL(startjointPos, startdescPose, 2, 0, 100, 100, 100, -1, 0, exaxisPos, 0, 0, offdese, 0, 10);
        robot.WeaveStart(0);
        robot.MoveL(endjointPos, enddescPose, 2, 0, 100, 100, 100, -1, 0, exaxisPos, 0, 0, offdese, 0, 10);
        robot.WeaveEnd(0);

        robot.WeaveSetPara(0, 0, 2.000000, 0, 10.000000, 0.000000, 0.000000, 0, 0, 0, 0, 0, 0, 30);
        robot.MoveL(startjointPos, startdescPose, 2, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 10);
        robot.WeaveStart(0);
        robot.MoveL(endjointPos, enddescPose, 2, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 10);
        robot.WeaveEnd(0);
    }

    public static void testdown(Robot robot) {
        int rtn = 0;
        robot.ShutDownRobotOS();//测试开关机

//        rtn = robot.DataPackageDownload("D://zDOWN/");//数据备份包下载
//        System.out.println("DataPackageDownload rtn is: "+rtn);
//
//        System.out.println("AllDataSourceDownload start");
//        rtn = robot.AllDataSourceDownload("D://zDOWN/");//所有数据源下载
//        System.out.println("AllDataSourceDownload rtn is: "+rtn);

//        System.out.println("RbLogDownload start");
//        rtn = robot.RbLogDownload("D://zDOWN/");//控制器日志下载
//        System.out.println("RbLogDownload rtn is: "+rtn);

//        String[] SN = new String[1];
//        robot.GetRobotSN(SN);//获取控制箱SN码
//        System.out.println("robot SN is :"+SN[0]);

        for (int i = 0; i < 10; i++) {
            System.out.println("DataPackageDownload start");
            rtn = robot.DataPackageDownload("D://zDOWN/");
            System.out.println("DataPackageDownload rtn is: " + rtn + "  times  :" + i);
        }

        for (int i = 0; i < 10; i++) {
            System.out.println("AllDataSourceDownload start");
            rtn = robot.AllDataSourceDownload("D://zDOWN/");
            System.out.println("AllDataSourceDownload rtn is: " + rtn + "  times  :" + i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println("RbLogDownload start");
            rtn = robot.RbLogDownload("D://zDOWN/");
            System.out.println("RbLogDownload rtn is: " + rtn + "  times  :" + i);
        }
//        for (int i = 0; i < 10; i++)
//        {
//            String[] SN = new String[1];
//            robot.GetRobotSN(SN);
//            System.out.println("robot SN is :"+SN[0]+"  times  :"+i);
//        }
    }

    public static void Trigger(Robot robot) {
        int i;

        System.out.println("please input a number to trigger:");

        Scanner scanner = new Scanner(System.in);
        i = scanner.nextInt();

        System.out.println(i);
        int rtn = robot.ConveyorComDetectTrigger();
        System.out.println("ConveyorComDetectTrigger retval is: " + rtn);
        scanner.close();
    }

    public static int ConveyorTest(Robot robot)//传送带跟踪检测
    {

        int retval = 0;


        retval = 0;
        //float param[6] = { 1,10000,200,0,0,20 };
        //retval = robot->ConveyorSetParam(param, 0, 0, 0);
        //printf("ConveyorSetParam retval is: %d\n", retval);

        int index = 1;
        int max_time = 30000;
        int block = 0;
        retval = 0;

        /* 下面是一个传送带抓取流程 */
        DescPose startdescPose = new DescPose(139.176, 4.717, 9.088, -179.999, -0.004, -179.990);
        JointPos startjointPos = new JointPos(-34.129, -88.062, 97.839, -99.780, -90.003, -34.140);

        DescPose homePose = new DescPose(139.177, 4.717, 69.084, -180.000, -0.004, -179.989);
        JointPos homejointPos = new JointPos(-34.129, -88.618, 84.039, -85.423, -90.003, -34.140);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        retval = robot.MoveL(homejointPos, homePose, 1, 1, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
        System.out.println("MoveL to safety retval is: " + retval);


        Thread triggerThread = new Thread(() -> Trigger(robot));
        triggerThread.setDaemon(true);
        triggerThread.start();

        retval = robot.ConveyorComDetect(1000 * 10);
        System.out.println("ConveyorComDetect retval is: " + retval);

        retval = robot.ConveyorGetTrackData(2);
        System.out.println("ConveyorGetTrackData retval is: " + retval);


        retval = robot.ConveyorTrackStart(2);
        System.out.println("ConveyorTrackStart retval is: " + retval);

        robot.MoveL(startjointPos, startdescPose, 1, 1, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
        robot.MoveL(startjointPos, startdescPose, 1, 1, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);

        retval = robot.ConveyorTrackEnd();
        System.out.println("ConveyorTrackEnd retval is: " + retval);
        robot.MoveL(homejointPos, homePose, 1, 1, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);

        return 0;
    }

    //逆运动学测试
    public static void TestInverseKen(Robot robot) {
        DescPose dcs1 = new DescPose(32.316, -232.029, 1063.415, 90.159, 18.376, 36.575);
        DescPose dcs2 = new DescPose(105.25, -170.914, 1076.283, 87.032, 25.94, 54.644);
        DescPose dcs3 = new DescPose(79.164, 81.645, 1045.609, 133.691, -73.265, 162.726);
        DescPose dcs4 = new DescPose(298.779, -104.112, 298.242, 179.631, -0.628, -166.481);
        JointPos inverseRtn = new JointPos() {
        };

//        robot.GetInverseKin(0, dcs1, -1, inverseRtn);
//        System.out.println("dcs1 getinverse rtn is "+inverseRtn.J1+","+inverseRtn.J2+","+inverseRtn.J3+","+
//                inverseRtn.J4+","+inverseRtn.J5+","+inverseRtn.J6);
//        robot.GetInverseKin(0, dcs2, -1, inverseRtn);
//        System.out.println("dcs2 getinverse rtn is "+inverseRtn.J1+","+inverseRtn.J2+","+inverseRtn.J3+","+
//                inverseRtn.J4+","+inverseRtn.J5+","+inverseRtn.J6);
//        robot.GetInverseKin(0, dcs3, -1, inverseRtn);
//        System.out.println("dcs3 getinverse rtn is "+inverseRtn.J1+","+inverseRtn.J2+","+inverseRtn.J3+","+
//                inverseRtn.J4+","+inverseRtn.J5+","+inverseRtn.J6);
//        robot.GetInverseKin(0, dcs4, -1, inverseRtn);
//        System.out.println("dcs4 getinverse rtn is "+inverseRtn.J1+","+inverseRtn.J2+","+inverseRtn.J3+","+
//                inverseRtn.J4+","+inverseRtn.J5+","+inverseRtn.J6);

        JointPos jpos1 = new JointPos(56.999, -59.002, 56.996, -96.552, 60.392, -90.005);
        DescPose forwordResult = new DescPose() {
        };
        robot.GetForwardKin(jpos1, forwordResult);
        System.out.println("jpos1 forwordResult rtn is " + forwordResult.tran.x + "," + forwordResult.tran.y + "," + forwordResult.tran.z + "," +
                forwordResult.rpy.rx + "," + forwordResult.rpy.ry + "," + forwordResult.rpy.rz);
    }

    public static void testSmooth(Robot robot) {//测试平滑
        JointPos JP1 = new JointPos(88.927, -85.834, 80.289, -85.561, -91.388, 108.718);
        DescPose DP1 = new DescPose(88.739, -527.617, 514.939, -179.039, 1.494, 70.209);

        JointPos JP2 = new JointPos(27.036, -83.909, 80.284, -85.579, -90.027, 108.604);
        DescPose DP2 = new DescPose(-433.125, -334.428, 497.139, -179.723, -0.745, 8.437);

        JointPos JP3 = new JointPos(60.219, -94.324, 62.906, -62.005, -87.159, 108.598);
        DescPose DP3 = new DescPose(-112.215, -409.323, 686.497, 176.217, 2.338, 41.625);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

//        int error = robot.AccSmoothStart(false);

//        System.out.println("AccSmoothStart return:" + error);
        //MoveJ
        robot.MoveJ(JP1, DP1, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveJ(JP2, DP2, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveJ(JP1, DP1, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveJ(JP2, DP2, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);

        //MoveL
//        robot.MoveL(JP1, DP1, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 10);
//        robot.MoveL(JP2, DP2, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 10);
//        robot.MoveL(JP1, DP1, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 10);
//        robot.MoveL(JP2, DP2, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 10);

        //MoveC
//        robot.MoveC(JP3, DP3, 0, 0, 30, 100, exaxisPos, 0, offdese, JP1, DP1, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);
//        robot.MoveC(JP3, DP3, 0, 0, 30, 100, exaxisPos, 0, offdese, JP2, DP2, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);

        //Circle
//        robot.Circle(JP3, DP3, 0, 0, 100, 100.0, exaxisPos, JP2, DP2, 0, 0, 100.0, 100.0, exaxisPos, 100.0, 0, offdese);

//        error = robot.AccSmoothEnd(false);
    }

    public static void reconnect_test(Robot robot)//测试重连
    {
        int rtn = -1;
        DescPose p1Desc = new DescPose(-423.723, -145.123, 546.173, -161.851, -29.236, 150.755);
        JointPos p1Joint = new JointPos(6.001, -103.515, 102.462, -122.922, -90.77, -59.761);

        DescPose p2Desc = new DescPose(-458.433, -678.096, 290.075, -176.815, -6.699, -161.689);
        JointPos p2Joint = new JointPos(48.905, -43.486, 53.364, -107.265, -90.655, -59.635);


        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);
        int i = 0;
        while (i < 1000) {
            rtn = -1;
            boolean flag = robot.isConnected();//连接是否断开
            System.out.println("连接是否断开: " + flag);
            robot.Sleep(200);
//            while (flag==false)//没有断开
//            {
//                rtn=robot.MoveL(p1Joint, p1Desc, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 10);
//                rtn=robot.MoveL(p2Joint, p2Desc, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 10);
//                System.out.println("指令执行结果:"+rtn);
//            }
//            System.out.println("连接断开");
//            i++;
        }
    }

    private static void TestTrajectoryLA(Robot robot) {

        int rtn = 0;

        String nameA = "/fruser/traj/A.txt";
        String nameB = "/fruser/traj/B.txt";

        rtn = robot.LoadTrajectoryLA(nameA, 2, 0.0, 0, 1.0, 100.0, 200.0, 1000.0);//B样条
//        rtn = robot.LoadTrajectoryLA(nameA, 1, 2, 0, 2, 100.0, 200.0, 1000.0);

//        rtn = robot.LoadTrajectoryLA(nameB, 0, 0, 0, 1, 100.0, 100.0, 1000.0);    // 直线拟合
        System.out.println("LoadTrajectoryLA rtn is :" + rtn);

        DescPose startPos = new DescPose(0, 0, 0, 0, 0, 0);
        robot.GetTrajectoryStartPose(nameA, startPos);

        // MoveCart方法调用，假设参数类型和顺序与C++版本一致
        robot.MoveCart(startPos, 1, 0, (float) 100.0, (float) 100.0, (float) 100.0, -1, -1);

        rtn = robot.MoveTrajectoryLA();
        System.out.println("MoveTrajectoryLA rtn is: " + rtn);
    }

    //自定义碰撞检测阈值功能开始
    public static void CustomCollisionTest(Robot robot) {
        int[] safety = {5, 5, 5, 5, 5, 5};
        robot.SetCollisionStrategy(3, 1000, 150, 250, safety);
        double[] jointDetectionThreshould = {0.3, 0.3, 0.3, 0.3, 0.3, 0.3};
        double[] tcpDetectionThreshould = {60, 60, 60, 60, 60, 60};
        int rtn = robot.CustomCollisionDetectionStart(3, jointDetectionThreshould, tcpDetectionThreshould, 0);

        DescPose p1Desc = new DescPose(228.879, -503.594, 453.984, -175.580, 8.293, 171.267);
        JointPos p1Joint = new JointPos(102.700, -85.333, 90.518, -102.365, -83.932, 22.134);

        DescPose p2Desc = new DescPose(-333.302, -435.580, 449.866, -174.997, 2.017, 109.815);
        JointPos p2Joint = new JointPos(41.862, -85.333, 90.526, -100.587, -90.014, 22.135);

        ExaxisPos exaxisPos = new ExaxisPos(0.0, 0.0, 0.0, 0.0);
        DescPose offdese = new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        for (int i = 0; i < 10; ++i) {
            robot.MoveL(p1Joint, p1Desc, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 10);
            robot.MoveL(p2Joint, p2Desc, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 10);
        }
        rtn = robot.CustomCollisionDetectionEnd();
    }

    private static void TestReWeld(Robot robot) {
        int rtn = -1;
        rtn = robot.WeldingSetCheckArcInterruptionParam(1, 200);
        System.out.println("WeldingSetCheckArcInterruptionParam: " + rtn);
        rtn = robot.WeldingSetReWeldAfterBreakOffParam(1, 5.7, 98.2, 0);
        System.out.println("WeldingSetReWeldAfterBreakOffParam: " + rtn);
        int enable = 0;
        double length = 0;
        double velocity = 0;
        int moveType = 0;
        int checkEnable = 0;
        int arcInterruptTimeLength = 0;
        List<Integer> rtnArray = new ArrayList<Integer>() {
        };
        List<Number> rtnArrayWeld = new ArrayList<Number>() {
        };
        rtnArray = robot.WeldingGetCheckArcInterruptionParam();
        checkEnable = rtnArray.get(1);
        arcInterruptTimeLength = rtnArray.get(2);
        System.out.println("WeldingGetCheckArcInterruptionParam  checkEnable:" + checkEnable + ", arcInterruptTimeLength : " + arcInterruptTimeLength);
        rtnArrayWeld = robot.WeldingGetReWeldAfterBreakOffParam();
        enable = (int) rtnArrayWeld.get(1);
        length = (double) rtnArrayWeld.get(2);
        velocity = (double) rtnArrayWeld.get(3);
        moveType = (int) rtnArrayWeld.get(4);
        System.out.println("WeldingGetReWeldAfterBreakOffParam :" + enable + ",length: " + length + ",velocity :" + velocity + ",moveType :" + moveType);
        //焊接中断恢复
        robot.ProgramLoad("/fruser/test.lua");
        robot.ProgramRun();

        robot.Sleep(5000);

        while (true) {
            ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();
            pkg = robot.GetRobotRealTimeState();
//            System.out.println("welding breakoff state is " + pkg.weldingBreakOffstate.breakOffState);
//            if (pkg.weldingBreakOffstate.breakOffState == 1) {
//                System.out.println("welding breakoff !");
//                robot.Sleep(2000);
//                rtn = robot.WeldingStartReWeldAfterBreakOff();
//                System.out.println("WeldingStartReWeldAfterBreakOff: " + rtn);
//                break;
//            }
            robot.Sleep(100);
        }
    }

    public static void TestTCP(Robot robot) {
        DescPose p1Desc = new DescPose(-394.073, -276.405, 399.451, -133.692, 7.657, -139.047);
        JointPos p1Joint = new JointPos(15.234, -88.178, 96.583, -68.314, -52.303, -122.926);

        DescPose p2Desc = new DescPose(-187.141, -444.908, 432.425, 148.662, 15.483, -90.637);
        JointPos p2Joint = new JointPos(61.796, -91.959, 101.693, -102.417, -124.511, -122.767);

        DescPose p3Desc = new DescPose(-368.695, -485.023, 426.640, -162.588, 31.433, -97.036);
        JointPos p3Joint = new JointPos(43.896, -64.590, 60.087, -50.269, -94.663, -122.652);

        DescPose p4Desc = new DescPose(-291.069, -376.976, 467.560, -179.272, -2.326, -107.757);
        JointPos p4Joint = new JointPos(39.559, -94.731, 96.307, -93.141, -88.131, -122.673);

        DescPose p5Desc = new DescPose(-284.140, -488.041, 478.579, 179.785, -1.396, -98.030);
        JointPos p5Joint = new JointPos(49.283, -82.423, 81.993, -90.861, -89.427, -122.678);

        DescPose p6Desc = new DescPose(-296.307, -385.991, 484.492, -178.637, -0.057, -107.059);
        JointPos p6Joint = new JointPos(40.141, -92.742, 91.410, -87.978, -88.824, -122.808);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        JointPos[] posJ = {p1Joint, p2Joint, p3Joint, p4Joint, p5Joint, p6Joint};
        DescPose coordRtn = new DescPose();
        int rtn = robot.ComputeToolCoordWithPoints(0, posJ, coordRtn);
        System.out.println("ComputeToolCoordWithPoints :" + rtn + ",coord is:" + rtn + "," + coordRtn.tran.x + "," + coordRtn.tran.y + "," + coordRtn.tran.z + "," + coordRtn.rpy.rx + "," + coordRtn.rpy.ry + "," + coordRtn.rpy.rz);


        robot.MoveJ(p1Joint, p1Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(1);
        robot.MoveJ(p2Joint, p2Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(2);
        robot.MoveJ(p3Joint, p3Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(3);
        robot.MoveJ(p4Joint, p4Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetTcp4RefPoint(4);
        robot.ComputeTcp4(coordRtn);
        System.out.println("ComputeTcp4 : " + rtn + ", coord is: " + coordRtn.tran.x + "," + coordRtn.tran.y + "," + coordRtn.tran.z + "," + coordRtn.rpy.rx + "," + coordRtn.rpy.ry + "," + coordRtn.rpy.rz);
        //robot->MoveJ(&p5Joint, &p5Desc, 0, 0, 100, 100, 100, &exaxisPos, -1, 0, &offdese);
        //robot->MoveJ(&p6Joint, &p6Desc, 0, 0, 100, 100, 100, &exaxisPos, -1, 0, &offdese);

    }



    //电弧跟踪控制,测试用例
    public static void TestArcWeldTraceChange(Robot robot) {
        DescPose p1Desc = new DescPose(-72.912, -587.664, 31.849, 43.283, -6.731, 15.068);
        JointPos p1Joint = new JointPos(74.620, -80.903, 94.608, -109.882, -90.436, -13.432);

        DescPose p2Desc = new DescPose(-104.915, -483.712, -25.231, 42.228, -6.572, 18.433);
        JointPos p2Joint = new JointPos(66.431, -92.875, 116.362, -120.516, -88.627, -24.731);

        DescPose p3Desc = new DescPose(-242.834, -498.697, -23.681, 46.576, -5.286, 8.318);
        JointPos p3Joint = new JointPos(57.153, -82.046, 104.060, -116.659, -92.478, -24.735);

        ExaxisPos exaxisPos = new ExaxisPos(0.0, 0.0, 0.0, 0.0);
        DescPose offdese = new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        robot.WeldingSetVoltage(1, 19, 0, 0);
        robot.WeldingSetCurrent(1, 190, 0, 0);
        robot.MoveJ(p1Joint, p1Desc, 1, 1, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveL(p2Joint, p2Desc, 1, 1, 100, 100, 50, -1, exaxisPos, 0, 0, offdese, 0, 10);
        robot.ARCStart(1, 0, 10000);
        robot.ArcWeldTraceControl(1, 0, 1, 0.06, 5, 5, 60, 1, 0.06, 5, 5, 60, 0, 0, 4, 1, 10, 2, 2);
        robot.WeaveStart(0);
        robot.MoveL(p3Joint, p3Desc, 1, 1, 100, 100, 1, -1, exaxisPos, 0, 0, offdese, 0, 10);
        robot.WeaveEnd(0);
        robot.ArcWeldTraceControl(0, 0, 1, 0.06, 5, 5, 60, 1, 0.06, 5, 5, 60, 0, 0, 4, 1, 10, 2, 2);
        robot.ARCEnd(1, 0, 10000);
    }

    //摆动渐变-测试用例
    public static void TestWeaveChange(Robot robot) {
        DescPose p1Desc = new DescPose(-72.912, -587.664, 31.849, 43.283, -6.731, 15.068);
        JointPos p1Joint = new JointPos(74.620, -80.903, 94.608, -109.882, -90.436, -13.432);

        DescPose p2Desc = new DescPose(-104.915, -483.712, -25.231, 42.228, -6.572, 18.433);
        JointPos p2Joint = new JointPos(66.431, -92.875, 116.362, -120.516, -88.627, -24.731);

        DescPose p3Desc = new DescPose(-240.651, -483.840, -7.161, 46.577, -5.286, 8.318);
        JointPos p3Joint = new JointPos(56.457, -84.796, 104.618, -114.497, -92.422, -25.430);

        ExaxisPos exaxisPos = new ExaxisPos(0.0, 0.0, 0.0, 0.0);
        DescPose offdese = new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        robot.WeldingSetVoltage(1, 19, 0, 0);
        robot.WeldingSetCurrent(1, 190, 0, 0);
        robot.MoveJ(p1Joint, p1Desc, 1, 1, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.MoveL(p2Joint, p2Desc, 1, 1, 100, 100, 50, -1, exaxisPos, 0, 0, offdese, 0, 10);
        robot.ARCStart(1, 0, 10000);
        robot.ArcWeldTraceControl(1, 0, 1, 0.06, 5, 5, 60, 1, 0.06, 5, 5, 80, 0, 0, 4, 1, 10, 0, 0);
        robot.WeaveStart(0);
//        robot.WeaveChangeStart(1);
        robot.MoveL(p3Joint, p3Desc, 1, 1, 100, 100, 1, -1, exaxisPos, 0, 0, offdese, 0, 10);
        robot.WeaveChangeEnd();
        robot.WeaveEnd(0);
        robot.ArcWeldTraceControl(0, 0, 1, 0.06, 5, 5, 60, 1, 0.06, 5, 5, 80, 0, 0, 4, 1, 10, 0, 0);
        robot.ARCEnd(1, 0, 10000);
    }

    public static void TestTCP6(Robot robot) {
        DescPose p1Desc = new DescPose(-394.073, -276.405, 399.451, -133.692, 7.657, -139.047);
        JointPos p1Joint = new JointPos(15.234, -88.178, 96.583, -68.314, -52.303, -122.926);

        DescPose p2Desc = new DescPose(-187.141, -444.908, 432.425, 148.662, 15.483, -90.637);
        JointPos p2Joint = new JointPos(61.796, -91.959, 101.693, -102.417, -124.511, -122.767);

        DescPose p3Desc = new DescPose(-368.695, -485.023, 426.640, -162.588, 31.433, -97.036);
        JointPos p3Joint = new JointPos(43.896, -64.590, 60.087, -50.269, -94.663, -122.652);

        DescPose p4Desc = new DescPose(-291.069, -376.976, 467.560, -179.272, -2.326, -107.757);
        JointPos p4Joint = new JointPos(39.559, -94.731, 96.307, -93.141, -88.131, -122.673);

        DescPose p5Desc = new DescPose(-284.140, -488.041, 478.579, 179.785, -1.396, -98.030);
        JointPos p5Joint = new JointPos(49.283, -82.423, 81.993, -90.861, -89.427, -122.678);

        DescPose p6Desc = new DescPose(-296.307, -385.991, 484.492, -178.637, -0.057, -107.059);
        JointPos p6Joint = new JointPos(40.141, -92.742, 91.410, -87.978, -88.824, -122.808);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        JointPos[] posJ = {p1Joint, p2Joint, p3Joint, p4Joint, p5Joint, p6Joint};
        DescPose coordRtn = new DescPose();
        int rtn = robot.ComputeToolCoordWithPoints(1, posJ, coordRtn);
        System.out.println("ComputeToolCoordWithPoints: " + rtn + ", coord is :" + coordRtn.tran.x + "," + coordRtn.tran.y + "," + coordRtn.tran.z + "," + coordRtn.rpy.rx + "," + coordRtn.rpy.ry + "," + coordRtn.rpy.rz);


        robot.MoveJ(p1Joint, p1Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(1);
        robot.MoveJ(p2Joint, p2Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(2);
        robot.MoveJ(p3Joint, p3Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(3);
        robot.MoveJ(p4Joint, p4Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(4);
        robot.MoveJ(p5Joint, p5Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(5);
        robot.MoveJ(p6Joint, p6Desc, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetToolPoint(6);
        robot.ComputeTool(coordRtn);
        System.out.println("ComputeTool :" + rtn + ",coord is :" + coordRtn.tran.x + "," + coordRtn.tran.y + "," + coordRtn.tran.z + "," + coordRtn.rpy.rx + "," + coordRtn.rpy.ry + "," + coordRtn.rpy.rz);
    }

    public static void TestWObj(Robot robot) {
        DescPose p1Desc = new DescPose(-275.046, -293.122, 28.747, 174.533, -1.301, -112.101);
        JointPos p1Joint = new JointPos(35.207, -95.350, 133.703, -132.403, -93.897, -122.768);

        DescPose p2Desc = new DescPose(-280.339, -396.053, 29.762, 174.621, -3.448, -102.901);
        JointPos p2Joint = new JointPos(44.304, -85.020, 123.889, -134.679, -92.658, -122.768);

        DescPose p3Desc = new DescPose(-270.597, -290.603, 83.034, 179.314, 0.808, -114.171);
        JointPos p3Joint = new JointPos(32.975, -99.175, 125.966, -116.484, -91.014, -122.857);


        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        DescPose[] posTCP = {p1Desc, p2Desc, p3Desc};
        DescPose coordRtn = new DescPose();
        int rtn = robot.ComputeWObjCoordWithPoints(0, posTCP, 0, coordRtn);
        System.out.println("ComputeToolCoordWithPoints :" + rtn + ",coord is :" + coordRtn.tran.x + "," + coordRtn.tran.y + "," + coordRtn.tran.z + "," + coordRtn.rpy.rx + "," + coordRtn.rpy.ry + "," + coordRtn.rpy.rz);


        robot.MoveJ(p1Joint, p1Desc, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetWObjCoordPoint(1);
        robot.MoveJ(p2Joint, p2Desc, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetWObjCoordPoint(2);
        robot.MoveJ(p3Joint, p3Desc, 1, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.SetWObjCoordPoint(3);
        robot.ComputeWObjCoord(0, 0, coordRtn);
        System.out.println("ComputeTool :" + rtn + "coord is :" + coordRtn.tran.x + "," + coordRtn.tran.y + "," + coordRtn.tran.z + "," + coordRtn.rpy.rx + "," + coordRtn.rpy.ry + "," + coordRtn.rpy.rz);
        //robot->MoveJ(&p5Joint, &p5Desc, 0, 0, 100, 100, 100, &exaxisPos, -1, 0, &offdese);
        //robot->MoveJ(&p6Joint, &p6Desc, 0, 0, 100, 100, 100, &exaxisPos, -1, 0, &offdese);


    }

    public static void ExtAxisLaserTracking(Robot robot) {
        DescPose p1Desc = new DescPose(381.070, -177.767, 227.851, 20.031, -2.455, -111.479);
        JointPos p1Joint = new JointPos(8.383, -44.801, -111.050, -97.707, 78.144, 27.709);

        DescPose p2Desc = new DescPose(381.077, -177.762, 217.865, 20.014, -0.131, -110.631);
        JointPos p2Joint = new JointPos(1.792, -44.574, -113.176, -93.687, 82.384, 21.154);

        DescPose p3Desc = new DescPose(381.070, -177.767, 227.851, 20.031, -2.455, -111.479);
        JointPos p3Joint = new JointPos(8.383, -44.801, -111.050, -97.707, 78.144, 27.709);

        ExaxisPos exaxisPos = new ExaxisPos(0.0, 0.0, 0.0, 0.0);
        DescPose offdese = new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        ExaxisPos exaxisPosStart = new ExaxisPos(0.0, 0.0, 0.0, 0.0);
        robot.MoveJ(p1Joint, p1Desc, 8, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.ExtAxisMove(exaxisPosStart, 50.0);
        robot.MoveL(p2Joint, p2Desc, 8, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 10);
        robot.LaserSensorRecord(4, 1, 10, 2, 35, 0.1, 100);
        ExaxisPos exaxisPosTarget = new ExaxisPos(0.000, 400.015, 0.000, 0.000);
        robot.ExtAxisMove(exaxisPosTarget, 10.0);
        robot.LaserSensorRecord(0, 1, 10, 2, 35, 0.1, 100);
        robot.MoveJ(p3Joint, p3Desc, 8, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        robot.ExtAxisMove(exaxisPosStart, 50.0);
    }

    private static void TestWeave1(Robot robot) {
        DescPose desc_p1, desc_p2;

        desc_p1 = new DescPose(-299.979, -399.974, 74.979, 0.009, 0.001, -41.530);
        desc_p2 = new DescPose(-49.985, -399.956, 74.978, 0.009, 0.005, -41.530);

        JointPos j1 = new JointPos(41.476, -77.300, 118.714, -131.405, -90.002, -51.993);
        JointPos j2 = new JointPos(68.366, -89.562, 133.018, -133.446, -90.002, -25.105);

        ExaxisPos epos = new ExaxisPos();
        DescPose offset_pos = new DescPose();

        robot.WeaveSetPara(0, 4, 2.000000, 0, 10.000000, 0.000000, 0.000000, 0, 0, 0, 0, 0, 60.000000, 0);
        robot.MoveJ(j1, desc_p1, 13, 0, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.WeaveStart(0);
        robot.MoveL(j2, desc_p2, 13, 0, 10, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
        robot.WeaveEnd(0);

        robot.WeaveSetPara(0, 0, 1.000000, 0, 10.000000, 0.000000, 0.000000, 0, 0, 0, 0, 0, 30.000000, 0);
        robot.MoveJ(j1, desc_p1, 13, 0, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.WeaveStart(0);
        robot.MoveL(j2, desc_p2, 13, 0, 10, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
        robot.WeaveEnd(0);
    }


    private static void TrajectoryJ(Robot robot) {
//********************************轨迹文件读取运动 begin *********************************88
        ForceTorque tor = new ForceTorque(10.0, 10.0, 10.0, 10.0, 10.0, 10.0);
        robot.SetTrajectoryJForceTorque(tor);

        robot.SetTrajectoryJForceFx(2.0);
        robot.SetTrajectoryJForceFy(2.0);
        robot.SetTrajectoryJForceFz(2.0);
        robot.SetTrajectoryJTorqueTx(2.0);
        robot.SetTrajectoryJTorqueTy(2.0);
        robot.SetTrajectoryJTorqueTz(2.0);


        robot.LoadTrajectoryJ("/fruser/traj/test1011002.txt", 20, 1);
        DescPose startPos = new DescPose();
        robot.GetTrajectoryStartPose("/fruser/traj/test1011002.txt", startPos);
        robot.MoveCart(startPos, 0, 0, 40, 100.0, 100.0, -1.0, -1);

        ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
        System.out.println("Trajectory point num is " + pkg.trajectory_pnum);
        robot.SetTrajectoryJSpeed(40);
        robot.MoveTrajectoryJ();
//********************************轨迹文件读取运动 end *********************************88
    }


    private static void PointTableTest(Robot robot) {
//        robot.PointTableUpLoad("D://zUP/point_table_test1.db");//点位表上传
//        robot.PointTableDownLoad("point_table_test1.db", "D://zUP/");//点位表下载
//        String errStr = "";
//        robot.PointTableSwitch("point_table_test1.db", errStr);//切换点位表
//        //点位表更新LUA程序
//        robot.PointTableUpdateLua("point_table_test2.db", "1010Test.lua", errStr);
    }


    private static void RobotStateTest(Robot robot) {
        //robot.RobotEnable(0);//下使能
        //robot.Sleep(2000);
        ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();//获取机械臂实时状态
        System.out.println("Tool pos " + pkg.tl_cur_pos[0] + "  " + pkg.tl_cur_pos[1] + "  " + pkg.tl_cur_pos[2] + "  " + pkg.tl_cur_pos[3] + "  " + pkg.tl_cur_pos[4] + "  " + pkg.tl_cur_pos[5] + "  ");
        System.out.println("Joint pos " + pkg.jt_cur_pos[0] + "  " + pkg.jt_cur_pos[1] + "  " + pkg.jt_cur_pos[2] + "  " + pkg.jt_cur_pos[3] + "  " + pkg.jt_cur_pos[4] + "  " + pkg.jt_cur_pos[5] + "  ");
        System.out.println("Flange pos " + pkg.flange_cur_pos[0] + "  " + pkg.flange_cur_pos[1] + "  " + pkg.flange_cur_pos[2] + "  " + pkg.flange_cur_pos[3] + "  " + pkg.flange_cur_pos[4] + "  " + pkg.flange_cur_pos[5] + "  ");
        System.out.println("Tool " + pkg.tool);//工具号
        System.out.println("WObj " + pkg.user);//工件号
        System.out.println("Mode " + pkg.robot_mode);//机器人模式
        System.out.println("Enable " + pkg.rbtEnableState);//使能状态
        System.out.println("CI " + pkg.cl_dgt_input_h);//数字输入
        System.out.println("AI " + Arrays.toString(pkg.cl_analog_input));//控制箱模拟量输入
    }

    private static void GripperTest(Robot robot) {
//***********************************夹爪配置获取 begin ***********************************
        int company = 3;
        int device = 0;
        int softversion = 0;
        int bus = 1;
        int deviceID = -1;

        DeviceConfig gripperConfig = new DeviceConfig(company, device, softversion, bus);

        robot.SetGripperConfig(gripperConfig);
        robot.Sleep(1000);

        DeviceConfig getConfig = new DeviceConfig();
        robot.GetGripperConfig(getConfig);
        System.out.println("gripper 厂商:" + getConfig.company + " , 类型: " + getConfig.device + " , 软件版本: " + getConfig.softwareVersion);
//***********************************夹爪配置获取 end ***********************************

//***********************************夹爪激活、运动、状态获取 begin ***********************************
//        int index = 1;
//        byte act = 0;
//        int max_time = 30000;
//        byte block = 0;
//        int status = -1, fault = -1;
//        int rtn = -1;
//
//        rtn = robot.ActGripper(index, act);//激活夹爪
//        System.out.println("ActGripper rtn : " + rtn);
//        //robot.Sleep(1000);
//        act = 1;
//        rtn = robot.ActGripper(index, act);
//        System.out.println("ActGripper rtn : " + rtn);
//        //robot.Sleep(4000);
//
//        rtn = robot.MoveGripper(index, 80, 20, 50, max_time, block);//移动夹爪
//        System.out.println("MoveGripper rtn : " + rtn);
//        robot.Sleep(2000);
//        robot.MoveGripper(index, 20, 20, 50, max_time, block);//移动夹爪
//
//        robot.Sleep(4000);
////        ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();//获取状态
//        List<Integer> rtnArray = new ArrayList<Integer>() {};
//        rtnArray=robot.GetGripperMotionDone();
//        System.out.println("gripper motion done : " + rtnArray.get(2) +", " + rtnArray.get(1));
////        System.out.println("gripper motion done : " + pkg.gripper_motiondone +", " + pkg.gripper_fault);
//***********************************夹爪激活、运动、状态获取 end ***********************************

//***********************************正常进行抓取点和撤退点的计算 begin ***********************************
//        DescPose desc_pos1, desc_pos2;
//        desc_pos1 = new DescPose(-228.943, -584.228, 461.958,179.16, 5.559, 125.643);
//        desc_pos2 = new DescPose(-3.05, -627.474 , 461.967, 179.152, 5.565, 146.765);
//        robot.ComputePrePick(desc_pos1, 10, 0, desc_pos2);
//        System.out.println("ComputePrePick: " + desc_pos2.toString());
//
//        desc_pos2.tran.x = 0;
//        robot.ComputePostPick(desc_pos1, 10, 0, desc_pos2);
//        System.out.println("ComputePostPick: " + desc_pos2.toString());
//***********************************正常进行抓取点和撤退点的计算 end ***********************************

//        //传送带参考点
//***********************************传送带 begin ***********************************
        int rtn = -1;
        rtn = robot.ConveyorPointIORecord();//记录IO切入点
        System.out.println("ConveyorPointIORecord: rtn " + rtn);

        rtn = robot.ConveyorPointARecord();//记录A点
        System.out.println("ConveyorPointARecord: rtn " + rtn);

        rtn = robot.ConveyorRefPointRecord();//记录参考点
        System.out.println("ConveyorRefPointRecord: rtn  " + rtn);

        rtn = robot.ConveyorPointBRecord();//记录B点
        System.out.println("ConveyorPointBRecord: rtn " + rtn);

        //配置传送带
        robot.ConveyorSetParam(1, 10000, 2.0, 1, 1, 20, 0, 0, 100);
        System.out.println("ConveyorSetParam: rtn  " + rtn);


//        传送带跟踪抓取
        DescPose pos1 = new DescPose(-351.549, 87.914, 354.176, -179.679, -0.134, 2.468);
        DescPose pos2 = new DescPose(-351.203, -213.393, 351.054, -179.932, -0.508, 2.472);


        Object[] cmp = {0.0, 0.0, 0.0};
        rtn = robot.ConveyorCatchPointComp(cmp);//设置传动带抓取点补偿
        if (rtn != 0) {
            return;
        }
        System.out.println("ConveyorCatchPointComp: rtn  " + rtn);

        rtn = robot.MoveCart(pos1, 1, 0, 30.0, 180.0, 100.0, -1.0, -1);
        System.out.println("MoveCart: rtn  " + rtn);

        rtn = robot.ConveyorIODetect(10000);//传送带工件IO检测
        System.out.println("ConveyorIODetect: rtn   " + rtn);

        robot.ConveyorGetTrackData(1);//配置传送带跟踪抓取
        rtn = robot.ConveyorTrackStart(1);//跟踪开始
        System.out.println("ConveyorTrackStart: rtn  " + rtn);

        rtn = robot.ConveyorTrackMoveL("cvrCatchPoint", 1, 0, 100.0, 0.0, 100.0, -1.0);
        System.out.println("ConveyorTrackMoveL: rtn  " + rtn);

        rtn = robot.MoveGripper(1, 60, 60, 30, 30000, 0, 0, 0, 0, 0);
        System.out.println("MoveGripper: rtn  {rtn}");

        rtn = robot.ConveyorTrackMoveL("cvrRaisePoint", 1, 0, 100.0, 0.0, 100.0, -1.0);
        System.out.println("ConveyorTrackMoveL: rtn   " + rtn);

        rtn = robot.ConveyorTrackEnd();//传送带跟踪停止
        System.out.println("ConveyorTrackEnd: rtn  " + rtn);

        rtn = robot.MoveCart(pos2, 1, 0, 30.0, 180.0, 100.0, -1.0, -1);
        System.out.println("MoveCart: rtn  " + rtn);

        rtn = robot.MoveGripper(1, 100, 60, 30, 30000, 0, 0, 0, 0, 0);
        System.out.println("MoveGripper: rtn  " + rtn);
//
//***********************************传送带 end ***********************************

        //末端传感器配置参数、获取参数、激活和写入寄存器参数
//***********************************传感器配置与参数获取 begin ***********************************
//        DeviceConfig axleSensorConfig = new DeviceConfig(18, 0, 0, 1);
//        robot.AxleSensorConfig(axleSensorConfig);
//
//        DeviceConfig getConfig = new DeviceConfig();
//        robot.AxleSensorConfigGet(getConfig);
//        System.out.println("company is " + getConfig.company + ",  type is " + getConfig.device);
//
//        robot.AxleSensorActivate(1);
//
//        robot.AxleSensorRegWrite(1, 4, 6, 1, 0, 0, 0);
//***********************************传感器配置与参数获取 end ***********************************

    }


    private static void Stable(Robot robot) {
        int count = 0;
        while (true) {
            count++;
            DescPose desc_p1, desc_p2, desc_p3;
            desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
            desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
            desc_p3 = new DescPose(0, 0, 0, 0, 0, 0);

            desc_p1.tran.x = -104.846;
            desc_p1.tran.y = 309.573;
            desc_p1.tran.z = 336.647;
            desc_p1.rpy.rx = 179.681;
            desc_p1.rpy.ry = -0.419;
            desc_p1.rpy.rz = -92.692;

            desc_p2.tran.x = -318.287;
            desc_p2.tran.y = 158.502;
            desc_p2.tran.z = 346.184;
            desc_p2.rpy.rx = 179.602;
            desc_p2.rpy.ry = 1.081;
            desc_p2.rpy.rz = -46.342;

            desc_p3.tran.x = -67.635;
            desc_p3.tran.y = -387.487;
            desc_p3.tran.z = 283.656;
            desc_p3.rpy.rx = -175.639;
            desc_p3.rpy.ry = 0.886;
            desc_p3.rpy.rz = 93.416;

            JointPos j1 = new JointPos();
            JointPos j2 = new JointPos();
            JointPos j3 = new JointPos();


            robot.GetInverseKin(0, desc_p1, -1, j1);
            robot.GetInverseKin(0, desc_p2, -1, j2);
            robot.GetInverseKin(0, desc_p3, -1, j3);

            ExaxisPos epos = new ExaxisPos();
            DescPose offset_pos = new DescPose();

            robot.MoveJ(j1, desc_p1, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
            ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
            System.out.println("cur pos is " + Arrays.toString(pkg.jt_cur_pos) + "  motiondone state " + pkg.motion_done);
            robot.MoveJ(j2, desc_p2, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
            pkg = robot.GetRobotRealTimeState();
            System.out.println("cur pos is " + Arrays.toString(pkg.jt_cur_pos) + "  motiondone state " + pkg.motion_done + "  count is   " + count);
        }
    }


    private static void Welding(Robot robot) {
//        DescPose  desc_p1, desc_p2;
//        desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
//        desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
//
//        JointPos j1 = new JointPos(-28.529,-140.397,-81.08,-30.934,92.34,-5.629);
//        JointPos j2 = new JointPos(-11.045,-130.984,-104.495,-12.854,92.475,-5.547);
//
//        robot.GetForwardKin(j1,desc_p1);
//        robot.GetForwardKin(j2,desc_p2);
//
//
//        ExaxisPos epos = new ExaxisPos();
//        DescPose offset_pos = new DescPose();
//*********************************焊接起弧、收弧 begin **********************************
//        robot.MoveL(j1, desc_p1,0, 0, 20, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.ARCStart(0, 0, 10000);//焊接开始
//        robot.MoveL(j2, desc_p2,0, 0, 20, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.ARCEnd(0, 0, 10000);//焊接结束
//*********************************焊接起弧、收弧 end **********************************

//*********************************获取焊接电流电压、对应关系 begin **********************************
//        WeldCurrentAORelation currentRelation = new WeldCurrentAORelation(0, 1000, 0, 10, 0);
//        robot.WeldingSetCurrentRelation(currentRelation);
//        WeldVoltageAORelation voltageAORelation = new WeldVoltageAORelation(0, 100, 0, 10, 1);
//        robot.WeldingSetVoltageRelation(voltageAORelation);
//
//        WeldCurrentAORelation tmpCur = new WeldCurrentAORelation();
//        WeldVoltageAORelation tmpVol = new WeldVoltageAORelation();
//        robot.WeldingGetCurrentRelation(tmpCur);
//        robot.WeldingGetVoltageRelation(tmpVol);
//*********************************获取焊接电流电压、对应关系 end **********************************

//*********************************设置焊接电流电压值 begin **********************************
        robot.WeldingSetCurrent(0, 500, 0, 0);
        robot.WeldingSetVoltage(0, 60, 1, 0);
//*********************************设置焊接电流电压值 end **********************************

//*********************************设置摆动参数 begin **********************************
        robot.WeaveSetPara(0, 0, 2.0, 0, 0.0, 0, 0, 0, 100, 100, 50, 50, 1, 0);
//*********************************设置摆动参数 end **********************************

//*********************************摆动焊接 begin **********************************
        DescPose desc_p1 = new DescPose(688.259, -566.358, -139.354, -158.206, 0.324, -117.817);
        DescPose desc_p2 = new DescPose(700.078, -224.752, -149.191, -158.2, 0.239, -94.978);


        JointPos j1 = new JointPos(0, 0, 0, 0, 0, 0);
        JointPos j2 = new JointPos(0, 0, 0, 0, 0, 0);

        robot.GetInverseKin(0, desc_p1, -1, j1);
        robot.GetInverseKin(0, desc_p2, -1, j2);

        ExaxisPos epos = new ExaxisPos();
        DescPose offset_pos = new DescPose();

        robot.MoveL(j1, desc_p1, 3, 0, 30, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
        robot.WeaveSetPara(0, 0, 1.0, 0, 10.0, 0, 0, 0, 100, 100, 50, 50, 1, 0);
        robot.ARCStart(0, 0, 10000);
        robot.WeaveStart(0);
        robot.MoveL(j2, desc_p2, 3, 0, 10, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
        robot.ARCEnd(0, 0, 10000);
        robot.WeaveEnd(0);
//*********************************摆动焊接 end **********************************


//        robot.ArcWeldTraceControl(1, 0, 0, 0.06, 5, 5, 300, 1, -0.06, 5, 5, 300, 1, 0, 4, 1, 10);
//        robot.WeaveStart(0);
//        robot.WeaveStartSim(0);
//        //robot.WeaveInspectStart(0);
//        //robot.WeaveInspectEnd(0);
//        //robot.WeaveEndSim(0);
//        robot.ArcWeldTraceControl(0, 0, 0, 0.06, 5, 5, 300, 1, -0.06, 5, 5, 300, 1, 0, 4, 1, 10);
//        robot.WeaveEnd(0);

//        robot.WeldingSetCurrent(0, 500, 0, 0);
//        robot.WeldingSetVoltage(0, 60, 1, 0);
//        robot.Sleep(2000);

//*********************************送丝与送气 begin **********************************
//        robot.SetForwardWireFeed(0, 1);
//        robot.Sleep(2000);
//        robot.SetForwardWireFeed(0, 0);
//        robot.Sleep(2000);
//        robot.SetReverseWireFeed(0, 1);
//        robot.Sleep(2000);
//        robot.SetReverseWireFeed(0, 0);
//        robot.Sleep(2000);
//
//        robot.SetAspirated(0,1);
//        robot.Sleep(2000);
//        robot.SetAspirated(0,0);
//*********************************送丝与送气 end **********************************


        //焊丝寻位
//*********************************焊丝寻位 begin **********************************
//        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
//        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);
//
//        DescPose descStart = new DescPose(153.736,-715.249,-295.037,-179.829,2.613,-155.615);
//        JointPos jointStart = new JointPos(0,0,0,0,0,0);
//
//        DescPose descEnd = new DescPose(73.748,-645.825,-295.016,-179.828,2.608,-155.614);
//        JointPos jointEnd = new JointPos(0,0,0,0,0,0);
//
//        robot.GetInverseKin(0, descStart, -1, jointStart);
//        robot.GetInverseKin(0, descEnd, -1, jointEnd);
//
//        robot.MoveL(jointStart, descStart, 3, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);
//        robot.MoveL(jointEnd, descEnd, 3, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese,0, 100);
//
//        DescPose descREF0A = new DescPose(273.716,-723.539,-295.075,-179.829,2.608,-155.614);
//        JointPos jointREF0A = new JointPos(0,0,0,0,0,0);
//
//        DescPose descREF0B = new DescPose(202.588,-723.543,-295.039,-179.829,2.609,-155.614);
//        JointPos jointREF0B = new JointPos(0,0,0,0,0,0);
//
//        DescPose descREF1A = new DescPose(75.265,-525.091,-295.059,-179.83,2.609,-155.616);
//        JointPos jointREF1A = new JointPos(0,0,0,0,0,0);
//
//        DescPose descREF1B = new DescPose(75.258,-601.157,-295.075,-179.834,2.609,-155.616);
//        JointPos jointREF1B = new JointPos(0,0,0,0,0,0);
//
//        robot.GetInverseKin(0, descREF0A, -1, jointREF0A);
//        robot.GetInverseKin(0, descREF0B, -1, jointREF0B);
//        robot.GetInverseKin(0, descREF1A, -1, jointREF1A);
//        robot.GetInverseKin(0, descREF1B, -1, jointREF1B);
//
//        robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
//        robot.MoveL(jointREF0A, descREF0A, 3, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);  //起点
//        robot.MoveL(jointREF0B, descREF0B, 3, 0, 30, 100, 100, -1, exaxisPos, 1, 0, offdese, 0, 100);  //方向点
//        robot.WireSearchWait("REF0");
//        robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);
//
//        robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
//        robot.MoveL(jointREF1A, descREF1A, 3, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);  //起点
//        robot.MoveL(jointREF1B, descREF1B, 3, 0, 30, 100, 100, -1, exaxisPos, 1, 0, offdese, 0, 100);  //方向点
//        robot.WireSearchWait("REF1");
//        robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);
//
//
//        robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
//        robot.MoveL(jointREF0A, descREF0A, 3, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);  //起点
//        robot.MoveL(jointREF0B, descREF0B, 3, 0, 30, 100, 100, -1, exaxisPos, 1, 0, offdese, 0, 100);  //方向点
//        robot.WireSearchWait("RES0");
//        robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);
//
//        robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
//        robot.MoveL(jointREF1A, descREF1A, 3, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);  //起点
//        robot.MoveL(jointREF1B, descREF1B, 3, 0, 30, 100, 100, -1, exaxisPos, 1, 0, offdese, 0, 100);  //方向点
//        robot.WireSearchWait("RES1");
//        robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);
//
//        String[] varNameRef = { "REF0", "REF1", "#", "#", "#", "#"};
//        String[] varNameRes = { "RES0", "RES1", "#", "#", "#", "#" };
//
//        DescOffset offectPos = new DescOffset();
//        robot.GetWireSearchOffset(0, 0, varNameRef, varNameRes, offectPos);
//        robot.PointsOffsetEnable(offectPos.offsetFlag, offectPos.offset);
//        robot.MoveL(jointStart, descStart, 3, 1, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);
//        robot.MoveL(jointEnd, descEnd, 3, 1, 30, 100, 100, -1, exaxisPos, 1, 0, offdese, 0, 100);
//        robot.PointsOffsetDisable();
//*********************************焊丝寻位 end **********************************

//*********************************电弧跟踪 begin **********************************
//        robot.MoveJ(j1, desc_p1,1, 0, 10, 100, 100, epos, -1, 0, offset_pos);
//        robot.WeaveSetPara(0,0, 3.0, 0, 0.0, 0, 0, 0, 100, 100, 50, 50,1);
//        robot.ARCStart(0, 0, 10000);
//        robot.ArcWeldTraceControl(1, 0, 0, 0.06, 5, 5, 300, 1, -0.06, 5, 5, 300, 1, 0, 4, 1, 10);
//        robot.WeaveStart(0);
//        robot.MoveL(j2, desc_p2,1, 0, 10, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.ARCEnd(0, 0, 10000);
//        robot.ArcWeldTraceControl(0, 0, 0, 0.06, 5, 5, 300, 1, -0.06, 5, 5, 300, 1, 0, 4, 1, 10);
//        robot.WeaveEnd(0);
//*********************************电弧跟踪 end **********************************

//*********************************焊接工艺曲线参数配置与获取 begin **********************************
//        WeldingProcessParam param = new WeldingProcessParam(177.0,27.0,1000,178.0,28.0,176.0,26.0,1000);
//        robot.WeldingSetProcessParam(1, param);
//
//        WeldingProcessParam getParam = new WeldingProcessParam();
//        robot.WeldingGetProcessParam(1, getParam);
//*********************************焊接工艺曲线参数配置与获取 end **********************************

//*********************************焊接扩展IO功能配置 begin **********************************
//        robot.SetArcStartExtDoNum(1);
//        robot.SetAirControlExtDoNum(2);
//        robot.SetWireForwardFeedExtDoNum(3);
//        robot.SetWireReverseFeedExtDoNum(4);
//
//        robot.SetWeldReadyExtDiNum(5);
//        robot.SetArcDoneExtDiNum(6);
//        robot.SetExtDIWeldBreakOffRecover(7, 8);
//*********************************焊接扩展IO功能配置 end **********************************

//*********************************摆动预警 begin **********************************
//        DescPose desc_p1=new DescPose(-104.846,309.573,336.647,179.681,-0.419,-92.692);
//        DescPose desc_p2=new DescPose(-318.287,158.502,346.184,179.602,1.081,-46.342);
//        JointPos j1=new JointPos(0,0,0,0,0,0);
//        JointPos j2=new JointPos(0,0,0,0,0,0);
//
//        robot.GetInverseKin(0, desc_p1, -1, j1);
//        robot.GetInverseKin(0, desc_p2, -1, j2);
//
//        ExaxisPos epos = new ExaxisPos();
//        DescPose offset_pos = new DescPose();
//
//        robot.MoveL(j2, desc_p2,3, 0, 30, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.WeaveSetPara(0,0, 2.0, 0, 0.0, 0, 0, 0, 100, 100, 50, 50,1);
//        robot.WeaveInspectStart(0);
//        robot.MoveL(j2, desc_p2,3, 0, 30, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.WeaveInspectEnd(0);
//*********************************摆动预警 end **********************************


    }

    private static void TPD(Robot robot) {
//*************************************轨迹记录 begin ************************************
        int type = 1;
        String name = "tpd_2024";
        int period_ms = 2;
        int di_choose = 0;
        int do_choose = 0;

        robot.SetTPDDelete(name);

        robot.SetTPDParam(type, name, period_ms, di_choose, do_choose);

        robot.Mode(1);
        robot.Sleep(1000);
        robot.DragTeachSwitch(1);
        robot.SetTPDStart(type, name, period_ms, di_choose, do_choose);
        robot.Sleep(10000);
        robot.SetWebTPDStop();
        robot.DragTeachSwitch(0);
//*************************************轨迹记录 end ************************************

//*************************************轨迹复现 begin ************************************
        int tool = 0;
        int user = 0;
        double vel = 30.0;
        double acc = 100.0;
        double ovl = 100.0;
        double blendT = -1.0;
        int config = -1;
        byte blend = 1;

        DescPose desc_pose = new DescPose();
        robot.GetTPDStartPose(name, desc_pose);
        robot.SetTrajectoryJSpeed(100.0);

        robot.LoadTPD(name);
        robot.MoveCart(desc_pose, tool, user, vel, acc, ovl, blendT, config);
        robot.MoveTPD(name, blend, 80.0);
//*************************************轨迹复现 begin ************************************

    }

    private static void RobotState(Robot robot) {
//***************************** 机械臂安装角度查询 begin *****************************
        robot.SetRobotInstallAngle(23.4, 56.7);
        List<Number> rtnArr = robot.GetRobotInstallAngle();
        System.out.println("安装角度: " + rtnArr.get(1) + "  " + rtnArr.get(2));
        robot.SetRobotInstallAngle(0, 0);
//***************************** 机械臂安装角度查询 end *****************************


//        DescPose  desc_p1=new DescPose();
//
//        JointPos JP1=new JointPos(117.408,-86.777,81.499,-87.788,-92.964,92.959);
//        JointPos JP_test=new JointPos();
//        DescPose DP1 =new DescPose(327.359,-420.973,518.377,-177.199,3.209,114.449);
//        robot.GetInverseKin(0, DP1, -1, JP_test);
//        List<Integer> rtnArr =  robot.GetInverseKinHasSolution(0, DP1, JP1);//逆向是否有解
//        System.out.println("has Solution ? " + rtnArr.get(1));
//        robot.GetForwardKin(JP1, desc_p1);//正向运动学
//        JointPos j2 = new JointPos();
//        robot.GetInverseKinRef(0, DP1, JP1, JP_test);//逆向运动学

//*************************************获取固件、硬件版本 begin *********************************
//        String ctrlBoxBoardVersion = "";
//        String driver1Version = "";
//        String driver2Version = "";
//        String driver3Version = "";
//        String driver4Version = "";
//        String driver5Version = "";
//        String driver6Version = "";
//        String endBoardVersion = "";
//        robot.GetHardwareVersion(ctrlBoxBoardVersion ,driver1Version,  driver2Version,  driver3Version,
//                 driver4Version,  driver5Version,  driver6Version,  endBoardVersion);
//
//        robot.GetFirmwareVersion(ctrlBoxBoardVersion, driver1Version, driver2Version, driver3Version,
//                driver4Version, driver5Version, driver6Version, endBoardVersion);
//*************************************获取固件、硬件版本 end *********************************

//*************************************获取机械臂的SSH begin *********************************
//        String[] key = {""};
//        robot.GetSSHKeygen(key);
//        System.out.println("ssh key  " + key[0]);
//*************************************获取机械臂的SSH end *********************************

//*************************************获取负载重量和质心 begin *********************************
//        robot.SetLoadWeight(0);
//        robot.SetLoadCoord(new DescTran(0.0, 0.0, 0.0));
//
//        List<Number> rtnArr =  robot.GetTargetPayload(1);
//        DescTran cog = new DescTran();
//        robot.GetTargetPayloadCog(1, cog);
//
//        System.out.println("weight is " + rtnArr.get(1) + " cog is  " + cog.x + "  " + cog.y + "  " + cog.z);
//*************************************获取负载重量和质心 end *********************************

//*************************************获取机器人关节配置 begin *********************************
//        List<Integer> rtnArr = robot.GetRobotCurJointsConfig();
//        System.out.println("config is " + rtnArr.get(1));
//*************************************获取机器人关节配置 end *********************************

//*************************************获取机器人时钟 begin *********************************
//        List<Number> rtnArr = robot.GetSystemClock();
//        System.out.println("systom clock is  " + rtnArr.get(1));
//*************************************获取机器人时钟 end *********************************

//*************************************获取机器人当前速度 begin *********************************
//        List<Number> rtnArr = robot.GetDefaultTransVel();
//        System.out.println("机器人当前速度为: " + rtnArr.get(1));
//*************************************获取机器人当前速度 end *********************************

//*************************************获取机器人工具/工件坐标系 begin *********************************
//        DescPose offset = new DescPose();
//        robot.GetTCPOffset(1, offset);//工具
//        robot.GetWObjOffset(1, offset);//工件
//        System.out.println("offset is " + offset);
//*************************************获取机器人工具/工件坐标系 end *********************************

//*************************************获取机器人软限位 begin *********************************
//        Object[] neg_deg = new Object[]{0, 0 , 0, 0, 0, 0};
//        Object[] pos_deg = new Object[]{0, 0 , 0, 0, 0, 0};
//        robot.GetJointSoftLimitDeg(1,  neg_deg,  pos_deg);
//        System.out.println("neg is " + Arrays.toString(neg_deg) + " pos is " + Arrays.toString(pos_deg));
//*************************************获取机器人软限位 end *********************************

//*************************************获取机器人完成信号 begin *********************************
//        JointPos JP1=new JointPos(117.408,-86.777,81.499,-87.788,-92.964,92.959);
//        DescPose DP1 =new DescPose(327.359,-420.973,518.377,-177.199,3.209,114.449);
//        JointPos JP2=new JointPos(72.515,-86.774,81.525,-87.724,-91.964,92.958);
//        DescPose DP2=new DescPose(-63.512,-529.698,517.946,-178.192,3.070,69.554);
//
//        ExaxisPos epos = new ExaxisPos();
//        DescPose offset_pos = new DescPose();
//        robot.SetSpeed(10);
//        robot.MoveL(JP1, DP1,0, 0, 50, 100, 100, 0, epos, 0, 0, offset_pos, 0, 100);
//        robot.MoveL(JP2, DP2,0, 0, 50, 100, 100, 0, epos, 0, 0, offset_pos, 0, 100);
//        while (true)
//        {
//            ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
//            System.out.println("motion done is " + pkg.motion_done);
//            robot.Sleep(100);
//        }
//*************************************获取机器人完成信号 end *********************************

//*************************************获取机器人示教点 begin *********************************
//        List<Number> rtnArr = robot.GetRobotTeachingPoint("P1");
//        System.out.println("point data  " + rtnArr);
//*************************************获取机器人示教点 end *********************************

    }

    private static void RobotSafety(Robot robot) {
//****************************** 碰撞等级 begin***********************
//        Object[] config = {2.0, 2.0, 2.0, 2.0, 2.0, 2.0};
//        robot.SetAnticollision(0, config, 1);
//        int safetyMargin[]={10,10,10,10,10,10};
//        robot.SetCollisionStrategy(0,1000,10,safetyMargin);
//
//        robot.ProgramLoad("/fruser/test.lua");
//        robot.ProgramRun();
//****************************** 碰撞等级 end***********************

//****************************** 增加软限位 begin***********************
//        Object[] plimit = { 170.0, 80.0, 150.0, 80.0, 170.0, 160.0 };
//        robot.SetLimitPositive(plimit);
//
//        Object[] nlimit = { -170.0, -260.0, -150.0, -260.0, -170.0, -160.0 };
//        robot.SetLimitNegative(nlimit);
//****************************** 增加软限位 end***********************

//****************************** 清除所有错误 begin***********************
//        robot.SetLoadWeight(123.0);
//        robot.Sleep(3000);
//        robot.ResetAllError();
//****************************** 清除所有错误 end***********************

//****************************** 设置关节摩擦力补偿 begin ***********************
//        Object[] lcoeff = { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 };
//        Object[] wcoeff = { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 };
//        Object[] ccoeff = { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 };
//        Object[] fcoeff = { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 };
//
//        robot.FrictionCompensationOnOff(1);
//
//        robot.SetFrictionValue_level(lcoeff);//正装
//
//        robot.SetFrictionValue_wall(wcoeff);//侧装
//
//        robot.SetFrictionValue_ceiling(ccoeff);//倒装
//
//        robot.SetFrictionValue_freedom(fcoeff);//自由安装
//****************************** 设置关节摩擦力补偿 end ***********************


//        robot.SetCollisionDetectionMethod(1);//设置静态下碰撞检测
//        robot.SetStaticCollisionOnOff(1);

//****************************** 关节功率扭矩检测 begin ***********************
//        robot.DragTeachSwitch(1);
//        robot.SetPowerLimit(1, 2);
//        Object[] torques = { 0, 0, 0, 0, 0, 0 };
//        robot.GetJointTorques(1, torques);
//
//        int count = 20;
//        robot.ServoJTStart(); //   #servoJT开始
//        int error = 0;
//        while (count > 0)
//        {
//            //torques[0] = (double)torques[0] + 0.1;//  #每次1轴增加0.1NM，运动100次
//            error = robot.ServoJT(torques, 0.001);  //# 关节空间伺服模式运动
//            count = count - 1;
//            robot.Sleep(1);
//        }
//        error = robot.ServoJTEnd();  //#伺服运动结束

//****************************** 关节功率扭矩检测 end ***********************

    }


    private static void CommonSets(Robot robot) {
//**********************************设置速度 begin***************************
//        robot.Mode(1);
//        for(int i = 0; i < 90; i++)
//        {
//            robot.SetSpeed(i + 1);
//            robot.Sleep(50);
//        }
//
//        robot.Mode(0);
//**********************************设置速度 end***************************

//**********************************设置加速度 begin***************************
        JointPos j1 = new JointPos(-114.756, -109.423, 87.751, -65.834, -52.210, 92.992);
        JointPos j2 = new JointPos(-51.811, -91.530, 74.859, -81.580, -121.913, 92.990);
        JointPos j3 = new JointPos(-105.350, -65.747, 32.346, -26.434, -60.784, 92.992);
        JointPos j4 = new JointPos(-79.015, -81.540, 47.786, -57.729, -88.577, 92.991);
        JointPos j5 = new JointPos(-102.647, -99.334, 68.842, -59.256, -88.407, 92.937);
        JointPos j6 = new JointPos(-78.990, -78.608, 38.428, -51.812, -88.310, 92.939);

        DescPose desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
        DescPose desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
        DescPose desc_p3 = new DescPose(0, 0, 0, 0, 0, 0);
        DescPose desc_p4 = new DescPose(0, 0, 0, 0, 0, 0);
        DescPose desc_p5 = new DescPose(0, 0, 0, 0, 0, 0);
        DescPose desc_p6 = new DescPose(0, 0, 0, 0, 0, 0);

        robot.GetForwardKin(j1, desc_p1);
        robot.GetForwardKin(j2, desc_p2);
        robot.GetForwardKin(j3, desc_p3);
        robot.GetForwardKin(j4, desc_p4);
        robot.GetForwardKin(j5, desc_p5);
        robot.GetForwardKin(j6, desc_p6);
        ExaxisPos epos = new ExaxisPos();
        DescPose offset_pos = new DescPose();
//
//        robot.SetSpeed(60);
////
//        robot.SetOaccScale(10);
////        robot.MoveL(j1, desc_p1,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
////        robot.MoveL(j2, desc_p2,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
////
////        robot.SetOaccScale(90);
////        robot.MoveL(j1, desc_p1,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
////        robot.MoveL(j2, desc_p2,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);

        //测试加速度
//        ExaxisPos epos = new ExaxisPos();
//        DescPose offset_pos = new DescPose();
//        JointPos JP1=new JointPos(117.408,-86.777,81.499,-87.788,-92.964,92.959);
//        DescPose DP1 =new DescPose(327.359,-420.973,518.377,-177.199,3.209,114.449);
//        JointPos JP2=new JointPos(72.515,-86.774,81.525,-87.724,-91.964,92.958);
//        DescPose DP2=new DescPose();
//        JointPos JP3=new JointPos(89.281,-102.959,81.527,-69.955,-86.755,92.958);
//        DescPose DP3=new DescPose();
//        robot.GetForwardKin(JP2,DP2);
//        robot.GetForwardKin(JP3,DP3);
//
//        robot.SetSpeed(60);
//
//        robot.SetOaccScale(10);
//        robot.MoveL(JP1, DP1,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.MoveL(JP2, DP2,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//
//        robot.SetOaccScale(90);
//        robot.MoveL(JP1, DP1,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.MoveL(JP2, DP2,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//**********************************设置加速度 end***************************

//**********************************设置和获取系统变量 begin***************************
//        for(int i = 1; i < 10; i++)
//        {
//            robot.SetSysVarValue(i, i * 10);
//        }
//        for(int i = 1; i < 10; i++)
//        {
//            List<Number> rtnArr = robot.GetSysVarValue(i);
//            System.out.println("SysVarValue " +  i  + " is " + rtnArr.get(1));
//        }
//**********************************设置和获取系统变量 end***************************

//**********************************六点法工具坐标系标定 begin ***************************
//        JointPos jp1=new JointPos(-89.407,-148.279,-83.169,-45.689,133.689,41.705);
//        JointPos jp2=new JointPos(-67.595,-143.7,-88.006,-48.514,57.073,56.189);
//        JointPos jp3=new JointPos(-88.229,-152.355,-67.815,-78.07,129.029,58.739);
//        JointPos jp4=new JointPos(-77.528,-141.519,-89.826,-37.184,90.274,41.769);
//        JointPos jp5=new JointPos(-76.744,-138.219,-97.714,-32.595,90.255,42.558);
//        JointPos jp6=new JointPos(-77.595,-138.454,-90.065,-40.014,90.275,41.709);
//        ExaxisPos epos = new ExaxisPos();
//        DescPose offset_pos = new DescPose();
//
//        DescPose desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p3 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p4 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p5 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p6 = new DescPose(0, 0, 0, 0, 0, 0);
//        robot.GetForwardKin(jp1, desc_p1);
//        robot.GetForwardKin(jp2, desc_p2);
//        robot.GetForwardKin(jp3, desc_p3);
//        robot.GetForwardKin(jp4, desc_p4);
//        robot.GetForwardKin(jp5, desc_p5);
//        robot.GetForwardKin(jp6, desc_p6);
//        robot.MoveJ(jp1, desc_p1,0, 0, 30, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetToolPoint(1);
//
//        robot.MoveJ(jp2, desc_p2,0, 0, 30, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetToolPoint(2);
//
//        robot.MoveJ(jp3, desc_p3,0, 0, 30, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetToolPoint(3);
//
//        robot.MoveJ(jp4, desc_p4,0, 0, 30, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetToolPoint(4);
//
//        robot.MoveJ(jp5, desc_p5,0, 0, 30, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetToolPoint(5);
//
//        robot.MoveJ(jp6, desc_p6,0, 0, 30, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetToolPoint(6);
//
//        DescPose coord = new DescPose();
//        robot.ComputeTool(coord);
//        System.out.println("result is " + coord.tran.x + "  " + coord.tran.y + "  " + coord.tran.z + "  " + coord.rpy.rx + "  " + coord.rpy.ry + "  " + coord.rpy.rz);
//**********************************六点法工具坐标系标定 end ***************************

//**********************************4点法标定 begin ***************************
//        JointPos jp1=new JointPos(-89.407,-148.279,-83.169,-45.689,133.689,41.705);
//        JointPos jp2=new JointPos(-67.595,-143.7,-88.006,-48.514,57.073,56.189);
//        JointPos jp3=new JointPos(-88.229,-152.355,-67.815,-78.07,129.029,58.739);
//        JointPos jp4=new JointPos(-77.528,-141.519,-89.826,-37.184,90.274,41.769);
//        ExaxisPos epos = new ExaxisPos();
//        DescPose offset_pos = new DescPose();
//        DescPose desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p3 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p4 = new DescPose(0, 0, 0, 0, 0, 0);
//        robot.GetForwardKin(jp1, desc_p1);
//        robot.GetForwardKin(jp2, desc_p2);
//        robot.GetForwardKin(jp3, desc_p3);
//        robot.GetForwardKin(jp4, desc_p4);
//
//        robot.MoveJ(jp1, desc_p1,0, 0, 30, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetTcp4RefPoint(1);
//
//        robot.MoveJ(jp2, desc_p2,0, 0, 30, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetTcp4RefPoint(2);
//
//        robot.MoveJ(jp3, desc_p3,0, 0, 30, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetTcp4RefPoint(3);
//
//        robot.MoveJ(jp4, desc_p4,0, 0, 30, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetTcp4RefPoint(4);
//
//        DescPose coord4 = new DescPose();
//        robot.ComputeTcp4(coord4);
//        System.out.println("result is " + coord4.tran.x + "  " + coord4.tran.y + "  " + coord4.tran.z + "  " + coord4.rpy.rx + "  " + coord4.rpy.ry + "  " + coord4.rpy.rz);
//**********************************4点法标定 end ***************************

//**********************************设置工具坐标系和列表 begin ***************************
//        DescPose coord = new DescPose(0,  0,  20,  0,  0,  0);
//        robot.SetToolCoord(5, coord, 0, 0);
//        robot.SetToolList(5, coord, 0, 0);
//**********************************设置工具坐标系和列表 end ***************************

//**********************************外部工具坐标系标定 begin ***************************
//        JointPos j1 = new JointPos(-84.787, -152.056,-75.689 , -37.899, 94.486,41.709);
//        JointPos j2 = new JointPos(-79.438,-152.139,-75.634,-37.469,94.065,47.058);
//        JointPos j3 = new JointPos(-84.788,-145.179,-77.119,-43.345,94.487,41.709);
//
//
//        DescPose desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p3 = new DescPose(0, 0, 0, 0, 0, 0);
//
//        robot.GetForwardKin(j1, desc_p1);
//        robot.GetForwardKin(j2, desc_p2);
//        robot.GetForwardKin(j3, desc_p3);
//
//        ExaxisPos epos = new ExaxisPos();
//        DescPose offset_pos = new DescPose();
//
//        robot.MoveJ(j1, desc_p1,0, 0, 20, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetExTCPPoint(1);
//
//        robot.MoveJ(j2, desc_p2,0, 0, 20, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetExTCPPoint(2);
//
//        robot.MoveJ(j3, desc_p3,0, 0, 20, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetExTCPPoint(3);
//
//        DescPose coordE = new DescPose();
//        robot.ComputeExTCF(coordE);
//        System.out.println("result is " + coordE.tran.x + "  " + coordE.tran.y + "  " + coordE.tran.z + "  " + coordE.rpy.rx + "  " + coordE.rpy.ry + "  " + coordE.rpy.rz);
//**********************************外部工具坐标系标定 end ***************************

//**********************************设置外部工具坐标系和列表 begin ***************************
//        DescPose coordE = new DescPose(100,  0,  0,  0,  0,  0);
//        robot.SetExToolCoord(5, coordE, coordE);
//        robot.SetExToolList(5,coordE, coordE);
//**********************************设置外部工具坐标系和列表 end ***************************

//**********************************工件坐标系的标定 begin ***************************
//        JointPos j1 = new JointPos(-84.787, -152.056,-75.689,-37.899,94.486,41.709);
//        JointPos j2 = new JointPos(-79.438,-152.139,-75.634,-37.469,94.065,47.058);
//        JointPos j3 = new JointPos(-84.788,-145.179,-77.119,-43.345,94.487,41.709);
//        DescPose desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
//        DescPose desc_p3 = new DescPose(0, 0, 0, 0, 0, 0);
//
//        robot.GetForwardKin(j1, desc_p1);
//        robot.GetForwardKin(j2, desc_p2);
//        robot.GetForwardKin(j3, desc_p3);
//
//        ExaxisPos epos = new ExaxisPos();
//        DescPose offset_pos = new DescPose();
//
//        robot.MoveJ(j1, desc_p1,0, 0, 20, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetWObjCoordPoint(1);
//
//        robot.MoveJ(j2, desc_p2,0, 0, 20, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetWObjCoordPoint(2);
//
//        robot.MoveJ(j3, desc_p3,0, 0, 20, 100, 100, epos, -1, 0, offset_pos);
//        robot.SetWObjCoordPoint(3);
//
//        DescPose coordE = new DescPose();
//        robot.ComputeWObjCoord(0, coordE);
//        System.out.println("result is " + coordE.tran.x + "  " + coordE.tran.y + "  " + coordE.tran.z + "  " + coordE.rpy.rx + "  " + coordE.rpy.ry + "  " + coordE.rpy.rz);
//
//        robot.SetWObjCoord(5, coordE,0);
//        robot.SetWObjList(5,coordE,0);
//**********************************设置工件坐标系的标定 end ***************************

//**********************************设置末端负载 begin ***************************
//        robot.SetLoadWeight(2);
//        robot.SetLoadCoord(new DescTran(1.0, 2.0, 3.0));
//**********************************设置末端负载 end ***************************

//**********************************设置机器人安装方式 begin ***************************
//        robot.SetRobotInstallPos(0);
//**********************************设置机器人安装方式 begin ***************************

//**********************************设置机器人安装角度 begin ***************************
//        robot.SetRobotInstallAngle(0, 0);
//**********************************设置机器人安装角度 begin ***************************

    }

    private static void IOTest(Robot robot) {
//****************************** SetDO begin***********************
        robot.SetDO(8, 1, 0, 0);
        robot.Sleep(3000);
        robot.SetDO(8, 0, 0, 0);
//****************************** SetDO end***********************

//****************************** SetToolDO begin***********************
        robot.SetToolDO(0, 1, 0, 0);
        robot.Sleep(3000);
        robot.SetToolDO(0, 0, 0, 0);
//****************************** SetToolDO end***********************

//****************************** SetAO begin***********************
        for (int i = 0; i < 90; i++) {
            robot.SetAO(0, i + 1, 0);
            robot.SetAO(1, i + 1, 0);
            robot.Sleep(50);
        }
        robot.SetAO(0, 0.0, 0);
        robot.SetAO(1, 0.0, 0);
//****************************** SetAO end***********************

//****************************** SetToolAO begin***********************
        for (int i = 0; i < 99; i++) {
            robot.SetToolAO(0, i + 1, 0);
            robot.Sleep(50);
        }
        robot.SetToolAO(0, 0.0, 0);
//****************************** SetToolAO end***********************

//****************************** Wait begin***********************
        System.out.println("wait  start ");
        robot.WaitDI(1, 1, 10000, 0);//WaitDI
        robot.WaitMultiDI(0, 6, 6, 10000, 0);//WaitMultiDI
        robot.WaitToolDI(0, 1, 5000, 0);//WaitToolDI
        robot.WaitAI(0, 0, 8.0, 5000, 0);//WaitAI
        robot.WaitToolAI(0, 0, 20, 5000, 0);//WaitToolAI
        System.out.println("wait  end ");
//****************************** Wait end***********************


//****************************** MoveAOStart/Stop  MoveToolAOStart/Stop begin***********************
//        robot.MoveToolAOStart(0, 100, 80, 1);//末端AO飞拍开始
//        robot.MoveAOStart(0, 100, 80, 1);//控制箱AO飞拍
//        DescPose  desc_p1, desc_p2;
//
//        desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
//        desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
//
//        JointPos j1 = new JointPos(-81.684,-106.159,-74.447,-86.33,94.725,41.639);
//        JointPos j2 = new JointPos(-102.804,-106.159,-74.449,-86.328,94.715,41.639);
//
//        robot.GetForwardKin(j1,desc_p1);
//        robot.GetForwardKin(j2,desc_p2);
//
//        ExaxisPos epos = new ExaxisPos();
//        DescPose offset_pos = new DescPose();
//        robot.MoveL(j1, desc_p1,0, 0, 30, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.MoveL(j2, desc_p2,0, 0, 30, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.MoveToolAOStop();
//        robot.MoveAOStop();
//****************************** MoveAOStart/Stop  MoveToolAOStart/Stop end***********************

//        robot.SetDO(1, 1, 0, 0);
//        robot.SetDO(2, 1, 0, 0);
//        robot.SetDO(3, 1, 0, 0);
//
//        robot.SetAO(0, 50, 0);//A0口电流/电压
//        robot.SetAO(1, 70, 0);
//
//        robot.SetToolDO(0, 1, 0, 0);//设置工具数字量输出
//        robot.SetToolDO(1, 1, 0, 0);
//
//        robot.SetToolAO(0, 50, 0);
//
//        robot.SetOutputResetCtlBoxDO(1);
//        robot.SetOutputResetCtlBoxAO(1);
//        robot.SetOutputResetAxleDO(1);
//        robot.SetOutputResetAxleAO(1);
//
//        robot.Sleep(3000);
//
//        robot.ProgramLoad("/fruser/test.lua");
//        robot.ProgramRun();
    }

    private static void Moves(Robot robot) {
        DescPose tcoord, desc_p1, desc_p2, desc_p3, desc_p4;//笛卡尔空间位置与姿态
        tcoord = new DescPose(0, 0, 0, 0, 0, 0);
        desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
        desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
        desc_p3 = new DescPose(0, 0, 0, 0, 0, 0);
        desc_p4 = new DescPose(0, 0, 0, 0, 0, 0);

        desc_p1.tran.x = -104.846;
        desc_p1.tran.y = 309.573;
        desc_p1.tran.z = 336.647;
        desc_p1.rpy.rx = 179.681;
        desc_p1.rpy.ry = -0.419;
        desc_p1.rpy.rz = -92.692;

        desc_p2.tran.x = -318.287;
        desc_p2.tran.y = 158.502;
        desc_p2.tran.z = 346.184;
        desc_p2.rpy.rx = 179.602;
        desc_p2.rpy.ry = 1.081;
        desc_p2.rpy.rz = -46.342;

        desc_p3.tran.x = -352.414;
        desc_p3.tran.y = 24.059;
        desc_p3.tran.z = 395.376;
        desc_p3.rpy.rx = 179.755;
        desc_p3.rpy.ry = -1.045;
        desc_p3.rpy.rz = -23.877;

        desc_p4.tran.x = 195.474;
        desc_p4.tran.y = 423.278;
        desc_p4.tran.z = 228.509;
        desc_p4.rpy.rx = -179.199;
        desc_p4.rpy.ry = -0.567;
        desc_p4.rpy.rz = -130.209;

        JointPos j1 = new JointPos();
        JointPos j2 = new JointPos();
        JointPos j3 = new JointPos();
        JointPos j4 = new JointPos();
        robot.GetInverseKin(0, desc_p1, -1, j1);//逆向运动学求解
        robot.GetInverseKin(0, desc_p2, -1, j2);
        robot.GetInverseKin(0, desc_p3, -1, j3);
        robot.GetInverseKin(0, desc_p4, -1, j4);
        ExaxisPos epos = new ExaxisPos();
        DescPose offset_pos = new DescPose();

        JointPos JP1 = new JointPos(117.408, -86.777, 81.499, -87.788, -92.964, 92.959);
        DescPose DP1 = new DescPose(327.359, -420.973, 518.377, -177.199, 3.209, 114.449);
        JointPos JP2 = new JointPos(72.515, -86.774, 81.525, -87.724, -91.964, 92.958);
        DescPose DP2 = new DescPose(-63.512, -529.698, 517.946, -178.192, 3.07, 69.554);
//        JointPos JP3=new JointPos(89.281,-102.959,81.527,-69.955,-86.755,92.958);
//        DescPose DP3=new DescPose();
//        robot.GetForwardKin(JP3,DP3);

        robot.MoveJ(JP1, DP1, 0, 0, 30, 30, 100, epos, -1, 0, offset_pos);//关节空间运动
        robot.MoveJ(JP2, DP2, 0, 0, 30, 100, 100, epos, -1, 0, offset_pos);//关节空间运动
//        robot.MoveL(JP2, DP2,0, 0, 30, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);//直线运动
//        robot.MoveCart(DP2, 0, 0, 30.0, 100.0, 100.0, -1.0, -1);
//        robot.MoveC(JP3, DP3, 0, 0, 30, 100, epos, 0, offset_pos, JP1, DP1, 0, 0, 100, 100, epos, 0, offset_pos, 100, -1);
//        robot.Circle(JP3, DP3, 0, 0, 10, 100.0, epos, JP2, DP2, 0, 0, 100.0, 100.0, epos, 100.0, 0, offset_pos);


//        robot.MoveL(j2, desc_p2,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);//直线运动
//        robot.MoveC(j1, desc_p1, 0, 0, 100, 100, epos, 0, offset_pos, j2, desc_p2, 0, 0, 100, 100, epos, 0, offset_pos, 100, -1);
//        robot.Circle(j1, desc_p1, 0, 0, 100.0, 100.0, epos, j2, desc_p2, 0, 0, 100.0, 100.0, epos, 100.0, 0, offset_pos);
//        robot.MoveCart(desc_p3, 0, 0, 100.0, 100.0, 100.0, -1.0, -1);
//        robot.StartJOG(0, 1, 0, 30, 100, 90);//关节点动
        //robot.Sleep(3000);
//        robot.StopJOG(1);//点动停止
//        robot.ImmStopJOG();//点动立即停止
//        SpiralParam param = new SpiralParam(5,10.0,30.0,10.0,5.0,0);//螺旋线
//        robot.NewSpiral(JP1, DP1, 0, 0, 50, 100, epos, 100, 0, offset_pos, param);

//        JointPos j5 = new JointPos();
//        ExaxisPos ePos=new ExaxisPos();
//        int ret = robot.GetActualJointPosDegree(j5);
//        if (ret == 0)
//        {
//            int count = 200;
//            while (count > 0)
//            {
//                robot.ServoJ(j5, ePos,100, 100, 0.008, 0, 0);
//                j5.J1 += 0.2;//1关节位置增加
//                count -= 1;
//                robot.WaitMs((int)(8));
//            }
//        }

//*********************************ServoCart begin******************************
//        DescPose desc_pos_dt = new DescPose(0, 0, 0, 0, 0, 0);
//        desc_pos_dt.tran.z = -0.5;
//        Object[] pos_gain = { 0.0, 0.0, 1.0, 0.0, 0.0, 0.0 };//仅z轴增加
//        int mode = 2;//工具坐标系下增量运动
//        float vel = 0.0f;
//        float acc = 0.0f;
//        float cmdT = 0.008f;
//        float filterT = 0.0f;
//        float gain = 0.0f;
//        int count = 200;
//
//        robot.SetSpeed(20);
//
//        while (count > 0)
//        {
//            robot.ServoCart(mode, desc_pos_dt, pos_gain, acc, vel, cmdT, filterT, gain);
//            count -= 1;
//            robot.WaitMs((int)(cmdT * 1000));
//        }
        //*********************************ServoCart end******************************


//*********************************Spline begin******************************
//        robot.MoveJ(j1, desc_p1,4, 0, 100, 100, 100, epos, -1, 0, offset_pos);
//        robot.SplineStart();
//        robot.SplinePTP(j4, desc_p4, 0, 0, 100, 100, 100);
//        robot.SplinePTP(j1, desc_p1, 0, 0, 100, 100, 100);
//        robot.SplinePTP(j2, desc_p2, 0, 0, 100, 100, 100);
//        robot.SplinePTP(j3, desc_p3, 0, 0, 100, 100, 100);
//
//        robot.SplinePTP(JP1, DP1, 0, 0, 50, 100, 100);
//        robot.SplineEnd();
//*********************************Spline end******************************

//*********************************New Spline begin******************************
//        DescPose desc_p1 =new DescPose(-104.846, 309.573, 336.647, 179.681, -0.419, -92.692);
//        DescPose desc_p2=new DescPose(-194.846, 309.573, 336.647, 179.681,-0.419, -92.692;);
//        DescPose desc_p3=new DescPose(-254.846, 259.573,336.647, 179.681, -0.419, -92.692;);
//        DescPose desc_p4=new DescPose(-304.846,259.573, 336.647, 179.681, -0.419, -92.692;);
//        JointPos j1 = new JointPos();
//        JointPos j2 = new JointPos();
//        JointPos j3 = new JointPos();
//        JointPos j4 = new JointPos();
//        robot.GetInverseKin(0, desc_p1, -1, j1);//逆向运动学求解
//        robot.GetInverseKin(0, desc_p2, -1, j2);
//        robot.GetInverseKin(0, desc_p3, -1, j3);
//        robot.GetInverseKin(0, desc_p4, -1, j4);
//        robot.MoveCart(desc_p1, 0, 0, 100.0, 100.0, 100.0, -1.0, -1);
//        robot.NewSplineStart(0, 5000);//新样条开始
//        robot.NewSplinePoint(j1, desc_p1, 0, 0, 100, 100, 100, 50, 0);//新样条指令点
//        robot.NewSplinePoint(j2, desc_p2, 0, 0, 100, 100, 100, 50, 0);
//        robot.NewSplinePoint(j3, desc_p3, 0, 0, 100, 100, 100, 50, 0);
//        robot.NewSplinePoint(j4, desc_p4, 0, 0, 100, 100, 100, 50, 1);
//        robot.NewSplineEnd();//新样条结束
//*********************************New Spline end******************************

//*********************************点位偏移 begin******************************
//        robot.MoveL(j1, desc_p1,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.MoveL(j2, desc_p2,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//
//        DescPose off = new DescPose(0, 0, 100, 0, 0, 0);
//        robot.PointsOffsetEnable(0, off);
//        robot.MoveL(j1, desc_p1,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.MoveL(j2, desc_p2,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.PointsOffsetDisable();
//
//        robot.MoveL(JP1, DP1,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.MoveL(JP2, DP2,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//
//        DescPose off = new DescPose(0, 0, 50, 0, 0, 0);
//        robot.PointsOffsetEnable(0, off);
//        robot.MoveL(JP1, DP1,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.MoveL(JP2, DP2,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.PointsOffsetDisable();
//*********************************点位偏移 end******************************


//        desc_p1.tran.x = 279.039;
//        desc_p1.tran.y = 195.765;
//        desc_p1.tran.z = 274.245;
//        desc_p1.rpy.rx = 274.245;
//        desc_p1.rpy.ry = -1.997;
//        desc_p1.rpy.rz = -165.405;
//
//        desc_p2.tran.x = -316.395;
//        desc_p2.tran.y = 126.269;
//        desc_p2.tran.z = 299.333;
//        desc_p2.rpy.rx = 179.649;
//        desc_p2.rpy.ry = 2.389;
//        desc_p2.rpy.rz = -42.183;
//
//        robot.GetInverseKin(0, desc_p1, -1, j1);
//        robot.GetInverseKin(0, desc_p2, -1, j2);
//


//*********************************超速保护 begin******************************
        //robot.MoveL(j1, desc_p1,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 3, 100);
        //robot.MoveL(j2, desc_p2,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 3, 100);

        //测试
        //robot.MoveL(JP1, DP1,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 3, 100);
        //robot.MoveL(JP2, DP2,0, 0, 100, 100, 100, -1, epos, 0, 0, offset_pos, 3, 100);
//*********************************超速保护 end******************************

    }

    private static void Standard(Robot robot) {
//******************************获取版本号和ip begin ***************************
        String[] ip = {""};
        String version = "";
        byte state = 0;

        version = robot.GetSDKVersion();
        System.out.println("SDK version : " + version);
        int rtn = robot.GetControllerIP(ip);
        System.out.println("controller ip : " + ip[0] + "  " + rtn);
//******************************获取版本号和ip end ***************************

        robot.Mode(1);//1-手动模式  0-自动模式
        robot.Sleep(1000);
        robot.DragTeachSwitch(1);//进入拖动模式
        robot.Sleep(1000);
        ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
        System.out.println("drag state : " + pkg.robot_state);
        robot.Sleep(1000);
        robot.DragTeachSwitch(0);//退出拖动模式
        robot.Sleep(1000);
//
//
        pkg = robot.GetRobotRealTimeState();
        System.out.println("drag state : " + pkg.robot_state);
        if (pkg.robot_state == 4) {
            System.out.println("拖动模式");
        } else {
            System.out.println("非拖动模式");
        }

//使能和手动、自动切换
//        robot.RobotEnable(0);//下使能
//        robot.Sleep(1000);
//        robot.RobotEnable(1);//上使能
//        robot.Sleep(1000);
//        robot.RobotEnable(0);
//        robot.Sleep(1000);
//        robot.RobotEnable(1);
//        robot.Sleep(1000);
//
//        robot.Mode(0);//手动、自动状态切换 0-自动
//        robot.Sleep(1000);
//        robot.Mode(1);
//        robot.Sleep(1000);
//        robot.Mode(0);
//        robot.Sleep(1000);
//        robot.Mode(1);
//        robot.Sleep(1000);
    }

    private static void ForceSensor(Robot robot) {
//**********************************获取力传感器配置接口 begin ******************************
//        DeviceConfig config = new DeviceConfig();
//        config.company = 24;
//        config.device = 0;
//        config.softwareVersion = 0;
//        config.bus = 0;
//
//        robot.FT_SetConfig(config);
//        robot.Sleep(1000);
//        config.company = 0;
//        robot.FT_GetConfig(config);
//        System.out.println("FT config : " + config.company + ", " + config.device + ", " + config.softwareVersion + ", " + config.bus);
//**********************************获取力传感器配置接口 end ******************************

//**********************************力传感器激活与零点矫正 begin ******************************
//        robot.FT_Activate(0);  //复位
//        robot.Sleep(2000);
//
//        robot.FT_Activate(1);  //激活
//        robot.Sleep(2000);
//
//        robot.FT_SetZero(0);//0去除零点
//        robot.Sleep(2000);
//
//        robot.FT_SetZero(1);//1零点矫正
//**********************************力传感器激活与零点矫正 end ******************************

//**********************************力传感器负载辨识 begin ******************************
//        double weight = 0.1;
//        int rtn = -1;
//
//        DescPose tcoord, desc_p1, desc_p2, desc_p3;
//        tcoord = new DescPose(0, 0, 0, 0, 0, 0);
//        desc_p1 = new DescPose(-14.404,-455.283,319.847,-172.935,25.141,-68.097);
//        desc_p2 = new DescPose(-107.999,-599.174,285.939,153.472,12.686,-71.284);
//        desc_p3 = new DescPose(6.586,-704.897,309.638,178.909,-27.759,-70.479);
//
//        DescPose coord = new DescPose(0, 0 ,0, 1, 0, 0);
//        robot.FT_SetRCS(0, coord);
//        robot.Sleep(1000);
//
//        tcoord.tran.z = 35.0;
//        robot.SetToolCoord(8, tcoord, 1, 0);
//        robot.Sleep(1000);
//        robot.FT_PdIdenRecord(10);
//        robot.Sleep(1000);
//        List<Number> rtnArray =  robot.FT_PdIdenCompute();
//        System.out.println("payload weight : " + rtnArray.get(1));
//
//
//        rtn = robot.MoveCart(desc_p1, 0, 0, 20.0f, 100.0f, 100.0f, -1.0f, -1);
//        robot.Sleep(1000);
//        robot.FT_PdCogIdenRecord(2, 1);
//        robot.MoveCart(desc_p2, 0, 0, 20.0f, 100.0f, 100.0f, -1.0f, -1);
//        robot.Sleep(1000);
//        robot.FT_PdCogIdenRecord(2, 2);
//        robot.MoveCart(desc_p3, 0, 0, 20.0f, 100.0f, 100.0f, -1.0f, -1);
//        robot.Sleep(1000);
//        robot.FT_PdCogIdenRecord(2, 3);
//        robot.Sleep(1000);
//        //DescTran cog = new DescTran(0, 0, 0);
//
//        DescTran rtnCog = new DescTran();
//        robot.FT_PdCogIdenCompute(rtnCog);
//        System.out.println("cog : " + rtnCog.x + ", " + rtnCog.y + ", " + rtnCog.z);
//**********************************力传感器负载辨识 end ******************************


//**********************************恒力控制 begin ******************************
//        byte flag = 1;
//        byte sensor_id = 8;
//        Object[] select = { 0,0,1,0,0,0 };
//        Object[] ft_pid = { 0.0005, 0.0, 0.0, 0.0, 0.0, 0.0 };
//        byte adj_sign = 0;
//        byte ILC_sign = 0;
//        float max_dis = 100.0f;
//        float max_ang = 0.0f;
//        ForceTorque ft = new ForceTorque(0, 0, -10, 0 ,0 ,0);
//
//
//        JointPos j1=new JointPos(-21.724,-136.814,-59.518,-68.853,89.245,-66.35);
//        DescPose desc_p1 = new DescPose(703.996,-391.695,240.708,-178.756,-4.709,-45.447);
//
//        JointPos j2=new JointPos(0.079,-130.285,-71.029,-72.115,88.945,-62.736);
//        DescPose desc_p2 = new DescPose(738.755,-102.812,226.704,177.488,2.566,-27.209);
//
//        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);
//
//        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
//
//
//        //关节空间运动
//        robot.MoveL(j1, desc_p1, 0, 0, 40.0f, 180.0f, 20.0f, -1.0f, epos, 0, 0, offset_pos, 0, 100);
//        int rtn = robot.FT_Control(flag, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, 0, 0, 0);
//        System.out.println("FT_Control start rtn " + rtn);
//
//        robot.MoveL(j2, desc_p2, 0, 0, 10.0f, 180.0f, 20.0f, -1.0f, epos, 0, 0, offset_pos, 0, 100);
//        flag = 0;
//        rtn = robot.FT_Control(flag, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang, 0, 0 ,0);
//        System.out.println("FT_Control end rtn " + rtn);
//**********************************恒力控制 end ******************************

//**********************************碰撞守护 begin ******************************
        byte flag = 1;
        byte sensor_id = 2;
        Object[] select = {0, 0, 1, 0, 0, 0};//只启用x轴碰撞守护
        Object[] max_threshold = {0.01, 0.01, 5.01, 0.01, 0.01, 0.01};
        Object[] min_threshold = {0.01, 0.01, 5.01, 0.01, 0.01, 0.01};

        ForceTorque ft = new ForceTorque(1.0, 0.0, 2.0, 0.0, 0.0, 0.0);
        DescPose desc_p1, desc_p2, desc_p3;

        desc_p1 = new DescPose(-280.5, -474.534, 320.677, 177.986, 1.498, -118.235);
        desc_p2 = new DescPose(-283.273, -468.668, 172.905, 177.986, 1.498, -118.235);

        int[] safetyMargin = {5, 5, 5, 5, 5, 5};
        robot.SetCollisionStrategy(5, 1000, 150, 150, safetyMargin);
        int rtn = robot.FT_Guard(flag, sensor_id, select, ft, max_threshold, min_threshold);
        System.out.println("FT_Guard start rtn " + rtn);
        robot.MoveCart(desc_p1, 0, 0, 20, 100.0f, 100.0f, -1.0f, -1);
        robot.MoveCart(desc_p2, 0, 0, 20, 100.0f, 100.0f, -1.0f, -1);
        flag = 0;
        rtn = robot.FT_Guard(flag, sensor_id, select, ft, max_threshold, min_threshold);
        System.out.println("FT_Guard end rtn " + rtn);
//**********************************碰撞守护 end ******************************

//**********************************柔顺控制 begin ******************************
//        byte flag = 1;
//        int sensor_id = 8;
//        Object[] select = { 1, 1, 1, 0, 0, 0 };
//        Object[] ft_pid = { 0.0005, 0.0, 0.0, 0.0, 0.0, 0.0 };
//        int adj_sign = 0;
//        int ILC_sign = 0;
//        double max_dis = 100.0;
//        double max_ang = 0.0;
//
//        ForceTorque ft = new ForceTorque(-10.0, -10.0, -10.0, 0.0, 0.0, 0.0);
//        DescPose desc_p1, desc_p2, offset_pos;
//        JointPos j1, j2;
//        j1=new JointPos(-21.724, -136.814, -59.518, -68.853, 89.245, -66.359);
//        j2=new JointPos(0.079, -130.285, -71.029, -72.115, 88.945, -62.736);
//
//        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);
//        desc_p1 = new DescPose(703.996, -391.695, 240.708, -178.756, -4.709, -45.447);
//        desc_p2 = new DescPose(738.755, -102.812, 226.704, 177.488, 2.566, -27.209);
//        offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
//
//
//        ft.fx = -10.0;
//        ft.fy = -10.0;
//        ft.fz = -10.0;
//        robot.FT_Control(flag, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang,0,0,0);
//        float p = 0.00005f;
//        float force = 10.0f;
//        int rtn = robot.FT_ComplianceStart(p, force);
//        System.out.println("FT_ComplianceStart rtn " + rtn);
//
//        robot.MoveL(j1, desc_p1, 0, 0, 20.0, 180.0, 100.0, -1.0, epos, 0, 1, offset_pos, 0, 100);
//
//        rtn = robot.FT_ComplianceStop();
//        System.out.println("FT_ComplianceStop rtn " + rtn);
//        flag = 0;
//        robot.FT_Control(flag, sensor_id, select, ft, ft_pid, adj_sign, ILC_sign, max_dis, max_ang,0,0,0);
//**********************************柔顺控制 end ******************************

//**********************************力传感器辅助拖动、状态获取 begin ******************************
//        List<Integer> rtnArray = robot.GetForceAndTorqueDragState();
//        System.out.println("the drag state is  " + rtnArray.get(1) + "  ForceAndJointImpedance state  " + rtnArray.get(2));
//
//        robot.Sleep(1000);
//        Object[] M = { 15.0, 15.0, 15.0, 0.5, 0.5, 0.1 };
//        Object[] B = { 150.0, 150.0, 150.0, 5.0, 5.0, 1.0 };
//        Object[] K = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
//        Object[] F = { 10.0, 10.0, 10.0, 1.0, 1.0, 1.0 };
//        robot.EndForceDragControl(1, 0, 0, M, B, K, F, 50, 100);
//
//        rtnArray = robot.GetForceAndTorqueDragState();
//        System.out.println("the drag state is" + rtnArray.get(1) + "  ForceAndJointImpedance state  " + rtnArray.get(2));
//
//        robot.Sleep(1000 * 10);
//        robot.EndForceDragControl(0, 0, 0, M, B, K, F, 50, 100);
//
//        rtnArray = robot.GetForceAndTorqueDragState();
//        System.out.println("the drag state is" + rtnArray.get(1) + "  ForceAndJointImpedance state  " + rtnArray.get(2));
//        robot.Sleep(1000);
//**********************************力传感器辅助拖动、状态获取 end ******************************

//**********************************六维力混合拖动 begin ******************************
//        robot.DragTeachSwitch(1);
//        Object[] lamdeDain = { 3.0, 2.0, 2.0, 2.0, 2.0, 3.0 };
//        Object[] KGain = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
//        Object[] BGain = { 150.0, 150.0, 150.0, 5.0, 5.0, 1.0 };
//        robot.ForceAndJointImpedanceStartStop(1, 0, lamdeDain, KGain, BGain, 1000.0, 180.0);
//
//        List<Integer> rtnArray = robot.GetForceAndTorqueDragState();
//        System.out.println("the drag state is  " + rtnArray.get(1) + "  ForceAndJointImpedance state  " + rtnArray.get(2));
//
//        robot.Sleep(1000 * 10);
//        robot.DragTeachSwitch(0);
//
//        robot.ForceAndJointImpedanceStartStop(0, 0, lamdeDain, KGain, BGain, 1000.0, 180.0);
//
//        rtnArray = robot.GetForceAndTorqueDragState();
//        System.out.println("the drag state is  " + rtnArray.get(1) + "  ForceAndJointImpedance state  " + rtnArray.get(2));
//**********************************六维力混合拖动 end ******************************


//**********************************力传感器负载设置与获取 begin ******************************
//        robot.SetForceSensorPayLoad(1.34);
//        DescTran cog = new DescTran(0.778, 2.554, 48.765);
//        robot.SetForceSensorPayLoadCog(cog);
//        double weight = 0;
//
//        List<Number> rtnArrays = robot.GetForceSensorPayLoad();
//        DescTran getCog = new DescTran(0.0, 0.0, 0.0);
//        robot.GetForceSensorPayLoadCog(getCog);
//        System.out.println("the FT load is " +  rtnArrays.get(1) + "  cog is  " + getCog.x + "  " + getCog.y + "   " + getCog.z);
//**********************************力传感器负载设置与获取 end ******************************

//**********************************力传感器负载自动校零 begin ******************************
//        robot.SetForceSensorPayLoad(0.0);
//        DescTran zeroCog = new DescTran();
//        robot.SetForceSensorPayLoadCog(zeroCog);
//
//        MassCenter center = new MassCenter();
//        robot.ForceSensorAutoComputeLoad(center);
//        System.out.println("the result is weight " + center.weight + " pos is  " + center.cog.x + "  " + center.cog.y + "  " + center.cog.z);
//**********************************力传感器负载自动校零 end ******************************

//
//        robot.SetLoadWeight(0.0f);
//        robot.Sleep(1000);
//        DescTran coord = new DescTran(0, 0, 0);
//
//        robot.SetLoadCoord(coord);
//        robot.Sleep(1000);
//        robot.FT_SetZero(0);//0去除零点  1零点矫正
//        robot.Sleep(1000);
//
//        ForceTorque ft = new ForceTorque(0, 0, 0, 0, 0, 0);
//        int rtn = robot.FT_GetForceTorqueOrigin(1, ref ft);
//         System.out.println("ft origin : {ft.fx}, {ft.fy}, { ft.fz}, { ft.tx}, { ft.ty}, { ft.tz}    rtn   {rtn}");
//        rtn = robot.FT_SetZero(1);//零点矫正
//        // System.out.println("set zero rtn {rtn}");
//
//        robot.Sleep(2000);
//        rtn = robot.FT_GetForceTorqueOrigin(1,ft);
//         System.out.println("ft rcs : {ft.fx}, {ft.fy}, {ft.fz}, {ft.tx}, {ft.ty}, {ft.tz}  rtn  {rtn}");

//        robot.FT_GetForceTorqueRCS(1, ft);
//         System.out.println("FT_GetForceTorqueRCS rcs : {ft.fx}, {ft.fy}, {ft.fz}, {ft.tx}, {ft.ty}, {ft.tz}");

//        double weight = 0.1;
//        int rtn = -1;
//        DescPose tcoord, desc_p1, desc_p2, desc_p3;
//        tcoord = new DescPose(0, 0, 0, 0, 0, 0);
//        desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
//        desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
//        desc_p3 = new DescPose(0, 0, 0, 0, 0, 0);
//
//        robot.FT_SetRCS(0);
//        robot.Sleep(1000);
//
//        tcoord.tran.z = 35.0;
//        robot.SetToolCoord(10, tcoord, 1, 0);
//        robot.Sleep(1000);
//        robot.FT_PdIdenRecord(10);
//        robot.Sleep(1000);
//        robot.FT_PdIdenCompute();
//        System.out.println("payload weight : {weight}");
//
//        desc_p1.tran.x = -47.805;
//        desc_p1.tran.y = -362.266;
//        desc_p1.tran.z = 317.754;
//        desc_p1.rpy.rx = -179.496;
//        desc_p1.rpy.ry = -0.255;
//        desc_p1.rpy.rz = 34.948;
//
//        desc_p2.tran.x = -77.805;
//        desc_p2.tran.y = -312.266;
//        desc_p2.tran.z = 317.754;
//        desc_p2.rpy.rx = -179.496;
//        desc_p2.rpy.ry = -0.255;
//        desc_p2.rpy.rz = 34.948;
//
//        desc_p3.tran.x = -167.805;
//        desc_p3.tran.y = -312.266;
//        desc_p3.tran.z = 387.754;
//        desc_p3.rpy.rx = -179.496;
//        desc_p3.rpy.ry = -0.255;
//        desc_p3.rpy.rz = 34.948;
//
//        rtn = robot.MoveCart(desc_p1, 0, 0, 100.0f, 100.0f, 100.0f, -1.0f, -1);
//        System.out.println("MoveCart rtn  " + rtn);
//        robot.Sleep(1000);
//        robot.FT_PdCogIdenRecord(10, 1);
//        robot.MoveCart(desc_p2, 0, 0, 100.0f, 100.0f, 100.0f, -1.0f, -1);
//        robot.Sleep(1000);
//        robot.FT_PdCogIdenRecord(10, 2);
//        robot.MoveCart(desc_p3, 0, 0, 100.0f, 100.0f, 100.0f, -1.0f, -1);
//        robot.Sleep(1000);
//        robot.FT_PdCogIdenRecord(10, 3);
//        robot.Sleep(1000);
//
//        DescTran cog = new DescTran(0, 0, 0);
//        robot.FT_PdCogIdenCompute(cog);
//        System.out.println("cog : " + cog.x + ", " + cog.y + ", " + cog.z);
    }

    static void DOReset(Robot robot) {
//*********************************DO/AO输出口复位状态 begin ***********************************
//        robot.SetDO(0,1,0,0);
//        robot.SetToolDO(0,1,0,0);
        robot.SetAO(0, 60, 0);
        robot.SetToolAO(0, 60, 0);
        robot.Sleep(2000);

//        robot.SetOutputResetCtlBoxDO(1);
//        robot.SetOutputResetAxleDO(1);//工具
//        robot.SetOutputResetCtlBoxAO(1);
//        robot.SetOutputResetAxleAO(1);//工具

        JointPos j1 = new JointPos(-81.684, -106.159, -74.447, -86.33, 94.725, 41.639);
        JointPos j2 = new JointPos(-102.804, -106.159, -74.449, -86.328, 94.715, 41.639);
        DescPose desc_p1, desc_p2;

        desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
        desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
        robot.GetForwardKin(j1, desc_p1);
        robot.GetForwardKin(j2, desc_p2);

        ExaxisPos epos = new ExaxisPos();
        DescPose offset_pos = new DescPose();
        robot.MoveL(j1, desc_p1, 0, 0, 30, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);

//        robot.MoveToolAOStop();
//        robot.MoveAOStop();

//*********************************DO/AO输出口复位状态 end ***********************************


//********************************扩展AO/DO begin ***********************************
//        UDPComParam param = new UDPComParam("192.168.58.88", 2021, 2, 100, 3, 100, 1, 100, 10);
//        robot.ExtDevSetUDPComParam(param);
//
//        UDPComParam getParam = new UDPComParam();
//        robot.ExtDevGetUDPComParam(getParam);
//        System.out.println(" " + getParam.ip + " ,   " + getParam.port + " ,   " + getParam.period + " ,  " + getParam.lossPkgTime + " ,   " + getParam.lossPkgNum + " ,   " + getParam.disconnectTime + " ,   " + getParam.reconnectEnable + " ,   " + getParam.reconnectPeriod + " ,   " + getParam.reconnectNum);
//
//        robot.ExtDevUnloadUDPDriver();
//        robot.Sleep(1000);
//        robot.ExtDevLoadUDPDriver();
//        robot.Sleep(2000);
//
//        robot.SetAuxDO(0, true, false, false);
//        robot.SetAuxDO(1, true, false, false);
//        robot.SetAuxDO(2, true, false, false);
//        robot.SetAuxDO(3, true, false, false);
//
        robot.SetAuxAO(0, 1234, false);
        robot.SetAuxAO(1, 2345, false);
        robot.SetAuxAO(2, 3456, false);
        robot.SetAuxAO(3, 1111, false);
//        robot.SetOutputResetExtDO(1);
//        robot.SetOutputResetExtAO(1);
//
//        robot.Sleep(3000);
//        DescPose  desc_p1, desc_p2;
//
//        desc_p1 = new DescPose(0, 0, 0, 0, 0, 0);
//        desc_p2 = new DescPose(0, 0, 0, 0, 0, 0);
//
//        JointPos j1 = new JointPos(-81.684,-106.159,-74.447,-86.33,94.725,41.639);
//        JointPos j2 = new JointPos(-102.804,-106.159,-74.449,-86.328,94.715,41.639);
//
//        robot.GetForwardKin(j1,desc_p1);
//        robot.GetForwardKin(j2,desc_p2);
//
//        ExaxisPos epos = new ExaxisPos();
//        DescPose offset_pos = new DescPose();
//        robot.MoveL(j1, desc_p1,0, 0, 30, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//        robot.MoveL(j2, desc_p2,0, 0, 30, 100, 100, -1, epos, 0, 0, offset_pos, 0, 100);
//********************************扩展AO/DO end ***********************************

//        robot.ProgramLoad("/fruser/test.lua");
//        robot.ProgramRun();
    }

    public int testSyncMoveJ(Robot robot)
    {
        //1.标定并应用机器人工具坐标系，您可以使用四点法或六点法进行工具坐标系的标定和应用，涉及工具坐标系标定的接口如下：
        //  int SetToolPoint(int point_num); //设置工具参考点-六点法
        //  int ComputeTool(ref DescPose tcp_pose); //计算工具坐标系
        //  int SetTcp4RefPoint(int point_num);  //设置工具参考点-四点法
        //  int ComputeTcp4(ref DescPose tcp_pose);  //计算工具坐标系-四点法
        //  int SetToolCoord(int id, DescPose coord, int type, int install); //设置应用工具坐标系
        //  int SetToolList(int id, DescPose coord, int type, int install);  //设置应用工具坐标系列表
        //2.设置UDP通信参数，并加载UDP通信
        UDPComParam param=new UDPComParam("192.168.58.88", 2021, 2, 100, 3, 100, 1, 100, 10,0);
        robot.ExtDevSetUDPComParam(param);
        robot.ExtDevLoadUDPDriver();
        //3.设置扩展轴参数，包括扩展轴类型、扩展轴驱动器参数、扩展轴DH参数
        robot.SetAxisDHParaConfig(4, 200, 200, 0, 0, 0, 0, 0, 0); //单轴变位机及DH参数
        robot.SetRobotPosToAxis(1); //扩展轴安装位置
        robot.ExtAxisParamConfig(1, 0, 1, 100, -100, 10, 10, 12, 131072, 0, 1, 0, 0); //伺服驱动器参数，本示例为单轴变位机，因此只需要设置一个驱动器参数，若您选择包含多个轴的扩展轴类型，需要每一个轴设置驱动器参数
        //4.设置所选的轴使能、回零
        robot.ExtAxisServoOn(1, 0);
        robot.ExtAxisSetHoming(1, 0, 20, 3);
        //5.进行扩展轴坐标系标定及应用
        DescPose pos = new DescPose(/* 输入您的标定点坐标 */ );
        robot.SetRefPointInExAxisEnd(pos);
        robot.PositionorSetRefPoint(1); /*您需要通过四个不同位置的点来标定扩展轴，因此需要调用此接口4次才能完成标定 */
        DescPose coord = new DescPose();
        robot.PositionorComputeECoordSys(coord); //计算扩展轴标定结果
        robot.ExtAxisActiveECoordSys(1, 1, coord, 1); //将标定结果应用到扩展轴坐标系
        //6.在扩展轴上标定工件坐标系，您需要用到以下接口
        //int SetWObjCoordPoint(int point_num);
        //int ComputeWObjCoord(int method, ref DescPose wobj_pose);
        //int SetWObjCoord(int id, DescPose coord);
        //int SetWObjList(int id, DescPose coord);
        //7.记录您的同步关节运动起始点
        DescPose startdescPose = new DescPose(/*输入您的坐标*/ );
        JointPos startjointPos = new JointPos(/*输入您的坐标*/ );
        ExaxisPos startexaxisPos = new ExaxisPos(/* 输入您的扩展轴起始点坐标 */ );
        //8.记录您的同步关节运动终点坐标
        DescPose enddescPose = new DescPose(/*输入您的坐标*/ );
        JointPos endjointPos = new JointPos(/*输入您的坐标*/ );
        ExaxisPos endexaxisPos =new ExaxisPos(/* 输入您的扩展轴终点坐标 */);
        //9.编写同步运动程序
        //运动到起始点，假设应用的工具坐标系、工件坐标系都是1
        robot.ExtAxisMove(startexaxisPos, 20);
        DescPose offdese = new DescPose( 0, 0, 0, 0, 0, 0 );
        robot.MoveJ(startjointPos, startdescPose, 1, 1, 100, 100, 100, startexaxisPos, 0, 0, offdese);
        //开始同步运动
        robot.ExtAxisSyncMoveJ(endjointPos, enddescPose, 1, 1, 100, 100, 100, endexaxisPos, -1, 0, offdese);
        robot.MoveJ(startjointPos, 1, 1, 100, 100, 100, startexaxisPos, 0, 0, offdese);
        //开始同步运动
        robot.ExtAxisSyncMoveJ(endjointPos, 1, 1, 100, 100, 100, endexaxisPos, -1, 0, offdese);
        robot.CloseRPC();
        return 0;
    }

    public int testSyncMoveL(Robot robot)
    {
        //1.标定并应用机器人工具坐标系，您可以使用四点法或六点法进行工具坐标系的标定和应用，涉及工具坐标系标定的接口如下：
        //  int SetToolPoint(int point_num); //设置工具参考点-六点法
        //  int ComputeTool(ref DescPose tcp_pose); //计算工具坐标系
        //  int SetTcp4RefPoint(int point_num);  //设置工具参考点-四点法
        //  int ComputeTcp4(ref DescPose tcp_pose);  //计算工具坐标系-四点法
        //  int SetToolCoord(int id, DescPose coord, int type, int install); //设置应用工具坐标系
        //  int SetToolList(int id, DescPose coord, int type, int install);  //设置应用工具坐标系列表
        //2.设置UDP通信参数，并加载UDP通信
        UDPComParam param=new UDPComParam("192.168.58.88", 2021, 2, 100, 3, 100, 1, 100, 10,0);
        robot.ExtDevSetUDPComParam(param);
        robot.ExtDevLoadUDPDriver();
        //3.设置扩展轴参数，包括扩展轴类型、扩展轴驱动器参数、扩展轴DH参数
        robot.SetAxisDHParaConfig(4, 200, 200, 0, 0, 0, 0, 0, 0); //单轴变位机及DH参数
        robot.SetRobotPosToAxis(1); //扩展轴安装位置
        robot.ExtAxisParamConfig(1, 0, 1, 100, -100, 10, 10, 12, 131072, 0, 1, 0, 0); //伺服驱动器参数，本示例为单轴变位机，因此只需要设置一个驱动器参数，若您选择包含多个轴的扩展轴类型，需要每一个轴设置驱动器参数
        //4.设置所选的轴使能、回零
        robot.ExtAxisServoOn(1, 0);
        robot.ExtAxisSetHoming(1, 0, 20, 3);
        //5.进行扩展轴坐标系标定及应用
        DescPose pos = new DescPose(/* 输入您的标定点坐标 */ );
        robot.SetRefPointInExAxisEnd(pos);
        robot.PositionorSetRefPoint(1); /*您需要通过四个不同位置的点来标定扩展轴，因此需要调用此接口4次才能完成标定 */
        DescPose coord = new DescPose();
        robot.PositionorComputeECoordSys(coord); //计算扩展轴标定结果
        robot.ExtAxisActiveECoordSys(1, 1, coord, 1); //将标定结果应用到扩展轴坐标系
        //6.在扩展轴上标定工件坐标系，您需要用到以下接口
        //int SetWObjCoordPoint(int point_num);
        //int ComputeWObjCoord(int method, ref DescPose wobj_pose);
        //int SetWObjCoord(int id, DescPose coord);
        //int SetWObjList(int id, DescPose coord);
        //7.记录您的同步关节运动起始点
        DescPose startdescPose = new DescPose(/*输入您的坐标*/ );
        JointPos startjointPos = new JointPos(/*输入您的坐标*/ );
        ExaxisPos startexaxisPos = new ExaxisPos(/* 输入您的扩展轴起始点坐标 */ );
        //8.记录您的同步关节运动终点坐标
        DescPose enddescPose = new DescPose(/*输入您的坐标*/ );
        JointPos endjointPos = new JointPos(/*输入您的坐标*/ );
        ExaxisPos endexaxisPos =new ExaxisPos(/* 输入您的扩展轴终点坐标 */);
        //9.编写同步运动程序
        //运动到起始点，假设应用的工具坐标系、工件坐标系都是1
        robot.ExtAxisMove(startexaxisPos, 20);
        DescPose offdese = new DescPose( 0, 0, 0, 0, 0, 0 );
        robot.MoveJ(startjointPos, startdescPose, 1, 1, 100, 100, 100, startexaxisPos, 0, 0, offdese);
        //开始同步运动
        robot.ExtAxisSyncMoveL(endjointPos, enddescPose, 1, 1, 100, 100, 100, 0, endexaxisPos, 0, offdese);
        robot.MoveJ(startjointPos, 1, 1, 100, 100, 100, startexaxisPos, 0, 0, offdese);
        //开始同步运动
        robot.ExtAxisSyncMoveL(enddescPose, 1, 1, 100, 100, 100, 0, endexaxisPos, 0, offdese,-1);
        robot.CloseRPC();
        return 0;
    }

    public int testSyncMoveC(Robot robot)
    {
        //1.标定并应用机器人工具坐标系，您可以使用四点法或六点法进行工具坐标系的标定和应用，涉及工具坐标系标定的接口如下：
        //  int SetToolPoint(int point_num); //设置工具参考点-六点法
        //  int ComputeTool(ref DescPose tcp_pose); //计算工具坐标系
        //  int SetTcp4RefPoint(int point_num);  //设置工具参考点-四点法
        //  int ComputeTcp4(ref DescPose tcp_pose);  //计算工具坐标系-四点法
        //  int SetToolCoord(int id, DescPose coord, int type, int install); //设置应用工具坐标系
        //  int SetToolList(int id, DescPose coord, int type, int install);  //设置应用工具坐标系列表
        //2.设置UDP通信参数，并加载UDP通信
        UDPComParam param=new UDPComParam("192.168.58.88", 2021, 2, 100, 3, 100, 1, 100, 10,0);
        robot.ExtDevSetUDPComParam(param);
        robot.ExtDevLoadUDPDriver();
        //3.设置扩展轴参数，包括扩展轴类型、扩展轴驱动器参数、扩展轴DH参数
        robot.SetAxisDHParaConfig(4, 200, 200, 0, 0, 0, 0, 0, 0); //单轴变位机及DH参数
        robot.SetRobotPosToAxis(1); //扩展轴安装位置
        robot.ExtAxisParamConfig(1, 0, 1, 100, -100, 10, 10, 12, 131072, 0, 1, 0, 0); //伺服驱动器参数，本示例为单轴变位机，因此只需要设置一个驱动器参数，若您选择包含多个轴的扩展轴类型，需要每一个轴设置驱动器参数
        //4.设置所选的轴使能、回零
        robot.ExtAxisServoOn(1, 0);
        robot.ExtAxisSetHoming(1, 0, 20, 3);
        //5.进行扩展轴坐标系标定及应用
        DescPose pos = new DescPose(/* 输入您的标定点坐标 */ );
        robot.SetRefPointInExAxisEnd(pos);
        robot.PositionorSetRefPoint(1); /*您需要通过四个不同位置的点来标定扩展轴，因此需要调用此接口4次才能完成标定 */
        DescPose coord = new DescPose();
        robot.PositionorComputeECoordSys(coord); //计算扩展轴标定结果
        robot.ExtAxisActiveECoordSys(1, 1, coord, 1); //将标定结果应用到扩展轴坐标系
        //6.在扩展轴上标定工件坐标系，您需要用到以下接口
        //int SetWObjCoordPoint(int point_num);
        //int ComputeWObjCoord(int method, ref DescPose wobj_pose);
        //int SetWObjCoord(int id, DescPose coord);
        //int SetWObjList(int id, DescPose coord);
        //7.记录您的同步圆弧运动起始点
        DescPose startdescPose = new DescPose(/*输入您的坐标*/ );
        JointPos startjointPos = new JointPos(/*输入您的坐标*/ );
        ExaxisPos startexaxisPos = new ExaxisPos(/* 输入您的扩展轴起始点坐标 */ );
        //8.记录您的同步圆弧运动终点坐标
        DescPose enddescPose = new DescPose(/*输入您的坐标*/ );
        JointPos endjointPos = new JointPos(/*输入您的坐标*/ );
        ExaxisPos endexaxisPos = new ExaxisPos(/* 输入您的扩展轴终点坐标 */ );
        //9.记录您的同步圆弧运动中间点坐标
        DescPose middescPose = new DescPose(/*输入您的坐标*/ );
        JointPos midjointPos =new JointPos(/*输入您的坐标*/ );
        ExaxisPos midexaxisPos = new ExaxisPos(/* 输入机器人圆弧中间点时的扩展轴坐标 */ );
        //10.编写同步运动程序
        //运动到起始点，假设应用的工具坐标系、工件坐标系都是1
        robot.ExtAxisMove(startexaxisPos, 20);
        DescPose offdese = new DescPose( 0, 0, 0, 0, 0, 0 );
        robot.MoveJ(startjointPos, startdescPose, 1, 1, 100, 100, 100, startexaxisPos, 0, 0, offdese);
        //开始同步运动
        robot.ExtAxisSyncMoveC(midjointPos, middescPose, 1, 1, 100, 100, midexaxisPos, 0, offdese, endjointPos, enddescPose, 1, 1, 100, 100, endexaxisPos, 0, offdese, 100, 0);
        robot.MoveJ(startjointPos, 1, 1, 100, 100, 100, startexaxisPos, 0, 0, offdese);
        //开始同步运动
        robot.ExtAxisSyncMoveC(middescPose, 1, 1, 100, 100, midexaxisPos, 0, offdese, enddescPose, 1, 1, 100, 100, endexaxisPos, 0, offdese, 100, 0,-1);
        robot.CloseRPC();
        return 0;
    }

    private static void UDPAxisSyncMove(Robot robot) {
//        robot.Mode(0);
//        int tool = 1;
//        int user = 0;
//        double vel = 20.0;
//        double acc = 100.0;
//        double ovl = 100.0;
//        float blendT = -1;
//        float blendR = -1;
//        int flag = 0;
//        int type = 1;
//
//        ExaxisPos exaxisPos = new ExaxisPos( 0, 0, 0, 0 );
//
//        DescPose d0 = new DescPose(311.189, -309.688, 401.836, -174.375, -1.409, -82.354);
//        JointPos j0 =new JointPos(118.217, -99.669, 79.928, -73.559, -85.229, -69.359);
//
//        JointPos joint_pos0 =new JointPos(111.549,-99.821,108.707,-99.308,-85.305,-41.515);
//        DescPose desc_pos0 = new DescPose(273.499,-345.746,201.573,-176.566 ,3.235,-116.819);
//        ExaxisPos e_pos0=new ExaxisPos(0,0,0,0);
//
//        JointPos joint_pos1 = new JointPos(112.395,-65.118,67.815,-61.449,-88.669,-41.517);
//        DescPose desc_pos1 = new DescPose(291.393,-420.519,201.089,156.297,21.019,-120.919);
//        ExaxisPos e_pos1 = new ExaxisPos(-30, -30, 0, 0);
//
//
//        JointPos j2 = new JointPos(111.549,-98.369,108.036,-103.789,-95.203,-69.358);
//        DescPose desc_pos2 = new DescPose(234.544,-392.777,205.566,176.584,-5.694,-89.109);
//        ExaxisPos epos2 = new ExaxisPos(0.000,0.000,0.000,0.000);
//
//        JointPos j3 = new JointPos(113.908,-61.947,63.829,-64.478,-85.406,-69.256);
//        DescPose desc_pos3 = new DescPose(336.049,-444.969,192.799,173.776 ,27.104,-89.455);
//        ExaxisPos epos3 = new ExaxisPos(-30.000,-30.000, 0.000, 0.000);
//
//        //圆弧的起点
//        JointPos j4 = new JointPos(137.204,-98.475,106.624,-97.769,-90.634,-69.24);
//        DescPose desc_pos4 = new DescPose(381.269,-218.688,205.735,179.274,0.128,-63.556);
//
//        JointPos j5 = new JointPos(115.069,-92.709,97.285,-82.809,-90.455,-77.146);
//        DescPose desc_pos5 = new DescPose(264.049,-329.478 ,220.747,176.906,11.359,-78.044);
//        ExaxisPos  epos5 = new ExaxisPos(-15, 0, 0, 0);
//
//
//        JointPos j6 = new JointPos(102.409,-63.115,70.559,-70.156,-86.529,-77.148);
//        DescPose desc_pos6 = new DescPose(232.407,-494.228 ,158.115,176.803,27.319,-92.056);
//        ExaxisPos  epos6 = new ExaxisPos(-30, 0, 0, 0);
//
//        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
//
//        //同步关节运动
//        robot.MoveJ(j0, d0, 1, 0, vel, acc, ovl, exaxisPos, -1, 0, offset_pos);
//        robot.ExtAxisMove(exaxisPos,40);
//        robot.ExtAxisSyncMoveJ(joint_pos0, desc_pos0, 1, 0,20,100,100,e_pos0,-1,0,offset_pos);
//        robot.ExtAxisSyncMoveJ(joint_pos1, desc_pos1, 1, 0,20,100,100,e_pos1,-1,0,offset_pos);
//
//
//        //同步直线运动
//        robot.MoveJ(j0, d0, 1, 0, vel, acc, ovl, exaxisPos, -1, 0, offset_pos);
//        robot.ExtAxisMove(exaxisPos,40);
//        robot.ExtAxisSyncMoveL(j2, desc_pos2, tool, user, 40, 100, 100, -1, epos2, 0, offset_pos);
//        robot.ExtAxisSyncMoveL(j3, desc_pos3, tool, user, 40, 100, 100, -1, epos3, 0, offset_pos);
////
////        //同步圆弧运动
//        robot.MoveJ(j0, d0, 1, 0, vel, acc, ovl, exaxisPos, -1, 0, offset_pos);
//        robot.ExtAxisMove(exaxisPos,20);
//        robot.MoveJ(j4, desc_pos4, 1, 0, vel, acc, ovl, exaxisPos, -1, 0, offset_pos);
//
//        robot.ExtAxisSyncMoveC(j5, desc_pos5, tool, user, 40, 100, epos5, 0, offset_pos, j6, desc_pos6, tool, user, 40, 100, epos6, 0, offset_pos, 100, 0);
//
//        robot.Sleep(3000);
//        robot.MoveJ(j0, d0, 1, 0, vel, acc, ovl, exaxisPos, -1, 0, offset_pos);
//        robot.ExtAxisMove(exaxisPos,40);
//        robot.Mode(1);
    }

    private static void UDPAxis(Robot robot) {
//***********************************************UPD扩展轴参数配置与获取 begin**************************************
        UDPComParam param = new UDPComParam("192.168.58.88", 2021, 2, 100, 3, 100, 1, 100, 10,0);
        robot.ExtDevSetUDPComParam(param);//udp扩展轴通讯

        UDPComParam getParam = new UDPComParam();
        robot.ExtDevGetUDPComParam(getParam);
        System.out.println(" " + getParam.ip + " ,   " + getParam.port + " ,   " + getParam.period + " ,  " + getParam.lossPkgTime + " ,   " + getParam.lossPkgNum + " ,   " + getParam.disconnectTime + " ,   " + getParam.reconnectEnable + " ,   " + getParam.reconnectPeriod + " ,   " + getParam.reconnectNum);
//***********************************************UPD扩展轴参数配置与获取 end **************************************

//***********************************************UPD加载与卸载 begin **************************************
        robot.ExtDevUnloadUDPDriver();//卸载UDP通信
        robot.Sleep(1000);
        robot.ExtDevLoadUDPDriver();//加载UDP通信
        robot.Sleep(1000);
//***********************************************UPD加载与卸载 end **************************************

//***********************************************UPD扩展轴按照位置、DH参数、轴参数设置 begin **************************************
//        robot.ExtAxisServoOn(1, 1);//扩展轴1使能
//        robot.ExtAxisServoOn(2, 1);//扩展轴2使能
//        robot.Sleep(1000);
//        robot.ExtAxisSetHoming(1, 0, 10, 3);//1,2扩展轴都回零
//        robot.ExtAxisSetHoming(2, 0, 10, 3);
//        robot.Sleep(1000);
//
//        int rtn = 0;
//        rtn = robot.SetAxisDHParaConfig(1, 128.5, 206.4, 0, 0, 0, 0, 0, 0);
//        System.out.println("SetAxisDHParaConfig rtn is " + rtn);
//        rtn = robot.SetRobotPosToAxis(1);
//        System.out.println("SetRobotPosToAxis rtn is " + rtn);
//        rtn = robot.ExtAxisParamConfig(1,1, 0, 50, -50, 1000, 1000, 1.905, 262144, 200, 0, 0, 0);
//        System.out.println("ExtAxisParamConfig rtn is " + rtn);
//        rtn = robot.ExtAxisParamConfig(2,2, 0, 1000, -1000, 1000, 1000, 4.444, 262144, 200, 0, 0, 0);
//        System.out.println("ExtAxisParamConfig rtn is " + rtn);
//***********************************************UPD扩展轴按照位置、DH参数、轴参数设置 end *************************************

//***********************************************UPD扩展轴坐标系标定 begin *****a*********************************
//        int rtn = 0;
//        rtn = robot.SetAxisDHParaConfig(1, 128.5, 206.4, 0, 0, 0, 0, 0, 0);
//        System.out.println("SetAxisDHParaConfig rtn is " + rtn);
//        rtn = robot.SetRobotPosToAxis(1);
//        System.out.println("SetRobotPosToAxis rtn is " + rtn);
//        rtn = robot.ExtAxisParamConfig(1,1, 0, 1000, -1000, 100, 100, 1.905, 262144, 200, 0, 0, 0);
//        System.out.println("ExtAxisParamConfig rtn is " + rtn);
//        rtn = robot.ExtAxisParamConfig(2,2, 0, 1000, -1000, 100, 100, 4.444, 262144, 200, 0, 0, 0);
//        System.out.println("ExtAxisParamConfig rtn is " + rtn);
//
//        int tool = 1;
//        int user = 0;
//        double vel = 20;
//        double acc = 100;
//        double ovl = 100;
//        int offset_flag = 0;
//
//        ExaxisPos exaxisPos = new ExaxisPos( 0, 0, 0, 0 );
//        DescPose offdese = new DescPose( 0, 0, 0, 0, 0, 0 );
//
//
//        DescPose descPose0 = new DescPose(311.189,-309.688,401.836,-174.375,-1.409,-82.354);
//        JointPos jointPos0 = new JointPos(118.217,-99.669,79.928,-73.559,-85.229,-69.359);
//        robot.MoveJ(jointPos0, descPose0, 1, 0, vel, acc, ovl, exaxisPos, -1, offset_flag, offdese);
//
//        robot.ExtAxisMove(exaxisPos, 20);//UDP扩展轴运动,速度100
//
//        DescPose descPose1 = new DescPose(359.526,-516.038,194.469,-175.689,2.781,-87.609);
//        JointPos jointPos1 = new JointPos(113.015,-72.49,80.079,-96.505,-84.986,-69.309);
//
//        ExaxisPos pos1 = new ExaxisPos(0.000,0.000,0.000,0.000);
//        robot.ExtAxisMove(pos1, 20);//UDP扩展轴运动,速度100
//        robot.MoveJ(jointPos1, descPose1, 1, 0, vel, acc, ovl, exaxisPos, -1, offset_flag, offdese);
//        robot.PositionorSetRefPoint(1);
//
//        robot.MoveJ(jointPos0, descPose0, 1, 0, vel, acc, ovl, exaxisPos, -1, offset_flag, offdese);
//
//        ExaxisPos pos2 = new ExaxisPos(-10.000, -10.000, 0.000, 0.000);
//        robot.ExtAxisMove(pos2, 20);
//        DescPose descPose2 = new DescPose( 333.347,-541.958,164.894,-176.47,4.284,-90.725);
//        JointPos jointPos2 = new JointPos(109.989,-69.637,80.146,-97.755,-85.188,-69.269);
//        robot.MoveJ(jointPos2, descPose2, 1, 0, vel, acc, ovl, exaxisPos, -1, offset_flag, offdese);
//        robot.PositionorSetRefPoint(2);
//
//        robot.MoveJ(jointPos0, descPose0, 1, 0, vel, acc, ovl, exaxisPos, -1, offset_flag, offdese);
//
//        ExaxisPos pos3 = new ExaxisPos(-20.000, -20.000, 0.000, 0.00);
//        robot.ExtAxisMove(pos3, 20);
//        DescPose descPose3 = new DescPose(306.488,-559.238,135.948,-174.925,0.235,-93.517);
//        JointPos jointPos3 = new JointPos(107.137,-71.377,87.975,-108.167,-85.169,-69.269);
//        robot.MoveJ(jointPos3, descPose3, 1, 0, vel, acc, ovl, exaxisPos, -1, offset_flag, offdese);
//        robot.PositionorSetRefPoint(3);
//
//        robot.MoveJ(jointPos0, descPose0, 1, 0, vel, acc, ovl, exaxisPos, -1, offset_flag, offdese);
//
//        ExaxisPos pos4 = new ExaxisPos(-30.000, -30.000, 0.000, 0.000);
//        robot.ExtAxisMove(pos4, 20);
//        DescPose descPose4 = new DescPose( 285.528,-569.999,108.568,-174.367,-1.239,-95.643);
//        JointPos jointPos4 = new JointPos(105.016,-71.137,92.326,-114.339,-85.169,-69.269);
//        robot.MoveJ(jointPos4, descPose4, 1, 0, vel, acc, ovl, exaxisPos, -1, offset_flag, offdese);
//        robot.PositionorSetRefPoint(4);
//
//        DescPose axisCoord = new DescPose();
        //robot.PositionorComputeECoordSys(axisCoord);
        //robot.ExtAxisActiveECoordSys(3,1,axisCoord,1);
        //System.out.println("axis coord is " + axisCoord);

//        robot.MoveJ(jointPos0, descPose0, 1, 0, vel, acc, ovl, exaxisPos, -1, offset_flag, offdese);
//        robot.ExtAxisMove(pos1, 20);
//***********************************************UPD扩展轴坐标系标定 end **************************************

//***********************************************UPD扩展轴异步运动 begin **************************************
//        int tool = 1;
//        int user = 0;
//        double vel = 20;
//        double acc = 100;
//        double ovl = 100;
//        int offset_flag = 0;
//        ExaxisPos exaxisPos = new ExaxisPos( 0, 0, 0, 0 );
//        DescPose offdese = new DescPose( 0, 0, 0, 0, 0, 0 );
//
//
//        DescPose descPose0 = new DescPose(311.189,-309.688,401.836,-174.375,-1.409,-82.354);
//        JointPos jointPos0 = new JointPos(118.217,-99.669,79.928,-73.559,-85.229,-69.359);
//        //robot.MoveJ(jointPos0, descPose0, 1, 0, vel, acc, ovl, exaxisPos, -1, offset_flag, offdese);
//
//        ExaxisPos pos = new ExaxisPos( 0, 0, 0, 0 );
//        robot.ExtAxisMove(pos,40);
//
//        pos.axis1 = 20;
//        pos.axis2 = 100;
//        robot.ExtAxisMove(pos, 40);
//
//        pos.axis1 = -20;
//        pos.axis2 = -100;
//        robot.ExtAxisMove(pos, 40);
        //        robot.ExtAxisMove(pos,40);
//***********************************************UPD扩展轴异步运动 end **************************************

//***********************************************UPD扩展轴DOI begin **************************************
//        for(int i = 0; i < 128; i++)
//        {
//            robot.SetAuxDO(i, true, false, true);
//            robot.Sleep(50);
//        }
//
//        for(int i = 0; i < 128; i++)
//        {
//            robot.SetAuxDO(i, false, false, true);
//            robot.Sleep(50);
//        }
//
//        for (int i = 0; i < 4; i ++)
//        {
//            robot.SetAuxAO(i, i * 1000, false);
//            robot.Sleep(1000);
//        }
//
//        for (int i = 0; i < 4; i ++)
//        {
//            robot.SetAuxAO(i, 0, false);
//            robot.Sleep(1000);
//        }
//
//        while (true)
//        {
//            ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
//            System.out.println("aux DI1 DI0  is " + Integer.toBinaryString(pkg.extDIState[0]));
//        }
//***********************************************UPD扩展轴DOI end **************************************


//        robot.ExtAxisActiveECoordSys(3, 3, axisCoord, 1);

//
//        DescPose refPointPos = new DescPose(122.0, 312.0, 0, 0, 0, 0);
//        robot.SetRefPointInExAxisEnd(refPointPos);
//
//        robot.PositionorSetRefPoint(1);
//        //robot.PositionorSetRefPoint(2);
//        //robot.PositionorSetRefPoint(3);
//        //robot.PositionorSetRefPoint(4);
//
//        //DescPose coord = new DescPose();
//        //robot.PositionorComputeECoordSys(ref coord);
//
//
//        robot.ExtAxisServoOn(1, 1);
//        robot.ExtAxisSetHoming(1, 0, 10, 3);
//        robot.ExtAxisStartJog(1, 1, 100, 100, 20);
//        robot.Sleep(1000 * 2);
//        robot.ExtAxisStopJog(1);
//        robot.ExtAxisServoOn(1, 0);
//
//        ExaxisPos pos = new ExaxisPos(10, 0, 0, 0);
//        robot.ExtAxisMove(pos, 10);
    }

    private static void TestUDPWireSearch(Robot robot)
    {
        UDPComParam param = new UDPComParam("192.168.58.88", 2021, 2, 100, 3, 100, 1, 100, 10,0);
        robot.ExtDevSetUDPComParam(param);//udp扩展轴通讯

        robot.SetWireSearchExtDIONum(0, 0);

        int rtn0, rtn1, rtn2 = 0;
        ExaxisPos exaxisPos = new ExaxisPos(0.0, 0.0, 0.0, 0.0);
        DescPose offdese = new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        DescPose descStart = new DescPose(-158.767, -510.596, 271.709, -179.427, -0.745, -137.349);
        JointPos jointStart = new JointPos(61.667, -79.848, 108.639, -119.682, -89.700, -70.985);

        DescPose descEnd = new DescPose(0.332, -516.427, 270.688, 178.165, 0.017, -119.989);
        JointPos jointEnd = new JointPos(79.021, -81.839, 110.752, -118.298, -91.729, -70.981);

        robot.MoveL(jointStart, descStart, 1, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);
        robot.MoveL(jointEnd, descEnd, 1, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);

        DescPose descREF0A = new DescPose(-66.106, -560.746, 270.381, 176.479, -0.126, -126.745);
        JointPos jointREF0A = new JointPos(73.531, -75.588, 102.941, -116.250, -93.347, -69.689);

        DescPose descREF0B = new DescPose(-66.109, -528.440, 270.407, 176.479, -0.129, -126.744);
        JointPos jointREF0B = new JointPos(72.534, -79.625, 108.046, -117.379, -93.366, -70.687);

        DescPose descREF1A = new DescPose(72.975, -473.242, 270.399, 176.479, -0.129, -126.744);
        JointPos jointREF1A = new JointPos(87.169, -86.509, 115.710, -117.341, -92.993, -56.034);

        DescPose descREF1B = new DescPose(31.355, -473.238, 270.405, 176.480, -0.130, -126.745);
        JointPos jointREF1B = new JointPos(82.117, -87.146, 116.470, -117.737, -93.145, -61.090);

        rtn0 = robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
        robot.MoveL(jointREF0A, descREF0A, 1, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);  //起点
        robot.MoveL(jointREF0B, descREF0B, 1, 0, 10, 100, 100, -1, exaxisPos, 1, 0, offdese, 0, 100);  //方向点
        rtn1 = robot.WireSearchWait("REF0");
        rtn2 = robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);

        rtn0 = robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
        robot.MoveL(jointREF1A, descREF1A, 1, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);  //起点
        robot.MoveL(jointREF1B, descREF1B, 1, 0, 10, 100, 100, -1, exaxisPos, 1, 0, offdese, 0, 100);  //方向点
        rtn1 = robot.WireSearchWait("REF1");
        rtn2 = robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);

        rtn0 = robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
        robot.MoveL(jointREF0A, descREF0A, 1, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);  //起点
        robot.MoveL(jointREF0B, descREF0B, 1, 0, 10, 100, 100, -1, exaxisPos, 1, 0, offdese, 0, 100);  //方向点
        rtn1 = robot.WireSearchWait("RES0");
        rtn2 = robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);

        rtn0 = robot.WireSearchStart(0, 10, 100, 0, 10, 100, 0);
        robot.MoveL(jointREF1A, descREF1A, 1, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);  //起点
        robot.MoveL(jointREF1B, descREF1B, 1, 0, 10, 100, 100, -1, exaxisPos, 1, 0, offdese, 0, 100);  //方向点
        rtn1 = robot.WireSearchWait("RES1");
        rtn2 = robot.WireSearchEnd(0, 10, 100, 0, 10, 100, 0);

        String[] varNameRef = {"REF0", "REF1", "#", "#", "#", "#"};
        String[] varNameRes = {"RES0", "RES1", "#", "#", "#", "#"};
        int offectFlag = 0;
        //DescPose offectPos = new DescPose(0, 0, 0, 0, 0, 0);
        DescOffset offset = new DescOffset();
        rtn0 = robot.GetWireSearchOffset(0, 0, varNameRef, varNameRes, offset);
        robot.PointsOffsetEnable(0, offset.offset);
        robot.MoveL(jointStart, descStart, 1, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 0, 100);
        robot.MoveL(jointEnd, descEnd, 1, 0, 100, 100, 100, -1, exaxisPos, 1, 0, offdese, 0, 100);
        robot.PointsOffsetDisable();

    }

    private static void TestTractorMove(Robot robot) {
        UDPComParam param = new UDPComParam("192.168.58.88", 2021, 2, 100, 3, 100, 1, 100, 10,0);
        robot.ExtDevSetUDPComParam(param);//udp扩展轴通讯
        robot.ExtAxisParamConfig(1, 0, 0, 50000, -50000, 1000, 1000, 6.280, 16384, 200, 0, 0, 0);
        robot.ExtAxisParamConfig(2, 0, 0, 50000, -50000, 1000, 1000, 6.280, 16384, 200, 0, 0, 0);
        robot.SetAxisDHParaConfig(5, 0, 0, 0, 0, 0, 0, 0, 0);

        robot.TractorEnable(false);
        robot.Sleep(2000);
        robot.TractorEnable(true);
        robot.Sleep(2000);
        robot.TractorHoming();

        robot.Sleep(2000);
        robot.TractorMoveL(100, 20);
        robot.Sleep(5000);
        robot.TractorMoveL(-100, 20);
        robot.Sleep(5000);
        robot.TractorMoveC(300, 90, 20);
        robot.Sleep(2000);
        robot.TractorStop();//小车停止
        robot.TractorMoveC(300, -90, 20);
    }


    private static void TestWeldmechineMode(Robot robot) {
        UDPComParam param = new UDPComParam("192.168.58.88", 2021, 2, 50, 5, 50, 1, 50, 10,0);
        robot.ExtDevSetUDPComParam(param);//udp扩展轴通讯
        robot.ExtDevLoadUDPDriver();

        robot.SetWeldMachineCtrlModeExtDoNum(17);//DO
        for (int i = 0; i < 5; i++) {
            robot.SetWeldMachineCtrlMode(0);//设置焊机控制模式
            robot.Sleep(500);
            robot.SetWeldMachineCtrlMode(1);
            robot.Sleep(500);
        }

        robot.SetWeldMachineCtrlModeExtDoNum(18);
        for (int i = 0; i < 5; i++) {
            robot.SetWeldMachineCtrlMode(0);
            robot.Sleep(500);
            robot.SetWeldMachineCtrlMode(1);
            robot.Sleep(500);
        }

        robot.SetWeldMachineCtrlModeExtDoNum(19);
        for (int i = 0; i < 5; i++) {
            robot.SetWeldMachineCtrlMode(0);//设置焊机控制模式
            robot.Sleep(500);
            robot.SetWeldMachineCtrlMode(1);
            robot.Sleep(500);
        }
    }

    private static void Program(Robot robot) {
// *************************************LUA文件下载 begin ********************************
        robot.LuaDownLoad("1010TestLUA.lua", "D://LUA/");
//*************************************LUA文件下载 end ********************************

//*************************************获取当前所有LUA begin ********************************
//        List<String> names = new ArrayList<String>();
//        robot.GetLuaList(names);
//        System.out.println("lua Num " + names.size() + "   " + names.get(0));
//*************************************获取当前所有LUA end ********************************

//*************************************LUA文件删除 end ********************************
//        robot.LuaDelete("1010TestLUA.lua");
//*************************************LUA文件删除 end ********************************

// *************************************LUA文件上传 begin ********************************
//        String errStr = "";
//        robot.LuaUpload("D://LUA/1010TestLUA.lua", errStr);
//        System.out.println("robot upload 1010TestLUA lua result " + errStr);
// *************************************LUA文件上传 end ********************************


// *************************************开机自动加载默认程序 begin ********************************
//        robot.LoadDefaultProgConfig(1,"/fruser/1010Test.lua");
// *************************************开机自动加载默认程序 end ********************************

// *************************************加载、获取LUA名称 begin ********************************
//        robot.Mode(0);
//        robot.ProgramLoad("/fruser/1010Test.lua");
//        String[] loadedNameStr = new String[1];
//        robot.GetLoadedProgram(loadedNameStr);
//        System.out.println("Loaded lua Name is " + loadedNameStr[0]);
//        robot.ProgramRun();
//*************************************加载、获取LUA名称 end ********************************

//*************************************获取运行状态及行号 begin ********************************
//        robot.Mode(0);
//        robot.ProgramLoad("/fruser/1010Test.lua");
//        String[] loadedNameStr = new String[1];
//        robot.GetLoadedProgram(loadedNameStr);
//        System.out.println("Loaded lua Name is " + loadedNameStr[0]);
//        robot.ProgramRun();
//
//        while(true)
//        {
//            List<Integer> results =  robot.GetCurrentLine();
//            ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
//            System.out.println("current line is " + results.get(1) + " Robot Runing State: " + pkg.robot_state);
//            robot.Sleep(500);
//        }
//*************************************获取运行状态及行号 end ********************************

//*************************************LUA暂停、停止、运行 begin ********************************
//        robot.Mode(0);
//        robot.ProgramLoad("/fruser/1010Test.lua");
//        String[] loadedNameStr = new String[1];
//        robot.GetLoadedProgram(loadedNameStr);
//        System.out.println("Loaded lua Name is " + loadedNameStr[0]);
//        robot.ProgramRun();//开始
//
//        robot.Sleep(1000);
//        for(int i = 0; i < 10;  i++)
//        {
//            robot.PauseMotion();//暂停运动
//            robot.Sleep(1000);
//            robot.ResumeMotion();//恢复运动
//            robot.Sleep(1000);
//        }
//
//        robot.StopMotion();//停止
//*************************************LUA暂停、停止、运行 begin ********************************

    }

    static int SegmentWeld(Robot robot) {
        DescPose startdescPose = new DescPose(185.859, -520.154, 193.129, -177.129, 1.339, -137.789);
        JointPos startjointPos = new JointPos(-60.989, -94.515, -89.479, -83.514, 91.957, -13.124);

        DescPose enddescPose = new DescPose(-243.7033, -543.868, 143.199, -177.954, 1.528, 177.758);
        JointPos endjointPos = new JointPos(-105.479, -101.919, -87.979, -78.455, 91.955, -13.183);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        robot.SegmentWeldStart(startdescPose, enddescPose, startjointPos, endjointPos, 80, 40, 0, 0, 5000, true, 0, 3, 0, 30, 30, 100, -1, exaxisPos, 0, 0, offdese);

        return 0;
    }

    static int TestAuxServo(Robot robot) {
//*****************************************设置和获取485扩展轴配置参数 begin *************************************
//        Axis485Param param = new Axis485Param();
//        param.servoCompany = 1;           // 伺服驱动器厂商，1-戴纳泰克
//        param.servoModel = 1;             // 伺服驱动器型号，1-FD100-750C
//        param.servoSoftVersion = 1;       // 伺服驱动器软件版本，1-V1.0
//        param.servoResolution = 131072;        // 编码器分辨率
//        param.axisMechTransRatio = 13.45;  // 机械传动比
//        robot.AuxServoSetParam(1, param);//设置485扩展轴参数
//
//        robot.AuxServoGetParam(1, param);
//        System.out.println("auxservo param servoCompany: " + param.servoCompany + "  servoModel:  " + param.servoModel +
//                "  param.servoSoftVersion:  " + param.servoSoftVersion + "  servoResolution:  " + param.servoResolution + "  axisMechTransRatio:  " + param.axisMechTransRatio);
//*****************************************设置和获取485扩展轴配置参数 end *************************************


//*****************************************485扩展轴使能、回零、速度模式 begin *************************************
//        robot.AuxServoSetControlMode(1, 1);
//        robot.Sleep(2000);
//        robot.AuxServoEnable(1, 0);
//        robot.Sleep(3000);
//        robot.AuxServoEnable(1, 1);
//        robot.Sleep(2000);
//        robot.AuxServoHoming(1, 1, 10, 10,100);
//        robot.Sleep(5000);
//
//        robot.AuxServoSetTargetSpeed(1, 100,100);
//        robot.Sleep(3000);
//        robot.AuxServoSetTargetSpeed(1, -200,100);
//        robot.Sleep(3000);
//        robot.AuxServoSetTargetSpeed(1, 0,100);
//*****************************************485扩展轴使能、回零、速度模式 end *************************************

//*****************************************485扩展轴位置模式 begin *************************************
//        robot.AuxServoSetControlMode(1, 0);
//        robot.Sleep(2000);
//        robot.AuxServoEnable(1, 1);
//        robot.Sleep(2000);
//        robot.AuxServoHoming(1, 1, 10, 10,100);
//        robot.Sleep(5000);
//        robot.AuxServoSetTargetPos(1, -100, 30,100);
//        robot.Sleep(2000);
//        robot.AuxServoSetTargetPos(1, 500, 30,100);
//*****************************************485扩展轴位置模式 end *************************************

//*****************************************获取485伺服电机状态 begin *************************************
//        robot.AuxServoSetControlMode(1, 1);
//        robot.Sleep(2000);
//        robot.AuxServoEnable(1, 0);
//        robot.Sleep(3000);
//        robot.AuxServoEnable(1, 1);
//        robot.Sleep(2000);
//        robot.AuxServoHoming(1, 1, 10, 10,100);
//        robot.Sleep(5000);
//
//        robot.AuxServoSetTargetSpeed(1, 40,100);
//        robot.Sleep(3000);
//        robot.AuxServoSetTargetSpeed(1, 40,100);
//
//        robot.AuxServosetStatusID(1);
//
//        while (true)
//        {
//            ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
//            System.out.println("aux servo cur Pos :" + pkg.auxState.servoPos + "  cur vel:  " + pkg.auxState.servoVel);
//            robot.Sleep(100);
//        }
//*****************************************获取485伺服电机状态 end *************************************

//*****************************************485扩展轴加速度生效 begin *************************************
        robot.AuxServoEnable(1, 0);
        robot.Sleep(1000);

        robot.AuxServoSetControlMode(1, 0);
        robot.Sleep(2000);

        robot.AuxServoEnable(1, 1);
        robot.Sleep(2000);
        robot.AuxServoHoming(1, 1, 10, 10, 100);
        robot.Sleep(4000);

        while (true) {
            robot.AuxServoSetAcc(500, 500);
            robot.AuxServoSetTargetPos(1, 1000, 100, 50);
            robot.Sleep(2000);
            robot.AuxServoSetTargetPos(1, 0, 500, 50);
            robot.Sleep(3000);
            robot.AuxServoSetTargetPos(1, 1000, 500, 30);
            robot.Sleep(5000);
            robot.AuxServoSetTargetPos(1, 0, 500, 30);
            robot.Sleep(5000);
        }
//*****************************************485扩展轴加速度生效 end *************************************

//*****************************************485扩展轴急停减速配置 begin *************************************
//        robot.AuxServoEnable(1, 0);
//        robot.Sleep(2000);
//        robot.AuxServoSetControlMode(1, 1);
//        robot.Sleep(2000);
//        robot.AuxServoEnable(1, 1);
//        robot.Sleep(2000);
//        robot.AuxServoHoming(1, 1, 10, 10,100);
//        robot.Sleep(4000);
//
//        robot.AuxServoSetEmergencyStopAcc(200, 200);
//        robot.Sleep(1000);
//        List<Number> rtn =  robot.AuxServoGetEmergencyStopAcc();
//        System.out.println("emergency acc is " + rtn.get(1) + "   dec is " + rtn.get(2));
//
//        robot.AuxServoSetTargetSpeed(1, 500, 100);
//
//        robot.ProgramLoad("/fruser/test1014.lua");
//        robot.ProgramRun();
//
//        int i = 0;
//        while (true) {
//            ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();
//            pkg = robot.GetRobotRealTimeState();
//            System.out.println("time " + pkg.robotTime.second + " : "  + pkg.robotTime.millisecond + "   cur velocity is " + pkg.auxState.servoVel + "   cur 485 axis emergency state is " + ((pkg.auxState.servoState >> 7) & 0x01) + "    robot collision state is " + pkg.collisionState + "  robot emergency state is " + pkg.EmergencyStop);
//            robot.Sleep(5);
//        }
//*****************************************485扩展轴急停减速配置 end *************************************

//*****************************************485扩展轴急停状态获取 begin *************************************
//        robot.AuxServoEnable(1, 0);
//        robot.Sleep(2000);
//        robot.AuxServoSetControlMode(1, 1);
//        robot.Sleep(2000);
//
//        robot.AuxServoEnable(1, 1);
//        robot.Sleep(2000);
//
//        robot.AuxServoHoming(1, 1, 10, 10,100);
//        robot.Sleep(4000);
//
//        robot.AuxServoSetEmergencyStopAcc(200, 200);
//        robot.Sleep(1000);
//        List<Number> rtn =  robot.AuxServoGetEmergencyStopAcc();
//        System.out.println("emergency acc is " + rtn.get(1) + "   dec is " + rtn.get(2));
//
//        robot.AuxServoSetTargetSpeed(1, 500, 100);
//
//        robot.ProgramLoad("/fruser/test1014.lua");
//        robot.ProgramRun();
//
//        int i = 0;
//        while (true) {
//            i++;
//            if(i > 400)
//            {
//                robot.ResetAllError();
//                i = 0;
//
//                robot.AuxServoSetTargetSpeed(1, 200, 100);
//            }
//
//            ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();
//            pkg = robot.GetRobotRealTimeState();
//            System.out.println("time " + pkg.robotTime.second + " : "  + pkg.robotTime.millisecond + "   cur velocity is " + pkg.auxState.servoVel + "   cur 485 axis emergency state is " + ((pkg.auxState.servoState >> 7) & 0x01) + "    robot collision state is " + pkg.collisionState + "  robot emergency state is " + pkg.EmergencyStop);
//            robot.Sleep(5);
//        }
//*****************************************485扩展轴急停状态获取 end *************************************

//*****************************************速度模式设置加速度百分比生效 end *************************************
//        robot.AuxServoEnable(1, 0);
//        robot.Sleep(1000);
//        robot.AuxServoSetControlMode(1, 1);
//        robot.Sleep(1000);
//        robot.AuxServoEnable(1, 1);
//        robot.Sleep(1000);
//        robot.AuxServoHoming(1, 1, 10, 10,100);
//        robot.Sleep(3000);
//        while(true){
//            robot.AuxServoSetTargetSpeed(1, 500, 100);
//            robot.Sleep(2000);
//            robot.AuxServoSetTargetSpeed(1, 0, 100);
//            robot.Sleep(2000);
//            robot.AuxServoSetTargetSpeed(1, 500, 10);
//            robot.Sleep(2000);
//            robot.AuxServoSetTargetSpeed(1, 0, 10);
//        }
//*****************************************位置模式设置加速度百分比生效 end *************************************
//        robot.AuxServoEnable(1, 0);
//        robot.Sleep(1000);
//        robot.AuxServoSetControlMode(1, 0);
//        robot.Sleep(1000);
//        robot.AuxServoEnable(1, 1);
//        robot.Sleep(1000);
//        robot.AuxServoHoming(1, 1, 10, 10,100);
//        robot.Sleep(3000);
//        while(true){
//            robot.AuxServoSetTargetPos(1, 1000, 500,100);
//            robot.Sleep(2000);
//            robot.AuxServoSetTargetPos(1, 0, 500,100);
//            robot.Sleep(2000);
//            robot.AuxServoSetTargetPos(1, 1000, 500,10);
//            robot.Sleep(2000);
//            robot.AuxServoSetTargetPos(1, 0, 500,100);
//            robot.Sleep(2000);
//        }
//*****************************************位置模式设置加速度百分比生效 end *************************************

//*****************************************加速度稳定性 begin *************************************
//        robot.AuxServoEnable(1, 0);
//        robot.Sleep(1000);
//        robot.AuxServoSetControlMode(1, 0);
//        robot.Sleep(1000);
//        robot.AuxServoEnable(1, 1);
//        robot.Sleep(1000);
//        robot.AuxServoHoming(1, 1, 10, 10,100);
//        robot.Sleep(3000);
//        int i=0;
//        while(true){
//            robot.AuxServoSetTargetPos(1, 1000*(i+1), 500,100);
//            robot.Sleep(2000);
//            robot.AuxServoSetTargetPos(1, 1000*(i+2), 500,20);
//            robot.Sleep(2000);
//            i=i+2;
//        }
//*****************************************加速度稳定性 end *************************************

//        return 0;
    }

    private static void TestEndLuaGripper(Robot robot) {
//        String rttser = "";
//        robot.AxleLuaUpload("D://zUP/AXLE_LUA_End_DaHuan.lua", rttser);

        ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();
        AxleComParam param = new AxleComParam(7, 8, 1, 0, 5, 3, 1);
        //AxleComParam param = new AxleComParam(8,7,2,1,6,4,2);
        robot.SetAxleCommunicationParam(param);

        AxleComParam getParam = new AxleComParam();
        robot.GetAxleCommunicationParam(getParam);

        robot.SetAxleLuaEnable(1);
        int[] luaEnableStatus = new int[5];
        robot.GetAxleLuaEnableStatus(luaEnableStatus);
        robot.SetAxleLuaEnableDeviceType(0, 1, 0, 0);
        int[] type = new int[10];
        robot.GetAxleLuaEnableDeviceType(type);
        //int[] func = new int[]{0,1,1,1,1,0,1,1,0,1,0,0,0,0,0,0};
        int[] func = new int[]{0,1,1,1,1,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        robot.SetAxleLuaGripperFunc(2, func);
        int[] getFunc = new int[16];
        robot.GetAxleLuaGripperFunc(2, getFunc);
        int[] forceEnable = new int[16];
        int[] gripperEnable = new int[16];
        int[] ioEnable = new int[16];
        int[] handEnable = new int[16];
        robot.GetAxleLuaEnableDevice(forceEnable, gripperEnable, ioEnable, handEnable);
        robot.ActGripper(2, 0);
        robot.Sleep(2000);
        robot.ActGripper(2, 1);
        robot.Sleep(2000);
        robot.MoveGripper(2, 1, 10, 100, 50000, 0, 0, 0, 0, 0);
        int pos = 0;
        while (true)
        {
            pkg = robot.GetRobotRealTimeState();
            System.out.println("pos is " + pkg.gripper_position);
            robot.Sleep(100);
        }

    }

    private static void EndLuaUpload(Robot robot) {
//        robot.AxleLuaUpload("D://zUP/AXLE_LUA_End_DaHuan_WeiHangBad.lua");
        //robot.AxleLuaUpload("D://zUP/AXLE_LUA_End_DaHuan_WeiHang.lua");
//        robot.SetAxleLuaEnable(1);
//        TestEndLuaGripper(robot);
//        while (true)
//        {
//            ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
//            System.out.println("end lua err code is " + pkg.endLuaErrCode);
//            System.out.println("gripper pos is " + pkg.gripper_position);
//            robot.Sleep(100);
//        }
    }

    private static void TestEndLuaForce(Robot robot) {
        ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();
        AxleComParam param = new AxleComParam(7, 8, 1, 0, 5, 3, 1);
        robot.SetAxleCommunicationParam(param);

        AxleComParam getParam = new AxleComParam();
        robot.GetAxleCommunicationParam(getParam);

        robot.SetAxleLuaEnable(1);
        int[] luaEnableStatus = new int[5];
        robot.GetAxleLuaEnableStatus(luaEnableStatus);
        robot.SetAxleLuaEnableDeviceType(1, 0, 0, 0);
        int[] type = new int[10];
        robot.GetAxleLuaEnableDeviceType(type);

        int[] forceEnable = new int[16];
        int[] gripperEnable = new int[16];
        int[] ioEnable = new int[16];
        int[] handEnable = new int[16];
        robot.GetAxleLuaEnableDevice(forceEnable, gripperEnable, ioEnable, handEnable);

        robot.Sleep(1000);
        Object[] M = {15.0, 15.0, 15.0, 0.5, 0.5, 0.1};
        Object[] B = {150.0, 150.0, 150.0, 5.0, 5.0, 1.0};
        Object[] K = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        Object[] F = {10.0, 10.0, 10.0, 1.0, 1.0, 1.0};
        robot.EndForceDragControl(1, 0, 0, 0, M, B, K, F, 50, 100);

        robot.Sleep(2000);

        robot.EndForceDragControl(0, 0, 0, 0, M, B, K, F, 50, 100);
    }

    //肘部ARC，测试奇异点保护
    public static void TestSingularAvoidEArc(Robot robot) {
        DescPose startdescPose = new DescPose(-57.170, -690.147, 370.969, 176.438, -8.320, 169.881);
        JointPos startjointPos = new JointPos(78.017, -62.036, 69.561, -94.199, -98.416, -1.360);

        DescPose middescPose = new DescPose(-71.044, -743.395, 375.996, -179.499, -5.398, 168.739);
        JointPos midjointPos = new JointPos(77.417, -55.000, 58.732, -94.360, -95.385, -1.376);

        DescPose enddescPose = new DescPose(-439.979, -512.743, 396.472, 178.112, 3.625, 146.576);
        JointPos endjointPos = new JointPos(40.243, -65.402, 70.802, -92.565, -87.055, -16.465);


        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        robot.MoveL(startjointPos, startdescPose, 0, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
        robot.SingularAvoidStart(2, 10, 5, 5);
        robot.MoveC(midjointPos, middescPose, 0, 0, 30, 100, exaxisPos, 0, offdese, endjointPos, enddescPose, 0, 0, 30, 100, exaxisPos, 0, offdese, 100, -1);
        robot.SingularAvoidEnd();


//        DescPose startdescPose=new DescPose(-352.437, -88.350, 226.471, 177.222, 4.924, 86.631);
//        JointPos startjointPos=new JointPos(-3.463, -84.308, 105.579, -108.475, -85.087, -0.334);
//
//        DescPose middescPose=new DescPose(-518.339, -23.706, 207.899, -178.420, 0.171, 71.697);
//        JointPos midjointPos=new JointPos(-8.587, -51.805, 64.914, -104.695, -90.099, 9.718);
//
//        DescPose enddescPose=new DescPose(-273.934, 323.003, 227.224, 176.398, 2.783, 66.064);
//        JointPos endjointPos=new JointPos(-63.460, -71.228, 88.068, -102.291, -90.149, -39.605);
//
//
//        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
//        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
//
//        robot.MoveL(startjointPos, startdescPose, 0, 0, 50, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
//        robot.SingularAvoidStart(1, 100, 50, 10);
//        robot.MoveC(midjointPos, middescPose, 0, 0, 50, 100, exaxisPos, 0, offdese, endjointPos, enddescPose, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);
//        robot.SingularAvoidEnd();
    }

    //肩部ARC，测试奇异点保护
    public static void TestSingularAvoidSArc(Robot robot) {
        robot.SingularAvoidEnd();
        int rtn = 0;
        DescPose startdescPose = new DescPose(299.993, -168.982, 299.998, 179.999, -0.002, -166.415);
        JointPos startjointPos = new JointPos(-12.160, -71.236, -131.775, -66.992, 90.000, 64.255);

        DescPose middescPose = new DescPose(249.985, -140.988, 299.929, 179.996, -0.013, -166.417);
        JointPos midjointPos = new JointPos(-8.604, -60.474, -137.494, -72.046, 89.999, 67.813);

        DescPose enddescPose = new DescPose(-249.991, -168.980, 299.981, 179.999, 0.004, -107.386);
        JointPos endjointPos = new JointPos(-126.186, -63.401, -136.126, -70.477, 89.998, -108.800);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        rtn = robot.MoveL(startjointPos, startdescPose, 0, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
        rtn = robot.SingularAvoidStart(2, 30, 5, 5);
        rtn = robot.MoveC(midjointPos, middescPose, 0, 0, 30, 100, exaxisPos, 0, offdese, endjointPos, enddescPose, 0, 0, 30, 100, exaxisPos, 0, offdese, 100, -1);
        rtn = robot.SingularAvoidEnd();
        System.out.println("robot moving rtn is " + rtn);

//        robot.SingularAvoidEnd();
//        DescPose startdescPose=new DescPose(-379.749, -113.569, 262.288, -178.716, 2.620, 91.597);
//        JointPos startjointPos=new JointPos(1.208, -80.436, 93.788, -104.620, -87.372, -0.331);
//
//        DescPose middescPose=new DescPose(-151.941, -155.742, 262.756, 177.693, 2.571, 106.941);
//        JointPos midjointPos=new JointPos(16.727, -121.385, 124.147, -90.442, -87.440, -0.318);
//
//        DescPose enddescPose=new DescPose(-211.982, 218.852, 280.712, 176.819, -4.408, 26.857);
//        JointPos endjointPos=new JointPos(-63.754, -98.766, 105.961, -94.052, -94.435, -0.366);
//
//        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
//        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
//
//        robot.MoveL(startjointPos, startdescPose, 0, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
//        robot.SingularAvoidStart(0, 150, 50, 20);//开启保护
//        robot.MoveC(midjointPos, middescPose, 0, 0, 30, 100, exaxisPos, 0, offdese, endjointPos, enddescPose, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);
//        robot.SingularAvoidEnd();
    }

    //腕部Line，测试奇异点保护
    public static void TestSingularAvoidWLin(Robot robot) {
        DescPose startdescPose = new DescPose(-352.574, -685.606, 479.415, -15.926, -54.905, 130.693);
        JointPos startjointPos = new JointPos(49.630, -56.597, 60.013, -57.990, 42.725, 146.834);

        DescPose enddescPose = new DescPose(-653.655, -235.943, 434.585, -176.403, -54.513, -66.719);
        JointPos endjointPos = new JointPos(5.072, -58.920, 55.280, -57.939, -41.207, 146.834);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        robot.MoveL(startjointPos, startdescPose, 0, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
        robot.SingularAvoidStart(2, 30, 10, 3);
        robot.MoveL(endjointPos, enddescPose, 0, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
        robot.SingularAvoidEnd();

//        DescPose startdescPose=new DescPose(-402.473, -185.876, 103.985, -175.367, 59.682, 94.221);
//        JointPos startjointPos=new JointPos(-0.095, -50.828, 109.737, -150.708, -30.225, -0.623);
//
//        DescPose enddescPose=new DescPose(-399.264, -184.434, 296.022, -4.402, 58.061, -94.161);
//        JointPos endjointPos=new JointPos(-0.095, -65.547, 105.145, -131.397, 31.851, -0.622);
//
//        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
//        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
//
//        robot.MoveL(startjointPos, startdescPose, 0, 0, 50, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
//        robot.SingularAvoidStart(0, 150, 50, 20);
//        robot.MoveL(endjointPos, enddescPose, 0, 0, 50, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
//        robot.SingularAvoidEnd();
    }

    /// 腕部ARC，测试奇异点保护
    public static void TestSingularAvoidWArc(Robot robot) {
        DescPose startdescPose = new DescPose(-352.575, -685.604, 479.380, -15.933, -54.906, 130.699);
        JointPos startjointPos = new JointPos(49.630, -56.597, 60.017, -57.989, 42.725, 146.834);

        DescPose middescPose = new DescPose(-437.302, -372.046, 366.764, -133.489, -62.309, -94.994);
        JointPos midjointPos = new JointPos(21.202, -72.442, 84.164, -51.660, -29.880, 146.823);

        DescPose enddescPose = new DescPose(-653.649, -235.926, 434.525, -176.386, -54.515, -66.734);
        JointPos endjointPos = new JointPos(5.070, -58.920, 55.287, -57.937, -41.207, 146.834);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        robot.MoveL(startjointPos, startdescPose, 0, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
        robot.SingularAvoidStart(2, 10, 5, 4);
        robot.MoveC(midjointPos, middescPose, 0, 0, 30, 100, exaxisPos, 0, offdese, endjointPos, enddescPose, 0, 0, 30, 100, exaxisPos, 0, offdese, 100, -1);
        robot.SingularAvoidEnd();

//        DescPose startdescPose=new DescPose(-352.794, -164.582, 132.122, 176.136, 50.177, 85.343);
//        JointPos startjointPos=new JointPos(-2.048, -66.683, 121.240, -141.651, -39.776, -0.564);
//
//        DescPose middescPose=new DescPose(-352.353, -3.338, 299.600, -1.730, 58.744, -136.276);
//        JointPos midjointPos=new JointPos(-30.807, -92.341, 126.259, -102.944, 33.740, -25.798);
//
//        DescPose enddescPose=new DescPose(-352.353, -3.337, 353.164, -1.729, 58.744, -136.276);
//        JointPos endjointPos=new JointPos(-30.807, -98.084, 116.943, -87.886, 33.740, -25.798);
//
//        DescPose descPose=new DescPose(-402.473, -185.876, 103.985, -175.367, 59.682, 94.221);
//
//        JointPos jointPos=new JointPos(-0.095, -50.828, 109.737, -150.708, -30.225, -0.623);
//
//        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
//        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
//
//        robot.MoveL(startjointPos, startdescPose, 0, 0, 50, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
//        robot.SingularAvoidStart(0, 150, 50, 20);
//        robot.MoveC(midjointPos, middescPose, 0, 0, 100, 100, exaxisPos, 0, offdese, endjointPos, enddescPose, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);
//        robot.MoveL(jointPos, descPose, 0, 0, 50, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
//        robot.SingularAvoidEnd();
    }

    //肩部Line，测试奇异点保护
    public static void TestSingularAvoidSLin(Robot robot) {
        DescPose startdescPose = new DescPose(300.002, -102.991, 299.994, 180.000, -0.001, -166.416);
        JointPos startjointPos = new JointPos(-0.189, -66.345, -134.615, -69.042, 90.000, 76.227);

        DescPose enddescPose = new DescPose(-300.000, -103.001, 299.994, 179.998, 0.003, -107.384);
        JointPos endjointPos = new JointPos(-142.292, -66.345, -134.615, -69.042, 89.997, -124.908);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        robot.MoveL(startjointPos, startdescPose, 0, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
        robot.SingularAvoidStart(2, 30, 10, 3);
        robot.MoveL(endjointPos, enddescPose, 0, 0, 30, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
        robot.SingularAvoidEnd();

//        DescPose startdescPose=new DescPose(-379.749, -113.569, 262.293, -178.715, 2.620, 91.597);
//        JointPos startjointPos=new JointPos(1.208, -80.436, 93.788, -104.620, -87.372, -0.331);
//
//        DescPose enddescPose=new DescPose(252.972, -74.287, 316.795, -177.588, 2.451, 97.588);
//        JointPos endjointPos=new JointPos(7.165, -170.868, 63.507, 14.965, -87.534, -0.319);
//
//        ExaxisPos exaxisPos=new ExaxisPos(0, 0, 0, 0);
//        DescPose offdese=new DescPose(0, 0, 0, 0, 0, 0);
//
//        robot.MoveL(startjointPos, startdescPose, 0, 0, 50, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
//        robot.SingularAvoidStart(0, 150, 50, 20);
//        robot.MoveL(endjointPos, enddescPose, 0, 0, 50, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
//        robot.SingularAvoidEnd();
    }

    //轨迹J文件上传与删除
    public static void UploadTrajectoryJ(Robot robot) {
        robot.TrajectoryJDelete("testA.txt");//删除轨迹文件
        robot.TrajectoryJUpLoad("D://zUP/testA.txt");

        int retval = 0;
        String traj_file_name = "/fruser/traj/testA.txt";
        retval = robot.LoadTrajectoryJ(traj_file_name, 100, 1);
        System.out.println("LoadTrajectoryJ %s, retval is:" + traj_file_name + retval);

        DescPose traj_start_pose = new DescPose(0, 0, 0, 0, 0, 0);
        retval = robot.GetTrajectoryStartPose(traj_file_name, traj_start_pose);
        System.out.println("GetTrajectoryStartPose is: %d" + retval);
        System.out.println("desc_pos:" + "(" + traj_start_pose.tran.x + "," + traj_start_pose.tran.y + "," + traj_start_pose.tran.z + "," + traj_start_pose.rpy.rx + "," + traj_start_pose.rpy.ry + "," + traj_start_pose.rpy.rz + ")");

        robot.SetSpeed(30);
        robot.MoveCart(traj_start_pose, 1, 0, 100, 100, 100, -1, -1);

        robot.Sleep(5000);

        int traj_num = 0;

        ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
        traj_num = pkg.trajectory_pnum;
        System.out.println("GetTrajectoryStartPose traj num is:" + traj_num);

        retval = robot.MoveTrajectoryJ();
        System.out.println("MoveTrajectoryJ retval is:" + retval);
    }

    //轨迹J文件上传与删除
    public static void UploadTrajectoryB(Robot robot) {
        robot.TrajectoryJDelete("testB.txt");//删除轨迹文件
        robot.TrajectoryJUpLoad("D://zUP/testB.txt");

        int retval = 0;
        String traj_file_name = "/fruser/traj/testB.txt";
        retval = robot.LoadTrajectoryJ(traj_file_name, 100, 1);
        System.out.println("LoadTrajectoryJ " + traj_file_name + ", retval is:" + retval);

        DescPose traj_start_pose = new DescPose(0, 0, 0, 0, 0, 0);
        retval = robot.GetTrajectoryStartPose(traj_file_name, traj_start_pose);
        System.out.println("GetTrajectoryStartPose is:" + retval);
        System.out.println("desc_pos:" + traj_start_pose.tran.x + "," + traj_start_pose.tran.y + "," + traj_start_pose.tran.z + "," + traj_start_pose.rpy.rx + "," + traj_start_pose.rpy.ry + "," + traj_start_pose.rpy.rz);

        robot.SetSpeed(30);
        robot.MoveCart(traj_start_pose, 1, 0, 100, 100, 100, -1, -1);

        robot.Sleep(5000);

        int traj_num = 0;
        ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
        traj_num = pkg.trajectory_pnum;
        System.out.println("GetTrajectoryStartPose traj num is:" + traj_num);

        retval = robot.MoveTrajectoryJ();
        System.out.println("MoveTrajectoryJ retval is:" + retval);
    }

    public static void MoveRotGripper(Robot robot, int pos, double rotPos) {
        robot.ResetAllError();
        robot.ActGripper(1, 1);
        robot.Sleep(1000);
        int rtn = robot.MoveGripper(1, pos, 50, 50, 5000, 1, 1, rotPos, 50, 100);
        System.out.println("move gripper rtn is:" + rtn);
        while (true) {
            ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
            if (Math.abs(pkg.gripper_position - pos) < 1.5) {
                break;
            } else {
                System.out.println("cur gripper pos is:" + pkg.gripper_position);
                robot.Sleep(10);
            }
        }
        System.out.println("Gripper Motion Done:" + pos);
    }

    public static void SetAO(Robot robot, float value) {
        robot.SetAO(0, value, 0);
        robot.SetAO(1, value, 0);
        robot.SetToolAO(0, value, 0);
        while (true) {
            ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
            if (Math.abs(pkg.cl_analog_output[0] / 40.96 - value) < 0.5) {
                break;
            } else {
                System.out.println("cur AO value is" + pkg.cl_analog_output[0]);
                robot.Sleep(1);
            }
        }
        System.out.println("setAO Done:" + value);
    }

    public static void FIRArc(Robot robot, boolean enable) {
        DescPose startdescPose = new DescPose(-366.397, -572.427, 418.339, -178.972, 1.829, -142.970);
        JointPos startjointPos = new JointPos(43.651, -70.284, 91.057, -109.075, -88.768, -83.382);

        DescPose middescPose = new DescPose(-569.710, -132.595, 395.147, 178.418, -1.893, 171.051);
        JointPos midjointPos = new JointPos(-2.334, -79.300, 108.196, -120.594, -91.790, -83.386);

        DescPose enddescPose = new DescPose(-608.420, 610.692, 314.930, -176.438, -1.756, 117.333);
        JointPos endjointPos = new JointPos(-56.153, -46.964, 68.015, -113.200, -86.661, -83.479);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        if (enable) {
            robot.LinArcFIRPlanningStart(1000, 1000, 1000, 1000);
            robot.MoveL(startjointPos, startdescPose, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
            robot.MoveC(midjointPos, middescPose, 0, 0, 100, 100, exaxisPos, 0, offdese, endjointPos, enddescPose, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);
            robot.LinArcFIRPlanningEnd();
        } else {
            robot.MoveL(startjointPos, startdescPose, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
            robot.MoveC(midjointPos, middescPose, 0, 0, 100, 100, exaxisPos, 0, offdese, endjointPos, enddescPose, 0, 0, 100, 100, exaxisPos, 0, offdese, 100, -1);
        }
    }

    public static void FIRLin(Robot robot, boolean enable) {
        DescPose startdescPose = new DescPose(-569.710, -132.595, 395.147, 178.418, -1.893, 171.051);
        JointPos startjointPos = new JointPos(-2.334, -79.300, 108.196, -120.594, -91.790, -83.386);

        DescPose enddescPose = new DescPose(-366.397, -572.427, 418.339, -178.972, 1.829, -142.970);
        JointPos endjointPos = new JointPos(43.651, -70.284, 91.057, -109.075, -88.768, -83.382);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        if (enable) {
            robot.LinArcFIRPlanningStart(5000, 5000, 5000, 5000);
            robot.MoveL(startjointPos, startdescPose, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
            robot.MoveL(endjointPos, enddescPose, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
            robot.LinArcFIRPlanningEnd();
        } else {
            robot.MoveL(startjointPos, startdescPose, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
            robot.MoveL(endjointPos, enddescPose, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
        }
    }

    public static void FIRLinL(Robot robot, boolean enable) {
        DescPose startdescPose = new DescPose(-608.420, 610.692, 314.930, -176.438, -1.756, 117.333);
        JointPos startjointPos = new JointPos(-56.153, -46.964, 68.015, -113.200, -86.661, -83.479);

        DescPose enddescPose = new DescPose(-366.397, -572.427, 418.339, -178.972, 1.829, -142.970);
        JointPos endjointPos = new JointPos(43.651, -70.284, 91.057, -109.075, -88.768, -83.382);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        if (enable) {
            robot.LinArcFIRPlanningStart(5000, 5000, 5000, 5000);
            robot.MoveL(startjointPos, startdescPose, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
            robot.MoveL(endjointPos, enddescPose, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
            robot.LinArcFIRPlanningEnd();
        } else {
            robot.MoveL(startjointPos, startdescPose, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
            robot.MoveL(endjointPos, enddescPose, 0, 0, 100, 100, 100, -1, exaxisPos, 0, 0, offdese, 1, 1);
        }
    }

    public static void FIRPTP(Robot robot, boolean enable) {
        DescPose startdescPose = new DescPose(-569.710, -132.595, 395.147, 178.418, -1.893, 171.051);
        JointPos startjointPos = new JointPos(-2.334, -79.300, 108.196, -120.594, -91.790, -83.386);

        DescPose enddescPose = new DescPose(-366.397, -572.427, 418.339, -178.972, 1.829, -142.970);
        JointPos endjointPos = new JointPos(43.651, -70.284, 91.057, -109.075, -88.768, -83.382);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        if (enable) {
            robot.PtpFIRPlanningStart(1000);
            robot.MoveJ(startjointPos, startdescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
            robot.MoveJ(endjointPos, enddescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
            robot.PtpFIRPlanningEnd();
        } else {
            robot.MoveJ(startjointPos, startdescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
            robot.MoveJ(endjointPos, enddescPose, 0, 0, 100, 100, 100, exaxisPos, -1, 0, offdese);
        }
    }

public static void TestTPD2(Robot robot)
{
    int rtn = 0;
    int type = 1;
    String name = "tpd2025";
    int period_ms = 4;
    int di_choose = 0;
    int do_choose = 0;

    robot.SetTPDParam(type, name, period_ms, di_choose, do_choose);

    robot.Mode(1);
    robot.Sleep(1000);
    robot.DragTeachSwitch(1);
    robot.SetTPDStart(type, name, period_ms, di_choose, do_choose);
    robot.Sleep(3000);
    robot.SetWebTPDStop();
    robot.DragTeachSwitch(0);

    robot.Sleep(1000);
    double ovl = 100.0;
    int blend = 0;
    DescPose start_pose = new DescPose();
    rtn = robot.LoadTPD(name);
    System.out.printf("LoadTPD rtn is: %d\n", rtn);

    robot.GetTPDStartPose(name, start_pose);
    System.out.printf("start pose, xyz is: %f %f %f. rpy is: %f %f %f \n", start_pose.tran.x, start_pose.tran.y, start_pose.tran.z, start_pose.rpy.rx, start_pose.rpy.ry, start_pose.rpy.rz);
    //robot.MoveCart(&start_pose, 0, 0, 100, 100, ovl, -1, -1);
    //robot.Sleep(1000);

    rtn = robot.MoveToTPDStart(name, 0, 100);
    System.out.printf("MoveToTPDStart rtn is: %d\n", rtn);

    rtn = robot.MoveTPD(name, blend, ovl);
    System.out.printf("MoveTPD rtn is: %d\n", rtn);

    robot.Sleep(5000);

    robot.SetTPDDelete(name);

    return ;
}

    public static void testAxleGenCom(Robot robot) {
        int[] led_on = {0xAB, 0xBA, 0x12, 0x01, 0x01, 0x79};
        int[] led_off = {0xAB, 0xBA, 0x12, 0x01, 0x00, 0x78};
        int[] version = {0xAB, 0xBA, 0x11, 0x00, 0x76};
        int[] state = {0xAB, 0xBA, 0x1B, 0x01, 0xAA, 0x2B};

        int[] rcvdata = new int[16];
        int ret = 0;
        int cnt = 1;

        JointPos p1Joint = new JointPos(88.708, -86.178, 140.989, -141.825, -89.162, -49.879);
        DescPose p1Desc = new DescPose(188.007, -377.850, 260.207, 178.715, 2.823, -131.466);

        JointPos p2Joint = new JointPos(112.131, -75.554, 126.989, -139.027, -88.044, -26.477);
        DescPose p2Desc = new DescPose(368.003, -377.848, 260.211, 178.715, 2.823, -131.465);

        ExaxisPos exaxisPos = new ExaxisPos(0, 0, 0, 0);
        DescPose offdese = new DescPose(0, 0, 0, 0, 0, 0);

        // 开启末端透传功能
        robot.SetAxleGenComEnable(1);
        robot.SetAxleLuaEnable(1);

        while (cnt <= 10000) {
            // 读取版本号
            ret = robot.SndRcvAxleGenComCmdData(5, version, 10, rcvdata);
            if (ret == 0) {
                System.out.printf(" hard version : %d,hard code:%d, soft version:%d %d, soft code:%d \n",
                        rcvdata[4], rcvdata[5], rcvdata[6], rcvdata[7], rcvdata[8]);
            } else {
                System.out.println("SndRcvAxleGenComCmdData version fail: " + ret);
                break;
            }
            robot.Sleep(1000);

            // 读取艾灸头在位状态
            ret = robot.SndRcvAxleGenComCmdData(6, state, 6, rcvdata);
            if (ret == 0) {
                System.out.printf(" state : %d \n", rcvdata[4]);
            }
            robot.Sleep(1000);

            // 开启艾灸头激光
            ret = robot.SndRcvAxleGenComCmdData(6, led_on, 6, rcvdata);
            if (ret == 0) {
                System.out.printf("led on rcv data is: %d, %d, %d, %d, %d, %d  \n",
                        rcvdata[0], rcvdata[1], rcvdata[2], rcvdata[3], rcvdata[4], rcvdata[5]);
            }
            robot.MoveJ(p1Joint, p1Desc, 0, 0, 100.0, 100.0, 100.0, exaxisPos, -1.0, 0, offdese);
            robot.Sleep(4000);

            // 关闭艾灸头激光
            ret = robot.SndRcvAxleGenComCmdData(6, led_off, 6, rcvdata);
            if (ret == 0) {
                System.out.printf("led off rcv data is: %d, %d, %d, %d, %d, %d \n",
                        rcvdata[0], rcvdata[1], rcvdata[2], rcvdata[3], rcvdata[4], rcvdata[5]);
            }
            robot.MoveJ(p2Joint, p2Desc, 0, 0, 100.0, 100.0, 100.0, exaxisPos, -1.0, 0, offdese);
            robot.Sleep(1000);

            System.out.println("***********************complete No. " + cnt + " SDK test*****************************");
            cnt++;
        }
    }

    public static void TestRobotStopOnComDisc(Robot robot)
    {
        int[] enable = {0};
        int[] confirmTime = {0};
        int rtn = 0;
        rtn = robot.SetRobotStopOnComDisc(0, true, 330);
        rtn = robot.SetRobotStopOnComDisc(1, true, 550);
        rtn = robot.SetRobotStopOnComDisc(2, true, 110);
        rtn = robot.SetRobotStopOnComDisc(3, true, 220);
        System.out.printf("SetRobotStopOnComDisc %d\n", rtn);

        robot.GetRobotStopOnComDisc(0, enable, confirmTime);
        System.out.printf("GetRobotStopOnComDisc 8080 rtn %d; enable is %d; confirm time is %d\n", rtn, enable[0], confirmTime[0]);
        robot.GetRobotStopOnComDisc(1, enable, confirmTime);
        System.out.printf("GetRobotStopOnComDisc 8083 rtn %d; enable is %d; confirm time is %d\n", rtn, enable[0], confirmTime[0]);
        robot.GetRobotStopOnComDisc(2, enable, confirmTime);
        System.out.printf("GetRobotStopOnComDisc 20002 rtn %d; enable is %d; confirm time is %d\n", rtn, enable[0], confirmTime[0]);
        robot.GetRobotStopOnComDisc(3, enable, confirmTime);
        System.out.printf("GetRobotStopOnComDisc 20004 rtn %d; enable is %d; confirm time is %d\n", rtn, enable[0], confirmTime[0]);

        return;
    }

public static void TestRobotUDP (Robot robot) {
    robot.udpCmdClient.SetUDPCmdRpyCallback((srcType, count, cmdID, dataLen, content) -> {
        System.out.println("\n[收到机器人 UDP 回复]");
        System.out.println("srcType: " + srcType);
        System.out.println("count: " + count);
        System.out.println("cmdID: " + cmdID);
        System.out.println("dataLen: " + dataLen);
        System.out.println("内容 (content): " + content);
        return 0;
    });
    // 发送帧
    String frameToSend = "/f/bIII52III236III7IIIMode(1)III/b/f";
    robot.SendUDPFrame(frameToSend);
    robot.Sleep(2000);
    frameToSend = "/f/bIII52III236III7IIIMode(0)III/b/f";
    robot.SendUDPFrame(frameToSend);
    robot.Sleep(2000);
    frameToSend = "/f/bIII41III201III153IIIMoveJ(53.857,-89.441,119.453,-22.664,61.059,3.369,-54.249,-491.930,375.396,96.474,-6.896,-7.783,0,0,100,100,100,0.000,0.000,0.000,0.000,-1,0,0,0,0,0,0,0)III/b/f";
    robot.SendUDPFrame(frameToSend);
    robot.Sleep(2000);
    frameToSend = "/f/bIII42III203III163IIIMoveL(81.736,-85.284,114.974,-23.261,88.746,6.799,125.744,-506.570,375.396,96.474,-6.896,-7.783,0,0,100,100,100,-1,0,0.000,0.000,0.000,0.000,0,0,0,0,0,0,0,0,100,0)III/b/f";
    robot.SendUDPFrame(frameToSend);
    robot.Sleep(2000);
    frameToSend = "/f/bIII47III400III15IIIGetMCVersion(1)III/b/f/f/bIII48III424III21IIIGetSlaveFirmVersion()III/b/f";
    robot.SendUDPFrame(frameToSend);
    robot.Sleep(2000);

//        int rtn;
//
//        rtn = robot.SendUDPFrame("/f/bIII20III303III7IIIMode(0)III/b/f");
//        System.out.println("SendUDPFrame rtn is " + rtn);
//
//        rtn = robot.SendUDPFrame("III20III303III7IIIMode(0)III/b/f");
//        System.out.println("SendUDPFrame rtn is " + rtn);
//
//        rtn = robot.SendUDPFrame("/f/bIII20III303III7IIIMode(0)");
//        System.out.println("SendUDPFrame rtn is " + rtn);
//
//        rtn = robot.SendUDPFrame("/f/bIII20III303III6IIIMode(0)III/b/f");
//        System.out.println("SendUDPFrame rtn is " + rtn);
//
//        rtn = robot.SendUDPFrame("/f/b|||20|||303|||7|||Mode(0)|||/b/f");
//        System.out.println("SendUDPFrame rtn is " + rtn);
//
//        rtn = robot.SendUDPFrame("/f/bII20II303II7IIMode(0)II/b/f");
//        System.out.println("SendUDPFrame rtn is " + rtn);
    }
//    public static int TestSetVelReducePara(Robot robot) {
//        int rtn = 0;
//
//        JointPos j1 = new JointPos(0, -90, 90, 0, 0, 0);
//        JointPos j2 = new JointPos(90, -90, 90, 0, 0, 0);
//        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);
//        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
//
//        robot.SetSpeed(80);
//        rtn = robot.SetVelReducePara(2, 30, 1);
//        System.out.printf("SetVelReducePara param error rtn is %d\n", rtn);
//
//        rtn = robot.SetVelReducePara(0, 30, 1);
//        System.out.printf("SetVelReducePara disable reduce vel rtn is %d\n", rtn);
//        robot.MoveJ(j1, 0, 0, 100, 100, 100.0, epos, -1.0, 0, offset_pos);
//        robot.MoveJ(j2, 0, 0, 100, 100, 100.0, epos, -1.0, 0, offset_pos);
//
//        rtn = robot.SetVelReducePara(1, 30, 1);
//        System.out.printf("SetVelReducePara reduce vel rtn is %d\n", rtn);
//        robot.MoveJ(j1, 0, 0, 100, 100, 100.0, epos, -1.0, 0, offset_pos);
//        robot.MoveJ(j2, 0, 0, 100, 100, 100.0, epos, -1.0, 0, offset_pos);
//
//        rtn = robot.SetVelReducePara(2, 30, 2);
//        System.out.printf("SetVelReducePara disable robot rtn is %d\n", rtn);
//        robot.MoveJ(j1, 0, 0, 100, 100, 100.0, epos, -1.0, 0, offset_pos);
//        robot.MoveJ(j2, 0, 0, 100, 100, 100.0, epos, -1.0, 0, offset_pos);
//
//        robot.Sleep(2000);
//        robot.ResetAllError();
//        robot.RobotEnable(1);
//        robot.Sleep(1000);
//
//        rtn = robot.SetVelReducePara(2, 30, 0);
//        System.out.printf("SetVelReducePara report error rtn is %d\n", rtn);
//        robot.MoveJ(j1, 0, 0, 100, 100, 100.0, epos, -1.0, 0, offset_pos);
//        robot.MoveJ(j2, 0, 0, 100, 100, 100.0, epos, -1.0, 0, offset_pos);
//
//        robot.Sleep(1000);
//        return 0;
//    }
    /**
     * @brief 测试定点摆动功能
     * @param robot Robot对象
     * @return 错误码
     */
    public static int TestOriginPointWeave(Robot robot) {
        JointPos j = new JointPos(39.886, -98.580, -124.032, -47.393, 90.000, 40.842);
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);
        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);

        DescPose refPoint = new DescPose(400.021, 300.022, 299.996, 179.997, -0.003, -90.956);
        robot.MoveJ(j, 1, 0, 100, 100, 100.0, epos, -1.0, 0, offset_pos);

        robot.OriginPointWeaveStart(0, 0, refPoint, 3);
        robot.MoveStationary();
        robot.OriginPointWeaveEnd();

        robot.Sleep(2000);

        robot.MoveJ(j, 1, 0, 100, 100, 100.0, epos, -1.0, 0, offset_pos);
        robot.OriginPointWeaveStart(0, 1, refPoint, 3);
        robot.MoveStationary();
        robot.OriginPointWeaveEnd();

        robot.Sleep(1000);
        return 0;
    }

    public static int TestOriginPointWeave2(Robot robot) {
        JointPos j = new JointPos(39.886, -98.580, -124.032, -47.393, 90.000, 40.842);
        ExaxisPos epos1 = new ExaxisPos(0, 0, 0, 0);
        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
        ExaxisPos epos2 = new ExaxisPos(5, 0, 0, 0);
        DescPose refPoint = new DescPose(400.021, 300.022, 299.996, 179.997, -0.003, -90.956);

        int rtn = 0;
        robot.LaserTrackingSensorConfig("192.168.58.20", 5020);
        robot.LaserTrackingSensorSamplePeriod(20);
        robot.LoadPosSensorDriver(101);

        // 加载 UDP 驱动
        robot.ExtDevLoadUDPDriver();

        // 设置外部轴命令完成时间
        rtn = robot.SetExAxisCmdDoneTime(5000.0);
        System.out.println("SetExAxisCmdDoneTime rtn is " + rtn);
        // 使能外部轴 1 和 2
        rtn = robot.ExtAxisServoOn(1, 1);
        System.out.println("ExtAxisServoOn axis id 1 rtn is " + rtn);
        rtn = robot.ExtAxisServoOn(2, 1);
        System.out.println("ExtAxisServoOn axis id 2 rtn is " + rtn);
        robot.Sleep(2000);

        // 设置外部轴回零
        robot.ExtAxisSetHoming(1, 0, 10, 2);
        robot.LaserTrackingLaserOnOff(1,0);


        //// 1---不带扩展轴
        robot.LaserTrackingTrackOnOff(1, 4);
        robot.Sleep(200);
        // 启动定点摆动
        robot.OriginPointWeaveStart(0, 0, refPoint, 10);
        robot.MoveStationary();   // 执行固定运动（假设该方法存在）
        robot.OriginPointWeaveEnd();
        robot.LaserTrackingTrackOnOff(0, 4);

        robot.Sleep(2000);         // 等待2秒

        //// 2----带扩展轴
        robot.ExtAxisMove(epos1, 100, -1);
        robot.LaserTrackingTrackOnOff(1, 4);
        // 启动定点摆动
        robot.OriginPointWeaveStart(0, 0, refPoint, 20);
        robot.ExtAxisMove(epos2, 100, -1);
        robot.OriginPointWeaveEnd();
        robot.LaserTrackingTrackOnOff(0, 4);

        robot.Sleep(1000);
        return 0;
    }

 /**
     * @brief 测试IO配置功能
     * @param robot Robot对象
     * @return 错误码
     */
    public static int TestIOConfig(Robot robot) {
        int[] setDIConfig = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] getDIConfig = new int[8];
        int rtn = robot.SetDIConfig(setDIConfig);
        System.out.println("SetDIConfig rtn is " + rtn);
        rtn = robot.GetDIConfig(getDIConfig);
        System.out.println("GetDIConfig rtn is " + rtn + ", value is " +
            getDIConfig[0] + " " + getDIConfig[1] + " " + getDIConfig[2] + " " + getDIConfig[3] + " " +
            getDIConfig[4] + " " + getDIConfig[5] + " " + getDIConfig[6] + " " + getDIConfig[7]);

        int[] setDOConfig = new int[]{9, 10, 11, 12, 13, 14, 15, 16};
        int[] getDOConfig = new int[8];
        rtn = robot.SetDOConfig(setDOConfig);
        System.out.println("SetDOConfig rtn is " + rtn);
        rtn = robot.GetDOConfig(getDOConfig);
        System.out.println("GetDOConfig rtn is " + rtn + ", value is " +
            getDOConfig[0] + " " + getDOConfig[1] + " " + getDOConfig[2] + " " + getDOConfig[3] + " " +
            getDOConfig[4] + " " + getDOConfig[5] + " " + getDOConfig[6] + " " + getDOConfig[7]);

        int[] setToolDIConfig = new int[]{17, 18};
        int[] getToolDIConfig = new int[2];
        rtn = robot.SetToolDIConfig(setToolDIConfig);
        System.out.println("SetToolDIConfig rtn is " + rtn);
        rtn = robot.GetToolDIConfig(getToolDIConfig);
        System.out.println("GetToolDIConfig rtn is " + rtn + ", value is " + getToolDIConfig[0] + " " + getToolDIConfig[1]);

        int[] setDIConfigLevel = new int[]{1, 1, 1, 1, 0, 0, 0, 0};
        int[] getDIConfigLevel = new int[8];
        rtn = robot.SetDIConfigLevel(setDIConfigLevel);
        System.out.println("SetDIConfigLevel rtn is " + rtn);
        rtn = robot.GetDIConfigLevel(getDIConfigLevel);
        System.out.println("GetDIConfigLevel rtn is " + rtn + ", value is " +
            getDIConfigLevel[0] + " " + getDIConfigLevel[1] + " " + getDIConfigLevel[2] + " " + getDIConfigLevel[3] + " " +
            getDIConfigLevel[4] + " " + getDIConfigLevel[5] + " " + getDIConfigLevel[6] + " " + getDIConfigLevel[7]);

        int[] setDOConfigLevel = new int[]{0, 0, 0, 0, 1, 1, 1, 1};
        int[] getDOConfigLevel = new int[8];
        rtn = robot.SetDOConfigLevel(setDOConfigLevel);
        System.out.println("SetDOConfigLevel rtn is " + rtn);
        rtn = robot.GetDOConfigLevel(getDOConfigLevel);
        System.out.println("GetDOConfigLevel rtn is " + rtn + ", value is " +
            getDOConfigLevel[0] + " " + getDOConfigLevel[1] + " " + getDOConfigLevel[2] + " " + getDOConfigLevel[3] + " " +
            getDOConfigLevel[4] + " " + getDOConfigLevel[5] + " " + getDOConfigLevel[6] + " " + getDOConfigLevel[7]);

        int[] setToolDIConfigLevel = new int[]{1, 0};
        int[] getToolDIConfigLevel = new int[2];
        rtn = robot.SetToolDIConfigLevel(setToolDIConfigLevel);
        System.out.println("SetToolDIConfigLevel rtn is " + rtn);
        rtn = robot.GetToolDIConfigLevel(getToolDIConfigLevel);
        System.out.println("GetToolDIConfigLevel rtn is " + rtn + ", value is " + getToolDIConfigLevel[0] + " " + getToolDIConfigLevel[1]);

        int[] setStandardDILevel = new int[]{1, 1, 1, 1, 0, 0, 0, 0};
        int[] getStandardDILevel = new int[8];
        rtn = robot.SetStandardDILevel(setStandardDILevel);
        System.out.println("SetStandardDILevel rtn is " + rtn);
        rtn = robot.GetStandardDILevel(getStandardDILevel);
        System.out.println("GetStandardDILevel rtn is " + rtn + ", value is " +
            getStandardDILevel[0] + " " + getStandardDILevel[1] + " " + getStandardDILevel[2] + " " + getStandardDILevel[3] + " " +
            getStandardDILevel[4] + " " + getStandardDILevel[5] + " " + getStandardDILevel[6] + " " + getStandardDILevel[7]);

        int[] setStandardDOLevel = new int[]{0, 0, 0, 0, 1, 1, 1, 1};
        int[] getStandardDOLevel = new int[8];
        rtn = robot.SetStandardDOLevel(setStandardDOLevel);
        System.out.println("SetStandardDOLevel rtn is " + rtn);
        rtn = robot.GetStandardDOLevel(getStandardDOLevel);
        System.out.println("GetStandardDOLevel rtn is " + rtn + ", value is " +
            getStandardDOLevel[0] + " " + getStandardDOLevel[1] + " " + getStandardDOLevel[2] + " " + getStandardDOLevel[3] + " " +
            getStandardDOLevel[4] + " " + getStandardDOLevel[5] + " " + getStandardDOLevel[6] + " " + getStandardDOLevel[7]);

        robot.Sleep(2000);
        return 0;
    }

    public static int TestUDPAxis_CPP(Robot robot) {
//        UDPComParam param = new UDPComParam("192.168.58.88", 2021, 2, 100, 3, 200, 1, 100, 5, 1);
//        int rtn = robot.ExtDevSetUDPComParam(param);
//        System.out.println("ExtDevSetUDPComParam rtn is " + rtn);
//
//        UDPComParam getParam = new UDPComParam();
//        rtn = robot.ExtDevGetUDPComParam(getParam);
//        String showParam = "\nip " + getParam.ip +
//                "\nport " + getParam.port +
//                "\nperiod  " + getParam.period +
//                "\nlossPkgTime " + getParam.lossPkgTime +
//                "\nlossPkgNum  " + getParam.lossPkgNum +
//                "\ndisConntime  " + getParam.disconnectTime +
//                "\nreconnecable  " + getParam.reconnectEnable +
//                "\nreconnperiod  " + getParam.reconnectPeriod +
//                "\nreconnnun  " + getParam.reconnectNum +
//                "\nselfConnect  " + getParam.selfConnect;
//        System.out.println("ExtDevGetUDPComParam rtn is " + rtn + showParam);
//
//        rtn = robot.ExtDevLoadUDPDriver();
//        System.out.println("ExtDevLoadUDPDriver rtn is " + rtn);
//
//        rtn = robot.SetExAxisCmdDoneTime(5000.0);
//        System.out.println("SetExAxisCmdDoneTime rtn is " + rtn);
        int rtn =0;
        rtn = robot.ExtAxisServoOn(1, 1);
        System.out.println("ExtAxisServoOn axis id 1 rtn is " + rtn);
        rtn = robot.ExtAxisServoOn(2, 1);
        System.out.println("ExtAxisServoOn axis id 2 rtn is " + rtn);
        robot.Sleep(2000);

        rtn = robot.ExtAxisSetHoming(1, 0, 10.0, 2.0);
        System.out.println("ExtAxisSetHoming axis id 1 rtn is " + rtn);
        robot.Sleep(2000);
        rtn = robot.ExtAxisSetHoming(2, 0, 10.0, 2.0);
        System.out.println("ExtAxisSetHoming axis id 2 rtn is " + rtn);

        robot.Sleep(4000);

        rtn = robot.SetRobotPosToAxis(1);
        System.out.println("SetRobotPosToAxis rtn is " + rtn);

        rtn = robot.SetAxisDHParaConfig(10, 20.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        System.out.println("SetAxisDHParaConfig rtn is " + rtn);

        rtn = robot.ExtAxisParamConfig(1, 1, 1, 1000.0, -1000.0, 1000.0, 1000.0, 1.905, 262144, 200.0, 1, 0, 0);
        System.out.println("ExtAxisParamConfig axis 1 rtn is " + rtn);
        rtn = robot.ExtAxisParamConfig(2, 1, 1, 1000.0, -1000.0, 1000.0, 1000.0, 4.444, 262144, 200.0, 1, 0, 0);
        System.out.println("ExtAxisParamConfig axis 2 rtn is " + rtn);

        robot.Sleep(1000 * 3);
        rtn = robot.ExtAxisStartJog(1, 0, 10.0, 10.0, 30.0);
        System.out.println("ExtAxisStartJog axis 1 rtn is " + rtn);
        robot.Sleep(1000);
        rtn = robot.ExtAxisStopJog(1);
        System.out.println("ExtAxisStopJog axis 1 rtn is " + rtn);
        robot.Sleep(1000 * 3);
        rtn = robot.ExtAxisServoOn(1, 0);
        System.out.println("ExtAxisServoOn axis id 1 disable rtn is " + rtn);

        robot.Sleep(1000 * 3);
        rtn = robot.ExtAxisStartJog(2, 0, 10.0, 10.0, 30.0);
        System.out.println("ExtAxisStartJog axis 2 rtn is " + rtn);
        robot.Sleep(1000);
        rtn = robot.ExtAxisStopJog(2);
        System.out.println("ExtAxisStopJog axis 2 rtn is " + rtn);
        robot.Sleep(1000 * 3);
        rtn = robot.ExtAxisServoOn(2, 0);
        System.out.println("ExtAxisServoOn axis id 2 disable rtn is " + rtn);

        rtn = robot.ExtDevUnloadUDPDriver();
        System.out.println("ExtDevUnloadUDPDriver rtn is " + rtn);

        return 0;
    }

    public static int TestCtrlOpenLuaOperate(Robot robot) {
        int rtn;

        rtn = robot.OpenLuaUpload("D://zUP/openlua/CtrlDev_WELDING_A.lua");
        System.out.println("OpenLuaUpload rtn is " + rtn);
        rtn = robot.OpenLuaUpload("D://zUP/openlua/CtrlDev_SWDPOLISH.lua");
        System.out.println("OpenLuaUpload rtn is " + rtn);
        rtn = robot.OpenLuaDownload("CtrlDev_WELDING_A.lua", "D://zDOWN/");
        System.out.println("OpenLuaDownload rtn is " + rtn);
        rtn = robot.OpenLuaDownload("CtrlDev_SWDPOLISH.lua", "D://zDOWN/");
        System.out.println("OpenLuaDownload rtn is " + rtn);

        rtn = robot.SetCtrlOpenLUAName(0, "CtrlDev_WELDING_A.lua");
        System.out.println("SetCtrlOpenLUAName rtn is " + rtn);
        rtn = robot.SetCtrlOpenLUAName(1, "CtrlDev_SWDPOLISH.lua");
        System.out.println("SetCtrlOpenLUAName rtn is " + rtn);

        String[] names = new String[4];
        rtn = robot.GetCtrlOpenLUAName(names);
        System.out.println("GetCtrlOpenLUAName rtn is " + rtn + ", names: " +
                names[0] + ", " + names[1] + ", " + names[2] + ", " + names[3]);

        rtn = robot.LoadCtrlOpenLUA(1);
        System.out.println("LoadCtrlOpenLUA rtn is " + rtn);
        robot.Sleep(2000);
        rtn = robot.UnloadCtrlOpenLUA(1);
        System.out.println("UnloadCtrlOpenLUA rtn is " + rtn);

        rtn = robot.OpenLuaDelete("CtrlDev_WELDING_A.lua");
        System.out.println("OpenLuaDelete rtn is " + rtn);
        rtn = robot.AllOpenLuaDelete();
        System.out.println("AllOpenLuaDelete rtn is " + rtn);

        robot.Sleep(1000);
        return 0;
    }

    public static void TestReadBasicStates(Robot robot) throws InterruptedException {
        int ret = 0;
//        List<RobotState> state = new ArrayList<>();
//        state.add(RobotState.RobotTime);
//        state.add(RobotState.JointCurPos);
//        state.add(RobotState.ToolCurPos);
////        state.add(RobotState.OtherStat);
//        ret = robot.SetRobotRealtimeStateConfig(state, 2000);
//        System.out.println("SetRobotRealtimeStateConfig rtn : " + ret);

        int rtn = robot.RPC("192.168.58.2");
        if (rtn == 0) {
            System.out.println("rpc连接 success");
        } else {
            System.out.println("rpc连接 fail");
            return;
        }

        Robot.StateConfigResult configResult = robot.GetRobotRealtimeStateConfig();
        System.out.println("[DEBUG] Current state config: " + configResult);
        System.out.println("[DEBUG] State list: " + configResult.stateList);

        System.out.println("\n===== 测试读取字段值 =====");

        // 等待状态数据更新
        robot.Sleep(1000);
        while(true) {
        robot.Sleep(200);
        // 通过 CNDE 获取状态数据
        ROBOT_STATE_PKG pkg = robot.CNDEGetStateData();
        if (pkg == null) {
            System.out.println("状态数据为空，请检查 CNDE 连接");
            return;
        }

        // 1. 帧信息
        System.out.println("\n--- 帧信息 ---");
        System.out.println("frame_head: 0x" + Integer.toHexString(pkg.frame_head));
        System.out.println("frame_cnt: " + pkg.frame_cnt);
        System.out.println("data_len: " + pkg.data_len);

        // 2. 程序与机器人状态
        System.out.println("\n--- 程序与机器人状态 ---");
        System.out.println("program_state: " + pkg.program_state);
        System.out.println("robot_state: " + pkg.robot_state);
        System.out.println("main_code: " + pkg.main_code);
        System.out.println("sub_code: " + pkg.sub_code);
        System.out.println("robot_mode: " + pkg.robot_mode);

        // 3. 位置数据
        System.out.println("\n--- 位置数据 ---");
        printArray("jt_cur_pos", pkg.jt_cur_pos);
        printArray("tl_cur_pos", pkg.tl_cur_pos);
        printArray("flange_cur_pos", pkg.flange_cur_pos);

        // // 4. 速度与加速度
         System.out.println("\n--- 速度与加速度 ---");
         printArray("target_tcp_cmp_speed", pkg.target_TCP_CmpSpeed);
         printArray("target_tcp_speed", pkg.target_TCP_Speed);
         printArray("actual_tcp_cmp_speed", pkg.actual_TCP_CmpSpeed);
         printArray("actual_TCP_Speed", pkg.actual_TCP_Speed);

        // // 5. 力矩数据
        // System.out.println("\n--- 力矩数据 ---");
         printArray("jt_cur_tor", pkg.jt_cur_tor);
         printArray("actual_joint_torque", pkg.jt_cur_tor);

        // 6. 工具与用户坐标系
        System.out.println("\n--- 坐标系 ---");
        System.out.println("tool: " + pkg.tool);
        System.out.println("user: " + pkg.user);
        printArray("tool_coord", pkg.toolCoord);
        printArray("wobjCoord", pkg.wobjCoord);
        printArray("extoolCoord", pkg.extoolCoord);
        printArray("exAxisCoord", pkg.exAxisCoord);

        // 7. 数字IO
        System.out.println("\n--- 数字IO ---");
        System.out.println("cl_dgt_output_h: " + pkg.cl_dgt_output_h);
        System.out.println("cl_dgt_output_l: " + pkg.cl_dgt_output_l);
        System.out.println("tl_dgt_output_l: " + pkg.tl_dgt_output_l);
        System.out.println("cl_dgt_input_h: " + pkg.cl_dgt_input_h);
        System.out.println("cl_dgt_input_l: " + pkg.cl_dgt_input_l);
        System.out.println("tl_dgt_input_l: " + pkg.tl_dgt_input_l);

        // 8. 模拟IO
        System.out.println("\n--- 模拟IO ---");
        printArray("cl_analog_input", pkg.cl_analog_input);
        System.out.println("tl_anglog_input: " + pkg.tl_anglog_input);
        printArray("cl_analog_output", pkg.cl_analog_output);
        System.out.println("tl_analog_output: " + pkg.tl_analog_output);

        // 9. 力传感器
        System.out.println("\n--- 力传感器 ---");
        printArray("ft_sensor_raw_data", pkg.ft_sensor_raw_data);
        printArray("ft_sensor_data", pkg.ft_sensor_data);
        System.out.println("ft_sensor_active: " + pkg.ft_sensor_active);

        // 10. 安全与状态标志
        System.out.println("\n--- 安全与状态标志 ---");
        System.out.println("motion_done: " + pkg.motion_done);
        System.out.println("mc_queue_len: " + pkg.mc_queue_len);
        System.out.println("collisionState: " + pkg.collisionState);
        System.out.println("trajectory_pnum: " + pkg.trajectory_pnum);
        System.out.println("safety_stop0_state: " + pkg.safety_stop0_state);
        System.out.println("safety_stop1_state: " + pkg.safety_stop1_state);

        // 11. 夹爪状态
        System.out.println("\n--- 夹爪状态 ---");
        System.out.println("gripper_fault_id: " + pkg.gripper_fault_id);
        System.out.println("gripper_fault: " + pkg.gripper_fault);
        System.out.println("gripper_active: " + pkg.gripper_active);
        System.out.println("gripper_position: " + pkg.gripper_position);
        System.out.println("gripper_speed: " + pkg.gripper_speed);
        System.out.println("gripper_current: " + pkg.gripper_current);
        System.out.println("gripper_temp: " + pkg.gripper_temp);
        System.out.println("gripper_voltage: " + pkg.gripper_voltage);
        System.out.println("gripperRotNum: " + pkg.gripperRotNum);
        System.out.println("gripperRotSpeed: " + pkg.gripperRotSpeed);
        System.out.println("gripperRotTorque: " + pkg.gripperRotTorque);

        // 12. 辅助轴状态
        System.out.println("\n--- 辅助轴状态 ---");
        if (pkg.aux_state != null) {
            System.out.println("aux_state: servoId=" + pkg.aux_state.servoId +
                ", errCode=" + pkg.aux_state.servoErrCode +
                ", state=" + pkg.aux_state.servoState +
                ", pos=" + pkg.aux_state.servoPos +
                ", vel=" + pkg.aux_state.servoVel +
                ", Torque=" + pkg.aux_state.servoTorque);
        }
        if (pkg.extAxisStatus != null) {
            for (int i = 0; i < pkg.extAxisStatus.length; i++) {
                System.out.println("extAxisStatus[" + i + "]: pos=" + pkg.extAxisStatus[i].pos +
                    ", vel=" + pkg.extAxisStatus[i].vel + ", ready=" + pkg.extAxisStatus[i].ready);
            }
        }

        // 13. 扩展IO
//        System.out.println("\n--- 扩展IO ---");
//        printShortArray("extDIState", pkg.extDIState);
//        printShortArray("extDOState", pkg.extDOState);
//        printShortArray("extAIState", pkg.extAIState);
//        printShortArray("extAOState", pkg.extAOState);

        // 14. 机器人时间
        System.out.println("\n--- 机器人时间 ---");
        if (pkg.robotTime != null) {
            System.out.println("robotTime: " + pkg.robotTime.year + "-" + pkg.robotTime.month + "-" + pkg.robotTime.day +
                " " + pkg.robotTime.hour + ":" + pkg.robotTime.minute + ":" + pkg.robotTime.second +
                "." + pkg.robotTime.millisecond);
        }
        System.out.println("softwareUpgradeState: " + pkg.softwareUpgradeState);

         // 15. 焊接状态
        System.out.println("\n--- 焊接状态 ---");
        if (pkg.weldingBreakOffState != null) {
            System.out.println("weldingBreakOffState: breakOffState=" + pkg.weldingBreakOffState.breakOffState +
                ", weldArcState=" + pkg.weldingBreakOffState.weldArcState);
        }

        // 16. 其他状态
        System.out.println("\n--- 其他状态 ---");
        System.out.println("endLuaErrCode: " + pkg.endLuaErrCode);
        System.out.println("smartToolState: " + pkg.smartToolState);
        System.out.println("wideVoltageCtrlBoxTemp: " + pkg.wideVoltageCtrlBoxTemp);
        System.out.println("wideVoltageCtrlBoxFanCurrent: " + pkg.wideVoltageCtrlBoxFanCurrent);
        System.out.println("rbtEnableState: " + pkg.rbtEnableState);

        // 17. 负载信息
        System.out.println("\n--- 负载信息 ---");
        System.out.println("load: " + pkg.load);
        printArray("loadCog", pkg.loadCog);

        // 18. 驱动器信息
        System.out.println("\n--- 驱动器信息 ---");
        printArray("jointDriverTorque", pkg.jointDriverTorque);
        printArray("jointDriverTemperature", pkg.jointDriverTemperature);

        // 19. 伺服命令
        System.out.println("\n--- 伺服命令 ---");
        System.out.println("servoJCmdNum: " + pkg.servoJCmdNum);

        // 20. 校验和
        System.out.println("\n--- 校验和 ---");
        System.out.println("check_sum: 0x" + Integer.toHexString(pkg.check_sum));

        System.out.println("speedScaleManual: " + pkg.speedScaleManual);
        System.out.println("speedScaleAuto: " + pkg.speedScaleAuto);

        // collisionLevel
        printArray("collisionLevel: " , pkg.collisionLevel);

//        System.out.println("\n===== ROBOT_STATE_PKG 所有字段值读取完成 =====\n");
        }
    }

    // 辅助方法：打印double数组
    private static void printArray(String name, double[] arr) {
        if (arr == null) return;
        StringBuilder sb = new StringBuilder(name + ": [");
        for (int i = 0; i < arr.length; i++) {
            sb.append(String.format("%.3f", arr[i]));
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    // 辅助方法：打印int数组
    private static void printArray(String name, int[] arr) {
        if (arr == null) return;
        StringBuilder sb = new StringBuilder(name + ": [");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        System.out.println(sb.toString());
    }


    public static void TestCRUD(Robot robot) {
       List<RobotState> state = new ArrayList<>();
       state.add(RobotState.ProgramState);
       state.add(RobotState.RobotState);
       state.add(RobotState.MainCode);
       state.add(RobotState.SubCode);
       state.add(RobotState.RobotMode);
//       state.add(RobotState.RobotTime);
      robot.SetRobotRealtimeStateConfig(state, 100);

//        robot.AddRobotRealtimeState(RobotState.CollisionLevel);
//        robot.AddRobotRealtimeState(RobotState.Load);
//        robot.AddRobotRealtimeState(RobotState.LoadCog);
//        robot.DeleteRobotRealtimeState(RobotState.RobotTime);
            Robot.StateConfigResult configResult = robot.GetRobotRealtimeStateConfig();
            System.out.println("[DEBUG] Current state config: " + configResult);
            System.out.println("[DEBUG] State list: " + configResult.stateList);

            int rtn = robot.RPC("192.168.58.2");
            if (rtn == 0) {
                System.out.println("rpc连接 success");
            } else {
                System.out.println("rpc连接 fail");
                return;
            }




    }

    // 辅助方法：打印short数组
    private static void printShortArray(String name, short[] arr) {
        if (arr == null) return;
        StringBuilder sb = new StringBuilder(name + ": [");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    private static void TestCNDEParamError(Robot robot) {
        int ret = 0;
        List<RobotState> state = new ArrayList<>();
        state.add(RobotState.RobotTime);
        state.add(RobotState.JointCurPos);
        state.add(RobotState.ToolCurPos);
//        state.add(RobotState.OtherStat);
        ret = robot.SetRobotRealtimeStateConfig(state, 2000);
        System.out.println("SetRobotRealtimeStateConfig rtn : " + ret);

        ret = robot.SetRobotRealtimeStatePeriod(7);
        System.out.println("SetRobotRealtimeStatePeriod 7ms rtn : " + ret);
        ret = robot.SetRobotRealtimeStatePeriod(8);
        System.out.println("SetRobotRealtimeStatePeriod 8ms rtn : " + ret);

        ret = robot.AddRobotRealtimeState(RobotState.JointCurPos);
        System.out.println("AddRobotRealtimeState JointCurPos rtn : " + ret);


        ret = robot.DeleteRobotRealtimeState(RobotState.CtrlBoxError);
        System.out.println("DeleteRobotRealtimeState CtrlBoxError rtn : " + ret);

        ret = robot.SetRobotRealtimeStatePeriod(1001);
        System.out.println("SetRobotRealtimeStatePeriod 1001ms rtn : " + ret);


        int rtn = robot.RPC("192.168.58.2");
        if (rtn == 0) {
            System.out.println("rpc连接 success");
        } else {
            System.out.println("rpc连接 fail");
            return;
        }

        Robot.StateConfigResult configResult = robot.GetRobotRealtimeStateConfig();
        System.out.println("[DEBUG] Current state config: " + configResult);
        System.out.println("[DEBUG] State list: " + configResult.stateList);
    }

    private static void TestDefault(Robot robot) {
         List<RobotState> state = new ArrayList<>();
         state.add(RobotState.RobotTime);
         robot.SetRobotRealtimeStateConfig(state, 100);


        int rtn = robot.RPC("192.168.58.2");
        if (rtn == 0) {
            System.out.println("rpc连接 success");
        } else {
            System.out.println("rpc连接 fail");
            return;
        }
        // 等待CNDE连接建立
        System.out.println("等待CNDE连接建立...");
        while (robot.CNDEGetStateData() == null) {
            robot.Sleep(100);
        }
        System.out.println("CNDE连接已建立，开始接收数据...");

        while(true) {
            robot.Sleep(1000);
            // 通过 CNDE 获取状态数据
            ROBOT_STATE_PKG pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("状态数据为空，CNDE连接断开，等待重连");
                continue;  // 连接断开时继续循环，等待重连
            }

           System.out.println("\n--- 位置数据 ---");
           printArray("jt_cur_pos", pkg.jt_cur_pos);
           printArray("tl_cur_pos", pkg.tl_cur_pos);
           printArray("flange_cur_pos", pkg.flange_cur_pos);

           System.out.println("\n--- 负载信息 ---");
           System.out.println("load: " + pkg.load);
           printArray("loadCog", pkg.loadCog);

            System.out.println("\n--- 机器人时间 ---");
            if (pkg.robotTime != null) {
                System.out.println("robotTime: " + pkg.robotTime.year + "-" + pkg.robotTime.month + "-" + pkg.robotTime.day +
                        " " + pkg.robotTime.hour + ":" + pkg.robotTime.minute + ":" + pkg.robotTime.second +
                        "." + pkg.robotTime.millisecond);
            }

           System.out.println("\n--- 关节指令位置 ---");
           printArray("targetJointPos", pkg.targetJointPos);

            // --- 扩展轴状态 ---
            System.out.println("--- 扩展轴状态 (ExtAxisStatus[4]) ---");
            if (pkg.extAxisStatus != null) {
                for (int i = 0; i < pkg.extAxisStatus.length; i++) {
                    EXT_AXIS_STATUS axis = pkg.extAxisStatus[i];
                    System.out.printf("Axis[%d]: pos=%.3f, vel=%.3f, errorCode=%d, ready=%d, inPos=%d, alarm=%d%n",
                            i, axis.pos, axis.vel, axis.errorCode, axis.ready, axis.inPos, axis.alarm);
                    System.out.printf("         flerr=%d, nlimit=%d, pLimit=%d, mdbsOffLine=%d, mdbsTimeout=%d, homingStatus=%d%n",
                            axis.flerr, axis.nlimit, axis.pLimit, axis.mdbsOffLine, axis.mdbsTimeout, axis.homingStatus);
                }
            } else {
                System.out.println("extAxisStatus: null");
            }
        }
    }

    private static void TestCNDEStarte(Robot robot) {
         List<RobotState> state = new ArrayList<>();
         state.add(RobotState.RobotTime);
         state.add(RobotState.ProgramRunState);
         robot.SetRobotRealtimeStateConfig(state, 100);

        int rtn = robot.RPC("192.168.58.2");
        if (rtn == 0) {
            System.out.println("rpc连接 success");
        } else {
            System.out.println("rpc连接 fail");
            return;
        }
        // 等待CNDE连接建立
        System.out.println("等待CNDE连接建立...");
        while (robot.CNDEGetStateData() == null) {
            robot.Sleep(100);
        }
        System.out.println("CNDE连接已建立，开始接收数据...");

        while(true) {
            robot.Sleep(1000);
            // 通过 CNDE 获取状态数据
            ROBOT_STATE_PKG pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("状态数据为空，CNDE连接断开，等待重连");
                continue;  // 连接断开时继续循环，等待重连
            }
//
//            System.out.println("\n--- 位置数据 ---");
//            printArray("jt_cur_pos", pkg.jt_cur_pos);
//            printArray("tl_cur_pos", pkg.tl_cur_pos);
//            printArray("flange_cur_pos", pkg.flange_cur_pos);
//
//            System.out.println("\n--- 负载信息 ---");
//            System.out.println("load: " + pkg.load);
//            printArray("loadCog", pkg.loadCog);
//
            System.out.println("\n--- 机器人时间 ---");
            if (pkg.robotTime != null) {
                System.out.println("robotTime: " + pkg.robotTime.year + "-" + pkg.robotTime.month + "-" + pkg.robotTime.day +
                        " " + pkg.robotTime.hour + ":" + pkg.robotTime.minute + ":" + pkg.robotTime.second +
                        "." + pkg.robotTime.millisecond);
            }
            if (pkg.robotTime != null) {
                System.out.println("robotTime: " + pkg.exaxisCoordID + "-" + pkg.exAxisCoord[0] + "-" + pkg.robotTime.day +
                        " " + pkg.robotTime.hour + ":" + pkg.robotTime.minute + ":" + pkg.robotTime.second +
                        "." + pkg.robotTime.millisecond);
            }

//        System.out.println("exaxisCoordID: " + pkg.exaxisCoordID);
//        printArray("exAxisCoord", pkg.exAxisCoord);


        System.out.println("ProgramRunState: " + pkg.ProgramRunState);
        robot.Sleep(100);
            // System.out.println("\n--- Socket状态 ---");
            // System.out.println("socketConnTimeout: " + pkg.socketConnTimeout);
            // System.out.println("socketReadTimeout: " + pkg.socketReadTimeout);
            // System.out.println("tsWebStateComErr: " + pkg.tsWebStateComErr);
//
//            System.out.println("\n--- 关节指令位置 ---");
//            printArray("targetJointPos", pkg.targetJointPos);
//
//            // --- 扩展轴状态 ---
//            System.out.println("--- 扩展轴状态 (ExtAxisStatus[4]) ---");
//            if (pkg.extAxisStatus != null) {
//                for (int i = 0; i < pkg.extAxisStatus.length; i++) {
//                    EXT_AXIS_STATUS axis = pkg.extAxisStatus[i];
//                    System.out.printf("Axis[%d]: pos=%.3f, vel=%.3f, errorCode=%d, ready=%d, inPos=%d, alarm=%d%n",
//                            i, axis.pos, axis.vel, axis.errorCode, axis.ready, axis.inPos, axis.alarm);
//                    System.out.printf("         flerr=%d, nlimit=%d, pLimit=%d, mdbsOffLine=%d, mdbsTimeout=%d, homingStatus=%d%n",
//                            axis.flerr, axis.nlimit, axis.pLimit, axis.mdbsOffLine, axis.mdbsTimeout, axis.homingStatus);
//                }
//            } else {
//                System.out.println("extAxisStatus: null");
//            }


        }
    }
    /**
     * 测试扩展状态配置 - 配置并打印指定的状态列表
     */
    private static void TestExtendedStates(Robot robot) {
        int ret = 0;

        List<RobotState> state = new ArrayList<>();
        state.add(RobotState.JointCurPos);
        state.add(RobotState.ToolCurPos);
        state.add(RobotState.FlangeCurPos);
        state.add(RobotState.ActualJointVel);
        state.add(RobotState.ActualJointAcc);
        state.add(RobotState.TargetTCPCmpSpeed);
        state.add(RobotState.TargetTCPSpeed);
        state.add(RobotState.ActualTCPCmpSpeed);
        state.add(RobotState.ActualTCPSpeed);
        state.add(RobotState.ActualJointTorque);
        state.add(RobotState.SafetyStop0State);
        state.add(RobotState.SafetyStop1State);
        state.add(RobotState.JointDriverTorque);
        state.add(RobotState.JointDriverTemperature);
        state.add(RobotState.RobotTime);
        state.add(RobotState.TargetJointTorque);
        state.add(RobotState.WideVoltageCtrlBoxTemp);
        state.add(RobotState.WideVoltageCtrlBoxFanCurrent);
        state.add(RobotState.TargetJointPos);
        state.add(RobotState.TargetJointVel);
        state.add(RobotState.TargetJointAcc);
        state.add(RobotState.TargetJointCurrent);
        state.add(RobotState.ActualJointCurrent);
        state.add(RobotState.ActualTCPForce);
        state.add(RobotState.TargetTCPPos);
        state.add(RobotState.SafetyBoxSingal);
        state.add(RobotState.BtnBoxStopSignal);
        // 新增状态
        state.add(RobotState.ProgramState);
        state.add(RobotState.RobotState);
        state.add(RobotState.RobotMode);
        state.add(RobotState.MotionDone);
        state.add(RobotState.McQueueLen);
        state.add(RobotState.TrajectoryPnum);
        state.add(RobotState.Tool);
        state.add(RobotState.User);
        state.add(RobotState.ClDgtOutputH);
        state.add(RobotState.ClDgtOutputL);
        state.add(RobotState.TlDgtOutputL);
        state.add(RobotState.ClDgtInputH);
        state.add(RobotState.ClDgtInputL);
        state.add(RobotState.TlDgtInputL);
        state.add(RobotState.ClAnalogInput);
        state.add(RobotState.TlAnglogInput);
        state.add(RobotState.RbtEnableState);
        state.add(RobotState.SoftwareUpgradeState);
        state.add(RobotState.WeldingBreakOffState);
        state.add(RobotState.ClAnalogOutput);
        state.add(RobotState.TlAnalogOutput);
        state.add(RobotState.ToolCoord);
        state.add(RobotState.WobjCoord);
        state.add(RobotState.ExtoolCoord);
        state.add(RobotState.ExAxisCoord);
        state.add(RobotState.Load);
        state.add(RobotState.LoadCog);
        state.add(RobotState.LastServoTarget);
        state.add(RobotState.ServoJCmdNum);
        state.add(RobotState.CollisionLevel);
        state.add(RobotState.SpeedScaleManual);
        state.add(RobotState.SpeedScaleAuto);
        state.add(RobotState.LuaLineNum);
        state.add(RobotState.AbnomalStop);
        state.add(RobotState.CurrentLuaFileName);
        state.add(RobotState.ProgramTotalLine);
        state.add(RobotState.WeldVoltage);
        state.add(RobotState.WeldCurrent);
        state.add(RobotState.WeldTrackVel);
        state.add(RobotState.UdpCmdState);
        state.add(RobotState.WeldReadyState);
        ret = robot.SetRobotRealtimeStateConfig(state, 200);
        System.out.println("SetRobotRealtimeStateConfig rtn : " + ret);


        int rtn = robot.RPC("192.168.58.2");
        if (rtn == 0) {
            System.out.println("rpc连接 success");
        } else {
            System.out.println("rpc连接 fail");
            return;
        }

        // 等待CNDE连接建立
        System.out.println("等待CNDE连接建立...");
        while (robot.CNDEGetStateData() == null) {
            robot.Sleep(100);
        }
        System.out.println("CNDE连接已建立，开始接收数据...");

        while(true) {
            robot.Sleep(1000);
            ROBOT_STATE_PKG pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("状态数据为空，CNDE连接断开，等待重连");
                continue;
            }

            // --- 位置数据 ---
            System.out.println("--- 位置数据 ---");
            printArray("jt_cur_pos (关节当前位置)", pkg.jt_cur_pos);
            printArray("tl_cur_pos (工具当前位置)", pkg.tl_cur_pos);
            printArray("flange_cur_pos (法兰当前位置)", pkg.flange_cur_pos);

            // --- 速度与加速度 ---
            System.out.println("--- 速度与加速度 ---");
            printArray("actualJointVel (实际关节速度)", pkg.actual_qd);
            printArray("actualJointAcc (实际关节加速度)", pkg.actual_qdd);
            printArray("target_TCP_CmpSpeed (目标TCP合成速度)", pkg.target_TCP_CmpSpeed);
            printArray("targetTCPSpeed (目标TCP速度)", pkg.target_TCP_Speed);
            printArray("actual_TCP_CmpSpeed (实际TCP合成速度)", pkg.actual_TCP_CmpSpeed);
            printArray("actualTCPSpeed (实际TCP速度)", pkg.actual_TCP_Speed);

            // --- 力矩 ---
            System.out.println("--- 力矩 ---");
            printArray("actualJointTorque (实际关节力矩)", pkg.jt_cur_tor);
            printArray("targetJointTorque (目标关节力矩)", pkg.jt_tgt_tor);
            printArray("actualTCPForce (实际TCP力)", pkg.actualTCPForce);

            // --- 驱动器信息 ---
            System.out.println("--- 驱动器信息 ---");
            printArray("jointDriverTorque (关节驱动力矩)", pkg.jointDriverTorque);
            printArray("jointDriverTemperature (关节驱动温度)", pkg.jointDriverTemperature);
            printArray("actualJointCurrent (实际关节电流)", pkg.actualJointCurrent);
            printArray("targetJointCurrent (目标关节电流)", pkg.targetJointCurrent);

            // --- 目标位置与速度 ---
            System.out.println("--- 目标位置与速度 ---");
            printArray("targetJointPos (目标关节位置)", pkg.targetJointPos);
            printArray("targetJointVel (目标关节速度)", pkg.targetJointVel);
            printArray("targetJointAcc (目标关节加速度)", pkg.targetJointAcc);
            printArray("targetTCPPos (目标TCP位置)", pkg.targetTCPPos);

            // --- 安全状态 ---
            System.out.println("--- 安全状态 ---");
            System.out.println("safetyStop0State: " + pkg.safety_stop0_state);
            System.out.println("safetyStop1State: " + pkg.safety_stop1_state);
            printArray("safetyBoxSingal (安全箱信号)", pkg.safetyBoxSingal);
            System.out.println("btnBoxStopSignal (按钮盒停止信号): " + pkg.btnBoxStopSignal);

            // --- 控制箱状态 ---
            System.out.println("--- 控制箱状态 ---");
            System.out.println("wideVoltageCtrlBoxTemp (控制箱温度): " + pkg.wideVoltageCtrlBoxTemp);
            System.out.println("wideVoltageCtrlBoxFanCurrent (控制箱风扇电流): " + pkg.wideVoltageCtrlBoxFanCurrent);

            // --- 机器人时间 ---
            System.out.println("--- 机器人时间 ---");
            if (pkg.robotTime != null) {
                System.out.println("robotTime: " + pkg.robotTime.year + "-" + pkg.robotTime.month + "-" + pkg.robotTime.day +
                        " " + pkg.robotTime.hour + ":" + pkg.robotTime.minute + ":" + pkg.robotTime.second +
                        "." + pkg.robotTime.millisecond);
            } else {
                System.out.println("robotTime: null");
            }
            System.out.println("------------------------------------------------------------------------------------------");
        }
    }

    /**
     * 测试扩展状态配置2 - 配置并打印新增的状态列表
     */
    private static void TestExtendedStates2(Robot robot) {
        int ret = 0;

        List<RobotState> state = new ArrayList<>();
        state.add(RobotState.ProgramState);
        state.add(RobotState.RobotState);
        state.add(RobotState.RobotMode);
        state.add(RobotState.MotionDone);
        state.add(RobotState.McQueueLen);
        state.add(RobotState.TrajectoryPnum);
        state.add(RobotState.Tool);
        state.add(RobotState.User);
        state.add(RobotState.ClDgtOutputH);
        state.add(RobotState.ClDgtOutputL);
        state.add(RobotState.TlDgtOutputL);
        state.add(RobotState.ClDgtInputH);
        state.add(RobotState.ClDgtInputL);
        state.add(RobotState.TlDgtInputL);
        state.add(RobotState.ClAnalogInput);
        state.add(RobotState.TlAnglogInput);
        state.add(RobotState.RbtEnableState);
        state.add(RobotState.SoftwareUpgradeState);
        state.add(RobotState.WeldingBreakOffState);
        state.add(RobotState.ClAnalogOutput);
        state.add(RobotState.TlAnalogOutput);
        state.add(RobotState.ToolCoord);
        state.add(RobotState.WobjCoord);
        state.add(RobotState.ExtoolCoord);
        state.add(RobotState.ExAxisCoord);
        state.add(RobotState.Load);
        state.add(RobotState.LoadCog);
        state.add(RobotState.LastServoTarget);
        state.add(RobotState.ServoJCmdNum);
        state.add(RobotState.CollisionLevel);
        state.add(RobotState.SpeedScaleManual);
        state.add(RobotState.SpeedScaleAuto);
        state.add(RobotState.LuaLineNum);
        state.add(RobotState.AbnomalStop);
        state.add(RobotState.CurrentLuaFileName);
        state.add(RobotState.ProgramTotalLine);
        state.add(RobotState.WeldVoltage);
        state.add(RobotState.WeldCurrent);
        state.add(RobotState.WeldTrackVel);
        state.add(RobotState.UdpCmdState);
        state.add(RobotState.WeldReadyState);
        ret = robot.SetRobotRealtimeStateConfig(state, 100);
        System.out.println("SetRobotRealtimeStateConfig rtn : " + ret);


        int rtn = robot.RPC("192.168.58.2");
        if (rtn == 0) {
            System.out.println("rpc连接 success");
        } else {
            System.out.println("rpc连接 fail");
            return;
        }

        // 等待CNDE连接建立
        System.out.println("等待CNDE连接建立...");
        while (robot.CNDEGetStateData() == null) {
            robot.Sleep(100);
        }
        System.out.println("CNDE连接已建立，开始接收数据...");

        while(true) {
            robot.Sleep(1000);
            ROBOT_STATE_PKG pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("状态数据为空，CNDE连接断开，等待重连");
                continue;
            }

            // --- 程序与运动状态 ---
//            System.out.println("--- 程序与运动状态 ---");
//            System.out.println("programState (程序状态): " + pkg.program_state);
//            System.out.println("robotState (机器人状态): " + pkg.robot_state);
//            System.out.println("robotMode (机器人模式): " + pkg.robot_mode);
//            System.out.println("motionDone (运动完成): " + pkg.motion_done);
//            System.out.println("mcQueueLen (MC队列长度): " + pkg.mc_queue_len);
//            System.out.println("trajectoryPnum (轨迹点数量): " + pkg.trajectory_pnum);
//
//            // --- 工具与用户坐标系 ---
            System.out.println("--- 工具与用户坐标系 ---");
            System.out.println("tool (工具号): " + pkg.tool);
            System.out.println("user (工件坐标系号): " + pkg.user);
            printArray("toolCoord (工具坐标)", pkg.toolCoord);
            printArray("wobjCoord (工件坐标)", pkg.wobjCoord);
            printArray("extoolCoord (外轴工具坐标)", pkg.extoolCoord);
            printArray("exAxisCoord (外轴坐标)", pkg.exAxisCoord);
//
//            // --- 数字IO ---
//            System.out.println("--- 数字IO ---");
//            System.out.println("clDgtOutputH (控制器数字输出高): " + pkg.cl_dgt_output_h);
//            System.out.println("clDgtOutputL (控制器数字输出低): " + pkg.cl_dgt_output_l);
//            System.out.println("tlDgtOutputL (工具数字输出低): " + pkg.tl_dgt_output_l);
//            System.out.println("clDgtInputH (控制器数字输入高): " + pkg.cl_dgt_input_h);
//            System.out.println("clDgtInputL (控制器数字输入低): " + pkg.cl_dgt_input_l);
//            System.out.println("tlDgtInputL (工具数字输入低): " + pkg.tl_dgt_input_l);
//
//            // --- 模拟IO ---
//            System.out.println("--- 模拟IO ---");
//            printArray("clAnalogInput (控制器模拟输入)", pkg.cl_analog_input);
//            System.out.println("TlAnglogInput (工具模拟输入)" + pkg.tl_anglog_input);
//            printArray("clAnalogOutput (控制器模拟输出)", pkg.cl_analog_output);
//            System.out.println("tlAnalogOutput (工具模拟输出)" + pkg.tl_analog_output);
////
//            // --- 状态标志 ---
//            System.out.println("--- 状态标志 ---");
//            System.out.println("rbtEnableState (机器人使能状态): " + pkg.rbtEnableState);
//            System.out.println("softwareUpgradeState (软件升级状态): " + pkg.softwareUpgradeState);
//            System.out.println("weldingBreakOffState (焊接断弧状态),breakOffState:" + pkg.weldingBreakOffState.breakOffState + ",weldArcState:" + pkg.weldingBreakOffState.weldArcState);
//
//            printArray("collisionLevel (碰撞等级): ", pkg.collisionLevel);
//            System.out.println("speedScaleManual (手动速度比例): " + pkg.speedScaleManual);
//            System.out.println("speedScaleAuto (自动速度比例): " + pkg.speedScaleAuto);
//            System.out.println("luaLineNum (Lua行号): " + pkg.luaLineNum);
//            System.out.println("abnomalStop (异常停止): " + pkg.abnomalStop);
//            System.out.println("udpCmdState (UDP命令状态): " + pkg.udpCmdState);
//            System.out.println("weldReadyState (焊接就绪状态): " + pkg.weldReadyState);
//
////            // --- Lua程序 ---
//            System.out.println("--- Lua程序 ---");
//            System.out.println("currentLuaFileName (当前Lua文件名): " + pkg.currentLuaFileName);
//            System.out.println("programTotalLine (程序总行数): " + pkg.programTotalLine);
////
////            // --- 负载信息 ---
//            System.out.println("--- 负载信息 ---");
//            System.out.println("load (负载): " + pkg.load);
//            printArray("loadCog (负载重心)", pkg.loadCog);
////
////            // --- 伺服目标 ---
//            System.out.println("--- 伺服目标 ---");
//            printArray("lastServoTarget (最后伺服目标)", pkg.lastServoTarget);
//            System.out.println("servoJCmdNum (ServoJ命令数): " + pkg.servoJCmdNum);
////
////            // --- 焊接数据 ---
            System.out.println("--- 焊接数据 ---");
            System.out.println("weldVoltage (焊接电压): " + pkg.weldVoltage);
            System.out.println("weldCurrent (焊接电流): " + pkg.weldCurrent);
            System.out.println("weldTrackVel (焊接跟踪速度): " + pkg.weldTrackVel);

            System.out.println("------------------------------------------------------------------------------------------");
        }
    }

    /**
     * 测试报警和错误状态 - 配置并打印各类报警和错误状态
     */
    private static void TestAlarmStates(Robot robot) {
        int ret = 0;

        List<RobotState> state = new ArrayList<>();
        state.add(RobotState.EmergencyStop);
        state.add(RobotState.MainCode);
        state.add(RobotState.SubCode);
        state.add(RobotState.CollisionState);
        state.add(RobotState.EndLuaErrCode);
        state.add(RobotState.TpdException);
        state.add(RobotState.AlarmRebootRobot);
        state.add(RobotState.DragAlarm);
        state.add(RobotState.SafetyDoorAlarm);
        state.add(RobotState.SafetyPlaneAlarm);
        state.add(RobotState.MotonAlarm);
        state.add(RobotState.InterfaceAlarm);
        state.add(RobotState.AlarmCheckEmergStopBtn);
        state.add(RobotState.TsTmCmdComError);
        state.add(RobotState.TsTmStateComError);
        state.add(RobotState.CtrlBoxError);
        state.add(RobotState.SafetyDataState);
        state.add(RobotState.CtrlOpenLuaErrCode);
        state.add(RobotState.StrangePosFlag);
        state.add(RobotState.Alarm);
        state.add(RobotState.DriverAlarm);
        state.add(RobotState.AliveSlaveNumError);
        state.add(RobotState.SlaveComError);
        state.add(RobotState.CmdPointError);
        state.add(RobotState.IOError);
        state.add(RobotState.GripperError);
        state.add(RobotState.FileError);
        state.add(RobotState.ParaError);
        state.add(RobotState.ExaxisOutLimitError);
        state.add(RobotState.DriverComError);
        state.add(RobotState.DriverError);
        state.add(RobotState.OutSoftLimitError);

        ret = robot.SetRobotRealtimeStateConfig(state, 100);
        System.out.println("SetRobotRealtimeStateConfig rtn : " + ret);

        int rtn = robot.RPC("192.168.58.2");
        if (rtn == 0) {
            System.out.println("rpc连接 success");
        } else {
            System.out.println("rpc连接 fail");
            return;
        }

        // 等待CNDE连接建立
        System.out.println("等待CNDE连接建立...");
        while (robot.CNDEGetStateData() == null) {
            robot.Sleep(100);
        }
        System.out.println("CNDE连接已建立，开始接收数据...");

        while (true) {
            robot.Sleep(1000);
            ROBOT_STATE_PKG pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("状态数据为空，CNDE连接断开，等待重连");
                continue;
            }

            // --- 急停与安全状态 ---
            System.out.println("--- 急停与安全状态 ---");
            System.out.println("EmergencyStop (急停状态): " + pkg.EmergencyStop);
            System.out.println("alarmCheckEmergStopBtn (报警检查急停按钮): " + pkg.alarmCheckEmergStopBtn);
            System.out.println("safetyDataState (安全数据状态): " + pkg.safetyDataState);

            // --- 故障码 ---
            System.out.println("--- 故障码 ---");
            System.out.println("mainCode (主故障码): " + pkg.main_code + ",subCode (子故障码): " + pkg.sub_code);

            // --- 碰撞与干涉 ---
//            System.out.println("--- 碰撞与干涉 ---");
//            System.out.println("collisionState (碰撞状态): " + pkg.collisionState);
//            System.out.println("motonAlarm (运动报警): " + pkg.motonAlarm);
//            System.out.println("interfaceAlarm (干涉报警): " + pkg.interfaceAlarm);

            // --- Lua相关错误 ---
            System.out.println("--- Lua相关错误 ---");
            System.out.println("endLuaErrCode (末端Lua错误码): " + pkg.endLuaErrCode);
            System.out.println("tpdException (TPD异常): " + pkg.tpdException);
            printArray("ctrlOpenLuaErrCode (控制打开Lua错误码)", pkg.ctrlOpenLuaErrCode);

            // --- 系统报警 ---
            System.out.println("--- 系统报警 ---");
            System.out.println("alarm (报警): " + pkg.alarm);
            System.out.println("alarmRebootRobot (报警重启机器人): " + pkg.alarmRebootRobot);
            System.out.println("dragAlarm (拖动报警): " + pkg.dragAlarm);
            System.out.println("safetyDoorAlarm (安全门报警): " + pkg.safetyDoorAlarm);
            System.out.println("safetyPlaneAlarm (安全平面报警): " + pkg.safetyPlaneAlarm);

            // --- 控制器与通信错误 ---
            System.out.println("--- 控制器与通信错误 ---");
            System.out.println("ctrlBoxError (控制箱错误): " + pkg.ctrlBoxError);
            System.out.println("tsTmCmdComError (命令通信错误): " + pkg.tsTmCmdComError);
            System.out.println("tsTmStateComError (状态通信错误): " + pkg.tsTmStateComError);

            // --- 驱动器相关 ---
//            System.out.println("--- 驱动器相关 ---");
//            System.out.println("driverAlarm (驱动器报警): " + pkg.driverAlarm + ",driverError (驱动器错误): " + pkg.driverError);
//            printArray("driverComError (驱动器通信错误)", pkg.driverComError);
//            System.out.println("aliveSlaveNumError (存活从站数量错误): " + pkg.aliveSlaveNumError);
//            printArray("slaveComError (从站通信错误)", pkg.slaveComError);

            // --- 其他错误 ---
            System.out.println("--- 其他错误 ---");
            System.out.println("strangePosFlag (奇异位置标志): " + pkg.strangePosFlag);
            System.out.println("cmdPointError (命令点错误): " + pkg.cmdPointError);
            System.out.println("IOError (IO错误): " + pkg.IOError);
            System.out.println("gripperError (夹爪错误): " + pkg.gripperError);
            System.out.println("fileError (文件错误): " + pkg.fileError);
            System.out.println("paraError (参数错误): " + pkg.paraError);
            System.out.println("exaxisOutLimitError (扩展轴超出软限位错误): " + pkg.exaxisOutLimitError);
            System.out.println("outSoftLimitError (超出软限位错误): " + pkg.outSoftLimitError);

            System.out.println("------------------------------------------------------------------------------------------");
        }
    }

    /**
     * 测试传感器、夹爪和Modbus状态 - 配置并打印力传感器、夹爪和通信相关状态
     */
    private static void TestSensorGripperStates(Robot robot) {
        int ret = 0;

        List<RobotState> state = new ArrayList<>();
        state.add(RobotState.FtSensorRawData);
        state.add(RobotState.FtSensorData);
        state.add(RobotState.FtSensorActive);
        state.add(RobotState.GripperMotiondone);
        state.add(RobotState.GripperFaultId);
        state.add(RobotState.GripperFault);
        state.add(RobotState.GripperActive);
        state.add(RobotState.GripperPosition);
        state.add(RobotState.GripperSpeed);
        state.add(RobotState.GripperCurrent);
        state.add(RobotState.GripperTemp);
        state.add(RobotState.GripperVoltage);
        state.add(RobotState.GripperRotNum);
        state.add(RobotState.GripperRotSpeed);
        state.add(RobotState.GripperRotTorque);
        state.add(RobotState.SmartToolState);
        state.add(RobotState.ModbusMasterConnect);
        state.add(RobotState.ModbusSlaveConnect);

        ret = robot.SetRobotRealtimeStateConfig(state, 100);
        System.out.println("SetRobotRealtimeStateConfig rtn : " + ret);

        int rtn = robot.RPC("192.168.58.2");
        if (rtn == 0) {
            System.out.println("rpc连接 success");
        } else {
            System.out.println("rpc连接 fail");
            return;
        }

        // 等待CNDE连接建立
        System.out.println("等待CNDE连接建立...");
        while (robot.CNDEGetStateData() == null) {
            robot.Sleep(100);
        }
        System.out.println("CNDE连接已建立，开始接收数据...");

        while (true) {
            robot.Sleep(1000);
            ROBOT_STATE_PKG pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("状态数据为空，CNDE连接断开，等待重连");
                continue;
            }

            // --- 力传感器 ---
            System.out.println("--- 力传感器 ---");
            System.out.println("ftSensorActive (力传感器激活状态): " + pkg.ft_sensor_active);
            printArray("ftSensorRawData (力传感器原始数据)", pkg.ft_sensor_raw_data);
            printArray("ftSensorData (力传感器数据)", pkg.ft_sensor_data);

            // --- 夹爪状态 ---
//            System.out.println("--- 夹爪状态 ---");
//            System.out.println("gripperMotiondone (夹爪运动完成): " + pkg.gripper_motiondone);
//            System.out.println("gripperFaultId (夹爪故障ID): " + pkg.gripper_fault_id);
//            System.out.println("gripperFault (夹爪故障): " + pkg.gripper_fault);
//            System.out.println("gripperActive (夹爪激活): " + pkg.gripper_active);
//            System.out.println("gripperPosition (夹爪位置): " + pkg.gripper_position);
//            System.out.println("gripperSpeed (夹爪速度): " + pkg.gripper_speed);
//            System.out.println("gripperCurrent (夹爪电流): " + pkg.gripper_current);
//            System.out.println("gripperTemp (夹爪温度): " + pkg.gripper_temp);
//            System.out.println("gripperVoltage (夹爪电压): " + pkg.gripper_voltage);
//
//            // --- 旋转夹爪 ---
//            System.out.println("--- 旋转夹爪 ---");
//            System.out.println("gripperRotNum (旋转夹爪圈数): " + pkg.gripperRotNum);
//            System.out.println("gripperRotSpeed (旋转夹爪速度): " + pkg.gripperRotSpeed);
//            System.out.println("gripperRotTorque (旋转夹爪力矩): " + pkg.gripperRotTorque);
//
//            // --- 智能工具与Modbus ---
//            System.out.println("--- smarttool与Modbus ---");
//            System.out.println("smartToolState: " + pkg.smartToolState);
//            System.out.println("--- Modbus ---");
//            System.out.println("modbusMasterConnect (Modbus主站连接): " + pkg.modbusMasterConnect);
//            System.out.println("modbusSlaveConnect (Modbus从站连接): " + pkg.modbusSlaveConnect);

            System.out.println("------------------------------------------------------------------------------------------");
        }
    }

    /**
     * 测试扩展轴和扩展IO状态 - 配置并打印辅助轴、扩展轴状态和扩展IO状态
     */
    private static void TestExtAxisStates(Robot robot) {
        int ret = 0;

        List<RobotState> state = new ArrayList<>();
        state.add(RobotState.AuxState);
        state.add(RobotState.ExtAxisStatus);
        state.add(RobotState.ExtDIState);
        state.add(RobotState.ExtDOState);
        state.add(RobotState.ExtAIState);
        state.add(RobotState.ExtAOState);

        ret = robot.SetRobotRealtimeStateConfig(state, 100);
        System.out.println("SetRobotRealtimeStateConfig rtn : " + ret);

        int rtn = robot.RPC("192.168.58.2");
        if (rtn == 0) {
            System.out.println("rpc连接 success");
        } else {
            System.out.println("rpc连接 fail");
            return;
        }

        // 等待CNDE连接建立
        System.out.println("等待CNDE连接建立...");
        while (robot.CNDEGetStateData() == null) {
            robot.Sleep(100);
        }
        System.out.println("CNDE连接已建立，开始接收数据...");

        while (true) {
            robot.Sleep(1000);
            ROBOT_STATE_PKG pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("状态数据为空，CNDE连接断开，等待重连");
                continue;
            }

            // --- 辅助轴状态 ---
            System.out.println("--- 辅助轴状态 (AuxState) ---");
            if (pkg.aux_state != null) {
                System.out.println("servoId (伺服ID): " + pkg.aux_state.servoId);
                System.out.println("servoErrCode (伺服错误码): " + pkg.aux_state.servoErrCode);
                System.out.println("servoState (伺服状态): " + pkg.aux_state.servoState);
                System.out.println("servoPos (伺服位置): " + pkg.aux_state.servoPos);
                System.out.println("servoVel (伺服速度): " + pkg.aux_state.servoVel);
                System.out.println("servoTorque (伺服力矩): " + pkg.aux_state.servoTorque);
            } else {
                System.out.println("aux_state: null");
            }

            // --- 扩展轴状态 ---
            System.out.println("--- 扩展轴状态 (ExtAxisStatus[4]) ---");
            if (pkg.extAxisStatus != null) {
                for (int i = 0; i < pkg.extAxisStatus.length; i++) {
                    EXT_AXIS_STATUS axis = pkg.extAxisStatus[i];
                    System.out.printf("Axis[%d]: pos=%.3f, vel=%.3f, errorCode=%d, ready=%d, inPos=%d, alarm=%d%n",
                        i, axis.pos, axis.vel, axis.errorCode, axis.ready, axis.inPos, axis.alarm);
                    System.out.printf("         flerr=%d, nlimit=%d, pLimit=%d, mdbsOffLine=%d, mdbsTimeout=%d, homingStatus=%d%n",
                        axis.flerr, axis.nlimit, axis.pLimit, axis.mdbsOffLine, axis.mdbsTimeout, axis.homingStatus);
                }
            } else {
                System.out.println("extAxisStatus: null");
            }

            // --- 扩展IO状态 ---
            System.out.println("--- 扩展IO状态 ---");
            System.out.print("extDIState (扩展数字输入): ");
            for (int i = 0; i < pkg.extDIState.length; i++) {
                System.out.print(pkg.extDIState[i] + " ");
            }
            System.out.println();

            System.out.print("extDOState (扩展数字输出): ");
            for (int i = 0; i < pkg.extDOState.length; i++) {
                System.out.print(pkg.extDOState[i] + " ");
            }
            System.out.println();

            System.out.print("extAIState (扩展模拟输入): ");
            for (int i = 0; i < pkg.extAIState.length; i++) {
                System.out.print(pkg.extAIState[i] + " ");
            }
            System.out.println();

            System.out.print("extAOState (扩展模拟输出): ");
            for (int i = 0; i < pkg.extAOState.length; i++) {
                System.out.print(pkg.extAOState[i] + " ");
            }
            System.out.println();

            System.out.println("------------------------------------------------------------------------------------------");
        }
    }

    /**
     * 正方形运动测试 - 使用MoveL执行正方形轨迹
     */
    private static int MoveRectangleMove(Robot robot)
    {
        List<RobotState> states = new ArrayList<>();
        states.add(RobotState.JointCurPos);
        states.add(RobotState.ToolCurPos);
        states.add(RobotState.MotionDone);
        int periodMs = 20;
        int ret = robot.SetRobotRealtimeStateConfig(states, periodMs);
        System.out.println("配置状态结果: " + ret);
        ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();
        int rtn = 0;
        robot.SetReconnectParam(true, 30000, 500);
        rtn = robot.RPC("192.168.58.2");
        if (rtn != 0)
        {
            System.out.printf("robot RPC failed%n");
            return 0;
        }

        robot.Sleep(3000);
        int tool = 0;
        int user = 0;
        float vel = 100.0f;
        float acc = 100.0f;
        float ovl = 100.0f;
        float blendR = -1.0f;   // 运动到位（阻塞）
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);
        int search = 0;
        int offset_flag = 0;
        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
        float oacc = 100.0f;
        int motionDone = 0;
        double step = 100.0;  // 边长 100mm
        int cycle = 1;
        while (true)
        {
            System.out.printf("%n========== 第 %d 次正方形运动 ==========%n", cycle);

            // 获取当前 TCP 位姿作为起点
            pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("获取实时状态失败，等待重连...");
                robot.Sleep(100);
                continue;
            }

            double startX = pkg.tl_cur_pos[0];
            double startY = pkg.tl_cur_pos[1];
            double startZ = pkg.tl_cur_pos[2];
            double startRX = pkg.tl_cur_pos[3];
            double startRY = pkg.tl_cur_pos[4];
            double startRZ = pkg.tl_cur_pos[5];

            // 定义四个目标点（相对起点）
            DescPose target1 = new DescPose(startX + step, startY, startZ, startRX, startRY, startRZ);
            DescPose target2 = new DescPose(startX + step, startY + step, startZ, startRX, startRY, startRZ);
            DescPose target3 = new DescPose(startX, startY + step, startZ, startRX, startRY, startRZ);
            DescPose target4 = new DescPose(startX, startY, startZ, startRX, startRY, startRZ);

            JointPos jpos = new JointPos(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

            JointPos jposRef1 = new JointPos(pkg.jt_cur_pos[0], pkg.jt_cur_pos[1], pkg.jt_cur_pos[2], pkg.jt_cur_pos[3], pkg.jt_cur_pos[4], pkg.jt_cur_pos[5]);
            JointPos refResult1 = new JointPos();
            robot.GetInverseKinRef(0, target1, jposRef1, refResult1);
            rtn = robot.MoveL(refResult1, target1, tool, user, vel, acc, ovl, blendR, epos, search, offset_flag, offset_pos, 0, 0);
            System.out.printf("movel errcode:%d%n", rtn);
            robot.Sleep(500);
            motionDone = 0;
            while (motionDone == 0)
            {
                pkg = robot.CNDEGetStateData();
                motionDone = pkg.motion_done;
                robot.Sleep(100);
            }
            System.out.println("movel 1 motion done\n");

            pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("获取实时状态失败，跳过本次循环");
                continue;
            }
            JointPos jposRef2 = new JointPos(pkg.jt_cur_pos[0], pkg.jt_cur_pos[1], pkg.jt_cur_pos[2], pkg.jt_cur_pos[3], pkg.jt_cur_pos[4], pkg.jt_cur_pos[5]);
            JointPos refResult2 = new JointPos();
            robot.GetInverseKinRef(0, target2, jposRef2, refResult2);
            rtn = robot.MoveL(refResult2, target2, tool, user, vel, acc, ovl, blendR, epos, search, offset_flag, offset_pos, 0, 0);
            System.out.printf("movel errcode:%d%n", rtn);
            robot.Sleep(500);
            motionDone = 0;
            while (motionDone == 0)
            {
                pkg = robot.CNDEGetStateData();
                motionDone = pkg.motion_done;
                robot.Sleep(100);
            }
            System.out.println("movel 2 motion done\n");

            pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("获取实时状态失败，跳过本次循环");
                continue;
            }
            JointPos jposRef3 = new JointPos(pkg.jt_cur_pos[0], pkg.jt_cur_pos[1], pkg.jt_cur_pos[2], pkg.jt_cur_pos[3], pkg.jt_cur_pos[4], pkg.jt_cur_pos[5]);
            JointPos refResult3 = new JointPos();
            robot.GetInverseKinRef(0, target3, jposRef3, refResult3);
            rtn = robot.MoveL(refResult3, target3, tool, user, vel, acc, ovl, blendR, epos, search, offset_flag, offset_pos, 0, 0);
            System.out.printf("movel errcode:%d%n", rtn);
            robot.Sleep(500);
            motionDone = 0;
            while (motionDone == 0)
            {
                pkg = robot.CNDEGetStateData();
                motionDone = pkg.motion_done;
                robot.Sleep(100);
            }
            System.out.println("movel 3 motion done\n");

            pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("获取实时状态失败，跳过本次循环");
                continue;
            }
            JointPos jposRef4 = new JointPos(pkg.jt_cur_pos[0], pkg.jt_cur_pos[1], pkg.jt_cur_pos[2], pkg.jt_cur_pos[3], pkg.jt_cur_pos[4], pkg.jt_cur_pos[5]);
            JointPos refResult4 = new JointPos();
            robot.GetInverseKinRef(0, target4, jposRef4, refResult4);
            rtn = robot.MoveL(refResult4, target4, tool, user, vel, acc, ovl, blendR, epos, search, offset_flag, offset_pos, 0, 0);
            System.out.printf("movel errcode:%d%n", rtn);
            robot.Sleep(500);
            motionDone = 0;
            while (motionDone == 0)
            {
                pkg = robot.CNDEGetStateData();
                motionDone = pkg.motion_done;
                robot.Sleep(100);
            }
            System.out.println("movel 4 motion done\n");
            System.out.printf("第 %d 次正方形运动完成%n", cycle);
            cycle++;
        }
    }


    /**
     * 测试通用轴通信数据 - 配置AxleGenComData状态并解析倍益康艾灸头协议
     */
    private static int TestAxleGenComData(Robot robot) {
        int ret = 0;
        int frameCount = 0;

        // 配置AxleGenComData状态
        List<RobotState> state = new ArrayList<>();
        state.add(RobotState.AxleGenComData);
        ret = robot.SetRobotRealtimeStateConfig(state, 100);
        System.out.println("SetRobotRealtimeStateConfig rtn : " + ret);

        // RPC连接
        int rtn = robot.RPC("192.168.58.3");
        if (rtn == 0) {
            System.out.println("rpc连接 success");
        } else {
            System.out.println("rpc连接 fail");
            return -1;
        }

        // 等待CNDE连接建立
        System.out.println("等待CNDE连接建立...");
        while (robot.CNDEGetStateData() == null) {
            robot.Sleep(100);
        }
        System.out.println("CNDE连接已建立，开始接收数据...");

        // 定义发送数据数组
        int[] version = new int[]{0x01, 0x00, 0x00, 0x00, 0x01};  // 读取版本号命令
        int[] stateCmd = new int[]{0x01, 0x00, 0x00, 0x00, 0x02}; // 读取状态命令
        int[] rcvdata = new int[10];

        while (true) {
            robot.Sleep(1000);
            ROBOT_STATE_PKG pkg = robot.CNDEGetStateData();
            if (pkg == null) {
                System.out.println("状态数据为空，CNDE连接断开，等待重连");
                continue;
            }
//
//            frameCount++;
//            System.out.printf("\n========== 帧 %d ==========%n", frameCount);
//
//            // 读取版本号
//            ret = robot.SndRcvAxleGenComCmdData(5, version, 10, rcvdata);
//            if (ret == 0) {
//                System.out.printf(" hard version : %d, hard code:%d, soft version:%d %d, soft code:%d%n",
//                        rcvdata[4], rcvdata[5], rcvdata[6], rcvdata[7], rcvdata[8]);
//            } else {
//                System.out.println("SndRcvAxleGenComCmdData version fail: " + ret);
//                continue;
//            }
//            robot.Sleep(1000);
//
//            // 读取艾灸头在位状态
//            ret = robot.SndRcvAxleGenComCmdData(6, stateCmd, 6, rcvdata);
//            if (ret == 0) {
//                System.out.printf(" state : %d%n", rcvdata[4]);
//            }
//            robot.Sleep(1000);

            // 解析axleGenComData数据
            int errorcode = pkg.axleGenComData[0];
            int datalen = pkg.axleGenComData[1];

            // 过滤异常包
            if ((errorcode != 0) || (datalen == 0) ||
                (pkg.axleGenComData[2] != (byte)0xAB) ||
                (pkg.axleGenComData[3] != (byte)0xBA)) {
                System.out.printf("################################rcv data is error, errorcode:%d%n", pkg.axleGenComData[0]);
                // 打印16进制数据
                StringBuilder hexString = new StringBuilder();
                for (int i = 0; i < pkg.axleGenComData.length; i++) {
                    hexString.append(String.format("%02X", pkg.axleGenComData[i] & 0xFF));
                    if (i < pkg.axleGenComData.length - 1) {
                        hexString.append("-");
                    }
                }
                System.out.println(hexString.toString());
                continue;
            }

            // 按照倍益康艾灸头协议进行组包
            int curTem = pkg.axleGenComData[6] & 0xFF;
            int targetTem = pkg.axleGenComData[7] & 0xFF;
            int genData1 = ((pkg.axleGenComData[8] & 0xFF) << 8) | (pkg.axleGenComData[9] & 0xFF);
            int genData2 = ((pkg.axleGenComData[10] & 0xFF) << 8) | (pkg.axleGenComData[11] & 0xFF);
            int genData3 = ((pkg.axleGenComData[12] & 0xFF) << 8) | (pkg.axleGenComData[13] & 0xFF);
            int genData4 = ((pkg.axleGenComData[14] & 0xFF) << 8) | (pkg.axleGenComData[15] & 0xFF);
            int genData5 = ((pkg.axleGenComData[16] & 0xFF) << 8) | (pkg.axleGenComData[17] & 0xFF);
            int genData6 = ((pkg.axleGenComData[18] & 0xFF) << 8) | (pkg.axleGenComData[19] & 0xFF);

            System.out.printf("the data is errorcode %d; datalen %d curTem %d; targetTem %d genData1 %d genData2 %d genData3 %d genData4 %d genData5 %d genData6 %d%n",
                    errorcode, datalen, curTem, targetTem, genData1, genData2, genData3, genData4, genData5, genData6);
        }
    }

    private static int testCndeServoJ(Robot robot) {

        ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();
        int rtn = 0;
        robot.LoggerInit(FrLogType.DIRECT, FrLogLevel.INFO, "D://log", 10, 10);
        robot.SetReconnectParam(true, 30000, 500);

        List<RobotState> states = Arrays.asList(
            RobotState.JointCurPos,
            RobotState.MotionDone,
            RobotState.McQueueLen,
            RobotState.LastServoTarget,
            RobotState.ServoJCmdNum
        );
        rtn = robot.SetRobotRealtimeStateConfig(states, 20);

        rtn = robot.RPC("192.168.58.2");
        if (rtn != 0)
        {
            System.out.printf("robot RPC failed %d%n", rtn);
            return 0;
        }

        JointPos j = new JointPos(0, 0, 0, 0, 0, 0);
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);

        double vel = 0.0;
        double acc = 0.0;
        double cmdT = 0.008;
        double filterT = 0.0;
        double gain = 0.0;
        int flag = 0;
        int count = 500;
        double dt = 0.1;
        int cmdID = 0;
        int comType = 0;  // RPC方式

        // 等待CNDE连接建立
        System.out.println("等待CNDE连接建立...");
        while (robot.CNDEGetStateData() == null) {
            robot.Sleep(100);
        }
        System.out.println("CNDE连接已建立");
        robot.Sleep(2000);
        int ret = robot.GetActualJointPosDegree(j);
        if (ret == 0)
        {
            cmdID += 1;
            robot.ServoMoveStart(comType);
            while (count > 0)
            {
                robot.ServoJ(j, epos, acc, vel, cmdT, filterT, gain, cmdID, comType);
                j.J5 += dt;
                count--;

                // 通过CNDE获取实时状态
                pkg = robot.CNDEGetStateData();
                if (pkg != null) {
                    System.out.printf("Servoj Count %d;  last pos is %f %f %f %f %f %f%n", pkg.servoJCmdNum,
                        pkg.lastServoTarget[0], pkg.lastServoTarget[1], pkg.lastServoTarget[2],
                        pkg.lastServoTarget[3], pkg.lastServoTarget[4], pkg.lastServoTarget[5]);

                    System.out.printf("motion queue len %d. motion done %d%n", pkg.mc_queue_len, pkg.motion_done);
                }

                if (count < 50)
                {
                    robot.MotionQueueClear();
                    pkg = robot.CNDEGetStateData();
                    if (pkg != null) {
                        System.out.printf("After queue clear, Servoj Count %d;  last pos is %f %f %f %f %f %f%n", pkg.servoJCmdNum,
                            pkg.lastServoTarget[0], pkg.lastServoTarget[1], pkg.lastServoTarget[2],
                            pkg.lastServoTarget[3], pkg.lastServoTarget[4], pkg.lastServoTarget[5]);
                    }
                    break;
                }

                robot.WaitMs((int)(1000 * cmdT));
            }
            robot.ServoMoveEnd(comType);
        }
        else
        {
            System.out.printf("GetActualJointPosDegree errcode:%d%n", ret);
        }

        robot.Sleep(1000);

        robot.CloseRPC();

        robot.Sleep(1000);
        return 0;
    }

    private static int TestSetTrajectoryJSpeed(Robot robot) {
        ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();
        int rtn;
        while(true) {
//        robot.SetReconnectParam(true, 30000, 500);
            rtn = robot.TrajectoryJUpLoad("D://zUP/horse.txt");
            System.out.printf("Upload TrajectoryJ A %d%n", rtn);
            String trajFileName = "horse.txt";
            rtn = robot.LoadTrajectoryJ(trajFileName, 100, 1);
            System.out.printf("LoadTrajectoryJ %s, rtn is: %d%n", trajFileName, rtn);
            DescPose trajStartPose = new DescPose();
            rtn = robot.GetTrajectoryStartPose(trajFileName, trajStartPose);
            System.out.printf("GetTrajectoryStartPose is: %d%n", rtn);
            System.out.printf("desc_pos:%f,%f,%f,%f,%f,%f%n", trajStartPose.tran.x, trajStartPose.tran.y, trajStartPose.tran.z, trajStartPose.rpy.rx, trajStartPose.rpy.ry, trajStartPose.rpy.rz);
            robot.Sleep(1000);
            robot.SetSpeed(50);
            robot.MoveCart(trajStartPose, 0, 0, 100, 100, 100, -1, -1);
            rtn = robot.GetTrajectoryPointNum(0);
            pkg = robot.GetRobotRealTimeState();
            int trajNum = pkg.trajectory_pnum;
            System.out.printf("GetTrajectoryPointNum rtn is: %d, traj num is: %d%n", rtn, trajNum);

            rtn = robot.MoveTrajectoryJ();
            System.out.printf("MoveTrajectoryJ rtn is: %d%n", rtn);

            robot.Sleep(1000);

            pkg = robot.GetRobotRealTimeState();
            int trajspeedMode = 1;

            if (pkg.motion_done == 0) {
                pkg = robot.GetRobotRealTimeState();

                rtn = robot.SetTrajectoryJSpeed(10.0, trajspeedMode);
                System.out.printf("SetTrajectoryJSpeed is: %d%n", rtn);

                robot.Sleep(3000);

                rtn = robot.SetTrajectoryJSpeed(80.0, trajspeedMode);
                System.out.printf("SetTrajectoryJSpeed is: %d%n", rtn);

                robot.Sleep(3000);

                rtn = robot.SetTrajectoryJSpeed(50.0, trajspeedMode);
                System.out.printf("SetTrajectoryJSpeed is: %d%n", rtn);

                robot.Sleep(3000);
            }

        }

//        return 0;
    }

    /**
     * @brief 测试实时状态配置接口
     * 演示 SetRobotRealtimeStateConfig、AddRobotRealtimeState、
     * SetRobotRealtimeStatePeriod、GetRobotRealtimeStateConfig 的使用
     */
    public static void TestRealtimeStateConfig(Robot robot)
    {
        System.out.println("========== TestRealtimeStateConfig Start ==========");

        // 1. 创建初始状态列表
        List<RobotState> stateList1 = new ArrayList<>();
        stateList1.add(RobotState.ProgramState);
        stateList1.add(RobotState.RobotState);
        stateList1.add(RobotState.JointCurPos);
        stateList1.add(RobotState.ToolCurPos);

        // 2. 第一次调用 SetRobotRealtimeStateConfig 配置状态和周期
        int period1 = 100;  // 100ms周期
        int rtn = robot.SetRobotRealtimeStateConfig(stateList1, period1);
        System.out.printf("1. SetRobotRealtimeStateConfig (initial list, period=%d) rtn: %d%n", period1, rtn);

        if (rtn == 0) {
            // 3. 添加额外状态
            rtn = robot.AddRobotRealtimeState(RobotState.RobotTime);
            System.out.printf("2. AddRobotRealtimeState (RobotTime) rtn: %d%n", rtn);

            // 4. 再次调用 SetRobotRealtimeStateConfig 重新配置（不同状态列表）
            List<RobotState> stateList2 = new ArrayList<>();
            stateList2.add(RobotState.ProgramState);
            stateList2.add(RobotState.RobotState);
            stateList2.add(RobotState.MainCode);
            stateList2.add(RobotState.SubCode);
            stateList2.add(RobotState.JointCurPos);
            stateList2.add(RobotState.ToolCurPos);
            stateList2.add(RobotState.ActualJointTorque);

            int period2 = 50;  // 50ms周期
            rtn = robot.SetRobotRealtimeStateConfig(stateList2, period2);
            System.out.printf("3. SetRobotRealtimeStateConfig (updated list, period=%d) rtn: %d%n", period2, rtn);

            // 5. 修改周期
            int newPeriod = 80;  // 80ms周期
            rtn = robot.SetRobotRealtimeStatePeriod(newPeriod);
            System.out.printf("4. SetRobotRealtimeStatePeriod (period=%d) rtn: %d%n", newPeriod, rtn);

            // 6. 获取当前配置并打印
            Robot.StateConfigResult configResult = robot.GetRobotRealtimeStateConfig();
            System.out.println("5. GetRobotRealtimeStateConfig result:");
            System.out.printf("   - Period: %d ms%n", configResult.period);
            System.out.println("   - Configured States:");
            for (int i = 0; i < configResult.stateList.size(); i++) {
                System.out.printf("     [%d] %s%n", i, configResult.stateList.get(i));
            }

            rtn = robot.RPC("192.168.58.2");
            if (rtn == 0) {
                System.out.println("rpc连接 success");
            } else {
                System.out.println("rpc连接 fail");
                return;
            }
            // 等待CNDE连接建立
            System.out.println("等待CNDE连接建立...");
            while (robot.CNDEGetStateData() == null) {
                robot.Sleep(100);
            }
            System.out.println("CNDE连接已建立，开始接收数据...");


            // 7. 循环读取实时状态验证配置是否生效
            System.out.println("6. Reading real-time states for 5 seconds...");
            while(true) {
                robot.Sleep(1000);
                // 通过 CNDE 获取状态数据
                ROBOT_STATE_PKG pkg = robot.CNDEGetStateData();
                if (pkg == null) {
                    System.out.println("状态数据为空，CNDE连接断开，等待重连");
                    continue;  // 连接断开时继续循环，等待重连
                }
                System.out.println("\n--- 机器人时间 ---");
                if (pkg.robotTime != null) {
                    System.out.println("robotTime: " + pkg.robotTime.year + "-" + pkg.robotTime.month + "-" + pkg.robotTime.day +
                            " " + pkg.robotTime.hour + ":" + pkg.robotTime.minute + ":" + pkg.robotTime.second +
                            "." + pkg.robotTime.millisecond);
                }

                System.out.println("   --- 状态信息 ---");
                System.out.printf("   program_state: %d%n", pkg.program_state);
                System.out.printf("   robot_state: %d%n", pkg.robot_state);
                System.out.printf("   main_code: %d%n", pkg.main_code);
                System.out.printf("   sub_code: %d%n", pkg.sub_code);
                System.out.println("   --- 关节位置 (actual_joint_pos) ---");
                System.out.printf("   jt_cur_pos[0-2]: %.2f, %.2f, %.2f%n",
                    pkg.jt_cur_pos[0], pkg.jt_cur_pos[1], pkg.jt_cur_pos[2]);
                System.out.printf("   jt_cur_pos[3-5]: %.2f, %.2f, %.2f%n",
                    pkg.jt_cur_pos[3], pkg.jt_cur_pos[4], pkg.jt_cur_pos[5]);
                System.out.println("   --- TCP位置 (actual_TCP_pos) ---");
                System.out.printf("   tl_cur_pos[0-2]: %.2f, %.2f, %.2f%n",
                    pkg.tl_cur_pos[0], pkg.tl_cur_pos[1], pkg.tl_cur_pos[2]);
                System.out.printf("   tl_cur_pos[3-5]: %.2f, %.2f, %.2f%n",
                    pkg.tl_cur_pos[3], pkg.tl_cur_pos[4], pkg.tl_cur_pos[5]);
                System.out.println("   --- 关节力矩 (actual_joint_torque) ---");
                System.out.printf("   jt_cur_tor[0-2]: %.2f, %.2f, %.2f%n",
                    pkg.jt_cur_tor[0], pkg.jt_cur_tor[1], pkg.jt_cur_tor[2]);
                System.out.printf("   jt_cur_tor[3-5]: %.2f, %.2f, %.2f%n",
                    pkg.jt_cur_tor[3], pkg.jt_cur_tor[4], pkg.jt_cur_tor[5]);
                robot.Sleep(500);
            }
        } else {
            System.out.printf("SetRobotRealtimeStateConfig failed with error: %d%n", rtn);
        }

        System.out.println("========== TestRealtimeStateConfig End ==========");
    }

    public static int ServoMITtest(Robot robot)
    {
            robot.udpCmdClient.SetUDPCmdRpyCallback((srcType, count, cmdID, dataLen, content) -> {
            System.out.println("\n[Received UDP reply from robot]");
            System.out.println("srcType: " + srcType);
            System.out.println("count: " + count);
            System.out.println("cmdID: " + cmdID);
            System.out.println("dataLen: " + dataLen);
            System.out.println("content: " + content);
            return 0;
        });
        while (true)
        {
            robot.ResetAllError();
            robot.Sleep(500);

            double[] posGain = new double[] { 0, 0, 0, 0, 0, 0 };
            double[] desPos = new double[] { 0, 0, 0, 0, 0, 0 };
            double[] velGain = new double[] { 0, 0, 0, 0, 0, 0 };
            double[] desVel = new double[] { 0, 0, 0, 0, 0, 0 };

            List<Number> joint_toq=new ArrayList<>();
            joint_toq=robot.GetJointTorques(1);
            double[] torques=new double[]{(double)joint_toq.get(1),(double)joint_toq.get(2),(double)joint_toq.get(3),(double)joint_toq.get(4),(double)joint_toq.get(5),(double)joint_toq.get(6)};
            System.out.println("111111");

            robot.ServoMITStart(1);
            System.out.println("ServoMITStart");

            ROBOT_STATE_PKG pkg = robot.GetRobotRealTimeState();
            robot.DragTeachSwitch(1);
            System.out.println("DragTeachSwitch");

            double intev = 0.008;
            int error = 0;
            while (true)
            {
                torques[5] = 0.03;
                System.out.println("ServoMIT call");
                error = robot.ServoMIT(posGain, desPos, velGain, desVel, torques, intev, 0);

                System.out.println("ServoMIT111111 rtn is " + error);
                robot.Sleep(10);

                pkg = robot.GetRobotRealTimeState();
                System.out.println("pkg.jt_cur_pos[5]:" + pkg.jt_cur_pos[5]);
                if (pkg.jt_cur_pos[5] > 30)
                {
                    break;
                }
            }

            while (true)
            {
                torques[5] = -0.03;
                error = robot.ServoMIT(posGain, desPos, velGain, desVel, torques, intev, 0);

                System.out.println("ServoJT222222 rtn is " + error);
                robot.Sleep(10);

                pkg = robot.GetRobotRealTimeState();
                System.out.println("pkg.jt_cur_pos[5]:" + pkg.jt_cur_pos[5]);
                if (pkg.jt_cur_pos[5] < 0)
                {
                    break;
                }
            }

            robot.DragTeachSwitch(0);
            error = robot.ServoMITEnd(1);
        }
        // return 0;
    }

    public static int ServoJVtest(Robot robot)
    {
        double[] joint_vel = new double[] { 10, 0, 0, 0, 0, 0 };
        double[] exis_vel = new double[] { 0, 0, 0, 0 };
        double acc = 0.0;
        double vel = 0.0;
        double cmdT = 0.008;
        double filterT = 0.0;
        double gain = 0.0;
        int cnt = 0;
        while (cnt < 200)
        {
            int error = robot.ServoJV(joint_vel, exis_vel, acc, vel, cmdT, filterT, gain);
            System.out.println("MAIN ServoJV rtn is " + error);
//            robot.Sleep(10);
            cnt++;
        }

        return 0;
    }

    public static int testLsaerWeld(Robot robot) {
        int rtn = -1;
        // 加载UDP扩展轴驱动
        rtn = robot.ExtDevLoadUDPDriver();
        if (rtn != 0) {
            System.out.println("Failed to load UDP driver, error code: " + rtn);
        }
        robot.Sleep(1000);

        // 设置激光焊接参数: io_type=1, num=3, scanSpeed=2000, scanWidth=3, peakPower=1500, dutyCycle=100, freq=1000
        rtn = robot.SetLaserWeldingParam(1, 3, 2000, 3, 1500, 100, 1000);
        if (rtn != 0) {
            System.out.println("SetLaserWeldingParam failed, error code: " + rtn);
        } else {
            System.out.println("SetLaserWeldingParam success");
        }

        // 设置启动的DO端口号
        rtn = robot.SetLaserWeldingStartExtDoNum(1);
        if (rtn != 0) {
            System.out.println("SetLaserWeldingStartExtDoNum failed, error code: " + rtn);
        }

        // 设置为模式0（示教模式）
        rtn = robot.Mode(0);
        if (rtn != 0) {
            System.out.println("Set mode 0 failed, error code: " + rtn);
        }
        robot.Sleep(1000);

        // 创建点位对象
        DescPose desc_pos1 = new DescPose(-303.721, -206.960, 297.105, 152.209, 19.857, 109.166);
        DescPose desc_pos2 = new DescPose(-301.575, -254.888, 284.786, 155.919, 26.946, 111.629);
        DescPose desc_safe = new DescPose(-344.386, -280.830, 435.073, 173.835, 15.333, 124.931);

        JointPos jointPos1 = new JointPos(9.827, -99.740, 120.088, -78.900, -77.241, -17.904);
        JointPos jointPos2 = new JointPos(15.251, -96.456, 120.138, -84.664, -68.542, -17.843);
        JointPos jointSafe = new JointPos(19.142, -98.078, 101.493, -83.078, -77.070, -17.794);

        ExaxisPos exaxis = new ExaxisPos(0.0, 0.0, 0.0, 0.0);
        DescPose offset = new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        // 移动到第一个焊接点
        int error = robot.MoveL(desc_pos1, 0, 0, 100, 100, 100, -1, 0, exaxis, 0, 0, offset, -1, 0,0,0);
        System.out.println("MoveL to pos1 return: " + error);

        // 开启激光（出光）
        rtn = robot.SetLaserWeldingStartEnd(1, 1, 10000);
        if (rtn != 0) {
            System.out.println("SetLaserWeldingStartEnd (start) failed, error code: " + rtn);
        } else {
            System.out.println("Laser started");
        }

        // 移动到第二个焊接点（焊接过程中）
        rtn = robot.MoveL(desc_pos2, 0, 0, 30, 100, 100, -1, 0, exaxis, 0, 0, offset, -1, 0,0, 0);
        System.out.println("MoveL to pos2 return: " + rtn);

        // 关闭激光（收光）
        rtn = robot.SetLaserWeldingStartEnd(1, 0, 10000);
        if (rtn != 0) {
            System.out.println("SetLaserWeldingStartEnd (stop) failed, error code: " + rtn);
        } else {
            System.out.println("Laser stopped");
        }
        robot.Sleep(500);
        // 移动到安全点
        rtn = robot.MoveL(desc_safe, 0, 0, 100, 100, 100, -1, 0, exaxis, 0, 0, offset, -1, 0,0,0);
        System.out.println("MoveL to safe_pos return: " + rtn);

        // 设置为模式1（远程模式）
        rtn = robot.Mode(1);
        if (rtn != 0) {
            System.out.println("Set mode 1 failed, error code: " + rtn);
        }
        robot.Sleep(1000);

        // 关闭连接
        robot.CloseRPC();
        robot.Sleep(1000);

        System.out.println("Test completed");

        return 0;
    }
    public static int TestUDPAxis(Robot robot) {
        int rtn = -1;

        // 设置UDP通信参数
        UDPComParam param = new UDPComParam("192.168.58.88", 2021, 2, 100, 3, 200, 1, 100, 5, 1);
        rtn = robot.ExtDevSetUDPComParam(param);
        System.out.println("ExtDevSetUDPComParam rtn is " + rtn);

        // 获取UDP通信参数
        UDPComParam getParam = new UDPComParam();
        rtn = robot.ExtDevGetUDPComParam(getParam);
        String paramStr = "\nip " + getParam.ip + "\nport " + getParam.port + "\nperiod " + getParam.period +
                "\nlossPkgTime " + getParam.lossPkgTime + "\nlossPkgNum " + getParam.lossPkgNum +
                "\ndisconnectTime " + getParam.disconnectTime + "\nreconnectEnable " + getParam.reconnectEnable +
                "\nreconnectPeriod " + getParam.reconnectPeriod + "\nreconnectNum " + getParam.reconnectNum +
                "\nselfConnect " + getParam.selfConnect;
        System.out.println("ExtDevGetUDPComParam rtn is " + rtn + paramStr);

        // 加载UDP驱动
        robot.ExtDevLoadUDPDriver();

        // 设置扩展轴命令完成时间
        rtn = robot.SetExAxisCmdDoneTime(5000.0);
        System.out.println("SetExAxisCmdDoneTime rtn is " + rtn);

        // 使能扩展轴
        rtn = robot.ExtAxisServoOn(1, 1);
        System.out.println("ExtAxisServoOn axis id 1 rtn is " + rtn);
        rtn = robot.ExtAxisServoOn(2, 1);
        System.out.println("ExtAxisServoOn axis id 2 rtn is " + rtn);
        robot.Sleep(2000);

        // 设置回零
        robot.ExtAxisSetHoming(1, 0, 10, 2);
        robot.Sleep(2000);
        rtn = robot.ExtAxisSetHoming(2, 0, 10, 2);
        System.out.println("ExtAxisSetHoming rtn is " + rtn);

        robot.Sleep(4000);

        // 设置机器人位置到轴
        rtn = robot.SetRobotPosToAxis(1);
        System.out.println("SetRobotPosToAxis rtn is " + rtn);

        // 设置轴DH参数配置
        rtn = robot.SetAxisDHParaConfig(10, 20, 0, 0, 0, 0, 0, 0, 0);
        System.out.println("SetAxisDHParaConfig rtn is " + rtn);

        // 配置扩展轴参数
        rtn = robot.ExtAxisParamConfig(1, 1, 1, 1000, -1000, 1000, 1000, 1.905, 262144, 200, 1, 0, 0);
        System.out.println("ExtAxisParamConfig axis 1 rtn is " + rtn);

        // 获取扩展轴参数
        Object[] params1 = new Object[12];
        rtn = robot.ExtAxisGetParamConfig(1, params1);
        System.out.printf("axis id 1 ExtAxisGetParamConfig : axisType %d, axisDirection %d, axisMax %.2f, axisMin %.2f, axisVel %.2f, axisAcc %.2f, axisLead %.2f, encResolution %d, axisOffect %.2f, axisCompany %d, axisModel %d, axisEncType %d\n",
                (int)params1[0], (int)params1[1], (double)params1[2], (double)params1[3],
                (double)params1[4], (double)params1[5], (double)params1[6], (int)params1[7],
                (double)params1[8], (int)params1[9], (int)params1[10], (int)params1[11]);

        // 配置扩展轴2参数
        rtn = robot.ExtAxisParamConfig(2, 1, 1, 1000, -1000, 1000, 1000, 4.444, 262144, 200, 1, 0, 0);
        System.out.println("ExtAxisParamConfig axis 2 rtn is " + rtn);

        // 获取扩展轴2参数
        Object[] params2 = new Object[12];
        rtn = robot.ExtAxisGetParamConfig(2, params2);
        System.out.printf("axis id 2 ExtAxisGetParamConfig : axisType %d, axisDirection %d, axisMax %.2f, axisMin %.2f, axisVel %.2f, axisAcc %.2f, axisLead %.2f, encResolution %d, axisOffect %.2f, axisCompany %d, axisModel %d, axisEncType %d\n",
                (int)params2[0], (int)params2[1], (double)params2[2], (double)params2[3],
                (double)params2[4], (double)params2[5], (double)params2[6], (int)params2[7],
                (double)params2[8], (int)params2[9], (int)params2[10], (int)params2[11]);

        robot.Sleep(3000);

        // 扩展轴1点动
        robot.ExtAxisStartJog(1, 0, 10, 10, 30);
        robot.Sleep(1000);
        robot.ExtAxisStopJog(1);
        robot.Sleep(3000);
        robot.ExtAxisServoOn(1, 0);

        robot.Sleep(3000);

        // 扩展轴2点动
        robot.ExtAxisStartJog(2, 0, 10, 10, 30);
        robot.Sleep(1000);
        robot.ExtAxisStopJog(2);
        robot.Sleep(3000);
        robot.ExtAxisServoOn(2, 0);

        // 卸载UDP驱动
        robot.ExtDevUnloadUDPDriver();

        return 0;
    }

    public static int TestUserLed(Robot robot)
    {
        robot.SetUserLEDColor(true, true, true);
        robot.Sleep(1000);
        robot.SetUserLEDColor(false, false, false);
        robot.Sleep(1000);
        robot.SetUserLEDColor(true, false, false);
        robot.Sleep(1000);
        robot.SetUserLEDColor(false, true, false);
        robot.Sleep(1000);
        robot.SetUserLEDColor(false, false, true);
        robot.Sleep(1000);

        return 0;
    }

    public static int TestDexterousHands(Robot robot) {
        int id = 1;               // 从站号
        int slaveNum = 4;         // 控制4个手指
        int max_time = 8000;      // 最大等待时间 8秒
        int[] speed = new int[16]; // 速度数组，全0表示使用默认速度
        int[] force = new int[16]; // 力矩数组

        robot.LoggerInit(FrLogType.DIRECT, FrLogLevel.INFO, "D://log", 10, 10);
        // 初始化力矩数组：前4个手指设为50%，其余为0（通过Move指令下发数值）
        for (int i = 0; i < 16; i++) {
            force[i] = (i < 4) ? 50 : 0;
        }

        // 设置位置数组（前4个手指有效）
        final double[] pos = new double[16];

        JointPos j1 = new JointPos(-91.876, -85.920, 109.279, -86.239, -96.664, -28.563);
        JointPos j2 = new JointPos(-40.954, -85.920, 109.279, -86.239, -96.664, -28.563);
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);
        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);

        System.out.println("===== 灵巧手完整功能测试开始 =====");

        // 1. 清除错误
        int ret = robot.ClearDexterousHandsError();
        System.out.println("ClearDexterousHandsError -> " + ret);

        // ========== 2. 设置功能开关 ==========
        int[] setFunc = new int[32];
        setFunc[2] = 1;   // 启用位置设置功能
        setFunc[4] = 1;   // 启用力矩设置功能
        setFunc[9] = 1;   // 读位置
        setFunc[10] = 1;   // 读力矩
        setFunc[11] = 1;   // 读状态
        setFunc[22] = 1;   // 单轴运动状态

        ret = robot.SetDexterousHandsFunc(id, setFunc);
        System.out.println("SetDexterousHandsFunc(使能+初始化+位置/速度/力矩功能启用) -> " + ret);

        // ========== 3. 读取功能状态（验证设置是否生效） ==========
        int[] getFunc = new int[32];  // GetDexterousHandsFunc 返回32个整数
        ret = robot.GetDexterousHandsFunc(id, getFunc);
        System.out.println("GetDexterousHandsFunc -> " + ret);
        if (ret == 0) {
            // 打印全部32个数值
            System.out.println("GetDexterousHandsFunc 返回的全部32个数值:");
            for (int i = 0; i < getFunc.length; i++) {
                System.out.print("  [" + i + "]=" + getFunc[i]);
                if ((i + 1) % 8 == 0) {
                    System.out.println();          // 每8个换行
                } else if (i < getFunc.length - 1) {
                    System.out.print(", ");
                }
            }
            if (getFunc.length % 8 != 0) {
                System.out.println();              // 如果最后一行不足8个则补换行
            }
        }

        // ========== 4. 激活灵巧手 ==========
        ret = robot.SetDexterousHandsAct(id, 1);
        System.out.println("SetDexterousHandsAct(激活) -> " + ret);
        if (ret != 0) {
            System.out.println("激活失败，测试中止");
            return ret;
        }

        // ========== 5. 初始移动到 20°（通过Move指令下发位置和力矩数值） ==========
        setPositions(pos, 20, 20, 20, 20);
        ret = robot.SetDexterousHandsMove(id, slaveNum, pos, speed, force, max_time);
        System.out.println("初始移动 20° -> " + ret);
        robot.Sleep(5000);

        // ========== 6. 往复运动10次（10° ↔ 50°） ==========
        System.out.println("开始往复运动10次...");
        for (int iteration = 1; iteration <= 10; iteration++) {
            robot.MoveJ(j1, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);

            setPositions(pos, 10, 10, 10, 10);
            ret = robot.SetDexterousHandsMove(id, slaveNum, pos, speed, force, max_time);
            System.out.println("[" + iteration + "] 移动到 10° -> " + ret);
            robot.Sleep(1000);

            robot.MoveJ(j2, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);

            setPositions(pos, 50, 50, 50, 50);
            ret = robot.SetDexterousHandsMove(id, slaveNum, pos, speed, force, max_time);
            System.out.println("[" + iteration + "] 移动到 50° -> " + ret);
            robot.Sleep(1000);
        }

        System.out.println("测试完成（功能开关设置/读取 + 激活 + 10次往复运动）。");

        return 0;
    }

    // 辅助函数：设置位置数组（前4个手指有效）
    private static void setPositions(double[] pos, double v1, double v2, double v3, double v4) {
        for (int i = 0; i < 16; i++) {
            pos[i] = 0;
        }
        pos[0] = v1;
        pos[1] = v2;
        pos[2] = v3;
        pos[3] = v4;
    }

    public static int ProgramLoadTest(Robot robot) {
        int error = robot.ProgramLoad("test.lua");
        System.out.println("ProgramLoad return " + error);

        String[] result = new String[1];
        error = robot.GetLoadedProgram(result);
        System.out.println("GetLoadedProgram return " + error);
        System.out.println("Loaded lua name is : " + result[0]);

        robot.Mode(0);
        robot.Sleep(1000);
        robot.ProgramRun();
        robot.Sleep(1000);
        robot.Mode(1);

        return 0;
    }

    public static int LoadDefaultProgConfigTest(Robot robot) {
        robot.LoadDefaultProgConfig(1, "test.lua");
        String[] result = new String[1];
        int error = robot.GetLoadedProgram(result);
        System.out.println("Loaded lua name is : " + result[0]);
        System.out.println("GetLoadedProgram return " + error);
        robot.Mode(0);
        robot.Sleep(100000);
        robot.Mode(1);

        return 0;
    }

    private static boolean GripperDropAlarmTest(Robot robot)
    {
        int gripperIndex = 1;
        int vel = 50;
        int force = 50;
        int testCycles = 10;
        int passCount = 0;
        int failCount = 0;

        System.out.println("=== 夹爪工件掉落报警测试 开始 ===");
        System.out.println("测试次数: " + testCycles + ", 夹爪索引: " + gripperIndex);

        // 切换到手动模式
        int rtn = robot.Mode(1);
        System.out.println("Mode(1) rtn=" + rtn);

        for (int cycle = 1; cycle <= testCycles; cycle++)
        {
            System.out.println("\n--- 第 " + cycle + "/" + testCycles + " 次测试 ---");
            boolean cyclePass = true;

            try
            {
                ROBOT_STATE_PKG state = new ROBOT_STATE_PKG();

                // 清除残留故障
                state = robot.GetRobotRealTimeState();
                if (state.main_code != 0 || state.sub_code != 0)
                {
                    System.out.println("  残留故障码[" + state.main_code + "," + state.sub_code + "]，先清除");
                    robot.ResetAllError();
                    robot.Sleep(500);
                }

                // 步骤1: 夹爪张开到0位置
                System.out.println("  步骤1: 夹爪张开到0位置...");
                rtn = robot.MoveGripper(gripperIndex, 0, vel, force, 10000, 1, 0, 0, 0, 0);
                if (rtn != 0)
                {
                    System.out.println("  夹爪张开失败, rtn=" + rtn);
                    failCount++; continue;
                }
                //robot.Sleep(3000);

                // 步骤2: 放入工件
                System.out.println("  步骤2: 请放入工件... (等待3秒)");
                robot.Sleep(3000);

                // 步骤3: 夹爪闭合到100位置
                System.out.println("  步骤3: 夹爪闭合到100位置...");
                rtn = robot.MoveGripper(gripperIndex, 100, vel, force, 10000, 1, 0, 0, 0, 0);
                if (rtn != 0)
                {
                    System.out.println("  夹爪闭合失败, rtn=" + rtn);
                    failCount++; continue;
                }
                robot.Sleep(4000);

                // 步骤4: 拿掉工件
                System.out.println("  步骤4: 请拿掉工件... (等待2秒)");
                robot.Sleep(1000);

                // 步骤5: 检测3000ms内报警 (main_code=8, sub_code=3 表示工件掉落)
                System.out.println("  步骤5: 检测报警...");
                boolean alarmDetected = false;
                long startTime = System.currentTimeMillis();
                final int alarmTimeout = 3000;

                while ((System.currentTimeMillis() - startTime) < alarmTimeout)
                {
                    state = robot.GetRobotRealTimeState();

                    if (state.main_code == 8 && state.sub_code == 3)
                    {
                        long elapsed = System.currentTimeMillis() - startTime;
                        System.out.println("  PASS: 检测到工件掉落报警");
                        System.out.println("    main_code=" + state.main_code + ", sub_code=" + state.sub_code);
                        System.out.println("    响应时间: " + elapsed + "ms");

                        int co2State = (state.cl_dgt_output_h & 0x04) > 0 ? 1 : 0;
                        System.out.println("    CO2=" + co2State);
                        System.out.println("    program_state=" + state.program_state + " (1=停止)");

                        alarmDetected = true;
                        break;
                    }
                    robot.Sleep(100);
                }

                if (!alarmDetected)
                {
                    System.out.println("  FAIL: 未在" + alarmTimeout + "ms内检测到报警");
                    state = robot.GetRobotRealTimeState();
                    System.out.println("    当前 main_code=" + state.main_code + ", sub_code=" + state.sub_code);
                    cyclePass = false;
                }

                // 步骤6: 清除报警，复位+激活夹爪
                System.out.println("  步骤6: 清除报警...");
                rtn = robot.ResetAllError();
                System.out.println("  ResetAllError rtn=" + rtn);
                robot.Sleep(1500);

                // 复位夹爪
                System.out.println("  步骤7: 复位夹爪...");
                rtn = robot.ActGripper(gripperIndex, 0);
                System.out.println("  ActGripper(reset) rtn=" + rtn);
                robot.Sleep(3000);

                // 激活夹爪
                System.out.println("  步骤8: 激活夹爪...");
                rtn = robot.ActGripper(gripperIndex, 1);
                System.out.println("  ActGripper(activate) rtn=" + rtn);
                robot.Sleep(5000);

                state = robot.GetRobotRealTimeState();
                System.out.println("  最终状态: main_code=" + state.main_code + ", sub_code=" + state.sub_code);

                if (state.main_code == 0 && state.sub_code == 0)
                    System.out.println("  报警已清除, 夹爪已复位激活");
                else
                {
                    System.out.println("  WARN: 状态未完全恢复");
                    cyclePass = false;
                }

                if (cyclePass)
                    passCount++;
                else
                    failCount++;
            }
            catch (Exception ex)
            {
                System.out.println("  异常: " + ex.getMessage());
                failCount++;
            }
        }

        System.out.println("\n=== 测试结果 ===");
        System.out.println("通过: " + passCount + ", 失败: " + failCount + ", 总计: " + testCycles);
        System.out.println("通过率: " + ((double)passCount / testCycles * 100) + "%");

        return failCount == 0;
    }

    /**
     * 摆动过程中调速和实时偏移测试
     * WeaveStart -> MoveL -> SetSpeed调速 -> SetWeaveOffsetRT偏移(50次) -> WeaveEnd
     */
    public static void TestWeaveSpeedAndOffset(Robot robot) {
        System.out.println("============================================================");
        System.out.println("  摆动调速与实时偏移测试");
        System.out.println("============================================================");

        if (robot == null) {
            System.out.println("ERROR: 机器人未连接!");
            return;
        }

        int rtn;
        ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);
        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);

        JointPos j1 = new JointPos(5.027, -84.331, -75.139, -103.690, 86.379, 20.794);
        DescPose d1 = new DescPose(324.752, -83.339, 366.314, -172.321, -0.936, -106.047);

        JointPos j2 = new JointPos(-35.335, -117.598, -57.174, -95.234, 90.001, -19.560);
        DescPose d2 = new DescPose(324.999, -355.439, 260.000, 179.995, 0.003, -105.775);

        JointPos j3 = new JointPos(59.787, -117.594, -57.183, -95.222, 90.006, 75.562);
        DescPose d3 = new DescPose(324.998, 355.441, 260.002, 179.995, 0.003, -105.775);

        // ---- Step 1: MoveJ到起始点 ----
        System.out.println("\nStep 1: MoveJ to start point");
        rtn = robot.MoveJ(j1, d1, 1, 0, 100, 100, 50, epos, -1, 0, offset_pos);
        System.out.println("  MoveJ(j1) rtn=" + rtn);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ---- Step 2: MoveJ到摆动入口 ----
        System.out.println("\nStep 2: MoveJ to weave entry point");
        rtn = robot.MoveJ(j2, d2, 1, 0, 100, 100, 50, epos, -1, 0, offset_pos);
        System.out.println("  MoveJ(j2) rtn=" + rtn);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ---- Step 3: WeaveStart, 启动摆动MoveL线程 ----
        System.out.println("\nStep 3: WeaveStart + MoveL in background thread");
        robot.WeaveStart(0);

        final boolean[] weaveRunning = {true};
        final int[] threadRtn = {0};
        Thread weaveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                threadRtn[0] = robot.MoveL(j3, d3, 1, 0, 100, 100, 5, -1, 0, epos, 0, 0, offset_pos, 5, 0, 0, 10);
                System.out.println("  MoveL(weave) thread finished, rtn=" + threadRtn[0]);
                weaveRunning[0] = false;
            }
        });
        weaveThread.setDaemon(true);
        weaveThread.start();
        try {
            Thread.sleep(500);  // 等待运动开始
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ---- Step 4: 调速测试(主线程，摆动MoveL后台运行) ----
        System.out.println("\nStep 4: SetSpeed test during weaving");
        int[] speedValues = { 20, 50, 80, 30, 60, 10 };
        for (int speed : speedValues) {
            if (!weaveRunning[0]) break;
            rtn = robot.SetSpeedInstant(speed);
            pkg = robot.GetRobotRealTimeState();
            System.out.println("  SetSpeed(" + speed + ") -> rtn=" + rtn + ", TCP_CmpSpeed=" + pkg.target_TCP_CmpSpeed);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ---- Step 5: SetWeaveOffsetRT偏移测试(主线程，摆动MoveL后台运行) ----
        System.out.println("\nStep 5: SetWeaveOffsetRT test (50 iterations, delta=0.1)");
        double accumOffset = 0.0;
        for (int i = 0; i < 50 && weaveRunning[0]; i++) {
            accumOffset += 0.1;
            DescPose weaveOffset = new DescPose(0, 0, accumOffset, 0, 0, 0);
            rtn = robot.SetWeaveOffsetRT(weaveOffset);
            pkg = robot.GetRobotRealTimeState();
            System.out.printf("  [%d/50] SetWeaveOffsetRT(x=%.1f) -> rtn=%d, TCP_pos=(%.2f,%.2f,%.2f)\n",
                i + 1, accumOffset, rtn,
                pkg.tl_cur_pos[0], pkg.tl_cur_pos[1], pkg.tl_cur_pos[2]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // ---- Step 6: 等待摆动MoveL完成, WeaveEnd ----
        System.out.println("\nStep 6: Wait for weave MoveL, then WeaveEnd");
        try {
            weaveThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        robot.WeaveEnd(0);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ---- Step 7: MoveL返回起始点 ----
        System.out.println("\nStep 7: MoveL back to start");
        rtn = robot.MoveL(j1, d1, 1, 0, 100, 100, 50, -1, 0, epos, 0, 0, offset_pos, 50, 0, 0, 10);
        System.out.println("  MoveL(back) rtn=" + rtn);

        pkg = robot.GetRobotRealTimeState();
        System.out.println("\n  Final robot state: main_code=" + pkg.main_code + ", sub_code=" + pkg.sub_code);
        System.out.println("============================================================");
        System.out.println("  摆动调速与实时偏移测试 完成");
        System.out.println("============================================================");
    }

    /**
     * @brief 五指灵巧手完整功能测试
     *
     * 测试流程：
     * 1. 初始化机器人连接并配置参数
     * 2. 设置灵巧手功能开关（使能、初始化、位置/速度/力矩控制、多轴同步）
     * 3. 读取并验证功能开关状态
     * 4. 激活灵巧手
     * 5. 执行往复运动测试：机器人交替移动至两个位姿，灵巧手在 A/B/C 三组目标位置间切换
     *
     * @return int 0-测试成功，-1-连接失败，-2-激活失败
     */
    public static int TestFiveDexterousHands(Robot robot) {
        // ==================== 1. 初始化与连接 ====================
        ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();

        // ==================== 2. 运动参数配置 ====================
        final int DEXTEROUS_ID = 1;          // 从站号（灵巧手）
        final int FINGER_COUNT = 12;          // 控制手指数量
        final int MOVE_TIMEOUT_MS = 12000;    // 单次运动最大等待时间（毫秒）

        // 速度/力矩数组（12个手指，后4个留空）
        int[] speed = { 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 0, 0, 0, 0 };
        int[] force = { 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 0, 0, 0, 0 };

        // 三组目标位置（角度）
        double[] posA = { 5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5,  5, 0, 0, 0, 0 };
        double[] posB = { 60, 10, 70, 30, 70, 70, 10, 10, 10, 10, 10, 10, 0, 0, 0, 0 };
        double[] posC = { 50, 50, 20, 20, 0,  0,  0,  0,  70, 70, 70, 70, 0, 0, 0, 0 };

        // 机器人两个测试位姿
        JointPos j1 = new JointPos(-172.132, -90.455, -102.422, -67.864, 95.273, -21.129);
        JointPos j2 = new JointPos(-173.180, -106.578, -83.661, -70.600, 95.440, -22.167);
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);
        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);

        System.out.println("===== 灵巧手完整功能测试开始 =====");

        // ==================== 3. 清除错误状态 ====================
        int rtn = robot.ClearDexterousHandsError();
        System.out.printf("[清除错误] rtn = %d\n", rtn);

        // ==================== 4. 设置功能开关 ====================
        int[] setFuncA = new int[32];
        setFuncA[2] = 1;
        setFuncA[3] = 1;
        setFuncA[4] = 1;
        setFuncA[9] = 1;
        setFuncA[10] = 1;
        setFuncA[11] = 1;
        setFuncA[20] = 1;//多轴同步运动
        setFuncA[22] = 1;

        int[] setFuncB = new int[32];
        setFuncB[2] = 1;
        setFuncB[3] = 1;
        setFuncB[4] = 1;
        setFuncB[9] = 1;
        setFuncB[10] = 1;
        setFuncB[11] = 1;
        setFuncB[22] = 1;

        // 主站设置功能 A
        rtn = robot.SetDexterousHandsFunc(DEXTEROUS_ID, setFuncA);
        System.out.printf("[设置主站功能] rtn = %d\n", rtn);

        // 从站（手指2~12）设置功能 B
        for (int i = 2; i <= FINGER_COUNT; i++) {
            rtn = robot.SetDexterousHandsFunc(i, setFuncB);
        }
        System.out.printf("[设置从站功能（2~12）] rtn = %d\n", rtn);

        // ==================== 5. 读取并验证功能状态 ====================
        int[] getFunc = new int[32];
        rtn = robot.GetDexterousHandsFunc(DEXTEROUS_ID, getFunc);
        System.out.printf("[读取功能状态] rtn = %d\n", rtn);

        if (rtn == 0) {
            System.out.println("功能开关状态（32位）:");
            System.out.print("  ");
            for (int i = 0; i < 32; i++) {
                System.out.printf("[%d]=%d", i, getFunc[i]);
                if ((i + 1) % 8 == 0 && i < 31) {
                    System.out.print("\n  ");
                } else if (i < 31) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }

        // ==================== 6. 激活灵巧手 ====================
        rtn = robot.SetDexterousHandsAct(DEXTEROUS_ID, 1);
        System.out.printf("[激活灵巧手] rtn = %d\n", rtn);
        if (rtn != 0) {
            System.out.println("激活失败，测试中止");
            return -2;
        }

        robot.Sleep(5000);  // 等待激活完成

        // ==================== 7. 往复运动测试（10次循环） ====================
        System.out.println("\n开始往复运动测试（共10次循环）...");
        System.out.println("  位姿1: j1（左）  位姿2: j2（右）");
        System.out.println("  手指目标: A→B→A→C（每组4个动作）\n");

        for (int iteration = 1; iteration <= 10; iteration++) {
            System.out.printf("--- 第 %2d 次循环 ---\n", iteration);

            // 动作1：移至 j1 + 手指 A
            robot.MoveJ(j1, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
            rtn = robot.SetDexterousHandsMove(DEXTEROUS_ID, FINGER_COUNT, posA, speed, force, MOVE_TIMEOUT_MS);
            System.out.printf("  j1 + posA → %d\n", rtn);
            robot.Sleep(1000);

            // 动作2：移至 j2 + 手指 B
            robot.MoveJ(j2, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
            rtn = robot.SetDexterousHandsMove(DEXTEROUS_ID, FINGER_COUNT, posB, speed, force, MOVE_TIMEOUT_MS);
            System.out.printf("  j2 + posB → %d\n", rtn);
            robot.Sleep(1000);

            // 动作3：移至 j1 + 手指 A
            robot.MoveJ(j1, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
            rtn = robot.SetDexterousHandsMove(DEXTEROUS_ID, FINGER_COUNT, posA, speed, force, MOVE_TIMEOUT_MS);
            System.out.printf("  j1 + posA → %d\n", rtn);
            robot.Sleep(1000);

            // 动作4：移至 j2 + 手指 C
            robot.MoveJ(j2, 0, 0, 100, 100, 100, epos, -1, 0, offset_pos);
            rtn = robot.SetDexterousHandsMove(DEXTEROUS_ID, FINGER_COUNT, posC, speed, force, MOVE_TIMEOUT_MS);
            System.out.printf("  j2 + posC → %d\n", rtn);
            robot.Sleep(1000);
        }

        // ==================== 8. 测试完成 ====================
        System.out.println("\n===== 测试完成 =====");
        System.out.println("  功能开关设置/读取  ✓");
        System.out.println("  灵巧手激活        ✓");
        System.out.println("  10次往复运动      ✓");
        return 0;
    }

    public static int TestWorkPieceTrsf(Robot robot) {
        ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();

        JointPos j1 = new JointPos(-11.188, -64.165, -107.299, -76.706, 89.590, 92.983);
        DescPose desc1 = new DescPose(225.986, 190.694, 394.238, -6.230, -23.797, -98.972);
        JointPos j2 = new JointPos(-38.148, -97.408, -133.704, -30.999, 89.584, 92.986);
        DescPose desc2 = new DescPose(52.741, 262.917, 30.824, -5.696, -9.864, -126.092);
        JointPos j3 = new JointPos(-25.561, -123.131, -85.736, -94.911, 89.582, 93.006);
        DescPose desc3 = new DescPose(70.455, 88.410, 45.299, -4.101, 31.775, -113.199);
        JointPos j4 = new JointPos(-8.013, -125.881, -79.196, -84.440, 89.564, 93.005);
        DescPose desc4 = new DescPose(209.453, -73.895, 56.416, -4.727, 17.523, -95.906);
        JointPos j5 = new JointPos(-2.722, -94.518, -119.965, -54.518, 89.563, 93.005);
        DescPose desc5 = new DescPose(274.800, 81.106, 102.977, -5.467, -2.980, -90.711);
        JointPos j6 = new JointPos(-2.671, -56.234, -138.914, -25.099, 95.355, 92.967);
        DescPose desc6 = new DescPose(300.392, 177.281, 300.926, -1.909, -51.894, -89.703);
        JointPos j7 = new JointPos(-1.229, -121.184, -63.201, -122.331, 93.045, 93.019);
        DescPose desc7 = new DescPose(296.856, -31.294, 215.698, -0.589, 34.594, -88.954);

        ExaxisPos exaxis = new ExaxisPos(0.0, 0.0, 0.0, 0.0);
        DescPose offset = new DescPose(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        int tool = 1;
        int workpiece = 1;
        double blend = 5.0;

        robot.MoveJ(j1, desc1, tool, workpiece, 100, 100, 100, exaxis, -1, 0, offset);
        robot.MoveJ(j2, desc2, tool, workpiece, 100, 100, 100, exaxis, blend, 0, offset);
        robot.MoveL(j3, desc3, tool, workpiece, 10, 100, 100, blend, 0, exaxis, 0, 1, offset,-1, 0,0,0);
        robot.MoveC(j4, desc4, tool, workpiece, 100, 100, exaxis, 0, offset, j5, desc5, tool, workpiece, 100, 100, exaxis, 0, offset, 10, blend,100, 0);
        robot.Circle(j6, desc6, tool, workpiece, 100, 100, exaxis, j7, desc7, tool, workpiece, 100, 100, exaxis, 10, 0, offset, 100.0, blend,0);

        int rtn = robot.WorkPieceTrsfStart(2);
        System.out.printf("WorkPieceTrsfStart rtn is %d\n", rtn);
        robot.MoveJ(j1, desc1, tool, workpiece, 100, 100, 100, exaxis, -1, 0, offset);
        robot.MoveJ(j2, desc2, tool, workpiece, 100, 100, 100, exaxis, blend, 0, offset);
        robot.MoveL(j3, desc3, tool, workpiece, 10, 100, 100, blend, 0, exaxis, 0, 1, offset,-1, 0,0,0);
        robot.MoveC(j4, desc4, tool, workpiece, 100, 100, exaxis, 0, offset, j5, desc5, tool, workpiece, 100, 100, exaxis, 0, offset, 10, blend,100, 0);
        robot.Circle(j6, desc6, tool, workpiece, 100, 100, exaxis, j7, desc7, tool, workpiece, 100, 100, exaxis, 10, 0, offset, 100.0, blend, 0);

        robot.WorkPieceTrsfEnd();
        System.out.printf("WorkPieceTrsfEnd rtn is %d\n", rtn);
        robot.Sleep(2000);
        return 0;
    }

    public static int TestSetJointVelReducePara(Robot robot) {
        ROBOT_STATE_PKG pkg = new ROBOT_STATE_PKG();

        JointPos j1 = new JointPos(10.220, -11.121, -118.086, -46.739, 82.036, 131.503);
        JointPos j2 = new JointPos(89.782, -11.122, -118.086, -46.740, 82.036, 131.504);
        ExaxisPos epos = new ExaxisPos(0, 0, 0, 0);
        DescPose offset_pos = new DescPose(0, 0, 0, 0, 0, 0);
        robot.SetSpeed(20);

        double[] maxJointVelA = {100.0, 100.0, 100.0, 100.0, 100.0, 100.0};
        int rtn = robot.SetVelReducePara(2, 200, 0, maxJointVelA);
        System.out.printf("SetVelReducePara param error rtn is %d\n", rtn);
        robot.MoveJ(j1, 1, 2, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.MoveJ(j2, 1, 2, 100, 100, 100, epos, -1, 0, offset_pos);

        double[] maxJointVelB = {20.0, 20.0, 20.0, 20.0, 20.0, 20.0};
        rtn = robot.SetVelReducePara(2, 200, 0, maxJointVelB);
        System.out.printf("SetVelReducePara reduce vel rtn is %d\n", rtn);
        robot.MoveJ(j1, 1, 2, 100, 100, 100, epos, -1, 0, offset_pos);
        robot.MoveJ(j2, 1, 2, 100, 100, 100, epos, -1, 0, offset_pos);

        robot.Sleep(2000);
        return 0;
    }

    public static int TestCoord(Robot robot)
    {
        int id = 1;
        DescPose toolCoord = new DescPose();
        DescPose extoolCoord = new DescPose();
        DescPose exworkpieceCoord = new DescPose();
        DescPose wobjCoord = new DescPose();
        DescPose exAxisCoord = new DescPose();
        int[] type = new int[1];
        int[] install = new int[1];
        int[] toolID = new int[1];
        int[] loadNo = new int[1];
        robot.GetToolCoordWithID(id, toolCoord, type, install, toolID, loadNo);
        System.out.printf("GetToolCoordWithID %d, %f %f %f %f %f %f,  type = %d, install = %d, toolID = %d, loadNo = %d\n", id,
            toolCoord.tran.x, toolCoord.tran.y, toolCoord.tran.z,
            toolCoord.rpy.rx, toolCoord.rpy.ry, toolCoord.rpy.rz, type[0], install[0], toolID[0], loadNo[0]);
        int[] refFrame = new int[1];
        robot.GetWObjCoordWithID(id, wobjCoord, refFrame);
        System.out.printf("GetWObjCoordWithID %d, %f %f %f %f %f %f, refFrame = %d\n", id,
            wobjCoord.tran.x, wobjCoord.tran.y, wobjCoord.tran.z,
            wobjCoord.rpy.rx, wobjCoord.rpy.ry, wobjCoord.rpy.rz, refFrame[0]);


        robot.GetExToolCoordWithID(21, extoolCoord, exworkpieceCoord);
        System.out.printf("GetExToolCoordWithID %d, %f %f %f %f %f %f\n", id,
            extoolCoord.tran.x, extoolCoord.tran.y, extoolCoord.tran.z,
            extoolCoord.rpy.rx, extoolCoord.rpy.ry, extoolCoord.rpy.rz,
            exworkpieceCoord.tran.x, exworkpieceCoord.tran.y, exworkpieceCoord.tran.z,
            exworkpieceCoord.rpy.rx, exworkpieceCoord.rpy.ry, exworkpieceCoord.rpy.rz);

        int[] axisCoordNum = new int[1];
        int[] calibFlag = new int[1];
        robot.GetExAxisCoordWithID(id, exAxisCoord, axisCoordNum, calibFlag);
        System.out.printf("GetExAxisCoordWithID %d, %f %f %f %f %f %f, axisCoordNum = %d, calibFlag = %d\n", id,
            exAxisCoord.tran.x, exAxisCoord.tran.y, exAxisCoord.tran.z,
            exAxisCoord.rpy.rx, exAxisCoord.rpy.ry, exAxisCoord.rpy.rz, axisCoordNum[0], calibFlag[0]);

        double[] weight = new double[1];
        DescTran cog = new DescTran();
        robot.GetTargetPayloadWithID(id, weight, cog);
        System.out.printf("GetTargetPayloadWithID %d, %f %f %f %f\n", id, weight[0],
            cog.x, cog.y, cog.z);
        robot.GetCurToolCoord(toolCoord);
        System.out.printf("GetCurToolCoord %f %f %f %f %f %f\n",
            toolCoord.tran.x, toolCoord.tran.y, toolCoord.tran.z,
            toolCoord.rpy.rx, toolCoord.rpy.ry, toolCoord.rpy.rz);
        robot.GetCurWObjCoord(wobjCoord);
        System.out.printf("GetCurWObjCoord %f %f %f %f %f %f\n",
            wobjCoord.tran.x, wobjCoord.tran.y, wobjCoord.tran.z,
            wobjCoord.rpy.rx, wobjCoord.rpy.ry, wobjCoord.rpy.rz);
        robot.GetCurExToolCoord(extoolCoord);
        System.out.printf("GetExToolCoordWithID %f %f %f %f %f %f\n",
            extoolCoord.tran.x, extoolCoord.tran.y, extoolCoord.tran.z,
            extoolCoord.rpy.rx, extoolCoord.rpy.ry, extoolCoord.rpy.rz);
        robot.GetCurExAxisCoord(exAxisCoord);
        System.out.printf("GetCurExAxisCoord %f %f %f %f %f %f\n",
            exAxisCoord.tran.x, exAxisCoord.tran.y, exAxisCoord.tran.z,
            exAxisCoord.rpy.rx, exAxisCoord.rpy.ry, exAxisCoord.rpy.rz);
        DescTran cogT = new DescTran();
        List<Number> weightT = robot.GetTargetPayload(0);
        robot.GetTargetPayloadCog(0, cogT);
        System.out.println("GetTargetPayload is " + weightT.get(0) + " cogT is  " + cogT.x + "  " + cogT.y + "  " + cogT.z);
        DescPose coordSet = new DescPose(0, 1, 2, 3, 4, 5);
        robot.SetToolCoord(1, coordSet, 0, 0, 1, 0);
        robot.SetWObjCoord(1, coordSet, 0);
        robot.SetLoadWeight(1, 1.3);
        cog.x = 10;
        cog.y = 20;
        cog.z = 30;
        robot.SetLoadCoord(1, cog);
        DescPose etcp = new DescPose(0, 0, 100, 0, 0, 0);
        DescPose etool = new DescPose(0, 0, 50, 0, 0, 0);
        int rtn = robot.SetExToolCoord(21, etcp, etool);
        System.out.printf("SetExToolCoord rtn is %d\n", rtn);
        robot.ExtAxisActiveECoordSys(1, 1, coordSet, 1);
        return 0;
    }

    public static int TestStationaryTrack(Robot robot)
    {
        System.out.println("\n========== 传送带静止跟踪测试 ==========");

        int rtn;

        JointPos j1 = new JointPos(-35.146, -102.684, 120.805, -100.401, -90.295, 150.105);
        DescPose d1 = new DescPose(-121.814, -348.341, 209.978, -173.152, -3.585, -5.446);

        ExaxisPos ex = new ExaxisPos(0, 0, 0, 0);
        DescPose zeroOff = new DescPose(0, 0, 0, 0, 0, 0);

        int tool = 1;
        int workpiece = 1;

        rtn = robot.ConveyorSetParam(0, 10000, 200, 0, 0, 10,0,0,0);


        robot.MoveJ(j1, d1, tool, workpiece, 100, 100, 100, ex, -1, 0, zeroOff);

        // Step 1: SetDO 控制信号
        System.out.println("--- Step 1: SetDO(6,1) ---");
        rtn = robot.SetDO(6, 1, 0, 0);
        System.out.println("  SetDO(6,1) rtn=" + rtn);

        // Step 2: 传送带跟踪开始
        System.out.println("--- Step 2: ConveyorTrackStart(2) ---");
        rtn = robot.ConveyorTrackStart(2);
        System.out.println("  ConveyorTrackStart(2) rtn=" + rtn);

        // Step 3: 工件IO检测
        System.out.println("--- Step 3: ConveyorIODetect(10000) ---");
        rtn = robot.ConveyorIODetect(10000);
        System.out.println("  ConveyorIODetect(10000) rtn=" + rtn);

        // Step 4: 获取跟踪数据
        System.out.println("--- Step 4: ConveyorGetTrackData(2) ---");
        rtn = robot.ConveyorGetTrackData(2);
        System.out.println("  ConveyorGetTrackData(2) rtn=" + rtn);

        // Step 5: 静止跟踪参数配置 (时间模式, 200s, 距离5)
        System.out.println("--- Step 5: SetStationaryTrackPara(0,200,5) ---");
        rtn = robot.SetStationaryTrackPara(0, 5, 5);
        System.out.println("  SetStationaryTrackPara(0,200,5) rtn=" + rtn);

        // Step 6: 执行静止跟踪运动
        System.out.println("--- Step 6: MoveStationary() ---");
        rtn = robot.MoveStationary();
        robot.WaitStationaryMotionDone();
        System.out.println("  MoveStationary() rtn=" + rtn);

        // Step 7: 传送带跟踪结束
        System.out.println("--- Step 7: ConveyorTrackEnd() ---");
        rtn = robot.ConveyorTrackEnd();
        System.out.println("  ConveyorTrackEnd() rtn=" + rtn);

        // Step 8: SetDO 关闭信号
        System.out.println("--- Step 8: SetDO(6,0) ---");
        rtn = robot.SetDO(6, 0, 0, 0);
        System.out.println("  SetDO(6,0) rtn=" + rtn);

        System.out.println("\n========== 静止跟踪测试完成 ==========");
        return 0;
    }

}
