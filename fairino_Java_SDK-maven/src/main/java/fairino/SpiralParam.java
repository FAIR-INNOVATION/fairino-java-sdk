package fairino;
public class SpiralParam
{
    public int circle_num;           /* 螺旋圈数  */
    public double circle_angle;         /* 螺旋倾角  */
    public double rad_init;             /* 螺旋初始半径，单位mm  */
    public double rad_add;              /* 半径增量  */
    public double rotaxis_add;          /* 转轴方向增量  */
    public int rot_direction;  /* 旋转方向，0-顺时针，1-逆时针  */
    public int velAccMode;     /* 速度加速度参数模式：0-角速度恒定；1-线速度恒定 */
    public SpiralParam(int circleNum, double circleAngle, double radInit, double radAdd, double rotaxisAdd, int rotDirection,int vel_AccMode)
    {
        circle_num = circleNum;
        circle_angle = circleAngle;
        rad_init = radInit;
        rad_add = radAdd;
        rotaxis_add = rotaxisAdd;
        rot_direction = rotDirection;
        velAccMode=vel_AccMode;
    }
    public SpiralParam(){

    }
}
