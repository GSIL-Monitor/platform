package cn.laeni.platform.ossfile.entity.constant;

/**
 * Session中的key值
 * 该类应该在所有模块中都应该存在并完全相同
 * @author laeni.cn
 */

public enum SessionKeyEnum {
    /**
     * 用户模块的User对象在Session中的键值
     */
    USER("user"),
    /**
     * 本模块的User对象在Session中的键值
     */
    LOCAL_USER("localUser");

    private String key;

    SessionKeyEnum(String key){
        this.key = key;
    }

    /**
     * 获取该Session值的key
     * @return
     */
    public String getKey() {
        return key;
    }
}
