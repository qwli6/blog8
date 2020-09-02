package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.BlackIp;
import me.lqw.blog8.model.dto.common.CR;
import me.lqw.blog8.model.dto.common.ResultDTO;
import me.lqw.blog8.service.BlackIpService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 黑名单管理
 *
 * @author liqiwen
 * @version 1.2
 * <p>
 * 加入黑名单的用户无法访问网站, 前提是获取的 ip 准确
 * @since 1.2
 */
@RequestMapping("console")
@Controller
public class BlackIpController extends AbstractBaseController {

    /**
     * 黑名单业务实现
     */
    private final BlackIpService blackIpService;

    /**
     * 构造方法注入
     *
     * @param blackIpService blackIpService
     */
    public BlackIpController(BlackIpService blackIpService) {
        this.blackIpService = blackIpService;
    }

    /**
     * 获取所有的黑名单列表
     *
     * @param model model
     * @return string
     */
    @GetMapping("blackips")
    public String index(Model model) {
        model.addAttribute("blackips", blackIpService.selectAll());
        return "console/blackip/index";
    }

    /**
     * 保存黑名单
     *
     * @param blackIp blackIp
     * @return CR<?>
     */
    @PostMapping("blackip/save")
    @ResponseBody
    public CR<?> save(@Valid @RequestBody BlackIp blackIp) {
        return ResultDTO.create(blackIpService.save(blackIp));
    }

    /**
     * 删除 ip
     *
     * @param id id
     */
    @DeleteMapping("blackip/{id}/delete")
    @ResponseBody
    public void delete(@PathVariable("id") Integer id) {
        blackIpService.delete(id);
    }
}
