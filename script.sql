-- 1. Nationalite
CREATE TABLE nationalite (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

-- 2. Situation_familiale
CREATE TABLE situation_familiale (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

-- 3. type_visa
CREATE TABLE type_visa (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL  -- 'investisseur', 'travailleur'
);

-- 4. Demandeur (references nationalite and situation_familiale)
CREATE TABLE demandeur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    date_naissance DATE NOT NULL,
    lieu_naissance VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    adresse TEXT NOT NULL,
    id_situation_familiale INT NOT NULL,
    id_nationalite INT NOT NULL,
    FOREIGN KEY (id_situation_familiale) REFERENCES situation_familiale(id),
    FOREIGN KEY (id_nationalite) REFERENCES nationalite(id)
);

-- 5. Passeport (references demandeur)
CREATE TABLE passeport (
    id SERIAL PRIMARY KEY,
    id_demandeur INT NOT NULL,
    numero_passeport VARCHAR(50) NOT NULL UNIQUE,
    date_delivrance DATE NOT NULL,
    date_expiration DATE NOT NULL,
    pays_delivrance VARCHAR(50),
    type_passeport VARCHAR(50) DEFAULT 'Ordinaire'
);

-- 4. Demande de visa (étape administrative)
CREATE TABLE demande_visa (
    id_demande SERIAL PRIMARY KEY,
    id_personne INT NOT NULL REFERENCES personne(id_personne),
    type_demande VARCHAR(20) NOT NULL CHECK (type_demande IN ('transformable', 'non_transformable')),
    motif VARCHAR(50) CHECK (motif IN ('travailleur','investisseur','retraite','regroupement_familial','stage_mission')),
    date_demande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut_demande VARCHAR(30) DEFAULT 'dossier crée'
);

-- 5. Visa délivré (résultat de la demande)
CREATE TABLE visa (
    id_visa SERIAL PRIMARY KEY,
    id_personne INT NOT NULL REFERENCES personne(id_personne),
    id_demande INT NOT NULL REFERENCES demande_visa(id_demande),
    type_visa VARCHAR(20) NOT NULL CHECK (type_visa IN ('transformable','non_transformable','travailleur','etudiant')),
    date_emission DATE NOT NULL DEFAULT CURRENT_DATE,
    date_expiration DATE NOT NULL,
    est_transformable BOOLEAN DEFAULT FALSE,
    reference_visa VARCHAR(100) UNIQUE NOT NULL
);