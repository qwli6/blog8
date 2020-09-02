package me.lqw.blog8.service;

import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.mapper.CategoryMapper;
import me.lqw.blog8.model.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

/**
 * 分类业务逻辑实现类
 *
 * @author liqiwen
 * @version 1.0
 */
@Service
public class CategoryService extends AbstractBaseService<Category> {

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Category save(Category category) throws LogicException {
        categoryMapper.selectByName(category.getName()).ifPresent(e -> {
            throw new LogicException("categoryService.save.nameExists", "分类名称已存在");
        });
        categoryMapper.insert(category);
        return category;
    }


    @Transactional(readOnly = true)
    public List<Category> selectAll() {
        return categoryMapper.selectAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Integer id) throws LogicException {
        categoryMapper.selectById(id).orElseThrow(() -> new LogicException("categoryService.delete.notExists", "分类不存在"));

        List<Category> all = categoryMapper.selectAll();
        if (all.size() == 1) {
            throw new LogicException("categoryService.delete.oneLeft", "仅剩一个分类，不允许删除");
        }
        categoryMapper.delete(id);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                //重构文章索引
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(Category category) throws LogicException {
        Category old = categoryMapper.selectById(category.getId()).orElseThrow(() ->
                new LogicException("categoryService.update.notExists", "分类不存在"));
        if (old.getName().equals(category.getName()) && old.getShow().equals(category.getShow())) {
            return;
        }
        categoryMapper.selectByName(category.getName()).ifPresent(e -> {
            throw new LogicException("categoryService.update.nameExists", "名称已存在, 无法修改");
        });

        categoryMapper.update(category);
    }

}
