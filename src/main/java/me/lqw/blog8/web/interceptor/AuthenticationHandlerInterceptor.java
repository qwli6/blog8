package me.lqw.blog8.web.interceptor;

import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.service.RememberMeService;
import me.lqw.blog8.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 授权认证拦截器
 * @author liqiwen
 * @since 1.2
 * @version 1.2
 */
public class AuthenticationHandlerInterceptor extends AbstractBlogInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 记住我
     */
    private final RememberMeService rememberMeService;

    /**
     * 构造方法
     * @param rememberMeService rememberMeService
     */
    public AuthenticationHandlerInterceptor(RememberMeService rememberMeService) {
        this.rememberMeService = rememberMeService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

        logger.info("认证授权请求拦截器执行");

        if(!(handler instanceof HandlerMethod)){
            return  true;
        }


        if(BlogContext.isAuthorized()){
            String requestURI = request.getRequestURI();
            //登录的情况下还在访问登录页，直接重定向到首页
            if(BlogConstants.LOGIN_URI.equals(requestURI)){
                response.sendRedirect("/");
            }
            return true;
        }

        //是否是授权请求 & 用户是否登录
        if (WebUtil.isAuthRequest(request) && !BlogContext.isAuthorized()) {

            //自动登录是否成功
            boolean flag = rememberMeService.autoLogin(request, response);


            logger.info("AuthenticationHandlerInterceptor auto login success:[{}}", flag);

            if (flag) {
                return true;
            }

            //获取请求地址的 uri
            request.setAttribute(BlogConstants.REDIRECT_URL_ATTRIBUTE,
                    ServletUriComponentsBuilder.fromRequest(request).build().toString());

            //设置无权访问
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

            // 不放行
            return false;
        }
        //放行
        return true;
    }


    /**
     * 匹配拦截路径
     * @return List
     */
    @Override
    public List<String> matchPatterns(){
        return Collections.singletonList("/console/**");
    }
}
