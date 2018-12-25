package cn.laeni.platform.user.controller;

import cn.laeni.platform.user.entity.User;
import cn.laeni.platform.user.service.VerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <h2>OAuth2.0开放授权协议</h2>
 * <p>
 *
 * </p>
 * @author Laeni
 */
@Controller
public class OAuthController {

    @Autowired
    VerifyCodeService verifyCodeService;


    @RequestMapping("/oa")
    @ResponseBody
    public String oa(){
        User user = new User();
        user.setUserId("444444444");
        return verifyCodeService.getCode(user);
    }
}
