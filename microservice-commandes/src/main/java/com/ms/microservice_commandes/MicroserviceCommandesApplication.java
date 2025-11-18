package com.ms.microservice_commandes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // Pour Eureka Client
import org.springframework.cloud.client.loadbalancer.LoadBalanced; // Pour le load balancing
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
@EnableDiscoveryClient // Active le client Eureka pour l'enregistrement et la découverte
public class MicroserviceCommandesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceCommandesApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder builder) { // Injectez RestTemplateBuilder
		return builder
				.setConnectTimeout(Duration.ofSeconds(1)) // Timeout pour établir la connexion (1 seconde)
				.setReadTimeout(Duration.ofSeconds(2))    // Timeout pour la lecture de la réponse (2 secondes)
				.build();
	}
}