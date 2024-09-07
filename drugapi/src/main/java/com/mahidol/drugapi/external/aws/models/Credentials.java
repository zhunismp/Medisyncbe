package com.mahidol.drugapi.external.aws.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Credentials {
    private String accessKey;
    private String secretKey;
}
