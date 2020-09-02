package me.lqw.blog8.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具类
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public class StringUtil {

    /**
     * 邮箱 REGEX
     */
    public static final String EMAIL_REGEX = "^([a-z0-9A-Z]+[-|_|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$";

    /**
     * 电话 REGEX
     */
    public static final String PHONE_REGEX = "^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\\\d{8})|(0\\\\d{2}-\\\\d{8})|(0\\\\d{3}-\\\\d{7})$";

    /**
     * StringUtils 构造函数
     */
    private StringUtil() {
        super();
    }


    /**
     *
     * @param first 第一个字符串
     * @param second 第二个字符串
     * @return true | false
     * @since 2.0
     */
    public static Boolean equalsIgnoreCase(final Object first, final Object second) {
        if (first == null && second == null) {
            return Boolean.TRUE;
        }
        if (first == null || second == null) {
            return Boolean.FALSE;
        }
        return first.toString().equalsIgnoreCase(second.toString());
    }


    /**
     * 判断内容是否为 null | "" | " "
     *
     * @param str str
     * @return false | true
     */
    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }

        str = str.trim();

        return "".equals(str);
    }

    /**
     * 判断内容是否不为 null | "" | " "
     *
     * @param str str
     * @return false | true
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    /**
     * 校验是否是一个合法的邮箱
     *
     * @param str str
     * @return boolean  false | true
     */
    public static boolean isEmail(String str) {
        if (isNotBlank(str)) {
            return false;
        }
        Pattern regex = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = regex.matcher(str);
        return matcher.matches();
    }


    /**
     * 校验是否是一个合法的 phone
     *
     * @param str str
     * @return boolean false | true
     */
    public static boolean isPhone(String str) {
        if (isNotBlank(str)) {
            return false;
        }
        Pattern regex = Pattern.compile(PHONE_REGEX);
        Matcher matcher = regex.matcher(str);
        return matcher.matches();
    }

    /**
     * 校验字符串是否包含 xx 字符
     *
     * @param source source
     * @param target target
     * @return true | false
     */
    public static boolean contains(String source, String target) {
        return source.contains(target);
    }
}
