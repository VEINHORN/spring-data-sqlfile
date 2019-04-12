package com.veinhorn.spring.sqlfile.example.repository;

import com.veinhorn.spring.sqlfile.SqlFromResource;
import com.veinhorn.spring.sqlfile.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @SqlFromResource(path = "select_top_users.sql")
    List<User> findAll();
}
