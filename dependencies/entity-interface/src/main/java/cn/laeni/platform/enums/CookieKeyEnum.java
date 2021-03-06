package cn.laeni.platform.enums;

/**
 * 该异常为
 * @author laeni.cn
 */

public enum CookieKeyEnum {
    /**
     * 用于存储SESSION的key
     */
    SESSION("LAENI_SESSION"),
    USER_ID("user_id"),
    NICNAME("nickname"),
    AVATAR("avatar"),
    GRADE("grade"),
    LOGIN_OK("__ok");

    private String key;

    CookieKeyEnum(String key){
        this.key = key;
    }

    /**
     * 获取Cookiekey
     * @return
     */
    public String getKey() {
        return key;
    }
}
