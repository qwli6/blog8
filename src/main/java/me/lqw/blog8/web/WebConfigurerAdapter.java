package me.lqw.blog8.web;

import me.lqw.blog8.exception.resolver.BlogHandlerExceptionResolver;
import me.lqw.blog8.file.FileService;
import me.lqw.blog8.service.BlackIpService;
import me.lqw.blog8.service.BlogConfigService;
import me.lqw.blog8.web.filter.BlackIpFilter;
import me.lqw.blog8.web.filter.ContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
public class WebConfigurerAdapter implements WebMvcConfigurer {

    private final FileService fileService;

    private final BlogConfigService configService;


    public WebConfigurerAdapter(FileService fileService, BlogConfigService configService) {
        this.fileService = fileService;
        this.configService = configService;
//        handlerExceptionResolvers.add(new BlogHandlerExceptionResolver());
    }


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        registry.addInterceptor(new HandlerInterceptor() {
//            @Override
//            public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
//                                     Object handler) throws Exception {
//                if(WebUtil.isConsoleRequest(request) && !BlogContext.isAuthorized()){
//                    request.setAttribute(BlogConstants.REDIRECT_URL_ATTRIBUTE,
//                            ServletUriComponentsBuilder.fromRequest(request).build().toString());
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//                    return false;
//                }
//
//                return true;
//            }
//        }).addPathPatterns("/console/**");
//
//    }


    @Bean
    public FilterRegistrationBean<BlackIpFilter> filterFilterRegistrationBean(BlackIpService blackIpService){

        BlackIpFilter blackIpFilter = new BlackIpFilter(blackIpService);

        FilterRegistrationBean<BlackIpFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setName(BlackIpFilter.class.getSimpleName());
        filterRegistrationBean.setOrder(10);
        filterRegistrationBean.setFilter(blackIpFilter);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));

        return filterRegistrationBean;
    }


    @Bean
    public FilterRegistrationBean<ContextFilter> contextFilterFilterRegistrationBean(BlogConfigService configService){
        ContextFilter contextFilter = new ContextFilter(configService);

        FilterRegistrationBean<ContextFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setName(ContextFilter.class.getSimpleName());
        filterRegistrationBean.setOrder(9);
        filterRegistrationBean.setFilter(contextFilter);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));

        return filterRegistrationBean;
    }


    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new BlogHandlerExceptionResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //自定义资源处理器，需要额外扩展

    }

//    @Bean
//    public SimpleUrlHandlerMapping fileMapping(
//            FileResourceResolver fileResourceResolver,
//            ResourceProperties resourceProperties,
////                   ContentNegotiationManager contentNegotiationManager,
//            @Qualifier("mvcUrlPathHelper") UrlPathHelper urlPathHelper,
//            @Qualifier("mvcPathMatcher") PathMatcher pathMatcher, WebApplicationContext context) {
//        BlogResourceHttpRequestHandler requestHandler = new BlogResourceHttpRequestHandler(fileResourceResolver,resourceProperties, fileService);
//        requestHandler.setApplicationContext(context);
//        requestHandler.setServletContext(Objects.requireNonNull(context.getServletContext()));
//
//        if(urlPathHelper != null){
//            requestHandler.setUrlPathHelper(urlPathHelper);
//        }
//
////        Map<String, MediaType> mediaTypeMap = new HashMap<>();
////        mediaTypeMap.put("gif", MediaType.IMAGE_GIF);
////        mediaTypeMap.put("jpeg", MediaType.IMAGE_JPEG);
////        mediaTypeMap.put("png", MediaType.IMAGE_PNG);
////        requestHandler.setMediaTypes(mediaTypeMap);
////        if(contentNegotiationManager != null){
////            requestHandler.setContentNegotiationManager(contentNegotiationManager);
//////        }
////        try {
////            requestHandler.afterPropertiesSet();
////        } catch (Throwable ex){
////            throw new BeanInitializationException("Failed to init ResourceHttpRequestHandler", ex);
////        }
////        Map<String, HttpRequestHandler> urlMap = new LinkedHashMap<>();
////        urlMap.put("/images/**", requestHandler);
//        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping(urlMap);
//        simpleUrlHandlerMapping.setOrder(Ordered.LOWEST_PRECEDENCE-10);
//        simpleUrlHandlerMapping.setPathMatcher(pathMatcher);
//        if(urlPathHelper != null) {
//            simpleUrlHandlerMapping.setUrlPathHelper(urlPathHelper);
//        }
//
//        return simpleUrlHandlerMapping;
//    }
}
