package cn.laeni.platform.ossfile.entity.constant;

/**
 * 指定非默认数据在Redis中的存放路径
 * @author laeni.cn
 */
public enum RequestKeyEnum {
    /**
     * 规定Request中的键使用
     */
    USER("user"),
    /**
     * 规定Request中的键使用
     */
    LOCAL_USER("localUser");

    private String key;

    RequestKeyEnum(String key){
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
