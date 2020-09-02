package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.Tag;
import me.lqw.blog8.model.dto.common.CR;
import me.lqw.blog8.model.dto.common.ResultDTO;
import me.lqw.blog8.model.vo.TagPageQueryParam;
import me.lqw.blog8.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 标签处理器
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
@Controller
@RequestMapping("console")
public class TagController extends AbstractBaseController {

    /**
     * 标签业务处理类
     */
    private final TagService tagService;

    /**
     * 构造方法注入
     *
     * @param tagService tagService
     */
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * 获取所有的标签
     *
     * @param model      model
     * @param queryParam queryParam
     * @return string
     */
    @GetMapping("tags")
    private String index(Model model, TagPageQueryParam queryParam) {
        model.addAttribute("tagPage", tagService.selectPage(queryParam));
        return "console/tag/index";
    }

    /**
     * 获取所有的标签列表
     *
     * @return list
     */
    @GetMapping("tags/list")
    @ResponseBody
    public List<Tag> lists() {
        return tagService.selectAll();
    }

    /**
     * 删除标签
     *
     * @param id id
     */
    @DeleteMapping("tag/{id}/delete")
    @ResponseBody
    public void delete(@PathVariable("id") Integer id) {
        tagService.delete(id);
    }

    /**
     * 保存标签
     *
     * @param tag tag
     * @return CR<?>
     */
    @PostMapping("tag/save")
    @ResponseBody
    public CR<?> save(@Valid @RequestBody Tag tag) {
        return ResultDTO.create(tagService.save(tag));
    }

    /**
     * 更新标签内容
     *
     * @param id  id
     * @param tag tag
     */
    @PutMapping("tag/{id}/update")
    @ResponseBody
    public void update(@PathVariable("id") Integer id, @RequestBody Tag tag) {
        tag.setId(id);
        tagService.update(tag);
    }

}
