package me.lqw.blog8.service;

import me.lqw.blog8.model.Article;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.Renderer;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MarkdownHandler implements Serializable {

    private final HtmlRenderer renderer;
    private final Parser parser;

    public MarkdownHandler(){
        this.renderer = HtmlRenderer.builder().build();
        this.parser = Parser.builder().build();
    }

    public Map<Integer, String> toHtmls(List<Article> articles){
        if(articles.isEmpty()){
            return new HashMap<>();
        }
        Map<Integer, String> dataMap = new HashMap<>(articles.size()*2);
        for(Article article: articles){
            dataMap.put(article.getId(), toHtml(article.getContent()));
            dataMap.put(-article.getId(), toHtml(article.getDigest()));
        }
        return dataMap;
    }


    public String toHtml(String content){
        if(StringUtils.isEmpty(content)){
            return "";
        }
        return renderer.render(parser.parse(content));
    }
}
