package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.exception.ResourceNotFoundException;
import me.lqw.blog8.model.Moment;
import me.lqw.blog8.model.dto.common.CR;
import me.lqw.blog8.model.dto.common.ResultDTO;
import me.lqw.blog8.model.dto.page.PageResult;
import me.lqw.blog8.model.vo.MomentPageQueryParam;
import me.lqw.blog8.service.MomentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 动态后台管理
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@RequestMapping("console")
@Controller
public class MomentController extends AbstractBaseController {

    /**
     * 动态 service
     */
    private final MomentService momentService;

    /**
     * 构造方法注入
     *
     * @param momentService momentService
     */
    public MomentController(MomentService momentService) {
        this.momentService = momentService;
    }

    /**
     * 获取动态列表
     *
     * @param model      model
     * @param queryParam queryParam
     * @return string
     */
    @GetMapping("moments")
    public String index(Model model, MomentPageQueryParam queryParam) {
        if(!queryParam.hasPageSize()) {
            queryParam.setPageSize(20);
        }
        PageResult<Moment> momentPageData = momentService.selectPage(queryParam);
        model.addAttribute("momentPage", momentPageData);
        return "console/moment/index";
    }


    /**
     * 获取要编辑的动态
     *
     * @param id    id
     * @param model model
     * @return string
     * 可能会抛出 ResourceNotFoundException
     */
    @GetMapping("moment/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("moment", momentService.getMomentForEdit(id).orElseThrow(()
                -> new ResourceNotFoundException("momentService.edit.notExists", "待编辑的动态不存在")));
        return "console/moment/edit";
    }

    /**
     * 创建动态
     *
     * @return string
     */
    @GetMapping("moment/create")
    public String create() {
        return "console/moment/create";
    }


    /**
     * 删除动态
     *
     * @param id id
     */
    @DeleteMapping("moment/{id}/delete")
    @ResponseBody
    public void delete(@PathVariable("id") Integer id) {
        momentService.delete(id);
    }

    /**
     * 保存动态
     *
     * @param moment moment
     * @return CR<?> 通用返回
     */
    @PostMapping("moment/save")
    @ResponseBody
    public CR<?> save(@Valid @RequestBody Moment moment) {
        return ResultDTO.create(momentService.save(moment).getId());
    }

    /**
     * 更新动态
     *
     * @param id     id
     * @param moment moment
     */
    @PostMapping("moment/{id}/update")
    @ResponseBody
    public void update(@PathVariable("id") Integer id, @RequestBody Moment moment) {
        moment.setId(id);
        momentService.update(moment);
    }
}
