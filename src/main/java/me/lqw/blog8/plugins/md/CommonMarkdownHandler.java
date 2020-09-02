package me.lqw.blog8.plugins.md;

import me.lqw.blog8.util.StringUtil;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * CommonMarkdown 解析器
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Component
@Conditional(CommonMarkdownCondition.class)
@ConditionalOnWebApplication
public class CommonMarkdownHandler implements MarkdownParser {

    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * html 渲染
     */
    private final HtmlRenderer renderer;

    /**
     * markdown 节点解析器
     */
    private final Parser parser;

    /**
     * 解析扩展插件
     */
    List<Extension> extensions = Arrays.asList(TablesExtension.create(),
            AutolinkExtension.create(), HeadingAnchorExtension.create(),
            TaskListItemsExtension.create());

    /**
     * 构造方法
     */
    public CommonMarkdownHandler() {
        this.renderer = HtmlRenderer.builder().extensions(extensions).build();
        this.parser = Parser.builder().extensions(extensions).build();
    }

    /**
     * 解析多个 html
     *
     * @param markdownMap markdownMap
     * @return Map<Integer, String>
     */
    @Override
    public Map<Integer, String> parseMap(Map<Integer, String> markdownMap) {

        if (CollectionUtils.isEmpty(markdownMap)) {
            return Collections.emptyMap();
        }

        markdownMap.replaceAll((i, v) -> parse(markdownMap.get(i)));

        return markdownMap;
    }

    /**
     * 解析单个 markdown
     *
     * @param markdown markdown
     * @return String
     */
    @Override
    public String parse(String markdown) {
        if (StringUtil.isBlank(markdown)) {
            return "";
        }
        Node node = parser.parse(markdown);
        String render = renderer.render(node);
        Document document = Jsoup.parse(render);
        document.select("a").forEach(a -> {
            final String src = a.attr("href");
            //如果不是本网站内部的链接，全部添加 target = _blank 属性
//            if(!StringUtils.startsWithIgnoreCase(src, "https://localhost:8080") && !StringUtils.startsWithIgnoreCase("src", "#")){
//                a.attr("target", "_blank");
//            }

            a.removeAttr("id");
        });

        String html = document.select("body").html();
        //渲染的内容为
        logger.info("render html:[{}]", html);
        return html;
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
