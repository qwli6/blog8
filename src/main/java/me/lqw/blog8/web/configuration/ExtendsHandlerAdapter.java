package me.lqw.blog8.web.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义处理器适配器
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class ExtendsHandlerAdapter implements HandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    private final Map<String, String> urlDataMap;

    public ExtendsHandlerAdapter(Map<String, String> urlDataMa) {
        this.urlDataMap = urlDataMa;
    }

    @Override
    public boolean supports(@NonNull Object handler) {
        return handler instanceof String && "template".equals(handler);
    }

    @Override
    public ModelAndView handle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        logger.info("ExtendsHandlerAdapter handle");

        return null;
    }

    @Override
    public long getLastModified(@NonNull HttpServletRequest request, @NonNull Object handler) {
        return -1;
    }
}
