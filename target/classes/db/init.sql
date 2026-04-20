-- Initial database setup for Visa Management System
-- Run this script with superuser privileges (postgres or equivalent)

-- Step 1: Create database if it does not exist
CREATE DATABASE IF NOT EXISTS visadb;

-- Step 2: Connect to visadb and create schema
-- Note: After running this, reconnect to visadb database before running the rest

-- Step 3: Create tables (from script.sql)
CREATE TABLE IF NOT EXISTS personne (
    id_personne SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE NOT NULL,
    nationalite VARCHAR(50),
    numero_passeport VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(150),
    telephone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS demande_visa (
    id_demande SERIAL PRIMARY KEY,
    id_personne INT NOT NULL REFERENCES personne(id_personne),
    type_demande VARCHAR(20) NOT NULL CHECK (type_demande IN ('transformable', 'non_transformable')),
    motif VARCHAR(50) CHECK (motif IN ('travailleur','investisseur','retraite','regroupement_familial','stage_mission')),
    date_demande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut_demande VARCHAR(30) DEFAULT 'en_attente'
);

CREATE TABLE IF NOT EXISTS visa (
    id_visa SERIAL PRIMARY KEY,
    id_personne INT NOT NULL REFERENCES personne(id_personne),
    id_demande INT NOT NULL REFERENCES demande_visa(id_demande),
    type_visa VARCHAR(20) NOT NULL CHECK (type_visa IN ('transformable','non_transformable','travailleur','etudiant')),
    date_emission DATE NOT NULL DEFAULT CURRENT_DATE,
    date_expiration DATE NOT NULL,
    est_transformable BOOLEAN DEFAULT FALSE,
    reference_visa VARCHAR(100) UNIQUE NOT NULL
);

-- Step 4: Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_demande_visa_personne ON demande_visa(id_personne);
CREATE INDEX IF NOT EXISTS idx_visa_personne ON visa(id_personne);
CREATE INDEX IF NOT EXISTS idx_visa_demande ON visa(id_demande);

-- Step 5: Insert sample data (optional)
INSERT INTO personne (nom, prenom, date_naissance, nationalite, numero_passeport, email, telephone)
VALUES 
    ('Dupont', 'Jean', '1990-05-15', 'Française', 'AA123456', 'jean.dupont@email.com', '+33612345678'),
    ('Martin', 'Marie', '1992-08-22', 'Française', 'BB789012', 'marie.martin@email.com', '+33687654321')
ON CONFLICT (numero_passeport) DO NOTHING;

INSERT INTO demande_visa (id_personne, type_demande, motif, statut_demande)
SELECT id_personne, 'transformable', 'travailleur', 'en_attente'
FROM personne WHERE numero_passeport = 'AA123456'
ON CONFLICT DO NOTHING;

INSERT INTO visa (id_personne, id_demande, type_visa, date_emission, date_expiration, est_transformable, reference_visa)
SELECT p.id_personne, d.id_demande, 'travailleur', CURRENT_DATE, CURRENT_DATE + INTERVAL '365 days', true, 'VISA-' || p.id_personne || '-2026'
FROM demande_visa d
JOIN personne p ON d.id_personne = p.id_personne
WHERE p.numero_passeport = 'AA123456'
ON CONFLICT (reference_visa) DO NOTHING;
