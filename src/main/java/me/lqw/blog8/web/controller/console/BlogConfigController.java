package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.config.BlogConfigModel;
import me.lqw.blog8.model.config.EmailConfigModel;
import me.lqw.blog8.service.BlogConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 系统配置控制器
 * @author liqiwen
 * @version 1.0
 */
@RequestMapping("console")
@Controller
public class BlogConfigController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    private final BlogConfigService blogConfigService;

    public BlogConfigController(BlogConfigService blogConfigService) {
        this.blogConfigService = blogConfigService;
    }

    @GetMapping("configs")
    public String index(Model model){
        model.addAttribute("configs", new HashMap<>());

        return "console/config/index";
    }

    @PutMapping("config/blog/update")
    @ResponseBody
    public void updateBlogConfig(@RequestBody BlogConfigModel configModel) {
        blogConfigService.updateConfig(configModel);
    }

    @PutMapping("config/email/update")
    @ResponseBody
    public void updateEmailConfig(@RequestBody EmailConfigModel configModel) {
        blogConfigService.updateConfig(configModel);
    }


    @GetMapping("config")
    public String selectConfig(@RequestParam("key") String key, Model model){
//        blogConfigService.

//        return new HashMap<>();
        model.addAttribute("key", key);

        return "/console/config/blogConfig";
    }
}
