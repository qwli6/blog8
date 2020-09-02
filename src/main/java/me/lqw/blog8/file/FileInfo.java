package me.lqw.blog8.file;

import java.io.Serializable;

/**
 * 文件信息
 *
 * @author liqiwen
 * @version 1.2
 */
public class FileInfo implements Serializable {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件扩展名
     */
    private FileTypeEnum ext;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 是否可以编辑
     */
    private Boolean canEdit;

    /**
     * 是否是目录
     */
    private Boolean directory;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 无参构造方法
     */
    public FileInfo() {
        super();
    }

    /**
     * 有参构造方法
     *
     * @param fileInfo fileInfo
     */
    public FileInfo(FileInfo fileInfo) {
        super();
        this.fileName = fileInfo.fileName;
        this.ext = fileInfo.ext;
        this.size = fileInfo.size;
        this.canEdit = fileInfo.canEdit;
        this.directory = fileInfo.directory;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getDirectory() {
        return directory;
    }

    public void setDirectory(Boolean directory) {
        this.directory = directory;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileTypeEnum getExt() {
        return ext;
    }

    public void setExt(FileTypeEnum ext) {
        this.ext = ext;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
