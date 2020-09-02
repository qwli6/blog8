package me.lqw.blog8.event;

import org.springframework.context.ApplicationEvent;

/**
 * 基础事件
 *
 * @param <T> T
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public abstract class AbstractBlogEvent<T> extends ApplicationEvent {

    /**
     * create new Object
     */
    private T t;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public AbstractBlogEvent(Object source) {
        super(source);
    }

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     * @param t      t
     */
    public AbstractBlogEvent(Object source, T t) {
        super(source);
        this.t = t;
    }

    /**
     * get current object
     *
     * @return T
     */
    public T getObject() {
        return t;
    }
}
