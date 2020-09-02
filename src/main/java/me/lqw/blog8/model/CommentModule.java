package me.lqw.blog8.model;

import java.io.Serializable;

/**
 * 评论模块
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public class CommentModule implements Serializable {

    /**
     * 模块 id
     */
    private Integer id;

    /**
     * 模块名称
     */
    private String name;

    public CommentModule() {
        super();
    }

    /**
     * 构造方法
     *
     * @param id   id
     * @param name name
     */
    public CommentModule(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
