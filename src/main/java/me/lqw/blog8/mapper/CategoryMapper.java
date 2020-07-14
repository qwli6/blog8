package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CategoryMapper {

    void insert(Category category);

    List<Category> findAll();

    Optional<Category> findByName(@Param("name") String name);

    Optional<Category> findById(@Param("id") Integer id);

    int delete(@Param("id") Integer id);

    void update(Category category);
}
