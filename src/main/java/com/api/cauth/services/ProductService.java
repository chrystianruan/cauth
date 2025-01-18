package com.api.cauth.services;

import com.api.cauth.dtos.ProductDTO;
import com.api.cauth.entities.Product;
import com.api.cauth.exceptions.PermissaoException;
import com.api.cauth.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public void save(ProductDTO product, String accessKey) throws Exception {
        try {
            if (productRepository.existsByUrl(product.getUrl()) || productRepository.existsByName(product.getName())) {
                throw new PermissaoException("Product already exists");
            }
            Product productEntity = new Product();
            productEntity.setName(product.getName());
            productEntity.setDescription(product.getDescription());
            productEntity.setUrl(product.getUrl());
            productEntity.setAccessKey(accessKey);
            productRepository.save(productEntity);

        } catch (PermissaoException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}
