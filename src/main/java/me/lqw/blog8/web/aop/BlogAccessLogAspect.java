package me.lqw.blog8.web.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 切面记录日志
 *
 * @author liqiwen
 * @version 1.4
 * @since 2020-05-11 11:01:57
 * @since 1.4
 */
@Aspect
@Component
public class BlogAccessLogAspect {

    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 切点
     * 在 me.lqw.blog.web.controller 所有方法上织入切面
     */
    @Pointcut("execution(public * me.lqw.blog8.web.controller.*.*(..))")
    public void log() {
        //不做业务
    }


    /**
     * 在切入点之前执行
     *
     * @param joinPoint joinPoint
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        Assert.notNull(attributes, "attributes 为空, 无法获取对应的 request 对象");

        HttpServletRequest request = attributes.getRequest();

        //获取请求的 requestURL
        String requestURL = request.getRequestURL().toString();

        //获取请求的 remoteAddr
        String remoteAddr = request.getRemoteAddr();

        //获取请求的 classMethod
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName();

        //获取请求的入参
        Object[] object = joinPoint.getArgs();

        //创建访问日志
        BlogRequestLog blogRequestLog = new BlogRequestLog(requestURL, remoteAddr, classMethod, object);

        //输出访问日志
        logger.info("requestLog:[{}]", blogRequestLog.toString());

    }


    /**
     * 在切入点之后执行
     */
    @After("log()")
    public void doAfter() {
        //不做业务逻辑
    }

    /**
     * 方法返回之后执行
     *
     * @param result 获取返回值
     */
    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result) {
        if (result != null) {
            logger.info("======== doAfterReturn ======== result:[{}]", result.toString());
        }
    }

}
