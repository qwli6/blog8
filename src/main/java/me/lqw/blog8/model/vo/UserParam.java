package me.lqw.blog8.model.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 待更新的用户参数
 * @author liqiwen
 * @since 1.2
 * @version 1.2
 */
public class UserParam implements Serializable {

    /**
     * 用户真实姓名
     */
    @NotBlank(message = "待更新的用户名称不能为空")
    @Length(message = "姓名长度必须在 {min}~{max} 之间", max = 64)
    private String name;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "待更新的用户邮箱不能为空")
    @Length(message = "邮箱长度必须在 {min}~{max} 之间", max = 64)
    @Email(message = "非法的邮箱格式")
    private String email;

    /**
     * 用户头像
     */
    @NotBlank(message = "待更新的用户头像不能为空")
    @Length(message = "头像地址的长度必须在 {min}~{max} 之间", max = 64)
    @URL(message = "非法的头像地址")
    private String avatar;

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
