package me.lqw.blog8.file;

import me.lqw.blog8.plugins.md.MarkdownParser;
import me.lqw.blog8.util.StringUtil;
import me.lqw.blog8.web.controller.console.AbstractBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;

/**
 * 解析从 url 中直接访问 md 文件
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Controller
@RequestMapping
@Conditional(FileCondition.class)
public class MdFileViewController extends AbstractBaseController {

    /**
     * 日志处理
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * MD 模板解析器
     */
    private final MarkdownParser markdownParser;

    /**
     * 文件服务
     */
    private final FileService fileService;

    /**
     * 构造方法
     *
     * @param objectProvider objectProvider
     * @param fileService    fileService
     */
    public MdFileViewController(ObjectProvider<MarkdownParser> objectProvider, FileService fileService) {
//        this.markdownParser = objectProvider.getIfAvailable();
        this.markdownParser = objectProvider.stream().min(Comparator.comparingInt(Ordered::getOrder)).get();

        this.fileService = fileService;
    }

    /**
     * 直接访问 **.md 文件
     *
     * @param request request
     * @param model   model
     * @return String
     */
    @GetMapping("/**/*.md")
    public String parseMdFile(HttpServletRequest request, Model model) {
        //获取请求的 URI
        String filePath = (String) request.getAttribute(RequestMappingHandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if (StringUtil.isBlank(filePath)) {
            throw new RuntimeException(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE + "属性不存在");
        }
        //从本地的文件系统中获取文件
        FileInfoDetail fid = fileService.getFileInfoDetail(filePath);

        if (StringUtil.isNotBlank(fid.getContent())) {
            fid.setContent(markdownParser.parse(fid.getContent()));
        }

        model.addAttribute("title", fid.getFileName());
        model.addAttribute("content", fid.getContent());

        return "console/file/markdown";
    }
}
