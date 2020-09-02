package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Article;
import me.lqw.blog8.model.ArticleTag;
import me.lqw.blog8.model.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文章标签关联 Mapper
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Mapper
public interface ArticleTagMapper {

    /**
     * 根据文章删除关联关系
     *
     * @param article article
     */
    void deleteByArticle(Article article);

    /**
     * 插入关联关系
     *
     * @param articleTag articleTag
     */
    void insert(ArticleTag articleTag);

    /**
     * 批量插入关联关系
     *
     * @param articleTags articleTags
     */
    void batchInsert(List<ArticleTag> articleTags);

    /**
     * 根据 tag 删除关联关系
     *
     * @param tag tag
     */
    void deleteByTag(Tag tag);
}
