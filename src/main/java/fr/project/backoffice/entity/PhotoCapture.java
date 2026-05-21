package fr.project.backoffice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity for storing captured photos via webcam
 * Sprint 5: Webcam photo capture and demandeur file
 */
@Entity
@Table(name = "photo_capture")
public class PhotoCapture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demandeur", nullable = false)
    private Demandeur demandeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demande")
    private Demande demande;

    @Column(name = "photo_data", columnDefinition = "BYTEA", nullable = false)
    private byte[] photoData; // Binary image data

    @Column(name = "photo_format", nullable = false)
    private String photoFormat; // image/jpeg, image/png

    @Column(name = "capture_date", nullable = false)
    private LocalDateTime captureDate;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false; // Main photo for demandeur file

    @Column(name = "quality_score")
    private Double qualityScore; // 0-100, facial recognition quality

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

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }

    public String getPhotoFormat() {
        return photoFormat;
    }

    public void setPhotoFormat(String photoFormat) {
        this.photoFormat = photoFormat;
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

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Double getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(Double qualityScore) {
        this.qualityScore = qualityScore;
    }
}
