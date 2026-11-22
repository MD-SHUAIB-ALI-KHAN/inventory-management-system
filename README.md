# 📦 Inventory Management System

A full-stack inventory app (e.g. track medicine purchases like *Dolo 650*) with login, REST API and a simple web UI.

## Tech Stack
Java 21 · Spring Boot 3 · Spring Data JPA (Hibernate) · Spring Security · MySQL · REST API · HTML · CSS · JavaScript · JSON · Maven · JUnit 5 + Mockito

## Features
- Add / view / edit / delete items (full CRUD) and search by name
- Login & logout (Spring Security, BCrypt-hashed password)
- Bean validation + global exception handler (clean JSON errors: 400 / 404 / 500)
- Business rule: expiry date cannot be before purchase date
- Dashboard stats: item count, total quantity, stock value, low-stock count
- Low stock (<10) shown in red, expired items highlighted
- Unit tests for the service layer

## Architecture
```
Browser (HTML/CSS/JS fetch) → Controller → Service → Repository (Spring Data JPA) → MySQL
                                   ↑ GlobalExceptionHandler   ↑ Spring Security filter
```

## Project Structure
```
inventory/src/main/java/com/shuaib/inventory
├── InventoryApplication.java      # entry point
├── Item.java                      # JPA entity + validation
├── ItemRepository.java            # Spring Data JPA
├── ItemService.java               # business logic
├── ItemController.java            # REST endpoints
├── GlobalExceptionHandler.java    # error handling
├── ResourceNotFoundException.java
└── SecurityConfig.java            # login/logout, access rules
inventory/src/main/resources/static  # index.html, login.html, style.css, app.js
inventory/src/test/java/...          # ItemServiceTest
```

## Setup
1. Install JDK 21 and Maven. Start MySQL (XAMPP works).
2. Edit `inventory/src/main/resources/application.properties` → DB port, username, password.
   The database `inventory_db` is created automatically.
3. Run:
   ```
   cd inventory
   mvn spring-boot:run
   ```
4. Open http://localhost:8080 and log in: **admin / admin123**
5. Run tests: `mvn test`

## REST API (login required)
| Method | URL | Description |
|---|---|---|
| GET | `/api/items` | list all (`?search=dolo` to filter) |
| GET | `/api/items/{id}` | get one |
| POST | `/api/items` | create (201) |
| PUT | `/api/items/{id}` | update |
| DELETE | `/api/items/{id}` | delete (204) |

Sample JSON:
```json
{"name":"Dolo 650","category":"Medicine","batchNo":"B123","quantity":100,"price":30.5,
 "supplier":"MedPlus","purchaseDate":"2026-11-21","expiryDate":"2028-11-30"}
```

Error example (400): `{"status":400,"message":"Validation failed","errors":{"name":"Name is required"}}`

## Screenshots
_Add screenshots here (login page, dashboard, phpMyAdmin table)._

## Author
Md Shuaib Ali Khan — B.Tech CSE, BSA Crescent Institute of Science and Technology
