package me.lqw.blog8.exception;

public class LogicException extends RuntimeException {

    private final Message error;

    public LogicException(Message error) {
        this.error = error;
    }

    public LogicException(String code, String msg) {
        this.error = new Message(code, msg);
    }

    public Message getError() {
        return error;
    }
}
