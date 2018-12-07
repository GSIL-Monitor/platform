package cn.laeni.ossfile.entity.other;

/**
 * 调用对象存储上次成功后返回的对象值
 * @author laeni.cn
 */
public class OssResult {
    /**
     * 上传的文件名
     */
    private String name;
    /**
     * 原始文件名
     */
    private String originalName;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 文件类型
     */
    private String type;
    /**
     * 文件名后缀
     */
    private String suffix;
    /**
     * 对应对象存储的Key
     */
    private String key;
    /**
     * 图片网络地址
     */
    private String url;

    @Override
    public String toString() {
        return "OssResult{" +
                "name='" + name + '\'' +
                ", originalName='" + originalName + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                ", suffix='" + suffix + '\'' +
                ", key='" + key + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    /**
     * 获取上传的文件名
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 设置上传的文件名
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取原始文件名
     * @return
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     * 设置原始文件名
     * @param originalName
     */
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    /**
     * 获取文件大小(单位字节)
     * @return
     */
    public Long getSize() {
        return size;
    }

    /**
     * 设置文件大小
     * @param size
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * 获取文件类型
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * 设置文件类型
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取文件名后缀
     * @return
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * 设置文件名后缀
     * @param suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * 获取对应对象存储的Key
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置对应对象存储的Key
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 获取图片网络地址
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置图片网络地址
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
