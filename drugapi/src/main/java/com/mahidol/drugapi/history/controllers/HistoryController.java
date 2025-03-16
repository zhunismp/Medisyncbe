package com.mahidol.drugapi.history.controllers;

import com.mahidol.drugapi.common.exceptions.BindingError;
import com.mahidol.drugapi.history.dtos.request.EditHistoryRequest;
import com.mahidol.drugapi.history.dtos.request.SearchHistoryRequest;
import com.mahidol.drugapi.history.services.HistoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

        if (request.getGroupId().isPresent())
            return new ResponseEntity<>(historyService.searchGroupHistory(request), HttpStatus.OK);
        else if (request.getDrugId().isPresent())
            return new ResponseEntity<>(historyService.searchDrugHistory(request), HttpStatus.OK);

        throw new IllegalArgumentException("You must specify exactly either drug group id or drug id");
    }

    @GetMapping("/searchhistories/noti")
    public ResponseEntity<?> searchAllWithPreferredDate(
            @RequestParam("preferredDate") @Valid @PastOrPresent LocalDateTime preferredDate
    ) {
        
    }

    @PostMapping("/edithistories")
    public ResponseEntity<?> edit(@RequestBody @Valid EditHistoryRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());
        historyService.editHistory(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
