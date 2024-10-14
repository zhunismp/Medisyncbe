package com.mahidol.drugapi.internaldrug.services;

import com.mahidol.drugapi.external.aws.lambda.LambdaService;
import com.mahidol.drugapi.internaldrug.dto.request.InternalDrugSearchRequest;
import com.mahidol.drugapi.internaldrug.dto.response.InternalDrugSearchResponse;
import com.mahidol.drugapi.internaldrug.models.InternalDrug;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternalDrugService {

    private final LambdaService lambdaService;

    public InternalDrugService(@Qualifier("internalDrugLambdaService") LambdaService lambdaService) {
        this.lambdaService = lambdaService;
    }

    public InternalDrugSearchResponse getInternalDrug(InternalDrugSearchRequest request) {
        // TODO: Migrate pagination inside server instead of Lambda
        List<InternalDrug> internalDrugs = lambdaService.get(request, InternalDrug.class);

        return new InternalDrugSearchResponse(
                internalDrugs,
                internalDrugs.size()
        );
    }
}
