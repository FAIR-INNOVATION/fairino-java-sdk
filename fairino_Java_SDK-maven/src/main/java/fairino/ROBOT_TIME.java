package fairino;
public class ROBOT_TIME
{
    int year = 0;
    int month = 0;
    int day = 0;
    int hour = 0;
    int minute = 0;
    int second = 0;
    int millisecond = 0;

    public String ToString()
    {
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "." + millisecond;
    }
}
