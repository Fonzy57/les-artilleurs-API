# 🏈 Les Artilleurs API

Backend REST API for the **Les Artilleurs** American Football club platform.

This repository contains the **server-side application** responsible for authentication, authorization, and data management for both the public website and a private staff dashboard.

---

## 📌 Project Context

**Les Artilleurs API** is a personal project developed for an American football club.

The API is designed to:
- Securely authenticate club members (president, staff)
- Manage public website content
- Provide a private dashboard for internal club management

This repository is **backend-only**.  
The frontend is maintained in a separate repository (link below).

---

## 🎯 Main Objectives

- Provide a **secure REST API** for the club website and dashboard
- Handle **authentication and authorization** with role-based access
- Manage club content such as:
  - News
  - FAQ
  - Club information
- Serve as a scalable foundation for future features:
  - Player / licensed member management
  - Match and competition tracking
  - Internal staff tools

---

## 🛠️ Tech Stack

### Backend
- **Java 21**
- **Spring Boot**
- **Spring Web (REST API)**
- **Spring Data JPA (Hibernate)**
- **Spring Security**

### Database
- **MySQL**

### Security
- JWT authentication
- Role-based authorization
- Refresh token mechanism (in progress)
- Password reset token (in progress)
- Password hashing for sensitive data

### Build & Tools
- **Maven**
- **Lombok**
- Jakarta Validation

---

## 📁 Project Structure

The project follows a **layered and modular architecture**, with a clear separation of concerns:
- Controllers (API layer)
- Services (business logic)
- Repositories (data access)
- DTOs (data transfer)
- Security (JWT, roles, filters, tokens)

---

## ✅ Features Status

### Implemented
- JWT authentication
- Role-based access control
- User login / logout
- Public site management:
  - News
  - FAQ
  - Club information
- Secure password storage

### In Progress
- Refresh token mechanism
- Token lifecycle optimization

### Planned
- Staff dashboard data (players, matches, licenses)
- Extended admin features
- Improved test coverage

---

## 🐳 Local Development with Docker

A `docker-compose.yml` file is provided to simplify local development.

It includes:
- **MySQL 8.0** database
- **phpMyAdmin** for database administration

### Start database services

```bash
docker-compose up -d
```

### Services

| Service    | Port | Description          |
|-----------|------|----------------------|
| MySQL     | 3306 | Application database |
| phpMyAdmin| 8181 | Database UI          |

phpMyAdmin will be available at: http://localhost:8181

> Docker is optional but recommended for a consistent local environment.

---

## 🔀 Branching Strategy

- **`develop`** → active development branch (most up-to-date)
- **`main`** → production-ready code only

Until the project is deployed in production, **`develop` is the reference branch**.

---

## 🔗 Related Repositories

- **Frontend repository**: [Les Artilleurs - Frontend](https://github.com/Fonzy57/les-artilleurs-FRONT)

---

## 📄 Project Status

🚧 **In progress**

This project is actively developed and serves as:
- A real-world inspired backend architecture
- A technical showcase for recruiters
- A foundation for a production-ready club management platform

---

## ⚠️ License & Usage

This project is provided for **educational and demonstration purposes only**.

- ❌ Commercial use is not allowed
- ❌ Copying or reusing the code without permission is prohibited
- ✅ Viewing and learning from the code is allowed

---

## 👤 Author

Developed by **Stéphane**  
Full Stack Developer  

---

© 2026 Stéphane. All rights reserved.  
Unauthorized copying, modification, distribution, or use of this project or its source code is strictly prohibited.
