# Microservice Commandes

Ce microservice est responsable de la gestion des opérations CRUD (Create, Read, Update, Delete) sur les commandes. Il interagit avec le `microservice-produits` pour obtenir des informations sur les produits et implémente des mécanismes de résilience.

## 🚀 Technologies Utilisées

-   **Spring Boot 3+**
-   **Spring Web** (pour les API REST)
-   **Spring Data JPA** (pour la persistance des données)
-   **H2 Database** (base de données en mémoire pour le développement)
-   **Lombok** (pour réduire le code boilerplate)
-   **Spring Cloud Config Client** (pour la configuration centralisée)
-   **Spring Cloud Eureka Client** (pour l'enregistrement et la découverte)
-   **Spring Boot Actuator** (pour le monitoring et le rafraîchissement à chaud)
-   **Resilience4j** (pour le Circuit Breaker et la gestion des pannes)
-   **Springdoc-openapi / Swagger UI** (pour la documentation API)
-   **Maven**
-   **Java 17+**

## ⚙️ Comment le Lancer ?

1.  **Prérequis :**
    -   `spring-cloud-config-server` doit être lancé (sur `http://localhost:8888`).
    -   `spring-cloud-eureka-server` doit être lancé (sur `http://localhost:8761`).
    -   `microservice-produits` doit être lancé (sur `http://localhost:8082` ou autre).

2.  **Lancement :**
    ```bash
    mvn spring-boot:run
    ```
    Le microservice sera accessible sur `http://localhost:8081` (port configuré via le Config Server).
## H2 Database
<img width="1835" height="493" alt="image" src="https://github.com/user-attachments/assets/96248b75-bdba-420a-b93d-b646270ea7db" />


## 💡 Configuration

Ce microservice récupère sa configuration depuis le `spring-cloud-config-server` via le fichier `microservice-commandes.properties` situé dans le dépôt `microservices-config-repo`.

## 🧪 Endpoints API (via la Gateway ou directement)

Le préfixe pour toutes les routes est `/commandes`.
**Adresse directe :** `http://localhost:8081/commandes/...`
**Adresse via Gateway :** `http://localhost:8080/commandes/...`

| Méthode | Endpoint                                 | Description                                                                 | Corps de requête (JSON)                                       |
| :------ | :--------------------------------------- | :-------------------------------------------------------------------------- | :------------------------------------------------------------ |
| `GET`   | `/commandes`                             | Récupère toutes les commandes.                                              | `N/A`                                                         |
| `GET`   | `/commandes/{id}`                        | Récupère une commande par son ID.                                           | `N/A`                                                         |
| `POST`  | `/commandes`                             | Crée une nouvelle commande.                                                 | `{ "description": "...", "quantite": ..., "date": "AAAA-MM-JJ", "montant": ..., "idProduit": ... }` |
| `PUT`   | `/commandes/{id}`                        | Met à jour une commande existante.                                          | `{ "description": "...", "quantite": ..., "date": "AAAA-MM-JJ", "montant": ..., "idProduit": ... }` |
| `DELETE`| `/commandes/{id}`                        | Supprime une commande par son ID.                                           | `N/A`                                                         |
| `GET`   | `/commandes/produit-info/{idProduit}`  | Récupère des informations sur un produit via `microservice-produits` (avec Circuit Breaker). | `N/A`                                                         |
| `GET`   | `/commandes/test-produit-delay/{seconds}` | Teste le Circuit Breaker de Resilience4j en appelant un endpoint lent de `microservice-produits`. | `N/A`                                                         |
| `GET`   | `/commandes/config-info`                 | Affiche la valeur de la propriété `mes-config-ms.commandes-last`.            | `N/A`                                                         |

<img width="1300" height="953" alt="image" src="https://github.com/user-attachments/assets/e65d6c3a-935f-40e4-8eaa-191e219a1f12" />

## 📊 Monitoring et Documentation

-   **Swagger UI :** `http://localhost:8081/swagger-ui.html`
-   <img width="1818" height="1004" alt="image" src="https://github.com/user-attachments/assets/abaf3ead-6ad6-4ddd-9719-70df3f3a5111" />

-   **Actuator Endpoints :** `http://localhost:8081/actuator` (inclut `health`, `info`, `refresh`, `circuitbreakers`)

### Démonstration Resilience4j

Pour tester le Circuit Breaker :
1.  Assurez-vous que `microservice-produits` a son endpoint `/delay/{seconds}` fonctionnel.
2.  Accédez à `http://localhost:8081/actuator/circuitbreakers` pour voir l'état initial (`CLOSED`).
    <img width="1159" height="463" alt="image" src="https://github.com/user-attachments/assets/ce9f3c48-e5ec-4841-978f-fc7e0ced5533" />

4.  Envoyez plusieurs requêtes `POST` à `http://localhost:8081/actuator/refresh` pour recharger les propriétés Resilience4j depuis Git.
5.  Envoyez plusieurs requêtes `GET http://localhost:8081/commandes/test-produit-delay/3` (le timeout est configuré à 2s). Vous devriez voir des réponses de fallback.
   <img width="1274" height="724" alt="image" src="https://github.com/user-attachments/assets/9306f9ec-057a-4496-9ab1-3f40659320bd" />

7.  Vérifiez à nouveau `http://localhost:8081/actuator/circuitbreakers`. Le Circuit Breaker devrait passer à l'état `OPEN`.
    <img width="1276" height="878" alt="image" src="https://github.com/user-attachments/assets/8ea491b5-578b-47e8-b4c7-2df7be66141d" />

---

*Développé par Amine içame / Salma benOmar pour le module JEE.*
