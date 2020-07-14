package me.lqw.blog8.model;

public enum CommentCheckStrategy {
    FIRST("第一次评论时审核"),
    NEVER("评论从不审核"),
    FOREVER("每次评论都要审核");

    String desc;
    CommentCheckStrategy(String desc) {
        this.desc = desc;
    }
}
