package me.lqw.blog8.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import me.lqw.blog8.model.enums.ArticleStatusEnum;
import me.lqw.blog8.validator.ArticleStatus;
import me.lqw.blog8.validator.UrlName;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 文章实体类
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class Article implements Serializable {

    /**
     * 文章 id
     */
    private Integer id;

    /**
     * 文章标题
     */
    @NotBlank(message = "请提供文章标题")
    @Length(max = 128, min = 0, message = "文章标题的最大长度必须在 {min}~{max} 之间")
    private String title;

    /**
     * 文章内容
     */
    @NotEmpty(message = "请提供文章内容")
    @Length(max = 10240, min = 0, message = "文章内容的最大长度必须在 {min}~{max} 之间")
    private String content;

    /**
     * 文章别名
     */
    @Length(max = 16, min = 0, message = "文章别名长度不能必须在 {min}~{max} 之间")
    @UrlName(message = "别名格式错误, 不能包含空格, 斜杠等字符")
    private String urlName;

    /**
     * 文章创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "文章的创建时间必须是一个将来的时间")
    private LocalDateTime createAt;

    /**
     * 文章修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "文章的修改时间必须是一个将来的时间")
    private LocalDateTime modifyAt;

    /**
     * 文章发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "文章的发布时间必须是一个将来的时间")
    private LocalDateTime postAt;

    /**
     * 是否允许评论
     */
    @NotNull(message = "是否允许评论不允许为空")
    private Boolean allowComment;

    /**
     * 文章绑定分类
     */
    @NotNull(message = "文章绑定分类不允许为空")
    private Category category;

    /**
     * 文章特征图像
     */
    private String featureImage;

    /**
     * 文章摘要长度
     */
    @Length(max = 1024, min = 0, message = "文章摘要内容长度不能超过 {min}~{max} 个字符")
    private String digest;

    /**
     * 文章发布状态
     */
    @NotNull(message = "请提供一个文章状态")
    @ArticleStatus(message = "请提供一个合法的文章状态")
    private ArticleStatusEnum status;

    /**
     * 浏览数
     */
    @Min(value = 0, message = "文章的点击数数量必须大于 0")
    private Integer hits = 0;

    /**
     * 评论数
     */
    @Min(value = 0, message = "文章的评论数数量必须大于 0")
    private Integer comments = 0;

    /**
     * 文章关联标签
     */
    private Set<Tag> tags = new LinkedHashSet<>();

    /**
     * 文章构造方法
     */
    public Article() {
        super();
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public ArticleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ArticleStatusEnum status) {
        this.status = status;
    }

    public String getFeatureImage() {
        return featureImage;
    }

    public void setFeatureImage(String featureImage) {
        this.featureImage = featureImage;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public LocalDateTime getPostAt() {
        return postAt;
    }

    public void setPostAt(LocalDateTime postAt) {
        this.postAt = postAt;
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

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }
}
