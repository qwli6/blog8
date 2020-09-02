package me.lqw.blog8.model.dto;

import java.io.Serializable;

/**
 * 保存成功返回已保存的评论
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public class CommentSaved implements Serializable {

    /**
     * 评论 id
     */
    private Integer id;

    /**
     * 是否需要审核
     */
    private Boolean checking;

    /**
     * 构造方法
     *
     * @param id       id
     * @param checking 是否审核
     */
    public CommentSaved(Integer id, Boolean checking) {
        super();
        this.id = id;
        this.checking = checking;
    }

    /**
     * 构造方法
     */
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
