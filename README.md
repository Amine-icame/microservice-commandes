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
<img width="1835" height="493" alt="Screenshot 2025-11-18 161603" src="https://github.com/user-attachments/assets/609c8d83-0490-4f29-9998-7f1c2bc1c802" />



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

<img width="1300" height="953" alt="Screenshot 2025-11-18 165133" src="https://github.com/user-attachments/assets/7e16b558-25e0-4063-8518-dea9c5d021bb" />


## 📊 Monitoring et Documentation

-   **Swagger UI :** `http://localhost:8081/swagger-ui.html`
   <img width="1818" height="1004" alt="Screenshot 2025-11-18 161844" src="https://github.com/user-attachments/assets/27be41ee-f4a4-467f-af9c-08a3af686e9f" />


-   **Actuator Endpoints :** `http://localhost:8081/actuator` (inclut `health`, `info`, `refresh`, `circuitbreakers`)

### Démonstration Resilience4j

Pour tester le Circuit Breaker :
1.  Assurez-vous que `microservice-produits` a son endpoint `/delay/{seconds}` fonctionnel.
2.  Accédez à `http://localhost:8081/actuator/circuitbreakers` pour voir l'état initial (`CLOSED`).
    <img width="1159" height="463" alt="Screenshot 2025-11-18 161951" src="https://github.com/user-attachments/assets/ec6992cf-f504-42d9-8032-cfca8949a29a" />


4.  Envoyez plusieurs requêtes `POST` à `http://localhost:8081/actuator/refresh` pour recharger les propriétés Resilience4j depuis Git.
5.  Envoyez plusieurs requêtes `GET http://localhost:8081/commandes/test-produit-delay/3` (le timeout est configuré à 2s). Vous devriez voir des réponses de fallback.
   <img width="1274" height="724" alt="Screenshot 2025-11-18 162129" src="https://github.com/user-attachments/assets/876af454-7817-4d59-83a2-3245c71f4870" />


7.  Vérifiez à nouveau `http://localhost:8081/actuator/circuitbreakers`. Le Circuit Breaker devrait passer à l'état `OPEN`.
    <img width="1276" height="878" alt="Screenshot 2025-11-18 162215" src="https://github.com/user-attachments/assets/fac43bb1-259c-4a69-92f4-c68bbb64c190" />


---

*Développé par Amine içame / Salma benOmar pour le module JEE.*
