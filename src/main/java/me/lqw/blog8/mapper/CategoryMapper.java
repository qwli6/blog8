package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 分类 Mapper
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Mapper
public interface CategoryMapper {

    /**
     * 插入分类
     *
     * @param category category
     */
    void insert(Category category);

    /**
     * 获取所有分类
     *
     * @return list
     */
    List<Category> selectAll();

    /**
     * 根据名称获取分类
     *
     * @param name name
     * @return category
     */
    Optional<Category> selectByName(@Param("name") String name);

    /**
     * 根据 id 获取分类
     *
     * @param id id
     * @return category
     */
    Optional<Category> selectById(@Param("id") Integer id);

    /**
     * 删除分类
     *
     * @param id id
     * @return id
     */
    int delete(@Param("id") Integer id);

    /**
     * 更新分类信息
     *
     * @param category category
     */
    void update(Category category);
}
