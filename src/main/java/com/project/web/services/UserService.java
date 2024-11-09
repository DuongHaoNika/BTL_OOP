package com.project.web.services;

import com.project.web.components.JwtTokenUtil;
import com.project.web.dtos.UserDTO;
import com.project.web.exceptions.DataNotFoundException;
import com.project.web.models.Role;
import com.project.web.models.User;
import com.project.web.repositories.RoleRepository;
import com.project.web.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

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
                    .password(passwordEncoder.encode(userDTO.getPassword()))
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

    public String login(String username, String password) throws DataNotFoundException{
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new DataNotFoundException("Invalid user or password!");
        }

        if(!passwordEncoder.matches(password, user.get().getPassword())){
            throw new BadCredentialsException("Wrong username or password!");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password, user.get().getAuthorities());
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtil.generateToken(user.get());
    }
}
