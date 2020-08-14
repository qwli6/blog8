package me.lqw.blog8.model;

/**
 * 操作类型
 * @author liqiwen
 * @since 1.4
 * @version 1.4
 */
public enum OperateType {
    CREATE("创建"),
    UPDATE("更新"),
    DELETE("删除");

    OperateType(String desc) {
        this.desc = desc;
    }

    String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
