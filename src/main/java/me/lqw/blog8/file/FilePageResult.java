package me.lqw.blog8.file;

import me.lqw.blog8.model.dto.page.PageResult;
import me.lqw.blog8.model.vo.AbstractQueryParam;

import java.io.Serializable;
import java.util.List;

/**
 * 文件分页查询结果
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public class FilePageResult extends PageResult<FileInfo> implements Serializable {


    private List<String> path;

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    /**
     * 文件分页对象构造方法
     *
     * @param abstractQueryParam pageQueryParam
     * @param totalRow           totalRow
     * @param data               data
     */
    public FilePageResult(AbstractQueryParam abstractQueryParam, Integer totalRow, List<FileInfo> data) {
        super(abstractQueryParam, totalRow, data);
    }
}
