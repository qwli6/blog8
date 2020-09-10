package me.lqw.blog8.plugins.md;

import me.lqw.blog8.util.StringUtil;
import org.checkerframework.checker.units.qual.A;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    /**
     * html 渲染
     */
    private final HtmlRenderer renderer;

    /**
     * markdown 节点解析器
     */
    private final Parser parser;

    /**
     * 构造方法
     */
    public CommonMarkdownHandler() {
        /**
         * 解析扩展插件
         */
        //表格扩展
        //标题生成锚点
        //任务列表扩展
        List<Extension> extensions = Arrays.asList(
                //表格扩展
                TablesExtension.create(),

                AutolinkExtension.create(),

                //标题生成锚点
                HeadingAnchorExtension.create(),

                //任务列表扩展
                TaskListItemsExtension.create());
        this.renderer = HtmlRenderer.builder().extensions(extensions).attributeProviderFactory(new AttributeProviderFactory() {
            @Override
            public AttributeProvider create(AttributeProviderContext attributeProviderContext) {
                return new ImageAttributeProvider();
            }
        }).attributeProviderFactory(new AttributeProviderFactory() {
            @Override
            public AttributeProvider create(AttributeProviderContext attributeProviderContext) {
                return new LinkAttributeProvider();
            }
        }).nodeRendererFactory(new HtmlNodeRendererFactory() {
            @Override
            public NodeRenderer create(HtmlNodeRendererContext htmlNodeRendererContext) {
                return new ImageNodeRenderer(htmlNodeRendererContext);
            }
        }).build();
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

        WordCountVisitor visitor = new WordCountVisitor();
        node.accept(visitor);

        return renderer.render(node);
    }

    @Override
    public int getOrder() {
        return 2;
    }



    static class ImageNodeRenderer implements NodeRenderer {

        private final HtmlWriter htmlWriter;

        public ImageNodeRenderer(HtmlNodeRendererContext context) {
            this.htmlWriter = context.getWriter();
        }

        @Override
        public Set<Class<? extends Node>> getNodeTypes() {
            return Collections.singleton(IndentedCodeBlock.class);
        }

        @Override
        public void render(Node node) {
            // We only handle one type as per getNodeTypes, so we can just cast it here.
            IndentedCodeBlock codeBlock = (IndentedCodeBlock) node;



            htmlWriter.line();
            htmlWriter.tag("pre111");
            htmlWriter.text(codeBlock.getLiteral());
            htmlWriter.tag("/pre111");
            htmlWriter.line();
        }
    }


    static class LinkAttributeProvider implements AttributeProvider {

        //连接由 https 来判断是否在本页面打开还是新开窗口来打开
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            if(node instanceof Link) {
                String href = attributes.get("href");
                if(StringUtil.isNotBlank(href)){
                    if(href.startsWith("http://localhost:8080")){
                        attributes.put("target", "_self");
                    } else {
                        attributes.put("target", "_blank");
                    }
                }
            }
        }
    }


    static class ImageAttributeProvider implements AttributeProvider {

        //Node img 节点
        //tagName 标签名称
        //attributes 属性
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            if(node instanceof Image) {
                Node parent = node.getParent();
                attributes.put("class", "border");
                attributes.put("style", "height:50%; width: 50%");
            }
        }
    }


    static class WordCountVisitor extends AbstractVisitor {
        int wordCount = 0;

        @Override
        public void visit(Text text) {
            // Count words (this is just an example, don't actually do it this way for various reasons).
            wordCount += text.getLiteral().split("\\W+").length;

            // Descend into children (could be omitted in this case because Text nodes don't have children).
            visitChildren(text);
        }
    }
}
