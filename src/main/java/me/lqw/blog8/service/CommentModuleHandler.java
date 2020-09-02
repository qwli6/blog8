package me.lqw.blog8.service;

import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.model.Comment;
import me.lqw.blog8.model.CommentModule;

/**
 * 评论模块处理器
 *
 * @param <T> T
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public interface CommentModuleHandler<T> {

    /**
     * 获取模块名称
     *
     * @return String
     */
    String getModuleName();

    /**
     * 查询之前检查
     *
     * @param commentModule commentModule
     * @return T
     * @throws LogicException 逻辑异常
     */
    T checkBeforeQuery(CommentModule commentModule) throws LogicException;

    /**
     * 插入之前检查
     *
     * @param comment       comment
     * @param commentModule commentModule
     * @throws LogicException 逻辑异常
     */
    void checkBeforeSaved(Comment comment, CommentModule commentModule) throws LogicException;

    /**
     * 增加评论
     *
     * @param module module
     * @throws LogicException 逻辑异常
     */
    void increaseComments(CommentModule module) throws LogicException;
}
