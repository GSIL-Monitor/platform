/**
 * 如果未通过环境检查时,直接停止执行
 * 第三方应用使用本SDK调用本平台进行登录时,需要在<script/>标签中加上"login_app_id"属性(login_app_id为自己创建的应用ID)
 * 如果该名字在系统中有冲突,则可在下方初始变量中修改
 * TODO 创建应用的页面出来或这里说明中要加上该地址
 *     示例: <script login_app_id="100000000" src="//cdn.laeni.cn/js/user-1.0.js" type="text/javascript" charset="utf-8"></script>
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
	var
	// 登录成功回调函数
	redirectLogin = [],
	// 注销成功回调函数
	redirectLogout = [],
	// 在标签<script>中配置appId时使用的名字
	appIdName = "login_app_id";
	
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
		if (typeof Conf !== "undefined") {
			this.conf = Conf;
			// 获取URL配置
			this.urlConf = this.conf("url");
		} else {
			// throw new Error("需要Tools工具库的支持,请先引入: //cdn.laeni.cn/config/laeni.cn.js");
		}
		
		// 检测使用环境(如果不是本系统则获取使用者的app_id)
		if(this.tools.url.parseDomain(document.location.host) != this.tools.url.parseDomain(this.urlConf.home.value)) {
			var $appId = $('script[' + appIdName +']');
			if (!$appId) {
				throw new Error("尚未在<script>标签中检测到应用ID:" + appIdName + ", 该" + appIdName + "可在创建的应用列表中获取到.");
			}
			this.appId = $appId && $appId.attr(appIdName);
		}
	};
	
	/**
	 * 增加回调方法
	 * 示例:
	 *   user.addRedirect({type:"redirectLogin",value:function(){ ... }});
	 */
	UserImpl.prototype.addRedirect = function(obj){
		// 检查参数数是否符合规范
		if (typeof obj !== "object" || typeof obj.type !== "string" || typeof obj.value !== "function") {
			throw new Error('User.addRedirect 方法需要一个JSON对象,并且有字符串类型的"type", 和function类型的"value"');
		}
		
		// 添加登录成功方法
		if (obj.type.match(/login/ig)) {
			redirectLogin[redirectLogin.length] = obj.value;
		}
		// 添加注销成功方法
		else if (obj.type.match(/logout/ig)) {
			redirectLogout[redirectLogout.length] = obj.value;
		}
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
			timeout: 10000,
			type: "post",
			xhrFields: {
				withCredentials: true,
				thCredentials: true
			},
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
		return this.tools.cookie.get("user_id") && this.tools.cookie.get("__ok") ? true : false;
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
		
		// 调用注销后该执行的方法
		for(var i in redirectLogout) {
			if (typeof redirectLogout[i] == "function") {
				redirectLogout[i]();
			}
		}
		
	};
	
	/**
	 * 登录方法
	 * @method: 可选参数,即登录成功后执行的方法
	 * 
	 * 示例:
	 * $('#loginModal').modal('show') 显示登陆框
	 * $('#loginModal').modal('hide') 隐藏登陆框
	 * $('#loginModal').modal('handleUpdate') 重新调整高度
	 * 	如果是本系统则弹窗形式打开登录窗口
	 *  如果是第三方应用则带上"当前页面地址"和"appid"重定向到登录页
	 */
	UserImpl.prototype.login = function(method) {
		var _this = this,
			_urlConf = _this.urlConf;
		
		if (!$("#loginModal")[0]) {
			$('<div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="登陆框" aria-hidden="true"><div class="modal-dialog modal-dialog-centered" role="document"><div class="modal-content"><div class="modal-header"><div class="m_header row justify-content-end" style="width: 100%;"><div data-dismiss="modal" class="col-auto"><svg class="icon pointer" aria-hidden="true"><use xlink:href="#icon-buoumaotubiao20"></use></svg></div></div></div><div class="modal-body"><form method="post"><!-- 用户名框 --><div class="input-group mb-3"><div class="input-group-prepend"><span class="input-group-text" id="inputGroup-sizing-default"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-zhanghao"></use></svg></span></div><input type="text" class="form-control shadow-none" name="username" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default"></div><!-- 密码框 --><div class="input-group mb-3"><div class="input-group-prepend"><span class="input-group-text" id="inputGroup-sizing-default"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-mima"></use></svg></span></div><input type="text" class="form-control shadow-none" name="password" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default"></div><!-- 记住登录状态(7天) & 忘记密码 --><div class="row justify-content-between pl-3 pr-3"><!-- 是否添加记住我的功能 --><div class="col-auto"><input id="freeLogin" name="freeLogin" type="checkbox"><label for="freeLogin">7天免登陆</label><svg class="icon doubt pointer" aria-hidden="true" data-toggle="tooltip" data-placement="right" title="在与后端无任何交互的情况下,登录状态最大维持7天"><use xlink:href="#icon-yiwen"></use></svg></div><!-- 忘记密码 --><a class="retrieve-password col-auto pointer">忘记密码?</a></div><!-- 登录按钮 --><div class="m-info"><div class="info-top"></div><input type="button" class="m-submit btn btn-primary btn-block shadow-none" value="登录"/></div></form><!-- 立即注册 --><div class="row justify-content-center m-3">还未注册?&nbsp;<a href="" class="reg_link">立即注册</a></div><hr><!-- 第三方登录方式 --><div class="row other-login p-2 pl-4" ><div>使用其他帐号登录:</div><!-- QQ --><div class="QQ-login pointer"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-social-qq"></use></svg></div><!-- 微信 --><div class="pointer" onclick="javascript: alert(\'微信登录功能暂未支持!!!\')"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-weixin"></use></svg></div></div></div></div></div></div>'+
			'<script type="text/javascript">$(function(){var _user = new User(),_urlConf = _user.urlConf;(function(){$(".retrieve-password").attr("href", _urlConf.user.find_password);$(".reg_link").attr("href", _urlConf.user.reg);})();(function(){/* QQ */$(".QQ-login").bind("click", function(){_user.lc(_user.conf("lc").qq).login();});$(\'[data-toggle="tooltip"]\').tooltip();})();(function(){$("#loginModal .m-submit").click(function(){var data = $(this).closest("form").serializeArray();console.log(data)});})();});</script>'+
			'').appendTo("body");
		}
		
		if(method) {
			_this.loginSuccessMethod = method;
		}
		
		// 如果没有"this.appId"则说明是本平台(因为在本系统中就算设置了也不会获取),直接以嵌入式窗口打开登录
		if(!this.appId) {
			// 打开登录界面
			$('#loginModal').modal('show');
		}
		
		// 如果不是本地系统则带上本页面地址(地址需要编码)以及应用ID(app_id)重定向到登录页
		else {
			// 示例: http://user.laeni.cn/login.html?redirect_uri=http%3A%2F%2Flocalhost%2F&app_id=111111222
			document.location.href = _urlConf.user.login + "?redirect_uri=" + encodeURIComponent(document.location.href) + "&app_id=" + this.appId;
		}
	};
	
	/**
	 * 注册方法
	 */
	UserImpl.prototype.reg = function(){
		// 带上源地址,注册成功后回到这里
		var reg_url = this.urlConf.user.reg + "?redirect_uri=" + encodeURIComponent(document.location.href);
		window.open(reg_url, "_blank");
	};
	
	/**
	 * 从本地Cookie获取用户的基本信息
	 * @return JSON对象格式
	 */
	UserImpl.prototype.getUserInfo = function() {
		return this.tools.cookie.getAll();
	};
	
	/**
	 * 使用第三方帐号登录(适用于OAuth2.0协议)
	 * 第三方帐号只负责跳转到对应的登录网站,是否登录成功等不应该在这里判断
	 * @obj 包含第三方登录所需的必要信息
	 * @return 返回相应的对象,其中有.login()方法可以打开相应的登录页面
	 * 示例:
	 * 		var user = new User();
	 * 		// 查看说明
	 * 		user.lc.help();
	 * 		// 获取QQ登录对象
	 * 		var qq = user.lc({type:"QQ",....});
	 * 		// 查看QQ登录帮助
	 * 		qq.help();
	 * 		// 调用QQ登录
	 * 		qq.login();
	 */
	UserImpl.prototype.lc = function(obj){
		var _this = this;
		
		/* 如果当前环境是一个独立的登录页面,则可能是从其他地址跳转过来的,所以需要获取跳转前的源地址以及调用者的应用ID */
		var myUrlParaNames = _this.tools.url.getParas(document.location.href);
		// 从地址栏获取调用者的appId
		var otherAppId = myUrlParaNames["app_id"];
		// 从地址栏获取跳转前的页面
		var otherRedirecturi = myUrlParaNames["redirect_uri"];
		// 对你参数进行拼接
		var myRedirecturi = (otherAppId && otherRedirecturi) && ("appId=" + otherAppId + "&redirectUri=" + otherRedirecturi);

		// 目前支持的登录方式
		var type = ["QQ",];
		
		// 检查参数是否合法
		if (typeof obj != "object") {
			throw new Error("[QQ互联] :: 传入的参数不是一个JSON对象,无法从中获取到有用的信息,详情请参考help()方法.");
		}
		
		/**
		 * 打开第三方窗口后调用的函数
		 * 1.监听窗口是否得到焦点
		 * 2.直到得到焦点后判断是否登录成功
		 * 3.1.如果登录失败则提示用户解决方法
		 * 3.2.如果登录成功则取消该监听事件
		 * 4.关闭弹窗
		 */
		function publMethod(){
			
			// 给方法一个名字
			function method(){
				// 本次登录已经结束,所以取消这个事件
				$(window).unbind("focus",method);
				
				// TODO 判断是否成功登录(仅能判断本系统)
				if (_this.tools.cookie.get("__ok")) {
					// 执行调用登录窗口时传进来的方法
					if (typeof _this.loginSuccessMethod == "function") {
						_this.loginSuccessMethod();
					}
					
					// 执行登录成功后该执行的方法
					for(var i in redirectLogin) {
						if (typeof redirectLogin[i] == "function") {
							redirectLogin[i]();
						}
					}
					
					// 关闭窗口
					$('#loginModal').modal('hide');
				}
			}
			$(window).bind("focus",method);
		}
		
		// 定义一个变量存储第三方SDK返回的对象
		var lc;
		
		/**
		 * QQ登录SDK
		 * TODO 在登录页需要对第三方登录做单独的逻辑处理
		 */
		if (obj.type.match(/^qq$/ig)) {
			lc = {
				// 帮助函数
				help: function(){
					console.log("[QQ互联登录] ::"+
							'http://wiki.connect.qq.com'+
							'使用 login() 方法可调起QQ登录'+
							'初始化参数示例:[type:"QQ",appid:"应用ID",redirecturi:"QQ登录回调地址"]');
				},
				// 打开登录窗口
				login: function(){
					// 检查参数是否合法
					if (typeof obj.appid == "undefined" || !obj.appid) {
						throw new Error("参数应用ID(appid)格式错误!");
					}
					if (typeof obj.redirecturi == "undefined" || !obj.redirecturi) {
						throw new Error("参数回调地址(redirecturi)格式错误!");
					}
					
					// 获取回调地址(如果传入的是已经编码的格式则先将其转换为未编码的格式)
					if (obj.redirecturi.match(/%3A%2F%2F/)) {
						obj.redirecturi =  decodeURIComponent(obj.redirecturi);
					}
					
					// QQ回调地址+自定义回调地址(一般只有在在第三方调用时才有,该地址有本页面地址栏中源地址"redirect_uri"和应用Id"app_id"组成)
					// 并进行编码
					var redirecturi = encodeURIComponent(obj.redirecturi + (myRedirecturi ? ("?"+myRedirecturi) : ""));

					// 生成state值,防止攻击,并写入Cookie(每次刷新,一小时有效)
					state = _this.tools.number.randomNum(10000000,99999999).toString();
					_this.tools.cookie.set("state",state,null,null,1);
					
					// 拼接请求地址
					var url = 'https://graph.qq.com/oauth2.0/authorize?'+
						'response_type=code&'+			//授权类型，此值固定为"code"
						'client_id='+obj.appid+			//appid
						'&redirect_uri='+redirecturi+	//回调地址
						'&state='+state+				//防止攻击,原样带回到回调地址
						(document.documentElement.clientWidth<600 ? '&display=mobile' : '');// 是否显示为mobile端下的样式
					
					// 执行publMethod()方法,对登录行为进行监听
					publMethod();

					// 开启登录窗口
					return window.open(url, "_blank");
				}
			};
		}
		
		/**
		 * "xxx"第三方应用的SDK
		 */
		else if (obj.type.match(/^xxx$/ig)) {
			lc = {
				// 帮助函数
				help: function(){
					console.log("xxx登录示例");
				},
				// 打开登录窗口
				login: function(){
					// ....
					
					// 执行publMethod()方法,对登录行为进行监听
					publMethod();
					
					// 开启登录窗口
					return window.open("", "_blank");
				}
			};
		}
		
		// 在对象中加入帮助函数
		lc.help = function(){
			console.log("[使用第三方帐号登录帮助] ::\n"+
					"目前支持登录方式为:" + type.toString() +"\n"+
					'参数形式举例:{"type":"QQ",....}\n'+
					'详情请参考其对应的help()函数');
		};
		return lc;
	}
	
	/* 冻结对象并向外暴露唯一变量 */
	if (typeof Object.freeze == "function") {
		Object.freeze(UserImpl);
	}
	global.User = UserImpl;
})(typeof window !== "undefined" ? window : this);

