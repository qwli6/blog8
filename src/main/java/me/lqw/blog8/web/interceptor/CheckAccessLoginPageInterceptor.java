package me.lqw.blog8.web.interceptor;

import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.constants.BlogContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * 在什么情况下访问登录页面
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class CheckAccessLoginPageInterceptor extends AbstractBlogInterceptor {

    /**
     * 记录日志
     */
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {

        if(!(handler instanceof HandlerMethod)){
            //非 HandlerMethod
            return true;
        }

        String requestURI = request.getRequestURI();

        logger.info("CheckAccessLoginPageInterceptor requestURI: [{}]", requestURI);

        if(BlogConstants.LOGIN_URI.equals(requestURI) && BlogContext.isAuthorized()){
            //在登录的情况下访问登录页面，显然不被允许
            response.sendRedirect(BlogConstants.INDEX_URI);
        }

        return true;
    }

    /**
     * 匹配路径
     * @return List<String>
     */
    @Override
    public List<String> matchPatterns() {
        return Collections.singletonList("/**");
    }
}
