package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Tag;
import me.lqw.blog8.model.vo.TagPageQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 标签持久化操作类
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
@Mapper
public interface TagMapper {

    /**
     * 根据名称查找标签
     *
     * @param tagName tagName
     * @return Tag
     */
    Optional<Tag> selectByName(@Param("tagName") String tagName);

    /**
     * 插入标签
     *
     * @param tag tag
     */
    void insert(Tag tag);

    /**
     * 根据 id 查找标签
     *
     * @param id id
     * @return Tag
     */
    Optional<Tag> selectById(@Param("id") Integer id);

    /**
     * 更新标签, 这里只更新了标签名称
     *
     * @param tag tag
     */
    void update(Tag tag);

    /**
     * 统计标签的数量, 用来分页
     *
     * @param queryParam queryParam
     * @return int
     */
    int count(TagPageQueryParam queryParam);

    /**
     * 分页查找标签
     *
     * @param queryParam queryParam
     * @return list
     */
    List<Tag> selectPage(TagPageQueryParam queryParam);

    /**
     * 删除标签, 根据 id 删除
     *
     * @param id id
     */
    void deleteById(@Param("id") Integer id);

    /**
     * 查找全部标签
     *
     * @return list
     */
    List<Tag> selectAll();

}
