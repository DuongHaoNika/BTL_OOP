package com.project.web.services;

import com.project.web.models.User;
import com.project.web.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user ->
        {
            user.setPassword(userDetails.getPassword());
            user.setFullname(userDetails.getFullname());
            user.setHometown(userDetails.getHometown());
            user.setSex(userDetails.getSex());
            user.setAge(userDetails.getAge());
            user.setSex(userDetails.getSex());
            user.setSchool(userDetails.getSchool());
            // Cập nhật các thuộc tính khác nếu cần
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User login(User user)
    {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

}
