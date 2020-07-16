package me.lqw.blog8.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * jackson 工具类
 * @author liqiwen
 * @version 1.0
 */
public class JacksonUtil {

    private final static Logger logger = LoggerFactory.getLogger(JacksonUtil.class);


    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("toJsonString occurred exception:[{}]", e.getMessage(), e);
        }
        return "";
    }

    public static <T> T parseObject(String input, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(input)) {
                return null;
            }
            return objectMapper.readValue(input, clazz);
        } catch (Exception e) {
            logger.error("jackson parse to bean error,["+input+"]", e);
            return null;
        }
    }

    public static <T> T parseObject(String input, TypeReference<T> type) {
        try {
            if (StringUtils.isEmpty(input)) {
                return null;
            }
            return objectMapper.readValue(input, type);
        } catch (Exception e) {
            logger.error("jackson parse to bean error,["+input+"]", e);
            return null;
        }
    }
}
