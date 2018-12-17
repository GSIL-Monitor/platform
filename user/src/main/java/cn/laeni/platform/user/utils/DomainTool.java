package cn.laeni.platform.user.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 域名工具
 * @author laeni.cn
 */
public class DomainTool {

    /**
     * 获取顶级域名的正则表达式
     */
    private static final Pattern PATTERN = Pattern.compile("^([\\S]*\\.)?([^\\.]+\\.+(com|cn|net|org|biz|info|cc|tv|com.cn|ac.cn|net.cn|org.cn))$", Pattern.CASE_INSENSITIVE);

    /**
     * 提取主机名的顶级域名(取不到时返回原值)
     *
     * @param domain 待提取的域名
     * @return 顶级域名
     */
    public static String getMainDome(String domain) {

        Matcher matcher = PATTERN.matcher(domain);
        if (matcher.find()) {
            return matcher.group(2);
        }

        return domain;
    }

}
