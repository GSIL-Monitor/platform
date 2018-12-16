package cn.laeni.platform.ossfile.entity.constant;

/**
 * 指定非默认数据在Redis中的存放路径
 * @author laeni.cn
 */
public enum RedisKeyEnum {
    /**
     * 【】
     * 存储序列化为Json字符串后的User对象
     */
    USER("mysession:user:");

    private String key;

    RedisKeyEnum(String key){
        this.key = key;
    }

    /**
     * 获取Redis的存储路径
     * @return
     */
    public String getKey() {
        return key;
    }
}
