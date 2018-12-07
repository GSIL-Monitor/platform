/**
 * 主要是URL的配置信息
 */
(function(){
	var ROOT = "//laeni.cn",
		WWW = "//www.laeni.cn",
		/* 主页地址,一般情况访问www和root效果时一样的 */
		HOME = ROOT,
		CDN = "//cdn.laeni.cn",
		/* 需要动态加载的HTML页面地址 */
		HTML = CDN,
		USER = "//user.laeni.cn";
		
		
	window.LAENI_CONFIG = {
		URL : {
			ROOT : ROOT, WWW : WWW, HOME : HOME, CDN : CDN, HTML : HTML, USER : USER,
		},
		PATH : {
			/* api 接口--------------------------------------------------- */
			/**
			 * 注销登录地址
			 */
			LOGOUT_URL : "/api/logout",
			/**
			 * TODO 获取用户资料
			 */
			GET_USER_INFO: "",
			
			
			
			/* html页面(非全页面的需要嵌入指定页面才能使用)-------------------- */
			/**
			 * 登录页静态资源地址(非全页面)
			 */
			LOGIN_HTML : "/html/login.html",
			
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
			
			
			/**
			 * 独立注册页面页面
			 */
			REG_HTML : "/reg/reg.php",
			
			/* 发送验证码
			 * ?name=laeni@qq.com
			 */
			GET_VERIFY_CODE : "/api/sendVerifyCode",
			
			/* 验证验证码
			* ?verifyCode=123456
			*/
			CHECK_VERIFY_CODE : "/api/chekVerifyCode",
			
			/* 新用户注册地址: 提交手机或邮箱和相应的验证码进行注册
			* 如果该帐号已经被注册则返回提交的帐号是否已经被注册,否则为该帐号注册后并登录(和强制注册一样)
			* ?name=laeni@qq.com&verifyCode=123456
			* force=force (即表示强制注册,无需返回帐号是否存在,如果存在则与原来的解绑) */
			REG_ACCOUNT : "/api/regAccount",
			
			/* 检测指定类型的帐号是否存在
			* ?account&type */
			CHECK_LOGIN_ACCOUNT : "/api/checkLoginAccount",
			
			/* 动态更新用户信息
			* ?nickname&password&... */
			SAVE_OR_UPDATE_USER_INFO : "/api/saveOrUpdateUserInfor",
			
		}
	}
})();
