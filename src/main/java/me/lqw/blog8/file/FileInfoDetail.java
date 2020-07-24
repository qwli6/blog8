package me.lqw.blog8.file;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件详细信息
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class FileInfoDetail extends FileInfo implements Serializable {

    private LocalDateTime createAt;

    private LocalDateTime lastAccessAt;

    private LocalDateTime lastModifiedAt;

    /**
     * 文件内容
     * @since 1.2
     */
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getLastAccessAt() {
        return lastAccessAt;
    }

    public void setLastAccessAt(LocalDateTime lastAccessAt) {
        this.lastAccessAt = lastAccessAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public FileInfoDetail(FileInfo fileInfo) {
        super(fileInfo);

    }
}
