package me.lqw.blog8.mapper;

import me.lqw.blog8.model.BlogConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 博客配置 Mapper
 * @author liqiwen
 * @since 1.0
 * @version 1.0
 */
@Mapper
public interface BlogConfigMapper {

    /**
     * 根据 key 名称查找配置
     * @param key key
     * @return BlogConfig
     */
    Optional<BlogConfig> selectByKey(@Param("key") String key);

    /**
     * 更新配置内容
     * @param key key
     * @param value value
     */
    void updateConfig(@Param("key") String key, @Param("value") String value);
}
