package com.example.restapihomework.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CommentDTO {
    private long commentId;
    private String comment;
}
