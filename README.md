
# Online Learning Platform (LMS)

## Introduction

The Online Learning Platform (Learning Management System - LMS) is an application designed to enable instructors and students to interact and exchange knowledge online. The platform includes features such as registration, course management, lesson display, assessments, and other features that facilitate the learning process.

## Project Features

- **Registration/Login**: Supports both students and instructors for registering and logging in using their credentials.
- **Course Management**: Instructors can add, edit, and delete courses.
- **Student Management**: Add students to courses, track their results, and provide feedback.
- **Assessments Management**: Provide assessments based on students' performance in tests.
- **Review Management**: Students can add comments and reviews about the courses they have completed.

## Tools and Technologies Used

- **Java 17**: The core language used for building the platform.
- **Spring Boot**: The primary framework for building the application.
- **Spring Security**: To manage security and user authentication.
- **JWT**: For handling token-based authentication.
- **JPA (Hibernate)**: For data management and interacting with the database.
- **MySQL**: The database used for storing data.
- **Maven**: For dependency management and building the project.
- **JUnit and Mockito**: For unit testing.

## Setting Up the Project

### 1. Setup Environment

Before you can run the platform, ensure you have the following environment set up:

- **Java 17** or higher
- **Maven** for managing dependencies
- **MySQL** or any similar relational database

### 2. Set Up Database

1. Create a new database in MySQL:
   ```bash
   CREATE DATABASE lms;
   ```

2. Modify your database configuration in the `application.properties` or `application.yml` file:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/lms
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
   ```

### 3. Add Environment Variables

In the `application.properties` or `application.yml`, set up your JWT settings:

```properties
auth.token.jwtSecret=your_jwt_secret_key_here
auth.token.expirationTime=3600000
```

### 4. Build the Project

Use Maven to build and run the project:

```bash
mvn clean install
mvn spring-boot:run
```

### 5. Access the Application

Once the application starts, you can access the API at `http://localhost:8080`.

## API Endpoints

### 1. Register

#### Register a Student
```http
POST /register/student
```
**Request body**:
```json
{
    "name": "Student Name",
    "email": "student@example.com",
    "password": "password123",
    "age": 22,
    "gender": "Male",
    "phoneNumber": "1234567890",
}
```

#### Register an Instructor
```http
POST /register/instructor
```
**Request body**:
```json
{
    "name": "Instructor Name",
    "email": "instructor@example.com",
    "password": "password123",
    "age": 40,
    "gender": "Female",
    "phoneNumber": "0987654321",
    "bio": "Instructor bio here"
}
```

### 2. Login

#### Login
```http
POST /login
```
**Request body**:
```json
{
    "email": "user@example.com",
    "password": "password123"
}
```
**Response**:
```json
{
    "success": true,
    "data": "JWT_TOKEN_HERE",
    "message": "Login successful"
}
```

### 3. Add Course

#### Add New Course (For Instructors)
```http
POST /courses
```
**Request body**:
```json
{
    "name": "Java Programming",
    "description": "Learn the basics of Java programming",
}
```

### 4. Add Review

#### Add Review for Course (For Students)
```http
POST /reviews
```
**Request body**:
```json
{
    "courseId": 1,
    "StudentId":1.
    "rating": 5,
    "comment": "This course is amazing!"
}
```

## Unit Tests

JUnit and Mockito are used for testing the system's services. Ensure you write unit tests for all key services and controllers in the system.

## Contributing

If you wish to contribute to this project, you can follow these steps:

1. **Clone the project**:
   ```bash
   git clone https://github.com/yourusername/lms.git
   ```

2. **Create a new branch**:
   ```bash
   git checkout -b feature-name
   ```

3. **Make your changes**.

4. **Push the changes to the repository**:
   ```bash
   git commit -m "Add feature"
   git push origin feature-name
   ```

5. **Create a Pull Request**.

## Notes

- Ensure you use a **strong JWT Secret** key.
- This project can be customized to fit your own online learning platform needs.

---

**License**:
This project is open source, and you can use it and modify it under the open-source license terms.
