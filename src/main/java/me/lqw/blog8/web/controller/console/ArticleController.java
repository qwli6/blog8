package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.exception.ResourceNotFoundException;
import me.lqw.blog8.model.Article;
import me.lqw.blog8.model.vo.ArticleQueryParam;
import me.lqw.blog8.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("console")
public class ArticleController extends BaseController {


    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("articles")
    public String index(Model model, ArticleQueryParam queryParam) {
        model.addAttribute("articlePage", articleService.selectPage(queryParam));
        return "console/article/index";
    }


    @GetMapping("article/create")
    public String create() {
        return "console/article/create";
    }


    @PostMapping("article/save")
    @ResponseBody
    public int saved(@Valid @RequestBody Article article) {
        return articleService.save(article).getId();
    }


    @GetMapping("article/{id}/update")
    @ResponseBody
    public void findOne(@PathVariable("id") Integer id, @Valid @RequestBody Article article) {
        article.setId(id);
        articleService.update(article);
    }

    @GetMapping("article/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("article", articleService.getArticleForEdit(id).orElseThrow(()
                -> new ResourceNotFoundException("article.update.notExists", "内容不存在")));
        return "console/article/edit";
    }
}
