package com.planb.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planb.blog.common.Result;
import com.planb.blog.entity.Tag;
import com.planb.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/tags")
@RequiredArgsConstructor
public class AdminTagController {

    private final TagService tagService;

    @GetMapping
    public Result<Page<Tag>> getTags(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(tagService.searchTags(name, startDate, endDate, page, size));
    }

    @PostMapping
    public Result<Tag> createTag(@RequestBody Tag tag) {
        Tag created = tagService.createTag(tag);
        if (created != null) {
            return Result.success(created);
        }
        return Result.error("标签已存在");
    }

    @PutMapping("/{id}")
    public Result<Tag> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        Tag updated = tagService.updateTag(id, tag);
        if (updated != null) {
            return Result.success(updated);
        }
        return Result.error("标签不存在");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTag(@PathVariable Long id) {
        if (tagService.deleteTag(id)) {
            return Result.success();
        }
        return Result.error("标签不存在");
    }
}
