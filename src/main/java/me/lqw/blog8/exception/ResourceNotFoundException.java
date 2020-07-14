package me.lqw.blog8.exception;

public class ResourceNotFoundException extends RuntimeException {

    private Message error;

    public ResourceNotFoundException(Message error) {
        this.error = error;
    }

    public ResourceNotFoundException(String code, String msg){
        this.error = new Message(code, msg);
    }

    public Message getError() {
        return error;
    }

    public void setError(Message error) {
        this.error = error;
    }
}
