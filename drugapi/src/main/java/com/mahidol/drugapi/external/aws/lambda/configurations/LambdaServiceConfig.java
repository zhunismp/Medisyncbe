package com.mahidol.drugapi.external.aws.lambda.configurations;

import com.mahidol.drugapi.external.aws.lambda.LambdaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
public class LambdaServiceConfig {
    @Value("${aws.accesskey}")
    private String accesskey;
    @Value("${aws.secretkey}")
    private String secretkey;

    @Bean
    @Qualifier("internalDrugLambdaService")
    public LambdaService internalDrugLambdaService() {
        return new LambdaService("InternalDrugService", Region.AP_SOUTHEAST_1, accesskey, secretkey);
    }
}
