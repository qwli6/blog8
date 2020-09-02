package me.lqw.blog8.file;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件属性
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class FileAttributes implements Serializable {

    /**
     * 上次访问时间
     */
    private LocalDateTime lastAccess;

    /**
     * 上次修改时间
     */
    private LocalDateTime lastModified;

    /**
     * 人类可读大小
     */
    private String humanCanReadSize;

    /**
     * 操作系统可读权限
     */
    private Boolean osReadPermission;

    /**
     * 操作系统可写权限
     */
    private Boolean osWritePermission;

    /**
     * 操作系统可执行权限
     */
    private Boolean osExecutePermission;

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getHumanCanReadSize() {
        return humanCanReadSize;
    }

    public void setHumanCanReadSize(String humanCanReadSize) {
        this.humanCanReadSize = humanCanReadSize;
    }

    public Boolean getOsReadPermission() {
        return osReadPermission;
    }

    public void setOsReadPermission(Boolean osReadPermission) {
        this.osReadPermission = osReadPermission;
    }

    public Boolean getOsWritePermission() {
        return osWritePermission;
    }

    public void setOsWritePermission(Boolean osWritePermission) {
        this.osWritePermission = osWritePermission;
    }

    public Boolean getOsExecutePermission() {
        return osExecutePermission;
    }

    public void setOsExecutePermission(Boolean osExecutePermission) {
        this.osExecutePermission = osExecutePermission;
    }
}
