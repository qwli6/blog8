package me.lqw.blog8.model;

import java.io.Serializable;

/**
 * 后台首页数据统计
 *
 * @author liqiwen
 * @version 1.0
 * 2020-07-20 09:48:07
 * @since 1.2
 */
public class DataTotalStatistics implements Serializable {

    /**
     * 内容总数
     */
    private Integer articleCount;

    /**
     * 动态总数
     */
    private Integer momentCount;

    /**
     * 评论总数
     */
    private Integer commentCount;

    /**
     * 点击量
     */
    private Integer hitCount;

    /**
     * 构造函数
     */
    public DataTotalStatistics() {
        super();
    }

    /**
     * 构造函数
     *
     * @param articleCount articleCount
     * @param momentCount  momentCount
     * @param commentCount commentCount
     * @param hitCount     hitCount
     */
    public DataTotalStatistics(Integer articleCount, Integer momentCount, Integer commentCount, Integer hitCount) {
        super();
        this.articleCount = articleCount;
        this.momentCount = momentCount;
        this.commentCount = commentCount;
        this.hitCount = hitCount;
    }

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
