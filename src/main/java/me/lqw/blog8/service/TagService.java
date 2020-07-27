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

@Service
public class TagService extends BaseService<Tag> implements InitializingBean {

    private final TagMapper tagMapper;
    private final ArticleTagMapper articleTagMapper;

    public TagService(TagMapper tagMapper, ArticleTagMapper articleTagMapper) {
        this.tagMapper = tagMapper;
        this.articleTagMapper = articleTagMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Tag save(Tag tag) throws LogicException {
        tagMapper.findByName(tag.getTagName()).ifPresent(e -> {
            throw new LogicException("tagService.save.nameExists", "标签名称已存在");
        });
        tagMapper.insert(tag);
        return tag;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Integer id) throws LogicException {
        Tag tag = tagMapper.findById(id).orElseThrow(() -> new LogicException("tagService.delete.notExists", "标签不存在"));
        articleTagMapper.deleteByTag(tag);
        tagMapper.delete(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Tag tag) throws LogicException {
        Tag old = tagMapper.findById(tag.getId()).orElseThrow(()
                -> new LogicException("tagService.update.notExists", "标签不存在"));

        if(old.getTagName().equals(tag.getTagName())){
            return;
        }
        //不能更新名称已存在的标签
        tagMapper.findByName(tag.getTagName()).ifPresent(e -> {
            throw new LogicException("tagService.update.nameExists", "标签名称已经存在");
        });
        tagMapper.update(tag);
    }

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

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("TagService afterPropertiesSet()...");
    }


    public List<Tag> listAll() {
        return tagMapper.listAll();
    }
}
