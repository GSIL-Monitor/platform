package cn.laeni.platform.user.service;

import cn.laeni.platform.user.entity.Account;
import cn.laeni.platform.entity.ApiJson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {

	/**
	 * 用于本站应用的登录
	 *
	 * @param request 请求
	 * @param response 相应
	 * @param account 登录名/邮箱/手机号
	 * @param password 密码
	 * @return 执行结果
	 */
	ApiJson loginLocal(HttpServletRequest request, HttpServletResponse response, Account account, String password);

}
