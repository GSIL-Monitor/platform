/**执行/链入js或添加css(不重复)
 * script.runJs(value)
 * script.runCss(value)
 */
var script = {
	jsArray : new Array(),
	cssArray : new Array(),
	/**
	 * 【JS示例】
	 * $(document).ready(function(){
	 *	示例一:
	 * 	script.runJs([{"type":"path","value":"alert(123)"},...]);	#以数组形式传入时,当数组中对象的type为"path"时作为外链链入,否则直接执行
	 * 	示例二:
	 * 	script.runJs("alert(123)");		#以字符串形式传入时将直接执行
	 * })
	 */
	runJs: function(jss) {var jsArray = this.jsArray;/* js脚本分为链入式和内嵌式,链入式只能引入一次,否则执行(完全相同的脚本只执行一次) */if(typeof jss == 'string'){		/* 字符串 */runJsValue(jss);}else if(jss instanceof Array){	/* 数组 */for (index in jss){if(jss[index].type == "path"){addJsPath(jss[index].value);/* 链入 */}else{runJsValue(jss[index].value);/* 执行 */}}}this.jsArray = jsArray;/* 链入js脚本 */function addJsPath(value){if(value == undefined || value == null || value == ""){return;}var i=true;/* 查询HTML文档中是否有该js脚本 */$("head script").each(function(){if(this.getAttribute("src") == value){i = false;}});if(i){/* 引入相关的js文件 */$("head").append('<script src="'+value+'" type="text/javascript" charset="utf-8"></script>');}}/* 执行js脚本 */function runJsValue(value){if(value == undefined || value == null || value == ""){return;}var i=true;for (index in jsArray){		/* 遍历数组,查询是否已经有该js文件 */if(jsArray[index] == value){i = false;break;}}if(i){	/* jsArray中没有该js脚本则执行 */jsArray.push(value);eval(value);}}},
	/**
	 * 【CSS示例】
	 * $(document).ready(function(){
	 *	示例一:
	 * 	script.runCss([{"type":"path","value":'* {background-color: #000000;}'},...]);
	 * 	示例二:
	 * 	script.runCss('* {background-color: #000000;}');
	 * })
	 */
	runCss: function(Csss){var cssArray = this.cssArray;/* 同js一样,css脚本也分为链入式和内嵌式 链入式和内嵌式只能引入一次 */if(Csss == undefined || Csss == null || Csss == ""){return;}if(typeof Csss == 'string'){		/* 字符串 *//* 嵌入css */runCssValue(Csss);}else if(Csss instanceof Array){	/* 数组 */for (index in Csss){if(Csss[index].type == "path"){addCssPath(Csss[index].value);	/* 链入 */}else{runCssValue(Csss[index].value);	/* 嵌入 */}}}this.cssArray = cssArray;/* 链入css脚本 */function addCssPath(value){if(value == undefined || value == null || value == ""){return;}var i=true;/* 查询HTML文档中是否有该js脚本 */$("head link").each(function(){if(this.getAttribute("href") == value){i = false;}});if(i){/* 引入相关的css文件 */$("head").append('<link rel="stylesheet" type="text/css" href="'+value+'"/>');}}/* 嵌入css脚本 */function runCssValue(value){if(value == undefined || value == null || value == ""){return;};var i=true;for (index in cssArray){/* 遍历数组,查询是否已经有该css文件 */if(cssArray[index] == value){i = false;break;};};if(i){	/* cssArray中没有该css脚本则嵌入 *//* 嵌入css */$("head").append('<style type="text/css">'+value+'</style>');cssArray.push(value);};}},
}

/**仅仅只负责为用户授权
 * 1. 当有登录验证需求时调用该登录方法
 * 2. 验证通过后将用户的验证信息(以随机ID标识的信息)告诉第三方,并将该随机id返回给客户端
 * 3. 客户端通过该随机id向第三方服务器注册自己
 */

