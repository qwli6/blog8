//package me.lqw.blog8.plugins.thymeleaf;
//
//import org.springframework.web.servlet.View;
//import org.thymeleaf.spring5.view.ThymeleafViewResolver;
//
//import java.util.Locale;
//
///**
// * 扩展 ThymeleafViewResolver
// * 主要目的是为了实现模板可缓存, 同时为了实现模板可动态切换
// *
// * @author liqiwen
// * @version 1.5
// * @see ThymeleafViewResolver
// * @since 1.5
// */
//public class ExtendThymeleafViewResolver extends ThymeleafViewResolver {
//
//
//    @Override
//    protected View loadView(String viewName, Locale locale) throws Exception {
//        System.out.println("load view");
//        return super.loadView(viewName, locale);
//    }
//
//    @Override
//    public View resolveViewName(String viewName, Locale locale) throws Exception {
//        System.out.println("resolveViewName ");
//        return super.resolveViewName(viewName, locale);
//    }
//}
