package me.lqw.blog8.model.vo;

import me.lqw.blog8.model.Category;
import me.lqw.blog8.model.Tag;
import me.lqw.blog8.validator.StatusEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class HandledArticleQueryParam extends QueryParam implements Serializable {

    private QueryParam queryParam;

    public QueryParam getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(QueryParam queryParam) {
        this.queryParam = queryParam;
    }

    private Category category;

    private Tag tag;

    private String query;

    private StatusEnum status;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }


    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime begin;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public HandledArticleQueryParam(ArticleQueryParam queryParam, Integer categoryId, Integer tagId) {
        this.queryParam = queryParam;
        if(categoryId != null) {
            this.category = new Category();
            category.setId(categoryId);
        }
        this.currentPage = queryParam.getCurrentPage();
        this.pageSize = queryParam.getPageSize();
        this.query = queryParam.getQuery();
        this.status = queryParam.getStatus();
        if(tag != null){
            this.tag = new Tag(tagId);
        }
    }

    public HandledArticleQueryParam() {
    }
}
