package me.lqw.blog8.exception;

import me.lqw.blog8.constants.Message;

/**
 * 系统逻辑异常
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class LogicException extends AbstractBlogException {

    /**
     * 构造方法
     *
     * @param error error
     */
    public LogicException(Message error) {
        super(error.getMsg(), null, false, false, error);
    }

    /**
     * 构造方法
     *
     * @param code    code
     * @param message message
     */
    public LogicException(String code, String message) {
        super(message, null, false, false, new Message(code, message));
    }
}
