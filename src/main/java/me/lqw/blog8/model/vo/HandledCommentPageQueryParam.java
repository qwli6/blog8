package me.lqw.blog8.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.enums.CommentStatus;
import me.lqw.blog8.util.StringUtil;

import java.io.Serializable;

/**
 * 真正的评论查询入参
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public class HandledCommentPageQueryParam extends AbstractQueryParam implements Serializable {

    /**
     * 评论模块
     */
    @JsonIgnore
    private CommentModule commentModule;

    /**
     * 评论状态
     */
    private CommentStatus commentStatus;

    /**
     * 构造方法
     */
    public HandledCommentPageQueryParam() {
        super();
    }

    /**
     * 构造方法
     *
     * @param queryParam queryParam
     */
    public HandledCommentPageQueryParam(CommentPageQueryParam queryParam) {
        this.setCurrentPage(queryParam.getCurrentPage());
        this.setPageSize(queryParam.getPageSize());
        this.setIgnorePaging(queryParam.isIgnorePaging());
        this.setOffset(queryParam.getOffset());

        CommentModule commentModule = new CommentModule();
        if(queryParam.getModuleId() != null){
            commentModule.setId(Integer.parseInt(queryParam.getModuleId()));
        }
        if(StringUtil.isNotBlank(queryParam.getModuleName())){
            commentModule.setName(queryParam.getModuleName());
        }

        this.setCommentModule(commentModule);
        this.commentStatus = queryParam.getStatus();
    }

    public CommentModule getCommentModule() {
        return commentModule;
    }

    public void setCommentModule(CommentModule commentModule) {
        this.commentModule = commentModule;
    }

    public CommentStatus getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(CommentStatus commentStatus) {
        this.commentStatus = commentStatus;
    }
}
