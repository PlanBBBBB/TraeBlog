package com.planb.blog.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.planb.blog.entity.*;
import com.planb.blog.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminMapper adminMapper;
    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final LinkMapper linkMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (adminMapper.selectCount(null) == 0) {
            Admin admin = new Admin();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            adminMapper.insert(admin);
            System.out.println("管理员账号已创建");
        } else {
            // 迁移旧管理员账号为新的用户名和密码
            migrateAdminCredentials();
        }

        if (categoryMapper.selectCount(null) == 0) {
            List<String> categories = Arrays.asList("AI/ML", "数据库", "后端开发", "前端", "工具");
            categories.forEach(name -> {
                Category category = new Category();
                category.setName(name);
                categoryMapper.insert(category);
            });
        }

        if (tagMapper.selectCount(null) == 0) {
            List<String> tags = Arrays.asList("AIGC", "推荐系统", "Redis", "Java", "JVM", "MySQL", "Vue", "Git", "Docker");
            tags.forEach(name -> {
                Tag tag = new Tag();
                tag.setName(name);
                tagMapper.insert(tag);
            });
        }

        if (linkMapper.selectCount(null) == 0) {
            List<Link> links = Arrays.asList(
                    createLink("阮一峰的网络日志", "https://www.ruanyifeng.com/", "技术博客"),
                    createLink("GitHub", "https://github.com/", "全球代码托管平台"),
                    createLink("掘金", "https://juejin.cn/", "开发者社区"),
                    createLink("Stack Overflow", "https://stackoverflow.com/", "程序员问答社区")
            );
            links.forEach(linkMapper::insert);
        }

        if (articleMapper.selectCount(null) == 0) {
            createSampleArticles();
        }

        // 迁移已有文章的明文密码为 BCrypt 加密格式
        migrateArticlePasswords();
    }

    private Link createLink(String name, String url, String desc) {
        Link link = new Link();
        link.setName(name);
        link.setUrl(url);
        link.setDescription(desc);
        return link;
    }

    private void createSampleArticles() {
        List<Article> articles = Arrays.asList(
                createArticle("AIGC 广告推荐系统", "本项目是一个基于 AIGC 的个性化广告推荐系统...", "AI/ML", "AIGC, 推荐系统"),
                createArticle("Redis 相关知识 - 底层数据结构", "深入理解 Redis 的底层数据结构...", "数据库", "Redis, 数据库"),
                createArticle("Redis 持久化机制详解", "详细介绍 Redis 的 RDB 和 AOF 两种持久化机制...", "数据库", "Redis, 数据库"),
                createArticle("JVM 内存结构深度解析", "深入了解 JVM 的内存结构...", "后端开发", "Java, JVM"),
                createArticle("JVM 垃圾回收机制", "学习 JVM 的垃圾回收机制...", "后端开发", "Java, JVM"),
                createArticle("MySQL 索引优化实战", "MySQL 索引的最佳实践...", "数据库", "MySQL, 数据库"),
                createArticle("Vue 3 组合式 API 详解", "全面学习 Vue 3 的组合式 API...", "前端", "Vue, 前端"),
                createArticle("Docker 容器化部署", "Docker 的基础使用...", "工具", "Docker, 工具"),
                createArticle("Git 版本控制详解", "Git 的常用命令和工作流程...", "工具", "Git, 工具"),
                createArticle("Linux 常用命令大全", "Linux 系统常用命令汇总...", "工具", "Linux, 工具"),
                createArticle("Spring Boot 快速入门", "从零开始学习 Spring Boot...", "后端开发", "Spring Boot, Java"),
                createArticle("React 组件开发", "React 组件化开发的最佳实践...", "前端", "React, 前端")
        );
        articles.forEach(articleMapper::insert);
    }

    private Article createArticle(String title, String summary, String category, String tags) {
        Article article = new Article();
        article.setTitle(title);
        article.setSummary(summary);
        article.setContent(summary + "\n\n文章详细内容...");
        article.setCategory(category);
        article.setTags(tags);
        return article;
    }

    /**
     * 迁移已有文章的明文密码为 BCrypt 加密格式
     * BCrypt 哈希以 $2a$ 开头，如果密码不符合此格式则视为明文
     */
    private void migrateArticlePasswords() {
        List<Article> articles = articleMapper.selectList(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getIsLocked, true)
                        .isNotNull(Article::getPassword)
        );
        int migrated = 0;
        for (Article article : articles) {
            String pwd = article.getPassword();
            if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$") && !pwd.startsWith("$2y$")) {
                article.setPassword(passwordEncoder.encode(pwd));
                articleMapper.updateById(article);
                migrated++;
            }
        }
        if (migrated > 0) {
            System.out.println("已迁移 " + migrated + " 篇文章的密码为 BCrypt 加密格式");
        }
    }

    /**
     * 迁移管理员账号为新的用户名和密码
     * 仅在旧用户名为 "admin" 时触发
     */
    private void migrateAdminCredentials() {
        Admin admin = adminMapper.selectOne(
                new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, "admin"));
        if (admin != null) {
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            adminMapper.updateById(admin);
            System.out.println("管理员账号已迁移");
        }
    }
}
