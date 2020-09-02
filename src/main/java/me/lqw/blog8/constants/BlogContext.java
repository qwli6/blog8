package me.lqw.blog8.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统上下文属性
 *
 * @author liqiwen
 * @since 1.2
 * @since 1.2
 */
public final class BlogContext {

    private static final Logger logger = LoggerFactory.getLogger(BlogContext.class);

    /**
     * 是否登录
     */
    public static final ThreadLocal<Boolean> AUTH_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 每次请求 ip
     */
    public static final ThreadLocal<String> IP_LOCAL = new ThreadLocal<>();

    /**
     * 构造方法
     */
    private BlogContext() {
        super();
    }

    /**
     * 是否授权
     * @return false | true
     */
    public static Boolean isAuthorized() {
        logger.info("isAuthorized: [{}]", AUTH_THREAD_LOCAL.get());
        return AUTH_THREAD_LOCAL.get() != null && AUTH_THREAD_LOCAL.get();
    }

    /**
     * 获取 ip 地址
     * @return string
     */
    public static String getIp() {
        return IP_LOCAL.get() == null ? "" : IP_LOCAL.get();
    }

    /**
     * 设置 ip 地址
     * @param ip ip
     */
    public static void setIp(String ip) {
        IP_LOCAL.set(ip);
    }
}
