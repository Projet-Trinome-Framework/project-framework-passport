package fr.project.backoffice.controller;

import fr.project.backoffice.dto.PhotoCaptureDto;
import fr.project.backoffice.dto.SignatureCaptureDto;
import fr.project.backoffice.dto.DemandeurFileDto;
import fr.project.backoffice.service.PhotoSignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Sprint 5: Controller for photo capture, signature capture, and demandeur file
 */
@Controller
@RequestMapping("/backoffice/photo-signature")
public class PhotoSignatureController {

    @Autowired
    private PhotoSignatureService photoSignatureService;

    /**
     * Display photo/signature capture page
     */
    @GetMapping("/capture/{demandeurId}")
    public String showCaptureForm(@PathVariable Long demandeurId, Model model) {
        model.addAttribute("demandeurId", demandeurId);
        return "photo-signature-capture";
    }

    /**
     * Display demandeur file with photo and signature
     */
    @GetMapping("/file/{demandeurId}")
    public String showDemandeurFile(@PathVariable Long demandeurId, Model model) {
        try {
            DemandeurFileDto file = photoSignatureService.getDemandeurFile(demandeurId);
            model.addAttribute("demandeurFile", file);
            return "demandeur-file";
        } catch (IllegalArgumentException e) {
            return "redirect:/backoffice/dashboard";
        }
    }

    /**
     * Save photo from webcam
     */
    @PostMapping("/photo/{demandeurId}")
    @ResponseBody
    public ResponseEntity<PhotoCaptureDto> savePhoto(
            @PathVariable Long demandeurId,
            @RequestParam(required = false) Long demandeId,
            @RequestParam String photoData,
            @RequestParam(defaultValue = "image/png") String format) {
        try {
            PhotoCaptureDto photo = photoSignatureService.savePhoto(demandeurId, demandeId, photoData, format);
            return ResponseEntity.ok(photo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Save signature
     */
    @PostMapping("/signature/{demandeurId}")
    @ResponseBody
    public ResponseEntity<SignatureCaptureDto> saveSignature(
            @PathVariable Long demandeurId,
            @RequestParam(required = false) Long demandeId,
            @RequestParam String signatureData,
            @RequestParam(defaultValue = "svg") String format) {
        try {
            SignatureCaptureDto signature = photoSignatureService.saveSignature(demandeurId, demandeId, signatureData, format);
            return ResponseEntity.ok(signature);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all photos for a demandeur
     */
    @GetMapping("/photos/{demandeurId}")
    @ResponseBody
    public ResponseEntity<List<PhotoCaptureDto>> getPhotos(@PathVariable Long demandeurId) {
        try {
            List<PhotoCaptureDto> photos = photoSignatureService.getPhotos(demandeurId);
            return ResponseEntity.ok(photos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all signatures for a demandeur
     */
    @GetMapping("/signatures/{demandeurId}")
    @ResponseBody
    public ResponseEntity<List<SignatureCaptureDto>> getSignatures(@PathVariable Long demandeurId) {
        try {
            List<SignatureCaptureDto> signatures = photoSignatureService.getSignatures(demandeurId);
            return ResponseEntity.ok(signatures);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Set photo as primary
     */
    @PutMapping("/photo/{photoId}/primary")
    @ResponseBody
    public ResponseEntity<PhotoCaptureDto> setPrimaryPhoto(@PathVariable Long photoId) {
        try {
            PhotoCaptureDto photo = photoSignatureService.setPrimaryPhoto(photoId);
            return ResponseEntity.ok(photo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete photo
     */
    @DeleteMapping("/photo/{photoId}")
    @ResponseBody
    public ResponseEntity<?> deletePhoto(@PathVariable Long photoId) {
        try {
            photoSignatureService.deletePhoto(photoId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete signature
     */
    @DeleteMapping("/signature/{signatureId}")
    @ResponseBody
    public ResponseEntity<?> deleteSignature(@PathVariable Long signatureId) {
        try {
            photoSignatureService.deleteSignature(signatureId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get demandeur file with photo and signature
     */
    @GetMapping("/api/file/{demandeurId}")
    @ResponseBody
    public ResponseEntity<DemandeurFileDto> getDemandeurFile(@PathVariable Long demandeurId) {
        try {
            DemandeurFileDto file = photoSignatureService.getDemandeurFile(demandeurId);
            return ResponseEntity.ok(file);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
