package com.ms.microservice_commandes.service;

import com.ms.microservice_commandes.model.Commande;
import com.ms.microservice_commandes.repository.CommandeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service // Indique que c'est un composant de service Spring
@RefreshScope // Pour le rafraîchissement à chaud de la configuration
public class CommandeService {

    private static final Logger log = LoggerFactory.getLogger(CommandeService.class);
    private static final String PRODUIT_SERVICE_NAME = "produitService"; // Nom du Circuit Breaker

    @Autowired
    private RestTemplate restTemplate; // Nous allons créer un bean RestTemplate

    @Autowired
    private CommandeRepository commandeRepository;

    @Value("${mes-config-ms.commandes-last}")
    private int commandesLastDays;

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Optional<Commande> getCommandeById(Long id) {
        return commandeRepository.findById(id);
    }

    public Commande saveCommande(Commande commande) {
        return commandeRepository.save(commande);
    }

    public Optional<Commande> updateCommande(Long id, Commande commandeDetails) {
        Optional<Commande> optionalCommande = commandeRepository.findById(id);
        if (optionalCommande.isPresent()) {
            Commande existingCommande = optionalCommande.get();
            existingCommande.setDescription(commandeDetails.getDescription());
            existingCommande.setQuantite(commandeDetails.getQuantite());
            existingCommande.setDate(commandeDetails.getDate());
            existingCommande.setMontant(commandeDetails.getMontant());
            existingCommande.setIdProduit(commandeDetails.getIdProduit());
            return Optional.of(commandeRepository.save(existingCommande));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteCommande(Long id) {
        if (commandeRepository.existsById(id)) {
            commandeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public String getCommandesLastConfig() {
        return String.valueOf(commandesLastDays);
    }

    // Une méthode de service potentielle pour l'exercice c. (si vous voulez filtrer les commandes)
    public List<Commande> getRecentCommandes() {
        LocalDate startDate = LocalDate.now().minusDays(commandesLastDays);
        return commandeRepository.findCommandesFromLastDays(startDate);
    }

    // Méthode pour appeler le microservice-produits avec résilience
    @CircuitBreaker(name = PRODUIT_SERVICE_NAME, fallbackMethod = "getProduitFallback")
    public String getProduitInfo(Long idProduit) {
        log.info("Appel du microservice-produits pour l'ID : {}", idProduit);
        // Utilise le nom du service enregistré dans Eureka pour l'appel.
        // Spring Cloud LoadBalancer (intégré à RestTemplate grâce à @LoadBalanced)
        // va trouver l'instance de MICROSERVICE-PRODUITS
        String url = "http://MICROSERVICE-PRODUITS/produits/" + idProduit;
        return restTemplate.getForObject(url, String.class);
    }

    // Méthode pour appeler l'endpoint de délai pour les tests
    @CircuitBreaker(name = PRODUIT_SERVICE_NAME, fallbackMethod = "getProduitDelayFallback")
    public String getProduitDelay(int seconds) {
        log.info("Appel du microservice-produits pour le délai : {} secondes", seconds);
        String url = "http://MICROSERVICE-PRODUITS/produits/delay/" + seconds;
        return restTemplate.getForObject(url, String.class);
    }


    // Méthode de fallback pour getProduitInfo en cas de défaillance
    public String getProduitFallback(Long idProduit, Throwable t) {
        log.warn("Fallback activé pour getProduitInfo, ID : {}. Cause : {}", idProduit, t.getMessage());
        return "Information sur le produit ID " + idProduit + " non disponible pour le moment (fallback).";
    }

    // Méthode de fallback pour getProduitDelay en cas de défaillance
    public String getProduitDelayFallback(int seconds, Throwable t) {
        log.warn("Fallback activé pour getProduitDelay, durée : {}s. Cause : {}", seconds, t.getMessage());
        return "Le service produit est actuellement indisponible (timeout/fallback après " + seconds + "s de tentative).";
    }
}