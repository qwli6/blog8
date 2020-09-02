package me.lqw.blog8.model.vo;

import me.lqw.blog8.model.enums.CommentStatus;

import java.io.Serializable;

/**
 * 分页查询评论入参
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class CommentPageQueryParam extends AbstractQueryParam implements Serializable {

    /**
     * 评论状态
     */
    private CommentStatus status;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 模块 id
     */
    private String moduleId;


    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }
}
