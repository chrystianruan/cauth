package com.api.cauth.controllers;

import com.api.cauth.dtos.ClientDTO;
import com.api.cauth.exceptions.PermissaoException;
import com.api.cauth.services.ClientService;
import com.api.cauth.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    @Autowired
    private ClientService clientService;
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> addClient(@RequestHeader String accessKey, @RequestBody ClientDTO clientDTO) {
       try {
           clientService.save(accessKey, clientDTO);
           return new ResponseEntity<>(ResponseUtils.makeMessage("Client save successfully"), HttpStatus.CREATED);
       } catch (PermissaoException e) {
           return new ResponseEntity<>(ResponseUtils.makeMessage(e.getMessage()), HttpStatus.FORBIDDEN);
       }
       catch (Exception e) {
           return new ResponseEntity<>(ResponseUtils.makeMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
}
