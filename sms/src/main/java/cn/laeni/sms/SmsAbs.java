package cn.laeni.sms;

import com.aliyuncs.exceptions.ClientException;

import java.util.Map;

public interface SmsAbs {
	/**
	 * 指定模板单发
	 * @param PhoneNumbers	短信接收号码,支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
	 * 	
	 * @param SignName 短信签名
	 * 	
	 * @param smsTemId	短信模板ID
	 * 	
	 * @param TemplateParam	短信模板变量替换JSON串,友情提示:如果JSON中需要带换行符,请参照标准的JSON协议。
	 * 	
	 * @return JSON字符串
	 * 	requestId:请求ID(如:8906582E-6722)
	 *  code:状态码-返回OK代表请求成功,其他错误码详见错误码列表(如:OK)
	 *  message:状态码描述(如:请求成功)
	 *  bizId:发送回执ID,可根据该ID查询具体的发送状态(如:134523^4351232)
	 */
	public Map<String,String> sendSms(String phoneNumbers,String signName,String smsTemId ,String templateParam) throws ClientException;
	
	/**
	 * 指定模板群发
	 * @param PhoneNumberJson 短信接收号码,JSON格式,批量上限为100个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
	 * 	[“15000000000”,”15000000001”]
	 * @param SignNameJson 短信签名,JSON格式
	 * 	[“云通信”,”云通信”]
	 * @param TemplateCode 短信模板ID
	 * 	SMS_0000
	 * @param TemplateParamJson 短信模板变量替换JSON串,友情提示:如果JSON中需要带换行符,请参照标准的JSON协议
	 * 	[{“code”:”1234”,”product”:”ytx1”},{“code”:”5678”,”product”:”ytx2”}]
	 * @return JSON字符串
	 * 	RequestId:请求ID(如:8906582E-6722)
	 *  Code:状态码-返回OK代表请求成功,其他错误码详见错误码列表(如:OK)
	 *  Message:状态码描述(如:请求成功)
	 *  BizId:发送回执ID,可根据该ID查询具体的发送状态(如:134523^4351232)
	 */
	public String sendBatchSms(String PhoneNumberJson,String SignNameJson,String TemplateCode,String TemplateParamJson);
	
	/**
	 * 短信查询用于查询短信发送的状态，是否成功到达终端用户手机
	 * @param PhoneNumber 短信接收号码,如果需要查询国际短信,号码前需要带上对应国家的区号,区号的获取详见国际短信支持国家信息查询API接口
	 * 15000000000
	 * @param BizId (可选)发送流水号,从调用发送接口返回值中获取
	 * 1234^1234
	 * @param SendDate 短信发送日期格式yyyyMMdd,支持最近30天记录查询
	 * 20181212
	 * @param PageSize 页大小Max=50
	 * 10
	 * @param CurrentPage 当前页码 
	 * 1
	 * @return JSON字符串
	 * 
	 */
	public String querySendDetails(String PhoneNumber,String BizId,String SendDate,int PageSize,int CurrentPage);
}



