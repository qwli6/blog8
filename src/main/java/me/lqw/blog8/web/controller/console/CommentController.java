package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.vo.CommentPageQueryParam;
import me.lqw.blog8.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 评论后端管理控制器
 * @author liqiwen
 * @version 1.0
 */
@Controller
@RequestMapping("console")
public class CommentController {


    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("comments")
    public String index(CommentPageQueryParam queryParam, Model model){
        model.addAttribute("commentPage", commentService.selectPage(queryParam));

        return "console/comment/index";
    }

}
