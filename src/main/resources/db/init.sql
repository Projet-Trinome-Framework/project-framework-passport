-- Initial database setup for Visa Management System
-- Run this script after creating the database and connecting to it.

-- Step 1: Create database manually if needed
-- CREATE DATABASE visadb;
-- 
-- Then connect to visadb and run this script.

-- Step 2: Create tables
CREATE TABLE IF NOT EXISTS personne (
    id_personne SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE NOT NULL,
    nationalite VARCHAR(50),
    email VARCHAR(150),
    telephone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS situation_familiale (
    id_situation_familiale SERIAL PRIMARY KEY,
    id_personne INT NOT NULL UNIQUE REFERENCES personne(id_personne),
    statut_familial VARCHAR(50) NOT NULL CHECK (statut_familial IN ('célibataire','marié','divorcé','veuf','pacsé')),
    adresse VARCHAR(255),
    nb_enfants INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS passeport (
    id_passeport SERIAL PRIMARY KEY,
    id_personne INT NOT NULL UNIQUE REFERENCES personne(id_personne),
    numero_passeport VARCHAR(50) UNIQUE NOT NULL,
    date_delivrance DATE NOT NULL,
    date_expiration DATE NOT NULL,
    pays_delivrance VARCHAR(50),
    type_passeport VARCHAR(50) DEFAULT 'Ordinaire'
);

CREATE TABLE IF NOT EXISTS demande_visa (
    id_demande SERIAL PRIMARY KEY,
    id_personne INT NOT NULL REFERENCES personne(id_personne),
    type_demande VARCHAR(20) NOT NULL CHECK (type_demande IN ('transformable', 'non_transformable')),
    motif VARCHAR(50) CHECK (motif IN ('travailleur','investisseur','retraite','regroupement_familial','stage_mission')),
    date_demande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut_demande VARCHAR(30) DEFAULT 'dossier crée'
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

-- Step 3: Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_demande_visa_personne ON demande_visa(id_personne);
CREATE INDEX IF NOT EXISTS idx_visa_personne ON visa(id_personne);
CREATE INDEX IF NOT EXISTS idx_visa_demande ON visa(id_demande);
CREATE INDEX IF NOT EXISTS idx_passeport_personne ON passeport(id_personne);
CREATE INDEX IF NOT EXISTS idx_situation_familiale_personne ON situation_familiale(id_personne);

-- Step 4: Insert sample data (optional)
INSERT INTO personne (nom, prenom, date_naissance, nationalite, email, telephone)
SELECT nom, prenom, date_naissance, nationalite, email, telephone
FROM (VALUES
    ('Dupont', 'Jean', '1990-05-15', 'Française', 'jean.dupont@email.com', '+33612345678'),
    ('Martin', 'Marie', '1992-08-22', 'Française', 'marie.martin@email.com', '+33687654321')
) AS vals(nom, prenom, date_naissance, nationalite, email, telephone)
WHERE NOT EXISTS (
    SELECT 1
    FROM personne p
    WHERE p.nom = vals.nom
      AND p.prenom = vals.prenom
      AND p.date_naissance = vals.date_naissance
);

INSERT INTO situation_familiale (id_personne, statut_familial, adresse, nb_enfants)
SELECT id_personne, 'célibataire', 'Paris, France', 0
FROM personne
WHERE nom = 'Dupont' AND prenom = 'Jean'
ON CONFLICT DO NOTHING;

INSERT INTO situation_familiale (id_personne, statut_familial, adresse, nb_enfants)
SELECT id_personne, 'marié', 'Lyon, France', 1
FROM personne
WHERE nom = 'Martin' AND prenom = 'Marie'
ON CONFLICT DO NOTHING;

INSERT INTO passeport (id_personne, numero_passeport, date_delivrance, date_expiration, pays_delivrance, type_passeport)
SELECT id_personne, 'AA123456', '2018-05-15', '2028-05-14', 'France', 'Ordinaire'
FROM personne
WHERE nom = 'Dupont' AND prenom = 'Jean'
ON CONFLICT DO NOTHING;

INSERT INTO passeport (id_personne, numero_passeport, date_delivrance, date_expiration, pays_delivrance, type_passeport)
SELECT id_personne, 'BB789012', '2019-08-22', '2029-08-21', 'France', 'Ordinaire'
FROM personne
WHERE nom = 'Martin' AND prenom = 'Marie'
ON CONFLICT DO NOTHING;

INSERT INTO demande_visa (id_personne, type_demande, motif, statut_demande)
SELECT id_personne, 'transformable', 'travailleur', 'dossier crée'
FROM personne
WHERE nom = 'Dupont' AND prenom = 'Jean'
  AND NOT EXISTS (
    SELECT 1
    FROM demande_visa dv
    JOIN personne p ON dv.id_personne = p.id_personne
    WHERE p.nom = 'Dupont'
      AND p.prenom = 'Jean'
      AND dv.type_demande = 'transformable'
      AND dv.motif = 'travailleur'
  );

INSERT INTO visa (id_personne, id_demande, type_visa, date_emission, date_expiration, est_transformable, reference_visa)
SELECT p.id_personne, d.id_demande, 'travailleur', CURRENT_DATE, CURRENT_DATE + INTERVAL '365 days', true, 'VISA-' || p.id_personne || '-2026'
FROM demande_visa d
JOIN personne p ON d.id_personne = p.id_personne
WHERE p.nom = 'Dupont' AND p.prenom = 'Jean'
ON CONFLICT (reference_visa) DO NOTHING;
