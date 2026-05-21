package fr.project.backoffice.controller;

import fr.project.backoffice.dto.QRCodeDto;
import fr.project.backoffice.service.QRCodeService;
import fr.project.backoffice.repository.DemandeRepository;
import fr.project.backoffice.entity.Demande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Sprint 4: Controller for QR code generation and retrieval
 */
@Controller
@RequestMapping("/backoffice/qrcode")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private DemandeRepository demandeRepository;

    /**
     * Display QR code for a request
     */
    @GetMapping("/{demandeId}")
    public String showQRCode(@PathVariable Long demandeId, Model model) {
        Optional<Demande> demande = demandeRepository.findById(demandeId);
        if (demande.isEmpty()) {
            return "redirect:/backoffice/dashboard";
        }

        QRCodeDto qrCode = qrCodeService.generateQRCode(demande.get());
        model.addAttribute("qrCode", qrCode);
        model.addAttribute("demande", demande.get());

        return "qr-code-display";
    }

    /**
     * Get QR code as JSON (for API)
     */
    @GetMapping("/api/{demandeId}")
    @ResponseBody
    public ResponseEntity<QRCodeDto> getQRCode(@PathVariable Long demandeId) {
        Optional<Demande> demande = demandeRepository.findById(demandeId);
        if (demande.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        QRCodeDto qrCode = qrCodeService.generateQRCode(demande.get());
        return ResponseEntity.ok(qrCode);
    }

    /**
     * Get QR code by token (used by FrontOffice)
     */
    @GetMapping("/token/{token}")
    @ResponseBody
    public ResponseEntity<QRCodeDto> getQRCodeByToken(@PathVariable String token) {
        try {
            QRCodeDto qrCode = qrCodeService.getQRCodeByToken(token);
            return ResponseEntity.ok(qrCode);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Regenerate QR code
     */
    @PostMapping("/regenerate/{demandeId}")
    @ResponseBody
    public ResponseEntity<QRCodeDto> regenerateQRCode(@PathVariable Long demandeId) {
        try {
            QRCodeDto qrCode = qrCodeService.regenerateQRCode(demandeId);
            return ResponseEntity.ok(qrCode);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Download QR code image
     */
    @GetMapping("/download/{demandeId}")
    public ResponseEntity<byte[]> downloadQRCode(@PathVariable Long demandeId) {
        Optional<Demande> demande = demandeRepository.findById(demandeId);
        if (demande.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        QRCodeDto qrCode = qrCodeService.generateQRCode(demande.get());
        
        // Extract binary data from Base64
        String base64Image = qrCode.getQrCodeImage().split(",")[1];
        byte[] imageData = java.util.Base64.getDecoder().decode(base64Image);

        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .header("Content-Disposition", "attachment; filename=qr_code_" + demandeId + ".png")
                .body(imageData);
    }
}
