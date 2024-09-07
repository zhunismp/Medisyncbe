package com.mahidol.drugapi.internaldrug.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InternalDrugInfo {
    @JsonProperty("during_use")
    @SerializedName("during_use")
    private List<String> duringUse;
    @JsonProperty("storage")
    @SerializedName("storage")
    private List<String> storage;
    @JsonProperty("usage")
    @SerializedName("usage")
    private List<String> usage;
    @JsonProperty("warning")
    @SerializedName("warning")
    private List<String> warning;
    @JsonProperty("avoidance")
    @SerializedName("avoidance")
    private List<String> avoidance;
    @JsonProperty("forgetting")
    @SerializedName("forgetting")
    private List<String> forget;
    @JsonProperty("overdose")
    @SerializedName("overdose")
    private List<String> overdose;
    @JsonProperty("danger_side_effect")
    @SerializedName("danger_side_effect")
    private List<String> dangerSideEffect;
    @JsonProperty("side_effect")
    @SerializedName("side_effect")
    private List<String> sideEffect;
}
