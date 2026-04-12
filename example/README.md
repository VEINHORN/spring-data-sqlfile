# Spring Data SQL File Example

This module demonstrates how to use the **spring-data-sqlfile** library in a standard Spring Boot application. It showcases how to move SQL queries from Java annotations into separate `.sql` files, keeping your repository interfaces clean and maintainable.

## Key Features

*   **External SQL Files**: Queries are stored in `src/main/resources/*.sql`.
*   **Pagination Support**: Uses `countQueryPath` for paged results.
*   **Automatic Implementation**: Shows how the annotation processor generates the implementation at compile-time.
*   **H2 Database**: Uses an in-memory database for quick demonstration.

---

## Getting Started

### Prerequisites

*   **Java 8** or higher.
*   **Maven 3.6+**.

### Run the Application

Navigate to the project root and run:

```shell
mvn clean spring-boot:run -pl example
```

The application will start on `http://localhost:8080`.

---

## Project Overview

### 1. Repository Interface
See `UserRepository.java`. Notice the `@SqlFromResource` annotations pointing to SQL files.

```java
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @SqlFromResource(path = "select_all_users.sql", countQueryPath = "count_all_users.sql")
    Page<User> findAllPaged(Pageable pageable);
}
```

### 2. SQL Files
SQL files are located in `src/main/resources/`. For example, `select_all_users.sql`:

```sql
SELECT * FROM users;
```

### 3. Generated Code
When you run `mvn compile`, the library generates `UserRepositoryImpl.java` in `target/generated-sources/annotations`. This generated class contains the actual `@Query` annotations.

---

## Testing the Endpoints

Once the application is running, you can test the following endpoints:

### 1. Get all users
Returns a list of all users in the database.
```shell
curl http://localhost:8080/users
```

### 2. Get user by ID
Fetches a single user by their unique identifier.
```shell
curl http://localhost:8080/users/1
```

### 3. Search user by name
Finds a user by their username using a query parameter.
```shell
curl "http://localhost:8080/users?username=David"
```

### 4. Paged users
Demonstrates pagination support.
```shell
curl "http://localhost:8080/users/paged?page=0&size=5"
```

---

## Database Console
You can access the H2 console at `http://localhost:8080/h2-console` with:
*   **JDBC URL**: `jdbc:h2:mem:testdb`
*   **User**: `sa`
*   **Password**: `password`
