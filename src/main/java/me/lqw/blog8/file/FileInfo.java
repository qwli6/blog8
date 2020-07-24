package me.lqw.blog8.file;

import java.io.Serializable;

/**
 * 文件信息
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
     * 最近修改时间
     */
    private long lastModified;

    /**
     * 是否可以编辑
     */
    private Boolean canEdit;

    /**
     * 是否是目录
     */
    private Boolean directory;

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

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
