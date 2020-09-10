package me.lqw.blog8.plugins.template;

import me.lqw.blog8.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模板业务处理
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
@Component
public class TemplateService implements InitializingBean, DisposableBean {


    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());


    /**
     * 存储模板相关的内容
     */
    private Map<String, String> templateMap = new ConcurrentHashMap<>();


    /**
     * 获取模板资源
     * @param templateName templateName
     * @return Resource
     */
    public Resource getTemplateResource(String templateName) {

        logger.info("获取模板资源:" + templateName);

        if(StringUtil.isBlank(templateName)){

            return null;
        }

        String orDefault = templateMap.get(templateName);

        if(StringUtil.isNotBlank(orDefault)){
            return new FileSystemResource(Paths.get("/Users/liqiwen/Downloads/", "login.html"));
        }

        return null;

    }

    /**
     * 注册所有的模板的到系统内存中
     */
    public void registerAllTemplate() {
        logger.info("注册所有的模板到系统内存中");
        templateMap.put("/", "index");
        templateMap.put("/guestbook", "guestbook");
        templateMap.put("/moments", "moments");
        templateMap.put("/moment/{id}", "moments");
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }
}
