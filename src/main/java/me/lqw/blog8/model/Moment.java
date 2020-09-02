package me.lqw.blog8.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 动态
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class Moment implements Serializable {

    /**
     * 动态 id
     */
    private Integer id;

    /**
     * 动态内容
     */
    @NotBlank(message = "请添加动态内容")
    @Length(max = 1024, message = "动态内容长度不能超过 {max} 个字符")
    private String content;

    /**
     * 动态点击量
     */
    @Min(value = 0, message = "动态内容的点击量最小为 0")
    private Integer hits;

    /**
     * 动态评论量
     */
    @Min(value = 0, message = "动态内容的评论量最小为 0")
    private Integer comments;

    /**
     * 是否允许评论
     * 默认为 true
     */
    private Boolean allowComment = true;

    /**
     * 特征图像
     */
    private String featureImage;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @FutureOrPresent(message = "动态创建时间必须是一个将来的时间")
    private LocalDateTime createAt;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @FutureOrPresent(message = "动态修改时间必须是一个将来的时间")
    private LocalDateTime modifyAt;


    public String getFeatureImage() {
        return featureImage;
    }

    public void setFeatureImage(String featureImage) {
        this.featureImage = featureImage;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }
}
