# projet-trinome-framework

## Spring Boot BackOffice Structure

This project now includes a Spring Boot back-office module for managing visa requests and displaying visa information with a professional dashboard.

### Recommended folder structure

- `pom.xml`
- `script.sql` — PostgreSQL schema for `personne`, `demande_visa`, and `visa`
- `src/main/java/fr/project/backoffice`
  - `BackofficeApplication.java` — application entry point
  - `controller/VisaController.java` — dashboard endpoint
  - `service/VisaService.java` — business layer for visa aggregation
  - `repository/` — JPA repositories for each entity
  - `entity/` — JPA entities matching the PostgreSQL tables
  - `dto/VisaInfoDto.java` — data transfer object for dashboard display
- `src/main/resources/application.yml` — database configuration and Spring settings
- `src/main/resources/templates/visa-dashboard.html` — Thymeleaf dashboard view
- `src/main/resources/static/css/backoffice.css` — dashboard styling

## Database integration

Use the provided `script.sql` to create the PostgreSQL tables. The Spring Boot application is configured for PostgreSQL in `application.yml`.

Example database configuration:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/visadb
    username: postgres
    password: postgres
```

## Dashboard route

Start the application and visit:

- `http://localhost:8080/backoffice/visas`

This page shows:

- total visas
- pending requests
- visas expiring within 30 days
- transformable visas
- detailed visa records with applicant and passport data

## Setup & Run Instructions

### 1. Create and Initialize the Database

**Windows users:**
```bash
.\setup-db.bat
```

**Manual setup (all platforms):**
```bash
# Create database
psql -U postgres -h 127.0.0.1 -c "CREATE DATABASE IF NOT EXISTS visadb;"

# Initialize schema
psql -U postgres -h 127.0.0.1 -d visadb -f script.sql

# Add sample data
psql -U postgres -h 127.0.0.1 -d visadb -f src/main/resources/db/init.sql
```

### 2. Run the Application

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8020**

### 3. Access the Dashboard

Open your browser and navigate to:
```
http://localhost:8020/backoffice/visas
```

---

## Connection Details

- **Host:** localhost
- **Port:** 5432
- **Database:** visadb
- **User:** postgres
- **Password:** postgres

If you need different credentials, edit `src/main/resources/application.yml`

---

## Troubleshooting

For detailed setup troubleshooting, see [DATABASE_SETUP.md](DATABASE_SETUP.md)
