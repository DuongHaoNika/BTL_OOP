package com.project.web.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String source;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(name="created_at" ,nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at" ,nullable = false)
    private LocalDateTime updatedAt;
}
