package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.DataTotalStatistics;
import me.lqw.blog8.service.ArticleService;
import me.lqw.blog8.service.CommentService;
import me.lqw.blog8.service.MomentService;
import me.lqw.blog8.service.OperateLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台首页控制器
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("console")
public class BackendController extends AbstractBaseController {

    /**
     * 内容 service
     */
    private final ArticleService articleService;

    /**
     * 动态 service
     */
    private final MomentService momentService;

    /**
     * 评论 service
     */
    private final CommentService commentService;

    /**
     * 操作日志 service
     */
    private final OperateLogService operateLogService;


    /**
     * 构造函数
     *
     * @param articleService    articleService
     * @param momentService     momentService
     * @param commentService    commentService
     * @param operateLogService operateLogService
     */
    public BackendController(ArticleService articleService,
                             MomentService momentService,
                             CommentService commentService,
                             OperateLogService operateLogService) {
        this.articleService = articleService;
        this.momentService = momentService;
        this.commentService = commentService;
        this.operateLogService = operateLogService;
    }

    /**
     * 后台首页
     *
     * @param model model
     * @return string
     */
    @GetMapping("home")
    public String home(Model model) {

        DataTotalStatistics dataTotalStatistics = new DataTotalStatistics();
        dataTotalStatistics.setArticleCount(20);
        dataTotalStatistics.setCommentCount(21);
        dataTotalStatistics.setMomentCount(121);
        dataTotalStatistics.setHitCount(1123);

        model.addAttribute("dataTotalStatistics", dataTotalStatistics);


        model.addAttribute("latestOperateLogs", operateLogService.selectLatestOperateLogs(10));

        return "console/home";
    }
}
