package me.lqw.blog8.file;

import me.lqw.blog8.model.vo.QueryParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件查询参数
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class FileQueryParam extends QueryParam implements Serializable {

    private String fileName;

    private String targetPath;

    private boolean sortByLastModify;

    private boolean sortBySize;

    /**
     * 是否查看隐藏文件
     */
    private boolean hidden;

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isSortBySize() {
        return sortBySize;
    }

    public void setSortBySize(boolean sortBySize) {
        this.sortBySize = sortBySize;
    }

    private List<String> extensions;

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    public boolean isSortByLastModify() {
        return sortByLastModify;
    }

    public void setSortByLastModify(boolean sortByLastModify) {
        this.sortByLastModify = sortByLastModify;
    }

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

    /**
     * walk file is contain child dir
     */
    private Boolean containChildDir = false;

    /**
     * walk file is contain parent dir
     */
    private Boolean containParentDir = false;

    public Boolean getContainParentDir() {
        return containParentDir;
    }

    public void setContainParentDir(Boolean containParentDir) {
        this.containParentDir = containParentDir;
    }

    public Boolean getContainChildDir() {
        return containChildDir;
    }

    public void setContainChildDir(Boolean containChildDir) {
        this.containChildDir = containChildDir;
    }
}
