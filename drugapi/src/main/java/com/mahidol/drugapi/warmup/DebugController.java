package com.mahidol.drugapi.warmup;

import com.mahidol.drugapi.external.aws.s3.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {
    private final S3Service s3Service;

    public DebugController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return new ResponseEntity<>(Map.of("ping", "pong"), HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> fileUploadCheck(@RequestPart MultipartFile file) {
        String url = s3Service.uploadFile("medisync-test-monkey", "debug", file);
        return ResponseEntity.ok(Map.of("url", url));
    }

    // TODO: Fixed permission in AWS
    @DeleteMapping("/delete")
    public ResponseEntity<?> fileDeleteCheck() {
        s3Service.deleteFile("medisync-test-monkey", "debug");
        return ResponseEntity.ok("debug file was removed");
    }
    

}
