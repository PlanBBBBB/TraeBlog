package com.planb.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planb.blog.common.Result;
import com.planb.blog.entity.Article;
import com.planb.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/articles")
@RequiredArgsConstructor
public class AdminArticleController {

    private final ArticleService articleService;

    @GetMapping
    public Result<Page<Article>> getArticles(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(articleService.searchArticles(title, category, tag, startDate, endDate, page, size));
    }

    @PostMapping
    public Result<Article> createArticle(@RequestBody Article article) {
        return Result.success(articleService.createArticle(article));
    }

    @PutMapping("/{id}")
    public Result<Article> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        Article updated = articleService.updateArticle(id, article);
        if (updated != null) {
            return Result.success(updated);
        }
        return Result.error("文章不存在");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        if (articleService.deleteArticle(id)) {
            return Result.success();
        }
        return Result.error("文章不存在");
    }
}
