package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.config.BlogConfigModel;
import me.lqw.blog8.model.config.EmailConfigModel;
import me.lqw.blog8.service.BlogConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 系统配置控制器
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
@RequestMapping("console")
@Controller
public class BlogConfigController extends AbstractBaseController {

    /**
     * 博客系统配置业务实现类
     */
    private final BlogConfigService blogConfigService;

    /**
     * 构造方法注入
     *
     * @param blogConfigService blogConfigService
     */
    public BlogConfigController(BlogConfigService blogConfigService) {
        this.blogConfigService = blogConfigService;
    }

    /**
     * 获取所有的配置列表
     *
     * @param model model
     * @return string
     */
    @GetMapping("config/profile")
    public String index(Model model) {
        model.addAttribute("configs", new HashMap<>());
        return "console/config/profile";
    }


    @GetMapping("config/account")
    public String accountIndex(Model model) {
        model.addAttribute("account", new HashMap<>());

        return "console/config/account";
    }

    @GetMapping("config/email")
    public String emailIndex(Model model){

        return "console/config/email";
    }

    /**
     * 更新配置
     *
     * @param configModel configModel
     */
    @PutMapping("config/blog/update")
    @ResponseBody
    public void updateBlogConfig(@RequestBody BlogConfigModel configModel) {
        blogConfigService.updateConfig(configModel);
    }

    /**
     * 修改邮箱配置
     *
     * @param configModel configModel
     */
    @PutMapping("config/email/update")
    @ResponseBody
    public void updateEmailConfig(@RequestBody EmailConfigModel configModel) {
        blogConfigService.updateConfig(configModel);
    }


    @GetMapping("config")
    public String selectConfig(@RequestParam("key") String key, Model model) {
//        blogConfigService.

//        return new HashMap<>();
        model.addAttribute("key", key);

        return "console/config/blogConfig";
    }
}
