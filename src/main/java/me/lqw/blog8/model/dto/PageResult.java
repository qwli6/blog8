package me.lqw.blog8.model.dto;

import me.lqw.blog8.model.vo.QueryParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询对象
 * @param <T> T
 */
public class PageResult<T> implements Serializable {

    /**
     * 查询参数
     */
    private QueryParam queryParam;

    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 数据
     */
    private List<T> data;

    /**
     * 总行数
     */
    private Integer totalRow;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 是否有下一页
     */
    private boolean hasNextPage;

    /**
     * 是否有上一页
     */
    private boolean hasPrePage;

    /**
     * 是否是第一页
     */
    private boolean isFirstPage;

    /**
     * 是否是最后一页
     */
    private boolean isLastPage;

    /**
     * 数据集合大小
     */
    private Integer dataSize;

    public void setQueryParam(QueryParam queryParam) {
        this.queryParam = queryParam;
    }

    public QueryParam getQueryParam() {
        return queryParam;
    }

    public PageResult() {
        super();
    }

    public PageResult(QueryParam queryParam, Integer totalRow, List<T> data) {
        super();
        this.queryParam = queryParam;
        this.currentPage = queryParam.getCurrentPage();
        this.pageSize = queryParam.getPageSize();
        this.data = data;
        this.totalRow = totalRow;
        this.totalPage = (this.totalRow%this.pageSize == 0) ? (totalRow/this.pageSize) : (totalRow/this.pageSize)+1;
        this.dataSize = this.data == null ? 0 : this.data.size();
        this.hasPrePage = getCurrentPage() > 1;
        this.hasNextPage = getCurrentPage() < getTotalPage();
        this.isLastPage = getCurrentPage().equals(getTotalPage());
        this.isFirstPage = getCurrentPage() <= 1;
    }

    public static <T> List<T> empty(){

        return new ArrayList<>();
    }


    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPrePage() {
        return hasPrePage;
    }

    public void setHasPrePage(boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.isLastPage = lastPage;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
