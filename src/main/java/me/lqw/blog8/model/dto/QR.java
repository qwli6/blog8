package me.lqw.blog8.model.dto;

import me.lqw.blog8.model.vo.QueryParam;

import java.io.Serializable;

/**
 * 查询返回结果集封装
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class QR extends CR<PageResult<?>> implements Serializable {

    public QR(PageResult<?> pageResult){
        super(pageResult);
    }
}
