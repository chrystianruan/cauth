package com.api.cauth.entities;

import com.api.cauth.dtos.ProductDTO;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String url;
    @Nullable
    private String accessKey;

    public ProductDTO toProductDTO() {
        return new ProductDTO(this.name, this.description, this.url);
    }
}
