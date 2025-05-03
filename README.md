# 🎓 Online Learning Platform API

The **Online Learning Platform** is a backend RESTful API built with Java and Spring Boot. It allows instructors to create and manage courses, while students can enroll, study, and track their progress. The backend is secured with JWT and integrated with Swagger for API documentation. Data is stored in a MySQL database.

---

## 🧾 Introduction

This backend system provides the core logic and endpoints for a learning management system (LMS). It includes user authentication, course management, enrollment tracking, and more.

---

## ✨ Features

- 🔐 JWT-based authentication (Login & Registration)
- 👨‍🏫 Instructor & Student management
- 📚 Course creation, update, delete, and retrieval (CRUD)
- ✅ Student enrollment & result tracking
- 🛡️ Role-based access control (RBAC)
- 📄 API documentation with Swagger UI
- 🧪 Unit & controller testing using JUnit & Mockito

---

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot
- **Database**: MySQL
- **Authentication**: JWT
- **Testing**: JUnit, Mockito
- **API Documentation**: Swagger
- **ORM**: Spring Data JPA (Hibernate)
- **Build Tool**: Maven

---

## 🚀 Setup and Installation

### ✅ Prerequisites

- Java 17+
- Maven
- MySQL

---

### 📥 Clone the Repository

```bash
git clone https://github.com/yourusername/online-learning-platform.git
cd online-learning-platform
```

---

### 🗃️ Database Configuration

Create a MySQL database:

```sql
CREATE DATABASE online_learning_db;
```

Update `application.properties` with your credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/online_learning_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

⚠️ It's recommended to use environment variables or Spring's `@Value` to manage secrets securely.

---

## ▶️ Run the Project

```bash
mvn clean install
mvn spring-boot:run
```

---

## 📘 API Documentation

Access the API documentation via Swagger:

```
http://localhost:8080/swagger-ui/
```

---

## 🧩 Project Structure

```
📦 src
├── 📁 entity/           # JPA entities (Course, Instructor, Student, etc.)
├── 📁 repository/       # Spring Data JPA repositories
├── 📁 service/          # Service layer (business logic)
├── 📁 controller/       # REST controllers
├── 📁 security/         # JWT authentication and filters
├── 📁 config/           # Security and application configs
├── 📁 testing/          # JUnit & Mockito test classes
```

---

## 🧪 Running Tests

To run all unit and controller tests:

```bash
mvn test
```

Test coverage includes:
- Service layer
- Controller layer
- Authentication and validation logic

---

## 🔮 Future Enhancements

- 💳 Payment gateway for premium content
- 📝 Course reviews and feedback
- 📊 Learning progress analytics
- 📥 Upload video lectures and files
- 🔔 Email notifications for activities

---

## 🤝 Contributing

Contributions are welcome! Feel free to fork the repository and open a pull request:

1. Fork this repo
2. Create your feature branch: `git checkout -b feature/my-feature`
3. Commit your changes: `git commit -m "Add my feature"`
4. Push to the branch: `git push origin feature/my-feature`
5. Open a pull request

---

