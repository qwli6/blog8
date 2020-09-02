package me.lqw.blog8.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 上传模型
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class UploadModel implements Serializable {

    /**
     * 上传额外属性
     */
    private String extraField;

    /**
     * 上传目标路径
     */
    private String targetPath;

    /**
     * 上传文件内容
     */
    private MultipartFile[] files;

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getExtraField() {
        return extraField;
    }

    public void setExtraField(String extraField) {
        this.extraField = extraField;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }
}
