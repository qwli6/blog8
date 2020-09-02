package me.lqw.blog8.mapper;

import me.lqw.blog8.model.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 记录操作日志
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
@Mapper
public interface OperateLogMapper {

    /**
     * 插入操作日志
     *
     * @param operateLog operateLog
     */
    void insert(OperateLog operateLog);

    /**
     * 删除操作日志
     *
     * @param id id
     */
    void delete(@Param("id") Integer id);


    /**
     * 根据创建时间删除
     *
     * @param end end
     */
    void deleteByCreateAt(@Param("end") LocalDateTime end);


    /**
     * 获取最近的几条操作日志
     *
     * @param count count 数量
     * @return List<OperateLog>
     */
    List<OperateLog> selectLatestOperateLogs(@Param("count") int count);

    /**
     * 根据 id 查询
     *
     * @param id id
     */
    Optional<OperateLog> selectById(@Param("id") Integer id);
}
