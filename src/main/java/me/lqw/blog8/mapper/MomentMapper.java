package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Moment;
import me.lqw.blog8.model.MomentArchive;
import me.lqw.blog8.model.vo.MomentNavQueryParam;
import me.lqw.blog8.model.vo.MomentPageQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 动态持久层 Mapper
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Mapper
public interface MomentMapper {

    /**
     * 插入动态
     *
     * @param moment moment
     */
    void insert(Moment moment);

    /**
     * 查询动态数量
     *
     * @param queryParam queryParam
     * @return integer
     */
    int count(MomentPageQueryParam queryParam);

    /**
     * 分页查询动态列表
     *
     * @param queryParam queryParam
     * @return list
     */
    List<Moment> selectPage(MomentPageQueryParam queryParam);

    /**
     * 根据 id 查询动态
     *
     * @param id id
     * @return Moment
     */
    Optional<Moment> selectById(@Param("id") Integer id);

    /**
     * 更新动态
     *
     * @param moment moment
     */
    void update(Moment moment);

    /**
     * 获取最近的动态
     *
     * @return MomentArchive
     */
    MomentArchive selectLatestMoments(MomentPageQueryParam queryParam);

    /**
     * 归档数量
     *
     * @return int
     */
    int countMomentArchive(MomentPageQueryParam queryParam);

    /**
     * 查询归档
     *
     * @param queryParam queryParam
     * @return list
     */
    List<MomentArchive> selectMomentArchivePage(MomentPageQueryParam queryParam);

    /**
     * 增加点击量
     *
     * @param id   id
     * @param hits hits
     */
    void increaseHits(@Param("id") Integer id, @Param("hits") int hits);

    /**
     * 更新评论数量
     *
     * @param id       id
     * @param comments comments
     */
    void updateCommentCount(@Param("id") Integer id, @Param("comments") Integer comments);

    /**
     * 更新评论数量
     * @param id id
     */
    void increaseComments(@Param("id") Integer id);

    /**
     * 删除动态
     * @param id id
     */
    void deleteById(@Param("id") Integer id);

    /**
     * 下一个
     * @param queryParam queryParam
     * @return Moment
     */
    Optional<Moment> selectNextMoment(MomentNavQueryParam queryParam);

    /**
     * 上一个
     * @param queryParam queryParam
     * @return Moment
     */
    Optional<Moment> selectPrevMoment(MomentNavQueryParam queryParam);
}
