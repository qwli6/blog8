package me.lqw.blog8.web.controller.api;

import me.lqw.blog8.model.dto.common.CR;
import me.lqw.blog8.model.dto.common.ResultDTO;
import me.lqw.blog8.service.CategoryService;
import me.lqw.blog8.web.controller.console.AbstractBaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类相关 api
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@RestController
@RequestMapping("api/v1")
public class ApiCategoryController extends AbstractBaseController {

    /**
     * 分类 service
     */
    private final CategoryService categoryService;

    /**
     * 构造函数注入
     *
     * @param categoryService categoryService
     */
    public ApiCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * api 获取所有分类
     *
     * @return CR<?>
     */
    @GetMapping("categores")
    public CR<?> selectAll() {
        return ResultDTO.create(categoryService.selectAll());
    }
}
