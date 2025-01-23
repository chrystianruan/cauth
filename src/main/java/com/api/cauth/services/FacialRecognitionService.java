package com.api.cauth.services;

import com.api.cauth.StorageEnum;
import com.api.cauth.dtos.ClientFoundDTO;
import com.api.cauth.dtos.DataRecognitionDTO;
import com.api.cauth.dtos.FacialRecognitionDTO;
import com.api.cauth.entities.Client;
import com.api.cauth.exceptions.PermissaoException;
import com.api.cauth.repositories.ClientRepository;
import com.api.cauth.repositories.PhotoRepository;
import com.api.cauth.utils.ResponseUtils;
import com.api.cauth.utils.StorageUtils;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.FFmpegFrameFilter;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.*;
import org.opencv.face.EigenFaceRecognizer;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacialRecognitionService {

    private static Logger logger = LoggerFactory.getLogger(FacialRecognitionService.class)  ;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PhotoRepository photoRepository;

    public ClientFoundDTO validateClientImage(String imagePath) throws Exception {

        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            Loader.load(opencv_java.class);
            String cascadePath = "C:\\Users\\chrys\\Desktop\\projetos-pessoais\\cauth\\src\\main\\resources\\haarcascade_frontalface_alt.xml";
            CascadeClassifier faceDetector = new CascadeClassifier(cascadePath);

            String pathImage = StorageUtils.storeImage(imagePath, StorageEnum.PROCESSED_IMAGE);
            Mat imageRecebida = Imgcodecs.imread(pathImage);

            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(imageRecebida, faceDetections);

            if (faceDetections.toArray().length == 0) {
                throw new PermissaoException("Nenhuma face detectada na imagem.");
            }

            DataRecognitionDTO dataRecognition = createDataRecognitionDTO();

            EigenFaceRecognizer model = EigenFaceRecognizer.create();
            model.train(dataRecognition.getImages(), new MatOfInt(dataRecognition.getLabels().stream().mapToInt(i -> i).toArray()));

            for (Rect rect : faceDetections.toArray()) {
                Mat face = new Mat(imageRecebida, rect);
                Imgproc.cvtColor(face, face, Imgproc.COLOR_BGR2GRAY);
                Imgproc.resize(face, face, new Size(200, 200));
                Core.normalize(face, face, 0, 255, Core.NORM_MINMAX);

                int[] predictedLabel = new int[1];
                double[] confidence = new double[1];
                model.predict(face, predictedLabel, confidence);

                if (confidence[0] < 10000) {
                    return clientRepository.findOne((long) predictedLabel[0]).toClientFoundDTO();
                }
            }

            return null;
        } catch (PermissaoException e) {
            throw e;
        } catch (IOException e) {
            logger.info("Erro ao processar imagem.");
            throw e;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private DataRecognitionDTO createDataRecognitionDTO () {
        List<Mat> images = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();

        photoRepository.findAll().forEach(photo -> {
            Mat img = Imgcodecs.imread(photo.getPath());
            Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
            Imgproc.resize(img, img, new Size(200, 200));
            Core.normalize(img, img, 0, 255, Core.NORM_MINMAX);
            images.add(img);
            labels.add(photo.getClient().getId().intValue());
        });

        return new DataRecognitionDTO(labels, images);
    }


}
