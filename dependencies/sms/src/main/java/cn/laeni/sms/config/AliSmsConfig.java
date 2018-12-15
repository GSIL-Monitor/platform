package cn.laeni.sms.config;

import java.io.Serializable;

public class AliSmsConfig implements Serializable {
	private static final long serialVersionUID = 1251241003444041432L;

	// 超时时间
	private int defaultConnectTimeout = 5000;
	private int defaultReadTimeout = 5000;
	private String accessKeyId = "LTAI5aPt3LRqaLKd"; // 密匙ID(LTAI30cCTsdk41G2)(LTAI5aPt3LRqaLKd)
	private String accessKeySecret = "Zc7FHmpmIt0yjkcXe7LYEbTgZTHP90";  // 密匙(vKMurm8xj30CfdpwWa1gAvYB9xjHCL)(Zc7FHmpmIt0yjkcXe7LYEbTgZTHP90)
	private String smsSignature = "Laeni网"; // 默认短信签名
	private String smsTemId = "SMS_132400015";// 默认短信模板(验证码)

	@Override
	public String toString() {
		return "AliSmsConfig{" +
				"defaultConnectTimeout=" + defaultConnectTimeout +
				", defaultReadTimeout=" + defaultReadTimeout +
				", accessKeyId='" + accessKeyId + '\'' +
				", accessKeySecret='" + accessKeySecret + '\'' +
				", smsSignature='" + smsSignature + '\'' +
				", smsTemId='" + smsTemId + '\'' +
				'}';
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getDefaultConnectTimeout() {
		return defaultConnectTimeout;
	}

	public void setDefaultConnectTimeout(int defaultConnectTimeout) {
		this.defaultConnectTimeout = defaultConnectTimeout;
	}

	public int getDefaultReadTimeout() {
		return defaultReadTimeout;
	}

	public void setDefaultReadTimeout(int defaultReadTimeout) {
		this.defaultReadTimeout = defaultReadTimeout;
	}

	/**
	 * 获取密匙ID
	 * @return
	 */
	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	/**
	 * 获取密匙
	 * @return
	 */
	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getSmsSignature() {
		return smsSignature;
	}

	public void setSmsSignature(String smsSignature) {
		this.smsSignature = smsSignature;
	}

	public String getSmsTemId() {
		return smsTemId;
	}

	public void setSmsTemId(String smsTemId) {
		this.smsTemId = smsTemId;
	}

}
