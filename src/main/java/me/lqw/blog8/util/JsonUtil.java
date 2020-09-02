package me.lqw.blog8.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jackson 工具类
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
public class JsonUtil {

    /**
     * 日志处理
     */
    private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * ObjectMapper 对象
     */
    private static final ObjectMapper objectMapper;

    /**
     * 初始化 ObjectMapper 对象
     */
    static {
        objectMapper = new ObjectMapper();
    }

    /**
     * 构造方法
     */
    private JsonUtil() {
        super();
    }


    /**
     * 对象转换成 json 串
     *
     * @param object object
     * @return String
     */
    public static String toJsonString(Object object) {
        if (object == null) {
            return "";
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("toJsonString occurred exception:[{}]", e.getMessage(), e);
        }
        return "";
    }

    /**
     * 将字符串转换成对象
     *
     * @param input input
     * @param clazz class
     * @param <T>   T
     * @return T
     */
    public static <T> T parseObject(String input, Class<T> clazz) {

        if (StringUtil.isBlank(input)) {
            return null;
        }
        try {
            return objectMapper.readValue(input, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Jackson 转换成对象错误, [{}]", input, e);
        }
        return null;
    }

    /**
     * 将字符串转换成对象
     *
     * @param input input
     * @param type  type
     * @param <T>   T
     * @return T
     */
    public static <T> T parseObject(String input, TypeReference<T> type) {
        if (StringUtil.isBlank(input)) {
            return null;
        }
        try {
            return objectMapper.readValue(input, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Jackson 转换成对象错误, [{}]", input, e);
        }
        return null;
    }
}
