package me.lqw.blog8.web.filter;

import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.model.BlackIp;
import me.lqw.blog8.service.BlackIpService;
import me.lqw.blog8.util.StringUtil;
import me.lqw.blog8.util.WebUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 黑名单过滤器
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class BlackIpFilter extends OncePerRequestFilter implements Ordered {

    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 需要过滤的静态资源
     */
    private final List<String> staticResource = Arrays.asList(".json", ".js", ".css", ".ico", ".jpg", ".jpeg", ".mov");

    /**
     * 黑名单处理
     */
    private final BlackIpService blackIpService;

    /**
     * 构造方法
     *
     * @param blackIpService 黑名单业务实现类
     */
    public BlackIpFilter(BlackIpService blackIpService) {
        this.blackIpService = blackIpService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest,
                                    @NotNull HttpServletResponse httpServletResponse,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        //获取到请求的 requestURI
        String requestURI = httpServletRequest.getRequestURI();

        //如果发现是静态资源, 则直接过滤掉，静态资源放行
        for (String str : staticResource) {
            if (requestURI.contains(str)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
        }

        //获取到本次请求的 ip 地址
        String ip = WebUtil.getIp(httpServletRequest);

        if (logger.isDebugEnabled()) {
            logger.info("BlackIpFilter#doFilterInternal() ip:[{}]", ip);
        }

        //如果 ip 地址不为空
        if (StringUtil.isNotBlank(ip)) {
            Optional<BlackIp> blackIpOp = blackIpService.selectByIp(ip);
            //发现黑名单存在
            if (blackIpOp.isPresent()) {
                //设置响应状态为 403, 无权访问此网站
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            //将 ip 设置到 ThreadLocal 中, 后续还需要使用
            BlogContext.setIp(ip);
        }
        // 放行
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    /**
     * 过滤器匹配路径
     * @return list
     */
    public List<String> matchPatterns(){
        return Collections.singletonList("/*");
    }

    @Override
    public int getOrder() {
        return 9;
    }
}
