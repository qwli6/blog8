package me.lqw.blog8.model.vo;

import java.io.Serializable;

public class TagPageQueryParam extends PageQueryParam implements Serializable {

    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
