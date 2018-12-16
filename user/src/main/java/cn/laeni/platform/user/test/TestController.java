package cn.laeni.platform.user.test;

import cn.laeni.platform.user.mapper.LoginPhoneMapper;
import cn.laeni.platform.user.feign.ConnectQQFeign;
import cn.laeni.platform.user.other.entity.UserQQInfo;
import cn.laeni.platform.user.service.DubboServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin // 跨域访问
@Controller
public class TestController {

    @Autowired
    ConnectQQFeign connectQQFeign;

    /**
     * 登录手机号表
     */
    @Autowired
    private LoginPhoneMapper loginPhoneMapper;

    @Autowired
    private ServletContext application;

    @RequestMapping("/test")
    @ResponseBody
    public UserQQInfo upSql() {
        return connectQQFeign.getUserInfo("BCA11AA75189754D20C0086F19326481", "2162948A25C174A25C71325BF8228DF3");
    }

    @RequestMapping("/showCookies")
    @ResponseBody
    public String cookie(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            System.out.println(cookie.getName() + ":" + cookie.getValue());
        }
        return "成功打印到控制台";
    }

    /**
     * 获取SessionId
     * @param request
     * @return
     */
    @RequestMapping("/getSessionId")
    @ResponseBody
    public String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

    /**
     * 设置SessionId值,仅供测试
     * @param request
     * @return
     */
    @RequestMapping("/setSession")
    @ResponseBody
    public String setSession(HttpServletRequest request,String key, String value) {
        if (key == null || value == null) {
            return "请使用'?key=xxx&value=xxx'方式设置Session值";
        }
        request.getSession().setAttribute(key, value);
        return "【设置成功】=> " + key + ":" + value;
    }

    /**
     * 获取SessionId值,仅供测试
     * @param request
     * @return
     */
    @RequestMapping("/getSession")
    @ResponseBody
    public String getSession(HttpServletRequest request,String key) {
        if (key == null) {
            return "请使用'?key=xxx'方式设置需要或者的Sessionkey";
        }
        return "【获取成功】=> " + key + ":" + request.getSession().getAttribute(key);
    }

    // 测试Dubbo

    @com.alibaba.dubbo.config.annotation.Reference
    DubboServiceInterface serviceDemo;

    @RequestMapping("/testdubbo")
    @ResponseBody
    public String testdubbo() {
        return serviceDemo.getUser();
    }

    @RequestMapping("dubboConfig")
    @ResponseBody
    public String dubboConfig(@Value("${dubbo.registry.address}") String address) {
        return address;
    }

}
