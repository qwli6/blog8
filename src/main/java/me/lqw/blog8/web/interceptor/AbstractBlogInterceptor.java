package me.lqw.blog8.web.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.Collections;
import java.util.List;

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

    /**
     * 匹配拦截路径
     * @return List
     */
    public abstract List<String> matchPatterns();
}
