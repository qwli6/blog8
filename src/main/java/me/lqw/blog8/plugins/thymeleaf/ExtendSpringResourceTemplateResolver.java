package me.lqw.blog8.plugins.thymeleaf;

import me.lqw.blog8.plugins.template.TemplateService;
import me.lqw.blog8.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.templateresource.SpringResourceTemplateResource;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Map;

/**
 * 扩展 ExtendSpringResourceTemplateResolver
 * 主要目的是为了实现模板可缓存, 同时为了实现模板可动态切换
 *
 * @author liqiwen
 * @version 1.5
 * @see ThymeleafViewResolver
 * @since 1.5
 */
public class ExtendSpringResourceTemplateResolver extends SpringResourceTemplateResolver {


    private final TemplateService templateService;

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    public ExtendSpringResourceTemplateResolver(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, String resourceName, String characterEncoding, Map<String, Object> templateResolutionAttributes) {

        logger.info("获取视图资源：" + configuration);
        logger.info("获取视图资源模板名称：" + template);
        logger.info("获取视图资源模板名称 ownerTemplate：" + ownerTemplate);
        logger.info("获取视图资源模板 resourceName：" + resourceName);
        logger.info("获取视图资源模板 characterEncoding：" + characterEncoding);
        logger.info("获取视图资源模板 templateResolutionAttributes：" + JsonUtil.toJsonString(templateResolutionAttributes));
        Resource resource = templateService.getTemplateResource(template);
        if(resource != null) {
            return new SpringResourceTemplateResource(resource, Charset.defaultCharset().name());
        }
        return super.computeTemplateResource(configuration, ownerTemplate, template, resourceName, characterEncoding, templateResolutionAttributes);

    }
}
