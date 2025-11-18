package com.ms.microservice_commandes.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope // Cette annotation est cruciale pour le rafraîchissement à chaud
public class ConfigTestController {

    // Injecte la valeur de la propriété 'mes-config-ms.commandes-last'
    @Value("${mes-config-ms.commandes-last}")
    private int commandesLastDays;

    @GetMapping("/commandes/config-info")
    public String getConfigInfo() {
        return "Nombre de jours pour les dernières commandes (mes-config-ms.commandes-last) : " + commandesLastDays;
    }
}