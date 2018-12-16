package cn.laeni.platform.ossfile.tool;

/**
 * 进制转换工具
 * @author laeni.cn
 */
public class RandomNumber {
    /**
     * 定义包含[0-9],[a-z],[A-Z]的常数数组；
     */
    static final char[] DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                    'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
                    'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                    'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * 将10进制Long型数据类型转为64进制
     *
     * @param seq
     * @return
     */
    public static String toSixtyTwoRadixString(long seq) {
        StringBuilder sBuilder = new StringBuilder(40);
        while (true) {
            int remainder = (int) (seq % 62);
            sBuilder.append(DIGITS[remainder]);
            seq = seq / 62;
            if (seq == 0) {
                break;
            }
        }
        return sBuilder.reverse().toString();
    }

    /**
     * 将10进制Long型数据类型转为64进制
     *
     * @param seq
     * @return
     */
    public static String toSixtyTwoRadixString(String seq) {
        Long aLong = Long.parseLong(seq);
        return toSixtyTwoRadixString(aLong);
    }
}
