package com.api.cauth.services;

import com.api.cauth.dtos.ClientDTO;
import com.api.cauth.entities.Client;
import com.api.cauth.entities.Permission;
import com.api.cauth.exceptions.PermissaoException;
import com.api.cauth.repositories.ClientRepository;
import com.api.cauth.repositories.PermissionRepository;
import com.api.cauth.repositories.ProductRepository;
import com.api.cauth.utils.CryptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    private final Logger log = LoggerFactory.getLogger(ClientService.class);

    public void save(String accessKey, ClientDTO clientDTO) throws Exception {
       try {
           if (clientRepository.existsByEmail(clientDTO.getEmail())) {
               throw new PermissaoException("Email already exists");
           }
           Client client = new Client();
           client.setName(clientDTO.getName());
           client.setEmail(clientDTO.getEmail());
           client.setProduct(productRepository.findByAccessKey(accessKey));

           String pathFinal = "/home/chrystian/Desktop/images_cauth/"+gerarNomeArquivo("user", "jpg");
           client.setPathImage(pathFinal);

           storeImage(clientDTO.getFacialImage(), pathFinal);

           clientRepository.save(client);
           clientDTO.getPermissions().forEach(permission -> {
               Permission permissionEntity = new Permission();
               permissionEntity.setAction(permission);
               permissionEntity.setClient(client);
               permissionRepository.save(permissionEntity);
           });
       } catch (PermissaoException e) {
           throw e;
       } catch (Exception e) {
           log.error(e.getMessage());
           throw new Exception(e);
       }

    }

    public void storeImage(String base64Image, String pathFinal) {
        File file = new File(pathFinal);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(CryptUtils.convertBase64ToBytes(base64Image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String gerarNomeArquivo(String prefixo, String extensao) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return prefixo + "_" + timestamp +"."+extensao;
    }
}
