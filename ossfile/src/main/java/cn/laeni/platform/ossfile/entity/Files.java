package cn.laeni.platform.ossfile.entity;

public class Files {
    /**
     * 对应对象存储中的Key
     */
    private String fileKey;
    /**
     * 该文件上传者的ID
     */
    private String userId;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * md5
     */
    private String md5;
    /**
     * 文件后缀名
     */
    private String fileType;
    /**
     * 上传时的时间
     */
    private Integer time;

    @Override
    public String toString() {
        return "Files{" +
                "fileKey='" + fileKey + '\'' +
                ", userId='" + userId + '\'' +
                ", fileSize=" + fileSize +
                ", md5='" + md5 + '\'' +
                ", fileType='" + fileType + '\'' +
                ", time=" + time +
                '}';
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}