package com.planb.blog.chat.service;

import com.planb.blog.entity.Article;
import com.planb.blog.service.ArticleService;
import com.planb.blog.service.ArticleService.ArticleChangedEvent;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleIndexService {

    private final ArticleService articleService;
    private final EmbeddingModel embeddingModel;
    private final InMemoryEmbeddingStore<TextSegment> embeddingStore;

    /**
     * 应用启动后自动索引所有文章
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            indexAllArticles();
        } catch (Exception e) {
            log.error("文章索引初始化失败", e);
        }
    }

    /**
     * 监听文章变更事件，延迟重建索引
     */
    @EventListener(ArticleChangedEvent.class)
    public void onArticleChanged() {
        log.info("检测到文章变更，将重建索引...");
        try {
            rebuildIndex();
        } catch (Exception e) {
            log.error("文章变更触发索引重建失败", e);
        }
    }

    /**
     * 全量重建索引 (文章变更时调用)
     */
    public void rebuildIndex() {
        log.info("开始重建文章索引...");
        embeddingStore.removeAll();
        indexAllArticles();
    }

    /**
     * 索引所有文章
     */
    private void indexAllArticles() {
        List<Article> articles = articleService.list();
        if (articles.isEmpty()) {
            log.info("没有文章需要索引");
            return;
        }

        DocumentByParagraphSplitter splitter = new DocumentByParagraphSplitter(500, 50);
        List<TextSegment> allSegments = new ArrayList<>();

        for (Article article : articles) {
            // 跳过加密文章
            if (Boolean.TRUE.equals(article.getIsLocked())) {
                continue;
            }

            String docText = buildDocumentText(article);
            Metadata metadata = Metadata.from(Map.of(
                    "articleId", String.valueOf(article.getId()),
                    "title", article.getTitle() != null ? article.getTitle() : "",
                    "category", article.getCategory() != null ? article.getCategory() : ""
            ));

            Document document = Document.from(docText, metadata);
            allSegments.addAll(splitter.split(document));
        }

        if (!allSegments.isEmpty()) {
            List<Embedding> embeddings = embeddingModel.embedAll(allSegments).content();
            embeddingStore.addAll(embeddings, allSegments);
        }

        log.info("文章索引完成: {} 篇文章, {} 个文本片段", articles.size(), allSegments.size());
    }

    /**
     * 构建文档文本
     */
    private String buildDocumentText(Article article) {
        StringBuilder sb = new StringBuilder();
        sb.append("标题: ").append(article.getTitle()).append("\n");
        sb.append("分类: ").append(article.getCategory() != null ? article.getCategory() : "未分类").append("\n");
        sb.append("标签: ").append(article.getTags() != null ? article.getTags() : "无").append("\n\n");

        if (article.getSummary() != null && !article.getSummary().isEmpty()) {
            sb.append("摘要: ").append(article.getSummary()).append("\n\n");
        }

        if (article.getContent() != null && !article.getContent().isEmpty()) {
            sb.append("内容:\n").append(article.getContent());
        }

        return sb.toString();
    }
}
