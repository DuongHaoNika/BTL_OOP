package com.project.web.services;

import com.project.web.dtos.UserDTO;
import com.project.web.models.Role;
import com.project.web.models.User;
import com.project.web.repositories.RoleRepository;
import com.project.web.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(UserDTO userDTO) throws Exception {
        if(userRepository.existsByUsername(userDTO.getUsername())) {
            throw new Exception("Username is already exists");
        }
        else {
            Role role = roleRepository.findById(1L).orElse(null);
            String password = userDTO.getPassword();

            User user = User
                    .builder()
                    .fullname(userDTO.getFullname())
                    .age(userDTO.getAge())
                    .username(userDTO.getUsername())
                    .password(userDTO.getPassword())
                    .hometown(userDTO.getHometown())
                    .school(userDTO.getSchool())
                    .sex(userDTO.getSex())
                    .role(role)
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            return userRepository.save(user);
        }
    }
}
