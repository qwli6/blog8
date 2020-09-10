package me.lqw.blog8.model.dto;

import me.lqw.blog8.model.Moment;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 动态 DTO
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class MomentDTO implements Serializable {

    private String content;

    private int hits;

    private int comments;

    private LocalDateTime createAt;

    private String featureImage;


    public MomentDTO() {
        super();
    }

    public MomentDTO(Moment moment){
        this.content = moment.getContent();
        this.hits = moment.getHits();
        this.comments = moment.getComments();
        this.createAt = moment.getCreateAt();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getFeatureImage() {
        return featureImage;
    }

    public void setFeatureImage(String featureImage) {
        this.featureImage = featureImage;
    }
}
