package me.lqw.blog8.service;

import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.event.comments.CreateCommentEvent;
import me.lqw.blog8.exception.AbstractBlogException;
import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.mapper.ArticleMapper;
import me.lqw.blog8.mapper.CommentMapper;
import me.lqw.blog8.model.Article;
import me.lqw.blog8.model.Comment;
import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.dto.CommentDTO;
import me.lqw.blog8.model.dto.CommentSaved;
import me.lqw.blog8.model.dto.page.PageResult;
import me.lqw.blog8.model.enums.ArticleStatusEnum;
import me.lqw.blog8.model.enums.CommentCheckStrategy;
import me.lqw.blog8.model.enums.CommentStatus;
import me.lqw.blog8.model.vo.CheckConversationParams;
import me.lqw.blog8.model.vo.HandledCommentPageQueryParam;
import me.lqw.blog8.plugins.md.MarkdownParser;
import me.lqw.blog8.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.mail.MessagingException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 评论业务逻辑处理实现类
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Service
public class CommentService implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 日志处理
     */
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * 评论模块处理器
     */
    private final List<CommentModuleHandler<?>> handlers;

    /**
     * 评论持久化 Mapper
     */
    private final CommentMapper commentMapper;

    private final ArticleMapper articleMapper;

    /**
     * 博客配置
     */
    private final BlogConfigService configService;

    /**
     * MarkdownParser 解析
     */
    private final MarkdownParser markdownParser;


    private final SimpleMailHandler mailHandler;

    /**
     * 单线程池
     */
    private final ExecutorService service = Executors.newSingleThreadExecutor();

    /**
     * 构造方法注入
     *
     * @param objectProvider objectProvider
     * @param commentMapper  commentMapper
     * @param configService  configService
     */
    public CommentService(ObjectProvider<CommentModuleHandler<?>> objectProvider, CommentMapper commentMapper,
                          ArticleMapper articleMapper, SimpleMailHandler mailHandler,
                          BlogConfigService configService, ObjectProvider<MarkdownParser> markdownParserObjectProvider) {
        this.handlers = objectProvider.orderedStream().collect(Collectors.toList());
        this.articleMapper = articleMapper;
        this.commentMapper = commentMapper;
        this.configService = configService;
        this.markdownParser = markdownParserObjectProvider.stream().min(Comparator.comparingInt(MarkdownParser::getOrder)).get();
        this.mailHandler = mailHandler;
    }

    /**
     * 插入评论
     *
     * @param comment comment
     * @return CommentSaved
     * @throws LogicException 逻辑异常
     *        1. 评论模块不存在异常
     *        2. 父评论不存在异常
     *        3. 评论模块不匹配异常
     *        4. 父评论待审核异常
     *        5. 两次评论内容相同异常
     *        6. 操作太频繁异常
     *        7. 审核策略无效异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentSaved save(Comment comment) throws LogicException {
        CommentModule module = comment.getModule();
//        CommentModuleHandler<?> moduleHandler = handlers.stream().filter(h -> h.getModuleName().equals(module.getName()))
//                .findAny().orElseThrow(() -> new LogicException("comment.module.notExists", "评论模块不存在"));
//
//        //插入之前检查模块
//        moduleHandler.checkBeforeSaved(comment, module);

        //获取到评论内容
        String content = comment.getContent();

        //获取到评论中的父评论
        Comment parent = comment.getParent();

        //设置评论深度
        String path = "/";

        //父评论存在
        if (parent != null && parent.getId() != null) {

            //在 db 中查询父评论是否存在
            parent = commentMapper.selectById(parent.getId()).orElseThrow(()
                    -> new LogicException("parentComment.notExists", "父评论不存在"));

            CommentModule parentModule = parent.getModule();

            //判断父评论和子评论模块是否匹配
            if (parentModule == null || !parentModule.getId().equals(module.getId()) || !parentModule.getName().equals(module.getName())) {
                throw new LogicException("comment.module.mismatch", "评论模块不匹配");
            }

            //判断父评论状态是否是待审核状态
            CommentStatus status = parent.getStatus();
            if (status.equals(CommentStatus.WAIT_CHECK)) {
                throw new LogicException("parentComment.wait.check", "父评论待审核");
            }

            //重新赋值评论深度
            path = parent.getPath() + parent.getId() + "/";

            //重新设置父评论
            comment.setParent(parent);
        }
        comment.setPath(path);

        //已经获取到 ip 的前提下
        if (StringUtil.isNotBlank(comment.getIp())) {
            //获取该 ip 上一次评论的内容，对同一模块
            Optional<Comment> opLatestComment = commentMapper.selectLatestByModuleAndIp(module, comment.getIp());
            if (opLatestComment.isPresent()) {
                Comment lastComment = opLatestComment.get();
                //如果本次评论和上一次评论内容相同
                if (lastComment.getContent().equals(content)) {
                    throw new LogicException("comment.content.duplicate", "两次评论内容相同");
                }

                LocalDateTime last = lastComment.getCreateAt();
                LocalDateTime current = LocalDateTime.now();

                //如果对同一模块在 15s 进行多次评论, 如果登录了则无此限制
                if (current.minusSeconds(15).isBefore(last) && !BlogContext.isAuthorized()) {
                    throw new LogicException("comment.too.frequently", "操作太频繁了，请稍后再试");
                }
            }
        }

        //审核策略
        boolean checking;

        if (BlogContext.isAuthorized()) {
            checking = false; //管理员是不需要审核的
            comment.setAdmin(true);
            comment.setUsername("");
            comment.setWebsite("");
        } else {
            String email = comment.getEmail();
            if (StringUtil.isBlank(email)) {
                comment.setAvatar(BlogConstants.DEFAULT_AVATAR);
            } else {
                comment.setAvatar(DigestUtils.md5DigestAsHex(email.getBytes(Charset.defaultCharset())));
            }
            comment.setAdmin(false);

            CommentCheckStrategy strategy = configService.selectCurrentCheckStrategy().orElse(CommentCheckStrategy.FIRST);
            switch (strategy) {
                case FIRST: //首次提交评论审核
                    Optional<Comment> firstCommentOptional = commentMapper.findLastCommentByIp(comment.getIp());
                    if (firstCommentOptional.isPresent()) { //上条评论存在
                        CommentStatus status = firstCommentOptional.get().getStatus();
                        //根据上一条评论的状态来判断这条评论是否需要审核
                        checking = status == CommentStatus.WAIT_CHECK;
                    } else {
                        checking = true;
                    }
                    break;
                case NEVER: // 评论从不审核
                    checking = false;
                    break;
                case ALWAYS: // 评论总是需要审核
                    checking = true;
                    break;
                default: // 审核策略无效
                    throw new LogicException("invalid.check.strategy", "无效的审核策略");
            }
        }
        //设置评论状态
        comment.setStatus(checking ? CommentStatus.WAIT_CHECK : CommentStatus.NORMAL);

        //插入评论内容
        commentMapper.insert(comment);

        //设置通知计划
        final Comment _parent = parent;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() { //事务提交之后执行
                comment.setParent(_parent);
                //执行通知
                sendMail(comment);
                //正常状态内容 + 评论量
                if(comment.getStatus().equals(CommentStatus.NORMAL)){
                    applicationEventPublisher.publishEvent(new CreateCommentEvent(this, module));
                }
            }
        });
        return new CommentSaved(comment.getId(), checking);
    }

    /**
     * 删除评论
     *
     * @param id id
     * @throws LogicException 逻辑异常
     *         1. 评论不存在异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Integer id) throws LogicException {
        Comment comment = commentMapper.selectById(id).orElseThrow(() ->
                new LogicException("comment.notExists", "评论不存在"));
        commentMapper.deleteChildren(comment.getId());
        commentMapper.deleteById(comment.getId());
    }

    /**
     * 更新评论，只有管理员能更新评论
     *
     * @param comment comment
     * @throws LogicException 逻辑异常
     *                        1. 评论不存在异常
     *                        2. 未登录的情况下不能更改评论内容
     *                        3. 管理员只能更改自己的评论
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Comment comment) throws LogicException {

        if (!BlogContext.isAuthorized()) {
            throw new LogicException("need.auth", "操作不允许");
        }

        //更新必须是更新的自己的评论
        Comment old = commentMapper.selectById(comment.getId()).orElseThrow(()
                -> new LogicException("comment.notExists", "评论不存在"));

        if (!old.getAdmin()) {
            //仅可以更新自己的评论
            throw new LogicException("only.change.admin", "管理员仅可以更改自己的评论");
        }

        //评论内容不同时, 才需要更改
        if (!old.getContent().equals(comment.getContent())) {
            Comment update = new Comment();
            update.setId(comment.getId());
            update.setContent(comment.getContent());
            commentMapper.update(update);


            Comment parent = old.getParent();
            if (parent != null && !parent.getAdmin()) {
                //非自己的评论, 通知被评论人管理员评论有更新
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        update.setParent(parent);
                        sendMail(update);
                    }
                });
            }
        }
    }


    /**
     * 审核评论
     * @throws LogicException 逻辑异常
     *         1. 评论不存在异常
     *         2. 状态无法审核异常
     *         3. 文章不存在异常
     *         4. 未发布状态无法审核异常
     *
     *         使用同步关键字, 效率低，但是审核次数不多，不存在并发的情况
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized CommentSaved checkComment(Integer id) throws LogicException {
        Comment comment = commentMapper.selectById(id).orElseThrow(()
                -> new LogicException("comment.notExits", "评论不存在"));

        CommentStatus status = comment.getStatus();
        if (status == CommentStatus.NORMAL) {
            throw new LogicException("comment.status.normal", "评论已审核, 请勿重复操作");
        }
        comment.setStatus(CommentStatus.NORMAL);
        commentMapper.update(comment);

        CommentModule module = comment.getModule();

        Article article = articleMapper.selectById(module.getId()).orElseThrow(()
                -> new LogicException("article.notExits", "文章不存在"));
        ArticleStatusEnum articleStatus = article.getStatus();

        if(!articleStatus.equals(ArticleStatusEnum.POSTED)){
            throw new LogicException("article.status.notAllow", "文章状态不允许");
        }

        articleMapper.increaseComments(article.getId());

        return new CommentSaved(id, false);
    }

    /**
     * 执行邮件通知
     *
     * @param comment comment
     */
    private void sendMail(Comment comment) {
        logger.info("执行邮件通知...");

        service.submit(() -> {

            try {
                mailHandler.sendEmail();
            } catch (MessagingException e) {
                e.printStackTrace();
            } finally {
                service.shutdown();
            }


            System.out.println("执行邮件发送通知");
//            SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//            templateEngine.process()

        });
    }

    /**
     * 分页查询评论
     *
     * @param queryParam queryParam
     * @return PageResult
     */
    @Transactional(readOnly = true)
    public PageResult<CommentDTO> selectPage(HandledCommentPageQueryParam queryParam) throws LogicException {

        CommentModule commentModule = queryParam.getCommentModule();

        //未登录的情况下，只查询正常状态的评论
        if (!BlogContext.isAuthorized()) {
            queryParam.setCommentStatus(CommentStatus.NORMAL);
        }


        if(commentModule != null) {
            if(StringUtil.isNotBlank(commentModule.getName())) {
                CommentModuleHandler<?> commentModuleHandler = handlers.stream().filter(h -> h.getModuleName().equals(commentModule.getName()))
                        .findAny().orElseThrow(() -> new LogicException("comment.module.notExists", "评论模块不存在"));

                if (commentModule.getId() != null) {

                    commentModuleHandler.checkBeforeQuery(commentModule);
                }
            }
        }

        //查询评论数量
        int count = commentMapper.selectCount(queryParam);
        if (count == 0) {
            return new PageResult<>(queryParam, 0, Collections.emptyList());
        }

        //获取评论列表
        List<Comment> comments = commentMapper.selectPage(queryParam);
        if (CollectionUtils.isEmpty(comments)) {

            return new PageResult<>(queryParam, 0, Collections.emptyList());
        }

        List<CommentDTO> commentDTOS = processComments(comments);


        return new PageResult<>(queryParam, count, commentDTOS);
    }

    /**
     * 处理评论列表
     *
     * @param comments comments
     */
    private List<CommentDTO> processComments(List<Comment> comments) {

        List<CommentDTO> commentDtos = new ArrayList<>();

        if (!CollectionUtils.isEmpty(comments)) {

            commentDtos = comments.stream().map(CommentDTO::new).collect(Collectors.toList());

            for (CommentDTO commentDto : commentDtos) {

                CommentDTO _parent = commentDto.getParent();
                if(_parent != null){
                    commentMapper.selectById(_parent.getId()).ifPresent(e -> {
                        _parent.setNickname(e.getUsername());
                        _parent.setAdmin(e.getAdmin());
                        _parent.setAvatar(e.getAvatar());
                        _parent.setId(e.getId());

                        commentDto.setParent(_parent);
                    });
                }


                //非管理员评论, 隐藏邮箱
                if (!commentDto.getAdmin()) {
                    commentDto.setEmail("********");
                }
                commentDto.setContent(markdownParser.parse(commentDto.getContent()));
            }
        }
        return commentDtos;
    }

    /**
     * 查看会话
     * @param checkConversationParams 查看会话参数
     * @return list
     * @throws AbstractBlogException AbstractBlogException
     * <p>
     *    评论不存在异常
     *    评论待审核，无法查看评论会话
     * </p>
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> checkConversation(CheckConversationParams checkConversationParams) throws AbstractBlogException {
        Comment old = commentMapper.selectById(checkConversationParams.getId()).orElseThrow(()
                -> new LogicException("comment.notExists", "评论不存在"));

        if(old.getStatus().equals(CommentStatus.WAIT_CHECK)){
            throw new LogicException("currentComment.waitCheck", "评论待审核, 无法查看评论会话");
        }

        String path = old.getPath();
        if(StringUtil.isBlank(path) || "/".equals(path)){
            CommentDTO commentDto = new CommentDTO(old);
            if(!commentDto.getAdmin()){
                commentDto.setEmail("********");
            }
            if(StringUtil.isNotBlank(commentDto.getContent())){
                commentDto.setContent(markdownParser.parse(commentDto.getContent()));
            }
            return Collections.singletonList(commentDto);
        }

        List<Integer> ids = Arrays.stream(path.split("/")).filter(StringUtil::isNotBlank).map(Integer::new).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(ids)){
            ids.add(old.getId());
        }

        List<Comment> comments = commentMapper.selectByIds(ids);

        //未审核评论需要直接过滤出来
        List<CommentDTO> commentDtos = comments.stream().filter(e -> e.getStatus().equals(CommentStatus.NORMAL)).map(CommentDTO::new).sorted(Comparator.comparing(CommentDTO::getId)).collect(Collectors.toList());

        commentDtos.forEach(e -> {
            if(e.getAdmin()){ //非管理员邮箱不暴露邮箱账号
                e.setEmail("********");
            }
            if(StringUtil.isNotBlank(e.getContent())) {
                e.setContent(markdownParser.parse(e.getContent()));
            }
        });

        return commentDtos;
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
