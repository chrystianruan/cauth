package com.api.cauth.repositories;

import com.api.cauth.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    boolean existsByUrl(String url);
    Product findByAccessKey(String accessKey);
}
