package me.lqw.blog8.model;

import java.io.Serializable;

public class CommentModule implements Serializable {

    private Integer id;

    private String name;

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
