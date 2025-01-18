package com.api.cauth.controllers;

import com.api.cauth.dtos.FacialRecognitionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/facial")
@RequiredArgsConstructor
public class FacialRecognitionController {
    @PostMapping()
    public ResponseEntity<String> getFacialRecognition(@RequestBody FacialRecognitionDTO facialRecognitionDTO) {
        return ResponseEntity.ok("Facial Recognition");
    }
}