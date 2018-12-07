/**
 * USER.islogin()			// 检测是否登录授权(并不是百分百正确,如果服务端检测到未授权则会拒绝访问并删除cookie"__ok")
 * USER.login()				// 加载并显示登陆窗口
 * USER.isLoginWindow()		// 登录窗口是否已经显示
 * USER.loginWindowOut()	// 关闭登录窗口
 * USER.signOut()			// 注销登录(删除相关Cookie并调用云端注销链接)
 * USER.getUserinfo()		// 获取用户基本信息(本地Cookie中的数据)
 * USER.getUserinfo_f()		// 获取用户基本信息(登录的情况下,如果未登录则获取失败,并且需要自定义回调函数来获取)
 * USER.callback()			// 登录完成回调函数
 * USER.addCallback()		// 添加登录完成回调函数(初始化函数),执行成功后再次返回函数本身,且回调函数中不调用loginWindowOut()函数来关闭窗口
 * USER.addOtherLogin()		// 用于给第三方登录SDK将其自己注册过来(以便在某个操作后统一执行相关操作)
 */
(function(global) {
	var _user = function() {

		if (typeof UT == "undefined") {
			console.error("需要UT工具库的支持,请先引入://cdn.laeni.cn/public/js/utlis.js");
			return null;
		}

		var
			other_login = [], // 第三方登录Util引用,引用地方发SDK时将自己添加到该数组(统一操作)
			backFunc = [], // 回调方法
			$ut = UT, // 工具库
			_CONF = typeof LAENI_CONFIG != "undefined" ? LAENI_CONFIG : undefined,
			url = _CONF ? _CONF.URL.USER : "//127.0.0.1:7000",
			// 存储已经加载的登录页(防止加载后取消登录,且再次登录重复加载)
			loginHtml, // 存储登录页
			// 登录页静态资源地址
			loginHtmlUrl = _CONF ? (_CONF.URL.CDN + _CONF.PATH.LOGIN_HTML) : "//cdn.laeni.cn/html/login.html",
			// 注销登录地址
			logout_url = url + (_CONF ? _CONF.PATH.LOGOUT_URL : "/api/logout"),
			user_info = url + (_CONF ? _CONF.PATH.GET_USER_INFO : "/api/logout"),
			
			$loginWindw;//登录窗口
			
		/**
		 * 检测是否登录(在调用云端需要授权的功能时都应该先调用该函数确认)
		 * 因为在登录成功后再本地存储用户的基本信息(包括userId,nickname,头像地址,__ok)
		 * __ok表示是否通过云端验证,如果通过则可以请求云端操作(云端会再次验证,如果非法则自动清空该值)
		 */
		var _islogin = function() {
			return $ut.cookie.get("user_id") && $ut.cookie.get("__ok") ? true : false;
		}

		/**
		 * 调用该函数就会打开登录窗口
		 */
		var _login = function() {
			// 加载登录页面并显示
			if ($loginWindw) {
				// 开启蒙版
				$ut.mb.on();
				$loginWindw.removeClass("d-none");
				
			} else {
				$.ajax({
					url: loginHtmlUrl,
					xhrFields: {
						thCredentials: true
					},
					success: function(re) {
						// 开启蒙版
						$ut.mb.on();
						$("body").append(re);
						$loginWindw = $("#login");
					},
					error: function() {
						console.log("出错了!!!请检查网络后刷新重试...")
					}
				});
			}
		}

		/* 登录窗口是否已经显示 */
		var _isLoginWindow = function(){
			return !$loginWindw.hasClass("d-none");
		}

		/**
		 * 关闭登录窗口
		 */
		var _windowOut = function(){
			// 关闭蒙版
			$ut.mb.off();
			// 隐藏登录窗口
			$loginWindw.addClass("d-none");
			
			_callback();
		}
		
		/**
		 * 注销登录
		 */
		var _signOut = function() {
			// 从Cookie中删除用户信息
			$ut.cookie.del("__ok");
			$ut.cookie.del("user_id");
			$ut.cookie.del("nickname");
			$ut.cookie.del("grade");

			// 调用第三方登录SDK注销第三方登录
			for (var i = 0, l = other_login.length; i < l; i++) {
				other_login[i].signOut();
			}

			// 调用云端注销地址
			$.get(logout_url);
		}

		/**
		 * 获取用户基本信息
		 */
		var _getUserinfo = function() {
			return $ut && $ut.cookie.getAll();
		}
		
		/**
		 * 获取用户基本信息(从云端获取,获取的内容通过传入的回调函数传回)
		 */
		var _getUserinfo_f = function(callback) {
			if (_islogin() && typeof callback == "function") {
				$.ajax({
					url: user_info,
					type: "POST",
					xhrFields: {
						thCredentials: true
					},
					success: function(re) {
						callback(re);
					},
					error: function(re) {
						callback(re);
					},
				});
			}
		}

		/**
		 * 登录完成回调函数(登录窗口关闭后执行)
		 * 执行成功返回true
		 */
		var _callback = function() {
			// 执行用户定义的回调函数
			if (typeof backFunc == "function") {
				backFunc();
			} else if (typeof backFunc == "object" && backFunc instanceof Array) {
				for (var i = 0, l = backFunc.length; i < l; i++) {
					backFunc[i]();
				}
			}
			return true;
		}

		/**
		 * 添加登录完成回调函数(再次返回函数本身)
		 */
		var _addCallback = function(func) {
			if(typeof func != "undefined" && func instanceof Function ){
				func();
				backFunc[backFunc.length] = func;
			}
			
			return this;
		}

		/**
		 * 用于给第三方登录SDK将其自己注册过来
		 */
		var _other_login = function(othre) {
			if (typeof other_login != "object") {
				other_login = [];
			}
			other_login[other_login.length] = othre;
		}


		// 需要公开的方法写在这里
		return {
			islogin: _islogin,				// 检测是否登录授权(并不是百分百正确,因为这里只是检测Cookie中是否有__ok,如果服务端检测到未授权则会拒绝访问并删除cookie"__ok")
			login: _login,					// 登录(加载并显示登陆窗口)
			isLoginWindow:_isLoginWindow,	// 登录窗口是否已经显示
			loginWindowOut:_windowOut,		// 关闭登录窗口
			signOut: _signOut,				// 注销登录(删除相关Cookie并调用云端注销链接)
			getUserinfo: _getUserinfo,		// 获取用户基本信息(本地Cookie中的数据)
			getUserinfo_f: _getUserinfo_f,	// 获取用户基本信息(登录的情况下,如果未登录则获取失败,并且需要自定义回调函数来获取)
			callback: _callback,			// 登录完成回调函数(登录窗口关闭后执行)
			addCallback: _addCallback,		// 添加登录完成回调函数
			addOtherLogin: _other_login,	// 用于给第三方登录SDK将其自己注册过来(以便在某个操作后统一执行相关操作)
		};
	}();

	/* 冻结对象并向外暴露唯一变量 */
	if (typeof Object.freeze == "function") {
		Object.freeze(_user);
	}
	global.USER = _user;
})(typeof window !== "undefined" ? window : this);
