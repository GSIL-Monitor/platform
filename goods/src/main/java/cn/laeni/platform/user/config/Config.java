package cn.laeni.platform.user.config;

import cn.laeni.utils.string.CharacterUtils;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    
	/**
	 * 用于生成随机字符串
	 * @return
	 */
	@Bean
	public CharacterUtils characterUtils() {
		return new CharacterUtils();
	}

	/**
	 * mybatis的配置类
	 * https://blog.csdn.net/einarzhang/article/details/53022820
	 * @return
	 */
	/*@Bean
	@ConfigurationProperties(prefix="mybatis.configuration")	//使用配置文件
	public org.apache.ibatis.session.Configuration myConfig(){
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);	//开启驼峰命名转换(不使用配置文件)
		return configuration;
	}*/

	// 使用阿里云的JSON工具
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		return new HttpMessageConverters(new FastJsonHttpMessageConverter());
	}
	
}
