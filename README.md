# Midterm Project - Company & Employee REST API

Spring Boot (Java 21) REST API implementing CRUD for **Company** and **Employee** with layered architecture, DTO-based
responses, validation, Liquibase migrations, Swagger UI, and integration tests with MockMvc + Testcontainers.

## Security (Spring Security)

This project uses **Spring Security** with database-backed users, **BCrypt** password hashing, role-based access
control, and method-level security (`@PreAuthorize`).

### Login credentials

| Username | Password   | Role  |
|----------|------------|-------|
| `admin`  | `admin123` | ADMIN |
| `user`   | `user123`  | USER  |

Users are stored in the `users` table and seeded on first startup (`UserInitializerService`) with encrypted passwords.

### Authentication (form login + session cookie)

1. Open [`/login`](http://localhost:8080/login) - Spring Security’s built-in login page - and sign in. Spring creates an
   HTTP session and sets a **`JSESSIONID`** cookie.
2. Use the API in the same browser (e.g. Swagger UI) - the cookie is sent automatically.
3. **Logout**: `POST /logout` (or use the logout link after implementing one); session is invalidated.

Integration tests authenticate the same way: `POST /login` with username/password, then reuse the session cookie on API
calls.

**Example with curl**:

```bash
# 1) Log in (save cookies)
curl -c cookies.txt -b cookies.txt -X POST http://localhost:8080/login \
  -d "username=admin&password=admin123"

# 2) Read CSRF token from cookie file, then call API (example)
CSRF=$(grep XSRF-TOKEN cookies.txt | awk '{print $7}')
curl -b cookies.txt -H "X-XSRF-TOKEN: $CSRF" http://localhost:8080/api/profiles
```

### Access rules

| Area                                      | Enforcement                                                                            | Who can access             |
|-------------------------------------------|----------------------------------------------------------------------------------------|----------------------------|
| `GET /api/companies/**`                   | -                                                                                      | Public                     |
| `POST`, `PUT`, `DELETE /api/companies/**` | **`SecurityFilterChain`** `.hasRole("ADMIN")`                                          | **ADMIN** only             |
| `GET`, `POST`, `PUT /api/employees/**`    | **`SecurityFilterChain`** `.authenticated()`                                           | **USER** or **ADMIN**      |
| `DELETE /api/employees/**`                | **`SecurityFilterChain`** `.hasRole("ADMIN")`                                          | **ADMIN** only             |
| `GET /api/profiles/**`                    | **`SecurityFilterChain`** `.authenticated()` + **`@PreAuthorize`** on `ProfileService` | **ADMIN** only (see below) |
| Swagger UI, OpenAPI docs                  | -                                                                                      | Public                     |

### Two separate enforcement layers (no overlap)

1. **URL / filter chain** (`SecurityConfig`) - `hasRole("ADMIN")` for:
    - Company create, update, delete
    - Employee delete

2. **Method security** (`@PreAuthorize`) - `ProfileService` only:
    - `getAllProfiles()` and `getProfile(id)` use `@PreAuthorize("hasRole('ADMIN')")`
    - The filter chain only requires **login** for `/api/profiles/**`; a **USER** passes authentication but receives *
      *403 Forbidden** from method security. An **ADMIN** is allowed through.

This makes it obvious that `@PreAuthorize` is active: log in as `user` / `user123`, call `GET /api/profiles` → 403; log
in as `admin` / `admin123` → 200.

### Protected endpoints (authentication required)

- All `/api/employees/**` endpoints
- `/api/profiles/**` (must be logged in; ADMIN role enforced by `@PreAuthorize`)

### CSRF

CSRF is **explicitly configured** in `SecurityConfig`:

```java
.csrf(csrf -> csrf
		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
```

Because authentication uses **`JSESSIONID`**, mutating requests (`POST`, `PUT`, `DELETE`) must include a valid CSRF
token.

- **Login** (`/login`): Spring Security’s default login form includes the CSRF field automatically
- **REST API** (after login): send header **`X-XSRF-TOKEN`** matching the **`XSRF-TOKEN`** cookie
- **Tests**: MockMvc `.with(csrf())` on mutating requests

### Security configuration

- `SecurityConfig` - `SecurityFilterChain`, explicit CSRF setup, `BCryptPasswordEncoder`, form login/logout, URL rules
- `UserService` - loads users from PostgreSQL
- `@EnableMethodSecurity` - enables `@PreAuthorize` on service methods

## Profiles, i18n & logging

### Running with a profile

Default profile is **`dev`** (H2 in-memory + sample data). Use **`prod`** for PostgreSQL.

**Command line:**

```bash
# dev — H2, debug logging, pre-populated ref data
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# prod — PostgreSQL (local or via docker compose)
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

**IDE:** set active profile to `dev` or `prod` in run configuration (Spring Boot tab → Active profiles).

**Docker Compose** already sets `SPRING_PROFILES_ACTIVE=prod`.

### Custom properties (`app.*`)

Bound via `@ConfigurationProperties` + `@Validated` in `AppSettingsProperties` and `AppCredentialsProperties`:

| Property                            | Role                                           |
|-------------------------------------|------------------------------------------------|
| `app.settings.title`                | App title shown in `/api/app-info`             |
| `app.settings.pagination-limit`     | Max companies returned by `GET /api/companies` |
| `app.settings.contact-email`        | Contact email in app metadata                  |
| `app.settings.external-service-url` | External service URL in app metadata           |
| `app.credentials.adminPassword`     | Password for preconfigured admin user          |
| `app.credentials.userPassword`      | Password for preconfigured user                |

Values differ per profile — check `application-dev.yaml` vs `application-prod.yaml`.

### i18n (Accept-Language)

Message bundles: `messages.properties`, `messages_en.properties`, `messages_ka.properties` (UTF-8).

Locale comes from the **`Accept-Language`** header (`AcceptHeaderLocaleResolver`). Supported: `en`, `ka`.

**What is localized:**

- `GET /api/app-info` — `welcomeMessage` field
- `GlobalExceptionHandler` — 404, 403, 500 bodies
- Validation error messages in `openapi.yaml`

**Try it:**

```bash
curl -H "Accept-Language: en" http://localhost:8080/api/app-info
curl -H "Accept-Language: ka" http://localhost:8080/api/app-info
```

Georgian welcome example: `კეთილი იყოს თქვენი მობრძანება ...`

### Logging

- SLF4J via Lombok `@Slf4j`
- Levels: **DEBUG** (reads), **INFO** (creates/updates/deletes), **WARN/ERROR** (exceptions)
- Log file: **`logs/app.log`** (rolling, 10 MB per file, 7 days history)
- Profile controls verbosity — see table above

## Tech stack

- **Java**: 21
- **Spring Boot**: 4
- **Web**: Spring WebMVC
- **Security**: Spring Security (BCrypt, roles, method security)
- **Persistence**: Spring Data JPA (Hibernate)
- **Database**: PostgreSQL (prod) / H2 (dev)
- **Migrations**: Liquibase (`src/main/resources/db/changelog`)
- **OpenAPI / Swagger UI**: springdoc
- **DTOs + API interfaces**: generated from `src/main/resources/api/openapi.yaml` via OpenAPI Generator
- **Tests**: JUnit 5, MockMvc, Testcontainers (PostgreSQL)

## Project structure (layered)

- **Controller**: `src/main/java/org/edu/kiu/midterm/controller/`
    - `CompanyController`, `EmployeeController`, `ProfileController` - implement OpenAPI-generated interfaces (
      `org.edu.kiu.midterm.api.*`)
    - Login/logout: Spring Security built-in `/login` page (not a custom REST controller)
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

### App

- **GET** `/api/app-info` — app metadata + localized welcome (public)

### Company

- **POST** `/api/companies` — create company
- **GET** `/api/companies` — get all companies
- **GET** `/api/companies/{id}` — get company by id
- **PUT** `/api/companies/{id}` — update company
- **DELETE** `/api/companies/{id}` — delete company

### Employee

- **POST** `/api/employees` - create employee (authenticated)
- **GET** `/api/employees` - get all employees (authenticated)
- **GET** `/api/employees/{id}` - get employee by id (authenticated)
- **PUT** `/api/employees/{id}` - update employee (authenticated)
- **DELETE** `/api/employees/{id}` - delete employee (**ADMIN**)

### Profile

- **GET** `/api/profiles` - list profiles (**ADMIN**, via `@PreAuthorize`)
- **GET** `/api/profiles/{id}` - get profile by id (**ADMIN**, via `@PreAuthorize`)

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

