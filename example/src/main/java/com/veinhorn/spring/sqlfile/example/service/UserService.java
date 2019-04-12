package com.veinhorn.spring.sqlfile.example.service;

import com.veinhorn.spring.sqlfile.example.domain.User;
import com.veinhorn.spring.sqlfile.example.repository.UserRepositoryGenerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepositoryGenerated userRepository;

    public List<User> findAll(int id) {
        return userRepository.findAll();
    }
}
