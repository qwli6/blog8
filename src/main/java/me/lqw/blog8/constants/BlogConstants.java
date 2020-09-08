package me.lqw.blog8.constants;

import java.io.Serializable;

/**
 * 全局系统设置
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class BlogConstants implements Serializable {

    /**
     * 默认记住我登录时长为 7 天
     */
    public static final Long DEFAULT_EXPIRED_TIME = 7 * 24 * 3600L;

    /**
     * 用户记住我 token
     */
    public static final String REMEMBER_ME = "user.remember.token";

    /**
     * user session key
     */
    public static final String AUTH_USER = "auth_user";
    public static final String AUTH_TOP_USER = "auth_top_user";


    public static final String LOGIN_URI = "/login";
    public static final String INDEX_URI = "/";

    /**
     * 系统错误
     */
    public static final Message SYSTEM_ERROR = new Message("system.internal.error", "服务器可能无法理解您的请求");

    /**
     * 授权失败
     */
    public static final Message AUTH_FAILED = new Message("auth.failed", "用户认证失败");

    public static final Message AUTH_USER_FIRST = new Message("user.authRequired", "请先认证用户名和密码");

    public static final Message NO_HANDLER_FOUND = new Message("handler.notFound", "您好像来到了没有知识的荒原");



    /**
     * 最大分页大小
     */
    public static final int MAX_PAGE_SIZE = 50;

    /**
     * 重定向地址
     */
    public static final String REDIRECT_URL_ATTRIBUTE = "redirectUrl";

    /**
     * 默认头像地址
     */
    public static final String DEFAULT_AVATAR = "";

    /**
     * 审核策略 key
     */
    public static final String COMMENT_CHECK_STRATEGY = "CheckStrategy";

    /**
     * 邮件通知 key
     */
    public static final String BLOG_CONFIG_EMAIL = "BlogConfigEmail";

    /**
     * 评论通知
     */
    public static final String COMMENT_NOTIFY = "CommentNotify";

    /**
     * 博客配置
     */
    public static final String BLOG_CONFIG = "BlogConfig";

    /**
     * 构造方法
     */
    private BlogConstants() {
        super();
    }


}
