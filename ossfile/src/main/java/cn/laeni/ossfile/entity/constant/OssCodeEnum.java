package cn.laeni.ossfile.entity.constant;

import cn.laeni.ossfile.entity.other.EnumCode;

public enum  OssCodeEnum implements EnumCode {
    /**
     * 未开通网盘功能
     */
    NOT_OPEN("400","未开通网盘功能"),
    /**
     * 可用空间不足
     */
    NOT_FREE_SPACE("401","可用空间不足"),
    /**
     * 文件过大
     */
    FILE_LARGE("402","文件过大"),
    /**
     * 上传文件格式不允许
     */
    ERROR_FORMAT("403","上传文件格式不允许"),
    /**
     * 秒传
     */
    AGAIN("404","秒传");

    private String code;
    private String msg;

    OssCodeEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获取返回码
     * @return
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * 获取描述信息
     * @return
     */
    @Override
    public String getMsg() {
        return this.msg;
    }
}
