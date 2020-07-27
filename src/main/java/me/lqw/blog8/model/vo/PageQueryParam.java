package me.lqw.blog8.model.vo;

/**
 * 基础查询参数
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
public abstract class PageQueryParam {

    /**
     * 查询的当前页
     */
    private Integer currentPage = 1;

    /**
     * 查询的页大小
     */
    private Integer pageSize;

    /**
     * 查询的位移量
     */
    private Integer offset;

    /**
     * 是否忽略分页
     */
    private boolean ignorePaging;

    public boolean isIgnorePaging() {
        return ignorePaging;
    }

    public void setIgnorePaging(boolean ignorePaging) {
        this.ignorePaging = ignorePaging;
    }

    public Integer getOffset() {
        return (getCurrentPage()-1)*getPageSize();
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCurrentPage() {
        return currentPage == null || currentPage < 1 ? 1 : currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize == null || pageSize < 10 ? 10 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
