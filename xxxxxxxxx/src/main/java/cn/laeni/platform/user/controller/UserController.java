package cn.laeni.platform.user.controller;

import cn.laeni.platform.code.SystemCode;
import cn.laeni.platform.user.other.entity.ApiJson;
import cn.laeni.platform.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author laeni.cn
 */
@Controller
@CrossOrigin
public class UserController {
    protected static Logger logger = LoggerFactory.getLogger(UserController.class);
    /**
     * 用户Service
     */
    @Autowired
    private UserService userService;

    /**
     * <h1>查询指定类型的登录名是否存在</h1>
     * <p>(该方法以及弃用,请使用checkLoginAccount()方法)</p>
     * <p>主要用于注册或者更改用户名是提示用户</p>
     *
     * @param request 请求对象
     * @return 包含状态码的详细信息
     */
    @RequestMapping("/api/loginNameIsExist")
    @ResponseBody
    @Deprecated        // 弃用该方法
    public ApiJson loginNameIsExist(HttpServletRequest request, HttpServletResponse response) {
        return checkLoginAccount(request, response, request.getParameter("loginName"), "loginName");
    }

    /**
     * <h1>检查登录名(loginName)、手机号(phone)、登录邮箱(email)是否存在</h1>
     * <p>URL?account=****&type=loginName</p>
     *
     * @param request  请求头
     * @param response 响应头
     * @param account  帐号
     * @param type     帐号类型[loginName/loginPhone/loginEmail]
     * @return code=104表示不存在, code=105表示存在, 为其他是表示异常错误
     */
    @RequestMapping("/api/checkLoginAccount")
    @ResponseBody
    public ApiJson checkLoginAccount(HttpServletRequest request, HttpServletResponse response, String account, String type) {
        // 检查请求是否合法
        if (account == null || account.trim().length() < 5 || account.trim().length() > 64 || type == null) {
            return new ApiJson(SystemCode.ILLEGAL);
        }

        return userService.checkLoginAccount(account, type);
    }

    /**
     * <h1>保存或更新用户信息(用户已经登录的情况下)</h1>
     * <p>
     * nickname:用户昵称<br/>
     * avatar:头像地址<br/>
     * grade:用户等级<br/>
     * password:用户密码<br/>
     * loginName:登录名<br/>
     * loginPhone:登录手机号<br/>
     * loginPhoneCode:登录手机号对应的验证码(只有验证码正确才更新)<br/>
     * loginEmail:登录邮箱<br/>
     * loginEmailCode:登录邮箱对应的验证码(只有验证码正确才更新)<br/>
     * </p>
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return 包含状态码的详细信息
     */
    @RequestMapping("/api/saveOrUpdateUserInfor")
    @ResponseBody
    public ApiJson saveOrUpdateUserInfor(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 更新用户信息
            return userService.saveOrUpdateUserInfor(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiJson(SystemCode.OTHER);
        }
    }

    /**
     * 使用户注销登录
     *
     * @param request 请求对象,用于获取Session对象
     * @return 包含返回码的相信信息
     */
    @RequestMapping("/api/logout")
    @ResponseBody
    public ApiJson logout(HttpServletRequest request) {
        userService.logOut(request);
        return new ApiJson(SystemCode.SUCCESS);
    }

}
