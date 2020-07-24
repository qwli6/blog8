package me.lqw.blog8.web.filter;

import me.lqw.blog8.BlogContext;
import me.lqw.blog8.model.BlackIp;
import me.lqw.blog8.service.BlackIpService;
import me.lqw.blog8.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * 黑名单过滤器
 * @author liqiwen
 * @version 1.0
 */
public class BlackIpFilter extends OncePerRequestFilter {


    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    private final BlackIpService blackIpService;

    public BlackIpFilter(BlackIpService blackIpService) {
        this.blackIpService = blackIpService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String ip = WebUtil.getIp(httpServletRequest);
        logger.info("BlackIpFilter#doFilterInternal() ip:[{}]", ip);
        if(!StringUtils.isEmpty(ip)){
            Optional<BlackIp> blackIpOp = blackIpService.findByIp(ip);

            if(blackIpOp.isPresent()){
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            BlogContext.setIp(ip);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
