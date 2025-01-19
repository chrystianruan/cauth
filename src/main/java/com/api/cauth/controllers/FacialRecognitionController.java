package com.api.cauth.controllers;

import com.api.cauth.dtos.FacialRecognitionDTO;
import com.api.cauth.services.FacialRecognitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/facial")
@RequiredArgsConstructor
public class FacialRecognitionController {

    private final FacialRecognitionService facialRecognitionService;

    @PostMapping
    public ResponseEntity<?> getFacialRecognition(@RequestBody FacialRecognitionDTO facialRecognitionDTO) {
        try {
            // Chama o serviço para detectar rostos
            String result = facialRecognitionService.detectAndRespond(facialRecognitionDTO);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao processar a imagem: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}