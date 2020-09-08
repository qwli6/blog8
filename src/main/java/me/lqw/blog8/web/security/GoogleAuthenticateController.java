package me.lqw.blog8.web.security;

import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.model.User;
import me.lqw.blog8.util.StringUtil;
import me.lqw.blog8.web.controller.console.AbstractBaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * google 二次认证器
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
@Controller
public class GoogleAuthenticateController extends AbstractBaseController {

    /**
     * google 认证帮助类
     */
    private final GoogleAuthenticateHelper googleAuthenticateHelper;

    /**
     * 构造方法
     * @param googleAuthenticateHelper googleAuthenticateHelper
     */
    public GoogleAuthenticateController(GoogleAuthenticateHelper googleAuthenticateHelper) {
        this.googleAuthenticateHelper = googleAuthenticateHelper;
    }

    /**
     * google 二次认证
     * @return ResponseEntity
     */
    @PostMapping("session")
    @ResponseBody
    public ResponseEntity<Object> googleAuthenticate(HttpServletRequest request, @RequestBody @Valid GoogleTopCode googleTopCode){

        HttpSession session = request.getSession(false);

        //未登录的情况下直接访问这个接口，直接让它先去验证用户名密码
        if(session == null || session.getAttribute(BlogConstants.AUTH_USER) == null){
            Map<String, Object> errors = new HashMap<>();
            errors.put("errors", BlogConstants.AUTH_USER_FIRST);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
        }

        User user = (User) session.getAttribute(BlogConstants.AUTH_USER);
        String email = user.getEmail();

        if(StringUtil.isEmail(email)){
            Map<String, Object> errors = new HashMap<>();
            errors.put("errors", BlogConstants.AUTH_USER_FIRST);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
        }

        googleTopCode.setEmail(email);

        boolean checkCode = googleAuthenticateHelper.checkCode(googleTopCode);

        if(checkCode){
            BlogContext.AUTH_THREAD_LOCAL.set(true);
            session.setAttribute(BlogConstants.AUTH_TOP_USER, true);
        }
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("success", checkCode));
    }
}
