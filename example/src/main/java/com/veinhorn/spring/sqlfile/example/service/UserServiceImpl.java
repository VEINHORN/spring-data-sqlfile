package com.veinhorn.spring.sqlfile.example.service;

import com.veinhorn.spring.sqlfile.example.domain.User;
import com.veinhorn.spring.sqlfile.example.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepositoryImpl repository;

    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User getById(int userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<User> getByUsername(String username) {
        return Optional
                .ofNullable(repository.findByUsername(username))
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }
}
