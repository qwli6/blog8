package me.lqw.blog8.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileQueryParam implements Serializable {

    private String fileName;


    private String targetPath;

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    /**
     * suffix
     */
    private List<String> fileSuffixes = new ArrayList<>();


    public List<String> getFileSuffixes() {
        return fileSuffixes;
    }

    public void setFileSuffixes(List<String> fileSuffixes) {
        this.fileSuffixes = fileSuffixes;
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
