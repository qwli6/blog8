package me.lqw.blog8.exception.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 系统非法参数异常
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class IllegalArgumentExceptionResolver implements ExceptionResolver {

    /**
     * 是否匹配异常
     *
     * @param ex ex
     * @return false | true
     */
    @Override
    public boolean match(Exception ex) {
        return ex instanceof IllegalArgumentException;
    }

    /**
     * 从异常中读取错误信息
     *
     * @param request request
     * @param ex      ex
     * @return Map
     */
    @Override
    public Map<String, Object> readErrors(HttpServletRequest request, Exception ex) {

        IllegalArgumentException argumentException = (IllegalArgumentException) ex;
        String message = argumentException.getMessage();

        return Collections.emptyMap();
    }

    /**
     * 异常状态码
     *
     * @param request request
     * @param ex      ex
     * @return HttpStatus
     */
    @Override
    public int getStatus(HttpServletRequest request, Exception ex) {
        return HttpServletResponse.SC_CONFLICT;
    }
}
