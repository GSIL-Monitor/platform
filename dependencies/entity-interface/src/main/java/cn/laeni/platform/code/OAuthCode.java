package cn.laeni.platform.code;

/**
 * OAuth2.0授权状态码定义
 * @author Laeni
 */

public enum OAuthCode {
    /**
     * Code失效
     */
    CODE_INVALID("201","Code失效");

    private String code;
    private String msg;

    OAuthCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
