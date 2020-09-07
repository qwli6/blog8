package me.lqw.blog8.model.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import me.lqw.blog8.model.Comment;
import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.enums.CommentStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 返回给前端的内容, 屏蔽掉部分内容
 *
 * @author liqiwen
 * @version 1.3
 * @since 1.3
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO implements Serializable {

    /**
     * 评论 id
     */
    private Integer id;

    /**
     * 评论昵称
     */
    private String nickname;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createTime;

    /**
     * 评论模块
     */
    private CommentModule module;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论邮箱
     */
    private String email;

    /**
     * 头像 hash
     */
    private String avatar;

    /**
     * 是否管理员
     */
    private Boolean admin;

    /**
     * 评论深度
     */
    private String parentPath;

    /**
     * 审核状态
     */
    private Boolean checking;

    /**
     * 父评论
     */
    private CommentDTO parent;

    /**
     * 构造方法
     */
    public CommentDTO() {
        super();
    }

    /**
     * 构造方法
     *
     * @param comment comment
     */
    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.createTime = comment.getCreateAt();
        this.module = comment.getModule();
        this.content = comment.getContent();
        this.email = comment.getEmail();
        this.nickname = comment.getUsername();
        this.avatar = comment.getAvatar();
        this.admin = comment.getAdmin();
        this.parentPath = comment.getPath();

        Comment parent = comment.getParent();
        if(parent != null){
            CommentDTO _parent = new CommentDTO();
            _parent.setId(parent.getId());

            this.parent = _parent;
        }
        this.checking = comment.getStatus() == CommentStatus.WAIT_CHECK;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public CommentModule getModule() {
        return module;
    }

    public void setModule(CommentModule module) {
        this.module = module;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public Boolean getChecking() {
        return checking;
    }

    public void setChecking(Boolean checking) {
        this.checking = checking;
    }

    public CommentDTO getParent() {
        return parent;
    }

    public void setParent(CommentDTO parent) {
        this.parent = parent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
