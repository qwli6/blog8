package me.lqw.blog8.service;

import me.lqw.blog8.mapper.ArticleMapper;
import me.lqw.blog8.mapper.CommentMapper;
import me.lqw.blog8.mapper.MomentMapper;
import me.lqw.blog8.model.DataTotalStatistics;

/**
 * 数据统计
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class DataStatisticsService {

    /**
     * 内容持久层 Mapper
     */
    private final ArticleMapper articleMapper;

    /**
     * 动态持久层 Mapper
     */
    private final MomentMapper momentMapper;

    /**
     * 评论 Mapper
     */
    private final CommentMapper commentMapper;

    /**
     * 构造函数注入
     *
     * @param articleMapper articleMapper
     * @param momentMapper  momentMapper
     * @param commentMapper commentMapper
     */
    public DataStatisticsService(ArticleMapper articleMapper, MomentMapper momentMapper, CommentMapper commentMapper) {
        this.articleMapper = articleMapper;
        this.momentMapper = momentMapper;
        this.commentMapper = commentMapper;
    }


    public DataTotalStatistics dataStatistics() {

//        articleMapper.count


        return new DataTotalStatistics();
    }


}
