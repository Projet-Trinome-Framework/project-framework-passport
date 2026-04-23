-- Insertion des pièces justificatives pour le formulaire de demande de visa
-- Supporting documents insertion for visa request form

-- Documents communs (Common Documents)
INSERT INTO piece_justificative (libelle, description, obligatoire, categorie) VALUES
('photos_identite', '02 photos d''identité (2 identity photos)', TRUE, 'commun'),
('notice_renseignement', 'Notice de renseignement (Information form)', TRUE, 'commun'),
('demande_ministere', 'Demande adressée à Mr le Ministère de l''Intérieur et de la Décentralisation avec adresse e-mail et numéro téléphone portable', TRUE, 'commun'),
('visa_certifie', 'Photocopie certifiée du visa en cours de validité (Certified photocopy of valid visa)', TRUE, 'commun'),
('passeport_certifie', 'Photocopie certifiée de la première page du passeport (Certified photocopy of the first page of the passport)', TRUE, 'commun'),
('carte_resident_certifie', 'Photocopie certifiée de la carte résident en cours de validité (Certified photocopy of valid resident card)', TRUE, 'commun'),
('certificat_residence', 'Certificat de résidence à Madagascar (Certificate of residence in Madagascar)', TRUE, 'commun'),
('casier_judiciaire', 'Extrait de casier judiciaire moins de 3 mois (Criminal record extract less than 3 months old)', TRUE, 'commun');

-- Documents spécifiques aux investisseurs (Investor-specific Documents)
INSERT INTO piece_justificative (libelle, description, obligatoire, categorie) VALUES
('statut_societe', 'Statut de la Société (Company status)', TRUE, 'investisseur'),
('extrait_registre_commerce', 'Extrait d''inscription au registre de commerce (Extract from the commercial register)', TRUE, 'investisseur'),
('carte_fiscale', 'Carte fiscale (Tax card)', TRUE, 'investisseur');

-- Documents spécifiques aux travailleurs (Worker-specific Documents)
INSERT INTO piece_justificative (libelle, description, obligatoire, categorie) VALUES
('autorisation_emploi', 'Autorisation emploi délivrée à Madagascar par le de la Fonction publique (Work permit issued in Madagascar by the Civil Service)', TRUE, 'travailleur'),
('attestation_emploi', 'Attestation d''emploi délivré par l''employeur (Origin) (Employment certificate issued by the employer (Original))', TRUE, 'travailleur');

-- Documents spécifiques aux étudiants (Student-specific Documents) - pour utilisation future
INSERT INTO piece_justificative (libelle, description, obligatoire, categorie) VALUES
('certificat_scolarite', 'Certificat de scolarité (School certificate)', TRUE, 'etudiant'),
('attestation_ressources', 'Attestation de ressources financières (Financial resources certificate)', TRUE, 'etudiant'),
('assurance_etudiant', 'Assurance maladie étudiante (Student health insurance)', TRUE, 'etudiant');

-- Documents spécifiques au regroupement familial (Family Reunion-specific Documents) - pour utilisation future
INSERT INTO piece_justificative (libelle, description, obligatoire, categorie) VALUES
('acte_mariage', 'Acte de mariage (Marriage certificate)', TRUE, 'regroupement_familial'),
('acte_naissance_enfants', 'Actes de naissance des enfants (Children birth certificates)', TRUE, 'regroupement_familial'),
('attestation_logement', 'Attestation d''hébergement (Accommodation certificate)', TRUE, 'regroupement_familial'),
('justificatif_ressources', 'Justificatif de ressources suffisantes (Proof of sufficient resources)', TRUE, 'regroupement_familial');
