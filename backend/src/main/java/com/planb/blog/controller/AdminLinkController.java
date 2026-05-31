package com.planb.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.planb.blog.common.Result;
import com.planb.blog.entity.Link;
import com.planb.blog.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/links")
@RequiredArgsConstructor
public class AdminLinkController {

    private final LinkService linkService;

    @GetMapping
    public Result<Page<Link>> getLinks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(linkService.searchLinks(keyword, startDate, endDate, page, size));
    }

    @PostMapping
    public Result<Link> createLink(@RequestBody Link link) {
        return Result.success(linkService.createLink(link));
    }

    @PutMapping("/{id}")
    public Result<Link> updateLink(@PathVariable Long id, @RequestBody Link link) {
        Link updated = linkService.updateLink(id, link);
        if (updated != null) {
            return Result.success(updated);
        }
        return Result.error("链接不存在");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteLink(@PathVariable Long id) {
        if (linkService.deleteLink(id)) {
            return Result.success();
        }
        return Result.error("链接不存在");
    }
}
