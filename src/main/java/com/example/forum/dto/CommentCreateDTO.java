package com.example.forum.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Long type;
}
