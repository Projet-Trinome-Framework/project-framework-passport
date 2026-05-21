package fr.project.backoffice.controller;

import fr.project.backoffice.dto.QRCodeDto;
import fr.project.backoffice.dto.SearchResultDto;
import fr.project.backoffice.dto.DemandeurSearchResultDto;
import fr.project.backoffice.service.QRCodeService;
import fr.project.backoffice.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sprint 4: FrontOffice REST API
 * This API is used by the new FrontOffice application (Single Page Application)
 * Allows demandeurs to view their requests and scan QR codes
 */
@RestController
@RequestMapping("/api/frontoffice")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FrontOfficeController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private SearchService searchService;

    /**
     * Scan QR code and get request details
     */
    @GetMapping("/qr/{token}")
    public ResponseEntity<?> scanQRCode(@PathVariable String token) {
        try {
            QRCodeDto qrCode = qrCodeService.getQRCodeByToken(token);
            return ResponseEntity.ok(qrCode);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "QR code invalid or expired");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Search for request by number
     */
    @GetMapping("/search/request/{requestNumber}")
    public ResponseEntity<List<SearchResultDto>> searchByRequestNumber(@PathVariable String requestNumber) {
        List<SearchResultDto> results = searchService.searchByRequestNumber(requestNumber);
        return ResponseEntity.ok(results);
    }

    /**
     * Search for requests by passport number
     */
    @GetMapping("/search/passport/{passeportNumber}")
    public ResponseEntity<List<SearchResultDto>> searchByPassport(@PathVariable String passeportNumber) {
        List<SearchResultDto> results = searchService.searchByPassport(passeportNumber);
        return ResponseEntity.ok(results);
    }

    /**
     * Advanced search with filters
     */
    @PostMapping("/search/advanced")
    public ResponseEntity<List<SearchResultDto>> advancedSearch(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String typeVisa) {
        List<SearchResultDto> results = searchService.searchAdvanced(searchTerm, statut, typeVisa);
        return ResponseEntity.ok(results);
    }

    /**
     * Get status of a request
     */
    @GetMapping("/status/{demandeId}")
    public ResponseEntity<?> getRequestStatus(@PathVariable Long demandeId) {
        try {
            List<SearchResultDto> results = searchService.searchByRequestNumber(String.valueOf(demandeId));
            if (results.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Request not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(results.get(0));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "FrontOffice API");
        return ResponseEntity.ok(response);
    }

    /**
     * API version information
     */
    @GetMapping("/version")
    public ResponseEntity<Map<String, String>> version() {
        Map<String, String> response = new HashMap<>();
        response.put("version", "1.0.0");
        response.put("sprint", "Sprint 4");
        response.put("api", "FrontOffice REST API");
        return ResponseEntity.ok(response);
    }
}
