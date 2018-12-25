package cn.laeni.platform.user.entity;

import java.io.Serializable;

/** 登录名表实体
 * @author Laeni
 */
public class LoginName implements Serializable {

	private static final long serialVersionUID = -4943379844433937464L;

	/**
	 * 登录名
	 */
	private String loginName;
	/**
	 * 用户Id
	 */
	private String userId;
	/**
	 * 用户基本信息( 一对一关联的对象)
	 */
	private User user;

    @Override
    public String toString() {
        return "LoginName{" +
                "loginName='" + loginName + '\'' +
                ", userId='" + userId + '\'' +
                ", user=" + user +
                '}';
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
