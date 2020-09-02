package me.lqw.blog8.model.enums;

/**
 * 评论审核策略
 * <ul>
 *     <li>第一次评论审核</li>
 *     <li>评论从不审核</li>
 *     <li>每次评论都要审核</li>
 * </ul>
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public enum CommentCheckStrategy {
    FIRST("第一次评论时审核"),
    NEVER("评论从不审核"),
    ALWAYS("每次评论都要审核");

    String desc;

    CommentCheckStrategy(String desc) {
        this.desc = desc;
    }
}
