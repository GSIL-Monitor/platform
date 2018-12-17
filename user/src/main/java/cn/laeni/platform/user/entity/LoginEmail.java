package cn.laeni.platform.user.entity;

import java.io.Serializable;

public class LoginEmail implements Serializable{
	private static final long serialVersionUID = 2514915097202784133L;
	
	private String loginEmail;		// 登录邮箱号
	private String userId;			// 用户ID
	
	private User user;			// 用户基本信息( 一对一关联的对象)

	@Override
	public String toString() {
		return "LoginEmail{" +
				"loginEmail='" + loginEmail + '\'' +
				", userId='" + userId + '\'' +
				", user=" + user +
				'}';
	}

	public String getLoginEmail() {
		return loginEmail;
	}

	public void setLoginEmail(String loginEmail) {
		this.loginEmail = loginEmail;
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
