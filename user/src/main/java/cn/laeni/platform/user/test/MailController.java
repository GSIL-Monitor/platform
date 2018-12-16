package cn.laeni.platform.user.test;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

@RestController
public class MailController {

	/**
	 * 邮件支持
	 */
	@Autowired
	JavaMailSender mailSender;

	/**
	 * 模板引擎
	 */
	@Autowired
	FreeMarkerConfigurer freeMarkerConfigurer;

	@RequestMapping("sendemail")
	public String sendEmail() {
		try {
			final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
			final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
			message.setFrom("mr@laeni.cn");
			message.setTo("1058259177@qq.com");
			message.setSubject("测试邮件主题");

			// 发送模板邮件，支持 freemaker
			Map<String, String> model = new HashMap<>(1000);
			model.put("user", "zggdczfr");
			// 修改 application.properties 文件中的读取路径
			freeMarkerConfigurer.setTemplateLoaderPath("classpath:templates.email");
			freeMarkerConfigurer.setDefaultEncoding("UTF-8");
			// 读取 html 模板
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("verification-code.html");
			template.setOutputEncoding("UTF-8");
			
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			System.out.println(html);
			message.setText(html, true);

			System.out.println(System.currentTimeMillis());
			// mailSender.send(mimeMessage);

			System.out.println(System.currentTimeMillis());
			return null;
		} catch (Exception ex) {
			System.out.println("出错");
			return null;
		}
	}
}