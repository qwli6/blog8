package me.lqw.blog8.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志查询
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class OperateLogQueryParam implements Serializable {

    /**
     * 开始时间
     */
    private LocalDateTime begin;

    /**
     * 结束时间
     */
    private LocalDateTime end;

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
