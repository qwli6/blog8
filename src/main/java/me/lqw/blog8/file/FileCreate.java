package me.lqw.blog8.file;

import java.io.Serializable;

public class FileCreate implements Serializable {

    private String fileName;

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
