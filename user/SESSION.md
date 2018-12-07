# Session 键名一览

1. 存储注册验证码(regVerifCode)
> 	Map<String, List<VerifCode>> [
> 	    // 正在用于注册的帐号(一般为邮箱或手机号)
> 		beingRegName=1058259177@qq.com,
> 	    // 输入错误的次数
> 		beingRegFre=0,
> 	    // 使用数组存储帐号的验证码
> 		beingRegValue={
> 			1058259177@qq.com=[
> 	            // 添加时间,值
> 				{addTime=1530858472236, value=810882},
> 				{addTime=1530858532888, value=503132},
> 				{addTime=1530859029307, value=610782}
> 			],
> 			1512542527=[
> 	            // 添加时间,值
> 				{addTime=1530858472236, value=810882},
> 				{addTime=1530858532888, value=503132},
> 				{addTime=1530859029307, value=610782}
> 			],
> 		}
> 	]
2. ***