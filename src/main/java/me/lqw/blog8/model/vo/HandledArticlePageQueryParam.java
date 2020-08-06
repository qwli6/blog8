package me.lqw.blog8.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import me.lqw.blog8.model.Category;
import me.lqw.blog8.model.Tag;
import me.lqw.blog8.validator.StatusEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章内容查询参数
 * @author liqiwen
 * @version 1.0
 */
public class HandledArticlePageQueryParam extends PageQueryParam implements Serializable {

    /**
     * 是否查询私人内容
     */
    private boolean queryPrivate;

    /**
     * 是否查询受密码保护的内容
     */
    private boolean queryPasswordProtect;

    /**
     * 分页查询参数
     */
    private PageQueryParam pageQueryParam;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime begin;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime end;

    /**
     * 文章 id，从 lucene 根据 query/category/tag 获取文章列表，
     * 查询出文章的 id，然后进行检索
     *
     * 返给前端的时候，忽略这个字段
     */
    @JsonIgnoreProperties
    private List<Integer> ids;

    /**
     * 分类
     */
    private Category category;

    /**
     * 标签
     */
    private Tag tag;

    /**
     * 查询关键字段
     * 需要对长度进行限制
     */
    private String query;

    /**
     * 查询内容的状态
     * 用户未登录|POSTED
     * 用户已登录|ALL
     */
    private StatusEnum status;

    public PageQueryParam getPageQueryParam() {
        return pageQueryParam;
    }

    public void setPageQueryParam(PageQueryParam pageQueryParam) {
        this.pageQueryParam = pageQueryParam;
    }

    public boolean isQueryPrivate() {
        return queryPrivate;
    }

    public void setQueryPrivate(boolean queryPrivate) {
        this.queryPrivate = queryPrivate;
    }

    public boolean isQueryPasswordProtect() {
        return queryPasswordProtect;
    }

    public void setQueryPasswordProtect(boolean queryPasswordProtect) {
        this.queryPasswordProtect = queryPasswordProtect;
    }

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

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public HandledArticlePageQueryParam(ArticlePageQueryParam queryParam, Integer categoryId, Integer tagId) {
        super();
        this.pageQueryParam = queryParam;
        if(categoryId != null) {
            this.category = new Category();
            category.setId(categoryId);
        }
        this.query = queryParam.getQuery();
        this.status = queryParam.getStatus();
        if(tag != null){
            this.tag = new Tag(tagId);
        }
    }

    public HandledArticlePageQueryParam() {
        super();
    }

}
