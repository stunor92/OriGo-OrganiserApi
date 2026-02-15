# Origo Organiser API - Copilot Instructions

## Project Overview

This is a Spring Boot REST API for organizing and managing orienteering events. The API handles event data, courses, competitors, and results using IOF XML format standards. The backend is built with Kotlin and Spring Boot, uses PostgreSQL for data storage, and provides OpenAPI documentation.

## Tech Stack

- **Language**: Kotlin 2.3.10
- **Framework**: Spring Boot 4.0.2
- **JDK**: Java 25 (Liberica distribution)
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Dependencies**:
  - Spring Web (REST controllers)
  - Spring JDBC (database access)
  - Spring Security with OAuth2 Resource Server
  - Spring Actuator (monitoring)
  - SpringDoc OpenAPI (API documentation)
  - JAXB (XML marshalling/unmarshalling)
  - Logstash Logback Encoder (structured logging)
- **Testing**: JUnit 5, Spring Boot Test, Mockito Kotlin

## Project Structure

```
src/
├── main/
│   ├── kotlin/no/stunor/origo/organiserapi/
│   │   ├── controller/      # REST controllers
│   │   ├── services/        # Business logic services
│   │   ├── data/           # Repository layer
│   │   ├── model/          # Domain models
│   │   ├── security/       # Security configuration
│   │   └── exception/      # Custom exceptions
│   └── resources/
│       └── IOF.xsd         # IOF XML schema
└── test/
    └── kotlin/             # Test classes
```

## Coding Guidelines

### General Kotlin Style

- Use Kotlin idiomatic patterns and features (data classes, extension functions, null safety)
- Prefer `val` over `var` for immutability
- Use nullable types (`?`) appropriately and handle nulls safely
- Use `lateinit` for dependency injection with `@Autowired`
- Mark Spring components with `open` keyword (required for CGLIB proxies)
- Use `internal` visibility for controllers and services when appropriate

### Spring Boot Conventions

- Use constructor injection via `@Autowired` on properties
- Annotate service classes with `@Service` and mark them `open`
- Use `@Transactional` annotation on service methods that modify data
- REST controllers should use `@RestController` annotation
- Use appropriate HTTP method annotations: `@GetMapping`, `@PostMapping`, etc.
- Use `@PathVariable` for URL path parameters
- Use `@RequestBody` for request payloads
- Use `@RequestHeader` for custom headers

### Error Handling

- Use `require()` or `check()` for preconditions in Kotlin
- Throw custom exceptions (e.g., `EventNotFoundException`) for domain-specific errors
- Log errors with appropriate context using SLF4J logger
- Create logger instance: `private val log = LoggerFactory.getLogger(this.javaClass)`

### Database and Repositories

- Repository interfaces should extend from appropriate Spring Data interfaces
- Use UUID for entity identifiers
- Methods should return nullable types (`Type?`) when entity might not be found
- Use descriptive method names following Spring Data conventions

### XML Processing

- Use JAXB for XML marshalling/unmarshalling
- Initialize `JAXBContext` once per class for performance
- Always wrap XML parsing in try-catch blocks
- Use IOF (International Orienteering Federation) XML format standards

### Testing

- Use `@SpringBootTest` for integration tests
- Use JUnit 5 test annotations (`@Test`)
- Test classes should be marked `internal`
- Use Mockito Kotlin for mocking dependencies

### Logging

- Use SLF4J for logging
- Log at appropriate levels (ERROR, WARN, INFO, DEBUG)
- Include context in error logs (e.g., exception details)
- Use structured logging where applicable

## Build and Test Commands

- **Build**: `mvn compile`
- **Run tests**: `mvn test`
- **Package**: `mvn package`
- **Run application**: `mvn spring-boot:run`

## Security Considerations

- This API uses OAuth2 Resource Server for authentication
- Never hard-code credentials or secrets
- Use environment variables or application properties for configuration
- Validate all API inputs
- Use Spring Security annotations for authorization where needed

## API Documentation

- API documentation is generated using SpringDoc OpenAPI
- Endpoints are self-documented through annotations
- OpenAPI UI available at `/swagger-ui.html` when running

## Code Quality

- Follow consistent formatting and naming conventions
- Keep methods focused and single-purpose
- Use meaningful variable and function names
- Document complex business logic with comments
- Avoid code duplication; extract common logic to shared functions

## Dependencies

- When adding dependencies, always specify versions explicitly in `pom.xml`
- Use Spring Boot dependency management where possible
- Keep dependencies up to date for security patches
