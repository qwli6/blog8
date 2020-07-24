package me.lqw.blog8.service;

import me.lqw.blog8.BlogContext;
import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.exception.ResourceNotFoundException;
import me.lqw.blog8.mapper.CommentMapper;
import me.lqw.blog8.mapper.MomentMapper;
import me.lqw.blog8.model.Comment;
import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.Moment;
import me.lqw.blog8.model.MomentArchive;
import me.lqw.blog8.model.dto.PageResult;
import me.lqw.blog8.model.vo.MomentQueryParam;
import me.lqw.blog8.util.JacksonUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MomentService extends BaseService<Moment> implements CommentModuleHandler<Moment>, InitializingBean {


    private final MomentMapper momentMapper;
    private final CommentMapper commentMapper;
    private final MarkdownHandler mdHandler;


    public MomentService(MomentMapper momentMapper, CommentMapper commentMapper,
                         MarkdownHandler handler) {
        this.momentMapper = momentMapper;
        this.commentMapper = commentMapper;
        this.mdHandler = handler;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Moment save(Moment moment) throws LogicException {
        moment.setHits(0);
        moment.setComments(0);
        momentMapper.insert(moment);
        return moment;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Integer id) throws LogicException {
        Moment moment = momentMapper.findById(id).orElseThrow(() -> new LogicException("momentService.delete.notExists", "动态不存在"));
        commentMapper.deleteByModule(new CommentModule(moment.getId(), getModuleName()));
    }

    @Override
    public Moment checkBeforeQuery(CommentModule commentModule) throws LogicException {
        return null;
    }

    @Override
    public void checkBeforeSaved(Comment comment, CommentModule commentModule) throws LogicException {

    }

    @Override
    public void increaseComments(CommentModule module) throws LogicException {

    }

    @Transactional(readOnly = true)
    public PageResult<Moment> selectPage(MomentQueryParam queryParam) {
        Integer count = momentMapper.count(queryParam);
        if(count == null || count == 0){
            return new PageResult<>(queryParam, 0, new ArrayList<>());
        }
        List<Moment> moments = momentMapper.selectPage(queryParam);
        if(moments.isEmpty()){
            return new PageResult<>(queryParam, 0, new ArrayList<>());
        }
        return new PageResult<>(queryParam, count, moments);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Moment moment) throws LogicException {
        Moment old = momentMapper.findById(moment.getId()).orElseThrow(() -> new LogicException("momentService.update.notExists", "动态不存在"));
        if(old.getContent().equals(moment.getContent()) && old.getAllowComment().equals(moment.getAllowComment())){
            logger.info("修改内容一致, 不做处理! 原内容:[{}], 现内容:[{}]", JacksonUtil.toJsonString(old), JacksonUtil.toJsonString(moment));
            return;
        }
        momentMapper.update(moment);
    }

    @Transactional(readOnly = true)
    public Optional<Moment> getMomentForEdit(Integer id) {
        return momentMapper.findById(id);
    }

    @Override
    public String getModuleName() {
        return Moment.class.getSimpleName().toUpperCase();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("MomentService afterPropertiesSet()...");
    }


    @Transactional(readOnly = true)
    public MomentArchive selectLatestMoments() {
        MomentArchive momentArchive = momentMapper.selectLatestMoments();
        momentArchive.getMoments().forEach(e -> e.setContent(mdHandler.toHtml(e.getContent())));

        return momentArchive;
    }

    public Moment getMomentForView(int id) throws ResourceNotFoundException {
        Moment moment = momentMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("momentService.get.notExists", "资源不存在"));
        moment.setContent(mdHandler.toHtml(moment.getContent()));
        return moment;
    }

    public PageResult<MomentArchive> selectMomentArchivePage(MomentQueryParam queryParam) {
        int count = momentMapper.countMomentArchive();
        if(count == 0){
            return new PageResult<>(queryParam, 0, new ArrayList<>());
        }
        List<MomentArchive> momentArchives = momentMapper.selectMomentArchivePage(queryParam);
        if(momentArchives.isEmpty()){
            return new PageResult<>(queryParam, 0, new ArrayList<>());
        }
        momentArchives.forEach(e -> e.getMoments().forEach(y -> y.setContent(mdHandler.toHtml(y.getContent()))));
        return new PageResult<>(queryParam, count, momentArchives);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void hit(Integer id) throws LogicException {
        if(BlogContext.isAuthorized()){
            return;
        }
        momentMapper.findById(id).orElseThrow(() ->
                new LogicException("momentService.hit.notExists", "点击动态不存在"));
        momentMapper.increaseHits(id, 1);
    }
}
