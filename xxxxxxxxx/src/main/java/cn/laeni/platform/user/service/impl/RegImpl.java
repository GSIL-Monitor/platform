package cn.laeni.platform.user.service.impl;

import cn.laeni.platform.user.entity.*;
import cn.laeni.platform.user.feign.ConnectQQFeign;
import cn.laeni.platform.user.mapper.*;
import cn.laeni.platform.user.other.AccountTypeEnum;
import cn.laeni.platform.code.SystemCode;
import cn.laeni.platform.code.UserCode;
import cn.laeni.platform.user.other.entity.Account;
import cn.laeni.platform.user.other.entity.ApiJson;
import cn.laeni.platform.user.other.entity.UserQQInfo;
import cn.laeni.platform.user.service.RegService;
import cn.laeni.platform.user.service.UserIdFactory;
import cn.laeni.platform.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laeni.cn
 */
@Service
public class RegImpl implements RegService {
    protected static Logger logger = LoggerFactory.getLogger(RegImpl.class);

    /**
     * userId工厂
     */
    @Autowired
    private UserIdFactory userIdFactory;

    // Mapper
    /**
     * 用户表
     */
    @Autowired
    private UserMapper userMapper;
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
     * 登录名表
     */
    @Autowired
    private LoginNameMapper loginNameMapper;
    /**
     * QQ登录信息表
     */
    @Autowired
    private ConnectQQMapper connectQQMapper;

    /**
     * 用户Service
     */
    @Autowired
    private UserService userService;

    /**
     * Feign
     */
    @Autowired
    private ConnectQQFeign connectQQFeign;

    /**
     * 根据提交的"手机号或邮箱号"、"验证码"注册帐号
     *
     * @param request
     * @param response
     * @param account
     * @param verifyCode
     * @param force
     * @return
     */
    @Override
    public ApiJson regAccount(HttpServletRequest request, HttpServletResponse response,
                              String account, String verifyCode, String force) {

        // 创建一个用户名对象
        Account account1 = new Account();
        account1.setValue(account);

        // 用户Bean
        User user = userService.getUserByName(account1);

        /*
         * 检查用户用于注册的手机号或者邮箱号是否已经被注册 如果被注册则返回用户的一些基本信息 如果没有被注册则注册该用户
         */
        // 没有被注册或强制注册
        if (user == null || force != null) {
            // 注册帐号
            user = this.reg(account1);
            // 存储登录成功的用户信息
            userService.loginSuccess(request, response, user);
            return new ApiJson(SystemCode.SUCCESS);
        }
        // 不是强制注册且用户已经存在
        else {
            ApiJson apiJson = new ApiJson(UserCode.USER_EXIST);
            apiJson.setData(this.getData(user, account1));
            return apiJson;
        }

    }

    /**
     * 如果是第一次使用QQ登陆则调用该方法为新用户添加userId与之关联
     * @param connectQQ 从QQ服务器查询出来的用户信息
     * @return
     */
    @Override
    public User regQQAccount(ConnectQQ connectQQ) {
        // 查询QQ用户信息(一般只在第一次才查询)
        UserQQInfo userQQInfo;

        try {
            userQQInfo = connectQQFeign.getUserInfo(connectQQ.getAccessToken(), connectQQ.getOpenid());

            if (userQQInfo.getRet() != 0) {
                throw new Exception("RegImpl.regQQAccount(): 查询QQ用户信息出错,但理论上上不应该会出错!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // 创建一个新用户并存入用户表
        User user = new User();

        // 注册用户
        // 在ID库中取出一个"用户id"
        String userId = userIdFactory.getUserIdAndDel();

        user.setUserId(userId);
        // 第一次登录使用QQ头像地址
        user.setAvatar(userQQInfo.getFigureurlQQ100() != null && !userQQInfo.getFigureurlQQ100().equals("")
                ? userQQInfo.getFigureurlQQ100()
                : userQQInfo.getFigureurlQQ40());
        // 第一次登录使用QQ昵称
        user.setNickname(userQQInfo.getNickname());
        userMapper.saveUser(user);

        // 将QQ信息保存到QQ信息表中
        // 使用刚刚生成的UserId与该QQ用户绑定
        connectQQ.setUserId(userId);
        connectQQMapper.saveConnectQQ(connectQQ);

        return user;
    }

    // -private--------------------private------------------------private-----------

    /**
     * 将基本信息打包(格式化)
     * 存储用户基本信息 nickname => 昵称 loginName => 登录名 loginPhone => 登录手机号 loginEmail =>
     * 登录邮箱 account => 用户用于注册的手机号或者邮箱号类型
     *
     * @param user        userBean
     * @param account 用于注册的帐号的类型
     * @return 以键值对方式返回"昵称","登录名","登录手机号"
     */
    private Map<String, String> getData(User user, Account account) {
        // 用于存储返回的数据
        Map<String, String> data = new HashMap<>();

        // 登录手机号
        LoginPhone loginPhone = null;
        // 登录邮箱
        LoginEmail loginEmail = null;

        /*
         * 使用户注册时的手机或者邮箱号明文显示
         */
        if (account.getTypeEnum() == AccountTypeEnum.PHONE) {
            loginPhone = loginPhoneMapper.findLoginPhoneByUserId(user.getUserId());
            if (loginPhone != null) {
                data.put("loginPhone", loginPhone.getLoginPhone());
            }
        } else if (account.getTypeEnum() == AccountTypeEnum.EMAIL) {
            loginEmail = loginEmailMapper.findLoginEmailByUserId(user.getUserId());
            if (loginEmail != null) {
                data.put("loginEmail", loginEmail.getLoginEmail());
            }
        }

        // 用户用于注册的手机号或者邮箱号类型
        data.put("account", account.getTypeEnum().getType());

        // 登录名(隐藏部分)
        LoginName loginName = loginNameMapper.findLoginNameByUserId(user.getUserId());
        if (loginName != null) {
            // 隐藏部分内容
            data.put("loginName", this.ycLoginName(loginName.getLoginName()));
        } else {
            data.put("loginName", "——————");
        }

        /*
         * 使登录手机号(隐藏部分)
         */
        if (!data.containsKey("loginPhone") && loginPhone != null) {
            // 隐藏部分内容
            data.put("loginPhone", ycLoginPhone(loginPhone.getLoginPhone()));
        } else if (!data.containsKey("loginPhone")) {
            data.put("loginPhone", "——————");
        }

        /*
         * 使登录邮箱号(隐藏部分)
         */
        if (!data.containsKey("loginEmail") && loginEmail != null) {

            // 将邮箱地址添加到data
            data.put("loginEmail", ycLoginEmail(loginEmail.getLoginEmail()));
        } else if (!data.containsKey("loginEmail")) {
            data.put("loginEmail", "——————");
        }

        // 昵称
        data.put("nickname", user.getNickname());

        return data;
    }

    /**
     * 根据提供的信息注册帐号
     *
     * @param account 用户名
     */
    private User reg(Account account) {
        // 在ID库中取出一个"用户id"
        String userId = userIdFactory.getUserIdAndDel();

        // 创建一个新用户并存入用户表
        User user = new User();
        user.setUserId(userId);
        userMapper.saveUser(user);

        if (account.getTypeEnum() == AccountTypeEnum.PHONE) {
            LoginPhone loginPhoneBean = new LoginPhone();
            loginPhoneBean.setLoginPhone(account.getValue());
            loginPhoneBean.setUserId(userId);
            // 将登陆手机号保存到数据库
            loginPhoneMapper.inOrUpLoginPhone(loginPhoneBean);
        } else if (account.getTypeEnum() == AccountTypeEnum.EMAIL) {
            LoginEmail loginEmailBean = new LoginEmail();
            loginEmailBean.setLoginEmail(account.getValue());
            loginEmailBean.setUserId(userId);
            loginEmailMapper.inOrUpLoginEmail(loginEmailBean);
        }

        return user;
    }

    /**
     * 隐藏部分登录名
     *
     * @param loginName 登录名
     * @return
     */
    private String ycLoginName(String loginName) {
        char[] loginNameArray = loginName.toCharArray();
        char[] loginNameArrayT = {'*', '*', '*', '*', '*', '*', '*'};
        loginNameArrayT[0] = loginNameArray[0];
        loginNameArrayT[loginNameArrayT.length - 1] = loginNameArray[loginNameArray.length - 1];
        return String.valueOf(loginNameArrayT);
    }

    /**
     * 隐藏部分手机号
     *
     * @param loginPhone 手机号
     * @return
     */
    private String ycLoginPhone(String loginPhone) {
        char[] loginPhoneArray = loginPhone.toCharArray();
        char[] loginPhoneArrayT = {'*', '*', '*', '*', '*', '*', '*', '*', '*', '*', '*'};
        try {
            loginPhoneArrayT[0] = loginPhoneArray[0];
            loginPhoneArrayT[1] = loginPhoneArray[1];
            loginPhoneArrayT[2] = loginPhoneArray[2];
            loginPhoneArrayT[7] = loginPhoneArray[7];
            loginPhoneArrayT[8] = loginPhoneArray[8];
            loginPhoneArrayT[9] = loginPhoneArray[9];
            loginPhoneArrayT[10] = loginPhoneArray[10];
            return String.valueOf(loginPhoneArrayT);
        } catch (Exception e) {
            System.out.println("数组索引超出范围");
            return "——————";
        }
    }

    /**
     * 隐藏部分邮箱号
     *
     * @param loginEmail 邮箱号
     * @return
     */
    private String ycLoginEmail(String loginEmail) {
        char[] loginEmailArray = loginEmail.toCharArray();
        // 邮箱地址前半段
        StringBuilder loginEmailL = new StringBuilder();
        // 邮箱地址后半段
        StringBuilder loginEmailR = new StringBuilder();

        // 标记是否为邮箱地址的后半段
        boolean h = false;
        for (int i = 0; i < loginEmailArray.length; i++) {
            if (loginEmailArray[i] == '@') {
                h = true;
            }
            if (h) {
                /*
                 * 后半段
                 */
                loginEmailR.append(loginEmailArray[i]);
            } else {
                /*
                 * 前半段
                 */
                if (i < 2) {
                    loginEmailL.append(loginEmailArray[i]);
                }
            }
        }
        return loginEmailL.append("*****").append(loginEmailR.toString()).toString();
    }

}
