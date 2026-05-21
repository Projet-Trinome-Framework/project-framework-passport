package fr.project.backoffice.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import fr.project.backoffice.dto.QRCodeDto;
import fr.project.backoffice.entity.QRCodeData;
import fr.project.backoffice.entity.Demande;
import fr.project.backoffice.repository.QRCodeDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Sprint 4: Service for QR code generation and management
 */
@Service
@Transactional
public class QRCodeService {
    private static final Logger logger = LoggerFactory.getLogger(QRCodeService.class);

    @Autowired
    private QRCodeDataRepository qrCodeDataRepository;

    private static final int QR_CODE_SIZE = 300;

    /**
     * Generate QR code for a new request
     */
    @Transactional
    public QRCodeDto generateQRCode(Demande demande) {
        // Check if QR code already exists
        Optional<QRCodeData> existingQR = qrCodeDataRepository.findByDemande(demande);
        if (existingQR.isPresent()) {
            return mapToDto(existingQR.get());
        }

        try {
            // Create unique token
            String requestToken = UUID.randomUUID().toString();

            // Create QR code value (JSON string with request information)
            Map<String, Object> qrData = new HashMap<>();
            qrData.put("token", requestToken);
            qrData.put("demandeId", demande.getId());
            qrData.put("demandeurId", demande.getDemandeur().getId());
            qrData.put("generatedAt", LocalDateTime.now());

            ObjectMapper mapper = new ObjectMapper();
            String qrValue = mapper.writeValueAsString(qrData);

            // Generate QR code image
            byte[] qrImage = generateQRCodeImage(qrValue);

            // Save to database
            QRCodeData qrCodeData = new QRCodeData();
            qrCodeData.setDemande(demande);
            qrCodeData.setQrCodeValue(qrValue);
            qrCodeData.setQrCodeImage(qrImage);
            qrCodeData.setRequestToken(requestToken);
            qrCodeData.setGeneratedDate(LocalDateTime.now());
            qrCodeData.setIsActive(true);

            QRCodeData saved = qrCodeDataRepository.save(qrCodeData);
            logger.info("QR code generated for request: {}", demande.getId());

            return mapToDto(saved);
        } catch (Exception e) {
            logger.error("Error generating QR code for request: {}", demande.getId(), e);
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    /**
     * Get QR code by request token (used by FrontOffice)
     */
    public QRCodeDto getQRCodeByToken(String requestToken) {
        Optional<QRCodeData> qrCode = qrCodeDataRepository.findByRequestToken(requestToken);
        if (qrCode.isEmpty()) {
            throw new IllegalArgumentException("QR code token not found: " + requestToken);
        }
        return mapToDto(qrCode.get());
    }

    /**
     * Get QR code by request ID
     */
    public QRCodeDto getQRCodeByRequestId(Long demandeId) {
        Optional<QRCodeData> qrCode = qrCodeDataRepository.findById(demandeId);
        if (qrCode.isEmpty()) {
            throw new IllegalArgumentException("QR code not found for request: " + demandeId);
        }
        return mapToDto(qrCode.get());
    }

    /**
     * Regenerate QR code for existing request
     */
    @Transactional
    public QRCodeDto regenerateQRCode(Long demandeId) {
        Optional<QRCodeData> existingQR = qrCodeDataRepository.findById(demandeId);
        if (existingQR.isEmpty()) {
            throw new IllegalArgumentException("QR code not found for request: " + demandeId);
        }

        QRCodeData qrCode = existingQR.get();
        qrCodeDataRepository.delete(qrCode);

        return generateQRCode(qrCode.getDemande());
    }

    /**
     * Deactivate QR code
     */
    @Transactional
    public void deactivateQRCode(Long qrCodeId) {
        Optional<QRCodeData> qrCode = qrCodeDataRepository.findById(qrCodeId);
        if (qrCode.isPresent()) {
            qrCode.get().setIsActive(false);
            qrCodeDataRepository.save(qrCode.get());
            logger.info("QR code deactivated: {}", qrCodeId);
        }
    }

    /**
     * Generate QR code image using ZXing
     */
    private byte[] generateQRCodeImage(String text) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    /**
     * Map entity to DTO
     */
    private QRCodeDto mapToDto(QRCodeData qrCodeData) {
        String base64Image = Base64.getEncoder().encodeToString(qrCodeData.getQrCodeImage());
        return QRCodeDto.builder()
                .id(qrCodeData.getId())
                .demandeId(qrCodeData.getDemande().getId())
                .qrCodeImage("data:image/png;base64," + base64Image)
                .requestToken(qrCodeData.getRequestToken())
                .requestNumber(String.valueOf(qrCodeData.getDemande().getId()))
                .demandeurName(qrCodeData.getDemande().getDemandeur().getNom() + " " +
                        qrCodeData.getDemande().getDemandeur().getPrenom())
                .build();
    }
}
