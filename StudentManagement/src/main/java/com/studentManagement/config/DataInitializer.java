package com.studentManagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.studentManagement.model.Role;
import com.studentManagement.model.User;
import com.studentManagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail("admin@student.com")) {
            return;
        }

        userRepository.save(User.builder()
                .name("System Admin")
                .email("admin@student.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build());
    }
}
