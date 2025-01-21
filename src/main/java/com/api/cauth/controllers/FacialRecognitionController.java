package com.api.cauth.controllers;

import com.api.cauth.dtos.FacialRecognitionDTO;
import com.api.cauth.utils.CryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/auth/facial")
@RequiredArgsConstructor
public class FacialRecognitionController {
    @PostMapping()
    public ResponseEntity<String> getFacialRecognition(@RequestBody String image) {
        byte[] imageBytes = CryptUtils.convertBase64ToBytes(image);
        return ResponseEntity.ok("Facial Recognition");
    }
}