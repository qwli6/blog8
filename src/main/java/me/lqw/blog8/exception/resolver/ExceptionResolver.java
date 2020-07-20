package me.lqw.blog8.exception.resolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 异常解析接口
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public interface ExceptionResolver {
    boolean match(Exception ex);

    Map<String, Object> readErrors(HttpServletRequest request, Exception ex);

    int getStatus(HttpServletRequest request, Exception ex);
}