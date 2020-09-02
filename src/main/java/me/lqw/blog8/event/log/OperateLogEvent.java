package me.lqw.blog8.event.log;

import me.lqw.blog8.event.AbstractBlogEvent;
import me.lqw.blog8.model.OperateLog;

/**
 * 操作日志事件
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public class OperateLogEvent extends AbstractBlogEvent<OperateLog> {

    /**
     * create log event where access the system
     *
     * @param source     source
     * @param operateLog operateLog
     */
    public OperateLogEvent(Object source, OperateLog operateLog) {
        super(source, operateLog);
    }
}
