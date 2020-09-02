package me.lqw.blog8.event.comments;

import me.lqw.blog8.event.AbstractBlogEvent;
import me.lqw.blog8.model.Moment;

/**
 * 动态创建评论事件
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class MomentCreateCommentEvent extends AbstractBlogEvent<Moment> {

    /**
     * create moment event when write a comment in moment
     *
     * @param source source
     * @param moment moment
     */
    public MomentCreateCommentEvent(Object source, Moment moment) {
        super(source, moment);
    }
}
