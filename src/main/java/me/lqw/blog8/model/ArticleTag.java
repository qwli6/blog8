package me.lqw.blog8.model;

import java.io.Serializable;

public class ArticleTag implements Serializable {

    private Article article;

    private Tag tag;

    public ArticleTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }

    public ArticleTag() {
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
