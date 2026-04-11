package test;

import java.lang.Integer;
import java.lang.String;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryWithCountImpl extends JpaRepository<String, Integer> {
    @Query(value = "SELECT *" +
               "    FROM users" +
               "    ORDER BY karma" +
               "    LIMIT 5",
           countQuery = "SELECT COUNT(*) FROM users;",
           nativeQuery = true)
    Page<String> findPageAll();
}
