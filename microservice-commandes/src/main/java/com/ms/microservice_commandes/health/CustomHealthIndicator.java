package com.ms.microservice_commandes.health;

import com.ms.microservice_commandes.repository.CommandeRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component // Enregistre cette classe comme un bean Spring
public class CustomHealthIndicator implements HealthIndicator {

    private final CommandeRepository commandeRepository;

    // Injection du CommandeRepository via le constructeur
    public CustomHealthIndicator(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    @Override
    public Health health() {
        try {
            // Vérifier si des commandes existent dans la base de données
            long numberOfCommands = commandeRepository.count();

            if (numberOfCommands > 0) {
                // Si des commandes existent, le service est considéré comme "UP"
                return Health.up()
                        .withDetail("message", "Le microservice-commandes est en bonne santé. Nombre de commandes: " + numberOfCommands)
                        .build();
            } else {
                // Si aucune commande n'existe, le service est considéré comme "DOWN"
                return Health.down()
                        .withDetail("message", "Le microservice-commandes est en DOWN : aucune commande trouvée dans la base de données.")
                        .build();
            }
        } catch (Exception e) {
            // En cas d'exception (ex: problème de connexion à la base de données)
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withDetail("message", "Le microservice-commandes est en DOWN : Erreur lors de la vérification de la base de données.")
                    .build();
        }
    }
}