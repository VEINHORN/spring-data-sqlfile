# spring-data-sqlfile

[![Java CI with Maven](https://github.com/VEINHORN/spring-data-sqlfile/actions/workflows/maven.yml/badge.svg)](https://github.com/VEINHORN/spring-data-sqlfile/actions/workflows/maven.yml) [![](https://jitpack.io/v/VEINHORN/spring-data-sqlfile.svg)](https://jitpack.io/#VEINHORN/spring-data-sqlfile)

**spring-data-sqlfile** is a Java library that allows you to move large SQL queries from your Spring Data JPA `@Query` annotations into separate `.sql` files in your resources folder. This keeps your Java code clean and allows you to use SQL syntax highlighting and formatting in your IDE.

## Why use spring-data-sqlfile?

*   **Clean Code**: No more multiline strings or messy SQL inside Java annotations.
*   **IDE Support**: Full SQL syntax highlighting, auto-completion, and formatting for your queries.
*   **Maintainability**: Manage complex queries as separate files.
*   **Automatic Generation**: Uses annotation processing to generate the final repository at compile-time.

---

## Installation

This library is available via [JitPack](https://jitpack.io/#VEINHORN/spring-data-sqlfile).

### 1. Add JitPack repository

Add this to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### 2. Add dependency

```xml
<dependency>
    <groupId>com.github.VEINHORN.spring-data-sqlfile</groupId>
    <artifactId>sqlfile-processor</artifactId>
    <version>v0.1.0</version>
</dependency>
```

---

## How it Works

The library uses an **Annotation Processor** to scan your interfaces marked with `@Repository`. For every method annotated with `@SqlFromResource`, it reads the specified SQL file from your resources and generates a new interface (defaulting to the original name + `Impl` postfix) containing the `@Query` annotation with the injected SQL.

---

## Quick Start

### 1. Define your Repository

Create an interface and annotate its methods with `@SqlFromResource`. Provide the path to your SQL file relative to the `resources` folder.

```java
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @SqlFromResource(path = "sql/find_all_users.sql")
    List<User> findAll();

    @SqlFromResource(path = "sql/find_by_id.sql")
    User findByUserId(Integer userId);
}
```

### 2. Create SQL files

Place your SQL files in `src/main/resources/sql/`.

**src/main/resources/sql/find_all_users.sql**:
```sql
SELECT * FROM users;
```

**src/main/resources/sql/find_by_id.sql**:
```sql
SELECT * FROM users WHERE id = :userId;
```

### 3. Compile your project

Run `mvn clean compile`. The annotation processor will generate `UserRepositoryImpl` in the `target/generated-sources` directory.

### 4. Use the generated Repository

In your service, inject the generated `UserRepositoryImpl` interface:

```java
@Service
public class UserService {
    @Autowired
    private UserRepositoryImpl repository; // Note the 'Impl' postfix

    public List<User> getAllUsers() {
        return repository.findAll();
    }
}
```

---

## Configuration

### Customizing the Class Postfix

By default, the processor appends `Impl` to the generated interface name. You can customize this in your `maven-compiler-plugin` configuration:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.0</version>
    <configuration>
        <compilerArgs>
            <arg>-AclassPostfix=Generated</arg>
        </compilerArgs>
    </configuration>
</plugin>
```
*Now the generated interface will be named `UserRepositoryGenerated`.*

---

## Example

For a complete working setup, check out the [example module](example).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
