package me.lqw.blog8.web.controller;

import me.lqw.blog8.BlogContext;
import me.lqw.blog8.model.User;
import me.lqw.blog8.model.vo.LoginParam;
import me.lqw.blog8.service.UserService;
import me.lqw.blog8.web.controller.console.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController extends BaseController {


    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String login(){
        return "login";
    }


    @PostMapping(value = "login")
    @ResponseBody
    public ResponseEntity<?> userAuth(@Valid @RequestBody LoginParam loginParam, HttpServletRequest request){
        HttpSession session = request.getSession();

        User user = userService.userAuth(loginParam.getUsername(), loginParam.getPassword());

        session.setAttribute("user", user);

        return ResponseEntity.ok().build();
    }


    @GetMapping("authorized")
    @ResponseBody
    public ResponseEntity<Boolean> authorized(){
        return ResponseEntity.ok(BlogContext.isAuthorized());
    }
}
