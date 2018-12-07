package cn.laeni.user.other.entity;

import java.io.Serializable;

public class VerifCode implements Serializable{

	private static final long serialVersionUID = 8786370211430638253L;
	
	private long addTime = System.currentTimeMillis();	// 添加时间
	private String value = null;	//验证码的值

	public VerifCode() {
	}

	public VerifCode(String value) {
		this.value = value;
	}

	/**
	 * 获取添加时时间戳
	 * @return
	 */
	public long getAddTime() {
		return addTime;
	}
	/**
	 * VerifCode
	 * @param addTime
	 */
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	/**
	 * 获取验证码的值
	 * @return
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 设置值
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "VerifCode [addTime=" + addTime + ", value=" + value + "]";
	}
}
