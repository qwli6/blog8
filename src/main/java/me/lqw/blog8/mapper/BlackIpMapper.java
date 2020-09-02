package me.lqw.blog8.mapper;

import me.lqw.blog8.model.BlackIp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 黑名单 ip Mapper
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Mapper
public interface BlackIpMapper {

    /**
     * 根据 ip 查询黑名单
     *
     * @param ip ip
     * @return BlackIp
     */
    Optional<BlackIp> selectByIp(@Param("ip") String ip);

    /**
     * 根据 id 查询黑名单
     *
     * @param id id
     * @return BlackIp
     */
    Optional<BlackIp> selectById(@Param("id") Integer id);

    /**
     * 删除黑名单
     *
     * @param id id
     */
    void deleteById(@Param("id") Integer id);

    /**
     * 插入黑名单
     *
     * @param blackIp blackIp
     */
    void insert(BlackIp blackIp);

    /**
     * 获取所有的黑名单列表
     *
     * @return BlackIp
     */
    List<BlackIp> selectAll();

    /**
     * 更新黑名单
     *
     * @param blackIp blackIp
     */
    void update(BlackIp blackIp);
}
