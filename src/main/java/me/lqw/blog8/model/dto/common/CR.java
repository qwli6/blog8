package me.lqw.blog8.model.dto.common;

import java.io.Serializable;

/**
 * 通用结果集封装返回
 *
 * @param <T> T
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class CR<T> extends AbstractResponse implements Serializable {

    /**
     * 返回数据
     */
    private T data;

    /**
     * 构造方法
     */
    public CR() {
        super();
    }

    /**
     * 构造方法
     *
     * @param t t
     */
    public CR(T t) {
        super();
        this.data = t;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
