package cn.laeni.platform.user.controller;

import cn.laeni.platform.user.service.ConnectService;
import cn.laeni.platform.user.service.RegService;
import cn.laeni.platform.user.service.UserService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
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

    /**
     * 首页URL
     */
    @Value("${system.url_home:http://laeni.cn}")
    private String urlHome;

    @Autowired
    public ConnectController(ConnectQQMapper connectQQMapper, UserMapper userMapper, ConnectService connectService, RegService regService, UserService userService) {
        this.connectQQMapper = connectQQMapper;
        this.userMapper = userMapper;
        this.connectService = connectService;
        this.regService = regService;
        this.userService = userService;
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
     * @param code     Authorization Code, 由QQ互联自动加上, 用于获取Access Token, 使用一次会失效
     * @param state      原样返回的state值
     * @param usercancel 移动端用户取消登录授权标识(否则该值为空)
     * @return null或者要渲染的模板
     */
    @RequestMapping("/callback/qq")
    public String qqLogin(HttpServletRequest request, HttpServletResponse response,
                          String code, String state, String usercancel, String redirect_uri) {
        try {
            /* 验证state是否正确以及QQ穿过来的code是否在有效期内,不满足则关闭QQ登录窗口，回到原始登录界面
             * 并提示重新登录等信息：
             *     对于弹窗登录的，调用本系统的url检测是否登录
             *     对于第三方调用者，带上错误信息重定向到调用者提供的回调地址
             */
            // 查询AccessToken/openId/续期Token/过期时间
            connectService.checkState(request, response, state,usercancel);

            // 从QQ互联查询QQ用户信息
            ConnectQQ connectQQ = connectService.getQQAccessToken(code);

            if (connectQQ == null) {
                throw new LoginQQException("AccessToken过期，跳转回原来的页面！");
            }

            // 在本地查询原来的QQ用户信息
            ConnectQQ connectQQ2 = connectQQMapper.findConnectQQByOpenIdOrUserId(connectQQ.getOpenid());
            // 判断该用户是否为新用户
            if (connectQQ2 == null) {
                // 将QQ用户信息插入到新用户的用户信息中,并返回新用户的信息
                User user = regService.regQQAccount(connectQQ);

                // QQ登录成功,将相关信息保存到Cookie中
                connectService.loginSuccess(request, response, connectQQ);
                userService.loginSuccess(request, response, user);

                // 跳转到中间页面,再从中间页面回到原页面
                logger.info("新用户登录成功:/login/bind_pwd");
                request.setAttribute("user", user);
                return "/login/bind_pwd";
            } else {
                // QQ登录成功,将相关信息保存到Cookie中
                connectService.loginSuccess(request, response, connectQQ2);
                String userId = connectQQ2.getUserId();
                User user = userMapper.findUserByUserId(userId);
                userService.loginSuccess(request, response, user);
                // 回到原页面
                throw new LoginQQException("老用户登录成功！");
            }
        }
        // 捕捉到该异常后将跳转到登陆前页面或者将登录创建关闭
        catch (LoginQQException e) {
            logger.info(e.getMessage());
            // 跳转到原来的页面或者首页(新窗口登录的将窗口关闭)
//			return backToThePast(request);

            // 登录成功后跳转到该页面,使用该页面的js关闭临时登录窗口
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
