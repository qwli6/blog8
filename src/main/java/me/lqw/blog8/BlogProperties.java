package me.lqw.blog8;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 博客配置文件
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Validated
@ConfigurationProperties(prefix = "blog.core")
@Component
public class BlogProperties {

    /**
     * 文件开关
     */
    private boolean fileEnable;

    /**
     * 启动时是否重构索引
     */
    private boolean rebuildIndexWhenStartup;

    public boolean isFileEnable() {
        return fileEnable;
    }

    public void setFileEnable(boolean fileEnable) {
        this.fileEnable = fileEnable;
    }

    public boolean isRebuildIndexWhenStartup() {
        return rebuildIndexWhenStartup;
    }

    public void setRebuildIndexWhenStartup(boolean rebuildIndexWhenStartup) {
        this.rebuildIndexWhenStartup = rebuildIndexWhenStartup;
    }
}
