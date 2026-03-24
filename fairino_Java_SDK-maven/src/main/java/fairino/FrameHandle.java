package fairino;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @brief 机器人通讯帧处理工具库
 */
public class FrameHandle {

    /**
     * @brief 分割原始数据流中的完整帧
     * @param data 原始字符串数据
     * @return 完整帧列表
     */
    public static List<String> SplitFrame(String data) {
        List<String> result = new ArrayList<>();
        int pos = 0;

        while (pos < data.length()) {
            int start = data.indexOf("/f/b", pos);
            if (start == -1) break;

            int end = data.indexOf("/b/f", start);
            if (end == -1) break;

            // 提取完整帧，包括 "/b/f" (长度为4)
            result.add(data.substring(start, end + 4));
            pos = end + 4;
        }

        return result;
    }

    /**
     * @brief 解析单条帧字符串为 Frame 对象
     * @param frameStr 原始帧字符串
     * @return Frame 对象，解析失败返回空属性的对象
     */
    public static Frame UnpacketFrame(String frameStr) {
        Frame frame = new Frame();

        // 1. 基本长度检查
        if (frameStr == null || frameStr.length() < 12) { // "/f/b" + "III" + ... + "III" + "/b/f"
            return frame;
        }

        // 2. 验证帧头帧尾
        if (!frameStr.startsWith("/f/b") || !frameStr.endsWith("/b/f")) {
            return frame;
        }

        // 3. 提取中间数据并分割
        String body = frameStr.substring(4, frameStr.length() - 4);
        String[] parts = body.split("III", -1);

        // 期待格式: [empty_if_starts_with_III, count, cmdID, contentLen, content, empty_if_ends_with_III]
        // 或者处理过滤掉空的部分
        List<String> filteredParts = new ArrayList<>();
        for (String p : parts) {
            if (!p.isEmpty()) {
                filteredParts.add(p);
            }
        }

        if (filteredParts.size() < 4) {
            return frame;
        }

        try {
            frame.count = Integer.parseInt(filteredParts.get(0));
            frame.cmdID = Integer.parseInt(filteredParts.get(1));
            frame.contentLen = Integer.parseInt(filteredParts.get(2));
            frame.content = filteredParts.get(3);

            // 验证内容长度 (如果协议要求严格匹配)
            if (frame.contentLen > 0 && frame.content.length() != frame.contentLen) {
                // 如果长度不匹配，根据 C++ 逻辑清除内容
                // frame.content = "";
                // frame.contentLen = 0;
            }
        } catch (NumberFormatException e) {
            return frame;
        }

        return frame;
    }

    /**
     * @brief 获取机器人 LUA 程序 500 错误行号和错误码
     * @param content 帧内容
     * @param errInfo 结果输出数组 [行号, 错误码]
     */
    public static void GetRobotLUAProgram500ErrCode(String content, int[] errInfo) {
        if (content == null || errInfo == null || errInfo.length < 2) return;
        
        errInfo[0] = 0; // errLinNum
        errInfo[1] = 0; // luaErrCode

        // 检查是否是 lua 错误
        int luaPos = content.indexOf(".lua");
        if (luaPos == -1) return;

        // 找第一个冒号（文件名后的冒号）
        int colon1 = content.indexOf(':', luaPos);
        if (colon1 == -1) return;

        // 找第二个冒号（行号后的冒号）
        int colon2 = content.indexOf(':', colon1 + 1);
        if (colon2 == -1) return;

        try {
            // 提取行号
            String lineStr = content.substring(colon1 + 1, colon2);
            errInfo[0] = Integer.parseInt(lineStr.trim());

            // 找错误码数字
            int errcodePos = content.indexOf("errcode", colon2);
            if (errcodePos != -1) {
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(content.substring(errcodePos + 7));
                if (m.find()) {
                    errInfo[1] = Integer.parseInt(m.group());
                }
            }
        } catch (Exception e) {
            // 解析失败
        }
    }

    /**
     * @brief 将 Frame 对象打包成字符串
     * @param frame Frame 对象
     * @return 打包后的字符串
     */
    public static String packFrame(Frame frame) {
        if (frame == null) return "";
        
        StringBuilder sb = new StringBuilder();
        sb.append("/f/bIII");
        sb.append(frame.count);
        sb.append("III");
        sb.append(frame.cmdID);
        sb.append("III");
        
        int len = (frame.content != null) ? frame.content.length() : 0;
        sb.append(len);
        sb.append("III");
        
        if (frame.content != null) {
            sb.append(frame.content);
        }
        
        sb.append("III/b/f");
        return sb.toString();
    }
}

/**
 * @brief 帧数据结构，对应 C++ 中的 FRAME 结构体
 */
class Frame {
    /** 帧计数 */
    public int count;
    /** 命令 ID */
    public int cmdID;
    /** 内容长度（字节数） */
    public int contentLen;
    /** 帧内容（命令参数字符串） */
    public String content;

    /** 默认构造一个空帧 */
    public Frame() {
        this(0, 0, "");
    }

    /**
     * 构造一个完整帧（自动计算内容长度）
     * @param count   帧计数
     * @param cmdID   命令 ID
     * @param content 内容字符串
     */
    public Frame(int count, int cmdID, String content) {
        this.count = count;
        this.cmdID = cmdID;
        this.content = content != null ? content : "";
        this.contentLen = this.content.length();
    }
}