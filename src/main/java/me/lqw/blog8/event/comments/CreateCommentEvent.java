package me.lqw.blog8.event.comments;

import me.lqw.blog8.model.CommentModule;
import org.springframework.context.ApplicationEvent;

public class CreateCommentEvent extends ApplicationEvent {


    private CommentModule commentModule;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public CreateCommentEvent(Object source) {
        super(source);
    }

    public CreateCommentEvent(Object source, CommentModule commentModule){
        super(source);
        this.commentModule = commentModule;
    }

    public CommentModule getCommentModule() {
        return commentModule;
    }
}
