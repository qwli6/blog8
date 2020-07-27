package me.lqw.blog8.model.vo;

import me.lqw.blog8.validator.StatusEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 文章归档查询参数
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class ArticleArchivePageQueryParam extends PageQueryParam implements Serializable {

    /**
     * 是否归档私人内容
     */
    private boolean queryPrivate;


    /**
     * 归档状态
     */
    private StatusEnum status;


    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Boolean begin;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Boolean end;


    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public boolean isQueryPrivate() {
        return queryPrivate;
    }

    public void setQueryPrivate(boolean queryPrivate) {
        this.queryPrivate = queryPrivate;
    }

    public Boolean getBegin() {
        return begin;
    }

    public void setBegin(Boolean begin) {
        this.begin = begin;
    }

    public Boolean getEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }
}
