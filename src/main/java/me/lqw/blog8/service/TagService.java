package me.lqw.blog8.service;

import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.mapper.ArticleTagMapper;
import me.lqw.blog8.mapper.TagMapper;
import me.lqw.blog8.model.Tag;
import me.lqw.blog8.model.dto.PageResult;
import me.lqw.blog8.model.vo.TagPageQueryParam;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签实现类
 * @author liqiwen
 * @since 1.2
 * @version 1.2
 */
@Service
public class TagService extends BaseService<Tag> implements InitializingBean {

    /**
     * 标签操作 Mapper
     */
    private final TagMapper tagMapper;

    /**
     * 文章标签关联操作 Mapper
     */
    private final ArticleTagMapper articleTagMapper;

    /**
     * 构造方法注入
     * @param tagMapper tagMapper
     * @param articleTagMapper articleTagMapper
     */
    public TagService(TagMapper tagMapper, ArticleTagMapper articleTagMapper) {
        this.tagMapper = tagMapper;
        this.articleTagMapper = articleTagMapper;
    }

    /**
     * 保存标签
     * @param tag tag
     * @return Tag
     * @throws LogicException LogicException
     * 1. 标签名称存在异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Tag save(Tag tag) throws LogicException {
        tagMapper.selectByName(tag.getTagName()).ifPresent(e -> {
            throw new LogicException("tagService.save.nameExists", "标签名称已存在");
        });
        tagMapper.insert(tag);
        return tag;
    }

    /**
     * 删除标签
     * @param id id
     * @throws LogicException LogicException
     * 1. 标签不存在异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Integer id) throws LogicException {
        Tag tag = tagMapper.selectById(id).orElseThrow(() -> new LogicException("tagService.delete.notExists", "标签不存在"));

        //删除标签和文章的关联关系
        articleTagMapper.deleteByTag(tag);

        //删除标签
        tagMapper.deleteById(id);
    }

    /**
     * 更新标签
     * @param tag tag
     * @throws LogicException LogicException
     * 1. 标签不存在异常
     * 2. 名称已存在异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Tag tag) throws LogicException {
        Tag old = tagMapper.selectById(tag.getId()).orElseThrow(()
                -> new LogicException("tagService.update.notExists", "标签不存在"));

        if(old.getTagName().equals(tag.getTagName())){
            return;
        }
        //不能更新名称已存在的标签
        tagMapper.selectByName(tag.getTagName()).ifPresent(e -> {
            throw new LogicException("tagService.update.nameExists", "标签名称已经存在");
        });
        tagMapper.update(tag);
    }

    /**
     * 分页查找标签
     * @param queryParam queryParam
     * @return 标签列表
     */
    @Transactional(readOnly = true)
    public PageResult<Tag> selectPage(TagPageQueryParam queryParam) {
        int count = tagMapper.count(queryParam);
        if(count == 0){
            return new PageResult<>(queryParam, 0, new ArrayList<>());
        }
        List<Tag> tags = tagMapper.selectPage(queryParam);
        if(tags.isEmpty()){
            return new PageResult<>(queryParam, 0, new ArrayList<>());
        }
        return new PageResult<>(queryParam, count, tags);
    }

    /**
     * 获取所有标签
     * @return list
     */
    public List<Tag> listAll() {
        return tagMapper.listAll();
    }

    /**
     * 初始化完毕调用
     * @throws Exception Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("TagService afterPropertiesSet");
    }
}
