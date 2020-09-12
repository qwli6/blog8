package me.lqw.blog8.web.aop;

import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.service.ProtectResource;
import me.lqw.blog8.web.security.lock.Lock;
import me.lqw.blog8.web.security.lock.LockKey;
import me.lqw.blog8.web.security.lock.LockManager;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Aspect
@Order(1)
public class LockAspect {

    private final LockManager<?> lockManager;

    public LockAspect(LockManager<?> lockManager) {
        this.lockManager = lockManager;
    }

    @AfterReturning(value = "@within(LockProtected) || @annotation(LockProtected)", returning = "protectResource")
    public void after(ProtectResource protectResource){

        // 需要验证密码
        if (protectResource != null && protectResource.getLockId() != null && !BlogContext.isAuthorized()) {
            Lock lock = lockManager.findLock(protectResource.getLockId());
            if (lock != null) {
                lock.setProtectResource(protectResource);
//                LockKey key = LockKeyContext.getKey(protectResource.getResourceId());
//                lock.tryOpen(key);
            }
        }
    }
}
