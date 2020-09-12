package me.lqw.blog8.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.exception.AbstractBlogException;
import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.exception.ResourceNotFoundException;
import me.lqw.blog8.exception.UnauthorizedException;
import me.lqw.blog8.mapper.CommentMapper;
import me.lqw.blog8.mapper.MomentMapper;
import me.lqw.blog8.model.Comment;
import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.Moment;
import me.lqw.blog8.model.MomentArchive;
import me.lqw.blog8.model.dto.CommentDTO;
import me.lqw.blog8.model.dto.MomentDTO;
import me.lqw.blog8.model.dto.MomentNavDTO;
import me.lqw.blog8.model.dto.page.PageResult;
import me.lqw.blog8.model.vo.MomentNavQueryParam;
import me.lqw.blog8.model.vo.MomentPageQueryParam;
import me.lqw.blog8.plugins.md.MarkdownParser;
import me.lqw.blog8.util.HtmlUtil;
import me.lqw.blog8.util.JsonUtil;
import me.lqw.blog8.util.JsoupUtil;
import me.lqw.blog8.util.StringUtil;
import me.lqw.blog8.web.security.lock.LockProtect;
import org.springframework.beans.BeanUtils;
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
     * @param moment 保存的动态
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
     * @param id id id
     * @throws LogicException 逻辑异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Integer id) throws LogicException {
        Moment moment = momentMapper.selectById(id).orElseThrow(() ->
                new LogicException("moment.notExists", "动态不存在"));
        //删除模块评论
        commentMapper.deleteByModule(new CommentModule(moment.getId(), getModuleName()));
        //删除动态
        momentMapper.deleteById(moment.getId());
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
                -> new LogicException(BlogConstants.MOMENT_NOT_EXISTS));

        // 是否设置了允许访客评论
        if (!moment.getAllowComment() && !BlogContext.isAuthorized()) {
            throw new LogicException("comment.notAllow", "该动态设置了不允许访客评论");
        }

    }

    @Override
    public void increaseComments(CommentModule module) throws LogicException {

    }

    /**
     * 分页查找动态
     * @param queryParam queryParam
     * @return PageResult<T>
     */
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

    /**
     * 处理动态内容
     * @param data data
     */
    private void processMomentContent(List<Moment> data) {
        if(!CollectionUtils.isEmpty(data)){
            for(Moment moment: data){
                String content = moment.getContent();
                if(StringUtil.isBlank(content)){
                    continue;
                }
                String parse = markdownParser.parse(content);

                if(StringUtil.isNotBlank(parse)) {

                    JsoupUtil.getFirstImage(parse).ifPresent(moment::setFeatureImage);

                    Optional<String> contentOp = JsoupUtil.cleanAllHtml(parse);
                    if(contentOp.isPresent()){
                        content = contentOp.get();
                        if(content.length() > 20){
                           content = content.substring(0, 20)+"...";
                        }
                    }
                }
                moment.setContent(content);
            }
        }
    }

    /**
     * 更新动态
     * @param moment moment
     * @throws LogicException 逻辑异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Moment moment) throws AbstractBlogException {
        Moment old = momentMapper.selectById(moment.getId()).orElseThrow(()
                -> new LogicException(BlogConstants.MOMENT_NOT_EXISTS));
        if (old.getContent().equals(moment.getContent()) && //内容一致
                old.getAllowComment().equals(moment.getAllowComment()) && //是否允许评论一致
                old.getPrivateMoment().equals(moment.getPrivateMoment())) { //是否私有内容一致
            logger.info("修改内容一致, 不做处理! 原内容:[{}], 现内容:[{}]", JsonUtil.toJsonString(old), JsonUtil.toJsonString(moment));
            return;
        }
        momentMapper.update(moment);
    }

    /**
     * 获取动态内容以作修改
     * @param id id
     * @return Moment
     */
    @Transactional(readOnly = true)
    public Moment getMomentForEdit(Integer id) throws AbstractBlogException {
        return momentMapper.selectById(id).orElseThrow(() -> new ResourceNotFoundException(BlogConstants.MOMENT_NOT_EXISTS));
    }


    /**
     * 获取最近的动态
     * @return MomentArchive
     */
    @Transactional(readOnly = true)
    public MomentArchive selectLatestMoments() {

        MomentPageQueryParam queryParam = new MomentPageQueryParam();

        Boolean authorized = BlogContext.isAuthorized();
        if(!authorized){ //未登录
            queryParam.setQueryPrivate(false);
        }

        //
        MomentArchive momentArchive = momentMapper.selectLatestMoments(queryParam);

        List<Moment> moments = momentArchive.getMoments();

        processMomentContent(moments);

        return momentArchive;
    }

    /**
     * 展示动态
     * @param id id
     * @return Moment
     * @throws AbstractBlogException
     * 资源找不到异常
     */
    @Transactional(readOnly = true)
    public Moment getMomentForView(int id) throws AbstractBlogException {
        Moment moment = momentMapper.selectById(id).orElseThrow(()
                -> new ResourceNotFoundException(BlogConstants.MOMENT_NOT_EXISTS));

        Boolean privateMoment = moment.getPrivateMoment();
        if(privateMoment != null && privateMoment && !BlogContext.isAuthorized()){
            throw new UnauthorizedException(BlogConstants.AUTHORIZATION_REQUIRED);
        }
        moment.setContent(markdownParser.parse(moment.getContent()));
        return moment;
    }


    /**
     * 获取动态导航 上一个，下一个
     * @param id id
     * @return MomentNavDTO
     */
    @Transactional(readOnly = true)
    public MomentNavDTO selectMomentNav(int id) {

        Optional<Moment> currentMomentOp = momentMapper.selectById(id);
        if(!currentMomentOp.isPresent()){
            return new MomentNavDTO(null, null);
        }
        Moment currentMoment = currentMomentOp.get();

        MomentNavQueryParam queryParam = new MomentNavQueryParam();
        queryParam.setCurrentId(currentMoment.getId());

        Boolean queryPrivate = null;

        if(!BlogContext.isAuthorized()){
            queryPrivate = false;
        }
        queryParam.setQueryPrivate(queryPrivate);

        Optional<Moment> nextMomentOp = momentMapper.selectNextMoment(queryParam);
        Optional<Moment> prevMomentOp = momentMapper.selectPrevMoment(queryParam);

        MomentNavDTO momentNavDto = new MomentNavDTO();
        if(nextMomentOp.isPresent()){
            Moment moment = nextMomentOp.get();
            processMomentContent(Collections.singletonList(moment));
            momentNavDto.setNext(moment);
        }

        if(prevMomentOp.isPresent()){
            Moment moment = prevMomentOp.get();
            processMomentContent(Collections.singletonList(moment));
            momentNavDto.setPrev(moment);
        }

        return momentNavDto;
    }


    /**
     * 归档动态分页大小
     * @param queryParam queryParam
     * @return PageResult<T>
     */
    @Transactional(readOnly = true)
    public PageResult<MomentArchive> selectMomentArchivePage(MomentPageQueryParam queryParam) {
        Boolean authorized = BlogContext.isAuthorized();
        if(!authorized){
            queryParam.setQueryPrivate(false);
        }
        int count = momentMapper.countMomentArchive(queryParam);
        if (count == 0) {
            return new PageResult<>(queryParam, 0, new ArrayList<>());
        }
        List<MomentArchive> momentArchives = momentMapper.selectMomentArchivePage(queryParam);
        if (momentArchives.isEmpty()) {
            return new PageResult<>(queryParam, 0, new ArrayList<>());
        }
        momentArchives.forEach(e -> processMomentContent(e.getMoments()));
        return new PageResult<>(queryParam, count, momentArchives);
    }

    /**
     * 动态点击
     * @param id id
     * @throws AbstractBlogException 逻辑异常
     * 动态不存在
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void hit(Integer id) throws AbstractBlogException {
        if (BlogContext.isAuthorized()) {
            return;
        }
        momentMapper.selectById(id).orElseThrow(() ->
                new LogicException(BlogConstants.MOMENT_NOT_EXISTS));
        momentMapper.increaseHits(id, 1);
    }


    @Override
    public String getModuleName() {
        return Moment.class.getSimpleName().toLowerCase();
    }
}
