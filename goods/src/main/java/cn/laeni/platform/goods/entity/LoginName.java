package cn.laeni.platform.goods.entity;

import java.io.Serializable;

public class LoginName implements Serializable {

	private static final long serialVersionUID = -4943379844433937464L;

	private String loginName; // 登录名
	private String userId; // 用户Id

	private User deUser; // 用户基本信息( 一对一关联的对象)

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
		return deUser;
	}

	public void setUser(User deUser) {
		this.deUser = deUser;
	}

	@Override
	public String toString() {
		return "LoginName [loginName=" + loginName + ", userId=" + userId + "]";
	}
	

}
