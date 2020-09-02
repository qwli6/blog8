package me.lqw.blog8.web.configuration;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义处理器映射器
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class ExtendSimpleUrlHandlerMapping implements HandlerMapping {
    @Override
    public HandlerExecutionChain getHandler(@NonNull HttpServletRequest request) throws Exception {
        return null;
    }
}
