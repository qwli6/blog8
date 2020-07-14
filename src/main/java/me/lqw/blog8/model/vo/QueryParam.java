package me.lqw.blog8.model.vo;

public abstract class QueryParam {

    protected Integer currentPage = 1;

    protected Integer pageSize;

    protected Integer offset;

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
