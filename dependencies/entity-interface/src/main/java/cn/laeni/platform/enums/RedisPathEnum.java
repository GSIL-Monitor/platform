package cn.laeni.platform.enums;

/**
 * 指定非默认数据在Redis中的存放路径
 * @author laeni.cn
 */
public enum RedisPathEnum {
    /**
     * 存储序列化为Json字符串后的User对象
     */
    USER("mysession:user:");

    private String redisPath;

    RedisPathEnum(String redisPath){
        this.redisPath = redisPath;
    }

    /**
     * 获取Redis的存储路径
     * @return
     */
    public String getRedisPath() {
        return redisPath;
    }
}
