package cn.laeni.platform.entity;

import cn.laeni.platform.code.OAuthCode;
import cn.laeni.platform.code.SystemCode;
import cn.laeni.platform.user.code.UserCode;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 通用返回码标准定义
 * 
 * @author laeni.cn
 *
 */
public class ApiJson implements Serializable {
	/**
	 * 控制器输出时不显示该属性
	 */
	@JSONField(serialize=false)
	private Object enumCode;
	private String code;
	private String msg;
	private Object data;

	/**
	 * 系统状态
	 * 
	 * @param systemCode
	 */
	public ApiJson(SystemCode systemCode) {
		this.enumCode = systemCode;
		this.code = systemCode.getCode();
		this.msg = systemCode.getMsg();
	}

	/**
	 * 用户状态
	 *
	 * @param userCode
	 */
	public ApiJson(UserCode userCode) {
		this.enumCode = userCode;
		this.code = userCode.getCode();
		this.msg = userCode.getMsg();
	}

	/**
	 * OAuth2.0
	 *
	 * @param userCode
	 */
	public ApiJson(OAuthCode userCode) {
		this.enumCode = userCode;
		this.code = userCode.getCode();
		this.msg = userCode.getMsg();
	}

	public Object getEnumCode() {
		return enumCode;
	}

	public void setEnumCode(Object enumCode) {
		this.enumCode = enumCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ApiJson [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}

}