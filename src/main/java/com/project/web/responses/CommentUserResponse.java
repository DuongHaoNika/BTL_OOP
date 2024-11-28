package com.project.web.responses;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentUserResponse {
    private Long id;

    private String body;

    private String username;

    private String fullname;

    private String userImage;

    private String urlImage;

    private LocalDateTime updatedAt;
}
