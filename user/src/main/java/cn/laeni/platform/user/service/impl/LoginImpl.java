package cn.laeni.platform.user.service.impl;

import cn.laeni.platform.code.SystemCode;
import cn.laeni.platform.user.code.UserCode;
import cn.laeni.platform.user.entity.Account;
import cn.laeni.platform.entity.ApiJson;
import cn.laeni.platform.user.service.LoginService;
import cn.laeni.platform.user.service.UserService;
import cn.laeni.platform.user.entity.AppDomain;
import cn.laeni.platform.user.entity.Password;
import cn.laeni.platform.user.entity.User;
import cn.laeni.platform.user.mapper.AppDomainMapper;
import cn.laeni.platform.user.mapper.PasswordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author laeni.cn
 */
@Service
public class LoginImpl implements LoginService {
    protected static Logger logger = LoggerFactory.getLogger(LoginService.class);

    /**
     * appMapper
     */
    @Autowired
    private AppDomainMapper appDomainMapper;
    /**
     * passwordMapper
     */
    @Autowired
    private PasswordMapper passwordMapper;

    /**
     * 用户Service
     */
    @Autowired
    private UserService userService;


    // 方法区---------------------------------------------------------------------------------------

    /**
     * 用于本站应用的登录
     * @param request 请求
     * @param response 相应
     * @param account 登录名/邮箱/手机号
     * @param password 密码
     * @return 登录状态的相关信息
     */
    @Override
    public ApiJson loginLocal(HttpServletRequest request, HttpServletResponse response, Account account, String password) {

        // 根据帐号(登录名/邮箱/手机号)查询一个用户类(用户基本型信息)
        User userBean = userService.getUserByName(account);

        // 空号
        if (userBean == null) {
            return new ApiJson(UserCode.USER_NULL);
        } else {
            Password passwordBean = passwordMapper.findPasswordByUserId(userBean.getUserId());

            if (passwordBean == null) {
                passwordBean = passwordMapper.findPasswordByUserId(userBean.getUserId());
            }
            if (passwordBean == null || password == null || !password.equals(passwordBean.getPassword())) {
                // 密码错误
                return new ApiJson(UserCode.PASSWORD_ERROR);
            }
            // 用户被禁用
            if (passwordBean.getForbid()) {
                return new ApiJson(UserCode.USER_DISABLE);
            }
        }

        userService.loginSuccess(request, response, userBean);

        return new ApiJson(SystemCode.SUCCESS);
    }

    /**
     *  通过请求者域名判断请求这属于哪个应用
     * @param serverName 请求者域名
     * @return 请求者对应的应用ID
     */
    @Override
    public String getAppId(String serverName) {
        AppDomain appDomain = appDomainMapper.findByDomain(serverName);
        if (appDomain == null) {
            appDomain = appDomainMapper.findByDomain("*." + serverName);
        }
        if (appDomain != null) {
            return appDomain.getAppId();
        }
        return null;
    }

}
