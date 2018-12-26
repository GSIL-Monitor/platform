package cn.laeni.platform.user.controller;

import cn.laeni.platform.user.entity.Application;
import cn.laeni.platform.user.service.*;
import cn.laeni.platform.user.entity.ConnectQQ;
import cn.laeni.platform.user.entity.User;
import cn.laeni.platform.user.mapper.ConnectQQMapper;
import cn.laeni.platform.user.mapper.UserMapper;
import cn.laeni.platform.user.exception.LoginQQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * QQ互联...
 *
 * @author laeni.cn
 */
@Controller
@CrossOrigin
public class ConnectController {
    private static Logger logger = LoggerFactory.getLogger(ConnectController.class);

    /**
     * QQ登录信息表
     */
    private final ConnectQQMapper connectQQMapper;
    /**
     * 用户信息表
     */
    private final UserMapper userMapper;

    private final ConnectService connectService;
    private final RegService regService;
    private final UserService userService;
    private final ApplicationService applicationService;
    private final VerifyCodeService verifyCodeService;

    /**
     * 首页URL
     */
    @Value("${system.url_home:http://laeni.cn}")
    private String urlHome;

    @Autowired
    public ConnectController(ConnectQQMapper connectQQMapper, UserMapper userMapper, ConnectService connectService,
                             RegService regService, UserService userService,ApplicationService applicationService,
                             VerifyCodeService verifyCodeService) {
        this.connectQQMapper = connectQQMapper;
        this.userMapper = userMapper;
        this.connectService = connectService;
        this.regService = regService;
        this.userService = userService;
        this.applicationService = applicationService;
        this.verifyCodeService = verifyCodeService;
    }

    /**
     * QQ互联回调地址
     * 1.如果QQ的"code"过期则重定向到QQ登录页
     * 2.0.认证成功后判断用户是否是新用户
     *   2.1.如果是新用户则登录成功后提示是否完善用户信息
     *   2.2.如果是老用户则直接跳转到原始页面
     * 3.0.最终登录成功后,判断是否有回调地址
     *   3.1.如果有回调地址则带上code(授权用,有效期1分钟)重定向到回调地址
     *   3.2.如果没有回调地址则关闭当前窗口(渲染"/login/login_success_and_close_window"页面,由该页面的js关闭窗口)
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param model     Authorization Code, 由QQ互联自动加上, 用于获取Access Token, 使用一次会失效
     * @param code     Authorization Code, 由QQ互联自动加上, 用于获取Access Token, 使用一次会失效
     * @param state      原样返回的state值
     * @param usercancel 移动端用户取消登录授权标识(否则该值为空)
     * @param redirectUri 调用方的源地址(登录成功后需要跳回去的地址),非回调地址(回调地址需要自己在数据库中去查询)
     * @param appId 调用方的应用ID
     * @param transfer 间接通过QQ回调传递过来的参数
     * @return 渲染模板
     */
    @RequestMapping("/callback/qq")
    public String qqLogin(HttpServletRequest request, HttpServletResponse response, Model model,
                          String code, String state, String usercancel, String redirectUri, String appId,
                          String transfer) {
        System.out.println("transfer:" + transfer);
        // 转发地址
        String forward;
        // 用户
        User user;
        // 调用者(本系统无调用者)
        Application application;

        try {
            /* 验证state是否正确以及QQ穿过来的code是否在有效期内,不满足则关闭QQ登录窗口，回到原始登录界面
             * 并提示重新登录等信息：
             *     对于弹窗登录的，调用本系统的url检测是否登录
             *     对于第三方调用者，带上错误信息重定向到调用者提供的回调地址
             */
            // 查询AccessToken/openId/续期Token/过期时间
            connectService.checkState(request, response, state,usercancel);

            // 从QQ互联查询QQ用户信息
            ConnectQQ newConnectQQ = connectService.getQQAccessToken(code);
            if (newConnectQQ == null) {
                throw new LoginQQException("AccessToken过期，跳转回原来的页面！");
            }

            // 在本地查询原来的QQ用户信息
            ConnectQQ connectQQ = connectQQMapper.findConnectQQByOpenIdOrUserId(newConnectQQ.getOpenid());

            // 判断该用户是否为新用户
            if (connectQQ == null) {
                connectQQ = newConnectQQ;

                // 将QQ用户信息插入到新用户的用户信息中,并返回新用户的信息
                user = regService.regQQAccount(connectQQ);

                // 跳转到中间页面,再从中间页面回到原页面
                logger.info("新用户登录成功:login/bind_pwd");
                model.addAttribute("user", user);
                forward = "login/bind_pwd";
            } else {
                user = userMapper.selectByPrimaryKey(connectQQ.getUserId());

                // 回到原页面
                // 使用该页面的js关闭临时登录窗口
                forward = "login/login_success_and_close_window";
            }

            // 判断是否为本系统用户登录(如果是则将信息存入Cookie,否则生成身份认证code重定向到调用者的回调地址)
            if (redirectUri != null && appId != null) {
                // 获取一个短时间的一次性令牌

                // 拼接指定地址
                String redirect = "redirect:" +
                        // 查询调用者设置的回调地址
                        applicationService.getApplicationByAppId(appId).getRedirectUri() +
                        "?redirect_uri=" + redirectUri +
                        "&app_id=" + appId +
                        "&code=" + verifyCodeService.getCode(user);

                // 将该信息发送到"新用户信息完善界面",只有完善相关信息后才访问回调接口
                model.addAttribute("redirect", redirect);
                logger.info("回调地址:{}",redirect);
            }
            // 本系统用户登录成功
            else {
                // QQ登录成功,将相关信息保存到Cookie中
                userService.loginSuccess(request, response, user);
            }
            return forward;
        }

        /*
         捕捉到该异常说明参数值不合法或者相关值已经过期等
         对于弹窗登录的，先关闭本页面（此页面是QQ登录页面新开的），调用本系统的url检测是否登录
         对于第三方调用者，带上错误信息重定向到调用者提供的回调地址
         */
        catch (LoginQQException e) {
            logger.info(e.getMessage());

            // 如果下列两个参数不为空表示是第三方调用者所调用的,需要重定向到调用方提供的回调地址去
            if (redirectUri != null && appId != null) {
                application = applicationService.getApplicationByAppId(appId);
                if (application == null) {
                    logger.error("检测到redirectUri和appId,应用推断应属于第三方应用,但是数据库中无法查询到相关信息!");
                    return null;
                }

                // 重定向到指定地址
                return "redirect:" +
                        application.getRedirectUri() +
                        "?redirect_uri=" + redirectUri +
                        "&app_id=" + appId +
                        "&err_smg=未知错误,请重试!";
            }
            // 使用该页面的js关闭临时登录窗口
            return "/login/login_success_and_close_window";
        }
        // 其他错误则跳转到错误页面
        catch (Exception e) {
            e.printStackTrace();
            // 跳转到错误页面
            return "redirect:error";
        }
    }
}
