package me.lqw.blog8.model.enums;

/**
 * 评论状态
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public enum CommentStatus {
    /**
     * 正常评论
     */
    NORMAL("正常"),

    /**
     * 待审核状态
     */
    WAIT_CHECK("待审核");

    /**
     * 描述
     */
    String desc;

    /**
     * 构造方法
     *
     * @param desc desc
     */
    CommentStatus(String desc) {
        this.desc = desc;
    }
}
