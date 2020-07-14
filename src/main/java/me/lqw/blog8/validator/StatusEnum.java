package me.lqw.blog8.validator;

public enum StatusEnum {
    DRAFT("草稿"),
    POSTED("已发布"),
    SCHEDULED("计划中"),
    ;

    String code;

    StatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
