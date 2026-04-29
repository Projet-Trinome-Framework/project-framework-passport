# Guide de Test - Fonctionnalité de Duplicata

## Prérequis

1. **Base de données PostgreSQL** en cours d'exécution
2. **Application Spring Boot** démarrée
3. **Données de test** insérées (demandeurs existants)

## Configuration de test

### 1. Insérer des données de test

Avant de tester, assurez-vous que les tables et les données de test sont présentes :

```sql
-- Vérifier que les tables existent
SELECT * FROM demandeur;
SELECT * FROM passeport;
SELECT * FROM visa;
SELECT * FROM carte_resident;
SELECT * FROM type_demande;

-- Insérer des données de test si nécessaire
INSERT INTO nationalite (libelle) VALUES ('Française'), ('Malgache'), ('Suisse');
INSERT INTO situation_familiale (libelle) VALUES ('Célibataire'), ('Marié'), ('Divorcé');

INSERT INTO type_visa (libelle) VALUES ('travailleur'), ('investisseur');

-- Insérer un demandeur de test
INSERT INTO demandeur (nom, prenom, date_naissance, lieu_naissance, telephone, email, adresse, id_situation_familiale, id_nationalite)
VALUES ('Dupont', 'Jean', '1985-03-15', 'Paris', '+33612345678', 'jean.dupont@example.com', '123 Rue de Paris, Paris', 1, 1);

-- Récupérer l'ID du demandeur
-- Supposons que l'ID est 1

-- Insérer un passeport
INSERT INTO passeport (id_demandeur, numero_passeport, date_delivrance, date_expiration, pays_delivrance)
VALUES (1, 'AB123456', '2020-01-15', '2030-01-15', 'France');

-- Récupérer l'ID du passeport
-- Supposons que l'ID est 1

-- Insérer une visa_transformable
INSERT INTO visa_transformable (id_demandeur, id_passeport, numero_reference)
VALUES (1, 1, 'VISA-REF-12345');

-- Insérer une demande
INSERT INTO demande (id_visa_transformable, date_demande, id_statut, id_demandeur, id_type_visa, id_type_demande)
VALUES (1, '2024-01-15', 1, 1, 1, 1);

-- Récupérer l'ID de la demande
-- Supposons que l'ID est 1

-- Insérer un visa
INSERT INTO visa (id_demande, reference, date_debut, date_fin, id_passeport)
VALUES (1, 'VISA-001', '2024-01-15', '2026-01-15', 1);
```

## Scénarios de test

### Scénario 1 : Transfert de visa - Applicant existant

**Objectif** : Tester le transfert de visa pour un applicant existant avec un nouveau passeport

**Étapes** :
1. Accéder à `http://localhost:8080/backoffice/visas`
2. Cliquer sur le bouton **"+ Duplicate Request"**
3. Sélectionner **"Passeport Perdu"**
4. Chercher l'applicant par email : `jean.dupont@example.com`
5. Les données de Jean Dupont doivent s'afficher (nom, prénom, passeport actuel, etc.)
6. Sélectionner **"Utiliser ces données"**
7. Entrer le numéro du nouveau passeport : `CD789012`
8. Entrer la date de délivrance du nouveau passeport : `2026-04-01`
9. Entrer la date d'expiration du nouveau passeport : `2036-04-01`
10. Sélectionner les documents requis
11. Cliquer sur **"Soumettre la demande"**

**Résultats attendus** :
- ✅ Message de succès : "Demande de transfert de visa créée avec succès ! Statut : Approuvée"
- ✅ Une nouvelle demande est créée avec le statut "Approuvée"
- ✅ Un nouveau passeport est créé
- ✅ Une nouvelle visa est créée pour le nouveau passeport avec la même date d'expiration
- ✅ Les documents sont enregistrés

### Scénario 2 : Duplicata de carte résident - Applicant existant

**Objectif** : Tester la demande de duplicata de carte résident

**Étapes** :
1. Accéder à `http://localhost:8080/backoffice/duplicates/form`
2. Sélectionner **"Carte de Résident Perdue"**
3. Chercher l'applicant par nom : `Dupont`
4. Les données doivent s'afficher
5. Cliquer sur **"Utiliser ces données"**
6. Sélectionner les documents requis
7. Cliquer sur **"Soumettre la demande"**

**Résultats attendus** :
- ✅ Message de succès : "Demande de duplicata de carte résident créée avec succès ! Statut : Approuvée"
- ✅ Une nouvelle demande est créée avec le statut "Approuvée"
- ✅ Une nouvelle carte résident est créée avec la même date d'expiration

### Scénario 3 : Transfert de visa - Nouvel applicant (from scratch)

**Objectif** : Tester le transfert de visa pour un nouvel applicant

**Étapes** :
1. Accéder à `http://localhost:8080/backoffice/duplicates/form`
2. Sélectionner **"Passeport Perdu"**
3. Chercher par email : `nonexistent@example.com` (qui n'existe pas)
4. Sélectionner **"Remplir les données from scratch"**
5. Remplir tous les champs obligatoires :
   - Nom : `Martin`
   - Prénom : `Pierre`
   - Date de naissance : `1990-06-20`
   - Lieu de naissance : `Lyon`
   - Nationalité : `Française`
   - Email : `pierre.martin@example.com`
   - Téléphone : `+33698765432`
   - Situation familiale : `Marié`
   - Adresse : `456 Rue de Lyon, Lyon`
6. Remplir le passeport perdu :
   - Numéro du passeport : `EF345678`
   - Date de délivrance : `2019-05-10`
   - Date d'expiration : `2029-05-10`
   - Pays de délivrance : `France`
7. Remplir le nouveau passeport :
   - Numéro : `GH901234`
   - Date de délivrance : `2026-04-01`
   - Date d'expiration : `2036-04-01`
8. Sélectionner les documents
9. Cliquer sur **"Soumettre la demande"**

**Résultats attendus** :
- ✅ Message de succès
- ✅ Un nouveau demandeur est créé
- ✅ Un nouveau passeport est créé
- ✅ Une nouvelle demande est créée avec le statut "Approuvée"

### Scénario 4 : Recherche sans résultat

**Objectif** : Tester le comportement quand aucun applicant n'est trouvé

**Étapes** :
1. Accéder à `http://localhost:8080/backoffice/duplicates/form`
2. Sélectionner un type de demande
3. Chercher par email : `doesnotexist@example.com`
4. Cliquer sur **"Rechercher"**

**Résultats attendus** :
- ✅ Aucune information d'applicant n'est affichée
- ✅ Le formulaire reste vide pour le remplissage manuel
- ✅ L'option "Remplir les données from scratch" est disponible

## Vérification en base de données

### 1. Vérifier qu'une nouvelle demande a été créée

```sql
-- Récupérer la dernière demande
SELECT d.id, d.date_demande, d.id_statut, dm.nom, dm.prenom, td.libelle
FROM demande d
JOIN demandeur dm ON d.id_demandeur = dm.id
JOIN type_demande td ON d.id_type_demande = td.id
ORDER BY d.id DESC
LIMIT 1;

-- Résultat attendu : id_statut = 3 (Approuvée)
```

### 2. Vérifier que le nouveau passeport a été créé

```sql
-- Récupérer tous les passeports
SELECT p.id, p.numero_passeport, p.date_expiration, d.nom, d.prenom
FROM passeport p
JOIN demandeur d ON p.id_demandeur = d.id
ORDER BY p.id DESC;
```

### 3. Vérifier que la nouvelle visa a été créée

```sql
-- Récupérer toutes les visas
SELECT v.id, v.reference, v.date_debut, v.date_fin, p.numero_passeport
FROM visa v
JOIN passeport p ON v.id_passeport = p.id
ORDER BY v.id DESC;
```

### 4. Vérifier que la nouvelle carte résident a été créée

```sql
-- Récupérer toutes les cartes résidents
SELECT cr.id, cr.reference, cr.date_debut, cr.date_fin, p.numero_passeport
FROM carte_resident cr
JOIN passeport p ON cr.id_passeport = p.id
ORDER BY cr.id DESC;
```

## Logs et Debugging

### 1. Activer les logs de Hibernate (optionnel)

Ajouter à `application.yml` :

```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

### 2. Vérifier les erreurs dans la console

```
[ERROR] - Erreur lors de la création de la demande : ...
[INFO] - Demande créée avec succès pour le demandeur ID: 1
```

## Checklist de test

- [ ] Scénario 1 : Transfert de visa - Applicant existant ✅
- [ ] Scénario 2 : Duplicata de carte résident - Applicant existant ✅
- [ ] Scénario 3 : Transfert de visa - Nouvel applicant ✅
- [ ] Scénario 4 : Recherche sans résultat ✅
- [ ] Vérifier la base de données - Demande créée ✅
- [ ] Vérifier la base de données - Passeport créé ✅
- [ ] Vérifier la base de données - Visa/Carte créée ✅
- [ ] Vérifier les statuts - Status = Approuvée ✅
- [ ] Vérifier les documents - Documents enregistrés ✅

## Points clés à vérifier

1. **Recherche de demandeur** : La recherche doit fonctionner par email ou nom
2. **Pré-remplissage** : Si un demandeur est trouvé, les données doivent être pré-remplies
3. **Validation côté client** : Les champs obligatoires doivent être validés
4. **Création de la demande** : Le statut doit toujours être "Approuvée"
5. **Documents** : Les documents sélectionnés doivent être enregistrés
6. **Intégrité des données** : Les relations entre entités doivent être maintenues
