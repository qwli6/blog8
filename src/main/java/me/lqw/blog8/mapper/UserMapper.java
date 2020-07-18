package me.lqw.blog8.mapper;

import me.lqw.blog8.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {

    Optional<User> findUser(@Param("username") String username,
                            @Param("password") String password);


    Optional<User> findByName(@Param("username") String username);

    int countUser();

    void insert(User user);

    void update(User user);

}
