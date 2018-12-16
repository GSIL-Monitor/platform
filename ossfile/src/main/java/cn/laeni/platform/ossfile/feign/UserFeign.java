package cn.laeni.platform.ossfile.feign;

import cn.laeni.platform.ossfile.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * User
 *
 * @author laeni.cn
 */
//@FeignClient("USER")
@FeignClient(url = "http://user.laeni.cn", name="USER")
//@FeignClient(url = "127.0.0.1:7000", name="USER")
public interface UserFeign {

    /**
     * 调用User模块判断该用户是否通过授权
     * @return
     */
    @RequestMapping(value = "/pri/legalize", method = RequestMethod.GET)
    User userLegalize(@RequestParam("sessionId") String sessionId);

}
