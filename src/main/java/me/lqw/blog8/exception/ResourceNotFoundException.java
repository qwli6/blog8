package me.lqw.blog8.exception;

import me.lqw.blog8.constants.Message;

/**
 * 资源未找到异常
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class ResourceNotFoundException extends AbstractBlogException {

    /**
     * 构造方法
     *
     * @param error error
     */
    public ResourceNotFoundException(Message error) {
        super(error.getMsg(), null, false, false, error);
    }

    /**
     * 构造方法
     *
     * @param code    code
     * @param message message
     */
    public ResourceNotFoundException(String code, String message) {
        super(message, null, false, false, new Message(code, message));
    }
}
