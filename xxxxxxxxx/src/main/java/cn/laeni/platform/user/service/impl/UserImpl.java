package cn.laeni.platform.user.service.impl;

import cn.laeni.platform.user.entity.*;
import cn.laeni.platform.user.mapper.*;
import cn.laeni.platform.user.other.AccountTypeEnum;
import cn.laeni.platform.user.other.CookieKeyEnum;
import cn.laeni.platform.user.other.RedisPathEnum;
import cn.laeni.platform.code.SystemCode;
import cn.laeni.platform.code.UserCode;
import cn.laeni.platform.user.other.entity.Account;
import cn.laeni.platform.user.other.entity.ApiJson;
import cn.laeni.platform.user.service.LoginService;
import cn.laeni.platform.user.service.UserService;
import cn.laeni.platform.user.utils.AccountTool;
import cn.laeni.platform.user.utils.CookieAndSessionUtli;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserImpl implements UserService {
    protected static Logger logger = LoggerFactory.getLogger(UserImpl.class);

    /**
     * 用户登录后过期时间
     */
    @Value("${user.inactive:2592000}")
    private int inactive;

    /**
     * Redis
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    // Mapper
    /**
     * 登录名表
     */
    @Autowired
    private LoginNameMapper loginNameMapper;
    /**
     * 登录手机号表
     */
    @Autowired
    private LoginPhoneMapper loginPhoneMapper;
    /**
     * 登录邮箱号表
     */
    @Autowired
    private LoginEmailMapper loginEmailMapper;
    /**
     * 用户表
     */
    @Autowired
    private UserMapper userMapper;
    /**
     * 用户密码表
     */
    @Autowired
    private PasswordMapper passwordMapper;

    // Utils
    /**
     * 用于验证码传入的参数
     */
    @Autowired
    public AccountTool accountTool;

    // Service
    /**
     * 登录Service
     */
    @Autowired
    LoginService loginService;
    /**
     * 查询用户登录名及密码信息
     */
    @Autowired
    private LoginNameMapper deLoginNameMapper;
    /**
     * 查询用户登录邮箱及密码信息
     */
    @Autowired
    private LoginEmailMapper deLoginEmailMapper;
    /**
     * 查询用户登录邮箱及密码信息
     */
    @Autowired
    private LoginPhoneMapper deLoginPhoneMapper;

    // 方法区------------------------------------------------------------------------------------------

    /**
     * 根据帐号(登录名/邮箱/手机号/用户ID)查询一个用户对象(用户基本型信息)
     *
     * @param account 登录名/邮箱/手机号/用户ID
     * @return User对象
     */
    @Override
    public User getUserByName(Account account) {
        if (account == null) {
            return null;
        }

        try {
            if (account.getTypeEnum() == AccountTypeEnum.EMAIL) {
                // 邮箱认证
                return deLoginEmailMapper.findLoginEmailByLoginEmail(account.getValue()).getUser();
            }
            if (account.getTypeEnum() == AccountTypeEnum.PHONE) {
                // 手机号认证
                return deLoginPhoneMapper.findLoginPhoneByLoginPhone(account.getValue()).getUser();
            }
            // 登录名认证
            return deLoginNameMapper.findLoginNameByLoginName(account.getValue()).getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询指定类型的登录名是否存在
     * @param account 帐号
     * @param type 帐号类型[loginName/loginPhone/loginEmail]
     * @return
     */
    @Override
    public ApiJson checkLoginAccount(String account, String type) {

        switch (type) {
            case "loginName":
                return loginNameIsExist(account);

            case "loginPhone":
                return loginPhoneIsExist(account);

            case "loginEmail":
                return loginEmailIsExist(account);

            default:
                return new ApiJson(SystemCode.ILLEGAL);
        }
    }

    /**
     * 查询登录名是否存在
     *
     * @param loginName
     * @return
     */
    @Override
    public ApiJson loginNameIsExist(String loginName) {
        LoginName loginNameBean = loginNameMapper.findLoginNameByLoginName(loginName);
        return loginNameBean == null ? new ApiJson(UserCode.USER_NULL) : new ApiJson(UserCode.USER_EXIST);
    }

    /**
     * 查询登录手机号是否存在
     *
     * @param loginPhone 待查询的手机号
     * @return
     */
    @Override
    public ApiJson loginPhoneIsExist(String loginPhone) {
        LoginPhone loginPhoneBean = loginPhoneMapper.findLoginPhoneByLoginPhone(loginPhone);
        return loginPhoneBean == null ? new ApiJson(UserCode.USER_NULL) : new ApiJson(UserCode.USER_EXIST);
    }

    /**
     * 查询登录邮箱是否存在
     *
     * @param loginEmail 待查询的登录邮箱
     * @return
     */
    @Override
    public ApiJson loginEmailIsExist(String loginEmail) {
        LoginEmail loginEmailBean = loginEmailMapper.findLoginEmailByLoginEmail(loginEmail);
        return loginEmailBean == null ? new ApiJson(UserCode.USER_NULL) : new ApiJson(UserCode.USER_EXIST);
    }


    /**
     * 保存用户登录后的Session信息
     *
     * @param session session对象
     * @param key     保存时的键
     * @param value   键对应的值
     */
    @Override
    public void saveSession(HttpSession session, String key, Object value) {
        // 设置session过期时间
        session.setMaxInactiveInterval(inactive);

        // 将需要存入Session的对象存入Session
        session.setAttribute(key, value);
    }

    /**
     * 删除用户的所有Session
     *
     * @param request
     */
    @Override
    public void deleteSessAll(HttpServletRequest request) {
        // 过期Session
        request.getSession().invalidate();
    }

    /**
     * 删除用户的部分Session(键为key对应的内容)
     *
     * @param request
     * @param key
     * @return
     */
    @Override
    public boolean deleteSessVal(HttpServletRequest request, String key) {
        try {
            request.getSession().removeAttribute(key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 保存或更新用户信息,需要对资料进行合法性验证(用户已经登录的情况下)
     *
     * @param request  请求对象
     * @param response
     * @return
     */
    @Override
    public ApiJson saveOrUpdateUserInfor(HttpServletRequest request, HttpServletResponse response) {
        //  获取当前正在操作的用户
//        User user = (User) request.getSession().getAttribute(SessionKeyEnum.USER.getKey());
        // TODO 更改授权方式时需要同时更改此处
        User user = getUserBySessionId(request.getSession().getId());

        // 如果从session中获取不到User信息则表示用户的登录已经过期
        if (user == null) {
            return new ApiJson(UserCode.LOGIN_INVALID);
        }

        // 获取传入的全部参数
        Map<String, String> infors = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            // 不能为空
            if ("".equals(name) || "".equals(value)) {
                continue;
            }
            infors.put(name, value);
        }

        // 判断传入的参数是否合法
        ApiJson apiJson = accountTool.checksInfors(request, infors, true);
        if (apiJson.getEnumCode() != SystemCode.SUCCESS) {
            return apiJson;
        }

        // 更新用户信息
        this.saveOrUpdateUserInfor(user, infors);

        // 使用统一登录成功方法对SESSION中的user进行更新
        this.loginSuccess(request, response, user);

        return new ApiJson(SystemCode.SUCCESS);
    }

    /**
     * 保存或更新用户信息(用户已经登录的情况下)
     *
     * @param user user对象,指明要修改的用户
     * @param map  需要修改的参数列表
     */
    @Override
    public void saveOrUpdateUserInfor(User user, Map<String, String> map) {
        // 更新用户表
        if (
            // 用户昵称
                map.containsKey("nickname")
                        // 头像地址
                        || map.containsKey("avatar")
                        // 用户等级
                        || map.containsKey("grade")) {

            if (map.get("nickname") != null) {
                user.setNickname(map.get("nickname"));
            }
            if (map.get("avatar") != null) {
                user.setAvatar(map.get("avatar"));
            }
            if (map.get("grade") != null) {
                user.setGrade(Integer.parseInt(map.get("grade")));
            }

            // 保存到数据库
            userMapper.upUser(user);
        }

        // 更新用户密码表
        if (map.containsKey("password")) {
            Password password = passwordMapper.findPasswordByUserId(user.getUserId());
            if (password == null) {
                password = new Password();
                password.setUserId(user.getUserId());
            }
            password.setPassword(map.get("password"));
            passwordMapper.updatePassword(password);
        }

        // 更新登录名表
        if (map.containsKey("loginName")) {
            LoginName loginName = new LoginName();
            loginName.setUserId(user.getUserId());
            loginName.setLoginName(map.get("loginName"));
            loginNameMapper.inOrUpLoginName(loginName);
        }

        // 更新登录手机号表
        if (map.containsKey("loginPhone")) {
            LoginPhone loginPhone = new LoginPhone();
            loginPhone.setUserId(user.getUserId());
            loginPhone.setLoginPhone(map.get("loginPhone"));
            loginPhoneMapper.inOrUpLoginPhone(loginPhone);
        }

        // 更新登录邮箱表
        if (map.containsKey("loginEmail")) {
            LoginEmail loginEmail = new LoginEmail();
            loginEmail.setUserId(user.getUserId());
            loginEmail.setLoginEmail(map.get("loginEmail"));
            loginEmailMapper.inOrUpLoginEmail(loginEmail);
        }
    }

    /**
     * 登录成功统一处理函数
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param user     用户信息
     */
    @Override
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, User user) {
        try {
            CookieAndSessionUtli cookieAndSessionUtli = new CookieAndSessionUtli(request, response);

            HttpSession session = request.getSession();

            // 将user对象序列化后的字符串存入redis缓存表示登录成功
            String sessionId = session.getId();
            cookieAndSessionUtli.setCookie(CookieKeyEnum.SESSION.getKey() , sessionId);
            redisTemplate.opsForValue().set(RedisPathEnum.USER.getRedisPath() + sessionId, JSON.toJSONString(user));

            // 将user的常用信息写入Cookie中
            cookieAndSessionUtli.setCookie(CookieKeyEnum.USER_ID.getKey(), user.getUserId());
            cookieAndSessionUtli.setCookie(CookieKeyEnum.NICNAME.getKey(), user.getNickname());
            cookieAndSessionUtli.setCookie(CookieKeyEnum.AVATAR.getKey(), user.getAvatar());
            cookieAndSessionUtli.setCookie(CookieKeyEnum.LOGIN_OK.getKey(), "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注销用户登录
     * @param request  请求对象
     */
    @Override
    public void logOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        // 删除Session中存储的User序列化字符串
        redisTemplate.delete(RedisPathEnum.USER.getRedisPath() + session.getId());
        // 过期Session
        this.deleteSessAll(request);
    }

    /**
     * 通过SessionId获取已经登录的用户信息,返回User对象
     * @param sessionId sessionId
     * @return User对象
     */
    @Override
    public User getUserBySessionId(String sessionId) {
        // 从Redis缓存中去查询User序列化后的字符串
        String userStr = redisTemplate.opsForValue().get(RedisPathEnum.USER.getRedisPath() + sessionId);

        // 如果没找到就说明该用户没登录或登录已经过期
        if (userStr == null) {
            return null;
        }

        // 返回反序列化后的User对象
        return JSON.parseObject(userStr, User.class);
    }

}
