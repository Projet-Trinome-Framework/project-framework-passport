package fr.project.backoffice.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "demande_piece_justificative")
public class DemandePieceJustificative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_piece_justificative", nullable = false)
    private PieceJustificative pieceJustificative;

    @Column(name = "soumis")
    private Boolean soumis;

    @Column(name = "date_soumission")
    private LocalDate datesoumission;

    @Column(name = "validee")
    private Boolean validee;

    @Column(name = "date_validation")
    private LocalDate dateValidation;

    @Column(name = "commentaire")
    private String commentaire;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public PieceJustificative getPieceJustificative() {
        return pieceJustificative;
    }

    public void setPieceJustificative(PieceJustificative pieceJustificative) {
        this.pieceJustificative = pieceJustificative;
    }

    public Boolean getSoumis() {
        return soumis;
    }

    public void setSoumis(Boolean soumis) {
        this.soumis = soumis;
    }

    public LocalDate getDatesoumission() {
        return datesoumission;
    }

    public void setDatesoumission(LocalDate datesoumission) {
        this.datesoumission = datesoumission;
    }

    public Boolean getValidee() {
        return validee;
    }

    public void setValidee(Boolean validee) {
        this.validee = validee;
    }

    public LocalDate getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}