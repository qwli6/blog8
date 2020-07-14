package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("console")
public class BackendController extends BaseController {


    private final ArticleService articleService;

    public BackendController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("home")
    public String home(){
        return "console/home";
    }
}
