//package me.lqw.blog8.plugins.thymeleaf;
//
//import org.springframework.web.servlet.HandlerMapping;
//import org.thymeleaf.spring5.view.ThymeleafView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
///**
// * 扩展 Thymeleaf
// * 主要目的是为了实现模板可缓存, 同时为了实现模板可动态切换
// *
// * @author liqiwen
// * @version 1.5
// * @see org.thymeleaf.spring5.view.ThymeleafView
// * @since 1.5
// */
//public class ExtendThymeleafView extends ThymeleafView {
//
//
//    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";
//
//
//    private final Map<String, String> urlMap = new HashMap<>();
//
//    public ExtendThymeleafView() {
//        super();
//        urlMap.put("/", "");
//    }
//
//    /**
//     * 渲染页面
//     *
//     * @param markupSelectorsToRender markupSelectorsToRender
//     * @param model                   model
//     * @param request                 request
//     * @param response                response
//     * @throws Exception Exception
//     */
//    @Override
//    protected void renderFragment(Set<String> markupSelectorsToRender, Map<String, ?> model,
//                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
////        request.setAttribute("name", "test");
//
//        String attribute = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//        if (urlMap.containsKey(attribute)) {
//            super.renderFragment(markupSelectorsToRender, model, request, response);
//        }
//    }
//
//    @Override
//    public String getContentType() {
//        return DEFAULT_CONTENT_TYPE;
//    }
//}
