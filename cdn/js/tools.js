(function (global) {
	var _ut = function () {
		/**
		 * Cookie工具
		 * set(name, value[, domain(默认为顶级域名)[, path(默认为"/")[, hour(单位:小时,默认为浏览器时关闭失效)]]])
		 * del(name[, domain(默认为顶级域名和当前域名)[, path(默认为"/"和当前路径)]])
		 * 	del().........删除
		 */
		var $Cookie = function () {
			/**
			 * @hour: 过期时间(小时)
			 */
			function set(name, value, domain, path, hour) {
				if (hour) {
					var expire = new Date();
					expire.setTime(expire.getTime() + 3600000 * hour);
				}
				document.cookie = name + "=" + encodeURIComponent(value) + "; " +
						(hour ? ("expires=" + expire.toGMTString() + "; ") : "") +
						(path ? ("path=" + path + "; ") : "path=/; ") +
						(domain ? ("domain=." + $Url.parseDomain(domain) + ";") : ("domain=." + $Url.parseDomain(document.location.host) + ";"));
				return true;
			}
			function del(name, domain, path) {
				function deleteCookie (name, domain, path) {
					document.cookie = name + "=; expires=Mon, 26 Jul 1997 05:00:00 GMT; " + (path ? ("path=" + path + "; ") :
						"path=/; ") + (domain ? ("domain=" + domain + ";") : ("domain=" + document.location.host + ";"));
				}
				
				// 精确删除
				if (name && domain && path) {
					deleteCookie(name, domain, path);
				}
				
				// 删除全部path
				else if (name && domain) {
					// 获取当前页面的路径
					var path = $Url.getUrlPath();
					// 删除根路径
					deleteCookie(name, domain);
					// 删除当前路径
					deleteCookie(name, domain, path);
				}
				
				// 删除全部domain及全部path
				else if (name){
					// 获取当前页面的路径
					var host = $Url.parseDomain();
					
					del(name, host);
					del(name, "." + host);
					del(name, document.location.host);
				}
				
				// 删除全部Cookie
				else {
					for (var key in $Cookie.getAll()) {
						del(key);
					}
				}
				return true;
			}
			function get(name) {
				var r = new RegExp("(?:^|;+|\\s+)" + name + "=([^;]*)"),
					m = document.cookie.match(r);
				return decodeURIComponent(!m ? "" : m[1]);
			}
			function getAll() {
				var cookies={},cookieArray = document.cookie.split(/; /);
				for(var i=0,l=cookieArray.length;i<l;i++){
					var cookie=cookieArray[i].split("=");
					cookies[cookie[0]]=decodeURIComponent(cookie[1]);
				}
				return cookies;
			}
			
			return {
				set: set,
				del: del,
				get:  get,
				getAll: getAll 
			}
		}();

		// 控制台输出工具
		var $Console = function (s) {
			var LEVELS = {
				log: 3,
				info: 2,
				warn: 1,
				error: 0
			};
			var _QC_CONSOLE_DEBUG_LEVEL = LEVELS.info;
			var cons_prefix = s ? s : ' :: [UT] > ';
			var trace = function (funName) {
				return function (args) {
					window.console && console[funName] && getDebugLevel() >= LEVELS[funName] && console[funName](cons_prefix +
						args);
				}
			};
			var getDebugLevel = function () {
				return ~~(_QC_CONSOLE_DEBUG_LEVEL || LEVELS.info);
			};
			return {
				log: trace("log"),
				info: trace("info"),
				warn: trace("warn"),
				error: trace("error"),
				setLevel: function (lvNm) {
					return _QC_CONSOLE_DEBUG_LEVEL = LEVELS[lvNm] || _QC_CONSOLE_DEBUG_LEVEL;
				}
			}
		};

		/**移动组件函数
		 * 鼠标"拖动"$mouse,移动$mobile
		 * 如果只传递进来一个元素,则表示拖动谁就移动谁
		 */
		var $mobile = function ($mouse, $mobile) {
			/* 如果只传递进来一个元素,则表示拖动谁就移动谁 */
			if ($mobile == undefined) {
				$mobile = $mouse;
			}

			/* 将鼠标样式改为"移动"样式 */
			$mouse.css('cursor', 'move');

			$mobile.css("position", "fixed");

			/* 设置元素的初始位置 */
			var stuname_x = ($(window).width() - $mobile.width()) / 2;
			var stuname_y = ($(window).height() - $mobile.height()) / 2;
			$mobile.css({
				'left': stuname_x,
				'top': stuname_y
			});

			/* 当鼠标按下时 */
			$mouse.mousedown(function () {
				beginmove(event, $mobile);
			});

			/* 当鼠标按键弹起时，解除元素移动，让元素停留在当前位置 */
			$(document).mouseup(function () {
				$(this).unbind("mousemove");
				// 记录元素位置
				// 当前元素的位置
				var cx = $mouse.css("left").split("px").join("");
				var cy = $mouse.css("top").split("px").join("");
				// 保存位置
				stuname_x = cx;
				stuname_y = cy;
			})

			/* 开始移动元素位置 */
			function beginmove(e, _this) {
				/* 记录开始移动时待移动元素的位置 */
				var cx = _this.css("left").split("px").join("");
				var cy = _this.css("top").split("px").join("");
				/* 记录开始移动时鼠标的位置 */
				var sx = e.pageX;
				var sy = e.pageY;

				/* 计算当前鼠标和元素之间位置的偏移量，让移动后的元素以鼠标按下时的位置为坐标。
				（默认以元素左上点为坐标） */
				var px = sx - cx;
				var py = sy - cy;

				/* 绑定鼠标的移动事件，因为光标在DIV元素外面也要有效果，所以要用doucment的事件，而不用DIV元素的事件 */
				$(document).bind("mousemove", function (ev) {
					/* 当前鼠标的位置 */
					sx = ev.pageX;
					sy = ev.pageY;

					/* 当前元素位置(鼠标位置减去偏移量) */
					var _x = sx - px;
					if (_x < 0) {
						_x = 0;
					} else if (_x >= ($(window).width() - $("#login").width())) {
						_x = $(window).width() - $("#login").width();
					}
					var _y = sy - py;
					if (_y < 0) {
						_y = 0;
					} else if (_y > ($(window).height() - $("#login").height())) {
						_y = $(window).height() - $("#login").height();
					}
					/* 设定元素位置 */
					_this.css({
						'left': _x,
						'top': _y
					});
				});
			}
		};

		/*使指定元素居中*/
		var $center = function ($mobile) {
			if ($check.isDomObject($mobile)) {
				/*如果是Doc对象将其转换为JQuery对象*/
				$mobile = $($mobile);
			} else if (!isJQueryObject($mobile)) {
				/*如果既不是Jquery对象,也不是Dom对象则直接退出*/
				throw new Error("center方法接收到的参数既不是jQuery对象,也不是docment对象!");
			}
			$mobile.ready(function () {
				$mobile.css({
					'top': ($(window).height() - $mobile[0].offsetHeight) / 2,
					'left': ($(window).width() - $mobile[0].offsetWidth) / 2,
					'position': 'absolute'
				});
			});
		};

		/*
		* 检测相关类
		* 1. checkInput($o)检查输入框$o的内容是否符合要求
		* 2. checkInputCleanIcon() 检测全部输入框,当有数据时时显示清空按钮(并实现按钮的功能)
		* 1. isJQueryObject(obj):判断是否为jQuery对象
		* 2. isDomObject(obj):判断是否为DOM对象
		* 3. ifMousemove(hd): 判断鼠标是否在指定元素外
		*/
		var $check = {
			/*检查输入框内容是否符合要求
			 * 规则及相应的错误信息定义在属性"check"中: check='[{rule:"^\\d{8}$",error:"位数不符合要求"},]'
			 * 返回对象{success:true,msg:"OK"} {success:false,msg:"错误提示信息"}
			 * 注意:
			 * 		检查时依次检查定义的规则,遇到错误就停止继续监测,并马上返回该错误信息
			 */
			checkInput : function ($o) {
				/* 如果不是doc对象则转换为dom对象 */
				if ($check.isJQueryObject($o)) {
					$o = $o[0];
				}
				/* 定义返回对象模型 */
				var re = {
					success: true,
					msg: "OK"
				};

				/* 获取要判断的内容 */
				var $value = $o && $o.value;
				if($value == undefined || $value == "") return re;
				
				/* 限制输入长度 */
				var langth = $o.getAttribute("maxlength");
				if (langth != null && $value.length > langth) {
					$o.value = $value.substring(0, langth);
					$value = $o.value;
				}

				/* 检查是否符合要求 */
				/* 获取检查错误的正则表达式及错误提示组成的JSON字符串 */
				var str = $o.getAttribute("check");
				if (str != null) {
					/* 将JSON字符串转换为对象 */
					var jsonObj = eval("(" + str + ")"); 

					for (var var1 in jsonObj) {
						/* 获取验证字符串(一般为正则表达式) */
						var rule = jsonObj[var1].rule;
						if ($value.match(rule) == null) {
							re.success = false;
							re.msg = jsonObj[var1].error;
							return re;
						}
					}
				}
				return re;
			},
			
			/**
			 * 检测全部输入框,当有数据时显示清空按钮(并实现按钮的功能)
			 */
			checkInputCleanIcon : function(){

				var
				f6211 = function(){
					var	$this = $(this),
						$myInput = $this.closest(".input-close-icon").find(".clean-icon"),
						myAttr = $this.attr("aria-describedby"),
						$ariaDescribedby = $("#"+myAttr);
					
					// 有内容则显示清空按钮
					if($this.val()==""){
						$myInput.addClass("d-none");
					} else {
						$myInput.removeClass("d-none");
					}

					// 每输入一个就验证输入是否合法
					var res = myAttr && $check.checkInput($this);
					res && (res.success ? $ariaDescribedby.html(null) : $ariaDescribedby.html(res.msg));
					
					// 使清空按钮生效
					$myInput.find("svg").click(function(){
						$myInput.addClass("d-none"),	//隐藏清空按钮
						$this.val(null);				//清空输入框的内容
						// 清空错误提示信息
						myAttr && $ariaDescribedby.html(null);
						// 手动触发change事件(值改变事件)
						$this.trigger('change');
					});
				},
				$input = $("input");
				
				$input.unbind('input propertychange', f6211),
				$input.bind('input propertychange', f6211);
			},
			
			/*判断是否为jQuery对象*/
			isJQueryObject: function ($o) {
				if ($o == null) {
					return false;
				}

				if ($o instanceof jQuery) {
					return true;
				} else {
					return false;
				}
			},
			/*判断是否为DOM对象*/
			isDomObject: function (obj) {
				//首先要对HTMLElement进行类型检查，因为即使在支持HTMLElement的浏览器中，类型却是有差别的，
				//在Chrome,Opera中HTMLElement的类型为function，此时就不能用它来判断了
				return (typeof HTMLElement === 'object') ?
					function (obj) {
						return obj instanceof HTMLElement;
					} :
					function (obj) {
						return obj && typeof obj === 'object' && obj.nodeType === 1 && typeof obj.nodeName === 'string';
					};
			},
			/**
			 * 判断鼠标是否在指定元素外
			 * 当原生鼠标移除某元素不起作用或乱跳时可以使用该函数
			 */
			ifMousemove: function (hd) {
				var x = event.clientX;
				var y = event.clientY;
				for (i in hd) {
					var x1 = hd.offsetLeft
					var y1 = hd.offsetTop
					var toSprmIWidth = hd.offsetWidth; //对象宽
					var toSprmIHeight = hd.offsetHeight; //对象高
					if (x < x1 || y < y1 || x > (x1 + toSprmIWidth) || y > (y1 + toSprmIHeight)) {
						return true;
					}
					return false;
				}
			},
			// 其他检查函数函数
			
			
		};

		/*
		 * 数字相关对象
		 * 1.randomNum:生成从minNum到maxNum的随机数
		 */
		var $Number = {
			/*生成从minNum到maxNum的随机数*/
			randomNum: function (minNum, maxNum) {
				switch (arguments.length) {
					case 1:
						return parseInt(Math.random() * minNum + 1, 10);
						break;
					case 2:
						return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10);
						break;
					default:
						return 0;
						break;
				}
			},
			/* 将10进制转为62进制 */
			string10to62: function (number) {
				var chars = '0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ'.split(''),
					radix = chars.length,
					qutient = +number,
					arr = [];
				do {
					mod = qutient % radix;
					qutient = (qutient - mod) / radix;
					arr.unshift(chars[mod]);
				} while (qutient);
				return arr.join('');
			},
			/* 将10进制转为62进制 */
			string62to10: function (number_code) {
				var chars = '0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ',
					radix = chars.length,
					number_code = String(number_code),
					len = number_code.length,
					i = 0,
					origin_number = 0;
					
				while (i < len) {
					origin_number += Math.pow(radix, i++) * chars.indexOf(number_code.charAt(len - i) || 0);
				}
				return origin_number;
			},
		};

		/**
		 * 开启或关闭蒙版
		 * 1. mb.on(zIndex):开启蒙版
		 * 2. mb.off():关闭蒙版
		 */
		var $Mb = function () {
			var
				/* 默认蒙版层数 */
				deIndex = 999,
				/*随机生成的蒙版标识id*/
				id = "mb" + $Number.randomNum(100000, 999999),
				/*蒙版参数*/
				mbConfig = {
					width: "100%",
					height: "100%",
					position: "fixed",
					top: 0,
					zIndex: 1,
					backgroundColor: "rgba(0, 0, 0, 0.5)",
				};

			/*将蒙版参数转换为HTML代码*/
			function mbHtmlToString() {
				//		var mbHtml = '<div style=""></div>';
				var mbHtml = '<div id="' + id +
					'"style="' +
					'width: ' + mbConfig.width + ';' +
					'height: ' + mbConfig.height + ';' +
					'position: ' + mbConfig.position + ';' +
					'top: ' + mbConfig.top + ';' +
					'z-index: ' + mbConfig.zIndex + ';' +
					'background-color: ' + mbConfig.backgroundColor + '' +
					';"></div>'
				return mbHtml;
			}

			/*添加蒙版*/
			function addMb() {
				$(document).ready(function () {
					$("body").append(mbHtmlToString());
				});
			}

			/*开启蒙版
			 * 1.查看是否有蒙版
			 * 2.如果蒙版存在,则查看蒙版的层是否大于等于操作元素的指定的层(如果未指定则大于它本身层)
			 * 3.如果小于则修改
			 * 4.如果蒙版不存在则将生成新的蒙版代码插入到Doc中
			 * 5.如果明确规定蒙版的层数,则直接使用规定的数
			 */
			onFun = function (zIndex) {
				var /*蒙版*/
					$mbDoc = document.getElementById(id);

				/*如果明确规定层数*/
				mbConfig.zIndex = zIndex == null ? deIndex : zIndex;

				/*判断蒙版是否存在*/
				if ($mbDoc == null) { // 不存在
					/*将蒙版代码添加到doc*/
					addMb();
				} else { // 存在
					$($mbDoc).css("z-index", mbConfig.zIndex);
				}
			}

			/* 关闭蒙版
			 * */
			offFun = function () {
				$("#" + id).ready(function () {
					$("#" + id).remove();
				})
			}

			return {
				on: onFun,
				off: offFun,
			}
		}();

		/**
		 * 输入框相关操作方法
		 * changeButton("on")----(默认)可点击
		 * changeButton("off")---不可点击
		 */
		var $Component = function(){
			function changeButton($obj,status){
				if(status == "off"){
					$obj.addClass("disabled").attr("disabled","");
				}else{
					$obj.removeClass("disabled").removeAttr("disabled");
				}
			}
			
			return {changeButton: changeButton};
		}();

		/**
		 * URL的相关方法
		 * .getHost(url)...........获取指定URL的主机名(默认获取当前页面的主机名)
		 * .parseDomain(url).......获取url的顶级域名(默认获取当前页面的顶级域名)
		 * .getUrlPath(urlStr).....获取当前相对路径(默认获取当前页面的相对路径)
		 * .getParaNames(urlStr)...获取所有参数列表(默认获取当前页面的参数列表)
		 * .getUrlParam(paraName)..获取当前URL的参数
		 * .getUrlAll()............获取当前页面的全地址
		 * .getParas(urlStr).......获取指定URL所有参数组成的对象(默认获取当前URL)
		 */
		var $Url = function () {
			var
			// 获取域名
			getHost = function (url) {
				if (!url) {
					return document.location.host;
				} else {
					// 可能的地址形式
					// 10.0.0.5
					// 10.0.0.5/index
					// http://10.0.0.5/index
					
					var a = url.split("//");
					if (a.length >= 2) {
						return a[1].split("/")[0];
					} else {
						return a[0].split("/")[0];
					}
				}
				
			},

			// 获取顶级域名
			parseDomain = function(urlStr) {
				var str = urlStr ? getHost(urlStr) : document.location.host;
				
				var topLevel = ['com', 'net', 'org', 'gov', 'edu', 'mil', 'biz', 'name', 'info', 'mobi', 'pro', 'travel', 'museum',
					'int', 'areo', 'post', 'rec'
				];
				var domains = str.split('.');
				if (domains.length <= 1) return str;
				if (!isNaN(domains[domains.length - 1])) return str;
				var i = 0;
				while (i < topLevel.length && topLevel[i] != domains[domains.length - 1]) i++;
				if (i != topLevel.length) return domains[domains.length - 2] + '.' + domains[domains.length - 1];
				else {
					i = 0;
					while (i < topLevel.length && topLevel[i] != domains[domains.length - 2]) i++;
					if (i == topLevel.length) return domains[domains.length - 2] + '.' + domains[domains.length - 1];
					else return domains[domains.length - 3] + '.' + domains[domains.length - 2] + '.' + domains[domains.length - 1];
				}
			},
			
			// 获取全地址
			getUrlAll = function () {
				return document.location.href;
			},

			// 获取相对路径
			getUrlPath = function (urlStr) {
				// 首先获取 Url，然后把 Url 通过 "//" 截成两部分，再从后一部分中截取相对路径。
				// 如果截取到的相对路径中有参数，则把参数去掉。
				var url = urlStr || document.location.href;　　
				
				// 去掉协议部分
				var arrUrl = url.split("//");
				arrUrl = arrUrl[arrUrl.length-1];

				// 去掉HOST部分
				var arrUrl = arrUrl.replace(getHost(arrUrl),"");

				// 去掉参数
				var arrUrl = arrUrl.split("?")[0];
				
				// 去掉请求部分
				var arrUrl = arrUrl.replace(/\/[^(\/)]+$/,"");
				return arrUrl || "/";
			},

			// 获取指定URL所有参数组成的对象(默认获取当前URL)
			getParas = function(urlStr){
				// 同于存储键值对
				var paras = {};
				
				var url = urlStr || document.location.href;
				var arrObj = url.split("?");
				
				if (arrObj.length > 1) {
					var arrPara = arrObj[1].split("&");
					var arr;
					for (var i = 0; i < arrPara.length; i++) {
						arr = arrPara[i].split("=");

						if (arr != null) {
							paras[arr[0]] = arr[1];
						};
					};
				};
				return paras;
			},
			
			// 获取所有参数名(以数组方式返回)
			getParaNames = function (urlStr) {
				// 用于存放参数列表
				var paraName = [];

				var arrObj = getParas(urlStr || document.location.href);

				for (var i in arrObj) {
					paraName[paraName.length] = i;
				}
				
				return paraName;
			},
			
			// 获取当前URL的参数值
			getUrlParam = function (paraName) {
				var paraName = [];
				
				var arrObj = getParas(document.location.href);
				
				return arrObj[paraName];
			};

			return {
				getHost: getHost,
				parseDomain: parseDomain,
				getUrlAll: getUrlAll,
				getUrlPath: getUrlPath,
				getParas:getParas,
				getParaNames: getParaNames,
				getUrlParam: getUrlParam,
			};
		}();

		/**
		 * 轮播函数
		 */
		var $banner = function (duration, playTime) {
			//选择第一个li标签并复制
			var $newli = $(".bannerBox li").eq(0).clone();
			//将复制的标签加入到轮播的最后一张后面
			$(".bannerBox").append($newli);
			var $osb = $(".scrollBanner");
			var $oUL = $(".bannerBox");
			var $oLIs = $oUL.children();
			//获取所有小圆点
			var $oNavlist = $(".scroll_num a");
			//获取左右箭头
			var $arrLeft = $(".arr_left");
			var $arrRight = $(".arr_right");
			//表示当前元素的位置
			var index = 0;
			//获取所有轮播元素的个数
			var imgLength = $oLIs.length - 1;
			//总体高宽度
			var bannerWidth = $osb.width();
			var bannerHeight = $osb.height();

			//自动设置
			auto();

			function auto() {
				//宽度=(所有图片张数+1)*单张图片的宽度*/
				$oUL.css({
					"width": $oLIs.length * bannerWidth + "px"
				});
				//设置子元素的高宽
				$(".bannerBox li a img").css({
					"width": bannerWidth + "px"
				}).css({
					"height": bannerHeight + "px"
				})
			}

			//点击下一张图片按钮
			$arrRight.on("click", function () {
				index++;
				if (index > imgLength) {
					index = 1;
					$oUL.css({
						"left": 0 + "px"
					})
				}
				//执行一个动画
				move(index);
			})
			//点击上一张图片按钮
			$arrLeft.on("click", function () {
				index--;
				if (index < 0) {
					index = 2;
					$oUL.css({
						"left": -(imgLength) * bannerWidth + "px"
					})
				}
				//执行一个动画
				move(index);
			})

			function move(index) {
				$oUL.stop().animate({
					"left": index * (-bannerWidth) + "px"
				}, playTime);
				$oNavlist.removeClass("current");
				$oNavlist.eq(index >= imgLength ? 0 : index).addClass("current")
			}

			//定义一个定时器()每隔时间duration则调用函数
			var timer = setInterval(function () {
				$arrRight.click();
			}, duration);

			//鼠标划入、划出执行的方法
			function clearTimer($ele) {
				$ele.hover(function () {
					//停止定时器
					clearInterval(timer);
				}, function () {
					//重新创建一个定时器
					timer = setInterval(function () {
						$arrRight.click();
					}, duration);
				}); //两个方法分别对应划入和划出持行的方法
			}

			//鼠标滑过哪些元素停止轮播
			clearTimer($oUL);
			clearTimer($arrLeft);
			clearTimer($arrRight);
			clearTimer($oNavlist);

			//点击对应的小圆点跳转到对应的元素上
			$(".scroll_num li").each(function () {
				var _index = $(this).index();
				$(this).on("click", function () {
					if (_index > imgLength) {
						_index = 1;
					}
					move(_index);
				})
			})

			//鼠标划入显示左右按钮
			$(".scrollBanner").hover(function () {
				$arrLeft.css("display", "inline-block");
				$arrRight.css("display", "inline-block");
			}, function () {
				$arrLeft.css("display", "none");
				$arrRight.css("display", "none");
			})
		}


		/**
		 * 显示提示信息工具
		 * 参数格式为:("提示信息"[,提示停留时长][,消失动画时长][,提示信息类型(默认为成功提示)])
		 * tips("新的信息")-----------------显示提示信息
		 * tips("新的信息",1000)------------1000毫秒后开始消失
		 * tips("新的信息",1000,2000)-------1000毫秒后开始消失,并且消失动画执行2000毫秒才结束
		 * tips("错误信息","error")---------显示错误信息
		 */
		var $tips = function () {
			// 检查参数个数是否符合要求
			if (arguments.length < 1 || arguments.length > 4) {
				throw new Error('参时传递错误,参数格式为:("提示信息"[,提示停留时长][,消失动画时长][,提示信息类型(默认为成功提示)])');
			}
			
			// t1: 显示时间, t2: 渐隐动画效果时间(隐藏后删除该元素)
			var
			// 默认显示1000毫秒后开始消失
			t1 = 1000,
			// 默认消失动画时长为2000毫秒
			t2 = 2000,
			
			// 创建一个div用于显示信息
			div1 = '<div class="row justify-content-center" style="float: left;position: fixed;width: 100%;z-index: 99999999;top: 5rem;">'+
						'<div class="alert %alert-type% col-auto" role="alert">'+
							arguments[0]+
						'</div>'+
					'</div>',
			// 默认样式
			alertType = "alert-success";

			// 可变参数支持
			for (var i = 1; i < arguments.length; i++) {
				// 判断第二个参数
				if (i == 1){
					if(typeof arguments[i] == "number") {
						t1 = arguments[i];
						continue;
					} else {
						if ("error" == arguments[i]) {
							alertType = "alert-danger";
						}
						break;
					}
				}
				
				// 判断第三个参数
				if (i == 2){
					if(typeof arguments[i] == "number") {
						t2 = arguments[i];
						continue;
					} else {
						if ("error" == arguments[i]) {
							alertType = "alert-danger";
						}
						break;
					}
				}
				
				// 判断第四个参数
				if(typeof arguments[i] == "string" && "error" == arguments[i]) {
					alertType = "alert-danger";
				}
			}
			
			var $div = $(div1.replace(/%alert-type%/g, alertType));
			$div.appendTo("body");

			setTimeout(function(){
				// 渐变隐藏该元素
				$div.fadeOut(t2);
				// 开始隐藏开始计时t2时间后删除该节点
				setTimeout(function(){
					$div.remove();
				}, t1 + t2);
			},t1);
		};

		return {
			cookie:		$Cookie,	// Cookie
			console:	$Console,	// 控制台输出工具
			mobile:		$mobile,	// 移动组件函数
			center:		$center,	// 使指定元素居中
			check:		$check,		// 检测相关类
			number:		$Number,	// 数字相关
			mb:			$Mb,		// 蒙版操作
			input:		$Component,	// 输入框操作
			component:	$Component,	// 组件操作
			url:		$Url,		// URL地址相关方法
			banner:		$banner,	// 轮播函数
			tips:		$tips,		// 显示提示信息工具
			version: function () {
				return "1.1.0";
			}
		}
	};


	/* 冻结对象并向外暴露唯一变量 */
	if (typeof Object.freeze == "function") {
		Object.freeze(_ut);
	}
	global.Tools = _ut();
}(typeof window !== "undefined" ? window : this));
