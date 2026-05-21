package fr.project.backoffice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity for storing digitized signatures
 * Sprint 5: Signature capture for demandeur
 */
@Entity
@Table(name = "signature_capture")
public class SignatureCapture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demandeur", nullable = false)
    private Demandeur demandeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demande")
    private Demande demande;

    @Column(name = "signature_data", columnDefinition = "TEXT", nullable = false)
    private String signatureData; // SVG path data or base64 image

    @Column(name = "signature_format", nullable = false)
    private String signatureFormat; // svg, image/png, image/jpeg

    @Column(name = "capture_date", nullable = false)
    private LocalDateTime captureDate;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid = true;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Demandeur getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Demandeur demandeur) {
        this.demandeur = demandeur;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public String getSignatureData() {
        return signatureData;
    }

    public void setSignatureData(String signatureData) {
        this.signatureData = signatureData;
    }

    public String getSignatureFormat() {
        return signatureFormat;
    }

    public void setSignatureFormat(String signatureFormat) {
        this.signatureFormat = signatureFormat;
    }

    public LocalDateTime getCaptureDate() {
        return captureDate;
    }

    public void setCaptureDate(LocalDateTime captureDate) {
        this.captureDate = captureDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }
}
