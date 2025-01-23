package com.api.cauth.entities;

import com.api.cauth.dtos.ClientFoundDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "clients")
@Getter @Setter
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @OneToMany(mappedBy = "client")
    private List<Permission> permissions;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @OneToMany(mappedBy = "client")
    private List<Photo> photos;


    public ClientFoundDTO toClientFoundDTO() {
        return new ClientFoundDTO(this.name, this.email, this.permissions.stream().map(Permission::getAction).toList(), this.product.toProductDTO());
    }

}
