# Employee Task Manager

A Spring Boot command-line application for managing employees and tasks. It uses
JPA entities, Spring Data repositories, service and facade layers with dependency
injection, an H2 database and a `CommandLineRunner` user interface.

## Run

```sh
./mvnw spring-boot:run
```

## Structure

- `model/` — `Employee` and `Task` JPA entities
- `repository/` — Spring Data repositories
- `service/` — business logic
- `facade/` — application facade
- `ui/` — command-line interface
- `src/test/` — Spring Boot context test
