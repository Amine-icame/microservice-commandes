package com.ms.microservice_commandes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import lombok.AllArgsConstructor; // Pour le constructeur avec tous les arguments
import lombok.Data;             // Pour getters, setters, toString, equals, hashCode
import lombok.NoArgsConstructor;  // Pour le constructeur par défaut

@Entity
@Table(name = "COMMANDE")
@Data // Génère getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur par défaut sans arguments
@AllArgsConstructor // Génère un constructeur avec tous les arguments (y compris idProduit)
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @Min(value = 1, message = "La quantité doit être au moins 1")
    private int quantite;

    @NotNull(message = "La date est obligatoire")
    private LocalDate date;

    @Min(value = 0, message = "Le montant ne peut pas être négatif")
    private double montant;

    private Long idProduit; // Le nouveau champ
}