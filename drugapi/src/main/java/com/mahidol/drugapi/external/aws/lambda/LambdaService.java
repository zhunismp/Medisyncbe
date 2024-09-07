package com.mahidol.drugapi.external.aws.lambda;

import com.mahidol.drugapi.common.dtos.BaseSearchRequest;
import com.mahidol.drugapi.common.exceptions.InternalServerError;
import com.mahidol.drugapi.common.utils.JsonParser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class LambdaService {
    private static final Logger logger = LoggerFactory.getLogger(LambdaService.class);
    private String lambdaFunction;
    private Region region;
    private LambdaClient lambdaClient;

    public LambdaService(String lambdaFunction, Region region, String accesskey, String secretkey) {
        this.lambdaFunction = lambdaFunction;
        this.region = region;

        AwsCredentials awsCredentials = AwsBasicCredentials.create(accesskey, secretkey);
        this.lambdaClient = LambdaClient.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    public <R, T extends BaseSearchRequest> List<R> get(T request, Class<R> tClass) {
        try {
            InvokeRequest invokeRequest = InvokeRequest.builder()
                    .functionName(lambdaFunction)
                    .payload(SdkBytes.fromUtf8String(request.toString()))
                    .build();

            InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);
            String responsePayload = invokeResponse.payload().asUtf8String();

            logger.info("Request payload: " + request);
            logger.info("Response payload: " + responsePayload);

            return JsonParser.parseJsonList(responsePayload, tClass).getOrElseThrow(e -> e);
        } catch (Exception e) {
            throw new InternalServerError("Something went wrong while calling internal drug service.");
        }
    }
}
