package com.api.cauth.controllers;

import com.api.cauth.dtos.ProductDTO;
import com.api.cauth.exceptions.PermissaoException;
import com.api.cauth.services.ProductService;
import com.api.cauth.utils.CryptUtils;
import com.api.cauth.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping("/save-product")
    public ResponseEntity<Map<String, String>> save(@RequestBody ProductDTO product) {
        try {
            String accessKey = CryptUtils.generateNewToken();
            productService.save(product, accessKey);

            return new ResponseEntity<>(ResponseUtils.makeMessageWithToken("Product saved successfully", accessKey), HttpStatus.OK);

        } catch (PermissaoException e) {
            return new ResponseEntity<>(ResponseUtils.makeMessage(e.getMessage()), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseUtils.makeMessage("Internal error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
