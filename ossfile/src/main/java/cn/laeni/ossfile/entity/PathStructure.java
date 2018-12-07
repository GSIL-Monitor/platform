package cn.laeni.ossfile.entity;

/**
 * 路径ID
 * @author laeni.cn
 */
public class PathStructure {
    /**
     * 唯一的路径ID
     */
    private Integer pathId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 文件或目录名
     */
    private String pathName;
    /**
     * 父目录的ID(即属于哪个目录,根目录为空)
     */
    private Integer parentPathId;
    /**
     * 时间
     */
    private Integer time;
    /**
     * 是否为目录
     */
    private Boolean directory;

    @Override
    public String toString() {
        return "PathStructure{" +
                "pathId=" + pathId +
                ", userId='" + userId + '\'' +
                ", pathName='" + pathName + '\'' +
                ", parentPathId=" + parentPathId +
                ", time=" + time +
                ", directory=" + directory +
                '}';
    }

    public Integer getPathId() {
        return pathId;
    }

    public void setPathId(Integer pathId) {
        this.pathId = pathId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public Integer getParentPathId() {
        return parentPathId;
    }

    public void setParentPathId(Integer parentPathId) {
        this.parentPathId = parentPathId;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Boolean getDirectory() {
        return directory;
    }

    public void setDirectory(Boolean directory) {
        this.directory = directory;
    }
}