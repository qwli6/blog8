package me.lqw.blog8.model;

import java.io.Serializable;

/**
 * 文章标签关联关系
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class ArticleTag implements Serializable {

    /**
     * 文章
     */
    private Article article;

    /**
     * 标签
     */
    private Tag tag;

    /**
     * 构造方法
     *
     * @param article article
     * @param tag     tag
     */
    public ArticleTag(Article article, Tag tag) {
        super();
        this.article = article;
        this.tag = tag;
    }

    /**
     * 构造方法
     */
    public ArticleTag() {
        super();
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
