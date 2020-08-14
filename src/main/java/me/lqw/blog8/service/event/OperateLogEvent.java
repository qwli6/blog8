package me.lqw.blog8.service.event;

import me.lqw.blog8.model.OperateLog;
import org.springframework.context.ApplicationEvent;

/**
 * 操作日志事件
 * @author liqiwen
 * @since 1.4
 * @version 1.4
 */
public class OperateLogEvent extends ApplicationEvent {

    private OperateLog operateLog;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public OperateLogEvent(Object source) {
        super(source);
    }

    public OperateLogEvent(Object source, OperateLog operateLog) {
        super(source);
        this.operateLog = operateLog;
    }

    public OperateLog getOperateLog() {
        return operateLog;
    }
}
