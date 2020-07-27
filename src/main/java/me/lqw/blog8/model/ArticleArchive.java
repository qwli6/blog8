package me.lqw.blog8.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 文章归档
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class ArticleArchive implements Serializable {

    /**
     * 归档日期
     */
    private LocalDate date;

    /**
     * 归档列表
     */
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
