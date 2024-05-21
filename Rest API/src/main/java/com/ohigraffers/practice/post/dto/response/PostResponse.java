package com.ohigraffers.practice.post.dto.response;

import com.ohigraffers.practice.post.model.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "게시글 정보 DTO")
public class PostResponse {

    @Schema(description = "게시글 번호(PK)")
    private Long code;

    @Schema(description = "게시글 제목")
    private String title;

    @Schema(description = "게시글 내용")
    private String content;

    @Schema(description = "게시글 작성자")
    private String writer;

    @Schema(description = "게시글이 작성된 시간")
    private LocalDateTime createdAt;

    @Schema(description = "게시글이 수정된 시간")
    private LocalDateTime modifiedAt;

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getCode(),
                post.getTitle(),
                post.getContent(),
                post.getWriter(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
