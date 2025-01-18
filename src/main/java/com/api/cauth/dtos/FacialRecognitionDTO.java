package com.api.cauth.dtos;

import com.api.cauth.entities.Client;

import java.util.List;

public class FacialRecognitionDTO {
    public ClientDTO client;
    public List<String> permissions;
}
