package me.lqw.blog8.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.validation.MessageCodesResolver;

import java.io.Serializable;

/**
 * MessageCodeResolver
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class BlogMessageCodeResolver implements MessageCodesResolver, Serializable {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());


    public BlogMessageCodeResolver() {
        super();
    }

    @NonNull
    @Override
    public String[] resolveMessageCodes(@NonNull String errorCode, @NonNull String objectName) {
        logger.info("校验异常: [{}] ==== [{}]", errorCode, objectName);
        return new String[]{errorCode + "." + objectName};
    }

    @NonNull
    @Override
    public String[] resolveMessageCodes(@NonNull String errorCode, @NonNull String objectName, @NonNull String field, Class<?> fieldType) {
        logger.info("校验异常222: [{}] ==== [{}]===== [{}]", errorCode, objectName, field);
        return new String[]{errorCode + "." + objectName + "." + field};
    }
}
