package test;

import com.veinhorn.spring.sqlfile.SqlFromResource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryWithCount extends JpaRepository<String, Integer> {
    @SqlFromResource(path = "find_top_users.sql", countQueryPath = "count_users.sql")
    Page<String> findPageAll();
}
