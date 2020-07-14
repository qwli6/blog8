package me.lqw.blog8;

import me.lqw.blog8.exception.Message;

public class BlogConstants {

    private BlogConstants() {
        super();
    }

    public static final String AUTH_USER = "user";
    public static final Message SYSTEM_ERROR = new Message("system.internal.error", "系统内部异常");
    public static final Message AUTH_FAILED = new Message("auth.failed","用户认证失败");
    public static final int MAX_PAGE_SIZE = 50;
    public static final String REDIRECT_URL_ATTRIBUTE = "redirectUrl";

    public static final String DEFAULT_AVATAR = "";

}
