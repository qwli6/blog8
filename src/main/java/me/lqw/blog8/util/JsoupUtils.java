package me.lqw.blog8.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Optional;

/**
 * jsoupUtils 工具类
 * @author liqiwen
 * @version 1.0
 */
public class JsoupUtils {

	private JsoupUtils() {
		super();
	}

	public static Optional<String> getFirstImage(String content) {
		return getFirstImage(Jsoup.parse(content));
	}

	public static Optional<String> getFirstImage(Document document) {
		return Optional.ofNullable(document.selectFirst("img[src],video[poster]")).map(ele -> {
			if (ele.normalName().equals("img")) {
				return ele.attr("src");
			} else {
				return ele.attr("poster");
			}
		});
	}
}