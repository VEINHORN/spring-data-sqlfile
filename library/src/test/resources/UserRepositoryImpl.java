package test;

import java.lang.Integer;
import java.lang.String;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryImpl extends JpaRepository<String, Integer> {
    @Query(value = "SELECT *" +
               "    FROM users" +
               "    ORDER BY karma" +
               "    LIMIT 5",
           nativeQuery = true)
    List<String> findAll();
}