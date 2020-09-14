package me.lqw.blog8.web.configuration;

import me.lqw.blog8.exception.BlogMessageCodeResolver;
import me.lqw.blog8.exception.resolver.BlogHandlerExceptionResolver;
import me.lqw.blog8.file.FileProperties;
import me.lqw.blog8.plugins.template.TemplateService;
import me.lqw.blog8.plugins.thymeleaf.ExtendSpringResourceTemplateResolver;
import me.lqw.blog8.service.BlackIpService;
import me.lqw.blog8.service.BlogConfigService;
import me.lqw.blog8.service.RememberMeService;
import me.lqw.blog8.web.filter.BlackIpFilter;
import me.lqw.blog8.web.filter.ContextFilter;
import me.lqw.blog8.web.interceptor.AuthenticationHandlerInterceptor;
import me.lqw.blog8.web.interceptor.CheckAccessLoginPageInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.lang.NonNull;
import org.springframework.util.PathMatcher;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.util.UrlPathHelper;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WebMvc 配置信息
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Configuration
public class WebAutoConfigurationSupport extends WebMvcConfigurationSupport {


    private final TemplateService templateService;


    private final FileProperties fileProperties;

    /**
     * 记住我 service
     */
    private final RememberMeService rememberMeService;

    /**
     * 异常解析器
     */
    private final BlogHandlerExceptionResolver handlerExceptionResolver;

    /**
     * 构造方法注入
     *
     * @param rememberMeService 记住我
     * @param exceptionResolver 异常解析器
     */
    public WebAutoConfigurationSupport(RememberMeService rememberMeService,
                                       TemplateService templateService,
                                       FileProperties fileProperties,
                                       BlogHandlerExceptionResolver exceptionResolver) {
        this.handlerExceptionResolver = exceptionResolver;
        this.rememberMeService = rememberMeService;
        this.templateService = templateService;
        this.fileProperties = fileProperties;

    }

    @Override
    protected MessageCodesResolver getMessageCodesResolver() {
        return new BlogMessageCodeResolver();
    }

    //    @Bean
//    public TemplateEngine templateEngine(){
//        SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
//        springResourceTemplateResolver.set
//        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
//        springTemplateEngine.set
//    ThymeleafView
//    }


//    @Bean
//    public SpringTemplateEngine templateEngine(){
//        ExtendSpringResourceTemplateResolver templateResolver = new ExtendSpringResourceTemplateResolver();
//
//        templateResolver.setSuffix(".html");
//
//        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
//        springTemplateEngine.setTemplateResolver(templateResolver);
//        springTemplateEngine.addDialect(new AbstractProcessorDialect("template dialect", "template", StandardDialect.PROCESSOR_PRECEDENCE) {
//            @Override
//            public Set<IProcessor> getProcessors(String dialectPrefix) {
//                DataTagProcessors dataTagProcessors = new DataTagProcessors(dialectPrefix);
//                Set<IProcessor> processorSet = new HashSet<>();
//                processorSet.add(dataTagProcessors);
//                return processorSet;
//            }
//        });
//
//        return springTemplateEngine;
//    }

//
//    @Bean
//    public ExtendSpringResourceTemplateResolver viewResolver(TemplateService templateService){
//        ExtendSpringResourceTemplateResolver viewResolver = new ExtendSpringResourceTemplateResolver(templateService);
////        viewResolver
////        viewResolver.setTemplateEngine(templateEngine());
//        viewResolver.setOrder(0);
//        viewResolver.setTemplateMode(TemplateMode.HTML);
//        viewResolver.setSuffix(".html");
//        viewResolver.setPrefix("");
//        viewResolver.setCacheable(false);
//
//
//        templateService.registerAllTemplate();
//        return viewResolver;
//    }




    /**
     * 添加拦截器
     *
     * @param registry registry
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        //授权拦截请求
        AuthenticationHandlerInterceptor authenticationHandlerInterceptor = new AuthenticationHandlerInterceptor(rememberMeService);
        CheckAccessLoginPageInterceptor checkAccessLoginPageInterceptor = new CheckAccessLoginPageInterceptor();
        registry.addInterceptor(checkAccessLoginPageInterceptor).addPathPatterns(checkAccessLoginPageInterceptor.matchPatterns());
        registry.addInterceptor(authenticationHandlerInterceptor).addPathPatterns(authenticationHandlerInterceptor.matchPatterns());
    }

    @Override
    protected void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/**").addResourceLocations("file:" + fileProperties.getUploadPath());
    }


    @Bean
    public ExtendsHandlerAdapter handlerAdapter(){
        Map<String, String> map = new HashMap<>();
        map.put("hello", "world");
        return new ExtendsHandlerAdapter(map);
    }

    /**
     * 配置黑名单过滤器
     *
     * @param blackIpService 黑名单业务实现类
     * @return 过滤器注册 bean
     */
    @Bean
    public FilterRegistrationBean<BlackIpFilter> filterFilterRegistrationBean(BlackIpService blackIpService) {
        BlackIpFilter blackIpFilter = new BlackIpFilter(blackIpService);
        FilterRegistrationBean<BlackIpFilter> blackIpFilterBean = new FilterRegistrationBean<>();
        blackIpFilterBean.setName(BlackIpFilter.class.getSimpleName());
        blackIpFilterBean.setOrder(blackIpFilter.getOrder());
        blackIpFilterBean.setFilter(blackIpFilter);
        blackIpFilterBean.setUrlPatterns(blackIpFilter.matchPatterns());
        return blackIpFilterBean;
    }

    @Override
    protected void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT","PATCH")
                .maxAge(3600);
    }

    /**
     * 上下文过滤器
     *
     * @param configService 配置类
     * @return 过滤器注册 bean
     */
    @Bean
    public FilterRegistrationBean<ContextFilter> contextFilterFilterRegistrationBean(BlogConfigService configService) {
        ContextFilter contextFilter = new ContextFilter(configService);
        FilterRegistrationBean<ContextFilter> contextFilterBean = new FilterRegistrationBean<>();
        contextFilterBean.setName(ContextFilter.class.getSimpleName());
        contextFilterBean.setOrder(contextFilter.getOrder());
        contextFilterBean.setFilter(contextFilter);
        contextFilterBean.setUrlPatterns(contextFilter.matchPatterns());
        return contextFilterBean;
    }

    /**
     * 配置自定义的异常解析器
     *
     * @param resolvers resolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(0, handlerExceptionResolver);
    }

//    @Override
//    public HandlerMapping resourceHandlerMapping(UrlPathHelper urlPathHelper, PathMatcher pathMatcher, ContentNegotiationManager contentNegotiationManager, FormattingConversionService conversionService, ResourceUrlProvider resourceUrlProvider) {
//        return super.resourceHandlerMapping(urlPathHelper, pathMatcher, contentNegotiationManager, conversionService, resourceUrlProvider);
//    }
}
