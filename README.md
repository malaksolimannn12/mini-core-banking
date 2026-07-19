# Mini Core Banking System

A backend REST API simulating core banking operations, built as hands-on preparation for a Core Banking (T24) role.

## Tech Stack

- Java 17
- Spring Boot 3.3.2
- Spring Data JPA / Hibernate
- MySQL
- Maven
- Lombok

## Features

- Customer management - full CRUD
- Account management - full CRUD, linked to customers
- Banking operations - deposit, withdraw, transfer (with balance validation)
- Transaction history - automatic logging of every operation
- Interest calculation - percentage-based balance growth
- Account statements - date-range transaction filtering
- User accounts - registration and login, with role-based design (Admin / Customer)
- Validation - Jakarta Bean Validation on all incoming data
- Global exception handling - clean, structured JSON error responses
- DTO layer - separates internal database structure from the public API

## Architecture

Controller to Service to Repository to Database

Each entity (Customer, Account, Transaction, User) follows this layered pattern, with dedicated Request/Response DTOs to control exactly what data is exposed.

## Data Model

Customer (1) - many Account (1) - many Transaction
User - optional one-to-one - Customer

## Getting Started

1. Clone the repository
2. Create a MySQL database (auto-created on first run if it doesn't exist)
3. Update src/main/resources/application.properties with your MySQL credentials
4. Run the app: mvn spring-boot:run
5. The API will be available at http://localhost:8080

## Key Endpoints

POST /customers - Create a customer
GET /customers - List all customers
POST /accounts - Create an account
POST /accounts/{id}/deposit - Deposit funds
POST /accounts/{id}/withdraw - Withdraw funds
POST /accounts/{fromId}/transfer - Transfer between accounts
POST /accounts/{id}/interest - Apply interest
GET /accounts/{id}/statement - Get transaction statement (date range)
GET /transactions/account/{id} - View full transaction history
POST /users/register - Register a new user
POST /users/login - Log in

## Roadmap

- Full test coverage (Postman)
- Password hashing / Spring Security integration
- Role-based authorization
- Oracle database migration
- JBoss/WildFly deployment