package me.lqw.blog8.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 文件系统配置
 * @author liqiwen
 * @since 1.2
 * @version 1.2
 */
@Configuration
@Conditional(FileCondition.class)
@ConditionalOnProperty(prefix = "blog.file", value = "enabled", havingValue = "true")
@ConditionalOnWebApplication
public class LocalFileSystemConfiguration extends WebMvcConfigurationSupport implements InitializingBean {

    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * 文件服务
     */
    private final FileProperties fileProperties;

    /**
     * 构造方法
     * @param fileProperties fileProperties
     */
    public LocalFileSystemConfiguration(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }


    /**
     * 暂时使用默认的资源处理器
     * @param registry registry
     */
    @Override
    protected void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/**").addResourceLocations("file:" + fileProperties.getUploadPath());
    }

    /**
     * 初始化完成后回调
     */
    @Override
    public void afterPropertiesSet() {
        logger.info("LocalFileSystemConfiguration initial succeed");
    }
}
