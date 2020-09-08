package me.lqw.blog8.web.security;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 二次认证码提交类
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class GoogleTopCode implements Serializable {

    /**
     * 这里对提交的认证码做基本的校验
     * google 的认证码一般都是 6 位，
     * 这里的提示信息不要写 6 位，只能防小白
     */
    @NotBlank(message = "二次验证码不能为空")
    @Length(min = 6, max = 24, message = "二次验证码的长度为 {min} ~ {max} 之间")
    private String topCode;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTopCode() {
        return topCode;
    }

    public void setTopCode(String topCode) {
        this.topCode = topCode;
    }
}
