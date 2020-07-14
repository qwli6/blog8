package me.lqw.blog8.exception;


public class UnauthorizedException extends LogicException {

    public UnauthorizedException(String code, String msg) {
        super(code, msg);
    }
}
