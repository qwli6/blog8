package me.lqw.blog8.web;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 切面记录日志
 * @author liqiwen
 * @since 2020-05-11 11:01:57
 */
@Aspect
@Component
public class LogAspect {


    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    /**
     * 切点
     * 在 me.lqw.blog.web.controller 所有方法上织入切面
     */
    @Pointcut("execution(public * me.lqw.blog8.web.controller.*.*(..))")
    public void log() {

    }


    /**
     * 在切入点之前执行
     * @param joinPoint joinPoint
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestURL = request.getRequestURL().toString();
        String remoteAddr = request.getRemoteAddr();

        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName();

        Object[] object =joinPoint.getArgs();

        RequestLog requestLog = new RequestLog(requestURL, remoteAddr, classMethod, object);

        logger.info("requestLog:[{}]", requestLog.toString());

    }


    /**
     * 在切入点之后执行
     */
    @After("log()")
    public void doAfter(){
        logger.info("======== doAfter ========");

    }


    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("======== doAfterReturn ======== result:[{}]",result);

    }

}
