package cn.laeni.platform.user.service.impl;

import cn.laeni.platform.user.feign.ConnectQQFeign;
import cn.laeni.platform.user.service.ConnectService;
import cn.laeni.platform.user.utils.CookieAndSessionUtli;
import cn.laeni.platform.user.entity.ConnectQQ;
import cn.laeni.platform.user.exception.LoginQQException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ConnectImpl implements ConnectService {


    private static final Pattern PATTERN = Pattern.compile("\"openid\":\"(\\w+)\"");

    // Feign
    @Autowired
    private ConnectQQFeign connectQQFeign;

    // Config
    @Value("${connect.qq.appid}")
    private String appid;

    /*
     * 从QQ查询openid 、 accessToken(其包含了续期的Token以及当前Token过期时间) 返回的ConnectQQ中不包括userId
     */
    @Override
    public ConnectQQ getQQAccessToken(String code) {
        /*获取Access Token等信息,并封装到JavaBean中*/
        ConnectQQ connectQQ = new ConnectQQ();
        String client = connectQQFeign.getAccessToken(code);

        // 如果有错误信息则直接返回null
        if (Pattern.matches(".*\"error\":\\d{6}.*", client)) {
            return null;
        }
        for (String splits : client.split("&")) {

            String[] split = splits.split("=");
            switch (split[0]) {
                case "access_token":
                    connectQQ.setAccessToken(split[1]);
                    break;
                case "expires_in":
                    connectQQ.addExpires(Long.parseLong(split[1]));
                    break;
                case "refresh_token":
                    connectQQ.setRefreshToken(split[1]);
                    break;
            }
        }

        /* 获取openid */
        String clientOpenid = connectQQFeign.getOpenId(connectQQ.getAccessToken());

        // 使用正则表达式提取openid的值
        Matcher matcher = PATTERN.matcher(clientOpenid);
        if (matcher.find()) {
            connectQQ.setOpenid(matcher.group(1));
            return connectQQ;
        }

        return null;
    }

    /**
     * 验证state是否正确
     * @param request    请求头
     * @param response   响应头
     * @param state      原样返回的state值
     * @param usercancel 移动端用户取消登录授权标识(否则该值为空)
     */
    @Override
    public void checkState(HttpServletRequest request, HttpServletResponse response, String state, String usercancel) {
        CookieAndSessionUtli cookieUtli = new CookieAndSessionUtli(request, response);
        // Cookie中存储的值
        String stateCookie = cookieUtli.getCookieValue("state");

        if (stateCookie == null || !stateCookie.equals(state) || usercancel != null) {
            throw new LoginQQException("state验证不通过或移动端用户取消登录授权，跳转回原来的页面！");
        }
    }

}
