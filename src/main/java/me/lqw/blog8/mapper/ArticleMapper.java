package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Article;
import me.lqw.blog8.model.ArticleArchive;
import me.lqw.blog8.model.vo.ArticleQueryParam;
import me.lqw.blog8.model.vo.HandledArticleQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ArticleMapper {


    void insert(Article article);

    Optional<Article> findByUrlName(@Param("urlName") String urlName);

    int count(HandledArticleQueryParam queryParam);

    List<Article> selectPage(HandledArticleQueryParam queryParam);

    Optional<Article> findById(@Param("id") Integer id);

    void increaseHits(@Param("id") int id, @Param("hits") int hits);

    void delete(@Param("id") Integer id);

    int countArchive(ArticleQueryParam queryParam);

    List<ArticleArchive> selectArchivePage(ArticleQueryParam queryParam);

    void increaseComments(@Param("id") Integer id);
}
