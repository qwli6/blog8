package me.lqw.blog8.plugins.md;


import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.profile.pegdown.Extensions;
import com.vladsch.flexmark.profile.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import me.lqw.blog8.util.StringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;

/**
 * FlexMarkdownHandler markdown 解析器
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
@Component
@Conditional(FlexMarkdownCondition.class)
@ConditionalOnWebApplication
public class FlexMarkdownHandler implements MarkdownParser {

    final private static DataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(true, Extensions.ALL);

    /**
     * 解析器
     */
    private final Parser parser;

    /**
     * html 渲染
     */
    private final HtmlRenderer renderer;

    /**
     * 构造函数
     */
    public FlexMarkdownHandler() {

        MutableDataSet options = new MutableDataSet();
        this.parser = Parser.builder(OPTIONS).build();
        this.renderer = HtmlRenderer.builder(OPTIONS).build();
    }

    @Override
    public String parse(String html) {
        if (StringUtil.isBlank(html)) {
            return "";
        }
        Document node = parser.parse(html);
        return renderer.render(node);
    }

    @Override
    public Map<Integer, String> parseMap(Map<Integer, String> markdownMap) {
        if (CollectionUtils.isEmpty(markdownMap)) {
            return Collections.emptyMap();
        }

        markdownMap.replaceAll((i, v) -> parse(markdownMap.get(i)));

        return markdownMap;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
