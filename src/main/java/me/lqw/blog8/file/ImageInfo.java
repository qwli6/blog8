package me.lqw.blog8.file;

import java.io.Serializable;

/**
 * 图片信息
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public class ImageInfo implements Serializable {

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
