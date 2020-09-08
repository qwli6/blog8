package me.lqw.blog8.exception.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义博客系统异常解析器
 * 对一些可能出现的异常, 我们需要自己去解析, 不使用 SpringBoot 的默认返回
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
@Component
public class BlogHandlerExceptionResolver extends ResponseEntityExceptionHandler implements HandlerExceptionResolver, ErrorAttributes {

    /**
     * 错误 key
     */
    public static final String ERROR_ATTRIBUTES = BlogHandlerExceptionResolver.class.getName() + ".ERROR_ATTRIBUTE";

    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 异常解析列表
     */
    private final List<ExceptionResolver> resolvers = new ArrayList<>();

    /**
     * 构造方法
     */
    public BlogHandlerExceptionResolver() {
        //初始化全部的 ExceptionResolver
        resolvers.add(new NoHandlerFoundExceptionResolver());
        resolvers.add(new ResourceNotFoundExceptionResolver());
        resolvers.add(new UnauthorizedExceptionResolver());
        resolvers.add(new LogicExceptionResolver());
        resolvers.add(new HttpMethodNotAllowedExceptionResolver());
        resolvers.add(new HttpMediaTypeNotSupportExceptionResolver());
        resolvers.add(new IllegalArgumentExceptionResolver());
        resolvers.add(new NumberFormatExceptionResolver());
        resolvers.add(new MethodArgumentNotValidExceptionResolver());
    }

    @Override
    public ModelAndView resolveException(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                         Object handler, @NonNull Exception ex) {

//        logger.info("解析系统异常:[{}]", ex.getMessage(), ex);
        if (resolve(request, response, ex)) {
            return new ModelAndView();
        }
        return null;
    }

    /**
     * 渲染错误页面异常
     *
     * @param request  request
     * @param response response
     * @param ex       ex
     */
    public void resolveErrorPageException(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        request.removeAttribute(ERROR_ATTRIBUTES);
    }


    /**
     * 获取错误信息，默认不实现
     *
     * @param webRequest webRequest
     * @return Throwable
     */
    @Override
    public Throwable getError(WebRequest webRequest) {
        return null;
    }


    /**
     * 从 Request 对象中获取错误信息集合
     *
     * @param webRequest webRequest
     * @param options    options
     * @return Map<String, Object>
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        return (Map<String, Object>) webRequest.getAttribute(ERROR_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
    }


    /**
     * 解析异常
     *
     * @param request  request
     * @param response response
     * @param ex       ex
     * @return boolean
     */
    public boolean resolve(HttpServletRequest request, HttpServletResponse response, Exception ex) {

        //先移除上一次填充的错误信息
        request.removeAttribute(ERROR_ATTRIBUTES);

        //如果是我们自定义的异常解析
        for (ExceptionResolver exceptionResolver : resolvers) {
            if (!exceptionResolver.match(ex)) {
                continue;
            }
            int status = exceptionResolver.getStatus(request, ex);
            Map<String, Object> errors = exceptionResolver.readErrors(request, ex);
            //重新把我们的异常信息填充进去
            request.setAttribute(ERROR_ATTRIBUTES, errors);
            try {
                response.sendError(status);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return true;
        }
        return false;
    }

}
