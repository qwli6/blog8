package me.lqw.blog8.exception;

import org.springframework.validation.MessageCodesResolver;

public class BlogMessageCodeResolver implements MessageCodesResolver {


    public BlogMessageCodeResolver() {
        super();
    }

    @Override
    public String[] resolveMessageCodes(String s, String s1) {
        return new String[]{s + "." + s1};
    }

    @Override
    public String[] resolveMessageCodes(String s, String s1, String s2, Class<?> aClass) {
        return new String[]{s + "." + s1 + "." + s2};
    }
}
