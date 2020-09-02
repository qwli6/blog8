package me.lqw.blog8.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章分类
 *
 * @author liqiwen
 * @version 1.4
 * @since 1.4
 */
public class Category implements Serializable {

    /**
     * 分类 id
     */
    private Integer id;

    /**
     * 分类名称
     */
    @NotBlank(message = "请提供一个分类名称")
    @Length(max = 16, message = "分类名称的长度不能超过 {max} 个字符")
    private String name;

    /**
     * 分类的创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "分类的创建时间必须是一个未来的时间")
    private LocalDateTime createAt;

    /**
     * 分类的修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "分类的修改时间必须是一个未来的时间")
    private LocalDateTime modifyAt;

    /**
     * 分类排序字段
     */
    @NotNull(message = "请提供一个分类的排序字段")
    @Min(0)
    @Max(100)
    private Integer sort = 0;

    /**
     * 分类在前端页面是否显示
     */
    private Boolean show = true;

    /**
     * 构造方法
     */
    public Category() {
        super();
    }

    /**
     * 构造方法
     *
     * @param id id
     */
    public Category(Integer id) {
        super();
        this.id = id;
    }


    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
