package me.lqw.blog8.file;

import java.io.Serializable;

public class FileUpdated implements Serializable {

    /**
     * 更新内容
     */
    private String content;

    /**
     * 文件路径
     */
    private String path;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
