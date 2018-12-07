package cn.laeni.ossfile.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 融合了用户文件夹以及文件的详细信息，这些信息都是用户可见的
 * @author laeni.cn
 */
public class PathAndFile {
    /**
     * 用户目录id
     * 唯一标识每一个文件或目录在用户目录中的位置
     */
    private Integer pathId;

    /**
     * 文件或目录名
     */
    private String pathName;

    /**
     * 文件或目录所在的目录id(父路径的id)
     */
    private Integer parentPathId;

    /**
     * 用户创建文件或目录的时间
     */
    private Integer time;

    /**
     * 是否为目录
     */
    private Boolean directory;

    /**
     * 文件的使用者
     */
    private String used;

    /**
     * 文件key,与对象存储中的key一一对应
     */
    @JSONField(serialize = false)
    private String fileKey;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 文件类型(后缀)
     */
    private String fileType;

    /**
     * 文件链接
     */
    private String fileLink = "https://ossfile-1252266447.cos.ap-chengdu.myqcloud.com/";



    @Override
    public String toString() {
        return "PathAndFile{" +
                "pathId=" + pathId +
                ", pathName='" + pathName + '\'' +
                ", parentPathId=" + parentPathId +
                ", time=" + time +
                ", directory=" + directory +
                ", used='" + used + '\'' +
                ", fileKey='" + fileKey + '\'' +
                ", fileSize=" + fileSize +
                ", fileType='" + fileType + '\'' +
                '}';
    }

    public Integer getPathId() {
        return pathId;
    }

    public void setPathId(Integer pathId) {
        this.pathId = pathId;
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

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileLink() {
        return fileLink + fileKey;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

}
