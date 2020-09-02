package me.lqw.blog8.model.vo;

import me.lqw.blog8.model.enums.ArticleStatusEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 内容查询参数
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class ArticlePageQueryParam extends AbstractQueryParam implements Serializable {

    /**
     * 关键字
     */
    private String query;

    /**
     * 查询标签
     */
    private String tag;

    /**
     * 查询分类
     */
    private String category;

    /**
     * 查询状态
     */
    private ArticleStatusEnum status;

    /**
     * 结束查询时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;

    /**
     * 开始查询时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime begin;


    public ArticleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ArticleStatusEnum status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
