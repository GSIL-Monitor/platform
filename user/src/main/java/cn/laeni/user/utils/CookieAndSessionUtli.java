package cn.laeni.user.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Cookie工具
 * @author laeni.cn
 */
public class CookieAndSessionUtli {

    protected static Logger logger = LoggerFactory.getLogger(CookieAndSessionUtli.class);

    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * 所有Cookie
     */
    private Cookie[] cookies;

    /**
     * 顶级域名
     */
    private String domain;

    public CookieAndSessionUtli(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.cookies = request.getCookies();
        this.domain = DomainTool.getMainDome(request.getServerName());
    }

    /**
     * 获取请求着访问本站时的域名的顶级域名
     * @return
     */
    public String getDomain() {
        return domain;
    }

    /**
     * 返回一个Cookie对象
     *
     * @param name Cookie名
     * @return Cookie对象
     */
    public Cookie getCookie(String name) {
        if (cookies != null){
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 返回一个Cookie值
     *
     * @param name Cookie名
     * @return Cookie值
     */
    public String getCookieValue(String name) {
        Cookie cookie = this.getCookie(name);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 设置Cookie
     *
     * @param cookie 待插入的cookie值
     */
    public void addCookie(Cookie cookie) {
        response.addCookie(cookie);
    }

    /**
     * 根据具体值设置Cookie
     * 超时时间为0表示删除cookie
     *
     * @param name   Cookie键
     * @param value  Cookie值
     * @param domain [可选]域名,默认为"."+"访问者域名"
     * @param path   [可选],域,默认为"/"
     * @param hour   [可选],超时时间(小时),默认30天
     */
    public void setCookie(String name, String value, String domain, String path, Integer hour) throws UnsupportedEncodingException {
        if (name == null || value == null) {
            return;
        }
        Cookie cookie = new Cookie(name, value);
        if (path != null && !path.equals("")) {
            cookie.setPath(path);
        } else {
            cookie.setPath("/");
        }
        if (hour != null) {
            cookie.setMaxAge(hour * 3600);
        } else {
            cookie.setMaxAge(2592000);
        }
        if (domain != null) {
            cookie.setDomain(domain);
        } else {
            cookie.setDomain(this.domain);
        }
        this.addCookie(cookie);
    }

    /**
     * 根据具体值设置Cookie
     * 超时时间为0表示删除cookie
     *
     * @param name  Cookie名
     * @param value Cookie名对应的值
     */
    public void setCookie(String name, String value) throws UnsupportedEncodingException {
        this.setCookie(name, value, null, null, null);
    }

}
