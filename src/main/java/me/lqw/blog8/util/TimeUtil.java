package me.lqw.blog8.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liqiwen
 * @version 1.2
 * 时间格式化工具
 * @since 1.2
 */
public class TimeUtil {

    private static final String DEFAULT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_YYYY_MM_DD = "yyyy-MM-dd";

    private final Logger logger = LoggerFactory.getLogger(TimeUtil.class);

    private TimeUtil() {
        super();
    }
}
