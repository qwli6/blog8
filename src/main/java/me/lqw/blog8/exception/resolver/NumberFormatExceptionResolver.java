package me.lqw.blog8.exception.resolver;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 格式转换异常
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class NumberFormatExceptionResolver implements ExceptionResolver {
    @Override
    public boolean match(Exception ex) {
        return ex instanceof NumberFormatException;
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
