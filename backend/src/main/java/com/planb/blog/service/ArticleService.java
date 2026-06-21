package com.planb.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.planb.blog.entity.Article;
import com.planb.blog.mapper.ArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {

    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    public ArticleService(ApplicationEventPublisher eventPublisher, PasswordEncoder passwordEncoder) {
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<Article> getArticles(int page, int size) {
        return this.page(new Page<>(page, size), 
                new LambdaQueryWrapper<Article>().orderByDesc(Article::getCreateTime));
    }

    public Page<Article> searchArticles(String title, String category, String tag, 
                                        LocalDate startDate, LocalDate endDate, 
                                        int page, int size) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(title)) {
            wrapper.like(Article::getTitle, title);
        }
        
        if (StringUtils.hasText(category)) {
            wrapper.eq(Article::getCategory, category);
        }
        
        if (StringUtils.hasText(tag)) {
            wrapper.like(Article::getTags, tag);
        }
        
        if (startDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            wrapper.ge(Article::getCreateTime, startDateTime);
        }
        
        if (endDate != null) {
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            wrapper.le(Article::getCreateTime, endDateTime);
        }
        
        wrapper.orderByDesc(Article::getCreateTime);
        
        return this.page(new Page<>(page, size), wrapper);
    }

    public Optional<Article> getArticleById(Long id) {
        return Optional.ofNullable(this.getById(id));
    }

    public boolean verifyPassword(Long id, String password) {
        Article article = this.getById(id);
        if (article != null && article.getIsLocked() != null && article.getIsLocked()) {
            return article.getPassword() != null && passwordEncoder.matches(password, article.getPassword());
        }
        return true; // 如果没有锁，直接通过
    }

    public Page<Article> getArticlesByCategory(String category, int page, int size) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Article::getCategory, category);
        wrapper.orderByDesc(Article::getCreateTime);
        return this.page(new Page<>(page, size), wrapper);
    }

    public Page<Article> getArticlesByTag(String tag, int page, int size) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Article::getTags, tag);
        wrapper.orderByDesc(Article::getCreateTime);
        return this.page(new Page<>(page, size), wrapper);
    }

    public Article createArticle(Article article) {
        if (article.getIsLocked() == null) {
            article.setIsLocked(false);
        }
        // 密码加密存储
        if (article.getIsLocked() && StringUtils.hasText(article.getPassword())) {
            article.setPassword(passwordEncoder.encode(article.getPassword()));
        }
        this.save(article);
        publishArticleChangedEvent();
        return article;
    }

    public Article updateArticle(Long id, Article articleDetails) {
        Article article = this.getById(id);
        if (article != null) {
            article.setTitle(articleDetails.getTitle());
            article.setContent(articleDetails.getContent());
            article.setSummary(articleDetails.getSummary());
            article.setCategory(articleDetails.getCategory());
            article.setTags(articleDetails.getTags());
            article.setIsLocked(articleDetails.getIsLocked());
            // 密码加密存储：仅在密码有值时重新加密
            if (articleDetails.getIsLocked() != null && articleDetails.getIsLocked()
                    && StringUtils.hasText(articleDetails.getPassword())) {
                // 如果密码不是已加密的格式，则加密
                if (!articleDetails.getPassword().startsWith("$2a$")) {
                    article.setPassword(passwordEncoder.encode(articleDetails.getPassword()));
                } else {
                    article.setPassword(articleDetails.getPassword());
                }
            } else {
                article.setPassword(articleDetails.getPassword());
            }
            this.updateById(article);
            publishArticleChangedEvent();
            return article;
        }
        return null;
    }

    public boolean deleteArticle(Long id) {
        boolean result = this.removeById(id);
        if (result) {
            publishArticleChangedEvent();
        }
        return result;
    }

    private void publishArticleChangedEvent() {
        eventPublisher.publishEvent(new ArticleChangedEvent(this));
    }

    /**
     * 文章变更事件
     */
    public static class ArticleChangedEvent extends org.springframework.context.ApplicationEvent {
        public ArticleChangedEvent(Object source) {
            super(source);
        }
    }
}
