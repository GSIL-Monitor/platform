package cn.laeni.user.other;

/**
 * Session中的key值
 * 该类应该在所有模块中都应该存在并完全相同
 * @author laeni.cn
 */
public enum SessionKeyEnum {
    USER("user");

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
