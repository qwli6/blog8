package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.service.BlogConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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


    @GetMapping("config")
    @ResponseBody
    public Object queryConfig(@RequestParam("key") String key){

        return new HashMap<>();
    }
}
