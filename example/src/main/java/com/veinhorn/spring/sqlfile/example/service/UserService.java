package com.veinhorn.spring.sqlfile.example.service;

import com.veinhorn.spring.sqlfile.example.domain.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getById(int userId);
    List<User> getByUsername(String username);
}
