package com.api.cauth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter
public class ClientDTO {
    public String name;
    public String email;
    @JsonProperty("facial_images")
    public List<String> facialImages;
    public List<String> permissions;
}
