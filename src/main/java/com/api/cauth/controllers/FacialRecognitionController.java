package com.api.cauth.controllers;

import com.api.cauth.dtos.ClientFoundDTO;
import com.api.cauth.entities.Client;
import com.api.cauth.exceptions.PermissaoException;
import com.api.cauth.services.FacialRecognitionService;
import com.api.cauth.utils.ResponseUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/facial-recognition")
public class FacialRecognitionController {
    @Autowired
    private FacialRecognitionService facialRecognitionService;

    @PostMapping("")
    public ResponseEntity<Map<String, ?>> getFacialRecognition(@RequestBody String image) {
        try {
           ClientFoundDTO clientFound = facialRecognitionService.validateClientImage(image);

           if (clientFound == null) {
               return ResponseEntity.badRequest().body(ResponseUtils.makeMessage("Client not found"));
           }

           return ResponseEntity.ok(ResponseUtils.makeMessageWithObject(clientFound));
        }
        catch (PermissaoException exception) {
            return ResponseEntity.badRequest().body(ResponseUtils.makeMessage(exception.getMessage()));
        }
        catch (IOException exception) {
            return ResponseEntity.internalServerError().body(ResponseUtils.makeMessage("Error reading image"));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseUtils.makeMessage("Internal server error"));
        }
    }


}