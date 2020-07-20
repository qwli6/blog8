package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.Category;
import me.lqw.blog8.model.Tag;
import me.lqw.blog8.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("console")
public class CategoryController extends BaseController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("categories")
    public String index(Model model){
        model.addAttribute("categories", categoryService.findAll());
        return "console/category/index";
    }

    @GetMapping(value = "categories/list")
    @ResponseBody
    public Map<String, List<Category>> findAll(){
        Map<String, List<Category>> dataMap = new HashMap<>();
        dataMap.put("data", categoryService.findAll());
        return dataMap;
    }

    @PostMapping("category/save")
    @ResponseBody
    public ResponseEntity<Integer> save(@Valid @RequestBody Category category){
        return ResponseEntity.ok(categoryService.save(category).getId());
    }

    @DeleteMapping("category/{id}/delete")
    @ResponseBody
    public void delete(@PathVariable("id") Integer id){
        categoryService.delete(id);
    }

    @PutMapping("category/{id}/update")
    @ResponseBody
    public void update(@PathVariable("id") Integer id, @RequestBody Category category){
        category.setId(id);
        categoryService.update(category);
    }


}
