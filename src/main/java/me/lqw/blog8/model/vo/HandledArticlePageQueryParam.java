package me.lqw.blog8.model.vo;

import me.lqw.blog8.model.Category;
import me.lqw.blog8.model.Tag;
import me.lqw.blog8.model.enums.ArticleStatusEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章内容查询参数
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class HandledArticlePageQueryParam extends AbstractQueryParam implements Serializable {

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
    private AbstractQueryParam abstractQueryParam;

    /**
     * 开始查询时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime begin;

    /**
     * 结束查询时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;

    /**
     * 文章 id，从 lucene 根据 query/category/tag 获取文章列表，
     * 查询出文章的 id，然后进行检索
     * <p>
     * 返给前端的时候，忽略这个字段
     */
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
    private ArticleStatusEnum status;

    /**
     * 构造方法
     */
    public HandledArticlePageQueryParam() {
        super();
    }

    /**
     * 构造方法
     *
     * @param queryParam queryParam
     * @param categoryId categoryId
     * @param tagId      tagId
     */
    public HandledArticlePageQueryParam(ArticlePageQueryParam queryParam, Integer categoryId, Integer tagId) {
        super();
        this.abstractQueryParam = queryParam;
        if (categoryId != null) {
            this.category = new Category(categoryId);
        }
        this.query = queryParam.getQuery();
        this.status = queryParam.getStatus();
        this.setCurrentPage(queryParam.getCurrentPage());
        this.setPageSize(queryParam.getPageSize());
        this.setOffset(queryParam.getOffset());

        if (tag != null) {
            this.tag = new Tag(tagId);
        }

        if (queryParam.getBegin() != null) {
            this.begin = queryParam.getBegin();
        }

        if (queryParam.getEnd() != null) {
            this.end = queryParam.getEnd();
        }
    }

    public AbstractQueryParam getPageQueryParam() {
        return abstractQueryParam;
    }

    public void setPageQueryParam(AbstractQueryParam abstractQueryParam) {
        this.abstractQueryParam = abstractQueryParam;
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

    public ArticleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ArticleStatusEnum status) {
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

}
