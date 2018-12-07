package cn.laeni.ossfile.entity;

import java.io.Serializable;

public class UserSpace implements Serializable {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 总空间
     */
    private Long sumSpace;
    /**
     * 已用空间
     */
    private Long usedSpace;

    @Override
    public String toString() {
        return "UserSpace{" +
                "userId='" + userId + '\'' +
                ", sumSpace=" + sumSpace +
                ", usedSpace=" + usedSpace +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getSumSpace() {
        return sumSpace;
    }

    public void setSumSpace(Long sumSpace) {
        this.sumSpace = sumSpace;
    }

    public Long getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(Long usedSpace) {
        this.usedSpace = usedSpace;
    }
}