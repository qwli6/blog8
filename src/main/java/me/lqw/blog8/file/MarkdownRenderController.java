package me.lqw.blog8.file;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MarkdownRenderController {


    @GetMapping("{fileName}.md")
    public String render(HttpServletRequest request, @PathVariable("fileName") String fileName) {

        System.out.println(fileName);

        return "console/file/markdown";
    }
}
