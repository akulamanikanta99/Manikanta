### Customer Rewards Service

This Spring Boot application calculates monthly and total reward points for customers based on their transaction history
using simple, rule-based logic.

### Features

Calculates reward points for each customer transaction.

Computes monthly and total reward points.

Fetch reward summary via REST APIs.

Designed to handle multiple customers and transactions.

Follows clean architecture with DTOs and services.

Includes integration with PostgreSQL and uses JPA for ORM.

### Points Calculation Logic

Transaction Amount Reward Points Logic
$0 - $50 0 points
$50 - $100 1 point per dollar over $50
$100+ 1 point/dollar from $50–$100 + 2 points/dollar above $100

### Example:

A transaction of $120 results in:

50 points for $50–$100

40 points for $20 over $100

Total = 90 points

### Tech Stack

Tech Version
Java 21
Spring Boot 3.x
PostgreSQL 15+
Maven Latest
Spring Data JPA Included
Spring Security Included
Lombok Used for boilerplate code reduction

### Environment Setup

Install Java 21, Maven, and PostgreSQL.

Configure DB:
properties

# src/main/resources/application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/assignment
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update

Run the application:
mvn clean install
mvn spring-boot:run

🔁 REST API Documentation
Base URL: http://localhost:8080/api/v1

### Customer APIs

✅ Register Customer
POST /register

### Request Body

{
"name": "Alice",
"email": "alice@example.com",
"password": "secret123"
}

### Response

Customer registered successfully

🔑 Login
POST /login?email=alice@example.com&password=secret123

### Response

Login successful

🚪 Logout
POST /logout

### Response

Logged out successfully

❌ Delete Customer
DELETE /{customerId}

### Response

Customer with ID 1 and associated transactions and reward points deleted successfully

📄 Get Customer by ID
GET /{customerId}

### Response

{
"customerId": 1,
"name": "Alice",
"email": "alice@example.com"
}

📋 Get All Customers
GET /

### Response

[
{
"customerId": 1,
"name": "Alice",
"email": "alice@example.com"
}
]

### Transaction API

➕ Add Transaction
POST /transactions

### Request Body

{
"amount": 120.0,
"spentDetails": "Grocery shopping",
"date": "2024-11-01T10:30:00",
"customer": {
"customerId": 1
}
}

### Response

Transaction processed
🏆 Rewards API
📊 Get Reward Summary
GET /customer-reward-summary/{customerId}?startDate=2024-11-01&endDate=2025-01-31

### ### Response

[
{
"id": 1,
"customerName": "Alice",
"transactions": [
{
"id": 1,
"customerId": 1,
"amount": 120.0,
"transactionDate": "2024-11-01T10:30:00"
}
],
"monthlyPoints": [
{
"year": 2024,
"month": "NOVEMBER",
"points": 90
}
]