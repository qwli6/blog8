package me.lqw.blog8.service;

import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.exception.AbstractBlogException;
import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.exception.UnauthorizedException;
import me.lqw.blog8.mapper.UserMapper;
import me.lqw.blog8.model.User;
import me.lqw.blog8.util.SecurityUtil;
import me.lqw.blog8.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 用户业务实现类
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
@Service
public class UserService extends AbstractBaseService<User> {

    /**
     * 用户操作持久类
     */
    private final UserMapper userMapper;

    /**
     * 构造函数注入
     *
     * @param userMapper userMapper
     */
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 用户认证
     * @param username username
     * @param password password
     * @return User
     * @throws AbstractBlogException 逻辑异常
     */
    @Transactional(readOnly = true)
    public User userAuth(String username, String password) throws AbstractBlogException {

        User user = new User(username);

        //user not found
        user = userMapper.selectByUser(user).orElseThrow(()
                -> new UnauthorizedException(BlogConstants.AUTH_FAILED));

        //encode password
//        String encodePassword = SecurityUtil.encodePasswordUseMd5(password);

        //password mismatch
        if(StringUtil.isBlank(user.getPassword()) || !user.getPassword().equals(password)){
            throw new UnauthorizedException(BlogConstants.AUTH_FAILED);
        }

        //set password is null
        user.setPassword(null);
        //set ThreadLocal
        BlogContext.AUTH_THREAD_LOCAL.set(true);

        return user;
    }


    /**
     * 保存用户
     *
     * @param user user
     * @return User
     * @throws LogicException 异常
     */
    @Override
    public User save(User user) throws AbstractBlogException {
        return null;
    }

    /**
     * delete user
     * @param id id
     * @throws AbstractBlogException logicException
     */
    @Override
    public void delete(Integer id) throws AbstractBlogException {

    }

    /**
     * update user
     * @param user user
     * @throws AbstractBlogException logicException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(User user) throws AbstractBlogException {
        user = userMapper.selectById(user.getId()).orElseThrow(()
                -> new LogicException("user.notExists", "用户不存在"));

        userMapper.update(user);
    }

    /**
     * update password
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(Integer id, String password) throws AbstractBlogException {
        User user = userMapper.selectById(id).orElseThrow(() -> new LogicException("user.notExists", "用户不存在"));

        String encodePassword = SecurityUtil.encodePasswordUseMd5(password);

        if(StringUtil.isBlank(encodePassword) || encodePassword.equals(user.getPassword())){
            throw new LogicException("password.same", "更新密码与原密码相同");
        }

        User updateUser = new User(id);
        updateUser.setPassword(encodePassword);

        userMapper.updatePassword(updateUser);
    }
}
