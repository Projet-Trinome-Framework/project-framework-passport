# Code Examples - Fonctionnalité de Duplicata

## 1. Recherche de Demandeur

### Dans le Service

```java
// DuplicateRequestService.java
public Optional<DemandeurSearchResultDto> searchApplicant(String email, String nom) {
    Optional<Demandeur> demandeur = Optional.empty();

    // Recherche par email en priorité
    if (email != null && !email.isEmpty()) {
        demandeur = demandeurRepository.findByEmail(email);
    } 
    // Sinon par nom
    else if (nom != null && !nom.isEmpty()) {
        List<Demandeur> demandeurs = demandeurRepository.findByNomContainingIgnoreCase(nom);
        if (!demandeurs.isEmpty()) {
            demandeur = Optional.of(demandeurs.get(0));
        }
    }

    // Conversion en DTO
    if (demandeur.isPresent()) {
        return Optional.of(mapToDemandeurSearchResultDto(demandeur.get()));
    }
    return Optional.empty();
}
```

### Dans le Repository

```java
// DemandeurRepository.java
@Repository
public interface DemandeurRepository extends JpaRepository<Demandeur, Long> {
    Optional<Demandeur> findByEmail(String email);
    List<Demandeur> findByNomAndPrenom(String nom, String prenom);
    List<Demandeur> findByNomContainingIgnoreCase(String nom);
}
```

### Frontend - Appel AJAX

```javascript
// duplicate-request-form.html
function searchApplicant() {
    const email = document.getElementById('rechercheMail').value;
    const nom = document.getElementById('rechercheNom').value;

    if (!email && !nom) {
        alert('Veuillez entrer un email ou un nom');
        return;
    }

    fetch('/backoffice/duplicates/search-applicant', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `email=${encodeURIComponent(email)}&nom=${encodeURIComponent(nom)}`
    })
    .then(response => response.json())
    .then(data => {
        if (data) {
            displayApplicantInfo(data);
        } else {
            alert('Aucun applicant trouvé');
        }
    })
    .catch(error => console.error('Error:', error));
}
```

## 2. Création de Demande de Transfert de Visa

### Service - Logique métier

```java
@Transactional
public void createVisaTransferRequest(DuplicateRequestDto dto) {
    // 1. Récupérer ou créer le demandeur
    Demandeur demandeur = getDemandeur(dto);
    
    // 2. Récupérer l'ancien passeport
    Passeport ancienPasseport = passeportRepository
        .findByNumeroPasseport(dto.getNumeroPasseport())
        .orElseThrow(() -> new IllegalArgumentException("Ancien passeport non trouvé"));

    // 3. Créer le nouveau passeport
    Passeport nouveauPasseport = new Passeport();
    nouveauPasseport.setDemandeur(demandeur);
    nouveauPasseport.setNumeroPasseport(dto.getNouveauNumeroPasseport());
    nouveauPasseport.setDateDelivrance(dto.getDateDelivranceNouveauPasseport());
    nouveauPasseport.setDateExpiration(dto.getDateExpirationNouveauPasseport());
    nouveauPasseport.setPaysDelivrance(dto.getPaysDelivrance());
    Passeport savedNouveauPasseport = passeportRepository.save(nouveauPasseport);

    // 4. Récupérer l'ancien visa
    List<Visa> visasAnciens = visaRepository.findAll().stream()
        .filter(v -> v.getPasseport().getId().equals(ancienPasseport.getId()))
        .toList();

    // 5. Créer les types
    TypeVisa typeVisa = new TypeVisa();
    typeVisa.setLibelle("transfert_visa");
    TypeVisa savedTypeVisa = typeVisaRepository.save(typeVisa);

    TypeDemande typeDemande = new TypeDemande();
    typeDemande.setLibelle("transfert_visa_passeport_perdu");
    TypeDemande savedTypeDemande = typeDemandeRepository.save(typeDemande);

    // 6. Créer VisaTransformable
    VisaTransformable visaTransformable = new VisaTransformable();
    visaTransformable.setDemandeur(demandeur);
    visaTransformable.setPasseport(savedNouveauPasseport);
    visaTransformable.setNumeroReference(generateReferenceVisa(demandeur.getId()));
    VisaTransformable savedVisaTransformable = visaTransformableRepository.save(visaTransformable);

    // 7. Créer la Demande avec statut "Approuvée"
    Demande demande = new Demande();
    demande.setVisaTransformable(savedVisaTransformable);
    demande.setDateDemande(LocalDate.now());
    demande.setIdStatut(3); // ◄─── APPROUVÉE
    demande.setDemandeur(demandeur);
    demande.setTypeVisa(savedTypeVisa);
    demande.setTypeDemande(savedTypeDemande);
    demande.setDateTraitement(LocalDate.now());
    Demande savedDemande = demandeRepository.save(demande);

    // 8. Créer le nouveau Visa
    if (!visasAnciens.isEmpty()) {
        Visa visaAncien = visasAnciens.get(0);
        Visa nouvelleVisa = new Visa();
        nouvelleVisa.setDemande(savedDemande);
        nouvelleVisa.setReference(generateReferenceVisa(demandeur.getId()));
        nouvelleVisa.setDateDebut(LocalDate.now());
        nouvelleVisa.setDateFin(visaAncien.getDateFin()); // ◄─── Conservation
        nouvelleVisa.setPasseport(savedNouveauPasseport);
        visaRepository.save(nouvelleVisa);
    }

    // 9. Enregistrer les documents
    if (dto.getDocuments() != null && !dto.getDocuments().isEmpty()) {
        for (Long documentId : dto.getDocuments()) {
            DemandePieceJustificative demandePiece = new DemandePieceJustificative();
            demandePiece.setDemande(savedDemande);
            demandePiece.setPieceJustificative(pieceJustificativeRepository.findById(documentId).orElse(null));
            demandePiece.setSoumis(true);
            demandePiece.setDateSoumission(LocalDate.now());
            demandePieceJustificativeRepository.save(demandePiece);
        }
    }
}
```

### Points clés du code :
- ✅ **@Transactional** : Assure que toutes les opérations réussissent ou s'annulent
- ✅ **Status = 3** : "Approuvée" directement
- ✅ **Conservation des dates** : `setDateFin(visaAncien.getDateFin())`
- ✅ **Génération de références** : `generateReferenceVisa()`
- ✅ **Documents enregistrés** : `DemandePieceJustificative`

## 3. Logique de Demandeur (Existant ou Nouveau)

```java
private Demandeur getDemandeur(DuplicateRequestDto dto) {
    // Si on a l'ID d'un demandeur existant
    if (dto.getIdDemandeurExistant() != null) {
        return demandeurRepository.findById(dto.getIdDemandeurExistant())
            .orElseThrow(() -> new IllegalArgumentException("Demandeur non trouvé"));
    }

    // Sinon, créer un nouveau demandeur
    Demandeur demandeur = new Demandeur();
    demandeur.setNom(dto.getNom());
    demandeur.setPrenom(dto.getPrenom());
    demandeur.setDateNaissance(dto.getDateNaissance());
    demandeur.setLieuNaissance(dto.getLieuNaissance());
    demandeur.setEmail(dto.getEmail());
    demandeur.setTelephone(dto.getTelephone());
    demandeur.setAdresse(dto.getAdresse());

    // Créer nationalité
    Nationalite nationalite = new Nationalite();
    nationalite.setLibelle(dto.getNationalite());
    Nationalite savedNationalite = nationaliteRepository.save(nationalite);
    demandeur.setNationalite(savedNationalite);

    // Créer situation familiale
    SituationFamiliale situation = new SituationFamiliale();
    situation.setLibelle(dto.getSituationFamiliale());
    SituationFamiliale savedSituation = situationFamilialeRepository.save(situation);
    demandeur.setSituationFamiliale(savedSituation);

    return demandeurRepository.save(demandeur);
}
```

### Points clés :
- ✅ **Vérification du paramètre** : `if (dto.getIdDemandeurExistant() != null)`
- ✅ **Création si nécessaire** : Nouvelle instance de tous les objets nécessaires
- ✅ **Persistance immédiate** : `save()` appelé avant utilisation

## 4. Controller - Endpoints

```java
@PostMapping("/search-applicant")
public ResponseEntity<?> searchApplicant(
    @RequestParam(required = false) String email,
    @RequestParam(required = false) String nom) {
    
    if ((email == null || email.isEmpty()) && (nom == null || nom.isEmpty())) {
        return ResponseEntity.badRequest().body("Email ou nom requis");
    }

    Optional<DemandeurSearchResultDto> result = 
        duplicateRequestService.searchApplicant(email, nom);
    
    return ResponseEntity.ok(result);
}

@PostMapping("/visa-transfer")
public String submitVisaTransferRequest(
    DuplicateRequestDto dto, 
    RedirectAttributes redirectAttributes) {
    
    try {
        if (!"passeport_perdu".equals(dto.getTypesDuplicate())) {
            throw new IllegalArgumentException("Type de duplicata invalide");
        }
        duplicateRequestService.createVisaTransferRequest(dto);
        redirectAttributes.addFlashAttribute("success", 
            "Demande de transfert de visa créée avec succès ! Statut : Approuvée");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", 
            "Erreur lors de la création de la demande : " + e.getMessage());
    }
    return "redirect:/backoffice/duplicates/form";
}
```

### Points clés :
- ✅ **Validation des paramètres** : Vérification du type
- ✅ **RedirectAttributes** : Messages flash dans la session
- ✅ **Gestion d'erreur** : Try/catch avec message utilisateur
- ✅ **Redirection** : Après succès ou erreur

## 5. Frontend - Formulaire Dynamique

```html
<!-- Bouton pour sélectionner le type -->
<div class="choice-buttons">
    <button type="button" class="choice-btn" 
            onclick="selectDuplicateType('passeport_perdu')">
        <h4>📄 Passeport Perdu</h4>
        <p>Transfert de visa vers le nouveau passeport</p>
    </button>
    <button type="button" class="choice-btn" 
            onclick="selectDuplicateType('carte_resident_perdue')">
        <h4>🏠 Carte de Résident Perdue</h4>
        <p>Demande de duplicata de carte résident</p>
    </button>
</div>

<!-- Section new passport (visible seulement pour passeport perdu) -->
<div id="newPassportSection" class="form-section" style="display: none;">
    <h3>Informations du nouveau passeport</h3>
    <div class="form-grid">
        <div class="form-group">
            <label for="nouveauNumeroPasseport">Nouveau numéro de passeport *</label>
            <input type="text" id="nouveauNumeroPasseport" 
                   name="nouveauNumeroPasseport" />
        </div>
        <!-- ... autres champs ... -->
    </div>
</div>
```

### JavaScript pour contrôler l'affichage

```javascript
function selectDuplicateType(type) {
    selectedDuplicateType = type;
    
    // Mettre à jour le style des boutons
    document.querySelectorAll('.choice-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.closest('.choice-btn').classList.add('active');
    
    // Afficher les sections
    document.getElementById('searchSection').style.display = 'block';
    document.getElementById('formSection').style.display = 'block';
    document.getElementById('typesDuplicate').value = type;
    
    // Afficher/masquer la section du nouveau passeport
    if (type === 'passeport_perdu') {
        document.getElementById('newPassportSection').style.display = 'block';
    } else {
        document.getElementById('newPassportSection').style.display = 'none';
    }
    
    // Définir l'action du formulaire
    const form = document.getElementById('duplicateForm');
    if (type === 'passeport_perdu') {
        form.action = '/backoffice/duplicates/visa-transfer';
    } else {
        form.action = '/backoffice/duplicates/resident-card-duplicate';
    }
}
```

### Points clés :
- ✅ **Affichage conditionnel** : Sections montrées/cachées selon le type
- ✅ **Actions dynamiques** : URL du formulaire change
- ✅ **Style feedback** : Bouton actif mis en évidence
- ✅ **User experience** : Interface claire et intuitive

## 6. DTOs

```java
// DuplicateRequestDto.java - Exemple de construction

DuplicateRequestDto dto = new DuplicateRequestDto();

// Cas 1: Applicant existant
dto.setTypesDuplicate("passeport_perdu");
dto.setIdDemandeurExistant(1L);
dto.setNumeroPasseport("AB123456");
dto.setNouveauNumeroPasseport("CD789012");
dto.setDateDelivranceNouveauPasseport(LocalDate.of(2026, 4, 1));
dto.setDateExpirationNouveauPasseport(LocalDate.of(2036, 4, 1));
dto.setDocuments(Arrays.asList(1L, 2L, 3L)); // IDs des documents

// Cas 2: Nouvel applicant
dto.setTypesDuplicate("carte_resident_perdue");
dto.setNom("Martin");
dto.setPrenom("Pierre");
dto.setDateNaissance(LocalDate.of(1990, 6, 20));
dto.setEmail("pierre.martin@example.com");
// ... autres champs ...
```

## 7. Validation des données

### Côté client (JavaScript)

```javascript
// Validation avant soumission
document.getElementById('duplicateForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    if (selectedDuplicateType === 'passeport_perdu') {
        // Vérifier les champs du nouveau passeport
        if (!document.getElementById('nouveauNumeroPasseport').value ||
            !document.getElementById('dateDelivranceNouveauPasseport').value ||
            !document.getElementById('dateExpirationNouveauPasseport').value) {
            alert('Veuillez remplir tous les champs du nouveau passeport');
            return;
        }
    }
    
    this.submit();
});
```

### Côté serveur (Java)

```java
if (!"passeport_perdu".equals(dto.getTypesDuplicate()) 
    && !"carte_resident_perdue".equals(dto.getTypesDuplique())) {
    throw new IllegalArgumentException("Type de duplicata invalide");
}

if (dto.getNumeroPasseport() == null || dto.getNumeroPasseport().isEmpty()) {
    throw new IllegalArgumentException("Numéro de passeport requis");
}
```

---

## Résumé des patrons de conception utilisés

| Patron | Utilisation |
|--------|------------|
| **DTO (Data Transfer Object)** | Échange de données entre les couches |
| **Service Layer** | Logique métier encapsulée |
| **Repository Pattern** | Accès aux données abstraits |
| **Transactional** | Intégrité transactionnelle |
| **Dependency Injection** | Injection des dépendances |
| **AJAX/Fetch** | Recherche asynchrone |
| **Conditional Rendering** | Affichage dynami de formulaire |
| **Flash Messages** | Notifications utilisateur |

---

Ces exemples de code montrent comment utiliser correctement les différentes parties de la fonctionnalité !
