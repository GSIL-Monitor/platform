/**
 * 主要是URL的配置信息
 */
(function(global) {
	var HOME = "//laeni.cn",
		WWW = "//www.laeni.cn",
		/* 主页地址,一般情况访问www和root效果时一样的 */
		CDN = "//cdn.laeni.cn",
		USER = "//user.laeni.cn";
		
	// 支持的配置类型
	var types = ["url","lc"];

	_config = function(configTypeName) {
		// 判断参数类型是否合法
		if (typeof configTypeName !== "string") {
			throw new Error("参数需要接受一个String类型的值,但是接受了" + typeof configTypeName + '类型的值; 执行"Conf.help()"可查看支持的参数!!!');
		} else {
			var i=0
			while (i < types.length){
				if (types[i] == configTypeName) {
					break;
				}
				i++;
			}
			
			if (i == types.length) {
				throw new Error('没有名为"' + configTypeName + '"的参数, 支持的参数列表为:' + types.toString());
			}
		}
		
		
		/**
		 * URl地址配置
		 * 使用示例:
		 *     var urlConf = Conf("url");
		 *     // 获取嵌入式html页面地址
		 *     var loginHtml = urlConf.cdn.loginHtml;
		 */
		if (configTypeName == "url") {
			var
			home = {
				value: HOME,
			},
			www = {
				value: WWW
			},
			cdn = function(){
				var
				value = CDN,
				/**
				* 导航栏(临时) TODO
				*/
				headerHtml = value + "/html/navbar.html",
				/**
				 * html页面(非全页面的需要嵌入指定页面才能使用)
				 * 登录页静态资源地址(非全页面)
				 */
				loginHtml = value + "/html/login.html"
				
				return {
					value:value, headerHtml:headerHtml, loginHtml: loginHtml
				};
			}(),
			
			user = function(){
				var
				value = USER,
				/**
				 * 独立注册页面
				 */
				reg = value + "/reg.html",
				/**
				 * 独立登录页面
				 */
				login = value + "/login.html",
				/**
				 * 找回密码页面
				 */
				find_password = value + "/password_find.html",
				/**
				* 检测是否登录
				*/
				checkLogin = value + "/api/checkLogin",
				/**
				* 注销登录地址
				*/
				logout = value + "/api/logout"
		
				return {
					reg:reg,find_password:find_password,
					value:value, checkLogin:checkLogin, logout:logout
				};
			}();
			
			return {
				home: home,
				www: www,
				cdn: cdn,
				user: user
			};
		}
	
		// 第三方登录配置
		else if(configTypeName == "lc") {
			// QQ登录了配置
			var qq = function(){
				return {type:"QQ",
					// 应用ID
					appid:"101481333",
					// 回调地址
					redirecturi:"https://laeni.cn/callback/qq",
				};
			}();
			
			return {
				qq:qq
			}
		}
	};
	
	/**
	 * 帮助函数
	 */
	_config.prototype.help = function(){
		console.log('[配置 ::\n]' +
				'使用方法: Conf("配置类型").xxx.xxx'+
				'支持的配置类型名:' + types.toString());
	};

	global.Conf = _config;

	/**
	 * 保留做参考
	 */
	PATH = {
		/**
		 * TODO 独立登录页面
		 */


		/**
		 * TODO 找回密码界面
		 */


		/**
		 * TODO 注销该帐号界面
		 */


		/**
		 * TODO 注销该帐号规则说明页面
		 */



		/**
		 * TODO 个人中心页面
		 */

		/* 发送验证码
		 * ?name=laeni@qq.com
		 */
		GET_VERIFY_CODE: "/api/sendVerifyCode",

		/* 验证验证码
		 * ?verifyCode=123456
		 */
		CHECK_VERIFY_CODE: "/api/chekVerifyCode",

		/* 新用户注册地址: 提交手机或邮箱和相应的验证码进行注册
		 * 如果该帐号已经被注册则返回提交的帐号是否已经被注册,否则为该帐号注册后并登录(和强制注册一样)
		 * ?name=laeni@qq.com&verifyCode=123456
		 * force=force (即表示强制注册,无需返回帐号是否存在,如果存在则与原来的解绑) */
		REG_ACCOUNT: "/api/regAccount",

		/* 检测指定类型的帐号是否存在
		 * ?account&type */
		CHECK_LOGIN_ACCOUNT: "/api/checkLoginAccount",

		/* 动态更新用户信息
		 * ?nickname&password&... */
		SAVE_OR_UPDATE_USER_INFO: "/api/saveOrUpdateUserInfor",
	};

})(typeof window !== "undefined" ? window : this);
