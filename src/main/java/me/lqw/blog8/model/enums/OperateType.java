package me.lqw.blog8.model.enums;

/**
 * 操作类型
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public enum OperateType {
    /**
     * 创建
     */
    CREATE("创建"),

    /**
     * 更新
     */
    UPDATE("更新"),

    /**
     * 删除
     */
    DELETE("删除");

    String desc;

    OperateType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
