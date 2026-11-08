# 💬 The Message - Real-Time Chat Backend

> A scalable, containerized Spring Boot backend built from scratch for a real-time messaging application, featuring WebSocket configuration, JWT security, relational database mapping, and automated email verification.

---

## 🚀 Project Overview
**The Message** is a microservice-ready backend application designed to handle user authentication, relational chat rooms, and real-time messaging. Built using **Java 21** and **Spring Boot**, the project leverages modern enterprise patterns to ensure scalability, security, and clean separation of concerns.

---

## 📐 Architecture & Database Design

The database schema was conceptualized from scratch using a robust Entity-Relationship model:
* **Users & Chats (Many-to-Many):** A user can participate in multiple chats, and a chat can belong to multiple users.
* **Chats & Messages (One-to-Many):** A single chat room can contain many messages, but each message strictly belongs to one unique chat room.
* **JPA/Hibernate Integration:** Instead of manually writing DDL scripts in MySQL, Spring Boot automatically manages and updates the database schema (`ddl-auto=update`) using Hibernate as an ORM bridge.

---

## 📂 Project Structure

The project is structured following clean architecture principles to ensure high maintainability and scalability:

```text
src/main/java/by/Brahyam/Garcia/Blas/the_message/
│
├── Config/         # Configurations for WebSockets, JWT, and standard Spring settings
├── Controllers/    # REST endpoints managing incoming/outgoing internet traffic (Public & Secured)
├── Dao/            # Spring Data JPA repositories handling precise database operations
├── Dto/            # Data Transfer Objects for filtering payloads and protecting sensitive data
├── Enums/          # Constants for user types, roles, and account states
├── Exceptions/     # Custom global error and exception handlers
├── Security/       # JWT filters, authentication beans, and Spring Security rules
├── Service/        # Business logic interfaces (the "brain" of the application)
├── Servicelmpl/    # Concrete implementations of business logic and mail handling
└── Util/           # Helper classes and standard web response wrappers
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)
## 📋 Prerequisites

Before running this project, ensure you have the following installed on your local machine:
* **[Git](https://git-scm.com/)**: To clone the repository.
* **[Docker Desktop](https://www.docker.com/products/docker-desktop/)**: Must be installed and running in the background to build and spin up the containers.
* *(Note: You do NOT need Java, Maven, or MySQL installed locally. Docker handles all dependencies internally.)*
