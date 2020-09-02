package me.lqw.blog8.exception;

import me.lqw.blog8.constants.Message;

/**
 * 错误请求异常
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class BadRequestException extends AbstractBlogException {
    /**
     * 构造方法
     *
     * @param error error
     */
    public BadRequestException(Message error) {
        super(error.getMsg(), null, false, false, error);
    }

    /**
     * 构造方法
     *
     * @param code    code
     * @param message message
     */
    public BadRequestException(String code, String message) {
        super(message, null, false, false, new Message(code, message));
    }
}
