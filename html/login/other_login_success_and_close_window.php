<!DOCTYPE html>
<html lang="zh-CN" class="w">
<script type="text/javascript">
	window.close();
</script>
	<head>
		<!-- 下面3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=2">
		
		<meta name="renderer" content="webkit">
		<meta name="spm-id" content="a21bo">
		<meta name="aplus-xplug" content="NONE">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<!-- 关闭自动将连串数字识别为电话号码 -->
		<meta name="format-detection" content="telephone=no">
		<!-- 声明此网页同时适用于PC和移动端(一般使用了CSS3的媒体查询,能根据各种屏幕尺寸自适应的网站) -->
		<meta name="applicable-device" content="pc,mobile">
		<!-- <meta name="keywords" content="以逗号分隔的关键字"> -->
		<!-- <meta name="description" content="这里是页面描述"> -->
		<base target="_blank">

		<!-- Bootstrap -->
		<link href="https://chengdu-1252266447.cos.ap-chengdu.myqcloud.com/cdn.laeni.cn/public/bootstrap-4.0.0-dist/css/bootstrap.css"
		 rel="stylesheet" type="text/css" />
		<!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
		<script src="https://chengdu-1252266447.cos.ap-chengdu.myqcloud.com/cdn.laeni.cn/public/js/jquery-3.3.1.min.js" type="text/javascript"
		 charset="utf-8"></script>
		<!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
		<script src="https://chengdu-1252266447.cos.ap-chengdu.myqcloud.com/cdn.laeni.cn/public/bootstrap-4.0.0-dist/js/bootstrap.min.js"
		 type="text/javascript" charset="utf-8"></script>
		<!-- HTML5 shim 和 Respond.js 是为了让 IE8 支持 HTML5 元素和媒体查询（media queries）功能 -->
		<!-- 警告：通过 file:// 协议（就是直接将 html 页面拖拽到浏览器中）访问页面时 Respond.js 不起作用 -->
		<!--[if lt IE 9]>
			<script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
			<script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
		
		<!-- IconFont -->
		<script src="https://chengdu-1252266447.cos.ap-chengdu.myqcloud.com/cdn.laeni.cn/public/iconfont/iconfont.js"></script>
		<link rel="stylesheet" type="text/css" href="https://chengdu-1252266447.cos.ap-chengdu.myqcloud.com/cdn.laeni.cn/public/iconfont/iconfont.css" />

		<!-- 自定义JavaScript工具: utlis.js -->
		<script src="https://chengdu-1252266447.cos.ap-chengdu.myqcloud.com/cdn.laeni.cn/public/js/utlis.js" type="text/javascript"
		 charset="utf-8"></script>

		<!-- 重置一些浏览器默认样式: reset.css -->
		<link rel="stylesheet" type="text/css" href="https://chengdu-1252266447.cos.ap-chengdu.myqcloud.com/cdn.laeni.cn/public/css/reset.css" />

		<!-- 前端配置文件 -->
		<script src="//cdn.laeni.cn/public/config/laeni.cn.config.js" type="text/javascript" charset="utf-8"></script>

		<!-- 如需使用登录功能,则引入该js文件即可: user.js -->
		<script src="//cdn.laeni.cn/public/js/user.js" type="text/javascript"
		 charset="utf-8"></script>
		 
		<title>导航栏模块</title>
	</head>
	<body>
		<!-- 通知栏 -->
		<!-- <div>通知通知通知通知通知通知通知通知</div> -->

		<!-- 导航栏: TODO 测试阶段,公用部分自动引入 -->
		<script type="text/javascript" id="header_script">
			$(function() {
				$.ajax({
					url: (typeof LAENI_CONFIG != "undefined" && LAENI_CONFIG.URL.CDN + "/html/header_1.html") || "//cdn.laeni.cn/html/header_1.html",
					xhrFields: {
						thCredentials: true
					},
					success: function(re) {
						$("#header_script").before(re).remove();
					}
				});
			});
		</script>

		<div style="text-align: center;font-size: 60px;margin-top: 180px;">登录成功</div>
		<div style="text-align: center;font-size: 30px;">请关闭当前窗口</div>

		
	</body>

</html>
