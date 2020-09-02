package me.lqw.blog8.model.enums;

/**
 * 状态枚举
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public enum ArticleStatusEnum {

    /**
     * 草稿
     */
    DRAFT("草稿"),

    /**
     * 已发布
     */
    POSTED("已发布"),

    /**
     * 计划中
     */
    SCHEDULED("计划中"),
    ;

    /**
     * code 描述码
     */
    String code;

    /**
     * 构造方法
     *
     * @param code code
     */
    ArticleStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
