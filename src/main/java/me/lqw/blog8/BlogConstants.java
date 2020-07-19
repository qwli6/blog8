package me.lqw.blog8;

import me.lqw.blog8.exception.Message;

import java.io.Serializable;

/**
 * 全局系统设置
 * @author liqiwen
 * @version 1.0
 */
public class BlogConstants implements Serializable {

    private BlogConstants() {
        super();
    }

    public static final String AUTH_USER = "user";
    public static final Message SYSTEM_ERROR = new Message("system.internal.error", "系统内部异常");
    public static final Message AUTH_FAILED = new Message("auth.failed","用户认证失败");
    public static final int MAX_PAGE_SIZE = 50;
    public static final String REDIRECT_URL_ATTRIBUTE = "redirectUrl";

    public static final String DEFAULT_AVATAR = "";




    //审核策略
    public static final String COMMENT_CHECK_STRATEGY = "CheckStrategy";
    //评论通知
    public static final String COMMENT_NOTIFY = "CommentNotify";
    //博客配置
    public static final String BLOG_CONFIG = "BlogConfig";

}
