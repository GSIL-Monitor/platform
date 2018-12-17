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
     * QQ登录成功后统一处理方法
     *
     * @param request   请求头
     * @param response  响应头
     * @param connectQQ 与用户id一一对应的QQ信息 如果用户与相应QQ绑定,则有一条对应记录
     */
    void loginSuccess(HttpServletRequest request, HttpServletResponse response, ConnectQQ connectQQ);

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
