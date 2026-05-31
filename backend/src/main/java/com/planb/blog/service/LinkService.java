package com.planb.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.planb.blog.entity.Link;
import com.planb.blog.mapper.LinkMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class LinkService extends ServiceImpl<LinkMapper, Link> {

    public List<Link> getAllLinks() {
        // 前台按照 sort 降序排列，大的排前面
        return this.list(new LambdaQueryWrapper<Link>()
            .orderByDesc(Link::getSort)
            .orderByDesc(Link::getCreateTime));
    }

    public Page<Link> getLinks(int page, int size) {
        return this.page(new Page<>(page, size), 
            new LambdaQueryWrapper<Link>().orderByDesc(Link::getCreateTime));
    }

    public Page<Link> searchLinks(String keyword, LocalDate startDate, LocalDate endDate, 
                                  int page, int size) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like(Link::getName, keyword)
                .or()
                .like(Link::getUrl, keyword)
                .or()
                .like(Link::getDescription, keyword)
            );
        }
        
        if (startDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            wrapper.ge(Link::getCreateTime, startDateTime);
        }
        
        if (endDate != null) {
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            wrapper.le(Link::getCreateTime, endDateTime);
        }
        
        wrapper.orderByDesc(Link::getCreateTime);
        
        return this.page(new Page<>(page, size), wrapper);
    }

    public Optional<Link> getLinkById(Long id) {
        return Optional.ofNullable(this.getById(id));
    }

    public Link createLink(Link link) {
        // 如果 sort 为空，设置默认值 0
        if (link.getSort() == null) {
            link.setSort(0);
        }
        this.save(link);
        return link;
    }

    public Link updateLink(Long id, Link linkDetails) {
        Link link = this.getById(id);
        if (link != null) {
            link.setName(linkDetails.getName());
            link.setUrl(linkDetails.getUrl());
            link.setDescription(linkDetails.getDescription());
            if (linkDetails.getSort() != null) {
                link.setSort(linkDetails.getSort());
            }
            this.updateById(link);
            return link;
        }
        return null;
    }

    public boolean deleteLink(Long id) {
        return this.removeById(id);
    }
}
