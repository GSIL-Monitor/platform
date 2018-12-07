# 使用说明

## 位置
> cdn.laeni.cn > html > login.html

## 登录成功
 * 成功后在本地Cookie中存储用户的userId,nickname,grade

## API(USER)
### islogin() -- 检测是否登录
	在调用云端需要授权的功能时都应该先调用该函数确认
	如果Cookie中有用户user_id和__ok则表示已经登录
	__ok表示是否通过云端验证,如果通过则可以请求云端操作(云端会再次验证,如果非法则自动清空该值)
	
## QQ登录使用
	<!-- 加入QQ登录图标 -->
	<svg class="icon qqLoginBtn" aria-hidden="true">
		<use xlink:href="#icon-social-qq"></use>
	</svg>
	<!-- 为QQ图标增加点击事件,并增加相关功能 qc-btn-class为点击该class元素则自动跳起QQ登录 -->
	<script src="http://cdn.laeni.cn/public/js/lc_qq_sdk.js" type="text/javascript" charset="utf-8" qc-appid="101481333" qc-redirecturi="https://laeni.cn/callback/qq"
		qc-btn-class="qqLoginBtn"></script>
	