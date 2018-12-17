package cn.laeni.platform.user.other;

/**
 * 用于设置验证码的类型
 *
 * @author laeni.cn
 */
public enum VerifyType {
    /**
     * 注册
     */
    REG_VERIF_CODE("regVerifCode", "注册"),
    /**
     * 登录
     */
    LOGIN_VERIF_CODE("loginVerifCode", "登录");

    private String key;
    private String explanation;

    VerifyType(String key, String explanation) {
        this.key = key;
        this.explanation = explanation;
    }

    public String getKey() {
        return key;
    }

    public String getExplanation() {
        return explanation;
    }
}
