package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.exception.ResourceNotFoundException;
import me.lqw.blog8.model.Moment;
import me.lqw.blog8.model.vo.MomentQueryParam;
import me.lqw.blog8.service.MomentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("console")
@Controller
public class MomentController extends BaseController {


    private final MomentService momentService;

    public MomentController(MomentService momentService) {
        this.momentService = momentService;
    }

    @GetMapping("moments")
    private String index(Model model, MomentQueryParam queryParam){
        model.addAttribute("momentPage", momentService.selectPage(queryParam));
        return "console/moment/index";
    }


    @GetMapping("moment/{id}/edit")
    private String edit(@PathVariable("id") Integer id, Model model){
        model.addAttribute("moment", momentService.getMomentForEdit(id).orElseThrow(() -> new ResourceNotFoundException("momentService.edit.notExists", "动态不存在")));
        return "console/moment/edit";
    }

    @GetMapping("moment/create")
    private String create(){
        return "console/moment/create";
    }


    @DeleteMapping("moment/{id}/delete")
    @ResponseBody
    public void delete(@PathVariable("id") Integer id){
        momentService.delete(id);
    }

    @PostMapping("moment/save")
    @ResponseBody
    public ResponseEntity<Integer> save(@Valid @RequestBody Moment moment) {
        return ResponseEntity.ok(momentService.save(moment).getId());
    }

    @PostMapping("moment/{id}/update")
    @ResponseBody
    public void update(@PathVariable("id") Integer id, @RequestBody Moment moment){
        moment.setId(id);
        momentService.update(moment);
    }
}
