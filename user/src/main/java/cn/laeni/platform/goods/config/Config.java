package cn.laeni.platform.goods.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import cn.laeni.sms.AliSms;
import cn.laeni.sms.SmsAbs;
import cn.laeni.utils.string.CharacterUtils;

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
	
	// 【阿里云】短信接口
	@Bean("aliyun")
	public SmsAbs aliyunSmsAbs() {
		return new AliSms();
	}
	
	// 【云之讯】短信接口
	@Bean("ucpaas")
	public SmsAbs ucpaasSmsAbs() {
		return null;
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
