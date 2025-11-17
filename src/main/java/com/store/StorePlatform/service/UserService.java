package com.store.StorePlatform.service;

import com.store.StorePlatform.entity.User;
import com.store.StorePlatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User getOrCreate(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setEmail(email.toLowerCase());
                    return userRepository.save(user);
                });
    }

    @Transactional
    public User updateLastLogin(String email, Instant timestamp) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("User not found for email " + email));
        user.setLastLoginAt(timestamp);
        return userRepository.save(user);
    }
}

