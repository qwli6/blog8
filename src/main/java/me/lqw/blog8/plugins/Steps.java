package me.lqw.blog8.plugins;

import me.lqw.blog8.model.dto.PageResult;

import java.util.List;

public class Steps {

    /**
     * 计算分页步长
     * @param pageResult pageResult
     * @param step step
     * @return list
     * 当前页 为 1/2/3/4/5 ，总数不超过 5 ，返回 1 2 3 4 5
     * 当前页 为 ? 总数超过 5，返回 2 3 4 5 6/3 4 5 6 7
     */
    public List<Integer> step(PageResult<?> pageResult, Integer step){
        Integer currentPage = pageResult.getCurrentPage();
        Integer totalPage = pageResult.getTotalPage();



        return null;
    }
}
