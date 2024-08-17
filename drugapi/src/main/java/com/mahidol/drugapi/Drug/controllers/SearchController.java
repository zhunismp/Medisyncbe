package com.mahidol.drugapi.Drug.controllers;

import com.mahidol.drugapi.Drug.entites.InternalDrug;
import com.mahidol.drugapi.Drug.models.request.SearchRequest;
import com.mahidol.drugapi.Drug.services.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/drugs")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        List<InternalDrug> result = request.getGenericName().map(searchService::searchInternalDrugByGenericName).orElse(searchService.searchInternalDrug());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
