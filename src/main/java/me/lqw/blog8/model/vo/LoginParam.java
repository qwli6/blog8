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

    @NotBlank(message = "请提供登录用户名")
    @Length(min = 4, max = 16, message = "用户名的长度必须在[4, 16]之间")
    private String username;

    @NotBlank(message = "请提供登录密码")
    @Length(min = 4, max = 64, message = "用户名的长度必须在[4, 64]之间")
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
