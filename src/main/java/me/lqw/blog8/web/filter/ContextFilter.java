package me.lqw.blog8.web.filter;

import me.lqw.blog8.BlogConstants;
import me.lqw.blog8.BlogContext;
import me.lqw.blog8.model.User;
import me.lqw.blog8.model.config.BlogConfigModel;
import me.lqw.blog8.service.BlogConfigService;
import me.lqw.blog8.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 上下文过滤器
 * @author liqiwen
 * @version 1.0
 */
public class ContextFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final BlogConfigService blogConfigService;

    public ContextFilter(BlogConfigService blogConfigService) {
        this.blogConfigService = blogConfigService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        String ip = WebUtil.getIp((HttpServletRequest) request);

        BlogContext.setIp(ip);

        logger.info("ContextFilter#doFilter...");

        HttpSession session = ((HttpServletRequest) request).getSession();
        if(session == null){
            session = ((HttpServletRequest) request).getSession(true);
        }
        User user = (User) session.getAttribute(BlogConstants.AUTH_USER);
        BlogContext.AUTH_THREAD_LOCAL.set(user != null);

        logger.info("用户是否登录：[{}]", BlogContext.isAuthorized());


        logger.debug("Start load blog config info");
        BlogConfigModel blogConfigModel = blogConfigService.selectBlogConfig(BlogConstants.BLOG_CONFIG);

        request.setAttribute("blogConfigModel", blogConfigModel);




        chain.doFilter(request,response);


    }
}
