package com.mahidol.drugapi.druggroup.controllers;

import com.mahidol.drugapi.common.exceptions.BindingError;
import com.mahidol.drugapi.druggroup.dtos.request.AddDrugRequest;
import com.mahidol.drugapi.druggroup.dtos.request.CreateGroupRequest;
import com.mahidol.drugapi.druggroup.dtos.request.SearchGroupRequest;
import com.mahidol.drugapi.druggroup.dtos.response.SearchGroupResponse;
import com.mahidol.drugapi.druggroup.services.DrugGroupService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class DrugGroupController {
    private final DrugGroupService drugGroupService;

    public DrugGroupController(DrugGroupService drugGroupService) {
        this.drugGroupService = drugGroupService;
    }

    @PostMapping("/groups")
    public ResponseEntity<?> create(@RequestBody @Valid CreateGroupRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());

        drugGroupService.create(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/searchgroups")
    public ResponseEntity<?> search(@RequestBody @Valid SearchGroupRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());
        SearchGroupResponse response = drugGroupService.search(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/groups")
    public ResponseEntity<?> remove(
            @RequestParam
            @NotNull(message = "Drug group id should not be null")
            UUID drugGroupId,

            @RequestParam
            @NotNull(message = "User id should not be null")
            UUID userId,

            @RequestParam
            @NotNull(message = "isRemoveDrug flag should not be null")
            Boolean isRemoveDrug
    ) {
        drugGroupService.remove(drugGroupId, userId, isRemoveDrug);
        return new ResponseEntity<>(Map.of("drugGroupId", drugGroupId), HttpStatus.OK);
    }

    @PostMapping("/groups/drugs")
    public ResponseEntity<?> addDrugs(@RequestBody @Valid AddDrugRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new BindingError(bindingResult.getFieldErrors());

        drugGroupService.addDrugsToGroup(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
