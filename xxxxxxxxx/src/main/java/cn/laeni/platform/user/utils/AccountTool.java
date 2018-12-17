package cn.laeni.platform.goods.utils;

import cn.laeni.platform.goods.other.AccountTypeEnum;
import cn.laeni.platform.goods.other.VerifyType;
import cn.laeni.platform.goods.other.code.UserCode;
import cn.laeni.platform.goods.other.code.SystemCode;
import cn.laeni.platform.goods.other.entity.ApiJson;
import cn.laeni.platform.goods.service.VerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 帐号相关工具类
 * @author laeni.cn
 */
@Component
public class AccountTool {
    /**
     * 验证码逻辑
     */
    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 验证手机号的正则表达式
     */
    public static final String PHONE_TYPE = "^\\d{11}$";
    /**
     * 验证邮箱号的正则表达式
     */
    public static final String EMAIL_TYPE = "^\\S+@\\S+\\.\\S+$";


    /**
     * 判断帐号类型(手机号?邮箱号?)<br />
     * phone : 手机号<br />
     * email : 电子邮箱地址<br />
     *
     * @return 类型字符串(phone)
     */
    public static AccountTypeEnum accountType(String account) {
        if (account == null) {
            return null;
        }

        // 11位手机号码
        if (account.matches(PHONE_TYPE)) {
            return AccountTypeEnum.PHONE;
        }

        // 邮箱地址
        if (account.matches(EMAIL_TYPE)) {
            return AccountTypeEnum.EMAIL;
        }

        return null;
    }

    /**
     * 检查用户各项资料是否正确
     *
     * @param infors
     * @return
     */
    public boolean checksInfors(HttpServletRequest request, Map<String, String> infors) {
        return this.checksInfors(request, infors, false).getCode().equals(SystemCode.SUCCESS.getCode());
    }

    /**
     * 检查用户各项资料是否正确
     *
     * @param infors       检查传入的参数列表是否合法
     * @param checkAccount 当传入的参数有手机号或者邮箱号时是否验证其验证码的合法性
     * @return
     */
    public ApiJson checksInfors(HttpServletRequest request, Map<String, String> infors, boolean checkAccount) {
        // 资料填写有误
        ApiJson apiJson = new ApiJson(UserCode.ILLEGAL_DATA);

        for (Entry<String, String> entry : infors.entrySet()) {
            // 检查昵称(长度不能大于16)
            if ("nickname".equals(entry.getKey())) {
                // 检查昵称的长度
                if (entry.getValue().toCharArray().length > 16) {
                    return apiJson;
                }
            }
            // 检查登录名(长度不能小于5且大于16)
            else if (entry.getKey().equals("loginName")) {
                // 检查登录名长度
                int length = entry.getValue().toCharArray().length;
                if (length < 5 || length > 16) {
                    return apiJson;
                }
            }
            // 检查登录密码(长度不能小于5且大于16)
            else if (entry.getKey().equals("password")) {
                // 检查登录密码长度
                int length = entry.getValue().toCharArray().length;
                if (length < 5 || length > 32) {
                    return apiJson;
                }
            }
            // 检查手机号(只能是1开头且长度为11)
            else if (entry.getKey().equals("loginPhone")) {
                char[] charArray = entry.getValue().toCharArray();
                // 检查登录密码长度
                if (charArray.length != 11 || charArray[0] != '1') {
                    return apiJson;
                }
            }
            // 检查邮箱(能通过正则表达式的检测)
            else if (entry.getKey().equals("loginEmail")) {
                String value = entry.getValue();
                //检查登录密码长度
                if (value.length() > 32 || !"email".equals(AccountTool.accountType(value))) {
                    return apiJson;
                }
            }
        }

        if (checkAccount) {
            if (infors.containsKey("loginPhone")) {
                if (!infors.containsKey("loginPhoneCode") ||
                        verifyCodeService.chekVerifyCode(request, VerifyType.REG_VERIF_CODE, infors.get("loginPhone"),
                                infors.get("loginPhoneCode")).getEnumCode() != SystemCode.SUCCESS) {
                    // 验证码为空或者不正确
                    return new ApiJson(UserCode.VERIFYCODE_EXPIRE);
                }
            }
            if (infors.containsKey("loginEmail")) {
                if (!infors.containsKey("loginEmailCode") ||
                        verifyCodeService.chekVerifyCode(request, VerifyType.REG_VERIF_CODE, infors.get("loginEmail"),
                                infors.get("loginEmailCode")).getEnumCode() != SystemCode.SUCCESS) {
                    // 验证码为空或者不正确
                    return new ApiJson(UserCode.VERIFYCODE_EXPIRE);
                }
            }
        }

        return new ApiJson(SystemCode.SUCCESS);
    }
}
