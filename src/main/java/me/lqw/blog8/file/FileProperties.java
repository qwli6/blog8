package me.lqw.blog8.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * 系统文件属性
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Conditional(FileCondition.class)
@Component
@ConfigurationProperties(prefix = "blog.file")
public class FileProperties {

    /**
     * 文件上传路径
     */
    private String uploadPath;

    /**
     * 文件上传根路径
     */
    private String fileRootPath;

    public String getFileRootPath() {
        return fileRootPath;
    }

    public void setFileRootPath(String fileRootPath) {
        this.fileRootPath = fileRootPath;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}
