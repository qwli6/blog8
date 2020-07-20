package me.lqw.blog8.exception.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 系统非法参数异常
 * @author liqiwen
 * @version 1.0
 */
public class IllegalArgumentExceptionResolver implements ExceptionResolver {
    @Override
    public boolean match(Exception ex) {
        return ex instanceof IllegalArgumentException;
    }

    @Override
    public Map<String, Object> readErrors(HttpServletRequest request, Exception ex) {

        IllegalArgumentException argumentException = (IllegalArgumentException) ex;
        String message = argumentException.getMessage();


        return Collections.emptyMap();
    }

    @Override
    public int getStatus(HttpServletRequest request, Exception ex) {
        return HttpServletResponse.SC_CONFLICT;
    }
}
