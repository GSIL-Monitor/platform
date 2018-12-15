package cn.laeni.user;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/*@ComponentScan
@EnableAutoConfiguration
@EnableEurekaClient*/

/**
 * 核心启动类
 *
 * @author laeni.cn
 */
@SpringBootApplication
@RefreshScope           // 利用Zookeeper进行配置热更新
@EnableFeignClients     // 启动 Spring Cloud Feign 的支持
@EnableRedisHttpSession // 将SESSION内容存入Redis数据库中
@ServletComponentScan   // 开启Servlet/Filter/Listener自动扫描
@DubboComponentScan(basePackages = "cn.laeni.user")    // 可以使用配置代替: dubbo.scan.basePackages="cn.laeni"
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
