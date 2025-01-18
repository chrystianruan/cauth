package com.api.cauth.services;

import com.api.cauth.dtos.ClientDTO;
import com.api.cauth.entities.Client;
import com.api.cauth.entities.Permission;
import com.api.cauth.repositories.ClientRepository;
import com.api.cauth.repositories.PermissionRepository;
import com.api.cauth.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    public void save(String accessKey, ClientDTO clientDTO) throws Exception {
       try {
           if (clientRepository.existsByEmail(clientDTO.getEmail())) {
               throw new Exception("Email already exists");
           }
           Client client = new Client();
           client.setName(clientDTO.getName());
           client.setEmail(clientDTO.getEmail());
           client.setProduct(productRepository.findByAccessKey(accessKey));
           client.setFacialImage(clientDTO.getFacialImage().getBytes());
           clientRepository.save(client);
           clientDTO.getPermissions().forEach(permission -> {
               Permission permissionEntity = new Permission();
               permissionEntity.setAction(permission);
               permissionEntity.setClient(client);
               permissionRepository.save(permissionEntity);
           });


       } catch (Exception e) {
           throw new Exception(e);
       }

    }
}
