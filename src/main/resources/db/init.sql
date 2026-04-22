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
    pays_delivrance VARCHAR(100),
    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id)
);

-- 6. Statut_passeport (references passeport)
CREATE TABLE statut_passeport (
    id SERIAL PRIMARY KEY,
    id_passeport INT NOT NULL,
    statut INT NOT NULL,  -- 'actif', 'expire', 'perdu', 'volee'
    date_changement_statut DATE,
    FOREIGN KEY (id_passeport) REFERENCES passeport(id)
);

-- 7. Visa_transformable (references demandeur and passeport)
CREATE TABLE visa_transformable (
    id SERIAL PRIMARY KEY,
    id_demandeur INT NOT NULL,
    id_passeport INT NOT NULL,
    numero_reference VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id),
    FOREIGN KEY (id_passeport) REFERENCES passeport(id)
);

-- 8. Type_demande
CREATE TABLE type_demande (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

-- 9. Demande (references visa_transformable, demandeur, type_visa, type_demande)
-- Note: id_statut is just an integer (no FK) as in original
CREATE TABLE demande (
    id SERIAL PRIMARY KEY,
    id_visa_transformable INT NOT NULL,
    date_demande DATE NOT NULL,
    id_statut INT NOT NULL,
    id_demandeur INT NOT NULL,
    id_type_visa INT NOT NULL,
    id_type_demande INT NOT NULL,
    date_traitement DATE,
    FOREIGN KEY (id_visa_transformable) REFERENCES visa_transformable(id),
    FOREIGN KEY (id_demandeur) REFERENCES demandeur(id),
    FOREIGN KEY (id_type_visa) REFERENCES type_visa(id),
    FOREIGN KEY (id_type_demande) REFERENCES type_demande(id)
);

-- 10. Statut_demande (references demande)
CREATE TABLE statut_demande (
    id SERIAL PRIMARY KEY,
    id_demande INT NOT NULL,
    statut INT NOT NULL,  -- 'brouillon', 'soumise', 'en_cours', 'validee', 'rejetee'
    date_changement_statut DATE,
    FOREIGN KEY (id_demande) REFERENCES demande(id)
);

-- 11. Visa (references passeport and demande)
CREATE TABLE visa (
    id SERIAL PRIMARY KEY,
    id_demande INT NOT NULL,
    reference VARCHAR(50),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    id_passeport INT NOT NULL,
    FOREIGN KEY (id_passeport) REFERENCES passeport(id),
    FOREIGN KEY (id_demande) REFERENCES demande(id)
);

-- 12. carte_resident (references demande and passeport)
CREATE TABLE carte_resident (
    id SERIAL PRIMARY KEY,
    id_demande INT NOT NULL,
    reference VARCHAR(50),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    id_passeport INT NOT NULL,
    FOREIGN KEY (id_demande) REFERENCES demande(id),
    FOREIGN KEY (id_passeport) REFERENCES passeport(id)
);