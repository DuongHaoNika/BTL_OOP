package com.project.web.dtos;

import com.project.web.models.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NotBlank(message = "Username is required!")
    private String username;

    @NotBlank(message = "Password is required!")
    private String password;

    @NotBlank(message = "Name is required!")
    private String fullname;

    private Integer age;

    private String hometown;

    private String school;

    private Boolean sex;

    private Boolean active;
}
