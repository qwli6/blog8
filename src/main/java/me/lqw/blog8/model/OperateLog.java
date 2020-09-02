package me.lqw.blog8.model;

import me.lqw.blog8.model.enums.OperateType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public class OperateLog implements Serializable {

    /**
     * 操作日志 id
     */
    private Integer id;

    /**
     * 操作类型
     * <ol>
     *     <li>CREATE 创建</li>
     *     <li>U 修改</li>
     *     <li>D 删除</li>
     * </ol>
     */
    private OperateType type;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 操作日志产生的 ip
     */
    private String ip;

    /**
     * 操作人名称
     */
    private String username;

    /**
     * 日志操作的创建时间
     */
    private LocalDateTime createAt;

    /**
     * 日志操作的修改时间
     */
    private LocalDateTime modifyAt;

    /**
     * 构造方法
     */
    public OperateLog() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OperateType getType() {
        return type;
    }

    public void setType(OperateType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
