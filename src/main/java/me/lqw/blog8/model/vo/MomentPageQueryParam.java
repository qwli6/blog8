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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
