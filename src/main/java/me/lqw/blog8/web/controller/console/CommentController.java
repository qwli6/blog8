package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.dto.CommentDTO;
import me.lqw.blog8.model.dto.common.CR;
import me.lqw.blog8.model.dto.common.ResultDTO;
import me.lqw.blog8.model.dto.page.PageResult;
import me.lqw.blog8.model.vo.CommentPageQueryParam;
import me.lqw.blog8.model.vo.HandledCommentPageQueryParam;
import me.lqw.blog8.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 评论后端管理控制器
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.2
 */
@Controller
@RequestMapping("console")
public class CommentController extends AbstractBaseController {

    /**
     * 评论 service
     */
    private final CommentService commentService;

    /**
     * 构造方法注入
     *
     * @param commentService commentService
     */
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 评论查询页面
     *
     * @param queryParam queryParam
     * @param model      model
     * @return String
     */
    @GetMapping("comments")
    public String index(CommentPageQueryParam queryParam, Model model) {

        if(!queryParam.hasPageSize()){
            queryParam.setPageSize(10);
        }

        queryParam.setModuleName("article");
        HandledCommentPageQueryParam handledCommentPageQueryParam = new HandledCommentPageQueryParam(queryParam);

        PageResult<CommentDTO> commentPageResult = commentService.selectPage(handledCommentPageQueryParam);
        commentPageResult.setQueryParam(queryParam);
        model.addAttribute("commentPage", commentPageResult);

        return "console/comment/index";
    }

    /**
     * 审核评论
     *
     * @param id id
     * @return CR<?>
     */
    @PostMapping("comment/{id}/check")
    @ResponseBody
    public CR<?> checkComment(@PathVariable("id") Integer id) {
        return ResultDTO.create(commentService.checkComment(id));
    }

}
