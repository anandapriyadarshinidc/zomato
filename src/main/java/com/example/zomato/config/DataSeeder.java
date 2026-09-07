package com.example.zomato.config;

import com.example.zomato.entity.User;
import com.example.zomato.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds a single static user on application startup, since this project has
 * no registration/login/authentication. This static user (id = 1) is the
 * "logged in" user used for all cart and order operations.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User staticUser = new User("Static User", "user@example.com");
            userRepository.save(staticUser);
        }
    }
}
