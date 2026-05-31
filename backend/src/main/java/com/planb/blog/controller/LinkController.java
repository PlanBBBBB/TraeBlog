package com.planb.blog.controller;

import com.planb.blog.common.Result;
import com.planb.blog.entity.Link;
import com.planb.blog.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @GetMapping
    public Result<List<Link>> getAllLinks() {
        return Result.success(linkService.getAllLinks());
    }
}
