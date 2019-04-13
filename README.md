# spring-data-sqlfile

[![Build Status](https://travis-ci.org/VEINHORN/spring-data-sqlfile.svg?branch=master)](https://travis-ci.org/VEINHORN/spring-data-sqlfile) [![](https://jitpack.io/v/VEINHORN/spring-data-sqlfile.svg)](https://jitpack.io/#VEINHORN/spring-data-sqlfile)

When your *SQL* queries become huge, `spring-data-sqlfile` helps you to load them from resources folder. It makes your code clean and you can work with well-formed *SQL* queries in your IDE.

## Install

### Add JitPack repository

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### Add dependency

```xml
<dependency>
    <groupId>com.github.VEINHORN.spring-data-sqlfile</groupId>
    <artifactId>sqlfile-processor</artifactId>
    <version>521abae4f2</version>
</dependency>
```

## Usage

Just mark methods with `SqlFromResource` annotation and provide valid path to the SQL query.

```java
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @SqlFromResource(path = "select_top_users.sql")
    List<User> findAll();

    @SqlFromResource(path = "select_user_by_id.sql")
    User findById(int userId);

    @SqlFromResource(path = "select_user_by_name.sql")
    User findByUsername(String username);
}
```

Recompile project and you'll give generated repository with SQL queries injected from resources folder. See [example](example) for more details.


```java
@Repository
public interface UserRepositoryGenerated extends JpaRepository<User, Integer> {
  @Query(
      value = "SELECT *     FROM users     WHERE id = 2;",
      nativeQuery = true
  )
  List<User> findAll();

  @Query(
      value = "SELECT *     FROM users     WHERE id = :userId",
      nativeQuery = true
  )
  User findById(int userId);

  @Query(
      value = "SELECT *     FROM users     WHERE username = :username",
      nativeQuery = true
  )
  User findByUsername(String username);
}
```

# How it works

`spring-data-sqlfile` is an annotation processor which generates code during compile time.
