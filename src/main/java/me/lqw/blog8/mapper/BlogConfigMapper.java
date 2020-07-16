package me.lqw.blog8.mapper;

import me.lqw.blog8.model.BlogConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface BlogConfigMapper {


    Optional<BlogConfig> selectByKey(@Param("key") String key);

}
