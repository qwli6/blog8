package me.lqw.blog8.model.dto;

import java.io.Serializable;

/**
 * 通用结果集封装返回
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 * @param <T> T
 */
public class CR<T> extends BaseResponse implements Serializable {

    private T data;

    public CR() {
        super();
    }

    public CR(T t){
        this.data = t;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
