package me.lqw.blog8.model;

public enum CommentStatus {
    NORMAL("正常"),
    WAIT_CHECK("待审核");


    String desc;
    CommentStatus(String desc) {
        this.desc = desc;
    }
}
