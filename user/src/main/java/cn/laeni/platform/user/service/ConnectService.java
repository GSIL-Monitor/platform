package cn.laeni.platform.user.service;

import cn.laeni.platform.user.entity.ConnectQQ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ConnectService {

    /**
     * 从QQ查询openid 、 accessToken(其包含了续期的Token以及当前Token过期时间)
     * 返回的ConnectQQ中不包括userId
     *
     * @param code Authorization Code
     * @return 用户id一一对应的信息
     */
    ConnectQQ getQQAccessToken(String code);

    /**
     * 验证state是否正确
     *
     * @param request    请求头
     * @param response   响应头
     * @param state      原样返回的state值
     * @param usercancel 移动端用户取消登录授权标识(否则该值为空)
     */
    void checkState(HttpServletRequest request, HttpServletResponse response, String state, String usercancel);
}
