package com.veinhorn.spring.sqlfile.example.controller;

import com.veinhorn.spring.sqlfile.example.domain.User;
import com.veinhorn.spring.sqlfile.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    List<User> findAll() {
        return userService.findAll(10);
    }
}
