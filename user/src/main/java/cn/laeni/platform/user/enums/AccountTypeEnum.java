package cn.laeni.platform.user.enums;

/**
 * 帐号类型
 * @author laeni.cn
 */
public enum AccountTypeEnum {
    /**
     * 手机号
     */
    PHONE("Phone", "手机号"),
    /**
     * 邮箱号
     */
    EMAIL("Email", "邮箱号");

    private String type;
    private String typeName;

    AccountTypeEnum(String type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
