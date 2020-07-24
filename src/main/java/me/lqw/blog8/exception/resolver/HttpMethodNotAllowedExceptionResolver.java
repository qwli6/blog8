package me.lqw.blog8.exception.resolver;

import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 方法不支持异常
 * @author liqiwen
 * @version 1.0
 * @since 1.2
 */
public class HttpMethodNotAllowedExceptionResolver implements ExceptionResolver {
    @Override
    public boolean match(Exception ex) {
        return ex instanceof HttpRequestMethodNotSupportedException;
    }

    @Override
    public Map<String, Object> readErrors(HttpServletRequest request, Exception ex) {
        return Collections.emptyMap();
    }

    @Override
    public int getStatus(HttpServletRequest request, Exception ex) {
        return HttpServletResponse.SC_METHOD_NOT_ALLOWED;
    }
}
