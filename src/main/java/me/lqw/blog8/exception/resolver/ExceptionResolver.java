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

    /**
     * 异常是否匹配
     * @param ex ex
     * @return boolean
     */
    boolean match(Exception ex);

    /**
     * 从异常中读取错误信息
     * @param request request
     * @param ex ex
     * @return Map
     */
    Map<String, Object> readErrors(HttpServletRequest request, Exception ex);

    /**
     * 获取异常的状态码
     * @param request request
     * @param ex ex
     * @return int
     */
    int getStatus(HttpServletRequest request, Exception ex);
}