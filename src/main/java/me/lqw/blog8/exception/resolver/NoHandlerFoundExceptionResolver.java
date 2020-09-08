package me.lqw.blog8.exception.resolver;

import me.lqw.blog8.constants.BlogConstants;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 没有发现处理器异常
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 * 需要在 application.yml 中添加配置  spring.mvc.throw-exception-if-no-handler-found: true
 */
public class NoHandlerFoundExceptionResolver implements ExceptionResolver {
    @Override
    public boolean match(Exception ex) {
        return ex instanceof NoHandlerFoundException;
    }

    @Override
    public Map<String, Object> readErrors(HttpServletRequest request, Exception ex) {
        return Collections.singletonMap("errors", BlogConstants.NO_HANDLER_FOUND);
    }

    @Override
    public int getStatus(HttpServletRequest request, Exception ex) {
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
