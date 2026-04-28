# Sprint 2 : Fonctionnalité de Duplicata

## Vue d'ensemble
Cette fonctionnalité gère les demandes de duplicata pour les cas de perte de passeport ou de carte de résident. Elle permet aux demandeurs qui sont déjà dans le système de faire des demandes avec leurs données pré-remplies, ou aux nouveaux demandeurs de remplir les informations depuis zéro.

## Cas d'usage

### 1. **Passeport Perdu - Transfert de Visa**
- Une personne a perdu son passeport mais possède un visa valide
- Elle obtient un nouveau passeport
- Elle demande un transfert de visa vers son nouveau passeport
- **Information supplémentaire requise** : Numéro du nouveau passeport
- **Statut de la demande** : Directement "Approuvée" (régularisation administrative)

### 2. **Carte de Résident Perdue - Demande de Duplicata**
- Une personne a perdu sa carte de résident
- Elle demande un duplicata de la carte
- **Statut de la demande** : Directement "Approuvée" (régularisation administrative)

## Workflow

### Étape 1 : Sélection du type de duplicata
L'utilisateur choisit entre deux options :
- 📄 **Passeport Perdu** → Transfert de visa vers le nouveau passeport
- 🏠 **Carte de Résident Perdue** → Demande de duplicata de carte résident

### Étape 2 : Recherche de l'applicant existant
L'utilisateur peut rechercher un applicant existant par :
- **Email** (optionnel)
- **Nom** (optionnel)

**Résultats possibles** :
- **Applicant trouvé** : Les données sont pré-remplies. L'utilisateur peut choisir d'utiliser ces données ou de remplir manuellement.
- **Aucun applicant trouvé** : L'utilisateur remplit tous les champs depuis zéro.

### Étape 3 : Remplissage du formulaire
En fonction du type de duplicata :

#### Pour **Passeport Perdu** :
- Informations du demandeur (nom, prénom, date de naissance, etc.)
- Informations du **passeport perdu** (numéro, date de délivrance, date d'expiration)
- Informations du **nouveau passeport** (numéro, date de délivrance, date d'expiration)
- Documents requis

#### Pour **Carte de Résident Perdue** :
- Informations du demandeur (préfillies si existant)
- Informations du passeport actuel
- Documents requis

## Statut des demandes

Tous les demandes de duplicata sont créées avec le statut **"Approuvée"** (valeur : 3) car il s'agit d'une régularisation administrative.

**Statuts des demandes** :
- 1 : Brouillon
- 2 : Soumise
- 3 : **Approuvée** (utilisé pour les duplicatas)
- 4 : Validée
- 5 : Rejetée

## Structure des données

### Entités impliquées

1. **Demandeur** : Les informations personnelles de la personne
2. **Passeport** : Le document de voyage
3. **Visa** : Le visa lié au passeport (pour le cas du transfert)
4. **CarteResident** : La carte de résident (pour le cas du duplicata)
5. **Demande** : La demande elle-même
6. **VisaTransformable** : Lien entre demandeur, passeport et demande
7. **TypeDemande** : Type de demande (transfert_visa_passeport_perdu, duplicata_carte_resident)
8. **DemandePieceJustificative** : Documents soumis pour la demande

### Types de demande ajoutés

```sql
INSERT INTO type_demande (libelle) VALUES
('transfert_visa_passeport_perdu'),
('duplicata_carte_resident');
```

## Points d'intégration

### Controller : `DuplicateRequestController`
- **Route** : `/backoffice/duplicates`
- **Endpoints** :
  - `GET /form` : Affiche le formulaire de duplicata
  - `POST /search-applicant` : Recherche un applicant existant (AJAX)
  - `POST /visa-transfer` : Soumet une demande de transfert de visa
  - `POST /resident-card-duplicate` : Soumet une demande de duplicata de carte résident

### Service : `DuplicateRequestService`
- `searchApplicant(email, nom)` : Recherche un demandeur par email ou nom
- `createVissaTransferRequest(dto)` : Crée une demande de transfert de visa
- `createCarteResidentDuplicateRequest(dto)` : Crée une demande de duplicata de carte résident

### DTO : `DuplicateRequestDto`
Contient tous les champs nécessaires pour les deux types de demandes.

### Template : `duplicate-request-form.html`
Formulaire dynamique à trois étapes pour soumettre une demande de duplicata.

## Améliorations apportées aux repositories

### `DemandeurRepository`
- `findByEmail(String email)` : Cherche un demandeur par email
- `findByNomAndPrenom(String nom, String prenom)` : Cherche par nom et prénom
- `findByNomContainingIgnoreCase(String nom)` : Cherche par nom (insensible à la casse)

### `PasseportRepository`
- `findByNumeroPasseport(String numeroPasseport)` : Cherche un passeport par numéro
- `findByDemandeur(Demandeur demandeur)` : Récupère les passeports d'un demandeur

### `CarteResidentRepository`
- `findByDemande(Demande demande)` : Récupère les cartes associées à une demande
- `findTopByOrderByIdDesc()` : Récupère la carte la plus récente

## Gestion des documents

Seuls les **documents communs** sont requis pour les demandes de duplicata (pas de documents catégorie-spécifiques).

Les documents communs incluent :
- Photos d'identité
- Photocopie certifiée du visa en cours
- Photocopie certifiée du passeport
- Etc.

## Intégration au dashboard

Un bouton **"+ Duplicate Request"** a été ajouté au dashboard visa pour accéder rapidement au formulaire de duplicata.

## Validation et Gestion des erreurs

### Validations côté client (JavaScript)
- Vérification que le type de duplicata est sélectionné
- Vérification que les champs obligatoires sont remplis
- Pour le cas "Passeport Perdu" : Vérification des champs du nouveau passeport

### Validations côté serveur (Service)
- Vérification du type de duplicata
- Vérification que le passeport/applicant existe
- Vérification des données de base du demandeur

## Exemples d'utilisation

### Scénario 1 : Demandeur existant - Transfert de visa
1. L'utilisateur sélectionne "Passeport Perdu"
2. L'utilisateur recherche par email : `jean.dupont@example.com`
3. Les données de Jean Dupont s'affichent
4. L'utilisateur entre le numéro du nouveau passeport
5. La demande est créée avec statut "Approuvée"

### Scénario 2 : Nouveau demandeur - Duplicata de carte résident
1. L'utilisateur sélectionne "Carte de Résident Perdue"
2. L'utilisateur recherche par nom mais ne trouve rien
3. L'utilisateur remplit tous les champs manuellement
4. La demande est créée avec statut "Approuvée"

## Prochaines étapes (Sprints futurs)

- [ ] Notification par email au demandeur
- [ ] Génération de documents PDF
- [ ] Suivi de la demande en temps réel
- [ ] Historique des demandes par applicant
- [ ] Rapports et statistiques
