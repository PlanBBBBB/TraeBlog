package com.planb.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.planb.blog.entity.Article;
import com.planb.blog.entity.Category;
import com.planb.blog.mapper.ArticleMapper;
import com.planb.blog.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    private final ArticleMapper articleMapper;

    public CategoryService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    public List<Category> getAllCategories() {
        return this.list();
    }

    public Page<Category> getCategories(int page, int size) {
        return this.page(new Page<>(page, size), 
            new LambdaQueryWrapper<Category>().orderByDesc(Category::getCreateTime));
    }

    public Page<Category> searchCategories(String name, LocalDate startDate, LocalDate endDate, 
                                          int page, int size) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            wrapper.like(Category::getName, name);
        }
        
        if (startDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            wrapper.ge(Category::getCreateTime, startDateTime);
        }
        
        if (endDate != null) {
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            wrapper.le(Category::getCreateTime, endDateTime);
        }
        
        wrapper.orderByDesc(Category::getCreateTime);
        
        return this.page(new Page<>(page, size), wrapper);
    }

    public Optional<Category> getCategoryById(Long id) {
        return Optional.ofNullable(this.getById(id));
    }

    public Optional<Category> getCategoryByName(String name) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, name);
        return Optional.ofNullable(this.getOne(wrapper));
    }

    public Category createCategory(Category category) {
        if (this.getCategoryByName(category.getName()).isPresent()) {
            return null;
        }
        this.save(category);
        return category;
    }

    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = this.getById(id);
        if (category != null) {
            category.setName(categoryDetails.getName());
            this.updateById(category);
            return category;
        }
        return null;
    }

    /**
     * 查找使用该分类的文章标题列表
     */
    public List<String> findAssociatedArticleTitles(String categoryName) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getCategory, categoryName)
                .select(Article::getTitle);
        return articleMapper.selectList(wrapper).stream()
                .map(Article::getTitle)
                .collect(Collectors.toList());
    }

    public boolean deleteCategory(Long id) {
        return this.removeById(id);
    }
}
