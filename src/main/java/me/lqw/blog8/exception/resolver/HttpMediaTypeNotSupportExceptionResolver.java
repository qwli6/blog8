package me.lqw.blog8.exception.resolver;

import org.springframework.web.HttpMediaTypeNotSupportedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * 媒体类型不支持异常
 * @author liqiwen
 * @version 1.0
 * @since 1.2
 */
public class HttpMediaTypeNotSupportExceptionResolver implements ExceptionResolver {
    @Override
    public boolean match(Exception ex) {
        return ex instanceof HttpMediaTypeNotSupportedException;
    }

    @Override
    public Map<String, Object> readErrors(HttpServletRequest request, Exception ex) {
        return Collections.emptyMap();
    }

    @Override
    public int getStatus(HttpServletRequest request, Exception ex) {
        return HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
    }
}
