package com.ms.microservice_commandes.repository;



import com.ms.microservice_commandes.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    @Query("SELECT c FROM Commande c WHERE c.date >= :startDate")
    List<Commande> findCommandesFromLastDays(@Param("startDate") LocalDate startDate);

    long count();
}