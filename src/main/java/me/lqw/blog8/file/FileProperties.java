package me.lqw.blog8.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Conditional(FileCondition.class)
@Component
@ConfigurationProperties(prefix = "blog.file")
public class FileProperties {

    private String uploadPath;

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}
