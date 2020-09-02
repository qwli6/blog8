package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.constants.BlogConstants;
import me.lqw.blog8.model.User;
import me.lqw.blog8.model.dto.common.CR;
import me.lqw.blog8.model.dto.common.ResultDTO;
import me.lqw.blog8.model.vo.PasswordParam;
import me.lqw.blog8.model.vo.UserParam;
import me.lqw.blog8.service.RememberMeService;
import me.lqw.blog8.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 用户后台管理控制器
 * @author liqiwen
 * @since 1.2
 * @version 1.2
 */
@Controller
@RequestMapping("console")
public class UserBackendController extends AbstractBaseController {

    /**
     * user service
     */
    private final UserService userService;

    /**
     * rememberMe
     */
    private final RememberMeService rememberMeService;

    /**
     * 构造方法
     * @param userService userService
     */
    public UserBackendController(UserService userService, RememberMeService rememberMeService) {
        this.userService = userService;
        this.rememberMeService = rememberMeService;
    }

    /**
     * 获取用户信息
     * @param model model
     * @param request request
     * @return string
     */
    @GetMapping("user/profiles")
    public String userProfiles(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(BlogConstants.AUTH_USER);
        model.addAttribute("user", user);
        model.addAttribute("user1", new User("张三"));
        return "console/user/index";
    }

    /**
     * 更新用户信息
     * @param userParam userParam
     * @return CR<?>
     */
    @PutMapping("user/{id}/update")
    @ResponseBody
    public CR<?> update(@PathVariable("id") Integer id, @Valid @RequestBody UserParam userParam) {
        User user = new User(userParam);
        user.setId(id);
        userService.update(user);
        return ResultDTO.create();
    }

    /**
     * 更新用户密码
     * @param passwordParam passwordParam
     * @return CR<?>
     */
    @PutMapping("user/{id}/update/password")
    @ResponseBody
    public CR<?> updatePassword(@PathVariable("id") Integer id, @Valid @RequestBody PasswordParam passwordParam,
                                HttpServletRequest request, HttpServletResponse response) {
        String newPassword = passwordParam.getNewPassword();
        String repeatPassword = passwordParam.getRepeatPassword();


        User user = new User(id);
        user.setPassword(newPassword);
//        userService.updatePassword(user);

        rememberMeService.clearRemember(request, response);

        return ResultDTO.create();
    }
}
