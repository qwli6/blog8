package me.lqw.blog8.file;

import java.io.Serializable;

/**
 * 上传的视频信息
 * @author liqiwen
 * @version 1.0
 */
public class VideoInfo implements Serializable {

    /**
     * 视频高度
     */
    private int width;

    /**
     * 视频宽度
     */
    private int height;

    /**
     * 视频播放时长
     */
    private long duration;

    public VideoInfo(int width, int height, long duration) {
        this.width = width;
        this.height = height;
        this.duration = duration;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
