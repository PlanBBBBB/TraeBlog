package com.planb.blog.chat.controller;

import com.planb.blog.chat.dto.ChatRequest;
import com.planb.blog.chat.dto.ChatResponse;
import com.planb.blog.chat.service.ArticleIndexService;
import com.planb.blog.chat.service.ChatService;
import com.planb.blog.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ArticleIndexService articleIndexService;

    /**
     * IP 限流: 每个IP每分钟最多10次请求
     */
    private final ConcurrentHashMap<String, AtomicInteger> rateLimitMap = new ConcurrentHashMap<>();
    private volatile long lastResetTime = System.currentTimeMillis();

    /**
     * 同步聊天接口
     */
    @PostMapping
    public Result<ChatResponse> chat(@RequestBody ChatRequest request, HttpServletRequest httpRequest) {
        // 限流检查
        if (!checkRateLimit(httpRequest)) {
            return Result.error(429, "请求过于频繁，请稍后再试");
        }

        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return Result.error("消息不能为空");
        }

        if (request.getMessage().length() > 1000) {
            return Result.error("消息长度不能超过1000个字符");
        }

        try {
            ChatResponse response = chatService.chat(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("聊天处理失败", e);
            return Result.error("AI 服务暂时不可用，请稍后重试");
        }
    }

    /**
     * SSE 流式聊天接口
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream(@RequestBody ChatRequest request, HttpServletRequest httpRequest) {
        // 限流检查 - 如果超限，返回错误的 SSE
        if (!checkRateLimit(httpRequest)) {
            SseEmitter emitter = new SseEmitter();
            try {
                emitter.send(SseEmitter.event().name("error").data("请求过于频繁，请稍后再试"));
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
            return emitter;
        }

        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            SseEmitter emitter = new SseEmitter();
            try {
                emitter.send(SseEmitter.event().name("error").data("消息不能为空"));
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
            return emitter;
        }

        return chatService.chatStream(request);
    }

    /**
     * 手动重建索引
     */
    @PostMapping("/index/rebuild")
    public Result<String> rebuildIndex() {
        try {
            articleIndexService.rebuildIndex();
            return Result.success("索引重建成功");
        } catch (Exception e) {
            log.error("索引重建失败", e);
            return Result.error("索引重建失败: " + e.getMessage());
        }
    }

    /**
     * 简单 IP 限流
     */
    private boolean checkRateLimit(HttpServletRequest request) {
        String ip = getClientIp(request);
        long now = System.currentTimeMillis();

        // 每分钟重置计数
        if (now - lastResetTime > 60_000) {
            rateLimitMap.clear();
            lastResetTime = now;
        }

        AtomicInteger count = rateLimitMap.computeIfAbsent(ip, k -> new AtomicInteger(0));
        return count.incrementAndGet() <= 10;
    }

    /**
     * 获取客户端 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For 可能包含多个 IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
