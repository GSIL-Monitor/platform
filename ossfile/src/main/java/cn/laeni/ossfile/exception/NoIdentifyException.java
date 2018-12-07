package cn.laeni.ossfile.exception;

/**
 * 该异常表示用户未登录
 * @author laeni.cn
 */
public class NoIdentifyException extends RuntimeException {

    private static final long serialVersionUID = -1662133517843353542L;

    public NoIdentifyException(String message) {
        super(message);
    }
    public NoIdentifyException() {
        super();
    }
}
