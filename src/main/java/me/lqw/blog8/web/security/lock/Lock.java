package me.lqw.blog8.web.security.lock;

import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.model.User;
import me.lqw.blog8.service.ProtectResource;

import javax.servlet.http.HttpServletRequest;

public abstract class Lock {

    private String id;

    private String name;

    private User user;

    private ProtectResource protectResource;

    /**
     * 从请求中获取钥匙
     *
     * @param request
     */
    public abstract LockKey getKeyFromRequest(HttpServletRequest request);

    /**
     * 开锁
     */
    public abstract void tryOpen(LockKey key) throws LogicException;

    /**
     * 解锁地址
     *
     * @return
     * @throws LogicException
     */
    public abstract String keyInputUrl() throws LogicException;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProtectResource getProtectResource() {
        return protectResource;
    }

    public void setProtectResource(ProtectResource protectResource) {
        this.protectResource = protectResource;
    }
}
