package cn.laeni.platform.goods.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页 这里只是做一个提示
 * @author Laeni
 */
@RestController
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "系统完全采用前后端分离实现，请勿直接访问！！！";
    }
}
