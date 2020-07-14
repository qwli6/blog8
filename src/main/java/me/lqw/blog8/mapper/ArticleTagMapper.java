package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Article;
import me.lqw.blog8.model.ArticleTag;
import me.lqw.blog8.model.Tag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleTagMapper {


//    @Delete("delete from blog_article_tag where article_id = #{id}")
    void deleteByArticle(Article article);

    void insert(ArticleTag articleTag);

    void batchInsert(List<ArticleTag> articleTags);

    void deleteByTag(Tag tag);
}
