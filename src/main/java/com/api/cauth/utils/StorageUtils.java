package com.api.cauth.utils;

import com.api.cauth.StorageEnum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class StorageUtils {
    private final static String pathStorageClientImage = "C:\\Users\\chrys\\Desktop\\images_cauth\\";
    private final static String pathStorageProcessedImage = "C:\\Users\\chrys\\Desktop\\images_processed_temp\\";

    public static String storeImage(String base64Image, StorageEnum storageEnum) throws IOException {
        String pathFinal = pathStorageClientImage + gerarNomeArquivo("client", "jpg");
        if (storageEnum == StorageEnum.PROCESSED_IMAGE) {
            pathFinal = pathStorageProcessedImage + gerarNomeArquivo("processed", "jpg");
        }
        File file = new File(pathFinal);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(CryptUtils.convertBase64ToBytes(base64Image));
            return pathFinal;
        } catch (IOException e) {
            throw e;
        }
    }

    private static String gerarNomeArquivo(String prefixo, String extensao) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        return prefixo + "_" + timestamp +"."+extensao;
    }
}
