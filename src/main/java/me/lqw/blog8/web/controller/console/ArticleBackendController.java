package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.Article;
import me.lqw.blog8.model.dto.common.CR;
import me.lqw.blog8.model.dto.common.ResultDTO;
import me.lqw.blog8.model.dto.page.PageResult;
import me.lqw.blog8.model.enums.ArticleStatusEnum;
import me.lqw.blog8.model.vo.ArticlePageQueryParam;
import me.lqw.blog8.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 文章后台管理
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
@Controller
@RequestMapping("console")
public class ArticleBackendController extends AbstractBaseController {

    /**
     * 文章操作业务实现类
     */
    private final ArticleService articleService;

    /**
     * 构造方法注入
     *
     * @param articleService articleService
     */
    public ArticleBackendController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 获取文章列表
     *
     * @param model      model
     * @param queryParam queryParam
     * @return string
     */
    @GetMapping("articles")
    public String index(Model model, ArticlePageQueryParam queryParam) {
        if(!queryParam.hasPageSize()){
            queryParam.setPageSize(10);
        }
        PageResult<Article> articlePage = articleService.selectPage(queryParam);
        model.addAttribute("articlePage", articlePage);
        return "console/article/index";
    }

    /**
     * 创建文章
     *
     * @return string
     */
    @GetMapping("article/create")
    public String create() {
        return "console/article/create";
    }

    /**
     * 保存文章
     *
     * @param article article
     * @return CR<?>
     */
    @PostMapping("article/save")
    @ResponseBody
    public CR<?> save(@Valid @RequestBody Article article) {
        article.setStatus(ArticleStatusEnum.POSTED);
        article.setAllowComment(true);
        return ResultDTO.create(articleService.save(article).getId());
    }

    /**
     * 更新文章
     *
     * @param id      id
     * @param article article
     */
    @PutMapping("article/{id}/update")
    @ResponseBody
    public void update(@PathVariable("id") Integer id, @Valid @RequestBody Article article) {
        article.setId(id);
        articleService.update(article);
    }

    /**
     * 预览文章
     * @param id id
     * @return CR<?>
     */
    @GetMapping("article/{id}/preview")
    @ResponseBody
    public CR<?> preview(@PathVariable("id") Integer id){
        return ResultDTO.create(articleService.selectArticleForPreview(id));
    }


    /**
     * 删除文章
     * @param id id
     * @return CR<?>
     */
    @DeleteMapping("article/{id}/delete")
    @ResponseBody
    public CR<?> delete(@PathVariable("id") Integer id) {
        articleService.delete(id);
        return ResultDTO.create();
    }



    /**
     * 获取待更新的文章
     *
     * @param id    id
     * @param model model
     * @return string
     * 此处可能会抛出 ResourceNotFoundException
     */
    @GetMapping("article/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Article article = articleService.selectArticleForEdit(id);
        model.addAttribute("article", article);
        return "console/article/edit";
    }
}
