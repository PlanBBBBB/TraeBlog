package com.planb.blog.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String reply;
    private List<Source> sources;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Source {
        private Long articleId;
        private String title;
        private String category;
    }
}
