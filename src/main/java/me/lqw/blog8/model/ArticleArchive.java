package me.lqw.blog8.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ArticleArchive implements Serializable {

    private LocalDate date;

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
