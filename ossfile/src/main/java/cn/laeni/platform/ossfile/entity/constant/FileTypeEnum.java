package cn.laeni.platform.ossfile.entity.constant;

/**
 * 文件类型
 * @author laeni.cn
 */

public enum FileTypeEnum {

    /**
     * 文档
     */
    DOCUMENT("document", "'pdf','txt','ppt','pptx','doc','docx','xlsx','xlsm','xltx','xltm','xlsb','xlam'"),
    /**
     * 图像
     */
    IMAGE("image", "'psd','pdd','gif','jpeg','jpg','png','svg'"),
    /**
     * 视频
     */
    VIDEO("video","'avi','rm','rmvb','mpeg','mpg','dat','mov','qt','asf','wmv'"),
    /**
     * 音乐
     */
    MUSIC("music","'mp3','flpc','ape','midi','wav','cda','wma'");

    FileTypeEnum(String typeName,String fullName) {
        this.typeName = typeName;
        this.fullName = fullName;
    }

    /**
     * 类型名
     */
    private String typeName;

    private String fullName;

    /**
     * 获取文件类型名
     * @return
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * <h1>获取该文件类型对应的详细扩展名</h1>
     * <p>如document对应的扩展名有:txt,ppt,pptx,doc,docx等</p>
     * @return
     */
    public String getFullName() {
        return fullName;
    }

}
