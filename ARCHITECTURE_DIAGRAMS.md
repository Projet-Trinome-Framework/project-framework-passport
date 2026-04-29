# Diagrammes - Fonctionnalité de Duplicata

## 1. Architecture générale

```
┌─────────────────────────────────────────────────────────────────┐
│                     Frontend (Thymeleaf)                        │
│         duplicate-request-form.html                            │
│  ┌────────────────────────────────────────────────────────┐   │
│  │  Étape 1: Choix type    Étape 2: Recherche   Étape 3: │   │
│  │  (Passeport/Carte)      (Email/Nom)          Form      │   │
│  └────────────────────────────────────────────────────────┘   │
└────────────────────────┬────────────────────────────────────────┘
                         │ AJAX/POST
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Controller Layer                             │
│         DuplicateRequestController                             │
│  ┌────────────────────────────────────────────────────────┐   │
│  │ GET /form                                              │   │
│  │ POST /search-applicant (AJAX JSON)                     │   │
│  │ POST /visa-transfer                                    │   │
│  │ POST /resident-card-duplicate                          │   │
│  └────────────────────────────────────────────────────────┘   │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Service Layer                               │
│         DuplicateRequestService                                │
│  ┌────────────────────────────────────────────────────────┐   │
│  │ searchApplicant()                                      │   │
│  │ createVisaTransferRequest()                            │   │
│  │ createCarteResidentDuplicateRequest()                  │   │
│  │ getDemandeur() [create or find]                        │   │
│  └────────────────────────────────────────────────────────┘   │
└────────────────────────┬────────────────────────────────────────┘
                         │
      ┌──────────────────┼──────────────────┐
      ▼                  ▼                  ▼
┌─────────────┐  ┌──────────────┐  ┌──────────────────┐
│  Repositories                 │  │  Entities        │
├─────────────┤  ├──────────────┤  ├──────────────────┤
│ • Demandeur │  │ • Demandeur  │  │ • Demande        │
│ • Passport  │  │ • Passeport  │  │ • Visa           │
│ • CarteRes. │  │ • Visa       │  │ • CarteResident  │
│ • Demande   │  │ • CarteRes.  │  │ • VisaTransform. │
│ • etc.      │  │ • etc.       │  │ • TypeDemande    │
└─────────────┘  └──────────────┘  └──────────────────┘
      │                  │                  │
      └──────────────────┼──────────────────┘
                         ▼
            ┌──────────────────────────┐
            │  PostgreSQL Database      │
            │  (Base de données)        │
            └──────────────────────────┘
```

## 2. Flux de Transfert de Visa (Passeport Perdu)

```
START
  │
  ├─ User sélectionne "Passeport Perdu"
  │   │
  │   ├─ Form affichée avec 3 étapes
  │   │
  └─────► Étape 1: Recherche applicant
           │
           ├─ Par Email ──┐
           │              ├─► Recherche en base
           ├─ Par Nom ────┤
           │              │
           └─────────────┐│
                          ▼
                    Trouvé ? ─────► OUI ─── Pré-remplir Form (Étape 3)
                    │
                    └─► NON ──── Formulaire vide (Étape 3)
                              (utilisateur remplir from scratch)
                    
         Étape 2: Remplir données demandeur (si nouveau)
           │
           ├─ Nom, Prénom, Date naissance
           ├─ Email, Téléphone
           ├─ Nationalité, Situation familiale
           ├─ Adresse
           │
           └─────────────┐
                         ▼
         Étape 3: Remplir passeport perdu + nouveau
           │
           ├─ Ancien passeport:
           │  ├─ Numéro
           │  ├─ Date délivrance
           │  └─ Date expiration
           │
           ├─ Nouveau passeport:
           │  ├─ Numéro (REQUIS)
           │  ├─ Date délivrance (REQUIS)
           │  └─ Date expiration (REQUIS)
           │
           ├─ Documents requis (sélection)
           │
           └─────────────┐
                         ▼
         SUBMIT /visa-transfer
           │
           ├─ Récupérer/Créer Demandeur
           │   └─► Si ID fourni: récupérer depuis BD
           │   └─► Sinon: créer nouveau
           │
           ├─ Récupérer ancien Passeport
           │   └─► Query: findByNumeroPasseport()
           │
           ├─ Créer nouveau Passeport
           │   ├─ setNumeroPasseport(nouveauNumero)
           │   ├─ setDateDelivrance(nouvelle date)
           │   ├─ setDateExpiration(nouvelle date)
           │   └─ save()
           │
           ├─ Récupérer ancien Visa
           │   └─► Query: findAll() filter by old passport
           │
           ├─ Créer TypeVisa "transfert_visa"
           │
           ├─ Créer TypeDemande "transfert_visa_passeport_perdu"
           │
           ├─ Créer VisaTransformable
           │   ├─ setDemandeur(demandeur)
           │   ├─ setPasseport(nouveauPasseport)
           │   ├─ setNumeroReference(generated)
           │   └─ save()
           │
           ├─ Créer Demande
           │   ├─ setVisaTransformable(visaTransformable)
           │   ├─ setDateDemande(now)
           │   ├─ setIdStatut(3) ◄─── "Approuvée"
           │   ├─ setDemandeur(demandeur)
           │   ├─ setTypeVisa(typeVisa)
           │   ├─ setTypeDemande(typeDemande)
           │   ├─ setDateTraitement(now)
           │   └─ save()
           │
           ├─ Créer nouvelle Visa
           │   ├─ setDemande(demande)
           │   ├─ setReference(generated)
           │   ├─ setDateDebut(now)
           │   ├─ setDateFin(oldVisa.dateExpiration) ◄─── Conservation
           │   ├─ setPasseport(nouveauPasseport)
           │   └─ save()
           │
           ├─ Enregistrer Documents
           │   └─► Pour chaque document sélectionné:
           │       ├─ Créer DemandePieceJustificative
           │       ├─ setSoumis(true)
           │       ├─ setDateSoumission(now)
           │       └─ save()
           │
           └─────────────┐
                         ▼
         REDIRECT /duplicates/form
           │
           ├─ Flash message: "Succès!"
           │
           └─────────────┐
                         ▼
                      END
```

## 3. Flux de Duplicata de Carte Résident

```
START
  │
  ├─ User sélectionne "Carte de Résident Perdue"
  │
  ├─ RECHERCHE (identique au transfert)
  │
  ├─ REMPLISSAGE FORME (identique au transfert)
  │   (pas de nouveau passeport ici)
  │
  └─────► SUBMIT /resident-card-duplicate
           │
           ├─ Récupérer/Créer Demandeur (identique)
           │
           ├─ Récupérer Passeport
           │   └─► Query: findByNumeroPasseport()
           │
           ├─ Récupérer CarteResident existante
           │   └─► Query: findAll() filter by passeport
           │
           ├─ Créer TypeVisa "carte_resident"
           │
           ├─ Créer TypeDemande "duplicata_carte_resident"
           │
           ├─ Créer VisaTransformable
           │
           ├─ Créer Demande
           │   └─ setIdStatut(3) ◄─── "Approuvée"
           │
           ├─ Créer nouvelle CarteResident
           │   ├─ setReference(generated)
           │   ├─ setDateDebut(now)
           │   ├─ setDateFin(oldCarte.dateFin) ◄─── Conservation
           │   ├─ setPasseport(passeport)
           │   └─ save()
           │
           ├─ Enregistrer Documents (identique)
           │
           └─────────────┐
                         ▼
         REDIRECT + Flash message
           │
           └─────────────┐
                         ▼
                      END
```

## 4. Cycle de vie de la Demande

```
                    Demande de Duplicata
                           │
                           ▼
            ┌────────────────────────────┐
            │   Création de la Demande   │
            │   - ID statut = 3          │
            │   - "Approuvée"            │
            │   - Date traitement = now  │
            └────────────────────────────┘
                           │
                           ▼
            ┌────────────────────────────┐
            │  Création du Nouveau Doc   │
            │  - Visa ou CarteResident   │
            │  - Dates de la demande     │
            │  - Référence générée       │
            └────────────────────────────┘
                           │
                           ▼
            ┌────────────────────────────┐
            │   Enregistrement Documents │
            │   - DemandePieceJust.      │
            │   - Date soumission        │
            │   - Statut: soumis = true  │
            └────────────────────────────┘
                           │
                           ▼
                    ✅ DEMANDE COMPLÈTE
```

## 5. Entités et Associations

```
┌──────────────────┐
│   Demandeur      │
│                  │
│ - nom            │
│ - prenom         │
│ - email          │
│ - telephone      │
│ - adresse        │
│ - dateNaissance  │
│ - lieuNaissance  │
└─────────┬────────┘
          │
          ├────────────────┬──────────────┬─────────────────┐
          │                │              │                 │
          ▼                ▼              ▼                 ▼
    ┌─────────────┐  ┌─────────┐  ┌──────────┐  ┌──────────────────┐
    │  Passeport  │  │ Visa    │  │CarteRési │  │VisaTransformable │
    │             │  │         │  │dent      │  │                  │
    │ - numero    │  │ - ref   │  │ - ref    │  │ - numeroRef      │
    │ - dateDeliv │  │ - dateD │  │ - dateD  │  │                  │
    │ - dateExp   │  │ - dateF │  │ - dateF  │  │ 1:N relation avec│
    │ - pays      │  │         │  │ - dateD. │  │ Demande          │
    └─────────────┘  └────┬────┘  └────┬─────┘  └──────────────────┘
          │                │            │                │
          │                │            │                │
          └────────┬───────┴────────┬───┴────────────┬───┘
                   │                │                │
                   ▼                ▼                ▼
            ┌───────────────────────────────┐
            │      Demande (Request)        │
            │                               │
            │ - dateDemande                 │
            │ - idStatut = 3 (Approuvée)    │
            │ - dateTraitement              │
            │ - typeDemande                 │
            │ - typeVisa                    │
            │ - visaTransformable           │
            │ - demandeur                   │
            └───────────────┬───────────────┘
                            │
                            ▼
            ┌───────────────────────────────┐
            │  DemandePieceJustificative    │
            │                               │
            │ - pieceJustificative          │
            │ - soumis = true               │
            │ - dateSoumission              │
            │ - validee (optionnel)         │
            │ - commentaire (optionnel)     │
            └───────────────────────────────┘
```

## 6. Statuts de Demande

```
Statuts disponibles:
├─ 1: BROUILLON (Draft)
├─ 2: SOUMISE (Submitted)
├─ 3: APPROUVÉE ◄─── Utilisé pour les Duplicatas
├─ 4: VALIDÉE (Validated)
└─ 5: REJETÉE (Rejected)

Pour les demandes de Duplicata:
└─► Créé directement avec statut 3 (Approuvée)
    car c'est une régularisation administrative
```

---

Ces diagrammes illustrent:
1. **Architecture générale** : Comment les couches interagissent
2. **Flux de transfert de visa** : Le processus détaillé pas à pas
3. **Flux de carte résident** : Similaire mais sans nouveau passeport
4. **Cycle de vie** : Comment la demande progresse
5. **Entités** : Les relations entre les objets
6. **Statuts** : Comment les statuts sont utilisés
