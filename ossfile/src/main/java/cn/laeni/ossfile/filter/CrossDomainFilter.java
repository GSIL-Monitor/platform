package cn.laeni.ossfile.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 统一进行跨域访问支持处理
 * 测试期间允许全部跨域访问,正式部署之后可关闭
 * @author laeni.cn
 */
@WebFilter(filterName = "crossDomainFilter",urlPatterns = {"/*"})
public class CrossDomainFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest)request;
		HttpServletResponse servletResponse = (HttpServletResponse)response;

		// 获取页面的请求地址
		String address = servletRequest.getHeader("referer");
		if (address != null) {
			try {
				URL url = new URL(address);
				/* url.getProtocol()获取URL请求请求协议
				 * url.getAuthority()能获取包含端口部分
				 */
				servletResponse.setHeader("Access-Control-Allow-Origin", url.getProtocol() + "://" + url.getAuthority());
				servletResponse.setHeader("Access-Control-Allow-Credentials", "true");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		chain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}
}
