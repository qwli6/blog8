package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Tag;
import me.lqw.blog8.model.vo.TagPageQueryParam;
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

    int count(TagPageQueryParam queryParam);


    List<Tag> selectPage(TagPageQueryParam queryParam);

    void delete(@Param("id") Integer id);

    List<Tag> listAll();

}
