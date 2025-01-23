package com.api.cauth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientFoundDTO {
    private String name;
    private String email;
    private List<String> permissions;
    private ProductDTO product;


}
