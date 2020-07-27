package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Article;
import me.lqw.blog8.model.ArticleArchive;
import me.lqw.blog8.model.vo.ArticleArchivePageQueryParam;
import me.lqw.blog8.model.vo.ArticlePageQueryParam;
import me.lqw.blog8.model.vo.HandledArticlePageQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ArticleMapper {


    void insert(Article article);

    Optional<Article> findByUrlName(@Param("urlName") String urlName);

    int count(HandledArticlePageQueryParam queryParam);

    List<Article> selectPage(HandledArticlePageQueryParam queryParam);

    Optional<Article> findById(@Param("id") Integer id);

    void increaseHits(@Param("id") int id, @Param("hits") int hits);

    void delete(@Param("id") Integer id);

    /**
     * 查询归档的内容数量
     * @param queryParam queryParam
     * @return int
     */
    int selectCountByArchiveParams(ArticleArchivePageQueryParam queryParam);

    /**
     * 查询文章归档的数据
     * @param queryParam queryParam
     * @return List<ArticleArchive>
     */
    List<ArticleArchive> selectArchivePage(ArticleArchivePageQueryParam queryParam);

    void increaseComments(@Param("id") Integer id);
}
