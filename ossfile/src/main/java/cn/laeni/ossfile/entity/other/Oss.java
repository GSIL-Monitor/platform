package cn.laeni.ossfile.entity.other;

import java.io.File;

/**
 * 存储桶(对象存储)
 * 每个运营商甚至每一个存储桶都独立有自己的OssBean对象
 * @author laeni.cn
 */
public interface Oss {
    /**
     * 获取URL的根路径
     * @return
     */
    String getMainUrl();

    /**
     * <h2>文件上传</h2>
     * 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
     * 大文件上传请参照 API 文档高级 API 上传
     *
     * @param localFile 待上传的文件
     * @param key       对象键:详情参考 [唯一标识](https://cloud.tencent.com/document/product/436/13324)
     */
    void localFile(File localFile, String key);

    /**
     * 下载文件
     *
     * @param fileName 指定要下载到的本地路径
     * @param key      对象键:详情参考 [唯一标识](https://cloud.tencent.com/document/product/436/13324)
     * @return File
     */
    File downFile(String fileName, String key);

    /**
     * 删除文件
     *
     * @param key 对象键:详情参考 [唯一标识](https://cloud.tencent.com/document/product/436/13324)
     */
    void delete(String key);
}
