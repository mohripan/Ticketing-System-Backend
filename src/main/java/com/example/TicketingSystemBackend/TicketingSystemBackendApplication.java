package com.example.TicketingSystemBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.TicketingSystemBackend.repository")
@EntityScan(basePackages = "com.example.TicketingSystemBackend.model")
public class TicketingSystemBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(TicketingSystemBackendApplication.class, args);
	}

}
