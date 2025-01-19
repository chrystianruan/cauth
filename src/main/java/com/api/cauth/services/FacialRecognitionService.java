package com.api.cauth.services;

import com.api.cauth.dtos.FacialRecognitionDTO;
import com.api.cauth.utils.FacialRecognitionUtils;
import lombok.RequiredArgsConstructor;
import org.opencv.core.Rect;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacialRecognitionService {

    private final FacialRecognitionUtils facialRecognitionUtils;

    public String detectAndRespond(FacialRecognitionDTO facialRecognitionDTO) throws Exception {
        if (facialRecognitionDTO == null || facialRecognitionDTO.client == null || facialRecognitionDTO.client.facialImage == null) {
            throw new IllegalArgumentException("Dados do cliente ou imagem facial estão ausentes.");
        }
        byte[] imageBytes = Base64.getDecoder().decode(facialRecognitionDTO.client.facialImage);
        List<Rect> detectedFaces = facialRecognitionUtils.detectFaces(imageBytes);
        if (detectedFaces.isEmpty()) {
            return "Nenhum rosto detectado.";
        }

        return detectedFaces.size() + " rosto(s) detectado(s).";
    }
}