package me.lqw.blog8.service;

import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.model.Comment;
import me.lqw.blog8.model.CommentModule;

public interface CommentModuleHandler<T> {

    String getModuleName();


    T checkBeforeQuery(CommentModule commentModule) throws LogicException;


    void checkBeforeSaved(Comment comment, CommentModule commentModule) throws LogicException;

    void increaseComments(CommentModule module) throws LogicException;
}
