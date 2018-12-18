$(document).ready(function () {
	var 
		_CONF = typeof LAENI_CONFIG != "undefined" ? LAENI_CONFIG : undefined,
		url = _CONF ? _CONF.URL.USER : "//laeni.cn",
		/* 发送验证码 */
		url1 = url + (_CONF && _CONF.PATH.GET_VERIFY_CODE),
		/* 验证验证码 */
		url2 = url + (_CONF && _CONF.PATH.CHECK_VERIFY_CODE),
		/* 新用户注册地址: 提交手机或邮箱和相应的验证码进行注册
		 * 如果该帐号已经被注册则返回提交的帐号是否已经被注册,否则为该帐号注册后并登录(和强制注册一样)
		 * ?name=laeni@qq.com&verifyCode=123456
		 * force=force (即表示强制注册,无需返回帐号是否存在,如果存在则与原来的解绑) */
		url3 = url + (_CONF && _CONF.PATH.REG_ACCOUNT),
		/* 检查登录名是否正确 */
		url4 = url + (_CONF && _CONF.PATH.CHECK_LOGIN_ACCOUNT),
		/* 更新用户信息 */
		url5 = url + (_CONF && _CONF.PATH.SAVE_OR_UPDATE_USER_INFO),
		
		/* 自定义工具库 */
		$ut = typeof UT == "object" ? UT : null,
		/* 用户操作工具函数 */
		$user = typeof USER == "object" ? USER : null,
		
		
		/* 【设置登录方式】元素 */
		$form1 = $('.form1'),
		$form2 = $(".form2"),
		/* 【帐号框】祖先元素 */
		$reg1 = $form1.find("#reg1"),
		/* 【验证码框】祖先元素 */
		$reg2 = $form1.find("#reg2"),
		/* 【下一步按钮】祖先元素 */
		$reg3 = $form1.find("#reg3"),

		/* 【帐号框】 */
		$reg_name1 = $('#account'),
		/* 获取【帐号框】错误文字提示元素 */
		$regError1 = $("#reg-error1"),
		/* 获取【帐号框】图标提示元素 */
		$errorIcon = $reg1.find("li i"),

		/* 【验证码框】 */
		$reg_name2 = $('#verifyCode'),
		/* 【获取验证码按钮】 */
		$regverify = $reg2.find(".reg-verify"),
		/* 获取【验证码框】图标提示元素 */
		$errorIcon2 = $reg2.find("li i"),
		/* 【验证码框】错误提示框 */
		$regError2 = $reg2.find(".reg-error1"),
		
		
		/* 帐号已经存在的提示信息 */
		$ts = $("#ts"),
		/* 提示关闭按钮 */
		$tsClose = $ts.find(".close use"),
		/* 更换手机号进行注册 */
		$li4 = $ts.find(".oper_1 .li4"),
		/* 登录名 */
		$loginName = $ts.find(".login_name"),
		/* 昵称 */
		$nickname = $ts.find(".nickname"),
		/* 帐号类型(手机号或邮箱号) */
		$accType = $ts.find(".acc_type"),
		/* 帐号(手机号或邮箱号) */
		$acc = $ts.find(".acc"),
		/* 反帐号类型(手机号或邮箱号)(与前面的相反,前面为手机,这里就为邮箱) */
		$accTypeCon = $ts.find(".acc_type_con"),
		/* 反帐号(手机号或邮箱号)(与前面的相反,前面为手机,这里就为邮箱) */
		$accCon = $ts.find(".acc_con"),
		/* 继续使用该手机号/邮箱号注册元素 */
		$li3 = $ts.find(".li3"),

		/* 【设置登录方式】->【下一步】按钮 */
		$next1 = $reg3.find(".next"),
		// 下一步按钮
		$f2Next = $form2.find(".next"),

		/* 定时器(倒计时结束可重新获取验证码) */
		cycle = null,
		
		/* 注册成功页面 */
		$ok = $("#reg .ok"),
		// 倒计时时间
		$time = $ok.find(".date0"),
		// 定时器1(倒计时结束跳转页面)
		cycle1 = null,
		// 注册成功后需要跳转到的URL名称(如"首页"/"注册前页面")
		$urlTypeName = $ok.find(".url_type_name"),
		// 跳转信息
		$tm = $ok.find(".tm"),
		// 立即跳转按钮
		$justJump = $ok.find("just_jump"),
		// 取消跳转按钮
		$aboJump = $ok.find(".abo_jump");
		
	/**设置按钮是否可用
	 * on(可用) off(不可用)  await(等待)
	 */
	function buttonStyle($o, type) {
		switch (type) {
			case "on":
				$o.removeAttr("disabled");
				$o.removeClass("button-type-off");
				$o.addClass("button-type-on");
				break;
			case "await":
			default:

			case "off":
				$o.attr("disabled", "disabled");
				$o.removeClass("button-type-on");
				$o.addClass("button-type-off");
				break;
		}
	}
	/**设置图标是否显示绿色(可用)
	 * @param {Object} $o
	 * @param {Object} type
	 */
	function iconStyle($o, type) {
		switch (type) {
			case "on":
				$o.removeClass("icon-type-off");
				$o.addClass("icon-type-on");
				break;
			case "off":
				$o.removeClass("icon-type-on");
				$o.addClass("icon-type-off");
				break;
			case "await":
			default:

				break;
		}
	}
	/**设置输入框是否可用
	 * @param {Object} $o
	 * @param {Object} type
	 */
	function inputStyle($o, type) {
		switch (type) {
			case "on":
				$o.removeClass("input-type-off");
				$o.addClass("input-type-on");
				$o.removeAttr("disabled");
				break;
			case "off":
				$o.removeClass("input-type-on");
				$o.addClass("input-type-off");
				$o.attr("disabled", "disabled");
				break;
			case "await":
			default:

				break;
		}
	}
	/**
	 * 选项卡切换<br />
	 * switchCart(2)表示前2-1个都标记为已完成,且第二个是正在完成状态
	 */
	function switchCart(eq) {
		var $reg = $("#reg"),
			$olLo = $reg.find(".rt-1 ol li"),
			$cardBody = $reg.find(".card_body");

		// 参数不合法则退出
		if (eq <= 0 || eq > $olLo.length) {
			return;
		}

		/* 将1到eq的标记为已经完成 */
		for (var i = 0; i < eq; i++) {
			/* 将i个加上"已经完成"的标识 */
			$olLo.eq(i).addClass("ok1");
			if (i+1 != eq) {
				$olLo.eq(i).removeClass("run"),
				$cardBody.eq(i).addClass("none");
			}
		}
		/* 设置第eq个为正在进行 */
		$olLo.eq(eq - 1).addClass("run"),
		$cardBody.eq(eq - 1).removeClass("none");
		
		/* 将eq之后的标识去掉 */
		for (var i = eq + 1; i <= $olLo.length; i++) {
			$olLo.eq(i - 1).removeClass("ok1"),
			$olLo.eq(i - 1).removeClass("run"),
			$cardBody.eq(i - 1).addClass("none");
		}
	}
	/**
	 * 显示或隐藏"帐号已存在"的相关提示信息
	 */
	function tsFun(type){
		switch (type){
			case "off":
				// 隐藏
				$ts.addClass("none"),
				// 关闭蒙版
				$ut && $ut.mb.off();
				break;
			case "on":
			default:
				// 开启蒙版
				$ut && $ut.mb.on(),
				// 居中
				$ut && $ut.center($ts),
				// 设置为可见
				$ts.removeClass("none");
				break;
		}
	}

	
	/* 智能输入框 */
	$ut && $ut.check.checkInputCleanIcon();
	
	/* 当文本框获得焦点是选择框中的文本 */
	$("input").focus(function () {
		this.select();
	});

	/* 检查帐号框输入是否符合要求 */
	$reg_name1.bind('input propertychange', function () {
		/* 结束定时器 */
		clearInterval(cycle);
		/* 设置【获取验证码】按钮文字 */
		$regverify.val("获取验证码");
		/* 当帐号改变时清空【验证码框】并设置其为不可用 */
		$reg_name2.val(null);
		inputStyle($reg_name2, "off");
		/* 清空验证码提示信息 */
		$regError2.text(null);
		/* 当帐号改变时设置【下一步】按钮为不可用 */
		buttonStyle($next1, "off");
		
		if ($regError1.html()=="" && $reg_name1.val() != "") { /*符合*/
			/* 设置提示图标样式(绿色) */
			iconStyle($errorIcon, "on");
			/* 设置"获取验证码"按钮样式为(可用) */
			buttonStyle($regverify, "on");
		} else {
			/* 设置提示图标样式(灰色) */
			iconStyle($errorIcon, "off");
			/* 设置"获取验证码"按钮样式为(不可用) */
			buttonStyle($regverify, "off");
		}

	});
	/* 检查验证码是否输入正确 */
	$reg_name2.bind('input propertychange', function () {
		$this = $(this);
		/* 验证码内容 */
		value = $this.val();

		/* 设置图标为"错误" */
		iconStyle($errorIcon2, "off");
		/* 设置下一步按钮为不可用 */
		buttonStyle($next1, "off");

		if (value.length >= 6) {
			/* 使验证码框失去焦点 */
			this.blur();
			/* 请求服务器检查验证码是否正确 */
			$.ajax({
				url: url2,
				data: $form1.find("form").serializeArray(),
				xhrFields: {
					withCredentials: true,
					thCredentials: true
				},
				type: "post",
				dataType: "json",
				timeout: 10000,
				success: function (re) {
					/* 正确或等待 */
					if (re.code == "000") {
						/* 设置错误提示信息为空 */
						$regError2.text(null);
						/* 设置图标为"正确" */
						iconStyle($errorIcon2, "on");
						/* 设置下一步按钮为可用 */
						buttonStyle($next1, "on");
					} else {
						/* 设置错误提示信息 */
						$regError2.text(re.msg);
					}
				},
				error: function() {
					alert("网络错误,请稍候再试!")
				}
			});
		}
	});
	/* 获取验证码按钮点击事件 */
	$regverify.click(function () {
		/* 阻止默认事件 */
		event.preventDefault();
		/* 将按钮设置为不可用,防止多次点击 */
		buttonStyle($regverify, "off");
		/* 清空验证码框 */
		$reg_name2.html(null);

		/* 请求服务器发送验证码 */
		$.ajax({
			url: url1,
			data: $form1.find("form").serializeArray(),
			xhrFields: {
				withCredentials: true,
				thCredentials: true
			},
			type: "POST",
			dataType: "json",
			timeout: 10000,
			crossDomain: true,
			success: function (re) {
				/* 正确或等待 */
				if (re.code == "000" || re.code == "003") {
					/* 使验证码框可用并得到焦点 */
					inputStyle($reg_name2, "on");
					$reg_name2.focus();
					if (re.code == "000") {
						/* 如果正确则清空错误提示信息 */
						$regError2.text(null);
					}else {
						/* 设置错误提示信息 */
						$regError2.text("发送次数过多(且原验证码可能未过期)");
					}

					/* 创建倒计时 */
					var c = function () {
						if (re.data.time <= 0) {
							/* 结束定时器 */
							clearInterval(cycle);
							/* 使按钮可用 */
							buttonStyle($regverify, "on");
							$regverify.val("重新获取");
						} else {
							$regverify.val("重新获取(" + (--re.data.time) + ")");
						}
						return c;
					}
					cycle = setInterval(c(), 1000);
				} else {
					/* 其他错误：使按钮可用 */
					buttonStyle($regverify, "on")
					/* 提示错误信息 */
					$regError2.text(re.msg);
				}

			}
		});
	});
	/*填入验证码后的下一步事件
	 * 1.检查该帐号是否被注册
	 * 2.如果没有被注册则直接提示用户设置基本信息
	 * 3.如果已经注册,则让用户自己选择"找回登录密码"、"继续注册"、"立即登陆"(同时显示用户的基本资料-昵称/头像)
	 */
	$next1.click(function () {
		/* 阻止默认事件 */
		event.preventDefault();
		/*获取帐号*/
		name = $reg_name1.val();
		/*获取验证码*/
		verifyCode = $reg_name2.val();
		$.ajax({
			url: url3,
			data: $form1.find("form").serializeArray(),
			xhrFields: {
				withCredentials: true,
				thCredentials: true
			},
			type: "post",
			dataType: "json",
			timeout: 10000,
			success: function (re) {
				/* 请求检查的帐号为空并且将该帐号添加到了用户表中 */
				if(re.code == "000"){
					// 跳到用户资料填写页面
					switchCart(2);
				}else if(re.code == "104"){
					console.log(re)
					/* 显示用户已经存在,供用户选择是否使用该帐号继续注册还是登录,或者找回密码*/
					// 昵称
					$nickname.text(re.data.nickname),
					// 登录名
					$loginName.text(re.data.loginName);
					if(re.data.accountType == "phone") {
						$accType.text("手机号"),
						$acc.text(re.data.loginPhone);
						$accTypeCon.text("邮箱号"),
						$accCon.text(re.data.loginEmail);
					}else if(re.data.accountType == "email"){
						$accType.text("邮箱号"),
						$acc.text(re.data.loginEmail),
						$accTypeCon.text("手机号"),
						$accCon.text(re.data.loginPhone);
					}
					// 显示"帐号已存在"的提示信息
					tsFun("on");
				}else if(re.code == "120"){
					/* 验证码已经过期 */
					// 提示过期信息
					$regError2.text("由于您长时间未操作，请重新获取验证码"),
					// 下一步按钮不可用
					buttonStyle($next1,"off");
				}else{
					// 其他错误
					alert(re.msg);
				}
			},
			error: function () {
				alert("请求失败,请检查网络是否畅通!");
			}
		});
	});
	
	
	/* 帐号已经存在的提示内容 */
	// 关闭按钮
	function tsClose(){
		// 关闭提示
		$ts.addClass("none"),
		// 关闭蒙版
		$ut && $ut.mb.off();
	}
	$tsClose.bind("click",tsClose);
	$li4.bind("click",tsClose);
	
	/* 继续使用该手机号/邮箱号注册的点击事件 */
	$li3.click(function(){
		/*获取帐号*/
		var name = $reg_name1.val(),
		/*获取验证码*/
		verifyCode = $reg_name2.val(),
		valueArray = $form1.find("form").serializeArray();
		// 在原参数的基础上增加一个强制注册指令
		valueArray[valueArray.length] = {name:"force",value:"force"};
		
		$.ajax({
			url: url3,
			type: "post",
			dataType: "json",
			timeout: 10000,
			data: valueArray,
			xhrFields: {
				withCredentials: true,
				thCredentials: true
			},
			success: function (re) {
				console.log(re)
				/* 请求检查的帐号为空并且将该帐号添加到了用户表中 */
				if(re.code == "000"){
					// 跳到用户资料填写页面
					switchCart(2);
					// 关闭提示信息
					tsFun("off");
				}else if(re.code == '120'){
					/* 验证码已经过期 */
					// 提示过期信息
					$regError2.text("由于您长时间未操作，请重新操作"),
					// 下一步按钮不可用
					buttonStyle($next1,"off"),
					// 关闭提示信息
					tsFun("off")
				}else{
					// 直接提示错误信息
					alert(re.msg);
				}
			},
			error: function () {
				alert("请求失败,请检查网络是否畅通!");
			}
		});
	});

	/* 资料填写验证 */
	(function(){
		var 
			// 昵称框所在的li标签
			$nickname = $form2.find("nickname"),
			// 登录名框所在的li标签
			$loginNameLi = $form2.find(".login_name"),
			// 登录密码框所在的li标签
			$password = $form2.find(".password"),
			// 确定密码框所在的li标签
			$passwordS = $form2.find(".password_s"),
			
			// 标记
			isLoginName = true,// 登录名框是否验证通过(默认可以不填写)
			isPassword = false,// 密码框是否验证通过
			isPasswordS = false;//确认密码框是否验证通过
		
		// 如果所有参数都合法,则将下一步按钮设置为可用
		function f2NextOk(){
			
			if(isLoginName && isPassword && isPasswordS){
				// 设置按钮可用
				buttonStyle($f2Next,"on");
			}else{
				// 设置按钮不可用
				buttonStyle($f2Next,"off");
			}
		}
		// 检查两次输入的密码使用相同
		function pasEqPass(){
			var
				$this = $passwordS.find("input"),
				// 获取密码的内容
				password = $password.find("input").val(),
				// 获取确认密码框中的内容
				passwordS = $this.val();
			
			if(passwordS == ""){
				$this.parents("li").find(".error").text(null);
				return;
			}
			
			if(password == passwordS){
				// 合法
				$this.parents("li").find(".error").text(null);
				isPasswordS = true;
			}else{
				$this.parents("li").find(".error").text("两次密码输入不一样,请重新输入");
				isPasswordS = false;
			}
			// 如果所有参数都合法则将按钮设置为可用
			f2NextOk();
		}
		
		// 检查登录名是否存在
		$loginNameLi.find("input").blur(function(){
			var
				$this = $(this),
				value = $this.val();
				
			
			// 可以为空
			if(value==null || value==""){
				/* 不存在 */
				// 清空错误信息
				$this.parents("li").find(".error").text(null);
				// 将登录名项标记为合法
				isLoginName = true;
				// 如果所有参数都合法则将按钮设置为可用
				f2NextOk();
				return;
			}
			
			$.ajax({
				url: url4,
				data: {
					account: value,
					type: "loginName"
				},
				type: "post",
				dataType: "json",
				timeout: 10000,
				xhrFields: {
					withCredentials: true,
					thCredentials: true
				},
				success: function (re) {
					if (re.code == "104") {
						/* 帐号已经存在 */
						// 提示相关信息
						$this.parents("li").find(".error").text(re.msg);
						// 将登录名项标记为不合法
						isLoginName = false;
					} else if(re.code == "105"){
						/* 不存在 */
						// 清空错误信息
						$this.parents("li").find(".error").text(null);
						// 将登录名项标记为合法
						isLoginName = true;
						// 如果所有参数都合法则将按钮设置为可用
						f2NextOk();
					}
				},
			});
		});
		$loginNameLi.find("input").bind('input propertychange', function(){
			var $this = $(this);
			
			// 如果为空则表示通过
			if($this.val()=="" || $this.val()==undefined){
				// 清空错误信息
				$this.parents("li").find(".error").text(null);
				// 将登录名项标记为合法
				isLoginName = true;
				// 如果所有参数都合法则将按钮设置为可用
				f2NextOk();
			}else{
				// 将登录名项标记为合法
				isLoginName = false;
			}
			
			// 使下一步按钮不可用
			f2NextOk();
			
		})
		// 检查密码输入是否合法
		$password.find("input").bind('input propertychange', function(){
			var $this = $(this);
			
			// 使下一步按钮不可用
			f2NextOk();
			// 将密码框标记为不合法
			isPassword = false;
			// 将密码框标记为不合法
			isPassword = false;
			
			if($("#password_small").html() == ""){
				// 将密码框标记为合法
				isPassword = true;
				//检查两次密码是否相同
				pasEqPass();
				// 如果所有参数都合法则将按钮设置为可用
				f2NextOk();
			}
		});
		// 检查确认密码是否与密码框的内容一致
		$passwordS.find("input").bind('input propertychange', pasEqPass);
	})(null);

	/* 提交资料 */
	$f2Next.click(function(){
		/* 阻止默认事件 */
		event.preventDefault();
		
		// 提交资料
		$.ajax({
			url: url5,
			data: $(".form2 form").serializeArray(),
			type: "POST",
			dataType: "json",
			timeout: 10000,
			xhrFields: {
				withCredentials: true,
				thCredentials: true
			},
			crossDomain: true,
			success: function (re) {
				if(re.code=="000"){
					// 如果注册页面时从其他地方跳转而来则跳转到其他地方(默认跳转到首页)
					var redirect_uri = $ut && $ut.url.getUrlParam('redirect_uri');
					
					redirect_uri = decodeURIComponent(redirect_uri);
					if(redirect_uri){
						$ok.find(".url_0").attr("href",redirect_uri)
					}
					
					// 立即跳转函数功能
					function justJumpFunc(){
						// 取消定时器
						clearInterval(cycle1);
						// 立即跳转
						if(redirect_uri=="" || redirect_uri==null){
							// 跳转到首页
							window.location.href="/";
						}else{
							// 跳转到指定页面
							window.location.href=redirect_uri;
						}
					}
					
					/* 创建倒计时(定时器到后自动跳转到指定页面) */
					cycle1 = setInterval(function() {
						var time = $time.text();
						if(time <= "0"){
							// 执行"立即跳转"按钮的功能
							justJumpFunc();
						}else{
							$time.text($time.text()-1)
						}
					}, 1000);
					
					// 跳转到选项卡3
					switchCart(3);
					
					// 立即跳转按钮功能
					$justJump.click(justJumpFunc);
					// 取消跳转按钮功能
					$aboJump.click(function(){
						// 将相关清除
						$tm.find("*").remove();
						// 取消定时器
						clearInterval(cycle1);
					});
				} else {
					alert(re.msg);
				}
			}
		});
	});
	
});

