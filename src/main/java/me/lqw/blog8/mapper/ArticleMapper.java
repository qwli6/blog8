package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Article;
import me.lqw.blog8.model.ArticleArchive;
import me.lqw.blog8.model.vo.ArticleArchivePageQueryParam;
import me.lqw.blog8.model.vo.HandledArticlePageQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 文章持久类处理
 * @author liqiwen
 * @version 1.0
 */
@Mapper
public interface ArticleMapper {

    /**
     * 插入内容
     * @param article article
     */
    void insert(Article article);

    /**
     * 根据 urlName 查找文章
     * @param urlName urlName
     * @return Article
     */
    Optional<Article> selectByUrlName(@Param("urlName") String urlName);

    /**
     * 按条件获取文章数量
     * @param queryParam queryParam
     * @return integer
     */
    Integer selectCount(HandledArticlePageQueryParam queryParam);

    /**
     * 分页查找文章
     * @param queryParam queryParam
     * @return list
     */
    List<Article> selectPage(HandledArticlePageQueryParam queryParam);

    /**
     * 根据订单 id 查找文章
     * @param id id
     * @return Article
     */
    Optional<Article> selectById(@Param("id") Integer id);

    /**
     * 增加文章的点击量
     * @param id id
     * @param hits hits
     */
    void increaseHits(@Param("id") int id, @Param("hits") int hits);

    /**
     * 根据 id 删除文章
     * @param id id
     */
    void deleteById(@Param("id") Integer id);

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

    /**
     * 根据 id 增加文章的评论数
     * @param id id
     */
    void increaseComments(@Param("id") Integer id);

    /**
     * 更新内容，根据主键更新
     * @param article article
     */
    void update(Article article);
}
