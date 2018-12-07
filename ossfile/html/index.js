$(function() {
	var
	// 用户对象
	_user = typeof USER != "undefined" ? USER : null,
	// 自定义工具包
	_ut = typeof UT != "undefined" ? UT : null;
	
	/* 使主要内容区高度充满整个容器 */
	(function() {
		var
			// 待设置高度的元素
			mybody = $("#mybody"),
			// 其他元素的高度
			otherHeight = 50,
			// body内容的高度
			bodyHeight = document.body.scrollHeight;
		
		// 屏幕可见区域高度 - 其他元素的高度
		mybody.css("height", $(window).height() - otherHeight + "px");
		window.onresize = function() {
			mybody.css("height", $(window).height() - otherHeight + "px");
		}
	})();

	/* 打开页面是首先进行登录 */
	(function() {
		if (!_user.islogin()) {
			_user.login();
		}
	})();

	/* 文件所用操作 */
	(function(){
		var
		// 所有文件和目录的共同父节点
		$panValue = $('#pan_value'),
		// 返回上一级按钮
		$backspace = $("#backspace"),
		// 地址栏
		$address = $("#address"),
		// 搜索框
		$sidebarNav = $("#sidebar-nav"),
		// 显示方式按钮
		$showType = $("#show-type");
		
		/* 点击路径时执行显示该路径下的所以内容 */
		var pathBotton = function(){
			var
			$this = $(this),
			buttons = $address.find('button'),
			// 路径的长度
			length = buttons.length,
			// 点击路径在所有路径中的索引
			index = $this.index();
			
			if(length > 1 && index != length-1) {
				// 加载点击目录下的所有内容
				showFileByPathId($this.attr("pathid"));
				// 删除点击按钮后面的内容
				deletePath(buttons,index);
			}
		};
		
		/* 点击目录时加载新内容 */
		var directoryNext = function() {
			var $this = $(this),
				// 代表整个文件(可能是目录)本身
				$file = $this.closest(".file"),
				// 获取当前被点击的目录的"path_id"
				pathId = $file.find(".fileId").val(),
				// 记录当前被点击元素的名字
				fileName = $file.find(".file-title").text(),
				// 原始路径按钮
				ht = '<button type="button" class="btn btn-link btn-secondary" pathid="%pathId%">%fileName%/</button>';
				
			// 加载当前目录下的所有内容
			showFileByPathId(pathId);
			
			// 将当前目录名添加到"地址栏"
			$address.append(ht.replace(/%pathid%/ig, pathId).replace(/%fileName%/g, fileName)),
			
			// 为地址栏按钮添加点击事件
			$address.find("button").unbind("click", pathBotton).bind("click", pathBotton);
		};
		
		/* 删除指定路径后面的路径
		 * @param buttons 所有路径
		 * @param index 当前索引,即删除此索引后的所以内容
		 */
		function deletePath(buttons,index) {
			for (var i=buttons.length-1;i>index;i--) {
				buttons.eq(i).remove();
			}
		}
		
		/**
		 * 将传入的文件或目录对象显示出来,并为这些节点添加相应的事件
		 * 参数为文件或目录对象组成的数组
		 * @param fileList 需要显示的目录对象和文件对象组成的数据
		 * @param notClean 是否不清空原数据(即保留原数据)
		 */
		function showFileList(fileList, notClean){
			// 清空原来的文件列表
			if (!notClean){
				$panValue.html(null);
			}
			
			// 遍历并显示所有文件或目录
			for (var index in fileList) {
				$panValue.append(File.getFileHtml(fileList[index]));
			}
			
			// 已经获取到内容
			if ($panValue.html()) {
				// 点击该节点(表示目录)会进入下一级目录
				$panValue.find(".file .next").unbind("click", directoryNext).bind("click", directoryNext);
				
				// 复制链接地址功能(通过插件实现)
				var cli = new ClipboardJS('.copy-link');
				cli.on('success', function(e) {
					_ut && _ut.tips("复制成功");
				});
				cli.on('error', function(e) {
					console.log(e);
					_ut && _ut.tips("复制失败,可升级浏览器后再试!");
				});
			}
			// 没有获取到内容
			else {
				// 提示
				// alert("没有数据!!!")
			}
		}
		
		/**
		 * 根据传入类型显示文件列表
		 * 可选值: all(根目录)/image(图片)/document(文档)/video(视频)/music(音乐)
		 * 如果成功则返回"true",否则返回"false"
		 */
		function showFileTypeList(fileType) {
			switch (fileType){
				case "all":
					// 显示全部文件(根目录)
					showFileByPathId();
					// 删除根路径后面的所有路径
					deletePath($address.find('button'),0);
					// 显示地址栏
					$address.parent().removeClass("none");
					break;
				case "image":
				case "document":
				case "video":
				case "music":
					// 隐藏地址栏
					$address.parent().addClass("none");
					// 加载内容
					http({
						url: "/getPathAndFileByType",
						data: {typeName:fileType},
						back: function(result){
							if (result.code == "000") {
								// 显示数据并执行其他逻辑
								showFileList(result.data);
							} else {
								alert(result.msg);
							}
							
						},
					});
					break;
				default:
					break;
			}
		}
		
		/**
		 * 刷新当前页面
		 * 1.如果左侧导航没有选中,则可能是搜索,即不刷新
		 * 1.如果左侧导航为全部文件,则按路径刷新
		 * 3.如果是分类则按分类重新加载
		 */
		function refresh() {
			var $navUi = $("#nav-ui .active"),
				// 分类名
				type = $navUi.attr("type");
				
			switch (type){
				case "all":
					// 显示全部文件(根目录)
					showFileByPathId($address.find("button").last().attr("pathid"));
					break;
				case "image":
				case "document":
				case "video":
				case "music":
					// 重新加载内容
					showFileTypeList(type);
					break;
				default:
					break;
			}
		}
		
		/**
		 * 统一进行Http请求(如果出错时进行统一处理)
		 * @param url
		 * @param back(data) 回调函数,参数为返回的data数据(文件对象或目录对象组成的列表)
		 */
		function http(obj) {
			if (typeof obj == "object") {
			
				$.ajax({
					url: obj.url,
					data: obj.data,
					success: function(result){
						if(typeof result == "string") {
							result = eval("(" + result + ")");
						}
						
						console.log(result);
						
						// 未登录
						if (result.code == "141") {
							USER.login();
						}
						// 未开通网盘功能
						else if (result.code == "400") {
							// 提示用户开通（显示开通Modal框）
							$("#openService").modal('show');
						}
						// 其他未统一处理的状态码(包括正确000)
						else {
							if (typeof obj.back == "function") {
								obj.back(result);
							}
						}
					},
					error: function(data){
						console.log("请求出错");
						console.log(data);
					}
				});
			}
		}
		
		/**
		* 根据"目录ID"显示文件列表(为空或者0表示获取根目录下的内容)
		* @param pathId 目录Id(即该目录下的所有文件)
		*/
		function showFileByPathId(pathId) {
			
			http({
				url: "/getPathStructureList",
				data: {parentPathId:pathId},
				back: function(result){
					if (result.code == "000") {
						// 显示数据并执行其他逻辑
						showFileList(result.data);
					} else {
						alert(result.msg);
					}
				},
			});
		}
		
		/**
		* 根据文件名搜索文件信息
		* @param fileName 部分文件名
		*/
		function sidebarByName(fileName) {
			http({
				url: "/selectAllByPathName",
				data: {pathName:fileName},
				back: function(result){
					if (result.code == "000") {
						// 显示数据并执行其他逻辑
						showFileList(result.data);
					} else {
						alert(result.msg);
					}
				},
			});
		}
		
		/**
		 * => 此自执行函数中定义的是不改变的按钮的事件
		 */
		(function(){
			// 默认显示全部文件(根目录)
			showFileByPathId();
			
			// 左侧显示文件类型选项卡的点击事件
			$("#nav-ui ul li").click(function(){
				var $this = $(this);
				
				if (!$this.hasClass("active")) {
					// 清空所有标记
					$this.parent().children().removeClass("active");
					// 为当前点击元素添加标记
					$this.addClass("active");
					// 加载内容
					showFileTypeList($this.attr("type"));
				}
			});
			
			// 返回上一级按钮
			$backspace.click(function(){
				// 
				var $buttons = $address.find("button"),
				length = $buttons.length;
				
				if (length > 1) {
					// 加载上一级目录文件信息
					showFileByPathId($buttons.eq(length-2).attr("pathid"));
					
					// 删除最后一个路径
					$buttons.eq(length-1).remove();
				}
			});
		
			// 改变显示方式(大小图标切换)
			$showType.find(".dropdown-menu .dropdown-item").click(function(){
				$this = $(this);
				if ($this.attr("value") != $showType.attr("value")) {
					// 改变值
					$showType.attr("value", $this.attr("value"));
					$showType.find("button span").html($this.html());
					
					/**
					 * 显示内容(显示前判断显示类型
					 * 可选值为:all(全部文件)/image(图片)/document(文档)/video(视频)/music(音乐))
					 */
					// 获取显示类型
					var showTypeName = "all";
					$("#nav-ui ul li").each(function(){
						var $this = $(this);
						if ($this.hasClass("active")) {
							showTypeName = $this.attr("type");
						}
					});
					
					switch (showTypeName){
						case "image":
						case "document":
						case "video":
						case "music":
							// 加载内容
							showFileTypeList(showTypeName);
							break;
							
						case "all":
							// 判断路径中最后一个路径的pathId值
							$address = $("#address");
							var $buttons = $address.find("button");
							
							var pathId = $buttons.eq($buttons.length-1).attr("pathid");
							
							// 加载内容
							showFileByPathId(pathId);
							break;
						default:
							console.log("功能暂时未实现")
							break;
					}
				}
			});
		
			// 搜索框搜索事件
			$sidebarNav.find("#search_button").click(function(){
				// 获取搜索框的内容
				var value = $sidebarNav.find("input").val();
				if(value != undefined && value != "") {
					// 清空左边选项卡的标记色
					$("#nav-ui ul li").removeClass("active");
					
					// 将内容进行编码
					value = encodeURIComponent(value);
					
					// 持行搜索方法并显示搜索得到的数据
					sidebarByName(value);
				}
			});
		
			// 新建目录事件
			$('#newDirectory').on('okHide', function(e) {
				var 
				input = $("#newDirectory .modal-body form input"),
				value = input.val();
				if (value == ""){
					return false;
				}
				// 清空输入框
				input.val(null);
				// 创建文件夹
				http({
					url: "/insertPathByPathName",
					data: {
						// 新目录的名字
						pathName:value,
						// 父文件目录的id(即目录要创建在哪里)
						parentPathId: $address.find("button").eq(length-1).attr("pathid")
					},
					back: function(result){
						if (result.code == "000") {
							// 显示数据并执行其他逻辑(不清空原来的数据)
							showFileList([result.data], true);
						} else {
							alert(result.msg);
						}
					},
				});
			});
			
			$("#openService").on('okHide', function(e) {
				http({
					// 开通网盘功能
					url: "/openService",
					back: function(result){
						if (result.code == "000") {
							// 刷新页面
							history.go();
						} else {
							alert(result.msg);
						}
					},
				});
			});
			
			// 上传文件(传输列表)
			(function(){
				var
				// 文件传输列表Modal框
				$uploadFile = $("#uploadFile"),
				// 存储待传输的文件列表
				/*[{id: Math.random()(随机数),file: file对象},{id: Math.random()(随机数),file: file对象}]*/
				fileList = [],
				// 显示多进度的div元素
				$fileLoadingList = $("#file-loading-list"),
				// 创建ajax后返回ajax对象(用于取消上传操作)
				myAjax = null,
				// 当前正在上传的文件对象
				file,
				// 进度条html代码
				fileLoadinghtml = '<div class="sui-msg msg-block sui-progress progress-large" key="%fileKey%">'+
										'<div style="width: 0%;" class="bar"></div>'+
										'<div class="file-loading">%fileName%'+
											'<button type="button" data-dismiss="msgs" class="sui-close">×</button>'+
										'</div>'+
									'</div>';
				
				// ajax递归上传文件
				var sendAjax = function(pathid) {
					if (fileList.length <= 0) {
						// 如果执行过AJAX则刷新页面
						if (myAjax) {
							// 没有文件可上传,刷新页面
							refresh();
							// 收起传输列表
							$uploadFile.modal('hide');
							// 将AJAX对象置为空
							myAjax = null;
						}
						
						return;
					}
					// 删除并返回数组的第一个元素
					file = fileList.shift();
					
					var formData = new FormData();
					/* upFile 为键名,需要后台以同样的名字才能获取到该文件 */
					formData.append('upFile', file.fileValue);
					pathid && formData.append('parentPathId', pathid);
					
					myAjax = $.ajax({
						url: "/localFile",
						type: "POST",
						// cache: false,
						data: formData,
						/**
						*必须false才会自动加上正确的Content-Type
						*/
						contentType: false,
						/**
						* 必须false才会避开jQuery对 formdata 的默认处理
						* XMLHttpRequest会对 formdata 进行正确的处理
						*/
						processData: false,
						//监听用于上传显示进度
						/* xhr: function() {
							var xhr = $.ajaxSettings.xhr();
							if (onprogress && xhr.upload) {
								xhr.upload.addEventListener("progress", onprogress, false);
								return xhr;
							}
						}, */
						success: function(data) {
							// 判断是否成功
							if (typeof data == "object" &&
									(data.code == "000" || data.code == "404")) {
								// 删除进度条
								$('#file-loading-list [key="' + file.fileKey + '"]').remove();
							} else {
								alert(data.msg)
							}
							
							// 回调本函数上传其他文件
							sendAjax(pathid);
						},
						error: function(data) {
							// 回调本函数上传其他文件
							sendAjax(pathid);
						}
					});
				}
				
				// 进度条暂停事件方法
				var stopLoading = function(){
					var $loading = $(this).closest(".sui-msg"),
					key = $loading.attr("key");
					
					// 从列表中删除fileKey为key的对象
					for(var j in fileList) {
						var obj = fileList[j];
						if (obj.fileKey == key) {
							fileList.splice($.inArray(obj, fileList), 1);
							break;
						}
					}
					
					// 如果点的是正在上传中的文件的进度条,还需要取消ajax
					if (file.fileKey == key) {
						myAjax.abort();
					}
				};
				
				// 对文件输入框进行监听,如果选中新文件后就将其放入文件数组中去
				$("#selectfile").change(function(){
					for (var i in this.files) {
						// 判断是否是文件
						if (this.files[i] && typeof this.files[i] == "object" && this.files[i].size > 0) {
							var fileData = {
								fileKey: Math.random().toString(),
								fileValue: this.files[i],
							}
							
							// 将文件加入到列表
							fileList.push(fileData);
							
							// 添加进度条
							var html = fileLoadinghtml.
								// 替换随机生成的key值
								replace(/%fileKey%/g, fileData.fileKey).
								// 替换文件名
								replace(/%fileName%/g, fileData.fileValue.name);
							$fileLoadingList.append(html);
						}
					}
					
					// 为进度条的取消按钮添加方法
					$('.file-loading button[data-dismiss="msgs"]').click(stopLoading);
					
					// 清空文件选择框
					$(this).val(null);
					
					// 使Modal框重新居中显示
					$uploadFile.modal('resize');
				});
				
				// 上传文件
				$("#upload-files").click(function(){
					if (fileList.length <= 0) {
						alert("目前无文件可上传!!!");
						return;
					}
					// 上传文件(上传时指定上传到哪里)
					sendAjax($address.find("button").last().attr("pathid"));
				});
				
				// 清空上传
				$("#clean-file-list").click(function(){
					fileList = [];
					$fileLoadingList.html(null);
				});
				
			})();
			
		})();
	})();

	/* File对象的常用操作方法 */
	var File = function(){
		/*【小图标】html代码
		 * 替换函数: str.replace(/需要替换的字符串/g，"新字符串")
		 * %type% : 显示类型,可选值:min(小图标)/max(大图标)/full(详细信息)
		 * %typeHtml% : 根据不同的类型加入不同的html代码
		 * %directory% : 是否为目录
		 * %fileType% : 文件类型
		 * %fileId%	: 文件Id
		 * %fileLink% : 文件链接
		 * %fileIcon% : 显示图标
		 * %pathName% : 文件名或目录名
		 * %next% : 如果是目录则有.next
		 */
		var html = 	'<div class="file %type%">'+
						'<div class="file-info">'+
							'<!-- 文件类型 -->'+
							'<input type="hidden" class="fileType" value="%fileType%" />'+
							'<!-- 是否为目录 -->'+
							'<input type="hidden" class="directory" value="%directory%" />'+
							'<!-- 文件Id(唯一的) -->'+
							'<input type="hidden" class="fileId" value="%fileId%" />'+
							'<!-- 文件链接(只有文件才有链接) -->'+
							'<input type="hidden" class="fileLink" value="%fileLink%" />'+
						'</div>'+
						'<!-- 文件图标和文件名 -->'+
						'<div class="file-val">'+
							'<div class="file-ico %next%">%typeHtml%</div>'+
							'<div class="file-title">%pathName%</div>'+
						'</div>'+
						'<!-- 操作菜单 -->'+
						'<div class="file-val file-menu">'+
							'<!-- 操作菜单列表 -->'+
							'<div class="file-val file-menu-list none">'+
								'<div id="nav">'+
									'<ul class="sui-nav nav-list">'+
										'<li><a>重命名</a></li>'+
										'<li><a>删除</a></li>'+
										'<li><a>移动</a></li>'+
										'<!-- <li><a>复制</a></li> -->'+
										'<!-- <li><a>分享</a></li> -->'+
										'<li class="copy-link" data-clipboard-text="%fileLink%"><a>复制链接</a></li>'+
									'</ul>'+
								'</div>'+
							'</div>'+
							'<span></span><span></span>'+
						'</div>'+
					'</div>';
		
		// 文件类型和图标(阿里图标库为准)类型对应关系
		var fileIcon = {
			// PPT
			"ppt":"ppt",
			// Word
			"word":"word",
			// 目录
			"folder":"icon_folder",
			// Excel
			"excel":"excel",
			// 图片
			"img":"New_img",
			// 音乐
			"music":"music",
			// TXT文本
			"txt":"TXTtubiao",
			// 视频
			"video":"shipin",
			// 压缩包
			"yasuo":"yasuo",
			// PDF
			"pdf":"pdf",
			// 其他
			"other":"wenjian",
		};
		// 文件类型详细
		var fileTypeArray = [
			// PPT
			{name: "ppt", value: ["ppt","pptx"]},
			// Word
			{name: "word", value: ["doc","docx"]},
			// Excel
			{name: "excel", value: ["xlsx","xlsm","xltx","xltm","xlsb","xlam"]},
			// PDF
			{name: "pdf", value: ["pdf"]},
			// 图片
			{name: "img", value: ["psd","pdd","gif","jpeg","jpg","png","pdf","svg"]},
			// 音乐
			{name: "music", value: ["mp3","flpc","ape","midi","wav","cda","wma"]},
			// txt文本
			{name: "txt", value: ["txt"]},
			// 视频
			{name: "video", value: ["avi","rm","rmvb","mpeg","mpg","dat","mov","qt","asf","wmv"]},
			// 压缩包
			{name: "yasuo", value: ["zip","rar","7z","cab","arj","lzh","ace","tar","gzip","uue","bz2","jar","iso","z"]},
		];
		
		/* 根据文件扩展名获取文件类型 */
		var getType = function(fileTypeName) {
			for (var index1 in fileTypeArray) {
				var fileType = fileTypeArray[index1];
				
				for (var index2 in fileType.value) {
					var typeName = fileType.value[index2];
					// 判断原文件名是否和目标文件名相等
					if (fileTypeName.toLowerCase() == typeName.toLowerCase()) {
						return fileType.name;
					}
				}
			}
			return "other";
		}
		
		/* 根据文件类型获取图标显示名class名 */
		var getFileIcon = function(fileTypeName) {
			return fileIcon[fileTypeName] ? fileIcon[fileTypeName] : fileIcon["other"];
		}
		
		/**
		 * 根据文件对象返回相应的html
		 * 返回时会自动判断显示类型
		 */
		var getFileHtml = function(file){
			// 定义一个变量接收原始html值
			var fileHt = html,
			// 显示类型,可选值:min(小图标)/max(大图标)/full(详细信息)
			showTypeValue = $("#show-type").attr("value");
			
			
			// 根据显示类型替换具体的html类容
			switch (showTypeValue){
				case "min":
					/* 小图标 */
					fileHt = fileHt.replace(/%typeHtml%/g, '<svg class="icon" aria-hidden="true"><use xlink:href="#icon-%fileIcon%"></use></svg>');
					break;
				case "max":
					/* 大图标 && 图片类型 */
					if (!file.directory && getType(file.fileType) == 'img') {
						fileHt = fileHt.replace(/%typeHtml%/g, '<img src="%fileLink%" >');
					} else {
						fileHt = fileHt.replace(/%typeHtml%/g, '<svg class="icon" aria-hidden="true"><use xlink:href="#icon-%fileIcon%"></use></svg>');
					}
					break;
				case "full":
					break;
				default:
					break;
			}
			
			// 替换显示类型
			fileHt = fileHt.replace(/%type%/g, showTypeValue);
			
			// 替换目录的文件Id
			fileHt = fileHt.replace(/%fileId%/g, file.pathId);
			// 替换目录名
			fileHt = fileHt.replace(/%pathName%/g, file.pathName);
			// 替换是否为目录
			fileHt = fileHt.replace(/%directory%/g, file.directory);
			
			// 目录
			if (file.directory) {
				// 替换图标名
				fileHt = fileHt.replace(/%fileIcon%/g, getFileIcon("folder"));
				// 目录可点击
				fileHt = fileHt.replace(/%next%/g, "next");
			}
			// 文件
			else {
				// 替换图标名
				fileHt = fileHt.replace(/%fileIcon%/g, getFileIcon(getType(file.fileType)));
				// 替换文件类型(只有文件有该值)
				fileHt = fileHt.replace(/%fileType%/g, file.fileType);
				// 替换文件链接地址(只有文件有该值)
				fileHt = fileHt.replace(/%fileLink%/g, file.fileLink);
				// 文件不可点击
				fileHt = fileHt.replace(/%next%/g, "");
			}
			return fileHt;
		}
		
		return {
			/* 根据文件类型获取图标显示名class名 */
			getFileIcon : getFileIcon,
			/* 根据文件扩展名获取文件类型 */
			getType : getType,
			/* 根据文件对象返回相应的fileHtml */
			getFileHtml : getFileHtml,
		};
	}();

});