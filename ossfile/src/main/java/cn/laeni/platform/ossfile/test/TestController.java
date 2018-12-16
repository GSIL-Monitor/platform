package cn.laeni.platform.ossfile.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin // 跨域访问
@RestController
public class TestController {

    @Value("${name:默认nam}")
    String name;
    @Value("${oss.qcloud.appid}")
    String appid;

    @RequestMapping("/test")
    public String test() {
        return "我的名字是：" + name + ",appid" + appid;
    }

}
