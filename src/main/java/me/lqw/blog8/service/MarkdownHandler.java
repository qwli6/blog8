package me.lqw.blog8.service;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
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
        return renderer.render(parser.parse(content));
    }
}
