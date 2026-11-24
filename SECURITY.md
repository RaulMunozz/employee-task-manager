# Security Policy

**Last reviewed:** 2026-11-24  
**Scope:** `RaulMunozz/employee-task-manager` — Spring Boot command-line application for employees and tasks.

## Architecture

- Spring Boot 4.2.0, Java 17, Spring Data JPA, H2 in-memory database, Maven Wrapper 3.9.12.
- Entry point: `EmployeeTaskManagerSpringApplication` plus `EmployeeTaskManagerCLI` (`CommandLineRunner`).
- It is a **CLI**, not a web server: no controllers, endpoints, forms or HTTP port.
- Entities: `Employee` (name, email, department, salary) and `Task` (name, employee ID, deadline, priority, status).
- No authentication, authorization, sessions, cookies or user accounts.

## Findings fixed in this revision

| Severity | Finding | Fix |
|---|---|---|
| Medium | H2 console dependency and `spring.h2.console.enabled=true` in configuration | Removed `spring-boot-h2console` from `pom.xml`; console explicitly disabled in `application.properties`. |
| Medium | Insecure defaults hard-coded in properties | Datasource user/password and `ddl-auto` now read from environment variables with local-friendly defaults (`sa`, empty password, `create-drop`). |
| Medium | Outdated Spring Boot 4.0.1 | Updated to 4.2.0, the current stable release confirmed from Maven Central metadata. |
| Medium | Weak validation: email only checked for `@`, `NaN` salary accepted, deadline free text, null priority/status could throw | Centralised validation with an email pattern, finite/non-negative salary bounds, ISO `LocalDate` parsing and allowlists for priority/status. |
| Medium | No automated dependency update process | Added `.github/dependabot.yml` for weekly Maven updates. |
| Low | Deprecated explicit H2 dialect warning | Kept for compatibility but documented; not a security defect. |

## Known limitations / risks if the design changes

- **Critical if exposed as a web service:** the application has no authentication or authorization. Any future HTTP layer must add `spring-boot-starter-security` with deny-by-default rules before exposure.
- **Medium — sensitive output:** `Employee.toString()` prints email and salary to stdout. Keep terminal output out of shared logs; mask or omit these fields if logging is introduced.
- **Medium — in-memory storage:** `create-drop` is correct for the CLI demo but not for persistent or production data. Set `JPA_DDL_AUTO=validate` with a real migration and a managed database before any deployment.
- **Low — source duplication:** older `.java` copies exist in the repository root and are not compiled by Maven. The authoritative code is under `src/main/java`. Consider deleting the duplicates in a separate cleanup commit.
- **Low — no security logging:** the CLI uses `System.out`; there is no security event log because there is no remote access or authentication.

## External configuration

Only needed when moving beyond the local demo:

```text
DB_USER=<database user>
DB_PASSWORD=<database password>
JPA_DDL_AUTO=validate
```

Never commit real database passwords. The H2 console must remain disabled.

## Verification performed

- Spring Boot context test (`mvnw clean test`) run locally after the changes.
- Static review of the CLI flow, repositories, entities and configuration.
- No SQL injection found: all persistence uses Spring Data derived queries, with no `@Query`, native SQL or string concatenation.
- No hardcoded credentials or API keys found; the wrapper password variables are unset Maven options, not values.

## Maintenance

- Run `.\mvnw.cmd clean test` after changes.
- Review Dependabot pull requests before merging.
- If web endpoints, file uploads, secrets or user roles are added, perform a new full audit before release.
