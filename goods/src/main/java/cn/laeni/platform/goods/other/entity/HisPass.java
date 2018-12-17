package cn.laeni.platform.goods.other.entity;

/**
 * 表示历史密码对象中的子对象,每一个对象代表一次历史密码
 * @author laeni.cn
 */
public class HisPass{
	private String value;	// 值
	private Long changeTime;	// 修该密码的时间
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Long getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(Long changeTime) {
		this.changeTime = changeTime;
	}
}