package cn.laeni.platform.ossfile.filter;

import cn.laeni.platform.ossfile.entity.User;
import cn.laeni.platform.ossfile.entity.UserSpace;
import cn.laeni.platform.ossfile.entity.constant.*;
import cn.laeni.platform.ossfile.entity.other.ApiJson;
import cn.laeni.platform.ossfile.exception.NoIdentifyException;
import cn.laeni.platform.ossfile.exception.NoOpenOssException;
import cn.laeni.platform.ossfile.feign.UserFeign;
import cn.laeni.platform.ossfile.mapper.UserSpaceMapper;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * <h1>登录认证过滤器</h1>
 * <p>
 * 验证逻辑:<br/>
 * 1.在Cookie中判断是否有标识用户登录的id;<br/>
 * 如果没有直接返回验证不通过的结果<br/>
 * 2.如果Cookie中找到1中的目标值,则在Session中判断是否有User对象;<br/>
 * 如果有User对象则表明用户已经登录,不再请求User模块;<br/>
 * 如果没有User对象,则请求User模块判断用户是否登录<br/>
 * </p>
 *
 * @author laeni.cn
 */
@WebFilter(filterName = "authenFilter", urlPatterns = {"/*"})
@Order(value = 1)   // 值越小,优先级越高
public class AuthenFilter implements Filter {

    /**
     * 定义需要排除过滤的URL
     */
    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/openService")));

    @Autowired
    UserSpaceMapper userSpaceMapper;

    /**
     * UserFeign(User模块的远程调用)
     */
    @Autowired
    UserFeign userFeign;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // 获取本次请求的url
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");

        try {
            HttpSession session = request.getSession();
            Object user = null;

            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                // 未通过
                throw new NoIdentifyException();
            }

            int i = 0;
            for (; i < cookies.length; i++) {
                Cookie cookie = cookies[i];

                // 只有当Cookie中存在自定义Session的key值时才继续进行下一步操作
                if (CookieKeyEnum.SESSION.getKey().equals(cookie.getName())) {

                    // 先去Session中去找用户信息
                    user = session.getAttribute(SessionKeyEnum.USER.getKey());

                    if (user == null) {
                        // 从User模块去找
                        user = userFeign.userLegalize(cookie.getValue());
                        if (user == null) {
                            // 未通过
                            throw new NoIdentifyException();
                        }
                    }

                    break;
                }
            }

            if (i == cookies.length) {
                // 未通过
                throw new NoIdentifyException();
            }

            // 将user存入Request,以免再次去查询
            request.setAttribute(RequestKeyEnum.USER.getKey(), user);
            // 将user存入Session,以免下次去User模块查询
            session.setAttribute(SessionKeyEnum.USER.getKey(), user);

            // 判断用户是否已经开通文件存储功能
            UserSpace userSpace = this.getInstance(((User) user).getUserId(), session);
            if (userSpace == null) {
                // 如果用户没有开通,并且不是正在开通,则推出
                if (!ALLOWED_PATHS.contains(path)){
                    throw new NoOpenOssException("未开通网盘功能");
                }
            } else {
                // 如果用户已经登录,则将相关信息存入Session
                session.setAttribute(SessionKeyEnum.LOCAL_USER.getKey(), userSpace);
            }
            // 继续请求
            chain.doFilter(servletRequest, response);
        }
        // 用户未登录
        catch (NoIdentifyException e) {
            // 返回未登录的信息
            response.getWriter().print(JSON.toJSONString(new ApiJson<>(UserCodeEnum.LOGIN_INVALID)));
        }
        // 用户未开通本服务
        catch (NoOpenOssException e) {
            // 返回用户未开通本服务的信息
            response.getWriter().print(JSON.toJSONString(new ApiJson<>(OssCodeEnum.NOT_OPEN)));
        }
        // 系统内部错误
        catch (Exception e) {
            e.printStackTrace();
            // 返回内部系统错误信息
            response.getWriter().print(JSON.toJSONString(new ApiJson<>(SystemCodeEnum.OTHER)));
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * 从Session(优先)或者数据库中获取UsrSpace对象
     *
     * @param session
     * @return
     */
    private UserSpace getInstance(String userId, HttpSession session) {
        // 先在Session中查询UserSpace对象
        UserSpace userSpace = (UserSpace) session.getAttribute(SessionKeyEnum.LOCAL_USER.getKey());
        if (userSpace == null) {
            userSpace = userSpaceMapper.selectByPrimaryKey(userId);
        }
        return userSpace;
    }
}
