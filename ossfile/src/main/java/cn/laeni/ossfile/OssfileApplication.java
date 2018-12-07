package cn.laeni.ossfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 核心启动类
 *
 * @author laeni.cn
 */
@SpringBootApplication
@EnableDiscoveryClient     // eureka客户端
@EnableFeignClients        // 启动 Spring Cloud Feign 的支持
@EnableRedisHttpSession    // 将SESSION内容存入Redis数据库中
@ServletComponentScan      // 开启Servlet/Filter/Listener自动扫描
public class OssfileApplication {
    public static void main(String[] args) {
        SpringApplication.run(OssfileApplication.class, args);
    }
}
