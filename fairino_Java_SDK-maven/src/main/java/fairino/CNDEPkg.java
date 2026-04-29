package fairino;

import java.util.ArrayList;
import java.util.List;

/**
 * CNDE 帧结构定义
 */
public class CNDEPkg {
    public static final int CNDE_FRAME_TYPE_START = 2;
    public static final int CNDE_FRAME_TYPE_STOP = 3;
    public static final int CNDE_FRAME_TYPE_OUTPUT_STATE = 4;
    public static final int CNDE_FRAME_TYPE_MESSAGE = 6;

    public static final int CNDE_HEAD = 0x5A5A;
    public static final int CNDE_END = 0xA5A5;

    public int head = CNDE_HEAD;
    public int count = 0;
    public int type = 0;
    public int len = 0;
    public List<Byte> data = new ArrayList<>();
    public int end = CNDE_END;

    public void clear() {
        count = 0;
        type = 0;
        len = 0;
        data.clear();
    }
}
