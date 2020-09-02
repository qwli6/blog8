package me.lqw.blog8.exception;

import me.lqw.blog8.constants.Message;


/**
 * blog 系统异常根类
 *
 * @author liqiwen
 * @since 1.2
 */
public abstract class AbstractBlogException extends RuntimeException {

    /**
     * 错误消息
     */
    private final Message error;

    /**
     * 构造方法
     *
     * @param message            message
     * @param cause              code
     * @param enableSuppression  enableSuppression
     * @param writableStackTrace writableStackTrace
     * @param error              error
     */
    public AbstractBlogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Message error) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.error = error;
    }

    /**
     * 获取错误信息
     *
     * @return Message
     */
    public Message getError() {
        return error;
    }
}
