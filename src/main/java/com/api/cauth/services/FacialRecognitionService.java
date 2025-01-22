package com.api.cauth.services;

import com.api.cauth.dtos.FacialRecognitionDTO;
import com.api.cauth.entities.Client;
import com.api.cauth.repositories.ClientRepository;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacialRecognitionService {

    private static Logger logger = LoggerFactory.getLogger(FacialRecognitionService.class)  ;
    @Autowired
    private ClientRepository clientRepository;

    public List<FacialRecognitionDTO> createListClientsTrainingImages () {
        List<FacialRecognitionDTO> facialRecognitionDTOs = new ArrayList<>();
        for (Client client : clientRepository.findAll()) {
            FacialRecognitionDTO facialRecognitionDTO = new FacialRecognitionDTO();
            facialRecognitionDTO.setClient(client);
            facialRecognitionDTO.setTrainingImage(preprocessImage(client.getPathImage()));
        }
        return facialRecognitionDTOs;
    }

    public Mat preprocessImage(String imagePath) {
        Mat image = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_GRAYSCALE);
        Imgproc.resize(image, image, new Size(200, 200));
        return image;
    }

}
