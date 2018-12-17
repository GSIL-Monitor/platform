<?
接入参数{
	accountSid(开发者账号ID){
		a.开发者在UcPaaS平台的唯一标示，在SDK登录、REST请求中使用；
		b.开发者账号ID在网站注册后，系统自动生成，不可以更改
	}
		b326ea59c73c6bbb82305b6bcdfddad3
	authToken(账户授权令牌){
		a.相当于开发者账号ID（Account Sid）的密码，在SDK登录、REST请求中使用；
		b.系统自动生成；如果出现开发者Token泄露的情况，系统支持开发者Token重置。
		c.需要注册手机进行验证后方可查看
	}
		17b84a2e373d18adf664589cb7fedb45
	Rest URL
		https://api.ucpaas.com
	appId:
		4b4efe8ba0c34dc0b46c4bba591a9eb4
		
}
($appId,$to,$templateId,$param)

对接接口：{
	为了确保数据隐私和安全， REST API须通过HTTPS方式请求 URL格式 
	https://api.ucpaas.com/2014-06-30/Accounts/b326ea59c73c6bbb82305b6bcdfddad3/{function}/{operation}?sig={SigParameter}
	https://api.ucpaas.com/{SoftVersion}/Accounts/{accountSid}/{function}/{operation}?sig={SigParameter}

	URL参数属性说明{
		属性			说明
		SoftVersion		云之讯REST API版本号，当前版本号为：2014-06-30
		Account			此参数为默认固定值
		accountSid		注册云之讯官网，在控制台中即可获取此参数
		function		业务功能
		operation		业务操作，业务功能的各类具体操作分支
		SigParameter		请求URL必须带有此参数{
			1、URL后必须带有sig参数，sig= MD5（账户Id + 账户授权令牌 + 时间戳），共32位(注:转成大写)
			2、使用MD5加密（账户Id + 账户授权令牌 + 时间戳），共32位
			3、时间戳是当前系统时间（24小时制），格式“yyyyMMddHHmmss”。时间戳有效时间为50分钟
		}
	}
} 

HTTP标准包头字段(必填) {
	属性				说明
	Accept				客户端响应接收数据格式：application/xml、application/json
	Content-Type		类型：application/xml;charset=utf-8、application/json;charset=utf-8
	Authorization		验证信息{
		1、使用Base64编码（账户Id + 冒号 + 时间戳） 
		2、冒号为英文冒号
		3、时间戳是当前系统时间（24小时制），格式“yyyyMMddHHmmss”，需与SigParameter中时间戳相同
	}
	Content-Length		包体长度
}

短信接口{
	1.appId ：创建应用时系统分配的唯一标示，在“应用列表”中可以查询 
	2.templateId ：创建短信模板时系统分配的唯一标示，在“短信管理”中可以查询
	3. to ：需要下发短信的手机号码,支持国际号码，需要加国家码。 
	4.param ：模板中的替换参数，如果有多个参数则需要写在同一个字符串中，以逗号分隔. （如：param=“a,b,c”）
	JSON 请求示例/*
		POST/2014-06-30/Accounts/b326ea59c73c6bbb82305b6bcdfddad3/Messages/templateSMS?sig=769190B9A223549407D2164CAE92152E
		Host:api.ucpaas.com
		Accept:application/json
		Content-Type:application/json;charset=utf-8
		Authorization:YjMyNmVhNTljNzNjNmJiYjgyMzA1YjZiY2RmZGRhZDM6MjAxNzEwMjMxMDE4NDY=
		{
		 "templateSMS" : {
			"appId"       : "4b4efe8ba0c34dc0b46c4bba591a9eb4",
			"param"       : "0000",
			"templateId"  : "1",
			"to"          : "15125425127"
			}
		}
	*/
	JSON 响应示例 /*
		{
		 "resp"        : {
			"respCode"    : "000000",
			"failure"     : 1,
			"templateSMS" : {
				"createDate"  : 20140623185016,
				"smsId"       : "f96f79240e372587e9284cd580d8f953"
				}
			}
		}
	*/
}

短信状态报告推送接口{
		短信验证码状态报告推送接口，平台将验证码通过手机短信的形式发送到用户手机上，并将短信验证码发送状态推送到开发者服务器上(需在开发者后台自助配置接收状态报告服务器的URL地址)。 
	• 推送包体

	属性	类型	约束	说明
	type	String	必选	1:状态报告，2：上行
	smsid	String	必选	回状态报告给客户时填充smsId
	status	String	必选	0:成功；1：提交失败，4：失败，5：关键字（keys），6：黑/白名单，7：超频（overrate），8：unknown
	reportTime	String	必选	状态报告返回时间
	desc	String	必选	
	• XML推送示例

	POST /coolweb/voiceCode HTTP/1.1 
	Host: 172.16.10.32:8080 
	Content-Type:text/xml;charset=utf-8 
	Accept:application/xml 
	Content-Length: 461
	 
	<?xml version="1.0"?> 
	<request>     
		<type>1</type>    
		<smsid>******</smsid>
		<status>******</status>
		<reportTime>******</reportTime>
		<desc>******</desc>
	</request> 
	• 响应包体

	属性	类型	约束	说明
	retcode	int	必选	返回错误码，0：成功，非0：失败
	• XML响应示例

	<?xml version="1.0" encoding="UTF-8"?>
	<response>
		<retcode>0</retcode>
	</response> 
}