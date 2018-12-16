package cn.laeni.platform.ossfile.config;


import cn.laeni.platform.ossfile.entity.other.CosClient;
import cn.laeni.platform.ossfile.entity.other.Oss;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 配置JavaBean
 * @author laeni.cn
 */
@Configuration
public class DefaultConfig {

    /**
     * fastjson
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        return new HttpMessageConverters(new FastJsonHttpMessageConverter());
    }

    /**
     * MVC文件上传
     * @return
     */
    @Bean
    public CommonsMultipartResolver getCommonsMultipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        // 指定上传的文件的最大值(单位字节,1KB=1024字节),-1表示不受限制
        commonsMultipartResolver.setMaxUploadSize(-1);
        return commonsMultipartResolver;
    }

    /**
     * 对象存储(腾讯存储桶)
     * @return
     */
    @Bean
    public Oss getCosClient(){
        CosClient cosClient = new CosClient();
        cosClient.setSecretId("AKID31oPo3q2nMjutgoF1ricfvElKtS0s5cs");
        cosClient.setSecretKey("Y6woicVMCoU9c8Z1SMnXLA1rgUH3ZH2n");
        // COS地域的简称,参照 https://cloud.tencent.com/document/product/436/6224
        cosClient.setRegionName("ap-chengdu");
        // 存储桶名称
        cosClient.setBucketName("ossfile-1252266447");

        return cosClient;
    }

}
