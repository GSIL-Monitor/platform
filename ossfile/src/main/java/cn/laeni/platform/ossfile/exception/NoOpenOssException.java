package cn.laeni.platform.ossfile.exception;

/**
 * 该异常表示用户已经,但未开通网盘功能
 * @author laeni.cn
 */
public class NoOpenOssException extends RuntimeException {

    private static final long serialVersionUID = -1662133517843353542L;

    public NoOpenOssException(String message) {
        super(message);
    }
    public NoOpenOssException() {
        super();
    }
}
