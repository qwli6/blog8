package me.lqw.blog8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BlogContext {

    private static final Logger logger = LoggerFactory.getLogger(BlogContext.class);

    private BlogContext() {
        super();
    }

    public static final ThreadLocal<Boolean> AUTH_THREAD_LOCAL = new ThreadLocal<>();
    public static final ThreadLocal<String> IP_LOCAL = new ThreadLocal<>();


    public static Boolean isAuthorized(){
        logger.info("isAuthorized: [{}]", AUTH_THREAD_LOCAL.get());
        return AUTH_THREAD_LOCAL.get() == null ? false : AUTH_THREAD_LOCAL.get();
    }


    public static void setIp(String ip){
        IP_LOCAL.set(ip);
    }

    public static String getIp(){
        return IP_LOCAL.get() == null ? "" : IP_LOCAL.get();
    }
}
