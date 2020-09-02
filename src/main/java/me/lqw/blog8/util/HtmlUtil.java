package me.lqw.blog8.util;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * HtmlClean 类，清除 xss 字符
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class HtmlUtil {

    public static final String REPLACE_STRING = "*";

    /**
     * 构造方法
     */
    private HtmlUtil() {
        //私有构造方法
        super();
    }

    /**
     * 替换 xss 字符
     *
     * @param html html
     * @return string
     */
    public static String xssEncode(String html) {
        if (StringUtils.isEmpty(html)) {
            return html;
        } else {
            html = cleanXSSAndSql(html);
        }
        StringBuilder sb = new StringBuilder(html.length() + 16);
        for (int i = 0; i < html.length(); i++) {
            char c = sb.charAt(i);
            switch (c) {
                case '>':
                    sb.append(">");
                    break;
                case '<':
                    sb.append("＜");// 转义小于号
                    break;
                case '\'':
                    sb.append("＇");// 转义单引号
                    break;
                case '\"':
                    sb.append("＂");// 转义双引号
                    break;
                case '&':
                    sb.append("＆");// 转义&
                    break;
                case '#':
                    sb.append("＃");// 转义#
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * xss校验
     *
     * @param value value
     * @return string
     */
    public static String cleanXSSAndSql(String value) {
        if (!StringUtils.isEmpty(value)) {
            // Avoid null characters
            value = value.replaceAll(" ", REPLACE_STRING);
            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<[\r\n| | ]*script[\r\n| | ]*>(.*?)</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e-xpression
            scriptPattern = Pattern.compile("src[\r\n| | ]*=[\r\n| | ]*[\\\"|\\'](.*?)[\\\"|\\']", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll(REPLACE_STRING);
            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</[\r\n| | ]*script[\r\n| | ]*>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll(REPLACE_STRING);
            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<[\r\n| | ]*script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid e-xpression(...) expressions
            scriptPattern = Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll(REPLACE_STRING);
            // Avoid /r /n:... expressions
            scriptPattern = Pattern.compile("\"\\\\s*|\\t|\\r|\\n\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }
}
