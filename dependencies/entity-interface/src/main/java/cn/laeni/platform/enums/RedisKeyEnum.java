package cn.laeni.platform.enums;

/**
 * 指定非默认数据在Redis中的存放路径
 * @author laeni.cn
 */
public enum RedisKeyEnum {
    /**
     * 存储序列化为Json字符串后的User对象
     */
    USER("mysession:user:"),
    /**
     * 用于本系统中OAuth2.0协议中code的存储(应用授权)
     */
    AUTH_CODE_APP("oauth:authCode:app:"),
    /**
     * 用于本系统中OAuth2.0协议中code的存储(帐号授权,主要用于模块间跳转授权)
     */
    AUTH_CODE_USER("oauth:authCode:user:");

    private String redisPath;

    RedisKeyEnum(String redisPath){
        this.redisPath = redisPath;
    }

    /**
     * 获取Redis的存储路径
     * @return 路径
     */
    public String getKey() {
        return redisPath;
    }
}
