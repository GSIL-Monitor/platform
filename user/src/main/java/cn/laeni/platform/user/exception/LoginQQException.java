package cn.laeni.platform.user.exception;

/**
 * 捕捉到该异常后将跳转到登陆前页面或者将登录创建关闭
 * @author laeni.cn
 */
public class LoginQQException extends RuntimeException {

    private static final long serialVersionUID = 8521924021393124172L;

    public LoginQQException(String message) {
        super(message);
    }
}
