package com.api.cauth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacialRecognitionDTO {
    public ClientDTO client;
    @JsonProperty("facial_image")
    public String facialImage;
}
