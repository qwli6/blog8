package me.lqw.blog8.web.controller;

import me.lqw.blog8.BlogContext;
import me.lqw.blog8.exception.ResourceNotFoundException;
import me.lqw.blog8.model.Comment;
import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.CommentSaved;
import me.lqw.blog8.model.CommentStatus;
import me.lqw.blog8.model.dto.PageResult;
import me.lqw.blog8.model.vo.ArticleArchivePageQueryParam;
import me.lqw.blog8.model.vo.ArticlePageQueryParam;
import me.lqw.blog8.model.vo.CommentPageQueryParam;
import me.lqw.blog8.model.vo.MomentPageQueryParam;
import me.lqw.blog8.service.ArticleService;
import me.lqw.blog8.service.CommentService;
import me.lqw.blog8.service.MomentService;
import me.lqw.blog8.service.SimpleMailHandler;
import me.lqw.blog8.validator.StatusEnum;
import me.lqw.blog8.web.controller.console.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Controller
public class MainController extends BaseController {


    private final ArticleService articleService;
    private final MomentService momentService;
    private final CommentService commentService;

    private final SimpleMailHandler mailHandler;

    public MainController(ArticleService articleService, MomentService momentService,
                          CommentService commentService,
                          SimpleMailHandler mailHandler) {
        this.articleService = articleService;
        this.momentService = momentService;
        this.commentService = commentService;
        this.mailHandler = mailHandler;
    }

    @GetMapping
    public String index(ArticlePageQueryParam queryParam, Model model){
        queryParam.setStatus(StatusEnum.POSTED);
        model.addAttribute("articlePage", articleService.selectPage(queryParam));
        model.addAttribute("latestMomentPage", momentService.selectLatestMoments());
        return "index";
    }

    @GetMapping("article/{idOrUrlName}")
    public String detail(@PathVariable("idOrUrlName") String idOrUrlName, Model model){
        model.addAttribute("article", articleService.get(idOrUrlName).orElseThrow(()
                -> new ResourceNotFoundException("article.get.notExists", "资源未找到")));
        return "article";
    }


    @GetMapping("moments")
    public String moments(MomentPageQueryParam queryParam, Model model){
        model.addAttribute("momentPage", momentService.selectMomentArchivePage(queryParam));
        return "moments";
    }

    @GetMapping("moment/{id}")
    public String moment(@PathVariable("id") int id, Model model){
        model.addAttribute("moment", momentService.getMomentForView(id));
        return "moment";
    }

    @GetMapping("archives")
    public String archives(Model model, ArticleArchivePageQueryParam queryParam){
        model.addAttribute("articlePage", articleService.selectArchivePage(queryParam));
        return "archives";
    }



    @PutMapping("moment/{id}/hits")
    @ResponseBody
    public void hitMoment(@PathVariable("id") Integer id){
        momentService.hit(id);
    }

    @PutMapping("article/{id}/hits")
    @ResponseBody
    public void hitArticle(@PathVariable("id") Integer id){
        articleService.hit(id);
    }


    @PostMapping("module/{name}/{id}/comment/save")
    @ResponseBody
    public CommentSaved save(@Valid @RequestBody Comment comment, @PathVariable("name") String name,
                             @PathVariable("id") Integer id){
        CommentModule commentModule = new CommentModule(id, name);

        comment.setIp(BlogContext.getIp());

        comment.setModule(commentModule);

        return commentService.save(comment);
    }


    @GetMapping("api/module/{name}/{id}/comments")
    @ResponseBody
    public PageResult<Comment> selectPage(@PathVariable("name") String name,
                                          @PathVariable("id") Integer id, CommentPageQueryParam queryParam) {
        queryParam.setModule(new CommentModule(id, name));

        if(!BlogContext.isAuthorized()){
            queryParam.setStatus(CommentStatus.NORMAL);
        }

        return commentService.selectPage(queryParam);

    }


    @GetMapping("test")
    @ResponseBody
    public String sendMail() throws MessagingException {
        mailHandler.sendEmail();

        return "success";
    }
}
