# Emergency Call Management System

A full-stack application for managing emergency calls. This system provides a RESTful backend built with Spring Boot, Hibernate, and Spring Security, along with a reactive front-end built using React and TypeScript. The application supports CRUD operations on emergency calls, pagination, filtering, user registration with password validation, and basic authentication with multiple roles. All components are containerized and deployed using Docker Compose.

---

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
    - [Backend](#backend)
    - [Frontend](#frontend)
- [API Documentation (Swagger)](#api-documentation-swagger)
- [Testing](#testing)
- [Authentication and Default Accounts](#authentication-and-default-accounts)
- [Production Considerations & Simplifications](#production-considerations--simplifications)

---

## Introduction

The Emergency Call Management System is designed to help manage emergency calls with a strong emphasis on a robust backend. The system allows administrators and users to create, update, delete, and view emergency calls. In addition, the application supports user registration with proper validation and provides detailed API documentation via Swagger.

---

## Features

### Backend
- **RESTful APIs:** CRUD endpoints for emergency calls.
- **ORM:** Hibernate for object-relational mapping.
- **Pagination & Filtering:** Retrieve emergency calls with pagination.
- **Validation & Error Handling:** Input validation using Spring Validation and global exception handling.
- **Security:** Basic authentication using Spring Security with multiple roles (e.g., ADMIN, USER).
- **User Registration:** New users can register with password validation (minimum 6 characters). The system prevents registration of reserved usernames (e.g., "admin").
- **Swagger Integration:** Interactive API documentation available via Swagger.

### Frontend
- **React & TypeScript:** A reactive, type-safe front-end application.
- **Material‑UI:** For a modern and responsive UI.
- **React Router:** For client-side routing (includes Login, Registration, and Dashboard pages).
- **CRUD Dashboard:** Create, edit (PUT), delete, and view emergency calls with pagination.
- **Authentication:** Users must log in with Basic Auth credentials; the app supports user registration.

### Deployment
- **Docker & Docker Compose:** Containerized deployment with separate containers for backend and frontend. Start the entire application using a single command.

---

## Technologies Used

### Backend:
- Java 17
- Spring Boot (v3.x)
- Spring Data JPA (Hibernate)
- Spring Security
- Springdoc OpenAPI (Swagger)
- PostgreSQL
- H2 Database (in-memory; for tests)
- Maven

### Frontend:
- React 18
- TypeScript
- Material‑UI
- React Router DOM
- Create React App

### DevOps:
- Docker
- Docker Compose

---

## Project Structure

```plaintext
emergency-call-management-system/
├── backend/
│   ├── src/main/java/com/example/edvantistask
│   │   ├── config/             # Security and CORS configuration
│   │   ├── controller/         # REST controllers (EmergencyCallController, RegistrationController, etc.)
│   │   ├── dto/                # Data Transfer Objects (EmergencyCallDTO, RegistrationRequest as record, etc.)
│   │   ├── exception/          # Custom exceptions and global error handler
│   │   ├── model/              # Entity classes (EmergencyCall, UserAccount, etc.)
│   │   ├── repository/         # Spring Data JPA repositories
│   │   ├── service/            # Business logic and service implementations
│   │   └── EdvantisTaskApplication.java  # Main application and CommandLineRunner for default admin
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/         # Shared UI components (Layout, etc.)
│   │   ├── contexts/           # AuthContext for authentication state
│   │   ├── pages/              # Pages (Login, Register, Dashboard)
│   │   ├── App.tsx
│   │   └── index.tsx
│   ├── package.json
│   ├── tsconfig.json
│   └── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## Setup and Installation

### Prerequisites:
- Docker and Docker Compose.

### Clone the Repository:
```bash
cd emergency-call-management-system
```

### Build and Run the Application:
Use Docker Compose to build and start all services:
```bash
docker-compose up --build
```
This command builds both the backend and frontend images and starts the containers.

---

## Usage

### Backend
#### API Endpoints:
- `POST /api/emergency-calls` – Create a new emergency call.
- `GET /api/emergency-calls` – Get a paginated list of emergency calls. Supports filtering by incident type.
- `GET /api/emergency-calls/{id}` – Retrieve details of a specific emergency call.
- `PUT /api/emergency-calls/{id}` – Update an existing emergency call.
- `DELETE /api/emergency-calls/{id}` – Delete an emergency call.
- `POST /api/register` – Register a new user account.

#### Swagger Documentation:
Access Swagger UI at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

#### Default Admin Account:
A default administrator account is created on startup:
- **Username:** admin
- **Password:** admin

This account can be used to access protected endpoints (e.g., Swagger).

### Frontend
#### User Interface:
Access the frontend at: [http://localhost:3000](http://localhost:3000)

- **Login Page:** Users must sign in using their credentials.
- **Registration Page:** Accessible via the “Register” link on the Login page.
- **Dashboard:** After logging in, view a paginated list of emergency calls. You can add, edit (PUT), and delete calls.

---

## API Documentation (Swagger)

The backend integrates Swagger (via Springdoc OpenAPI) for interactive API documentation. Visit:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Testing

### Backend Integration Tests:
Integration tests have been written using Spring Boot Test and MockMvc.
#### Running Tests:
```bash
cd edvantis-task-BE
mvn test
```

---

## Authentication and Default Accounts

- New users can register through the `/api/register` endpoint.
- Default admin account (`admin/admin`) is created on startup for administrative access.

---

## Production Considerations & Simplifications

### What Would Change for a Production Environment

1. **Security Enhancements:**
    - Implement HTTPS.
    - Use JWT or OAuth2 instead of Basic Auth.
    - Store credentials securely (e.g., environment variables, HashiCorp Vault).
    - Enable CSRF protection and tighten CORS policies.
    - Harden security (rate limiting, audit logging).

2. **API Documentation and Monitoring:**
    - Restrict access to Swagger UI in production.
    - Integrate centralized logging and monitoring (ELK, Prometheus, Grafana).

3. **Deployment and Infrastructure:**
    - Use Kubernetes or Docker Swarm for scalability.
    - Implement load balancing and auto-scaling.
    - Use persistent storage and backups.

4. **Frontend Optimizations:**
    - Serve assets via a CDN.
    - Optimize build and caching strategies.
    - Use environment variables for API endpoints.

5. **Simplifications Applied in the Current Implementation:**
    - **H2 Database:** Fast but not production-ready.
    - **Basic Authentication:** Simplified but not secure for production.
    - **Default Admin Account:** Hardcoded for testing, should be securely managed in production.
    - **Simplified Error Handling and Logging:** Minimal logging for clarity.
    - **Docker Compose for Local Use:** Production should use advanced orchestration solutions.

By addressing these considerations, the application will be more secure, scalable, and maintainable in a real-world environment.

