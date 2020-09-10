package me.lqw.blog8.service;

import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.exception.ResourceNotFoundException;
import me.lqw.blog8.mapper.CommentMapper;
import me.lqw.blog8.mapper.MomentMapper;
import me.lqw.blog8.model.Comment;
import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.Moment;
import me.lqw.blog8.model.MomentArchive;
import me.lqw.blog8.model.dto.CommentDTO;
import me.lqw.blog8.model.dto.MomentDTO;
import me.lqw.blog8.model.dto.page.PageResult;
import me.lqw.blog8.model.vo.MomentPageQueryParam;
import me.lqw.blog8.plugins.md.MarkdownParser;
import me.lqw.blog8.util.JsonUtil;
import me.lqw.blog8.util.JsoupUtil;
import me.lqw.blog8.util.StringUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 动态业务处理类
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Service
public class MomentService extends AbstractBaseService<Moment> implements CommentModuleHandler<Moment> {

    /**
     * 动态 Mapper
     */
    private final MomentMapper momentMapper;

    /**
     * 评论 Mapper
     */
    private final CommentMapper commentMapper;

    /**
     * MarkdownParser
     */
    private final MarkdownParser markdownParser;

    /**
     * 构造方法
     *
     * @param momentMapper   momentMapper
     * @param commentMapper  commentMapper
     * @param objectProvider objectProvider
     */
    public MomentService(MomentMapper momentMapper, CommentMapper commentMapper,
                         ObjectProvider<MarkdownParser> objectProvider) {
        this.momentMapper = momentMapper;
        this.commentMapper = commentMapper;
        this.markdownParser = objectProvider.stream().min(Comparator.comparingInt(Ordered::getOrder)).get();
    }

    /**
     * 保存动态
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Moment save(Moment moment) throws LogicException {
        moment.setHits(0);
        moment.setComments(0);
        momentMapper.insert(moment);
        return moment;
    }


    /**
     * 删除动态
     *
     * @param id id
     * @throws LogicException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Integer id) throws LogicException {
        Moment moment = momentMapper.selectById(id).orElseThrow(() -> new LogicException("momentService.delete.notExists", "动态不存在"));
        commentMapper.deleteByModule(new CommentModule(moment.getId(), getModuleName()));
    }

    @Override
    public Moment checkBeforeQuery(CommentModule commentModule) throws LogicException {
        return null;
    }

    /**
     * 插入评论之前先检查
     *
     * @param comment       comment
     * @param commentModule commentModule
     * @throws LogicException 逻辑异常
     *                        1. 动态不存在异常
     *                        2. 动态设置了不允许访客评论
     */
    @Override
    public void checkBeforeSaved(Comment comment, CommentModule commentModule) throws LogicException {
        Integer id = commentModule.getId();

        // 动态是否存在
        Moment moment = momentMapper.selectById(id).orElseThrow(()
                -> new LogicException("moment.notExits", "动态不存在"));

        // 是否设置了允许访客评论
        if (!moment.getAllowComment() && !BlogContext.isAuthorized()) {
            throw new LogicException("comment.notAllow", "该动态设置了不允许访客评论");
        }

    }

    @Override
    public void increaseComments(CommentModule module) throws LogicException {

    }

    @Transactional(readOnly = true)
    public PageResult<Moment> selectPage(MomentPageQueryParam queryParam) {
        int count = momentMapper.count(queryParam);
        if (count == 0) {
            return new PageResult<>(queryParam, 0, Collections.emptyList());
        }
        List<Moment> moments = momentMapper.selectPage(queryParam);
        if (moments.isEmpty()) {
            return new PageResult<>(queryParam, 0, Collections.emptyList());
        }
        PageResult<Moment> pageResult = new PageResult<>(queryParam, count, moments);
        processMomentContent(pageResult.getData());
        return pageResult;
    }

    private void processMomentContent(List<Moment> data) {
        if(!CollectionUtils.isEmpty(data)){
            for(Moment moment: data){
                String content = moment.getContent();
                if(StringUtil.isBlank(content)){
                    continue;
                }
                moment.setContent(markdownParser.parse(content));
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Moment moment) throws LogicException {
        Moment old = momentMapper.selectById(moment.getId()).orElseThrow(() -> new LogicException("momentService.update.notExists", "动态不存在"));
        if (old.getContent().equals(moment.getContent()) && old.getAllowComment().equals(moment.getAllowComment())) {
            logger.info("修改内容一致, 不做处理! 原内容:[{}], 现内容:[{}]", JsonUtil.toJsonString(old), JsonUtil.toJsonString(moment));
            return;
        }
        momentMapper.update(moment);
    }

    @Transactional(readOnly = true)
    public Optional<Moment> getMomentForEdit(Integer id) {
        return momentMapper.selectById(id);
    }

    @Override
    public String getModuleName() {
        return Moment.class.getSimpleName().toLowerCase();
    }

    @Transactional(readOnly = true)
    public MomentArchive selectLatestMoments() {

        MomentArchive momentArchive = momentMapper.selectLatestMoments();

        List<Moment> moments = momentArchive.getMoments();

        for(Moment moment: moments) {
            String content = moment.getContent();
            String htmlContent = markdownParser.parse(content);
            JsoupUtil.getFirstImage(htmlContent).ifPresent(moment::setFeatureImage);

            if(StringUtil.isNotBlank(htmlContent)){
                JsoupUtil.cleanAllHtml(htmlContent).ifPresent(e -> {
                    if(e.length() > 20){
                        e = e.substring(0, 20) + "....";
                    }
                    moment.setContent(e);
                });
            }
        }
        return momentArchive;
    }

    public Optional<Moment> getMomentForView(int id) throws ResourceNotFoundException {
        Optional<Moment> momentOp = momentMapper.selectById(id);
        if (momentOp.isPresent()) {
            Moment moment = momentOp.get();
            moment.setContent(markdownParser.parse(moment.getContent()));
            return Optional.of(moment);
        }
        return Optional.empty();
    }

    public PageResult<MomentArchive> selectMomentArchivePage(MomentPageQueryParam queryParam) {
        int count = momentMapper.countMomentArchive();
        if (count == 0) {
            return new PageResult<>(queryParam, 0, new ArrayList<>());
        }
        List<MomentArchive> momentArchives = momentMapper.selectMomentArchivePage(queryParam);
        if (momentArchives.isEmpty()) {
            return new PageResult<>(queryParam, 0, new ArrayList<>());
        }
        momentArchives.forEach(e -> e.getMoments().forEach(y -> y.setContent(markdownParser.parse(y.getContent()))));
        return new PageResult<>(queryParam, count, momentArchives);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void hit(Integer id) throws LogicException {
        if (BlogContext.isAuthorized()) {
            return;
        }
        momentMapper.selectById(id).orElseThrow(() ->
                new LogicException("momentService.hit.notExists", "点击动态不存在"));
        momentMapper.increaseHits(id, 1);
    }
}
