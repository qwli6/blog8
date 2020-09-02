package me.lqw.blog8.constants;

import java.io.Serializable;

/**
 * 消息实体，用于给前端返回
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
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

    /**
     * 构造方法
     *
     * @param code code
     * @param msg  msg
     */
    public Message(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    /**
     * 构造方法
     */
    public Message() {
        super();
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

}
