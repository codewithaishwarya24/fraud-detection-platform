# Fraud Detection Platform
### Spring Boot Microservices (Java 17 + PostgreSQL + IntelliJ) — Local Setup Guide

---

## Overview

The **Fraud Detection Platform** is a microservices-based Spring Boot project that allows issuers to view and analyze transactions for fraud detection.  
This setup runs **completely locally (no Docker required)** using:

- ☕ Java 17 (Temurin)
- ⚙️ Spring Boot 3
- 🐘 PostgreSQL 18 (port `5433`)
- 💻 IntelliJ IDEA Community Edition

Each microservice runs independently:

| Service | Description |
|----------|--------------|
| 💳 **transaction-service** | Handles and displays transaction data |
| 🏪 **merchant-service** | Manages merchant details |
| 🌐 **api-gateway** | Routes API traffic to other services |

---

## 1. Required Installations

Install these tools before setting up the project:

| Tool | Version | Purpose | Download |
|------|----------|----------|-----------|
| **Java (JDK)** | 17 (Temurin) | Required for Spring Boot | [https://adoptium.net](https://adoptium.net) |
| **Maven** | 3.8+ | Build automation | [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi) |
| **PostgreSQL** | 18 | Database | [https://www.postgresql.org/download](https://www.postgresql.org/download) |
|**IntelliJ IDEA** | Community Edition | IDE | [https://www.jetbrains.com/idea/download](https://www.jetbrains.com/idea/download) |
| **Git** | latest | Clone / version control | [https://git-scm.com/downloads](https://git-scm.com/downloads) |

---

## 2. Verify Installations

After installing everything, open a terminal or PowerShell and run:

```bash
java -version
mvn -version
psql --version
git --version
```

You should see version details for each command.

---

## 3. PostgreSQL Setup (Port 5433)

### Step 1: Start pgAdmin 4
- Connect to your local PostgreSQL 18 server (port `5433`) using your `postgres` superuser credentials.

### Step 2: Create the database
1. In pgAdmin, right-click **Databases → Create → Database**
2. Enter:
    - **Database name:** `frauddb`
    - **Owner:** `postgres`
3. Click **Save**

### Step 3: Create the application user
1. Expand **Login/Group Roles** → right-click → **Create → Login/Group Role**
2. Fill in:
    - **Role name:** `fraud_user`
    - **Password:** `fraud_pass`
3. Under **Privileges**, enable:
    - ✅ Can Login
    - ✅ Create DB
4. Click **Save**

### Step 4: Grant privileges to the user
1. Right-click `frauddb → Properties → Security tab`
2. Add a privilege:
    - **Role:** `fraud_user`
    - **Privileges:** CONNECT, TEMP, CREATE
3. Click **Save**

### Step 5: (Optional) Change database owner
- Right-click `frauddb → Properties → Owner` → select `fraud_user`.

✅ Database `frauddb` and user `fraud_user` are ready for the app.

---

## 4. Clone the Project

In your terminal or Git Bash:

```bash
git clone https://github.com/<your-org>/fraud-detection-platform.git
cd fraud-detection-platform
```

---

## 5. Verify Database Configuration

Open these files:
- `transaction-service/src/main/resources/application.yml`
- `merchant-service/src/main/resources/application.yml`

Make sure both have this configuration:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/frauddb
    username: fraud_user
    password: fraud_pass
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

The API Gateway doesn’t need DB settings.

---

## 6. Open in IntelliJ

1. Launch **IntelliJ IDEA**
2. Click **Open** → choose the folder `fraud-detection-platform/`
3. IntelliJ will detect and import Maven modules automatically:
   ```
   api-gateway
   merchant-service
   transaction-service
   ```
4. Go to:
   **File → Project Structure → Project**
    - Project SDK → Java 17
    - Language Level → 17
5. Wait for indexing and dependencies to finish loading.

---

## 7. Build the Project

In IntelliJ terminal (or Git Bash):

```bash
mvn clean package -DskipTests
```

Expected message:
```
BUILD SUCCESS
```

---

## 8. Run Each Microservice

Run services one by one — either using IntelliJ’s Run button or the commands below.

### ▶ Transaction Service
```bash
cd transaction-service
mvn spring-boot:run
```

### ▶ Merchant Service
```bash
cd merchant-service
mvn spring-boot:run
```

### ▶ API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

---

## 9. Verify Services are Running

Once all are started, check the following URLs in your browser or Postman:

| Service | URL | Expected Output |
|----------|------|----------------|
| Merchant Service | [http://localhost:8082/api/merchants/health](http://localhost:8082/api/merchants/health) | `Merchant service running OK` |
| Transaction Service | [http://localhost:8081/api/transactions/health](http://localhost:8081/api/transactions/health) | `Transaction service running OK` |
| Gateway → Transaction | [http://localhost:8080/api/transactions/health](http://localhost:8080/api/transactions/health) | `Transaction service running OK` |
| Gateway → Merchant | [http://localhost:8080/api/merchants/health](http://localhost:8080/api/merchants/health) | `Merchant service running OK` |

---