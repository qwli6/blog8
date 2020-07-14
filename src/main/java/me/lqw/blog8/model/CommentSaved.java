package me.lqw.blog8.model;

import java.io.Serializable;

public class CommentSaved implements Serializable {

    private Integer id;

    private Boolean checking;

    public CommentSaved(Integer id, Boolean checking) {
        super();
        this.id = id;
        this.checking = checking;
    }

    public CommentSaved() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getChecking() {
        return checking;
    }

    public void setChecking(Boolean checking) {
        this.checking = checking;
    }
}
