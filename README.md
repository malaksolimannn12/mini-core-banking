# Mini Core Banking System

Spring Boot skeleton for the Core Banking T24 application project (Customer, Account, Deposit, Withdraw, Transfer).

## Day 1 goal
Get this running with zero errors before touching the database.

## Run it (Linux)

```bash
java -version        # confirm JDK 17+
cd mini-core-banking
mvn clean install
mvn spring-boot:run
```

Then check it's alive:

```bash
curl http://localhost:8080/health
```

Expected response:
```json
{"status":"UP","service":"mini-core-banking"}
```

## If it fails to start with a DB connection error
You haven't installed/started MySQL yet — that's expected before Day 2. Either:
1. Install MySQL and create a `bank` database (`CREATE DATABASE bank;`), or
2. Temporarily uncomment the `spring.autoconfigure.exclude=...` line in
   `src/main/resources/application.properties` to boot without a datasource,
   then remove it once MySQL is ready.

## Structure
```
src/main/java/com/bank/minicorebanking/
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── exception
 └── config
```

## Next steps (per the study plan)
- Day 2: connect MySQL/Oracle, create `Customer` entity + table
- Day 3: `CustomerRepository`
- Day 4: first REST API (`POST /customers`), test in Postman
- Day 5: `Account` entity + CRUD
- Day 6: Deposit / Withdraw
- Day 7+: Transfer, Transaction history, validation, global exception handling
