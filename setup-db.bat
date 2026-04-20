@echo off
REM Database Setup Script
REM This script creates the visadb database and initializes tables

echo ===========================
echo Visa BackOffice DB Setup
echo ===========================
echo.

REM Check if psql is available
where psql >nul 2>nul
if errorlevel 1 (
    echo ERROR: psql not found in PATH
    echo Please add PostgreSQL\bin to your system PATH
    pause
    exit /b 1
)

echo [1/4] Creating database visadb...
psql -U postgres -h 127.0.0.1 -c "CREATE DATABASE IF NOT EXISTS visadb;" >nul 2>&1
if errorlevel 1 (
    echo ERROR: Failed to create database
    echo Make sure PostgreSQL is running and credentials are correct
    pause
    exit /b 1
)

echo [2/4] Creating schema and tables...
psql -U postgres -h 127.0.0.1 -d visadb -f "%~dp0script.sql" >nul 2>&1
if errorlevel 1 (
    echo WARNING: Error running script.sql (it might already exist)
)

echo [3/4] Creating indexes...
psql -U postgres -h 127.0.0.1 -d visadb -c "CREATE INDEX IF NOT EXISTS idx_demande_visa_personne ON demande_visa(id_personne);" >nul 2>&1
psql -U postgres -h 127.0.0.1 -d visadb -c "CREATE INDEX IF NOT EXISTS idx_visa_personne ON visa(id_personne);" >nul 2>&1
psql -U postgres -h 127.0.0.1 -d visadb -c "CREATE INDEX IF NOT EXISTS idx_visa_demande ON visa(id_demande);" >nul 2>&1

echo [4/4] Inserting sample data...
psql -U postgres -h 127.0.0.1 -d visadb -f "%~dp0src\main\resources\db\init.sql" >nul 2>&1
if errorlevel 1 (
    echo WARNING: Some sample data might not have inserted (tables might be empty)
)

echo.
echo ===========================
echo Setup Complete!
echo ===========================
echo.
echo Next step: Run 'mvn spring-boot:run'
echo Then open: http://localhost:8020/backoffice/visas
echo.
pause
