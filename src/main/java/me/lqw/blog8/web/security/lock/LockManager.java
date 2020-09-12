package me.lqw.blog8.web.security.lock;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LockManager<T> {

    private Map<String, T> lockMap;




    public Lock findLock(String lockId) {

        if(lockId != null ){
            lockMap = new ConcurrentHashMap<>();
        }

        return null;
//        return (Lock) lockMap.get(lockId);
    }


}
