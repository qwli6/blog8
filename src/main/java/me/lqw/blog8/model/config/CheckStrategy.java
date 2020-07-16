package me.lqw.blog8.model.config;

import java.io.Serializable;

public class CheckStrategy<T> implements Serializable {

    private Boolean active;

    private T t;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
