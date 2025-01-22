package com.api.cauth.controllers;

import com.api.cauth.StorageEnum;
import com.api.cauth.dtos.FacialRecognitionDTO;
import com.api.cauth.repositories.ClientRepository;
import com.api.cauth.services.FacialRecognitionService;
import com.api.cauth.utils.ResponseUtils;
import com.api.cauth.utils.StorageUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.openblas.global.openblas;
import org.bytedeco.openblas.global.openblas_nolapack;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.*;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;

import org.opencv.objdetect.CascadeClassifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facial-recognition")
@RequiredArgsConstructor
public class FacialRecognitionController {

    private final FacialRecognitionService facialRecognitionService;
    private final ClientRepository clientRepository;

    @PostMapping("")
    public ResponseEntity<Map<String, String>> getFacialRecognition(@RequestBody String image) {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            String cascadePath = "path/cauth/src/main/resource/shaarcascade_frontalface_alt.xml";
            CascadeClassifier faceDetector = new CascadeClassifier(cascadePath);

            List<FacialRecognitionDTO> clientsWithImagesTrained = facialRecognitionService.createListClientsTrainingImages();

            List<Mat> trainingImages = new ArrayList<>();
            Mat labels = new Mat(clientsWithImagesTrained.size(), 1, CvType.CV_32SC1);
            int i = 0;

            for (FacialRecognitionDTO facialRecognitionDTO : clientsWithImagesTrained) {
                labels.put(i, (int) (long) facialRecognitionDTO.getClient().getId());
                trainingImages.add(facialRecognitionDTO.getTrainingImage());
                i++;
            }

            FaceRecognizer recognizer = LBPHFaceRecognizer.create();
            recognizer.train(trainingImages, labels);

            String testImagePath = StorageUtils.storeImage(image, StorageEnum.PROCESSED_IMAGE);
            Mat testImage = facialRecognitionService.preprocessImage(testImagePath);

            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(testImage, faceDetections);

            if (faceDetections.toArray().length == 0) {
                System.out.println("Nenhum rosto encontrado na imagem recebida.");
                return null;
            }

            int[] label = new int[1];
            double[] confidence = new double[1];
            recognizer.predict(testImage, label, confidence);

            if (confidence[0] < 80.0) {
                System.out.println("Reconhecido como: " +clientRepository.findById((long)label[0])+" com confiança: "+ confidence[0]);
            } else {
                System.out.println("Nenhuma correspondência encontrada.");
            }

            return ResponseEntity.ok().body(ResponseUtils.makeMessage("teste"));

        }
        catch (IOException exception) {
            return ResponseEntity.internalServerError().body(ResponseUtils.makeMessage("Erro ao gravar arquivo processado"));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}