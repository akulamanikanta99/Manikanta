#  Customer Rewards Service

A Spring Boot application that calculates **monthly** and **total reward points** for customers based on their transaction history.

##  Features

- Calculates reward points for each customer transaction
- Computes **monthly** and **total** reward points
- REST APIs to fetch reward summaries
- Supports multiple customers and transactions
- Uses clean architecture (DTOs, Services, Repositories)
- Integrated with PostgreSQL using Spring Data JPA
- Includes authentication with Spring Security


##  Points Calculation Logic

| Transaction Amount | Reward Points                    |
|--------------------|----------------------------------|
| $0 - $50           | 0 points                         |
| $50 - $100         | 1 point per $ over $50           |
| Over $100          | 1 point/$ from $50–$100 + 2/$ > $100 |

### Example:

For a transaction of `$120`:
- 50 points for the $50 between $50–$100
- 40 points for the $20 over $100  
  **Total: 90 points**


## 🛠 Tech Stack

| Technology       | Version / Usage         |
|------------------|--------------------------|
| Java             | 21                       |
| Spring Boot      | 3.x                      |
| PostgreSQL       | 15+                      |
| Spring Data JPA  | ✅ ORM                   |
| Spring Security  | ✅ Authentication         |
| Lombok           | ✅ Boilerplate reduction |
| Build Tool       | Maven (latest)           |


##  Environment Setup

### Prerequisites

- Java 21
- Maven
- PostgreSQL

###  Configure Database

**File:** `src/main/resources/application.properties`

### properties
spring.datasource.url=jdbc:postgresql://localhost:5432/assignment
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update


### Run the App
mvn clean install
mvn spring-boot:run


### REST API Endpoints
http://localhost:8080/api/v1
# Register Customer
POST /register
# Request Body
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
  }
]