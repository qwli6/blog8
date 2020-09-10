package me.lqw.blog8.web.configuration;

import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.view.ThymeleafView;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.Locale;
import java.util.Map;

/**
 * 自定义视图解析器
 * @author liqiwen
 * @since 2.0
 * @version 2.0
 */
public class MyViewResolver extends ThymeleafViewResolver implements Ordered {

    private Map<String, String> viewMap;

    public MyViewResolver(Map<String, String> viewMap) {
        this.viewMap = viewMap;
    }

    public MyViewResolver() {
    }

    /**
     * 排序
     * @return int
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 根据传入的视图名称查找视图, 返回一个 View 对象
     * @param viewName 视图名称
     * @param locale locale
     * @return View
     * @throws Exception Exception
     */
    @Override
    public View resolveViewName(@NonNull String viewName, @NonNull Locale locale) throws Exception {
        if(CollectionUtils.isEmpty(viewMap)){
            return null;
        }
        String viewContent = viewMap.get(viewName);

        //通过视图名称返回一个 View 对象

        return new MyView(viewContent);
    }
}
