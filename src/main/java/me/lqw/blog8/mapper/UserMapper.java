package me.lqw.blog8.mapper;

import me.lqw.blog8.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 用户持久层 Mapper
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Mapper
public interface UserMapper {

    /**
     * 查找用户, 根据用户名和密码
     *
     * @param user user
     * @return User
     */
    Optional<User> selectByUser(User user);

    /**
     * 查找用户, 根据用户名
     *
     * @param username username
     * @return User
     */
    Optional<User> selectByName(@Param("username") String username);

    /**
     * 查找用户
     * @param id id
     * @return User
     */
    Optional<User> selectById(@Param("id") Integer id);

    /**
     * 统计用户数量
     *
     * @return int
     */
    int countUser();

    /**
     * 保存用户
     *
     * @param user user
     */
    void insert(User user);

    /**
     * 更新用户
     *
     * @param user user
     */
    void update(User user);

    /**
     * 更新用户密码
     * @param user user
     */
    void updatePassword(User user);

}
