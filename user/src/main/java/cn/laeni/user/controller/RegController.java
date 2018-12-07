package cn.laeni.user.controller;

import cn.laeni.user.other.VerifyType;
import cn.laeni.user.other.code.SystemCode;
import cn.laeni.user.other.entity.ApiJson;
import cn.laeni.user.service.RegService;
import cn.laeni.user.service.VerifyCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin // 跨域访问
public class RegController {
    protected static Logger logger = LoggerFactory.getLogger(RegController.class);

    /**
     * 验证码Service: 验证验证码
     */
    @Autowired
    private VerifyCodeService verifyCodeService;
    /**
     * 注册Service: 检查用户帐号是否存在
     */
    @Autowired
    private RegService regService;

    // 显示用户注册页

    /**
     * 检查输入手机号或邮箱号是否被注册
     * <p>未注册时注册并返回相关信息|已注册时返回已注册相关信息|(验证码正确的前提下)</p>
     * @param request
     * @param response
     * @param account 手机号或邮箱号
     * @param verifyCode 验证码
     * @param force 是否为强制注册
     * @return
     */
    @RequestMapping("/api/regAccount")
    @ResponseBody
    public ApiJson regAccount(HttpServletRequest request, HttpServletResponse response,
                              String account, String verifyCode, String force) {
        try {
            // 检查请求的帐号和验证码是否正确
            ApiJson apiJson = verifyCodeService.chekVerifyCode(request, VerifyType.REG_VERIF_CODE, account, verifyCode);
            if (apiJson.getEnumCode() != SystemCode.SUCCESS) {
                // 验证码不正确时返回验证码的返回码
                return apiJson;
            }

            // 使用获取的手机号或者邮箱号注册帐号
            return regService.regAccount(request, response, account, verifyCode, force);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiJson(SystemCode.OTHER);
        }
    }

}
