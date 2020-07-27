package me.lqw.blog8.model.vo;

import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.CommentStatus;

import java.io.Serializable;

public class CommentPageQueryParam extends PageQueryParam implements Serializable {

    private CommentStatus status;

    private CommentModule module;

    public CommentModule getModule() {
        return module;
    }

    public void setModule(CommentModule module) {
        this.module = module;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }
}
