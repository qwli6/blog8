package me.lqw.blog8.service;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Markdown 解析器
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Component
public class MarkdownHandler implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final HtmlRenderer renderer;
    private final Parser parser;

    List<Extension> extensions = Arrays.asList(TablesExtension.create(),
            AutolinkExtension.create(), HeadingAnchorExtension.create(),
            TaskListItemsExtension.create());


    public MarkdownHandler(){
        this.renderer = HtmlRenderer.builder().extensions(extensions).build();
        this.parser = Parser.builder().extensions(extensions).build();
    }

    public Map<Integer, String> toHtmls(Map<Integer, String> markdownMap){
        if(markdownMap.isEmpty()){
            return new HashMap<>();
        }
        markdownMap.replaceAll((i, v) -> toHtml(markdownMap.get(i)));
        return markdownMap;
    }


    public String toHtml(String content){
        if(StringUtils.isEmpty(content)){
            return "";
        }
        String render = renderer.render(parser.parse(content));
        Document document = Jsoup.parse(render);
        document.select("a").forEach(a -> {
            final String src = a.attr("href");
            //如果不是本网站内部的链接，全部添加 target = _blank 属性
            if(!StringUtils.startsWithIgnoreCase(src, "https://localhost:8080") && !StringUtils.startsWithIgnoreCase("src", "#")){
                a.attr("target", "_blank");
            }

            a.removeAttr("id");
        });

        String html = document.select("body").html();
        //渲染的内容为
        logger.info("render html:[{}]", html);
        return html;
    }
}
