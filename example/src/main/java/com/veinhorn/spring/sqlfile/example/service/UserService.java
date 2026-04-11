package com.veinhorn.spring.sqlfile.example.service;

import com.veinhorn.spring.sqlfile.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> getAll();
    Page<User> getAllPaged(Pageable pageable);
    User getById(int userId);
    List<User> getByUsername(String username);
}
