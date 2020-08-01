package me.lqw.blog8.service;

import me.lqw.blog8.BlogConstants;
import me.lqw.blog8.model.User;
import me.lqw.blog8.model.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * 记住我
 * @author liqiwen
 * @since 1.2
 * @version 1.2
 */
@Component
public class RememberMeService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    /**
     * 记住我
     * @param request request
     * @param response response
     * @param user user
     */
    public void remember(HttpServletRequest request, HttpServletResponse response, User user){

        String username = user.getUsername();
        String password = user.getPassword();

        UserToken token = UserToken.createToken(username).expiredTime();

        Cookie cookie;
        Optional<Cookie> rememberCookieOp = Arrays.stream(request.getCookies())
                .filter(e -> e.getName().equals(BlogConstants.REMEMBER_ME)).findFirst();
        if(rememberCookieOp.isPresent()){
            cookie = rememberCookieOp.get();
            cookie.setValue(token.getToken());
        } else {
            cookie = new Cookie(BlogConstants.REMEMBER_ME, token.getToken());
        }
        cookie.setHttpOnly(true);
        cookie.setPath("/console/");
        cookie.setMaxAge(token.getExpiredTime().intValue());
        response.addCookie(cookie);
    }
}
