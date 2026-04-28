package fr.project.backoffice.dto;

import java.time.LocalDate;
import java.util.List;

public class DuplicateRequestDto {

    // Type of duplicate request
    private String typesDuplicate; // "passeport_perdu" or "carte_resident_perdue"

    // Search existing applicant
    private String rechercheMail;
    private String rechercheNom;
    private Long idDemandeurExistant;

    // Applicant Information (required for new applicant)
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String nationalite;
    private String email;
    private String telephone;
    private String situationFamiliale;
    private String adresse;

    // Passport Details
    private String numeroPasseport;
    private LocalDate dateDelivrance;
    private LocalDate dateExpirationPasseport;
    private String paysDelivrance;
    private String typePasseport;

    // For Lost Passport Case - New Passport Number
    private String nouveauNumeroPasseport;
    private LocalDate dateDelivranceNouveauPasseport;
    private LocalDate dateExpirationNouveauPasseport;

    // Documents
    private List<Long> documents;

    // Getters and Setters
    public String getTypesDuplicate() {
        return typesDuplicate;
    }

    public void setTypesDuplicate(String typesDuplicate) {
        this.typesDuplicate = typesDuplicate;
    }

    public String getRechercheMail() {
        return rechercheMail;
    }

    public void setRechercheMail(String rechercheMail) {
        this.rechercheMail = rechercheMail;
    }

    public String getRechercheNom() {
        return rechercheNom;
    }

    public void setRechercheNom(String rechercheNom) {
        this.rechercheNom = rechercheNom;
    }

    public Long getIdDemandeurExistant() {
        return idDemandeurExistant;
    }

    public void setIdDemandeurExistant(Long idDemandeurExistant) {
        this.idDemandeurExistant = idDemandeurExistant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSituationFamiliale() {
        return situationFamiliale;
    }

    public void setSituationFamiliale(String situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumeroPasseport() {
        return numeroPasseport;
    }

    public void setNumeroPasseport(String numeroPasseport) {
        this.numeroPasseport = numeroPasseport;
    }

    public LocalDate getDateDelivrance() {
        return dateDelivrance;
    }

    public void setDateDelivrance(LocalDate dateDelivrance) {
        this.dateDelivrance = dateDelivrance;
    }

    public LocalDate getDateExpirationPasseport() {
        return dateExpirationPasseport;
    }

    public void setDateExpirationPasseport(LocalDate dateExpirationPasseport) {
        this.dateExpirationPasseport = dateExpirationPasseport;
    }

    public String getPaysDelivrance() {
        return paysDelivrance;
    }

    public void setPaysDelivrance(String paysDelivrance) {
        this.paysDelivrance = paysDelivrance;
    }

    public String getTypePasseport() {
        return typePasseport;
    }

    public void setTypePasseport(String typePasseport) {
        this.typePasseport = typePasseport;
    }

    public String getNouveauNumeroPasseport() {
        return nouveauNumeroPasseport;
    }

    public void setNouveauNumeroPasseport(String nouveauNumeroPasseport) {
        this.nouveauNumeroPasseport = nouveauNumeroPasseport;
    }

    public LocalDate getDateDelivranceNouveauPasseport() {
        return dateDelivranceNouveauPasseport;
    }

    public void setDateDelivranceNouveauPasseport(LocalDate dateDelivranceNouveauPasseport) {
        this.dateDelivranceNouveauPasseport = dateDelivranceNouveauPasseport;
    }

    public LocalDate getDateExpirationNouveauPasseport() {
        return dateExpirationNouveauPasseport;
    }

    public void setDateExpirationNouveauPasseport(LocalDate dateExpirationNouveauPasseport) {
        this.dateExpirationNouveauPasseport = dateExpirationNouveauPasseport;
    }

    public List<Long> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Long> documents) {
        this.documents = documents;
    }
}
