package me.lqw.blog8.event;

import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.event.comments.CreateCommentEvent;
import me.lqw.blog8.event.log.OperateLogEvent;
import me.lqw.blog8.mapper.ArticleMapper;
import me.lqw.blog8.mapper.MomentMapper;
import me.lqw.blog8.mapper.OperateLogMapper;
import me.lqw.blog8.model.Article;
import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.OperateLog;
import me.lqw.blog8.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 事件监听处理器
 *
 * @author liqiwen
 * @version 1.4
 */
@Component
public class BlogEventListenerHandler {

    /**
     * 日志打印 Logger
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 操作日志 Mapper
     */
    private final OperateLogMapper operateLogMapper;

    private final ArticleMapper articleMapper;

    private final MomentMapper momentMapper;

    /**
     * 构造方法注入
     *
     * @param operateLogMapper operateLogMapper
     */
    public BlogEventListenerHandler(OperateLogMapper operateLogMapper, ArticleMapper articleMapper,
                                    MomentMapper momentMapper) {
        this.operateLogMapper = operateLogMapper;
        this.articleMapper = articleMapper;
        this.momentMapper = momentMapper;
    }

    /**
     * 监听容器关闭事件
     */
    @EventListener(ContextClosedEvent.class)
    public void handleContextCloseEvent() {
        logger.info("EventHandler#handleContextCloseEvent 被关闭了");
        //当服务关闭后，发送邮件通知管理员
    }

    /**
     * 监听 OperateLogEvent 事件
     *
     * @param operateLogEvent operateLogEvent
     */
    @EventListener(OperateLogEvent.class)
    @Transactional(propagation = Propagation.REQUIRED)
    public void handleOperateLogEvent(OperateLogEvent operateLogEvent) {
        OperateLog operateLog = operateLogEvent.getObject();
        operateLog.setIp(BlogContext.getIp());
        operateLogMapper.insert(operateLog);
    }

    /**
     * 创建评论事件
     * @param createCommentEvent createCommentEvent
     */
    @EventListener(CreateCommentEvent.class)
    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized void handleCommentCreateEvent(CreateCommentEvent createCommentEvent) {
        CommentModule commentModule = createCommentEvent.getCommentModule();
        switch (commentModule.getName()){
            case "article":
                articleMapper.increaseComments(commentModule.getId());
                break;
            case "moment":
                momentMapper.increaseComments(commentModule.getId());
                break;
            case "template":
                break;
        }

    }

}
