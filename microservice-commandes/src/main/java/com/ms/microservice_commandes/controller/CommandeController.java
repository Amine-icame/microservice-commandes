package com.ms.microservice_commandes.controller;

import com.ms.microservice_commandes.model.Commande;
// import com.ms.microservice_commandes.repository.CommandeRepository; // <-- N'EST PLUS NÉCESSAIRE D'INJECTER DIRECTEMENT ICI
import com.ms.microservice_commandes.service.CommandeService;
// import jakarta.validation.Valid; // Si vous n'utilisez pas @Valid sur les paramètres, cette importation est inutile
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/commandes")
@Tag(name = "Commandes", description = "API pour la gestion des commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService; // C'est le service que nous devons utiliser

    @Operation(summary = "Récupère toutes les commandes") // Ajoute un résumé pour la méthode
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des commandes récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeService.getAllCommandes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        Optional<Commande> commande = commandeService.getCommandeById(id);
        return commande.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Commande> createCommande(@RequestBody Commande commande) {
        if (commande.getDate() == null) {
            commande.setDate(LocalDate.now());
        }
        // Utilisation du service pour sauvegarder la commande
        Commande savedCommande = commandeService.saveCommande(commande); // Assurez-vous que CommandeService a une méthode saveCommande
        return new ResponseEntity<>(savedCommande, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Commande> updateCommande(@PathVariable Long id, @RequestBody Commande commandeDetails) {
        // Utilisation du service pour mettre à jour la commande
        Optional<Commande> updatedCommande = commandeService.updateCommande(id, commandeDetails); // Assurez-vous que CommandeService a une méthode updateCommande
        return updatedCommande.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        boolean deleted = commandeService.deleteCommande(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/config")
    public String getConfig() {
        return "Configuration actuelle: afficher les commandes des " +
                commandeService.getCommandesLastConfig() + " derniers jours";
    }

    // Nouveau endpoint pour tester Resilience4j
    @GetMapping("/produit-info/{idProduit}")
    public String getProduitInfoFromService(@PathVariable Long idProduit) {
        return commandeService.getProduitInfo(idProduit);
    }

    // Nouveau endpoint pour tester le délai et le Circuit Breaker
    @GetMapping("/test-produit-delay/{seconds}")
    public String testProduitDelay(@PathVariable int seconds) {
        return commandeService.getProduitDelay(seconds);
    }
}