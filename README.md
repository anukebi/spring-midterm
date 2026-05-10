# Midterm Project — Company & Employee REST API

Spring Boot (Java 21) REST API implementing CRUD for **Company** and **Employee** with layered architecture, DTO-based
responses, validation, Liquibase migrations, Swagger UI, and integration tests with MockMvc + Testcontainers.

## Tech stack

- **Java**: 21
- **Spring Boot**: 4
- **Web**: Spring WebMVC
- **Persistence**: Spring Data JPA (Hibernate)
- **Database**: PostgreSQL
- **Migrations**: Liquibase (`src/main/resources/db/changelog`)
- **OpenAPI / Swagger UI**: springdoc
- **DTOs + API interfaces**: generated from `src/main/resources/api/openapi.yaml` via OpenAPI Generator
- **Tests**: JUnit 5, MockMvc, Testcontainers (PostgreSQL)

## Project structure (layered)

- **Controller**: `src/main/java/org/edu/kiu/midterm/controller/`
    - `CompanyController`, `EmployeeController`
    - Implements generated API interfaces (`org.edu.kiu.midterm.api.*`)
- **Service**: `src/main/java/org/edu/kiu/midterm/service/`
    - Business logic lives here (create/get/update/delete)
- **Repository**: `src/main/java/org/edu/kiu/midterm/repository/`
    - Spring Data JPA repositories
- **Entities**: `src/main/java/org/edu/kiu/midterm/model/entity/`
    - `CompanyEntity` ↔ `EmployeeEntity` relationship (One-to-Many / Many-to-One)
    - `EmployeeEntity` ↔ `ProfileEntity` relationship (One-to-One)
- **DTOs**: generated to `target/generated-sources/java/org/edu/kiu/midterm/model/dto/`

## Entities & relationships

- **Company** has many **Employees**
- **Employee** belongs to one **Company** (optional `companyId`)
- **Employee** has one **Profile**

## API endpoints (CRUD)

Defined in `src/main/resources/api/openapi.yaml`.

### Company

- **POST** `/api/companies` — create company
- **GET** `/api/companies` — get all companies
- **GET** `/api/companies/{id}` — get company by id
- **PUT** `/api/companies/{id}` — update company
- **DELETE** `/api/companies/{id}` — delete company

### Employee

- **POST** `/api/employees` — create employee
- **GET** `/api/employees` — get all employees
- **GET** `/api/employees/{id}` — get employee by id
- **PUT** `/api/employees/{id}` — update employee
- **DELETE** `/api/employees/{id}` — delete employee

## Validation & error handling

- Bean validation is applied on request bodies (`@Valid`) from the generated API interfaces.
- Required fields are defined in `openapi.yaml` (e.g., `CompanyDto.name`, `EmployeeDto.firstName/lastName/email`), which
  generates `@NotNull` in DTOs.
- Size/email rules come from the OpenAPI schema (`minLength/maxLength`, `format: email`).
- Validation errors return **HTTP 400** with a JSON map of `{ fieldName: message }` via `GlobalExceptionHandler`.
- Non-existing resource access returns **HTTP 404** (`EntityNotFoundException`).

## Swagger UI

Run the app and open Swagger UI (springdoc default):

- `http://localhost:8080/swagger-ui/index.html`

## Running the application via Docker Compose

This repository includes a `Dockerfile` and `docker-compose.yml` that start:

- `db`: PostgreSQL
- `app`: the Spring Boot application (built into a jar inside the Docker image)

Run:

```bash
docker compose up --build
```

Then open:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

Stop containers:

```bash
docker compose down
```

## Running tests (integration tests)

Integration tests use:

- `MockMvc` for HTTP assertions
- `Testcontainers` to start a real **PostgreSQL** container
- `CoreTest` base class (`src/test/java/org/edu/kiu/midterm/support/CoreTest.java`)

Run:

```bash
mvn test
```

### Test data

Request/expected DTO JSON data are located in:

- `src/test/resources/data/company/`
- `src/test/resources/data/employee/`

They are loaded using `CoreTest.loadResource(...)`.

