package me.lqw.blog8.model.dto.common;

import me.lqw.blog8.model.dto.page.PageResult;

import java.io.Serializable;

/**
 * 返回结果集处理
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class ResultDTO implements Serializable {

    /**
     * 创建一个通用成功返回
     *
     * @return CR<?>
     */
    public static CR<?> create() {
        return new CR<>();
    }

    /**
     * 创建一个通用成功返回, 带 data 数据
     *
     * @param data data
     * @return CR<?>
     */
    public static CR<?> create(Object data) {
        return new CR<>(data);
    }

    /**
     * 创建一个通用查询返回
     *
     * @param pageResult pageResult
     * @return QR
     */
    public static QR createQR(PageResult<?> pageResult) {
        return new QR(pageResult);
    }
}
