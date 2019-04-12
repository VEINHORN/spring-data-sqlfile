package com.veinhorn.spring.sqlfile.example;

import com.veinhorn.spring.sqlfile.example.domain.User;
import com.veinhorn.spring.sqlfile.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User(1, "David");

        userRepository.save(user);
    }
}
