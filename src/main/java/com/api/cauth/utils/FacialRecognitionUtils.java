package com.api.cauth.utils;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FacialRecognitionUtils {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String HAAR_CASCADE_PATH = "src/main/resources/haarcascade_frontalface_alt.xml";

    public List<Rect> detectFaces(byte[] imageBytes) {
        Mat image = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);
        CascadeClassifier faceDetector = new CascadeClassifier(HAAR_CASCADE_PATH);
        if (faceDetector.empty()) {
            throw new RuntimeException("Falha ao carregar o modelo Haar Cascade");
        }

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        return faceDetections.toList();
    }

    public static Mat highlightFaces(Mat image, List<Rect> faces) {
        for (Rect face : faces) {
            Imgproc.rectangle(image, face.tl(), face.br(), new Scalar(0, 255, 0), 3);
        }

        return image;
    }
}

