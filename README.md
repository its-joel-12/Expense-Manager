<h1 align="center">🧾 Expense Manager</h1>

## ✨About The Project

Expense Manager is a full-stack web application designed to help users efficiently manage their personal expenses and budgets. The application allows users to register, log in securely, categorize their expenses, and track spending over time. It includes role-based access control, user authentication, and optional two-factor authentication (2FA) for enhanced security.

## 🔍 Features
- 👤 User Registration & Login (with email/password)
- 🛡️ Role-Based Access Control (Admin/User roles)
- 🔐 Secure Authentication with JWT & optional 2FA
- 💰 Add, View, Update, and Delete Expenses
- 🗂️ Categorize Expenses (Food, Travel, Bills, etc.)
- 📊 Monthly Budget Tracking (optional enhancement)
- 🔎 View Expense History by date, category, or range
- 🛠️ Global Exception Handling and Custom Error Messages
- ⚙️ Spring Boot Backend, secured with Spring Security

## ⚙️Project Dependencies & Versions:
- Java____ 21
- Maven__ 3.9.8
- Spring__ 3.4.4
### Maven Dependencies:
- Spring Web.
- DevTools
- Spring Data Jpa
- Spring Validator
- Spring Security
- Postgresql
- Lombok
- Swagger Documentation
- ModelMapper

## Installation

1. Clone the repo:
    ```bash
    git clone https://github.com/its-joel-12/Expense-Manager.git
    ```
2. Go to the project directory:
   ```bash
   cd ./Expense-Manager
   ```
3. Create .env file and allow the IDE access it during project run:
   ```bash
   cp .env.sample .env
   ```
   > Add the required env credentials/values
4. Build the Project:
    ```bash
    mvn clean package
    ```
5. Run the JAR File:
    ```bash
    java -jar target/ExpenseManager-0.0.1-SNAPSHOT.jar
    ```
   > Open [http://localhost:8080](http://localhost:8080) with your browser to see the result.

## 📁 Project Structure
```text
   Expense-Manager/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── app/
│   │   │           ├── ExpenseManagerApplication.java
│   │   │           ├── config/           # Security, JWT, OAuth2, Mail configs
│   │   │           ├── controller/       # REST controllers
│   │   │           ├── model/            # JPA entities
│   │   │           ├── repository/       # Spring Data JPA repositories
│   │   │           ├── service/          # Business logic
│   │   │           ├── security/         # Security filters, JWT utils, 2FA
│   │   │           └── util/             # Utility classes
│   │   └── resources/
│   │       ├── application.properties    # Main config
│   │       ├── static/                   # Static web assets (if any)
│   └── test/
│       └── java/
│           └── com/
│               └── secure/
│                   └── ...               # Unit and integration tests
├── pom.xml
├── Dockerfile
├── README.md
```