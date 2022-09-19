package test;

import com.veinhorn.spring.sqlfile.SqlFromResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<String, Integer> {
    @SqlFromResource(path = "find_top_users.sql")
    List<String> findAll();
}