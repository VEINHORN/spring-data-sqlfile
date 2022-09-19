package com.veinhorn.spring.sqlfile.example.controller;

import com.veinhorn.spring.sqlfile.example.domain.User;
import com.veinhorn.spring.sqlfile.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping
    public List<User> getAll(@RequestParam String username) {
        if (username != null) return service.getByUsername(username);

        return service.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return service.getById(id);
    }
}
