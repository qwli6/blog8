package me.lqw.blog8.model.vo;

import java.io.Serializable;

public class MomentNavQueryParam implements Serializable {
    private int currentId;

    private Boolean queryPrivate;

    public int getCurrentId() {
        return currentId;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    public Boolean getQueryPrivate() {
        return queryPrivate;
    }

    public void setQueryPrivate(Boolean queryPrivate) {
        this.queryPrivate = queryPrivate;
    }
}
