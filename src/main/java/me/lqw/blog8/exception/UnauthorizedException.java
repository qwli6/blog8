package me.lqw.blog8.exception;

import me.lqw.blog8.constants.Message;

/**
 * 未授权异常
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class UnauthorizedException extends AbstractBlogException {

    /**
     * 构造方法
     *
     * @param error error
     */
    public UnauthorizedException(Message error) {
        super(error.getMsg(), null, false, false, error);
    }

    /**
     * 构造方法
     *
     * @param code code
     * @param msg  msg
     */
    public UnauthorizedException(String code, String msg) {
        super(msg, null, false, false, new Message(code, msg));
    }
}
