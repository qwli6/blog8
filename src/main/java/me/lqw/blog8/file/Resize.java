package me.lqw.blog8.file;

import java.io.Serializable;
import java.util.Objects;

/**
 * 缩放比例
 * @author liqiwen
 * @version 1.0
 */
public class Resize implements Serializable {


    private Integer size;

    private Integer height;

    private Integer width;

    private Boolean keepRatio;

    private Integer quality;


    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Boolean getKeepRatio() {
        return keepRatio;
    }

    public void setKeepRatio(Boolean keepRatio) {
        this.keepRatio = keepRatio;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }


    @Override
    public String toString() {
        if(size != null){
            return String.valueOf(size);
        }
        return width + "_" + "x" + Objects.toString(height, "_") + (keepRatio ? "!": "");
    }
}
