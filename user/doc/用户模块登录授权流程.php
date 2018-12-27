<?
User模块授权流程

1.开始登录时生成一个一次性标识"登录CODE",在登录时发送到用户模块后台
	登录CODE:
		[临时且唯一,可选,如果在调用登录函数时传入则使用传入的,否则自动生成一个"(当前毫秒时间戳后7位+6为随机数)转为36进制=8位字符串"]
		Tools.number.string10to62(new Date().getTime().toString().substr(6) + Tools.number.randomNum(100000,999999))
		用于到调用方后台查询本次登录的用户信息(即判断用户是否登录)
	应用ID(client_id): 辅助授权
	state: 不做任何处理,原样带回
	
	授权地址(authorize_uri): 不用发给"用户模块后台"

2.登录:
	直接使用用户名密码进行登录:
		a."客户端"向"用户模块后台"提交: "登录CODE"+"应用ID"+"相关授权信息(用户名+密码)"
		b.登录成功后"用户模块后台"向"客户端"返回: JSON格式数据(其中包括"登录CODE"和"授权CODE")
		c."客户端"向"本系统后台"提交: "登录CODE[必须]"+"授权CODE[必须]"
			c1."本系统后台"向"用户模块后台"提交: "应用ID"+"登录CODE"+"应用密匙(防止他人冒用该应用)"
			c2."用户模块后台"向"本系统后台"返回: "授权令牌(access_token)"+"秒为单位的有效期(expires_in)"+"续期令牌(refresh_token)"
			c3."本系统后台"向"用户模块后台"调用接口查询用户数据
		d."本系统后台"向"客户端"返回相关数据(包括是否登录成功)

	打开第三方登录(一定要使用重定向到回调地址):
		a."客户端"向"第三方登录"提交: 包含->"登录CODE"+"应用ID"+"相关授权信息(QQ等第三方登录成功凭证)"
		b.登录成功后"用户模块后台"带上"登录CODE"和"授权CODE"重定向到"本系统后台(回调地址)"
			c1."本系统后台"向"用户模块后台"提交: "应用ID"+"登录CODE"+"应用密匙(防止他人冒用该应用)"
			c2."用户模块后台"向"本系统后台"返回: "授权令牌(access_token)"+"秒为单位的有效期(expires_in)"+"续期令牌(refresh_token)"
			c3."本系统后台"向"用户模块后台"调用接口查询用户数据
		c."本系统后台"关闭登录窗口
		d."客户端"使用"登录CODE"+"应用ID"到"本系统后台"查询是否登录

3.登录成功后:
	弹窗登录: 关闭登录窗口
	新标签登录: 返回登录前页面

弹窗:
	直接:
		客户端 => USER : 用户名 + 密码 + 应用ID
			返回: 认证CODE
		客户端 => 授权地址 : 应用ID + 认证CODE
			返回: 成功 > 关闭Modal
	第三方
		客户端 => QQ : QQ需要的认证信息 + 应用ID + 回调地址 + 登录标识CODE
			QQ回调USER : 应用ID + 回调地址 + 登录标识CODE
				USER回调本地: 认证CODE + 登录标识CODE + 应用ID
					成功 > 重定向到源地址(或新用户先重定向到资料完善页,再由资料完善页重定向到源地址)
		客户端 => 后台 : 登录标识CODE
			返回: 成功 > 关闭Modal
新窗口:
	直接:
		客户端 => USER : 用户名 + 密码 + 应用ID
			返回: 认证CODE
		客户端 => 授权地址 : 应用ID + 认证CODE
			返回: 成功 > 重定向到原始页
	第三方
		客户端 => QQ : QQ需要的认证信息 + 应用ID + 回调地址 + 登录标识CODE
			QQ回调USER : 应用ID + 回调地址 + 登录标识CODE
				USER回调本地: 认证CODE + 登录标识CODE + 应用ID
					成功 > 重定向到源地址(或新用户先重定向到资料完善页,再由资料完善页重定向到源地址)
		客户端 => 后台 : 登录标识CODE
			返回: 成功 > 重定向到原始页区别:
	1.1.直接登录使用ajax授权
    1.2.使用第三方登录需要回调授权
	2.1.后端如果检测到"登录标识CODE"说明是重定向过去的,成功后将"登录标识CODE"进行存储并关闭登录窗口
	2.2.后端如果没有检测到"登录标识CODE",说明是ajax请求的,返回成功数据即可
	3.user-SDK如果检测到时独立登录页则跳转到登陆前页面(找不到页面则转到hemo页)