package me.lqw.blog8.web.controller;

import me.lqw.blog8.constants.BlogContext;
import me.lqw.blog8.exception.ResourceNotFoundException;
import me.lqw.blog8.model.Comment;
import me.lqw.blog8.model.CommentModule;
import me.lqw.blog8.model.dto.CommentDTO;
import me.lqw.blog8.model.dto.common.CR;
import me.lqw.blog8.model.dto.common.ResultDTO;
import me.lqw.blog8.model.dto.page.PageResult;
import me.lqw.blog8.model.enums.ArticleStatusEnum;
import me.lqw.blog8.model.enums.CommentStatus;
import me.lqw.blog8.model.vo.*;
import me.lqw.blog8.service.ArticleService;
import me.lqw.blog8.service.CommentService;
import me.lqw.blog8.service.MomentService;
import me.lqw.blog8.service.SimpleMailHandler;
import me.lqw.blog8.web.controller.console.AbstractBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * 首页主控制器
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Controller
public class MainController extends AbstractBaseController {

    /**
     * 文章服务
     */
    private final ArticleService articleService;

    /**
     * 动态服务
     */
    private final MomentService momentService;

    /**
     * 评论服务
     */
    private final CommentService commentService;


    @Resource
    private SimpleMailHandler simpleMailHandler;

    /**
     * 构造方法
     * @param articleService articleService
     * @param momentService  momentService
     * @param commentService commentService
     */
    public MainController(ArticleService articleService, MomentService momentService,
                          CommentService commentService) {
        this.articleService = articleService;
        this.momentService = momentService;
        this.commentService = commentService;
    }

    /**
     * 获取网站首页数据
     * @param queryParam queryParam
     * @param model model
     * @return String
     */
    @GetMapping
    public String index(ArticlePageQueryParam queryParam, Model model) {
        queryParam.setStatus(ArticleStatusEnum.POSTED);
        model.addAttribute("articlePage", articleService.selectPage(queryParam));
        model.addAttribute("latestMomentPage", momentService.selectLatestMoments());
        return "index";
    }

    /**
     * 获取文章内容详情数据
     * @param idOrUrlName 文章 id 或者文章别名
     * @param model model
     * @return String
     */
    @GetMapping("article/{idOrUrlName}")
    public String detail(@PathVariable("idOrUrlName") String idOrUrlName, Model model) {
        model.addAttribute("article", articleService.selectArticleForView(idOrUrlName));
        return "article";
    }


    /**
     * 获取动态列表数据
     * @param queryParam queryParam
     * @param model model
     * @return string
     */
    @GetMapping("moments")
    public String moments(MomentPageQueryParam queryParam, Model model) {
        model.addAttribute("momentPage", momentService.selectMomentArchivePage(queryParam));
        return "moments";
    }

    /**
     * 获取动态详情
     * @param id id
     * @param model model
     * @return string
     */
    @GetMapping("moment/{id}")
    public String moment(@PathVariable("id") int id, Model model) {
        model.addAttribute("moment", momentService.getMomentForView(id).orElseThrow(() ->
                new ResourceNotFoundException("momentService.get.notExists", "动态不存在")));
        return "moment";
    }

    /**
     * 文章归档页面
     * @param model model
     * @param queryParam queryParam
     * @return string
     */
    @GetMapping("archives")
    public String archives(Model model, ArticleArchivePageQueryParam queryParam) {
        model.addAttribute("articlePage", articleService.selectArchivePage(queryParam));
        return "archives";
    }


    @GetMapping("guestbook")
    public String guestbook(){

        return "guestbook";

    }


    /**
     * 点击动态
     * @param id id
     */
    @PutMapping("moment/{id}/hits")
    @ResponseBody
    public void hitMoment(@PathVariable("id") Integer id) {
        momentService.hit(id);
    }

    /**
     * 点击内容
     * @param id id
     */
    @PutMapping("article/{id}/hits")
    @ResponseBody
    public void hitArticle(@PathVariable("id") Integer id) {
        articleService.hit(id);
    }


    /**
     * 发布评论
     * @param comment comment
     * @param name    模块名称
     * @param id      模块 id
     * @return CR<?>
     */
    @PostMapping("api/module/{name}/{id}/comment/save")
    @ResponseBody
    public CR<?> save(@Valid @RequestBody Comment comment,
                      @PathVariable("name") String name,
                      @PathVariable("id") Integer id) {
        CommentModule commentModule = new CommentModule(id, name);

        comment.setIp(BlogContext.getIp());

        comment.setModule(commentModule);

        return ResultDTO.create(commentService.save(comment));
    }


    /**
     * 查询评论列表
     * @param name       模块名称
     * @param id         模块 id
     * @param queryParam queryParam
     * @return CR<?>
     */
    @GetMapping("api/module/{name}/{id}/comments")
    @ResponseBody
    public CR<?> selectPage(@PathVariable("name") String name,
                            @PathVariable("id") Integer id,
                            HandledCommentPageQueryParam queryParam) {
        if(!queryParam.hasPageSize()){
            queryParam.setPageSize(1);
        }

        queryParam.setCommentModule(new CommentModule(id, name));
        queryParam.setIgnorePaging(false);
        PageResult<CommentDTO> commentPageResult = commentService.selectPage(queryParam);
        commentPageResult.setQueryParam(queryParam);
        return ResultDTO.create(commentPageResult);
    }


    /**
     * 获取评论会话
     * @param name name
     * @param id id
     * @param commentId 评论 id
     * @return CR<?>
     */
    @GetMapping("api/module/{name}/{id}/comment/{commentId}/conversation")
    @ResponseBody
    public CR<?> checkConversation(@PathVariable("name") String name,
                                   @PathVariable("id") Integer id,
                                   @PathVariable("commentId") Integer commentId){

        CommentModule commentModule = new CommentModule(id, name);

        return ResultDTO.create(commentService.checkConversation(new CheckConversationParams(commentModule, commentId)));
    }


    @GetMapping("test")
    @ResponseBody
    public String sendMail(){

        try {
//            simpleMailHandler.sendEmail();
            Comment comment = new Comment();
            Comment _parent = new Comment();
            _parent.setUsername("selfassu");
            _parent.setContent("评论写的太重了，感觉还是要封装一下，将评论做成插件。这样灵活度也高一点。");
            comment.setParent(_parent);

            comment.setUsername("张三");
            comment.setContent("我也感觉是这样的, 博主封装好了可以通知可以公布出来，学习一下");
            comment.setCreateAt(LocalDateTime.now());
            comment.setStatus(CommentStatus.WAIT_CHECK);
            simpleMailHandler.sendTemplateMail("commentMailTemplate.html", comment);

        } catch (MessagingException e) {
            e.printStackTrace();

        }

        return "success";
    }
}
