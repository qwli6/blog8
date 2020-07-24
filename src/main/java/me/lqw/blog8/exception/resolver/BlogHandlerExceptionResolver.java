package me.lqw.blog8.exception.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义博客系统异常解析器
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class BlogHandlerExceptionResolver implements HandlerExceptionResolver, ErrorAttributes {

    private static final String ERROR_ATTRIBUTES = BlogHandlerExceptionResolver.class.getName() + ".ERROR_ATTRIBUTE";

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final List<ExceptionResolver> resolvers = new ArrayList<>();

    public BlogHandlerExceptionResolver() {
        resolvers.add(new ResourceNotFoundHandlerExceptionResolver());
        resolvers.add(new UnauthorizedHandlerExceptionResolver());
        resolvers.add(new LogicHandlerExceptionResolver());

        resolvers.add(new HttpMethodNotAllowedExceptionResolver());
        resolvers.add(new HttpMediaTypeNotSupportExceptionResolver());
        resolvers.add(new IllegalArgumentExceptionResolver());
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {

        if (resolve(request, response, ex)) {

            return new ModelAndView();
        }

//        String requestURI = request.getRequestURI();
//        logger.error("BlogHandlerExceptionResolver#resolveException requestURI:[{}]", requestURI);
//
//        if(ex instanceof ResourceNotFoundException){
//            ResourceNotFoundException resourceNotFoundException = (ResourceNotFoundException) ex;
//
//            Message error = resourceNotFoundException.getError();
//
//            ModelAndView mav = new ModelAndView("page_error");
//            mav.addObject("error", error);
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            return mav;
//        }

        return null;
    }

    @Override
    public Throwable getError(WebRequest webRequest) {
        return null;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        // Let Spring handle the error first, we will modify later :)
//        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Map<String, Object> errorAttributes = (Map<String, Object>) webRequest.getAttribute(ERROR_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);

        //format & update timestamp
//        Object timestamp = errorAttributes.get("timestamp");
//        if(timestamp == null){
//            errorAttributes.put("timestamp", dateFormat.format(new Date()));
//        } else {
//            errorAttributes.put("timestamp", dateFormat.format((Date) timestamp));
//        }
        // insert a new key

        Assert.notNull(errorAttributes, "System exception may be empty.");

        Assert.notEmpty(errorAttributes, "System exception may be missing.");

        errorAttributes.put("version", "1.2");
        return errorAttributes;
    }

    public boolean resolve(HttpServletRequest request, HttpServletResponse response, Exception ex){
        request.removeAttribute(ERROR_ATTRIBUTES);
        for(ExceptionResolver exceptionResolver : resolvers){
            if(!exceptionResolver.match(ex)){
                continue;
            }
            int status = exceptionResolver.getStatus(request, ex);
            Map<String, Object> errors = exceptionResolver.readErrors(request, ex);
            request.setAttribute(ERROR_ATTRIBUTES, errors);
            try{
                response.sendError(status);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
            return true;
        }

        return false;
    }


}
