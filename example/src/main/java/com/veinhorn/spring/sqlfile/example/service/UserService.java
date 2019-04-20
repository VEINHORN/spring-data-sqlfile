package com.veinhorn.spring.sqlfile.example.service;

import com.veinhorn.spring.sqlfile.example.domain.User;
import com.veinhorn.spring.sqlfile.example.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepositoryImpl userRepository;

    public List<User> findAll(int id) {
        return userRepository.findAll();
    }
}
