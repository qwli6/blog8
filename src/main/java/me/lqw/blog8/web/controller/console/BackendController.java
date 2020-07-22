package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.DataTotalStatistics;
import me.lqw.blog8.service.ArticleService;
import me.lqw.blog8.service.CommentService;
import me.lqw.blog8.service.MomentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("console")
public class BackendController extends BaseController {


    private final ArticleService articleService;
    private final MomentService momentService;

    private final CommentService commentService;

    public BackendController(ArticleService articleService,
                             MomentService momentService,
                             CommentService commentService) {
        this.articleService = articleService;
        this.momentService = momentService;
        this.commentService = commentService;
    }

    @GetMapping("home")
    public String home(Model model){

        DataTotalStatistics dataTotalStatistics = new DataTotalStatistics();
        dataTotalStatistics.setArticleCount(20);
        dataTotalStatistics.setCommentCount(21);
        dataTotalStatistics.setMomentCount(121);
        dataTotalStatistics.setHitCount(1123);

        model.addAttribute("dataTotalStatistics", dataTotalStatistics);

        return "console/home";
    }
}
