package me.lqw.blog8.model;


import me.lqw.blog8.model.vo.UserParam;

import javax.validation.constraints.Email;
import java.io.Serializable;

/**
 * 描述:
 * 用户信息
 *
 * @author liqiwen
 * @version 1.2
 * @since 2018-08-21 09:37
 */
public class User implements Serializable {

    /**
     * 用户 id
     */
    private Integer id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户真实名称
     */
    private String name;

    /**
     * 用户邮箱
     */
    @Email(message = "请提供有效的邮箱地址")
    private String email;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户对象构造方法
     */
    public User() {
        super();
    }

    /**
     * 构造方法
     * @param id id
     */
    public User(Integer id) {
        super();
        this.id = id;
    }

    /**
     * 用户对象构造方法
     * @param username username
     */
    public User(String username) {
        super();
        this.username = username;
    }

    /**
     * 用户对象构造方法
     * @param username username
     * @param password password
     */
    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    /**
     * 构造方法
     * @param userParam userParam
     */
    public User(UserParam userParam) {
        super();
        this.name = userParam.getName();
        this.email = userParam.getEmail();
        this.avatar = userParam.getAvatar();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
