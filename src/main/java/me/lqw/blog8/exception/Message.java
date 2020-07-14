package me.lqw.blog8.exception;

import java.io.Serializable;

public class Message implements Serializable {

    private String code;

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
