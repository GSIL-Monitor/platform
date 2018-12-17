package cn.laeni.platform.user.entity;

import java.io.Serializable;

/**
 * 与用户id一一对应 如果用户与相应QQ绑定,则有一条对应记录
 * 
 * @author laeni.cn
 *
 */
public class ConnectQQ implements Serializable {
	private static final long serialVersionUID = 5025399747400736897L;

	/**
	 * 与QQ唯一对应
	 */
	private String openid;
	/**
	 * 用户唯一id
	 */
	private String userId;
	/**
	 * 授权令牌，Access_Token
	 */
	private String accessToken;
	/**
	 * 在授权自动续期步骤中，获取新的Access_Token时需要提供的参数
	 */
	private String refreshToken;
	/**
	 * 过期日期(添加的时候+7776000秒)
	 */
	private Long expires;

	@Override
	public String toString() {
		return "ConnectQQ [openid=" + openid + ", userId=" + userId + ", accessToken=" + accessToken + ", refreshToken="
				+ refreshToken + ", expires=" + expires + "]";
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getExpires() {
		return expires;
	}

	public void setExpires(Long l) {
		this.expires = l;
	}
	public void addExpires(Long l) {
		this.expires = System.currentTimeMillis() / 1000 + l;
	}

}
