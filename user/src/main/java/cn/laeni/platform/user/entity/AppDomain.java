package cn.laeni.platform.user.entity;

import java.io.Serializable;

/**
 * 域名 - AppId
 * @author laeni.cn
 *
 */
public class AppDomain  implements Serializable {

	private static final long serialVersionUID = -3852153214650051038L;

	/**
	 * 对应的域名 varchar(32)
	 */
	private String domain;
	/**
	 * 应用ID varchar(16)
	 */
	private String appId;

	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	@Override
	public String toString() {
		return "AppDomain [domain=" + domain + ", appId=" + appId + "]";
	}

}
