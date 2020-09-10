package me.lqw.blog8.file.meta;

import java.io.Serializable;

/**
 * 获取音频文件的元信息
 * @author liqiwen
 * @since 2.2
 * @version 2.2
 */
public class AudioMetaInfo implements Serializable {

    /**
     * 音频大小
     */
    private long audioSize;

    /**
     * 音频码率
     */
    private String audioBitRate;

    /**
     * 音频格式
     */
    private String format;

    /**
     * 音频持续时长
     */
    private long duration;


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getAudioSize() {
        return audioSize;
    }

    public void setAudioSize(long audioSize) {
        this.audioSize = audioSize;
    }

    public String getAudioBitRate() {
        return audioBitRate;
    }

    public void setAudioBitRate(String audioBitRate) {
        this.audioBitRate = audioBitRate;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
