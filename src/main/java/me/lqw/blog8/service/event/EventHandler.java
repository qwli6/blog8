package me.lqw.blog8.service.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventHandler {


    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @EventListener(ContextClosedEvent.class)
    public void handleContextCloseEvent(){
        logger.info("EventHandler#handleContextCloseEvent 被关闭了");
        //当服务关闭后，发送邮件通知管理员
    }
}
