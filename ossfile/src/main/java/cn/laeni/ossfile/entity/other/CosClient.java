package cn.laeni.ossfile.entity.other;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.util.Calendar;

public class CosClient implements Oss {

    private String secretId;
    private String secretKey;
    /**
     * COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
     */
    private String regionName;
    /**
     * bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
     */
    private String bucketName;

    /**
     * 计数器(调用该方法的次数)
     */
    private static long i = 0;
    /**
     * 重置计数器的时间戳
     */
    private static long itime;

    // TODO 最后使用时间,最好是每当使用该对象的方法时就更新该时间,并建立定时器30秒后检测该对象有没有被使用,30秒期间内没有被使用则将其关闭
    private Long time;
    // 客户端对象
    private COSClient cosClient = null;

    // 初始化客户端
    private synchronized void init() {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // clientConfig中包含了设置region, https(默认http), 超时, 代理等set方法, 使用可参见源码或者接口文档FAQ中说明
        ClientConfig clientConfig = new ClientConfig(new Region(regionName));
        this.cosClient = new COSClient(cred, clientConfig);
    }

    /**
     * 返回当天调用该方法的次数
     *
     * @return
     */
    public synchronized long getindex() {
        // 如果重置计数器的时间不在当天范围内则重置计数器
        // 获取当天凌晨0点0分0秒Date
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        if (itime < calendar1.getTimeInMillis()) {
            itime = System.currentTimeMillis();
            i = 0;// 重置计数器
        }

        return ++i;
    }

    /**
     * 获取URL的根路径
     * @return
     */
    @Override
    public String getMainUrl() {
        return "https://ossfile-1252266447.cos.ap-chengdu.myqcloud.com/";
    }

    /**
     * <h2>文件上传</h2>
     * 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
     * 大文件上传请参照 API 文档高级 API 上传
     *
     * @param localFile 待上传的文件
     * @param key       对象键:详情参考 [唯一标识](https://cloud.tencent.com/document/product/436/13324)
     */
    @Override
    public void localFile(File localFile, String key) {
        init();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        cosClient.putObject(putObjectRequest);
    }

    /**
     * 下载文件
     *
     * @param fileName 指定要下载到的本地路径
     * @param key      对象键:详情参考 [唯一标识](https://cloud.tencent.com/document/product/436/13324)
     * @return File
     */
    @Override
    public File downFile(String fileName, String key) {
        init();
        File downFile = new File(fileName);
        // 指定要下载的文件所在的 bucket 和对象键
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
        return downFile;
    }

    /**
     * 下载文件(当缺省保存路径是默认保存于临时缓存区)
     *
     * @param key 对象键:详情参考 [唯一标识](https://cloud.tencent.com/document/product/436/13324)
     * @return File
     */
    public File downFile(String key) {
        return this.downFile(System.getProperty("java.io.tmpdir"), key);
    }

    /**
     * 删除文件
     *
     * @param key 对象键:详情参考 [唯一标识](https://cloud.tencent.com/document/product/436/13324)
     */
    @Override
    public void delete(String key) {
        init();
        if (cosClient != null && key != null) {
            cosClient.deleteObject(bucketName, key);
        }
    }

    /**
     * 关闭客户端(关闭后台线程)
     */
    public void close() {
        if (cosClient != null) {
            cosClient.shutdown();
            cosClient = null;
        }
    }

    @Override
    public String toString() {
        return "CosClient{" +
                "secretId='" + secretId + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", region_name='" + regionName + '\'' +
                ", bucketName='" + bucketName + '\'' +
                '}';
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
