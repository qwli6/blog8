package me.lqw.blog8.exception.resolver;

import me.lqw.blog8.exception.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 未授权异常
 *  在未登录的情况下，访问授权资源抛出的异常
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class UnauthorizedHandlerExceptionResolver implements ExceptionResolver {

    @Override
    public boolean match(Exception ex) {
        return ex instanceof UnauthorizedException;
    }

    @Override
    public Map<String, Object> readErrors(HttpServletRequest request, Exception ex) {
        return Collections.singletonMap("errors", ((UnauthorizedException) ex).getError());
    }

    @Override
    public int getStatus(HttpServletRequest request, Exception ex) {
        return HttpServletResponse.SC_UNAUTHORIZED;
    }
}