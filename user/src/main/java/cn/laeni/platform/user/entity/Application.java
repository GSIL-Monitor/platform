package cn.laeni.platform.user.entity;

public class Application {
    /**
     * 应用ID(系统分配,不可改变)
     */
    private String appId;
    /**
     * 应用名称(自定义)
     */
    private String appName;
    /**
     * 该应用的所有者
     */
    private String userId;
    /**
     * 应用回调地址(","分割多个)
     */
    private String redirectUri;
    /**
     * 增加时间(时间戳)
     */
    private Long addTime;
    /**
     * 过期时间(2年,可续期)
     */
    private Long expiredTime;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }
}