//package me.lqw.blog8.plugins.thymeleaf;
//
//import org.thymeleaf.IEngineConfiguration;
//import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
//import org.thymeleaf.spring5.view.ThymeleafViewResolver;
//import org.thymeleaf.templateresource.ITemplateResource;
//
//import java.util.Map;
//
///**
// * 扩展 ExtendSpringResourceTemplateResolver
// * 主要目的是为了实现模板可缓存, 同时为了实现模板可动态切换
// *
// * @author liqiwen
// * @version 1.5
// * @see ThymeleafViewResolver
// * @since 1.5
// */
//public class ExtendSpringResourceTemplateResolver extends SpringResourceTemplateResolver {
//
//    @Override
//    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, String resourceName, String characterEncoding, Map<String, Object> templateResolutionAttributes) {
//        return super.computeTemplateResource(configuration, ownerTemplate, template, resourceName, characterEncoding, templateResolutionAttributes);
//
////        System.out.println("获取视图资源:");
////        FileSystemResource resource = new FileSystemResource(Paths.get("/Users/liqiwen/Downloads/", "index2.html"));
////        return new SpringResourceTemplateResource(resource, Charset.defaultCharset().name());
//    }
//}
