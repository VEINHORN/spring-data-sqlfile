# spring-data-sqlfile

When your Spring Data queries become huge, `spring-data-sqlfile` helps you load it from resources folder.

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
