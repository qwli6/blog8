package me.lqw.blog8.service;

import me.lqw.blog8.BlogProperties;
import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.exception.AbstractBlogException;
import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.exception.ResourceNotFoundException;
import me.lqw.blog8.exception.UnauthorizedException;
import me.lqw.blog8.mapper.*;
import me.lqw.blog8.model.*;
import me.lqw.blog8.model.dto.page.PageResult;
import me.lqw.blog8.model.enums.ArticleStatusEnum;
import me.lqw.blog8.model.vo.ArticleArchivePageQueryParam;
import me.lqw.blog8.model.vo.ArticlePageQueryParam;
import me.lqw.blog8.model.vo.HandledArticlePageQueryParam;
import me.lqw.blog8.plugins.md.MarkdownParser;
import me.lqw.blog8.util.JsonUtil;
import me.lqw.blog8.util.JsoupUtil;
import me.lqw.blog8.util.StringUtil;
import me.lqw.blog8.validator.ArticleStatus;
import org.apache.lucene.queryparser.classic.ParseException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 内容业务实现类
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Service
public class ArticleService extends AbstractBaseService<Article> implements CommentModuleHandler<Article>, InitializingBean {

    /**
     * 可重入读写锁
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final BlogProperties blogProperties;


    /**
     * 文章操作持久 Mapper
     */
    private final ArticleMapper articleMapper;

    /**
     * 分类操作 Mapper
     */
    private final CategoryMapper categoryMapper;

    /**
     * 文章标签关联 Mapper
     */
    private final ArticleTagMapper articleTagMapper;

    /**
     * 标签 Mapper
     */
    private final TagMapper tagMapper;

    /**
     * Markdown 解析
     */
    private final MarkdownParser markdownParser;

    /**
     * 评论 Mapper
     */
    private final CommentMapper commentMapper;

    /**
     * 文章索引构建器
     */
    private final ArticleIndexer articleIndexer;

    /**
     * 构造方法注入
     *
     * @param articleMapper    文章 mapper
     * @param categoryMapper   分类 mapper
     * @param articleTagMapper 关联关系 mapper
     * @param tagMapper        标签 mapper
     * @param objectProvider   markdown 解析
     * @param commentMapper    评论 mapper
     * @throws IOException IOException, 构建索引异常
     */
    public ArticleService(ArticleMapper articleMapper, CategoryMapper categoryMapper,
                          ArticleTagMapper articleTagMapper, TagMapper tagMapper,
                          BlogProperties blogProperties,
                          ObjectProvider<MarkdownParser> objectProvider,
                          CommentMapper commentMapper) throws IOException {
        this.articleMapper = articleMapper;
        this.categoryMapper = categoryMapper;
        this.articleTagMapper = articleTagMapper;
        this.tagMapper = tagMapper;
        this.markdownParser = objectProvider.stream().min(Comparator.comparingInt(Ordered::getOrder)).get();
        this.commentMapper = commentMapper;
        this.articleIndexer = new ArticleIndexer();
        this.blogProperties = blogProperties;
    }

    /**
     * 保存内容
     *
     * @param article article
     * @return Article
     * @throws LogicException 逻辑异常
     *                        1. 别名已经存在异常
     *                        2. 分类不存在异常
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public Article save(Article article) throws LogicException {
        Boolean privateArticle = article.getPrivateArticle();
        if(privateArticle == null){
            article.setPrivateArticle(false);
        }
        article.setHits(0);
        article.setComments(0);
        article.setCreateAt(LocalDateTime.now());
        article.setModifyAt(LocalDateTime.now());

        String urlName = article.getUrlName();
        //获取内容的 urlName，判断 urlName 是否存在
        if (StringUtil.isNotBlank(urlName)) {
            if (urlName.contains("/")) {
                urlName = urlName.replace("/", "");
            }
            articleMapper.selectByUrlName(urlName).ifPresent(e -> {
                throw new LogicException("article.aliasExists", "别名已经存在");
            });
        }
        //获取内容的分类, 判断分类是否存在
        Category category = article.getCategory();
        categoryMapper.selectById(category.getId()).orElseThrow(() ->
                new LogicException("category.notExists", "分类不存在"));

        //判断内容的状态
        ArticleStatusEnum status = article.getStatus();

        switch (status) {
            case DRAFT:
                //如果是草稿, 则直接设置 postAt 为空
                article.setPostAt(null);
                break;
            case POSTED:
                //如果是已发布状态, 则直接设置成当前时间
                article.setPostAt(LocalDateTime.now());
                break;
            case SCHEDULED:
                //如果是计划中，则必须指明发布时间
                if (article.getPostAt() == null || article.getPostAt().isBefore(LocalDateTime.now())) {
                    throw new LogicException("invalid.scheduleTime", "无效的发布时间");
                }
                break;
            default:
                //其他状态一律设置成草稿
                article.setStatus(ArticleStatusEnum.DRAFT);
                break;

        }
        //判断是否为定时发送
        if (status.equals(ArticleStatusEnum.SCHEDULED)) {
            LocalDateTime postAt = article.getPostAt();

            if (postAt.minusMinutes(1).isBefore(LocalDateTime.now())) {
                article.setPostAt(LocalDateTime.now());
                article.setStatus(ArticleStatusEnum.POSTED);
            } else {
                ScheduleManager.addArticle(article);
            }
        }

        articleMapper.insert(article);

        processArticleTags(article);

        //事务提交之后, 尝试重构索引
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                lock.writeLock().lock();
                try {
                    articleIndexer.addDocument(Collections.singletonList(article));
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("add document failed when saved article!: [{}]", e.getMessage(), e);
                } finally {
                    lock.writeLock().unlock();
                }
            }
        });
        return article;
    }

    /**
     * 删除内容
     *
     * @param id id
     * @throws LogicException 业务逻辑异常
     *                        1. 内容不存在异常
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public void delete(Integer id) throws AbstractBlogException {
        Article article = articleMapper.selectById(id).orElseThrow(()
                -> new LogicException(BlogConstants.ARTICLE_NOT_EXISTS));
        //删除内容本身
        articleMapper.deleteById(article.getId());
        //删除与之关联的关联标签
        articleTagMapper.deleteByArticle(article);
        //删除内容的评论以及子评论
        commentMapper.deleteByModule(new CommentModule(article.getId(), getModuleName()));

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                lock.writeLock().lock();
                try {
                    articleIndexer.deleteDocument(id);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("remove index failed when delete the article [{}], the failed reason: [{}]", id, e.getMessage(), e);
                } finally {
                    lock.writeLock().unlock();
                }
            }
        });
    }

    private void processArticleTags(Article article) {
        articleTagMapper.deleteByArticle(article);
        Set<Tag> tags = article.getTags();
        if (!CollectionUtils.isEmpty(tags)) {
            List<ArticleTag> articleTags = new ArrayList<>();
            tags.forEach(e -> {
                Optional<Tag> tagOp = tagMapper.selectById(e.getId());
                if(tagOp.isPresent()){
                     e = tagOp.get();
                     articleTags.add(new ArticleTag(article, e));
                }
            });
            if (!CollectionUtils.isEmpty(articleTags)) {
                articleTagMapper.batchInsert(articleTags);
            }
        }
    }


    /**
     * 分页查询内容列表
     *
     * @param queryParam queryParam
     * @return PageResult<Article>
     */
    @Transactional(readOnly = true)
    public PageResult<Article> selectPage(ArticlePageQueryParam queryParam) {
        String category = queryParam.getCategory();
        String tag = queryParam.getTag();
        Integer categoryId = null;
        Integer tagId = null;
        if (StringUtil.isNotBlank(category)) {
            Optional<Category> categoryOptional = categoryMapper.selectByName(category);
            if (categoryOptional.isPresent()) {
                categoryId = categoryOptional.get().getId();
            }
            if (categoryId == null) {
                return new PageResult<>(queryParam, 0, Collections.emptyList());
            }
        }

        if (StringUtil.isNotBlank(tag)) {
            Optional<Tag> byName = tagMapper.selectByName(tag);
            if (byName.isPresent()) {
                tagId = byName.get().getId();
            }
            if (tagId == null) {
                return new PageResult<>(queryParam, 0, Collections.emptyList());
            }
        }

        List<Integer> ids = new ArrayList<>();
        String query = queryParam.getQuery();
        if(StringUtil.isNotBlank(query)) {
            //利用 lucene 从索引库中查询出内容的 id，根据 id 去关联查询
            lock.readLock().lock();
            try {
                ids = articleIndexer.doSearchByLucene(query);

                logger.info("search by lucene ids: [{}]", JsonUtil.toJsonString(ids));

                if(CollectionUtils.isEmpty(ids)){
                    return new PageResult<>(queryParam, 0, Collections.emptyList());
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                logger.error("search by lucene for get index failed! failed reason: [{}]", e.getMessage(), e);
            } finally {
                lock.readLock().unlock();
            }
        }

        HandledArticlePageQueryParam handledArticleQueryParam = new HandledArticlePageQueryParam(queryParam, categoryId, tagId);
        handledArticleQueryParam.setIds(ids);
        if (!BlogContext.isAuthorized()) {
            handledArticleQueryParam.setStatus(ArticleStatusEnum.POSTED);
            handledArticleQueryParam.setQueryPasswordProtect(false);
            handledArticleQueryParam.setQueryPrivate(false);
        }

        int count = articleMapper.selectCount(handledArticleQueryParam);

        if (count == 0) {
            return new PageResult<>(queryParam, 0, Collections.emptyList());
        }

        List<Article> articles = articleMapper.selectPage(handledArticleQueryParam);

        processArticles(articles, BlogContext.isAuthorized());

        return new PageResult<>(queryParam, count, articles);

    }

    /**
     * 文章按年月 归档
     *
     * @param queryParam queryParam
     * @return PageResult<ArticleArchive>
     */
    @Transactional(readOnly = true)
    public PageResult<ArticleArchive> selectArchivePage(ArticleArchivePageQueryParam queryParam) {

        if (!BlogContext.isAuthorized()) {
            queryParam.setStatus(ArticleStatusEnum.POSTED);
            queryParam.setQueryPrivate(false);
            queryParam.setQueryPasswordProtect(false);
        }

        int count = articleMapper.selectCountByArchiveParams(queryParam);

        PageResult<ArticleArchive> pageResult;

        if (count == 0) {
            return new PageResult<>(queryParam, 0, Collections.emptyList());
        }

        List<ArticleArchive> data = articleMapper.selectArchivePage(queryParam);

        if (data == null || data.isEmpty()) {
            return new PageResult<>(queryParam, 0, Collections.emptyList());
        }

        pageResult = new PageResult<>(queryParam, count, data);

        for (ArticleArchive archive : pageResult.getData()) {
            List<Article> articles = archive.getArticles();
            processArticles(articles, BlogContext.isAuthorized());
        }

        return new PageResult<>(queryParam, count, data);
    }

    /**
     * 处理内容
     * <ul>
     *     1. 处理标签
     *     2. 内容 markdown 转 html
     *     3. 生成缩略图
     * </ul>
     *
     * @param articles 待处理的内容列表
     * @param auth     是否登录
     */
    private void processArticles(List<Article> articles, boolean auth) {

        logger.info("this articles will be process. [{}]", JsonUtil.toJsonString(articles));

        if(CollectionUtils.isEmpty(articles)) {
            logger.error("no articles need to be process, because the list is empty");
            return;
        }

        for (Article article : articles) {

            // process access api
            article.setAccessApi(StringUtil.isNotBlank(article.getUrlName()) ? article.getUrlName() : String.valueOf(article.getId()));

            Set<Tag> tags = article.getTags();
            if (!tags.isEmpty()) {
                Iterator<Tag> iterator = tags.iterator();
                while (iterator.hasNext()) {
                    Tag next = iterator.next();
                    Optional<Tag> tagOp = tagMapper.selectById(next.getId());
                    if (tagOp.isPresent()) {
                        next.setTagName(tagOp.get().getTagName());
                    } else {
                        iterator.remove();
                    }
                }
            }
        }

        processArticleContents(articles);

    }


    /**
     * 处理文章的内容 articles
     * @param articles articles
     */
    private void processArticleContents(List<Article> articles) {

        Map<Integer, String> markdownMap = new HashMap<>();
        for (Article article : articles) {
            if (StringUtil.isNotBlank(article.getDigest())) {
                markdownMap.put(-article.getId(), article.getDigest());
            }
            if (StringUtil.isNotBlank(article.getContent())) {
                markdownMap.put(article.getId(), article.getContent());
            }
        }

        if (!CollectionUtils.isEmpty(markdownMap)) {
            Map<Integer, String> htmlMap = markdownParser.parseMap(markdownMap);
            for (Article article : articles) {
                //展示内容的时候，需要过滤出 xss 字符，避免执行脚本
                article.setDigest(htmlMap.get(-article.getId()));
                article.setContent(htmlMap.get(article.getId()));

                //获取文章的第一幅图片，如果存在，则设置为文章的特征图
                Optional<String> featureImageOp = JsoupUtil.getFirstImage(article.getContent());
                featureImageOp.ifPresent(article::setFeatureImage);
            }
        }
    }


    /**
     * 增加内容的点击量
     *
     * @param id id
     * @throws LogicException LogicException
     *    maybe throw exception
     *    1. the article not exists
     *    2. illegal access
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void hit(int id) throws LogicException {
        //登录的情况下点击, 不计算点击量
        if (BlogContext.isAuthorized()) {
            return;
        }

        Article article = articleMapper.selectById(id).orElseThrow(()
                -> new LogicException(BlogConstants.ARTICLE_NOT_EXISTS));
        ArticleStatusEnum status = article.getStatus();
        // use not auth
        // but access resource is !POSTED || privateResource
        if (!ArticleStatusEnum.POSTED.equals(status) || article.getPrivateArticle()) {
            throw new LogicException(BlogConstants.AUTHORIZATION_REQUIRED);
        }

        lock.writeLock().lock();
        try {
            AtomicInteger hits = new AtomicInteger(article.getHits());
            articleMapper.increaseHits(id, hits.incrementAndGet());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 获取待修改的内容
     * @param id id
     * @return Article
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public Article selectArticleForEdit(int id) throws LogicException {
        return articleMapper.selectById(id).orElseThrow(() ->
                new ResourceNotFoundException(BlogConstants.ARTICLE_NOT_EXISTS));
    }

    /**
     * 获取文章, 查看
     * @param idOrUrlName id Or urlName
     * @return Article
     */
    @Transactional(readOnly = true)
    public Article selectArticleForView(String idOrUrlName) throws LogicException {
        Optional<Article> articleOp;
        try {
            Integer id = Integer.parseInt(idOrUrlName);
            articleOp = articleMapper.selectById(id);
        } catch (NumberFormatException e) {
            articleOp = articleMapper.selectByUrlName(idOrUrlName);
        }

        Article article = articleOp.orElseThrow(()
                -> new ResourceNotFoundException(BlogConstants.ARTICLE_NOT_EXISTS));

        ArticleStatusEnum articleStatus = article.getStatus();

        if(!BlogContext.isAuthorized()) {
            if (!ArticleStatusEnum.POSTED.equals(articleStatus) || article.getPrivateArticle()) {
                throw new UnauthorizedException(BlogConstants.AUTHORIZATION_REQUIRED);
            }
        }

        handleArticles(Collections.singletonList(article), BlogContext.isAuthorized());

        return article;
    }

    private void handleArticles(List<Article> articles, Boolean auth) {
        for (Article article : articles) {
            String content = article.getContent();
            article.setContent(markdownParser.parse(content));
        }
    }

    @Override
    public String getModuleName() {
        return Article.class.getSimpleName().toLowerCase();
    }

    /**
     * 查询评论之前先校验
     *
     * @param commentModule commentModule
     * @return Article
     * @throws LogicException 逻辑异常
     *     1. 内容是否存在
     *     2. 未登录的情况下是不允许查询评论的
     */
    @Override
    public Article checkBeforeQuery(CommentModule commentModule) throws LogicException {
        Integer id = commentModule.getId();

        Article article = articleMapper.selectById(id).orElseThrow(()
                -> new LogicException(BlogConstants.ARTICLE_NOT_EXISTS));

        //未登录并且文章状态不为发布, 不允许查询
        if (!BlogContext.isAuthorized() && (!article.getStatus().equals(ArticleStatusEnum.POSTED) || article.getPrivateArticle())) {
            throw new LogicException("permission.reject", "您无无权访问未发布的资源评论");
        }

        return article;
    }

    /**
     * 查询评论前，检查内容状态
     *
     * @param comment       comment
     * @param commentModule commentModule
     *                      可能会抛出如下异常:
     *                      <ul>
     *                         <li>1. 内容不存在异常</li>
     *                         <li>2. 非发布状态，无法对其操作异常</li>
     *                      </ul>
     */
    @Override
    public void checkBeforeSaved(Comment comment, CommentModule commentModule) throws LogicException {

        Integer id = commentModule.getId();

        //查询文章是否存在
        Article article = articleMapper.selectById(id).orElseThrow(()
                -> new LogicException("article.notExists", "内容不存在"));

        // 文章设置了不允许访客评论
        if (!article.getAllowComment() && !BlogContext.isAuthorized()) {
            throw new LogicException("comment.notAllow", "文章已设置成不允许访客评论");
        }

        //文章状态不允许评论
        if (!ArticleStatusEnum.POSTED.equals(article.getStatus())) {
            throw new LogicException("state.notAllow", "非发布状态, 无法对其进行评论");
        }
    }

    /**
     * 增加内容的评论数量
     *
     * @param module module
     * @throws LogicException 逻辑异常
     *                        1. 内容不存在异常
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    @Override
    public void increaseComments(CommentModule module) throws LogicException {
        Article old = articleMapper.selectById(module.getId()).orElseThrow(()
                -> new LogicException(BlogConstants.ARTICLE_NOT_EXISTS));
        lock.writeLock().lock();
        try {
            AtomicInteger comments = new AtomicInteger(old.getComments());
            articleMapper.increaseComments(module.getId(), comments.incrementAndGet());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 更新内容
     *
     * @param article article
     * @throws LogicException LogicException
     *                        1. 内容不存在异常
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void update(Article article) throws AbstractBlogException {
        Article old = articleMapper.selectById(article.getId()).orElseThrow(() ->
                new LogicException(BlogConstants.ARTICLE_NOT_EXISTS));

        Category category = article.getCategory();
        if (category == null || category.getId() == null) {
            throw new LogicException("", "");
        }

        categoryMapper.selectById(category.getId()).orElseThrow(()
                -> new LogicException("category.notExists", "分类不存在"));

        //针对旧的内容状态来设置发布时间
        String urlName = article.getUrlName();
        if(StringUtil.isBlank(urlName)){
            article.setUrlName("");
        }


        article.setModifyAt(LocalDateTime.now());

        articleMapper.update(article);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                lock.writeLock().lock();
                try {
                    articleIndexer.updateDocument(article);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("update index failed when use update article [{}], failed reason: [{}]", article.getId(), e.getMessage(), e);
                } finally {
                    lock.writeLock().unlock();
                }
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        logger.info("articleService initial finished.");

        if(articleIndexer != null && blogProperties != null && blogProperties.isRebuildIndexWhenStartup()){
            logger.info("articleService initial finished. rebuild index flag is :[{}]", blogProperties.isRebuildIndexWhenStartup());
            logger.info("articleService initial finished. starting rebuild index....");
            HandledArticlePageQueryParam handledArticlePageQueryParam = new HandledArticlePageQueryParam();
            handledArticlePageQueryParam.setIgnorePaging(true);

            List<Article> articles = articleMapper.selectPage(handledArticlePageQueryParam);

            if(!articles.isEmpty()){
                articleIndexer.rebuild(articles);
            }
        }
    }

    public String selectArticleForPreview(Integer id) throws LogicException {
        Article old = articleMapper.selectById(id).orElseThrow(() ->
                new LogicException(BlogConstants.ARTICLE_NOT_EXISTS));

        if(!BlogContext.isAuthorized()){
            throw new UnauthorizedException(BlogConstants.AUTHORIZATION_REQUIRED);
        }

        String content = old.getContent();
        return markdownParser.parse(content);
    }

    public static class ArticleScheduledCallable implements Callable<String> {

        private final Article article;

        public ArticleScheduledCallable(Article article) {
            this.article = article;
        }

        @Override
        public String call() throws Exception {

            LocalDateTime postAt = article.getPostAt();


            return null;
        }
    }


    public static class ScheduleManager {

        private static BlockingQueue<Article> blockingQueue;
        private static ExecutorService executorService;


        public ScheduleManager() {
            blockingQueue = new LinkedBlockingDeque<>(10);
            executorService = Executors.newScheduledThreadPool(3);
        }

        public static void addArticle(Article article) {
            try {
                boolean offer = blockingQueue.offer(article, 10, TimeUnit.SECONDS);
                if(offer){
                    //添加成功
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        public static void run(){
//            if(blockingQueue.isEmpty()) {
//                blockingQueue.take();
//            }
//
//            while (true){
//                Article article = blockingQueue.poll(10, TimeUnit.SECONDS);
//                if(article != null){
//                    ArticleScheduledCallable callable = new ArticleScheduledCallable(article);
//                    executorService.submit(callable);
//                }
//            }
//        }
    }
}
