package me.lqw.blog8.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Component
public class BlogHandlerExceptionResolver implements HandlerExceptionResolver, ErrorAttributes {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public BlogHandlerExceptionResolver() {
        List<ExceptionResolver> resolvers = new ArrayList<>();
        resolvers.add(new ResourceNotFoundHandlerExceptionResolver());
        resolvers.add(new UnauthorizedHandlerExceptionResolver());
        resolvers.add(new LogicHandlerExceptionResolver());
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {

        String requestURI = request.getRequestURI();
        logger.error("BlogHandlerExceptionResolver#resolveException requestURI:[{}]", requestURI);

        if(ex instanceof ResourceNotFoundException){
            ResourceNotFoundException resourceNotFoundException = (ResourceNotFoundException) ex;

            Message error = resourceNotFoundException.getError();

            ModelAndView mav = new ModelAndView("page_error");
            mav.addObject("error", error);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return mav;
        }

        return null;
    }

    @Override
    public Throwable getError(WebRequest webRequest) {
        return null;
    }


    static class ResourceNotFoundHandlerExceptionResolver implements ExceptionResolver {

        @Override
        public boolean match(Exception ex) {
            return ex instanceof ResourceNotFoundException;
        }

        @Override
        public Map<String, Object> readErrors(HttpServletRequest request, Exception ex) {
            return Collections.singletonMap("errors", ((ResourceNotFoundException) ex).getError());
        }

        @Override
        public int getStatus(HttpServletRequest request, Exception ex) {
            return HttpServletResponse.SC_NOT_FOUND;
        }
    }


    static class LogicHandlerExceptionResolver implements ExceptionResolver {

        @Override
        public boolean match(Exception ex) {
            return ex instanceof LogicException;
        }

        @Override
        public Map<String, Object> readErrors(HttpServletRequest request, Exception ex) {
            return Collections.singletonMap("errors", ((ResourceNotFoundException) ex).getError());
        }

        @Override
        public int getStatus(HttpServletRequest request, Exception ex) {
            return HttpServletResponse.SC_BAD_REQUEST;
        }
    }


    static class UnauthorizedHandlerExceptionResolver implements ExceptionResolver {

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

    interface ExceptionResolver {
        boolean match(Exception ex);

        Map<String, Object> readErrors(HttpServletRequest request, Exception ex);

        int getStatus(HttpServletRequest request, Exception ex);
    }
}
