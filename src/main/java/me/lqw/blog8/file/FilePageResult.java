package me.lqw.blog8.file;

import me.lqw.blog8.model.dto.PageResult;
import me.lqw.blog8.model.vo.PageQueryParam;

import java.io.Serializable;
import java.util.List;

/**
 * 文件分页查询结果
 * @author liqiwen
 * @version 1.2
 */
public class FilePageResult extends PageResult<FileInfo> implements Serializable {

    public FilePageResult(PageQueryParam pageQueryParam, Integer totalRow, List<FileInfo> data) {
        super(pageQueryParam, totalRow, data);
    }
}
