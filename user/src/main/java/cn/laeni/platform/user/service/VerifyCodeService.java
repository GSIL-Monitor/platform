package cn.laeni.platform.user.service;

import cn.laeni.platform.user.other.VerifyType;
import cn.laeni.platform.user.other.entity.ApiJson;
import com.aliyuncs.exceptions.ClientException;

import javax.servlet.http.HttpServletRequest;

/**
 * 发送验证码以及核对验证码
 * @author laeni.cn
 *
 */
public interface VerifyCodeService {

	/**
	 * 发送验证码(手机或邮箱验证码)
	 * @param request 请求头
	 * @param account 手机号或邮箱号
	 * @param verifyType 验证码类型(注册,登录...)
	 * @return ApiJson
	 */
	public ApiJson sendVerifCode(HttpServletRequest request, String account, VerifyType verifyType);

	/**
	 * 发送短信验证码
	 * @param account 手机号
	 * @param randomString 6为数字验证码
	 * @return ApiJson
	 * @throws ClientException
	 */
	public ApiJson sendSmsVerifCode(String account, String randomString) throws ClientException;

	/**
	 * 发送邮件验证码
	 * @param account 邮箱号
	 * @param randomString 6为数字验证码
	 * @return ApiJson
	 * @throws ClientException
	 */
	public ApiJson sendEmailVerifCode(String account, String randomString) throws Exception;

	/**
	 * 核对验证码<br/>
	 * 连续输错 maxFailures 后失效
	 * @param request
	 * @return
	 */
	public ApiJson chekVerifyCode(HttpServletRequest request, VerifyType verifyType, String account, String verifyCode);
	
	/**
	 * 删除指定类型的所有验证码,此操作一般在登录成功或尝试验证次数超限时使用
	 * @param request
	 * @param verifyType
	 * @return
	 */
	public boolean deleteVerifCode(HttpServletRequest request, VerifyType verifyType);
}
