package me.lqw.blog8.file.meta;

import java.io.Serializable;

/**
 * 图片元信息
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public class ImageMetaInfo implements Serializable {

    /**
     * 图片宽度
     */
    private int width;

    /**
     * 图片高度
     */
    private int height;

    /**
     * 图片大小
     */
    private long size;

    /**
     * 构造方法
     */
    public ImageMetaInfo() {
        super();
    }

    /**
     * 构造方法
     * @param width width
     * @param height height
     * @param size size
     */
    public ImageMetaInfo(int width, int height, long size) {
        super();
        this.width = width;
        this.height = height;
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
