package com.api.cauth.controllers;

import com.api.cauth.StorageEnum;
import com.api.cauth.dtos.FacialRecognitionDTO;
import com.api.cauth.repositories.ClientRepository;
import com.api.cauth.services.FacialRecognitionService;
import com.api.cauth.utils.ResponseUtils;
import com.api.cauth.utils.StorageUtils;
import lombok.RequiredArgsConstructor;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.*;

import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
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
            Loader.load(opencv_java.class);
            String cascadePath = "C:\\Users\\chrys\\Desktop\\projetos-pessoais\\cauth\\src\\main\\resources\\haarcascade_frontalface_alt.xml";
            CascadeClassifier faceDetector = new CascadeClassifier(cascadePath);

            String pathImage = StorageUtils.storeImage(image, StorageEnum.PROCESSED_IMAGE);
            Mat imageRecebida = Imgcodecs.imread(pathImage);

            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(imageRecebida, faceDetections);

            if (faceDetections.toArray().length == 0) {
                return ResponseEntity.ok().body(ResponseUtils.makeMessage("Nenhum rosto encontrado."));
            }

            List<Mat> images = new ArrayList<>();
            List<Integer> labels = new ArrayList<>();

            clientRepository.findAll().forEach(client -> {
                Mat img = Imgcodecs.imread(client.getPathImage(), Imgcodecs.IMREAD_GRAYSCALE);
                Imgproc.resize(img, img, new Size(200, 200));
                images.add(img);
                labels.add(client.getId().intValue());
            });

            FaceRecognizer model = LBPHFaceRecognizer.create();
            model.train(images, new MatOfInt(labels.stream().mapToInt(i -> i).toArray()));

            for (Rect rect : faceDetections.toArray()) {
                Mat face = new Mat(imageRecebida, rect);
                Imgproc.cvtColor(face, face, Imgproc.COLOR_BGR2GRAY);
                Imgproc.resize(face, face, new Size(200, 200));

                int[] predictedLabel = new int[1];
                double[] confidence = new double[1];
                model.predict(face, predictedLabel, confidence);

                if (confidence[0] < 95) {
                    return ResponseEntity.ok().body(ResponseUtils.makeMessage("Identificado como cliente: " + clientRepository.findOne((long) predictedLabel[0]).getName()));
                } else {
                    return ResponseEntity.ok().body(ResponseUtils.makeMessage("Rosto não reconhecido."));
                }
            }

            return ResponseEntity.ok().body(ResponseUtils.makeMessage("Reconhecimento concluído."));
        }
        catch (IOException exception) {
            return ResponseEntity.internalServerError().body(ResponseUtils.makeMessage("Erro ao processar imagem."));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}