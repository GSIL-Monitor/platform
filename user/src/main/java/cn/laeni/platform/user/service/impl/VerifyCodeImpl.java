package cn.laeni.platform.user.service.impl;

import cn.laeni.platform.enums.RedisKeyEnum;
import cn.laeni.platform.user.entity.*;
import cn.laeni.platform.user.enums.AccountTypeEnum;
import cn.laeni.platform.user.enums.VerifyType;
import cn.laeni.platform.code.SystemCode;
import cn.laeni.platform.user.code.UserCode;
import cn.laeni.platform.entity.ApiJson;
import cn.laeni.sms.SmsAbs;
import cn.laeni.platform.user.service.VerifyCodeService;
import cn.laeni.utils.string.CharacterUtils;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import sun.security.provider.MD5;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class VerifyCodeImpl implements VerifyCodeService {
    protected static Logger logger = LoggerFactory.getLogger(VerifyCodeImpl.class);

    // 验证码配置
    /**
     * 验证码有效期(单位:秒)
     */
    @Value("${verificationCode.validityPeriod:300}")
    private int validityPeriod;
    /**
     * 每个帐号下最多保留几个有效验证码
     */
    @Value("${verificationCode.maxCode:3}")
    private int maxCode;
    /**
     * 输错几次后验证码失效
     */
    @Value("${verificationCode.maxFailures:5}")
    private int maxFailures;
    /**
     * 两个验证码发送最小间隔时间(单位:秒)
     */
    @Value("${verificationCode.intervals:60}")
    private int intervals;
    // end 验证码

    // 邮件
    /**
     * 默认发件人地址
     */
    @Value("${spring.mail.from:m@laeni.cn}")
    private String from;
    // end 邮件

    /**
     * code过期时间,单位:秒
     */
    @Value("${oauth.code-invalid-time}")
    int codeInvalidTime;

    /**
     * 邮件支持
     */
    @Autowired
    JavaMailSender mailSender;
    /**
     * 模板引擎
     */
    @Autowired
    FreeMarkerConfigurer freeMarkerConfigurer;
    /**
     * 短信支持(阿里云)
     */
    @Resource(name = "aliyun")
    SmsAbs sms;
    /**
     * 随机字符串生成工具
     */
    @Autowired
    CharacterUtils characterUtils;
    /**
     * Redis
     */
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate redisTemplate;




    /**
     * 发送验证码(手机或邮箱验证码)
     *
     * @param request    请求头
     * @param account    手机号或邮箱号
     * @param verifyType 验证码类型(注册,登录...)
     * @return ApiJson
     */
    @Override
    public ApiJson sendVerifCode(HttpServletRequest request, String account, VerifyType verifyType) {

        // 创建一个用户名对象
        Account account1 = new Account();
        account1.setValue(account);

        // 检查发送验证码的时间是否在规定得时间之内,如果可以再次发送则返回null,否则返回ApiJson对象
        ApiJson apiJson = this.checkSendTime(getOverTime(account, verifyType));
        if (apiJson != null) {
            return apiJson;
        }

        // 生成随机验证码
        String randomString = characterUtils.getRandomString(6, "012356789");

        try {
            if (account1.getTypeEnum() == AccountTypeEnum.PHONE) {
                // 发送短信验证码
                // TODO 测试期间不真发验证码
                apiJson = this.sendSmsVerifCode(account, randomString);
            } else if (account1.getTypeEnum() == AccountTypeEnum.EMAIL) {
                // 发送邮箱验证码
                // TODO 测试期间不真发验证码
                apiJson = this.sendEmailVerifCode(account, randomString);
            } else {
                return new ApiJson(SystemCode.ILLEGAL);
            }

            apiJson = new ApiJson(SystemCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiJson(SystemCode.OTHER);        // 内部服务器错误
        }

        // 成功
        if (apiJson.getEnumCode() == SystemCode.SUCCESS) {
            // 将验证码存入SESSION
            save(request.getSession(), verifyType, account, randomString);

            // 设置下一次获取验证码的时间
            Map<String, Integer> data = new HashMap<>();
            data.put("time", intervals);
            apiJson.setData(data);

            logger.info("验证码是:" + randomString);
        }

        return apiJson;
    }

    /**
     * 发送短信验证码
     *
     * @param account      手机号
     * @param randomString 6为数字验证码
     * @return
     * @throws ClientException
     */
    @Override
    public ApiJson sendSmsVerifCode(String account, String randomString) throws ClientException {
        // 使用云平台发送验证信息
        Map<String, String> sendSms = sms.sendSms(account, "LAENI网", "SMS_138062930", "{\"code\":\"" + randomString + "\"}");

        if ("OK".equals(sendSms.get("code"))) {
            // 发送成功
            return new ApiJson(SystemCode.SUCCESS);
        } else if (sendSms.get("code").equals("isv.MOBILE_NUMBER_ILLEGAL")) {
            // 资料填写有误，请检查后再试
            return new ApiJson(UserCode.ILLEGAL_DATA);
        } else if (sendSms.get("code").equals("isv.BUSINESS_LIMIT_CONTROL")) {
            // 超频
            return new ApiJson(SystemCode.EXCEED);
        } else {
            // 非法请求
            return new ApiJson(SystemCode.ILLEGAL);
        }
    }

    /**
     * 发送邮箱验证码
     *
     * @param account      邮箱号
     * @param randomString 6为数字验证码
     * @return
     * @throws Exception
     */
    @Override
    public ApiJson sendEmailVerifCode(String account, String randomString) throws Exception {

        // 设置邮件发送时的基本信息
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        message.setFrom(from);
        message.setTo(account);
        message.setSubject("【Laeni】验证中心");

        // 设置邮件模板的参数，支持 freemaker
        Map<String, String> model = new HashMap<>();
        model.put("code", randomString);
        model.put("time", new SimpleDateFormat("y/M/d H:m").format(new Date()));

        // 修改 application.properties 文件中的读取路径
//		freeMarkerConfigurer.setTemplateLoaderPath("classpath:templates.email");

        // 读取 html 模板
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("email/verification-code.html");

        String htmlView = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        message.setText(htmlView, true);

        // 由于发送邮件很慢,所以以线程方式发送验证码
        new Thread(() -> mailSender.send(mimeMessage)).start();

        return new ApiJson(SystemCode.SUCCESS);
    }

    /**
     * 核对验证码 * 连续输错 maxFailures 次后失效
     *
     * @param request
     * @param verifyType
     * @param account
     * @param verifyCode
     * @return
     */
    @Override
    public ApiJson chekVerifyCode(HttpServletRequest request, VerifyType verifyType,
                                  String account, String verifyCode) {
        try {
            if (account == null) {
                return new ApiJson(UserCode.ACCOUNT_ILLEGAL);
            }
            HttpSession session = request.getSession();

            // 从SESSION中获取验证码
            RegVerifCode regVerifCode = (RegVerifCode) session.getAttribute(verifyType.getKey());

            if (verifyCode == null || regVerifCode == null) {
                // 无效验证码
                return new ApiJson(UserCode.VERIFYCODE_EXPIRE);
            }

            // 判断输入次数是否超过限制(超过限制则失效)
            if (regVerifCode.getBeingRegFre() >= maxFailures) {
                deleteVerifCode(request, verifyType);
                return new ApiJson(UserCode.VERIFYCODE_EXPIRE);
            }

            // 清空过期的验证码
            this.removeExcess(regVerifCode);

            // 获取帐号对应的验证码
            List<VerifCode> regValueList = regVerifCode.getBeingRegValueList(account);

            if (regValueList == null || regValueList.size() == 0) {
                // 无效验证码
                return new ApiJson(UserCode.VERIFYCODE_EXPIRE);
            }

            for (VerifCode verifCode : regValueList) {
                if (verifCode.getValue().equals(verifyCode)) {
                    // 正确
                    return new ApiJson(SystemCode.SUCCESS);
                }
            }
            // 将错误次数加一并保存
            regVerifCode.addBeingRegFre();
            session.setAttribute(verifyType.getKey(), regVerifCode);
            // 返回验证码错误
            return new ApiJson(UserCode.VERIFYCODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            // 系统内部错误
            return new ApiJson(SystemCode.OTHER);
        }
    }

    /**
     * 删除指定类型的验证码
     *
     * @param request
     * @param verifyType
     * @return
     */
    @Override
    public boolean deleteVerifCode(HttpServletRequest request, VerifyType verifyType) {
        request.getSession().removeAttribute(verifyType.getKey());
        return false;
    }

    @Override
    public String getCode(Object obj) {
        // 去掉"-",并转为小写
        String nodeKey =  UUID.randomUUID().toString().replace("-", "").toLowerCase();

        redisTemplate.opsForValue().set(RedisKeyEnum.AUTH_CODE_APP.getKey() + nodeKey, obj,
                // 设置过期时间
                codeInvalidTime, TimeUnit.MINUTES);

        return nodeKey;
    }

    @Override
    public ApiJson checkCode(String nodeKey, Object obj) {
        Object o = redisTemplate.opsForValue().get(RedisKeyEnum.AUTH_CODE_APP.getKey() + nodeKey);

        try {
            if (obj instanceof User) {
                User user = (User)o;

            } else if (obj instanceof Application) {
                Application application = (Application)o;
            }
        } catch (Exception e) {

        }

        return null;
    }

    // 将验证码存入SESSION
	/*	【格式】
Map<String, List<VerifCode>> [
    // 正在用于注册的帐号(一般为邮箱或手机号)
    beingRegName=1058259177@qq.com,
    // 输入错误的次数
    beingRegFre=0,
    // 使用数组存储帐号的验证码
    beingRegValue={
        1058259177@qq.com=[
            // 添加时间,值
            {addTime=1530858472236, value=810882},
            {addTime=1530858532888, value=503132},
            {addTime=1530859029307, value=610782}
        ],
        1512542527=[
            // 添加时间,值
            {addTime=1530858472236, value=810882},
            {addTime=1530858532888, value=503132},
            {addTime=1530859029307, value=610782}
        ],
    }
]
	 */

    /**
     * 检查发送验证码的时间是否在规定得时间之内,如果可以再次发送验证码则返回null,否则返回一个对象
     *
     * @param overTime 帐号可再次获取验证码的时间
     * @return 详细信息对象
     */
    private ApiJson checkSendTime(long overTime) {
        if (overTime > 0) {
            // 次数超限(支持1条/分钟，5条/小时 ，累计10条/天)
            ApiJson apiJson = new ApiJson(SystemCode.EXCEED);
            Map<String, Long> data = new HashMap<>();
            data.put("time", overTime);

            apiJson.setData(data);
            return apiJson;
        }
        return null;
    }

    /**
     * 将验证码保存于Session中
     *
     * @param session      HttpSession
     * @param verifyType   验证码类型
     * @param account      帐号
     * @param randomString 验证码
     */
    private void save(HttpSession session, VerifyType verifyType, String account, String randomString) {
        // 用于存储验证码
        RegVerifCode regVerifCode = (RegVerifCode) session.getAttribute(verifyType.getKey());
        if (regVerifCode == null) {
            regVerifCode = new RegVerifCode();
        }

        // 设置当前正在用于注册的帐号
        regVerifCode.setBeingRegName(account);

        // 每当重新获取验证码都将输入错误次数清零
        regVerifCode.setBeingRegFre(0);

        // 获取该帐号下所有验证码
        List<VerifCode> valueList = regVerifCode.getBeingRegValueList(account);
        // 如果为空说明此时还没有验证码
        if (valueList == null) {
            valueList = new ArrayList<>();
            regVerifCode.setBeingRegValueList(valueList);
        }

        // 将新的验证码添加到其他验证码集合中
        valueList.add(new VerifCode(randomString));

        // 清除过期以及超过数量的验证码
        this.removeExcess(regVerifCode);

        // 将全部内容存入SESSION
        session.setAttribute(verifyType.getKey(), regVerifCode);

        // 记录获取时间
        // 记录帐号获取验证码的时间,值记录成功的
        stringRedisTemplate.opsForValue().set(
                "user:" + verifyType.getKey() + ":" + account,    // 键
                String.valueOf(System.currentTimeMillis()),            // 值为当前时间戳
                1,                //过期时间
                TimeUnit.DAYS    // 时间单位(天)
        );
    }

    /**
     * 清除过期以及超过数量的验证码
     *
     * @param regVerifCode
     */
    private void removeExcess(RegVerifCode regVerifCode) {
        try {
            Map<String, List<VerifCode>> beingRegValue = regVerifCode.getBeingRegValue();
            for (List<VerifCode> valueList : beingRegValue.values()) {

                List<VerifCode> verifCodeList = new ArrayList<>();
                // 清空过期的
                for (VerifCode verifCode : valueList) {
                    if (verifCode.getAddTime() + validityPeriod * 1000 <= System.currentTimeMillis()) {
                        verifCodeList.add(verifCode);
                    }
                }
                for (VerifCode verifCode : verifCodeList) {
                    valueList.remove(verifCode);
                }

                // 数量控制在规定内(最多不能超过 maxCode 个)
                if (valueList.size() > maxCode) {
                    valueList.remove(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("清除过期以及超过数量的验证码错误");
        }
    }

    /**
     * 验证该帐号可再次获取验证码的时间
     *
     * @param account
     * @param verifyType
     * @return
     */
    private long getOverTime(String account, VerifyType verifyType) {
        String overTime = stringRedisTemplate.opsForValue().get("user:" + verifyType.getKey() + ":" + account);
        Long time;
        if (overTime == null) {
            return 0;
        } else {
            time = Long.valueOf(overTime);
        }

        // 当前时间戳
        long t = System.currentTimeMillis();

        // intervals 为间隔的秒数
        if (time + intervals * 1000 > t) {
            return intervals - (t - time) / 1000;
        }

        return 0;
    }
}
