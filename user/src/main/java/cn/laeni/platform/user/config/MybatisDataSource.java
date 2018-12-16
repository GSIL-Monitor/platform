package cn.laeni.platform.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "cn.laeni.platform.user.mapper") // 扫描：该包下相应的class,主要是MyBatis的持久化类.
public class MybatisDataSource {
	@Qualifier(value = "dataSource")
	@Bean("dataSource")
	@ConfigurationProperties(prefix = "datasource.druid") // 配置结构
	@Primary // 主数据源
	public DataSource dataSource() {
		return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
	}
}