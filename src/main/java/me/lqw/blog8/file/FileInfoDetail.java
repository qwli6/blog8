package me.lqw.blog8.file;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件详细信息
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class FileInfoDetail extends FileInfo implements Serializable {

    /**
     * 文件内容
     *
     * @since 1.2
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createAt;


    /**
     * 文件属性
     */
    private FileAttributes fileAttributes;


    /**
     * 构造方法
     *
     * @param fileInfo fileInfo
     */
    public FileInfoDetail(FileInfo fileInfo) {
        super(fileInfo);
    }

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

    public FileAttributes getFileAttributes() {
        return fileAttributes;
    }

    public void setFileAttributes(FileAttributes fileAttributes) {
        this.fileAttributes = fileAttributes;
    }
}
