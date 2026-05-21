package fr.project.backoffice.controller;

import fr.project.backoffice.dto.SearchResultDto;
import fr.project.backoffice.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Sprint 4: Controller for searching requests by number or passport
 */
@Controller
@RequestMapping("/backoffice/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * Display search form
     */
    @GetMapping
    public String showSearchForm(Model model) {
        return "search-form";
    }

    /**
     * Search by request number
     */
    @GetMapping("/request/{requestNumber}")
    @ResponseBody
    public ResponseEntity<List<SearchResultDto>> searchByRequestNumber(
            @PathVariable String requestNumber) {
        List<SearchResultDto> results = searchService.searchByRequestNumber(requestNumber);
        return ResponseEntity.ok(results);
    }

    /**
     * Search by passport
     */
    @GetMapping("/passport/{passeportNumber}")
    @ResponseBody
    public ResponseEntity<List<SearchResultDto>> searchByPassport(
            @PathVariable String passeportNumber) {
        List<SearchResultDto> results = searchService.searchByPassport(passeportNumber);
        return ResponseEntity.ok(results);
    }

    /**
     * Advanced search with filters
     */
    @PostMapping("/advanced")
    @ResponseBody
    public ResponseEntity<List<SearchResultDto>> advancedSearch(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String typeVisa) {
        List<SearchResultDto> results = searchService.searchAdvanced(searchTerm, statut, typeVisa);
        return ResponseEntity.ok(results);
    }

    /**
     * Display search results page
     */
    @PostMapping("/results")
    public String showSearchResults(
            @RequestParam String searchTerm,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String typeVisa,
            Model model) {
        List<SearchResultDto> results = searchService.searchAdvanced(searchTerm, statut, typeVisa);
        model.addAttribute("results", results);
        model.addAttribute("searchTerm", searchTerm);

        return "search-results";
    }
}
