package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Tag;
import me.lqw.blog8.model.vo.TagQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TagMapper {

    Optional<Tag> findByName(@Param("tagName") String tagName);

    void insert(Tag tag);

    Optional<Tag> findById(@Param("id") Integer id);

    void update(Tag tag);

    int count(TagQueryParam queryParam);


    List<Tag> selectPage(TagQueryParam queryParam);

    void delete(@Param("id") Integer id);
}
