package com.project.web.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = true)
    private Integer age;

    @Column(nullable = true)
    private String hometown;

    @Column(nullable = true)
    private String school;

    @Column(nullable = true)
    private Boolean sex;

    @Column(nullable = true)
    private Boolean active;

    @Column(name="created_at" ,nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at" ,nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
