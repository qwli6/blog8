package me.lqw.blog8.file;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文件资源解析器
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Component
@Conditional(FileCondition.class)
@ConditionalOnWebApplication
public class FileResourceResolver implements ResourceResolver {


    private final FileService fileService;
    private final FileProperties fileProperties;

    public FileResourceResolver(FileService fileService, FileProperties fileProperties) {
        this.fileService = fileService;
        this.fileProperties = fileProperties;
    }

    @Override
    public Resource resolveResource(HttpServletRequest request, String requestPath,
                                    List<? extends Resource> locations, ResourceResolverChain chain) {
        System.out.println(requestPath);

        String requestURI = request.getRequestURI();
        System.out.println(requestURI);

        return fileService.getProcessedFile(requestPath).map(ReadablePathSource::new).orElse(null);
    }

    @Override
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations,
                                 ResourceResolverChain chain) {
        return null;
    }
}
