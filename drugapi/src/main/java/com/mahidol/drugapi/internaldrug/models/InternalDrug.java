package com.mahidol.drugapi.internaldrug.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InternalDrug {
    @JsonProperty("id")
    @SerializedName("id")
    private UUID id;

    @SerializedName("generic_name")
    @JsonProperty("generic_name")
    private String genericName;

    @JsonProperty("dosage_form")
    @SerializedName("dosage_form")
    private String dosageForm;

    @JsonProperty("strength_unit")
    @SerializedName("strength_unit")
    private String strengthUnit;

    @JsonProperty("drug_info")
    @SerializedName("drug_info")
    private InternalDrugInfo internalDrugInfo;
}
