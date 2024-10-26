package com.mahidol.drugapi.external.aws.s3;

import com.mahidol.drugapi.common.exceptions.InternalServerError;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;

@Service
public class S3Service {
    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    public S3Service(Region region, String accessKey, String secretKey) {
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        this.s3Presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public String uploadFile(String bucketName, String key, MultipartFile file) {
        try {
            if (!validateFileContent(file.getContentType()))
                throw new Exception("Required format jpeg or png but got: " + file.getContentType());

            File targetFile = fileTransform(key, file);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(targetFile));

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofHours(1))
                    .build();

            return s3Presigner.presignGetObject(presignRequest).url().toString();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public Optional<String> getUrl(String bucketName, String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headObjectRequest);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofHours(1))
                    .build();

            return Optional.of(s3Presigner.presignGetObject(presignRequest).url().toString());
        } catch (S3Exception e) {
            if (e.statusCode() == 404) return Optional.empty();
            else throw e;
        }
    }


    public void deleteFile(String bucketName, String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    private boolean isExists(String bucketName, String key) {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);

        return listObjectsV2Response.contents()
                .stream()
                .anyMatch(s3ObjectSummary -> s3ObjectSummary.getValueForField("key", String.class).map(v -> v.equals(key)).orElse(false));
    }

    private File fileTransform(String fileName, MultipartFile file) throws IOException {
        File convFile = new File(fileName);
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        return convFile;
    }

    private Boolean validateFileContent(String contentType) {
        return Set.of("image/jpeg", "image/png").contains(contentType);
    }
}
