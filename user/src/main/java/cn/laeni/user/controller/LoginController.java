package cn.laeni.user.controller;

import cn.laeni.user.other.code.SystemCode;
import cn.laeni.user.other.entity.Account;
import cn.laeni.user.other.entity.ApiJson;
import cn.laeni.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 1.本应用直接登录成功 2.其他应用强制使用本应用的通行证认证,只是认证成功后返回相应的认证信息
 *
 * @author laeni.cn
 */
@Controller
@CrossOrigin // 跨域
public class LoginController {

    /**
     * 登录Service
     */
    @Autowired
    LoginService loginService;

    /**
     * 直接渲染登录页面 TODO
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public String login() {
        return "直接展示登录界面";
    }

    /**
     * 返回"非法请求"字样提示
     *
     * @return
     */
    @RequestMapping("/api")
    @ResponseBody
    public String api() {
        return "非法请求";
    }

    /**
     * 【登录验证】通过JS提交用户名密码验证用户信息 如果是本站则直接设置session表示登录成功
     *  如果是其他应用，则将用户信息发送给第三方回调接口，并返回相关认证信息
     * @param request 请求
     * @param response 相应
     * @param account 登录名/邮箱/手机号
     * @param password 密码
     * @param appid 应用id(第三方应用需要带上自己的应用id)
     * @return 处理状态
     */
    @RequestMapping("/api/login")
    @ResponseBody
    public ApiJson loginWeb(HttpServletRequest request, HttpServletResponse response,
                            String account, String password, String appid) {
        try {
            // 本站应用
            if (appid == null) {
                return loginService.loginLocal(request, response, new Account(account), password);
            } else {
                // TODO 其他应用登录暂未实现
                return new ApiJson(SystemCode.ILLEGAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 系统错误
            return new ApiJson(SystemCode.OTHER);
        }
    }

}
