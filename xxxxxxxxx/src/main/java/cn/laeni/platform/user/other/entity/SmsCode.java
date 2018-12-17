package cn.laeni.platform.user.other.entity;

import java.io.Serializable;

public class SmsCode implements Serializable{
	private static final long serialVersionUID = -1676886370363410673L;

	private long addTime;
	private String value;
	
	public long getAddTime() {
		return addTime;
	}
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "{addTime=" + addTime + ", value=" + value + "}";
	}
}
