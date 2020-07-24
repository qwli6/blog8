package me.lqw.blog8.service;

import me.lqw.blog8.BlogConstants;
import me.lqw.blog8.BlogContext;
import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.mapper.CommentMapper;
import me.lqw.blog8.model.*;
import me.lqw.blog8.model.dto.PageResult;
import me.lqw.blog8.model.vo.CommentQueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class CommentService implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final List<CommentModuleHandler<?>> handlers;
    private final CommentMapper commentMapper;
    private final BlogConfigService configService;
    private ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();

    private ExecutorService service = Executors.newSingleThreadExecutor();


    public CommentService(ObjectProvider<CommentModuleHandler<?>> objectProvider, CommentMapper commentMapper,
                          BlogConfigService configService) {
        this.handlers = objectProvider.orderedStream().collect(Collectors.toList());
        this.commentMapper = commentMapper;
        this.configService = configService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CommentSaved save(Comment comment) throws LogicException {
        CommentModule module = comment.getModule();
        CommentModuleHandler<?> moduleHandler = handlers.stream().filter(h -> h.getModuleName().equals(module.getName()))
                .findAny().orElseThrow(() -> new LogicException("comment.module.notExists", "评论模块不存在"));

        moduleHandler.checkBeforeSaved(comment, module);

        String content = comment.getContent();


        Comment parent = comment.getParent();
        String path = "/";
        if(parent != null && parent.getId() != null){

            parent = commentMapper.findById(parent.getId()).orElseThrow(()
                    -> new LogicException("parentComment.notExists", "父评论不存在"));

            CommentModule parentModule = parent.getModule();
            if(!parentModule.equals(module)){
                throw new LogicException("comment.module.mismatch", "评论模块不匹配");
            }

            CommentStatus status = parent.getStatus();
            if(status.equals(CommentStatus.WAIT_CHECK)){
                throw new LogicException("parentComment.wait.check", "父评论待审核");
            }
            path = path + parent.getId() + "/";
            comment.setParent(parent);
        } else {
            path = "/";
        }
        comment.setPath(path);

        Optional<Comment> opLatestComment = commentMapper.findLatestByModuleAndIp(module, comment.getIp());
        if(opLatestComment.isPresent()){
            Comment lastComment = opLatestComment.get();
            if(lastComment.getContent().equals(content)) {
                throw new LogicException("comment.content.duplicate", "两次评论内容相同");
            }

            LocalDateTime lastCreateAt = lastComment.getCreateAt();
            LocalDateTime currentCreateAt = LocalDateTime.now();

            if(currentCreateAt.minusSeconds(15).isBefore(lastCreateAt) && !BlogContext.isAuthorized()){
                throw new LogicException("comment.too.frequently", "操作太频繁了，请稍后再试");
            }
        }

        boolean checking;
        CommentCheckStrategy strategy = configService.findCurrentCheckStrategy().orElse(CommentCheckStrategy.FIRST);
        switch (strategy){
            case FIRST:
                Optional<Comment> firstCommentOptional = commentMapper.findLastCommentByIp(comment.getIp());
                if(firstCommentOptional.isPresent()) { //上条评论存在
                    CommentStatus status = firstCommentOptional.get().getStatus();
                    if (status == CommentStatus.WAIT_CHECK) { //上条评论状态为待审核
                        checking = false; //那么本条评论也需要审核
                        break;
                    }
                }
                checking = false;
                break;
            case NEVER:
                checking = false;
                break;
            case FOREVER:
                checking = true;
                break;
            default:
                throw new LogicException("invalid.check.strategy", "无效的审核策略");
        }
//        comment.setCheck(checking);

        if(BlogContext.isAuthorized()){
            checking = false;
            comment.setAdmin(true);
            comment.setUsername("");
            comment.setWebsite("");
        } else {
            String email = comment.getEmail();
            if(StringUtils.isEmpty(email)){
                comment.setAvatar(BlogConstants.DEFAULT_AVATAR);
            } else {
                comment.setAvatar(DigestUtils.md5DigestAsHex(email.getBytes(Charset.defaultCharset())));
            }
            comment.setAdmin(false);
        }
        comment.setStatus(checking ? CommentStatus.WAIT_CHECK : CommentStatus.NORMAL);

        commentMapper.insert(comment);



        final Comment _parent = parent;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                comment.setParent(_parent);
                //执行通知
                sendMail(comment);
                moduleHandler.increaseComments(comment.getModule());
            }
        });
        return new CommentSaved(comment.getId(), checking);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Integer id) throws LogicException {
        Comment comment = commentMapper.findById(id).orElseThrow(() ->
                new LogicException("comment.notExists", "评论不存在"));
        commentMapper.deleteChildren(comment.getId());
        commentMapper.delete(comment.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Comment comment) throws LogicException {

    }


    /**
     * 执行邮件通知
     * @param comment comment
     */
    private void sendMail(Comment comment) {
        logger.info("执行邮件通知...");

        service.submit(() -> {

            System.out.println("执行邮件发送通知");

        });
    }

    public PageResult<Comment> selectPage(CommentQueryParam queryParam) {
        CommentModule module = queryParam.getModule();
        CommentModuleHandler<?> moduleHandler = handlers.stream().filter(h -> h.getModuleName().equals(module.getName()))
                .findAny().orElseThrow(() -> new LogicException("comment.module.notExists", "评论模块不存在"));

        moduleHandler.checkBeforeQuery(module);

        int count = commentMapper.countByQueryParam(queryParam);
        if(count == 0){

            return new PageResult<>(queryParam, 0, Collections.emptyList());
        }

        List<Comment> comments = commentMapper.selectPage(queryParam);
        if(comments.isEmpty()){

            return new PageResult<>(queryParam, 0, Collections.emptyList());
        }

        processComments(comments);


        return new PageResult<>(queryParam, comments.size(), comments);
    }


    private void processComments(List<Comment> comments){
        for(Comment comment : comments){
            Comment parent = commentMapper.findParentCommentById(comment.getId()).orElse(null);
            comment.setParent(parent);
            if(comment.getAdmin()){
                comment.setEmail("");
            }
        }
    }


    @EventListener(ContextClosedEvent.class)
    public void handleContextCloseEvent(){
        logger.info("上下文被关闭了");
        if(service != null && service.isShutdown()){
            service.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("CommentService afterPropertiesSet()...");
    }


}
