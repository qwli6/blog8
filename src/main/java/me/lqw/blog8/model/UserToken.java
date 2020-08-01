package me.lqw.blog8.model;

import sun.tools.jstat.Token;

import java.io.Serializable;

/**
 * 用户登录凭证
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 * 生成用户自动登录的凭证
 */
public class UserToken implements Serializable {


    /**
     * 默认记住我登录时长为 7 天
     */
    private static final Long DEFAULT_EXPIRED_TIME = 7*24*3600L;

    private String token;

    private Long expiredTime;

    public static UserToken createToken(String token){
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        return userToken;
    }

    public UserToken expiredTime(){
        this.expiredTime = DEFAULT_EXPIRED_TIME;
        return this;
    }

    public UserToken expiredTime(Long expiredTime){
        if(expiredTime == null || expiredTime < 0){
            this.expiredTime = DEFAULT_EXPIRED_TIME;
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
