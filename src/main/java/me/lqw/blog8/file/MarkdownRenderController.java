package me.lqw.blog8.file;

import me.lqw.blog8.service.MarkdownHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 解析从 url 中直接访问 md 文件
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Controller
@RequestMapping
@Conditional(FileCondition.class)
public class MarkdownRenderController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * MD 模板解析器
     */
    private final MarkdownHandler handler;

    /**
     * 文件服务
     */
    private final FileService fileService;

    public MarkdownRenderController(MarkdownHandler handler, FileService fileService) {
        this.handler = handler;
        this.fileService = fileService;
    }

    @GetMapping("/**/*.md")
    public String renderMarkdown(HttpServletRequest request, Model model) {

        String pathAttribute = (String) request.getAttribute(RequestMappingHandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if(StringUtils.isEmpty(pathAttribute)){
            throw new RuntimeException(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE + "属性不存在");
        }
        FileInfoDetail fid = fileService.getFileInfoDetail(pathAttribute);

        if(!StringUtils.isEmpty(fid.getContent())){
            fid.setContent(handler.toHtml(fid.getContent()));
        }

        model.addAttribute("title", fid.getFileName());
        model.addAttribute("content", fid.getContent());

        return "console/file/markdown";
    }
}
