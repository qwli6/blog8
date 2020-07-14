package me.lqw.blog8.web.filter;

import me.lqw.blog8.BlogContext;
import me.lqw.blog8.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 上下文过滤器
 * @author liqiwen
 * @version 1.0
 */
public class ContextFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        String ip = WebUtil.getIp((HttpServletRequest) request);

        BlogContext.setIp(ip);

        logger.info("ContextFilter#doFilter...");

        chain.doFilter(request,response);


    }
}
