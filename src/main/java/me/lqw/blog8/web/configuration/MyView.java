package me.lqw.blog8.web.configuration;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 自定义视图
 * @since 2.2
 * @version 2.2
 */
public class MyView implements View {

    private String content;

    public MyView(String content) {
        this.content = content;
    }

    @Override
    public String getContentType() {
        return MediaType.TEXT_HTML_VALUE;
    }

    @Override
    public void render(Map<String, ?> model, @NonNull HttpServletRequest request,
                       @NonNull HttpServletResponse response) throws Exception {


        //渲染视图


    }
}
