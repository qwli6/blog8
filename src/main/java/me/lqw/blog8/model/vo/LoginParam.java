package me.lqw.blog8.model.vo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录入参
 * @author liqiwen
 * @version 1.0
 */
public class LoginParam implements Serializable {

    @NotBlank(message = "please provide an username.")
    @Length(min = 4, max = 16, message = "username length must between [4, 16].")
    private String username;

    @NotBlank(message = "please provide a password.")
    @Length(min = 4, max = 64, message = "username length must between [4, 64].")
    private String password;

    /**
     * 记住我, 默认不记住
     * @since 1.2
     */
    private Boolean rememberMe = false;

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
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
}
