/**
 * 所有代码全部放入自执行函数中
 * 尽量防止变量污染
 * 唯一向外暴露一个变量"LC"
 */
(function (global) {
	var _lc = function () {
		var
			// 定义用户基本信息
			appid, 				// 应用的唯一标识
			redirecturi,		// 回调地址
			isMobile,			// *是否显示为移动端

			// 定义其他非用户基本信息
			loginWinCon,		// 登录窗口参数(width:窗口宽度,height:窗口高度,menubar:是否独立窗口)
			state,				// 临时参数,防止攻击
			interval,			// 定时器
			qqLoginWindow,		// 新开的登录窗口
			btnClass,			// 按钮class值
			addBackFunc = [],	// [数组,可能有多个回调函数]登录成功或失败回调函数
			$UT = typeof UT != "undefined" ? UT : null,				// 工具类
			// 初始化jQuery
			_jQuery = typeof $ != "undefined" ? $ : (typeof jQuery != "undefined" ? jQuery : null),
			// 输出信息
			$console = $UT && $UT.console && $UT.console(":: QQ登陆sdk > ") || console,
			// 获取Authorization Code的地址
			urlAuthorCode = "https://graph.qq.com/oauth2.0/authorize";


		/*
		 * 登录时自动初始化函数
		 */
		function init(){
			if(!$UT){
				console.error("需要js工具包的支持,请下引入:https://chengdu-1252266447.cos.ap-chengdu.myqcloud.com/cdn.laeni.cn/public/js/utlis.js");
				return;
			}
			
			// 引入QQ登录的script标签
			var qc_script;
			
			/* 循环找出目标script标签*/
			var scripts = document.getElementsByTagName("script");
			for(var i = 0, script, l = scripts.length; i < l; i++){
				var sc = scripts[i];
				if (sc.hasAttribute("qc-appid") && sc.hasAttribute("qc-redirecturi")) {
					qc_script = sc;
					break;
				}
			}
			
			// 用于获取标签中的para属性的值
			function getDataPara(para) {
				return qc_script && (qc_script.dataset && qc_script.dataset[para] || qc_script.getAttribute("data-" + para) || qc_script.getAttribute("qc-" + para));
			}
			
			// 获取appid
			if(!appid){
				appid = getDataPara("appid");
			}
			
			// 获取回调地址
			var url = getDataPara("redirecturi");
			if(!redirecturi){
				// 无论传入的是什么格式都先将其转换为未编码的格式
				redirecturi = (url && url.match(/%3A%2F%2F/) && decodeURIComponent(url)) || url;
			}
			
			// 尝试获取按钮的类名
			btnClass = btnClass || getDataPara("btn-class");
			// 绑定事件
			bindClick();
		}
		// 当该节点加载完后再执行初始化方法
		document.addEventListener("load", init, true);
		// window.addEventListener("load", init, true);
		
		// 将登陆的点击事件绑定到相应的类元素上
		function bindClick(){
			
			if(btnClass) {
				if(typeof btnClass == "string"){
					btnClass = btnClass.split(",")
				}
				for( var i=0; i<btnClass.length && btnClass[i] != ""; i++){
					// 将点击登录事件绑定到元素上
					var $btnClassList = document.getElementsByClassName(btnClass[i]);
					for(var i=0;i<$btnClassList.length;i++){
						if($btnClassList[i].onclick == undefined){
							$btnClassList[i].onclick = _login;
						}
					}
				}
			}
		}
		
		// 手动初始化,设置QQ互联登录所需的必要信息(使用配置方式的可省略此步骤)
		function _init(obj){
			if(typeof obj != "object"){
				return false;
			}
			// 获取appid
			appid = obj.appid;
			
			// 获取回调地址(无论传入的是什么格式都先将其转换为未编码的格式)
			redirecturi = (obj.redirecturi && obj.redirecturi.match(/%3A%2F%2F/) && decodeURIComponent(obj.redirecturi)) || obj.redirecturi;
			
			// 尝试获取按钮的类名(如果没有则只能使用通过JS代码调用login()调起登陆框)
			btnClass = obj.btnClass;
			// 设置是否显示为移动端
			isMobile = obj.mobile;
			
			// 为class名为btnClass的元素增加点击事件
			bindClick();
			
			return true;
		}
		
		/**
		 * 登录
		 * 当点击登录按钮时执行的方法
		 */
		function _login() {
			if(!appid || !redirecturi){
				$console.error("请正确引入并配置相关参数(\"qc-appid\"和\"qc-redirecturi\"是必须的)，详情请参考http://note.youdao.com/noteshare?id=74d861508306e4fa0f14699fbc54cc5e&sub=E25888D41CD3496A86C22DDF48172F6E");
				return;
			}
			
			// 将本页面的地址附加到回调地址上发送给后端(该地址需要编码)
			var reurl = encodeURIComponent(redirecturi+"?redirect_uri="+$UT.url.getUrlAll());
			
			// 生成state值,防止攻击,并写入Cookie(每次刷新,一小时有效)
			state = $UT.number.randomNum(10000000,99999999).toString();
			$UT.cookie.set("state",state,null,null,1);
			
			// 开启登录窗口
			var url = urlAuthorCode+'?'+
				'response_type=code&'+			//授权类型，此值固定为"code"
				'client_id='+appid+				//appid
				'&redirect_uri='+reurl+			//回调地址
				'&state='+state+				//防止攻击,原样带回到回调地址
				(isMobile && '&display=mobile' || ''),// 是否显示为mobile端下的样式
			
				/* 窗口宽度*/
				QQLoginWindowWidth = (loginWinCon && loginWinCon.width) || 750,
				/* 窗口高度 */
				QQLoginWindowHeight = (loginWinCon && loginWinCon.height) || 532,
				/* 窗口垂直方向上的距离*/
				QQLoginWindowTop = (loginWinCon && loginWinCon.top) || (window.screen.availHeight - Number(QQLoginWindowHeight))/2,
				/* 窗口水平方向上的距离*/
				QQLoginWindowLeft = (loginWinCon && loginWinCon.left) || (window.screen.availWidth - Number(QQLoginWindowWidth))/2;
			
			qqLoginWindow = window.open(url, "TencentLogin",
				"width="+ QQLoginWindowWidth
				+",height="+ QQLoginWindowHeight
				+",top="+ QQLoginWindowTop
				+",left="+ QQLoginWindowLeft
				+",menubar="+ ((loginWinCon && loginWinCon.menubar && "1") || "0")
				+",scrollbars=1,resizable = 1, status = 1, titlebar = 0, toolbar = 0, location = 1 "
			);
			console.log("state:" + state)
			console.log("url:" + url)
			// 由于部分浏览器对窗口关闭事件支持不完美
			// 所以采用轮询方式检测窗口是否被关闭(可以改进)
			// 设置定时器,检测窗口是否关闭
			interval = qqLoginWindow && setInterval(function(){
				// 如果窗口关闭则取消定时器
				qqLoginWindow.closed && clearInterval(interval);
				// 窗口关闭后系统回调函数
				windowClose();
			},500);
		};
		
		/*
		* 窗口关闭后系统回调函数
		* 可能成功登录,也可能中途关闭了
		*/
		function windowClose(){
			if(qqLoginWindow && qqLoginWindow.closed){
				// 执行用户定义的回调函数
				if(typeof typeof addBackFunc == "function"){
					addBackFunc();
				} else if(typeof addBackFunc == "object" && addBackFunc instanceof Array){
					for(var i=0,l=addBackFunc.length;i<l;i++){
						addBackFunc[i]();
					}
				}
				
				// 关闭登录窗口并自动执行初始化方法
				USER && USER.loginWindowOut();
			}
		}
		
		/*
		 * 登出
		 * 仅仅是在本地Cookie中删除QQ用户的appid/openid/access_token
		 */
		function _signOut() {
			if( $UT && $UT.cookie ){
				$UT.cookie.del("appid");
				$UT.cookie.del("openid");
				$UT.cookie.del("access_token");
			}
		}

		/*
		 * 设置回调函数 function(function func) 
		 * 注意： _setaddBackFunc方法只能在用户登录授权后调用，建议总是在使用check方法并返回true的条件下，调用getMe方法
		 */
		function _setaddBackFunc(func) {
			addBackFunc = func;
			if(typeof addBackFunc == "object" && addBackFunc instanceof Array) {
				addBackFunc[addBackFunc.length] = func;
			} else {
				addBackFunc = func;
			}
		}
		
		/*
		 * 获取基本数据
		 */
		function _getInfo(callback) {
			if (typeof $ == "function" || typeof jQuery == "function") {
				_jQuery.ajax({
					url: "http://user.laeni.cn/api/qqinfo",
					type: "GET",
					data: {
						access_token : "A3D8BDB354E1D377C16305FE0B8171AD",
						oauth_consumer_key : "101481333",
						openid: "2162948A25C174A25C71325BF8228DF3"
					},
					success: function (data) {
						var qqinfo;
						if(typeof data == "string"){
							qqinfo = eval("(" + data + ")");
						}else{
							qqinfo = data;
						}
						if(typeof callback == "function"){
							callback(qqinfo);
						}
					},
					error: function(){
						if(typeof callback == "function"){
							callback(null);
						}
					}
	
				})
			}
		}
		
		/*
		 * 检查是否已经登录
		 * 如果已经登录,但是网页是二次加载,则调用该方法后将拉取用户对于信息回本地
		 * return Boolean
		 */
		function _check() {
			return !!($UT && $UT.cookie && $UT.cookie.get("appid") && $UT.cookie.get("openid") && $UT.cookie.get("access_token"));
		}

		// 需要对外暴露的方法在这里返回
		return { 
			init: _init,				// 通过JSON设置QQ互联所需的必须信息
			getInfo: _getInfo,			// 从QQ服务器获取基本数据(Cookie中需要有有appid/openid/access_token)
			signOut: _signOut,			// 登出(仅仅是在本地Cookie中删除QQ用户的appid/openid/access_token)
			check: _check,				// 检查QQ登录是否有效(检测Cookie:appid/openid/access_token是否存在)
			login: _login,				// 打开登录窗口
			setaddBackFunc: _setaddBackFunc,	// 设置登录成功或失败回调函数(窗口关闭)
		}
	}();


	/* 冻结对象并向外暴露唯一变量 */
	if (typeof Object.freeze == "function") {
		Object.freeze(_lc);
	}
	global.LC = _lc;
}(typeof window !== "undefined" ? window : this)),



(function(){
	// 将自己注册给USER,主要是让User能调用注销登录的方法
	if(typeof USER == "object"){
		USER.addOtherLogin(LC);
	}
}());
