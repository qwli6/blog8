package me.lqw.blog8.file;

import me.lqw.blog8.model.vo.AbstractQueryParam;

import java.io.Serializable;
import java.util.List;

/**
 * 文件查询参数
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class FilePageQueryParam extends AbstractQueryParam implements Serializable {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 目标路径
     */
    private String targetPath;

    /**
     * 是否按照最近修改时间排序
     */
    private Boolean sortByLastModify;

    /**
     * 是否根据文件大小排序
     */
    private Boolean sortBySize;

    /**
     * 是否查看隐藏文件
     */
    private Boolean hidden;

    /**
     * 查找时是否包含子目录
     */
    private Boolean containChildDir = false;

    /**
     * 查找时是否包含父目录
     */
    private Boolean containParentDir = false;

    /**
     * 文件扩展名列表
     */
    private List<String> extensions;

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean isSortBySize() {
        return sortBySize;
    }

    public void setSortBySize(Boolean sortBySize) {
        this.sortBySize = sortBySize;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    public Boolean isSortByLastModify() {
        return sortByLastModify;
    }

    public void setSortByLastModify(Boolean sortByLastModify) {
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
