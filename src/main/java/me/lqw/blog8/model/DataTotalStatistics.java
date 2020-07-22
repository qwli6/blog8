package me.lqw.blog8.model;

import java.io.Serializable;

/**
 * 后台首页数据统计
 * @author liqiwen
 * @version 1.0
 * 2020-07-20 09:48:07
 * @since 1.2
 */
public class DataTotalStatistics implements Serializable {

    private Integer articleCount;

    private Integer momentCount;

    private Integer commentCount;

    private Integer hitCount;


    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    public Integer getMomentCount() {
        return momentCount;
    }

    public void setMomentCount(Integer momentCount) {
        this.momentCount = momentCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getHitCount() {
        return hitCount;
    }

    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }
}
