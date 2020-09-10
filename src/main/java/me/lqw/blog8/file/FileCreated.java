package me.lqw.blog8.file;

import java.io.Serializable;

/**
 * 创建文件参数
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class FileCreated implements Serializable {

    /**
     * 待创建的文件名称
     */
    private String fileName;

    /**
     * 创建文件类型
     * 为 0|null 创建目录
     * 为 1|其他 创建文件
     */
    private Integer fileType;

    /**
     * 创建文件存放路径
     */
    private String targetPath;

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }
}
