package me.lqw.blog8.service;

import me.lqw.blog8.BlogContext;
import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.exception.UnauthorizedException;
import me.lqw.blog8.mapper.UserMapper;
import me.lqw.blog8.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户业务实现类
 * @author liqiwen
 * @version 1.0
 */
@Service
public class UserService implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User userAuth(String username, String password) throws LogicException {

        Optional<User> userOp = userMapper.findUser(username, password);
        if(userOp.isPresent()){

            User user = userOp.get();
            user.setPassword(null);
            BlogContext.AUTH_THREAD_LOCAL.set(true);
            return user;
        }
        throw new UnauthorizedException("user.auth.fail", "用户认证失败");
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("UserService#afterPropertiesSet()...");
    }


}
