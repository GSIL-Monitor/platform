/**
 * @author Tony
 * @date 2018-01-10
 * @project rest_demo
 */
package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import cn.laeni.ucpaas.AbsRestClient;
import cn.laeni.ucpaas.JsonReqClient;

public class RestTest {

	static AbsRestClient InstantiationRestAPI() {
		return new JsonReqClient();
	}
	
	
	/**
	 * 测试说明  启动main方法后，请在控制台输入数字(数字对应 相应的调用方法)，回车键结束
	 * 参数名称含义，请参考rest api 文档
	 * @throws IOException 
	 * @method main
	 */
	public static void main(String[] args) throws IOException{
		String sid = "b326ea59c73c6bbb82305b6bcdfddad3";	//用户的账号唯一标识“Account Sid”，在开发者控制台获取
		String token = "17b84a2e373d18adf664589cb7fedb45";	//用户密钥，在开发者控制台获取
		String appid = "4b4efe8ba0c34dc0b46c4bba591a9eb4";	//创建应用时系统分配的唯一标示
		String templateid = "183986";						//创建短信模板时系统分配的唯一标示
		String param = "laeni";							//【	选填】模板中的替换参数，如该模板不存在参数则无需传该参数或者参数为空，如果有多个参数则需要写在同一个字符串中，以英文逗号分隔 （如：“a,b,c”），参数中不能含有特殊符号“【】”和“,”
		String mobile = "15125425127";						//接收的手机号，多个手机号码以英文逗号分隔 （如：“18011984299,18011801180”），最多单次支持100个号码，如果号码重复，则只发送一条，暂仅支持国内号码
		String uid = "4b4efe8ba0c34dc0b46c4bba591a9eb4";	//【	选填】用户透传ID，随状态报告返回
		String type = "";									//短信类型：0:通知短信、5:会员服务短信（需企业认证）、4:验证码短信(此类型content内必须至少有一个参数{1})
		String template_name = "";							//【	选填】短信模板名称，限6个汉字或20个数字、英文字、符号
		String autograph = "";								//短信签名，建议使用公司名/APP名/网站名，限2-12个汉字、英文字母和数字，不能纯数字
		String content = "";								//短信内容，最长500字，不得含有【】符号，可支持输入参数，参数示例“{1}”、“{2}”
		String page_num = "";								//【	选填】页码，默认值为1
		String page_size = "";								//【	选填】每页个数，最大100个，默认30个
		
		System.out.println("请输入方法对应的数字(例如1),Enter键结束:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String methodNumber = br.readLine();
		if (StringUtils.isBlank(methodNumber)){
			System.out.println("请输入正确的数字，不可为空");
			return;
		}
		
		if (methodNumber.equals("1")) {  
			try {
				String result=InstantiationRestAPI().sendSms(sid, token, appid, templateid, param, mobile, uid);
				System.out.println("Response content is: " + result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (methodNumber.equals("2")) { //指定模板群发
			try {
				String result=InstantiationRestAPI().sendSmsBatch(sid, token, appid, templateid, param, mobile, uid);
				System.out.println("Response content is: " + result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (methodNumber.equals("3")) {  //增加模板
			try {
				String result=InstantiationRestAPI().addSmsTemplate(sid, token, appid, type, template_name, autograph, content);
				System.out.println("Response content is: " + result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (methodNumber.equals("4")) {  //查询模板
			try {
				String result=InstantiationRestAPI().getSmsTemplate(sid, token, appid, templateid, page_num, page_size);
				System.out.println("Response content is: " + result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (methodNumber.equals("5")) {  //编辑模板
			try {
				String result=InstantiationRestAPI().editSmsTemplate(sid, token, appid, templateid, type, template_name, autograph, content);
				System.out.println("Response content is: " + result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (methodNumber.equals("6")) {  //删除模板
			try {
				String result=InstantiationRestAPI().deleterSmsTemplate(sid, token, appid, templateid);
				System.out.println("Response content is: " + result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 	
	}
}
