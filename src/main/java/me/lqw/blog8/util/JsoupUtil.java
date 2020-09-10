package me.lqw.blog8.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.util.Optional;

/**
 * jsoupUtil 工具类
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class JsoupUtil {

    /**
     * 构造方法
     */
    private JsoupUtil() {
        super();
    }

    /**
     * 获取内容里面的第一张图片
     * @param content content
     * @return String
     */
    public static Optional<String> getFirstImage(String content) {
        return getFirstImage(Jsoup.parse(content));
    }

    /**
     * 获取文档里面的第一张图片
     * @param document document
     * @return String
     */
    public static Optional<String> getFirstImage(Document document) {
        return Optional.ofNullable(document.selectFirst("img[src],video[poster]")).map(ele -> {
            if (ele.normalName().equals("img")) {
                return ele.attr("src");
            } else {
                return ele.attr("poster");
            }
        });
    }

    /**
     * 清除所有的 html 标签
     * @param content content
     * @return String
     */
    public static Optional<String> cleanAllHtml(String content){

        return Optional.ofNullable(Jsoup.clean(content, Whitelist.none()));

    }
}