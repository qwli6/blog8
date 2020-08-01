package me.lqw.blog8.exception;

import java.io.Serializable;

/**
 * 消息实体，用于给前端返回
 * @author liqiwen
 * @since 1.0
 * @version 1.0
 */
public class Message implements Serializable {

    /**
     * 业务码
     */
    private String code;

    /**
     * 业务描述
     */
    private String msg;

    public Message(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Message() {
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

    @Override
    public String toString() {
        return "Message{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
