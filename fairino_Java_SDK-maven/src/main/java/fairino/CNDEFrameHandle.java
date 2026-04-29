package fairino;

import java.util.ArrayList;
import java.util.List;

/**
 * CNDE 帧处理类
 * 负责 CNDE 数据包的组包和解析
 */
public class CNDEFrameHandle {

    /**
     * 将 CNDE_PKG 打包成帧数据
     * @param pkg CNDE 数据包
     * @return 帧数据字节列表
     */
    public static List<Byte> CNDEPkgToFrame(CNDEPkg pkg) {
        List<Byte> frame = new ArrayList<>();

        // 包头 0x5A5A
        frame.add((byte) 0x5A);
        frame.add((byte) 0x5A);

        // count
        frame.add((byte) (pkg.count & 0xFF));

        // type
        frame.add((byte) (pkg.type & 0xFF));

        // len (2 bytes, little endian)
        byte[] lenBytes = Int16ToByte(pkg.len);
        frame.add(lenBytes[0]);
        frame.add(lenBytes[1]);

        // data
        frame.addAll(pkg.data);

        // 包尾 0xA5A5
        frame.add((byte) 0xA5);
        frame.add((byte) 0xA5);

        return frame;
    }

    /**
     * 将帧数据解析为 CNDE_PKG
     * @param frame 帧数据字节列表
     * @param pkg 解析后的 CNDE 数据包
     * @return 0-成功，其他-错误码
     */
    public static int FrameToCNDEPkg(List<Byte> frame, CNDEPkg pkg) {
        if (frame.size() < 8) {
            System.err.println("error pkg length too small");
            return -1;
        }

        // 解析 head
        byte[] headBuf = {frame.get(0), frame.get(1)};
        int head = ByteToInt16(headBuf);

        // 解析 len
        byte[] lenBuf = {frame.get(4), frame.get(5)};
        int len = ByteToInt16(lenBuf);

        // 解析 end
        byte[] tailBuf = {frame.get(frame.size() - 2), frame.get(frame.size() - 1)};
        int end = ByteToInt16(tailBuf);

        pkg.len = len;

        // 检查长度
        if (len != frame.size() - 8) {
            System.err.println("error pkg length: " + len + " " + (frame.size() - 8));
            return -2;
        }

        // 检查包头
        if (head != CNDEPkg.CNDE_HEAD) {
            System.err.println("error pkg head");
            return -3;
        }

        // 检查包尾
        if (end != CNDEPkg.CNDE_END) {
            System.err.println("error pkg end");
            return -4;
        }

        pkg.head = head;
        pkg.end = end;
        pkg.count = frame.get(2) & 0xFF;
        pkg.type = frame.get(3) & 0xFF;

        // 解析 data
        pkg.data.clear();
        if (frame.size() >= 6 + len) {
            for (int i = 6; i < 6 + len; i++) {
                pkg.data.add(frame.get(i));
            }
        }

        return 0;
    }

    /**
     * 将 short 转换为 2 字节数组 (little endian)
     * @param value short 值
     * @return 2 字节数组
     */
    public static byte[] Int16ToByte(int value) {
        byte[] result = new byte[2];
        result[0] = (byte) (value & 0x00FF);
        result[1] = (byte) ((value & 0xFF00) >> 8);
        return result;
    }

    /**
     * 将 2 字节数组转换为 short (little endian)
     * @param arrByte 2 字节数组
     * @return short 值
     */
    public static int ByteToInt16(byte[] arrByte) {
        return ((arrByte[1] & 0xFF) << 8) | (arrByte[0] & 0xFF);
    }

    /**
     * 将字节列表转换为字节数组
     * @param byteList 字节列表
     * @return 字节数组
     */
    public static byte[] toByteArray(List<Byte> byteList) {
        byte[] result = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            result[i] = byteList.get(i);
        }
        return result;
    }

    /**
     * 将字节数组转换为字节列表
     * @param byteArray 字节数组
     * @return 字节列表
     */
    public static List<Byte> toByteList(byte[] byteArray) {
        List<Byte> result = new ArrayList<>();
        for (byte b : byteArray) {
            result.add(b);
        }
        return result;
    }
}
