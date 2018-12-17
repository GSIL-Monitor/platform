package cn.laeni.platform.goods.service;

import cn.laeni.platform.goods.other.entity.ApiJson;
import cn.laeni.platform.goods.entity.ConnectQQ;
import cn.laeni.platform.goods.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RegService {
	/**
	 * 根据提交的"手机号或邮箱号"、"验证码"注册帐号<br />
	 * 如果是强制注册则不检查提交的帐号是否已经注册<br />
	 * 已经注册:<br />
	 * 		返回已经注册的信息<br />
	 * 未注册:<br />
	 * 		1.在帐号库中取出一个"用户id"加入到用户库中<br />
	 * 		2.将用户用于注册的手机或邮箱关联到该"用户id"<br />
	 * 		3.返回"未注册"的相关信息<br />
	 * @return
	 */
	ApiJson regAccount(HttpServletRequest request, HttpServletResponse response,
                       String account, String verifyCode, String force);
	
	/**
	 *  如果是第一次使用QQ登陆则调用该方法为新用户添加userId与之关联
	 * @param connectQQ 从QQ服务器查询出来的用户信息
	 * @return	用户信息
	 */
	User regQQAccount(ConnectQQ connectQQ);
}
