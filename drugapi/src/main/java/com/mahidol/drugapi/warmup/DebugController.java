package com.mahidol.drugapi.warmup;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @CrossOrigin(origins = "*")
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return new ResponseEntity<>(Map.of("ping", "pong"), HttpStatus.OK);
    }
}
