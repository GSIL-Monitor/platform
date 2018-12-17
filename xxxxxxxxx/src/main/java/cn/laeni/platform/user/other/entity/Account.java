package cn.laeni.platform.goods.other.entity;

import cn.laeni.platform.goods.other.AccountTypeEnum;
import cn.laeni.platform.goods.utils.AccountTool;

/**
 * 帐号,即可能代表手机号/邮箱号/登录名等
 * @author laeni.cn
 */
public class Account {
    private String value;
    private AccountTypeEnum typeEnum;

    /**
     * 无参构造方法
     */
    public Account(){
    }

    public Account(String account) {
        this.setValue(account);
    }

    /**
     * 获取帐号的值
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置账户,并自动判断该帐号的类型
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
        this.typeEnum = AccountTool.accountType(value);
    }

    /**
     * 获取帐号的类型
     * @return
     */
    public AccountTypeEnum getTypeEnum() {
        return typeEnum;
    }

}
