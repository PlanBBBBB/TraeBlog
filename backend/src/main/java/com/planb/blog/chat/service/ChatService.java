package com.planb.blog.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planb.blog.chat.dto.ChatRequest;
import com.planb.blog.chat.dto.ChatResponse;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final dev.langchain4j.model.chat.ChatModel chatModel;
    private final StreamingChatModel streamingChatModel;
    private final ContentRetriever contentRetriever;
    private final ObjectMapper objectMapper;

    @Value("${chat.bot.system-prompt:你是PlanB's Blog的AI助手。}")
    private String systemPrompt;

    /**
     * 同步聊天
     */
    public ChatResponse chat(ChatRequest request) {
        String userMessage = request.getMessage();

        // RAG 检索相关内容
        List<Content> contents = retrieveContent(userMessage);
        String context = buildContext(contents);

        // 构建消息列表
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(SystemMessage.from(systemPrompt + "\n\n以下是博客中的相关内容供参考：\n" + context));
        messages.add(UserMessage.from(userMessage));

        // 调用 LLM
        dev.langchain4j.model.chat.response.ChatResponse response = chatModel.chat(messages);
        String reply = response.aiMessage().text();

        // 提取引用来源
        List<ChatResponse.Source> sources = extractSources(contents);

        return new ChatResponse(reply, sources);
    }

    /**
     * 流式聊天 (SSE)
     */
    public SseEmitter chatStream(ChatRequest request) {
        SseEmitter emitter = new SseEmitter(120_000L); // 2分钟超时

        String userMessage = request.getMessage();

        // RAG 检索
        List<Content> contents = retrieveContent(userMessage);
        String context = buildContext(contents);
        List<ChatResponse.Source> sources = extractSources(contents);

        // 构建消息
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(SystemMessage.from(systemPrompt + "\n\n以下是博客中的相关内容供参考：\n" + context));
        messages.add(UserMessage.from(userMessage));

        // 先发送来源信息
        try {
            String sourcesJson = objectMapper.writeValueAsString(
                    Map.of("type", "sources", "data", sources));
            emitter.send(SseEmitter.event().name("sources").data(sourcesJson));
        } catch (IOException e) {
            log.warn("发送来源信息失败", e);
        }

        // 流式调用 LLM
        streamingChatModel.chat(messages, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                try {
                    emitter.send(SseEmitter.event().data(partialResponse));
                } catch (IOException e) {
                    log.warn("SSE 发送失败", e);
                    emitter.completeWithError(e);
                }
            }

            @Override
            public void onCompleteResponse(dev.langchain4j.model.chat.response.ChatResponse response) {
                try {
                    emitter.send(SseEmitter.event().name("done").data("[DONE]"));
                    emitter.complete();
                } catch (IOException e) {
                    log.warn("SSE 完成发送失败", e);
                    emitter.completeWithError(e);
                }
            }

            @Override
            public void onError(Throwable error) {
                log.error("LLM 流式调用失败", error);
                try {
                    emitter.send(SseEmitter.event().name("error")
                            .data("抱歉，AI 服务暂时不可用，请稍后重试。"));
                } catch (IOException ignored) {}
                emitter.completeWithError(error);
            }
        });

        return emitter;
    }

    /**
     * RAG 检索博客内容
     */
    private List<Content> retrieveContent(String query) {
        try {
            return contentRetriever.retrieve(dev.langchain4j.rag.query.Query.from(query));
        } catch (Exception e) {
            log.warn("RAG 检索失败，将不使用上下文", e);
            return Collections.emptyList();
        }
    }

    /**
     * 构建上下文文本
     */
    private String buildContext(List<Content> contents) {
        if (contents.isEmpty()) {
            return "（未找到相关的博客文章内容）";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contents.size(); i++) {
            sb.append("--- 片段 ").append(i + 1).append(" ---\n");
            sb.append(contents.get(i).textSegment().text()).append("\n\n");
        }
        return sb.toString();
    }

    /**
     * 从检索结果中提取文章来源
     */
    private List<ChatResponse.Source> extractSources(List<Content> contents) {
        Set<Long> seenIds = new HashSet<>();
        List<ChatResponse.Source> sources = new ArrayList<>();

        for (Content content : contents) {
            var metadata = content.textSegment().metadata();
            String articleIdStr = metadata.getString("articleId");
            if (articleIdStr != null) {
                try {
                    Long articleId = Long.parseLong(articleIdStr);
                    if (seenIds.add(articleId)) {
                        sources.add(new ChatResponse.Source(
                                articleId,
                                metadata.getString("title"),
                                metadata.getString("category")
                        ));
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        return sources;
    }
}
