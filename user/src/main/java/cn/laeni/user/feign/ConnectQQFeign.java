package cn.laeni.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.laeni.user.other.entity.UserQQInfo;

/**
 * QQ互联信http请求
 * @author laeni.cn
 */
@FeignClient(url = "https://graph.qq.com", name = "engine")
public interface ConnectQQFeign {

	/**
	  *  获取QQ互联AccessToken 十分钟内过期
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/oauth2.0/token?grant_type=authorization_code"
			+ "&client_id=${connect.qq.appid}"
			+ "&client_secret=${connect.qq.appkey}"
			+ "&redirect_uri=https%3A%2F%2Flaeni.cn%2Fcallback%2Fqq"
			+ "&code={code}",
			method = RequestMethod.GET)
	String getAccessToken(@PathVariable("code") String code);
	
	/**
	 *   获取openId
	 * @param accessToken
	 * @return
	 */
	@RequestMapping(value = "/oauth2.0/me", method = RequestMethod.GET)
	String getOpenId(@RequestParam("access_token") String accessToken);
	
	/**
	 *   获取QQ用户信息
	 * @param accessToken
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "/user/get_user_info?oauth_consumer_key=${connect.qq.appid}"
			+ "&openid={openid}"
			+ "&access_token={accessToken}", method = RequestMethod.GET)
	UserQQInfo getUserInfo(@PathVariable("accessToken") String accessToken, @PathVariable("openid") String openid);
	
}
