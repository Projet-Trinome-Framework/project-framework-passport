package fr.project.backoffice.dto;

import java.time.LocalDate;

public class DemandeurSearchResultDto {

    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String email;
    private String telephone;
    private String adresse;
    private String nationalite;
    private String situationFamiliale;

    // Current passport info
    private String numeroPasseportActuel;
    private LocalDate dateExpirationPasseportActuel;

    // Existing visas or resident cards
    private Boolean aVisaOuCarteResident;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getSituationFamiliale() {
        return situationFamiliale;
    }

    public void setSituationFamiliale(String situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    public String getNumeroPasseportActuel() {
        return numeroPasseportActuel;
    }

    public void setNumeroPasseportActuel(String numeroPasseportActuel) {
        this.numeroPasseportActuel = numeroPasseportActuel;
    }

    public LocalDate getDateExpirationPasseportActuel() {
        return dateExpirationPasseportActuel;
    }

    public void setDateExpirationPasseportActuel(LocalDate dateExpirationPasseportActuel) {
        this.dateExpirationPasseportActuel = dateExpirationPasseportActuel;
    }

    public Boolean getAVisaOuCarteResident() {
        return aVisaOuCarteResident;
    }

    public void setAVisaOuCarteResident(Boolean aVisaOuCarteResident) {
        this.aVisaOuCarteResident = aVisaOuCarteResident;
    }
}
