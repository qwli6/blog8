package me.lqw.blog8.web;

import me.lqw.blog8.BlogConstants;
import me.lqw.blog8.BlogContext;
import me.lqw.blog8.exception.BlogHandlerExceptionResolver;
import me.lqw.blog8.exception.Message;
import me.lqw.blog8.exception.UnauthorizedException;
import me.lqw.blog8.file.BlogResourceHttpRequestHandler;
import me.lqw.blog8.file.FileResourceResolver;
import me.lqw.blog8.file.FileService;
import me.lqw.blog8.service.BlackIpService;
import me.lqw.blog8.service.BlogConfigService;
import me.lqw.blog8.util.WebUtil;
import me.lqw.blog8.web.filter.BlackIpFilter;
import me.lqw.blog8.web.filter.ContextFilter;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.util.PathMatcher;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Configuration
public class WebConfigurerAdapter implements WebMvcConfigurer {

    private final FileService fileService;

    private final BlogConfigService configService;

    private final BlogHandlerExceptionResolver resolver;




    public WebConfigurerAdapter(FileService fileService, BlogConfigService configService ,BlogHandlerExceptionResolver resolver) {
        this.resolver = resolver;
        this.fileService = fileService;
        this.configService = configService;
//        handlerExceptionResolvers.add(new BlogHandlerExceptionResolver());
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

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

    }


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
        resolvers.add(resolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //自定义资源处理器，需要额外扩展

    }

//    @Bean
//    public SimpleUrlHandlerMapping fileMapping(
//                    FileResourceResolver fileResourceResolver,
//                    ResourceProperties resourceProperties,
////                   ContentNegotiationManager contentNegotiationManager,
//                   @Qualifier("mvcUrlPathHelper") UrlPathHelper urlPathHelper,
//                   @Qualifier("mvcPathMatcher") PathMatcher pathMatcher, WebApplicationContext context) {
//        BlogResourceHttpRequestHandler requestHandler = new BlogResourceHttpRequestHandler(fileResourceResolver,resourceProperties);
//        requestHandler.setApplicationContext(context);
//        requestHandler.setServletContext(Objects.requireNonNull(context.getServletContext()));
//
//        if(urlPathHelper != null){
//            requestHandler.setUrlPathHelper(urlPathHelper);
//        }
//
//        Map<String, MediaType> mediaTypeMap = new HashMap<>();
//        mediaTypeMap.put("gif", MediaType.IMAGE_GIF);
//        mediaTypeMap.put("jpeg", MediaType.IMAGE_JPEG);
//        mediaTypeMap.put("png", MediaType.IMAGE_PNG);
//        requestHandler.setMediaTypes(mediaTypeMap);
////        if(contentNegotiationManager != null){
////            requestHandler.setContentNegotiationManager(contentNegotiationManager);
////        }
//        try {
//            requestHandler.afterPropertiesSet();
//        } catch (Throwable ex){
//            throw new BeanInitializationException("Failed to init ResourceHttpRequestHandler", ex);
//        }
//        Map<String, HttpRequestHandler> urlMap = new LinkedHashMap<>();
//        urlMap.put("/images/**", requestHandler);
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
