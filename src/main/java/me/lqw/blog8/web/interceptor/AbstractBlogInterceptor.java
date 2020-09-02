package me.lqw.blog8.web.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 系统抽象拦截器
 * @author liqiwen
 * @since 1.2
 * @version 1.2
 */
public abstract class AbstractBlogInterceptor extends HandlerInterceptorAdapter {

    /**
     * 构造方法
     */
    public AbstractBlogInterceptor() {
        super();
    }
}
