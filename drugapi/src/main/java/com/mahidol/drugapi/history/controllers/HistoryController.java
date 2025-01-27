package com.mahidol.drugapi.history.controllers;

import com.mahidol.drugapi.common.exceptions.BindingError;
import com.mahidol.drugapi.history.dtos.request.EditHistoryRequest;
import com.mahidol.drugapi.history.dtos.request.SearchHistoryRequest;
import com.mahidol.drugapi.history.dtos.response.SearchHistoryResponse;
import com.mahidol.drugapi.history.services.HistoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping("/searchhistories")
    public ResponseEntity<?> search(@RequestBody @Valid SearchHistoryRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());
        SearchHistoryResponse response = historyService.search(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/edithistories")
    public ResponseEntity<?> edit(@RequestBody @Valid EditHistoryRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());
        historyService.editHistory(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
