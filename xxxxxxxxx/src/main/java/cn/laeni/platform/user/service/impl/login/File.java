package cn.laeni.platform.user.service.impl.login;

import java.io.Serializable;

/**
 * 该对象用于存储扫描到的html、js和css等内容
 * @author laeni.cn
 *
 */
public class File implements Serializable {

	private static final long serialVersionUID = -6283092918455658566L;
	
	private String html = "";
	private String css;
	private String js;
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getCss() {
		return css;
	}
	public void setCss(String css) {
		this.css = css;
	}
	public String getJs() {
		return js;
	}
	public void setJs(String js) {
		this.js = js;
	}
	@Override
	public String toString() {
		return "File [html=" + html + ", css=" + css + ", js=" + js + "]";
	}
	
}
