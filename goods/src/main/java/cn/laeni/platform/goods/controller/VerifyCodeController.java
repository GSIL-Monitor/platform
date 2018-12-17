package cn.laeni.platform.goods.controller;

import cn.laeni.platform.goods.other.VerifyType;
import cn.laeni.platform.goods.other.code.SystemCode;
import cn.laeni.platform.goods.other.entity.ApiJson;
import cn.laeni.platform.goods.service.VerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 发送验证码或验证验证码是否正确
 *
 * @author laeni.cn
 */
@RestController
@CrossOrigin // 跨域访问
public class VerifyCodeController {

    /**
     * 验证码Service
     */
    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 发送验证码(手机或邮箱验证码)
     *
     * @param account    帐号(手机或邮箱验证码)
     * @return
     */
    @RequestMapping("/api/sendVerifyCode")
    public ApiJson sendVerifyCode(HttpServletRequest request, HttpServletResponse response, String account) {
        // 非法请求
        if (account == null) {
            return new ApiJson(SystemCode.ILLEGAL);
        }
        // 发送验证码
        return verifyCodeService.sendVerifCode(request, account, VerifyType.REG_VERIF_CODE);
    }

    /**
     * 验证验证码
     *
     * @param account   帐号(手机号或者邮箱号)
     * @param verifyCode 验证码(浏览器传过来的)
     * @return ApiJson
     */
    @RequestMapping("/api/chekVerifyCode")
    public ApiJson chekVerifyCode(HttpServletRequest request, HttpServletResponse response,
                                  String account, String verifyCode) {
        return verifyCodeService.chekVerifyCode(request, VerifyType.REG_VERIF_CODE, account, verifyCode);
    }

}
