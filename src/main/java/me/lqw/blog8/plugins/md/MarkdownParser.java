package me.lqw.blog8.plugins.md;

import org.springframework.core.Ordered;

import java.util.Map;

/**
 * Markdown 解析器
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public interface MarkdownParser extends Ordered {

    /**
     * 解析 markdown 接口
     *
     * @param markdown markdown
     * @return string
     */
    String parse(String markdown);

    /**
     * 批量解析 markdown 接口
     *
     * @param markdownMap markdownMap
     * @return Map<Integer, String>
     */
    Map<Integer, String> parseMap(Map<Integer, String> markdownMap);
}
