package me.lqw.blog8.model.vo;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 更新密码入参
 * @author liqiwen
 * @since 1.2
 * @version 1.2
 */
public class PasswordParam implements Serializable {

    @NotBlank(message = "旧密码不能为空")
    @Length(message = "密码长度必须在 {min}~{max} 之间", min = 4, max = 64)
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Length(message = "密码长度必须在 {min}~{max} 之间", min = 4, max = 64)
    private String newPassword;

    @NotBlank(message = "重复密码不能为空")
    @Length(message = "密码长度必须在 {min}~{max} 之间", min = 4, max = 64)
    private String repeatPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
