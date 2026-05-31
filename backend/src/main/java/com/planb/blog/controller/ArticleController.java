package com.planb.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planb.blog.common.Result;
import com.planb.blog.entity.Article;
import com.planb.blog.service.ArticleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public Result<Page<Article>> getArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return Result.success(articleService.getArticles(page, size));
    }

    @GetMapping("/{id}")
    public Result<Article> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id)
                .map(Result::success)
                .orElse(Result.error("文章不存在"));
    }

    @PostMapping("/{id}/verify-password")
    public Result<Boolean> verifyPassword(@PathVariable Long id, @RequestBody PasswordRequest request) {
        boolean valid = articleService.verifyPassword(id, request.getPassword());
        if (valid) {
            return Result.success(true);
        } else {
            return Result.error("密码错误");
        }
    }

    @Data
    public static class PasswordRequest {
        private String password;
    }
}
