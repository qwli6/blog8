package me.lqw.blog8.model;

import me.lqw.blog8.constants.BlogConstants;

import java.io.Serializable;

/**
 * 用户登录凭证
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 * 生成用户自动登录的凭证
 */
public class AuthToken implements Serializable {

    /**
     * 用户 token
     */
    private String token;

    /**
     * 有效期
     */
    private Long expiredTime;

    /**
     * 创建 UserToken
     *
     * @param token token
     * @return UserToken
     */
    public static AuthToken createToken(String token) {
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        return authToken;
    }

    /**
     * 设置过期时间
     *
     * @return UserToken
     */
    public AuthToken expiredTime() {
        this.expiredTime = BlogConstants.DEFAULT_EXPIRED_TIME;
        return this;
    }

    /**
     * 设置过期时间
     *
     * @param expiredTime 过期时间
     * @return UserToken
     */
    public AuthToken expiredTime(Long expiredTime) {
        if (expiredTime == null || expiredTime < 0) {
            this.expiredTime = BlogConstants.DEFAULT_EXPIRED_TIME;
            return this;
        }
        this.expiredTime = expiredTime;
        return this;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }
}
