package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.Category;
import me.lqw.blog8.model.dto.common.CR;
import me.lqw.blog8.model.dto.common.ResultDTO;
import me.lqw.blog8.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类控制器
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Controller
@RequestMapping("console")
public class CategoryController extends AbstractBaseController {

    /**
     * 分类业务
     */
    private final CategoryService categoryService;

    /**
     * 构造方法
     *
     * @param categoryService categoryService
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 分类列表展示页面
     *
     * @param model model
     * @return string
     */
    @GetMapping("categories")
    public String index(Model model) {
        model.addAttribute("categories", categoryService.selectAll());
        return "console/category/index";
    }

    /**
     * 获取分类列表
     *
     * @return list
     */
    @GetMapping(value = "categories/list")
    @ResponseBody
    public Map<String, List<Category>> findAll() {
        Map<String, List<Category>> dataMap = new HashMap<>();
        dataMap.put("data", categoryService.selectAll());
        return dataMap;
    }

    /**
     * 保存新分类
     *
     * @param category category
     * @return CR<?>
     */
    @PostMapping("category/save")
    @ResponseBody
    public CR<?> save(@Valid @RequestBody Category category) {
        return ResultDTO.create(categoryService.save(category));
    }

    /**
     * 删除分类
     *
     * @param id id
     */
    @DeleteMapping("category/{id}/delete")
    @ResponseBody
    public void delete(@PathVariable("id") Integer id) {
        categoryService.delete(id);
    }

    /**
     * 修改分类
     *
     * @param id       id
     * @param category category
     */
    @PutMapping("category/{id}/update")
    @ResponseBody
    public void update(@PathVariable("id") Integer id, @RequestBody Category category) {
        category.setId(id);
        categoryService.update(category);
    }
}
