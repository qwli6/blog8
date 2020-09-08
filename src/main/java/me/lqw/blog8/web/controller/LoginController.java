package me.lqw.blog8.web.controller;

import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.event.BlogEventPublishHandler;
import me.lqw.blog8.model.OperateLog;
import me.lqw.blog8.model.User;
import me.lqw.blog8.model.dto.common.CR;
import me.lqw.blog8.model.dto.common.ResultDTO;
import me.lqw.blog8.model.enums.OperateType;
import me.lqw.blog8.model.vo.LoginParam;
import me.lqw.blog8.service.RememberMeService;
import me.lqw.blog8.service.UserService;
import me.lqw.blog8.web.controller.console.AbstractBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 登录控制器
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
@Controller
public class LoginController extends AbstractBaseController {

    /**
     * 用户 UserService
     */
    private final UserService userService;

    /**
     * 记住我 service
     */
    private final RememberMeService rememberMeService;

    public LoginController(UserService userService, RememberMeService rememberMeService) {
        this.userService = userService;
        this.rememberMeService = rememberMeService;
    }

    /**
     * 访问登录页面
     *
     * @return login.html
     */
    @GetMapping("login")
    public String login() {
        return "login";
    }


    /**
     * 登录请求
     *
     * @param loginParam loginParam
     * @param request    request
     * @return CR<?>
     */
    @PostMapping(value = "api/token")
    @ResponseBody
    public CR<?> userAuth(@Valid @RequestBody LoginParam loginParam, HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();

        User user = userService.userAuth(loginParam.getUsername(), loginParam.getPassword());

        session.setAttribute(BlogConstants.AUTH_USER, user);

        return ResultDTO.create();
    }


    /**
     * 用户登出
     *
     * @param request  request
     * @param response response
     * @return CR<?>
     */
    @PostMapping("logout")
    @ResponseBody
    public CR<?> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute(BlogConstants.AUTH_USER);
        session.removeAttribute(BlogConstants.AUTH_TOP_USER);
        rememberMeService.clearRemember(request, response);
        return ResultDTO.create();
    }


    /**
     * 是否登录
     *
     * @return CR<?>
     */
    @GetMapping("authorized")
    @ResponseBody
    public CR<?> authorized() {
        return ResultDTO.create(BlogContext.isAuthorized());
    }

}
