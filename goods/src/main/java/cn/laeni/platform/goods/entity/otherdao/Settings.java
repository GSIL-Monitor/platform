package cn.laeni.platform.goods.entity.otherdao;

import java.io.Serializable;

public class Settings implements Serializable{

	private static final long serialVersionUID = -4513140540347251119L;
	
	private String cloudName;
	private String settingName;
	private String value;
	
	public String getCloudName() {
		return cloudName;
	}
	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}
	public String getSettingName() {
		return settingName;
	}
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Settings [cloudName=" + cloudName + ", settingName=" + settingName + ", value=" + value + "]";
	}
}
