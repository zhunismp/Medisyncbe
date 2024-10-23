package com.mahidol.drugapi.external.aws.s3.configurations;

import com.mahidol.drugapi.external.aws.s3.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
public class S3ServiceConfig {
    @Value("${aws.accesskey}")
    private String accesskey;
    @Value("${aws.secretkey}")
    private String secretkey;

    @Bean
    public S3Service s3Service() {
        return new S3Service(Region.AP_SOUTHEAST_1, accesskey, secretkey);
    }
}
