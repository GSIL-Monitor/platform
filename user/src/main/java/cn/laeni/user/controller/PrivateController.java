package cn.laeni.user.controller;

import cn.laeni.user.dao.entity.User;
import cn.laeni.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模块之间使用,不对外公开
 * @author laeni.cn
 */
@RestController
public class PrivateController {

    @Autowired
    private UserService userService;

    /**
     * 供其他模块判断用户是否已经登录
     * @param sessionId
     * @return
     */
    @RequestMapping("/pri/legalize")
    public User legalize(String sessionId) {
        if (sessionId == null) {
            return null;
        }

        // 从Redis缓存中去查询User序列化后的字符串
        User user = userService.getUserBySessionId(sessionId);

        // 如果为null就说明该用户没登录或登录已经过期
        if (user == null) {
            return null;
        } else {
            return user;
        }
    }
}
