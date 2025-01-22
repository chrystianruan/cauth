package com.api.cauth.dtos;

import com.api.cauth.entities.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.opencv.core.Mat;
import org.springframework.stereotype.Component;


@Component
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacialRecognitionDTO {
    private Client client;
    private Mat trainingImage;
}
