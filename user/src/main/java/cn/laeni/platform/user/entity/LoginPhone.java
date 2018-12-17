package cn.laeni.platform.user.entity;

import java.io.Serializable;

public class LoginPhone implements Serializable{
	private static final long serialVersionUID = -2520136468390389418L;
	
	private String loginPhone;
	private String userId;

	private User user;			// 用户基本信息( 一对一关联的对象)

	@Override
	public String toString() {
		return "LoginPhone{" +
				"loginPhone='" + loginPhone + '\'' +
				", userId='" + userId + '\'' +
				", user=" + user +
				'}';
	}

	public String getLoginPhone() {
		return loginPhone;
	}

	public void setLoginPhone(String loginPhone) {
		this.loginPhone = loginPhone;
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
