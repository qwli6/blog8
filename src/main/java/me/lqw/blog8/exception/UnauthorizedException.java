package me.lqw.blog8.exception;

/**
 * 未认证异常
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class UnauthorizedException extends LogicException {

    public UnauthorizedException(String code, String msg) {
        super(code, msg);
    }
}
