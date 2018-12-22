/**
 * 如果为通过环境检查时,User对象为null
 * 引入User SDK 后,
 */

/**
 * +USER.islogin()			// 检测是否登录授权(并不是百分百正确,如果服务端检测到未授权则会拒绝访问并删除cookie"__ok")
 * +USER.login()				// 加载并显示登陆窗口
 * USER.isLoginWindow()		// 登录窗口是否已经显示
 * USER.loginWindowOut()	// 关闭登录窗口
 * +USER.signOut()			// 注销登录(删除相关Cookie并调用云端注销链接)
 * +USER.getUserinfo()		// 获取用户基本信息(本地Cookie中的数据)
 * USER.getUserinfo_f()		// 获取用户基本信息(登录的情况下,如果未登录则获取失败,并且需要自定义回调函数来获取)
 * USER.callback()			// 登录完成回调函数
 * USER.addCallback()		// 添加登录完成回调函数(初始化函数),执行成功后再次返回函数本身,且回调函数中不调用loginWindowOut()函数来关闭窗口
 * USER.addOtherLogin()		// 用于给第三方登录SDK将其自己注册过来(以便在某个操作后统一执行相关操作)
 */

// 用户模块基本(SDK)
(function(global) {
	
	/**
	* 相当于构造方法
	*/
	var UserImpl = function() {
		// 环境检测
		
		// 检查jQuery
		if (typeof $ != "function" || $ != jQuery) {
			throw new Error("需要在最前面先引入jQuery");
		}
		
		// 获取工具类
		if (typeof Tools == "undefined") {
			throw new Error("需要Tools工具库的支持,请先引入: //cdn.laeni.cn/js/tools.js");
		}
		// 自定义工具类
		this.tools = Tools;
		
		// 获取配置类
		if (typeof Conf == "undefined") {
			throw new Error("需要Tools工具库的支持,请先引入: //cdn.laeni.cn/config/laeni.cn.js");
		}
		this.conf = Conf;
		// 获取URL配置
		this.urlConf = this.conf("url");
	};
	
	/**
	 * 对ajax请求进行大部分操作的统一处理
	 * @obj: JSON对象,包含进行url请求的必要信息
	 * @autoLogin: 未登录时是否自动登录,默认"是"
	 */
	UserImpl.prototype.ajax= function(obj,autoLogin) {
		var _this = this;
		
		if(arguments.length < 2) {
			autoLogin = true;
		}
		
		if (typeof obj !== "object" || typeof obj.url !== "string") {
			throw new Error("必须传入一个JSON对象,并且对象中应该包含用于进行ajax请求的必要信息,比如url");
		}
		$.ajax({
			url: obj.url,
			data: obj.data,
			success: function(result){
				if(typeof result == "string") {
					result = eval("(" + result + ")");
				}
				
				// TODO
				console.log(result);
				
				// 未登录自动登录
				if (autoLogin && result.code == "141") {
					_this.login();
				}
				
				// 其他未统一处理的状态码(包括正确000)
				else {
					if (typeof obj.back == "function") {
						obj.back(result);
					}
				}
			},
			error: function(data){
				_this.tools.tips("请求出错,请检查网络是否畅通!!!","error");
				console.log(data);
			}
		});
	};
	
	/**
	* 检测是否登录(本地检测)
	* 因为在登录成功后再本地存储用户的基本信息(包括userId,nickname,头像地址,__ok)
	* __ok表示是否通过云端验证,如果通过则可以请求云端操作(云端会再次验证,如果非法则自动清空该值)
	*/
	UserImpl.prototype.islogin = function() {
		return this.tools.cookie.get("user_id") && $ut.cookie.get("__ok") ? true : false;
	};
	
	/**
	 * 检测云端是否已经登录(一般不会用)
	 * 具体功能需要自己传入回调函数进行实现
	 * @obj: JSON对象,包含已经登录该调用的方法"success",和未登录该调用的方法"nosuccess"
	 */
	UserImpl.prototype.isloginCloud = function(obj) {
		var _this = this,
			_urlConf = _this.urlConf;
		
		_this.ajax({
			url: _urlConf.user.checkLogin,
			success: function(result) {
				if (typeof obj == "object") {
					if (typeof obj.success == "function" && result.code == "000") {
						// 已经登录
						obj.success();
					} else if (typeof obj.nosuccess == "function" && result.code == "141") {
						// 没有登录
						obj.nosuccess();
					}
				}
			}
		},false);
	};
	
	/**
	 * 注销登录
	 * @method: 退出登录成功后执行的方法
	 */
	UserImpl.prototype.signOut = function(method) {
		var _this = this,
			_urlConf = _this.urlConf;
		
		// 清除本地Cookie
		_this.tools.cookie.del("__ok"),
		_this.tools.cookie.del("user_id"),
		_this.tools.cookie.del("nickname"),
		_this.tools.cookie.del("avatar"),
		_this.tools.cookie.del("grade");
		
		// 调用远程注销地址(仅本系统会调用)
		if(_this.tools.url.parseDomain(document.location.host) == _this.tools.url.parseDomain(_urlConf.home.value)) {
			_this.ajax({
				url: _urlConf.user.logout,
				success: function(result){
					if (typeof method == "function") {
						method(result);
					}
				}
			});
		} else {
			if (typeof method == "function") {
				method(result);
			}
		}
		
	};
	
	/**
	 * 登录方法
	 * $('#myModal').modal('show') 显示登陆框
	 * $('#myModal').modal('hide') 隐藏登陆框
	 * $('#myModal').modal('handleUpdate') 重新调整高度
	 */
	UserImpl.prototype.login = function() {
		var _this = this,
			_urlConf = _this.urlConf;
		
		// 本系统如果是本地系统则直接打开登录界面
		if(_this.tools.url.parseDomain(document.location.host) == _this.tools.url.parseDomain(_urlConf.home.value)) {
			// 打开登录界面
			$('#loginModal').modal('show');
		}
		
		// 如果不是本地系统则在新窗口中打开登录页面
		else {
			// TODO 跳转到登陆界面并设置监听事件
		}
	};
	
	/**
	 * 注册方法
	 */
	UserImpl.prototype.reg = function(){
		window.open(this.urlConf.user.reg, "_blank");
	};
	
	/**
	 * 从本地Cookie获取用户的基本信息
	 * @return JSON对象格式
	 */
	UserImpl.prototype.getUserInfo = function() {
		return this.tools.cookie.getAll();
	};
	
	/* 冻结对象并向外暴露唯一变量 */
	if (typeof Object.freeze == "function") {
		Object.freeze(UserImpl);
	}
	global.User = UserImpl;
})(typeof window !== "undefined" ? window : this);
