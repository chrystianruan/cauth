package com.api.cauth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.opencv.core.Mat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataRecognitionDTO {
    private List<Integer> labels;
    private List<Mat> images;
}
