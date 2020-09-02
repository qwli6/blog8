package me.lqw.blog8.file;

import java.io.Serializable;
import java.util.Map;

/**
 * 文件统计
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class FileStatistic implements Serializable {

    /**
     * 大小
     */
    private long size;

    private long yy;

    private long zz;

    private Map<String, Object> map;

    public FileStatistic(long size, long yy, long zz, Map<String, Object> map) {
        this.size = size;
        this.yy = yy;
        this.zz = zz;
        this.map = map;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getYy() {
        return yy;
    }

    public void setYy(long yy) {
        this.yy = yy;
    }

    public long getZz() {
        return zz;
    }

    public void setZz(long zz) {
        this.zz = zz;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
