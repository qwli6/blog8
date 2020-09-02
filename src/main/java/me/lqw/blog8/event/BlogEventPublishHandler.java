package me.lqw.blog8.event;

import me.lqw.blog8.event.log.OperateLogEvent;
import me.lqw.blog8.model.OperateLog;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * 事件发布 Handler
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
@Component
public class BlogEventPublishHandler implements ApplicationEventPublisherAware {

    /**
     * 事件发布者
     */
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 发布操作日志事件
     *
     * @param operateLog operateLog
     */
    public void publishEvent(OperateLog operateLog) {
        OperateLogEvent logEvent = new OperateLogEvent(this, operateLog);
        applicationEventPublisher.publishEvent(logEvent);
    }

    /**
     * set 方法注入
     *
     * @param applicationEventPublisher applicationEventPublisher
     */
    @Override
    public void setApplicationEventPublisher(@Nullable ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
