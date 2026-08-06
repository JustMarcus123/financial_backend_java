# Financial Backend (401k / Retirement Plan Management API)

A Spring Boot REST API for managing employer-sponsored retirement plans — think a simplified 401(k) administration platform. It handles sponsors (employers), employees, payroll-driven contributions, and plan loans, with JWT-based authentication and role-based access control.

> **Status:** Practice / learning project — built to work through backend architecture, auth, and multi-tenant style access control in Spring Boot. Not production-ready (see [Known Limitations](#known-limitations)).

## Features

- **Authentication** — JWT access tokens + refresh tokens, stored as HttpOnly cookies
- **Role-based access control** — `EMPLOYER_ADMIN` and `EMPLOYEE` roles, enforced via `@PreAuthorize`
- **Sponsor management** — create, update, activate employer/plan sponsors
- **Employee management** — add employees under a sponsor, scoped so admins only see their own sponsor's employees
- **Payroll processing** — CSV payroll upload, drives contribution calculations and balance updates
- **Plan loans** — employees can request loans against their plan balance; employer admins can review loan requests
- **API documentation** — Swagger / OpenAPI UI via springdoc
- **Health checks** — Spring Boot Actuator endpoints

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 |
| Security | Spring Security, JWT (jjwt) |
| Persistence | Spring Data JPA, PostgreSQL |
| Build | Maven |
| Testing | JUnit 5, Mockito |
| Docs | springdoc-openapi (Swagger UI) |
| Containerization | Docker |

## Architecture

The project follows a standard layered architecture:

```
Controller  →  Service  →  Repository  →  Entity
                  ↓
                 Dto (Request / Response)
```

- **Controller** — REST endpoints, request/response handling
- **Service** — business logic
- **Repository** — Spring Data JPA interfaces for persistence
- **Entity** — JPA-mapped domain models
- **Dto** — request/response payloads, kept separate from entities
- **Config** — security config, JWT filter, password encoding
- **Exception** — centralized exception handling via `@ControllerAdvice`

A separate `PlanLoan` module handles loan requests from both the employee and employer sides.

## Getting Started

### Prerequisites

- Java 21
- Maven (or use the included `./mvnw` wrapper)
- PostgreSQL running locally (or via Docker)

### Environment Variables

The app is configured via environment variables (with local defaults in `application.properties`):

| Variable | Description | Default |
|---|---|---|
| `SPRING_DATASOURCE_URL` | Postgres JDBC URL | `jdbc:postgresql://localhost:5432/finance_portfolio_db` |
| `SPRING_DATASOURCE_USERNAME` | DB username | `choinu` |
| `SPRING_DATASOURCE_PASSWORD` | DB password | *(none)* |
| `JWT_SECRET` | Secret key for signing JWTs (256+ bits) | *(placeholder — override in real use)* |
| `APP_BASE_URL` | Frontend base URL, used for links/CORS | `http://localhost:5173` |
| `SERVER_PORT` | Port the app runs on | `8080` |

> ⚠️ Email (SMTP) credentials should also be supplied via environment variables rather than committed to `application.properties`.

### Run locally

```bash
# clone the repo
git clone https://github.com/JustMarcus123/financial_backend_java.git
cd financial_backend_java

# set required env vars, then run
./mvnw spring-boot:run
```

### Run with Docker

```bash
docker build -t financial-backend .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/finance_portfolio_db \
  -e SPRING_DATASOURCE_USERNAME=your_user \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  -e JWT_SECRET=your_secret \
  financial-backend
```

### API Docs

Once running, Swagger UI is available at:
```
http://localhost:8080/swagger-ui.html
```

## API Overview

| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/api/auth/login` | Public | Log in, sets access/refresh token cookies |
| POST | `/api/auth/refresh` | Public | Refresh access token |
| POST | `/api/auth/logout` | Authenticated | Log out, clears cookies |
| GET | `/api/auth/me` | Authenticated | Get current session's user info |
| POST | `/api/users/register` | Public | Register a new user |
| POST | `/api/sponsor/create` | — | Create a sponsor |
| GET | `/api/sponsor/allsponsor` | — | List all sponsors |
| PUT | `/api/sponsor/update_sponsor/{id}` | — | Update a sponsor |
| PUT | `/api/sponsor/activate/{id}` | — | Activate a sponsor |
| POST | `/api/employee/add` | `EMPLOYER_ADMIN` | Add an employee under the admin's sponsor |
| GET | `/api/employee/fetch` | `EMPLOYER_ADMIN` | List employees under the admin's sponsor |
| POST | `/api/payroll/upload` | `EMPLOYER_ADMIN` | Upload a payroll CSV, processes contributions |
| GET | `/api/payroll/fetchBalance` | Authenticated | Get the logged-in employee's plan balance |
| POST | `/api/loanrequest/newRequest` | `EMPLOYEE` | Request a new plan loan |
| GET | `/api/loanrequest/fetchLoanStatus` | `EMPLOYEE` | Check status of the employee's loan requests |
| GET | `/api/loanrequest/fetchLoanRequest` | `EMPLOYER_ADMIN` | View incoming loan requests |

## Testing

```bash
./mvnw test
```

Current coverage is limited to core payroll service logic — expanding coverage around contribution and loan calculations is a planned next step.

## Known Limitations

This is a practice project and is not deployment-ready. Known gaps:

- Test coverage is thin outside of `PayrollService`
- Some duplicated code between the `Employee` and `Employer` loan-request packages
- No CI pipeline yet

## Roadmap

- [ ] Expand test coverage (loans, contributions, payroll edge cases)
- [ ] Refactor duplicate exception classes between `PlanLoan/Employee` and `PlanLoan/Employer`
- [ ] Add CI (GitHub Actions) for build + test on push
- [ ] Add integration tests with Testcontainers (Postgres)
