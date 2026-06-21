package com.planb.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planb.blog.common.Result;
import com.planb.blog.entity.Article;
import com.planb.blog.entity.Tag;
import com.planb.blog.service.ArticleService;
import com.planb.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final ArticleService articleService;

    @GetMapping
    public Result<List<Tag>> getAllTags() {
        return Result.success(tagService.getAllTags());
    }

    @GetMapping("/{name}/articles")
    public Result<Page<Article>> getArticlesByTag(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        Page<Article> result = articleService.getArticlesByTag(name, page, size);
        ArticleService.sanitizeForPublic(result);
        return Result.success(result);
    }
}
