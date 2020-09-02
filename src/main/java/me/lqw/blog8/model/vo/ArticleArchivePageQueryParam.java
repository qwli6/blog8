package me.lqw.blog8.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import me.lqw.blog8.model.enums.ArticleStatusEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 文章归档查询参数
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class ArticleArchivePageQueryParam extends AbstractQueryParam implements Serializable {

    /**
     * 是否归档私人内容
     */
    private boolean queryPrivate;

    /**
     * 是否归档已经密码保护的内容
     */
    private boolean queryPasswordProtect;

    /**
     * 归档状态
     */
    private ArticleStatusEnum status;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private Boolean begin;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private Boolean end;


    public boolean isQueryPasswordProtect() {
        return queryPasswordProtect;
    }

    public void setQueryPasswordProtect(boolean queryPasswordProtect) {
        this.queryPasswordProtect = queryPasswordProtect;
    }

    public ArticleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ArticleStatusEnum status) {
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
