/**
 * 主要是URL的配置信息
 */
(function() {
	var HOME = "//laeni.cn",
		WWW = "//www.laeni.cn",
		/* 主页地址,一般情况访问www和root效果时一样的 */
		CDN = "//cdn.laeni.cn",
		/* 需要动态加载的HTML页面地址 */
		HTML = CDN,
		USER = "//user.laeni.cn";

	window.CONFIG = function() {
		
	};

	window.CONFIG.url = function() {
		var
		home = {
			value: HOME,
		},
		www = {
			value: WWW
		},
		cdn = {
			value: CDN
		},
		html = {
			value: HTML
		},
		user = {
			value: USER,
			/**
			 * 独立注册页面页面
			 */
			// reg_html: value + "/reg.html",
			/**
			 * 导航栏(临时) TODO
			 */
			header_html: cdn.value + "/html/header_1.html"
		};
		
		return {
			home: home,
			www: www,
			cdn: cdn,
			html: html,
			user: user
		};
	}();

	PATH = {
		/* api 接口--------------------------------------------------- */
		/**
		 * 注销登录地址
		 */
		LOGOUT_URL: "/api/logout",
		/**
		 * TODO 获取用户资料
		 */
		GET_USER_INFO: "",



		/* html页面(非全页面的需要嵌入指定页面才能使用)-------------------- */
		/**
		 * 登录页静态资源地址(非全页面)
		 */
		LOGIN_HTML: "/html/login.html",

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

})();
