# Bluemoon EdTech Backend

Backend MVP for an EdTech platform with one administrator and student accounts. The administrator manages courses, lessons, and enrollment approvals. Students register, request access, and consume lessons after approval.

## Technology stack

- Java 17
- Spring Boot 3.5.7
- Spring Security with JWT
- Database-backed refresh tokens
- Spring Data JPA / Hibernate
- PostgreSQL
- SendGrid email
- Maven
- Swagger / OpenAPI

## Main application flow

```text
Admin creates course -> adds lessons -> publishes course
                                             |
Student registers -> logs in -> requests access
                                             |
                                  Admin approves request
                                             |
                       Student gets lesson access for 30 days
```

## Roles

### Student

Every account created through registration has the `STUDENT` role.

Students can:

- Register and log in
- Refresh access tokens and log out
- Read and update their profile
- Change their email using OTP
- Reset their password using OTP
- Browse published courses
- Request access to a published course
- View approved courses
- View lessons while enrollment is active

Students cannot manage courses, manage lessons, publish courses, or approve enrollments.

### Admin

The admin is created automatically during application startup from environment variables.

The admin can:

- Create, update, delete, publish, and unpublish courses
- Add, update, list, and delete lessons
- View pending enrollment requests
- Approve enrollment requests

## Prerequisites

Install:

1. JDK 17
2. PostgreSQL
3. Git
4. Maven 3.9+ or use the included Maven wrapper
5. A SendGrid account and API key for OTP emails
6. Optionally, Postman for API testing

Verify Java:

```powershell
java -version
```

The output must report Java 17.

Verify Maven:

```powershell
mvn -version
```

## PostgreSQL setup

Start PostgreSQL and create the database:

```sql
CREATE DATABASE edtech_db;
```

The local datasource configuration is in `src/main/resources/application.yml`.

Current expected connection:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/edtech_db
    username: postgres
    password: NewStrongPassword123
```

Update these values if the local PostgreSQL username, password, port, or database name differs.

Hibernate uses `ddl-auto: update`, so tables are created or updated automatically when the application starts.

## Required environment variables

| Variable | Purpose |
|---|---|
| `ADMIN_EMAIL` | Email used to create the administrator |
| `ADMIN_PASSWORD` | Administrator login password |
| `ADMIN_NAME` | Administrator display name |
| `SENDGRID_API_KEY` | Sends password-reset and email-change OTPs |

Set them in PowerShell before starting:

```powershell
$env:ADMIN_EMAIL="admin@example.com"
$env:ADMIN_PASSWORD="StrongAdminPassword123"
$env:ADMIN_NAME="Bluemoon Admin"
$env:SENDGRID_API_KEY="your-sendgrid-api-key"
```

For IntelliJ IDEA, open:

```text
Run → Edit Configurations → Environment variables
```

Add the same four variables.

## Admin bootstrap behavior

During startup:

1. The backend checks whether a user with `ADMIN_EMAIL` exists.
2. If the email does not exist, it creates a verified `ADMIN`.
3. If the email already exists, it does nothing.

Changing `ADMIN_PASSWORD` later does not change the password of an admin already stored in the database.

## Run the application

Open a terminal in the directory containing this README and `pom.xml`.

Using installed Maven:

```powershell
mvn spring-boot:run
```

Using the included wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

The server runs at:

```text
http://localhost:8081
```

Health check:

```http
GET http://localhost:8081/api/health
```

Expected response:

```text
OK
```

## Build and tests

Run tests:

```powershell
mvn test
```

Build the JAR:

```powershell
mvn clean package
```

Run the JAR:

```powershell
java -jar target/bluemoon-edtech-0.0.1-SNAPSHOT.jar
```

## Swagger

Swagger UI:

```text
http://localhost:8081/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8081/v3/api-docs
```

For protected endpoints:

1. Log in.
2. Copy `accessToken`.
3. Click **Authorize** in Swagger.
4. Enter the access token.

## Frontend configuration

Local API base URL:

```javascript
const API_BASE_URL = "http://localhost:8081";
```

Protected requests require:

```http
Authorization: Bearer <accessToken>
Content-Type: application/json
```

Example:

```javascript
const response = await fetch(`${API_BASE_URL}/user/my-courses`, {
  headers: {
    Authorization: `Bearer ${accessToken}`,
  },
});
```

### Important CORS limitation

The backend does not currently define a CORS policy. A browser frontend running at another origin, such as `http://localhost:3000` or `http://localhost:5173`, may be blocked.

CORS must be configured in the backend before normal browser integration. Swagger and Postman are not affected by browser CORS restrictions.

## Authentication behavior

- Access token lifetime: 15 minutes
- Refresh token lifetime: 7 days
- OTP lifetime: 5 minutes

The frontend should store:

- `accessToken` for protected requests
- `refreshToken` for obtaining a new access token

When a protected request returns `401`, call `/api/auth/refresh`, store the new access token, and retry the original request.

Logout revokes the supplied refresh token. Password reset and email change revoke all refresh tokens belonging to that user.

## API reference

### Authentication

#### Register student

```http
POST /api/auth/register
```

```json
{
  "name": "Student Name",
  "email": "student@example.com",
  "password": "password123",
  "phone": "9876543210"
}
```

Response:

```json
{
  "id": "user-public-uuid",
  "name": "Student Name",
  "email": "student@example.com",
  "role": "STUDENT"
}
```

#### Login

```http
POST /api/auth/login
```

```json
{
  "email": "student@example.com",
  "password": "password123"
}
```

Response:

```json
{
  "id": "user-public-uuid",
  "name": "Student Name",
  "email": "student@example.com",
  "verified": true,
  "accessToken": "jwt-access-token",
  "refreshToken": "refresh-token"
}
```

The admin uses the same login endpoint.

#### Refresh access token

```http
POST /api/auth/refresh
```

```json
{
  "refreshToken": "refresh-token"
}
```

Response:

```json
{
  "accessToken": "new-jwt-access-token"
}
```

#### Logout

```http
POST /api/auth/logout
```

```json
{
  "refreshToken": "refresh-token"
}
```

Response: `200 OK`

### Forgot password

Request OTP:

```http
POST /api/auth/forgot-password
```

```json
{
  "email": "student@example.com"
}
```

Verify OTP:

```http
POST /api/auth/verify-forgot-otp
```

```json
{
  "email": "student@example.com",
  "otp": "123456"
}
```

Reset password:

```http
POST /api/auth/reset-password
```

```json
{
  "email": "student@example.com",
  "newPassword": "newpassword123"
}
```

### Profile

Authentication is required.

Get profile:

```http
GET /api/profile
```

Update profile:

```http
PUT /api/profile
```

All update fields are optional:

```json
{
  "college": "Example College",
  "year": 3,
  "stream": "CSE",
  "about": "Student profile",
  "address": "Kolkata",
  "linkedin": "https://linkedin.com/in/example",
  "github": "https://github.com/example",
  "website": "https://example.com",
  "profileImageUrl": "https://example.com/profile.jpg"
}
```

Profile response:

```json
{
  "userPublicId": "user-public-uuid",
  "college": "Example College",
  "year": 3,
  "stream": "CSE",
  "about": "Student profile",
  "address": "Kolkata",
  "linkedin": "https://linkedin.com/in/example",
  "github": "https://github.com/example",
  "website": "https://example.com",
  "profileImageUrl": "https://example.com/profile.jpg"
}
```

### Change email

Authentication is required.

Request OTP:

```http
POST /user/change-email/request
```

```json
{
  "newEmail": "newemail@example.com"
}
```

Verify OTP:

```http
POST /user/change-email/verify-otp
```

```json
{
  "newEmail": "newemail@example.com",
  "otp": "123456"
}
```

After success, clear tokens and send the user to login.

### Public courses

No authentication is required.

```http
GET /courses
GET /courses/{courseId}
```

Course response:

```json
{
  "id": 1,
  "title": "Java Fundamentals",
  "description": "Introduction to Java",
  "thumbnailUrl": "https://example.com/java.jpg",
  "published": true
}
```

### Student enrollment and lessons

Authentication is required.

Request access to a published course:

```http
POST /user/courses/{courseId}/request-access
```

Response:

```text
Access request sent
```

Get active courses:

```http
GET /user/my-courses
```

Response:

```json
[
  {
    "id": 1,
    "title": "Java Fundamentals",
    "description": "Introduction to Java",
    "thumbnailUrl": "https://example.com/java.jpg",
    "published": true
  }
]
```

Get lessons:

```http
GET /courses/{courseId}/lessons
```

An active enrollment is required.

```json
[
  {
    "id": 1,
    "title": "Introduction",
    "videoUrl": "https://example.com/video-1",
    "orderIndex": 1
  }
]
```

### Admin courses

The `ADMIN` role is required.

Create:

```http
POST /admin/courses
```

```json
{
  "title": "Java Fundamentals",
  "description": "Introduction to Java",
  "thumbnailUrl": "https://example.com/java.jpg"
}
```

New courses are unpublished by default.

Other course endpoints:

```http
GET    /admin/courses
GET    /admin/courses/{courseId}
PUT    /admin/courses/{courseId}
DELETE /admin/courses/{courseId}
PUT    /admin/courses/{courseId}/publish
PUT    /admin/courses/{courseId}/unpublish
```

Update payload; all fields are optional:

```json
{
  "title": "Updated title",
  "description": "Updated description",
  "thumbnailUrl": "https://example.com/new-image.jpg",
  "published": false
}
```

Deleting a course also deletes its lessons.

### Admin lessons

The `ADMIN` role is required.

Add lesson:

```http
POST /admin/courses/{courseId}/lessons
```

```json
{
  "title": "Introduction",
  "videoUrl": "https://example.com/video-1",
  "orderIndex": 1
}
```

`orderIndex` must be positive and unique within a course.

Other lesson endpoints:

```http
GET    /admin/courses/{courseId}/lessons
PUT    /admin/lessons/{lessonId}
DELETE /admin/lessons/{lessonId}
```

Update payload; all fields are optional:

```json
{
  "title": "Updated introduction",
  "videoUrl": "https://example.com/updated-video",
  "orderIndex": 2
}
```

### Admin enrollments

The `ADMIN` role is required.

List pending requests:

```http
GET /admin/enrollments/pending
```

```json
[
  {
    "id": 1,
    "userId": "user-public-uuid",
    "userName": "Student Name",
    "userEmail": "student@example.com",
    "courseId": 1,
    "courseTitle": "Java Fundamentals",
    "status": "PENDING",
    "startDate": null,
    "expiryDate": null
  }
]
```

Approve:

```http
PUT /admin/enrollments/{enrollmentId}/approve
```

Approval grants course access for 30 days.

## Enrollment expiration

A scheduled job runs every day at midnight in the server timezone.

It finds enrollments where:

```text
status = ACTIVE
expiryDate < current time
```

It changes their status to `EXPIRED`. Expired students cannot retrieve lesson content.

## Error responses

Typical error:

```json
{
  "timestamp": "2026-06-21T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "fieldErrors": {
    "email": "must be a well-formed email address"
  }
}
```

| Status | Meaning |
|---|---|
| `400` | Invalid request, validation, OTP, or refresh token |
| `401` | Missing/invalid authentication or incorrect login |
| `403` | Authenticated but not permitted, or no active enrollment |
| `404` | Requested resource not found |
| `409` | Duplicate or conflicting operation |
| `500` | Unexpected server error |

## Frontend implementation checklist

- Use separate student and admin routes/layouts.
- Attach the access token to every protected request.
- Refresh the access token after `401`.
- Clear tokens after logout, password reset, or email change.
- Show only published courses to public visitors.
- Add a student “Request Access” action.
- Do not show lessons unless the lesson endpoint succeeds.
- Add admin screens for courses, lessons, and enrollment approval.
- Handle `400`, `401`, `403`, `404`, and `409` separately.
- Configure backend CORS before browser integration.

## Current MVP limitations

- CORS is not configured.
- Enrollment rejection and cancellation are not implemented.
- Enrollment duration is fixed at 30 days.
- Course progress tracking is not implemented.
- Payment integration is not implemented.
- List endpoints are not paginated.
- Database migrations are not managed by Flyway or Liquibase.
- Deployment configuration is not included.
