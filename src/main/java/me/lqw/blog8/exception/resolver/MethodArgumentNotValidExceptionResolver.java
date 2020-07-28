package me.lqw.blog8.exception.resolver;

import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 方法参数未校验异常
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class MethodArgumentNotValidExceptionResolver implements ExceptionResolver {
    @Override
    public boolean match(Exception ex) {
        return ex instanceof MethodArgumentNotValidException;
    }

    @Override
    public Map<String, Object> readErrors(HttpServletRequest request, Exception ex) {
        return Collections.emptyMap();
    }

    @Override
    public int getStatus(HttpServletRequest request, Exception ex) {
        return HttpServletResponse.SC_BAD_REQUEST;
    }
}
