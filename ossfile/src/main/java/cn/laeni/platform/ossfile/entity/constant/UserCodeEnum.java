package cn.laeni.platform.ossfile.entity.constant;

import cn.laeni.platform.ossfile.entity.other.EnumCode;

/**
 * 用户状态
 * ACCOUNT_RESERVED		100	帐号被系统保留
 * ACCOUNT_ILLEGAL		102	非法帐号
 * 
 * USER_DISABLE			103	帐号被禁用
 * USER_EXIST			104	用户存在
 * USER_NULL			105	用户不存在
 * 
 * PASSWORD_ERROR		110	密码错误
 * ACCORPASS_ERROR		111	用户名或密码错误
 * 
 * VERIFYCODE_EXPIRE	120	验证码无效,请重新获取
 * VERIFYCODE_ERROR		121	验证码错误
 * 						122	验证码发送次数过多
 * 
 * ILLEGAL_DATA			131	资料填写有误，请检查后再试
 * 
 * LOGIN_INVALID		141 登录失效
 * @author laeni.cn
 */
public enum UserCodeEnum implements EnumCode {
	/**
	 * 帐号被系统保留
	 */
	ACCOUNT_RESERVED("100","帐号被系统保留"),
	/**
	 * 资料填写有误，请检查后再试
	 */
	ILLEGAL_DATA("101","资料填写有误，请检查后再试"),
	/**
	 * 非法帐号
	 */
	ACCOUNT_ILLEGAL("102","非法帐号"),
	/**
	 * 帐号被禁用
	 */
	USER_DISABLE("103","帐号被禁用"),
	/**
	 * 用户存在
	 */
	USER_EXIST("104","用户存在"),
	/**
	 * 用户不存在
	 */
	USER_NULL("105","用户不存在"),
	/**
	 * 密码错误
	 */
	PASSWORD_ERROR("105","密码错误"),
	/**
	 * 用户名或密码错误
	 */
	ACCORPASS_ERROR("105","用户名或密码错误"),
	/**
	 * 验证码无效,请重新获取
	 */
	VERIFYCODE_EXPIRE("120","验证码无效,请重新获取"),
	/**
	 * 验证码错误
	 */
	VERIFYCODE_ERROR("121","验证码错误"),
	/**
	 * 登录失效
	 */
	LOGIN_INVALID("141","登录失效或未登录");

	private String code;
	private String msg;

	UserCodeEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	@Override
	public String getCode() {
		return code;
	}
	@Override
	public String getMsg() {
		return msg;
	}
}
