package cn.com.zhihetech.online.core.common;

import java.io.Serializable;

/**
 * 服务器相应客户端信息类
 * Created by ShenYunjie on 2015/11/18.
 */
public class ResponseMessage<T> implements Serializable, Cloneable {

    private int code;
    private boolean success;
    private String msg;
    private T data;

    public ResponseMessage() {
        super();
    }

    /**
     * @param code    状态码
     * @param success 操作是否成功
     * @param msg     相应信息
     */
    public ResponseMessage(int code, boolean success, String msg) {
        this.code = code;
        this.success = success;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
