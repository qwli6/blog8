package me.lqw.blog8.service;

import me.lqw.blog8.exception.AbstractBlogException;
import me.lqw.blog8.exception.LogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 抽象 Service
 *
 * @param <T> T
 * @version 1.2
 * @since 1.2
 */
public abstract class AbstractBaseService<T> {

    /**
     * 日志记录
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 保存
     *
     * @param t t
     * @return T
     * @throws LogicException 逻辑异常
     */
    public abstract T save(T t) throws AbstractBlogException;

    /**
     * 删除方法
     *
     * @param id id
     * @throws LogicException 逻辑异常
     */
    public abstract void delete(Integer id) throws AbstractBlogException;

    /**
     * 更新
     *
     * @param t t
     * @throws LogicException 逻辑异常
     */
    public abstract void update(T t) throws AbstractBlogException;
}
