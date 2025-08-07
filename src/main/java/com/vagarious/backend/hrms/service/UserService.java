package com.vagarious.backend.hrms.service;

import com.vagarious.backend.hrms.model.User;
import com.vagarious.backend.hrms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createEmployee(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User employee = new User();
        employee.setUsername(username);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setRole(User.Role.EMPLOYEE);
        employee.setEnabled(true);

        return userRepository.save(employee);
    }

    public List<User> getAllEmployees() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == User.Role.EMPLOYEE)
                .toList();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean deleteEmployee(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent() && user.get().getRole() == User.Role.EMPLOYEE) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 