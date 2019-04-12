# spring-data-sqlfile

When your *SQL* queries become huge, `spring-data-sqlfile` helps you to load them from resources folder. It makes your code clean and you can work with well-formed *SQL* queries in your IDE.

## Usage

Just mark methods with `SqlFromResource` annotation and provide valid path to the SQL query.

```java
@Repository
public interface UserRepository {
    @SqlFromResource(path = "select_top_users.sql")
    String findById(int userId);
}
```

Then you will get below code, which you can use in your services:

```java
interface UserRepositoryGenerated {
  @Query("SELECT *     FROM users     WHERE id > 3;")
  void findById();
}
```

# How it works

`spring-data-sqlfile` is an annotation processor which generates code during compile time.
