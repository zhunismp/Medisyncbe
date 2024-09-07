package com.mahidol.drugapi.internaldrug.controllers;

import com.mahidol.drugapi.common.exceptions.BindingError;
import com.mahidol.drugapi.internaldrug.dto.request.InternalDrugSearchRequest;
import com.mahidol.drugapi.internaldrug.dto.response.InternalDrugSearchResponse;
import com.mahidol.drugapi.internaldrug.services.InternalDrugService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/internaldrugs")
public class InternalDrugController {
    private final InternalDrugService internalDrugService;

    public InternalDrugController(InternalDrugService internalDrugService) {
        this.internalDrugService = internalDrugService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> getInternalDrug(@RequestBody @Valid InternalDrugSearchRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());
        InternalDrugSearchResponse response = internalDrugService.getInternalDrug(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
