package me.lqw.blog8.mapper;

import me.lqw.blog8.model.BlackIp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface BlackIpMapper {

    Optional<BlackIp> findByIp(@Param("ip") String ip);

    Optional<BlackIp> findById(@Param("id") Integer id);

    void delete(@Param("id") Integer id);

    void insert(BlackIp blackIp);
}
