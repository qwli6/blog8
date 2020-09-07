package me.lqw.blog8.model.vo;

import me.lqw.blog8.model.CommentModule;

import java.io.Serializable;

/**
 * 查看会话参数
 * @author liqiwen
 * @version 2.1
 * @since 2.1
 */
public class CheckConversationParams implements Serializable {

    /**
     * 评论模块
     */
    private CommentModule commentModule;

    /**
     * 评论 id
     */
    private Integer id;

    /**
     * 构造方法
     */
    public CheckConversationParams() {
        super();
    }

    /**
     * 全参数构造方法
     * @param commentModule commentModule
     * @param id id
     */
    public CheckConversationParams(CommentModule commentModule, Integer id) {
        super();
        this.commentModule = commentModule;
        this.id = id;
    }

    public CommentModule getCommentModule() {
        return commentModule;
    }

    public void setCommentModule(CommentModule commentModule) {
        this.commentModule = commentModule;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
