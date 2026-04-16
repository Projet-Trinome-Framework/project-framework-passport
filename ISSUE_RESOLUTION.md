# Issue Resolution: PostgreSQL Connection Error

## Problem

The Spring Boot application was failing to start with the following error:

```
java.sql.SQLException: Cannot create a database connection of type class org.postgresql.jdbc.PgConnection
org.postgresql.util.PSQLException: Connection to localhost:5432 refused
```

## Root Causes

1. **Database didn't exist:** The application was configured to connect to database `visadb` which hadn't been created
2. **Tables not initialized:** Even after database creation, the required tables (personne, demande_visa, visa) weren't created
3. **Incorrect DDL configuration:** The original `ddl-auto: none` meant Hibernate wouldn't help with schema initialization
4. **Connection pool issues:** Missing HikariCP configuration for proper connection management
5. **Port mismatch:** Original config used port 5433, but PostgreSQL runs on 5432 by default

---

## Solutions Implemented

### 1. Updated Configuration (application.yml)

| Change | Impact |
|--------|---------|
| Changed port from 5433 → 5432 | Matches default PostgreSQL port |
| Added HikariCP settings | Proper connection pooling (max 5 connections) |
| Set `ddl-auto: validate` | Validates schema without modifying it |
| Increased connection timeout | 20 second timeout for slow connections |
| Removed deprecated `dialect` property | PostgreSQL dialect is auto-detected |

### 2. Added PostgreSQL Driver Dependency

Added `HikariCP` as explicit dependency for improved connection management:
```xml
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
</dependency>
```

### 3. Created Database Initialization Script

New file: `src/main/resources/db/init.sql`
- Creates database structure
- Adds sample data
- Creates performance indexes

### 4. Created Automated Setup Script

New file: `setup-db.bat` (Windows)
- Automates database creation
- Initializes schema with single command
- Verifies PostgreSQL connectivity

### 5. Created Comprehensive Documentation

New file: `DATABASE_SETUP.md`
- Step-by-step setup guide
- Troubleshooting section
- Port verification commands
- Configuration reference

---

## How to Fix Your Setup

### Option 1: Automated Setup (Windows)

```powershell
.\setup-db.bat
mvn spring-boot:run
```

### Option 2: Manual Setup

```bash
# 1. Create database
psql -U postgres -c "CREATE DATABASE visadb;"

# 2. Initialize schema
psql -U postgres -d visadb -f script.sql

# 3. Add sample data
psql -U postgres -d visadb -f src/main/resources/db/init.sql

# 4. Run application
mvn spring-boot:run
```

### Option 3: Clean Start with DDL Auto

If you want Spring Boot to create tables automatically:

Edit `application.yml`:
```yaml
jpa:
  hibernate:
    ddl-auto: update  # Creates missing tables automatically
```

Then run: `mvn spring-boot:run`

---

## Files Changed

```
✓ pom.xml
  └─ Added HikariCP dependency

✓ src/main/resources/application.yml
  └─ Fixed port (5432)
  └─ Added HikariCP config
  └─ Fixed Hibernate dialect
  └─ Set ddl-auto: validate

✓ src/main/resources/db/init.sql (NEW)
  └─ Database initialization script

✓ setup-db.bat (NEW)
  └─ Automated Windows setup

✓ DATABASE_SETUP.md (NEW)
  └─ Complete setup guide

✓ README.md
  └─ Updated with setup instructions
```

---

## Verification Checklist

After following the setup steps:

- [ ] Database `visadb` exists
- [ ] Tables `personne`, `demande_visa`, `visa` exist
- [ ] Application starts without connection errors
- [ ] Dashboard accessible at `http://localhost:8020/backoffice/visas`
- [ ] Sample data displays in the table

---

## Configuration Reference

### application.yml - Final Configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/visadb
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 5
      minimum-idle: 2
      connection-timeout: 20000
  jpa:
    hibernate:
      ddl-auto: validate  # validate, update, or create
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: false
server:
  port: 8020
```

### PostgreSQL Defaults

- **Host:** localhost
- **Port:** 5432
- **Admin User:** postgres
- **Default Password:** Set during PostgreSQL installation

---

## Performance Notes

The new configuration includes:

- **HikariCP Connection Pool:** 5 max connections, 2 minimum idle
- **Connection Timeout:** 20 seconds (handles slow starts)
- **Batch Size:** 20 (optimizes SQL batch operations)
- **SQL Formatting:** Enabled for debugging

---

## Next Steps

1. Run setup script or manual SQL commands
2. Start application: `mvn spring-boot:run`
3. Access dashboard: `http://localhost:8020/backoffice/visas`
4. Create admin users and manage visa data

For additional features like REST API, authentication, or advanced reporting, see the main README.
