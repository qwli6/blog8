package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Comment;
import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.vo.HandledCommentPageQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 评论操作持久化类
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Mapper
public interface CommentMapper {

    /**
     * 插入评论
     *
     * @param comment comment
     */
    void insert(Comment comment);

    /**
     * 根据模块删除评论
     *
     * @param commentModule commentModule
     */
    void deleteByModule(CommentModule commentModule);

    /**
     * 根据 id 查询评论
     *
     * @param id id
     * @return Comment
     */
    Optional<Comment> selectById(@Param("id") Integer id);

    /**
     * 根据 ip 获取最近的评论
     *
     * @param ip ip
     * @return comment
     */
    Optional<Comment> findLastCommentByIp(@Param("ip") String ip);

    Optional<Comment> selectLatestByModuleAndIp(@Param("module") CommentModule module, @Param("ip") String ip);

    /**
     * 根据 id 删除评论
     *
     * @param id id
     */
    void deleteById(@Param("id") Integer id);

    /**
     * 删除子评论
     *
     * @param id id
     */
    void deleteChildren(@Param("id") Integer id);

    /**
     * 查询评论数量
     *
     * @param queryParam queryParam
     * @return int
     */
    int selectCount(HandledCommentPageQueryParam queryParam);

    /**
     * 分页查询评论
     *
     * @param queryParam queryParam
     * @return List
     */
    List<Comment> selectPage(HandledCommentPageQueryParam queryParam);

    /**
     * 查询父评论
     *
     * @param id id
     * @return Comment
     */
    Optional<Comment> selectParentById(@Param("id") Integer id);

    /**
     * 更新评论
     *
     * @param comment comment
     */
    void update(Comment comment);
}
