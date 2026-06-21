package com.planb.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.planb.blog.entity.Article;
import com.planb.blog.entity.Tag;
import com.planb.blog.mapper.ArticleMapper;
import com.planb.blog.mapper.TagMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService extends ServiceImpl<TagMapper, Tag> {

    private final ArticleMapper articleMapper;

    public TagService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    public List<Tag> getAllTags() {
        return this.list();
    }

    public Page<Tag> getTags(int page, int size) {
        return this.page(new Page<>(page, size), 
            new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getCreateTime));
    }

    public Page<Tag> searchTags(String name, LocalDate startDate, LocalDate endDate, 
                                int page, int size) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            wrapper.like(Tag::getName, name);
        }
        
        if (startDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            wrapper.ge(Tag::getCreateTime, startDateTime);
        }
        
        if (endDate != null) {
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            wrapper.le(Tag::getCreateTime, endDateTime);
        }
        
        wrapper.orderByDesc(Tag::getCreateTime);
        
        return this.page(new Page<>(page, size), wrapper);
    }

    public Optional<Tag> getTagById(Long id) {
        return Optional.ofNullable(this.getById(id));
    }

    public Optional<Tag> getTagByName(String name) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getName, name);
        return Optional.ofNullable(this.getOne(wrapper));
    }

    public Tag createTag(Tag tag) {
        if (this.getTagByName(tag.getName()).isPresent()) {
            return null;
        }
        this.save(tag);
        return tag;
    }

    public Tag updateTag(Long id, Tag tagDetails) {
        Tag tag = this.getById(id);
        if (tag != null) {
            tag.setName(tagDetails.getName());
            this.updateById(tag);
            return tag;
        }
        return null;
    }

    /**
     * 查找使用该标签的文章标题列表
     */
    public List<String> findAssociatedArticleTitles(String tagName) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w
                .eq(Article::getTags, tagName)
                .or().likeRight(Article::getTags, tagName + ",")
                .or().likeLeft(Article::getTags, "," + tagName)
                .or().like(Article::getTags, "," + tagName + ",")
        ).select(Article::getTitle);
        return articleMapper.selectList(wrapper).stream()
                .map(Article::getTitle)
                .collect(Collectors.toList());
    }

    public boolean deleteTag(Long id) {
        return this.removeById(id);
    }
}
