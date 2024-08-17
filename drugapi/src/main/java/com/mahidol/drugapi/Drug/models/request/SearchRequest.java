package com.mahidol.drugapi.Drug.models.request;


import lombok.Getter;

import java.util.Optional;

@Getter
public class SearchRequest {
    private Optional<String> genericName;
}
