package me.lqw.blog8.service;

import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.model.AuthToken;
import me.lqw.blog8.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * 记住我
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Component
public class RememberMeService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 记住我
     *
     * @param request  request
     * @param response response
     * @param user     user
     */
    public void remember(HttpServletRequest request, HttpServletResponse response, User user) {

        String username = user.getUsername();
        String password = user.getPassword();

        AuthToken token = AuthToken.createToken(username).expiredTime();

        Cookie cookie;
        Optional<Cookie> rememberOp = getCookie(request, BlogConstants.REMEMBER_ME);
        if (rememberOp.isPresent()) {
            cookie = rememberOp.get();
            cookie.setValue(token.getToken());
        } else {
            cookie = new Cookie(BlogConstants.REMEMBER_ME, token.getToken());
        }
        cookie.setHttpOnly(true);
        cookie.setPath("/console/");
        cookie.setMaxAge(token.getExpiredTime().intValue());
        response.addCookie(cookie);
    }

    /**
     * 自动登录
     *
     * @param request  request
     * @param response response
     * @return boolean
     */
    public boolean autoLogin(HttpServletRequest request, HttpServletResponse response) {
        logger.info("autoLogin...");
        Optional<Cookie> rememberOp = getCookie(request, BlogConstants.REMEMBER_ME);
        //            Cookie cookie = rememberOp.get();
        //            cookie.setMaxAge();
        return rememberOp.isPresent();
    }

    /**
     * 清除记住我
     *
     * @param request  request
     * @param response response
     */
    public void clearRemember(HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> rememberOp = getCookie(request, BlogConstants.REMEMBER_ME);
        if (rememberOp.isPresent()) {
            Cookie cookie = rememberOp.get();
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 获取 cookie
     *
     * @param request request
     * @param key     key
     * @return Cookie
     */
    public Optional<Cookie> getCookie(HttpServletRequest request, String key) {
        if (StringUtils.isEmpty(key)) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies()).filter(e -> e.getName().equals(key)).findFirst();
    }
}
