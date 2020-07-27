package me.lqw.blog8.file;

import java.io.Serializable;

/**
 * Lookup
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 * 2020-07-27 08:14:07
 */
public class Lookup implements Serializable {

    /**
     * 给定路径
     */
    public final String path;

    /**
     * 必须为文件夹
     */
    public Boolean mustExists;

    /**
     * 是否必须为规则的文件
     */
    public Boolean mustRegularFile;

    /**
     * 是否必须为目录
     */
    public Boolean mustDir;

    /**
     * 是否忽略根目录
     */
    public Boolean ignoreRoot;

    /**
     * 给定路径，返回一个 Lookup 对象
     * @param path path
     */
    public Lookup(String path) {
        this.path = path;
    }

    public static Lookup newLookup(String path){
        return new Lookup(path);
    }


    public Lookup setMustExists(boolean mustExists){
        this.mustExists = mustExists;
        return this;
    }

    public Lookup setMustDir(boolean mustDir){
        this.mustDir = mustDir;
        return this;
    }

    public Lookup setMustRegularFile(boolean mustRegularFile){
        this.mustRegularFile = mustRegularFile;
        return this;
    }

    public Lookup setIgnoreRoot(boolean ignoreRoot){
        this.ignoreRoot = ignoreRoot;
        return this;
    }
}
