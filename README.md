This Spring Boot application calculates and tracks reward points for customers based on their transaction history. Customers earn points based on their purchases with the following rules:

2 points for every dollar spent over $100.

1 point for every dollar spent between $50 and $100.

The system processes transaction data over a three-month period, calculating monthly rewards as well as the total accumulated points for each customer.

Key Features
Dynamic Reward Calculation: Automatically calculates reward points based on transaction amounts.

Monthly and Total Point Accumulation: Tracks and reports reward points for each customer on a monthly basis, along with their total accumulated points.

Efficient Processing: Handles a large number of customer transactions without performance degradation.

Easy-to-Understand Calculation Logic: Provides clear and transparent reward point calculations for each transaction.

Reward Points Calculation Logic
The reward points for a given transaction are determined by the amount spent, following these rules:

1. Transactions Over $100
   For every dollar spent above $100, the customer earns 2 points per dollar.

2. Transactions Between $50 and $100
   For every dollar spent between $50 and $100, the customer earns 1 point per dollar.

Example Calculation
Consider a transaction where the customer spends $120:

For the first $50, the customer earns 1 point per dollar:
50 points

For the next $50, the customer earns 1 point per dollar:
50 points

For the remaining $20 (above $100), the customer earns 2 points per dollar:
40 points

Total points for the transaction = 50 + 50 + 40 = 140 points.

System Requirements
Before running the application, ensure that you have the following installed:

Java 21

Maven (for dependency management and building)

PostgreSQL (for database management)