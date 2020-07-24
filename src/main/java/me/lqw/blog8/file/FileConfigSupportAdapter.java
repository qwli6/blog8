package me.lqw.blog8.file;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.PathMatcher;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UrlPathHelper;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Conditional(FileCondition.class)
public class FileConfigSupportAdapter implements WebMvcConfigurer {

    private final FileService fileService;

    public FileConfigSupportAdapter(WebMvcProperties webMvcProperties, FileService fileService){
        this.fileService = fileService;
        if("/**".equalsIgnoreCase(webMvcProperties.getStaticPathPattern())){
            throw new RuntimeException("本地文件服务已经配置，请在 application.properties 中指定 spring.mvc.static-path-pattern，该值不能为/**");
        }
    }


    @Bean
    public SimpleUrlHandlerMapping fileMapping(FileService fileService, ResourceProperties resourceProperties,
                                               FileProperties fileProperties, ContentNegotiationManager contentNegotiationManager,
                                               @Qualifier("mvcUrlPathHelper") UrlPathHelper urlPathHelper,
                                               @Qualifier("mvcPathMatcher") PathMatcher pathMatcher, WebApplicationContext context) throws Exception {
        FileResourceResolver resolver = new FileResourceResolver(fileService, fileProperties);
        BlogResourceHttpRequestHandler handler = new BlogResourceHttpRequestHandler(resolver, resourceProperties, fileService);
        handler.setApplicationContext(context);
        handler.setServletContext(context.getServletContext());
        if (urlPathHelper != null) {
            handler.setUrlPathHelper(urlPathHelper);
        }
        if (contentNegotiationManager != null) {
            handler.setContentNegotiationManager(contentNegotiationManager);
        }
        handler.afterPropertiesSet();

        Map<String, ResourceHttpRequestHandler> handlerMap = new HashMap<>();
        handlerMap.put("/**", handler);
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping(handlerMap);
        mapping.setOrder(Ordered.LOWEST_PRECEDENCE);
        mapping.setPathMatcher(pathMatcher);
        mapping.setUrlPathHelper(urlPathHelper);
        return mapping;
    }
}
