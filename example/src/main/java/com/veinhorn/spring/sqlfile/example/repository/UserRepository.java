package com.veinhorn.spring.sqlfile.example.repository;

import com.veinhorn.spring.sqlfile.SqlFromResource;
import com.veinhorn.spring.sqlfile.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional(readOnly = true)
    @SqlFromResource(path = "select_top_users.sql")
    List<User> findAll();

    @Transactional(readOnly = true)
    @SqlFromResource(path = "select_all_users.sql", countQueryPath = "count_all_users.sql")
    Page<User> findAllPaged(Pageable pageable);

    @SqlFromResource(path = "select_user_by_id.sql")
    User findByUserId(Integer userId);

    @SqlFromResource(path = "select_user_by_name.sql")
    User findByUsername(String username);
}
