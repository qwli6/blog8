package me.lqw.blog8.web.filter;

import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.model.User;
import me.lqw.blog8.model.config.BlogConfigModel;
import me.lqw.blog8.service.BlogConfigService;
import me.lqw.blog8.util.StringUtil;
import me.lqw.blog8.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 上下文过滤器
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class ContextFilter implements Filter, Ordered {

    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 博客配置
     */
    private final BlogConfigService blogConfigService;

    /**
     * 构造方法
     *
     * @param blogConfigService 博客配置设置
     */
    public ContextFilter(BlogConfigService blogConfigService) {
        this.blogConfigService = blogConfigService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;



        String ip = WebUtil.getIp(httpServletRequest);

        BlogContext.setIp(ip);

        HttpSession session = httpServletRequest.getSession();
        if (session == null) {
            session = httpServletRequest.getSession(true);
        }
        User user = (User) session.getAttribute(BlogConstants.AUTH_USER);
        Boolean authTopUser = (Boolean) session.getAttribute(BlogConstants.AUTH_TOP_USER);
        BlogContext.AUTH_THREAD_LOCAL.set(user != null && authTopUser != null && authTopUser);

        BlogConfigModel blogConfigModel = blogConfigService.selectBlogConfig(BlogConstants.BLOG_CONFIG);
        request.setAttribute("blogConfigModel", blogConfigModel);

        chain.doFilter(request, response);
    }

    /**
     * 过滤器匹配路径
     * 匹配所有路径
     * @return list
     */
    public List<String> matchPatterns(){
        return Collections.singletonList("/*");
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
