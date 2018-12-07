package cn.laeni.ossfile.entity;

import java.io.Serializable;

/**
 * 用户基本信息
 *
 * @author laeni.cn
 */
public class User implements Serializable {
    private static final long serialVersionUID = -8115538448701751641L;

    /**
     * 用户唯一id(固定不变)
     */
    private String userId;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 用户等级
     */
    private Integer grade;

    /**
     * 获取用户唯一id(固定不变)
     *
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户唯一id
     *
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取用户昵称
     *
     * @return
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置用户昵称
     *
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取头像地址
     *
     * @return
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置头像地址
     *
     * @param avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 获取用户等级
     *
     * @return
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * 设置用户等级
     *
     * @param grade
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", grade=" + grade +
                '}';
    }
}
