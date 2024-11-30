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
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public List<User> findAll() {
        return userRepository.findAll();
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
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .hometown(userDTO.getHometown())
                    .school(userDTO.getSchool())
                    .sex(userDTO.getSex())
                    .role(role)
                    .avatar("https://static.vecteezy.com/system/resources/previews/005/129/844/non_2x/profile-user-icon-isolated-on-white-background-eps10-free-vector.jpg")
                    .active(true)
                    .createdAt(LocalDateTime.now().atZone(ZoneId.of("UTC+7")).toLocalDateTime())
                    .updatedAt(LocalDateTime.now().atZone(ZoneId.of("UTC+7")).toLocalDateTime())
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

    public User save(User user) {
        return userRepository.save(user);
    }



    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
