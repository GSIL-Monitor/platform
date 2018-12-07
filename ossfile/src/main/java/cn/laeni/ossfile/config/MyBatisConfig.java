package cn.laeni.ossfile.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 配置JavaBean
 * @author laeni.cn
 */
@Configuration
// 扫描：该包下相应的class,主要是MyBatis的持久化类.
@MapperScan(basePackages = "cn.laeni.ossfile.mapper", sqlSessionTemplateRef = "dbmybatisSqlSessionTemplate")
public class MyBatisConfig {


    /**
     * 主数据源
     * <p>
     *     XML写法
     *     <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
     *         <!-- 基本属性 url、user、password -->
     *         <property name="url" value="${url}"/>
     *         <property name="username" value="${username}"/>
     *         <property name="password" value="${password}"/>
     *
     *         <!-- 配置初始化大小、最小、最大 -->
     *         <property name="initialSize" value="1"/>
     *         <property name="minIdle" value="1"/>
     *         <property name="maxActive" value="20"/>
     *
     *         <!-- 配置获取连接等待超时的时间 -->
     *         <property name="maxWait" value="60000"/>
     *
     *         <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
     *         <property name="timeBetweenEvictionRunsMillis" value="60000"/>
     *
     *         <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
     *         <property name="minEvictableIdleTimeMillis" value="300000"/>
     *
     *         <property name="validationQuery" value="SELECT 'x'"/>
     *         <property name="testWhileIdle" value="true"/>
     *         <property name="testOnBorrow" value="false"/>
     *         <property name="testOnReturn" value="false"/>
     *
     *         <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
     *         <property name="poolPreparedStatements" value="true"/>
     *         <property name="maxPoolPreparedStatementPerConnectionSize" value="20"/>
     *
     *         <!-- 配置监控统计拦截的filters -->
     *         <property name="filters" value="stat"/>
     *     </bean>
     *     </p>
     * @return
     */
    @Qualifier(value = "dataSource")
    @Bean("ossdataSource")
    @ConfigurationProperties(prefix = "spring.druid.ossfile") // 配置结构
    @Primary // 主数据源
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
    }

    @Bean(name = "dbmybatisSqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("ossdataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:cn/laeni/ossfile/mapper/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "dbmybatisTransactionManager")
    @Primary
    public PlatformTransactionManager testTransactionManager(@Qualifier("ossdataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "dbmybatisSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("dbmybatisSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * MyBatis映射文件和映射接口扫描： (当全部采用注解配置Mapper时可以省略)采用XML配置Mapper时，必须同时指明以下两个Bean
     * @param dataSource
     * @return
     * @throws IOException
     */
    /*@Bean("sqlSessionFactory")
    public SqlSessionFactoryBean getSqlSessionFactoryBean(@Qualifier("dataSource") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置数据源
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 映射文件路径(可以使用通配符)
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:cn/laeni/ossfile/mapper/*.xml"));

        return sqlSessionFactoryBean;
    }*/

    /**
     * 自动映射接口扫描器：自动识别这些映射接口,如果有注解的直接生成相关的Mapper,如果没有注解的还会与上面扫到的xml进行适配
     * @return
     */
    /*@Bean
    public MapperScannerConfigurer getMapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        // 扫描映射接口所在位置
        mapperScannerConfigurer.setBasePackage("cn.laeni.ossfile.mapper");
        // 告诉Spring扫描到的接口交给哪个SqlSessionFactory使用
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");

        return mapperScannerConfigurer;
    }*/

}
