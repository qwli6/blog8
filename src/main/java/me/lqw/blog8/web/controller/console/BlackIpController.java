package me.lqw.blog8.web.controller.console;

import me.lqw.blog8.model.BlackIp;
import me.lqw.blog8.service.BlackIpService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("console")
@Controller
public class BlackIpController extends BaseController {

    private final BlackIpService blackIpService;

    public BlackIpController(BlackIpService blackIpService) {
        this.blackIpService = blackIpService;
    }

    @GetMapping("blackips")
    public String index(Model model){
        model.addAttribute("blackips", blackIpService.findAll());
        return "console/blackip/index";
    }


    @PostMapping("blackip/save")
    @ResponseBody
    public ResponseEntity<Integer> save(@RequestBody BlackIp blackIp){
        return ResponseEntity.ok(blackIpService.save(blackIp));
    }


    @DeleteMapping("blackip/{ip}/delete")
    @ResponseBody
    public void delete(@PathVariable("ip") String ip){
        blackIpService.deleteByIp(ip);
    }
}
