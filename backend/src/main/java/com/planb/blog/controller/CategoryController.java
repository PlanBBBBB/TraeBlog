package com.planb.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planb.blog.common.Result;
import com.planb.blog.entity.Article;
import com.planb.blog.entity.Category;
import com.planb.blog.service.ArticleService;
import com.planb.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ArticleService articleService;

    @GetMapping
    public Result<List<Category>> getAllCategories() {
        return Result.success(categoryService.getAllCategories());
    }

    @GetMapping("/{name}/articles")
    public Result<Page<Article>> getArticlesByCategory(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        Page<Article> result = articleService.getArticlesByCategory(name, page, size);
        ArticleService.sanitizeForPublic(result);
        return Result.success(result);
    }
}
