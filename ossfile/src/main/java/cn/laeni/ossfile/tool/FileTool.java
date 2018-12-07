package cn.laeni.ossfile.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author laeni.cn
 */
public class FileTool {
    /**
     * 定义正则表达式规则
     */
    private static Pattern pattern = Pattern.compile(".+\\.(\\w*)$");

    /**
     * 提取文件类型，并返回类型字符串
     * @param fileName
     * @return String
     */
    public static String getFileType(String fileName){
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()){
            return matcher.group(1);
        }else {
            return null;
        }
    }
}
