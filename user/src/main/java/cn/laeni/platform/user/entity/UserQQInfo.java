package cn.laeni.platform.user.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class UserQQInfo {
	private int ret; // 返回码
	private String msg; // 如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码
	@JSONField(name = "is_lost")
	private int isLost;
	private String nickname; // 用户在QQ空间的昵称
	private String gender; // 性别。 如果获取不到则默认返回"男"
	private String province; // 省
	private String city; // 市
	private String year; // 生日(年)
	private String constellation;
	private String vip;
	private String level;

	@JSONField(name = "figureurl")
	private String figureurl30; // QQ空间头像URL(30px)

	@JSONField(name = "figureurl_1")
	private String figureurl50; // QQ空间头像URL(50px)

	@JSONField(name = "figureurl_2")
	private String figureurl100; // QQ空间头像URL(100px)

	@JSONField(name = "figureurl_qq_1")
	private String figureurlQQ40; // QQ头像URL(40px)
	
	@JSONField(name = "figureurl_qq_2")
	private String figureurlQQ100; // QQ头像URL(100px-并非所有用户都拥有)
	
	@JSONField(name = "is_yellow_vip")
	private String isYellowVip;
	
	@JSONField(name = "yellow_vip_level")
	private String yellowVipLevel;
	
	@JSONField(name = "is_yellow_year_vip")
	private String isYellowYearVip;

	@Override
	public String toString() {
		return "UserQQInfo [ret=" + ret + ", msg=" + msg + ", isLost=" + isLost + ", nickname=" + nickname + ", gender="
				+ gender + ", province=" + province + ", city=" + city + ", year=" + year + ", constellation="
				+ constellation + ", vip=" + vip + ", level=" + level + ", figureurl30=" + figureurl30
				+ ", figureurl50=" + figureurl50 + ", figureurl100=" + figureurl100 + ", figureurlQQ40=" + figureurlQQ40
				+ ", figureurlQQ100=" + figureurlQQ100 + ", isYellowVip=" + isYellowVip + ", yellowVipLevel="
				+ yellowVipLevel + ", isYellowYearVip=" + isYellowYearVip + "]";
	}

	/**
	 * 获取返回码
	 * @return
	 */
	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	/**
	 * 获取提示信息<br/>
	 * 如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码
	 * @return
	 */
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getIsLost() {
		return isLost;
	}

	public void setIsLost(int isLost) {
		this.isLost = isLost;
	}

	/**获取用户在QQ空间的昵称
	 * 
	 * @return
	 */
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * 获取性别<br/>如果获取不到则默认返回"男"
	 * @return
	 */
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * 获取用户归属地(省)
	 * @return
	 */
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * 获取用户归属地(市)
	 * @return
	 */
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 获取用户生日(年)
	 * @return
	 */
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * 获取用户30pxQQ空间头像URL
	 * @return
	 */
	public String getFigureurl30() {
		return figureurl30;
	}

	public void setFigureurl30(String figureurl30) {
		this.figureurl30 = figureurl30;
	}

	/**
	 * 获取用户50pxQQ空间头像URL
	 * @return
	 */
	public String getFigureurl50() {
		return figureurl50;
	}

	public void setFigureurl50(String figureurl50) {
		this.figureurl50 = figureurl50;
	}

	/**
	 * 获取用户100pxQQ空间头像URL
	 * @return
	 */
	public String getFigureurl100() {
		return figureurl100;
	}

	public void setFigureurl100(String figureurl100) {
		this.figureurl100 = figureurl100;
	}

	/**
	 * 获取用户40pxQQ头像URL
	 * @return
	 */
	public String getFigureurlQQ40() {
		return figureurlQQ40;
	}

	public void setFigureurlQQ40(String figureurlQQ40) {
		this.figureurlQQ40 = figureurlQQ40;
	}

	/**
	 * 获取用户100pxQQ头像URL
	 * @return
	 */
	public String getFigureurlQQ100() {
		return figureurlQQ100;
	}

	public void setFigureurlQQ100(String figureurlQQ100) {
		this.figureurlQQ100 = figureurlQQ100;
	}

	public String getIsYellowVip() {
		return isYellowVip;
	}

	public void setIsYellowVip(String isYellowVip) {
		this.isYellowVip = isYellowVip;
	}

	public String getYellowVipLevel() {
		return yellowVipLevel;
	}

	public void setYellowVipLevel(String yellowVipLevel) {
		this.yellowVipLevel = yellowVipLevel;
	}

	public String getIsYellowYearVip() {
		return isYellowYearVip;
	}

	public void setIsYellowYearVip(String isYellowYearVip) {
		this.isYellowYearVip = isYellowYearVip;
	}
	
}
