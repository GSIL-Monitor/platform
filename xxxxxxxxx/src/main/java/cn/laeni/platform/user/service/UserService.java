package cn.laeni.platform.goods.service;

import cn.laeni.platform.goods.other.entity.Account;
import cn.laeni.platform.goods.other.entity.ApiJson;
import cn.laeni.platform.goods.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService {

    /**
     * 根据帐号(登录名/邮箱/手机号/用户ID)查询一个用户类(用户基本型信息)
     *
     * @param account 登录名/邮箱/手机号/用户ID
     * @return
     */
    User getUserByName(Account account);

    /**
     * <h1>查询指定类型的登录名是否存在</h1>
     *
     * @param account 帐号
     * @param type    帐号类型[loginName/loginPhone/loginEmail]
     * @return code=104表示不存在, code=105表示存在, 为其他是表示异常错误
     */
    ApiJson checkLoginAccount(String account, String type);

    /**
     * 查询登录名是否存在
     *
     * @param loginName
     * @return
     */
    ApiJson loginNameIsExist(String loginName);

    /**
     * 查询登录手机号是否存在
     *
     * @param loginPhone 待查询的手机号
     * @return 状态码和说明
     */
    ApiJson loginPhoneIsExist(String loginPhone);

    /**
     * 查询登录邮箱是否存在
     *
     * @param loginEmail 待查询的登录邮箱
     * @return 状态码和说明
     */
    ApiJson loginEmailIsExist(String loginEmail);

    /**
     * 保存用户登录后的Session信息
     *
     * @param session session对象
     * @param key     保存时的键
     * @param value   键对应的值
     * @return
     */
    void saveSession(HttpSession session, String key, Object value);

    /**
     * 删除用户的所有Session
     *
     * @param request
     * @return
     */
    void deleteSessAll(HttpServletRequest request);

    /**
     * 删除用户的部分Session(键为key对应的内容)
     *
     * @param request
     * @param key
     * @return
     */
    boolean deleteSessVal(HttpServletRequest request, String key);


    /**
     * <h1>保存或更新用户信息(用户已经登录的情况下)</h1>
     * <e>需要判断传入的信息是否合法(包括手机和邮箱需要对应验证码正确后才能更改)</e>
     * <p>
     * nickname:用户昵称<br/>
     * avatar:头像地址<br/>
     * grade:用户等级<br/>
     * password:用户密码<br/>
     * loginName:登录名<br/>
     * loginPhone:登录手机号<br/>
     * loginPhoneCode:登录手机号对应的验证码(只有验证码正确才更新)<br/>
     * loginEmail:登录邮箱<br/>
     * loginEmailCode:登录邮箱对应的验证码(只有验证码正确才更新)<br/>
     * </p>
     *
     * @param request 请求对象
     * @return 包含状态码的详细信息
     */
    ApiJson saveOrUpdateUserInfor(HttpServletRequest request, HttpServletResponse response);

    /**
     * <h1>保存或更新用户信息(用户已经登录的情况下)</h1>
     * <e>不做判断,只要传入数据数更新对应的信息</e>
     * <p>
     * nickname:用户昵称<br/>
     * avatar:头像地址<br/>
     * grade:用户等级<br/>
     * password:用户密码<br/>
     * loginName:登录名<br/>
     * loginPhone:登录手机号<br/>
     * loginEmail:登录邮箱<br/>
     * </p>
     *
     * @param user user对象,指明要修改的用户
     * @param map  需要修改的参数列表
     */
    void saveOrUpdateUserInfor(User user, Map<String, String> map);

    /**
     * 登录成功统一处理函数
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param user     用户信息
     */
    void loginSuccess(HttpServletRequest request, HttpServletResponse response, User user);

    /**
     * 注销用户登录
     *
     * @param request  请求对象
     */
    void logOut(HttpServletRequest request);

    /**
     * 通过SessionId获取已经登录的用户信息,返回User对象
     * @param sessionId sessionId
     * @return User对象
     */
    User getUserBySessionId(String sessionId);

}
