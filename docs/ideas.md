# Ideas

## Generate all from one interface

The main idea is to declare what you want, the rest library will generate for you. In that case we can just remove this interface after compilation.

- Define repositories in declarative way
- Define all repositories in one file

### Define

#### 1st type

```java
@Repositories
public interface MyRepositories {
  @RepositoryMethods({
    @RepositoryMethod(name="findByIdAndUsername", types = { Integer.class, String.class }) // get parameters from method name
  }),
  JpaRepository<User, Integer> userRepository();
  
  
  JpaRepository<Order, Integer> orderRepository();
}
```

#### 2nd type

```java
@Repositories
public interface MyRepositories {
  @RepositoryMethods({
    @RepositoryMethod(name="findByIdAndUsername(int, String)") // get parameters from method name
  }),
  JpaRepository<User, Integer> userRepository();
  
  
  JpaRepository<Order, Integer> orderRepository();
}
```





### Result



```java
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  User findById(Integer id); // we can analyze SQL and get this id field name from it
}

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
  
}
```



## Create Spring Service in declarative way

The main idea is to describe your service. The rest for us. Declarative style of service. We can use GraphQL and mapstruct to simplify service declaration.

```java
@Microservice
public interface UserMicroservice {
  @WebController()
  @Get @Post void user(
    @WebService()
  	Object user;
  );
}
```

This will generate several classes like controllers, services, repositories.

## Notes

Based on my analysis, the experimental branch was an exploration of a more declarative and centralized way to define Spring Data JPA repositories. Here's a breakdown of what you were trying
to achieve:

1. Centralized Repository Definition
   The primary goal was to move away from creating multiple individual interface files for each repository. Instead, you wanted to define all repositories for a project (or a module) within a
   single "master" interface annotated with @Repositories.

In your experimental-example, this looked like this:

```java
@Repositories
public interface MyRepositories {
    @RepositoryMethods({
    @RepositoryMethod(name = "findById(int)", queryPath = "find_by_id.sql")
    })
JpaRepository<User, Integer> userRepository();
}
```


2. Automated Repository Generation
   The SpringSqlFileProcessor was intended to take this single interface and generate individual, standard Spring Data JPA repository interfaces (e.g., UserRepository).
- Each method in the @Repositories interface (like userRepository()) would trigger the generation of a corresponding @Repository interface.
- The return type (e.g., JpaRepository<User, Integer>) would define the entity and ID types for the generated repository.

3. Method Signature Parsing
   You introduced a way to describe repository methods through annotations rather than Java method signatures:
- @RepositoryMethod(name = "findById(int)", queryPath = "find_by_id.sql")
- The idea was to parse the name string (e.g., findById(int)) to automatically determine the method name and parameter types for the generated interface. This is mentioned in docs/ideas.md
  as a way to "get parameters from method name."

4. Declarative Services/Microservices
   Your docs/ideas.md suggests an even broader vision: using a similar declarative style to generate not just repositories, but also Controllers and Services (e.g., using @Microservice and
   @WebController annotations). The goal was to describe the system's architecture in a high-level interface and let the annotation processor handle the boilerplate of generating the
   multi-layered Spring components.

Current Implementation Status
- Annotations: The experimental annotations (@Repositories, @RepositoryMethod, @RepositoryMethods) are defined.
- Documentation: The core concepts are outlined in docs/ideas.md.
- Processor: The SpringSqlFileProcessor.java is currently focused on the "main" feature (generating implementations for existing @Repository interfaces using @SqlFromResource). The logic to
  handle the new @Repositories flow hasn't been implemented yet.
- Example: The experimental-example serves as a blueprint for how you wanted the API to feel for the end-user.

Essentially, you were prototyping a "Repository DSL" (Domain Specific Language) using Java annotations to drastically reduce boilerplate in Spring Data applications.