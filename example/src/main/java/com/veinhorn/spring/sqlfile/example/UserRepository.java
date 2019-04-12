package com.veinhorn.spring.sqlfile.example;

import com.veinhorn.spring.sqlfile.SqlFromResource;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    @SqlFromResource(path = "query.sql")
    String findById(int userId);
}
