package com.planb.blog.chat.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Value("${chat.bot.max-retrieved-docs:3}")
    private int maxRetrievedDocs;

    @Value("${chat.bot.min-relevance-score:0.5}")
    private double minRelevanceScore;

    /**
     * 本地 Embedding 模型 (ONNX, 384维, 约80MB)
     */
    @Bean
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    /**
     * 内存向量存储 (可重建)
     */
    @Bean
    public InMemoryEmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    /**
     * RAG 内容检索器
     */
    @Bean
    public ContentRetriever contentRetriever(InMemoryEmbeddingStore<TextSegment> embeddingStore,
                                              EmbeddingModel embeddingModel) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(maxRetrievedDocs)
                .minScore(minRelevanceScore)
                .build();
    }

    /**
     * 聊天记忆 (保留最近20轮对话)
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(20);
    }
}
