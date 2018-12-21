package cn.laeni.platform.code;

/**
 * 系统状态
 * SUCCESS			000	OK
 * ILLEGAL			-1	非法请求
 * CONTEXT_EMBAR	-2	使用环境严重异常,疑似黑客,请更换设备再试
 * OTHER			001	系统内部错误
 * EXCEED_VERIFY	002	请求次数过多,需要验证
 * EXCEED			003 请求次数过多,请稍候再试
 * CONTEXT_OFFSITE	004	不在常用地,需要验证
 */
public enum SystemCode {
	/**
	 * 成功
	 */
	SUCCESS("000", "OK"),
	/**
	 * 非法请求
	 */
	ILLEGAL("-1","非法请求"),
	/**
	 * 使用环境严重异常，请更换设备再试
	 */
	CONTEXT_EMBAR("-2","使用环境严重异常，请更换设备再试"),
	/**
	 * 系统内部错误
	 */
	OTHER("001","系统内部错误"),
	/**
	 * 请求次数过多,需要验证
	 */
	EXCEED_VERIFY("002","请求次数过多,需要验证"),
	/**
	 * 请求次数过多,请稍候再试
	 */
	EXCEED("003","请求次数过多,请稍候再试");
	
	private String code;
	private String msg;

	SystemCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}