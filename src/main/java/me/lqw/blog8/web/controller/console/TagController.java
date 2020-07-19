package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.Tag;
import me.lqw.blog8.model.vo.TagQueryParam;
import me.lqw.blog8.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("console")
public class TagController extends BaseController {


    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("tags")
    private String index(Model model, TagQueryParam queryParam){
        model.addAttribute("tagPage", tagService.selectPage(queryParam));
        return "/console/tag/index";
    }

    @GetMapping("tags/list")
    @ResponseBody
    public List<Tag> lists(){
        return tagService.listAll();
    }


    @DeleteMapping("tag/{id}/delete")
    @ResponseBody
    public void delete(@PathVariable("id") Integer id){
        tagService.delete(id);
    }


    @PostMapping("tag/save")
    @ResponseBody
    public ResponseEntity<Integer> save(@Valid @RequestBody Tag tag){
        return ResponseEntity.ok(tagService.save(tag).getId());
    }

    @PostMapping("tag/{id}/update")
    @ResponseBody
    public void update(@PathVariable("id") Integer id, @RequestBody Tag tag){
        tag.setId(id);
        tagService.update(tag);
    }

}
