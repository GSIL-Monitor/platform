package cn.laeni.platform.user.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegVerifCode implements Serializable{
	private static final long serialVersionUID = 6992962724442084438L;
	
	private String beingRegName=null;	// 正在用于注册的帐号(一般为邮箱或手机号)
	private int beingRegFre = 0;			// 记录输入错误的次数
	private Map<String, List<VerifCode>> beingRegValue = new HashMap<>();

	@Override
	public String toString() {
		return "RegVerifCode{" +
				"beingRegName='" + beingRegName + '\'' +
				", beingRegFre=" + beingRegFre +
				", beingRegValue=" + beingRegValue +
				'}';
	}

	/**
	 * 获取当前正在用于注册的帐号
	 * @return
	 */
	public String getBeingRegName() {
		return beingRegName;
	}
	/**
	 * 设置当前正在用于注册的帐号
	 * @param beingRegName
	 */
	public void setBeingRegName(String beingRegName) {
		this.beingRegName = beingRegName;
	}
	/**
	 * 获取输入错误的次数
	 * @return
	 */
	public int getBeingRegFre() {
		return beingRegFre;
	}
	/**
	 * 设置输入错误的次数
	 * @param beingRegFre
	 */
	public void setBeingRegFre(int beingRegFre) {
		this.beingRegFre = beingRegFre;
	}

	/*
	 * 使输入错误的次数加1
	 */
	public void addBeingRegFre() {
		this.beingRegFre++;
	}
	/**
	 * 以数组形式返回当前正在注册的帐号下的所以验证码
	 * @return
	 */
	public List<VerifCode> getBeingRegValueList(String account) {
		return beingRegValue.get(account);
	}
	/**
	 * 将当前正在注册的帐号下的所以验证码以数组形式存储
	 * @param beingRegValueList
	 */
	public void setBeingRegValueList(List<VerifCode> beingRegValueList) {
		this.beingRegValue.put(beingRegName, beingRegValueList);
	}
	/**
	 * 获取所有验证码
	 * @return
	 */
	public Map<String, List<VerifCode>> getBeingRegValue() {
		return beingRegValue;
	}
	/**
	 * 设置所有验证码
	 * @param beingRegValue
	 */
	public void setBeingRegValue(Map<String, List<VerifCode>> beingRegValue) {
		this.beingRegValue = beingRegValue;
	}
}
