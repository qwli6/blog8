package me.lqw.blog8.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import me.lqw.blog8.model.enums.CommentStatus;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论内容
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class Comment implements Serializable {

    /**
     * 评论 id
     */
    private Integer id;

    /**
     * 评论内容
     */
    @NotBlank(message = "请提供评论内容")
    @Length(max = 1024, min = 0, message = "评论内容长度必须在 {min}~{max} 之间")
    private String content;

    /**
     * 评论创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @FutureOrPresent(message = "评论的创建时间必须是一个未来时间")
    private LocalDateTime createAt;


    /**
     * 评论修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @FutureOrPresent(message = "评论的修改时间必须是一个未来时间")
    @JsonIgnore
    private LocalDateTime modifyAt;

    /**
     * 评论邮箱
     */
    @Length(max = 64, message = "邮箱的长度不能超过 {max} 个字符")
    @Email(message = "请提供一个有效的邮箱地址")
    @NotBlank(message = "用户邮箱不能为空")
    private String email;

    /**
     * 评论人名称
     */
    @NotBlank(message = "评论人昵称不能为空")
    @Length(message = "用户的长度必须在 {min}~{max} 之间", max = 16, min = 1)
    private String username;

    /**
     * 评论网站
     */
    @URL(message = "请提供一个有效的网站地址")
    @Length(max = 64, min = 0, message = "用户的网站地址长度必须在 {min}~{max} 之间")
    private String website;

    /**
     * 是否管理员评论
     */
    private Boolean admin;

    /**
     * 评论邮箱 md5，如果没有邮箱，则默认为默认头像
     */
    private String avatar;

    /**
     * 评论地址的深度，最大 255，不建议太深
     */
    private String path;

    /**
     * 上一级父评论
     */
    private Comment parent;

    /**
     * 评论模块
     */
    private CommentModule module;

    /**
     * 评论状态
     */
    private CommentStatus status;

    /**
     * 评论人 ip
     */
    private String ip;

    /**
     * 根据 ip 解析出来的地址
     */
    private String address;

    /**
     * 评论客户端信息
     */
    private String fromClient;

    /**
     * 构造方法
     */
    public Comment() {
        super();
    }

    /**
     * 构造方法
     *
     * @param id id
     */
    public Comment(Integer id) {
        super();
        this.id = id;
    }

    /**
     * 构造方法
     *
     * @param content  content
     * @param email    email
     * @param username username
     * @param website  website
     */
    public Comment(@NotBlank(message = "请提供评论内容") @Length(max = 1024, min = 0, message = "评论内容长度必须在 {min}~{max} 之间") String content, @Length(max = 64, message = "邮箱的长度不能超过 {max} 个字符") @Email(message = "请提供一个有效的邮箱地址") @NotBlank(message = "用户邮箱不能为空") String email, @NotBlank(message = "评论人昵称不能为空") @Length(message = "用户的长度必须在 {min}~{max} 之间", max = 16, min = 1) String username, @URL(message = "请提供一个有效的网站地址") @Length(max = 64, min = 0, message = "用户的网站地址长度必须在 {min}~{max} 之间") String website) {
        this.content = content;
        this.email = email;
        this.username = username;
        this.website = website;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFromClient() {
        return fromClient;
    }

    public void setFromClient(String fromClient) {
        this.fromClient = fromClient;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public CommentModule getModule() {
        return module;
    }

    public void setModule(CommentModule module) {
        this.module = module;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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
