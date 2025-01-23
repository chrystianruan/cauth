package com.api.cauth.repositories;

import com.api.cauth.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("select c from Client c where c.id = ?1")
    Client findOne(Long id);
    boolean existsByEmail(String email);
}
