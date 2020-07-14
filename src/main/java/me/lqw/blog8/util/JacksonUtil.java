package me.lqw.blog8.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JacksonUtil {

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
}
