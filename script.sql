-- 1. Personne (demandeur)
CREATE TABLE personne (
    id_personne SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE NOT NULL,
    nationalite VARCHAR(50),
    numero_passeport VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(150),
    telephone VARCHAR(50)
);

-- 2. Demande de visa (étape administrative)
CREATE TABLE demande_visa (
    id_demande SERIAL PRIMARY KEY,
    id_personne INT NOT NULL REFERENCES personne(id_personne),
    type_demande VARCHAR(20) NOT NULL CHECK (type_demande IN ('transformable', 'non_transformable')),
    motif VARCHAR(50) CHECK (motif IN ('travailleur','investisseur','retraite','regroupement_familial','stage_mission')),
    date_demande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut_demande VARCHAR(30) DEFAULT 'en_attente'
);

-- 3. Visa délivré (résultat de la demande)
CREATE TABLE visa (
    id_visa SERIAL PRIMARY KEY,
    id_personne INT NOT NULL REFERENCES personne(id_personne),
    id_demande INT NOT NULL REFERENCES demande_visa(id_demande),
    type_visa VARCHAR(20) NOT NULL CHECK (type_visa IN ('transformable','non_transformable','travailleur','etudiant')),
    date_emission DATE NOT NULL DEFAULT CURRENT_DATE,
    date_expiration DATE NOT NULL,
    est_transformable BOOLEAN DEFAULT FALSE
);