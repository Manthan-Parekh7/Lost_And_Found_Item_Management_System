# 🎒 Lost and Found Management System

A secure and efficient **Spring Boot REST API** project designed to help users report lost or found items. Users can raise claims on found items, and only verified claims are approved by the admin. Built with **in-built Spring Security**, the system enforces access restrictions so that users can only modify their own data. The application also includes **email notifications** on claim decisions.

---

## 📌 Features

### 👤 User Functionalities:
- Report lost items with name, description, and location.
- Post found items for others to claim.
- View and claim found items.
- Update or delete only **their own lost/found items and claims**.
- Receive **email notifications** when claim is approved or rejected.

### 🔐 Admin Functionalities:
- View all items and claims.
- Match lost and found items based on name and location.
- Approve or reject claims using a simple interface.
- Send **automatic email updates** to users about claim status.

---

## 🔒 Security & Authorization

This application uses **in-built Spring Security** to handle:
- User authentication and session-based access control.
- Role-based authorization for `USER` and `ADMIN` roles.
- Ensures that:
  - Users **can only access or modify their own data**.
  - Admins have elevated permissions to approve/reject claims and manage all data.

---

## 🧰 Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security (in-built)**
- **MySQL / H2**
- **JavaMailSender** (for email notifications)
- **Postman** (for testing APIs)

---

## 🗂 Project Structure

```bash
LostAndFoundManagement/
├── src/
│   ├── main/
│   │   ├── java/com/ce/LostAndFoundManagement/
│   │   │   ├── dao/          # Repositories (Data Access Layer)
│   │   │   ├── entity/       # Entity classes
│   │   │   ├── rest/         # Controllers
│   │   │   ├── security/     # Spring Security Configurations
│   │   │   └── service/      # Business logic
│   │   └── resources/        # application.properties, static files
│   ├── test/                 # Unit and Integration Tests
├── pom.xml                   # Maven Build File
├── .gitignore
└── README.md
