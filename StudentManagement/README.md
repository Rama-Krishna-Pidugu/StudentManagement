# StudentManagement (Spring Boot)

A simple Student Management web application with role-based access:
- **Admin** can create students/departments, assign department+semester, and upload marks.
- **Student** can view profile, filter marks by semester, and mark attendance (max once per day).

The UI is served from `src/main/resources/static/` and communicates with REST APIs that return a consistent `ApiResponse`.

---

## Modules

Key packages/classes:
- `com.studentManagement.controller`
  - `AuthController`, `AdminController`, `StudentController`
  - `GlobalExceptionHandler` (returns errors as `ApiResponse`)
  - `ViewController` (redirects `/` to `login.html`)
- `com.studentManagement.service`
  - `AuthService` (login/logout/current user)
  - `AdminService` (student/department/assignment/marks)
  - `StudentService` (profile/marks/attendance)
  - `CustomUserDetailsService` (loads users by email for Spring Security)
- `com.studentManagement.repository`
  - JPA repositories for `User`, `Student`, `Department`, `Mark`, `Attendance`
- `com.studentManagement.model`
  - Entities: `User`, `Student`, `Department`, `Mark`, `Attendance`
  - Enum: `Role` (`ADMIN`, `STUDENT`)
- `com.studentManagement.config`
  - `SecurityConfig` (Spring Security setup)
  - `DataInitializer` (seeds default admin user)
- `com.studentManagement.dto`
  - `Dtos.java`: consolidated DTOs into a single file (records)

Frontend:
- `src/main/resources/static/login.html`
- `src/main/resources/static/admin-dashboard.html`
- `src/main/resources/static/student-dashboard.html`
- `src/main/resources/static/js/app.js`
- `src/main/resources/static/css/styles.css`

---

## System Requirements

Server runtime:
- Java **17**
- Spring Boot **3.2.5**
- A Java build tool: **Maven Wrapper** (`./mvnw`)

Database:
- **MySQL** (configured in `src/main/resources/application.properties`)
  - `spring.datasource.url=jdbc:mysql://localhost:3306/studentmanagement`
  - Set `spring.datasource.username` and `spring.datasource.password` for your environment
  - JPA auto schema update: `spring.jpa.hibernate.ddl-auto=update`

Frontend:
- Web browser (uses jQuery from CDN)

---

## System Architecture

Layered architecture:
1. **Controllers** expose REST endpoints.
2. **Services** implement business logic (validation, mapping, and checks).
3. **Repositories** access the database via Spring Data JPA.
4. **Entities** define the database model.
5. **Security** controls access per role using Spring Security and HTTP session.

Security flow:
- Users authenticate via `POST /api/auth/login`.
- The app uses **roles** from the `User` entity (`Role.ADMIN` / `Role.STUDENT`).
- URL authorization rules are defined in `SecurityConfig`:
  - `/api/admin/**` requires `ROLE_ADMIN`
  - `/api/student/**` requires `ROLE_STUDENT`
  - `GET /api/auth/me` requires authentication

DTO approach:
- All DTOs are consolidated into `com.studentManagement.dto.Dtos` as nested records.

---

## Output / What the Project Produces

After building and running, the application provides:
- REST APIs that respond with JSON:
  - Successful responses: `ApiResponse.success(message, data)`
  - Error responses: `ApiResponse.error(message)`
- Two role-based dashboards:
  - `admin-dashboard.html` (Admin functions)
  - `student-dashboard.html` (Student functions)
- Persistent data stored in MySQL tables:
  - `users`, `students`, `departments`, `marks`, `attendance`

Default seeded data:
- `DataInitializer` creates an admin user if it does not exist (email: `admin@student.com`).

---

## How It Works (End-to-End)

1. **Start the app**
   - Run the Spring Boot app (see “Run” below).
   - Access `http://localhost:8080/` which redirects to `login.html`.

2. **Login (AJAX)**
   - `login.html` sends credentials to `POST /api/auth/login`.
   - `AuthService.login(...)` authenticates via Spring Security, creates an HTTP session, and returns:
     - `AuthResponse` including the authenticated user role.
   - Frontend redirects based on role:
     - `ADMIN` → `/admin-dashboard.html`
     - otherwise → `/student-dashboard.html`

3. **Admin dashboard**
   - Loads current user via `GET /api/auth/me`.
   - Calls:
     - `GET /api/admin/students` and `GET /api/admin/departments`
     - `POST /api/admin/students` (create student account + student row)
     - `POST /api/admin/departments` (create department)
     - `PUT /api/admin/students/assign` (assign department + semester)
     - `POST /api/admin/marks` (upload marks)

4. **Student dashboard**
   - Loads current user via `GET /api/auth/me`.
   - Calls:
     - `GET /api/student/profile`
     - `GET /api/student/marks` (optionally `?semester=...`)
     - `GET /api/student/attendance`
     - `POST /api/student/attendance` (mark today’s attendance)

   Attendance rule:
   - The system checks if attendance for today already exists and throws an error if so.

5. **Error handling**
   - Validation errors and application errors are converted into `ApiResponse` by `GlobalExceptionHandler`.

---

## REST API Summary

All endpoints return JSON shaped like `ApiResponse<T>`:
- `success: true/false`
- `message`
- `data`
- `timestamp`

### Auth
- `POST /api/auth/login` (public)
- `POST /api/auth/logout` (authenticated)
- `GET /api/auth/me` (authenticated)

### Admin
- `POST /api/admin/students`
- `GET /api/admin/students`
- `POST /api/admin/departments`
- `GET /api/admin/departments`
- `PUT /api/admin/students/assign`
- `POST /api/admin/marks`

### Student
- `GET /api/student/profile`
- `GET /api/student/marks?semester={semester}` (semester optional)
- `POST /api/student/attendance`
- `GET /api/student/attendance`

---

## Run Instructions

1. Ensure MySQL is running and the database exists:
   - database name: `studentmanagement`
2. Update `src/main/resources/application.properties` with your MySQL credentials.
3. Build and run using Maven Wrapper:
   - `./mvnw spring-boot:run`
4. Open:
   - `http://localhost:8080/login.html`

---

## Notes

- The frontend is static and served by Spring Boot; requests are made using jQuery AJAX.
- The project uses HTTP sessions (not JWT).
- During development, JPA is configured with `ddl-auto=update` which can modify the schema automatically.

