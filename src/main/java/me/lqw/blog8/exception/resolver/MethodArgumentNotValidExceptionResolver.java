package me.lqw.blog8.exception.resolver;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 方法参数未校验异常
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class MethodArgumentNotValidExceptionResolver implements ExceptionResolver {

    /**
     * 是否匹配该异常
     *
     * @param ex ex
     * @return false | true
     */
    @Override
    public boolean match(Exception ex) {
        return ex instanceof MethodArgumentNotValidException;
    }

    /**
     * 从异常中读取错误
     *
     * @param request request
     * @param ex      ex
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> readErrors(HttpServletRequest request, Exception ex) {
        MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();

        if(!CollectionUtils.isEmpty(allErrors)){

        }

        for (ObjectError objectError : allErrors) {
//            objectError.get

        }

        return Collections.singletonMap("errors", "ip 错误");
    }

    /**
     * 获取校验状态, 返回 httpStatus
     *
     * @param request request
     * @param ex      ex
     * @return int
     */
    @Override
    public int getStatus(HttpServletRequest request, Exception ex) {
        return HttpServletResponse.SC_BAD_REQUEST;
    }
}
