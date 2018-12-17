package cn.laeni.platform.user.other.entity;

import cn.laeni.platform.user.other.code.SystemCode;
import cn.laeni.platform.user.other.code.UserCode;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 通用返回码标准定义
 * 
 * @author laeni.cn
 *
 */
public class ApiJson implements Serializable {
	private static final long serialVersionUID = 7911624289323178723L;

	@JSONField(serialize=false)		// 控制器输出时不显示该属性
	private Object enumCode;
	private String code = "0";
	private String msg = "OK";
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