package com.veinhorn.spring.sqlfile.example;

import com.veinhorn.spring.sqlfile.SqlFile;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    @SqlFile(path = "query.sql")
    String findById(int userId);
}
