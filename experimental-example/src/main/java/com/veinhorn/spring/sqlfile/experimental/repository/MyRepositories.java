package com.veinhorn.spring.sqlfile.experimental.repository;

import com.veinhorn.spring.sqlfile.experimental.Repositories;
import com.veinhorn.spring.sqlfile.experimental.RepositoryMethod;
import com.veinhorn.spring.sqlfile.experimental.RepositoryMethods;
import com.veinhorn.spring.sqlfile.experimental.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repositories
public interface MyRepositories {
    @RepositoryMethods({
            @RepositoryMethod(
                    name = "findById(int)", // get types
                    queryPath = "find_by_id.sql"
            )
    })
    JpaRepository<User, Integer> userRepository();

}
