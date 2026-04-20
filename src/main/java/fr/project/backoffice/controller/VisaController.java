package fr.project.backoffice.controller;

import fr.project.backoffice.dto.CreateVisaRequestDto;
import fr.project.backoffice.dto.VisaInfoDto;
import fr.project.backoffice.service.VisaRequestService;
import fr.project.backoffice.service.VisaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/backoffice")
public class VisaController {

    private final VisaService visaService;
    private final VisaRequestService visaRequestService;

    public VisaController(VisaService visaService, VisaRequestService visaRequestService) {
        this.visaService = visaService;
        this.visaRequestService = visaRequestService;
    }

    @GetMapping("/visas")
    public String visaDashboard(Model model) {
        List<VisaInfoDto> visas = visaService.getAllVisaInfo();
        long totalVisas = visas.size();
        long pendingRequests = visas.stream()
                .filter(visa -> "en_attente".equalsIgnoreCase(visa.getStatutDemande()))
                .count();
        long expiringSoon = visas.stream()
                .filter(visa -> visa.getDaysToExpire() <= 30)
                .count();
        long transformableCount = visas.stream()
                .filter(VisaInfoDto::getTransformable)
                .count();

        model.addAttribute("visas", visas);
        model.addAttribute("totalVisas", totalVisas);
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("expiringSoon", expiringSoon);
        model.addAttribute("transformableCount", transformableCount);

        return "visa-dashboard";
    }

    @PostMapping("/visas/add")
    public String addVisaRequest(CreateVisaRequestDto dto, RedirectAttributes redirectAttributes) {
        try {
            visaRequestService.createVisaRequest(dto);
            redirectAttributes.addFlashAttribute("success", "Visa request added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding visa request: " + e.getMessage());
        }
        return "redirect:/backoffice/visas";
    }
}
