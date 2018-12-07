package cn.laeni.ossfile.entity.other;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 通用返回码标准定义
 *
 * @author laeni.cn
 */
public class ApiJson<T> implements Serializable {
    private static final long serialVersionUID = 7911624289323178723L;

    // 控制器输出时不显示该属性
    @JSONField(serialize = false)
    private T enumCode;
    private String code = "000";
    private String msg = "OK";
    private Object data;

    /**
     *
     * @param systemCode 对应的状态码对象
     */
    public ApiJson(T systemCode) {
        this.enumCode = systemCode;
        if( systemCode instanceof EnumCode ){
            this.code = ((EnumCode)systemCode).getCode();
            this.msg = ((EnumCode)systemCode).getMsg();
        }
    }

    public T getEnumCode() {
        return enumCode;
    }

    public void setEnumCode(T enumCode) {
        this.enumCode = enumCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiJson [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }

}