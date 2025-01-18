package com.api.cauth.dtos;

import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String name;
    private String description;
    private String url;
}
