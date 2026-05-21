package fr.project.backoffice.service;

import fr.project.backoffice.dto.PhotoCaptureDto;
import fr.project.backoffice.dto.SignatureCaptureDto;
import fr.project.backoffice.dto.DemandeurFileDto;
import fr.project.backoffice.entity.PhotoCapture;
import fr.project.backoffice.entity.SignatureCapture;
import fr.project.backoffice.entity.Demandeur;
import fr.project.backoffice.entity.Demande;
import fr.project.backoffice.repository.PhotoCaptureRepository;
import fr.project.backoffice.repository.SignatureCaptureRepository;
import fr.project.backoffice.repository.DemandeurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * Sprint 5: Service for photo capture, signature capture, and demandeur file management
 */
@Service
@Transactional
public class PhotoSignatureService {
    private static final Logger logger = LoggerFactory.getLogger(PhotoSignatureService.class);

    @Autowired
    private PhotoCaptureRepository photoCaptureRepository;

    @Autowired
    private SignatureCaptureRepository signatureCaptureRepository;

    @Autowired
    private DemandeurRepository demandeurRepository;

    /**
     * Save captured photo from webcam
     */
    @Transactional
    public PhotoCaptureDto savePhoto(Long demandeurId, Long demandeId, String photoBase64, String format) {
        Demandeur demandeur = demandeurRepository.findById(demandeurId)
                .orElseThrow(() -> new IllegalArgumentException("Demandeur not found: " + demandeurId));

        // Decode Base64 to binary
        byte[] photoData = Base64.getDecoder().decode(photoBase64.split(",")[1]); // Remove data URI prefix

        PhotoCapture photo = new PhotoCapture();
        photo.setDemandeur(demandeur);
        if (demandeId != null) {
            // Note: You'll need to inject DemandeRepository for this
            // photo.setDemande(...);
        }
        photo.setPhotoData(photoData);
        photo.setPhotoFormat(format);
        photo.setCaptureDate(LocalDateTime.now());
        photo.setFileName(demandeur.getId() + "_" + System.currentTimeMillis() + ".png");

        // Set as primary if no previous photo exists
        Optional<PhotoCapture> existingPrimary = photoCaptureRepository
                .findByDemandeurAndIsPrimaryTrue(demandeur);
        if (existingPrimary.isEmpty()) {
            photo.setIsPrimary(true);
        }

        PhotoCapture saved = photoCaptureRepository.save(photo);
        logger.info("Photo saved for demandeur: {}", demandeurId);

        return mapPhotoToDto(saved);
    }

    /**
     * Save captured signature
     */
    @Transactional
    public SignatureCaptureDto saveSignature(Long demandeurId, Long demandeId, String signatureData, String format) {
        Demandeur demandeur = demandeurRepository.findById(demandeurId)
                .orElseThrow(() -> new IllegalArgumentException("Demandeur not found: " + demandeurId));

        SignatureCapture signature = new SignatureCapture();
        signature.setDemandeur(demandeur);
        if (demandeId != null) {
            // Note: You'll need to inject DemandeRepository for this
            // signature.setDemande(...);
        }
        signature.setSignatureData(signatureData); // SVG or base64
        signature.setSignatureFormat(format); // svg or image/png
        signature.setCaptureDate(LocalDateTime.now());
        signature.setFileName(demandeur.getId() + "_sig_" + System.currentTimeMillis() + ".svg");
        signature.setIsValid(true);

        SignatureCapture saved = signatureCaptureRepository.save(signature);
        logger.info("Signature saved for demandeur: {}", demandeurId);

        return mapSignatureToDto(saved);
    }

    /**
     * Get demandeur file with photo and signature
     */
    @Transactional(readOnly = true)
    public DemandeurFileDto getDemandeurFile(Long demandeurId) {
        Demandeur demandeur = demandeurRepository.findById(demandeurId)
                .orElseThrow(() -> new IllegalArgumentException("Demandeur not found: " + demandeurId));

        // Get primary photo
        Optional<PhotoCapture> primaryPhoto = photoCaptureRepository
                .findByDemandeurAndIsPrimaryTrue(demandeur);

        // Get valid signature
        Optional<SignatureCapture> signature = signatureCaptureRepository
                .findByDemandeurAndIsValidTrue(demandeur);

        String photoBase64 = primaryPhoto.isPresent() ?
                "data:image/png;base64," + Base64.getEncoder().encodeToString(primaryPhoto.get().getPhotoData())
                : null;

        String signatureData = signature.isPresent() ? signature.get().getSignatureData() : null;

        return DemandeurFileDto.builder()
                .id(demandeur.getId())
                .nom(demandeur.getNom())
                .prenom(demandeur.getPrenom())
                .dateNaissance(demandeur.getDateNaissance())
                .lieuNaissance(demandeur.getLieuNaissance())
                .telephone(demandeur.getTelephone())
                .email(demandeur.getEmail())
                .adresse(demandeur.getAdresse())
                .situationFamiliale(demandeur.getSituationFamiliale() != null ?
                        demandeur.getSituationFamiliale().getLibelle() : "N/A")
                .nationalite(demandeur.getNationalite() != null ?
                        demandeur.getNationalite().getLibelle() : "N/A")
                .photoData(photoBase64)
                .signatureData(signatureData)
                .build();
    }

    /**
     * Update primary photo
     */
    @Transactional
    public PhotoCaptureDto setPrimaryPhoto(Long photoId) {
        PhotoCapture photo = photoCaptureRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Photo not found: " + photoId));

        // Unset previous primary
        Optional<PhotoCapture> currentPrimary = photoCaptureRepository
                .findByDemandeurAndIsPrimaryTrue(photo.getDemandeur());
        if (currentPrimary.isPresent() && !currentPrimary.get().getId().equals(photoId)) {
            currentPrimary.get().setIsPrimary(false);
            photoCaptureRepository.save(currentPrimary.get());
        }

        // Set new primary
        photo.setIsPrimary(true);
        PhotoCapture saved = photoCaptureRepository.save(photo);

        logger.info("Primary photo updated: {}", photoId);
        return mapPhotoToDto(saved);
    }

    /**
     * Get all photos for a demandeur
     */
    @Transactional(readOnly = true)
    public List<PhotoCaptureDto> getPhotos(Long demandeurId) {
        Demandeur demandeur = demandeurRepository.findById(demandeurId)
                .orElseThrow(() -> new IllegalArgumentException("Demandeur not found: " + demandeurId));

        return photoCaptureRepository.findByDemandeur(demandeur).stream()
                .map(this::mapPhotoToDto)
                .toList();
    }

    /**
     * Get all signatures for a demandeur
     */
    @Transactional(readOnly = true)
    public List<SignatureCaptureDto> getSignatures(Long demandeurId) {
        Demandeur demandeur = demandeurRepository.findById(demandeurId)
                .orElseThrow(() -> new IllegalArgumentException("Demandeur not found: " + demandeurId));

        return signatureCaptureRepository.findByDemandeur(demandeur).stream()
                .map(this::mapSignatureToDto)
                .toList();
    }

    /**
     * Delete photo
     */
    @Transactional
    public void deletePhoto(Long photoId) {
        PhotoCapture photo = photoCaptureRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Photo not found: " + photoId));

        photoCaptureRepository.delete(photo);
        logger.info("Photo deleted: {}", photoId);
    }

    /**
     * Delete signature
     */
    @Transactional
    public void deleteSignature(Long signatureId) {
        SignatureCapture signature = signatureCaptureRepository.findById(signatureId)
                .orElseThrow(() -> new IllegalArgumentException("Signature not found: " + signatureId));

        signatureCaptureRepository.delete(signature);
        logger.info("Signature deleted: {}", signatureId);
    }

    /**
     * Map PhotoCapture entity to DTO
     */
    private PhotoCaptureDto mapPhotoToDto(PhotoCapture photo) {
        String photoBase64 = "data:image/png;base64," + 
                Base64.getEncoder().encodeToString(photo.getPhotoData());

        return PhotoCaptureDto.builder()
                .id(photo.getId())
                .demandeurId(photo.getDemandeur().getId())
                .demandeId(photo.getDemande() != null ? photo.getDemande().getId() : null)
                .photoData(photoBase64)
                .photoFormat(photo.getPhotoFormat())
                .fileName(photo.getFileName())
                .isPrimary(photo.getIsPrimary())
                .qualityScore(photo.getQualityScore())
                .build();
    }

    /**
     * Map SignatureCapture entity to DTO
     */
    private SignatureCaptureDto mapSignatureToDto(SignatureCapture signature) {
        return SignatureCaptureDto.builder()
                .id(signature.getId())
                .demandeurId(signature.getDemandeur().getId())
                .demandeId(signature.getDemande() != null ? signature.getDemande().getId() : null)
                .signatureData(signature.getSignatureData())
                .signatureFormat(signature.getSignatureFormat())
                .fileName(signature.getFileName())
                .isValid(signature.getIsValid())
                .build();
    }
}
