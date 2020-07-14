package me.lqw.blog8.file;

import java.io.Serializable;
import java.util.Date;

public class FileInfo implements Serializable {

    private String fileName;

    private String fileSuffix;

    private long size;

    private long lastModified;

    private Boolean canEdit;

    private Boolean dir;

    public Boolean getDir() {
        return dir;
    }

    public void setDir(Boolean dir) {
        this.dir = dir;
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

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
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
