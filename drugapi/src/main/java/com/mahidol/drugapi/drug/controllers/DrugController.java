package com.mahidol.drugapi.drug.controllers;

import com.mahidol.drugapi.common.exceptions.BindingError;
import com.mahidol.drugapi.drug.dtos.request.CreateDrugRequest;
import com.mahidol.drugapi.drug.dtos.request.SearchDrugRequest;
import com.mahidol.drugapi.drug.dtos.request.UpdateDrugRequest;
import com.mahidol.drugapi.drug.dtos.response.SearchDrugResponse;
import com.mahidol.drugapi.drug.services.DrugService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class DrugController {
    private final DrugService drugService;

    public DrugController(DrugService drugService) {
        this.drugService = drugService;
    }

    @PostMapping("/searchdrugs")
    public ResponseEntity<?> search(@RequestBody @Valid SearchDrugRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());
        SearchDrugResponse response = drugService.search(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/drugs")
    public ResponseEntity<?> add(@RequestBody @Valid CreateDrugRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());

        drugService.add(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/drugs")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateDrugRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());

        drugService.update(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/drugs")
    public ResponseEntity<?> remove(@RequestParam UUID userId, @RequestParam UUID drugId) {
        drugService.remove(userId, drugId);
        return new ResponseEntity<>(Map.of("drugId", drugId), HttpStatus.ACCEPTED);
    }

}
