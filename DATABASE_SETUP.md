# Database Setup Guide

## Prerequisites

- PostgreSQL 12+ installed and running
- Default PostgreSQL port: 5432
- Default credentials: `username: postgres`

## Setup Steps

### 1. Verify PostgreSQL is Running

**Windows:**
```powershell
Get-Service postgresql-x64-* | Select-Object Name, Status
```

If not running, start it:
```powershell
Start-Service -Name postgresql-x64-17  # Update version number as needed
```

### 2. Create the Database

Open **pgAdmin** or use command line:

**Command line (psql):**
```bash
psql -U postgres -c "CREATE DATABASE visadb;"
```

If you get a connection error, try:
```bash
psql -U postgres -h 127.0.0.1 -p 5432 -c "CREATE DATABASE visadb;"
```

### 3. Create Tables and Schema

Using the init script:

```bash
psql -U postgres -d visadb -f src/main/resources/db/init.sql
```

Or import the original schema:

```bash
psql -U postgres -d visadb -f script.sql
```

### 4. Verify the Setup

```bash
psql -U postgres -d visadb -c "\dt"
```

You should see:
```
           List of relations
 Schema |     Name      | Type  |  Owner
--------+---------------+-------+----------
 public | demande_visa  | table | postgres
 public | personne      | table | postgres
 public | visa          | table | postgres
```

### 5. Run the Spring Boot Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8020`

Access the visa dashboard:
- **URL:** `http://localhost:8020/backoffice/visas`

---

## Troubleshooting

### Error: "Connection refused"

1. Check PostgreSQL is running:
   ```powershell
   Get-Service postgresql-x64-* | Select-Object Status
   ```

2. Verify the port (default 5432):
   ```bash
   psql -U postgres -h 127.0.0.1 -c "SELECT version();"
   ```

3. If port is different, update `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:YOUR_PORT/visadb
   ```

### Error: "database 'visadb' does not exist"

Create it first:
```bash
psql -U postgres -c "CREATE DATABASE visadb;"
```

### Error: "table 'personne' does not exist"

Run the initialization script:
```bash
psql -U postgres -d visadb -f src/main/resources/db/init.sql
```

### Error: "password authentication failed"

Update credentials in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    username: your_postgres_user
    password: your_postgres_password
```

---

## Configuration Details

**Current settings** (`application.yml`):
- **Host:** localhost
- **Port:** 5432
- **Database:** visadb
- **User:** postgres
- **Hibernate DDL:** validate (checks schema without modifying)
- **Connection Pool:** HikariCP (max 5 connections)

---

## Development Tips

- To automatically create tables on startup, change in `application.yml`:
  ```yaml
  jpa:
    hibernate:
      ddl-auto: update  # or 'create' for fresh schema
  ```

- To add sample data, run:
  ```bash
  psql -U postgres -d visadb << 'EOF'
  INSERT INTO personne VALUES (...);
  EOF
  ```

- Monitor SQL queries by enabling logs:
  ```yaml
  spring:
    jpa:
      show-sql: true
  ```
