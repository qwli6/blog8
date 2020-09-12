package me.lqw.blog8.model.vo;

import java.io.Serializable;

/**
 * 动态分页查询参数
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class MomentPageQueryParam extends AbstractQueryParam implements Serializable {

    /**
     * 关键字
     */
    private String query;

    /**
     * 是否查询私人动态
     * @since 2.2
     */
    private Boolean queryPrivate;

    public Boolean getQueryPrivate() {
        return queryPrivate;
    }

    public void setQueryPrivate(Boolean queryPrivate) {
        this.queryPrivate = queryPrivate;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
