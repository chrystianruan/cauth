package com.api.cauth.services;

import com.api.cauth.StorageEnum;
import com.api.cauth.dtos.ClientDTO;
import com.api.cauth.entities.Client;
import com.api.cauth.entities.Permission;
import com.api.cauth.entities.Photo;
import com.api.cauth.exceptions.PermissaoException;
import com.api.cauth.repositories.ClientRepository;
import com.api.cauth.repositories.PermissionRepository;
import com.api.cauth.repositories.PhotoRepository;
import com.api.cauth.repositories.ProductRepository;
import com.api.cauth.utils.StorageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PhotoRepository photoRepository;

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
           if (clientDTO.getFacialImages().size() < 7) {
               throw new PermissaoException("You must send at least 7 photos");
           }
           clientRepository.save(client);
           savePhotos(clientDTO.getFacialImages(), client);
           savePermissions(clientDTO.getPermissions(), client);

       } catch (IOException | PermissaoException e) {
           log.error(e.getMessage());
           throw e;
       } catch (Exception e) {
           log.error(e.getMessage());
           throw new Exception(e);
       }

    }

    private void savePhotos(List<String> images, Client client) throws IOException {
        for (String image : images) {
            try {
                String pathImage = StorageUtils.storeImage(image, StorageEnum.CLIENT);
                Photo photo = new Photo();
                photo.setClient(client);
                photo.setPath(pathImage);
                photoRepository.save(photo);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    private void savePermissions(List<String> permissions, Client client) {
        permissions.forEach(permission -> {
            Permission permissionEntity = new Permission();
            permissionEntity.setAction(permission);
            permissionEntity.setClient(client);

            permissionRepository.save(permissionEntity);
        });
    }


}
