package me.lqw.blog8.exception.resolver;

import me.lqw.blog8.exception.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 访问不存在的资源抛出的异常
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class ResourceNotFoundHandlerExceptionResolver implements ExceptionResolver {

    @Override
    public boolean match(Exception ex) {
        return ex instanceof ResourceNotFoundException;
    }

    @Override
    public Map<String, Object> readErrors(HttpServletRequest request, Exception ex) {
        return Collections.singletonMap("errors", ((ResourceNotFoundException) ex).getError());
    }

    @Override
    public int getStatus(HttpServletRequest request, Exception ex) {
        return HttpServletResponse.SC_NOT_FOUND;
    }
}