package me.lqw.blog8.model;


import javax.validation.constraints.Email;
import java.io.Serializable;

/**
 * 描述:
 *     用户信息
 * @author liqiwen
 * @since 2018-08-21 09:37
 */
public class User implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private String name;

    @Email
    private String email;

    private String avatar;

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
