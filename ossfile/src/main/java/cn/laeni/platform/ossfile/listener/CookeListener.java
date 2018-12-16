package cn.laeni.platform.ossfile.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Cookie转码器
 * 请求进入时对Cookie进行解码
 * 请求完成后对Cookie进行编码
 *
 * @author laeni.cn
 */
@WebListener("/*")
public class CookeListener implements ServletRequestListener {
    protected static Logger logger = LoggerFactory.getLogger(CookeListener.class);

    /**
     * 请求前
     *
     * @param sre
     */
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        Cookie[] cookies = ((HttpServletRequest) sre.getServletRequest()).getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                try {
                    // 编码cookie
                    cookie.setValue(URLDecoder.decode(cookie.getValue(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error("Cooekie监听器解码Cookie出错!");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 请求后
     *
     * @param sre
     */
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        Cookie[] cookies = ((HttpServletRequest) sre.getServletRequest()).getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                try {
                    cookie.setValue(URLEncoder.encode(cookie.getValue(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error("Cooekie监听器编码Cookie出错!");
                    e.printStackTrace();
                }
            }
        }
    }
}
