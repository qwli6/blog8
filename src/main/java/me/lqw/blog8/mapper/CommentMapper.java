package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Comment;
import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.vo.CommentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentMapper {

    void insert(Comment comment);

    void deleteByModule(CommentModule commentModule);

    Optional<Comment> findById(@Param("id") Integer id);

    Optional<Comment> findLastCommentByIp(@Param("ip") String ip);

    Optional<Comment> findLatestByModuleAndIp(@Param("module") CommentModule module, @Param("ip") String ip);

    void delete(@Param("id") Integer id);

    void deleteChildren(@Param("id") Integer id);

    int countByQueryParam(CommentQueryParam queryParam);

    List<Comment> selectPage(CommentQueryParam queryParam);

    Optional<Comment> findParentCommentById(@Param("id") Integer id);
}
